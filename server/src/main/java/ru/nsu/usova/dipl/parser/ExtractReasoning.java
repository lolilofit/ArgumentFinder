package ru.nsu.usova.dipl.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.fit.makhasoeva.diploma.logic.ITerm;
import ru.nsu.fit.makhasoeva.diploma.logic.impl.Predicate;
import ru.nsu.fit.makhasoeva.diploma.logic.impl.TermImpl;
import ru.nsu.fit.makhasoeva.diploma.syntax.dwarf.plain.model.WordPosition;
import ru.nsu.usova.dipl.logictext.LogicTextInteraction;
import ru.nsu.usova.dipl.parser.model.DelimInfo;
import ru.nsu.usova.dipl.situation.ReasoningConstruction;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//false <-
//true ->
public class ExtractReasoning {
    Map<String, DelimInfo> markers;

    List<String> unions = new ArrayList<>();

    ObjectMapper objectMapper = new ObjectMapper();

    List<Character> signs = new ArrayList<>();

    public ExtractReasoning() throws IOException {
        //D:\JavaProjects\dipl\server\src\main\resources\reasoningTemplates.txt
        BufferedReader markersSrc = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\JavaProjects\\dipl\\server\\src\\main\\resources\\reasoningTemplates.txt"), StandardCharsets.UTF_8));
        BufferedReader unionsSrc = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\JavaProjects\\dipl\\server\\src\\main\\resources\\unions.txt"), StandardCharsets.UTF_8));
        BufferedReader signsSrc = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\JavaProjects\\dipl\\server\\src\\main\\resources\\signs.txt"), StandardCharsets.UTF_8));

        String line;
        StringBuilder jsonMarkers = new StringBuilder();
        while ((line = markersSrc.readLine()) != null)
            jsonMarkers.append(line);

        while ((line = unionsSrc.readLine()) != null)
            unions.add(line);

        while ((line = signsSrc.readLine()) != null)
            signs.add(line.charAt(0));

        markers = objectMapper.readValue(jsonMarkers.toString(), new TypeReference<Map<String, DelimInfo>>() {
        });
    }

    private String trimSign(String src) {
        src = src.trim();

        if (signs.contains(src.charAt(src.length() - 1))) {
            src = src.substring(0, src.length() - 1);
            src = src.trim();
        }

        if (signs.contains(src.charAt(0))) {
            src = src.substring(1);
            src = src.trim();
        }

        return src;
    }

    private String removeBackUnions(String src) {
        src = trimSign(src);

        for (String union : unions) {
            if (Pattern.matches(".*" + union, src)) {
                src = src.substring(0, src.length() - union.length());
                break;
            }
        }
        return trimSign(src);
    }

    private String removeFrontUnions(String src) {
        src = trimSign(src);

        for (String union : unions) {
            if (Pattern.matches(union + ".*", src)) {
                src = src.substring(union.length() - 1);
                break;
            }
        }
        return trimSign(src);
    }

    private ReasoningConstruction parseConstruction(String src, String delim, DelimInfo info, String prevSentence, Long sentenceCount) {
        System.out.println("Parse me " + src);
        src = src.trim();
        if (src.charAt(src.length() - 1) == '.')
            src = src.substring(0, src.length() - 1);

        src = ParserUtils.makeFirstSymbolLower(src);
        String[] tokens = src.split(delim);

        if (tokens.length != 2)
            return null;

        if (prevSentence != null && tokens[0].equals(""))
            return new ReasoningConstruction(
                    prevSentence,
                    removeFrontUnions(tokens[1]),
                    true, 1L,
                    sentenceCount - 1,
                    sentenceCount
            );

        if (!Pattern.matches(".*[\\wа-я]+.*", tokens[0]) || !Pattern.matches(".*[\\wа-я]+.*", tokens[1]))
            return null;

        if (info.getDirection()) {
            return new ReasoningConstruction(
                    removeBackUnions(tokens[0]),
                    removeFrontUnions(tokens[1]),
                    info.getDirection(),
                    ParserUtils.getCommasCount(tokens[0]),
                    sentenceCount,
                    sentenceCount
            );
        } else {
            return new ReasoningConstruction(
                    removeBackUnions(tokens[1]),
                    removeFrontUnions(tokens[0]),
                    info.getDirection(),
                    ParserUtils.getCommasCount(tokens[0]),
                    sentenceCount,
                    sentenceCount
            );
        }
    }

    private static long getWordCount(ReasoningConstruction reasoningConstruction) {
        long firstPartCounter = 0L;

        if (reasoningConstruction.getDirection())
            firstPartCounter = Arrays.stream(reasoningConstruction.getSituationLink().getPremise().trim().split(" ")).filter(Objects::nonNull).count();
        else
            firstPartCounter = Arrays.stream(reasoningConstruction.getSituationLink().getResult().trim().split(" ")).filter(Objects::nonNull).count();

        return firstPartCounter + reasoningConstruction.getFirstPartCommasCount() + 1;
    }

    public static Map<WordPosition, Predicate> convertAdjectiveToVerb(Map<WordPosition, Predicate> map, List<WordPosition> excludeList) {
        return map.entrySet().stream().peek(entry -> {
            if (entry.getValue().getArguments().size() == 1 && entry.getValue().getArguments().containsKey("objectRole") && !excludeList.contains(entry.getKey())) {
                LinkedHashMap<String, ITerm> arguments = new LinkedHashMap<>();

                //arguments.put("actionRole", new TermImpl("являться_0", ITerm.VariableType.VARIABLE));
                arguments.put("objectRole", entry.getValue().getArguments().get("objectRole"));
                arguments.put("каким", new TermImpl(entry.getValue().getName(), ITerm.VariableType.CONSTANT));

                Predicate predicate = new Predicate("являться", arguments);
                entry.setValue(predicate);
            }
        }).collect(Collectors.toMap(Map.Entry<WordPosition, Predicate>::getKey, Map.Entry<WordPosition, Predicate>::getValue));
    }

    public static Map<WordPosition, Predicate> filterPredicates(LogicTextInteraction logicTextInteraction, java.util.function.Predicate<Map.Entry<WordPosition, Predicate>> p) {
        return convertAdjectiveToVerb(
                logicTextInteraction
                        .getPredicatesMap()
                        .entrySet()
                        .stream()
                        .filter(p)
                        .collect(Collectors.toMap(Map.Entry<WordPosition, Predicate>::getKey, Map.Entry<WordPosition, Predicate>::getValue)),
                new ArrayList<>(logicTextInteraction.getResolvedReferences().keySet())
        );
    }

    public static void extractLogicTextOutput(ReasoningConstruction reasoningConstruction, LogicTextInteraction logicTextInteraction) {
        long secondPart = getWordCount(reasoningConstruction);

        Map<WordPosition, Predicate> firstPredicates = filterPredicates(
                logicTextInteraction,
                (e) -> (!logicTextInteraction.getResolvedReferences().containsKey(new WordPosition(e.getKey().getSentence(), e.getKey().getWord())))
                        && (e.getKey().getSentence() == reasoningConstruction.getPremiseSentenceCount()
                        && (reasoningConstruction.getPremiseSentenceCount() != reasoningConstruction.getResultSentenceCount()
                        || e.getKey().getWord() < secondPart))
        );

        Map<WordPosition, Predicate> secondPredicates = filterPredicates(
                logicTextInteraction,
                (e) -> (!logicTextInteraction.getResolvedReferences().containsKey(new WordPosition(e.getKey().getSentence(), e.getKey().getWord())))
                        && (e.getKey().getSentence() == reasoningConstruction.getResultSentenceCount()
                        && (reasoningConstruction.getPremiseSentenceCount() != reasoningConstruction.getResultSentenceCount()
                        || e.getKey().getWord() >= secondPart))
        );

        if (reasoningConstruction.getDirection()) {
            reasoningConstruction.getPremisePredicates().putAll(firstPredicates);
            reasoningConstruction.getResultPredicates().putAll(secondPredicates);
        } else {
            reasoningConstruction.getPremisePredicates().putAll(secondPredicates);
            reasoningConstruction.getResultPredicates().putAll(firstPredicates);
        }
    }

    public List<ReasoningConstruction> parseReasoning(Map<String, LogicTextInteraction> paragraphsMap) {
        List<ReasoningConstruction> result = new ArrayList<>();

        String paragraph;
        String prevSentence = null;

        long sentenceCounter = 0;

        for (Map.Entry<String, LogicTextInteraction> pEntry : paragraphsMap.entrySet()) {
            paragraph = pEntry.getKey();

            String[] sentences = paragraph.split("\\.");

            for (String sentence : sentences) {
                for (Map.Entry<String, DelimInfo> entry : markers.entrySet()) {
                    if (sentence.toLowerCase().contains(entry.getKey())) {
                        ReasoningConstruction reasoningConstruction = parseConstruction(sentence, entry.getKey(), entry.getValue(), prevSentence, sentenceCounter);
                        if (reasoningConstruction != null) {
                            result.add(reasoningConstruction);

                            extractLogicTextOutput(reasoningConstruction, pEntry.getValue());
                            break;
                        }
                    }
                }
                prevSentence = sentence;
                sentenceCounter++;
            }
            sentenceCounter = 0;
        }
        return result;
    }
}
