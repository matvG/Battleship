package org.example.model;

public class Ship {
    private int length;

    private int row; // 1..16
    private int column; // 1..16

    private char orientation;

    private int health;

    public Ship(int length, int row, int column, char orientation) {
        this.length = length;
        this.row = row;
        this.column = column;
        this.orientation = orientation;
        this.health = length;
    }

    public int getHealth() {
        return health;
    }

    public void cutHealth() {
        health--;
    }

    public int getLength() {
        return length;
    }

    public int getHeadRow() {
        return row;
    }

    public char getHeadColumn() {
        return (char) ('A' + column - 1);
    }

    public char getOrientation() {
        return orientation;
    }

    @Override
    public String toString() {
        return "Корабль {" +
                "размер = " + length +
                ", координаты головы = [" + row + ", " + getHeadColumn() + "]" +
                ", направление = " + orientation +
                '}';
    }
}
