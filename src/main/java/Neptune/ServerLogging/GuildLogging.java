package Neptune.ServerLogging;

import Neptune.Storage.ConvertJSON;
import Neptune.Storage.StorageController;
import com.google.gson.internal.LinkedTreeMap;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.*;
import net.dv8tion.jda.core.events.guild.update.*;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;

import java.awt.*;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.ArrayList;

public class GuildLogging extends ConvertJSON {
    public void GuildVoice(GenericGuildVoiceEvent event, LinkedTreeMap<String, String> LoggingOptions){
        TextChannel textChannel = event.getGuild().getTextChannelById(LoggingOptions.get("LoggingChannel"));

        //check if logging is enabled
        if (!LoggingOptions.getOrDefault("LogVoiceActivity","disabled").equalsIgnoreCase("enabled")){
            return;
        }

        if (event instanceof GuildVoiceJoinEvent){
            GuildVoice((GuildVoiceJoinEvent) event, textChannel);
        }
        else if (event instanceof GuildVoiceLeaveEvent){
            GuildVoice((GuildVoiceLeaveEvent) event,textChannel);
        }
        else if (event instanceof GuildVoiceMoveEvent){
            GuildVoice((GuildVoiceMoveEvent) event,textChannel);
        }
    }
    private void GuildVoice(GuildVoiceJoinEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder =  getEmbedBuilder(event.getMember());
        embedBuilder.addField("Joined Voice Channel","#"+event.getChannelJoined().getName(),true);
        embedBuilder.setColor(Color.GREEN);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildVoice(GuildVoiceLeaveEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder =  getEmbedBuilder(event.getMember());
        embedBuilder.addField("Left Voice Channel", "#"+ event.getChannelLeft().getName(),true);
        embedBuilder.setColor(Color.RED);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildVoice(GuildVoiceMoveEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder =  getEmbedBuilder(event.getMember());
        embedBuilder.setDescription("Changed/Moved to another Voice Channel");
        embedBuilder.addField("Previous Channel",event.getChannelLeft().getName(),true);
        embedBuilder.addField("Current Channel",event.getChannelJoined().getName(),true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    public void GuildText(GenericGuildMessageEvent event, LinkedTreeMap<String, String> LoggingOptions, LinkedTreeMap<String, Object> guildSettings) {

        TextChannel textChannel = event.getGuild().getTextChannelById(LoggingOptions.get("LoggingChannel"));
        LinkedTreeMap<String, String> ChannelsLog = (LinkedTreeMap) guildSettings.getOrDefault("MessageLog",new LinkedTreeMap<>());
        String ChannelLogJson = ChannelsLog.getOrDefault(event.getChannel().getId(),"");
        ArrayList<LinkedTreeMap<String,String>> channelLog;

        if(ChannelLogJson.equalsIgnoreCase("")){
            channelLog = new ArrayList<>();
        }
        else {
            channelLog =  fromJSONList(ChannelLogJson);
        }
        //check if logging is enabled
        if (!LoggingOptions.getOrDefault("LogTextActivity","disabled").equalsIgnoreCase("enabled")){
            return;
        }
        if(event instanceof GuildMessageReceivedEvent){
            channelLog =  ChannelMessageStore(((GuildMessageReceivedEvent) event).getMessage(),channelLog);
            ChannelLogJson = toJSON(channelLog);
            ChannelsLog.put(event.getChannel().getId(),ChannelLogJson);
            StorageController.getInstance().updateGuildField(event.getGuild(),"MessageLog",ChannelsLog);
        }
        if (event instanceof GuildMessageUpdateEvent){
            channelLog =  ChannelMessageStore(((GuildMessageUpdateEvent) event).getMessage(),channelLog);
            ChannelLogJson = toJSON(channelLog);
            ChannelsLog.put(event.getChannel().getId(),ChannelLogJson);
            StorageController.getInstance().updateGuildField(event.getGuild(),"MessageLog",ChannelsLog);
            GuildText((GuildMessageUpdateEvent) event,textChannel, channelLog);
        }
        else if (event instanceof GuildMessageDeleteEvent){
            GuildText((GuildMessageDeleteEvent) event,textChannel,channelLog);
        }

    }
    private ArrayList<LinkedTreeMap<String, String>> ChannelMessageStore(Message eventMessage, ArrayList<LinkedTreeMap<String,String>> channelLog){
        while (channelLog.size() > 500){
            channelLog.remove(0);
        }
        boolean update = false;
        LinkedTreeMap<String, String> message = null;
            for (LinkedTreeMap<String, String> loggedMessage : channelLog){
                if (loggedMessage.getOrDefault("ID","").equalsIgnoreCase(eventMessage.getId())){
                    message = loggedMessage;
                    update = true;
                    break;
                }
            }

        if (message == null){
            message = new LinkedTreeMap<>();

        }

        message.put("ID",eventMessage.getId());
        message.put("messageContent",eventMessage.getContentDisplay());
        if (update){
            message.put("previousMessage", eventMessage.getContentDisplay());
        }
        message.put("AuthorID",eventMessage.getAuthor().getId());
        message.put("AuthorName", eventMessage.getAuthor().getName());
        message.put("AuthorDiscriminator", eventMessage.getAuthor().getDiscriminator());
        message.put("AuthorAvatarUrl",eventMessage.getAuthor().getEffectiveAvatarUrl());
        //TODO: Handle single image for thumbnail
        //TODO update message on edit, maybe previousMessage entry;

        //get list of media
        StringBuilder attachmentsList = new StringBuilder();
        for (Message.Attachment attachment : eventMessage.getAttachments()){
            attachmentsList.append(attachment.getUrl()).append("\n");
        }
        if(!attachmentsList.toString().equalsIgnoreCase("")){
            message.put("Attachments",attachmentsList.toString());
        }
        channelLog.add(message);
        return channelLog;
    }
    private void GuildText(GuildMessageUpdateEvent event, TextChannel textChannel, ArrayList<LinkedTreeMap<String, String>> ChannelLog){
        String previousMessage = "";
        for (LinkedTreeMap<String, String> message : ChannelLog){
            if(message.getOrDefault("ID","").equalsIgnoreCase(event.getMessage().getId())){
                previousMessage = message.getOrDefault("previousMessage","");
            }
        }
        EmbedBuilder embedBuilder =  getEmbedBuilder(event.getMember());
        embedBuilder.setDescription("Message Edited by " + event.getMember().getAsMention() + " in channel " + event.getChannel().getAsMention());

        if (!previousMessage.equalsIgnoreCase("")) {
            embedBuilder.addField("Old Message",previousMessage,false);
        }
        embedBuilder.addField("New Message",event.getMessage().getContentDisplay(),false);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildText(GuildMessageDeleteEvent event, TextChannel textChannel, ArrayList<LinkedTreeMap<String, String>> ChannelLog){
        LinkedTreeMap<String, String> previousMessage = null;
        for (LinkedTreeMap<String, String> message : ChannelLog){
            if(message.getOrDefault("ID","").equalsIgnoreCase(event.getMessageId())){
                previousMessage = message;
            }
        }
        EmbedBuilder embedBuilder =  getEmbedBuilder(event.getMessageId());
        embedBuilder.setDescription("Message deleted in " + event.getChannel().getAsMention());

        if (previousMessage != null && !previousMessage.getOrDefault("messageContent", "").equalsIgnoreCase("")) {
            embedBuilder.setAuthor(previousMessage.get("AuthorName") + "#" + previousMessage.get("AuthorDiscriminator"), null, previousMessage.getOrDefault("AuthorAvatarUrl", null));
            embedBuilder.addField("Deleted Message", previousMessage.getOrDefault("messageContent", ""), false);
        }

        embedBuilder.setColor(Color.RED);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    public void GuildMember(GenericGuildMemberEvent event, LinkedTreeMap<String, String> LoggingOptions){
        TextChannel textChannel = event.getGuild().getTextChannelById(LoggingOptions.get("LoggingChannel"));

        //check if logging is enabled
        if (!LoggingOptions.getOrDefault("LogMemberActivity","disabled").equalsIgnoreCase("enabled")){
            return;
        }

        if(event instanceof GuildMemberJoinEvent){
            GuildMember((GuildMemberJoinEvent) event,textChannel);
        }
        else if (event instanceof  GuildMemberLeaveEvent){
            GuildMember((GuildMemberLeaveEvent) event,textChannel);
        }
        else if (event instanceof GuildMemberNickChangeEvent){
            GuildMember((GuildMemberNickChangeEvent) event,textChannel);
        }
        else if (event instanceof GuildMemberRoleAddEvent){
            GuildMember((GuildMemberRoleAddEvent) event,textChannel);
        }
        else if(event instanceof GuildMemberRoleRemoveEvent){
            GuildMember((GuildMemberRoleRemoveEvent) event,textChannel);
        }
    }


    private void GuildMember(GuildMemberJoinEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder =  getEmbedBuilder(event.getMember());
        embedBuilder.setDescription(event.getMember().getAsMention() + " Joined the Server");
        embedBuilder.setColor(Color.GREEN);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildMember(GuildMemberLeaveEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder =  getEmbedBuilder(event.getMember());
        embedBuilder.setDescription(event.getMember().getAsMention() + " Left the Server");
        embedBuilder.setColor(Color.RED);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private void GuildMember(GuildMemberNickChangeEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder =  getEmbedBuilder(event.getMember());
        String oldName = event.getPrevNick();
        String newName = event.getNewNick();
        if (oldName == null){
            embedBuilder.setDescription(event.getMember().getAsMention() + " Set their nickname to ``"+ newName + "``");
        }
        else if (newName == null){
            embedBuilder.setDescription(event.getMember().getAsMention() + " Disabled their nickname");
        }
        else embedBuilder.setDescription(event.getMember().getAsMention() + " Changed their nickname from ``" + oldName +"`` to ``"+ newName + "``");
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildMember(GuildMemberRoleAddEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder =  getEmbedBuilder(event.getMember());
        embedBuilder.setDescription("Role Added ``" + event.getRoles().get(0).getName()+ "``");
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildMember(GuildMemberRoleRemoveEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder =  getEmbedBuilder(event.getMember());
        embedBuilder.setDescription("Role Removed ``" + event.getRoles().get(0).getName()+ "``");
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    public void GuildSettings(GenericGuildUpdateEvent event, LinkedTreeMap<String, String> LoggingOptions){
        TextChannel textChannel = event.getGuild().getTextChannelById(LoggingOptions.get("LoggingChannel"));

        //check if logging is enabled
        if (!LoggingOptions.getOrDefault("LogServerActivity","disabled").equalsIgnoreCase("enabled")){
            return;
        }

        if (event instanceof GuildUpdateAfkChannelEvent){
            GuildSettings((GuildUpdateAfkChannelEvent) event, textChannel);
        }
        else if(event instanceof GuildUpdateAfkTimeoutEvent){
            GuildSettings((GuildUpdateAfkTimeoutEvent) event,textChannel);
        }
        else if(event instanceof GuildUpdateExplicitContentLevelEvent){
            GuildSettings((GuildUpdateExplicitContentLevelEvent) event,textChannel);
        }
        else if (event instanceof GuildUpdateFeaturesEvent){
            GuildSettings((GuildUpdateFeaturesEvent) event,textChannel);
        }
        else if (event instanceof GuildUpdateIconEvent){
            GuildSettings((GuildUpdateIconEvent) event,textChannel);
        }
        else if (event instanceof GuildUpdateMFALevelEvent){
            GuildSettings((GuildUpdateMFALevelEvent) event,textChannel);
        }
        else if (event instanceof GuildUpdateNameEvent){
            GuildSettings((GuildUpdateNameEvent) event,textChannel);
        }
        else if (event instanceof GuildUpdateNotificationLevelEvent){
            GuildSettings((GuildUpdateNotificationLevelEvent) event,textChannel);
        }
        else if (event instanceof GuildUpdateOwnerEvent){
            GuildSettings((GuildUpdateOwnerEvent) event,textChannel);
        }
        else if (event instanceof  GuildUpdateRegionEvent){
            GuildSettings((GuildUpdateRegionEvent) event,textChannel);
        }
        else if (event instanceof GuildUpdateSplashEvent){
            GuildSettings((GuildUpdateSplashEvent) event,textChannel);
        }
        else if (event instanceof GuildUpdateSystemChannelEvent){
            GuildSettings((GuildUpdateSystemChannelEvent) event,textChannel);
        }
        else if (event instanceof GuildUpdateVerificationLevelEvent){
            GuildSettings((GuildUpdateVerificationLevelEvent) event,textChannel);
        }
    }
    private void GuildSettings(GuildUpdateAfkChannelEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("AFK Channel Updated");
        if (event.getOldAfkChannel() != null) {
            embedBuilder.addField("Old Channel", "#" + event.getOldAfkChannel().getName(),true);
        }
        else {
            embedBuilder.setDescription("AFK Channel Enabled");
        }
        if (event.getGuild().getAfkChannel() != null){
            embedBuilder.addField("New Channel","#"+event.getGuild().getAfkChannel().getName(),true);
        }
        else {
            embedBuilder.setDescription("AFK Channel Disabled");
        }
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildSettings(GuildUpdateAfkTimeoutEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("AFK Timeout Changed");
        embedBuilder.addField("Old Timeout", event.getOldAfkTimeout().getSeconds() / 60 + " Minutes",true);
        embedBuilder.addField("New Timeout", event.getGuild().getAfkTimeout().getSeconds() / 60 + " Minutes",true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildSettings(GuildUpdateExplicitContentLevelEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Explicit Content Filtering Level Changed");
        embedBuilder.addField("Old Level", String.valueOf(event.getOldLevel().getDescription()),false);
        embedBuilder.addField("New Level", String.valueOf(event.getGuild().getExplicitContentLevel().getDescription()),false);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildSettings(GuildUpdateFeaturesEvent event, TextChannel textChannel){
        //TODO: Figure out what im logging here
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        StringBuilder oldFeatures = new StringBuilder();
        StringBuilder newFeatures = new StringBuilder();
        for (String string : event.getOldFeatures()){
            oldFeatures.append(string).append("\n");
        }
        for (String string : event.getGuild().getFeatures()){
            newFeatures.append(string).append("\n");
        }
        embedBuilder.addField("Old Features",oldFeatures.toString(),true);
        embedBuilder.addField("New Features",newFeatures.toString(),true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildSettings(GuildUpdateIconEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Guild Icon Updated");
        embedBuilder.setImage(event.getGuild().getIconUrl());
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildSettings(GuildUpdateMFALevelEvent event,TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("MFA Level Updated");
        embedBuilder.addField("Old MFA Level",event.getOldMFALevel().toString(),true);
        embedBuilder.addField("New MFA Level", event.getGuild().getRequiredMFALevel().toString(),true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildSettings(GuildUpdateNameEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Guild Name Changed");
        embedBuilder.addField("Old Name",event.getOldName(),true);
        embedBuilder.addField("New Name",event.getGuild().getName(),true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildSettings(GuildUpdateNotificationLevelEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Default Notification Level Changed");
        embedBuilder.addField("Old Notification Level",event.getOldNotificationLevel().name(),false);
        embedBuilder.addField("New Notification Level",event.getGuild().getDefaultNotificationLevel().name(),false);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildSettings(GuildUpdateOwnerEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Guild Owner Changed");
        embedBuilder.addField("Old Owner",event.getOldOwner().getAsMention(),true);
        embedBuilder.addField("New Owner",event.getGuild().getOwner().getAsMention(),true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildSettings(GuildUpdateRegionEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Guild Region Changed");
        embedBuilder.addField("Old Region", event.getOldRegion().getName(),true);
        embedBuilder.addField("New Region", event.getNewRegion().getName(),true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildSettings(GuildUpdateSplashEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Guild Splash Image Updated");
        embedBuilder.setImage(event.getGuild().getSplashUrl());
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildSettings(GuildUpdateSystemChannelEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("System notification channel changed");
        embedBuilder.addField("Old Channel",event.getOldSystemChannel().getAsMention(),true);
        embedBuilder.addField("New Channel",event.getGuild().getSystemChannel().getAsMention(),true);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    private void GuildSettings(GuildUpdateVerificationLevelEvent event, TextChannel textChannel){
        EmbedBuilder embedBuilder = getEmbedBuilder(event.getGuild());
        embedBuilder.setDescription("Server User Verification Level Changed");
        embedBuilder.addField("Old Level",event.getOldVerificationLevel().name(),true);
        embedBuilder.addField("New Level",event.getGuild().getVerificationLevel().name(),true);
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
