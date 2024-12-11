package de.baumeister.day11;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DayElevenPartTwo {

    public static void main(String[] args) throws Exception {
        var str = Files.readString(Path.of(DayElevenPartOne.class.getResource("/day11/input.txt").toURI()));
        var numbers = Arrays.stream(str.split("\\s+")).map(Long::parseLong).toList();
        System.out.println(countStonesAfterBlinks(numbers, 75));
    }

    public static long countStonesAfterBlinks(List<Long> numbers, int blinks) {
        var stoneCounts = new HashMap<String, Long>();
        for (var num : numbers) {
            stoneCounts.put(String.valueOf(num), 1L);
        }

        for (var i = 0; i < blinks; i++) {
            var nextCounts = new HashMap<String, Long>();
            for (var entry : stoneCounts.entrySet()) {
                String key = entry.getKey();
                var count = entry.getValue();
                var num = Long.parseLong(key);

                if (num == 0) {
                    nextCounts.put("1", nextCounts.getOrDefault("1", 0L) + count);
                } else if (key.length() % 2 == 0) {
                    String left = String.valueOf(Long.parseLong(key.substring(0, key.length() / 2)));
                    String right = String.valueOf(Long.parseLong(key.substring(key.length() / 2)));
                    nextCounts.put(left, nextCounts.getOrDefault(left, 0L) + count);
                    nextCounts.put(right, nextCounts.getOrDefault(right, 0L) + count);
                } else {
                    long newNum = num * 2024;
                    nextCounts.put(String.valueOf(newNum), nextCounts.getOrDefault(String.valueOf(newNum), 0L) + count);
                }
            }
            stoneCounts = nextCounts;
        }

        return stoneCounts.values().stream().mapToLong(Long::longValue).sum();
    }

}
