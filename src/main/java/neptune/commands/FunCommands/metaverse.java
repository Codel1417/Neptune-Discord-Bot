package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import neptune.commands.ISlashCommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class metaverse implements ICommand, ISlashCommand {
    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        return genMessage(builder);
    }

    @Override
    public CommandData RegisterCommand(CommandData commandData) {
        return null;
    }

    @Override
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        return genMessage(builder);
    }
    private Message genMessage(MessageBuilder builder){
        String word = "Metaverse";
        String result = "1. There is Only One PLACEHOLDER\n" +
                        "2. The PLACEHOLDER is for Everyone.\n" +
                        "3. Nobody Controls the PLACEHOLDER.\n" +
                        "4. The PLACEHOLDER is Open.\n" +
                        "5. The PLACEHOLDER is Hardware-Independent.\n" +
                        "6. The PLACEHOLDER is a Network.\n" +
                        "7. The PLACEHOLDER is the Internet";
        result = result.replaceAll("PLACEHOLDER",word);
        builder.setContent(result);
        return builder.build();
    }
}
