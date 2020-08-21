package neptune.commands.FunCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class Translate implements CommandInterface {
    private final int TranslateChangeSize = 6;
    private final String SmallWord = "Nep";
    private final String LargeWord = "Nepu";
    @Override
    public String getName() {
        return "Translate";
    }

    @Override
    public String getCommand() {
        return "translate";
    }

    @Override
    public String getDescription() {
        return "Translates the previous message into Nep";
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
    public boolean getHideCommand() {
        return false;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public guildObject run(MessageReceivedEvent event,String messageContent, guildObject guildEntity) {

        String message;
        if(messageContent.equalsIgnoreCase("")){
            message = event.getChannel().getHistory().retrievePast(2).complete().get(1).getContentDisplay().replaceAll("\n"," \n ");
        }
        else{
            message = messageContent;
        }

        StringBuilder translatedMessage = new StringBuilder();
        for (String string: message.replaceAll("\n"," \n ").split(" ")) {
            if (string.contains("\n")) {
                translatedMessage.append("\n");
            }
            else if (string.length() < TranslateChangeSize) {
                translatedMessage.append(SmallWord).append(" ");
            }
            else translatedMessage.append(LargeWord).append(" ");
        }
        MessageBuilder builder = new MessageBuilder();
        builder.append(translatedMessage.toString());
        event.getChannel().sendMessage(builder.build()).queue();
        return guildEntity;
    }
}
