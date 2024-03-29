package ru.nsu.usova.dipl.javafx.situation;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nsu.usova.dipl.javafx.situation.repository.SituationRepository;
import ru.nsu.usova.dipl.javafx.situation.model.Situation;
import ru.nsu.usova.dipl.javafx.situation.model.SituationLink;
import ru.nsu.usova.dipl.javafx.situation.repository.SituationLinkRepository;

@Component
@Data
@RequiredArgsConstructor
public class DbIteratorFactory {
    private final SituationRepository situationRepository;

    private final SituationLinkRepository situationLinkRepository;

    private final Long defaultBatchSize = 4L;

    /**
     * Получения итератора для обхода таблицы ситуаций
     *
     * @return
     */
    public DbIterator<Situation> getSituationIterator() {
        return new DbIterator<>(situationRepository, defaultBatchSize);
    }

    /**
     * Получение итератора для обхода таблицы аргументаций
     *
     * @return
     */
    public DbIterator<SituationLink> getSituationLinkIterator() {
        return new DbIterator<>(situationLinkRepository, defaultBatchSize);
    }
}
