package neptune.prometheus;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import java.io.IOException;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.prometheus.client.Counter;
import io.prometheus.client.exporter.HTTPServer;
//import io.prometheus.client.hotspot.DefaultExports;

public class promListener implements EventListener{
    private static HTTPServer server;
    protected static final Logger log = LogManager.getLogger();
    static final Counter REQUESTS = Counter.build()
    .name("events_total").help("Total JDA Listener Events.").register();

    public promListener(){
        //log.trace("Start: Default Exports");
        //DefaultExports.initialize();
        //log.trace("Finish: Default Exports");
        log.trace("Start: HTTP Server");
        try {
            server = new HTTPServer(1234);
        } catch (IOException e1) {
               throw new RuntimeException(e1);
        }
        log.trace("Finish: HTTP Server");
    }
    private boolean threadCreated = false;
    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        REQUESTS.inc();
        if (event instanceof ReadyEvent && !threadCreated) {
            promPeriodic periodic = new promPeriodic( (ReadyEvent) event);
            Thread thread = new Thread(periodic);
            thread.setName("Prometheus Periodic Exporter");
            thread.start();
            threadCreated = true; // prevent duplicate threads from discord reconnect
        }
    }
}
