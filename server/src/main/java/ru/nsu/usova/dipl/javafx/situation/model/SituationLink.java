package ru.nsu.usova.dipl.javafx.situation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "reasoning")
public class SituationLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private Long id;

    @ManyToOne

    @JoinColumn(name="premise_situation_id", nullable=false)
    protected Situation premiseSituation = new Situation();

    @ManyToOne
    @JoinColumn(name="result_situation_id", nullable=false)
    protected Situation resultSituation = new Situation();

    @Column(name = "premise")
    protected String premise;

    @Column(name = "result")
    protected String result;
}
