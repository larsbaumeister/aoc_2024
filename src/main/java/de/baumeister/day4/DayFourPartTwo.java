package de.baumeister.day4;

import de.baumeister.day2.DayTwoPartOne;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DayFourPartTwo {

    public static void main(String[] args) throws Exception {
        var puzzle = Files.readAllLines(Path.of(DayTwoPartOne.class.getResource("/day4/input.txt").toURI())).stream()
                .map(line -> line.chars().mapToObj(c -> (char) c).toList())
                .toList();

        var count = countXmas(puzzle);
        System.out.println(count);
    }

    private static int countXmas(List<List<Character>> puzzle) {
        var count = 0;
        for (var x = 1; x < puzzle.size() - 1; x++) {
            for (var y = 1; y < puzzle.get(x).size() - 1; y++) {
                count += countXmasAtLocation(puzzle, new Location(x, y));
            }
        }
        return count;
    }

    private static int countXmasAtLocation(List<List<Character>> puzzle, Location middle) {
        var lul = new Location(middle.x() - 1, middle.y() - 1);
        var rul = new Location(middle.x() - 1, middle.y() + 1);
        var ldl = new Location(middle.x() + 1, middle.y() - 1);
        var rdl = new Location(middle.x() + 1, middle.y() + 1);

        var lu = puzzle.get(lul.x()).get(lul.y());
        var ru = puzzle.get(rul.x()).get(rul.y());
        var ld = puzzle.get(ldl.x()).get(ldl.y());
        var rd = puzzle.get(rdl.x()).get(rdl.y());
        var c = puzzle.get(middle.x()).get(middle.y());

        if (lu == 'M' && ru == 'S' && ld == 'M' && rd == 'S' && c == 'A') {
            return 1;
        }
        if (lu == 'S' && ru == 'M' && ld == 'S' && rd == 'M' && c == 'A') {
            return 1;
        }
        if (lu == 'M' && ru == 'M' && ld == 'S' && rd == 'S' && c == 'A') {
            return 1;
        }
        if (lu == 'S' && ru == 'S' && ld == 'M' && rd == 'M' && c == 'A') {
            return 1;
        }

        return 0;
    }

    record Location(int x, int y) { }


}
