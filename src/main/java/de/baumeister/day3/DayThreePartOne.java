package de.baumeister.day3;

import de.baumeister.day2.DayTwoPartOne;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class DayThreePartOne {
    public static void main(String[] args) throws URISyntaxException, IOException {
        var code = Files.readString(Path.of(DayTwoPartOne.class.getResource("/day3/input.txt").toURI()));
        var matcher = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)").matcher(code);

        long res = 0;
        while (matcher.find()) {
            res += (long) Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
        }
        System.out.println(res);
    }
}
