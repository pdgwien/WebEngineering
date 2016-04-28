package at.ac.tuwien.big.we16.ue3.websocket;

import at.ac.tuwien.big.we16.ue3.service.NotifierService;
import at.ac.tuwien.big.we16.ue3.service.ServiceFactory;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class BigBidConfigurator extends ServerEndpointConfig.Configurator {
    private NotifierService notifierService;

    public BigBidConfigurator() {
        this.notifierService = ServiceFactory.getNotifierService();
    }

    /**
     * From http://stackoverflow.com/a/17994303
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response)
    {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        config.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }

    /**
     * Useful: http://www.programmingforliving.com/2013/08/websocket-tomcat-8-ServerEndpointConfig-Configurator.html
     */
    @Override
     public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return (T) (new BigBidEndpoint(this.notifierService));
    }
}
