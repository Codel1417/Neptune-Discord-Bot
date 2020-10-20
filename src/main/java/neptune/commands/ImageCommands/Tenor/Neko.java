package neptune.commands.ImageCommands.Tenor;

import neptune.commands.CommandInterface;
import neptune.commands.TenorGif;
import neptune.commands.commandCategories;
import neptune.storage.Guild.guildObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Neko extends TenorGif implements CommandInterface {
    @Override
    public String getName() {
        return "Neko";
    }

    @Override
    public String getCommand() {
        return "neko";
    }

    @Override
    public String getDescription() {
        return "Images of Nyas";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Image;
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
        return false;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public guildObject run(
            GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {
        EmbedBuilder embedBuilder = getImageEmbed(event, "neko anime", true, "Have some nekos");
        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return guildEntity;
    }
}
