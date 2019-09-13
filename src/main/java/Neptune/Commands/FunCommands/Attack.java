package Neptune.Commands.FunCommands;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.RandomMediaPicker;
import Neptune.Commands.commandCategories;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Random;

public class Attack implements CommandInterface {
    @Override
    public String getName() {
        return "Attack";
    }

    @Override
    public String getCommand() {
        return "attack";
    }

    @Override
    public String getDescription() {
        return "Attack your ~~Friends~~ Enemies";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Fun;
    }

    @Override
    public String getHelp() {
        return "";
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

        EmbedBuilder embedBuilder = new EmbedBuilder();
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        RandomMediaPicker randomMediaPicker = new RandomMediaPicker();
        List<Member> mention = event.getMessage().getMentionedMembers();
        embedBuilder.setColor(Color.MAGENTA).setAuthor("Neptune");
        stringBuilder.append("Neptune attacked ");
        if (mention.size() != 0){
            Member target = mention.get(random.nextInt(mention.size()));
            stringBuilder.append(target.getAsMention()).append(" ");
        }
        stringBuilder.append("for ").append(random.nextInt(6)).append(" damage");
        embedBuilder.setDescription(stringBuilder);
        //send message + audio
        event.getChannel().sendMessage(embedBuilder.build()).queue();

        randomMediaPicker.sendMedia(new File(variablesStorage.getMediaFolder() + File.separator + "Attack"),event,false,true);
        return false;
    }
}
