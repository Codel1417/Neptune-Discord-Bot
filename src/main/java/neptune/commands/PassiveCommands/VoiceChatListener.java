package neptune.commands.PassiveCommands;

import io.sentry.Sentry;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class VoiceChatListener implements EventListener {
    protected static final Logger log = LogManager.getLogger();

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (event instanceof GuildVoiceLeaveEvent) {
            try {
                if (((GuildVoiceLeaveEvent) event).getChannelLeft().getMembers().size() == 1 && ((GuildVoiceLeaveEvent) event).getChannelLeft().getGuild().getAudioManager().isConnected()) {
                    ((GuildVoiceLeaveEvent) event).getChannelLeft().getGuild().getAudioManager().setSendingHandler(null);
                    ((GuildVoiceLeaveEvent) event).getChannelLeft().getGuild().getAudioManager().closeAudioConnection();
                    log.info("VOICE: Channel Empty, Disconnecting from VC");
                }
            } catch (Exception e) {
                log.error(e);
                Sentry.captureException(e);
            }
        }
    }
}
