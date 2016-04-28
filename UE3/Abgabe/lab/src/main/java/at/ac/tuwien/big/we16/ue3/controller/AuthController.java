package at.ac.tuwien.big.we16.ue3.controller;

import at.ac.tuwien.big.we16.ue3.exception.InvalidCredentialsException;
import at.ac.tuwien.big.we16.ue3.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public void getLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (this.authService.isLoggedIn(request.getSession())) {
            response.sendRedirect("./");
            return;
        }
        this.showLoginPage(request, response, false);
    }

    public void postLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (this.authService.isLoggedIn(request.getSession())) {
            response.sendRedirect("./");
            return;
        }
        try {
            this.authService.login(request.getSession(), request.getParameter("email"), request.getParameter("password"));
            response.sendRedirect("./");
        } catch (InvalidCredentialsException e) {
            this.showLoginPage(request, response, true);
        }
    }

    public void getLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.authService.logout(request.getSession());
        response.sendRedirect("./login");
    }

    private void showLoginPage(HttpServletRequest request, HttpServletResponse response, boolean error) throws ServletException, IOException {
        request.setAttribute("error", error);
        request.getRequestDispatcher("./views/login.jsp").forward(request, response);
    }
}
