package Neptune.Commands.PassiveCommands;

import Neptune.Storage.SQLite.LoggingHandler;
import Neptune.Storage.SQLite.SettingsStorage;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class guildListener implements EventListener {
    private VariablesStorage VariableStorageRead;
    private SettingsStorage settingsStorage = new SettingsStorage();
    private LoggingHandler loggingHandler = new LoggingHandler();
    public guildListener(VariablesStorage variablesStorage) {
        this.VariableStorageRead = variablesStorage;
    }

    private void onGuildJoin(GuildJoinEvent guildJoinEvent) {

        //notifies the owner of the bot when someone adds the bot to their server.
        guildJoinEvent.getJDA().getUserById(VariableStorageRead.getOwnerID()).openPrivateChannel().queue((channel) ->
                channel.sendMessage("New Server Added: " + guildJoinEvent.getGuild().getName()).queue());
    }

    private void onGuildVoiceUpdate(GuildVoiceUpdateEvent guildVoiceUpdateEvent) {
        //This disconnects the bot from vc when the last person leaves the voice chat.
        try {
            if (guildVoiceUpdateEvent.getGuild().getVoiceChannelById(guildVoiceUpdateEvent.getChannelLeft().getId()).getMembers().size() == 1 && guildVoiceUpdateEvent.getGuild().getAudioManager().isConnected()) {
                guildVoiceUpdateEvent.getGuild().getAudioManager().setSendingHandler(null);
                guildVoiceUpdateEvent.getGuild().getAudioManager().closeAudioConnection();
                System.out.println("Channel Empty, Disconnecting from VC");
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onEvent(Event event) {
       if (event instanceof GuildJoinEvent){
           onGuildJoin((GuildJoinEvent) event);
       }
       else if (event instanceof GuildVoiceUpdateEvent){
           onGuildVoiceUpdate((GuildVoiceUpdateEvent) event);
       }
       else if (event instanceof GuildLeaveEvent){
           settingsStorage.deleteGuild(((GuildLeaveEvent) event).getGuild().getId());
       }
       else if (event instanceof TextChannelDeleteEvent){
           loggingHandler.deleteChannelMessages(((TextChannelDeleteEvent) event).getChannel().getId());
       }
    }
}
