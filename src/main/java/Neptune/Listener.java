package Neptune;

import Neptune.ServerLogging.GuildLogging;
import Neptune.Storage.SQLite.SettingsStorage;
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

import java.util.Map;

//intercepts discord messages
public class Listener implements EventListener {
    private final messageInterprter message;
    private final GuildLogging guildLogging = new GuildLogging();
    private final SettingsStorage settingsStorage = new SettingsStorage();

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

    @Override
    public void onEvent(Event event) {
        if (event instanceof MessageReceivedEvent){
            onMessageReceived((MessageReceivedEvent) event);
        }
        if(event instanceof GenericGuildVoiceEvent){
            String GuildID = ((GenericGuildVoiceEvent) event).getGuild().getId();
            guildLogging.GuildVoice((GenericGuildVoiceEvent) event,settingsStorage.getGuildSettings(GuildID));
        }
        else if(event instanceof GenericGuildMessageEvent){
            String GuildID = ((GenericGuildMessageEvent) event).getGuild().getId();
            guildLogging.GuildText((GenericGuildMessageEvent) event,settingsStorage.getGuildSettings(GuildID));
        }
        else if(event instanceof GenericGuildMemberEvent){
            String GuildID = ((GenericGuildMemberEvent) event).getGuild().getId();
            guildLogging.GuildMember((GenericGuildMemberEvent) event,settingsStorage.getGuildSettings(GuildID));
        }
        else if (event instanceof GenericGuildUpdateEvent){
            String GuildID = ((GenericGuildUpdateEvent) event).getGuild().getId();
            guildLogging.GuildSettings((GenericGuildUpdateEvent) event,settingsStorage.getGuildSettings(GuildID));
        }
        else if (event instanceof GenericTextChannelEvent){
            String GuildID = ((GenericTextChannelEvent) event).getGuild().getId();
            guildLogging.GuildTextChannel((GenericTextChannelEvent) event,settingsStorage.getGuildSettings(GuildID));
        }
        else if (event instanceof GenericVoiceChannelEvent){
            String GuildID = ((GenericVoiceChannelEvent) event).getGuild().getId();
            guildLogging.GuildVoiceChannel((GenericVoiceChannelEvent) event,settingsStorage.getGuildSettings(GuildID));
        }

    }
}



