package org.rascalmpl.repl;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

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
 * This class only works correctly if the actual _raw_ output stream of the terminal is wrapped
 * with an object of this class.
 */
public class TerminalProgressBarMonitor extends FilterOutputStream implements IRascalMonitor  {
    /**
     * We administrate an ordered list of named bars, which will be printed from
     * top to bottom just above the next prompt.
     */
    private List<ProgressBar> bars = new LinkedList<>();
    
    /**
     * This writer is there to help with the encoding to UTF8. It writes directly to the
     * underlying stream.
     */
    private final PrintWriter writer;

    public TerminalProgressBarMonitor(OutputStream out) {
        super(out);
        this.writer = new PrintWriter(out, true, Charset.forName("UTF8" /*also for error messages */));
    }

    /**
     * Represents one currently running progress bar
     */
    private class ProgressBar {
        private final String name;
        private int max;
        private int current = 0;
        private int previousWidth=0;
        private int doneWidth=0;
        private int width = 80;
        private int lineWidth = 100;
        private String message = "";

        ProgressBar(String name, int max) {
            this.name = name;
            this.max = max;
        }

        void worked(int amount, String message) {
            this.current += Math.min(amount, max);
            this.message = message;
        }

        /**
         * To avoid flickering of all bars at the same time, we only reprint
         * the current bar
         */
        void update() {
            // to avoid flicker we only print if there is a new bar character to draw
            if (newWidth() != previousWidth) {
                writer.write(ANSI.hideCursor());
                writer.write(ANSI.moveUp(bars.size() - bars.indexOf(this)));
                write();
                writer.write(ANSI.moveDown(bars.size() - bars.indexOf(this)));
                writer.write(ANSI.showCursor());
            }
        }

        int newWidth() {
            var partDone = (current * 1.0) / (max * 1.0);
            return (int) Math.floor(width * partDone);
        }
        /**
         * Print the current state of the progress bar
         */
        void write() {
            previousWidth = doneWidth;
            doneWidth = newWidth();
            var overWidth = width - doneWidth;
            // var part_char = new String[] {" ", "▏", "▎", "▍", "▌", "▋", "▊", "▉"}[part_width];

            var line = "[" + "█".repeat(doneWidth) + " ".repeat(Math.max(0, overWidth)) + "] " + name.substring(0, Math.min(name.length() - 1, Math.max(0, lineWidth - (width + 3))));

            writer.println(line);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ProgressBar && ((ProgressBar) obj).name.equals(name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    /**
     * Clean the screen, ready for the next output (normal output or the next progress bars or both).
     * We use fancy ANSI codes here to move the cursor and clean the screen to remove previous versions
     * of the bars
     */
    private void eraseBars() {
        if (bars.isEmpty()) {
            return;
        }
            
        writer.write(ANSI.hideCursor());
        writer.write(ANSI.moveUp(bars.size())); 
        writer.write(ANSI.clearToEndOfScreen()); 
        writer.write(ANSI.showCursor());
    }

    /**
     * ANSI escape codes convenience functions
     */
    private static class ANSI {
        static String moveUp(int n) {
            return "\u001B[" + n + "F";
        }

        static String moveDown(int n) {
            return "\u001B[" + n + "E";
        }

        static String clearToEndOfScreen() {
            return "\u001B[0J";
        }

        static String hideCursor() {
            return "\u001B[?25l";
        }

        static String showCursor() {
            return "\u001B[?25h";
        }
    }

    /**
     * Simply print the bars. No cursor movement here.
     */
    private void printBars() {
        writer.write(ANSI.hideCursor());
        for (var pb : bars) {
            pb.write();
        }
        writer.write(ANSI.showCursor());
    }

    /**
     * Find a bar in the ordered list of bars, by name
     * @param name of the bar
     * @return the current instance by that name
     */
    private ProgressBar findBarByName(String name) {
        return bars.stream().filter(b -> b.name.equals(name)).findFirst().orElseGet(() -> null);
    }
    
    @Override
    public void jobStart(String name, int workShare, int totalWork) {
        var pb = findBarByName(name);
        
        eraseBars(); // to make room for the new bars

        if (findBarByName(name) == null) {
            bars.add(new ProgressBar(name, totalWork));
        }
        else {
            pb.current = 0;
            pb.max = totalWork;
        }

        printBars(); // probably one line longer than before!
    }

    @Override
    public void jobStep(String name, String message, int workShare) {
        ProgressBar pb = findBarByName(name);
        
        if (pb != null) {
            pb.worked(workShare, message);
            pb.update();
        }
    }

    @Override
    public int jobEnd(String name, boolean succeeded) {
        var pb = findBarByName(name);

        if (pb != null) {
            eraseBars();
            bars.remove(pb);
            printBars();
            return pb.current;
        }

        return -1;
    }

    @Override
    public boolean jobIsCanceled(String name) {
       // ? don't know what this should do
       return false;
    }

    @Override
    public void jobTodo(String name, int work) {
        eraseBars();
        var bar = findBarByName(name);
        bar.max += work;
        printBars();
    }

    @Override
    public void warning(String message, ISourceLocation src) {
        writer.println(("[WARNING] " + src + ": " + message));
    }

    /**
     * Here we make sure the progress bars are gone just before
     * someone wants to print in the console. When the printing
     * is ready, we simply add our own progress bars again.
     */
    @Override
    public void write(byte[] b) throws IOException {
        eraseBars();
        super.write(b);
        printBars();
    }

    /**
     * Here we make sure the progress bars are gone just before
     * someone wants to print in the console. When the printing
     * is ready, we simply add our own progress bars again.
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        eraseBars();
        super.write(b, off, len);
        printBars();
    }

    /**
     * Here we make sure the progress bars are gone just before
     * someone wants to print in the console. When the printing
     * is ready, we simply add our own progress bars again.
     */
    @Override
    public void write(int b) throws IOException {
        eraseBars();
        super.write(b);
        printBars();
    }

    @Override
    public void endAllJobs() {
        eraseBars();
        bars.clear();
        writer.write(ANSI.showCursor());
    }
}
