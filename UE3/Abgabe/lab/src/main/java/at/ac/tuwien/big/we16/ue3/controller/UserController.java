package at.ac.tuwien.big.we16.ue3.controller;

import at.ac.tuwien.big.we16.ue3.model.User;
import at.ac.tuwien.big.we16.ue3.service.AuthService;
import at.ac.tuwien.big.we16.ue3.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    public void getRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (this.authService.isLoggedIn(request.getSession())) {
            response.sendRedirect("/");
            return;
        }


        request.setAttribute("errors", this.createErrors());
        request.getRequestDispatcher("/views/registration.jsp").forward(request, response);
    }

    // TODO validation of user data

    /**
     * Edited by Fabian Wurm on 19-05-2016
     */
    public void postRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        boolean success = true;

        //The bean used to pass error messages to the jsp
        Map<String,String> errors = this.createErrors();

        //get form values from request
        String salutation = request.getParameter("salutation");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String dateString = request.getParameter("dateofbirth");
        LocalDate dateOfBirth = LocalDate.now(); //Initialization has no meaning
        String email = request.getParameter("email");
        String password = request.getParameter("password");


        //Check all form values
        if(firstname == null || firstname.equals("")){
            errors.put("firstname"," Vorname darf nicht leer sein");
            success = false;
        }

        if(lastname == null || lastname.equals("")){
            errors.put("lastname"," Nachname darf nicht leer sein");
            success = false;
        }

        if(dateString == null || dateString.equals("")){
            errors.put("dateOfBirth","Datum darf nicht leer sein");
            success = false;
        }else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                dateOfBirth = LocalDate.parse(dateString, formatter);
                LocalDate curDate = LocalDate.now();
                int age = Period.between(dateOfBirth, curDate).getYears();
                if (age < 18) {
                    errors.put("dateOfBirth"," Sie müssen mindestens 18 Jahre alt sein");
                    success = false;
                }
            } catch (Exception e) {
                errors.put("dateOfBirth"," nicht korrektes Datum");
                success = false;
            }
        }

        if(email == null || email.equals("")){
            errors.put("email"," Email darf nicht leer sein");
            success = false;
        }else if(!email.matches("^\\S+@\\S+\\.\\S+$")){
            errors.put("email"," Email Format inkorrekt");
            success = false;
        }

        if(password == null || password.equals("")){
            errors.put("password","Passwort muss zwischen 4 und 8 Zeichen besitzen");
            success = false;
        }

        //if all form fields are validated then the user is persisted
        if(success){
            User user = new User();
            user.setSalutation(salutation);
            user.setFirstname(firstname);
            user.setLastname(lastname);
            user.setEmail(email);
            user.setPassword(password);
            //Conversion from java.time.LocalDate to java.util.Date
            user.setDate(Date.from(dateOfBirth.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            user.setBalance(150000);

            this.userService.createUser(user);
            this.authService.login(request.getSession(), user);
            response.sendRedirect("/");
        }else{
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/views/registration.jsp").forward(request, response);
        }


    }

    /**
     * Initializes the error bean with empty strings
     * @return the initialized error been
     */
    private Map<String, String> createErrors(){
        Map<String,String> errors = new HashMap<>();

        errors.put("firstname","");
        errors.put("lastname","");
        errors.put("dateOfBirth","");
        errors.put("email","");
        errors.put("password","");
        return errors;
    }

}
