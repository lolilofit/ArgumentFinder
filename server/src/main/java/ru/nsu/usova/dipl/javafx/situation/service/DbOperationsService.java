package ru.nsu.usova.dipl.javafx.situation.service;

import ru.nsu.usova.dipl.javafx.situation.model.ReasoningConstruction;
import ru.nsu.usova.dipl.javafx.situation.model.Situation;
import ru.nsu.usova.dipl.javafx.situation.model.SituationLink;

import java.util.List;


public interface DbOperationsService {
    List<ReasoningConstruction> saveAllSituationsAndLinks(List<ReasoningConstruction> reasoningConstructions);

    SituationLink saveSituationLink(SituationLink situationLink);

    Situation saveSituation(Situation situation);
}
