package neptune.commands.UtilityCommands;

import neptune.commands.ICommand;
import neptune.commands.Helpers;
import neptune.storage.profileObject;
import neptune.storage.profileStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.awt.Color;
import java.util.Objects;
import java.util.TimeZone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;
public class profile implements ICommand {
    final Helpers helpers = new Helpers();
    protected static final Logger log = LogManager.getLogger();
    @Override
    public void run(GuildMessageReceivedEvent event, String messageContent) {
        try{
            String[] command = helpers.getCommandName(messageContent);
            if (command[1].length() == 0) {
                displayProfile(event, event.getAuthor().getId());
            }
            switch (command[0].toLowerCase()) {
                case "language":
                    {
                        updateLanguage(event, command[1]);
                        break;
                    }
                case "bio":
                    {
                        updateBio(event, command[1]);
                        break;
                    }
                case "timezone":
                    {
                        updateTimezone(event, command[1]);
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
        catch (Exception e){
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

    public void displayProfile(GuildMessageReceivedEvent event, String MemberID) {
        profileStorage storage = profileStorage.getInstance();
        profileObject profile = storage.getProfile(Objects.requireNonNull(event.getMember()).getId());

        int points = profile.getPoints();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Objects.requireNonNull(event.getJDA().getUserById(MemberID)).getName());
        TimeZone Timezone = profile.getTimeZone();

        embedBuilder.setDescription(profile.getBio());
        embedBuilder.addField("Language", profile.getLanguage(), true);
        if (Timezone != null) {
            embedBuilder.addField("TimeZone", Timezone.getID(), true);
        }
        embedBuilder.addField("Points", String.valueOf(points), true);
        embedBuilder.setFooter("Use '!nep profile help' to get a list of profile commands.");
        event.getChannel().sendMessage(embedBuilder.build()).queue();

        if (!profile.getSession().isDirty()){
            profile.closeSession();
        }
    }

    public void updateBio(GuildMessageReceivedEvent event, String Bio) {

        profileStorage storage = profileStorage.getInstance();
        profileObject profile = storage.getProfile(Objects.requireNonNull(event.getMember()).getId());
        boolean result = profile.setBio(Bio);
        if (result) {
            displayProfile(event, event.getAuthor().getId());
        } else {
            event.getChannel()
                    .sendMessage("Your bio can not be longer than 700 characters")
                    .queue();
            displayHelp(event);
        }
        storage.serialize(profile);
    }

    public void updateTimezone(GuildMessageReceivedEvent event, String TimeZone) {
        profileStorage storage = profileStorage.getInstance();
        profileObject profile = storage.getProfile(Objects.requireNonNull(event.getMember()).getId());
        boolean result = profile.setTimeZone(TimeZone);
        if (result) {
            displayProfile(event, event.getAuthor().getId());
        } else {
            event.getChannel()
                    .sendMessage("Invalid Format. Timezones must either be UTC or Country/Region")
                    .queue();
            displayHelp(event);
        }
        storage.serialize(profile);
        return;
    }

    public void updateLanguage(GuildMessageReceivedEvent event, String Language) {
        profileStorage storage = profileStorage.getInstance();
        profileObject profile = storage.getProfile(Objects.requireNonNull(event.getMember()).getId());
        boolean result = profile.setLanguage(Language);
        if (result) {
            displayProfile(event, event.getAuthor().getId());
        } else {
            event.getChannel().sendMessage("Invalid Language").queue();
            displayHelp(event);
        }
        storage.serialize(profile);
        return;
    }
}
