package at.ac.tuwien.big.we16.ue2.service;

import at.ac.tuwien.big.we16.ue2.model.Auction;
import at.ac.tuwien.big.we16.ue2.model.AuctionStorage;
import at.ac.tuwien.big.we16.ue2.model.AuctionStorageFactory;
import at.ac.tuwien.big.we16.ue2.model.User;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Patrick on 19/04/16.
 */
public class ComputerBidJob implements Runnable {
    static final double PROBABILITY = 0.3;
    private User computer;
    private AuctionStorage auctionStorage;

    public ComputerBidJob(User computer) {
        this.computer = computer;
        this.auctionStorage = AuctionStorageFactory.getAuctionStorage();
    }

    @Override
    public void run() {
        this.auctionStorage.getAuctions().stream().filter(auction -> auction.getHighestBidder() != this.computer && !auction.isExpired())
                .forEach(this::bidWithProbability);
    }

    private void bidWithProbability(Auction auction) {
        if (ThreadLocalRandom.current().nextDouble() <= PROBABILITY) {
            auction.bid(this.computer, auction.getHighestBid() + ThreadLocalRandom.current().nextInt(1, 100));
        }
    }
}
