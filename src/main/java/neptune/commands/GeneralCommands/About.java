package neptune.commands.GeneralCommands;

import neptune.commands.ICommand;
import neptune.commands.ISlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

public class About implements ICommand, ISlashCommand {
    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
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
        return builder.setEmbeds(embedBuilder.build()).build();
    }

    @Override
    public CommandData RegisterCommand(CommandData commandData) {
        return commandData;
    }

    @Override
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
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
        return builder.setEmbeds(embedBuilder.build()).build();
    }
}
