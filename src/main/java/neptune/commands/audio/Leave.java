package neptune.commands.audio;

import neptune.commands.ICommand;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Leave implements ICommand {
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        if (event.getGuild() != null && event.getGuild().getAudioManager().isConnected()) {
            event.getGuild().getAudioManager().setSendingHandler(null);
            event.getGuild().getAudioManager().closeAudioConnection();
            // event.getChannel().sendMessage("Neptune has left the Chat!").queue();
        } else {
            event.getChannel().sendMessage("Neptune is not connected to a voice channel").queue();
        }
    }
}
