package neptune.threads;

import neptune.CycleGameStatus;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class CycleActivityThreadStarter implements EventListener {
    protected static final Logger log = LogManager.getLogger();
    private Runnable CycleActivity;
    private boolean ActivityThread;
    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (event instanceof ReadyEvent && !ActivityThread) {
            CycleActivity = new CycleGameStatus((ReadyEvent) event);
            Thread CycleActivityThread = new Thread(CycleActivity);
            CycleActivityThread.setName("CycleActivityThread");
            CycleActivityThread.start();
            ActivityThread = true; // prevent duplicate threads from discord reconnect
        }
    }
}
