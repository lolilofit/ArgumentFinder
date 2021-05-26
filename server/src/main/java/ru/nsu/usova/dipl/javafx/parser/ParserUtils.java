package ru.nsu.usova.dipl.javafx.parser;

import java.util.Arrays;
import java.util.Objects;

public class ParserUtils {
    static long getPhraseLength(String src) {
        return Arrays.stream(src.split(" ")).filter(Objects::nonNull).count();
    }

    static String makeFirstSymbolLower(String src) {
        return src.replaceFirst(Character.toString(src.charAt(0)), Character.toString(src.charAt(0)).toLowerCase());
    }

    static long getCommasCount(String src) {
        return src.chars().filter(ch -> ch == ',').count();
    }
}
