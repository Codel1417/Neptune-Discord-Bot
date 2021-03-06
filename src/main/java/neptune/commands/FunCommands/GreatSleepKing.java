package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GreatSleepKing implements ICommand {
    HashMap<String, HashMap<String, String>> previousResults = new HashMap<>();
    ArrayList<String> emotions = new ArrayList<>();

    public GreatSleepKing() {
        // https://simple.wikipedia.org/wiki/List_of_emotions
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
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        String MemberID = event.getMember().getId();
        HashMap<String, String> map = previousResults.getOrDefault(MemberID, null);
        int sleep, hours = 0;
        String emotion;
        if (map == null
                || (Long.parseLong(map.get("TimeCreatedMS")) - System.currentTimeMillis())
                        > 64800000) {
            Random random = new Random();
            sleep = random.nextInt(24);
            hours = random.nextInt(24 - sleep);
            emotion = emotions.get(random.nextInt(emotions.size()));
            map = new HashMap<>();
            map.put("SleepTime", String.valueOf(sleep));
            map.put("MoodTime", String.valueOf(hours));
            map.put("Mood", emotion);
            map.put("TimeCreatedMS", String.valueOf(System.currentTimeMillis()));
            previousResults.put(MemberID, map);
        } else {
            sleep = Integer.parseInt(map.get("SleepTime"));
            hours = Integer.parseInt(map.get("MoodTime"));
            emotion = map.get("Mood");
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("You shall have... ").append(sleep).append(" hour(s) of sleep");
        if (hours != 0) {
            stringBuilder.append(" and ").append(hours).append("  hour(s) of ").append(emotion);
        }

        event.getChannel().sendMessage(stringBuilder).queue();
    }
}
