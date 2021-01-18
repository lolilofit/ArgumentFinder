package ru.nsu.usova.dipl.situation.db.repository;

import java.util.List;

public interface BatchRepository<T> {
    Long getMaxId();

    List<T> getAllByMinMaxId(Long minId, Long maxId);
}
