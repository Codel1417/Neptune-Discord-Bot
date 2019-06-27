package Neptune.Commands;

import Neptune.Storage.StorageControllerCached;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

class Nep {
    private StorageControllerCached storageController;
    Nep(StorageControllerCached storageController) {
        this.storageController = storageController;
    }
    //nep nep
    //takes the number of neps in a message and doubles it
    //only counts neps
    void nepCounter(String MessageContent, String reply, MessageReceivedEvent event) {
        String[] nepArray = MessageContent.split(" ");
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
            builder.setTTS(storageController.getTtsEnabled(event.getGuild()));
        }
        else builder.setTTS(false);
        builder.append(responseLine.toString());
        builder.sendTo(event.getChannel()).queue();
        //event.getChannel().sendMessage(responseLine.toString()).queue();

    }
}
