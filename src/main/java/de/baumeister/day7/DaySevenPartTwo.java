package de.baumeister.day7;

import de.baumeister.day2.DayTwoPartOne;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DaySevenPartTwo {

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
            var toConsume = new ArrayDeque<>(numbers.reversed());
            var current = new HashSet<Long>();
            current.add(result);
            while (!toConsume.isEmpty() && !current.isEmpty()) {
                var num = toConsume.poll();
                var next = new HashSet<Long>();
                for (var v : current) {
                    if (Objects.equals(v, num) && toConsume.isEmpty()) {
                        return true;
                    }
                    if (v > num) {
                        long changed = v - num;
                        next.add(changed);
                    }
                    if (v % num == 0) {
                        long changed = v / num;
                        next.add(changed);
                    }
                    long revConc = reverseConcat(v, num);
                    if (revConc != v) {
                        next.add(revConc);
                    }
                }
                current = next;
            }
            return false;
        }

        private long reverseConcat(long left, long right) {
            if (left == right) return 0;

            var ls = left + "";
            var rs = right + "";
            if (ls.endsWith(rs)) {
                return Long.parseLong(ls.substring(0, ls.length() - rs.length()));
            }
            return left;
        }

    }
}
