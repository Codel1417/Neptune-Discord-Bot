package neptune.commands;

import io.sentry.ITransaction;
import io.sentry.Sentry;
import io.sentry.SpanStatus;
import neptune.commands.PassiveCommands.*;
import neptune.prometheus.promListener;
import neptune.scheduler.SchedulerListener;
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
    private ArrayList<EventListener> eventListeners = new ArrayList<>();
    public ListenerHandler(){
        log.trace("Start: Registering Event Listeners");
        log.trace("Registering MessageListener");
        eventListeners.add(new MessageListener());
        log.trace("Registering promListener");
        eventListeners.add(new promListener());
        log.trace("Registering CycleActivityThreadStarter");
        eventListeners.add(new CycleActivityThreadStarter());
        log.trace("Registering LeaderboardListener");
        eventListeners.add(new LeaderboardListener());
        log.trace("Registering LoggingListener");
        eventListeners.add(new LoggingListener());
        log.trace("Registering SchedulerListener");
        eventListeners.add(new SchedulerListener());
        log.trace("Registering VoiceChatListener");
        eventListeners.add(new VoiceChatListener());
        log.trace("Registering GuildListener");
        eventListeners.add(new GuildListener());
        log.trace("Finished: Registering Event Listeners");

    }
    public void onEvent(@Nonnull GenericEvent event) {

        Sentry.addBreadcrumb(event.getClass().getName());
        ITransaction transaction = Sentry.startTransaction(event.getClass().getName(), "Run Event");
        transaction.setTag("JDA Shard", event.getJDA().getShardInfo().getShardString());
        for (EventListener listener : eventListeners){
            try {
                log.trace("Running listener: " + listener.getClass().getName());
                listener.onEvent(event);
            }
            catch (Exception e){
                log.error(e);
                Sentry.captureException(e);
                transaction.setThrowable(e);
                transaction.setStatus(SpanStatus.UNKNOWN_ERROR);
            }
            transaction.finish();
        }
    }
}
