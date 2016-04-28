package at.ac.tuwien.big.we16.ue3.service;

public abstract class ServiceFactory {
    private static ProductService productService;
    private static NotifierService notifierService;
    private static ComputerUserService computerUserService;
    private static UserService userService;

    public static ProductService getProductService() {
        if (productService == null) {
            productService = new ProductService();
        }
        return productService;
    }

    public static NotifierService getNotifierService() {
        if (notifierService == null) {
            notifierService = new NotifierService();
        }
        return notifierService;
    }

    public static ComputerUserService getComputerUserService() {
        if (computerUserService == null) {
            computerUserService = new ComputerUserService(
                    new BidService(),
                    getProductService()
            );
        }
        return computerUserService;
    }

    public static UserService getUserService() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }
}
