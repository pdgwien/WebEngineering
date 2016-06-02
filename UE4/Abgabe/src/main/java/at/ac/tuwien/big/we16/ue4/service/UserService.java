package at.ac.tuwien.big.we16.ue4.service;

import at.ac.tuwien.big.we16.ue4.error.FormError;
import at.ac.tuwien.big.we16.ue4.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue4.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class UserService {
    private final PersistenceService persistenceService;

    public UserService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public FormError createUser(User user, FormError err) { // throws PasswordHashingException {

        //Validate email uniqueness
        User u = null;
        try {
            u = getUserByEmail(user.getEmail());
        } catch (UserNotFoundException e){}
        if (u != null) {
            err.setMailError(true);
        }

        //Validate title
        if (user.getSalutation() == null || user.getSalutation().length() < 1)
            err.setTitleError(true);

        //Validate firstname
        if (user.getFirstname() == null || user.getFirstname().length() < 1)
            err.setFirstNameError(true);

        //Validate lastname
        if (user.getLastname() == null || user.getLastname().length() < 1)
            err.setLastNameError(true);

        //Validate date
        if (user.getDate() == null)
            err.setDateError(true);
        else {
            LocalDate currentDate = LocalDate.now();
            LocalDate userDate = user.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (currentDate.getYear() - userDate.getYear() < 18)
                err.setDateError(true);
            if (currentDate.getYear() - userDate.getYear() == 18 && currentDate.getDayOfYear() - userDate.getDayOfYear() < 0)
                err.setDateError(true);
        }

        //Validate email
        if (!user.getEmail().matches("^\\S+@\\S+\\.\\S+$"))
            err.setMailFormatError(true);

        //Validate password
        if (user.getPassword() == null || user.getPassword().length() < 4 || user.getPassword().length() > 12)
            err.setPasswordError(true);

        if (!err.isAnyError()) {
            EntityManager em = this.persistenceService.getEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            em.close();
        }
        return err;
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        EntityManager em = this.persistenceService.getEntityManager();
        em.getTransaction().begin();
        TypedQuery<User> query = em.createQuery("select u from User u where u.email = :email", User.class);
        query.setParameter("email", email);
        User user;
        try {
            user = query.getSingleResult();
            em.getTransaction().commit();
        } catch (NoResultException e) {
            throw new UserNotFoundException();
        } finally {
            em.close();
        }
        return user;
    }

    //Validation
    public Date parseDate(String strDate) {
        strDate = strDate.replaceAll( "[^\\d]", "/" );
        String[] dateFormat = {"dd/MM/yyyy","yyyy/MM/dd","yyyy/dd/MM","MM/dd/yyyy"};
        Date date = null;
        for(String actFormat : dateFormat) {
            if(isFormatValid(actFormat,strDate)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(actFormat);
                try {
                    return date = simpleDateFormat.parse(strDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return date;
    }

    private boolean isFormatValid(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(value);
            if (!value.equals(dateFormat.format(date))) {
                date = null;
            }
        } catch (ParseException e) {
            return false;
        }
        return date != null;
    }
}
