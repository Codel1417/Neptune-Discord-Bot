package neptune.commands.DevCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class GuildList implements CommandInterface {

    @Override
    public String getName() {
        return "Guild List";
    }

    @Override
    public String getCommand() {
        return "guildlist";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Dev;
    }

    @Override
    public String getHelp() {
        return "";
    }

    @Override
    public boolean getRequireManageServer() {
        return false;
    }

    @Override
    public boolean getHideCommand() {
        return true;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public guildObject run(
            GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {
        List<Guild> GuildList = event.getJDA().getShardManager().getGuilds();
        StringBuilder guilds = new StringBuilder();
        if (GuildList != null) {
            for (Guild guild : GuildList) {
                guilds.append(guild.getId()).append(": ").append(guild.getName());

                // to check if guild owners are dumb enough to give bots permissions that they do
                // not need.
                if (guild.getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
                    guilds.append("(ADMIN)");
                }
                guilds.append("\n");
            }
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Currently In Guilds");
        embedBuilder.setDescription(guilds.toString());
        embedBuilder.setColor(Color.MAGENTA);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return guildEntity;
    }
}
