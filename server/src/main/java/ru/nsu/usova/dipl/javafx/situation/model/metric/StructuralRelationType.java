package ru.nsu.usova.dipl.javafx.situation.model.metric;

import lombok.Data;

public enum StructuralRelationType {
    SAME(3, "Пересекаются полностью"),
    INCLUDE(2, "Одно включает второе"),
    INTERSECTION(1, "Пересечение");

    private final int priority;
    private final String description;

    private StructuralRelationType(int priority, String description) {
        this.priority = priority;
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }
}
