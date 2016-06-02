package at.ac.tuwien.big.we16.ue4.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import at.ac.tuwien.big.we16.ue4.service.ServiceFactory;

@WebListener
public class ShutdownListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // do nothing
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServiceFactory.getNotifierService().stop();
        ServiceFactory.getPersistenceService().close();
        ServiceFactory.getComputerUserService().stopAll();
    }
}
