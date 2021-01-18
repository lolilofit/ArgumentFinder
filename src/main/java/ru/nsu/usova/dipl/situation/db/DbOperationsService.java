package ru.nsu.usova.dipl.situation.db;

import ru.nsu.usova.dipl.situation.ReasoningConstruction;
import ru.nsu.usova.dipl.situation.Situation;
import ru.nsu.usova.dipl.situation.SituationLink;

import java.util.List;


public interface DbOperationsService {
    List<ReasoningConstruction> saveAllSituationsAndLinks(List<ReasoningConstruction> reasoningConstructions);

    SituationLink saveSituationLink(SituationLink situationLink);

    Situation saveSituation(Situation situation);
}
