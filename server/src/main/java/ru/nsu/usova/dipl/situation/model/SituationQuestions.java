package ru.nsu.usova.dipl.situation.model;

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
    private String key;

    @Column(name = "_value")
    private String value;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "situation_id")
    private Situation situation;

    public SituationQuestions(String key, String value, Situation situation) {
        this.key = key;
        this.value = value;
        this.situation = situation;
    }
}
