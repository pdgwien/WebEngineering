package at.ac.tuwien.big.we16.ue2.service;

import at.ac.tuwien.big.we16.ue2.model.User;
import at.ac.tuwien.big.we16.ue2.model.UserStorageFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Patrick on 19/04/16.
 */
@WebListener
public class BackgroundJobManager implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        User computer = new User();
        computer.setEmail("robot@roboto.corp");
        computer.setCredit(999999999);
        computer.setFirstName("Mr.");
        computer.setLastName("Roboto");
        computer.setCurrentAuctions(0);
        computer.setLostAuctions(0);
        computer.setWonAuctions(0);
        UserStorageFactory.getUserStorage().addUser(computer);
        scheduler.scheduleAtFixedRate(new ComputerBidJob(computer), 0, 10, TimeUnit.SECONDS);
        User computer2 = new User();
        computer2.setEmail("siri@apple.com");
        computer2.setCredit(999999999);
        computer2.setFirstName("Ms.");
        computer2.setLastName("Siri");
        computer2.setCurrentAuctions(0);
        computer2.setLostAuctions(0);
        computer2.setWonAuctions(0);
        UserStorageFactory.getUserStorage().addUser(computer2);
        scheduler.scheduleAtFixedRate(new ComputerBidJob(computer2), 5, 10, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(new FindExpiredAuctionsJob(), 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }

}
