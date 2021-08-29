package neptune.storage;

import io.sentry.Sentry;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.IOException;

public class logsStorageHandler {
    protected static final Logger log = LogManager.getLogger();
    private static  logsStorageHandler _instance;
    private final StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    private final Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
    private final SessionFactory factory = meta.getSessionFactoryBuilder().build();
    public void writeFile(logObject logEntity) throws IOException {
        Sentry.addBreadcrumb("Saving log entry for ID: " + logEntity.getMessageID());
        Session session = logEntity.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(logEntity);
        transaction.commit();
        session.close();
    }
    public static synchronized logsStorageHandler getInstance() {
        if (_instance == null) {
            synchronized (logsStorageHandler.class) {
                if (_instance == null) _instance = new logsStorageHandler();
            }
        }
        return _instance;
    }
    private logsStorageHandler(){}

    public logObject readFile(String messageID) throws IOException {
        Sentry.addBreadcrumb("Loading Log for ID: " + messageID);
        Session session = factory.openSession();
        logObject temp = (logObject) session.get("neptune.storage.logObject", messageID);
        if (temp == null) {
            return null;
        }
        temp.setSession(session);
        return temp;
    }

    public void deleteFile(String messageID) {
        try {
            logObject logEntity = readFile(messageID);
            Session session = logEntity.getSession();
            Transaction transaction = session.beginTransaction();
            session.delete(logEntity);
            transaction.commit();
            session.close();
        }
        catch (Exception e){
            Sentry.captureException(e);
            log.error(e);
        }
    }

    public void deleteGuild(String guildID) {
        try {
            GuildStorageHandler guildStorageHandler = GuildStorageHandler.getInstance();
            guildObject guildEntity = guildStorageHandler.readFile(guildID);
            Session session = guildEntity.getSession();
            Transaction transaction = session.beginTransaction();
            session.delete(guildEntity);
            transaction.commit();
            session.close();
        }
        catch (Exception e){
            Sentry.captureException(e);
            log.error(e);
        }
    }
}
