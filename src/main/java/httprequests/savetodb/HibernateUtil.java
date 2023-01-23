package httprequests.savetodb;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final ThreadLocal<Session> threadLocal = new ThreadLocal<>();

    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public static Session session() {
        Session session;
        if (threadLocal.get() == null) {
            session = sessionFactory.openSession();
            threadLocal.set(session);
        } else {
            session = threadLocal.get();
        }
        return session;
    }
}
