package neptune.storage.dao;

import io.sentry.Sentry;
import jakarta.persistence.EntityManager;
import neptune.storage.HibernateUtil;
import neptune.storage.entity.LogEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogsDao {
    protected static final Logger log = LogManager.getLogger();
    public void writeFile(LogEntity logEntity) {
        Sentry.addBreadcrumb("Saving log entry for ID: " + logEntity.getMessageID());
        EntityManager session = null;
        try {
            session = HibernateUtil.getEntityManager();
            session.getTransaction().begin();
            session.persist(logEntity);
            session.getTransaction().commit();
        }
        catch (Exception e){
            Sentry.captureException(e);
        }
        finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
    public LogsDao(){}

    public LogEntity readFile(String messageID) {
        Sentry.addBreadcrumb("Loading Log for ID: " + messageID);
        LogEntity temp;
        EntityManager session = null;
        try  {
            session = HibernateUtil.getEntityManager();
            temp = session.find(LogEntity.class, messageID);
            if (temp == null) {
                return null;
            }
            return temp;
        }
        finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public boolean deleteFile(String messageID) {
        Sentry.addBreadcrumb("Deleting Log for ID: " + messageID);
        EntityManager session = null;
        try {
            session = HibernateUtil.getEntityManager();
            session.getTransaction().begin();
            LogEntity temp = session.find(LogEntity.class, messageID);
            if (temp == null) {
                return false;
            }
            session.remove(temp);
            session.getTransaction().commit();
            return true;
        }
        catch (Exception e){
            Sentry.captureException(e);
            return false;
        }
        finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void deleteGuild(String guildID) {
        Sentry.addBreadcrumb("Deleting Logs for Guild: " + guildID);
        EntityManager session = null;
        try {
            session = HibernateUtil.getEntityManager();
            session.getTransaction().begin();
            session.createQuery("DELETE FROM LogEntity WHERE guildID = :guildID").setParameter("guildID", guildID).executeUpdate();
            session.getTransaction().commit();
        }
        catch (Exception e){
            Sentry.captureException(e);
        }
        finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
