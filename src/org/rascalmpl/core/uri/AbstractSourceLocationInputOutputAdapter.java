package org.rascalmpl.core.uri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import io.usethesource.vallang.ISourceLocation;

public abstract class AbstractSourceLocationInputOutputAdapter implements ISourceLocationInputOutput {
    private final ISourceLocationInput input;
    private final ISourceLocationOutput output;

    protected AbstractSourceLocationInputOutputAdapter(ISourceLocationInput input) {
        this.input = input;
        this.output = input instanceof ISourceLocationOutput ? (ISourceLocationOutput) input : null;
    }
    
    @Override
    public InputStream getInputStream(ISourceLocation uri) throws IOException {
        return input.getInputStream(uri);
    }

    @Override
    public Charset getCharset(ISourceLocation uri) throws IOException {
        return input.getCharset(uri);
    }

    @Override
    public boolean exists(ISourceLocation uri) {
        return input.exists(uri);
    }

    @Override
    public long lastModified(ISourceLocation uri) throws IOException {
        return input.lastModified(uri);
    }

    @Override
    public boolean isDirectory(ISourceLocation uri) {
        return input.isDirectory(uri);
    }

    @Override
    public boolean isFile(ISourceLocation uri) {
        return input.isFile(uri);
    }

    @Override
    public String[] list(ISourceLocation uri) throws IOException {
        return input.list(uri);
    }

    @Override
    public String scheme() {
        return input.scheme();
    }

    @Override
    public boolean supportsHost() {
        return input.supportsHost();
    }

    @Override
    public OutputStream getOutputStream(ISourceLocation uri, boolean append) throws IOException {
        if (output != null) {
            return output.getOutputStream(uri, append);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public void mkDirectory(ISourceLocation uri) throws IOException {
        if (output != null) {
            output.mkDirectory(uri);
        }
        throw new UnsupportedOperationException();

    }

    @Override
    public void remove(ISourceLocation uri) throws IOException {
        if (output != null) {
            output.remove(uri);
        }
        throw new UnsupportedOperationException();
    }
}
