package neptune.commands.InProgress;

import neptune.commands.CommandInterface;
import neptune.commands.CommonMethods;
import neptune.commands.VRChatRequest;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Map;

public class VRC extends CommonMethods implements CommandInterface {
    VRChatRequest vrChatRequest = new VRChatRequest();
    @Override
    public String getName() {
        return "VRChat";
    }

    @Override
    public String getCommand() {
        return "vrc";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Utility;
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
    public guildObject run(MessageReceivedEvent event, String messageContent, guildObject guildEntity) {
        String[] command = getCommandName(messageContent);
        switch(command[0]){
            case "world":{
                getWorldByID(command[1],event);
                break;
            }
            case "online":{
                getOnline(event);
                break;
            }
            default: displayMenu(event);
        }
        return guildEntity;
    }

    private void getOnline(MessageReceivedEvent event){
        //API
        String result = vrChatRequest.getOnlineUsers();
        //Send Message
        EmbedBuilder embedBuilder = getEmbedBuilder();
        embedBuilder.addField("VRChat Users Currently Online", result + " users online", true);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
    private void getWorldByID(String search, MessageReceivedEvent event){
        EmbedBuilder embedBuilder = getEmbedBuilder();

        //API
        Map<String,String> result = null;
        try {
            result = vrChatRequest.getWorldByID(search);
            if (!result.getOrDefault("name","").equalsIgnoreCase("")){
                embedBuilder.addField("Name",result.get("name"),true);
            }
            if (!result.getOrDefault("description","").equalsIgnoreCase("")){
                embedBuilder.addField("Description",result.get("description"),true);
            }
            if (!result.getOrDefault("authorName","").equalsIgnoreCase("")){
                embedBuilder.addField("Author",result.get("authorName"),true);
            }
            if (!result.getOrDefault("image","").equalsIgnoreCase("")){
                embedBuilder.setImage(result.get("image"));
            }
            if (!result.getOrDefault("capacity","").equalsIgnoreCase("")){
                embedBuilder.addField("Instance Capacity",result.get("capacity") + " users",true);
            }
            if (!result.getOrDefault("visits","").equalsIgnoreCase("")){
                embedBuilder.addField("Visits",result.get("visits") + " users",true);
            }
            if (!result.getOrDefault("occupants","").equalsIgnoreCase("")){
                embedBuilder.addField("Occupants",result.get("occupants") + " users",true);
            }
        }
        catch (NullPointerException e){
            embedBuilder.setDescription("World ID does not exist or threw an error");
        }

        //Send Message
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
    private EmbedBuilder getEmbedBuilder(){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("VRChat");
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setFooter("Powered by VRChat", "https://vrchat.com/public/media/logo.png");
        return embedBuilder;
    }
    private void displayMenu(MessageReceivedEvent event){
        EmbedBuilder embedBuilder = getEmbedBuilder();
        embedBuilder.setTitle("VRChat Help");
        embedBuilder.setDescription("Interact with the VRChat API");
        embedBuilder.addField("Get Users Online","!nep " + getCommand() + " online",false);
        embedBuilder.addField("World ID Search","!nep " + getCommand() + " world <ID>",false);
        embedBuilder.addField("User ID Search","!nep " + getCommand() + " user <ID>",false);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
