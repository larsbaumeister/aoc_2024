package de.baumeister.day1;

import com.google.common.collect.Streams;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class DayOnePartOne {

    public static void main(String[] args) throws URISyntaxException, IOException {
        var sum = Files.readAllLines(Path.of(DayOnePartOne.class.getResource("/day1/input.txt").toURI())).stream()
            .collect(Collectors.teeing(
                Collectors.mapping(line -> Integer.parseInt(line.split("\s+")[0]), Collectors.toList()),
                Collectors.mapping(line -> Integer.parseInt(line.split("\s+")[1]), Collectors.toList()),
                (left, right) -> Streams.zip(left.stream().sorted(), right.stream().sorted(), (l, r) -> Math.abs(l - r))
                        .mapToInt(Integer::intValue)
                        .sum()
            ));

        System.out.println(sum);

    }

}
