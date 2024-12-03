package de.baumeister.day3;

import de.baumeister.day2.DayTwoPartOne;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class DayThreePartTwo {

    public static void main(String[] args) throws Exception {
        var code = Files.readString(Path.of(DayTwoPartOne.class.getResource("/day3/input.txt").toURI()));
        var res = CodeRunner.runCode(code);
        System.out.println(res);
    }

    static class CodeRunner {
        boolean active = true;
        StringBuilder currentWord = new StringBuilder();
        Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        long sum = 0;

        public static long runCode(String code) {
            var runner = new CodeRunner();
            runner.run(code);
            return runner.sum;
        }

        private void run(String code) {
            code.chars().forEach(c -> nextToken((char) c));
        }

        private void nextToken(char token) {
            currentWord.append(token);
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
    }
}
