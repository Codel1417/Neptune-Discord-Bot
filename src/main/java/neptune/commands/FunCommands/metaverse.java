package neptune.commands.FunCommands;

import neptune.commands.ISlashCommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class metaverse implements ISlashCommand {


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
        String result = """
                1. There is Only One PLACEHOLDER
                2. The PLACEHOLDER is for Everyone.
                3. Nobody Controls the PLACEHOLDER.
                4. The PLACEHOLDER is Open.
                5. The PLACEHOLDER is Hardware-Independent.
                6. The PLACEHOLDER is a Network.
                7. The PLACEHOLDER is the Internet""";
        result = result.replaceAll("PLACEHOLDER",word);
        builder.setContent(result);
        return builder.build();
    }
}
