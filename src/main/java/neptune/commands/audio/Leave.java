package neptune.commands.audio;

import neptune.commands.ISlashCommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class Leave implements  ISlashCommand {


    @Override
    public CommandData RegisterCommand(CommandData commandData) {
        return null;
    }

    @Override
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        String response;
        if (event.getGuild().getAudioManager().isConnected()) {
            event.getGuild().getAudioManager().setSendingHandler(null);
            event.getGuild().getAudioManager().closeAudioConnection();
            response = "Neptune has left the Chat!";
        } else {
            response = "Neptune is not connected to a voice channel";
        }
        return  builder.setContent(response).build();    }
}
