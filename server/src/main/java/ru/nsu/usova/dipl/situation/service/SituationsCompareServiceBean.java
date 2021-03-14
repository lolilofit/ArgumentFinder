package ru.nsu.usova.dipl.situation.service;

import org.springframework.stereotype.Service;
import ru.nsu.usova.dipl.ontology.WordNetUtils;
import ru.nsu.usova.dipl.ontology.model.OntologyRelated;
import ru.nsu.usova.dipl.situation.SituationUtils;
import ru.nsu.usova.dipl.situation.model.Situation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class SituationsCompareServiceBean implements SituationCompareService {
    private Map<String, OntologyRelated> ontologyRelatedCache = new HashMap<>();

    @Override
    public float compare(Situation s, Situation s1) {
        if (s1.getQuestions() != null && !s1.getQuestions().isEmpty() && s.getQuestions() != null && !s.getQuestions().isEmpty())
            return compareComplexSituations(s, s1);
        if(s1.getChildSituations() != null && !s1.getChildSituations().isEmpty() && s.getChildSituations() != null && !s.getChildSituations().isEmpty())
            return compareBaseSituations(s, s1);
        if(s1.getChildSituations() == null || s1.getChildSituations().isEmpty())
            return compareSituationWithChild(s.getChildSituations(), s1);
        else
            return compareSituationWithChild(s1.getChildSituations(), s);
    }

    private boolean compareWords(String w1, String w2) {
        //if several words in string
        if (w1.split(" ").length > 1 || w2.split(" ").length > 1)
            return w1.equals(w2);

        try {
            OntologyRelated ontologyRelated1, ontologyRelated2;
            if(ontologyRelatedCache.containsKey(w1))
                ontologyRelated1 = ontologyRelatedCache.get(w1);
            else {
                ontologyRelated1 = WordNetUtils.ontologyRelated(w1);
                ontologyRelatedCache.put(w1, ontologyRelated1);
            }
            if(ontologyRelatedCache.containsKey(w2))
                ontologyRelated2 = ontologyRelatedCache.get(w2);
            else {
                ontologyRelated2 = WordNetUtils.ontologyRelated(w2);
                ontologyRelatedCache.put(w2, ontologyRelated2);
            }

            if (ontologyRelated1.getSynsets().size() == 0 && ontologyRelated2.getSynsets().size() == 0 && ontologyRelated1.getHyps().size() == 0 && ontologyRelated2.getHyps().size() == 0)
                return w1.equals(w2);

            return ontologyRelated1.compare(ontologyRelated2) || ontologyRelated2.compare(ontologyRelated1);
        } catch (InterruptedException | IOException | URISyntaxException e) {
            return w1.equals(w2);
        }
    }

    public float countUnique(Situation s1, Situation s2) {
        Set<String> firstSituationQuestions = new LinkedHashSet<>(s1.getQuestions().keySet());
        Set<String> secondSituationQuestions = new LinkedHashSet<>(s2.getQuestions().keySet());
        firstSituationQuestions.addAll(secondSituationQuestions);
        return (float) firstSituationQuestions.size();
    }

    private float compareComplexSituations(Situation s, Situation s1) {
        List<String> visited = new ArrayList<>();

        for (Map.Entry<String, String> thisParam : s1.getQuestions().entrySet()) {
            if (s.getQuestions().containsKey(thisParam.getKey())) {
                if (compareWords(thisParam.getValue(), s.getQuestions().get(thisParam.getKey()))) {
                    visited.add(thisParam.getKey());
                }
            }
        }
        return (float) visited.size() / countUnique(s1, s);
    }

    private float compareBaseSituations(Situation s, Situation s1) {
        List<List<Integer>> sequences, subsets;
        float sum = 0.0f, maxSum = 0.0f;

        if (s.getChildSituations().size() <= s1.getChildSituations().size()) {
            sequences = SituationUtils.generateSequences(s.getChildSituations().size());
            subsets = SituationUtils.generateAllSubset(s1.getChildSituations().size(), s.getChildSituations().size());
        } else {
            sequences = SituationUtils.generateSequences(s1.getChildSituations().size());
            subsets = SituationUtils.generateAllSubset(s.getChildSituations().size(), s1.getChildSituations().size());
        }

        for (List<Integer> outerList : subsets) {
            for (List<Integer> innerCounter : sequences) {
                sum = 0.0f;
                for (int i = 0; i < outerList.size(); i++)
                    if(s.getChildSituations().size() <= s1.getChildSituations().size())
                        sum += compare(s.getChildSituations().get(innerCounter.get(i)), s1.getChildSituations().get(outerList.get(i)));
                    else
                        sum += compare(s.getChildSituations().get(outerList.get(i)), s1.getChildSituations().get(innerCounter.get(i)));
                if (maxSum < sum)
                    maxSum = sum;
            }
        }
        return 0.5f * (maxSum / s.getChildSituations().size() + maxSum / s1.getChildSituations().size());
    }

    private float compareSituationWithChild(List<Situation> c, Situation s) {
        float maxDest = 0.0f;

        for(Situation child : c) {
            float dest = compare(child, s);
            if(maxDest < dest)
                maxDest = dest;
        }
        return maxDest;
    }
}
