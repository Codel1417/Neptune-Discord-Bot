package neptune.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface ICommand {
    void run(GuildMessageReceivedEvent event, String messageContent);
}
