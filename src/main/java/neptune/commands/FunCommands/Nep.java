package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Nep implements ICommand {
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        String[] nepArray = messageContent.split(" ");
        String reply = "nep ";
        // search for pattern in a
        int count = 2;
        for (String s : nepArray) {
            // if match found increase count
            if (reply.trim().toLowerCase().matches(s.trim().toLowerCase())) count++;
        }
        StringBuilder responseLine = new StringBuilder(reply);
        while (count > 0) {
            responseLine.append(reply);
            count = count - 1;
        }

        MessageBuilder builder = new MessageBuilder();
        builder.append(responseLine.toString());
        event.getChannel().sendMessage(builder.build()).queue();
        // event.getChannel().sendMessage(responseLine.toString()).queue();
    }
}
