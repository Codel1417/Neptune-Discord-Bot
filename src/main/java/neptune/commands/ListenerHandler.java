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
        eventListeners.add(new MessageListener());
        eventListeners.add(new promListener());
        eventListeners.add(new CycleActivityThreadStarter());
        eventListeners.add(new LeaderboardListener());
        eventListeners.add(new LoggingListener());
        eventListeners.add(new SchedulerListener());
        eventListeners.add(new VoiceChatListener());
        eventListeners.add(new GuildListener());
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
