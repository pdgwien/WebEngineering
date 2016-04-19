package at.ac.tuwien.big.we16.ue2.controller;

import at.ac.tuwien.big.we16.ue2.model.Auction;
import at.ac.tuwien.big.we16.ue2.model.AuctionStorage;
import at.ac.tuwien.big.we16.ue2.model.AuctionStorageFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Fabian on 16.04.2016.
 */
public class DetailsController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        AuctionStorage auctionStorage = AuctionStorageFactory.getAuctionStorage();

        String auctionName = request.getParameter("name");
        Auction auction = auctionStorage.getAuctionByName(auctionName);

        request.setAttribute("auction", auction);

        RequestDispatcher dis = getServletContext().getRequestDispatcher("/views/details.jsp");
        dis.forward(request, response);
    }
}
