package neptune.commands.UtilityCommands;

import me.dilley.MineStat;

import neptune.commands.ICommand;
import neptune.commands.ISlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

public class MinecraftServerStatus implements ICommand, ISlashCommand {

    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        String serverDomain;
        int port = 25565;
        String[] mcServer = messageContent.split(":");
        if (mcServer.length == 0) {
        }
        serverDomain = mcServer[0];

        if (mcServer.length > 1) {
            port = Integer.parseInt(mcServer[1]);
        }

        MineStat mineStat = new MineStat(serverDomain, port);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(mineStat.getAddress() + ":" + mineStat.getPort());
        if (mineStat.isServerUp()) {
            embedBuilder
                    .setColor(Color.GREEN)
                    .addField("MOTD", mineStat.getMotd(), false)
                    .addField(
                            "Players",
                            mineStat.getCurrentPlayers()
                                    + "/"
                                    + mineStat.getMaximumPlayers()
                                    + " Player(s) online",
                            true)
                    .addField("Latency", String.valueOf(mineStat.getLatency()), true)
                    .addField("Version", mineStat.getVersion(), true);
        } else {
            embedBuilder.setColor(Color.RED);
            embedBuilder.setDescription("Server offline or an incorrect server was entered");
        }
        return builder.setEmbeds(embedBuilder.build()).build();
    }

    @Override
    public CommandData RegisterCommand(CommandData commandData) {
        return commandData.addOption(OptionType.STRING,"Server Address","EX: 65.43.46.89:25565",true);
    }

    @Override
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        String serverDomain;
        int port = 25565;
        OptionMapping optionMapping = event.getOption("Server Address");
        String[] mcServer = optionMapping.getAsString().split(":");
        if (mcServer.length == 0) {
        }
        serverDomain = mcServer[0];

        if (mcServer.length > 1) {
            port = Integer.parseInt(mcServer[1]);
        }

        MineStat mineStat = new MineStat(serverDomain, port);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(mineStat.getAddress() + ":" + mineStat.getPort());
        if (mineStat.isServerUp()) {
            embedBuilder
                    .setColor(Color.GREEN)
                    .addField("MOTD", mineStat.getMotd(), false)
                    .addField(
                            "Players",
                            mineStat.getCurrentPlayers()
                                    + "/"
                                    + mineStat.getMaximumPlayers()
                                    + " Player(s) online",
                            true)
                    .addField("Latency", String.valueOf(mineStat.getLatency()), true)
                    .addField("Version", mineStat.getVersion(), true);
        } else {
            embedBuilder.setColor(Color.RED);
            embedBuilder.setDescription("Server offline or an incorrect server was entered");
        }
        return builder.setEmbeds(embedBuilder.build()).build();
    }
}
