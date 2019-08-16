package Neptune.Commands.PassiveCommands;

import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class guildListener extends ListenerAdapter {
    private VariablesStorage VariableStorageRead;

    public guildListener(VariablesStorage variablesStorage) {
        this.VariableStorageRead = variablesStorage;
    }

    @Override
    public void onGuildJoin(GuildJoinEvent guildJoinEvent) {

        //notifies the owner of the bot when someone adds the bot to their server.
        guildJoinEvent.getJDA().getUserById(VariableStorageRead.getOwnerID()).openPrivateChannel().queue((channel) ->
                channel.sendMessage("New Server Added: " + guildJoinEvent.getGuild().getName()).queue());
    }
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent guildVoiceUpdateEvent) {
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
}