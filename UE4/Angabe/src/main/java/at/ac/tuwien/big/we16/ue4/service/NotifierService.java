package at.ac.tuwien.big.we16.ue4.service;

import at.ac.tuwien.big.we16.ue4.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue4.model.Bid;
import at.ac.tuwien.big.we16.ue4.model.Product;
import at.ac.tuwien.big.we16.ue4.model.User;
import twitter4j.TwitterException;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NotifierService {
    private static Map<Session, HttpSession> clients = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executor;
    private final TwitterService twitterService;
    private final RESTService restService;

    public NotifierService(TwitterService twitterService, RESTService restService) {
        this.twitterService = twitterService;
        this.restService = restService;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.startExpiredProductsThread();
    }

    public void register(Session socketSession, HttpSession httpSession) {
        clients.put(socketSession, httpSession);
    }

    public void unregister(Session userSession) {
        clients.remove(userSession);
    }

    public void stop() {
        this.executor.shutdown();
    }

    public void notifyAllAboutBid(Bid bid) {
        this.sendToAllSockets(new NewBidVisitor(bid));
    }

    public void notifyReimbursement(User user) {
        this.sendToAllSockets(new ReimbursementVisitor(user));
    }

    private void startExpiredProductsThread() {
        executor.scheduleAtFixedRate(() -> {
            Collection<Product> newlyExpiredProducts = ServiceFactory.getProductService().checkProductsForExpiration();
            NotifierService.this.sendToTwitter(newlyExpiredProducts);
            if (!newlyExpiredProducts.isEmpty()) {
                NotifierService.this.sendToAllSockets(new ExpiredProductsVisitor(newlyExpiredProducts));
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void sendToTwitter(Collection<Product> newlyExpiredProducts) {
        newlyExpiredProducts.stream().filter(Product::hasBids).forEach(product -> {
            Bid highestBid = product.getHighestBid();
            String uuid = restService.sendToRESTAPI(product, highestBid);
            try {
                NotifierService.this.twitterService.publishUuid(new TwitterStatusMessage(highestBid.getUser().getFullName(), uuid, new Date()));
            } catch (TwitterException e) {
                // TODO
                e.printStackTrace();
            }
        });
    }

    private void sendToAllSockets(Visitor visitor) {
        for (Map.Entry<Session, HttpSession> client : clients.entrySet()) {
            try {
                Object sessionUser = client.getValue().getAttribute("user");
                if (sessionUser != null) {
                    User user = ServiceFactory.getUserService().getUserByEmail(((User) sessionUser).getEmail());
                    String text = visitor.getText(user);
                    if (text != null) {
                        client.getKey().getAsyncRemote().sendText(text);
                    }
                }
            } catch (IllegalStateException | UserNotFoundException e) {
                clients.remove(client.getKey());
            }
        }
    }

    private interface Visitor {
        String getText(User user);
    }

    private class NewBidVisitor implements Visitor {
        private final String text;

        public NewBidVisitor(Bid bid) {
            this.text = "{\"type\": \"newBid\", \"id\": \"" + bid.getProduct().getId() + "\", \"userFullName\": \"" + bid.getUser().getFullName() + "\", \"amount\": " + bid.getConvertedAmount() + "}";
        }

        public String getText(User user) {
            return this.text;
        }
    }

    private class ExpiredProductsVisitor implements Visitor {
        private final String expiredProductsText;

        public ExpiredProductsVisitor(Collection<Product> newlyExpiredProducts) {
            this.expiredProductsText = String.join(",", newlyExpiredProducts.stream().map(product -> "\"" + product.getId() + "\"").collect(Collectors.toList()));
        }

        public String getText(User user) {
            return "{\"type\": \"expiredProducts\", \"running\": " + user.getRunningAuctionsCount() + ", \"lost\": " + user.getLostAuctionsCount() + ", \"won\": " + user.getWonAuctionsCount() + ", \"balance\": " + user.getConvertedBalance() + ", \"expiredProducts\": [" + this.expiredProductsText + "]}";
        }
    }

    private class ReimbursementVisitor implements Visitor {
        private final User user;

        private ReimbursementVisitor(User user) {
            this.user = user;
        }

        @Override
        public String getText(User user) {
            if (this.user.equals(user)) {
                return "{\"type\": \"reimbursement\", \"balance\": " + this.user.getConvertedBalance() + "}";
            }
            return null;
        }
    }
}
