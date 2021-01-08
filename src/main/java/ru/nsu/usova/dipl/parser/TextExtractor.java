package ru.nsu.usova.dipl.parser;

import ru.nsu.usova.dipl.logictext.LogicTextInteraction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextExtractor {
    private Scanner src;

    public TextExtractor() throws FileNotFoundException {
        src = new Scanner(new File("src/main/resources/src.txt")).useDelimiter("<--->");
    }

    private void proceedParagraph(Map<String, LogicTextInteraction> result, String paragraph) throws IOException, InterruptedException {
        paragraph = paragraph.trim().replace("\n", "");

        LogicTextInteraction markupTextPart = LogicTextInteraction.getSentencePredicates(paragraph);

        result.put(paragraph, markupTextPart);
    }

    public Map<String, LogicTextInteraction> extractParagraphsFromFile() throws Exception {
        String paragraph;
        Map<String, LogicTextInteraction> result = new HashMap<>();

        while(src.hasNext()) {
            paragraph = src.next();
            proceedParagraph(result, paragraph);
        }
        return result;
    }

    public Map<String, LogicTextInteraction> extractParagraphsFromString(String paragraph) throws IOException, InterruptedException {
        Map<String, LogicTextInteraction> result = new HashMap<>();
        proceedParagraph(result, paragraph);
        return result;
    }
}
