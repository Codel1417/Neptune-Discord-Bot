package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import neptune.commands.ISlashCommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Objects;

public class PayRespect implements ICommand, ISlashCommand {
    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        return builder.setContent(event.getAuthor().getAsMention() + " has paid respect.").build();
    }

    @Override
    public CommandData RegisterCommand(CommandData commandData) {
        return commandData;
    }

    @Override
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        return builder.setContent(event.getUser().getAsMention() + " has paid respect.").build();
    }
}
