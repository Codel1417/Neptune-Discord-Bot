package Neptune;

import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;

import java.util.ArrayList;

public class CycleGameStatus {
    public void run(ReadyEvent event) {
        try {
            Thread.sleep(1000*15);
            ArrayList<Game> MessageLoop = new ArrayList<>();
            int messageNum = 0;

            event.getJDA().asBot().getShardManager().setGame(MessageLoop.get(messageNum));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
