package ru.nsu.usova.dipl.javafx.situation.repository;

import java.util.List;

public interface BatchRepository<T> {
    Long getMaxId();

    List<T> getAllByMinMaxId(Integer minId, Long maxId);
}
