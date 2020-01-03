package neptune.commands.FunCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.SQLite.SettingsStorage;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UWU_Translater implements CommandInterface {
    private HashMap<String, String> directTranslations = new HashMap<>();
    private HashMap<String, String> indirectTranslations = new HashMap<>();
    private SettingsStorage settingsStorage = new SettingsStorage();
    public UWU_Translater(){
        //init maps
        indirectTranslations.put("R","W");
        indirectTranslations.put("r","w");
        indirectTranslations.put("L","W");
        indirectTranslations.put("l","w");
        indirectTranslations.put("V","B");
        indirectTranslations.put("v","b");
        indirectTranslations.put("ove","uv");
        indirectTranslations.put("what","whawt");
        indirectTranslations.put("You","U");
        indirectTranslations.put("you","u");
        indirectTranslations.put("to","tuwu");
        indirectTranslations.put("on", "own");
        indirectTranslations.put("and","awnd");
        indirectTranslations.put("both", "bod");
        indirectTranslations.put("Both", "Bod");
        indirectTranslations.put("the", "de");
        indirectTranslations.put("The", "De");
        indirectTranslations.put("One","Wan");
        indirectTranslations.put("one","wan");


    }
    @Override
    public String getName() {
        return "UwU Translator";
    }

    @Override
    public String getCommand() {
        return "uwu";
    }

    @Override
    public String getDescription() {
        return "Translates the previous message from english to UwU";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Fun;
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
        return false;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public boolean run(MessageReceivedEvent event, VariablesStorage variablesStorage, String messageContent) {
        Random random = new Random();
        String message;
        if(messageContent.equalsIgnoreCase("")){
            message = event.getChannel().getHistory().retrievePast(2).complete().get(1).getContentDisplay().replaceAll("\n"," \n ");
        }
        else{
            message = messageContent;
        }
        StringBuilder result = new StringBuilder();
        if(message.length() != 0 ){
            for (String word : message.split(" ")) {
                if (word.matches("\n")){
                    result.append("\n");
                }
                else if (directTranslations.containsKey(word)){
                    result.append(directTranslations.get(word)).append(" ");
                }
                else{
                    for (Map.Entry<String, String> partial : indirectTranslations.entrySet()) {
                        if (word.toLowerCase().contains(partial.getKey().toLowerCase())){
                            word = word.replaceAll(partial.getKey(),partial.getValue());
                        }
                    }
                    if(word.contains(".")){
                        String[] faces = {" OwO"," UwU "," ////w////< mphhhh!!~ "," 3c fufufu~ ","x3 hehe~ "," =////w///= weh~ "};
                        //word = word.replaceAll(".",faces[random.nextInt(faces.length)]);
                    }
                    result.append(word).append(" ");
                }
            }
            boolean tts = settingsStorage.getGuildSettings(event.getGuild().getId()).getOrDefault("TTS","disabled").equalsIgnoreCase("enabled");

            MessageBuilder builder = new MessageBuilder();
            if (event.getMember().hasPermission(Permission.MESSAGE_TTS) && event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_TTS)) {
                builder.setTTS(tts);
            }
            else builder.setTTS(false);
            builder.append(result.toString());
            builder.sendTo(event.getChannel()).queue();
            //event.getChannel().sendMessage(result.toString()).queue();
        }
        return false;
    }
}
