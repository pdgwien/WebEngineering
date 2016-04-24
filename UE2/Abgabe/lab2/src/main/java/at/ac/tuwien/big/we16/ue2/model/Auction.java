package at.ac.tuwien.big.we16.ue2.model;

import at.ac.tuwien.big.we16.ue2.message.BalanceMessage;
import at.ac.tuwien.big.we16.ue2.message.BidMessage;
import at.ac.tuwien.big.we16.ue2.service.NotifierService;
import at.ac.tuwien.big.we16.ue2.service.ServiceFactory;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Auction {
    private long highestBid;
    private Date expirationDate;
    private User highestBidder;
    private String name;
    private String image;
    private boolean notified = false;
    private Set<User> bidders = new HashSet<>();
    private NotifierService notifierService;

    public Auction() {
        this.notifierService = ServiceFactory.getNotifierService();
    }

    public boolean bid(User user, long amount) {
        if (!this.isExpired()) {
            if (amount > this.getHighestBid()) {
                if (user.getBalance() >= amount) {
                    user.setBalance(user.getBalance() - amount);
                    if (this.getHighestBidder() != null) {
                        this.getHighestBidder().setBalance(this.getHighestBidder().getBalance() + this.getHighestBid());
                    }
                    this.setHighestBid(amount);
                    if (this.getHighestBidder() != null) {
                        notifierService.send(this.getHighestBidder(), new BalanceMessage(this.getHighestBidder().getBalance()));
                    }
                    this.setHighestBidder(user);
                    notifierService.broadcast(new BidMessage(user.getFullName(), amount, this.getName()));
                    if (bidders.add(user)) {
                        user.setCurrentAuctions(user.getCurrentAuctions() + 1);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public String displayHighestBid() {
        DecimalFormat decim = new DecimalFormat("0.00");
        return decim.format(highestBid/100.0);
    }

    public long getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(long highestBid) {
        this.highestBid = highestBid;
    }

    /*
    @return The expiration date, format: "yyyy,MM,dd,HH,mm,ss,sss"
     */
    public String getExpirationDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss,sss");
        return sdf.format(expirationDate);
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isExpired() {
        return expirationDate.before(new Date());
    }

    public User getHighestBidder() {
        return highestBidder;
    }

    public void setHighestBidder(User highestBidder) {
        this.highestBidder = highestBidder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public Set<User> getBidders() {
        return bidders;
    }
}
