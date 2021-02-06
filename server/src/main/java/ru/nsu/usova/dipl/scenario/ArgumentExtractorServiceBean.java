package ru.nsu.usova.dipl.scenario;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.fit.makhasoeva.diploma.logic.impl.Predicate;
import ru.nsu.fit.makhasoeva.diploma.syntax.dwarf.plain.model.WordPosition;
import ru.nsu.usova.dipl.logictext.LogicTextInteraction;
import ru.nsu.usova.dipl.parser.ExtractReasoning;
import ru.nsu.usova.dipl.scenario.model.LinkMetric;
import ru.nsu.usova.dipl.situation.ReasoningConstruction;
import ru.nsu.usova.dipl.situation.Situation;
import ru.nsu.usova.dipl.situation.SituationLink;
import ru.nsu.usova.dipl.situation.db.DbComponentFactory;
import ru.nsu.usova.dipl.situation.db.DbIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Data
@RequiredArgsConstructor
public class ArgumentExtractorServiceBean implements ArgumentExtractorService {
    private final DbComponentFactory dbComponentFactory;

    @Override
    public Situation getSituationFromStatement(String src) throws IOException, InterruptedException {
        LogicTextInteraction logicTextInteraction = new LogicTextInteraction(src);
        Map<WordPosition, Predicate> predicates = logicTextInteraction.getPredicatesMap();

        ReasoningConstruction reasoningConstruction = new ReasoningConstruction();
        reasoningConstruction.setPremisePredicates(predicates);

        Map<WordPosition, Predicate> predicateMap = ExtractReasoning.filterPredicates(logicTextInteraction, (e) -> true);
        reasoningConstruction.getPremisePredicates().putAll(predicateMap);

        reasoningConstruction.convertToSituations();

        return reasoningConstruction.getSituationLink().getPremiseSituation();
    }

    @Override
    public Situation extractClosestSituation(Situation s) {
        DbIterator<Situation> iterator = dbComponentFactory.getSituationIterator();

        Situation closestSituation = null;
        float maxMetric = 0.0f;

        while (iterator.hasNext()) {
            Situation extractedSituation = iterator.next();

            float metric = extractedSituation.compare(s);
            if (metric > maxMetric) {
                maxMetric = metric;
                closestSituation = extractedSituation;
            }
        }
        return closestSituation;
    }

    @Override
    public List<LinkMetric> findArgumentation(Situation s, float threshold) {
        List<LinkMetric> result = new ArrayList<>();
        DbIterator<SituationLink> iterator = dbComponentFactory.getSituationLinkIterator();

        while (iterator.hasNext()) {
            SituationLink extractedLink = iterator.next();

            float metric = extractedLink.getResultSituation().compare(s);
            System.out.println(metric);

            if (metric > threshold)
                result.add(new LinkMetric(metric, extractedLink));
        }
        return result;
    }
}
