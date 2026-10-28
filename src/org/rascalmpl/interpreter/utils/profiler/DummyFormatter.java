package org.rascalmpl.interpreter.utils.profiler;

import java.io.Writer;
import java.util.stream.Stream;

import org.rascalmpl.interpreter.utils.profiler.Sampler.Sample;

/**
 * Dummy formatter that does nothing
 */
public class DummyFormatter implements Formatter {

	@Override
	public void format(Writer writer, Stream<Sample> samples) {
	}

	@Override 
	public String getName() {
		return "N/A";
	}

	@Override
	public String getExtension() {
		return "txt";
	}
}
