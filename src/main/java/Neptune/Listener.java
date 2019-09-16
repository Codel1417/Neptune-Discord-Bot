package Neptune;

import Neptune.ServerLogging.GuildLogging;
import Neptune.Storage.SQLite.SettingsStorage;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.api.events.channel.voice.GenericVoiceChannelEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.api.events.guild.update.GenericGuildUpdateEvent;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import javax.annotation.Nonnull;
import java.util.Map;

//intercepts discord messages
public class Listener implements EventListener {
    private final messageInterprter message;
    private final GuildLogging guildLogging = new GuildLogging();
    private final SettingsStorage settingsStorage = new SettingsStorage();
    private Runnable CycleActivity;
    private boolean ActivityThread;

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
    public void onEvent(@Nonnull GenericEvent event) {

        //Cycle Activity Status Thread
        if (event instanceof ReadyEvent && !ActivityThread){
            CycleActivity = new CycleGameStatus((ReadyEvent) event);
            Thread CycleActivityThread = new Thread(CycleActivity);
            CycleActivityThread.setName("CycleActivityThread");
            CycleActivityThread.start();
            ActivityThread = true;
        }

        if (event instanceof MessageReceivedEvent){
            onMessageReceived((MessageReceivedEvent) event);
        }

        if (event instanceof GenericGuildEvent){

            //TODO Add missing guilds
            String GuildID = ((GenericGuildEvent) event).getGuild().getId();

            if (GuildID == null) return;

            Map<String, String> LoggingOptions = null;
            LoggingOptions = settingsStorage.getGuildSettings(GuildID);
            if (LoggingOptions == null) return;

            if (LoggingOptions.get("LoggingChannel") == null) return;
            if(LoggingOptions.getOrDefault("LoggingChannel","").equalsIgnoreCase("")) return;
            if(LoggingOptions.getOrDefault("LoggingEnabled","").equalsIgnoreCase("")) return;

            if(event instanceof GenericGuildVoiceEvent){
                guildLogging.GuildVoice((GenericGuildVoiceEvent) event,LoggingOptions);
            }
            else if(event instanceof GenericGuildMessageEvent){
                guildLogging.GuildText((GenericGuildMessageEvent) event,LoggingOptions);
            }
            else if(event instanceof GenericGuildMemberEvent){
                guildLogging.GuildMember((GenericGuildMemberEvent) event,LoggingOptions);
            }
            else if (event instanceof GenericGuildUpdateEvent){
                guildLogging.GuildSettings((GenericGuildUpdateEvent) event,LoggingOptions);
            }
            else if (event instanceof GenericTextChannelEvent){
                guildLogging.GuildTextChannel((GenericTextChannelEvent) event,LoggingOptions);
            }
            else if (event instanceof GenericVoiceChannelEvent){
                guildLogging.GuildVoiceChannel((GenericVoiceChannelEvent) event,LoggingOptions);
            }
        }
        else return;
    }
}



