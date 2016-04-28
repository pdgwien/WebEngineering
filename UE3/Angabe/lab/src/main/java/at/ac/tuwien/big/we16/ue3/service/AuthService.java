package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.exception.InvalidCredentialsException;
import at.ac.tuwien.big.we16.ue3.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue3.model.User;

import javax.servlet.http.HttpSession;

public class AuthService {
    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public void  login(HttpSession session, String email, String password) throws InvalidCredentialsException {
        session.setAttribute("user", this.checkCredentials(email, password));
    }

    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    public User getUser(HttpSession session) throws UserNotFoundException {
        if (session.getAttribute("user") != null) {
            User sessionUser = (User) session.getAttribute("user");
            return this.userService.getUserByEmail(sessionUser.getEmail());
        }
        return null;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    private User checkCredentials(String email, String password) throws InvalidCredentialsException {
        try {
            User user = this.userService.getUserByEmail(email);
            if (!password.equals(user.getPassword())) {
                throw new InvalidCredentialsException();
            }
            return user;
        } catch (UserNotFoundException e) {
            throw new InvalidCredentialsException();
        }
    }

    public void login(HttpSession session, User user) {
        session.setAttribute("user", user);
    }
}
