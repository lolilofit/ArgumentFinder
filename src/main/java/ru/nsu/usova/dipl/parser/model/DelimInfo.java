package ru.nsu.usova.dipl.parser.model;

public class DelimInfo {
    private Boolean direction;

    public DelimInfo() {}

    public DelimInfo(Boolean direction) {
        this.direction = direction;
    }

    public Boolean getDirection() {
        return direction;
    }

    public void setDirection(Boolean direction) {
        this.direction = direction;
    }
}
