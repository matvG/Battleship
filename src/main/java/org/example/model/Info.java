package org.example.model;

public class Info {
    private Ship ship;

    private Ship gavan;

    private boolean shooted;

    private int health;

    private FieldStatus fieldStatus;

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Ship getGavan() {
        return gavan;
    }

    public void setGavan(Ship gavan) {
        this.gavan = gavan;
    }

    public boolean isWater() {
        return ship == null;
    }

    public boolean isShooted() {
        return shooted;
    }

    public void setShooted(boolean shooted) {
        this.shooted = shooted;
    }

    public FieldStatus getFieldStatus() {
        return fieldStatus;
    }

    public void setFieldStatus(FieldStatus fieldStatus) {
        this.fieldStatus = fieldStatus;
    }

    @Override
    public String toString() {
        return "Информация о клетке { " +
                "корабль =" + ship +
                ", гавань =" + gavan +
                ", стреляная =" + shooted +
                ", здоровье =" + health +
                ", статус =" + fieldStatus +
                " }";
    }

    public char getChar() {
        if ((!isShip() && isWater() && gavan != null) || fieldStatus == FieldStatus.EMPTY) {
            return '+';
        } else if (isWater() && !shooted && gavan == null) {
            return '·';
        } else if ((isShip() && shooted) || FieldStatus.HIT.equals(fieldStatus) || FieldStatus.DEAD.equals(fieldStatus)) {
            return 'x';
        } else if (isShip() && !shooted) {
            return '█';
        } else if ((isWater() && shooted) || FieldStatus.MISS.equals(fieldStatus)) {
            return 'o';
        } else {
            return '?';
        }
    }

    public void cutHealth() {
        ship.cutHealth();
    }

    public int getHealth() {
        return ship.getHealth();
    }

    public boolean isShip() {
        return ship != null;
    }

    public boolean isGavan() {
        return gavan != null;
    }
}
