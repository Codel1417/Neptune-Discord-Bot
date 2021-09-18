package neptune.commands;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface ICommand {
    Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder);
}
