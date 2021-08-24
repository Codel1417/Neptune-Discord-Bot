package neptune.commands.PassiveCommands;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import neptune.storage.profileStorage;
import org.jetbrains.annotations.NotNull;

public class LeaderboardListener implements EventListener {

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildMessageReceivedEvent){
            profileStorage profile = profileStorage.getProfile(((GuildMessageReceivedEvent) event).getMember().getId());
            profile.incrimentPoints();
            profile.serialize();
        }
    } 
}
