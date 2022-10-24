package neptune.commands;

import neptune.threads.CycleActivityThreadStarter;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class ListenerHandler implements EventListener
{
    protected static final Logger log = LogManager.getLogger();
    private final ArrayList<EventListener> eventListeners = new ArrayList<>();
    public ListenerHandler(){
        log.trace("Start: Registering Event Listeners");
        log.trace("Registering CycleActivityThreadStarter");
        eventListeners.add(new CycleActivityThreadStarter());

    }
    public void onEvent(@Nonnull GenericEvent event) {

        for (EventListener listener : eventListeners){
            try {
                log.trace("Running listener: " + listener.getClass().getName());
                listener.onEvent(event);
            }
            catch (Exception e){
                log.error(e);
            }
        }
    }
}
