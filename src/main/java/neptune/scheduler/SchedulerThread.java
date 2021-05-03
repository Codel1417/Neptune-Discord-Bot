package neptune.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ReadyEvent;
import neptune.scheduler.entry.AbstractEntry;
import neptune.scheduler.entry.IAction;

public class SchedulerThread implements Runnable {
    private JDA jda;
    protected static final Logger log = LogManager.getLogger();

    protected SchedulerThread(ReadyEvent event) {
        this.jda = event.getJDA();
    }

    @Override
    public void run() {
        while (true){
            long currenttime = System.currentTimeMillis();
            for(IAction currentEntry: ScheduledTaskStorage.getInstance().getEntries()){
                //CAST ALL OF THE THINGS
                if (currenttime > ((AbstractEntry) currentEntry).getTaskRunTimeMS()){
                    currentEntry.runTask();
                    ((AbstractEntry) currentEntry).setRunTimes(((AbstractEntry) currentEntry).getRunTimes() - 1);
                    if (((AbstractEntry) currentEntry).getRunTimes() < 1){
                        ScheduledTaskStorage.getInstance().removeEntry(currentEntry);
                    }
                    else{
                        ((AbstractEntry) currentEntry).setLastRunTimeMS(currenttime);
                        ((AbstractEntry) currentEntry).setTaskRunTimeMS(currenttime + ((AbstractEntry) currentEntry).getTaskDelayTimeMS());
                    }
                }
            }
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                log.error(e);
                Sentry.captureException(e);
            }
        }
    }
}