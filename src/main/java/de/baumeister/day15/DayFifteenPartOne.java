package de.baumeister.day15;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class DayFifteenPartOne {

    public static void main(String[] args) throws Exception {
        var sections = Files.readString(Path.of(DayFifteenPartOne.class.getResource("/day15/input.txt").toURI())).split(System.lineSeparator() + System.lineSeparator());
        var map = RobotMap.from(sections[0]);

        sections[1].chars().mapToObj(c -> (char) c).forEach(c -> {
            switch (c) {
                case '^' -> map.moveRobot(UP);
                case 'v' -> map.moveRobot(DOWN);
                case '<' -> map.moveRobot(LEFT);
                case '>' -> map.moveRobot(RIGHT);
            }
        });

        System.out.println(map.getGlobalGps());
    }

    static class RobotMap {
        final char[][] map;
        private final int width ;
        private final int height;
        private Position robotPosition;

        public RobotMap(char[][] map) {
            this.map = map;
            this.width = map[0].length;
            this.height = map.length;
            this.robotPosition = findRobot();
        }

        private Position findRobot() {
            for (var y = 0; y < height; y++) {
                for (var x = 0; x < width; x++) {
                    if (map[y][x] == '@') {
                        return new Position(x, y);
                    }
                }
            }
            throw new IllegalStateException("No robot found");
        }

        public static RobotMap from(String s) {
            var map = Arrays.stream(s.split(System.lineSeparator()))
                    .map(String::toCharArray)
                    .toArray(char[][]::new);
            return new RobotMap(map);
        }

        public void moveRobot(Position direction) {
            if (!move(robotPosition, direction)) {
                robotPosition = robotPosition.move(direction);
            }
        }

        private boolean move(Position pos, Position direction) {
            var next = pos.move(direction);

            boolean wasBlocked = false;
            if (isBlock(next)) { wasBlocked = true; }
            if (isBox(next)) { wasBlocked = move(next, direction); }

            if (!wasBlocked) {
                var temp = map[pos.y()][pos.x()];
                map[pos.y()][pos.x()] = '.';
                map[next.y()][next.x()] = temp;
            }
            return wasBlocked;
        }

        private long getGlobalGps() {
            return LongStream.range(0, height)
                    .flatMap(y -> IntStream.range(0, width)
                        .mapToObj(x -> new Position(x, (int) y))
                        .filter(this::isBox)
                        .mapToLong(this::getBoxGps))
                    .sum();
        }

        private long getBoxGps(Position pos) {
            return 100L * pos.y() + pos.x();
        }

        private boolean isBlock(Position position) {
            return map[position.y()][position.x()] == '#';
        }

        private boolean isBox(Position position) {
            return map[position.y()][position.x()] == 'O';
        }
    }

    private static final Position UP = new Position(0, -1);
    private static final Position DOWN = new Position(0, 1);
    private static final Position LEFT = new Position(-1, 0);
    private static final Position RIGHT = new Position(1, 0);

    record Position(int x, int y) {
        public Position move(Position direction) {
            return new Position(this.x + direction.x(), this.y + direction.y());
        }
    }

}
