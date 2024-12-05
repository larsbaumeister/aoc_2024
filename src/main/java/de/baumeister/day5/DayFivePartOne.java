package de.baumeister.day5;

import de.baumeister.day2.DayTwoPartOne;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DayFivePartOne {

    public static void main(String[] args) throws Exception {
        var code = Files.readString(Path.of(DayTwoPartOne.class.getResource("/day5/input.txt").toURI()));
        var sections = code.split("\r\n\r\n");

        var rules = sections[0].lines()
            .map(line -> line.split("\\|"))
            .collect(Collectors.toMap(
                split -> Integer.parseInt(split[0]),
                split -> (List<Integer>) new ArrayList<>(List.of(Integer.valueOf(split[1]))),
                (a, b) -> { a.addAll(b); return a; }
            ));

        var sum = sections[1].lines()
            .map(line -> Stream.of(line.split(",")).map(Integer::parseInt).toList())
            .filter(update -> updateIsValid(update, rules))
            .mapToInt(update -> update.get(update.size() / 2))
            .sum();

        System.out.println(sum);
    }

    private static boolean updateIsValid(List<Integer> update, Map<Integer, List<Integer>> rules) {
        for (var i = 0; i < update.size(); i++) {
            var item = update.get(i);
            var rule = rules.get(item);

            for (var ruleItem : rule) {
                var idx = update.indexOf(ruleItem);
                if (idx != -1 && idx < i) {
                    return false;
                }
            }
        }
        return true;
    }

}
