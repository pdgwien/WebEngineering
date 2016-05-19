package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue3.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class UserService {
    private EntityManager em;
    private TypedQuery<User> query;
    private static final Logger logger = LogManager.getLogger(UserService.class);



    public UserService() {
        this.em = EntityManagerFactoryService.getEntityManager();
        this.query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
    }

    public void createUser(User user) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(user);
            tx.commit();
            logger.info("Persisted user {} with ID {}", user.getEmail(), user.getId());
        } catch (Exception e) {
            logger.error("Error while persisting: {}", e);
            tx.rollback();
        }
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        try {
            logger.info("Search for user with email {}", email);
            return this.query.setParameter("email", email).getSingleResult();
        } catch(NoResultException e) {
            throw new UserNotFoundException();
        }
    }


}
