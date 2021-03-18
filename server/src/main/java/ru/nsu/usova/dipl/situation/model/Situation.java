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

    private void printWithIndent(int indentNumber) {
        if (childSituations != null)
            childSituations.forEach(s -> s.printWithIndent(indentNumber + 1));
        if (questionsList != null)
            questionsList.forEach(q -> {
                System.out.println(" ".repeat(Math.max(0, indentNumber)) + q.getKey() + " -> " + q.getValue());
            });
        System.out.println();
    }

    public void print() {
        System.out.println("-----");
        printWithIndent(0);
        System.out.println("-----");
    }

    public void setChildSituations(List<Situation> childSituations) {
        this.childSituations = childSituations;
        childSituations.forEach(s -> s.setParentSituations(this));
    }

    public void setQuestionsList(Map<String, String> m) {
        Optional.of(m).ifPresent(map -> {
            map.forEach((k, v) -> {
                questionsList.add(new SituationQuestions(k, v, this));
            });
        });
    }

    @Override
    public String toString() {
        return "";
    }
}
