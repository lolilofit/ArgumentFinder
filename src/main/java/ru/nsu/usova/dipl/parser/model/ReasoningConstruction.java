package ru.nsu.usova.dipl.parser.model;

import ru.nsu.fit.makhasoeva.diploma.logic.impl.Predicate;
import ru.nsu.fit.makhasoeva.diploma.syntax.dwarf.plain.model.WordPosition;
import ru.nsu.usova.dipl.situation.Situation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class ReasoningConstruction {
    //важен порядок
    private static final Map<String, String> OCCASION_PATTERNS = Map.of(".*objectRoleEnding", "objectRoleEnding", ".*_\\d+", "_");

    private String premise;

    private String result;

    private Boolean direction;

    private long firstPartCommasCount = 0;

    private Map<WordPosition, Predicate> premisePredicates;

    private Map<WordPosition, Predicate> resultPredicates;

    private Situation premiseSituation;

    private Situation resultSituation;

    private long premiseSentenceCount;

    private long resultSentenceCount;

    public ReasoningConstruction(String premise,
                                 String result,
                                 Boolean direction,
                                 Long linkWordsNumber,
                                 Long premiseSentenceCount,
                                 Long resultSentenceCount
    ) {
        this.premise = premise;
        this.result = result;
        this.direction = direction;
        this.firstPartCommasCount = linkWordsNumber;
        this.premiseSentenceCount = premiseSentenceCount;
        this.resultSentenceCount = resultSentenceCount;

        premisePredicates = new HashMap<>();
        resultPredicates = new HashMap<>();
    }

    private String cleanWord(String word) {
        AtomicReference<String> result = new AtomicReference<>();
        result.set(word);

        OCCASION_PATTERNS.forEach((pattern, splitter) -> {
            if(Pattern.matches(pattern, result.get())) {
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
        premiseSituation = new Situation();
        resultSituation = new Situation();

        if (premisePredicates != null)
            convertOneMapToSituation(premisePredicates, premiseSituation);
        if (resultPredicates != null)
            convertOneMapToSituation(resultPredicates, resultSituation);
    }

    public String getPremise() {
        return premise;
    }

    public String getResult() {
        return result;
    }

    public Boolean getDirection() {
        return direction;
    }

    public long getFirstPartCommasCount() {
        return firstPartCommasCount;
    }

    public long getPremiseSentenceCount() {
        return premiseSentenceCount;
    }

    public long getResultSentenceCount() {
        return resultSentenceCount;
    }

    public Map<WordPosition, Predicate> getPremisePredicates() {
        return premisePredicates;
    }

    public Map<WordPosition, Predicate> getResultPredicates() {
        return resultPredicates;
    }

    public Situation getPremiseSituation() {
        return premiseSituation;
    }

    public Situation getResultSituation() {
        return resultSituation;
    }

    public void print() {
        System.out.println("--construction--");
        premiseSituation.print();
        resultSituation.print();
        System.out.println();
    }
}
