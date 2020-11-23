package neptune.commands.UtilityCommands;

import neptune.commands.CommandInterface;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.Enum.GuildOptionsEnum;
import neptune.storage.Guild.guildObject;
import neptune.storage.Guild.guildObject.guildOptionsObject;
import neptune.storage.Guild.guildObject.leaderboardObject;
import neptune.storage.Guild.guildObject.profileObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.Color;
import java.util.TimeZone;

public class profile implements CommandInterface {
    CommonMethods helpers = new CommonMethods();

    @Override
    public String getName() {
        return "Profile";
    }

    @Override
    public String getCommand() {
        return "profile";
    }

    @Override
    public String getDescription() {
        return "View and edit your Profile";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.General;
    }

    @Override
    public String getHelp() {
        return "Use '!Nep profile help' for a list of commands";
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
        String[] command = helpers.getCommandName(messageContent);
        if (command[1].length() == 0) {
            displayProfile(event, event.getAuthor().getId(), guildEntity);
            return guildEntity;
        }
        switch (command[0].toLowerCase()) {
            case "language":
                {
                    guildEntity = updateLanguage(event, command[1], guildEntity);
                    break;
                }
            case "bio":
                {
                    guildEntity = updateBio(event, command[1], guildEntity);
                    break;
                }
            case "timezone":
                {
                    guildEntity = updateTimezone(event, command[1], guildEntity);
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
        return guildEntity;
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
