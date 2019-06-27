package Neptune.Commands;

import Neptune.Storage.StorageControllerCached;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

class AdminOptions {
    void adminCommand(MessageReceivedEvent event, StorageControllerCached storageController, String[] CommandArray){
        System.out.println(CommandArray[0] + " | " + CommandArray[1]);
            if (CommandArray[0].trim().equalsIgnoreCase("custom_sounds")) {
                if(CommandArray[1].trim().equalsIgnoreCase("enabled")){
                    storageController.setCustomSoundsEnabled(event.getGuild(),true);
                    event.getChannel().sendMessage("Enabled Custom Sounds").queue();
                }
                else{
                    event.getChannel().sendMessage("Disabled Custom_Sounds").queue();
                    storageController.setCustomSoundsEnabled(event.getGuild(),false);
                }
            }
            else if (CommandArray[0].trim().equalsIgnoreCase("tts")) {
                if (event.getMember().isOwner() || (event.getMember().hasPermission(Permission.MANAGE_SERVER, Permission.MESSAGE_TTS) && event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_TTS))) {
                    if(CommandArray[1].trim().equalsIgnoreCase("enabled")){
                        storageController.setTtsEnabled(event.getGuild(),true);
                        event.getChannel().sendMessage("Enabled TTS").queue();
                    }
                    else {
                        event.getChannel().sendMessage("Disabled TTS").queue();
                        storageController.setTtsEnabled(event.getGuild(),false);
                    }
                }
                else {
                    event.getChannel().sendMessage("You lack the required permissions to change this!").queue();
                }
            }
            else {
                event.getChannel().sendMessage("Admin Options \n" +
                        "TTS : Enables/Disables tts | " + "Command: (Prefix) Admin TTS (enabled/disabled)" ).queue();
            }
    }
}

