package ru.nsu.usova.dipl.logictext;

import ru.nsu.fit.makhasoeva.diploma.logic.ITerm;
import ru.nsu.fit.makhasoeva.diploma.logic.impl.Predicate;
import ru.nsu.fit.makhasoeva.diploma.syntax.MarkupText;
import ru.nsu.fit.makhasoeva.diploma.syntax.MarkupTextPart;
import ru.nsu.fit.makhasoeva.diploma.syntax.TextStructureAnalyzerFactory;
import ru.nsu.fit.makhasoeva.diploma.syntax.dwarf.plain.model.WordPosition;
import ru.nsu.fit.makhasoeva.diploma.utils.Config;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class LogicTextInteraction extends MarkupTextPart {

    public LogicTextInteraction(String text) throws IOException, InterruptedException {
        super(TextStructureAnalyzerFactory.getInstance().analyze(text), new MarkupText());
        Locale.setDefault(new Locale(Config.getLocale()));
    }

    public static LogicTextInteraction getSentencePredicates(String text) throws IOException, InterruptedException {
        LogicTextInteraction markupTextPart = new LogicTextInteraction(text);
        System.out.println(markupTextPart.getPredicates().toString());
        return markupTextPart;
    }

    public Map<WordPosition, ITerm> getResolvedReferences() {
        return resolvedReferences;
    }

    public Map<WordPosition, Predicate> getPredicatesMap() {
        return  predicates;
    }
}
