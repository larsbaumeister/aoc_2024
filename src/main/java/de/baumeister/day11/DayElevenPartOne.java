package de.baumeister.day11;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DayElevenPartOne {

    public static void main(String[] args) throws Exception {
        var str = Files.readString(Path.of(DayElevenPartOne.class.getResource("/day11/input.txt").toURI()));
        var numbers = Arrays.stream(str.split("\\s+")).map(Long::parseLong).toList();
        for (var i = 0; i < 25; i++) {
            numbers = blink(numbers);
        }
        System.out.println(numbers.size());
    }

    public static List<Long> blink(List<Long> numbers) {
        var copy = new ArrayList<>(numbers);
        for (var i = 0; i < copy.size(); i++) {
            var elem = copy.get(i);
            var elemStr = String.valueOf(elem);
            if (elem == 0) {
                copy.set(i, 1L);
            } else if (elemStr.length() % 2 == 0) {
                copy.set(i, Long.parseLong(elemStr.substring(0, elemStr.length() / 2)));
                copy.add(++i, Long.parseLong(elemStr.substring(elemStr.length() / 2)));
            } else {
                copy.set(i, elem * 2024);
            }
        }
        return copy;
    }
    
}
