package Neptune;

import Neptune.ServerLogging.GuildLogging;
import Neptune.Storage.StorageController;
import Neptune.Storage.VariablesStorage;
import com.google.gson.internal.LinkedTreeMap;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.channel.category.GenericCategoryEvent;
import net.dv8tion.jda.core.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.core.events.channel.voice.GenericVoiceChannelEvent;
import net.dv8tion.jda.core.events.emote.GenericEmoteEvent;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.core.events.guild.update.GenericGuildUpdateEvent;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.core.events.role.GenericRoleEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

//intercepts discord messages
public class Listener implements EventListener {
    private final messageInterprter message;
    private final GuildLogging guildLogging = new GuildLogging();


    Listener(VariablesStorage variableStorageRead) {
        message = new messageInterprter(variableStorageRead);
    }
    //only for messages
    private void onMessageReceived(MessageReceivedEvent event) {
        System.out.println(event.getMessage());
        //checks if the message was sent from a bot
        if (event.getAuthor().isBot()) return;
        message.runEvent(event);

    }

    //listen for everything else
    //user actions
    private void onGenericGuild(GenericGuildEvent event){
        LinkedTreeMap<String, Object> guildSettings = (LinkedTreeMap<String, Object>) StorageController.getInstance().getGuild(event.getGuild());
        LinkedTreeMap<String, String> LoggingInfo = (LinkedTreeMap<String, String>) guildSettings.getOrDefault("Logging", new LinkedTreeMap<String, String>());
        String LoggingChannel = LoggingInfo.getOrDefault("LoggingChannel","");

        if(LoggingChannel.equalsIgnoreCase("")) return;

        if(event instanceof GenericGuildVoiceEvent){
            guildLogging.GuildVoice((GenericGuildVoiceEvent) event,LoggingInfo);
        }
        else if(event instanceof GenericGuildMessageEvent){
            guildLogging.GuildText((GenericGuildMessageEvent) event,LoggingInfo, guildSettings);
        }
        else if(event instanceof GenericGuildMemberEvent){
            guildLogging.GuildMember((GenericGuildMemberEvent) event,LoggingInfo);
        }
        else if (event instanceof GenericGuildUpdateEvent){
            guildLogging.GuildSettings((GenericGuildUpdateEvent) event,LoggingInfo);
        }
        else {
            System.out.println("onGenericGuild::"+event.toString());
        }
    }

    //text channel changes

    private void onGenericTextChannel(GenericTextChannelEvent event){
        LinkedTreeMap<String, Object> guildSettings = (LinkedTreeMap<String, Object>) StorageController.getInstance().getGuild(event.getGuild());
        LinkedTreeMap<String, String> LoggingInfo = (LinkedTreeMap<String, String>) guildSettings.getOrDefault("Logging", new LinkedTreeMap<String, String>());
        String LoggingChannel = LoggingInfo.getOrDefault("LoggingChannel","");
        if(LoggingChannel.equalsIgnoreCase("")) return;
        guildLogging.GuildTextChannel(event,LoggingInfo);
    }
    //voice channel changes
    private void onGenericVoiceChannel(GenericVoiceChannelEvent event){
        LinkedTreeMap<String, Object> guildSettings = (LinkedTreeMap<String, Object>) StorageController.getInstance().getGuild(event.getGuild());
        LinkedTreeMap<String, String> LoggingInfo = (LinkedTreeMap<String, String>) guildSettings.getOrDefault("Logging", new LinkedTreeMap<String, String>());
        String LoggingChannel = LoggingInfo.getOrDefault("LoggingChannel","");
        if(LoggingChannel.equalsIgnoreCase("")) return;
        guildLogging.GuildVoiceChannel(event,LoggingInfo);
    }


    @Override
    public void onEvent(Event event) {
        if (event instanceof MessageReceivedEvent){
            onMessageReceived((MessageReceivedEvent) event);
        }
        else if (event instanceof GenericGuildEvent){
            onGenericGuild((GenericGuildEvent) event);
        }
        else if (event instanceof GenericTextChannelEvent){
            onGenericTextChannel((GenericTextChannelEvent) event);
        }
        else if (event instanceof GenericVoiceChannelEvent){
            onGenericVoiceChannel((GenericVoiceChannelEvent) event);
        }
    }
}



