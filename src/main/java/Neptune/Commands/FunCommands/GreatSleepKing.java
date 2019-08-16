package Neptune.Commands.FunCommands;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.commandCategories;
import Neptune.Storage.StorageController;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GreatSleepKing implements CommandInterface {
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
    public boolean run(MessageReceivedEvent event, StorageController storageController, VariablesStorage variablesStorage, String messageContent) {
        Random random = new Random();
        int sleep = random.nextInt(24);
        int hours = random.nextInt(24-sleep);

        //https://simple.wikipedia.org/wiki/List_of_emotions
        ArrayList<String> emotions = new ArrayList<>();
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

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("You shall have... ").append(sleep).append(" hour(s) of sleep");
        if(hours != 0){
            stringBuilder.append(" and ").append(hours).append("  hour(s) of ").append(emotions.get(random.nextInt(emotions.size())));
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Great Sleep King").setColor(Color.MAGENTA).setDescription(stringBuilder);

        event.getChannel().sendMessage(embedBuilder.build()).queue();

        return true;
    }
}
