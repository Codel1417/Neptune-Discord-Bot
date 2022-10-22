package neptune.commands.UtilityCommands;

import neptune.commands.ICommand;
import neptune.commands.Helpers;
import neptune.storage.dao.GuildDao;
import neptune.storage.entity.GuildEntity;
import neptune.storage.entity.ProfileEntity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.awt.Color;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;
public class profile implements ICommand {
    final Helpers helpers = new Helpers();
    protected static final Logger log = LogManager.getLogger();


    public Message displayHelp(GuildMessageReceivedEvent event, MessageBuilder builder) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("Profile Management Help");
        String commandsString = """
                !nep profile language (Your Language)
                !nep profile timezone (Your TimeZone) <Country/Region | GMT>
                !nep profile bio (Some Text) <Limit 700 Characters>
                """;
        embedBuilder.addField("Commands", commandsString, false);
        return builder.setEmbeds(embedBuilder.build()).build();
    }

    public Message displayProfile(GuildMessageReceivedEvent event, String MemberID, MessageBuilder builder) {
        GuildDao guildDao = new GuildDao();
        GuildEntity guildentity = guildDao.getGuild(event.getGuild().getId());
        List<ProfileEntity> profileEntities = guildentity.getProfile();
        ProfileEntity profile = profileEntities.stream().filter(x -> x.getId().equals(MemberID)).findFirst().orElse(null);

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
        return builder.setEmbeds(embedBuilder.build()).build();
    }

    public Message updateBio(GuildMessageReceivedEvent event, String Bio, MessageBuilder builder) {

        GuildDao guildDao = new GuildDao();
        GuildEntity guildentity = guildDao.getGuild(event.getGuild().getId());
        List<ProfileEntity> profileEntities = guildentity.getProfile();
        ProfileEntity profile = profileEntities.stream().filter(x -> x.getId().equals(event.getMember().getId())).findFirst().orElse(new ProfileEntity(event.getMember().getId()));
        if (profileEntities.stream().noneMatch(x -> x.getId().equals(event.getMember().getId()))) {
            profileEntities.add(profile);
        }
        boolean result = profile.setBio(Bio);
        guildDao.saveGuild(guildentity);
        if (result) {
            return displayProfile(event, event.getAuthor().getId(), builder);
        } else {
            return  builder.setContent("Your bio can not be longer than 700 characters").build();
        }
    }

    public Message updateTimezone(GuildMessageReceivedEvent event, String TimeZone, MessageBuilder builder) {
        GuildDao guildDao = new GuildDao();
        GuildEntity guildentity = guildDao.getGuild(event.getGuild().getId());
        List<ProfileEntity> profileEntities = guildentity.getProfile();
        ProfileEntity profile = profileEntities.stream().filter(x -> x.getId().equals(event.getMember().getId())).findFirst().orElse(new ProfileEntity(event.getMember().getId()));
        if (profileEntities.stream().noneMatch(x -> x.getId().equals(event.getMember().getId()))) {
            profileEntities.add(profile);
        }
        boolean result = profile.setTimeZone(TimeZone);
        guildDao.saveGuild(guildentity);
        if (result) {
            return displayProfile(event, event.getAuthor().getId(), builder);
        } else {
            return builder.setContent("Invalid Format. Timezones must either be UTC or Country/Region").build();
        }
    }

    public Message updateLanguage(GuildMessageReceivedEvent event, String Language, MessageBuilder builder) {
        GuildDao guildDao = new GuildDao();
        GuildEntity guildentity = guildDao.getGuild(event.getGuild().getId());
        List<ProfileEntity> profileEntities = guildentity.getProfile();
        ProfileEntity profile = profileEntities.stream().filter(x -> x.getId().equals(event.getMember().getId())).findFirst().orElse(new ProfileEntity(event.getMember().getId()));
        if (profileEntities.stream().noneMatch(x -> x.getId().equals(event.getMember().getId()))) {
            profileEntities.add(profile);
        }

        boolean result = profile.setLanguage(Language);
        guildDao.saveGuild(guildentity);

        if (result) {
            return displayProfile(event, Language, builder);
        } else {
            return builder.setContent("Invalid Language").build();
        }
    }

    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        try{
            String[] command = helpers.getCommandName(messageContent);
            if (command[1].length() == 0) {
                displayProfile(event, event.getAuthor().getId(), builder);
            }
            switch (command[0].toLowerCase()) {
                case "language" -> {
                    return updateLanguage(event, command[1], builder);
                }
                case "bio" -> {
                    return updateBio(event, command[1], builder);
                }
                case "timezone" -> {
                    return updateTimezone(event, command[1], builder);
                }
                default -> {
                    return displayHelp(event, builder);
                }
            }
        }
        catch (Exception e){
            log.error(e);
            Sentry.captureException(e);
        }
        return displayHelp(event,builder);
    }
}
