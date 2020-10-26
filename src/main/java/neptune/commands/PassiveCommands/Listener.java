package neptune.commands.PassiveCommands;

import neptune.CycleGameStatus;
import neptune.messageInterprter;
import neptune.serverLogging.GuildLogging;
import neptune.storage.Enum.LoggingOptionsEnum;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import neptune.storage.Guild.guildObject.logOptionsObject;
import neptune.storage.VariablesStorage;
import neptune.storage.logsStorageHandler;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.GenericVoiceChannelEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
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

import javax.annotation.Nonnull;

// intercepts discord messages
public class Listener implements EventListener {
    private final neptune.messageInterprter messageInterprter;
    protected static final Logger log = LogManager.getLogger();
    private final GuildLogging guildLogging = new GuildLogging();
    logsStorageHandler logStorage = new logsStorageHandler();
    private Runnable CycleActivity;
    private boolean ActivityThread;

    GuildStorageHandler guildStorageHandler = new GuildStorageHandler();

    public Listener() {
        messageInterprter = new messageInterprter();
    }

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        // Cycle Activity Status Thread
        if (event instanceof ReadyEvent && !ActivityThread) {
            CycleActivity = new CycleGameStatus((ReadyEvent) event);
            Thread CycleActivityThread = new Thread(CycleActivity);
            CycleActivityThread.setName("CycleActivityThread");
            CycleActivityThread.start();
            ActivityThread = true;
        }


        if (event instanceof GenericGuildEvent) {
            String GuildID = ((GenericGuildEvent) event).getGuild().getId();
            if (GuildID == null) return;
            guildObject guildEntity = null;
            try {
                guildEntity = guildStorageHandler.readFile(GuildID);
            } catch (Exception e) {
                log.error(e);
                return;
            }
        
            // Commands
            if (event instanceof GuildMessageReceivedEvent) {
                if (((GuildMessageReceivedEvent) event).getAuthor().isBot()) return;
                guildEntity = messageInterprter.runEvent((GuildMessageReceivedEvent) event,guildEntity);
            }
            // Clear stored logs when text channel is deleted
            if (event instanceof TextChannelDeleteEvent) {
                logStorage.deleteChannel(
                        ((TextChannelDeleteEvent) event).getGuild().getId(),
                        ((TextChannelDeleteEvent) event).getChannel().getId());
            }
            // detete all data when server removes neptune
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
            if (event instanceof GuildJoinEvent) {
                event.getJDA()
                        .getUserById(new VariablesStorage().getOwnerID())
                        .openPrivateChannel()
                        .queue(
                                (channel) ->
                                        channel.sendMessage(
                                                        "GUILD: New Server Added: "
                                                                + ((GuildJoinEvent) event)
                                                                        .getGuild()
                                                                        .getName())
                                                .queue());
            }

            logOptionsObject logOptionsEntity = guildEntity.getLogOptions();
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
}
