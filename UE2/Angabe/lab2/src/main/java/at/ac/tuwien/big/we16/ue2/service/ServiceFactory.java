package at.ac.tuwien.big.we16.ue2.service;

public abstract class ServiceFactory {
    private static NotifierService notifierService;

    public static NotifierService getNotifierService() {
        if (notifierService == null) {
            notifierService = new NotifierService();
        }
        return notifierService;
    }

}
