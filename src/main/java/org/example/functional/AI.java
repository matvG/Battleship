package org.example.functional;

import org.example.model.Shot;
import org.example.model.FieldStatus;

public class AI {

    public Shot shoot(Board board) {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int column = 0; column < Board.SIZE; column++) {
                if (board.getInfo(row, column).getFieldStatus() == FieldStatus.HIT) {
                    int a = -1;
                    int b = 1;
                    int c = 1;
                    int d = -1;
                    while (board.boardCellExists(row + a, column) && board.getInfo(row + a, column).getFieldStatus() == FieldStatus.HIT) {
                        a--;
                    }
                    while (board.boardCellExists(row + b, column) && board.getInfo(row + b, column).getFieldStatus() == FieldStatus.HIT) {
                        b++;
                    }
                    while (board.boardCellExists(row, column + c) && board.getInfo(row, column + c).getFieldStatus() == FieldStatus.HIT) {
                        c++;
                    }
                    while (board.boardCellExists(row, column + d) && board.getInfo(row, column + d).getFieldStatus() == FieldStatus.HIT) {
                        d--;
                    }
                    if (a == -1 && b == 1 && c == 1 && d == -1) {
                        if (a < 0 && board.boardCellExists(row + a, column) && board.getInfo(row + a, column).getFieldStatus() == null) {
                            return new Shot(row + a, column);
                        }
                        if (b > 0 && board.boardCellExists(row + b, column) && board.getInfo(row + b, column).getFieldStatus() == null) {
                            return new Shot(row + b, column);
                        }
                        if (c > 0 && board.boardCellExists(row, column + c) && board.getInfo(row, column + c).getFieldStatus() == null) {
                            return new Shot(row, column + c);
                        }
                        if (d < 0 && board.boardCellExists(row, column + d) && board.getInfo(row, column + d).getFieldStatus() == null) {
                            return new Shot(row, column + d);
                        }
                    }
                    if (d < -1 || c > 1) {
                        if (board.boardCellExists(row, column + d) && board.getInfo(row, column + d).getFieldStatus() == null) {
                            return new Shot(row, column + d);
                        }
                        if (board.boardCellExists(row, column + c) && board.getInfo(row, column + c).getFieldStatus() == null) {
                            return new Shot(row, column + c);
                        }
                    }
                    if (a < -1 || b > 1) {
                        if (board.boardCellExists(row + a, column) && board.getInfo(row + a, column).getFieldStatus() == null) {
                            return new Shot(row + a, column);
                        }
                        if (board.boardCellExists(row + b, column) && board.getInfo(row + b, column).getFieldStatus() == null) {
                            return new Shot(row + b, column);
                        }
                    }
                }

            }
        }
        // 3 random
        return v3(board);
    }

    private Shot v3(Board board) {
        int row;
        int column;
        do {
            row = (int) (Math.random() * Board.SIZE);
            column = (int) (Math.random() * Board.SIZE);
        } while (board.getInfo(row, column).getFieldStatus() != null);
        return new Shot(row, column);
    }
}
