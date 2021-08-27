package neptune;

import co.elastic.apm.attach.ElasticApmAttacher;
import co.elastic.apm.opentracing.ElasticApmTracer;
import neptune.commands.PassiveCommands.CycleActivityThreadStarter;
import neptune.commands.PassiveCommands.Listener;
import neptune.commands.PassiveCommands.LoggingListener;
import neptune.prometheus.promListener;
import neptune.scheduler.SchedulerListener;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import io.sentry.Sentry;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    protected static final Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        log.trace("Start");
        log.trace("Begin Init Elastic APM");
        ElasticApmAttacher.attach();
        ElasticApmTracer tracer = new ElasticApmTracer();
        log.trace("End Init Elastic APM");

        /*SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(OtlpGrpcSpanExporter.builder().build()).build())
                .build();
        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .buildAndRegisterGlobal();*/
        log.trace("Begin Init Sentry");
        Sentry.init(options -> {
            options.setEnableExternalConfiguration(true);
            options.setEnableUncaughtExceptionHandler(true);
            options.setTracesSampleRate(1.0);
            options.setHostnameVerifier((arg0, arg1) -> true);
            options.setAttachThreads(true);
            options.setAttachServerName(true);
            options.setAttachStacktrace(true);
            //options.setRelease(System.getenv("NEPTUNE_COMMIT_ID"));
            options.setEnableAutoSessionTracking(true);
            options.setEnvironment("development");
        });
        log.trace("Finish Init Sentry");

        log.trace("Begin Init Webhook");
        // Currently, this only lets me know when the bot starts/updates
        WebhookClient client = startWebhook(System.getenv("NEPTUNE_STATUS_WEBHOOK"));
        log.trace("End Init Webhook");

        log.trace("Begin Init JDA");
        startJDA(System.getenv("NEPTUNE_TOKEN"));
        log.trace("End Init JDA");
        //client.send("Starting Neptune on commit id " + System.getenv("NEPTUNE_COMMIT_ID"));
    }

    private static void startJDA(String token) {
        log.info("Starting JDA");
        try {
            DefaultShardManagerBuilder.create(
                            token,
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_VOICE_STATES,
                            GatewayIntent.DIRECT_MESSAGES,
                            GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(new Listener(), new promListener(), new SchedulerListener(), new LoggingListener(), new CycleActivityThreadStarter())
                    .setActivity(Activity.listening("Nep Nep Nep Nep Nep"))
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS)
                    .build();
        } catch (LoginException e) {
            Sentry.captureException(e);
            log.error(e);
            System.exit(2);
        }
    }
    private static WebhookClient startWebhook(String url){
        try {
            WebhookClientBuilder builder = new WebhookClientBuilder(url);
            builder.setThreadFactory((job) -> {
                Thread thread = new Thread(job);
                thread.setName("Neptune Status Webhook");
                thread.setDaemon(true);
                return thread;
            });
            builder.setWait(true);
            return builder.build();
        }
        catch (Exception e){
            return null;
        }
    }
}
