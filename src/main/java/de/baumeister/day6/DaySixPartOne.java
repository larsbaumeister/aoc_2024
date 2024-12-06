package de.baumeister.day6;

import de.baumeister.day2.DayTwoPartOne;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DaySixPartOne {

    public static void main(String[] args) throws Exception {
        var puzzle = Files.readAllLines(Path.of(DayTwoPartOne.class.getResource("/day6/input.txt").toURI())).stream()
                .map(line -> line.chars().mapToObj(c -> (char) c).toList())
                .toList();

        var visited = new Boolean[puzzle.size()][puzzle.get(0).size()];

        walk(puzzle, visited);

        var count = Arrays.stream(visited)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .filter(Boolean::booleanValue)
                .count();

        System.out.println(count);

    }

    private static void walk(List<List<Character>> puzzle, Boolean[][] visited) {
        var pos = findStart(puzzle);
        var direction = DIRECTION_UP;
        while (pos.y() >= 0 && pos.y() < puzzle.size() && pos.x() >= 0 && pos.x() < puzzle.get(pos.y()).size()) {
            visited[pos.y()][pos.x()] = true;
            var next = pos.move(direction, 1);
            if (next.y() < 0 || next.y() >= puzzle.size() || next.x() < 0 || next.x() >= puzzle.get(next.y()).size()) {
                break;
            }
            var nextChar = puzzle.get(next.y()).get(next.x());
            if (nextChar == '#') {
                direction = direction.rotate90DegreesRight();
                pos = pos.move(direction, 1);
            } else {
                pos = next;
            }
        }
    }

    private static Location findStart(List<List<Character>> puzzle) {
        for (var y = 0; y < puzzle.size(); y++) {
            for (var x = 0; x < puzzle.get(y).size(); x++) {
                if (puzzle.get(y).get(x) == '^') {
                    return new Location(y, x);
                }
            }
        }
        throw new IllegalStateException("No start found");
    }

    record Location(int y, int x) {
        public Location move(Direction direction, int steps) {
            return new Location(y() + direction.y() * steps, x() + direction.x() * steps);
        }
    }
    record Direction(int y, int x) {
        public Direction rotate90DegreesRight() {
            return new Direction(x(), -y());
        }
    }

    private static final Direction DIRECTION_UP = new Direction(-1, 0);
}
