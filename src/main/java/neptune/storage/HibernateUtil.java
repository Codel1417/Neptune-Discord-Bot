package neptune.storage;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUnit;

public class HibernateUtil {
    @PersistenceUnit
    private static EntityManagerFactory sessionFactory;

    public static void Initialize() {
        sessionFactory = Persistence.createEntityManagerFactory( "xyz.codel1417.neptune.jpa" );
    }

    public static EntityManager getEntityManager() {
        return sessionFactory.createEntityManager();
    }
}
