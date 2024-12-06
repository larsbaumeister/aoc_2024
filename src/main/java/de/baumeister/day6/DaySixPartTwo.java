package de.baumeister.day6;

import de.baumeister.day2.DayTwoPartOne;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DaySixPartTwo {

    public static void main(String[] args) throws Exception {
        var puzzle = Puzzle.fromCharArray(Files.lines(Paths.get(DayTwoPartOne.class.getResource("/day6/input.txt").toURI()))
                .map(String::toCharArray)
                .toArray(char[][]::new));

        var count = puzzle.walkPath(null).visitedPositions.stream()
                .map(s -> s.position)
                .distinct()
                .filter(o -> !puzzle.start.equals(o) && puzzle.walkPath(o).isCircle)
                .count();

        System.out.println(count);
    }


    record Puzzle(char[][] puzzle, Position start, Set<Position> obstruction) {

        static Puzzle fromCharArray(char[][] puzzle) {
            var obstructions = new HashSet<Position>();
            Position start = null;

            for(var y = 0; y < puzzle.length; y++) {
                for(int x = 0; x < puzzle[0].length; x++) {
                    if(puzzle[y][x] == '#') {
                        obstructions.add(new Position(x, y));
                    } else if(puzzle[y][x] == '^') {
                        start = new Position(x, y);
                    }
                }
            }
            return new Puzzle(puzzle, start, obstructions);
        }

        PathResult walkPath(Position newObstacle) {
            var current = new PositionDirection(start, new Position(0, -1));
            var visited = new HashSet<PositionDirection>();
            while (isValid(current.position) && !visited.contains(current)) {
                var next = current.position.add(current.direction);
                if(obstruction.contains(next) || next.equals(newObstacle)) {
                    current = new PositionDirection(current.position, current.direction.turnRight());
                } else {
                    visited.add(current);
                    current = new PositionDirection(next, current.direction);
                }
            }
            return new PathResult(visited, visited.stream().map(s->s.position).collect(Collectors.toSet()).size(), isValid(current.position));
        }

        boolean isValid(Position position) {
            return 0 <= position.x && position.x < puzzle[0].length && 0 <= position.y && position.y < puzzle.length;
        }

    }

    record Position(int x, int y) {
        Position turnRight() {
            return new Position(-y, x);
        }
        Position add(Position other) {
            return new Position(other.x + x, other.y + y);
        }
    }

    record PositionDirection(Position position, Position direction) {    }
    record PathResult(Set<PositionDirection> visitedPositions, int count, boolean isCircle) {}

}
