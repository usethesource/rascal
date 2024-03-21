package org.rascalmpl.repl;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.rascalmpl.debug.IRascalMonitor;

import io.usethesource.vallang.ISourceLocation;

/**
 * The terminal progress bar monitor wraps the standard output stream to be able to monitor
 * output and keep the progress bars at the same place in the window while other prints happen
 * asyncronously (or even in parallel). It can be passed to the IDEServices API such that
 * clients can start progress bars, make them grow and end them using the API in util::Monitor.
 * Of course this only works if the actual raw output stream of the terminal is wrapped.
 */
public class TerminalProgressBarMonitor extends FilterOutputStream implements IRascalMonitor  {
    /**
     * We administrate an ordered list of named bars, which will be printed from
     * top to bottom just above the next prompt.
     */
    private List<ProgressBar> bars = new LinkedList<>();

    public TerminalProgressBarMonitor(OutputStream out) {
        super(out);
    }

    /**
     * Represents one currently running progress bar
     */
    private class ProgressBar {
        private final String name;
        private int max;
        private int current = 0;
        private String message = "";

        ProgressBar(String name, int max) {
            this.name = name;
            this.max = max;
        }

        ProgressBar(String name, int max, String message) {
            this.name = name;
            this.max = max;
            this.message = message;
        }

        void worked(int amount) {
            this.current += Math.min(amount, max);
        }

        void worked(int amount, String message) {
            this.current += Math.min(amount, max);
            this.message = message;
        }

        /**
         * Print the current state of the progress bar
         */
        void write() {
            write("".getBytes("UTF8"));
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

    }

    /**
     * Simply print the bars. No cursor movement here.
     */
    private void printBars() {

    }

    /**
     * Find a bar in the ordered list of bars, by name
     * @param name of the bar
     * @return the current instance by that name
     */
    private ProgressBar findBarByName(String name) {
        return bars.stream().filter(b -> b.name.equals(name)).findFirst().get();
    }
    
    @Override
    public void jobStart(String name, int workShare, int totalWork) {
        eraseBars(); // to make room for the new bars
        bars.add(new ProgressBar(name, totalWork));
        printBars(); // one line longer than before!
    }

    @Override
    public void jobStep(String name, String message, int workShare) {
        findBarByName(name).worked(workShare, message);
    }

    @Override
    public int jobEnd(String name, boolean succeeded) {
        var pb = findBarByName(name);
        bars.remove(pb);
        return pb.current;
    }

    @Override
    public boolean jobIsCanceled(String name) {
       // ? don't know what this should do
       return false;
    }

    @Override
    public void jobTodo(String name, int work) {
        var bar = findBarByName(name);
        bar.max = work;
        bar.current = 0; 
    }

    @Override
    public void warning(String message, ISourceLocation src) {
        try {
            out.write(("[WARNING] " + src + ": " + message).getBytes("UTF8"));
        }
        catch (IOException e) {
           // if we can't print, we can't print.
        }
    }
}
