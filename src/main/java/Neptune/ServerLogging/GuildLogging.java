package Neptune.ServerLogging;

import Neptune.Storage.SQLite.LoggingHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.text.update.*;
import net.dv8tion.jda.api.events.channel.voice.GenericVoiceChannelEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateBitrateEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdatePermissionsEvent;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.update.*;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;

import java.awt.*;
import java.util.Map;

public class GuildLogging {
    private final LoggingHandler loggingHandler = new LoggingHandler();
    public void GuildVoice(GenericGuildVoiceEvent event, Map<String, String> LoggingOptions) {
        TextChannel textChannel = event.getGuild().getTextChannelById(LoggingOptions.get("LoggingChannel"));

        //check if logging is enabled
        if (!LoggingOptions.getOrDefault("VoiceChannelLogging", "disabled").equalsIgnoreCase("enabled")) {
            return;
        }
        if (event instanceof GuildVoiceJoinEvent) {
            GuildVoice((GuildVoiceJoinEvent) event, textChannel);
        } else if (event instanceof GuildVoiceLeaveEvent) {
            GuildVoice((GuildVoiceLeaveEvent) event, textChannel);
        } else if (event instanceof GuildVoiceMoveEvent) {
            GuildVoice((GuildVoiceMoveEvent) event, textChannel);
        } else if (event instanceof GuildVoiceGuildDeafenEvent) {
            GuildVoice((GuildVoiceGuildDeafenEvent) event, textChannel);
        } else if (event instanceof GuildVoiceGuildMuteEvent) {
            GuildVoice((GuildVoiceGuildMuteEvent) event, textChannel);
        }
    }

    private void GuildVoice(GuildVoiceJoinEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getMember());
        embedBuilder.addField("Joined Voice Channel", "#" + event.getChannelJoined().getName(), true);
        embedBuilder.setColor(Color.GREEN);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildVoice(GuildVoiceLeaveEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getMember());
        embedBuilder.addField("Left Voice Channel", "#" + event.getChannelLeft().getName(), true);
        embedBuilder.setColor(Color.RED);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildVoice(GuildVoiceMoveEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getMember());
        embedBuilder.setDescription("Changed/Moved to another Voice Channel");
        embedBuilder.addField("Previous Channel", event.getChannelLeft().getName(), true);
        embedBuilder.addField("Current Channel", event.getChannelJoined().getName(), true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildVoice(GuildVoiceGuildDeafenEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getMember());
        if (event.isGuildDeafened()) {
            embedBuilder.setDescription(event.getMember().getAsMention() + " is now Guild Deafened in Voice Channels");
        } else {
            embedBuilder.setDescription(event.getMember().getAsMention() + " is no longer Guild Deafened in Voice Channels");
        }
        embedBuilder.setColor(Color.RED);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildVoice(GuildVoiceGuildMuteEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getMember());
        if (event.isGuildMuted()) {
            embedBuilder.setDescription(event.getMember().getAsMention() + " is now Guild Muted in Voice Channels");
        } else {
            embedBuilder.setDescription(event.getMember().getAsMention() + " is no longer Guild Muted in Voice Channels");
        }
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    public void GuildText(GenericGuildMessageEvent event, Map<String, String> LoggingOptions) {
        TextChannel textChannel = event.getGuild().getTextChannelById(LoggingOptions.get("LoggingChannel"));

        //check if logging is enabled
        if (!LoggingOptions.getOrDefault("TextChannelLogging", "disabled").equalsIgnoreCase("enabled")) {
            return;
        }

        if (event instanceof GuildMessageReceivedEvent) {
            //stops bot messages and self messages from being logged.
            if (((GuildMessageReceivedEvent) event).getAuthor().isBot() | ((GuildMessageReceivedEvent) event).getAuthor().getId().equalsIgnoreCase(event.getJDA().getSelfUser().getId()) | textChannel.getId().equalsIgnoreCase(event.getChannel().getId())) {
                return;
            }
            loggingHandler.newLogEntry(event.getGuild().getId(),event.getChannel().getId(),((GuildMessageReceivedEvent) event).getAuthor().getId(),event.getMessageId(),((GuildMessageReceivedEvent) event).getMessage().getContentDisplay());
        }
        if (event instanceof GuildMessageUpdateEvent) {
            GuildText((GuildMessageUpdateEvent) event,textChannel);

        } else if (event instanceof GuildMessageDeleteEvent) {
            GuildText((GuildMessageDeleteEvent) event,textChannel);
        }
    }

    private void GuildText(GuildMessageUpdateEvent event, TextChannel textChannel) {
        String PreviousMessage = "";
        //stops bot messages and self messages from being logged.
        if (event.getAuthor().isBot() | event.getAuthor().getId().equalsIgnoreCase(event.getJDA().getSelfUser().getId()) | textChannel.getId().equalsIgnoreCase(event.getChannel().getId())) {
            return;
        }

        Map<String, String> Message = loggingHandler.getLogEntry(event.getMessageId());
        if (Message != null){
            PreviousMessage = Message.get("MessageContent");
            loggingHandler.updateLogEntry(event.getMessageId(), event.getMessage().getContentDisplay(),PreviousMessage);
        }
        else {
            loggingHandler.newLogEntry(event.getGuild().getId(),event.getChannel().getId(), event.getAuthor().getId(),event.getMessageId(), event.getMessage().getContentDisplay());
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
        Map<String, String> Message = loggingHandler.getLogEntry(event.getMessageId());
        String PreviousMessage = "";
        User user = null;

        if (Message != null){
            PreviousMessage = Message.get("MessageContent");
            user = event.getJDA().getUserById(Message.getOrDefault("AuthorID",""));

        }
        loggingHandler.deleteLogEntry(event.getMessageId());

        //stops bot messages and self messages from being logged.
        if (user != null && user.isBot() | user.getId().equalsIgnoreCase(event.getJDA().getSelfUser().getId()) | textChannel.getId().equalsIgnoreCase(event.getChannel().getId())) {
            return;
        }

        EmbedBuilder embedBuilder = getEmbedBuilder(event.getMessageId());
        embedBuilder.setDescription("Message deleted in " + event.getChannel().getAsMention());

        if (!PreviousMessage.equalsIgnoreCase("")) {
            embedBuilder.addField("Deleted Message", PreviousMessage, false);
        }
        if (user != null){
            embedBuilder.setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getAvatarUrl());
        }


        embedBuilder.setColor(Color.RED);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    public void GuildMember(GenericGuildMemberEvent event, Map<String,String> LoggingOptions) {
        TextChannel textChannel = event.getGuild().getTextChannelById(LoggingOptions.get("LoggingChannel"));

        //check if logging is enabled
        if (!LoggingOptions.getOrDefault("MemberActivityLogging", "disabled").equalsIgnoreCase("enabled")) {
            return;
        }

        if (event instanceof GuildMemberJoinEvent) {
            GuildMember((GuildMemberJoinEvent) event, textChannel);
        } else if (event instanceof GuildMemberLeaveEvent) {
            GuildMember((GuildMemberLeaveEvent) event, textChannel);
        } else if (event instanceof GuildMemberUpdateNicknameEvent) {
            GuildMember((GuildMemberUpdateNicknameEvent) event, textChannel);
        } else if (event instanceof GuildMemberRoleAddEvent) {
            GuildMember((GuildMemberRoleAddEvent) event, textChannel);
        } else if (event instanceof GuildMemberRoleRemoveEvent) {
            GuildMember((GuildMemberRoleRemoveEvent) event, textChannel);
        }
    }

    private void GuildMember(GuildMemberJoinEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getMember());
        embedBuilder.setDescription(event.getMember().getAsMention() + " Joined the Server");
        embedBuilder.setThumbnail(event.getUser().getAvatarUrl());
        embedBuilder.setColor(Color.GREEN);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildMember(GuildMemberLeaveEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getMember());
        embedBuilder.setDescription(event.getMember().getAsMention() + " Left the Server");
        embedBuilder.setThumbnail(event.getUser().getAvatarUrl());
        embedBuilder.setColor(Color.RED);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildMember(GuildMemberUpdateNicknameEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getMember());
        String oldName = event.getOldNickname();
        String newName = event.getNewNickname();
        if (oldName == null) {
            embedBuilder.setDescription(event.getMember().getAsMention() + " Set their nickname to ``" + newName + "``");
        } else if (newName == null) {
            embedBuilder.setDescription(event.getMember().getAsMention() + " Disabled their nickname");
        } else
            embedBuilder.setDescription(event.getMember().getAsMention() + " Changed their nickname from ``" + oldName + "`` to ``" + newName + "``");
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildMember(GuildMemberRoleAddEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getMember());
        embedBuilder.setDescription("Role Added ``" + event.getRoles().get(0).getName() + "``");
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildMember(GuildMemberRoleRemoveEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getMember());
        embedBuilder.setDescription("Role Removed ``" + event.getRoles().get(0).getName() + "``");
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    public void GuildSettings(GenericGuildUpdateEvent event, Map<String, String> LoggingOptions) {
        TextChannel textChannel = event.getGuild().getTextChannelById(LoggingOptions.get("LoggingChannel"));

        //check if logging is enabled
        if (!LoggingOptions.getOrDefault("ServerModificationLogging", "disabled").equalsIgnoreCase("enabled")) {
            return;
        }
        if (event instanceof GuildUpdateAfkChannelEvent) {
            GuildSettings((GuildUpdateAfkChannelEvent) event, textChannel);
        } else if (event instanceof GuildUpdateAfkTimeoutEvent) {
            GuildSettings((GuildUpdateAfkTimeoutEvent) event, textChannel);
        } else if (event instanceof GuildUpdateExplicitContentLevelEvent) {
            GuildSettings((GuildUpdateExplicitContentLevelEvent) event, textChannel);
        } else if (event instanceof GuildUpdateFeaturesEvent) {
            GuildSettings((GuildUpdateFeaturesEvent) event, textChannel);
        } else if (event instanceof GuildUpdateIconEvent) {
            GuildSettings((GuildUpdateIconEvent) event, textChannel);
        } else if (event instanceof GuildUpdateMFALevelEvent) {
            GuildSettings((GuildUpdateMFALevelEvent) event, textChannel);
        } else if (event instanceof GuildUpdateNameEvent) {
            GuildSettings((GuildUpdateNameEvent) event, textChannel);
        } else if (event instanceof GuildUpdateNotificationLevelEvent) {
            GuildSettings((GuildUpdateNotificationLevelEvent) event, textChannel);
        } else if (event instanceof GuildUpdateOwnerEvent) {
            GuildSettings((GuildUpdateOwnerEvent) event, textChannel);
        } else if (event instanceof GuildUpdateRegionEvent) {
            GuildSettings((GuildUpdateRegionEvent) event, textChannel);
        } else if (event instanceof GuildUpdateSplashEvent) {
            GuildSettings((GuildUpdateSplashEvent) event, textChannel);
        } else if (event instanceof GuildUpdateSystemChannelEvent) {
            GuildSettings((GuildUpdateSystemChannelEvent) event, textChannel);
        } else if (event instanceof GuildUpdateVerificationLevelEvent) {
            GuildSettings((GuildUpdateVerificationLevelEvent) event, textChannel);
        }
    }

    private void GuildSettings(GuildUpdateAfkChannelEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("AFK Channel Updated");
        if (event.getOldAfkChannel() != null) {
            embedBuilder.addField("Old Channel", "#" + event.getOldAfkChannel().getName(), true);
        } else {
            embedBuilder.setDescription("AFK Channel Enabled");
        }
        if (event.getGuild().getAfkChannel() != null) {
            embedBuilder.addField("New Channel", "#" + event.getGuild().getAfkChannel().getName(), true);
        } else {
            embedBuilder.setDescription("AFK Channel Disabled");
        }
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildSettings(GuildUpdateAfkTimeoutEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("AFK Timeout Changed");
        embedBuilder.addField("Old Timeout", event.getOldAfkTimeout().getSeconds() / 60 + " Minutes", true);
        embedBuilder.addField("New Timeout", event.getGuild().getAfkTimeout().getSeconds() / 60 + " Minutes", true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildSettings(GuildUpdateExplicitContentLevelEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Explicit Content Filtering Level Changed");
        embedBuilder.addField("Old Level", String.valueOf(event.getOldLevel().getDescription()), false);
        embedBuilder.addField("New Level", String.valueOf(event.getGuild().getExplicitContentLevel().getDescription()), false);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildSettings(GuildUpdateFeaturesEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        StringBuilder oldFeatures = new StringBuilder();
        StringBuilder newFeatures = new StringBuilder();
        for (String string : event.getOldFeatures()) {
            oldFeatures.append(string).append("\n");
        }
        for (String string : event.getGuild().getFeatures()) {
            newFeatures.append(string).append("\n");
        }
        embedBuilder.addField("Old Features", oldFeatures.toString(), true);
        embedBuilder.addField("New Features", newFeatures.toString(), true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildSettings(GuildUpdateIconEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Guild Icon Updated");
        embedBuilder.setImage(event.getGuild().getIconUrl());
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildSettings(GuildUpdateMFALevelEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("MFA Level Updated");
        embedBuilder.addField("Old MFA Level", event.getOldMFALevel().toString(), true);
        embedBuilder.addField("New MFA Level", event.getGuild().getRequiredMFALevel().toString(), true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildSettings(GuildUpdateNameEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Guild Name Changed");
        embedBuilder.addField("Old Name", event.getOldName(), true);
        embedBuilder.addField("New Name", event.getGuild().getName(), true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildSettings(GuildUpdateNotificationLevelEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Default Notification Level Changed");
        embedBuilder.addField("Old Notification Level", event.getOldNotificationLevel().name(), false);
        embedBuilder.addField("New Notification Level", event.getGuild().getDefaultNotificationLevel().name(), false);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildSettings(GuildUpdateOwnerEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Guild Owner Changed");
        embedBuilder.addField("Old Owner", event.getOldOwner().getAsMention(), true);
        embedBuilder.addField("New Owner", event.getGuild().getOwner().getAsMention(), true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildSettings(GuildUpdateRegionEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Guild Region Changed");
        embedBuilder.addField("Old Region", event.getOldRegion().getName(), true);
        embedBuilder.addField("New Region", event.getNewRegion().getName(), true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildSettings(GuildUpdateSplashEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Guild Splash Image Updated");
        embedBuilder.setImage(event.getGuild().getSplashUrl());
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildSettings(GuildUpdateSystemChannelEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("System notification channel changed");
        embedBuilder.addField("Old Channel", event.getOldSystemChannel().getAsMention(), true);
        embedBuilder.addField("New Channel", event.getGuild().getSystemChannel().getAsMention(), true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildSettings(GuildUpdateVerificationLevelEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Server User Verification Level Changed");
        embedBuilder.addField("Old Level", event.getOldVerificationLevel().name(), true);
        embedBuilder.addField("New Level", event.getGuild().getVerificationLevel().name(), true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    public void GuildTextChannel(GenericTextChannelEvent event, Map<String, String> LoggingOptions) {
        TextChannel textChannel = event.getGuild().getTextChannelById(LoggingOptions.get("LoggingChannel"));
        //check if logging is enabled
        if (!LoggingOptions.getOrDefault("MemberActivityLogging", "disabled").equalsIgnoreCase("enabled")) {
            return;
        }
        if (event instanceof TextChannelCreateEvent) {
            GuildTextChannel((TextChannelCreateEvent) event, textChannel);
        } else if (event instanceof TextChannelDeleteEvent) {
            GuildTextChannel((TextChannelDeleteEvent) event, textChannel);
        } else if (event instanceof TextChannelUpdateNameEvent) {
            GuildTextChannel((TextChannelUpdateNameEvent) event, textChannel);
        } else if (event instanceof TextChannelUpdateNSFWEvent) {
            GuildTextChannel((TextChannelUpdateNSFWEvent) event, textChannel);
        } else if (event instanceof TextChannelUpdateParentEvent) {
            GuildTextChannel((TextChannelUpdateParentEvent) event, textChannel);
        } else if (event instanceof TextChannelUpdatePositionEvent) {
            GuildTextChannel((TextChannelUpdatePositionEvent) event, textChannel);
        } else if (event instanceof TextChannelUpdateTopicEvent) {
            //Spammy
            //GuildTextChannel((TextChannelUpdateTopicEvent) event,textChannel);
        }
        else if (event instanceof TextChannelUpdatePermissionsEvent){
            GuildTextChannel((TextChannelUpdatePermissionsEvent)event,textChannel);
        }
    }

    private void GuildTextChannel(TextChannelCreateEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("A New Text Channel was Created");
        embedBuilder.addField("New Channel", event.getChannel().getAsMention(), true);
        embedBuilder.setFooter("Channel Id " + event.getChannel().getId(), null);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildTextChannel(TextChannelDeleteEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("A Text Channel was Deleted");
        embedBuilder.addField("Deleted Channel", "#" + event.getChannel().getName(), true);
        embedBuilder.setFooter("Channel Id " + event.getChannel().getId(), null);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildTextChannel(TextChannelUpdateNameEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Text Channel Name Updated");
        embedBuilder.addField("Text Channel", event.getChannel().getAsMention(), true);
        embedBuilder.setFooter("Channel Id " + event.getChannel().getId(), null);
        embedBuilder.addBlankField(true);
        embedBuilder.addField("Old Name", "#" + event.getOldName(), true);
        embedBuilder.addField("New Name", "#" + event.getChannel().getName(), true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildTextChannel(TextChannelUpdatePermissionsEvent event, TextChannel textChannel) {
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Text Channel Permissions Updated");
        embedBuilder.addField("Channel Name", event.getChannel().getAsMention(),true);
        embedBuilder.setFooter("Channel Id " + event.getChannel().getId(), null);
        StringBuilder changedRoles = new StringBuilder();
        event.getChangedPermissionHolders();
        StringBuilder changedPermissionHolders = new StringBuilder();
        for (Role role : event.getChangedRoles()) {
            changedRoles.append(role.getName()).append("\n");
        }
        for (IPermissionHolder iPermissionHolder : event.getChangedPermissionHolders()) {
            changedPermissionHolders.append(iPermissionHolder).append("\n");
        }
        if (!changedRoles.toString().equalsIgnoreCase("")){
            embedBuilder.addField("Changed Roles", changedRoles.toString(),true);
        }
        if(!changedPermissionHolders.toString().equalsIgnoreCase("")){
            //embedBuilder.addField("Changed Permission Holders",changedPermissionHolders.toString(),true);
        }
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildTextChannel(TextChannelUpdateNSFWEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Text Channel NSFW Status Updated");
        embedBuilder.addField("Text Channel",event.getChannel().getAsMention(),true);
        embedBuilder.setFooter("Channel Id "+ event.getChannel().getId(),null);
        embedBuilder.addBlankField(true);
        embedBuilder.addField("Old NSFW Status", String.valueOf(event.getOldNSFW()),true);
        embedBuilder.addField("New NSFW Status", String.valueOf(event.getChannel().isNSFW()),true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildTextChannel(TextChannelUpdateParentEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Text Channel Parent Channel Changed");
        embedBuilder.addField("Text Channel",event.getChannel().getAsMention(),true);
        embedBuilder.setFooter("Channel Id "+ event.getChannel().getId(),null);
        embedBuilder.addBlankField(true);
        String OldParent = String.valueOf(event.getOldParent().getName());
        String NewParent = String.valueOf(event.getChannel().getParent().getName());
        if (OldParent != null){
            embedBuilder.addField("Old Parent",OldParent ,true);
        }
        if (NewParent != null){
            embedBuilder.addField("New Parent",NewParent ,true);
        }
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildTextChannel(TextChannelUpdatePositionEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Text Channel Position Changed");
        embedBuilder.addField("Text Channel",event.getChannel().getAsMention(),true);
        embedBuilder.setFooter("Channel Id "+ event.getChannel().getId(),null);
        embedBuilder.addBlankField(true);
        embedBuilder.addField("Old Position", String.valueOf(event.getOldPosition()),true);
        embedBuilder.addField("New Position", String.valueOf(event.getChannel().getPosition()),true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildTextChannel(TextChannelUpdateTopicEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Text Channel Topic Updated");
        embedBuilder.addField("Text Channel",event.getChannel().getAsMention(),true);
        embedBuilder.setFooter("Channel Id "+ event.getChannel().getId(),null);
        embedBuilder.addBlankField(true);
        String OldParent = String.valueOf(event.getOldTopic());
        String NewParent = String.valueOf(event.getChannel().getTopic());
        if (OldParent != null){
            embedBuilder.addField("Old Topic",OldParent ,true);
        }
        if (NewParent != null){
            embedBuilder.addField("New Topic",NewParent ,true);
        }
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    public void GuildVoiceChannel(GenericVoiceChannelEvent event, Map<String, String> LoggingOptions){
        TextChannel textChannel = event.getGuild().getTextChannelById(LoggingOptions.get("LoggingChannel"));
        //check if logging is enabled
        if (!LoggingOptions.getOrDefault("MemberActivityLogging","disabled").equalsIgnoreCase("enabled")){
            return;
        }
        if (event instanceof VoiceChannelCreateEvent){
            GuildVoiceChannel((VoiceChannelCreateEvent) event,textChannel);
        }
        else if(event instanceof VoiceChannelDeleteEvent){
            GuildVoiceChannel((VoiceChannelDeleteEvent) event,textChannel);
        }
        else if (event instanceof VoiceChannelUpdateBitrateEvent){
            GuildVoiceChannel((VoiceChannelUpdateBitrateEvent) event,textChannel);
        }
        else if (event instanceof VoiceChannelUpdateNameEvent){
            GuildVoiceChannel((VoiceChannelUpdateNameEvent) event,textChannel);
        }
        else if (event instanceof VoiceChannelUpdatePermissionsEvent){
            GuildVoiceChannel((VoiceChannelUpdatePermissionsEvent) event,textChannel);
        }
    }
    private void GuildVoiceChannel(VoiceChannelCreateEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("A New Voice Channel was created");
        embedBuilder.addField("New Channel","#"+event.getChannel().getName(),true);
        embedBuilder.setFooter("Channel Id "+ event.getChannel().getId(),null);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildVoiceChannel(VoiceChannelDeleteEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("A Voice Channel was deleted");
        embedBuilder.addField("Deleted Channel","#"+event.getChannel().getName(),true);
        embedBuilder.setFooter("Channel Id "+ event.getChannel().getId(),null);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildVoiceChannel(VoiceChannelUpdateBitrateEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Voice Channel Bitrate Updated");
        embedBuilder.addField("Voice Channel","#"+event.getChannel().getName(),true);
        embedBuilder.setFooter("Channel Id "+ event.getChannel().getId(),null);
        embedBuilder.addBlankField(true);
        embedBuilder.addField("Old Bitrate", String.valueOf(event.getOldBitrate()),true);
        embedBuilder.addField("New Bitrate", String.valueOf(event.getChannel().getBitrate()),true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildVoiceChannel(VoiceChannelUpdateNameEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Voice Channel Channel Name Updated");
        embedBuilder.setFooter("Channel Id "+ event.getChannel().getId(),null);
        embedBuilder.addBlankField(true);
        embedBuilder.addField("Old Name","#"+event.getOldName(),true);
        embedBuilder.addField("New Name","#"+event.getChannel().getName(),true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildVoiceChannel(VoiceChannelUpdatePermissionsEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Voice Channel Permissions Updated");
        embedBuilder.addField("Channel Name", event.getChannel().getName(),true);
        embedBuilder.setFooter("Channel Id " + event.getChannel().getId(), null);
        StringBuilder changedRoles = new StringBuilder();
        event.getChangedPermissionHolders();
        StringBuilder changedPermissionHolders = new StringBuilder();
        for (Role role : event.getChangedRoles()) {
            changedRoles.append(role.getName()).append("\n");
        }
        for (IPermissionHolder iPermissionHolder : event.getChangedPermissionHolders()) {
            changedPermissionHolders.append(iPermissionHolder).append("\n");
        }
        if (!changedRoles.toString().equalsIgnoreCase("")){
            embedBuilder.addField("Changed Roles", changedRoles.toString(),true);
        }
        if(!changedPermissionHolders.toString().equalsIgnoreCase("")){
            //embedBuilder.addField("Changed Permission Holders",changedPermissionHolders.toString(),true);
        }
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private EmbedBuilder getEmbedBuilder(Member member){
        //default embedBuilder Setting
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(member.getEffectiveName() +"#"+ member.getUser().getDiscriminator(),null,member.getUser().getEffectiveAvatarUrl());
        embedBuilder.setFooter("Member ID: " + member.getUser().getId(),null);
        embedBuilder.setColor(Color.MAGENTA);
        return embedBuilder;
    }
    private EmbedBuilder getEmbedBuilder(String messageID){
        //default embedBuilder Setting
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setFooter("Message ID: " + messageID,null);
        embedBuilder.setColor(Color.MAGENTA);
        return embedBuilder;
    }
    private EmbedBuilder getEmbedBuilder(Guild guild){
        //default embedBuilder Setting
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(guild.getName(),null,guild.getIconUrl());
        embedBuilder.setColor(Color.ORANGE);
        return embedBuilder;
    }
}
