package ru.nsu.usova.dipl.situation;

import ru.nsu.usova.dipl.situation.ontology.WordNetUtils;
import ru.nsu.usova.dipl.situation.ontology.model.OntologyRelated;

import javax.persistence.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Entity
@Table(name = "situation")
public class Situation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "situation_questions")
    @MapKeyColumn(name = "key")
    @Column(name = "questions")
    private Map<String, String> questions = new HashMap<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Column(name = "child_situations")
    private List<Situation> childSituations = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    @Column(name = "parent_situations")
    private List<Situation> parentSituations = new ArrayList<>();

    @Transient
    private List<List<Integer>> r;

    private boolean compareWords(String w1, String w2) {
        //if several words in string
        try {
            OntologyRelated ontologyRelated1 = WordNetUtils.ontologyRelated(w1);
            OntologyRelated ontologyRelated2 = WordNetUtils.ontologyRelated(w2);

            if(ontologyRelated1.getSynsets().size() == 0 && ontologyRelated2.getSynsets().size() == 0 && ontologyRelated1.getHyps().size() == 0 && ontologyRelated2.getHyps().size() == 0)
                return w1.equals(w2);

            return ontologyRelated1.compare(ontologyRelated2) || ontologyRelated2.compare(ontologyRelated1);
        } catch (InterruptedException | IOException | URISyntaxException e) {
            e.printStackTrace();
            return w1.equals(w2);
        }
    }

    public float countUnique(Situation s1, Situation s2) {
        Set<String> firstSituationQuestions = new LinkedHashSet<>(s1.questions.keySet());
        Set<String> secondSituationQuestions = new LinkedHashSet<>(s2.questions.keySet());
        firstSituationQuestions.addAll(secondSituationQuestions);
        return (float) firstSituationQuestions.size();
    }

    public float compare(Situation s) {
        if (questions != null && questions.size() != 0) {
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

            if(s.childSituations.size() <= this.childSituations.size()) {
                sequences = generateSequences(s.childSituations.size());
                comb(new ArrayList<>(),0, childSituations.size(), s.childSituations.size());
            }
            else {
                sequences = generateSequences(childSituations.size());
                comb(new ArrayList<>(),0, s.childSituations.size(), childSituations.size());
            }

            for (List<Integer> outerList : r) {
                for (List<Integer> innerCounter : sequences) {
                    for (int i = 0; i < outerList.size(); i++)
                        sum += this.childSituations.get(outerList.get(i)).compare(s.childSituations.get(innerCounter.get(i)));

                    if (maxSum < sum)
                        maxSum = sum;
                    sum = 0.0f;
                }
            }
            return 0.5f * (maxSum / s.childSituations.size() + maxSum / childSituations.size());
        }
    }

    private void comb(List<Integer> a, int cur, int n, int k) {
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

    private List<List<Integer>> generateSequences(int n) {
        List<List<Integer>> newResult = new ArrayList<>();
        List<List<Integer>> result = new ArrayList<>();

        result.add(Arrays.asList(0));

        for(int i = 1; i < n; i++) {
            for (List<Integer> list : result) {
                for (int k = 0; k <= list.size(); k++) {
                    List<Integer> copyList = new ArrayList<>(list);
                    copyList.add(k, i);
                    newResult.add(copyList);
                }
            }
            result = newResult;
            newResult = new ArrayList<>();
        }
        return result;
    }

    public List<Situation> getChildSituations() {
        return childSituations;
    }

    public Map<String, String> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<String, String> questions) {
        this.questions = questions;
    }

    public void setChildSituations(List<Situation> childSituations) {
        this.childSituations = childSituations;
    }

    private void printWithIndent(int indentNumber) {
        if (childSituations != null)
            childSituations.forEach(s -> s.printWithIndent(indentNumber + 1));
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
