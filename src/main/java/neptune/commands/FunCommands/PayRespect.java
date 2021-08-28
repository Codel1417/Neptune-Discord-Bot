package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Objects;

public class PayRespect implements ICommand {
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {

        event.getChannel()
                .sendMessage(Objects.requireNonNull(event.getMember()).getAsMention() + " has paid respect")
                .queue();
    }
}
