package neptune.serverLogging;

import java.io.IOException;

import io.sentry.Sentry;
import neptune.storage.logObject;
import neptune.storage.logsStorageHandler;
import neptune.storage.Enum.LoggingOptionsEnum;
import neptune.storage.Guild.guildObject.logOptionsObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.awt.*;

public class TextLog {
    logsStorageHandler logsStorageHandler = new logsStorageHandler();
    protected static Logger log = LogManager.getLogger();

    public void GuildText(GenericGuildMessageEvent event, logOptionsObject LoggingOptions) {
        TextChannel textChannel = event.getGuild().getTextChannelById(LoggingOptions.getChannel());


        // check if logging is enabled
        if (!LoggingOptions.getOption(LoggingOptionsEnum.TextChannelLogging)) {
            return;
        }

        if (event instanceof GuildMessageReceivedEvent) {
            // stops bot messages and self messages from being logged.
            if (((GuildMessageReceivedEvent) event).getAuthor().isBot()
                    | ((GuildMessageReceivedEvent) event)
                            .getAuthor()
                            .getId()
                            .equalsIgnoreCase(event.getJDA().getSelfUser().getId())
                    | textChannel.getId().equalsIgnoreCase(event.getChannel().getId())) {
                return;
            }
            logObject logEntity = new logObject();
            logEntity.setGuildID(event.getGuild().getId());
            logEntity.setChannelID(event.getChannel().getId());
            logEntity.setMemberID(((GuildMessageReceivedEvent) event).getAuthor().getId());
            logEntity.setMessageID(event.getMessageId());
            logEntity.setMessageContent(
                    ((GuildMessageReceivedEvent) event).getMessage().getContentDisplay());
            try {
                logsStorageHandler.writeFile(logEntity);
            } catch (IOException e) {
                Sentry.captureException(e);
                log.error(e);
            }
        }
        if (event instanceof GuildMessageUpdateEvent) {
            GuildText((GuildMessageUpdateEvent) event, textChannel);

        } else if (event instanceof GuildMessageDeleteEvent) {
            GuildText((GuildMessageDeleteEvent) event, textChannel);
        }
    }

    private void GuildText(GuildMessageUpdateEvent event, TextChannel textChannel) {
        String PreviousMessage = "";
        // stops bot messages and self messages from being logged.
        if (event.getAuthor().isBot()
                | event.getAuthor().getId().equalsIgnoreCase(event.getJDA().getSelfUser().getId())
                | textChannel.getId().equalsIgnoreCase(event.getChannel().getId())) {
            return;
        }
        try {
            logObject logEntity = logsStorageHandler.readFile(event.getMessageId(),event.getGuild().getId(),event.getChannel().getId());
            PreviousMessage = logEntity.getMessageContent();
            logEntity.setMessageContent(event.getMessage().getContentDisplay());
            logsStorageHandler.writeFile(logEntity);

        } catch (IOException e) {
            Sentry.captureException(e);
            log.error(e);
        }

        EmbedBuilder embedBuilder = getEmbedBuilder(event.getMember());
        embedBuilder.setDescription("Message Edited by " + event.getMember().getAsMention() + " in channel " + event.getChannel().getAsMention());

        if (!PreviousMessage.equalsIgnoreCase("")) {
            embedBuilder.addField("Old Message", PreviousMessage, false);
        }
        embedBuilder.addField("New Message", event.getMessage().getContentDisplay(), false);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildText(GuildMessageDeleteEvent event, TextChannel textChannel) {
        String PreviousMessage = "";
        User user = null;
        try {
            logObject logEntity =
                    logsStorageHandler.readFile(
                            event.getMessageId(),
                            event.getGuild().getId(),
                            event.getChannel().getId());
            PreviousMessage = logEntity.getMessageContent();
            logsStorageHandler.deleteFile(
                    event.getMessageId(), event.getGuild().getId(), event.getChannel().getId());
            user = event.getJDA().getUserById(logEntity.getMemberID());

        } catch (IOException e) {
            Sentry.captureException(e);
            log.error(e);
        }

        // stops bot messages and self messages from being logged.
        if (user == null) {
            return;
        }
        if (user.isBot()
                | user.getId().equalsIgnoreCase(event.getJDA().getSelfUser().getId())
                | textChannel.getId().equalsIgnoreCase(event.getChannel().getId())) {
            return;
        }
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getMessageId());
        embedBuilder.setDescription("Message deleted in " + event.getChannel().getAsMention());

        if (!PreviousMessage.equalsIgnoreCase("")) {
            embedBuilder.addField("Deleted Message", PreviousMessage, false);
        }
        if (user != null) {
            embedBuilder.setAuthor(
                    user.getName() + "#" + user.getDiscriminator(), null, user.getAvatarUrl());
        }

        embedBuilder.setColor(Color.RED);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private EmbedBuilder getEmbedBuilder(String messageID) {
        // default embedBuilder Setting
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setFooter("Message ID: " + messageID, null);
        embedBuilder.setColor(Color.MAGENTA);
        return embedBuilder;
    }
    private EmbedBuilder getEmbedBuilder(Member member) {
        // default embedBuilder Setting
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(
                member.getEffectiveName() + "#" + member.getUser().getDiscriminator(),
                null,
                member.getUser().getEffectiveAvatarUrl());
        embedBuilder.setFooter("Member ID: " + member.getUser().getId(), null);
        embedBuilder.setColor(Color.MAGENTA);
        return embedBuilder;
    }
}
