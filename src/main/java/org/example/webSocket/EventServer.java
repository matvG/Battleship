package org.example.webSocket;

import java.net.URI;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.javax.server.config.JavaxWebSocketServletContainerInitializer;

import javax.websocket.Session;

public class EventServer {

    private final Server server;
    private final ServerConnector connector;

    public EventServer() {
        server = new Server();
        connector = new ServerConnector(server);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        JavaxWebSocketServletContainerInitializer.configure(context, (servletContext, wsContainer) ->
        {
            wsContainer.setDefaultMaxTextMessageBufferSize(65535);
            wsContainer.setDefaultMaxSessionIdleTimeout(3600 * 1000);
            wsContainer.addEndpoint(EventServerSocket.class);
        });


    }

    public void setPort(int port) {
        connector.setPort(port);
    }

    public void start() throws Exception {
        server.start();
    }

    public URI getURI() {
        return server.getURI();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public void join() throws InterruptedException {
        System.out.println("Use Ctrl+C to stop server");
        server.join();
    }

    public Session getSession() {
        return EventServerSocket.getSession();
    }

    public boolean getConnection() {
        return EventServerSocket.getConnection();
    }
}