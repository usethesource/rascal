package org.rascalmpl.uri.unsupported;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

import org.rascalmpl.uri.FileAttributes;
import org.rascalmpl.uri.ISourceLocationInputOutput;
import org.rascalmpl.uri.ISourceLocationWatcher;

import io.usethesource.vallang.ISourceLocation;

abstract class UnsupportedURIResolver implements ISourceLocationInputOutput, ISourceLocationWatcher {

    private final String scheme;
    private final String message;

    protected UnsupportedURIResolver(String scheme, String message) {
        this.scheme = scheme;
        this.message = message;
    }

    private IOException buildException() {
        return new IOException(message);
    }

    @Override
    public InputStream getInputStream(ISourceLocation uri) throws IOException {
        throw buildException();
    }

    @Override
    public boolean exists(ISourceLocation uri) {
        return false;
    }

    @Override
    public long lastModified(ISourceLocation uri) throws IOException {
        throw buildException();
    }

    @Override
    public long size(ISourceLocation uri) throws IOException {
        throw buildException();
    }

    @Override
    public boolean isDirectory(ISourceLocation uri) {
        return false;
    }

    @Override
    public boolean isFile(ISourceLocation uri) {
        return false;
    }

    @Override
    public boolean isReadable(ISourceLocation uri) throws IOException {
        throw buildException();
    }

    @Override
    public String[] list(ISourceLocation uri) throws IOException {
        throw buildException();
    }

    @Override
    public String scheme() {
        return this.scheme;
    }

    @Override
    public boolean supportsHost() {
        return false;
    }

    @Override
    public FileAttributes stat(ISourceLocation uri) throws IOException {
        throw buildException();
    }

    @Override
    public OutputStream getOutputStream(ISourceLocation uri, boolean append) throws IOException {
        throw buildException();
    }

    @Override
    public void mkDirectory(ISourceLocation uri) throws IOException {
        throw buildException();
    }

    @Override
    public void remove(ISourceLocation uri) throws IOException {
        throw buildException();
    }

    @Override
    public void setLastModified(ISourceLocation uri, long timestamp) throws IOException {
        throw buildException();
    }

    @Override
    public boolean isWritable(ISourceLocation uri) throws IOException {
        throw buildException();
    }

    @Override
    public void watch(ISourceLocation root, Consumer<ISourceLocationChanged> watcher, boolean recursive)
        throws IOException {
        throw buildException();
    }

    @Override
    public void unwatch(ISourceLocation root, Consumer<ISourceLocationChanged> watcher, boolean recursive)
        throws IOException {
        throw buildException();
    }

    @Override
    public boolean supportsRecursiveWatch() {
        return false;
    }
    
}
