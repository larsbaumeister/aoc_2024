package de.baumeister.day9;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DayNinePartOne {

    public static void main(String[] args) throws Exception {
        var s = Files.readString(Path.of(DayNinePartOne.class.getResource("/day9/input.txt").toURI()));
        var fs = FileSystem.from(s);
        fs.shiftLeft();
        var sum = fs.checksum();
        System.out.println(sum);
    }

    static class FileSystem {

        private final List<FileBlock> blocks;

        FileSystem(List<FileBlock> blocks) {
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
                    var shiftPerformed = shiftBlockLeft(block);
                    if (!shiftPerformed)
                        break;
                }
            }
        }

        private boolean shiftBlockLeft(FileBlock block) {
            var cancelled = false;
            var remaining = block.size;
            while (remaining > 0) {
                var nextBlock = getNextEmptyBlock();
                var nextBlockIndex = blocks.indexOf(nextBlock);
                if (nextBlockIndex >= blocks.indexOf(block)) {
                    if (remaining == block.size) {
                        return false;
                    }
                    cancelled = true;
                }

                var toMove = Math.min(remaining, nextBlock.size);
                var newBlock = new FileBlock(block.id, toMove, false, block.value, true);
                blocks.set(nextBlockIndex, newBlock);

                if (toMove < nextBlock.size) {
                    var newEmptyBlock = new FileBlock(nextBlock.id, nextBlock.size - toMove, true, nextBlock.value, false);
                    blocks.add(nextBlockIndex + 1, newEmptyBlock);
                }

                blocks.add(new FileBlock(nextBlock.id, toMove, true, nextBlock.value, false));

                remaining -= toMove;
            }
            blocks.remove(block);
            return !cancelled;
        }

        private FileBlock getNextEmptyBlock() {
            for (var block : blocks) {
                if (block.isEmpty) {
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
