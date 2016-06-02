package at.ac.tuwien.big.we16.ue4.service;

import at.ac.tuwien.big.we16.ue4.exception.InvalidBidException;
import at.ac.tuwien.big.we16.ue4.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue4.model.Product;
import at.ac.tuwien.big.we16.ue4.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ComputerUserService {
    private final BidService bidService;
    private final ProductService productService;
    private final Collection<ScheduledExecutorService> executors;
    private final Random random;

    public ComputerUserService(BidService bidService, ProductService productService) {
        this.bidService = bidService;
        this.productService = productService;
        this.executors = new ArrayList<>();
        this.random = new Random();
    }

    public void start(User user) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (Product product : ComputerUserService.this.productService.getAllProducts()) {
                    if (!product.hasAuctionEnded() && ComputerUserService.this.random.nextDouble() < 0.3) {
                        int currentHighest = product.hasBids() ? product.getHighestBid().getAmount() : 0;
                        try {
                            ComputerUserService.this.bidService.makeBid(
                                    user,
                                    product,
                                    currentHighest + ComputerUserService.this.random.nextInt(10000)
                            );
                        } catch (InvalidBidException | UserNotFoundException e) {
                            // ignore
                        }
                    }
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
        this.executors.add(executor);
    }

    public void stopAll() {
        for (ScheduledExecutorService executor : this.executors) {
            executor.shutdown();
        }
    }
}
