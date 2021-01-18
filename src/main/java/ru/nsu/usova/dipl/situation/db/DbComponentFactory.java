package ru.nsu.usova.dipl.situation.db;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nsu.usova.dipl.situation.Situation;
import ru.nsu.usova.dipl.situation.SituationLink;
import ru.nsu.usova.dipl.situation.db.repository.SituationLinkRepository;
import ru.nsu.usova.dipl.situation.db.repository.SituationRepository;

@Component
@Data
@RequiredArgsConstructor
public class DbComponentFactory {
    private final SituationRepository situationRepository;

    private final SituationLinkRepository situationLinkRepository;

    private final int defaultBatchSize = 4;

    public DbIterator<Situation> getSituationIterator() {
        return new DbIterator<>(situationRepository, defaultBatchSize);
    }

    public DbIterator<SituationLink> getSituationLinkIterator() {
        return new DbIterator<>(situationLinkRepository ,defaultBatchSize);
    }
}
