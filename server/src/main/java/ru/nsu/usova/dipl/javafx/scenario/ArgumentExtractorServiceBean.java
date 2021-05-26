package ru.nsu.usova.dipl.javafx.scenario;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.fit.makhasoeva.diploma.logic.impl.Predicate;
import ru.nsu.fit.makhasoeva.diploma.syntax.dwarf.plain.model.WordPosition;
import ru.nsu.usova.dipl.javafx.logictext.LogicTextInteraction;
import ru.nsu.usova.dipl.javafx.situation.DbIterator;
import ru.nsu.usova.dipl.javafx.situation.DbIteratorFactory;
import ru.nsu.usova.dipl.javafx.situation.model.ReasoningConstruction;
import ru.nsu.usova.dipl.javafx.parser.ExtractReasoning;
import ru.nsu.usova.dipl.javafx.scenario.model.LinkMetric;
import ru.nsu.usova.dipl.javafx.situation.model.Situation;
import ru.nsu.usova.dipl.javafx.situation.model.SituationLink;
import ru.nsu.usova.dipl.javafx.situation.model.metric.SamePartRelationType;
import ru.nsu.usova.dipl.javafx.situation.model.metric.SituationMetric;
import ru.nsu.usova.dipl.javafx.situation.model.metric.StructuralRelationType;
import ru.nsu.usova.dipl.javafx.situation.service.SituationCompareService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Data
@RequiredArgsConstructor
public class ArgumentExtractorServiceBean implements ArgumentExtractorService {
    private final DbIteratorFactory dbIteratorFactory;
    private final SituationCompareService situationCompareService;

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
        DbIterator<Situation> iterator = dbIteratorFactory.getSituationIterator();

        Situation closestSituation = null;
        SituationMetric maxMetric = new SituationMetric(0.0f, SamePartRelationType.EQUAL, StructuralRelationType.SAME);

        while (iterator.hasNext()) {
            Situation extractedSituation = iterator.next();

            SituationMetric metric = situationCompareService.compare(s, extractedSituation);
            if (metric.getDistance() > maxMetric.getDistance()) {
                maxMetric = metric;
                closestSituation = extractedSituation;
            }
            if(metric.getDistance() == maxMetric.getDistance()
                    && metric.getStructuralRelationType().getPriority() < maxMetric.getStructuralRelationType().getPriority()) {
                maxMetric = metric;
                closestSituation = extractedSituation;
            }
        }
        return closestSituation;
    }

    @Override
    public List<LinkMetric> findArgumentation(Situation s, float threshold) {
        List<LinkMetric> result = new ArrayList<>();
        DbIterator<SituationLink> iterator = dbIteratorFactory.getSituationLinkIterator();

        while (iterator.hasNext()) {
            SituationLink extractedLink = iterator.next();

            SituationMetric metric = situationCompareService.compare(s, extractedLink.getResultSituation());
            System.out.println(metric);

            if (metric.getDistance() > threshold)
                result.add(new LinkMetric(
                        metric.getDistance(),
                        metric.getSamePartRelationType().getDescription(),
                        metric.getStructuralRelationType().getDescription(),
                        extractedLink
                ));
        }
        return result;
    }
}
