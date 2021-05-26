package ru.nsu.usova.dipl.javafx.situation;

import ru.nsu.usova.dipl.javafx.situation.repository.BatchRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Итератор для обхода базы данных
 *
 * @param <T>
 */
public class DbIterator<T> implements Iterator<T> {
    private BatchRepository<T> batchRepository;

    private List<T> extractedSituations = new ArrayList<>();

    private Integer dbCur = 0;

    private long maxId;

    private int listCur;

    private Long batchSize;

    public DbIterator(BatchRepository<T> situationRepository, Long batchSize) {
        this.batchRepository = situationRepository;
        this.batchSize = batchSize;

        maxId = situationRepository.getMaxId();
    }

    @Override
    public boolean hasNext() {
        if (maxId == 0 || batchSize == 0)
            return false;
        return listCur < extractedSituations.size() || dbCur <= maxId;
    }

    @Override
    public T next() {
        if (listCur < extractedSituations.size())
            return extractedSituations.get(listCur++);

        extractedSituations.clear();
        while (extractedSituations.size() == 0 && dbCur <= maxId) {
            extractedSituations = batchRepository.getAllByMinMaxId(dbCur, (dbCur + batchSize));
            dbCur += batchSize.intValue();
        }
        listCur = 1;
        return extractedSituations.get(0);
    }
}
