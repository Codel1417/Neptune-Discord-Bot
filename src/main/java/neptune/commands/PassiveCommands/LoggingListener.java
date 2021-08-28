package neptune.commands.PassiveCommands;

import io.sentry.Sentry;
import neptune.serverLogging.GuildLogging;
import neptune.storage.Enum.LoggingOptionsEnum;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.api.events.channel.voice.GenericVoiceChannelEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.api.events.guild.update.GenericGuildUpdateEvent;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

public class LoggingListener implements EventListener {
    protected static final Logger log = LogManager.getLogger();
    private final GuildLogging guildLogging = new GuildLogging();
    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GenericGuildEvent){
            guildObject guildEntity;
            try {
                guildEntity = GuildStorageHandler.getInstance().readFile(((GenericGuildEvent) event).getGuild().getId());
            } catch (Exception e) {
                log.error(e);
                Sentry.captureException(e);
                return;
            }
            guildObject.logOptionsObject logOptionsEntity = guildEntity.getLogOptions();

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
            }
            guildEntity.closeSession();
        }

    }
    
}
