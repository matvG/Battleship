package org.example.webSocket;

import org.example.functional.Game;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
public class EventClientSocket {
    private final CountDownLatch closureLatch = new CountDownLatch(1);

    private static boolean clientIsConnected = false;

    private static Session session;

    @OnOpen
    public void onWebSocketConnect(Session sess) throws IOException {
        session = sess;
        System.out.println("Socket Connected: " + sess);
        clientIsConnected = true;
    }


    @OnMessage
    public void onWebSocketText(Session sess, String message) throws IOException {
        notify(message);
    }

    private void notify(String message) {
        Game.gameTakeMessage(message);
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        System.out.println("Socket Closed: " + reason);
        clientIsConnected = false;
        closureLatch.countDown();
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }

    public static boolean isClientIsConnected() {
        return clientIsConnected;
    }

    public static Session getSession() {
        return session;
    }

    public void awaitClosure() throws InterruptedException {
        closureLatch.await();
    }
}
