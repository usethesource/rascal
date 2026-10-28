package org.rascalmpl.interpreter.utils.profiler;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.rascalmpl.interpreter.utils.profiler.Sampler.Labeled;
import org.rascalmpl.interpreter.utils.profiler.Sampler.Sample;

import io.usethesource.vallang.ISourceLocation;

/**
 * Formatter for the {@code .folded} format of Brendan Gregg's original `flamegraph.pl` script. Files written in this
 * format can be post-processed by that script to SVG.
 * 
 * @see https://github.com/brendangregg/flamegraph
 */
public class FoldedFormatter implements Formatter {

	@Override
	public void format(Writer writer, Stream<Sample> samples) {
        // Count for each "folded sample" how many times it occurs
		var counts = new HashMap<String, Integer>();
		var incVal = (Consumer<String>) foldedSample -> counts.merge(foldedSample, 1, Integer::sum);
		samples.map(FoldedFormatter::fold).forEach(incVal);		

        // Write the counts
        for (var count : counts.entrySet()) {
            var foldedSample = count.getKey();
            var n = count.getValue();
            Formatter.appendUnchecked(writer, foldedSample + " " + n + "\n"); // Newlines must be `\n` for `flamegraph.pl` to work (also on Windows)
        }
	}

	private static String fold(Sample sample) {
		return fold(sample.callStack);
	}

    private static String fold(List<Labeled<ISourceLocation>> callStack) {
        return callStack.stream().map(FoldedFormatter::fold).collect(Collectors.joining(";"));
    }

	private static String fold(Labeled<ISourceLocation> callFrame) {
		return callFrame.label + " at " + callFrame.value.toString();
	}

	@Override 
	public String getName() {
		return "Folded";
	}

	@Override
	public String getExtension() {
		return "folded";
	}
}
