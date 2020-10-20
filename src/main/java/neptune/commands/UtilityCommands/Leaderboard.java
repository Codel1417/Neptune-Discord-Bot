package neptune.commands.UtilityCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Leaderboard implements CommandInterface {
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
    public boolean getHideCommand() {
        return false;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public guildObject run(
            GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {
        StringBuilder stringBuilder = new StringBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle(getName());
        int count = 1;
        GuildStorageHandler guildStorageHandler = new GuildStorageHandler();
        Map<String, Integer> results;
        try {
            results =
                    guildStorageHandler
                            .readFile(event.getGuild().getId())
                            .getLeaderboard()
                            .getTopUsers();
        } catch (IOException e1) {
            e1.printStackTrace();
            return guildEntity;
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
        return guildEntity;
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
