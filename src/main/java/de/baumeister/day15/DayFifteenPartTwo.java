package de.baumeister.day15;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class DayFifteenPartTwo {

    public static void main(String[] args) throws Exception {
        var sections = Files.readString(Path.of(DayFifteenPartTwo.class.getResource("/day15/input.txt").toURI())).split(System.lineSeparator() + System.lineSeparator());
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
        char[][] map;
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
                    .map(RobotMap::transformLine)
                    .map(String::toCharArray)
                    .toArray(char[][]::new);
            return new RobotMap(map);
        }

        private static String transformLine(String oldLine) {
            var sb = new StringBuilder();
            for (int i = 0; i < oldLine.length(); i++) {
                switch (oldLine.charAt(i)) {
                    case '#' -> sb.append("##");
                    case '.' -> sb.append("..");
                    case 'O' -> sb.append("[]");
                    case '@' -> sb.append("@.");
                    default -> throw new IllegalArgumentException("Unknown character: " + oldLine.charAt(i));
                }
            }
            return sb.toString();
        }

        public void moveRobot(Position direction) {
            // perform all moves in a copy of the original map
            // only if no moves were blocked, update the original map
            var newMap = copyMap();
            if (!move(newMap, robotPosition, direction)) {
                robotPosition = robotPosition.move(direction);
                map = newMap;
            }
        }

        private boolean move(char[][] mapCopy, Position pos, Position direction) {
            var next = pos.move(direction);
            var nextChar = mapCopy[next.y()][next.x()];

            var wasBlocked = switch (nextChar) {
                case '#' -> true;
                case '[', ']' -> {
                    var oppositeBlockPart = switch (nextChar) {
                        case ']' -> new Position(next.x - 1, next.y);
                        case '[' -> new Position(next.x + 1, next.y);
                        default -> null;
                    };
                    yield move(mapCopy, next, direction) || (direction.y != 0 && move(mapCopy, oppositeBlockPart, direction));
                }
                default -> false;
            };
            var temp = mapCopy[pos.y()][pos.x()];
            mapCopy[pos.y()][pos.x()] = '.';
            mapCopy[next.y()][next.x()] = temp;
            return wasBlocked;
        }

        private long getGlobalGps() {
            return LongStream.range(0, height)
                    .flatMap(y -> IntStream.range(0, width)
                            .mapToObj(x -> new Position(x, (int) y))
                            .filter(p -> {
                                var c = map[p.y()][p.x()];
                                return c == '[';
                            })
                            .mapToLong(this::getBoxGps))
                    .sum();
        }

        private long getBoxGps(Position pos) {
            return 100L * pos.y() + pos.x();
        }

        private char[][] copyMap() {
            char[][] copy = new char[height][width];
            for (int y = 0; y < height; y++) {
                System.arraycopy(map[y], 0, copy[y], 0, width);
            }
            return copy;
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
