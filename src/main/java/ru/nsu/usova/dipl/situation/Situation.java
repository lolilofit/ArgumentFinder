package ru.nsu.usova.dipl.situation;

import ru.nsu.usova.dipl.situation.ontology.WordNetUtils;
import ru.nsu.usova.dipl.situation.ontology.model.OntologyRelated;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Situation {
    private Map<String, String> questions = new HashMap<>();

    private List<Situation> subsituations = new ArrayList<>();

    private boolean compareWords(String w1, String w2) {
        try {
            OntologyRelated ontologyRelated1 = WordNetUtils.ontologyRelated(w1);
            OntologyRelated ontologyRelated2 = WordNetUtils.ontologyRelated(w2);

            if(ontologyRelated1.getSynsets().size() == 0 && ontologyRelated2.getSynsets().size() == 0 && ontologyRelated1.getHyps().size() == 0 && ontologyRelated2.getHyps().size() == 0)
                return w1.equals(w2);

            return ontologyRelated1.compare(ontologyRelated2) || ontologyRelated2.compare(ontologyRelated1);
        } catch (InterruptedException | IOException | URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

    public float countUnique(Situation s1, Situation s2) {
        Set<String> firstSituationQuestions = new LinkedHashSet<>(s1.questions.keySet());
        Set<String> secondSituationQuestions = new LinkedHashSet<>(s2.questions.keySet());
        firstSituationQuestions.addAll(secondSituationQuestions);
        return (float) firstSituationQuestions.size();
    }

    public float compare(Object o) {
        if (!(o instanceof Situation))
            return 0.0f;
        Situation s = (Situation) o;

        if (questions != null) {
            //обработка несовпадающих ситуаций
            List<String> visited = new ArrayList<>();

            for (Map.Entry<String, String> thisParam : this.questions.entrySet()) {
                if (s.getQuestions().containsKey(thisParam.getKey())) {
                    if (compareWords(thisParam.getValue(), s.getQuestions().get(thisParam.getKey()))) {
                        visited.add(thisParam.getKey());
                    }
                }
            }
                return (float) visited.size() / countUnique(this, s);
        } else {
            if (s.subsituations.size() != this.subsituations.size())
                return 0.0f;

            List<Integer> visited = new ArrayList<>();

            for (int i = 0; i < this.subsituations.size(); i++) {
                for (int j = 0; j < s.subsituations.size(); j++) {
                    if (!visited.contains(j)) {
                        if (this.subsituations.get(i).equals(s.subsituations.get(j))) {
                            visited.add(j);
                            j = s.subsituations.size();
                        }
                    }
                }
            }

            //исправить!!!
            return (float) visited.size();
        }
    }

    public List<Situation> getSubsituations() {
        return subsituations;
    }

    public Map<String, String> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<String, String> questions) {
        this.questions = questions;
    }

    public void setSubsituations(List<Situation> subsituations) {
        this.subsituations = subsituations;
    }

    private void printWithIndent(int indentNumber) {
        if (subsituations != null)
            subsituations.forEach(s -> s.printWithIndent(indentNumber + 1));
        if (questions != null)
            questions.forEach((question, answer) -> {
                StringBuilder indent = new StringBuilder();
                for (int i = 0; i < indentNumber; i++)
                    indent.append(" ");

                System.out.println(indent.toString() + question + " -> " + answer);
            });
        System.out.println();
    }

    public void print() {
        System.out.println("-----");
        printWithIndent(0);
        System.out.println("-----");
    }
}
