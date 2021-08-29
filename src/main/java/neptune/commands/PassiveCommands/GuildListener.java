package neptune.commands.PassiveCommands;

import neptune.storage.logsStorageHandler;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class GuildListener implements EventListener {
    final logsStorageHandler logStorage = logsStorageHandler.getInstance();

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildLeaveEvent) {
            logStorage.deleteGuild(((GuildLeaveEvent) event).getGuild().getId());
        }
    }
}
