package ru.nsu.usova.dipl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SituationQuestions {
    private String questionKey;

    private String questionValue;
}
