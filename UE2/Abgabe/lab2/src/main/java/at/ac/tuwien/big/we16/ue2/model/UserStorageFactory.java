package at.ac.tuwien.big.we16.ue2.model;


/**
 * Created by Fabian on 15.04.2016.
 */
public class UserStorageFactory {

    private static UserStorage userStorage;

    public static UserStorage getUserStorage() {
        if (userStorage == null) {
            userStorage = new UserStorage();
        }
        return userStorage;
    }

}
