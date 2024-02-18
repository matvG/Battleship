package org.example.functional;

import org.example.model.Ship;
import org.example.model.Shot;
import org.example.exception.AddAllShipsToBoardException;
import org.example.exception.ValidationException;
import org.example.model.FieldStatus;
import org.example.webSocket.EventClient;
import org.example.webSocket.EventServer;

import javax.websocket.Session;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {

    Player player1;

    Player player2;

    int mode;

    private Session session;

    private static String message;

    private String opponentName;

    public Game() {
        this.mode = 1;
    }

    public static void gameTakeMessage(String message) {
        Game.message = message;
    }

    public static String getMessage() {
        return message;
    }

    public void clearLastMessage() {
        message = null;
    }

    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Player initFromFile(String fileName, String name) throws ValidationException, AddAllShipsToBoardException {
        Board board = new Board();
        board.addAllShipsToBoard(validate(loadFromFile(fileName)));
        Player player = new Player(board, name);
        System.out.println(player.toString(1));
        return player;
    }

    public Player initFromRandom(String name) throws AddAllShipsToBoardException {
        Board board = new Board();
        ShipsRandom shipsRandom = new ShipsRandom();
        shipsRandom.fillBoard(board);
        System.out.println(board);
        Player player = new Player(board, name);
        System.out.println(player.toString(1));
        return player;
    }

    public void initServerFromNetwork(EventClient eventClient, String ip) throws InterruptedException {
        URI uri = URI.create("ws://" + ip + "/events/");
        try {
            eventClient.run(uri);
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
        System.out.print("Начинаем подключение к серверу...");
        while (!eventClient.isConnected()) {
            Thread.sleep(1000L);
        }
        session = eventClient.getSession();
        System.out.println("готово");
        sendMessage(player1.getName());
        System.out.print("Ждём имя сервера...");
        while (getMessage() == null) {
            Thread.sleep(1000L);
        }
        opponentName = getMessage();
        System.out.println("получили " + opponentName);
    }

    public void initClientFromNetwork(EventServer eventServer) throws InterruptedException {
        System.out.print("Ждём подключение клиента...");
        while (!eventServer.getConnection()) {
            Thread.sleep(1000L);
        }
        session = eventServer.getSession();
        System.out.println("готово");
        sendMessage(player1.getName());
        System.out.print("Ждём имя клиента...");
        while (getMessage() == null) {
            Thread.sleep(100L);
        }
        opponentName = getMessage();
        System.out.println("получили " + opponentName);
    }

    private List<Ship> loadFromFile(String fileName) {
        List<Ship> shipList = new ArrayList<>();
        try (FileReader fileReader = new FileReader(fileName);
             Scanner scanner = new Scanner(fileReader)) {
            int i = 0;
            while (++i <= 21) {
                int length = scanner.nextInt();
                int row = scanner.nextInt();
                int column = scanner.nextInt(10 + 26);
                String direction = scanner.next();
                shipList.add(new Ship(length, row, column - 10 + 1, direction.charAt(0)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return shipList;
    }

    private List<Ship> validate(List<Ship> ship) throws ValidationException {
        Integer[] array = {6, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1};
        List<Integer> count = new ArrayList<>(Arrays.asList(array));
        for (Ship sh : ship) {
            if (sh.getLength() > 0 && sh.getLength() < 7 && count.contains(sh.getLength())) {
                count.remove(Integer.valueOf(sh.getLength()));
            } else {
                System.out.println(sh);
                System.out.println(count);
                throw new ValidationException("Неверно заданы длины кораблей(допустимое количество и размеры кораблей: 6 клеток - 1 штука, 5 клеток - 2 штуки, 4 клетки - 3 штуки, 3 клетки - 4 штуки, 2 клетки - 5 штук, 1 клетка - 6 штук)\nКорабль с такими данными: " + sh.getLength() + "Cтрока: " + sh.getHeadRow() + "Столбец: " + sh.getHeadColumn());
            }
            if (sh.getHeadColumn() < 'A' || sh.getHeadColumn() > 'P') {
                throw new ValidationException("Неверно заданы координаты головы корабля по колонкам(допустимые значения - A-P) у корабля с длиной: " + sh.getLength() + "\nCтрока: " + sh.getHeadRow() + " Столбец: " + sh.getHeadColumn());
            }
            if (sh.getHeadRow() <= 0 || sh.getHeadRow() > 16) {
                throw new ValidationException("Неверно заданы координаты головы корабля по строкам(допустимые значения - 1-16) у корабля с длиной: " + sh.getLength() + "Cтрока: " + sh.getHeadRow() + "Столбец: " + sh.getHeadColumn());
            }
            if (sh.getOrientation() != 'R' && sh.getOrientation() != 'D') {
                throw new ValidationException("Неверно задано направление корабля(допустимый параметр: 'R' - горизонтально вправо или 'D' - вертикально вниз) у корабля с длиной: " + sh.getLength() + "Cтрока: " + sh.getHeadRow() + "Столбец: " + sh.getHeadColumn());
            }
        }
        return ship;
    }

    @Override
    public String toString() {
        if (player1 == null || player2 == null) {
            return "";
        } else {
            return "Игра{ " +
                    player1.getName() + " = " + player1.toString(mode) +
                    (mode == 2 ? player2.getName() + " = " + player2.toString(mode) : "") +
                    " }";
        }
    }

    public Player initPlayer1(String file, String name) throws ValidationException, AddAllShipsToBoardException {
        mode = 1;
        if (file != null) {
            player1 = initFromFile(file, name);
            player1.setName(name);
        } else {
            player1 = initFromRandom(name);
        }
        return player1;
    }

    public Player initPlayer2(String file, String name) throws ValidationException, AddAllShipsToBoardException {
        mode = 2;
        if (file != null) {
            player2 = initFromFile(file, name);
            player2.setName(name);
        } else {
            player2 = initFromRandom(name);
        }
        return player2;
    }

    public String play() {
        Player shooter;
        Player victim;
        int count = 0;
        if (Math.random() >= 0.5) {
            shooter = player1;
            victim = player2;
        } else {
            shooter = player2;
            victim = player1;
        }
        while (player1.isAlive() && player2.isAlive()) {
            count++;
            System.out.println(shooter.getName() + " ваш ход");
            System.out.println(shooter.toString(mode));
            Shot shot = shooter.choose();
            if (shooter.isBot()) {
                System.out.println(shot);
            }
            FieldStatus fieldStatus = victim.tryToShoot(shot);
            System.out.println(fieldStatus.getTitle());
            shooter.notify(shot, fieldStatus);
            System.out.println(shooter.toString(mode));
            if (fieldStatus.equals(FieldStatus.MISS) || fieldStatus.equals(FieldStatus.EMPTY)) {
                System.out.println("Переход хода от " + shooter.getInfo() + " к " + victim.getInfo());
                /*if (mode == 2 && !victim.isBot()) {
                    scanner.nextLine();
                }*/
                //System.in.read();
                Player change = victim;
                victim = shooter;
                shooter = change;
            }
            System.out.println("Количество ходов: " + count);
        }
        return (player1.isAlive() ? player1.getName() : player2.getName()) + ": " + shooter.getInfo() + ", " + victim.getInfo();
    }


    public void networkPlay(boolean server) throws InterruptedException {
        boolean wait = !server;
        int count = 0;
        if (server) {
            Thread.sleep(2100L);
            if (Math.random() >= 0.5) {
                sendMessage("Переход хода");
                System.out.println("Переход хода к " + opponentName);
                wait = true;
            }
        }
        int opponentHealth = player1.getHealth();
        while (player1.isAlive() && opponentHealth > 0) {
            FieldStatus fieldStatus = null;
            while (wait) {
                Thread.sleep(1000L);
                String message = getMessage();
                if (message != null && message.equals("Переход хода")) {
                    wait = false;
                } else if (validCoordinate(message)) {
                    String[] shot = getMessage().split((" "));
                    clearLastMessage();
                    fieldStatus = player1.tryToShoot(new Shot(Integer.parseInt(shot[0]) - 1, shot[1].charAt(0) - 'A'));
                    sendMessage(fieldStatus.getTitle());
                    //System.out.println(fieldStatus);
                    System.out.println(player1.toString(mode));
                } else if (!player1.isAlive()) {
                    System.out.println("ВЫ ПРОИГРАЛИ");
                    break;
                }
            }
            count++;
            while (!wait) {
                System.out.println(player1.toString(mode));
                System.out.println(player1.getName() + " ваш ход");
                Shot shot = player1.choose();
                if (player1.isBot()) {
                    System.out.println(shot);
                }
                clearLastMessage();
                sendMessage("" + shot);
                boolean bool = true;
                while (bool) {
                    if (getMessage() != null && (getMessage().equals("Попал") || getMessage().equals("Мимо") || getMessage().equals("Корабль потоплен") || getMessage().equals("Пусто"))) {
                        bool = false;
                    }
                    Thread.sleep(1000L);
                }
                fieldStatus = FieldStatus.byTitle(getMessage());
                System.out.println(fieldStatus.getTitle());
                player1.notify(shot, fieldStatus);
                if (fieldStatus.equals(FieldStatus.MISS) || fieldStatus.equals(FieldStatus.EMPTY)) {
                    sendMessage("Переход хода");
                    System.out.println("Переход хода к " + opponentName);
                    wait = true;
                } else {
                    opponentHealth--;
                    if (opponentHealth < 1) {
                        System.out.println("ВЫ ВЫИГРАЛИ");
                        break;
                    }
                }
            }
        }
        if (player1.isAlive()) {
            System.out.println("Победил игрок " + player1.getName());
        } else {
            System.out.println("Победил игрок " + opponentName);
        }
    }

    public boolean validCoordinate(String input) {
        if (input == null) {
            return false;
        }
        String regex = "^(1[0-6]|[1-9]) [A-Pa-p]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
