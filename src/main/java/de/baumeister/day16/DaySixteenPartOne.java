package de.baumeister.day16;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class DaySixteenPartOne {

    public static void main(String[] args) throws Exception {
        var map = Files.lines(Paths.get(DaySixteenPartOne.class.getResource("/day16/input.txt").toURI()))
                .map(String::toCharArray)
                .toArray(char[][]::new);

        var race = new Race(map);

        var start = race.findStart();
        var end = race.findTarget();

        var distance = race.distance(start, end);

        race.printPath(distance.path);
        System.out.println(distance.distance);
    }

    static class Race {
        private final char[][] map;
        private final int width;
        private final int height;

        Race(char[][] map) {
            this.map = map;
            this.width = map[0].length;
            this.height = map.length;
        }

        public Position findStart() {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (map[y][x] == 'S') {
                        return new Position(x, y);
                    }
                }
            }
            throw new IllegalStateException("No start found");
        }

        public Position findTarget() {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (map[y][x] == 'E') {
                        return new Position(x, y);
                    }
                }
            }
            throw new IllegalStateException("No target found");
        }

        public Path distance(Position start, Position target) {
            int[][] distance = new int[height][width];
            for (int[] row : distance) {
                Arrays.fill(row, Integer.MAX_VALUE);
            }
            distance[start.y()][start.x()] = 0;

            var previous = new Position[height][width];
            for (var row : previous) {
                Arrays.fill(row, null);
            }

            var queue = new PriorityQueue<Cell>();
            queue.add(new Cell(start, 0, Direction.RIGHT));

            while (!queue.isEmpty()) {
                var current = queue.poll();
                if (current.position().equals(target)) {

                    return new Path(reconstructPath(previous, start, target), current.distance());
                }
                for (Direction direction : Direction.values()) {
                    var newPosition = current.position().add(direction.position());
                    var newValue = get(newPosition);
                    if (newValue != '#') {
                        var newDistance = current.direction() == direction
                                ? current.distance() + 1
                                : current.distance() + 1001;

                        if (newDistance < distance[newPosition.y()][newPosition.x()]) {
                            distance[newPosition.y()][newPosition.x()] = newDistance;
                            previous[newPosition.y()][newPosition.x()] = current.position();
                            queue.add(new Cell(newPosition, newDistance, direction));
                        }
                    }
                }
            }
            return null;
        }

        private List<Position> reconstructPath(Position[][] previous, Position start, Position target) {
            var path = new ArrayList<Position>();
            var current = target;
            while (!current.equals(start)) {
                path.add(current);
                current = previous[current.y()][current.x()];
            }
            path.add(start);
            return path;
        }

        private char get(Position position) {
            return map[position.y()][position.x()];
        }

        public void printPath(List<Position> path) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (path.contains(new Position(x, y))) {
                        System.out.print("Q");
                    } else {
                        System.out.print(map[y][x]);
                    }
                }
                System.out.println();
            }
        }

        enum Direction {
            UP(new Position(0, -1)),
            DOWN(new Position(0, 1)),
            LEFT(new Position(-1, 0)),
            RIGHT(new Position(1, 0));

            private final Position position;

            Direction(Position position) {
                this.position = position;
            }

            public Position position() {
                return position;
            }
        }

        record Position(int x, int y) {
            public Position add(Position other) {
                return new Position(this.x() + other.x(), this.y() + other.y());
            }
        }

        record Cell(Position position, int distance, Direction direction) implements Comparable<Cell> {
            @Override
            public int compareTo(Cell o) {
                return Integer.compare(distance, o.distance);
            }
        }

        record Path(List<Position> path, int distance) {
        }
    }
}
