package de.baumeister.day3;

import de.baumeister.day2.DayTwoPartOne;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class DayThreePartTwo {

    public static void main(String[] args) throws Exception {
        var code = Files.readString(Path.of(DayTwoPartOne.class.getResource("/day3/input.txt").toURI()));

        var active = true;
        var currentWord = new StringBuilder();
        var pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");

        long sum = 0;
        for (var i = 0; i < code.length(); i++) {
            var c = code.charAt(i);
            currentWord.append(c);
            if (currentWord.toString().endsWith("do()")) {
                active = true;
                currentWord = new StringBuilder();
            } else if (currentWord.toString().endsWith("don't()")) {
                active = false;
                currentWord = new StringBuilder();
            } else if (active) {
                var matcher = pattern.matcher(currentWord);
                while (matcher.find()) {
                    sum += (long) Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
                    currentWord = new StringBuilder();
                }
            }
        }

        System.out.println(sum);
    }

}
