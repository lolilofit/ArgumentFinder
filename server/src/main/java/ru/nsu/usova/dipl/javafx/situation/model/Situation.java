package ru.nsu.usova.dipl.javafx.situation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "situation")
@Data
public class Situation {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "situation")
    private List<SituationQuestions> questionsList = new ArrayList<>();

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
                System.out.println(" ".repeat(Math.max(0, indentNumber)) + q.getQuestionKey() + " -> " + q.getQuestionValue());
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
