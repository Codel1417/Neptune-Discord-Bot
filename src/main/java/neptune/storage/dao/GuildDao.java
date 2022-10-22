package neptune.storage.dao;

import jakarta.persistence.EntityManager;
import neptune.storage.HibernateUtil;
import neptune.storage.entity.GuildEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;

public class GuildDao {
    protected static final Logger log = LogManager.getLogger();
    public GuildDao() {
    }

    public GuildEntity getGuild(String guildID) {
        GuildEntity guildEntity;
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            guildEntity = (GuildEntity) em.find(GuildEntity.class, guildID);
            if (guildEntity != null){
                log.info("Loaded guild: " + guildID + " from database");
                return guildEntity;
            }
            log.info("Adding guild: " + guildID);
            guildEntity = new GuildEntity();
            guildEntity.setGuildID(guildID);
        }
        finally {
            if (em != null) {
                em.close();
            }
        }

        em.close();
        return guildEntity;
    }
    public boolean saveGuild(GuildEntity guildEntity){
        EntityManager session = null;
        try {
            if (guildEntity != null && guildEntity.getGuildID() != null){
                Sentry.addBreadcrumb("Saving Guild Options for ID: " + guildEntity.getGuildID());
                session = HibernateUtil.getEntityManager();
                session.getTransaction().begin();
                session.persist(guildEntity);
                session.getTransaction().commit();
                return true;
            }
        }
        catch (Exception e){
            log.error("Error saving guild: " + guildEntity.getGuildID(), e);
            Sentry.captureException(e);
            saveGuild(guildEntity);
            return false;
        }
        finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return false;
    }
}
