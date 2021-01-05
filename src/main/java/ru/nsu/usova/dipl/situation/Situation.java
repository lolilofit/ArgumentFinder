package ru.nsu.usova.dipl.situation;

import ru.nsu.usova.dipl.situation.ontology.WordNetUtils;
import ru.nsu.usova.dipl.situation.ontology.model.OntologyRelated;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Situation {
    private Map<String, String> params = new HashMap<>();

    private List<Situation> subsituations = new ArrayList<>();

    private WordNetUtils wordNetUtils;

    public Situation(WordNetUtils wordNetUtils) {
        this.wordNetUtils = wordNetUtils;
    }

    public boolean compareWords(String w1, String w2)  {
        try {
            OntologyRelated ontologyRelated1 = wordNetUtils.ontologyRelated(w1);
            OntologyRelated ontologyRelated2 = wordNetUtils.ontologyRelated(w2);

            return ontologyRelated1.compare(ontologyRelated2) || ontologyRelated2.compare(ontologyRelated1);
        } catch (InterruptedException | IOException | URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean equals(Object o) {
        if(!(o instanceof Situation))
            return false;
        Situation s = (Situation) o;

        if(params != null) {
            if(s.params.size() != this.params.size())
                return false;

            List<String> visited = new ArrayList<>();

            for(Map.Entry<String, String> thisParam : this.params.entrySet()) {
                for (Map.Entry<String, String> sParam : s.params.entrySet()) {
                    if (!visited.contains((thisParam.getKey()))) {
                        if (compareWords(thisParam.getValue(), sParam.getValue())) {
                            visited.add(thisParam.getKey());
                            break;
                        }
                    }
                }
            }

            if(visited.size() != this.params.size())
                return false;
        }
        else {
            if(s.subsituations.size() != this.subsituations.size())
                return false;

            List<Integer> visited = new ArrayList<>();

            for(int i = 0; i < this.subsituations.size(); i++) {
                for(int j = 0; j < s.subsituations.size(); j++) {
                    if(!visited.contains(j)) {
                        if(this.subsituations.get(i).equals(s.subsituations.get(j))) {
                            visited.add(j);
                            j = s.subsituations.size();
                        }
                    }
                }
            }

            if(visited.size() != this.subsituations.size())
                return false;
        }
        return true;
    }
}
