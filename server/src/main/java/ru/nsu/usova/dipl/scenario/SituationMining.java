package ru.nsu.usova.dipl.scenario;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import ru.nsu.usova.dipl.logictext.LogicTextInteraction;
import ru.nsu.usova.dipl.parser.ExtractReasoning;
import ru.nsu.usova.dipl.parser.TextExtractor;
import ru.nsu.usova.dipl.situation.ReasoningConstruction;
import ru.nsu.usova.dipl.situation.db.DbOperationsService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
@Data
@RequiredArgsConstructor
public class SituationMining {
    //private final SituationRepository situationRepository;
    //private final SituationLinkRepository situationLinkRepository;
    private final ApplicationArguments applicationArguments;

    private final ArgumentExtractorService argumentExtractorService;

    private final DbOperationsService dbOperationsService;

    private Map<String, LogicTextInteraction> getText() throws Exception {
        String[] args = applicationArguments.getSourceArgs();

        TextExtractor textExtractor;

        if(args.length == 0)
            textExtractor = new TextExtractor();
        else
            textExtractor = new TextExtractor(args[0]);

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
/*
        situationRepository.save(reasoningConstructionList.get(0).getSituationLink().getPremiseSituation());
        situationRepository.save(reasoningConstructionList.get(0).getSituationLink().getResultSituation());

        situationLinkRepository.save(reasoningConstructionList.get(0).getSituationLink());
        Iterable<SituationLink> l = situationLinkRepository.findAll();

        System.out.println("");

        System.out.println(reasoningConstructionList.get(1).getSituationLink().getPremiseSituation().getChildSituations().get(1).compare(reasoningConstructionList.get(2).getSituationLink().getPremiseSituation().getChildSituations().get(0)));
        System.out.println(reasoningConstructionList.get(1).getSituationLink().getPremiseSituation().compare(reasoningConstructionList.get(1).getSituationLink().getPremiseSituation()));

        reasoningConstructionList.forEach(c -> situationRepository.save(c.getSituationLink().getPremiseSituation()));
        List<Situation> s = situationRepository.findAll();
        System.out.println("");

 */
        dbOperationsService.saveAllSituationsAndLinks(reasoningConstructionList);

        //argumentExtractorService.findArgumentation(reasoningConstructionList.get(1).getSituationLink().getResultSituation(), 0);
    }
}