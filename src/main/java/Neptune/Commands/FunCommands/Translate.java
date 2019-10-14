package Neptune.Commands.FunCommands;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.commandCategories;
import Neptune.Storage.SQLite.SettingsStorage;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class Translate implements CommandInterface {
    private final int TranslateChangeSize = 6;
    private final String SmallWord = "Nep";
    private final String LargeWord = "Nepu";
    private SettingsStorage settingsStorage = new SettingsStorage();
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
        boolean tts = settingsStorage.getGuildSettings(event.getGuild().getId()).getOrDefault("TTS","disabled").equalsIgnoreCase("enabled");

        MessageBuilder builder = new MessageBuilder();
        if (event.getMember().hasPermission(Permission.MESSAGE_TTS) && event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_TTS)) {
            builder.setTTS(tts);
        }
        else builder.setTTS(false);
        builder.append(translatedMessage.toString());
        builder.sendTo(event.getChannel()).queue();
        //event.getChannel().sendMessage(translatedMessage.toString()).queue();
        return true;
    }
}
