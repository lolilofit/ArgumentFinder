package ru.nsu.usova.dipl.situation.service;

import ru.nsu.usova.dipl.situation.model.Situation;

public interface SituationCompareService {
    float compare(Situation s, Situation s1);
}
