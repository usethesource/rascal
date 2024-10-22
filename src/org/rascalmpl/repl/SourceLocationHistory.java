package org.rascalmpl.repl;

import static jline.internal.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;

import org.jline.reader.History.Entry;
import org.jline.utils.Log;
import org.rascalmpl.uri.URIResolverRegistry;

import io.usethesource.vallang.ISourceLocation;


public class SourceLocationHistory extends History implements PersistentHistory {

    private static final URIResolverRegistry reg = URIResolverRegistry.getInstance();
    private final ISourceLocation loc;

    public SourceLocationHistory(ISourceLocation loc) throws IOException {
        this.loc = loc;
        load(loc);
    }

    public void load(final ISourceLocation loc) throws IOException {
        checkNotNull(loc);
        if (reg.exists(loc)) {
            Log.trace("Loading history from: ", loc);
            load(reg.getInputStream(loc));
        }
    }

    public void load(final InputStream input) throws IOException {
        checkNotNull(input);
        load(new InputStreamReader(input));
    }

    public void load(final Reader reader) throws IOException {
        checkNotNull(reader);
        BufferedReader input = new BufferedReader(reader);

        String item;
        while ((item = input.readLine()) != null) {
            internalAdd(item);
        }
    }

    public void flush() throws IOException {
        Log.trace("Flushing history");

        try (PrintStream out = new PrintStream(reg.getOutputStream(loc, false))) {
            for (Entry entry : this) {
                out.println(entry.value());
            }
        }
    }

    public void purge() throws IOException {
        Log.trace("Purging history");
        clear();
        reg.remove(loc, true);
    }

}
