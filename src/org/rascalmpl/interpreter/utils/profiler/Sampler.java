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

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.impl.persistent.ValueFactory;

/**
 * Sampler of an evaluator. As samplers may use resources that need to be freed after use, implementations of this
 * interface must implement {@link #close()}.
 */
public interface Sampler extends Closeable {

    public void close();

	public void produceSample();

	public void consumeSamples(Consumer<Stream<RawSample>> action);

	/**
	 * Representation of a sample. Each sample consists of the time it was taken in nanoseconds and the call stack at
	 * that time. Each call stack consists of a list of the call frames and the location of the program counter (current
	 * AST). Each call frame consists of the location of the caller, the location of the callee, and the name of a
	 * function.
     *
     * <p>
     * This class also contains utility methods to convert samples to/from {@link IValue}s.
     * </p>
	 */
	static class RawSample {
		public final long nanoTime;
		public final RawCallStack callStack;

		private static final IValueFactory VF = ValueFactory.getInstance();

		public RawSample(long nanoTime, RawCallStack callStack) {
			this.nanoTime = nanoTime;
			this.callStack = callStack;
		}

		public boolean isEmpty() {
			return callStack.callFrames.isEmpty();
		}

		public boolean isInitializer() {
			var name = callStack.bottom().name;
			return Objects.equals("kwp initializer", name) ||
				Objects.equals("keyword parameter initializer", name);
		}

		public IValue toIValue() {
			return VF.tuple(VF.integer(nanoTime), callStack.toIValue());
		}

		public static RawSample of(ITuple v) {
            // Assumption: `v` has the right shape  (result of `toIValue`)
			var nanoTime = ((IInteger) ((ITuple) v).get(0)).longValue();
			var callStack = RawCallStack.of((ITuple) ((ITuple) v).get(1));
			return new RawSample(nanoTime, callStack);
		}

		public static RawSample of(Evaluator evaluator) {
			// Fetch the current state of the evaluator. This isn't thread-safe, in the sense that between
			// `getCurrentAST` and `getCurrentEnvt`, the state of the evaluator may change.
			var ast = evaluator.getCurrentAST();
			var env = evaluator.getCurrentEnvt();
			return new RawSample(System.nanoTime(), RawCallStack.of(ast, env));
		}
	}

	/**
	 * Representation of a call stack
	 */
	static class RawCallStack {
		public final ISourceLocation programCounter;
		public final List<RawCallFrame> callFrames; // Top to bottom

		private static final IValueFactory VF = ValueFactory.getInstance();

		public RawCallStack(ISourceLocation programCounter, List<RawCallFrame> callFrames) {
			this.programCounter = programCounter;
			this.callFrames = callFrames;
		}

		public RawCallFrame bottom() {
			return callFrames.get(callFrames.size() - 1);
		}

		public Stream<RawCallFrame> bottomToTop() {
			var n = callFrames.size();
			return IntStream.range(0, n).mapToObj(i -> callFrames.get(n - 1 - i));
		}

		public RawCallFrame top() {
			return callFrames.get(0);
		}

		public Stream<RawCallFrame> topToBottom() {
			return callFrames.stream();
		}

		public IValue toIValue() {
			var writer = VF.listWriter();
			callFrames.stream()
				.map(RawCallFrame::toIValue)
				.forEach(writer::append);

			return VF.tuple(programCounter, writer.done());
		}

		public static RawCallStack of(ITuple v) {
			// Assumption: `v` has the right shape (result of `toIValue`)
			var programCounter = (ISourceLocation) ((ITuple) v).get(0);
			var callFrames = ((IList) ((ITuple) v).get(1)).stream()
				.map(ITuple.class::cast)
				.map(RawCallFrame::of)
				.collect(Collectors.toList());

			return new RawCallStack(programCounter, callFrames);
		}

		public static RawCallStack of(AbstractAST ast, Environment env) {
			var programCounter = ast.getLocation();
			var callFrames = new ArrayList<RawCallFrame>();
			
			// The following traversal of the call stack is thread-safe, as all relevant fields of `Environment` objects
			// are final (i.e., the evaluator may proceed and update the call stack in a different thread, but the
			// representation of the current call stack remains unchanged as far as is relevant for the purpose here).
			while (env != null) {
				callFrames.add(RawCallFrame.of(env));
				env = env.getCallerScope();
			}

			return new RawCallStack(programCounter, callFrames);
		}
	}

	/**
	 * Representation of a call frame
	 */
	static class RawCallFrame {
		public final ISourceLocation caller;
		public final ISourceLocation callee;
		public final String name;

		private static final IValueFactory VF = ValueFactory.getInstance();

		public RawCallFrame(ISourceLocation caller, ISourceLocation callee, String name) {
			this.caller = caller;
			this.callee = callee;
			this.name = name;
		}

		public IValue toIValue() {
			return VF.tuple(caller, callee, VF.string(name));
		}

		public static RawCallFrame of(ITuple v) {
			// Assumption: `v` has the right shape (result of `toIValue`)
			var caller = (ISourceLocation) ((ITuple) v).get(0);
			var callee = (ISourceLocation) ((ITuple) v).get(1);
			var name = ((IString) ((ITuple) v).get(2)).getValue();
			return new RawCallFrame(caller, callee, name);
		}

		public static RawCallFrame of(Environment e) {
			assert e != null;

			// Each call frame in the call stack is represented as a linked list of environments in the evaluator. To
			// get the "right" caller, callee, and name of a call frame (e.g., for the purpose of constructing a flame
			// chart), the list of environments needs to be traversed until the right one is found.
			while (e != null) {
				var caller = unknownLocationIfNull(e.getCallerLocation());
				var callee = unknownLocationIfNull(e.getCreatorLocation());

				var name = e.getName();
				if (Objects.equals("Anonymous Function", name)) {
					name += " (" + callee + ")";
				}

				var parent = e.getParent();
				if (parent == null || parent.isRootScope() || e.isFunctionFrame()) {
					return new RawCallFrame(caller, callee, name);
				}

				e = parent;
			}

			// This can happen only when the initial `e` was null, which shouldn't have happened (as asserted)
			throw new NullPointerException();
		}

		private static ISourceLocation unknownLocationIfNull(ISourceLocation l) {
			return l == null ? URIUtil.unknownLocation() : l;
		}
	}
}
