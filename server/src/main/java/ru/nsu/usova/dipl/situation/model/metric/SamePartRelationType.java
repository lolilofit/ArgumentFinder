package ru.nsu.usova.dipl.situation.model.metric;

public enum SamePartRelationType {
    EQUAL(1, "Точное равенство"),
    SIMILAR(2, "Cхожие (синонимичные)"),
    GENERALIZATION(3, "Обобщение");

    private final int priority;
    private final String description;

    private SamePartRelationType(int priority, String description) {
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
