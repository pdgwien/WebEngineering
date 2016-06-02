package at.ac.tuwien.big.we16.ue4.service;

public abstract class ServiceFactory {
    private static ProductService productService;
    private static NotifierService notifierService;
    private static PersistenceService persistenceService;
    private static UserService userService;
    private static ComputerUserService computerUserService;

    /**
     * The product service is needed by the servlet and the websocket. Thus we need a central factory where both can
     * acquire the same instance.
     */
    public static ProductService getProductService() {
        if (productService == null) {
            productService = new ProductService(getPersistenceService());
        }
        return productService;
    }

    /**
     * The notifier service is needed by the websocket and the shutdown listener.
     */
    public static NotifierService getNotifierService() {
        if (notifierService == null) {
            notifierService = new NotifierService(new TwitterService(), new RESTService());
        }
        return notifierService;
    }

    /**
     * The computer user service is needed by the servlet and the shutdown listener.
     */
    public static ComputerUserService getComputerUserService() {
        if (computerUserService == null) {
            computerUserService = new ComputerUserService(
                    new BidService(getPersistenceService()),
                    getProductService()
            );
        }
        return computerUserService;
    }

    /**
     * The persistence service is needed by other services and the shutdown listener.
     */
    public static PersistenceService getPersistenceService() {
        if (persistenceService == null) {
            persistenceService = new PersistenceService();
        }
        return persistenceService;
    }

    /**
     * The persistence service is needed by the servlet and the notifier.
     */
    public static UserService getUserService() {
        if (userService == null) {
            userService = new UserService(getPersistenceService());
        }
        return userService;
    }
}
