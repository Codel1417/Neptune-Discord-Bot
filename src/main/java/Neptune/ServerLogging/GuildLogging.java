package Neptune.ServerLogging;

import com.google.gson.internal.LinkedTreeMap;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;

import java.awt.*;

public class GuildLogging {
    public void GuildVoice(GenericGuildVoiceEvent event, LinkedTreeMap<String, String> LoggingOptions){
        TextChannel textChannel = event.getGuild().getTextChannelById(LoggingOptions.get("LoggingChannel"));

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
    private EmbedBuilder getEmbedBuilder(Member member){
        //default embedBuilder Setting
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(member.getEffectiveName() +"#"+ member.getUser().getDiscriminator(),null,member.getUser().getEffectiveAvatarUrl());
        embedBuilder.setFooter("ID: " + member.getUser().getId(),null);
        embedBuilder.setColor(Color.MAGENTA);
        return embedBuilder;
    }
}
