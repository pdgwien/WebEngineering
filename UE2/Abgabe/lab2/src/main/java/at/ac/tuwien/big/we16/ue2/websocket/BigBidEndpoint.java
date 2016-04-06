package at.ac.tuwien.big.we16.ue2.websocket;

import at.ac.tuwien.big.we16.ue2.service.NotifierService;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * This endpoint listens on the /socket URL.
 */
@ServerEndpoint(value="/socket", configurator = BigBidConfigurator.class)
public class BigBidEndpoint {
    private final NotifierService notifierService;

    public BigBidEndpoint(NotifierService notifierService) {
        this.notifierService = notifierService;
    }

    /**
     * When a new WebSocket connection is established, we register both the
     * socket session and the associated HTTP session with the notifier service.
     */
    @OnOpen
    public void onOpen(Session socketSession, EndpointConfig config) {
        this.notifierService.register(socketSession, (HttpSession) config.getUserProperties().get(HttpSession.class.getName()));
    }

    /**
     * When a socket connection is closed, we remove its session from the
     * notifier service.
     */
    @OnClose
    public void onClose(Session socketSession) {
        this.notifierService.unregister(socketSession);
    }
}