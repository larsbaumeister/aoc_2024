package de.baumeister.day8;

import de.baumeister.day2.DayTwoPartOne;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DayEightPartOne {
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
                for(int x = 0; x < width; x++) {
                    if(isAntiNode(new Position(x, y))) {
                        count++;
                    }
                }
            }
            return count;
        }

        boolean isAntiNode(Position position) {
            for (int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {
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
            var newPos = foundAntenna.add(foundAntenna.subtract(currentPos));
            return isValid(newPos) && get(foundAntenna) == get(newPos);
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
    }

}
