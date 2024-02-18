package org.example.webSocket;

import java.util.Scanner;

public class ServerRunner {
    public static void main(String[] args) throws Exception {
        EventServer server = new EventServer();
        server.setPort(8080);
        server.start();
        EventServerSocket eventServerSocket = new EventServerSocket();
        while (!eventServerSocket.clientIsConnected()) {
            Thread.sleep(1000L);
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите координаты");
        String message = scanner.nextLine();
        eventServerSocket.sendMessage(message);
        server.join();
        server.stop();
    }
}
