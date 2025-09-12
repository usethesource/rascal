/*
 * Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.util.locations.impl;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.tuple.Pair;
import org.rascalmpl.util.locations.LineColumnOffsetMap;

public class ArrayLineOffsetMap implements LineColumnOffsetMap {
    private final IntArray lines;
    private final ArrayList<IntArray> wideColumnOffsets;
    private final ArrayList<IntArray> wideColumnOffsetsInverse;
    private final IntArray lineStartOffsets;

    public ArrayLineOffsetMap(IntArray lines, ArrayList<IntArray> wideColumnOffsets, ArrayList<IntArray> wideColumnOffsetsInverse, IntArray lineStartOffsets) {
        this.lines = lines;
        this.wideColumnOffsets = wideColumnOffsets;
        this.wideColumnOffsetsInverse = wideColumnOffsetsInverse;
        this.lineStartOffsets = lineStartOffsets;
    }

    @Override
    public int translateColumn(int line, int column, boolean isEnd) {
        int lineIndex = lines.search(line);
        if (lineIndex < 0) {
            return column;
        }
        return column + translateColumnForLine(wideColumnOffsets.get(lineIndex), column, isEnd);
    }

    private int translateColumnForLine(IntArray lineOffsets, int column, boolean isEnd) {
        int columnIndex = lineOffsets.search(column);
        if (columnIndex >= 0) {
            // exact hit
            if (isEnd) {
                // for a end cursor, we want to count this char twice as well
                return columnIndex + 1;
            }
            return columnIndex;
        }
        else {
            return Math.abs(columnIndex + 1);
        }
    }

    @Override
    public int translateInverseColumn(int line, int column, boolean isEnd) {
        int lineIndex = lines.search(line);
        if (lineIndex < 0) {
            return column;
        }
        return column - translateColumnForLine(wideColumnOffsetsInverse.get(lineIndex), column, isEnd);
    }

    @Override
    public Pair<Integer, Integer> calculateInverseOffsetLength(int beginLine, int beginColumn, int endLine, int endColumn) {
        if (beginLine > endLine || (beginLine == endLine && beginColumn > endColumn)) {
            throw new IllegalArgumentException(String.format("Begin position must be before end position [(%d,%d), (%d,%d)]", beginLine, beginColumn, endLine, endColumn));
        }
        int startOffset = lineStartOffsets.get(beginLine) + translateInverseColumn(beginLine, beginColumn, false);
        int endOffset = lineStartOffsets.get(endLine) + translateInverseColumn(endLine, endColumn, true);

        return Pair.of(startOffset, endOffset - startOffset);
    }

    @SuppressWarnings("java:S3776") // parsing tends to be complex
    public static LineColumnOffsetMap build(String contents) {
        int line = 0;
        int column = 0;
        char prev = '\0';
        GrowingIntArray linesWithSurrogate = new GrowingIntArray();
        ArrayList<IntArray> linesMap = new ArrayList<>(0);
        ArrayList<IntArray> inverseLinesMap = new ArrayList<>(0);
        GrowingIntArray currentLine = new GrowingIntArray();
        GrowingIntArray lineStartOffsets = new GrowingIntArray();

        lineStartOffsets.add(0);
        for(int i = 0, n = contents.length() ; i < n ; i++) {
            char c = contents.charAt(i);
            if (c == '\n' || c == '\r') {
                if (c != prev && (prev == '\r' || prev == '\n')) {
                    continue; // multichar newline skip it
                }
                if (!currentLine.isEmpty()) {
                    linesWithSurrogate.add(line);
                    linesMap.add(currentLine.build());
                    inverseLinesMap.add(currentLine.buildInverse());
                    currentLine = new GrowingIntArray();
                }
                line++;
                column = 0;
                lineStartOffsets.add(i + 1);
            }
            else {
                column++;
                if (Character.isHighSurrogate(c) && (i + 1) < n && Character.isLowSurrogate(contents.charAt(i + 1))) {
                    // full surrogate pair, register it, and skip the next char
                    currentLine.add(column);
                    i++;
                }
            }
            prev = c;
        }
        if (!currentLine.isEmpty()) {
            // handle last line
            linesWithSurrogate.add(line);
            linesMap.add(currentLine.build());
            inverseLinesMap.add(currentLine.buildInverse());
        }
        return new ArrayLineOffsetMap(linesWithSurrogate.build(), linesMap, inverseLinesMap, lineStartOffsets.build());
    }

    private static class GrowingIntArray {
        private int[] data = new int[0];
        private int filled = 0;

        public void add(int v) {
            growIfNeeded();
            data[filled] = v;
            filled++;
        }

        public IntArray buildInverse() {
            int[] result = new int[filled];
            for (int i = 0; i < filled; i++) {
                result[i] = data[i] + i;
            }
            return new IntArray(result, filled);
        }

        public boolean isEmpty() {
            return filled == 0;
        }

        public IntArray build() {
            return new IntArray(data, filled);
        }

        private void growIfNeeded() {
            if (filled >= data.length) {
                if (data.length == 0) {
                    data = new int[4];
                }
                else {
                    data = Arrays.copyOf(data, data.length + (data.length / 2));
                }
            }
        }
    }

    private static class IntArray {
        private final int[] data;
        private final int length;

        public IntArray(int[] data, int length) {
            this.data = data;
            this.length = length;
        }

        public int get(int i) {
            if (i < 0 || i >= length) {
                throw new IndexOutOfBoundsException(String.format("Cannot get element at %d; IntArray has %d elements", i, this.length));
            }
            return data[i];
        }

        /**
         * search for key, assume it's sorted data
         * @return >= 0 in case of exact match, below 0 is the insert point
         */
        public int search(int key) {
            if (length == 0) {
                return -1;
            }
            if (length <= 8) {
                // small array, just linear search
                for (int i = 0; i < length; i++) {
                    if (data[i] >= key) {
                        if (data[i] == key) {
                            return i;
                        }
                        return (-i) - 1;
                    }
                }
                return -(length + 1);
            }
            return Arrays.binarySearch(data, 0, length, key);
        }
    }


}
