package neptune;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CycleGameStatus implements Runnable {
    private ArrayList<Activity> MessageLoop = new ArrayList<>();
    private JDA jda;
    protected static final Logger log = LogManager.getLogger();

    public CycleGameStatus(ReadyEvent event) {
        jda = event.getJDA();
        URL resource = getClass().getClassLoader().getResource("StatusText.txt");
        if (resource == null) {
            log.error("file not found!");
        } else {
            Scanner scanner;
            try {
                scanner = new Scanner(new File(resource.toURI()));
                while (scanner.hasNext()){
                    MessageLoop.add(Activity.playing(scanner.nextLine()));
                }
            } catch (FileNotFoundException | URISyntaxException e) {
                log.error(e);
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
