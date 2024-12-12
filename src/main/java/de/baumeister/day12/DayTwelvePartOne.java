package de.baumeister.day12;

import de.baumeister.day10.DayTenPartOne;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class DayTwelvePartOne {
    public static void main(String[] args) throws Exception {
        var map = FieldMap.from(Files.lines(Paths.get(DayTenPartOne.class.getResource("/day12/input.txt").toURI()))
                .map(String::toCharArray)
                .toArray(char[][]::new));

        var sum = map.findAllFields().stream()
                .mapToInt(f -> map.fieldArea(f) * map.fieldPerimeter(f))
                .sum();
        System.out.println(sum);
    }

    static class FieldMap {

        final char[][] map;
        final int widht;
        final int height;

        private FieldMap(char[][] map) {
            this.map = map;
            this.widht = map[0].length;
            this.height = map.length;
        }

        static FieldMap from(char[][] puzzle) {
            return new FieldMap(puzzle);
        }

        public Set<Field> findAllFields() {
            var foundPositions = new HashSet<Position>();
            var fields = new HashSet<Field>();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < widht; x++) {
                    var position = new Position(x, y);
                    if (!foundPositions.contains(position)) {
                        var field = findField(position);
                        fields.add(field);
                        foundPositions.addAll(field.positions());
                    }
                }
            }
            return fields;
        }

        public Field findField(Position position) {
            var set = new HashSet<Position>();
            set.add(position);
            var field = new Field(get(position), set);
            floodFill(position, field);
            return field;
        }

        private void floodFill(Position position, Field field) {
            getSurroundingNeighbours(position, get(position))
                    .filter(p -> !field.positions().contains(p))
                    .forEach(p -> {
                        field.positions().add(p);
                        floodFill(p, field);
                    });
        }

        private Stream<Position> getSurroundingNeighbours(Position position) {
            return Stream.of(
                    new Position(position.x(), position.y() - 1),
                    new Position(position.x() - 1, position.y()),
                    new Position(position.x() + 1, position.y()),
                    new Position(position.x(), position.y() + 1)
            );
        }

        private Stream<Position> getSurroundingNeighbours(Position position, char c) {
            return getSurroundingNeighbours(position)
                    .filter(this::isValid)
                    .filter(p -> get(p) == c);
        }

        int fieldArea(Field field) {
            return field.positions().size();
        }

        int fieldPerimeter(Field field) {
            return field.positions().stream()
                .mapToInt(pos -> Math.toIntExact(getSurroundingNeighbours(pos)
                        .filter(p -> !isValid(p) || get(p) != get(pos))
                        .count()))
                .sum();
        }

        private boolean isValid(Position position) {
            return position.x() >= 0 && position.x() < widht && position.y() >= 0 && position.y() < height;
        }

        private char get(Position position) {
            return map[position.y()][position.x()];
        }
    }

    record Field(char type, Set<Position> positions) { }

    record Position(int x, int y) { }

}
