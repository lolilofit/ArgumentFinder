package ru.nsu.usova.dipl.situation.ontology.model;

import java.util.ArrayList;
import java.util.List;

public class OntologyRelated {
    private List<String> synsets = new ArrayList<>();

    private List<String> hyps = new ArrayList<>();

    public List<String> getHyps() {
        return hyps;
    }

    public List<String> getSynsets() {
        return synsets;
    }

    public void setHyps(List<String> hyps) {
        this.hyps = hyps;
    }

    public void setSynsets(List<String> synsets) {
        this.synsets = synsets;
    }
}
