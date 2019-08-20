package Neptune.Commands.FunCommands;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.commandCategories;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Nep implements CommandInterface {
    //nep nep
    //only counts neps
    @Override
    public String getName() {
        return "Nep";
    }

    @Override
    public String getCommand() {
        return "nep";
    }

    @Override
    public String getDescription() {
        return "Takes the number of neps in a message and adds one more";
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

        String[] nepArray = messageContent.split(" ");
        String reply = "nep ";
        // search for pattern in a
        int count = 2;
        for (String s : nepArray) {
            // if match found increase count
            if (reply.trim().toLowerCase().matches(s.trim().toLowerCase()))
                count++;
        }
        StringBuilder responseLine = new StringBuilder(reply);
        while (count > 0) {
            responseLine.append(reply);
            count = count - 1;
        }

        MessageBuilder builder = new MessageBuilder();
        if (event.getMember().hasPermission(Permission.MESSAGE_TTS) && event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_TTS)) {
            //builder.setTTS(storageController.getTtsEnabled(event.getGuild()));
        }
        else builder.setTTS(false);
        builder.append(responseLine.toString());
        builder.sendTo(event.getChannel()).queue();
        //event.getChannel().sendMessage(responseLine.toString()).queue();
        return true;    }

}
