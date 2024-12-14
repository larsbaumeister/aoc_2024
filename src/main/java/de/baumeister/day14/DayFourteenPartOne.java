package de.baumeister.day14;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DayFourteenPartOne {

    public static void main(String[] args) throws Exception {
        var input = Files.readString(Path.of(DayFourteenPartOne.class.getResource("/day14/input.txt").toURI()));
        var map = RobotMap.from(input, 101, 103);
        IntStream.range(0, 100).forEach(i -> map.tick());

        System.out.println(map.safetyFactor());

    }

    static class RobotMap {
        final int width;
        final int height;
        private final List<Robot> robots;

        public RobotMap(int width, int height) {
            this.width = width;
            this.height = height;
            this.robots = new ArrayList<>();
        }

        public static RobotMap from(String input, int width, int height) throws Exception {
            var pattern = Pattern.compile("p=(?<PX>\\d+),(?<PY>\\d+) v=(?<VX>-?\\d+),(?<VY>-?\\d+)");
            var robs = Arrays.stream(input.split(System.lineSeparator()))
                .map(s -> {
                    var matcher = pattern.matcher(s);
                    matcher.find();
                    return new Robot(new Vec2(
                        Long.parseLong(matcher.group("PX")),
                        Long.parseLong(matcher.group("PY"))
                    ), new Vec2(
                        Long.parseLong(matcher.group("VX")),
                        Long.parseLong(matcher.group("VY"))
                    ));
                })
                .toList();

            var map = new RobotMap(width, height);
            map.robots.addAll(robs);
            return map;
        }

        public void tick() {
            robots.forEach(robot -> robot.position = new Vec2(
                ((robot.position.x + robot.velocity.x) % width + width) % width,
                ((robot.position.y + robot.velocity.y) % height + height) % height
            ));
        }

        public long safetyFactor() {
            return Stream.of(Quadrant.values())
                    .mapToLong(this::countRobotsInQuadrant)
                    .reduce(1, (a, b) -> a * b);
        }

        private long countRobotsInQuadrant(Quadrant quadrant) {
            return robots.stream().filter(robot -> switch (quadrant) {
                case TOP_LEFT -> robot.position.x < width / 2 && robot.position.y < height / 2;
                case TOP_RIGHT -> robot.position.x > width / 2 && robot.position.y < height / 2;
                case BOTTOM_LEFT -> robot.position.x < width / 2 && robot.position.y > height / 2;
                case BOTTOM_RIGHT -> robot.position.x > width / 2 && robot.position.y > height / 2;
            }).count();
        }

    }

    enum Quadrant {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }

    record Vec2(long x, long y) {}

    static class Robot {
        Vec2 position;
        Vec2 velocity;

        Robot(Vec2 position, Vec2 velocity) {
            this.position = position;
            this.velocity = velocity;
        }
    }

}
