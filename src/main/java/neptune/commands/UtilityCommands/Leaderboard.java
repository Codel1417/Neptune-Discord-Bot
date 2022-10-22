package neptune.commands.UtilityCommands;

import neptune.commands.ISlashCommand;
import neptune.storage.dao.GuildDao;
import neptune.storage.entity.ProfileEntity;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.awt.*;
import java.util.*;
import java.util.List;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.sentry.Sentry;

public class Leaderboard implements ISlashCommand {
    protected static final Logger log = LogManager.getLogger();


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
    public LinkedHashMap<String, Integer> getTopUsers(Guild guild) {
        GuildDao guildDao = new GuildDao();
        List<ProfileEntity> profiles = guildDao.getGuild(guild.getId()).getProfile();

        HashMap<String, Integer> leaderboards = new HashMap<>();
        for (ProfileEntity member : profiles){
            leaderboards.put(member.getId(), member.getPoints());
        }
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();

        //Sorts the map by leaderboard score.
        leaderboards.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
        int resultSize = reverseSortedMap.size();
        if (resultSize > 10) {
            resultSize = 10;
        }
        LinkedHashMap<String, Integer> finalSortedMap = new LinkedHashMap<>();
        Iterator<Map.Entry<String, Integer>> entry = reverseSortedMap.entrySet().iterator();

        for (int i = 0; i <= resultSize; i++) {
            if (entry.hasNext()) {
                Map.Entry<String, Integer> entry1 = entry.next();
                finalSortedMap.put(entry1.getKey(), entry1.getValue());
            } else {
                break;
            }
        }
        return finalSortedMap;
    }

    @Override
    public CommandData RegisterCommand(CommandData commandData) {
        return null;
    }

    @Override
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        StringBuilder stringBuilder = new StringBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("Leaderboards");
        int count = 1;
        try {
            // https://howtodoinjava.com/sort/java-sort-map-by-values/
            LinkedHashMap<String, Integer> reverseSortedMap = getTopUsers(event.getGuild());
            for (Map.Entry<String, Integer> result : reverseSortedMap.entrySet()) {
                String userID = result.getKey();
                String member = userID;
                try {
                    member = Objects.requireNonNull(event.getJDA().getUserById(userID)).getAsMention();
                } catch (NullPointerException ignored) {

                }
                stringBuilder.append(count).append(") ").append(member).append(" Level: ").append(calculateRank(Integer.parseInt(String.valueOf(result.getValue()))));
                stringBuilder.append("\n");
                count++;
            }
            embedBuilder.setDescription(stringBuilder.toString());
            return builder.setEmbeds(embedBuilder.build()).build();
        } catch (Exception e1) {
            log.error(e1);
            Sentry.captureException(e1);
        }
        return builder.setContent("Unable to retrieve leaderboards.").build();
    }
}
