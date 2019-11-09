package neptune.commands.FunCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.SQLite.D10000Access;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Random;

public class D10K implements CommandInterface {
D10000Access d10000Access = new D10000Access();
    @Override
    public String getName() {
        return "D10,000 Random Result";
    }

    @Override
    public String getCommand() {
        return "d10k";
    }

    @Override
    public String getDescription() {
        return "D10K Random result from __The Net_Libram_of Random Magical Effects 2.0__";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Fun;
    }

    @Override
    public String getHelp() {
        return getCommand() + " <Number>  to get a specific result";
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
        int number = 0;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        Random random = new Random();
        String result = null;

        try{
            number = Integer.parseInt(messageContent);

            //prevent numbers above 10,000
            if (number > 10000){
                number = 0;
            }
        }
        catch (Exception e){
            //
        }

        if(number == 0){
            number = random.nextInt(10000) + 1;
        }

        result = d10000Access.getResult(number);
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("D10,000 Random Result");
        embedBuilder.setDescription(result);
        embedBuilder.setFooter("Effect #: " + number,null);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return true;
    }
}
