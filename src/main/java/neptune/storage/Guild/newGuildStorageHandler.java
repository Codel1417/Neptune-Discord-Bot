package neptune.storage.Guild;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import io.prometheus.client.cache.caffeine.CacheMetricsCollector;
import io.prometheus.client.hibernate.HibernateStatisticsCollector;
import io.sentry.Sentry;

public class newGuildStorageHandler {
    protected static final Logger log = LogManager.getLogger();
    private static volatile newGuildStorageHandler _instance;

    private final CacheMetricsCollector cacheMetrics = new CacheMetricsCollector().register();
    private final Cache<String, guildObject> cache = Caffeine.newBuilder()
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .maximumSize(10_000)
    .recordStats()
    .build();
    private final StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    private final Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
    private final SessionFactory factory = meta.getSessionFactoryBuilder().build();
    private final HibernateStatisticsCollector hibernateStatisticsCollector = new HibernateStatisticsCollector(factory, "Guilds").enablePerQueryMetrics().register();

    private newGuildStorageHandler() {
        cacheMetrics.addCache("GuildFilesCache", cache);
    }
    public static synchronized newGuildStorageHandler getInstance() {
        if (_instance == null) {
            synchronized (GuildStorageHandler.class) {
                if (_instance == null) _instance = new newGuildStorageHandler();
            }
        }
        return _instance;
    }
    
    public guildObject readFile(String guildID) {
        guildObject guildEntity = cache.getIfPresent(guildID);
        if (guildEntity != null) {
            log.info("Loaded guild: " + guildID + " from cache");
            return guildEntity;
        }
        Session session = factory.openSession();  
        guildEntity = (guildObject) session.get("neptune.storage.Guild.guildObject", guildID);
        session.close();    
        if (guildEntity != null){
            log.info("Loaded guild: " + guildID + " from database");
            return guildEntity;
        }
        else {
            log.info("Adding guild: " + guildID);
            return new guildObject(guildID);
        }
    }
    private void writeFile(guildObject guildEntity){
        Sentry.addBreadcrumb("Saving Guild Options for ID: " + guildEntity.getGuildID());
        cache.put(guildEntity.getGuildID(), guildEntity);
        Session session = factory.openSession();  
        Transaction t = session.beginTransaction();   
        session.save(guildEntity);
        t.commit();
        session.close();
    }
}
