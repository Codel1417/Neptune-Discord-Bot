package neptune.commands.PassiveCommands;

import neptune.storage.profileObject;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import neptune.storage.profileStorage;
import org.jetbrains.annotations.NotNull;


public class LeaderboardListener implements EventListener {

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildMessageReceivedEvent){
            profileStorage storage = profileStorage.getInstance();
            profileObject profile = storage.getProfile(((GuildMessageReceivedEvent) event).getMember().getId());
            profile.incrimentPoints();
            storage.serialize(profile);
        }
    } 
}
