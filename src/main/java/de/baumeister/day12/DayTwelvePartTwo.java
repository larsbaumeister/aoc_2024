package de.baumeister.day12;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class DayTwelvePartTwo {
    public static void main(String[] args) throws Exception {
        var map = FieldMap.from(Files.lines(Paths.get(DayTwelvePartTwo.class.getResource("/day12/input.txt").toURI()))
                .map(String::toCharArray)
                .toArray(char[][]::new));

        var sum = map.findAllFields().stream()
                .mapToInt(f -> map.fieldArea(f) * map.numberOfSides(f))
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

        int numberOfSides(Field field) {
            return field.positions().stream()
                    .mapToInt(this::numberOfCorners)
                    .sum();
        }

        int numberOfCorners(Position position) {
            var ptl = new Position(position.x() - 1, position.y() - 1);
            var tl = !isValid(ptl) || get(ptl) != get(position);

            var ptr = new Position(position.x() + 1, position.y() - 1);
            var tr = !isValid(ptr) || get(ptr) != get(position);

            var pbl = new Position(position.x() - 1, position.y() + 1);
            var bl = !isValid(pbl) || get(pbl) != get(position);

            var pbr = new Position(position.x() + 1, position.y() + 1);
            var br = !isValid(pbr) || get(pbr) != get(position);

            var pt = new Position(position.x(), position.y() - 1);
            var t = !isValid(pt) || get(pt) != get(position);

            var pl = new Position(position.x() - 1, position.y());
            var l = !isValid(pl) || get(pl) != get(position);

            var pr = new Position(position.x() + 1, position.y());
            var r = !isValid(pr) || get(pr) != get(position);

            var pb = new Position(position.x(), position.y() + 1);
            var b = !isValid(pb) || get(pb) != get(position);

            var count = 0;
            if (t && l) count++;
            if (t && r) count++;
            if (b && l) count++;
            if (b && r) count++;
            if (!t && !r && tr) count++;
            if (!t && !l && tl) count++;
            if (!b && !r && br) count++;
            if (!b && !l && bl) count++;
            return count;
        }

        int fieldArea(Field field) {
            return field.positions().size();
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


