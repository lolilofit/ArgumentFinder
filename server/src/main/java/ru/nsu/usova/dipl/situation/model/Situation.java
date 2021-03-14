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
    private List<Situation> childSituations = new ArrayList<>();

    @JsonIgnore
     @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_situations")
    private Situation parentSituations;

    @JsonIgnore
    @Transient
    private Map<String, String> questions = new HashMap<>();

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
