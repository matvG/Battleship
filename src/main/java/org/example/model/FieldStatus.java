package org.example.model;

public enum FieldStatus {
    HIT ("Попал"),
    DEAD ("Корабль потоплен"),
    MISS ("Мимо"),
    EMPTY ("Пусто");

    private String title;

    FieldStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static FieldStatus byTitle(String title) {
        for (FieldStatus fieldStatus : FieldStatus.values()) {
            if(fieldStatus.getTitle().equals(title)) {
                return fieldStatus;
            }
        }
        return FieldStatus.EMPTY;
    }
}