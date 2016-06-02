package at.ac.tuwien.big.we16.ue4.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceService {
    private final EntityManagerFactory entityManagerFactory;

    public PersistenceService() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
    }

    public EntityManager getEntityManager() {
        return this.entityManagerFactory.createEntityManager();
    }

    public void close() {
        this.entityManagerFactory.close();
    }
}
