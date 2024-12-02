package de.baumeister.day1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DayOnePartTwo {

    public static void main(String[] args) throws URISyntaxException, IOException {
        var res = Files.readAllLines(Path.of(DayOnePartOne.class.getResource("/day1/input.txt").toURI())).stream()
            .map(line -> line.split("\s+"))
            .collect(Collectors.teeing(
                Collectors.mapping(split -> Integer.parseInt(split[0]), Collectors.toList()),
                Collectors.mapping(split -> Integer.parseInt(split[1]), Collectors.toList()),
                (left, right) -> {
                    var counts = right.stream()
                            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

                    return left.stream()
                            .mapToInt(i -> i * counts.getOrDefault(i, 0L).intValue())
                            .sum();
                }
            ));

        System.out.println(res);
    }

}
