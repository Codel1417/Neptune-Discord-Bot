package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Ping implements ICommand {
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        event.getChannel().sendMessage("pong").queue();
    }
}
