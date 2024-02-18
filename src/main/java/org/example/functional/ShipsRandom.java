package org.example.functional;

import org.example.exception.AddAllShipsToBoardException;
import org.example.functional.Board;
import org.example.model.Info;
import org.example.model.Ship;

public class ShipsRandom {
    private Integer[] array = {6, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1};

    public void fillBoard(Board board) throws AddAllShipsToBoardException {
        int count = 0;
        for (Integer length : array) {
            int row;
            int column;
            char orientation;
            while (true) {
                row = (int) (Math.random() * Board.SIZE);
                column = (int) (Math.random() * Board.SIZE);
                if (Math.random() >= 0.5) {
                    orientation = 'D';
                } else {
                    orientation = 'R';
                }
                if (correct(board, (int) length, row, column, orientation)) {
                    break;
                }
                if(count++ > 10000) {
                    throw new RuntimeException("Зациклились при расстановке");
                }
            }
            Ship ship = new Ship(length, row + 1, column + 1, orientation);
            board.addShipToBoard(ship);
        }
    }

    private boolean correct(Board board, int length, int row, int column, char orientation) {
        for (int i = 0; i < length; i++) {
            int rowI = row + (orientation == 'D' ? i : 0);
            int columnI = column + (orientation == 'R' ? i : 0);
            if (rowI >= Board.SIZE || columnI >= Board.SIZE) {
                return false;
            }
            Info info = board.getInfo(rowI, columnI);
            if (info.isShip() || info.isGavan()) {
                return false;
            }
        }
        return true;
    }
}
