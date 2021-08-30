package neptune.storage;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import io.sentry.Sentry;

public class profileStorage {
    private static  profileStorage _instance;
    private final StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    private final Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
    private final SessionFactory factory = meta.getSessionFactoryBuilder().build();


    public static synchronized profileStorage getInstance() {
        if (_instance == null) {
            synchronized (profileStorage.class) {
                if (_instance == null) _instance = new profileStorage();
            }
        }
        return _instance;
    }
    private profileStorage(){
    }

    public profileObject getProfile(String ID){
        Sentry.addBreadcrumb("Loading Profile for ID: " + ID);
        Session session = factory.openSession();
        profileObject temp = (profileObject) session.get("neptune.storage.profileObject", ID);
        if (temp == null) {
            temp = new profileObject(ID);
        }
        temp.setSession(session);
        return temp;
    }

    public void serialize(profileObject profile){
        Sentry.addBreadcrumb("Saving Profile for ID: " + profile.getId());
        Session session = profile.getSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(profile);
        transaction.commit();
        session.close();
    }
}
