package at.ac.tuwien.big.we16.ue4.websocket;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import at.ac.tuwien.big.we16.ue4.service.NotifierService;

@ServerEndpoint(value="/socket", configurator = BigBidConfigurator.class)
public class BigBidEndpoint {
    private final NotifierService notifierService;

    public BigBidEndpoint(NotifierService notifierService) {
        this.notifierService = notifierService;
    }

    @OnOpen
    public void onOpen(Session socketSession, EndpointConfig config) {
        this.notifierService.register(socketSession, (HttpSession) config.getUserProperties().get(HttpSession.class.getName()));
    }

    @OnClose
    public void onClose(Session socketSession) {
        this.notifierService.unregister(socketSession);
    }
    

    @WebFilter("/socket")
    public static class MyFilter implements Filter {
    	   @Override
    	    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    	            throws IOException, ServletException {
    	        HttpServletRequest httpRequest = (HttpServletRequest) request;
    	        httpRequest.getSession();
    	        chain.doFilter(httpRequest, response);
    	    }

    	    public void init(FilterConfig config) throws ServletException { }
    	    public void destroy() { }
    }
}