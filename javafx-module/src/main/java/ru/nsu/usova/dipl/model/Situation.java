package ru.nsu.usova.dipl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Situation {
    private Long id;

    private List<SituationQuestions> questionsList;

    private List<Situation> childSituations;
}
