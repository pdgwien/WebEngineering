package at.ac.tuwien.big.we16.ue2.service;

import at.ac.tuwien.big.we16.ue2.model.Auction;
import at.ac.tuwien.big.we16.ue2.model.AuctionStorage;
import at.ac.tuwien.big.we16.ue2.model.AuctionStorageFactory;

/**
 * Created by Patrick on 19/04/16.
 */
public class FindExpiredAuctionsJob implements Runnable {
    private AuctionStorage auctionStorage;
    private NotifierService notifierService;

    public FindExpiredAuctionsJob() {
        this.auctionStorage = AuctionStorageFactory.getAuctionStorage();
        this.notifierService = ServiceFactory.getNotifierService();
    }

    @Override
    public void run() {
        this.auctionStorage.getAuctions().stream().filter(auction -> !auction.isNotified() && auction.isExpired())
                .forEach(this::updateData);
    }

    private void updateData(Auction auction) {
        auction.setNotified(true);
        if (auction.getHighestBidder() != null) {
            auction.getBidders().forEach(user -> {
                user.setCurrentAuctions(user.getCurrentAuctions() - 1);
            });
            auction.getBidders().remove(auction.getHighestBidder());
            auction.getBidders().stream().forEach(user -> {
                user.setLostAuctions(user.getLostAuctions() + 1);
            });
            auction.getHighestBidder().setWonAuctions(auction.getHighestBidder().getWonAuctions() + 1);
        }
        this.notifierService.broadcastPersonalExpiry(auction);
    }
}
