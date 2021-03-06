package neptune.commands.UtilityCommands;

import neptune.commands.ICommand;
import neptune.commands.Helpers;
import neptune.storage.Enum.GuildOptionsEnum;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import neptune.storage.Guild.guildObject.guildOptionsObject;
import neptune.storage.Guild.guildObject.leaderboardObject;
import neptune.storage.Guild.guildObject.profileObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.awt.Color;
import java.io.IOException;
import java.util.TimeZone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;
public class profile implements ICommand {
    Helpers helpers = new Helpers();
    protected static final Logger log = LogManager.getLogger();
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        try{
            String[] command = helpers.getCommandName(messageContent);
            if (command[1].length() == 0) {
                displayProfile(event, event.getAuthor().getId(), GuildStorageHandler.getInstance().readFile(event.getGuild().getId()));
            }
            switch (command[0].toLowerCase()) {
                case "language":
                    {
                        GuildStorageHandler.getInstance().writeFile(updateLanguage(event, command[1], GuildStorageHandler.getInstance().readFile(event.getGuild().getId())));
                        break;
                    }
                case "bio":
                    {
                        GuildStorageHandler.getInstance().writeFile(updateBio(event, command[1], GuildStorageHandler.getInstance().readFile(event.getGuild().getId())));
                        break;
                    }
                case "timezone":
                    {
                        GuildStorageHandler.getInstance().writeFile(updateTimezone(event, command[1], GuildStorageHandler.getInstance().readFile(event.getGuild().getId())));
                        break;
                    }
                case "help":
                    {
                        displayHelp(event);
                        break;
                    }
                default:
                    {
                        displayHelp(event);
                    }
            }
        }
        catch (IOException e){
            log.error(e);
            Sentry.captureException(e);
        }
    }

    public void displayHelp(GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("Profile Management Help");
        StringBuilder commandsString = new StringBuilder();
        commandsString.append("!nep profile language (Your Language)\n");
        commandsString.append("!nep profile timezone (Your TimeZone) <Country/Region | GMT>\n");
        commandsString.append("!nep profile bio (Some Text) <Limit 700 Characters>\n");
        embedBuilder.addField("Commands", commandsString.toString(), false);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    public void displayProfile(
            GuildMessageReceivedEvent event, String MemberID, guildObject guildEntity) {
        profileObject profileEntity = guildEntity.getProfiles();
        leaderboardObject leaderboard = guildEntity.getLeaderboard();
        guildOptionsObject guildOptions = guildEntity.getGuildOptions();

        int points = leaderboard.getPoints(MemberID);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(event.getJDA().getUserById(MemberID).getName());
        TimeZone Timezone = profileEntity.getTimeZone(MemberID);

        embedBuilder.setDescription(profileEntity.getBio(MemberID));
        embedBuilder.addField("Language", profileEntity.getLanguage(MemberID), true);
        if (Timezone != null) {
            embedBuilder.addField("TimeZone", Timezone.getID(), true);
        }
        if (guildOptions.getOption(GuildOptionsEnum.leaderboardEnabled)) {
            embedBuilder.addField("Points", String.valueOf(points), true);
        }
        embedBuilder.setFooter("Use '!nep profile help' to get a list of profile commands.");
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    public guildObject updateBio(
            GuildMessageReceivedEvent event, String Bio, guildObject guildEntity) {
        profileObject profileEntity = guildEntity.getProfiles();
        boolean result = profileEntity.setBio(event.getAuthor().getId(), Bio);
        if (result) {
            displayProfile(event, event.getAuthor().getId(), guildEntity);
        } else {
            event.getChannel()
                    .sendMessage("Your bio can not be longer than 700 characters")
                    .queue();
            displayHelp(event);
        }
        guildEntity.setProfiles(profileEntity);
        return guildEntity;
    }

    public guildObject updateTimezone(
            GuildMessageReceivedEvent event, String TimeZone, guildObject guildEntity) {
        profileObject profileEntity = guildEntity.getProfiles();
        boolean result = profileEntity.setTimeZone(event.getAuthor().getId(), TimeZone);
        if (result) {
            displayProfile(event, event.getAuthor().getId(), guildEntity);
        } else {
            event.getChannel()
                    .sendMessage("Invalid Format. Timezones must either be UTC or Country/Region")
                    .queue();
            displayHelp(event);
        }
        guildEntity.setProfiles(profileEntity);
        return guildEntity;
    }

    public guildObject updateLanguage(
            GuildMessageReceivedEvent event, String Language, guildObject guildEntity) {
        profileObject profileEntity = guildEntity.getProfiles();
        boolean result = profileEntity.setLanguage(event.getAuthor().getId(), Language);
        if (result) {
            displayProfile(event, event.getAuthor().getId(), guildEntity);
        } else {
            event.getChannel().sendMessage("Invalid Language").queue();
            displayHelp(event);
        }
        guildEntity.setProfiles(profileEntity);
        return guildEntity;
    }
}
