package ru.nsu.usova.dipl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.usova.dipl.logictext.LogicTextInteraction;
import ru.nsu.usova.dipl.parser.ExtractReasoning;
import ru.nsu.usova.dipl.parser.TextExtractor;
import ru.nsu.usova.dipl.parser.model.ReasoningConstruction;
import ru.nsu.usova.dipl.situation.Situation;
import ru.nsu.usova.dipl.situation.SituationRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
public class SituationMining {
    @Autowired
    private SituationRepository situationRepository;

    static Map<String, LogicTextInteraction> getText() throws Exception {
        TextExtractor textExtractor = new TextExtractor();
        return textExtractor.extractParagraphsFromFile();
    }

    @PostConstruct
    public void extractSituations() throws Exception {
        Map<String, LogicTextInteraction> source = getText();

        ExtractReasoning reasoning = new ExtractReasoning();

        List<ReasoningConstruction> reasoningConstructionList = reasoning.parseReasoning(source);
/*
        reasoningConstructionList.forEach(p -> {
            System.out.println(p.getPremise() + " -> " + p.getResult());

            for (Map.Entry<WordPosition, Predicate> e : p.getPremisePredicates().entrySet())
                System.out.println(e.getValue());
            System.out.println("\n");

            for (Map.Entry<WordPosition, Predicate> e : p.getResultPredicates().entrySet())
                System.out.println(e.getValue());
            System.out.println("------------");
        });
*/
        reasoningConstructionList.forEach(ReasoningConstruction::convertToSituations);

        //      reasoningConstructionList.forEach(ReasoningConstruction::print);
        //TextExtractor textExtractor = new TextExtractor();
        //Map<String, LogicTextInteraction> sen =  textExtractor.extractParagraphsFromString("Части аллергических реакций можно будет избежать.");

        System.out.println(reasoningConstructionList.get(1).getPremiseSituation().getChildSituations().get(1).compare(reasoningConstructionList.get(2).getPremiseSituation().getChildSituations().get(0)));
        System.out.println(reasoningConstructionList.get(1).getPremiseSituation().compare(reasoningConstructionList.get(1).getPremiseSituation()));

        reasoningConstructionList.forEach(c -> situationRepository.save(c.getPremiseSituation()));
        List<Situation> s = situationRepository.findAll();
        System.out.println("");
    }

    public void setSituationRepository(SituationRepository situationRepository) {
        this.situationRepository = situationRepository;
    }

    public SituationRepository getSituationRepository() {
        return situationRepository;
    }
}
