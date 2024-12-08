package de.baumeister.day8;

import de.baumeister.day2.DayTwoPartOne;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DayEightPartTwo {
    public static void main(String[] args) throws Exception {
        var map = AntannaMap.from(Files.lines(Paths.get(DayTwoPartOne.class.getResource("/day8/input.txt").toURI()))
                .map(String::toCharArray)
                .toArray(char[][]::new));

        System.out.println(map.antiNodeCount());
    }

    record AntannaMap(int width, int height, char[][] map) {
        static AntannaMap from(char[][] puzzle) {
            return new AntannaMap(puzzle[0].length, puzzle.length, puzzle);
        }

        int antiNodeCount() {
            var count = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (isAntiNode(new Position(x, y))) {
                        count++;
                    }
                }
            }
            return count;
        }

        boolean isAntiNode(Position position) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    var antennaPos = new Position(x, y);
                    if (!position.equals(antennaPos) && isAntenna(antennaPos) && hasInlineAntenna(position, antennaPos)) {
                        return true;
                    }
                }
            }
            return false;
        }

        boolean isAntenna(Position position) {
            var c =get(position);
            return Character.isAlphabetic(c) || Character.isDigit(c);
        }

        char get(Position position) {
            return map[position.y()][position.x()];
        }

        boolean hasInlineAntenna(Position currentPos, Position foundAntenna) {
            var direction = foundAntenna.subtract(currentPos).shorten();
            var pos = foundAntenna.add(direction);
            while (isValid(pos)) {
                if (get(foundAntenna) == get(pos)) {
                    return true;
                }
                pos = pos.add(direction);
            }

            pos = foundAntenna.subtract(direction);
            while (isValid(pos)) {
                if (get(foundAntenna) == get(pos)) {
                    return true;
                }
                pos = pos.subtract(direction);
            }
            return false;
        }

        boolean isValid(Position position) {
            return position.x() >= 0 && position.x() < width && position.y() >= 0 && position.y() < height;
        }

    }

    record Position(int x, int y) {
        Position add(Position other) {
            return new Position(x + other.x, y + other.y);
        }
        Position subtract(Position other) {
            return new Position(x - other.x, y - other.y);
        }
        Position shorten() {
            var gcd = gcd(x, y);
            return new Position(x / gcd, y / gcd);
        }
    }

    private static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

}

