package neptune.commands.PassiveCommands;

import neptune.storage.logsStorageHandler;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class GuildListener implements EventListener {
    final logsStorageHandler logStorage = new logsStorageHandler();

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        //TODO: Move to scheduler for async
        // Clear stored logs when text channel is deleted
        if (event instanceof GuildLeaveEvent) {
            logStorage.deleteGuild(((GuildLeaveEvent) event).getGuild().getId());
        }
    }
}
