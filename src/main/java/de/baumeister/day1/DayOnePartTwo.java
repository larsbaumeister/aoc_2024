package de.baumeister.day1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DayOnePartTwo {

    public static void main(String[] args) throws URISyntaxException, IOException {
        var leftList = new ArrayList<Integer>();
        var rightList = new ArrayList<Integer>();
        for (var line : Files.readAllLines(Path.of(DayOnePartOne.class.getResource("/day1/input.txt").toURI()))) {
            var split = line.split("\s+");
            leftList.add(Integer.parseInt(split[0]));
            rightList.add(Integer.parseInt(split[1]));
        }

        var counts = rightList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        var sum = leftList.stream()
                .mapToInt(i -> i * counts.getOrDefault(i, 0L).intValue())
                .sum();

        System.out.println(sum);

    }

}
