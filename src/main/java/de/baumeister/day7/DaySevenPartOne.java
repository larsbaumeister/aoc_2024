package de.baumeister.day7;

import de.baumeister.day2.DayTwoPartOne;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DaySevenPartOne {

    public static void main(String[] args) throws Exception {
        var sum = Files.readAllLines(Path.of(DayTwoPartOne.class.getResource("/day7/input.txt").toURI())).stream()
            .map(Calibration::from)
            .filter(Calibration::isValid)
            .mapToLong(Calibration::result)
            .sum();

        System.out.println(sum);
    }

    private record Calibration(long result, List<Long> numbers) {
        static Calibration from(String line) {
            var s = line.split(": ");
            return new Calibration(
                Long.parseLong(s[0]),
                Arrays.stream(s[1].split(" ")).map(Long::parseLong).toList()
            );
        }

        public boolean isValid() {
            var reversedNumbers = new ArrayDeque<>(numbers.reversed());
            var intermediateResults = new HashSet<Long>();
            intermediateResults.add(result);
            while (!reversedNumbers.isEmpty() && !intermediateResults.isEmpty()) {
                var number = reversedNumbers.poll();
                var next = new HashSet<Long>();
                for (var res : intermediateResults) {
                    if (Objects.equals(res, number) && reversedNumbers.isEmpty()) {
                        return true;
                    }
                    if (res > number) {
                        long changed = res - number;
                        next.add(changed);
                    }
                    if (res % number == 0) {
                        long changed = res / number;
                        next.add(changed);
                    }
                }
                intermediateResults = next;
            }
            return false;
        }

    }
}
