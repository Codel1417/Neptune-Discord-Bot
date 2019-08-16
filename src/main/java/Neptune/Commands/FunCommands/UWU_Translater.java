package Neptune.Commands.FunCommands;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.commandCategories;
import Neptune.Storage.StorageController;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class UWU_Translater implements CommandInterface {
    private HashMap<String, String> directTranslations = new HashMap<>();
    private HashMap<String, String> indirectTranslations = new HashMap<>();

    public UWU_Translater(){
        //init maps
        indirectTranslations.put("R","W");
        indirectTranslations.put("r","w");
        indirectTranslations.put("L","W");
        indirectTranslations.put("l","w");
        indirectTranslations.put("ove","uv");
        indirectTranslations.put("what","whawt");
        indirectTranslations.put("you","uwu");
        indirectTranslations.put("to","tuwu");
        indirectTranslations.put("on", "own");
        indirectTranslations.put("and","awnd");
        indirectTranslations.put("both", "bod");
        indirectTranslations.put("Both", "Bod");
        indirectTranslations.put("the", "de");
        indirectTranslations.put("The", "De");


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
    public boolean run(MessageReceivedEvent event, StorageController storageController, VariablesStorage variablesStorage, String messageContent) {
        String message = event.getChannel().getHistory().retrievePast(2).complete().get(1).getContentDisplay().replaceAll("\n"," \n ");
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
                    result.append(word).append(" ");
                }
            }
            event.getChannel().sendMessage(result.toString()).queue();
        }
        return false;
    }
}
