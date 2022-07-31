package neptune;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class CycleGameStatus implements Runnable {
    private final ArrayList<Activity> MessageLoop;
    private final JDA jda;
    protected static final Logger log = LogManager.getLogger();

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
                    // wait 15 seconds to avoid rate limit
                    sleep(15000);
                    jda.getPresence().setActivity(activity);
                }
            } catch (InterruptedException e) {
                log.error("Exception in CycleGameStatus", e);
            }
        }
    }
}
