package de.baumeister.day2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

public class DayTwoPartOne {

    public static void main(String[] args) throws URISyntaxException, IOException {

        var count = Files.readAllLines(Path.of(DayTwoPartOne.class.getResource("/day2/input.txt").toURI())).stream()
//        var count = Stream.of("7 6 4 2 1", "1 2 7 8 9", "9 7 6 2 1", "1 3 2 4 5", "8 6 4 4 1", "1 3 6 7 9")
                .map(line -> Arrays.stream(line.split("\s+")).mapToInt(Integer::parseInt).toArray())
                .filter(numbers -> isAscending(numbers) || isDescending(numbers))
                .count();

        System.out.println(count);

    }

    private static boolean isAscending(int[] numbers) {
        var prevElem = numbers[0];
        for (var i = 1; i < numbers.length; i++) {
            var distance = Math.abs(prevElem - numbers[i]);
            if (prevElem >= numbers[i] || distance > 3) {
                return false;
            }
            prevElem = numbers[i];
        }
        return true;
    }

    private static boolean isDescending(int[] numbers) {
        var prevElem = numbers[0];
        for (var i = 1; i < numbers.length; i++) {
            var distance = Math.abs(prevElem - numbers[i]);
            if (prevElem <= numbers[i] || distance > 3) {
                return false;
            }
            prevElem = numbers[i];
        }
        return true;
    }

}
