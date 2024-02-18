package org.example.functional;

import org.example.model.Shot;
import org.example.model.FieldStatus;

import javax.websocket.Session;

import java.io.IOException;
import java.util.Scanner;

public class Player {
    private Board ownerBoard;

    private Board opponentBoard;

    private String name;

    private boolean bot;
    private AI ai;

    public Player(Board ownerBoard, String name) {
        this.ownerBoard = ownerBoard;
        opponentBoard = new Board();
        this.name = name;
    }

    public int getHealth() {
        return ownerBoard.getHealth();
    }

    public boolean isBot() {
        return bot;
    }

    public Player setBot(boolean bot) {
        this.bot = bot;
        return this;
    }

    public void setAI(AI ai) {
        this.ai = ai;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(int mode) {
        return "\n\t\t" + this.getName() + "\n" +
                (mode != 2 ? "\tСвоё поле\n" + ownerBoard + "\n" : "") +
                "\tПоле противника\n" + opponentBoard;
    }

    public boolean isAlive() {
        return ownerBoard.getHealth() > 0;
    }

    public FieldStatus tryToShoot(Shot shot) {
        return ownerBoard.tryToShoot(shot.getRow(), shot.getColumn());
    }

    public void notify(Shot shot, FieldStatus fieldStatus) {
        opponentBoard.markShootResult(fieldStatus, shot.getRow(), shot.getColumn());
    }

    public Shot choose() {
        if (!isBot()) {
            Scanner scanner = new Scanner(System.in);
            String[] str;
            int row;
            int column;
            do {
                String line = scanner.nextLine();
                str = line.split(" ");
                row = Integer.parseInt(str[0]) - 1;
                column = str[1].charAt(0) - 'A';
            } while (str.length != 2);
            return new Shot(row, column);
        } else {
            if (ai != null) {
                return ai.shoot(opponentBoard);
            }
            int row = (int) (Math.random() * Board.SIZE);
            int column = (int) (Math.random() * Board.SIZE);
            return new Shot(row, column);
        }
    }

    public String getInfo() {
        return getName() + "=" + getHealth();
    }
}
