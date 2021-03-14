package ru.nsu.usova.dipl.situation.model;

import lombok.Data;
import ru.nsu.fit.makhasoeva.diploma.logic.impl.Predicate;
import ru.nsu.fit.makhasoeva.diploma.syntax.dwarf.plain.model.WordPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

@Data
public class ReasoningConstruction {
    //важен порядок
    private static final Map<String, String> OCCASION_PATTERNS = Map.of(".*objectRoleEnding_\\d+", "objectRoleEnding", ".*objectRoleEnding", "objectRoleEnding", ".*_\\d+", "_");

    private Boolean direction;

    private long firstPartCommasCount = 0;

    private Map<WordPosition, Predicate> premisePredicates;

    private Map<WordPosition, Predicate> resultPredicates;

    private long premiseSentenceCount;

    private long resultSentenceCount;

    private SituationLink situationLink;

    public ReasoningConstruction() {
        this.situationLink = new SituationLink();
        this.premisePredicates = new HashMap<>();
        this.resultPredicates = new HashMap<>();
    }

    public ReasoningConstruction(String premise,
                                 String result,
                                 Boolean direction,
                                 Long linkWordsNumber,
                                 Long premiseSentenceCount,
                                 Long resultSentenceCount
    ) {
        this.direction = direction;
        this.firstPartCommasCount = linkWordsNumber;
        this.premiseSentenceCount = premiseSentenceCount;
        this.resultSentenceCount = resultSentenceCount;
        this.situationLink = new SituationLink();
        this.situationLink.setPremise(premise);
        this.situationLink.setResult(result);

        this.premisePredicates = new HashMap<>();
        this.resultPredicates = new HashMap<>();
    }

    private String cleanWord(String word) {
        AtomicReference<String> result = new AtomicReference<>();
        result.set(word);

        OCCASION_PATTERNS.forEach((pattern, splitter) -> {
            if (Pattern.matches(pattern, result.get())) {
                String[] parts = result.get().split(splitter);
                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append(parts[0]);
                for (int i = 1; i < parts.length - 1; i++)
                    stringBuilder.append(" ").append(parts[i]);
                result.set(stringBuilder.toString());
            }
        });
        return result.get().replace("_", " ");
    }

    private void convertOneMapToSituation(Map<WordPosition, Predicate> predicates, Situation mainSituation) {
        List<Situation> situations = new ArrayList<>();
        predicates.forEach((position, predicate) -> {
            Situation s = new Situation();
            Map<String, String> params = new HashMap<>();

            params.put("Основное действие", predicate.getName());
            predicate.getArguments().forEach((argument, term) -> {
                if (!argument.equals("actionRole"))
                    params.put(argument, cleanWord(term.getName()));
            });
            s.setQuestions(params);
            situations.add(s);
        });
        mainSituation.setChildSituations(situations);
    }

    public void convertToSituations() {
        if (premisePredicates != null)
            convertOneMapToSituation(premisePredicates, situationLink.getPremiseSituation());
        if (resultPredicates != null)
            convertOneMapToSituation(resultPredicates, situationLink.getResultSituation());
    }

    public void print() {
        System.out.println("--construction--");
        situationLink.getPremiseSituation().print();
        situationLink.getResultSituation().print();
        System.out.println();
    }
}
