package at.ac.tuwien.big.we16.ue3.service;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by Patrick Grosslicht on 03/05/16.
 */
@WebListener
public class EntityManagerFactoryService implements ServletContextListener {
    private static javax.persistence.EntityManagerFactory entityManagerFactory;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        entityManagerFactory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        entityManagerFactory.close();
    }

    public static EntityManager getEntityManager() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
//            throw new IllegalStateException("Context not initialized.");
// TODO wenn hier eine Exception geschossen wird, dann läuft `startExpiredProductsThread` in NotifierService nicht durch.
/*              BigBidConfigurator erstellt NotifierService und ruft getEntityManager auf, bevor contextInitialized getriggert wird
                und somit wird immer die Exception geworfen, weil die entityManagerFactory noch nicht erstellt wurde
                */
        }

        return entityManagerFactory.createEntityManager();
    }
}
