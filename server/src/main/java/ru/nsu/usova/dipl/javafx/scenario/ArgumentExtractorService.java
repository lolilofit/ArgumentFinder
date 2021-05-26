package ru.nsu.usova.dipl.javafx.scenario;

import ru.nsu.usova.dipl.javafx.scenario.model.LinkMetric;
import ru.nsu.usova.dipl.javafx.situation.model.Situation;

import java.io.IOException;
import java.util.List;

public interface ArgumentExtractorService {
    Situation getSituationFromStatement(String src) throws IOException, InterruptedException;
    Situation extractClosestSituation(Situation s);
    List<LinkMetric> findArgumentation(Situation s, float threshold);
}
