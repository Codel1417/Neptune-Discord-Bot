package neptune.commands.UtilityCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.Guild.guildObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class Screenshare implements CommandInterface {
    @Override
    public String getName() {
        return "Server Screenshare";
    }

    @Override
    public String getCommand() {
        return "screenshare";
    }

    @Override
    public String getDescription() {
        return "Generates a screenshare url for server voice channels";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.General;
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
        return true;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public guildObject run(
            GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {

        if (event.getMember().getVoiceState().inVoiceChannel()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.addField(
                    "Channel", event.getMember().getVoiceState().getChannel().getName(), true);
            embedBuilder.setTitle("Screenshare Link");
            embedBuilder.setColor(Color.MAGENTA);
            embedBuilder.addField(
                    "URL",
                    "<https://discordapp.com/channels/"
                            + event.getGuild().getId()
                            + "/"
                            + event.getMember().getVoiceState().getChannel().getId()
                            + ">",
                    false);
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
        return guildEntity;
    }
}
