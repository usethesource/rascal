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
import java.util.Arrays;
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

    /**
     * If true this will enable the use of unicode tickboxes and clock tickers instead of creative use of ASCII characters.
     */
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

    private static class UnfinishedLine {
        final long threadId;
        private int curCapacity = 512;
        private byte[] buffer = new byte[curCapacity];
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
        private void store(byte[] newInput, int offset, int len) {
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

        public void write(byte[] n, OutputStream out) throws IOException {
            write(n, 0, n.length, out);
        }

        /**
         * Main workhorse looks for newline characters in the new input.
         *  - if there are newlines, than whatever is in the buffer can be flushed.
         *  - all the characters up to the last new new line are flushed immediately.
         *  - all the new characters after the last newline are buffered.
         */
        public void write(byte[] n, int offset, int len, OutputStream out) throws IOException {
            int lastNL = startOfLastLine(n, offset, len);

            if (lastNL == -1) {
                store(n, offset, len);
            }
            else {
                flush(out);
                out.write(n, offset, lastNL + 1);
                out.flush();
                store(n, lastNL + 1, len - (lastNL + 1));
            }
        }

        /**
         * This empties the current buffer onto the stream,
         * and resets the cursor.
         */
        private void flush(OutputStream out) throws IOException {
            if (curEnd != 0) {
                out.write(buffer, 0, curEnd);
                curEnd = 0;
            }
        }

        /**
         * Prints whatever is the last line in the buffer,
         * and adds a newline.
         */
        public void flushLastLine(OutputStream out) throws IOException {
            if (curEnd != 0) {
                flush(out);
                out.write('\n');
            }
        }
    
        private int startOfLastLine(byte[] buffer, int offset, int len) {
            for (int i = offset + len - 1; i >= offset; i--) {
                if (buffer[i] == '\n') {
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
                return (int) Math.floor(barWidth * partDone);
            }
            else {
                return barWidth % stepper;
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
                writer.println(clock);
                return;
            }

            var line 
                = done 
                + ANSI.grey8Background()
                + ANSI.brightWhiteForeground()
                + frontPart
                + ANSI.noBackground()
                + ANSI.normal()
                + backPart
                + " " + clock + " "
                + String.format("%d:%02d:%02d.%03d", duration.toHoursPart(), duration.toMinutes(), duration.toSecondsPart(), duration.toMillisPart())
                + " "
                ;

            writer.println(line); // note this puts us one line down
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

        public static String grey8Background() {
            return "\u001B[48;5;240m"; 
        }

        public static String brightWhiteForeground() {
            return "\u001B[97m";
        }

        static int getCursorPosition(PrintWriter writer, InputStream in) throws IOException {
            writer.write(ANSI.printCursorPosition());
            writer.flush();

            byte[] col = new byte[32];
            int len = in.read(col);
            String echo;

            try {
                echo = new String(col, 0, len, Configuration.getEncoding());
            }
            catch (StringIndexOutOfBoundsException e) {
                // this happens if there is some other input on stdin (for example a pipe)
                // TODO: the input is now read and can't be processed again.
                echo = "";
            }
    
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

        static String overlined() {
            return "\u001B[53m";
        }

        static String underlined() {
            return "\u001B[4m";
        }

        public static String printCursorPosition() {
            return "\u001B[6n";
        }

        public static String noBackground() {
            return "\u001B[49m";
        }

        public static String normal() {
            return "\u001B[0m";
        }

        public static String lightBackground() {
            return "\u001B[48;5;250m";
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
        if (bars.size() == 0) {
            // first new job, we take time to react to window resizing
            lineWidth = tm.getWidth();
        }

        if (totalWork == 0) {
            // makes it easy to use `size` for totalWork and not do anything
            // if there is nothing to do.
            return;
        }

        var pb = findBarByName(name);
        
        writer.write(ANSI.hideCursor());

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

        writer.write(ANSI.showCursor());
        writer.flush();
    }

    @Override
    public synchronized void jobStep(String name, String message, int workShare) {
        ProgressBar pb = findBarByName(name);
        
        if (pb != null) {
            writer.write(ANSI.hideCursor());
            pb.worked(workShare, message);
            pb.update();
            writer.write(ANSI.showCursor());
            writer.flush();
        }
    }

    @Override
    public synchronized int jobEnd(String name, boolean succeeded) {
        var pb = findBarByName(name);

        writer.write(ANSI.hideCursor());

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

        writer.write(ANSI.showCursor());

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
            // this must be the raw output stream
            // otherwise rascal prompts (which do not end in newlines) will be buffered
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
            findUnfinishedLine().write(new byte[] { (byte) b }, out);
            printBars();
        }
        else {
            out.write(b);
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
            try {
                l.flushLastLine(out);
            }
            catch (IOException e) {
                // might happen if the terminal crashes before we stop running 
            }
        }

        try {
            writer.write(ANSI.showCursor());
            writer.flush();
            out.flush();
        }
        catch (IOException e) {
            // ignore
        }
    }

    @Override
    public void close() throws IOException {
        endAllJobs();
    }
}
