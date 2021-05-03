package neptune;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;

public class CycleGameStatus implements Runnable {
    private ArrayList<Activity> MessageLoop = new ArrayList<>();
    private JDA jda;
    protected static final Logger log = LogManager.getLogger();

    public CycleGameStatus(ReadyEvent event) {
        jda = event.getJDA();
        //TODO FIX
        InputStream is = getClass().getResourceAsStream("StatusText.txt");
        if (is == null) {
            log.fatal("file not found!");
        } else {
            Scanner scanner;
            try {
                scanner = new Scanner(is);
                while (scanner.hasNext()){
                    MessageLoop.add(Activity.playing(scanner.nextLine()));
                }
            } catch (Exception e) {
                log.error(e);
                Sentry.captureException(e);
            }

        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (Activity activity : MessageLoop) {
                    Thread.sleep(1000 * 15); // wait 15 seconds to avoid ratelimit
                    jda.getPresence().setActivity(activity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
