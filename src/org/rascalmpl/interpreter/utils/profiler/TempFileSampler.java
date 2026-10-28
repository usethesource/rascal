package org.rascalmpl.interpreter.utils.profiler;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.rascalmpl.ideservices.GsonUtils;
import org.rascalmpl.interpreter.Evaluator;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.TypeStore;

/**
 * Sampler that temporarily stores samples on disk to avoid excessive memory usage. Samples are stored as base64-encoded
 * {@link IValue} objects (to be able to work directly with {@link ISourceLocation} objects, without additional
 * pretty-printing/parsing). To avoid blocking the main thread, an auxiliary thread is used to encode, write, read, and
 * decode samples.
 */
public class TempFileSampler implements Sampler {
	private final Evaluator evaluator;
    private final TypeStore typeStore;
	private final TempFile<Sample> tempFile;

    // To avoid pressuring the task queue of the auxiliary thread with excessively many tiny tasks, samples are buffered
    // up to a certain threshold before they are scheduled for encoding and writing. 
    private final List<Sample> buffer;
    private final int bufferSize;

	public TempFileSampler(Evaluator evaluator) throws IOException {
		this.evaluator = evaluator;
        this.typeStore = new TypeStore();
		this.tempFile = new TempFile<>(
			// Configure the handle to store samples on disk as base 64-encoded `IValue`s
			decoded -> GsonUtils.base64Encode(decoded.toIValue(evaluator.getValueFactory())),
			encoded -> Sample.of(GsonUtils.<IValue> base64Decode(encoded, typeStore))
		);

        this.buffer = new ArrayList<>();
        this.bufferSize = 1000;
	}

	@Override
	public void close() {
		tempFile.close();
	}

	public void produceSample() {
		var sample = Sample.of(evaluator);
		if (sample.callStack.isEmpty() || sample.isInitializer()) {
			return;
		}

        buffer.add(sample);
        if (buffer.size() == bufferSize) {
            tempFile.writeLines(flushBuffer());
        }
	}

	@Override
	public void consumeSamples(Consumer<Stream<Sample>> action) {
        tempFile.writeLines(flushBuffer());
		tempFile.readLines().thenAccept(samples -> {
            // `samples` is a lazily populated stream, iteratively read from the temporary file and decoded, to avoid
            // the need to keep the whole sample population in a collection in memory during consumption. Consumption
            // happens in the auxiliary thread of the temporary file handle. After consumption, the stream must be
            // closed to free resources. To ensure that `close` is called, `samples` should be considered private to
            // this object and not returned to the caller of this method.
			action.accept(samples);
			samples.close();
		}).join(); // The caller of this method should wait until consumption is done to avoid prematurely reporting completion in the REPL.
    }

    private Sample[] flushBuffer() {
        var flushed = new Sample[this.buffer.size()];
        buffer.toArray(flushed);
        buffer.clear();
        return flushed;
    }

    /**
    * Handle for a freshly created temporary file, internally coupled with a freshly created auxiliary thread dedicated
    * to: (1) encoding and writing objects of type {@code T} to the temporary file; (2) reading and decoding objects.
    */
    private static class TempFile<T> implements Closeable {
        private final ExecutorService executor; // Auxiliary thread and internal task queue
        private final Path path;
        private final BufferedWriter writer;
        private final Function<T, String> encode;
        private final Function<String, T> decode;

        public TempFile(Function<T, String> encode, Function<String, T> decode) throws IOException {
            this.executor = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
            this.path = Files.createTempFile("", ""); // Prefix and suffix don't matter
            this.writer = Files.newBufferedWriter(path);
            this.encode = encode;
            this.decode = decode;
        }

        @Override 
        public void close() {
            // First, schedule a final task to close the writer and delete the file. It is executed in the auxiliary
            // thread, via the internal task queue, to avoid the need for additional synchronization (to ensure all
            // previously scheduled tasks are executed before it).
            schedule(() -> {
                writer.close();
                Files.delete(path);
                return null; // Void
            });

            // Next, shutdown and await termination of all scheduled tasks
            executor.shutdown();
            try {
                while (!executor.awaitTermination(100, TimeUnit.MILLISECONDS));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public CompletableFuture<Stream<T>> readLines() {     
            return schedule(() -> {
                writer.flush();
                var reader = Files.newBufferedReader(path);
                var values = reader.lines().map(decode); // Lazily populated

                // The reader can be closed only when the stream has been fully consumed (outside this method)
                values.onClose(() -> {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
                
                return values;
            });
        }

        public void writeLines(T[] values) {
            schedule(() -> {
                for (T value : values) {
                    writer.write(encode.apply(value));
                    writer.newLine();
                }
                return null; // Void
            });
        }

        private <U> CompletableFuture<U> schedule(Callable<U> c) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return c.call();
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
            }, executor);
        }
    }
}
