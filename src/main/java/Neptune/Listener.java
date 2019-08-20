package Neptune;

import Neptune.Storage.StorageController;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.channel.category.GenericCategoryEvent;
import net.dv8tion.jda.core.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.core.events.channel.voice.GenericVoiceChannelEvent;
import net.dv8tion.jda.core.events.emote.GenericEmoteEvent;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.role.GenericRoleEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

//intercepts discord messages
public class Listener extends ListenerAdapter {
    private VariablesStorage variableStorageRead;
    private final messageInterprter message;
    Listener(VariablesStorage variableStorageRead) {
        this.variableStorageRead = variableStorageRead;
        message = new messageInterprter(variableStorageRead);
    }
    //only for messages
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println(event.getMessage());
        event.getJDA().getPresence().setGame(Game.playing(variableStorageRead.getCallBot() + " help"));
        //checks if the message was sent from a bot
        if (event.getAuthor().isBot()) return;
        message.runEvent(event);

    }

    //listen for everything else
    @Override
    public void onGenericGuild(GenericGuildEvent event){
        System.out.println(event.toString());
    }
    public void onGenericTextChannel(GenericTextChannelEvent event){
        System.out.println(event.toString());
    }
    public void onGenericVoiceChannel(GenericVoiceChannelEvent event){
        System.out.println(event.toString());
    }
    public void onGenericCategory(GenericCategoryEvent event){
        System.out.println(event.toString());
    }
    public void onGenericRole(GenericRoleEvent event){
        System.out.println(event.toString());
    }
    public void onGenericEmote(GenericEmoteEvent event){
        System.out.println(event.toString());
    }
}



