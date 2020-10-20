package neptune.commands.UtilityCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.Guild.guildObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.Color;
import java.time.Instant;

public class unixTime implements CommandInterface {

    @Override
    public String getName() {
        return "Time";
    }

    @Override
    public String getCommand() {
        return "time";
    }

    @Override
    public String getDescription() {
        return "Gives the time";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Utility;
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public boolean getRequireManageServer() {
        return false;
    }

    @Override
    public boolean getHideCommand() {
        return false;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public guildObject run(
            GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {
        Instant instant = Instant.now();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(getName());
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.addField("Unix Time", String.valueOf(instant.getEpochSecond()), true);
        embedBuilder.addField("UTC Time", instant.toString(), true);

        event.getChannel().sendMessage(embedBuilder.build()).queue();

        return guildEntity;
    }
}
