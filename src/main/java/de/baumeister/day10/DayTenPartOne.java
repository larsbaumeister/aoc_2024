package de.baumeister.day10;

import de.baumeister.day2.DayTwoPartOne;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class DayTenPartOne {
    public static void main(String[] args) throws Exception {
        var map = TopographicMap.from(Files.lines(Paths.get(DayTwoPartOne.class.getResource("/day10/input.txt").toURI()))
                .map(String::toCharArray)
                .toArray(char[][]::new));

        System.out.println(map.allTailHeads());

    }

    static class TopographicMap {
        private final char[][] map;

        private TopographicMap(char[][] map) {
            this.map = map;
        }

        static TopographicMap from(char[][] puzzle) {
            return new TopographicMap(puzzle);
        }

        void trailHeadScore(Position position, Set<Position> tops) {
            if (get(position) == '9') {
                tops.add(position);
            }

            getSurroundingPositions(position)
                .filter(p -> get(position) + 1 == get(p))
                .forEach(p -> trailHeadScore(p, tops));
        }

        int allTailHeads() {
            return getAllPositions()
                .filter(p -> get(p) == '0')
                .mapToInt(p -> {
                    var tops = new HashSet<Position>();
                    trailHeadScore(p, tops);
                    return tops.size();
                })
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
