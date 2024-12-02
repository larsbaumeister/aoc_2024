package de.baumeister.day2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DayTwoPartTwo {

    public static void main(String[] args) throws URISyntaxException, IOException {
         var count = Files.readAllLines(Path.of(DayTwoPartOne.class.getResource("/day2/input.txt").toURI())).stream()
                .map(line -> new LinkedList<>(Arrays.stream(line.split("\s+")).map(Integer::parseInt).toList()))
                .filter(numbers -> isAscending(numbers) || isDescending(numbers))
                .count();

        System.out.println(count);
    }

    private static boolean isAscending(List<Integer> numbers) {
        return isDescOrAsc(false, numbers, 1);
    }

    private static boolean isDescending(List<Integer> numbers) {
        return isDescOrAsc(true, numbers, 1);
    }

    private static boolean isDescOrAsc(boolean desc, List<Integer> numbers, int allowedRemovals) {
        var prevElem = numbers.getFirst();
        for (var i = 1; i < numbers.size(); i++) {
            var currentElem = numbers.get(i);
            var distance = Math.abs(prevElem - currentElem);
            if ((desc ? prevElem <= currentElem : prevElem >= currentElem) || distance > 3) {
                if (allowedRemovals > 0) {
                    var cp1 = new LinkedList<>(numbers);
                    cp1.remove(i);

                    var cp2 = new LinkedList<>(numbers);
                    cp2.remove(i - 1);
                    return isDescOrAsc(desc, cp1, allowedRemovals - 1) || isDescOrAsc(desc, cp2, allowedRemovals - 1);
                }
                return false;
            }
            prevElem = currentElem;
        }
        return true;
    }


}
