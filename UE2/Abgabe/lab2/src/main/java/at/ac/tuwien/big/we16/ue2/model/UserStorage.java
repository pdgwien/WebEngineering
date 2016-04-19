package at.ac.tuwien.big.we16.ue2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabian on 16.04.2016.
 */
public class UserStorage {

    private List<User> users;

    public UserStorage() {
        users = new ArrayList<User>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User getUserByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equals(email))
                return u;
        }
        return null;
    }

}
