package neptune.commands.PassiveCommands;

import neptune.storage.dao.GuildDao;
import neptune.storage.entity.GuildEntity;
import neptune.storage.entity.ProfileEntity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class LeaderboardListener implements EventListener {

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildMessageReceivedEvent){
            GuildDao guildDao = new GuildDao();
            GuildEntity guildentity = guildDao.getGuild(((GuildMessageReceivedEvent) event).getMember().getId());
            List<ProfileEntity> profileEntities = guildentity.getProfile();
            ProfileEntity profile = profileEntities.stream().filter(x -> x.getId().equals(((GuildMessageReceivedEvent) event).getMember().getId())).findFirst().orElse(new ProfileEntity(((GuildMessageReceivedEvent) event).getMember().getId()));
            if (profileEntities.stream().noneMatch(x -> x.getId().equals(((GuildMessageReceivedEvent) event).getMember().getId()))) {
                profileEntities.add(profile);
            }
            profile.incrimentPoints();
            guildDao.saveGuild(guildentity);
        }
    } 
}
