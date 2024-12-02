package de.baumeister.day2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class DayTwoPartOne {

    public static void main(String[] args) throws URISyntaxException, IOException {

        var count = Files.readAllLines(Path.of(DayTwoPartOne.class.getResource("/day2/input.txt").toURI())).stream()
                .map(line -> Arrays.stream(line.split("\s+")).mapToInt(Integer::parseInt).toArray())
                .filter(numbers -> isAscending(numbers) || isDescending(numbers))
                .count();

        System.out.println(count);

    }

    private static boolean isAscending(int[] numbers) {
        return isDescOrAsc(false, numbers);
    }

    private static boolean isDescending(int[] numbers) {
        return isDescOrAsc(true, numbers);
    }

    private static boolean isDescOrAsc(boolean desc, int[] numbers) {
        var prevElem = numbers[0];
        for (var i = 1; i < numbers.length; i++) {
            var distance = Math.abs(prevElem - numbers[i]);
            var currentElem = numbers[i];
            if ((desc ? prevElem <= currentElem : prevElem >= currentElem) || distance > 3) {
                return false;
            }
            prevElem = numbers[i];
        }
        return true;
    }

}
