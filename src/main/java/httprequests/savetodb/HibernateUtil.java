package httprequests.savetodb;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private final ThreadLocal<Session> threadLocal = new ThreadLocal<>();

    private final SessionFactory sessionFactory;

    public HibernateUtil() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public Session session() {
        Session session;
        if (threadLocal.get() == null) {
            session = sessionFactory.openSession();
            threadLocal.set(session);
        } else {
            session = threadLocal.get();
        }
        return session;
    }

    public void closeSessionFactoryIfOpened() {
        if (sessionFactory.isOpen()) sessionFactory.close();
    }
}
