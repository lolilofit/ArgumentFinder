package ru.nsu.usova.dipl.scenario;

import ru.nsu.usova.dipl.situation.Situation;

public interface ArgumentExtractorService {
    Situation extractClosestSituation(Situation s);
}
