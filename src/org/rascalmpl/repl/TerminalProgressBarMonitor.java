/*
 * Copyright (c) 2023-2025, NWO-I CWI and Swat.engineering
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
package org.rascalmpl.repl;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jline.jansi.Ansi;
import org.jline.jansi.Ansi.Color;
import org.jline.jansi.Ansi.Erase;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;
import org.rascalmpl.debug.IRascalMonitor;

import io.usethesource.vallang.ISourceLocation;

/**
 * The terminal progress bar monitor wraps the standard output stream to be able to monitor
 * output and keep the progress bars at the same place in the window while other prints happen
 * asyncronously (or even in parallel). It can be passed to the IDEServices API such that
 * clients can start progress bars, make them grow and end them using the API in util::Monitor.
 * 
 * This gives the console the ability to show progress almost as clearly as an IDE can with a
 * UI experience. 
 * 
 * This class only works correctly if the actual writer of the terminal is wrapped
 * with an object of this class. And all the writes to the terminal, go via this class.
 */
public class TerminalProgressBarMonitor extends PrintWriter implements IRascalMonitor  {
    /**
     * We administrate an ordered list of named bars, which will be printed from
     * top to bottom just above the next prompt.
     */
    private List<ProgressBar> bars = new LinkedList<>();
    
    /**
     * We also keep a list of currently unfinished lines (one for each thread).
     * Since there are generally very few threads a simple list beats a hash-map in terms
     * of memory allocation and possibly also lookup efficiency.
     */
    private List<UnfinishedLine> unfinishedLines = new ArrayList<>(3);

    /**
     * The entire width in character columns of the current terminal. Resizes everytime when we start
     * the first job.
     */
    private int lineWidth;

    /**
     * If true this will enable the use of unicode tickboxes and clock tickers instead of creative use of ASCII characters.
     */
    private final boolean unicodeEnabled;

    /**x    
     * Will make everything slow, but easier to spot mistakes
     */
    private static final boolean DEBUG = false;

    /**
     * Used to get updates to the width of the terminal
     */
    private final Terminal tm;

    /**
     * ANSI command for this terminal to hide the cursor
     */
    private final String hideCursor;
    /**
     * ANSI command for this terminal to show the cursor
     */
    private final String showCursor;

    public static boolean shouldWorkIn(Terminal tm) {
        Integer cols = tm.getNumericCapability(Capability.max_colors);
        if (cols == null || cols < 8) {
            return false;
        }

        return "\r".equals(tm.getStringCapability(Capability.carriage_return))
            && tm.getNumericCapability(Capability.columns) != null
            && tm.getNumericCapability(Capability.lines) != null
            && tm.getStringCapability(Capability.clear_screen) != null 
            && tm.getStringCapability(Capability.cursor_up) != null
            && tm.getStringCapability(Capability.cursor_down) != null
            ;
    }


    public TerminalProgressBarMonitor(Terminal tm) {
        super(DEBUG ? new AlwaysFlushAlwaysShowCursor(tm.writer(), tm) : tm.writer());
       
        this.tm = tm;
        
        this.lineWidth = tm.getWidth();
        this.unicodeEnabled = tm.encoding().newEncoder().canEncode(new ProgressBar("", 1).clocks[0]);

        this.hideCursor = interpretCapability(tm.getStringCapability(Capability.cursor_invisible));
        this.showCursor = interpretCapability(tm.getStringCapability(Capability.cursor_visible));
    }

    private static String interpretCapability(@Nullable String arg) {
        if (arg == null) {
            return "";
        }
        return arg.replace("\\E", "" + ((char)27)); // escape char
    }

    /**
     * Use this for debugging terminal cursor movements, step by step.
     */
    private static class AlwaysFlushAlwaysShowCursor extends PrintWriter {
        private final String showCursor;

        public AlwaysFlushAlwaysShowCursor(PrintWriter out, Terminal tm) {
            super(out);
            this.showCursor = interpretCapability(tm.getStringCapability(Capability.cursor_visible));
        }

        @Override
        public void write(int c) {
            super.write(c);
            super.write(showCursor);
            super.flush();
        }

        @Override
        public void write(char[] cbuf, int off, int len) {
            super.write(cbuf, off, len);
            super.write(showCursor);
            super.flush();
        }

        @Override
        public void write(String str, int off, int len) {
            super.write(str, off, len);
            super.write(showCursor);
            super.flush();
        }  
    }

    private class UnfinishedLine {
        final long threadId;
        private int curCapacity = 512;
        private char[] buffer = new char[curCapacity];
        private int curEnd = 0;

        public UnfinishedLine() {
            this.threadId = Thread.currentThread().getId();
        }

        /**
         * Adding input combines previously unfinished sentences with possible
         * new (unfinished) sentences. 
         * 
         * The resulting buffer nevers contain any newline character.
         */
        private void store(char[] newInput, int offset, int len) {
            if (len == 0) {
                return; // fast exit
            }

            // first ensure capacity of the array
            if (curEnd + len >= curCapacity) {
                curCapacity *= 2; 
                buffer = Arrays.copyOf(buffer, curCapacity);
            }

            System.arraycopy(newInput, offset, buffer, curEnd, len);
            curEnd += len;
        }

        /**
         * Main workhorse looks for newline characters in the new input.
         *  - if there are newlines, than whatever is in the buffer can be flushed.
         *  - all the characters up to the last new new line are flushed immediately.
         *  - all the new characters after the last newline are buffered.
         */
        public void write(char[] n, int offset, int len) {
            int lastNL = startOfLastLine(n, offset, len);

            if (lastNL == -1) {
                store(n, offset, len);
            }
            else {
                eraseBars(); // since we have some output to write, we now hide the bars
                flush();
                directWrite(n, offset, lastNL + 1);
                directFlush();
                printBars(); // and print the bars back again
                
                store(n, lastNL + 1, len - (lastNL + 1));
            }
        }

        public void write(String s, int offset, int len) {
            write(s.toCharArray(), offset, len);
        }

        /**
         * This empties the current buffer onto the stream,
         * and resets the cursor.
         */
        private void flush() {
            if (curEnd != 0) {
                directWrite(buffer, 0, curEnd);
                curEnd = 0;
            }
        }

        /**
         * Prints whatever is the last line in the buffer,
         * and adds a newline.
         */
        public void flushLastLine() {
            if (curEnd != 0) {
                flush();
                directWrite('\n');
            }
        }
    
        private int startOfLastLine(char[] buffer, int offset, int len) {
            for (int i = offset + len - 1; i >= offset; i--) {
                if (buffer[i] == '\n') {
                    return i;
                }
            }
    
            return -1;
        }
    }

    private void directPrintln(String s) {
        directWrite(s + System.lineSeparator());
    }
    private void directWrite(String s) {
        directWrite(s, 0, s.length());
    }

    private void directWrite(int c) {
        super.write(c);
    }

    private void directWrite(char[] buf, int offset, int length) {
        super.write(buf, offset, length);
    }

    private void directWrite(String buf, int offset, int length) {
        super.write(buf, offset, length);
    }

    private void directFlush() {
        super.flush();
    }

    /**
     * Represents one currently running progress bar
     */
    private class ProgressBar {
        private final long threadId;
        private final String name;
        private int max;
        private int current = 0;
        private int previousWidth = 0;
        private int doneWidth = 0;
        private final int barWidth = unicodeEnabled
            ? lineWidth - "☐ ".length() - " 🕐 00:00:00.000 ".length()
            : lineWidth - "? ".length() - " - 00:00:00.000 ".length();
        private final Instant startTime;
        private Duration duration;
        private String message = "";

        /**
         * Stepper is incremented with every jobStep that has an visible effect on the progress bar.
         * It is used to index into `clocks` or `twister` to create an animation effect.
         */
        private int stepper = 1;
        private final String[] clocks = new String[] {"🕐" , "🕑", "🕒", "🕓", "🕔", "🕕", "🕖", "🕗", "🕘", "🕙", "🕛"};
        private final String[] twister = new String[] {"." , ".", "o", "o", "O","O", "O", "o", "o", ".", "."};
        public int nesting = 0;

        ProgressBar(String name, int max) {
            this.threadId = Thread.currentThread().getId();
            this.name = name;
            this.max = Math.max(1, max);
            this.startTime = Instant.now();
            this.duration = Duration.ZERO;
            this.message = "";
        }

        void worked(int amount, String message) {
            if (current + amount > max) {
                // Fixing this warning helps avoiding to flicker the tick sign on and off, and also makes the progress bar
                // a more accurate depiction of the progress of the computation.
                warning("Monitor of " + name + " is over max (" + max + ") by " + (current + amount - max), null);    
            }

            this.current = Math.min(current + amount, max);
            this.duration = Duration.between(startTime, Instant.now());
            this.message = message;
        }

        /**
         * To avoid flickering of all bars at the same time, we only reprint
         * the current bar
         */
        void update() {
            // to avoid flicker we only print if there is a new bar character to draw
            if (newWidth() != previousWidth) {
                stepper++;
                directWrite(
                    Ansi.ansi()
                        .a(hideCursor)
                        .cursorUpLine(bars.size() - bars.indexOf(this))
                        .toString()
                );
                write(); // this moves the cursor already one line down due to `println`
                int distance = bars.size() - bars.indexOf(this) - 1;
                Ansi ansi = Ansi.ansi();
                if (distance > 0) {
                    // ANSI will move 1 line even if the parameter is 0
                    ansi = ansi.cursorDownLine(distance);
                }
                directWrite(ansi
                        .a(showCursor)
                        .toString()
                );
                directFlush();
            }
        }

        int newWidth() {
            if (max != 0) {
                current = Math.min(max, current); // for robustness sake
                var partDone = (current * 1.0) / (max * 1.0);
                return (int) Math.floor(barWidth * partDone);
            }
            else {
                return barWidth % stepper;
            }
        }

        private static final int BAR_COLOR_BG = 240; // 232-255:  grayscale from dark to light in 24 steps

        /**
         * Print the current state of the progress bar
         */
        void write() {
            previousWidth = doneWidth;
            doneWidth = newWidth();
                        
            // var overWidth = barWidth - doneWidth;
            var done = unicodeEnabled 
                ? (current >= max ? "☑ " : "☐ ")
                : (current >= max ? "X " : "O ")
                ;

            // capitalize
            var msg = message.length() > 0 
                ? message.substring(0, 1).toUpperCase() + message.substring(1, message.length())
                : "";

            var capName = name.length() > 0 
                ? name.substring(0, 1).toUpperCase() + name.substring(1, name.length())
                : "";
            
            capName = message.length() > 0 ? (capName + ": ") : capName;

                // fill up and cut off:
            msg = capName + msg;
            msg = (msg + " ".repeat(Math.max(0, barWidth - msg.length()))).substring(0, barWidth);

            // split
            var frontPart = msg.substring(0, doneWidth);
            var backPart = msg.substring(doneWidth, msg.length());
            var clock = unicodeEnabled ? clocks[stepper % clocks.length] : twister[stepper % twister.length];

            if (barWidth < 1) {
                return; // robustness against very small screens. At least don't throw bounds exceptions
            }
            else if (barWidth <= 3) { // we can print the clock for good measure 
                directPrintln(clock);
                return;
            }

            directWrite(
                Ansi.ansi()
                    .a(done)
                    .bg(BAR_COLOR_BG).fgBright(Color.WHITE)
                    .a(frontPart)
                    .reset()
                    .a(backPart)
                    .a(" " + clock)
                    .format(" %d:%02d:%02d.%03d ", duration.toHoursPart(), duration.toMinutes(), duration.toSecondsPart(), duration.toMillisPart())
                    .a(System.lineSeparator())
                    .toString()
            );
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ProgressBar 
                && ((ProgressBar) obj).name.equals(name)
                && ((ProgressBar) obj).threadId == threadId
                ;
        }

        @Override
        public int hashCode() {
            return name.hashCode() + 31 * (int) threadId;
        }

        public void done() {
            this.current = Math.min(current, max);
            this.duration = Duration.between(startTime, Instant.now());
            this.message = "";
        }
    }

    /**
     * Clean the screen, ready for the next output (normal output or the next progress bars or both).
     * We use fancy ANSI codes here to move the cursor and clean the screen to remove previous versions
     * of the bars
     */
    private void eraseBars() {
        if (!bars.isEmpty()) {
            directWrite(Ansi.ansi()
                .cursorUpLine(bars.size())
                .eraseScreen(Erase.FORWARD)
                .toString()
            );
        }
        directFlush();
    }

    /**
     * Simply print the bars. No cursor movement here. Hiding the cursor prevents flickering.
     */
    private void printBars() {
        for (var pb : bars) {
            pb.write();
        }
        
        directFlush();
        
    }

    /**
     * Find a bar in the ordered list of bars, by name.
     * @param name of the bar
     * @return the current instance by that name
     */
    private ProgressBar findBarByName(String name) {
        return bars.stream()
            .filter(b -> b.threadId == Thread.currentThread().getId())
            .filter(b -> b.name.equals(name)).findFirst().orElseGet(() -> null);
    }

    private UnfinishedLine findUnfinishedLine() {
        return unfinishedLines.stream()
            .filter(l -> l.threadId == Thread.currentThread().getId())
            .findAny()
            .orElseGet(() -> {
                UnfinishedLine l = new UnfinishedLine();
                unfinishedLines.add(l);
                return l;    
            });
    }
    
    @Override
    public synchronized void jobStart(String name, int workShare, int totalWork) {
        if (bars.isEmpty()) {
            // first new job, we take time to react to window resizing
            lineWidth = tm.getWidth();
        }

        if (totalWork == 0) {
            // makes it easy to use `size` for totalWork and not do anything
            // if there is nothing to do.
            return;
        }

        var pb = findBarByName(name);
        
        directWrite(hideCursor);

        if (pb == null) {    
            eraseBars(); // to make room for the new bars
            bars.add(new ProgressBar(name, totalWork));
            printBars(); // probably one line longer than before!   
        }
        else {
            // Zeno-bar: we add the new work to the already existing work
            pb.max += totalWork;
            pb.nesting++;
            pb.update();
        }

        directWrite(showCursor);
        directFlush();
    }

    @Override
    public synchronized void jobStep(String name, String message, int workShare) {
        ProgressBar pb = findBarByName(name);
        
        if (pb != null) {
            pb.worked(workShare, message);
            pb.update();
        }
    }

    @Override
    public synchronized int jobEnd(String name, boolean succeeded) {
        var pb = findBarByName(name);

        directWrite(hideCursor);

        if (pb != null && --pb.nesting == -1) {
            eraseBars();
            pb.done();
            bars.remove(pb);
            // print the left over bars under this one.
            printBars();
            return pb.current;
        }
        else if (pb != null) {
            pb.done();
            pb.update();
        }

        directWrite(showCursor);

        return -1;
    }

    @Override
    public synchronized boolean jobIsCanceled(String name) {
       // This is in UI environments where there is a cancel button.
       return false;
    }

    @Override
    public synchronized void jobTodo(String name, int work) {
        ProgressBar pb = findBarByName(name);
        
        if (pb != null) {
            pb.max += work;
            pb.update();
        }
    }

    @Override
    public synchronized void warning(String message, ISourceLocation src) {
        if (!bars.isEmpty()) {
            eraseBars();
        }

        directPrintln(("[WARNING] " + (src != null ? (src  + ": ") : "") + message));

        if (!bars.isEmpty()) {
            printBars();
        }
    }

    /**
     * Here we make sure the progress bars are gone just before
     * someone wants to print in the console. When the printing
     * is ready, we simply add our own progress bars again.
     * 
     * Special cases handle when there are unfinished lines in play.
     * This code guarantees that printBars is only called when the cursor
     * is before the first character of a line.
     */
    @Override
    public void write(String s, int off, int len) {
        synchronized(lock) {
            if (!bars.isEmpty()) {
                findUnfinishedLine().write(s, off, len);
            }
            else {
                directWrite(s, off, len);
            }
        }
    }
    /**
     * Here we make sure the progress bars are gone just before
     * someone wants to print in the console. When the printing
     * is ready, we simply add our own progress bars again.
     */
    @Override
    public void write(char[] buf, int off, int len)  {
        synchronized(lock) {
            if (!bars.isEmpty()) {
                findUnfinishedLine().write(buf, off, len);
            }
            else {
                // this must be the raw output stream
                // otherwise rascal prompts (which do not end in newlines) will be buffered
                directWrite(buf, off, len);
            }
        }
    }

    /**
     * Here we make sure the progress bars are gone just before
     * someone wants to print in the console. When the printing
     * is ready, we simply add our own progress bars again.
     */
    @Override
    public void write(int c) {
        synchronized(lock) {
            if (!bars.isEmpty()) {
                findUnfinishedLine().write(new char[] { (char) c }, 0, 1);
            }
            else {
                directWrite(c);
            }
        }
    }

    @Override
    public void println() {
        synchronized(lock) {
            if (!bars.isEmpty()) {
                write(System.lineSeparator());
            }
            else {
                super.println();
            }
        }
    }

    

    @Override
    public synchronized void endAllJobs() {
        if (!bars.isEmpty()) {
            eraseBars();
            printBars();
            bars.clear();
        }

        for (UnfinishedLine l : unfinishedLines) {
            l.flushLastLine();
        }

        try {
            directWrite(showCursor);
            directFlush();
            out.flush();
        }
        catch (IOException e) {
            // ignore
        }
    }

    @Override
    public void close() {
        try {
            endAllJobs();
        }
        finally {
            super.close();
        }
    }
}
