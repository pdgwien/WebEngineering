package at.ac.tuwien.big.we16.ue2.controller;

import at.ac.tuwien.big.we16.ue2.model.User;
import at.ac.tuwien.big.we16.ue2.model.UserStorage;
import at.ac.tuwien.big.we16.ue2.model.UserStorageFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginController extends HttpServlet {

    public LoginController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dis = getServletContext().getRequestDispatcher("/views/login.jsp");
        dis.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //TODO: Register Session

        HttpSession session = request.getSession(true);

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserStorage userStorage = UserStorageFactory.getUserStorage();

        User user = userStorage.getUserByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setBalance(150000);
            String[] emailParts = email.split("@");
            user.setFirstName(emailParts[0]);
            user.setLastName(emailParts[1]);
            user.setCurrentAuctions(0);
            user.setLostAuctions(0);
            user.setWonAuctions(0);
            userStorage.addUser(user);
        }
        session.setAttribute("user", user);

        response.sendRedirect("/overview");
    }
}
