package neptune.commands.UtilityCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.MySQL.LeaderboardStorage;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Map;

public class Leaderboard implements CommandInterface {
    LeaderboardStorage leaderboardStorage = new LeaderboardStorage();
    @Override
    public String getName() {
        return "Leaderboard";
    }

    @Override
    public String getCommand() {
        return "leaderboard";
    }

    @Override
    public String getDescription() {
        return "Displays the Top 10 users of the server";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Utility;
    }

    @Override
    public String getHelp() {
        return null;
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
        StringBuilder stringBuilder = new StringBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle(getName());
        int count = 1;
        Map<String,String> results =  leaderboardStorage.getTopUsers(event.getGuild().getId());
        for (Map.Entry<String,String> result : results.entrySet()){
            String userID = result.getKey();
            String member = userID;
            try{
                member = event.getJDA().getUserById(userID).getAsMention();
            }
            catch (NullPointerException e){
               System.out.println("User not in server");
            }

            stringBuilder.append(count).append(") ").append(member).append(" Level: ").append(calculateRank(Integer.parseInt(result.getValue())));
            stringBuilder.append("\n");
            count++;
        }
        embedBuilder.setDescription(stringBuilder.toString());
        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return false;
    }
    public int calculateRank(int points){
        int point = points;
        int rank = 1;
        while (point > 50) {
            point = point - 50;
            rank++;
        }
        return rank;
    }
    //used for level up notification
    public int calculateRankRemainder(int points){
        while (points > 50) {
            points = points - 50;
        }
        return points;
    }
}
