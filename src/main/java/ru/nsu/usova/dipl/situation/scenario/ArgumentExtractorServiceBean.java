package ru.nsu.usova.dipl.situation.scenario;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import ru.nsu.usova.dipl.situation.Situation;
import ru.nsu.usova.dipl.situation.SituationLink;
import ru.nsu.usova.dipl.situation.db.DbComponentFactory;
import ru.nsu.usova.dipl.situation.db.DbIterator;

@Service
@Data
@RequiredArgsConstructor
@ComponentScan(basePackageClasses = Situation.class)
public class ArgumentExtractorServiceBean implements  ArgumentExtractorService {
    private final DbComponentFactory dbComponentFactory;

    @Override
    public Situation extractClosestSituation(Situation s) {
        DbIterator<Situation> iterator = dbComponentFactory.getSituationIterator();

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

    @Override
    public SituationLink findArgumentation(Situation s) {
        DbIterator<SituationLink> iterator = dbComponentFactory.getSituationLinkIterator();

        SituationLink closestLink = null;
        float maxMetric = 0.0f;

        while(iterator.hasNext()) {
            SituationLink extractedLink = iterator.next();

            float metric = extractedLink.getResultSituation().compare(s);
            System.out.println(metric);

            if(metric > maxMetric) {
                maxMetric = metric;
                closestLink = extractedLink;
            }
        }
        return closestLink;
    }


}
