package ru.nsu.usova.dipl.situation.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SituationUtils {
    public static List<List<Integer>> generateAllSubset(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>());

        for(int cur = 0; cur < n; cur++) {
            List<List<Integer>> subset = new ArrayList<>();
            for(List<Integer> existingSubset : result) {
                if(existingSubset.size() + 1 <= k) {
                    List<Integer> arr = new ArrayList<>(existingSubset);
                    arr.add(cur);
                    subset.add(arr);
                }
            }
            result.addAll(subset);
        }

        return result.stream().filter(a -> a.size() == k).collect(Collectors.toList());
    }

    public static List<List<Integer>> generateSequences(int n) {
        List<List<Integer>> newResult = new ArrayList<>();
        List<List<Integer>> result = new ArrayList<>();

        result.add(Arrays.asList(0));

        for(int i = 1; i < n; i++) {
            for (List<Integer> list : result) {
                for (int k = 0; k <= list.size(); k++) {
                    List<Integer> copyList = new ArrayList<>(list);
                    copyList.add(k, i);
                    newResult.add(copyList);
                }
            }
            result = newResult;
            newResult = new ArrayList<>();
        }
        return result;
    }
}
