package ru.nsu.usova.dipl.javafx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Situation {
    private Long id;

    private List<SituationQuestions> questionsList;

    private List<Situation> childSituations;
}
