package at.ac.tuwien.big.we16.ue4.controller;

import at.ac.tuwien.big.we16.ue4.exception.InvalidBidException;
import at.ac.tuwien.big.we16.ue4.exception.ProductNotFoundException;
import at.ac.tuwien.big.we16.ue4.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue4.model.Product;
import at.ac.tuwien.big.we16.ue4.model.User;
import at.ac.tuwien.big.we16.ue4.service.AuthService;
import at.ac.tuwien.big.we16.ue4.service.BidService;
import at.ac.tuwien.big.we16.ue4.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class ProductController {

    private final ProductService productService;
    private final AuthService authService;
    private final BidService bidService;

    public ProductController(ProductService productService, AuthService authService, BidService bidService) {
        this.productService = productService;
        this.authService = authService;
        this.bidService = bidService;
    }

    public void getOverview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("products", this.productService.getAllProducts());
        request.getRequestDispatcher("/views/overview.jsp").forward(request, response);
    }

    public void getDetails(HttpServletRequest request, HttpServletResponse response, String id) throws ServletException, IOException, ProductNotFoundException {

        request.setAttribute("product", this.productService.getProductById(id));
        request.getRequestDispatcher("/views/details.jsp").forward(request, response);
    }

    public void postBid(HttpServletRequest request, HttpServletResponse response, String id) throws ServletException, IOException, ProductNotFoundException, UserNotFoundException {
        Product product = this.productService.getProductById(id);
        BigDecimal amount = new BigDecimal(request.getParameter("amount"));
        User user = this.authService.getUser(request.getSession());
        String json;
        try {
            this.bidService.makeBid(user, product, amount);
            json = "{\"success\": true, \"amount\": " + amount.toPlainString() + ", \"name\": \"" + user.getFullName() + "\"" +
                    ", \"balance\": " + user.getConvertedBalance() + ", \"runningAuctions\": " + user.getRunningAuctionsCount() + "}";
        } catch (InvalidBidException e) {
            json = "{\"success\": false}";
        }
        this.respondWithJson(response,json);
    }

    private void respondWithJson(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}