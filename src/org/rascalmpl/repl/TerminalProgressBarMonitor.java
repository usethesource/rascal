package org.rascalmpl.repl;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.rascalmpl.debug.IRascalMonitor;

import io.usethesource.vallang.ISourceLocation;

public class TerminalProgressBarMonitor implements IRascalMonitor {
    /**
     * We monitor for orthogonal output to keep the progress bars in the same place.
     */
    private final ProgressBarStreamWrapper out;

    /**
     * We administrate an ordered list of named bars, which will be printed from
     * top to bottom just above the next prompt.
     */
    private List<ProgressBar> bars = new LinkedList<>();

    TerminalProgressBarMonitor(OutputStream stdout) {
        out = new ProgressBarStreamWrapper(stdout);
    }

    /**
     * The outputstream is wrapped to erase the current progress bars
     * and let normal output be printed. When that is done, the bars
     * are printed again, as-if they were always there.
     */
    private class ProgressBarStreamWrapper extends FilterOutputStream {

        public ProgressBarStreamWrapper(OutputStream out) {
            super(out);
        }
    }

    /**
     * Represents one currently running progress bar
     */
    private static class ProgressBar {
        private final String name;
        private int max;
        private int current = 0;

        ProgressBar(String name, int max) {
            this.name = name;
            this.max = max;
        }

        void worked(int amount) {
            current += Math.min(amount, max);
        }

        String format() {
            return "" + current;
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
        findBarByName(name).worked(workShare);
    }

    @Override
    public int jobEnd(String name, boolean succeeded) {
        // uses the equals method of ProgressBar
        bars.remove(new ProgressBar(name, 0));
    }

    @Override
    public boolean jobIsCanceled(String name) {
        jobEnd(name, false);
    }

    @Override
    public void jobTodo(String name, int work) {
        var bar = findBarByName(name);
        bar.max = work;
        bar.current = 0; 
    }

    @Override
    public void warning(String message, ISourceLocation src) {
        out.write(("[WARNING] " + src + ": " + message));
    }
}
