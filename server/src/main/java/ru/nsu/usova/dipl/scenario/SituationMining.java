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
    private final ApplicationArguments applicationArguments;

    private final ArgumentExtractorService argumentExtractorService;

    private final DbOperationsService dbOperationsService;

    private final ExtractReasoning extractReasoning;

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
        List<ReasoningConstruction> reasoningConstructionList = extractReasoning.parseReasoning(source);

        reasoningConstructionList.forEach(ReasoningConstruction::convertToSituations);
        dbOperationsService.saveAllSituationsAndLinks(reasoningConstructionList);

        return new LoadTextInfo(reasoningConstructionList.size());
    }
}
