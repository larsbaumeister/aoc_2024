package de.baumeister.day4;

import de.baumeister.day2.DayTwoPartOne;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DayFourPartOne {
    public static void main(String[] args) throws Exception {
        var puzzle = Files.readAllLines(Path.of(DayTwoPartOne.class.getResource("/day4/input.txt").toURI())).stream()
                .map(line -> line.chars().mapToObj(c -> (char) c).toList())
                .toList();

        var count = countXmas(puzzle);
        System.out.println(count);
    }

    private static int countXmas(List<List<Character>> puzzle) {
        var count = 0;
        for (var x = 0; x < puzzle.size(); x++) {
            for (var y = 0; y < puzzle.get(x).size(); y++) {
                count += countXmasAtLocation(puzzle, new Location(x, y));
            }
        }
        return count;
    }

    private static int countXmasAtLocation(List<List<Character>> puzzle, Location start) {
        var locations = List.of(
                DIRECTION_UP, DIRECTION_DOWN, DIRECTION_LEFT, DIRECTION_RIGHT,
                DIRECTION_UP_LEFT, DIRECTION_UP_RIGHT, DIRECTION_DOWN_LEFT, DIRECTION_DOWN_RIGHT
        );
        var count = 0;
        for (var location : locations) {
            if (findWordAtLocation(puzzle, start, location, "XMAS")) {
                count++;
            }
        }
        return count;
    }

    private static boolean findWordAtLocation(List<List<Character>> puzzle, Location start, Direction direction, String word) {
        for (var i = 0; i < word.length(); i++) {
            var x = start.x() + i * direction.x();
            var y = start.y() + i * direction.y();
            if (x < 0 || x >= puzzle.size() || y < 0 || y >= puzzle.get(x).size()) {
                return false;
            }
            if (puzzle.get(x).get(y) != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    record Location(int x, int y) { }
    record Direction(int x, int y) { }

    private static final Direction DIRECTION_UP = new Direction(-1, 0);
    private static final Direction DIRECTION_DOWN = new Direction(1, 0);
    private static final Direction DIRECTION_LEFT = new Direction(0, -1);
    private static final Direction DIRECTION_RIGHT = new Direction(0, 1);
    private static final Direction DIRECTION_UP_LEFT = new Direction(-1, -1);
    private static final Direction DIRECTION_UP_RIGHT = new Direction(-1, 1);
    private static final Direction DIRECTION_DOWN_LEFT = new Direction(1, -1);
    private static final Direction DIRECTION_DOWN_RIGHT = new Direction(1, 1);


}
