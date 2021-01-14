package org.rascalmpl.repl;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class NotifieableInputStream extends InputStream {
    private final ConcurrentLinkedQueue<Byte> queue;
    private volatile boolean closed;
    private volatile IOException toThrow;
    private final Semaphore newData = new Semaphore(0);
    private final Thread reader;
    private final InputStream peekAt;

    /**
     * scan for certain bytes in the stream, and if they are found, call the callback function to see if it has to be swallowed.
     */
    public NotifieableInputStream(final InputStream peekAt, final byte[] watchFor, final Function<Byte, Boolean> swallow) {
        this.queue = new ConcurrentLinkedQueue<Byte>();
        this.closed = false;
        this.toThrow = null;
        this.peekAt = peekAt;
        this.reader = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    reading:
                        while (!closed) {
                            int b = peekAt.read();
                            if (b == -1) {
                                NotifieableInputStream.this.close();
                                return;
                            }
                            for (byte c: watchFor) {
                                if (b == c) {
                                    if (swallow.apply((byte)b)) {
                                        continue reading;
                                    }
                                    break;
                                }
                            }
                            queue.offer((byte)b);
                            newData.release();

                        }
                }
                catch(IOException e) {
                    if (!e.getMessage().contains("closed")) {
                        toThrow = e;
                        try {
                            NotifieableInputStream.this.close();
                        }
                        catch (IOException e1) {
                        }
                    }
                }
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
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                        ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
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
        Byte result = null;
        while ((result = queue.poll()) == null) {
            if (closed) {
                return -1;
            }
            try {
                newData.tryAcquire(10, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {
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
        return (result & 0xFF);
    }

    @Override
    public void close() throws IOException {
        closed = true;
        peekAt.close();
    }
}
