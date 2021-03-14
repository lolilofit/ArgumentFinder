package ru.nsu.usova.dipl.situation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.nsu.usova.dipl.ontology.WordNetUtils;
import ru.nsu.usova.dipl.ontology.model.OntologyRelated;
import ru.nsu.usova.dipl.situation.SituationUtils;

import javax.persistence.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Entity
@Table(name = "situation")
@Data
public class Situation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "situation")
    private List<SituationQuestions> questionsList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentSituations")
    //@Column(name = "child_situations")
    private List<Situation> childSituations = new ArrayList<>();

    @JsonIgnore
    //@OneToMany(fetch = FetchType.EAGER)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_situations")
    //@Column(name = "parent_situations")
    private Situation parentSituations;

    @JsonIgnore
    @Transient
    private Map<String, OntologyRelated> ontologyRelatedCache = new HashMap<>();

    @JsonIgnore
    @Transient
    private Map<String, String> questions = new HashMap<>();

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
        Set<String> firstSituationQuestions = new LinkedHashSet<>(s1.questions.keySet());
        Set<String> secondSituationQuestions = new LinkedHashSet<>(s2.questions.keySet());
        firstSituationQuestions.addAll(secondSituationQuestions);
        return (float) firstSituationQuestions.size();
    }

    public float compare(Situation s) {
        if (questions != null && !questions.isEmpty() && s.questions != null && !s.questions.isEmpty()) {
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
        }
        if(childSituations != null && !childSituations.isEmpty() && s.childSituations != null && !s.childSituations.isEmpty()) {
            List<List<Integer>> sequences, subsets;
            float sum = 0.0f, maxSum = 0.0f;

            if (s.childSituations.size() <= this.childSituations.size()) {
                sequences = SituationUtils.generateSequences(s.childSituations.size());
                subsets = SituationUtils.generateAllSubset(childSituations.size(), s.childSituations.size());
            } else {
                sequences = SituationUtils.generateSequences(childSituations.size());
                subsets = SituationUtils.generateAllSubset(s.childSituations.size(), childSituations.size());
            }


            for (List<Integer> outerList : subsets) {
                for (List<Integer> innerCounter : sequences) {
                    for (int i = 0; i < outerList.size(); i++)
                        if(s.childSituations.size() <= this.childSituations.size())
                            sum += this.childSituations.get(outerList.get(i)).compare(s.childSituations.get(innerCounter.get(i)));
                        else
                            sum += this.childSituations.get(innerCounter.get(i)).compare(s.childSituations.get(outerList.get(i)));

                    if (maxSum < sum)
                        maxSum = sum;
                    sum = 0.0f;
                }
            }
            return 0.5f * (maxSum / s.childSituations.size() + maxSum / childSituations.size());
        }
        if(childSituations == null || childSituations.isEmpty())
            return compareSituationWithChild(s.childSituations, this);
        else
            return compareSituationWithChild(childSituations, s);
    }
    public float compareSituationWithChild(List<Situation> c, Situation s) {
        float maxDest = 0.0f;

        for(Situation child : c) {
            float dest = s.compare(child);
            if(maxDest < dest)
                maxDest = dest;
        }
        return maxDest;
    }

    private void printWithIndent(int indentNumber) {
        if (childSituations != null)
            childSituations.forEach(s -> s.printWithIndent(indentNumber + 1));
        if (questions != null)
            questions.forEach((question, answer) -> {
                StringBuilder indent = new StringBuilder();
                indent.append(" ".repeat(Math.max(0, indentNumber)));

                System.out.println(indent.toString() + question + " -> " + answer);
            });
        System.out.println();
    }

    public void print() {
        System.out.println("-----");
        printWithIndent(0);
        System.out.println("-----");
    }

    public void setQuestions(Map<String, String> questions) {
        this.questions = questions;
        questions.forEach((key, value) -> questionsList.add(new SituationQuestions(key, value, this)));
    }
    
    public void setChildSituations(List<Situation> childSituations) {
        this.childSituations = childSituations;
        childSituations.forEach(s -> s.setParentSituations(this));
    }
}