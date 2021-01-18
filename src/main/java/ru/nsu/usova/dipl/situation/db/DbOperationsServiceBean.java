package ru.nsu.usova.dipl.situation.db;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.usova.dipl.situation.ReasoningConstruction;
import ru.nsu.usova.dipl.situation.Situation;
import ru.nsu.usova.dipl.situation.SituationLink;
import ru.nsu.usova.dipl.situation.db.repository.SituationLinkRepository;
import ru.nsu.usova.dipl.situation.db.repository.SituationRepository;

import java.util.List;

@Data
@RequiredArgsConstructor
@Service
public class DbOperationsServiceBean implements DbOperationsService {
    private final SituationRepository situationRepository;

    private final SituationLinkRepository situationLinkRepository;

    @Override
    public List<ReasoningConstruction> saveAllSituationsAndLinks(List<ReasoningConstruction> reasoningConstructions) {
        reasoningConstructions.forEach(r -> r.setSituationLink(saveSituationLink(r.getSituationLink())));
        return reasoningConstructions;
    }

    @Override
    public SituationLink saveSituationLink(SituationLink situationLink) {
        situationLink.setPremiseSituation(
                saveSituation(situationLink.getPremiseSituation()));
        situationLink.setResultSituation(
                saveSituation(situationLink.getResultSituation()));

        return situationLinkRepository.save(situationLink);
    }

    @Override
    public Situation saveSituation(Situation situation) {
        return situationRepository.save(situation);
    }
}
