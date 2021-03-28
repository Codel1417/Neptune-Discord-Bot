package neptune.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class SchedulerListener implements EventListener {
    protected static final Logger log = LogManager.getLogger();
    boolean threadCreated = false;
    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (event instanceof ReadyEvent && !threadCreated) {
            SchedulerThread scheduled = new SchedulerThread( (ReadyEvent) event);
            Thread thread = new Thread(scheduled);
            thread.setName("Task Scheduler");
            thread.start();
            threadCreated = true; // prevent duplicate threads from discord reconnect
        }
    }
}
