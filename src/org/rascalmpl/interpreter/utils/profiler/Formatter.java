package org.rascalmpl.interpreter.utils.profiler;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.stream.Stream;

import org.rascalmpl.interpreter.utils.profiler.Sampler.Sample;

/**
 * Formatter of samples
 */
public interface Formatter {

    public void format(Writer writer, Stream<Sample> samples);

    public String getName();

    public String getExtension();

    // -- Utility methods --

    public static Writer appendUnchecked(Writer w, CharSequence csq) {
        try {
            return w.append(csq);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void appendlnUnchecked(Writer w, CharSequence csq) {
        Formatter.appendUnchecked(w, csq);
        Formatter.appendUnchecked(w, System.lineSeparator());
    }
}
