package neptune.commands.PassiveCommands;

import neptune.CycleGameStatus;
import neptune.commands.CommandHandler;
import neptune.commands.RandomMediaPicker;
import neptune.serverLogging.GuildLogging;
import neptune.storage.Enum.GuildOptionsEnum;
import neptune.storage.Enum.LoggingOptionsEnum;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import neptune.storage.Guild.guildObject.logOptionsObject;
import neptune.storage.VariablesStorage;
import neptune.storage.logsStorageHandler;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.GenericVoiceChannelEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.api.events.guild.update.GenericGuildUpdateEvent;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.prometheus.client.Counter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.annotation.Nonnull;

// intercepts discord messages
public class Listener implements EventListener {
    protected static final Logger log = LogManager.getLogger();
    private final GuildLogging guildLogging = new GuildLogging();
    logsStorageHandler logStorage = new logsStorageHandler();
    private Runnable CycleActivity;
    private boolean ActivityThread;
    private final CommandHandler nepCommands = new CommandHandler();
    private final RandomMediaPicker randomMediaPicker = new RandomMediaPicker();
    static final Counter requests = Counter.build()
    .name("events_total").help("Total JDA Listener Events.").register();

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        requests.inc();
        // Startup tasks
        if (event instanceof ReadyEvent && !ActivityThread) {
            CycleActivity = new CycleGameStatus((ReadyEvent) event);
            Thread CycleActivityThread = new Thread(CycleActivity);
            CycleActivityThread.setName("CycleActivityThread");
            CycleActivityThread.start();
            ActivityThread = true; // prevent duplicate threads from discord reconnect
        }

        if (event instanceof GenericGuildEvent) {
            guildObject guildEntity = null;
            try {
                guildEntity =
                        GuildStorageHandler.getInstance().readFile(
                                ((GenericGuildEvent) event).getGuild().getId());
            } catch (Exception e) {
                log.error(e);
                return;
            }
            // Commands
            if (event instanceof GuildMessageReceivedEvent) {
                if (((GuildMessageReceivedEvent) event).getAuthor().isBot())
                    return; // blocks responses to other bots
                runEvent((GuildMessageReceivedEvent) event, guildEntity);
            }

            /*
             * This checks if a command is run to reduce duplicate writes
             */
            try {
                GuildStorageHandler.getInstance().writeFile(guildEntity);
            } catch (IOException e) {
                log.error(e);
            }

            // Clear stored logs when text channel is deleted
            if (event instanceof TextChannelDeleteEvent) {
                logStorage.deleteChannel(
                        ((TextChannelDeleteEvent) event).getGuild().getId(),
                        ((TextChannelDeleteEvent) event).getChannel().getId());
            }
            // detete all log data when server removes neptune
            else if (event instanceof GuildLeaveEvent) {
                logStorage.deleteGuild(((GuildLeaveEvent) event).getGuild().getId());
            }
            // disconnect from voice chat if neptune is the only one left
            else if (event instanceof GuildVoiceUpdateEvent) {
                try {
                    if (((GuildVoiceUpdateEvent) event).getChannelLeft().getMembers().size() == 1
                            && ((GuildVoiceUpdateEvent) event)
                                    .getChannelLeft()
                                    .getGuild()
                                    .getAudioManager()
                                    .isConnected()) {
                        ((GuildVoiceUpdateEvent) event)
                                .getChannelLeft()
                                .getGuild()
                                .getAudioManager()
                                .setSendingHandler(null);
                        ((GuildVoiceUpdateEvent) event)
                                .getChannelLeft()
                                .getGuild()
                                .getAudioManager()
                                .closeAudioConnection();
                        log.info("VOICE: Channel Empty, Disconnecting from VC");
                    }
                } catch (Exception ignored) {
                }
            }
            logOptionsObject logOptionsEntity = guildEntity.getLogOptions();

            // check if logging is set up first
            if (logOptionsEntity.getChannel() == null) return;
            if (!logOptionsEntity.getOption(LoggingOptionsEnum.GlobalLogging)) return;

            if (event instanceof GenericGuildVoiceEvent) {
                guildLogging.GuildVoice((GenericGuildVoiceEvent) event, logOptionsEntity);
            } else if (event instanceof GenericGuildMessageEvent) {
                guildLogging.GuildText((GenericGuildMessageEvent) event, logOptionsEntity);
            } else if (event instanceof GenericGuildMemberEvent) {
                guildLogging.GuildMember((GenericGuildMemberEvent) event, logOptionsEntity);
            } else if (event instanceof GenericGuildUpdateEvent) {
                guildLogging.GuildSettings((GenericGuildUpdateEvent) event, logOptionsEntity);
            } else if (event instanceof GenericTextChannelEvent) {
                guildLogging.GuildTextChannel((GenericTextChannelEvent) event, logOptionsEntity);
            } else if (event instanceof GenericVoiceChannelEvent) {
                guildLogging.GuildVoiceChannel((GenericVoiceChannelEvent) event, logOptionsEntity);
            }
        }
    }

    private boolean isBotCalled(Message message, boolean multiplePrefix) {
        // check for Normal Commands
        if (Arrays.asList(message.getContentRaw().split(" ")).get(0).equalsIgnoreCase("!nep"))
            return true;

        // additional hidden media features for private use
        if (multiplePrefix) {
            String[] Split = message.getContentRaw().split(" "); // splits the message into an array
            for (String string : new String[] {"!nep", "=", "./"}) {
                if (Split[0].toLowerCase().contains(string.toLowerCase())
                        || Split[0].equalsIgnoreCase(string)) return true;
            }
        }
        return false;
    }

    public boolean runEvent(GuildMessageReceivedEvent event, guildObject guildEntity) {
        // leaderboard
        guildEntity.getLeaderboard().incrimentPoint(event.getMember().getId());
        boolean result = false;
        // check if the bot was called in chat
        try {
            boolean multiPrefix =
                    guildEntity.getGuildOptions().getOption(GuildOptionsEnum.customSounds);
            if (isBotCalled(event.getMessage(), multiPrefix)) {
                result = nepCommands.run(event, guildEntity);
                // run command
                if (multiPrefix) {
                    VariablesStorage variablesStorage = new VariablesStorage();
                    randomMediaPicker.sendMedia(
                            new File(
                                    variablesStorage.getMediaFolder()
                                            + File.separator
                                            + "Custom"
                                            + File.separator
                                            + event.getMessage()
                                                    .getContentRaw()
                                                    .replace("=", "")
                                                    .replace("./", "")),
                            event,
                            true,
                            true);
                }
            }
            // return if bot was not called
        } catch (Exception e) {
            log.error(e);
        }
        return result;
    }
}
