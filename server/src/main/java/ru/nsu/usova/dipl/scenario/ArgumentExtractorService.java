package ru.nsu.usova.dipl.scenario;

import org.springframework.data.util.Pair;
import ru.nsu.usova.dipl.situation.Situation;
import ru.nsu.usova.dipl.situation.SituationLink;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ArgumentExtractorService {
    Situation getSituationFromStatement(String src) throws IOException, InterruptedException;
    Situation extractClosestSituation(Situation s);
    Map<SituationLink, Float> findArgumentation(Situation s, float threshold);
}
