package ru.nsu.usova.dipl.situation.db;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nsu.usova.dipl.situation.db.repository.SituationRepository;

@Component
@Data
@RequiredArgsConstructor
public class DbComponentFactory {
    private final SituationRepository situationRepository;

    private final int defaultBatchSize = 4;

    public DbSituationsIterator getSituationIterator() {
        return new DbSituationsIterator(situationRepository, defaultBatchSize);
    }
}
