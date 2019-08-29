package Neptune;

import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.EventListener;

import java.util.ArrayList;

public class CycleGameStatus implements EventListener {
    ArrayList<Game> MessageLoop;
    public CycleGameStatus(){
        MessageLoop = new ArrayList<>();
        MessageLoop.add(Game.playing("!Nep Help"));
        MessageLoop.add(Game.listening("Nep Nep Nep Nep Nep"));
        MessageLoop.add(Game.playing("!Nep UwU"));
        MessageLoop.add(Game.listening("Nepu Nep Nep"));

    }

    private void onReady(ReadyEvent event){
        while (true) {
            try {
                for (Game game : MessageLoop){
                    Thread.sleep(1000*15); //wait 15 seconds to avoid ratelimit
                    event.getJDA().asBot().getShardManager().setGame(game);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onEvent(Event event){
        if (event instanceof  ReadyEvent){
            onReady((ReadyEvent) event);
        }
    }
}
