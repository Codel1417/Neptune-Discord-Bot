package Neptune.ServerLogging;

import Neptune.Commands.CommonMethods;
import Neptune.Storage.ConvertJSON;
import Neptune.Storage.StorageController;
import com.google.gson.internal.LinkedTreeMap;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.*;
import net.dv8tion.jda.core.events.guild.update.GenericGuildUpdateEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

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
        embedBuilder.setDescription("Joined Voice Channel #"+ event.getChannelLeft().getName());
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
        if (event instanceof GuildMessageUpdateEvent){
            GuildText((GuildMessageUpdateEvent) event,textChannel, channelLog);
        }
        else if (event instanceof GuildMessageDeleteEvent){
            GuildText((GuildMessageDeleteEvent) event,textChannel,channelLog);
        }
        if(event instanceof GuildMessageReceivedEvent){
            if (channelLog.size() > 200){
                channelLog.remove(0);
            }
            LinkedTreeMap<String, String> message = new LinkedTreeMap<>();
            message.put("ID",event.getMessageId());
            message.put("messageContent",((GuildMessageReceivedEvent) event).getMessage().getContentDisplay());
            message.put("AuthorID",((GuildMessageReceivedEvent) event).getAuthor().getId());
            message.put("AuthorName", ((GuildMessageReceivedEvent) event).getAuthor().getName());
            message.put("AuthorDiscriminator", ((GuildMessageReceivedEvent) event).getAuthor().getDiscriminator());
            message.put("AuthorAvatarUrl",((GuildMessageReceivedEvent) event).getAuthor().getEffectiveAvatarUrl());
            channelLog.add(message);

            ChannelLogJson = toJSON(channelLog);
            ChannelsLog.put(event.getChannel().getId(),ChannelLogJson);
            StorageController.getInstance().updateGuildField(event.getGuild(),"MessageLog",ChannelsLog);
        }
    }
    private void GuildText(GuildMessageUpdateEvent event, TextChannel textChannel, ArrayList<LinkedTreeMap<String, String>> ChannelLog){
        String previousMessage = "";
        for (LinkedTreeMap<String, String> message : ChannelLog){
            if(message.getOrDefault("ID","").equalsIgnoreCase(event.getMessage().getId())){
                previousMessage = message.getOrDefault("messageContent","");
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

        if(!previousMessage.getOrDefault("messageContent","").equalsIgnoreCase("")){
            embedBuilder.setAuthor(previousMessage.get("AuthorName") +"#"+ previousMessage.get("AuthorDiscriminator"),null,previousMessage.getOrDefault("AuthorAvatarUrl",null));
            embedBuilder.addField("Deleted Message",previousMessage.getOrDefault("messageContent",""),false);
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
    private EmbedBuilder getEmbedBuilder(){
        //default embedBuilder Setting
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        return embedBuilder;
    }
}
