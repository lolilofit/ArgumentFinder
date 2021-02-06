package ru.nsu.usova.dipl.parser;

import ru.nsu.usova.dipl.logictext.LogicTextInteraction;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextExtractor {
    private BufferedReader src;

    public TextExtractor() throws FileNotFoundException {
    }

    public TextExtractor(String filename) throws FileNotFoundException, UnsupportedEncodingException {
        System.out.println("Open file " + filename);
        src = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8));
    }

    private void proceedParagraph(Map<String, LogicTextInteraction> result, String paragraph) throws IOException, InterruptedException {
        paragraph = paragraph.trim().replace("\n", "");

        LogicTextInteraction markupTextPart = LogicTextInteraction.getSentencePredicates(paragraph);

        result.put(paragraph, markupTextPart);
    }

    public Map<String, LogicTextInteraction> extractParagraphsFromFile() throws Exception {
        StringBuffer paragraph = new StringBuffer();
        String line;
        Map<String, LogicTextInteraction> result = new HashMap<>();

        while ((line = src.readLine()) != null) {
            if (line.equals("<--->")) {
                proceedParagraph(result, paragraph.toString());
                paragraph = new StringBuffer();
            }
            else
                paragraph.append(line);
        }
        return result;
    }

    public Map<String, LogicTextInteraction> extractParagraphsFromString(String paragraph) throws IOException, InterruptedException {
        String[] p = paragraph.split("<--->");
        Map<String, LogicTextInteraction> result = new HashMap<>();

        for (String s : p)
            proceedParagraph(result, s);
        return result;
    }
}
