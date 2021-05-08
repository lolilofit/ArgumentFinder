package ru.nsu.usova.dipl.situation.service;

import org.springframework.stereotype.Service;
import ru.nsu.usova.dipl.ontology.WordNetUtils;
import ru.nsu.usova.dipl.ontology.model.OntologyRelated;
import ru.nsu.usova.dipl.situation.SituationUtils;
import ru.nsu.usova.dipl.situation.model.Situation;
import ru.nsu.usova.dipl.situation.model.SituationQuestions;
import ru.nsu.usova.dipl.situation.model.metric.SamePartRelationType;
import ru.nsu.usova.dipl.situation.model.metric.SituationMetric;
import ru.nsu.usova.dipl.situation.model.metric.StructuralRelationType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SituationsCompareServiceBean implements SituationCompareService {
    //кэш для запросов к серверу онтологий
    private Map<String, OntologyRelated> ontologyRelatedCache = new HashMap<>();

    /**
     * Сравнить две ситуации с использованием онтологий
     *
     * @param s
     * @param s1
     * @return
     */
    @Override
    public SituationMetric compare(Situation s, Situation s1) {
        if (s1.getQuestionsList() != null && !s1.getQuestionsList().isEmpty()
                && s.getQuestionsList() != null && !s.getQuestionsList().isEmpty())
            return compareBaseSituations(s, s1);
        if (s1.getChildSituations() != null && !s1.getChildSituations().isEmpty() && s.getChildSituations() != null && !s.getChildSituations().isEmpty())
            return compareComplexSituations(s, s1);
        if (s1.getChildSituations() == null || s1.getChildSituations().isEmpty())
            return compareSituationWithChild(s.getChildSituations(), s1);
        else
            return compareSituationWithChild(s1.getChildSituations(), s);
    }

    /**
     * Сравнение слов с помощью словаря онтологий
     * Сравнивается непосредственное равенство, синонимичность слов, отношение понятия общее-частное
     * Если строки w1 или w2 состоят из нескольких слов, то проверяется непосредственное равенство
     *
     * @param w1
     * @param w2
     * @return
     */
    private SituationMetric compareWords(String w1, String w2) {
        SituationMetric metric = new SituationMetric(0, SamePartRelationType.EQUAL, StructuralRelationType.SAME);
        //if several words in string
        if (w1.split(" ").length > 1 || w2.split(" ").length > 1) {
            metric.setDistance(w1.equals(w2) ? 1.0f : 0.0f);
            return metric;
        }
        try {
            if(w1.equals(w2)) {
                metric.setDistance(1.0f);
                return metric;
            }
            //получить информацию о синонимах и общих понятиях через кэш
            // или запрос на сервер онтологий
            OntologyRelated ontologyRelated1, ontologyRelated2;
            if (ontologyRelatedCache.containsKey(w1))
                ontologyRelated1 = ontologyRelatedCache.get(w1);
            else {
                ontologyRelated1 = WordNetUtils.ontologyRelated(w1);
                ontologyRelatedCache.put(w1, ontologyRelated1);
            }
            if (ontologyRelatedCache.containsKey(w2))
                ontologyRelated2 = ontologyRelatedCache.get(w2);
            else {
                ontologyRelated2 = WordNetUtils.ontologyRelated(w2);
                ontologyRelatedCache.put(w2, ontologyRelated2);
            }

            if (ontologyRelated1.getSynsets().size() == 0
                    && ontologyRelated2.getSynsets().size() == 0
                    && ontologyRelated1.getHyps().size() == 0
                    && ontologyRelated2.getHyps().size() == 0) {
                metric.setDistance(0.0f);
                return metric;
            }

            ontologyRelated1.compare(ontologyRelated2, metric);
            ontologyRelated2.compare(ontologyRelated1, metric);
            return metric;
        } catch (InterruptedException | IOException | URISyntaxException e) {
            metric.setDistance(0.0f);
            return metric;
        }
    }

    public float countUnique(Situation s1, Situation s2) {
        Set<String> firstSituationQuestions = s1.getQuestionsList().stream()
                .map(SituationQuestions::getQuestionKey).collect(Collectors.toCollection(LinkedHashSet::new));
        Set<String> secondSituationQuestions = s2.getQuestionsList().stream()
                .map(SituationQuestions::getQuestionKey).collect(Collectors.toCollection(LinkedHashSet::new));
        firstSituationQuestions.addAll(secondSituationQuestions);
        return (float) firstSituationQuestions.size();
    }

    /**
     * @param s
     * @param s1
     * @return
     */
    private SituationMetric compareBaseSituations(Situation s, Situation s1) {
        SituationMetric metric = new SituationMetric(0, SamePartRelationType.EQUAL, StructuralRelationType.SAME);
        List<String> visited = new ArrayList<>();

        Map<String, String> s1Questions = s1.getQuestionsList().stream().collect(Collectors.toMap(SituationQuestions::getQuestionKey, SituationQuestions::getQuestionValue));
        Map<String, String> sQuestions = s.getQuestionsList().stream().collect(Collectors.toMap(SituationQuestions::getQuestionKey, SituationQuestions::getQuestionValue));

        for (Map.Entry<String, String> thisParam : s1Questions.entrySet()) {
            if (sQuestions.containsKey(thisParam.getKey())) {
                SituationMetric wordMetric = compareWords(thisParam.getValue(), sQuestions.get(thisParam.getKey()));
                if (wordMetric.getDistance() == 1.0f) {
                    visited.add(thisParam.getKey());
                    if (metric.getSamePartRelationType().getPriority() > wordMetric.getSamePartRelationType().getPriority())
                        metric.setSamePartRelationType(wordMetric.getSamePartRelationType());
                }
            }
        }
        if (visited.size() != sQuestions.size() && visited.size() != s1Questions.size())
            metric.setStructuralRelationType(StructuralRelationType.INTERSECTION);
        else if (visited.size() != sQuestions.size() || visited.size() != s1Questions.size())
            metric.setStructuralRelationType(StructuralRelationType.INCLUDE);

        metric.setDistance((float) visited.size() / countUnique(s1, s));
        return metric;
    }

    private void fillLists(List<List<Integer>> sequences, List<List<Integer>> subsets, List<Situation> s) {
        sequences.addAll(SituationUtils.generateSequences(s.get(1).getChildSituations().size()));
        subsets.addAll(SituationUtils.generateAllSubset(s.get(0).getChildSituations().size(), s.get(1).getChildSituations().size()));
    }

    private SituationMetric compareComplexSituations(Situation s, Situation s1) {
        SituationMetric metric = new SituationMetric(0, SamePartRelationType.EQUAL, StructuralRelationType.SAME);
        List<List<Integer>> sequences = new ArrayList<>(), subsets = new ArrayList<>();
        SituationMetric sum;
        int nonNullDist = 0, maxNonNullDist = 0;

        fillLists(sequences, subsets, s.getChildSituations().size() <= s1.getChildSituations().size() ? List.of(s1, s) : List.of(s, s1));

        for (List<Integer> outerList : subsets) {
            for (List<Integer> innerCounter : sequences) {
                sum = null;
                nonNullDist = 0;
                for (int i = 0; i < outerList.size(); i++) {
                    SituationMetric m;
                    if (s.getChildSituations().size() <= s1.getChildSituations().size()) {
                        m = compare(s1.getChildSituations().get(outerList.get(i)), s.getChildSituations().get(innerCounter.get(i)));
                    } else {
                        m = compare(s1.getChildSituations().get(innerCounter.get(i)), s.getChildSituations().get(outerList.get(i)));
                    }
                    if(sum == null)
                        sum = m;
                    else {
                        sum.setDistance(sum.getDistance() + m.getDistance());
                        if(sum.getSamePartRelationType().getPriority() > m.getSamePartRelationType().getPriority())
                            sum.setSamePartRelationType(m.getSamePartRelationType());
                        if(sum.getStructuralRelationType().getPriority() > m.getStructuralRelationType().getPriority())
                            sum.setStructuralRelationType(m.getStructuralRelationType());
                    }
                    if(m.getDistance() != 0.0f)
                        nonNullDist++;
                }

                if (sum != null && metric.getDistance() < sum.getDistance()) {
                    metric = sum;
                    maxNonNullDist = nonNullDist;
                }
            }
        }
        if (s.getChildSituations().size() != s1.getChildSituations().size()) {
            if(Math.min(s.getChildSituations().size(), s1.getChildSituations().size()) == maxNonNullDist && metric.getStructuralRelationType().getPriority() > StructuralRelationType.INCLUDE.getPriority())
                metric.setStructuralRelationType(StructuralRelationType.INCLUDE);
            else if(metric.getStructuralRelationType().getPriority() > StructuralRelationType.INTERSECTION.getPriority())
                metric.setStructuralRelationType(StructuralRelationType.INTERSECTION);
        }

        metric.setDistance(0.5f * (metric.getDistance() / s.getChildSituations().size() + metric.getDistance() / s1.getChildSituations().size()));
        return metric;
    }

    private SituationMetric compareSituationWithChild(List<Situation> c, Situation s) {
        SituationMetric maxMetric = new SituationMetric(0, SamePartRelationType.EQUAL, StructuralRelationType.SAME);

        for (Situation child : c) {
            SituationMetric metric = compare(child, s);
            if (maxMetric.getDistance() < metric.getDistance())
                maxMetric = metric;
            if (maxMetric.getDistance() == metric.getDistance()
                    && metric.getStructuralRelationType().getPriority() < maxMetric.getStructuralRelationType().getPriority())
                maxMetric = metric;
        }
        return maxMetric;
    }
}
