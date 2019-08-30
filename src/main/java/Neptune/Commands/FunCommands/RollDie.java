package Neptune.Commands.FunCommands;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.CommonMethods;
import Neptune.Commands.commandCategories;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class RollDie extends CommonMethods implements CommandInterface {
    Random random = new Random();
    @Override
    public String getName() {
        return "Roll a Die";
    }

    @Override
    public String getCommand() {
        return "roll";
    }

    @Override
    public String getDescription() {
        return "Roll a die of any size";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Fun;
    }

    @Override
    public String getHelp() {
        return getCommand() + "<Number>";
    }

    @Override
    public boolean getRequireManageServer() {
        return false;
    }

    @Override
    public boolean getRequireOwner() {
        return false;
    }

    @Override
    public boolean getHideCommand() {
        return false;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public boolean run(MessageReceivedEvent event, VariablesStorage variablesStorage, String messageContent) {
        int sides = Integer.decode(messageContent);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(getName());
        int result = random.nextInt(sides) + 1;
        embedBuilder.setDescription("I rolled a d" + sides + " and landed a " + result);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return true;
    }
}
