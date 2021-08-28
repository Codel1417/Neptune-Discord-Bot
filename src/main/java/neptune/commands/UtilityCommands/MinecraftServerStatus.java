package neptune.commands.UtilityCommands;

import me.dilley.MineStat;

import neptune.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class MinecraftServerStatus implements ICommand {

    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
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

        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
