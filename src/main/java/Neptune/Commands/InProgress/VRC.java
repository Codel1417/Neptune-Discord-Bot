package Neptune.Commands.InProgress;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.CommonMethods;
import Neptune.Commands.VRChatRequest;
import Neptune.Commands.commandCategories;
import Neptune.Storage.VariablesStorage;
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
    public boolean getRequireOwner() {
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
    public boolean run(MessageReceivedEvent event, VariablesStorage variablesStorage, String messageContent) {
        String[] command = getCommandName(messageContent);
        switch(command[0]){
            case "world":{
                getWorldByID(command[1],event,variablesStorage);
                break;
            }
            case "online":{
                getOnline(event);
                break;
            }
            default: displayMenu(event, variablesStorage);
        }
        return true;
    }

    private void getOnline(MessageReceivedEvent event){
        //API
        String result = vrChatRequest.getOnlineUsers();
        //Send Message
        EmbedBuilder embedBuilder = getEmbedBuilder();
        embedBuilder.addField("VRChat Users Currently Online", result + " users online", true);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
    private void getWorldByID(String search, MessageReceivedEvent event, VariablesStorage variablesStorage){
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
            if (!result.getOrDefault("assetUrl","").equalsIgnoreCase("")  && variablesStorage.getOwnerID().equalsIgnoreCase(event.getAuthor().getId())){
                embedBuilder.addField("assetUrl",result.get("assetUrl"),true);
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
    private void displayMenu(MessageReceivedEvent event, VariablesStorage variablesStorage){
        EmbedBuilder embedBuilder = getEmbedBuilder();
        embedBuilder.setTitle("VRChat Help");
        embedBuilder.setDescription("Interact with the VRChat API");
        embedBuilder.addField("Get Users Online",variablesStorage.getCallBot() +" " + getCommand() + " online",false);
        embedBuilder.addField("World ID Search",variablesStorage.getCallBot() +" " + getCommand() + " world <ID>",false);
        embedBuilder.addField("User ID Search",variablesStorage.getCallBot() +" " + getCommand() + " user <ID>",false);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
