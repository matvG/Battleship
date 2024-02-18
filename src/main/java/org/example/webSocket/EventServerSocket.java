package org.example.webSocket;

import org.example.functional.Game;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/events/")
public class EventServerSocket {
    private final CountDownLatch closureLatch = new CountDownLatch(1);

    private static boolean clientIsConnected = false;

    private static Session session;

    @OnOpen
    public void onWebSocketConnect(Session sess) throws IOException {
        System.out.println("Socket Connected: " + sess);
        session = sess;
        clientIsConnected = true;
    }


    @OnMessage
    public void onWebSocketText(Session sess, String message) throws IOException {
        notify(message);
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

    public void awaitClosure() throws InterruptedException {
        System.out.println("Awaiting closure from remote");
        closureLatch.await();
    }

    public boolean clientIsConnected() {
        return clientIsConnected;
    }

    public void sendMessage(String message) throws IOException {
        if (session != null) {
            System.out.println("Отправляем клиенту -> " + message);
            session.getBasicRemote().sendText(message);
        } else {
            System.out.println("Сообщение [" + message + "] не отправлено");
        }
    }

    public static Session getSession() {
        if (session != null) {
            return session;
        }
        return null;
    }

    public static boolean getConnection() {
        return clientIsConnected;
    }

    public void notify(String message) {
        Game.gameTakeMessage(message);
    }
}

