package ru.nsu.usova.dipl.ontology.model;

import lombok.Data;
import ru.nsu.usova.dipl.situation.model.metric.SamePartRelationType;
import ru.nsu.usova.dipl.situation.model.metric.SituationMetric;
import ru.nsu.usova.dipl.situation.model.metric.StructuralRelationType;

import java.util.ArrayList;
import java.util.List;

@Data
public class OntologyRelated {
    private final List<String> synsets = new ArrayList<>();

    private final List<String> hyps = new ArrayList<>();

    private boolean compareWithList(List<String> words, OntologyRelated ontologyRelated) {
        for (String word : words) {
            for (int j = 0; j < ontologyRelated.getSynsets().size(); j++)
                if (word.equals(ontologyRelated.getSynsets().get(j)))
                    return true;
            for (int j = 0; j < ontologyRelated.getHyps().size(); j++)
                if (word.equals(ontologyRelated.getHyps().get(j)))
                    return true;
        }
        return false;
    }

    public void compare(OntologyRelated ontologyRelated, SituationMetric metric) {
        if(compareWithList(synsets, ontologyRelated)) {
            metric.setDistance(1.0f);
            metric.setSamePartRelationType(SamePartRelationType.SIMILAR);
        }
        if(compareWithList(hyps, ontologyRelated)) {
           metric.setDistance(1.0f);
           metric.setSamePartRelationType(SamePartRelationType.GENERALIZATION);
        }
    }
}
