package neptune.commands.PassiveCommands;

import io.sentry.Sentry;
import neptune.serverLogging.GuildLogging;
import neptune.storage.dao.GuildDao;
import neptune.storage.entity.GuildEntity;
import neptune.storage.entity.LogOptionsEntity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.api.events.guild.update.GenericGuildUpdateEvent;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class LoggingListener implements EventListener {
    protected static final Logger log = LogManager.getLogger();
    private final GuildLogging guildLogging = new GuildLogging();
    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GenericGuildEvent){
            GuildDao guildDao = new GuildDao();
            GuildEntity guildEntity;
            try {
                guildEntity = guildDao.getGuild(((GenericGuildEvent) event).getGuild().getId());
            } catch (Exception e) {
                log.error(e);
                Sentry.captureException(e);
                return;
            }
            LogOptionsEntity logOptionsEntity = guildEntity.getLogConfig();

            // check if logging is set up first
            if (logOptionsEntity.getChannel() == null) return;
            if (!logOptionsEntity.isGlobalLogging()) return;

            if (event instanceof GenericGuildVoiceEvent) {
                guildLogging.GuildVoice((GenericGuildVoiceEvent) event, logOptionsEntity);
            } else if (event instanceof GenericGuildMessageEvent) {
                guildLogging.GuildText((GenericGuildMessageEvent) event, logOptionsEntity);
            } else if (event instanceof GenericGuildMemberEvent) {
                guildLogging.GuildMember((GenericGuildMemberEvent) event, logOptionsEntity);
            } else if (event instanceof GenericGuildUpdateEvent) {
                guildLogging.GuildSettings((GenericGuildUpdateEvent) event, logOptionsEntity);
            }
        }

    }
    
}
