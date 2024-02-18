package org.example;

import org.example.functional.AI;
import org.example.functional.*;
import org.example.webSocket.EventClient;
import org.example.webSocket.EventServer;

import java.nio.channels.ClosedChannelException;
import java.util.Scanner;

public class Runner {
    public static void main(String[] args) throws Exception {
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
        AI ai = new AI();
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();
        System.out.println("Хотите ли пользоваться подсказками?\n1. Да. \n2. Нет.");
        String hints = scanner.nextLine();
        String difficulty = null;
        if ("1".equals(hints)) {
            System.out.println("Выберите уровень умности подсказок:\n1. Лёгкий.\n2. Сложный.");
            difficulty = scanner.nextLine();
        }
        System.out.println(name + ", как вы хотите расставить корабли:\n1. Самостоятельно через txt файл.\n2. Рандомно.");
        String way = scanner.nextLine();
        String fileName1 = null;
        if (way.equals("1")) {
            System.out.print("Введите имя файла по которому будут расставляться корабли: ");
            fileName1 = scanner.nextLine();
        }
        game.initPlayer1(fileName1, name).setBot("1".equals(hints)).setAI("1".equals(difficulty) ? null : ai);



        System.out.println("Выберите режим игры:\n1. Одиночная. \n2. С напарником.");
        String mode = scanner.nextLine();
        switch (mode) {
            case "1" -> {
                System.out.println("Выберите уровень сложности бота:\n 1. Лёгкий.\n 2. Сложный.");
                difficulty = scanner.nextLine();
                switch (difficulty) {
                    case "1" -> {
                        System.out.println("Выбран режим игры с лёгким ботом");
                        game.initPlayer2("ships2.txt", "bot").setBot(true);
                    }
                    case "2" -> {
                        System.out.println("Выбран режим игры со сложным ботом");
                        game.initPlayer2(null, "bot").setBot(true).setAI(ai);
                    }
                    default -> System.out.println("Некорректный ввод!");
                }
                System.out.println("Победил игрок: " + game.play());
            }
            case "2" -> {
                System.out.println("Выбран режим игры с напарником");
                System.out.println("Выберите с кем будете играть: \n1. С игроком за одним пк. \n2. С игроком по локальной сети.");
                String way1 = scanner.nextLine();
                if (way1.equals("1")) {
                    System.out.println("Выбран режим игры с игроком за одним пк.");
                    System.out.print("Введите имя второго игрока: ");
                    String name2 = scanner.nextLine();
                    System.out.println(name2 + ", хотите пользоваться подсказками?\n1. Да \n2. Нет");
                    String hint2 = scanner.nextLine();
                    String difficulty2 = null;
                    if ("1".equals(hint2)) {
                        System.out.println(name2 + ", Выберите уровень умности подсказок для второго игрока:\n1. Лёгкий.\n2. Сложный.");
                        difficulty2 = scanner.nextLine();
                    }
                    System.out.println(name2 + ", как вы хотите расставить корабли:\n1. Самостоятельно через txt файл.\n2. Рандомно.");
                    String way2 = scanner.nextLine();
                    String fileName2 = null;
                    if ("1".equals(way2)) {
                        System.out.print(name2 + ", Введите имя файла по которому будут расставляться корабли второго игрока: ");
                        fileName2 = scanner.nextLine();
                    }

                    game.initPlayer2(fileName2, name2).setBot("1".equals(hint2)).setAI("1".equals(difficulty2) ? null: ai);

                    System.out.println("Победил игрок: " + game.play());
                } else {
                    System.out.println("Выбран режим по сети");
                    System.out.println("1. Создать игру. \n2. Подключиться к игре.");
                    String mode3 = scanner.nextLine();
                    switch (mode3) {
                        case "1" -> {
                            EventServer eventServer = new EventServer();
                            eventServer.setPort(8080);
                            eventServer.start();
                            game.initClientFromNetwork(eventServer);
                            game.networkPlay(true);
                            try {
                                Thread.sleep(1000L);
                                eventServer.stop();
                            } catch (ClosedChannelException e) {
                                System.out.println("Соединение принудительно закрыто");
                            }
                        }
                        case "2" -> {
                            System.out.println("Введите IP адрес для подключения (если хотите задать автоматически нажмите Enter localhost:8080)");
                            String ip = scanner.nextLine();
                            if (ip.isEmpty()) {
                                ip = "localhost:8080";
                            }
                            EventClient eventClient = new EventClient();
                            game.initServerFromNetwork(eventClient, ip);
                            game.networkPlay(false);

                            eventClient.close();
                        }
                        default -> System.out.println("Некорректный ввод!");
                    }
                }
            }
            default -> System.out.println("Некорректный ввод!");
        }

        //game.initPlayer1("ships.txt", "Вася").setBot(true).setAI(ai);
        //game.initPlayer2("ships2.txt", "Петя").setBot(true);

        //game.initPlayer1("ships.txt", "Вася").setBot(true).setAI(new AI());
        //game.initPlayer2("ships2.txt", "Петя").setBot(true).setAI(new AI());
    }
}