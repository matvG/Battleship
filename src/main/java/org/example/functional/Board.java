package org.example.functional;

import org.example.model.Info;
import org.example.model.Ship;
import org.example.exception.AddAllShipsToBoardException;
import org.example.model.FieldStatus;

import java.util.List;

public class Board {

    public static final int SIZE = 16;
    private Info[][] board;

    private int health;

    public Board() {
        board = new Info[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                board[row][column] = new Info();
            }
        }
    }

    public void addAllShipsToBoard(List<Ship> ships) throws AddAllShipsToBoardException {
        for (Ship ship : ships) {
            addShipToBoard(ship);
        }
    }

    public void addShipToBoard(Ship ship) throws AddAllShipsToBoardException {
        System.out.println("Ставим корабль: " + ship);
        int row = ship.getHeadRow() - 1;
        int column = ship.getHeadColumn() - 'A' + 1 - 1;
        for (int length = 0; length < ship.getLength(); length++) {
            if (ship.getOrientation() == 'R') {
                tryToDrawShip(ship, row, column + length);
            } else if (ship.getOrientation() == 'D') {
                tryToDrawShip(ship, row + length, column);
            } else {
                throw new AddAllShipsToBoardException("Невозможно поставить корабль: " + ship);
            }
        }
        System.out.println("Поставили корабль: " + ship);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                  |ABCDEFGHIJKLMNOP
                 ------------------
                """);
        for (int row = 0; row < 16; row++) {
            sb.append(String.format("%2d|", row + 1));
            for (int column = 0; column < 16; column++) {
                sb.append(board[row][column].getChar());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void tryToDrawGavan(Ship ship, int row, int column) {
        if (row >= 0 && row < SIZE && column >= 0 && column < SIZE && board[row][column].isWater()) {//0-15
            board[row][column].setGavan(ship);
        }
    }

    private void tryToDrawShip(Ship ship, int row, int column) throws AddAllShipsToBoardException {
        if (row < 0 || row >= SIZE || column < 0 || column >= SIZE) {//0-15
            throw new AddAllShipsToBoardException("ПРИ РАССТАНОВКЕ, КОРАБЛЬ: " + ship + " ВЫХОДИТ ЗА ПРЕДЕЛЫ КАРТЫ");
        }
        if (board[row][column].getShip() != null) {
            throw new AddAllShipsToBoardException("КОРАБЛИ ПЕРЕСЕКАЮТСЯ ДРУГ С ДРУГОМ! \nНевозможно расставить корабль: " + ship);
        }
        if (board[row][column].getGavan() != null && !ship.equals(board[row][column].getGavan())) {
            throw new AddAllShipsToBoardException("КОРАБЛИ КАСАЮТСЯ ДРУГ ДРУГА! \nНевозможно расставить корабль: " + ship);
        }
        board[row][column].setShip(ship);
        board[row][column].setGavan(null);
        tryToDrawGavan(ship, row, column - 1);
        tryToDrawGavan(ship, row + 1, column - 1);
        tryToDrawGavan(ship, row + 1, column);
        tryToDrawGavan(ship, row + 1, column + 1);
        tryToDrawGavan(ship, row, column + 1);
        tryToDrawGavan(ship, row - 1, column + 1);
        tryToDrawGavan(ship, row - 1, column);
        tryToDrawGavan(ship, row - 1, column - 1);
        health++;
    }

    public int getHealth() {
        return health;
    }

    public void cutHealth() {
        health--;
    }

    public FieldStatus tryToShoot(int row, int column) {
        if (!boardCellExists(row, column)) {
            return FieldStatus.MISS;
        }
        if (board[row][column].isShip()) {
            if (!board[row][column].isShooted()) {
                board[row][column].setShooted(true);
                board[row][column].cutHealth();
                cutHealth();
            }
            return board[row][column].getHealth() == 0 ? FieldStatus.DEAD : FieldStatus.HIT;
        }
        return FieldStatus.MISS;
    }


    public void markShootResult(FieldStatus fieldStatus, int row, int column) {
        if (!boardCellExists(row, column)) {
            return;
        }
        board[row][column].setShooted(true);
        board[row][column].setFieldStatus(fieldStatus);
        if (fieldStatus == FieldStatus.DEAD) {
            tag8CellsAround(row, column);
            markHittedSide(row, column, 0, 1);
            markHittedSide(row, column, 0, -1);
            markHittedSide(row, column, -1, 0);
            markHittedSide(row, column, 1, 0);
        }
    }

    private void markHittedSide(int row, int column, int addRow, int addColumn) {
        while (boardCellExists(row += addRow, column += addColumn) && board[row][column].getFieldStatus() == FieldStatus.HIT) {
            tag8CellsAround(row, column);
        }
    }

    public boolean boardCellExists(int row, int column) {
        return row >= 0 && row < SIZE && column >= 0 && column < SIZE;
    }

    public Info getInfo(int row, int column) {
        return board[row][column];
    }

    private void tag8CellsAround(int row, int column) {
        if (!boardCellExists(row, column)) {
            return;
        }
        tryToMarkField(row - 1, column - 1, FieldStatus.EMPTY);
        tryToMarkField(row - 1, column, FieldStatus.EMPTY);
        tryToMarkField(row - 1, column + 1, FieldStatus.EMPTY);
        tryToMarkField(row, column - 1, FieldStatus.EMPTY);
        tryToMarkField(row, column + 1, FieldStatus.EMPTY);
        tryToMarkField(row + 1, column - 1, FieldStatus.EMPTY);
        tryToMarkField(row + 1, column, FieldStatus.EMPTY);
        tryToMarkField(row + 1, column + 1, FieldStatus.EMPTY);
    }

    private void tryToMarkField(int row, int column, FieldStatus status) {
        if (!boardCellExists(row, column)) {
            return;
        }
        if (board[row][column].getFieldStatus() == null) {
            board[row][column].setFieldStatus(status);
        }
    }
}
