package at.ac.tuwien.big.we16.ue3.servlet;

import at.ac.tuwien.big.we16.ue3.controller.AuthController;
import at.ac.tuwien.big.we16.ue3.controller.ProductController;
import at.ac.tuwien.big.we16.ue3.controller.UserController;
import at.ac.tuwien.big.we16.ue3.exception.ProductNotFoundException;
import at.ac.tuwien.big.we16.ue3.exception.RequestException;
import at.ac.tuwien.big.we16.ue3.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue3.productdata.DataGenerator;
import at.ac.tuwien.big.we16.ue3.service.AuthService;
import at.ac.tuwien.big.we16.ue3.service.ServiceFactory;
import at.ac.tuwien.big.we16.ue3.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BigBidServlet extends HttpServlet {
    private static final String LOGIN_PATH = "/login";
    private static final String REGISTRATION_PATH = "/registration";
    private static final String LOGOUT_PATH = "/logout";
    private static final String OVERVIEW_PATH = "/";
    private static final String UUID_REGEX = "([0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12})";
    private static final String DETAILS_PATH = "/product/" + UUID_REGEX;
    private static final String BID_PATH = "/product/" + UUID_REGEX + "/bid";

    private ProductController productController;
    private AuthController authController;
    private UserController userController;
    private AuthService authService;

    @Override
    public void init() {
        UserService userService = ServiceFactory.getUserService();
        this.authService = new AuthService(userService);
        this.productController = new ProductController(ServiceFactory.getProductService(), this.authService, ServiceFactory.getBidService());
        this.authController = new AuthController(this.authService);
        this.userController = new UserController(userService, this.authService);
        (new DataGenerator()).generateData();
        try {
             ServiceFactory.getComputerUserService().start(userService.getUserByEmail("siri@apple.com"));
        } catch (UserNotFoundException e) {
            // ignore
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = this.getPath(request);
        this.noCacheHeaders(response);

        try {
            if (this.checkAuth(request, response, path)) {
                return;
            }

            // The product details path requires regex matching and thus cannot be included in the switch-statement
            if (this.productDetails(request, response, path)) {
                return;
            }

            switch (path) {
                case LOGIN_PATH:
                    this.authController.getLogin(request, response);
                    break;
                case LOGOUT_PATH:
                    this.authController.getLogout(request, response);
                    break;
                case REGISTRATION_PATH:
                    this.userController.getRegister(request, response);
                    break;
                case OVERVIEW_PATH:
                    this.productController.getOverview(request, response);
                    break;
                default:
                    response.sendError(404);
            }
        } catch (RequestException e) {
            response.sendError(e.getCode());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = this.getPath(request);

        try {
            if (this.checkAuth(request, response, path)) {
                return;
            }

            // The product bid path requires regex matching and thus cannot be included in the switch-statement
            if (this.productBid(request, response, path)) {
                return;
            }

            switch (path) {
                case LOGIN_PATH:
                    this.authController.postLogin(request, response);
                    break;
                case REGISTRATION_PATH:
                        this.userController.postRegister(request, response);
                    break;
                default:
                    response.sendError(404);
            }
        } catch (RequestException e) {
            response.sendError(e.getCode());
        }
    }

    /**
     * Checks whether the given path matches the bid-URL-format. If it does, extract the product id from
     * the URL, call the product controller and return true. Otherwise, return false.
     *
     * @return True if the path matches the bid-URL-format.
     * @throws ServletException
     * @throws IOException
     * @throws ProductNotFoundException
     * @throws UserNotFoundException
     */
    private boolean productBid(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException, ProductNotFoundException, UserNotFoundException {
        Pattern pattern = Pattern.compile(BID_PATH);
        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            this.productController.postBid(request, response, matcher.group(1));
            return true;
        }
        return false;
    }

    /**
     * Checks whether the given path matches the detail-page URL-format. If it does, extract the product id from
     * the URL, call the product controller and return true. Otherwise, return false.
     *
     * @return True if the path matches the detail-page URL-format.
     * @throws ServletException
     * @throws IOException
     */
    private boolean productDetails(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException, ProductNotFoundException {
        Pattern pattern = Pattern.compile(DETAILS_PATH);
        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            this.productController.getDetails(request, response, matcher.group(1));
            return true;
        }
        return false;
    }

    /**
     * Extract the path from the given request. The path is used for deciding which controller and action should
     * handle the request.
     *
     * @return The path as needed by doGet and doPost.
     */
    private String getPath(HttpServletRequest request) {
        return request.getServletPath();
    }

    /**
     * Check if the given path may be accessed by unauthenticated users.
     *
     * @return True if the given path may be accessed by unauthenticated users.
     */
    private boolean isPublicPath(String path) {
        return path.equals(LOGIN_PATH) || path.equals(REGISTRATION_PATH);
    }

    /**
     * Checks whether the given request comes from an authenticated user. If not, the response is set to redirect
     * to the login page.
     *
     * @return True if no user is authenticated for the given request.
     * @throws IOException
     */
    private boolean checkAuth(HttpServletRequest request, HttpServletResponse response, String path) throws IOException, UserNotFoundException {
        if (!this.authService.isLoggedIn(request.getSession()) && !this.isPublicPath(path)) {
            response.sendRedirect(request.getContextPath()+LOGIN_PATH);
            return true;
        }
        request.setAttribute("user", this.authService.getUser(request.getSession()));
        return false;
    }

    /**
     * Write headers to the given response which stop browsers from caching a page.
     * See http://cristian.sulea.net/blog.php?p=2014-01-14-disable-browser-caching-with-meta-html-tags
     */
    private void noCacheHeaders(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}