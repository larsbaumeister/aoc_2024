package de.baumeister.day1;

import com.google.common.collect.Streams;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class DayOne {

    public static void main(String[] args) throws URISyntaxException, IOException {
        var leftList = new ArrayList<Integer>();
        var rightList = new ArrayList<Integer>();
        for (var line : Files.readAllLines(Path.of(DayOne.class.getResource("/day1/input.txt").toURI()))) {
            var split = line.split("\s+");
            leftList.add(Integer.parseInt(split[0]));
            rightList.add(Integer.parseInt(split[1]));
        }

        var sum = Streams.zip(
                leftList.stream().sorted(),
                rightList.stream().sorted(),
                (l, r) -> Math.abs(l - r))
            .mapToInt(Integer::intValue)
            .sum();

        System.out.println(sum);

    }

}
