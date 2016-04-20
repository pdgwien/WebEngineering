package at.ac.tuwien.big.we16.ue2.message;

/**
 * Created by Patrick on 19/04/16.
 */
public class BidMessage extends Message {
    private String userFullName;
    private long newBid;
    private String auction;

    public BidMessage(String userFullName, long newBid, String auction) {
        super("bid");
        this.userFullName = userFullName;
        this.newBid = newBid;
        this.auction = auction;
    }
}
