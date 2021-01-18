package ru.nsu.usova.dipl.scenario;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.usova.dipl.situation.Situation;
import ru.nsu.usova.dipl.situation.db.DbComponentFactory;
import ru.nsu.usova.dipl.situation.db.DbSituationsIterator;

@Service
@Data
@RequiredArgsConstructor
public class ArgumentExtractorServiceBean implements  ArgumentExtractorService {
    private final DbComponentFactory dbComponentFactory;

    @Override
    public Situation extractClosestSituation(Situation s) {
        DbSituationsIterator iterator = dbComponentFactory.getSituationIterator();

        Situation closestSituation = null;
        float maxMetric = 0.0f;

        while(iterator.hasNext()) {
            Situation extractedSituation = iterator.next();

            float metric = extractedSituation.compare(s);
            if(metric > maxMetric) {
                maxMetric = metric;
                closestSituation = extractedSituation;
            }
        }
        return closestSituation;
    }
}
