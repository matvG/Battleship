package org.example.webSocket;

import org.eclipse.jetty.util.component.LifeCycle;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

public class EventClient {

    private Session session;

    private EventClientSocket eventClientSocket;

    private WebSocketContainer container;

    public static void main(String[] args) {
        EventClient client = new EventClient();
        URI uri = URI.create("ws://localhost:8080/events/");
        try {
            client.run(uri);
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    public void run(URI uri) {
        container = ContainerProvider.getWebSocketContainer();
        try {
            eventClientSocket = new EventClientSocket();
            container.setDefaultMaxSessionIdleTimeout(3600 * 1000);
            session = container.connectToServer(eventClientSocket, uri);
        } catch (DeploymentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isConnected() {
        return EventClientSocket.isClientIsConnected();
    }

    public Session getSession() {
        return EventClientSocket.getSession();
    }

    public void close() throws IOException {
        try {
            eventClientSocket.awaitClosure();
            session.close();
        } catch (InterruptedException e) {
            System.out.println("Соединение принудительно закрыто");
        } finally {
            LifeCycle.stop(container);
        }
    }
}
