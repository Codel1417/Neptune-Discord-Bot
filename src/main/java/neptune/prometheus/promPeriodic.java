package neptune.prometheus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.prometheus.client.Gauge;
import io.sentry.Sentry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ReadyEvent;

public class promPeriodic implements Runnable {
    protected static final Logger log = LogManager.getLogger();
    private JDA jda;
    static final Gauge GUILDCOUNT_GAUGE = Gauge.build().name("Total_Servers").help("Total Servers.").register();
    static final Gauge PING_GAUGE = Gauge.build().name("Gateway_Ping").help("Gateway Ping.").register();
    static final Gauge TOTAL_USERS_GAUGE = Gauge.build().name("Total_Users").help("Total users.").register();
    public promPeriodic(ReadyEvent event){
        this.jda = event.getJDA();
    }
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(60000);
                GUILDCOUNT_GAUGE.set(jda.getShardManager().getGuilds().size());
                PING_GAUGE.set(jda.getShardManager().getAverageGatewayPing());
                TOTAL_USERS_GAUGE.set(jda.getShardManager().getUsers().size());
            } catch (InterruptedException e) {
                log.error(e);
                Sentry.captureException(e);
            }
        }
    }
}
