package neptune.storage.Guild;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import io.sentry.Sentry;

public class GuildStorageHandler {
    protected static final Logger log = LogManager.getLogger();
    private static volatile GuildStorageHandler _instance;

    private final StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    private final Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
    private final SessionFactory factory = meta.getSessionFactoryBuilder().build();

    private GuildStorageHandler() {
    }
    public static synchronized GuildStorageHandler getInstance() {
        if (_instance == null) {
            synchronized (GuildStorageHandler.class) {
                if (_instance == null) _instance = new GuildStorageHandler();
            }
        }
        return _instance;
    }

    public guildObject readFile(String guildID) {
        guildObject guildEntity;
        Session session = factory.openSession();
        guildEntity = (guildObject) session.get("neptune.storage.Guild.guildObject", guildID);
        if (guildEntity != null){
            guildEntity.setSession(session);
            log.info("Loaded guild: " + guildID + " from database");
        }
        else {
            log.info("Adding guild: " + guildID);
            guildEntity = new guildObject(guildID);
            guildEntity.setSession(session);
        }
        return guildEntity;
    }
    public void writeFile(guildObject guildEntity){
        try {
            Sentry.addBreadcrumb("Saving Guild Options for ID: " + guildEntity.getGuildID());
            Session session = guildEntity.getSession();
            Transaction t = session.beginTransaction();
            session.save(guildEntity);
            t.commit();
            session.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
