package ru.nsu.usova.dipl.scenario;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import ru.nsu.usova.dipl.controllers.model.LoadTextInfo;
import ru.nsu.usova.dipl.logictext.LogicTextInteraction;
import ru.nsu.usova.dipl.parser.ExtractReasoning;
import ru.nsu.usova.dipl.parser.TextExtractor;
import ru.nsu.usova.dipl.situation.model.ReasoningConstruction;
import ru.nsu.usova.dipl.situation.service.DbOperationsService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Data
@RequiredArgsConstructor
public class SituationMining {
    //private final SituationRepository situationRepository;
    //private final SituationLinkRepository situationLinkRepository;
    private final ApplicationArguments applicationArguments;

    private final ArgumentExtractorService argumentExtractorService;

    private final DbOperationsService dbOperationsService;

    public LoadTextInfo extractSituationsByText(String text) {
        try {
            TextExtractor textExtractor = new TextExtractor();
            return extractSituation(textExtractor.extractParagraphsFromString(text));
        } catch (Exception e) {
            return new LoadTextInfo(0);
        }
    }

    @PostConstruct
    public void initialLoad() {
        extractSituationsByFile(null);
    }

    public LoadTextInfo extractSituationsByFile(String fileName) {
        try {
            List<String> args = applicationArguments.getNonOptionArgs();
            TextExtractor textExtractor;

            if (args.size() == 0)
                textExtractor = new TextExtractor(Objects.requireNonNullElse(fileName, "D:\\JavaProjects\\dipl\\server\\src\\main\\resources\\src.txt"));
            else
                textExtractor = new TextExtractor(args.get(0));

            return extractSituation(textExtractor.extractParagraphsFromFile());
        } catch (Exception e) {
            e.printStackTrace();
            return new LoadTextInfo(0);
        }
    }

    public LoadTextInfo extractSituation(Map<String, LogicTextInteraction> source) throws IOException {
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
        argumentExtractorService.findArgumentation(reasoningConstructionList.get(1).getSituationLink().getResultSituation(), 0);
 */
        dbOperationsService.saveAllSituationsAndLinks(reasoningConstructionList);

        return new LoadTextInfo(reasoningConstructionList.size());
    }
}
