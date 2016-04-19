package at.ac.tuwien.big.we16.ue2.websocket;

import at.ac.tuwien.big.we16.ue2.service.NotifierService;
import at.ac.tuwien.big.we16.ue2.service.ServiceFactory;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * This is a modified WebSocket server endpoint configurator which
 * a) modifies the handshake to save the HTTP session with the socket session
 * b) injects the NotifierService into all created endpoint instances
 */
public class BigBidConfigurator extends ServerEndpointConfig.Configurator {
    private NotifierService notifierService;

    public BigBidConfigurator() {
        this.notifierService = ServiceFactory.getNotifierService();
    }

    /**
     * From http://stackoverflow.com/a/17994303
     * <p>
     * Saves a reference to the HttpSession in the socket's session.
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        config.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }

    /**
     * Useful: http://www.programmingforliving.com/2013/08/websocket-tomcat-8-ServerEndpointConfig-Configurator.html
     * <p>
     * Injects the notifier service into the socket endpoint.
     */
    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return (T) (new BigBidEndpoint(this.notifierService));
    }
}
