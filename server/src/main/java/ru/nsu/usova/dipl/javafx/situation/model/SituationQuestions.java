package ru.nsu.usova.dipl.javafx.situation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "situation_questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SituationQuestions {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "_key")
    private String questionKey;

    @Column(name = "_value")
    private String questionValue;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "situation_id")
    private Situation situation;

    public SituationQuestions(String questionKey, String questionValue, Situation situation) {
        this.questionKey = questionKey;
        this.questionValue = questionValue;
        this.situation = situation;
    }
}
