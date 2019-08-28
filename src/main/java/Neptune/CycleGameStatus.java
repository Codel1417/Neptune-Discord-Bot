package Neptune;

import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;

public class CycleGameStatus extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event){
        ArrayList<Game> MessageLoop = new ArrayList<>();
        ShardManager shardManager = event.getJDA().asBot().getShardManager();
        MessageLoop.add(Game.playing("!Nep Help"));
        MessageLoop.add(Game.playing("Nep Nep"));
        MessageLoop.add(Game.playing("!Nep Help"));
        MessageLoop.add(Game.playing("Nepu Nep"));

        while (true) {
            try {
                for (Game game : MessageLoop){
                    Thread.sleep(1000*15); //wait 15 seconds to avoid ratelimit
                    shardManager.setGame(game);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
