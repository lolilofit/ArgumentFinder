package ru.nsu.usova.dipl.situation.db;

import ru.nsu.usova.dipl.situation.db.repository.BatchRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DbIterator<T> implements Iterator<T> {
    private BatchRepository<T> batchRepository;

    private List<T> extractedSituations = new ArrayList<>();

    private Long dbCur;

    private Long maxId;

    private int listCur;

    private int batchSize;

    public DbIterator(BatchRepository<T> situationRepository, int batchSize) {
        this.batchRepository = situationRepository;
        this.batchSize = batchSize;

        maxId = situationRepository.getMaxId();
    }

    @Override
    public boolean hasNext() {
        if(maxId == 0 || batchSize == 0)
            return false;
        return listCur < extractedSituations.size() || dbCur <= maxId;
    }

    @Override
    public T next() {
        if(listCur < extractedSituations.size())
            return extractedSituations.get(listCur++);

        while (extractedSituations.size() == 0 && dbCur <= maxId) {
            extractedSituations = batchRepository.getAllByMinMaxId(dbCur, dbCur + batchSize);
            dbCur += batchSize;
        }
        listCur = 1;
        return extractedSituations.get(0);
    }
}
