package ru.nsu.usova.dipl.situation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Situation {
    public Map<String, String> params = new HashMap<>();

    public List<Situation> subsituations = new ArrayList<>();

    public Situation() {

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
                for(Map.Entry<String, String> sParam : s.params.entrySet()) {
                    if(!visited.contains((sParam.getKey()))) {

                        //if(thisParam.getValue().equals(s.params.get(j))) {
                            visited.add(sParam.getValue());
                            break;
                        //}
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
