package neptune.commands.UtilityCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.VariablesStorage;
import me.dilley.MineStat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class MinecraftServerStatus implements CommandInterface {
    @Override
    public String getName() {
        return "Minecraft Server Status";
    }

    @Override
    public String getCommand() {
        return "mc";
    }

    @Override
    public String getDescription() {
        return "Gets the status of a Minecraft server. Supports IP and URL's. DynamicDNS domains are unsupported";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Utility;
    }

    @Override
    public String getHelp() {
        return "server.com:port";
    }

    @Override
    public boolean getRequireManageServer() {
        return false;
    }

    @Override
    public boolean getRequireOwner() {
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
    public boolean run(MessageReceivedEvent event, VariablesStorage variablesStorage, String messageContent) {
        String serverDomain;
        int port = 25565;
        String[] mcServer = messageContent.split(":");
        if(mcServer.length == 0){
            serverDomain = messageContent;
        }
        serverDomain = mcServer[0];

        if(mcServer.length > 1){
            port = Integer.parseInt(mcServer[1]);
        }

        MineStat mineStat = new MineStat(serverDomain,port);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(mineStat.getAddress() + ":" + mineStat.getPort());
        if(mineStat.isServerUp()){
            embedBuilder.setColor(Color.GREEN)
                    .addField("MOTD",mineStat.getMotd(),false)
                    .addField("Players",mineStat.getCurrentPlayers() + "/" + mineStat.getMaximumPlayers() + " Player(s) online",true)
                    .addField("Latency", String.valueOf(mineStat.getLatency()),true)
                    .addField("Version",mineStat.getVersion(),true);
        }
        else{
            embedBuilder.setColor(Color.RED);
            embedBuilder.setDescription("Server offline or an incorrect server was entered");
        }

        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return false;
    }
}
