package org.rascalmpl.repl.rascal;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Consumer;

import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;
import org.rascalmpl.repl.output.ICommandOutput;

/**
 * <p>
 * Monitor the reader stream of the terminal while a rascal command is running. 
 * To detect custom signals such as ctrl+\
 * </p>
 * 
 * <p>
 *  The runner should use this reader to read from stdin, just to make sure it gets a lock first
 *  and the contents aren't lost while it's reading
 * </p>
 */
public class RascalSpecificSignalsReader extends Reader {
    private final NonBlockingReader target;
    private volatile boolean keepRunning = true;
    private volatile boolean checkStream = false;
    private final Thread streamMonitor;
    private final IRascalLanguageProtocol lang;
    private final Consumer<ICommandOutput> printStackTrace;

    public RascalSpecificSignalsReader(Terminal term, IRascalLanguageProtocol lang, Consumer<ICommandOutput> printStackTrace) {
        target = term.reader();
        streamMonitor = new Thread(this::monitor, "Rascal's  Input stream monitor");
        streamMonitor.setDaemon(true);
        streamMonitor.start();
        this.lang = lang;
        this.printStackTrace = printStackTrace;
    }

    public void startStreamMonitoring() {
        checkStream = true;
    }

    public void pauseStreamMonitoring() {
        checkStream = false;
        // stop an active read/poll
        target.shutdown();
    }

    private static final int EOF = -1;
    private static final int TIMEOUT = -2;
    private static final int CTRL_SLASH = '\\' & 0x1F;

    private void monitor() {
        while (keepRunning) {
            while (checkStream) {
                int input;
                try {
                    synchronized (this.lock) {
                        input = target.peek(1000);
                    }
                }
                catch (IOException e) {
                    input = TIMEOUT;
                }
                switch (input) {
                    case EOF: return;
                    case TIMEOUT: continue;
                    case CTRL_SLASH: 
                        try {
                            printStackTrace.accept(lang.stackTraceRequested());
                        }
                        catch (Throwable _ignored) {}
                        break;

                    default: break;
                }
                // we got here, so let's swallow the character
                if (keepRunning) { // but make sure we're not interrupted by now
                    synchronized (this.lock) {
                        try {
                            target.read();
                        }
                        catch (IOException e) {
                        }
                    }
                }
            }
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                return;
            }
        }
    }



    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        synchronized (this.lock) {
            return target.read(cbuf, off, len);
        }
    }

    @Override
    public void close() throws IOException {
        keepRunning = false;
        pauseStreamMonitoring();
        streamMonitor.interrupt();
        synchronized (this.lock) {
            target.close();
        }
    }
    
}
