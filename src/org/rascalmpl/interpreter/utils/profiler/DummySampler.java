package org.rascalmpl.interpreter.utils.profiler;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Dummy sampler that does nothing
 */
public class DummySampler implements Sampler {

	@Override
	public void close() {
	}

	@Override
	public void produceSample() {
	}

	@Override
	public void consumeSamples(Consumer<Stream<Sample>> action) {
	}
}
