package Neptune;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessagePages {
    Map<String,ArrayList<String>> messages = new HashMap<>();

    public ArrayList<String> getPages(String MessageID){
        ArrayList<String> pages = messages.getOrDefault(MessageID,new ArrayList<String>());
        return pages;
    }
    public void addPaginatedMessage(ArrayList<String> Pages, MessageReceivedEvent event){
        messages.put(event.getMessageId(),Pages);
    }

}
