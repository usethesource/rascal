package org.rascalmpl.repl;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class NotifieableInputStream extends InputStream {
    private final BlockingQueue<Byte> queue;
    private volatile boolean closed;
    private volatile IOException toThrow;
    private final Thread reader;
    private final InputStream peekAt;

    /**
     * scan for certain bytes in the stream, and if they are found, call the callback function to see if
     * it has to be swallowed.
     */
    public NotifieableInputStream(final InputStream peekAt, final byte[] watchFor,
        final Function<Byte, Boolean> swallow) {
        this.queue = new ArrayBlockingQueue<>(8 * 1024);
        this.closed = false;
        this.toThrow = null;
        this.peekAt = peekAt;
        this.reader = new Thread(() -> {
            try {
                reading: while (!closed) {
                    int b = peekAt.read();
                    if (b == -1) {
                        NotifieableInputStream.this.close();
                        return;
                    }
                    for (byte c : watchFor) {
                        if (b == c) {
                            if (swallow.apply((byte) b)) {
                                continue reading;
                            }
                            break;
                        }
                    }
                    queue.put((byte) b);
                }
            }
            catch (IOException e2) {
                if (!e2.getMessage().contains("closed")) {
                    toThrow = e2;
                    try {
                        NotifieableInputStream.this.close();
                    }
                    catch (IOException e1) {
                    }
                }
            }
            catch (InterruptedException e3) {
                Thread.currentThread().interrupt();
            }
        });
        reader.setName("InputStream scanner");
        reader.setDaemon(true);
        reader.start();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        }
        else if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        }
        else if (len == 0) {
            return 0;
        }
        // we have to at least read one (so block until we can)
        int atLeastOne = read();
        if (atLeastOne == -1) {
            return -1;
        }
        int index = off;
        b[index++] = (byte) atLeastOne;

        // now consume the rest of the available bytes
        Byte current;
        while ((current = queue.poll()) != null && (index < off + len)) {
            b[index++] = current;
        }
        return index - off;
    }

    @Override
    public int read() throws IOException {
        Byte result;
        try {
            while ((result = queue.poll(100, TimeUnit.MILLISECONDS)) == null) {
                if (closed) {
                    return -1;
                }
                if (toThrow != null) {
                    IOException throwCopy = toThrow;
                    toThrow = null;
                    if (throwCopy != null) {
                        throw throwCopy;
                    }
                }
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return -1;
        }
        return (result & 0xFF);
    }

    @Override
    public void close() throws IOException {
        closed = true;
        peekAt.close();
    }
}
