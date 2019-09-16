package Neptune;

import Neptune.Commands.CommandRunner;
import Neptune.Commands.RandomMediaPicker;
import Neptune.Storage.SQLite.SettingsStorage;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

class messageInterprter {
    private final VariablesStorage VariableStorageRead;
    private final CommandRunner nepCommands;
    private final RandomMediaPicker randomMediaPicker = new RandomMediaPicker();
    private final SettingsStorage settingsStorage = new SettingsStorage();

    messageInterprter(VariablesStorage variablesStorage) {
        nepCommands = new CommandRunner(variablesStorage);
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
        boolean multiPrefix;

        //check if the bot was called in chat
        try {
            Map<String,String> test = checkStoredGuild(event.getGuild());
            if (test == null){
                multiPrefix = false;
            }
            else{
                multiPrefix = test.getOrDefault("CustomSounds","disabled").equalsIgnoreCase("enabled");
            }

            if (isBotCalled(event.getMessage(), multiPrefix)) {
                //print the message log in the console if the message was a command
                printConsoleLog(event);

                //run command
                if (!nepCommands.run(event, VariableStorageRead)) {
                    if(multiPrefix){
                        randomMediaPicker.sendMedia(new File(VariableStorageRead.getMediaFolder() + File.separator + "Custom" + File.separator +  event.getMessage().getContentRaw().replace("=","").replace("./","").trim()), event, true, true);
                    }
                }
            }
            //return if bot was not called
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Map<String, String> checkStoredGuild(Guild guildObject) {
        /*
        Checks the list to see if the current guild/server is stored, if not create a new guild entry.
         */
        if(guildObject != null) {
            Map<String, String> storedGuild =  settingsStorage.getGuildSettings(guildObject.getId());
            if (storedGuild == null){
                settingsStorage.addGuild(guildObject.getId());
                return settingsStorage.getGuildSettings(guildObject.getId());
            }
            return storedGuild;
        }
        return null;
    }
    private void printConsoleLog(MessageReceivedEvent event){
        StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("NEPTUNE: New Message:");
            if (event.getGuild() != null) {
                    stringBuilder.append("    Guild ID: ").append(event.getGuild().getId());
            }
            stringBuilder.append("    Channel ID ").append(event.getChannel().getId());
            stringBuilder.append("    Author ID: ").append(event.getAuthor().getId());
        stringBuilder.append("    Message Contents: ").append(event.getMessage().getContentRaw());
        stringBuilder.append("    message: ").append(event.getMessage());
        System.out.println(stringBuilder.toString());
    }
}
