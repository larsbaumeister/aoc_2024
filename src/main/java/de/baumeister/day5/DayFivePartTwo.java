package de.baumeister.day5;

import de.baumeister.day2.DayTwoPartOne;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DayFivePartTwo {


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
            .filter(update -> !update.stream()
                .flatMap(i -> rules.get(i).stream().map(ruleItem -> Map.entry(i, ruleItem)))
                .noneMatch(entry -> {
                    var idx = update.indexOf(entry.getValue());
                    return idx != -1 && idx < update.indexOf(entry.getKey());
                }))
            .map(update -> update.stream().sorted((o1, o2) -> rules.get(o2).contains(o1) ? -1 : 0).toList())
            .mapToInt(update -> update.get(update.size() / 2))
            .sum();

        System.out.println(sum);
    }

}
