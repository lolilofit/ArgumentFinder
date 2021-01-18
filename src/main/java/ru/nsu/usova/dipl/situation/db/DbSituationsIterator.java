package ru.nsu.usova.dipl.situation.db;

import ru.nsu.usova.dipl.situation.Situation;
import ru.nsu.usova.dipl.situation.db.repository.SituationRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DbSituationsIterator implements Iterator<Situation> {
    private SituationRepository situationRepository;

    private List<Situation> extractedSituations = new ArrayList<>();

    private Long dbCur;

    private Long maxId;

    private int listCur;

    private int batchSize;

    public DbSituationsIterator(SituationRepository situationRepository, int batchSize) {
        this.situationRepository = situationRepository;
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
    public Situation next() {
        if(listCur < extractedSituations.size())
            return extractedSituations.get(listCur++);

        while (extractedSituations.size() == 0 && dbCur <= maxId) {
            extractedSituations = situationRepository.getAllByMinMaxId(dbCur, dbCur + batchSize);
            dbCur += batchSize;
        }
        listCur = 1;
        return extractedSituations.get(0);
    }
}
