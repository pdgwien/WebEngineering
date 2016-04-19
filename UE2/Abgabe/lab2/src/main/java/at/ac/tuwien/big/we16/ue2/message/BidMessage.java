package at.ac.tuwien.big.we16.ue2.message;

/**
 * Created by Patrick on 19/04/16.
 */
public class BidMessage extends Message {
    private String userEmail;
    private long newBid;
    private String auction;

    public BidMessage(String userEmail, long newBid, String auction) {
        super("bid");
        this.userEmail = userEmail;
        this.newBid = newBid;
        this.auction = auction;
    }
}
