package ru.nsu.usova.dipl;

import ru.nsu.fit.makhasoeva.diploma.logic.impl.Predicate;
import ru.nsu.fit.makhasoeva.diploma.syntax.dwarf.plain.model.WordPosition;
import ru.nsu.usova.dipl.logictext.LogicTextInteraction;
import ru.nsu.usova.dipl.parser.ExtractReasoning;
import ru.nsu.usova.dipl.parser.TextExtractor;
import ru.nsu.usova.dipl.parser.model.ReasoningConstruction;

import java.util.List;
import java.util.Map;

public class Main {
    static Map<String, LogicTextInteraction> getText() throws Exception {
        TextExtractor textExtractor = new TextExtractor();
        return textExtractor.extractText();
    }

    public static void main(String[] args) throws Exception {
        LogicTextInteraction l = LogicTextInteraction.getSentencePredicates("Если человеку предложить уже очищенный мандарин, части аллергических реакций можно будет избежать.");

        Map<String, LogicTextInteraction> source = getText();

        ExtractReasoning reasoning = new ExtractReasoning();

        List<ReasoningConstruction> reasoningConstructionList = reasoning.parseReasoning(source);

        reasoningConstructionList.forEach(p -> {
            System.out.println(p.getPremise() + " -> " + p.getResult());

            for(Map.Entry<WordPosition, Predicate> e : p.getPremisePredicates().entrySet())
                System.out.println(e.getValue());
            System.out.println("\n");

            for(Map.Entry<WordPosition, Predicate> e : p.getResultPredicates().entrySet())
                System.out.println(e.getValue());
            System.out.println("------------");
        });
    }
}
