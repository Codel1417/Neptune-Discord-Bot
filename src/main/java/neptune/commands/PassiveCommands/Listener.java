package neptune.commands.PassiveCommands;

import co.elastic.apm.api.CaptureSpan;
import io.sentry.ITransaction;
import io.sentry.SpanStatus;
import io.sentry.protocol.User;
import neptune.commands.CommandHandler;
import neptune.commands.RandomMediaPicker;
import neptune.serverLogging.GuildLogging;
import neptune.storage.Enum.GuildOptionsEnum;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import neptune.storage.logsStorageHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;
import org.hibernate.Session;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import javax.annotation.Nonnull;

// intercepts discord messages
public class Listener implements EventListener {
    protected static final Logger log = LogManager.getLogger();
    private final GuildLogging guildLogging = new GuildLogging();
    final logsStorageHandler logStorage = new logsStorageHandler();
    private Runnable CycleActivity;
    private boolean ActivityThread;
    private final CommandHandler nepCommands = new CommandHandler();
    private final RandomMediaPicker randomMediaPicker = new RandomMediaPicker();

    @CaptureSpan(value = "GenericEvent", type = "command")
    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        Sentry.addBreadcrumb(event.getClass().getName());
        ITransaction transaction = Sentry.startTransaction(event.getClass().getName(), "Command Runner");
        if (event instanceof GenericGuildEvent) {
            guildObject guildEntity;
            try {
                guildEntity = GuildStorageHandler.getInstance().readFile(((GenericGuildEvent) event).getGuild().getId());
            } catch (Exception e) {
                log.error(e);
                Sentry.captureException(e);
                transaction.setThrowable(e);
                transaction.setStatus(SpanStatus.UNKNOWN_ERROR);
                transaction.finish();
                return;
            }
            // Commands
            if (event instanceof GuildMessageReceivedEvent) {
                if (((GuildMessageReceivedEvent) event).getAuthor().isBot()){
                    transaction.setStatus(SpanStatus.FAILED_PRECONDITION);
                    transaction.finish();
                    return;
                }
                runEvent((GuildMessageReceivedEvent) event, guildEntity);
            }
            //TODO: Move to scheduler for async
            // Clear stored logs when text channel is deleted
            if (event instanceof GuildLeaveEvent) {
                logStorage.deleteGuild(((GuildLeaveEvent) event).getGuild().getId());
            }
            // disconnect from voice chat if neptune is the only one left
            else if (event instanceof GuildVoiceUpdateEvent) {
                try {
                    if (Objects.requireNonNull(((GuildVoiceUpdateEvent) event).getChannelLeft()).getMembers().size() == 1 && Objects.requireNonNull(((GuildVoiceUpdateEvent) event).getChannelLeft()).getGuild().getAudioManager().isConnected()) {
                        Objects.requireNonNull(((GuildVoiceUpdateEvent) event).getChannelLeft()).getGuild().getAudioManager().setSendingHandler(null);
                        Objects.requireNonNull(((GuildVoiceUpdateEvent) event).getChannelLeft()).getGuild().getAudioManager().closeAudioConnection();
                        log.info("VOICE: Channel Empty, Disconnecting from VC");
                    }
                } catch (Exception e) {
                    log.error(e);
                    Sentry.captureException(e);
                }
            }
            guildEntity.closeSession();
            transaction.finish();
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
                if (Split[0].toLowerCase().contains(string.toLowerCase()) || Split[0].equalsIgnoreCase(string)) 
                        return true;
            }
        }
        return false;
    }

    public void runEvent(GuildMessageReceivedEvent event, guildObject guildEntity) {
        // check if the bot was called in chat
        try {
            boolean multiPrefix = guildEntity.getGuildOptions().getOption(GuildOptionsEnum.customSounds);
            if (isBotCalled(event.getMessage(), false)){
                nepCommands.run(event);
            }
            else if (isBotCalled(event.getMessage(), multiPrefix)) {
                randomMediaPicker.sendMedia(new File("Media" + File.separator + "Custom" + File.separator  + event.getMessage().getContentRaw().replace("=", "").replace("./", "")),event,true,true);
            }
            // return if bot was not called
        } catch (Exception e) {
            log.error(e);
            Sentry.captureException(e);
        }
    }
}
