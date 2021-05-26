package ru.nsu.usova.dipl.javafx.parser.model;

import lombok.Data;

@Data
public class DelimInfo {
    private Boolean direction;

    public DelimInfo() {}

    public DelimInfo(Boolean direction) {
        this.direction = direction;
    }
}
