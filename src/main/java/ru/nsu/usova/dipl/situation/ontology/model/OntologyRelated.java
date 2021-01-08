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

    public boolean compare(OntologyRelated ontologyRelated) {
        for(int i = 0; i < synsets.size(); i++) {
            for(int j = 0; j < ontologyRelated.getSynsets().size(); j++)
                if(synsets.get(i).equals(ontologyRelated.getSynsets().get(j)))
                    return true;
            for(int j = 0; j < ontologyRelated.getHyps().size(); j++)
                if(synsets.get(i).equals(ontologyRelated.getHyps().get(j)))
                    return true;
        }
        for(int i = 0; i < hyps.size(); i++) {
            for(int j = 0; j < ontologyRelated.getSynsets().size(); j++)
                if(hyps.get(i).equals(ontologyRelated.getSynsets().get(j)))
                    return true;
            for(int j = 0; j < ontologyRelated.getHyps().size(); j++)
                if(hyps.get(i).equals(ontologyRelated.getHyps().get(j)))
                    return true;
        }
        return false;
    }
}
