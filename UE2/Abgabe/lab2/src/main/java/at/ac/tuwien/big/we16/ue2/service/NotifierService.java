package at.ac.tuwien.big.we16.ue2.service;

import at.ac.tuwien.big.we16.ue2.message.AuctionExpiredMessage;
import at.ac.tuwien.big.we16.ue2.message.Message;
import at.ac.tuwien.big.we16.ue2.model.Auction;
import at.ac.tuwien.big.we16.ue2.model.User;
import com.google.gson.Gson;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class NotifierService {
    private static Map<Session, HttpSession> clients = new ConcurrentHashMap<>();
    private static Gson gson = new Gson();
    private final ScheduledExecutorService executor;

    public NotifierService() {
        // Use the scheduled executor to regularly check for recently expired auctions
        // and send a notification to all relevant users.
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void broadcast(Message message) {
        for (Session user : clients.keySet()) {
            try {
                user.getBasicRemote().sendText(gson.toJson(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(User user, Message message) {
        clients.entrySet().stream().filter(c -> c.getValue().getAttribute("user") == user).forEach(c -> {
            try {
                c.getKey().getBasicRemote().sendText(gson.toJson(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void broadcastPersonalExpiry(Auction auction) {
        for (Map.Entry<Session, HttpSession> entry : clients.entrySet()) {
            User user = (User) entry.getValue().getAttribute("user");
            try {
                entry.getKey().getBasicRemote().sendText(gson.toJson(new AuctionExpiredMessage(auction.getName(), user.getCredit(),
                        user.getCurrentAuctions(), user.getLostAuctions(), user.getWonAuctions())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used by the WebSocket endpoint to save a reference to all
     * connected users. A list of connections is needed so that the users can be
     * notified about events like new bids and expired auctions (see
     * assignment). We need the socket session so that we can push data to the
     * client. We need the HTTP session to find out which user is currently
     * logged in in the browser that opened the socket connection.
     */
    public void register(Session socketSession, HttpSession httpSession) {
        clients.put(socketSession, httpSession);
    }

    public void unregister(Session userSession) {
        clients.remove(userSession);
    }

    /**
     * Call this method from your servlet's shutdown listener to cleanly
     * shutdown the thread when the application stops.
     * <p>
     * See http://www.deadcoderising.com/execute-code-on-webapp-startup-and-shutdown-using-servletcontextlistener/
     */
    public void stop() {
        this.executor.shutdown();
    }
}
