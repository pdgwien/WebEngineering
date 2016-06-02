package at.ac.tuwien.big.we16.ue4.service;

import at.ac.tuwien.big.we16.ue4.exception.InvalidBidException;
import at.ac.tuwien.big.we16.ue4.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue4.model.Bid;
import at.ac.tuwien.big.we16.ue4.model.Product;
import at.ac.tuwien.big.we16.ue4.model.User;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

public class BidService {
    private final PersistenceService persistenceService;

    public BidService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void makeBid(User user, Product product, int centAmount) throws InvalidBidException, UserNotFoundException {
        if (product.hasAuctionEnded() || !product.isValidBidAmount(centAmount) || !user.hasSufficientBalance(centAmount)) {
            throw new InvalidBidException();
        }

        // possible cases:
        // * product has no bids
        //   -> decrease balance by total, increment running
        // * product's highest bid is by the user
        //   -> decrease balance by diff, don't increment running
        // * some other bid on the product is by the user
        //   -> decrease balance by total, don't increment running, reimburse the current highest bidder
        // * product has bids, but none by the user
        //   -> decrease balance by total, increment running, reimburse the current highest bidder

        EntityManager em = this.persistenceService.getEntityManager();
        em.getTransaction().begin();

        int decreaseAmount = centAmount;
        User highestBidder = null;

        if (product.hasBids()) {
            if (product.getHighestBid().isBy(user)) {
                // The given user already is the highest bidder, so we only substract the difference.
                decreaseAmount = centAmount - product.getHighestBid().getAmount();
            }
            else {
                // We need to get a managed entity
                highestBidder = ServiceFactory.getUserService().getUserByEmail(product.getHighestBid().getUser().getEmail());
                // Reimburse previous highest bidder.
                highestBidder.increaseBalance(product.getHighestBid().getAmount());
                ServiceFactory.getNotifierService().notifyReimbursement(highestBidder);
            }
        }

        if (!product.hasBidByUser(user)) {
            user.incrementRunningAuctions();
        }

        user.decreaseBalance(decreaseAmount);
        Bid bid = new Bid(centAmount, user);
        product.addBid(bid);

        em.persist(bid);
        em.merge(product);
        em.merge(user);
        if (highestBidder != null) {
            em.merge(highestBidder);
        }
        em.getTransaction().commit();
        em.close();

        bid.setProduct(product);
        ServiceFactory.getNotifierService().notifyAllAboutBid(bid);
        ServiceFactory.getNotifierService().notifyReimbursement(user);
    }

    public void makeBid(User user, Product product, BigDecimal amount) throws InvalidBidException, UserNotFoundException {
        try {
            int centAmount = amount.movePointRight(2).intValueExact();
            this.makeBid(user, product, centAmount);
        } catch (ArithmeticException e) {
            throw new InvalidBidException();
        }
    }
}
