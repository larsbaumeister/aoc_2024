package de.baumeister.day9;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DayNinePartTwo {
    public static void main(String[] args) throws Exception {
        var s = Files.readString(Path.of(DayNinePartOne.class.getResource("/day9/input.txt").toURI()));
        var fs = FileSystem.from(s);
        fs.shiftLeft();
        var sum = fs.checksum();
        System.out.println(sum);
    }

    static class FileSystem {

        private final List<FileBlock> blocks;

        private FileSystem(List<FileBlock> blocks) {
            this.blocks = blocks;
        }

        static FileSystem from(String s) {
            var list = new LinkedList<FileBlock>();
            for (var i = 0; i < s.length(); i++) {
                var isEmpty = i % 2 != 0;
                var block = new FileBlock(isEmpty ? -1 : i / 2, Character.getNumericValue(s.charAt(i)), isEmpty, s.charAt(i), false);
                if (block.size > 0)
                    list.add(block);
            }
            return new FileSystem(list);
        }

        void shiftLeft() {
            for (var block : new ArrayList<>(blocks).reversed()) {
                if (!block.isEmpty) {
                    var emptyBlock = getNextEmptyBlock(block.size);
                    if (emptyBlock == null || blocks.indexOf(emptyBlock) > blocks.indexOf(block)) {
                        continue;
                    }
                    if (emptyBlock.size > block.size) {
                        blocks.add(blocks.indexOf(emptyBlock) + 1, new FileBlock(-1, emptyBlock.size - block.size, true, ' ', false));
                    }
                    blocks.set(blocks.indexOf(emptyBlock), new FileBlock(block.id, block.size, false, block.value, true));
                    blocks.set(blocks.indexOf(block), new FileBlock(-1, block.size, true, block.value, false));
                }
            }
        }

        private FileBlock getNextEmptyBlock(int minSpace) {
            for (var block : blocks) {
                if (block.isEmpty && block.size >= minSpace) {
                    return block;
                }
            }
            return null;
        }

        private long checksum() {
            long sum = 0;
            var idx = 0;
            for (var block : blocks) {
                for (var i = 0; i < block.size; i++) {
                    sum += Math.max(0, (long) block.id * idx++);
                }
            }
            return sum;
        }

    }

    record FileBlock(int id, int size, boolean isEmpty, char value, boolean isCopy) {}
}
