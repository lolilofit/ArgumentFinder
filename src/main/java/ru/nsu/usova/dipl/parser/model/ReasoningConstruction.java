package ru.nsu.usova.dipl.parser.model;

import ru.nsu.fit.makhasoeva.diploma.logic.impl.Predicate;
import ru.nsu.fit.makhasoeva.diploma.syntax.dwarf.plain.model.WordPosition;

import java.util.HashMap;
import java.util.Map;

public class ReasoningConstruction {
    private String premise;

    private String result;

    private Boolean direction;

    private long firstPartCommasCount = 0;

    private Map<WordPosition, Predicate> premisePredicates;

    private Map<WordPosition, Predicate> resultPredicates;

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

    public String getPremise() {
        return premise;
    }

    public String getResult() {
        return result;
    }

    public Boolean getDirection() { return direction; }

    public long getFirstPartCommasCount() { return firstPartCommasCount; }

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
}
