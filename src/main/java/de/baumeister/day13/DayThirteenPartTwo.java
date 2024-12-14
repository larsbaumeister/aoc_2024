package de.baumeister.day13;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.regex.Pattern;

public class DayThirteenPartTwo {

    public static void main(String[] args) throws Exception {
        var s = Files.readString(Path.of(DayThirteenPartOne.class.getResource("/day13/input.txt").toURI()));
        var puzzels = Arrays.stream(s.split(System.lineSeparator() + System.lineSeparator()))
                .map(Puzzle::from)
                .mapToLong(Puzzle::calculatePrice)
                .sum();

        System.out.println(puzzels);
    }

    record Puzzle(long x1, long y1, long x2, long y2, long px, long py) {
        private static final Pattern PATTERN = Pattern.compile(".*X(\\+|=)(?<X>\\d+), Y(\\+|=)(?<Y>\\d+)");
        public static Puzzle from(String s) {
            var numbers = Arrays.stream(s.split(System.lineSeparator()))
                    .map(l -> {
                        var m = PATTERN.matcher(l);
                        m.find();
                        return new long[] {Long.parseLong(m.group("X")), Long.parseLong(m.group("Y"))};
                    })
                    .toList();

            return new Puzzle(numbers.get(0)[0], numbers.get(0)[1], numbers.get(1)[0], numbers.get(1)[1], numbers.get(2)[0] + 10000000000000L, numbers.get(2)[1] + 10000000000000L);
        }

        public long calculatePrice() {
            long b = (py * x1 - px * y1) / (y2 * x1 - x2 * y1);
            long a = (px - b * x2) / x1;

            if (a * x1 + b * x2 == px && a * y1 + b * y2 == py) {
                return a * 3 + b;
            }
            return 0;
        }
    }
}
