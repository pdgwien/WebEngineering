package at.ac.tuwien.big.we16.ue4.controller;

import at.ac.tuwien.big.we16.ue4.error.FormError;
import at.ac.tuwien.big.we16.ue4.model.User;
import at.ac.tuwien.big.we16.ue4.service.AuthService;
import at.ac.tuwien.big.we16.ue4.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    public void getRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//TODO: Try to register and login if successful, send back login information
        if (this.authService.isLoggedIn(request.getSession())) {
            response.sendRedirect("/");
            return;
        }
        request.setAttribute("error", new FormError());
        request.getRequestDispatcher("/views/registration.jsp").forward(request, response);
    }

    public void postRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        FormError formError = new FormError();
        Date date = userService.parseDate(request.getParameter("dateofbirth"));
        if (date == null) {
            formError.setDateFormatError(true);
        }
        User user = new User(
                request.getParameter("salutation"),
                request.getParameter("firstname"),
                request.getParameter("lastname"),
                request.getParameter("email"),
                request.getParameter("password"),
                date
        );
        //TODO: Instead of selecting a view, just send back login data
        formError = this.userService.createUser(user, formError);
        if (formError.isAnyError()) {
            request.setAttribute("error", formError);
            request.getRequestDispatcher("/views/registration.jsp").forward(request, response);
        } else {
            this.authService.login(request.getSession(), user);
            response.sendRedirect("/");
        }
    }

}
