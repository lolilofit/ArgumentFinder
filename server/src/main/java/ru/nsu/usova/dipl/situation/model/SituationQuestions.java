package ru.nsu.usova.dipl.situation.model;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "_key")
    private String key;

    @Column(name = "_value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "situation_id")
    private Situation situation;

    public SituationQuestions(String key, String value, Situation situation) {
        this.key = key;
        this.value = value;
        this.situation = situation;
    }
}
