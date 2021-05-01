package neptune.commands.UtilityCommands;

import neptune.commands.ICommand;
import neptune.storage.Guild.GuildStorageHandler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Leaderboard implements ICommand {
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        StringBuilder stringBuilder = new StringBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("Leaderboards");
        int count = 1;
        Map<String, Integer> results;
        try {
            results = GuildStorageHandler.getInstance().readFile(event.getGuild().getId()).getLeaderboard().getTopUsers();
        } catch (Exception e1) {
            e1.printStackTrace();
            return;
        }

        // https://howtodoinjava.com/sort/java-sort-map-by-values/
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
        // Use Comparator.reverseOrder() for reverse ordering
        results.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        for (Map.Entry<String, Integer> result : reverseSortedMap.entrySet()) {
            String userID = result.getKey();
            String member = userID;
            try {
                member = event.getJDA().getUserById(userID).getAsMention();
            } catch (NullPointerException e) {
            }

            stringBuilder
                    .append(count)
                    .append(") ")
                    .append(member)
                    .append(" Level: ")
                    .append(calculateRank(Integer.parseInt(String.valueOf(result.getValue()))));
            stringBuilder.append("\n");
            count++;
        }
        embedBuilder.setDescription(stringBuilder.toString());
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    public int calculateRank(int points) {
        int rank = 1;
        while (points > 50) {
            points = points - 50 * rank;
            rank++;
        }
        return rank;
    }
    // used for level up notification
    public int calculateRankRemainder(int points) {
        int rank = 1;
        while (points > 50) {
            points = points - 50 * rank;
            rank++;
        }
        return points;
    }
}
