package neptune.commands.GeneralCommands;

import neptune.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class About implements ICommand {
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(
                event.getJDA().getSelfUser().getName(),
                event.getJDA().getSelfUser().getAvatarUrl());
        embedBuilder.addField("Bot Author", "Code-L#1417", true);
        embedBuilder.addField("Dev Server", "https://discord.gg/A9M4dvm", true);
        embedBuilder.addField("Built Using", "Discord JDA Library", true);
        embedBuilder.addField(
                "Invite link",
                "https://discordapp.com/api/oauth2/authorize?client_id=545565550768816138&permissions=37087296&scope=bot",
                false);
        embedBuilder.addField("Github", "https://github.com/Codel1417/Neptune-Discord-Bot", false);
        embedBuilder.setColor(Color.MAGENTA);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
