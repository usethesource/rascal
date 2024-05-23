package org.rascalmpl.repl;

import java.io.FilterOutputStream;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.rascalmpl.debug.IRascalMonitor;
import io.usethesource.vallang.ISourceLocation;
import jline.Terminal;
import jline.internal.Configuration;

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
     * We also keep a list of currently unfinished lines (one for each thread).
     * Since there are generally very few threads a simple list beats a hash-map in terms
     * of memory allocation and possibly also lookup efficiency.
     */
    private List<UnfinishedLine> unfinishedLines = new ArrayList<>(3);

    /**
     * This writer is there to help with the encoding to what the terminal needs. It writes directly to the
     * underlying stream.
     */
    private final PrintWriter writer;

    /**
     * The entire width in character columns of the current terminal. Resizes everytime when we start
     * the first job.
     */
    private int lineWidth;

    private final boolean unicodeEnabled;

    /**x    
     * Will make everything slow, but easier to spot mistakes
     */
    private final boolean debug = false;

    /**
     * Used to get updates to the width of the terminal
     */
    private final Terminal tm;

    private final String encoding;

    @SuppressWarnings("resource")
    public TerminalProgressBarMonitor(OutputStream out, InputStream in, Terminal tm) {
        super(out);
       
        this.encoding = Configuration.getEncoding();
        this.tm = tm;
        
        PrintWriter theWriter = new PrintWriter(out, true, Charset.forName(encoding));
        this.writer = debug ? new PrintWriter(new AlwaysFlushAlwaysShowCursor(theWriter)) : theWriter;
        this.lineWidth = tm.getWidth();
        this.unicodeEnabled = ANSI.isUTF8enabled(theWriter, in);
        
        assert tm.isSupported() && tm.isAnsiSupported(): "interactive progress bar needs a working ANSI terminal";
        assert out.getClass() != TerminalProgressBarMonitor.class : "accidentally wrapping the wrapper.";
    }

    /**
     * Use this for debugging terminal cursor movements, step by step.
     */
    private static class AlwaysFlushAlwaysShowCursor extends FilterWriter {

        public AlwaysFlushAlwaysShowCursor(PrintWriter out) {
            super(out);
        }

        @Override
        public void write(int c) throws IOException {
            out.write(c);
            out.write(ANSI.showCursor());
            out.flush();
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            out.write(cbuf, off, len);
            out.write(ANSI.showCursor());
            out.flush();
        }

        @Override
        public void write(String str, int off, int len) throws IOException {
            out.write(str, off, len);
            out.write(ANSI.showCursor());
            out.flush();
        }  
    }

    private class UnfinishedLine {
        final long threadId;
        int curCapacity = 512;
        byte[] buffer = new byte[curCapacity];
        int curEnd = 0;
        int lastNewLine = 0;

        public UnfinishedLine() {
            this.threadId = Thread.currentThread().getId();
        }

        /**
         * Adding input combines previously unfinished sentences with possible
         * new sentences. A number of cases come together here that otherwise
         * should be diligently separated. 
         * 
         *   - An unfinished line can already exist or not
         *   - The new input can contain newlines or not
         *   - The new input can end with a newline character or not
         * 
         * By concatenating the previous unfinished line with the new input
         * all we have to do now is figure out where the last newline character is.
         * 
         */
        private void add(byte[] newInput, int offset, int len) {
            // first ensure capacity of the array
            if (curEnd + len >= curCapacity) {
                var oldCapacity = curCapacity;
                curCapacity *= 2; // this should not happen to often. we're talking a few lines of text.
                byte[] tmp = new byte[curCapacity];
                System.arraycopy(buffer, 0, tmp, 0, oldCapacity);
                buffer = tmp;
            }

            System.arraycopy(newInput, offset, buffer, curEnd, len);
            curEnd += len;
            lastNewLine = startOfLastLine(buffer);
        }

        public void write(byte[] n, OutputStream out) throws IOException {
            add(n, 0, n.length);
            flushToLastLine(out);
        }

        public void write(byte[] n, int offset, int len, OutputStream out) throws IOException {
            add(n, offset, len);
            flushToLastLine(out);
        }

        private void flushToLastLine(OutputStream out) throws IOException {
            if (lastNewLine != -1) {
                // write everything out (except the last unfinished line), but including the last newline
                out.write(buffer, 0, lastNewLine);
                
                // rewind buffer to throw everything away that has been written already
                curEnd -= lastNewLine;
                System.arraycopy(buffer, lastNewLine, buffer, 0, curEnd);
                lastNewLine = -1; // no newline anymore
            }

            // otherwise we wait until the next input comes to be able to complete a line.
        }

        public void writeLeftOvers(OutputStream out) throws IOException {
            out.write(buffer, 0, curEnd);
            out.write('\n');
            curEnd = 0;
            lastNewLine = -1;
        }
    
        private int startOfLastLine(byte[] b) {
            for (int i = b.length - 1; i >= 0; i--) {
                if (b[i] == '\n') {
                    return i;
                }
            }
    
            return -1;
        }
    }

    /**
     * Represents one currently running progress bar
     */
    private class ProgressBar {
        private final long threadId;
        private final String threadName;
        private final String name;
        private int max;
        private int current = 0;
        private int previousWidth = 0;
        private int doneWidth = 0;
        private final int barWidthUnicode = lineWidth - "☐ ".length() - " 🕐 00:00:00.000 ".length();
        private final int barWidthAscii   = lineWidth - "? ".length() - " - 00:00:00.000 ".length();
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
            this.threadName = Thread.currentThread().getName();
            this.name = name;
            this.max = Math.max(1, max);
            this.startTime = Instant.now();
            this.duration = Duration.ZERO;
            this.message = name;
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
                writer.write(ANSI.moveUp(bars.size() - bars.indexOf(this)));
                write(); // this moves the cursor already one line down due to `println`
                int distance = bars.size() - bars.indexOf(this) - 1;
                if (distance > 0) {
                    // ANSI will move 1 line even if the parameter is 0
                    writer.write(ANSI.moveDown(distance));
                }
                writer.flush();
            }
        }

        int newWidth() {
            if (max != 0) {
                current = Math.min(max, current); // for robustness sake
                var partDone = (current * 1.0) / (max * 1.0);
                return (int) Math.floor(barWidthUnicode * partDone);
            }
            else {
                return barWidthUnicode % stepper;
            }
        }

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
            var msg = message.substring(0, 1).toUpperCase() + message.substring(1, message.length());
            
            // fill up and cut off:
            msg = threadLabel() + msg;
            msg = (msg + " ".repeat(Math.max(0, barWidthUnicode - msg.length()))).substring(0, barWidthUnicode);

            // split
            var barWidth = unicodeEnabled ? barWidthUnicode : barWidthAscii;
            var frontPart = msg.substring(0, doneWidth);
            var backPart = msg.substring(doneWidth, msg.length());
            var clock = unicodeEnabled ? clocks[stepper % clocks.length] : twister[stepper % twister.length];

            if (barWidth < 1) {
                return; // robustness against very small screens. At least don't throw bounds exceptions
            }
            else if (barWidth <= 3) { // we can print the clock for good measure 
                writer.println(clock);
                return;
            }

            var line 
                = done 
                + ANSI.darkBackground() 
                + frontPart
                + ANSI.noBackground()
                + ANSI.lightBackground()
                + backPart
                + ANSI.noBackground()
                + " " + clock + " "
                + String.format("%d:%02d:%02d.%03d", duration.toHoursPart(), duration.toMinutes(), duration.toSecondsPart(), duration.toMillisPart())
                + " "
                ;

            writer.println("\r" + line); // note this puts us one line down
        }

        private String threadLabel() {
            if (threadName.isEmpty()) {
                return "";
            }
            else if ("main".equals(threadName)) {
                return "";
            }
            
            return threadName + ": ";
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
            this.message = name;
        }
    }

    /**
     * Clean the screen, ready for the next output (normal output or the next progress bars or both).
     * We use fancy ANSI codes here to move the cursor and clean the screen to remove previous versions
     * of the bars
     */
    private void eraseBars() {
        if (!bars.isEmpty()) {
            writer.write(ANSI.moveUp(bars.size())); 
            writer.write(ANSI.clearToEndOfScreen()); 
        }
        writer.flush();
    }

    /**
     * ANSI escape codes convenience functions
     */
    private static class ANSI {
        static boolean isUTF8enabled(PrintWriter writer, InputStream in) {
            try {
                int pos = getCursorPosition(writer, in);
                // Japanese A (あ) is typically 3 bytes in most encodings, but should be less than 3 ANSI columns
                // on screen if-and-only-if unicode is supported.
                writer.write("あ"); 
                writer.flush();
                int newPos = getCursorPosition(writer, in);
                int diff = newPos - pos;

                try {
                    return diff < 3;
                }
                finally {
                    while (--diff >= 0) {
                        writer.write(ANSI.delete());
                    }
                    writer.flush();
                }
            }
            catch (IOException e) {
               return false;
            }
        }

        static int getCursorPosition(PrintWriter writer, InputStream in) throws IOException {
            writer.write(ANSI.printCursorPosition());
            writer.flush();

            byte[] col = new byte[32];
            int len = in.read(col);
            String echo = new String(col, 0, len, Configuration.getEncoding());
    
            if (!echo.startsWith("\u001B[") || !echo.contains(";")) {
                return -1;
            }

            // terminal responds with ESC[n;mR, where n is the row and m is the column.
            echo = echo.split(";")[1]; // take the column part
            echo = echo.substring(0, echo.length() - 1); // remove the last R
            return Integer.parseInt(echo);
        }

        public static String delete() {
            return "\u001B[D\u001B[K";
        }

        static String moveUp(int n) {
            return "\u001B[" + n + "F";
        }

        public static String printCursorPosition() {
            return "\u001B[6n";
        }

        public static String darkBackground() {
            return "\u001B[48;5;242m";
        }

        public static String noBackground() {
            return "\u001B[49m";
        }

        public static String lightBackground() {
            return "\u001B[48;5;249m";
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
     * Simply print the bars. No cursor movement here. Hiding the cursor prevents flickering.
     */
    private void printBars() {
        if (bars.isEmpty()) {
            // no more bars to show, so cursor goes back.
            writer.write(ANSI.showCursor());
        }

        for (var pb : bars) {
            pb.write();
        }
        
        writer.flush();
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
        UnfinishedLine before = unfinishedLines.stream()
            .filter(l -> l.threadId == Thread.currentThread().getId())
            .findAny()
            .orElse(null);

        if (before == null) {
            UnfinishedLine l = new UnfinishedLine();
            unfinishedLines.add(l);
            before = l;
        }

        return before;
    }
    
    @Override
    public synchronized void jobStart(String name, int workShare, int totalWork) {
        if (bars.size() == 0) {
            // first new job, we take time to react to window resizing
            lineWidth = tm.getWidth();
            // remove the cursor
            writer.write(ANSI.hideCursor());
        }

        var pb = findBarByName(name);
        
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

        if (pb != null && --pb.nesting == -1) {
            eraseBars();
            // write it one last time into the scrollback buffer (on top)
            pb.done();
            pb.write();
            bars.remove(pb);
            // print the left over bars under this one.
            printBars();
            return pb.current;
        }
        else if (pb != null) {
            pb.done();
            pb.update();
        }

        return -1;
    }

    @Override
    public synchronized boolean jobIsCanceled(String name) {
       // ? don't know what this should do
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
        writer.println(("[WARNING] " + (src != null ? (src  + ": ") : "") + message));
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
    public synchronized void write(byte[] b) throws IOException {
        if (!bars.isEmpty()) {
            eraseBars();
        
            findUnfinishedLine().write(b, out);
            
            printBars();
        }
        else {
            out.write(b);
        }
    }

    /**
     * Here we make sure the progress bars are gone just before
     * someone wants to print in the console. When the printing
     * is ready, we simply add our own progress bars again.
     */
    @Override
    public synchronized void write(byte[] b, int off, int len) throws IOException {
        if (!bars.isEmpty()) {
            eraseBars();
            findUnfinishedLine().write(b, off, len, out);
            printBars();
        }
        else {
            out.write(b, off, len);
        }
    }

    /**
     * Here we make sure the progress bars are gone just before
     * someone wants to print in the console. When the printing
     * is ready, we simply add our own progress bars again.
     */
    @Override
    public synchronized void write(int b) throws IOException {
        if (!bars.isEmpty()) {
            eraseBars();
            out.write(b);
            printBars();
        }
        else {
            out.write(b);
        }
    }

    

    @Override
    public synchronized void endAllJobs() {
        for (var pb : bars) {
            if (pb.nesting >= 0) {
                writer.println("[INFO] " + pb.name + " is still at nesting level " + pb.nesting);
            }
        }

        bars.clear();
        for (UnfinishedLine l : unfinishedLines) {
            try {
                l.writeLeftOvers(out);
            }
            catch (IOException e) {
                // might happen if the terminal crashes before we stop running 
            }
        }
        writer.write(ANSI.showCursor());
        writer.flush();
    }
}
