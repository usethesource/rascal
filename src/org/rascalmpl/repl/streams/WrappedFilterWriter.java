package org.rascalmpl.repl.streams;



import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public abstract class WrappedFilterWriter extends FilterWriter {

    private final char[] pre;
    private final char[] post;

    public WrappedFilterWriter(Writer out,char[] pre, char[] post) {
        super(out);
        this.pre = pre;
        this.post = post;
    }

    @Override
    public void write(int c) throws IOException {
        out.write(pre);
        out.write(c);
        out.write(post);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        out.write(pre);
        out.write(cbuf, off, len);
        out.write(post);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        out.write(pre);
        out.write(str, off, len);
        out.write(post);
    }

}
