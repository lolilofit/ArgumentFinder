package ru.nsu.usova.dipl.situation.ontology.model;

import java.util.ArrayList;
import java.util.List;

public class OntologyRelated {
    private final List<String> synsets = new ArrayList<>();

    private final List<String> hyps = new ArrayList<>();

    public List<String> getHyps() {
        return hyps;
    }

    public List<String> getSynsets() {
        return synsets;
    }

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

    public boolean compare(OntologyRelated ontologyRelated) {
        return compareWithList(synsets, ontologyRelated) || compareWithList(hyps, ontologyRelated);
    }
}
