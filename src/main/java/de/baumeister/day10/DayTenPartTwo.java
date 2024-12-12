package de.baumeister.day10;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DayTenPartTwo {
    public static void main(String[] args) throws Exception {
        var map = TopographicMap.from(Files.lines(Paths.get(DayTenPartTwo.class.getResource("/day10/input.txt").toURI()))
                .map(String::toCharArray)
                .toArray(char[][]::new));

        System.out.println(map.allTailHeadScores());

    }

    static class TopographicMap {
        private final char[][] map;

        private TopographicMap(char[][] map) {
            this.map = map;
        }

        static TopographicMap from(char[][] puzzle) {
            return new TopographicMap(puzzle);
        }

        int trailHeadScore(Position position) {
            if (get(position) == '9') {
                return 1;
            }
            return getSurroundingPositions(position)
                .filter(p -> get(position) + 1 == get(p))
                .mapToInt(this::trailHeadScore)
                .sum();
        }

        int allTailHeadScores() {
            return getAllPositions()
                .filter(p -> get(p) == '0')
                .mapToInt(this::trailHeadScore)
                .sum();
        }

        Stream<Position> getAllPositions() {
            return Stream.iterate(new Position(0, 0),
                    p -> p.x() < map[p.y()].length - 1
                            ? new Position(p.x() + 1, p.y())
                            : new Position(0, p.y() + 1))
                .limit((long) map.length * map[0].length);
        }

        Stream<Position> getSurroundingPositions(Position p) {
            return Stream.of(
                new Position(p.x(), p.y() - 1),
                new Position(p.x() - 1, p.y()),
                new Position(p.x() + 1, p.y()),
                new Position(p.x(), p.y() + 1)
            )
            .filter(this::isValid);
        }

        char get(Position position) {
            return map[position.y()][position.x()];
        }

        boolean isValid(Position position) {
            return position.y() >= 0 && position.y() < map.length && position.x() >= 0 && position.x() < map[position.y()].length;
        }
    }

    record Position(int x, int y) {}
}
