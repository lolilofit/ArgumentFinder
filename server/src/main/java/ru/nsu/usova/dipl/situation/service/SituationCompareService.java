package ru.nsu.usova.dipl.situation.service;

import ru.nsu.usova.dipl.situation.model.Situation;
import ru.nsu.usova.dipl.situation.model.metric.SituationMetric;

public interface SituationCompareService {
    SituationMetric compare(Situation s, Situation s1);
}
