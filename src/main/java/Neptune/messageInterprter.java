package Neptune;

import Neptune.Commands.NepCommands;
import Neptune.Commands.RandomMediaPicker;
import Neptune.Storage.*;

import java.util.logging.Logger;

import com.google.gson.internal.LinkedTreeMap;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.File;
import java.util.Arrays;

class messageInterprter {
    private final VariablesStorage VariableStorageRead;
    private final static Logger Log = Logger.getLogger(messageInterprter.class.getName());
    private StorageController storageController;
    private final NepCommands nepCommands;
    private final RandomMediaPicker randomMediaPicker = new RandomMediaPicker();

    messageInterprter(StorageController storageController, VariablesStorage variablesStorage) {
        this.storageController = storageController;
        nepCommands = new NepCommands(variablesStorage);
        VariableStorageRead = variablesStorage;

    }

    private boolean isBotCalled(Message message, boolean multiplePrefix){
        //check for Normal Commands
        if(Arrays.asList(message.getContentRaw().split(" ")).get(0).trim().equalsIgnoreCase(VariableStorageRead.getCallBot())) return true;

        //for future use
        if (multiplePrefix) {

            String[] prefix = new String[]{VariableStorageRead.getCallBot(),"=","./"}; //command prefix supported
            String[] Split = message.getContentRaw().split(" "); //splits the message into an array

            for (String string : prefix) {
                if (Split[0].toLowerCase().contains(string.toLowerCase()) || Split[0].equalsIgnoreCase(string)) return true;
            }
        }
        return false;
    }

    void runEvent(MessageReceivedEvent event) {
        //log everything to console when debugging
        if (VariableStorageRead.getDevMode()) printConsoleLog(true, event);

        //Checks if the guild is currently stored, if not creates an entry
        checkStoredGuild(event.getGuild());

        //check if the bot was called in chat
        try {
            LinkedTreeMap<String, String> test = (LinkedTreeMap<String, String>) storageController.getGuild(event.getGuild());
            String multiPrefix = test.getOrDefault("Custom-Sounds","false");
            if (isBotCalled(event.getMessage(), multiPrefix.equalsIgnoreCase("true"))) {
                //print the message log in the console if the message was a command
                if (!VariableStorageRead.getDevMode()) printConsoleLog(false, event);

                //run command
                if (!nepCommands.run(event, storageController, VariableStorageRead)) {
                    randomMediaPicker.sendMedia(new File(VariableStorageRead.getMediaFolder() + File.separator + "Custom" + File.separator +  event.getMessage().getContentRaw().replace("=","").replace("./","").trim()), event, true, true);
                }
            }
            else {
                nepCommands.run(event, storageController, VariableStorageRead);
            }
            //return if bot was not called
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkStoredGuild(net.dv8tion.jda.core.entities.Guild guildObject) {
        /*
        Checks the list to see if the current guild/server is stored, if not create a new guild entry.
         */
        if(guildObject != null) {
            storageController.addGuild(guildObject);
        }
    }
    private void printConsoleLog(Boolean debug, MessageReceivedEvent event){
        StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("New Message \n");
            if (event.getGuild() != null) {
                stringBuilder.append("    Guild Name: ").append(event.getGuild().getName()).append("\n");
                if (debug){
                    stringBuilder.append("    Guild ID: ").append(event.getGuild().getId()).append("\n");
                }
            }
        stringBuilder.append("    Channel Name: ").append(event.getChannel().getName()).append("\n");
            if (debug){
                stringBuilder.append("    Channel ID ").append(event.getChannel().getId()).append("\n");
            }
        stringBuilder.append("    Author Name: ").append(event.getAuthor().getName()).append("\n");
            if (debug) {
                stringBuilder.append("    Author ID: ").append(event.getAuthor().getId()).append("\n");
            }
        stringBuilder.append("    Message Contents: ").append(event.getMessage().getContentRaw()).append("\n");
        stringBuilder.append("    message: ").append(event.getMessage()).append("\n");
        Log.info(stringBuilder.toString());
    }
}
