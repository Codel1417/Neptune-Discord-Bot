package neptune.commands.FunCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.MySQL.GreatSleepKingStorage;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class GreatSleepKing implements CommandInterface {
    ArrayList<String> emotions = new ArrayList<>();
    public GreatSleepKing(){
        //https://simple.wikipedia.org/wiki/List_of_emotions
        emotions.add("depression");
        emotions.add("happiness");
        emotions.add("anger");
        emotions.add("sadness");
        emotions.add("joy");
        emotions.add("disgust");
        emotions.add("anticipation");
        emotions.add("shame");
        emotions.add("pity");
        emotions.add("anxiety");
    }
    @Override
    public String getName() {
        return "The Great Sleep King";
    }

    @Override
    public String getCommand() {
        return "gsk";
    }

    @Override
    public String getDescription() {
        return "How much sleep will the Great Sleep King grant you tonight?";
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
        GreatSleepKingStorage greatSleepKingStorage = new GreatSleepKingStorage();
        Map<String,String> map = greatSleepKingStorage.getSleepInfo(event.getMember().getId());
        int sleep,hours = 0;
        String emotion;
        if (map == null || Integer.parseInt(map.get("TimeCreatedMS")) > 64800000){
            Random random = new Random();
            sleep = random.nextInt(24);
            hours = random.nextInt(24-sleep);
            emotion = emotions.get(random.nextInt(emotions.size()));
            greatSleepKingStorage.setTime(event.getMember().getId(),emotion,String.valueOf(hours),String.valueOf(sleep),String.valueOf(System.currentTimeMillis()));
        }
        else{
            sleep = Integer.parseInt(map.get("SleepTime"));
            hours = Integer.parseInt(map.get("MoodTime"));
            emotion = map.get("Mood");
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("You shall have... ").append(sleep).append(" hour(s) of sleep");
        if(hours != 0){
            stringBuilder.append(" and ").append(hours).append("  hour(s) of ").append(emotion);
        }

        event.getChannel().sendMessage(stringBuilder).queue();

        return true;
    }
}
