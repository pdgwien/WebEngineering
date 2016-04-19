package at.ac.tuwien.big.we16.ue2.message;

/**
 * Created by Patrick on 19/04/16.
 */
public class AuctionExpiredMessage extends Message {
    private String auction;
    private long balance;
    private int currentAuctions;
    private int lostAuctions;
    private int wonAuctions;

    public AuctionExpiredMessage(String auction, long balance, int currentAuctions, int lostAuctions, int wonAuctions) {
        super("auctionExpired");
        this.auction = auction;
        this.balance = balance;
        this.currentAuctions = currentAuctions;
        this.lostAuctions = lostAuctions;
        this.wonAuctions = wonAuctions;
    }
}
