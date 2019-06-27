package Neptune;

import Neptune.Commands.NepCommands;
import Neptune.Commands.RandomSoundPicker;
import Neptune.Storage.*;
import java.util.logging.Logger;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

class messageInterprter {
    private final VariablesStorage VariableStorageRead;
    private final static Logger Log = Logger.getLogger(messageInterprter.class.getName());
    private StorageControllerCached storageController;
    private final NepCommands nepCommands;
    private volatile  HashMap<String, Long> rateLimitMap = new HashMap<>();
    private FolderSearch folderSearch;
    private RandomSoundPicker randomSoundPicker = new RandomSoundPicker();


    messageInterprter(StorageControllerCached storageController, VariablesStorage variablesStorage) {
        this.storageController = storageController;
        nepCommands = new NepCommands(storageController, variablesStorage);
        VariableStorageRead = variablesStorage;
        folderSearch = new FolderSearch(VariableStorageRead.getCustomSoundsFolder());
    }

    private boolean isRateLimited(User user) {
        //Checks if the user sent a command too quickly
        if (VariableStorageRead.getMessageCooldownSeconds() > 0) {
            if(rateLimitMap.containsKey(user.getId())) {
                if (System.currentTimeMillis() - rateLimitMap.get(user.getId()) < VariableStorageRead.getMessageCooldownSeconds() * 1000) {
                    Log.info("Limit Reached!: " + user);
                    rateLimitMap.replace(user.getId(), System.currentTimeMillis());
                    return true;
                } else {
                    rateLimitMap.replace(user.getId(), System.currentTimeMillis());
                    return false;
                }
            }
            else {
                rateLimitMap.put(user.getId(), System.currentTimeMillis());
            }
        }
        return false;
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
            if (isBotCalled(event.getMessage(), storageController.getCustomSoundsEnabled(event.getGuild()))) {
                Log.info("        Bot Called");
                //print the message log in the console if the message was a command
                if (!VariableStorageRead.getDevMode()) printConsoleLog(false, event);

                if (isRateLimited(event.getAuthor())) return;

                //run command
                if (!nepCommands.runCommand(getCommandName(event.getMessage().getContentRaw().trim().toLowerCase().replaceFirst(VariableStorageRead.getCallBot().toLowerCase(), "").trim()),event, storageController)) {
                    String dir = event.getMessage().getContentRaw().replaceAll("[^a-zA-Z0-9]", "");

                    if (storageController.getCustomSoundsEnabled(event.getGuild()) && folderSearch.isFolder(new File(dir))) {
                        File folder = folderSearch.getFolder(new File(dir));
                        Log.info("    Found Custom Sound Folder");
                        if (event.getGuild().getAudioManager() != null || event.getMember().getVoiceState().getChannel() != null){
                            randomSoundPicker.playRandomAudioFile(event, folder);
                        }
                    }
                }
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
            if (!storageController.isGuildStored(guildObject)) {
                if (VariableStorageRead.getDevMode()) {
                    Log.info("    Adding New Guild: " + guildObject.getName());
                }
                storageController.addGuild(guildObject);
            }
            else {
                if (VariableStorageRead.getDevMode()) Log.info("    Found Guild: " + guildObject.getName());
            }
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
    private String[] getCommandName(String MessageContent){
        String[] splitStr = MessageContent.trim().split("\\s+");
        String[] returnText = new String[2];
        if (splitStr.length == 1) {
            returnText[0] = splitStr[0];
            returnText[1] = "";
        } else {
            returnText[0] = splitStr[0];
            returnText[1] = MessageContent.substring(splitStr[0].length());
        }
        return returnText;
    }
}
