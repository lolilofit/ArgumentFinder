package ru.nsu.usova.dipl.situation;

import ru.nsu.usova.dipl.situation.ontology.WordNetUtils;
import ru.nsu.usova.dipl.situation.ontology.model.OntologyRelated;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Situation {
    private Map<String, String> questions = new HashMap<>();

    private List<Situation> subsituations = new ArrayList<>();

    private List<List<Integer>> r;

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

    public float compare(Situation s) {
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
            List<List<Integer>> sequences;
            float sum = 0.0f, maxSum = 0.0f;
            r = new ArrayList<>();

            if(s.subsituations.size() <= this.subsituations.size()) {
                sequences = generateSequences(s.subsituations.size());
                comb(new ArrayList<>(),0, subsituations.size(), s.subsituations.size());
            }
            else {
                sequences = generateSequences(subsituations.size());
                comb(new ArrayList<>(),0, s.subsituations.size(), subsituations.size());
            }

            for (List<Integer> outerList : r) {
                for (List<Integer> innerCounter : sequences) {
                    for (int i = 0; i < outerList.size(); i++) {
                        for (int j = 0; j < innerCounter.size(); j++)
                            sum += this.subsituations.get(i).compare(s.subsituations.get(j));
                    }

                    if (maxSum < sum)
                        maxSum = sum;
                    sum = 0.0f;
                }
            }
            return maxSum / (s.subsituations.size() + subsituations.size());
        }
    }

    public void comb(List<Integer> a, int cur, int n, int k) {
        if(a.size() == k) {
            r.add(a);
            return;
        }
        if(cur >= n)
            return;

        List<Integer> copy1 = new ArrayList<>(a);
        List<Integer> copy2 = new ArrayList<>(a);

        copy1.add(cur);
        if(copy1.size() == k) {
            r.add(copy1);
            comb(copy2, cur + 1, n, k);
            return;
        }
        comb(copy1, cur + 1, n, k);
        comb(copy2, cur + 1, n, k);
    }

    public List<List<Integer>> generateSequences(int n) {
        List<List<Integer>> newResult = new ArrayList<>();
        List<List<Integer>> result = new ArrayList<>();

        result.add(Arrays.asList(1));

        for(int i = 2; i < n; i++) {
            for (List<Integer> list : result) {
                for (int k = 0; k <= list.size(); k++) {
                    List<Integer> copyList = new ArrayList<>(list);
                    copyList.add(i);
                    newResult.add(copyList);
                }
            }
            result = newResult;
            newResult = new ArrayList<>();
        }
        return result;
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
