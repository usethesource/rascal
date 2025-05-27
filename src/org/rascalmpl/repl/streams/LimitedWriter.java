package org.rascalmpl.repl.streams;

import java.io.IOException;
import java.io.Writer;

import org.rascalmpl.interpreter.utils.LimitedResultWriter.IOLimitReachedException;

public class LimitedWriter extends NonClosingFilterWriter {
    private static final long FULL_WRITE_GRACE = 128;
    private final long limit;
    private long written;

    public LimitedWriter(Writer out, long limit) {
        super(out);
        this.limit = limit;
        this.written = 0;
    }

    @Override
    public void write(int c) throws IOException {
        checkAvailableSpace(1);
        out.write(c);
        updateWrittenChars(1);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        len = checkAvailableSpace(len);
        out.write(cbuf, off, len);
        updateWrittenChars(len);
    }


    @Override
    public void write(String str, int off, int len) throws IOException {
        len = checkAvailableSpace(len);
        out.write(str, off, len);
        updateWrittenChars(len);
    }

    private void updateWrittenChars(int len) throws IOException {
        written += len;
        if (written >= limit) {
            out.write("...");
            out.flush();
        }
    }

    private int checkAvailableSpace(int len) {
        if (written >= limit) {
            throw new IOLimitReachedException();
        }
        if (written + len >= limit + FULL_WRITE_GRACE) {
            return (int)((limit + FULL_WRITE_GRACE) - written);
        }
        return len;
    }
}
