/*
 * Copyright (c) 2018-2026, NWO-I CWI and Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
