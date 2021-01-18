package ru.nsu.usova.dipl.scenario;

import ru.nsu.usova.dipl.situation.Situation;
import ru.nsu.usova.dipl.situation.SituationLink;

public interface ArgumentExtractorService {
    Situation extractClosestSituation(Situation s);

    SituationLink findArgumentation(Situation s);
}
