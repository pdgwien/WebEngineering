package at.ac.tuwien.big.we16.ue2.controller;


import at.ac.tuwien.big.we16.ue2.message.NewBidResponseMessage;
import at.ac.tuwien.big.we16.ue2.model.Auction;
import at.ac.tuwien.big.we16.ue2.model.AuctionStorage;
import at.ac.tuwien.big.we16.ue2.model.AuctionStorageFactory;
import at.ac.tuwien.big.we16.ue2.model.User;
import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class BidController extends HttpServlet{

    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        System.out.println("Test GET");
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        AuctionStorage auctionStorage = AuctionStorageFactory.getAuctionStorage();
        String auctionName = request.getParameter("auctionName");
        Auction auction = auctionStorage.getAuctionByName(auctionName);

        String bidString = request.getParameter("new-price");

        //Escape the escape-character to escape the meta-character in regex
        String[] bidStrings = bidString.split("\\.");


        long long1 = Long.parseLong(bidStrings[0])*100;
        long long2 = 0;
        if(bidStrings.length==2){
            long2 = Long.parseLong(bidStrings[1]);
        }


        long newBid = long1 + long2;

        boolean success = auction.bid(user, newBid);
        long newBalance = user.getBalance();
        int auctionCount = user.getCurrentAuctions();
        String highestBidder = user.getFullName();


        NewBidResponseMessage m = new NewBidResponseMessage(success,newBalance, auctionCount, highestBidder, newBid);

        Gson gson = new Gson();
        String responseText = gson.toJson(m);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseText);
        out.flush();
;    }
}
