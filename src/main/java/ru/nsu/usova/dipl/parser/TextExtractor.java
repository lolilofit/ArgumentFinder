package ru.nsu.usova.dipl.parser;

import ru.nsu.usova.dipl.logictext.LogicTextInteraction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextExtractor {
    private Scanner src;

    public TextExtractor() throws FileNotFoundException {
        src = new Scanner(new File("src/main/resources/src.txt")).useDelimiter("<--->");
    }

    public Map<String, LogicTextInteraction> extractText() throws Exception {
        String paragraph;

        Map<String, LogicTextInteraction> result = new HashMap<>();

        while(src.hasNext()) {
            paragraph = src.next();
            paragraph = paragraph.trim().replace("\n", "");

            LogicTextInteraction markupTextPart = LogicTextInteraction.getSentencePredicates(paragraph);

            result.put(paragraph, markupTextPart);
        }

        return result;
    }
}
