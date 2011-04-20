package org.opencustomer.framework.db;

import java.util.Hashtable;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateContext {
    
    private static final SessionFactory sessionFactory;
    
    private static final ThreadLocal<SessionWrapper> threadSession = new ThreadLocal<SessionWrapper>();
    
    private static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();
        
    static {
        sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
    }
    
    public static Session getSession() {
        return initSessionWrapper().getSession();
    }
    
    private final static SessionWrapper initSessionWrapper() {
        SessionWrapper session = threadSession.get();
        
        if(session == null) {
            session = new SessionWrapper(sessionFactory.openSession());
            threadSession.set(session);
        }
        
        return session;
    }
    
    public static void closeSession() {
        SessionWrapper session = threadSession.get();
        threadSession.set(null);
        
        if(session != null && session.getSession().isOpen()) {
            session.getSession().close();
        }
    }
    
    public static boolean beginTransaction() {
        boolean isTrxNew = false;
        
        Transaction trx = threadTransaction.get();
        
        if(trx == null) {
            trx = getSession().beginTransaction();
            threadTransaction.set(trx);
            isTrxNew = true;
        }
        
        return isTrxNew;
    }
    
    public static void commitTransaction() {
        Transaction trx = threadTransaction.get();
        
        try {
            if(trx != null && !trx.wasCommitted() && !trx.wasRolledBack()) {
                trx.commit();
                threadTransaction.set(null);
            }
        } catch(HibernateException e) {
            rollbackTransaction();
            throw e;
        }
    }
    
    public static void rollbackTransaction() {
        Transaction trx = threadTransaction.get();
        threadTransaction.set(null);
        
        try {
            if(trx != null && !trx.wasCommitted() && !trx.wasRolledBack()) {
                trx.rollback();
            }
        } finally {
            closeSession();
        }
    }
    
    public static void setAttribute(String key, Object value) {
        Hashtable<String, Object> attributes = initSessionWrapper().getAttributes();
        attributes.put(key, value);
    }
    
    public static Object getAttribute(String key) {
        Hashtable<String, Object> attributes = initSessionWrapper().getAttributes();
        return attributes.get(key);
    }
    
    public static void removeAttribute(String key) {
        Hashtable<String, Object> attributes = initSessionWrapper().getAttributes();
        attributes.remove(key);
    }
    
    private static final class SessionWrapper {
        
        private Session session;
        
        private Hashtable<String, Object> attributes = new Hashtable<String, Object>();

        public SessionWrapper(Session session) {
            this.session = session;
        }
        
        public Hashtable<String, Object> getAttributes()
        {
            return attributes;
        }

        public void setAttributes(Hashtable<String, Object> attributes)
        {
            this.attributes = attributes;
        }

        public Session getSession()
        {
            return session;
        }

        public void setSession(Session session)
        {
            this.session = session;
        }
    }
}
