package Neptune.Commands.AdminCommands;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.CommonMethods;
import Neptune.Commands.commandCategories;
import Neptune.Storage.StorageController;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AdminOptions extends CommonMethods implements CommandInterface {

    @Override
    public String getName() {
        return "Admin Menu";
    }

    @Override
    public String getCommand() {
        return "admin";
    }

    @Override
    public String getDescription() {
        return "Shows server admin options menu";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Help;
    }

    @Override
    public String getHelp() {
        return "";
    }

    @Override
    public boolean getRequireManageServer() {
        return true;
    }

    @Override
    public boolean getRequireOwner() {
        return false;
    }

    @Override
    public boolean run(MessageReceivedEvent event, StorageController storageController, VariablesStorage variablesStorage, String messageContent) {
        String[] CommandArray = getCommandName(getCommandName(event.getMessage().getContentRaw())[1]);

        System.out.println(CommandArray[0] + " | " + CommandArray[1]);
        if (CommandArray[0].trim().equalsIgnoreCase("custom_sounds")) {
            if(CommandArray[1].trim().equalsIgnoreCase("enabled")){
                storageController.updateGuildField(event.getGuild(),"Custom-Sounds","true");
                event.getChannel().sendMessage("Enabled Custom Sounds").queue();
            }
            else{
                event.getChannel().sendMessage("Disabled Custom_Sounds").queue();
                storageController.updateGuildField(event.getGuild(),"Custom-Sounds","false");
            }
        }
        else if (CommandArray[0].trim().equalsIgnoreCase("tts")) {
                if(CommandArray[1].trim().equalsIgnoreCase("enabled")){
                    storageController.updateGuildField(event.getGuild(),"TTS_Enabled","true");
                    event.getChannel().sendMessage("Enabled TTS").queue();
                }
                else {
                    event.getChannel().sendMessage("Disabled TTS").queue();
                    storageController.updateGuildField(event.getGuild(),"TTS_Enabled","false");
                }
        }
        else {
            event.getChannel().sendMessage("Admin Options \n" +
                    "TTS : Enables/Disables tts | " + "CommandInterface: (Prefix) Admin TTS (enabled/disabled)" ).queue();
        }
        return true;
    }


}

