package org.rascalmpl.interpreter.utils.profiler;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

/**
 * Sampler of an evaluator. As samplers may use resources that need to be freed after use, implementations of this
 * interface must implement {@link #close()}.
 */
public interface Sampler extends Closeable {

    public void close();

	public void produceSample();

	public void consumeSamples(Consumer<Stream<Sample>> action);

	/**
	 * Representation of a sample. Each sample consists of the time it was taken in nanoseconds and the call stack at
	 * that time. Each call stack consists of the call frames. Each call frame consists of the name of a function and
	 * the location of the call.
     *
     * <p>
     * This class also contains utility methods to convert samples to/from {@link IValue}s.
     * </p>
	 */
	static class Sample {
		public final long nanoTime;
		public final List<Labeled<ISourceLocation>> callStack;

		public Sample(long nanoTime, List<Labeled<ISourceLocation>> callStack) {
			this.nanoTime = nanoTime;
			this.callStack = callStack;
		}

		public boolean isInitializer() {
			var label = callStack.get(0).label;
			return Objects.equals("kwp initializer", label) ||
				Objects.equals("keyword parameter initializer", label);
		}

		public IValue toIValue(IValueFactory vf) {
			var list = vf.listWriter();
			for (var frame : callStack) {
				list.append(frame.toIValue(vf));
			}
			return vf.tuple(vf.integer(nanoTime), list.done());
		}

		public static Sample of(IValue v) {
            // Assumption: `v` has the right shape  (result of `toIValue`)
			var nanoTime = ((IInteger) ((ITuple) v).get(0)).longValue();
			var callStack = ((IList) ((ITuple) v).get(1)).stream()
				.map(Labeled::<ISourceLocation> of)
				.collect(Collectors.toList());

			return new Sample(nanoTime, callStack);
		}

		public static Sample of(Evaluator evaluator) {
			var nanoTime = System.nanoTime();
			var callStack = new ArrayList<Labeled<ISourceLocation>>();

			// Instead of calling `evaluator.getStackTrace`, the following code to traverse the stack of environments is
			// useful to get the call stack in the right iteration order (bottom-to-top instead of top-to-bottom).
			var e = evaluator.getCurrentEnvt();
			while (e != null) {
                var callFrame = new Labeled<>(locationOf(e), functionNameOf(e));
				callStack.add(0, callFrame);
				e = e.getCallerScope();
			}

			return new Sample(nanoTime, callStack);
		}

		private static ISourceLocation locationOf(Environment e) {
			var location = e.getCallerLocation();
			return location == null ? URIUtil.unknownLocation() : location;
		}

		private static String functionNameOf(Environment e) {
			// Each call frame in the call stack is represented as a linked list of environments in the evaluator. To
			// get the "right" name of a call frame (e.g., for the purpose of constructing a flame chart), the list of
            // environments needs to be traversed until the right one is found.
			var name = e.getName();
			var parent = e.getParent();
			if (Objects.equals("Anonymous Function", name)) {
				return name + " (" + e.getCreatorLocation() + ")"; 
			} else if (parent == null || parent == e.getRoot() || e.isFunctionFrame()) {
				return name;
			} else {
				return functionNameOf(parent);
			}
		}
	}

	/**
	 * Representation of a string-labeled `IValue`
	 */
	static class Labeled<T extends IValue> {
		public final T value;
		public final String label;
		
		public Labeled(T value, String label) {
			this.value = value;
			this.label = label;
		}

		public IValue toIValue(IValueFactory vf) {
			return vf.tuple(value, vf.string(label));
		}

		@SuppressWarnings("unchecked")
		public static <T extends IValue> Labeled<T> of(IValue v) {
            // Assumption: `v` has the right shape (result of `toIValue`)
			var value = (T) ((ITuple) v).get(0);
			var label = ((IString) ((ITuple) v).get(1)).getValue();
			return new Labeled<T>(value, label);
		}
	}
}
