package neptune.scheduler;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.JDA;

public class SchedulerThread implements Runnable {
    JDA jda;
    ArrayList<entry> entries = new ArrayList<entry>();
    protected static final Logger log = LogManager.getLogger();

    public SchedulerThread(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void run() {
        Long nextRunTime = System.currentTimeMillis();

        while (true){
            long currenttime = System.currentTimeMillis();
            if (currenttime >= nextRunTime){
                for(entry currentEntry: entries){
                    if (currenttime > currentEntry.taskRunTimeMS){
                        log.debug("Running Task: " + currentEntry.taskType.toString());
                        currentEntry.runTask();
                        currentEntry.runTimes--;
                        if (currentEntry.runTimes < 1){
                            entries.remove(currentEntry);
                        }
                        else{
                            //calculate next runtime
                        }
                    }
                }
                nextRunTime = currenttime + 30000;
            }
        }
    }


}