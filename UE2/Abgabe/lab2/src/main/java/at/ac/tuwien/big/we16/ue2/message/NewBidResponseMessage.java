package at.ac.tuwien.big.we16.ue2.message;

/**
 * Created by Fabian on 19.04.2016.
 */
public class NewBidResponseMessage extends Message {

    private boolean bidSuccess;
    private long newBalance;
    private int auctionCount;
    private String highestBidder;
    private long newBid;

    public NewBidResponseMessage(boolean bidSuccess, long newBalance, int auctionCount, String highestBidder, long newBid){
        super("newBidResponse");
        this.bidSuccess = bidSuccess;
        this.newBalance = newBalance;
        this.auctionCount = auctionCount;
        this.highestBidder = highestBidder;
        this.newBid = newBid;
    }

}
