package neptune;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;

import java.util.ArrayList;

public class CycleGameStatus implements Runnable {
    private final ArrayList<Activity> MessageLoop;
    private final JDA jda;

    public CycleGameStatus(ReadyEvent event) {
        jda = event.getJDA();
        MessageLoop = new ArrayList<>();
        MessageLoop.add(Activity.playing("!Nep Help"));
        MessageLoop.add(Activity.listening("Nep Nep Nep Nep Nep"));
        MessageLoop.add(Activity.playing("!Nep UwU"));
        MessageLoop.add(Activity.listening("Nepu Nep Nep"));
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                for (Activity activity : MessageLoop) {
                    //noinspection BusyWait
                    Thread.sleep(1000 * 15); // wait 15 seconds to avoid rate limit
                    jda.getPresence().setActivity(activity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
