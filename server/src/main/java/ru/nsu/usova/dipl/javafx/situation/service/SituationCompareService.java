package ru.nsu.usova.dipl.javafx.situation.service;

import ru.nsu.usova.dipl.javafx.situation.model.metric.SituationMetric;
import ru.nsu.usova.dipl.javafx.situation.model.Situation;

public interface SituationCompareService {
    SituationMetric compare(Situation s, Situation s1);
}
