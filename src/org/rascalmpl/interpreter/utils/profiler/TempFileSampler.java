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
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.rascalmpl.interpreter.Evaluator;

import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.impl.persistent.ValueFactory;
import io.usethesource.vallang.io.binary.message.IValueReader;
import io.usethesource.vallang.io.binary.message.IValueWriter;
import io.usethesource.vallang.io.binary.util.WindowSizes;
import io.usethesource.vallang.io.binary.wire.IWireInputStream;
import io.usethesource.vallang.io.binary.wire.IWireOutputStream;
import io.usethesource.vallang.io.binary.wire.binary.BinaryWireInputStream;
import io.usethesource.vallang.io.binary.wire.binary.BinaryWireOutputStream;
import io.usethesource.vallang.type.TypeStore;

/**
 * Sampler that temporarily stores samples on disk to avoid excessive memory usage. Samples are stored as {@link IValue}
 * objects (to be able to work directly with {@link ISourceLocation} objects, without additional
 * pretty-printing/parsing). To avoid blocking the main thread, an auxiliary thread is used to write/read samples
 * to/from disk.
 */
public class TempFileSampler implements Sampler {
	private final Evaluator evaluator;
	private final TempFile tempFile;

    // To avoid pressuring the task queue of the auxiliary thread with excessively many tiny tasks, samples are buffered
    // up to a certain threshold before they are scheduled for encoding and writing. 
    private final List<RawSample> buffer;
    private final int bufferSize;

    private static final ValueFactory VF = ValueFactory.getInstance();

	public TempFileSampler(Evaluator evaluator) throws IOException {
		this.evaluator = evaluator;
        this.tempFile = new TempFile();

        this.buffer = new ArrayList<>();
        this.bufferSize = 1000;
	}

	@Override
	public void close() {
		tempFile.close();
	}

	public void produceSample() {
		var sample = RawSample.of(evaluator);
		if (sample.isEmpty() || sample.isInitializer()) {
			return;
		}

        buffer.add(sample);
        if (buffer.size() == bufferSize) {
            tempFile.write(flushBuffer());
        }
	}

	@Override
	public void consumeSamples(Consumer<Stream<RawSample>> action) {
        tempFile.write(flushBuffer());
		tempFile.read().thenAccept(samples -> {
            // `samples` is a lazily populated stream, iteratively read from the temporary file, to avoid the need to
            // keep the whole sample population in a collection in memory during consumption. Consumption happens in the
            // auxiliary thread of the temporary file handle. After consumption, the stream must be closed to free
            // resources. To ensure that `close` is called, `samples` should be considered private to this object and
            // not returned to the caller of this method.
			action.accept(samples);
			samples.close();
		}).join(); // The caller of this method should wait until consumption is done to avoid prematurely reporting completion in the REPL.
    }

    private RawSample[] flushBuffer() {
        var flushed = new RawSample[this.buffer.size()];
        buffer.toArray(flushed);
        buffer.clear();
        return flushed;
    }

    /**
    * Handle for a freshly created temporary file, internally coupled with a freshly created auxiliary thread dedicated
    * to write/read samples to that file.
    */
    private static class TempFile implements Closeable {
        private final ExecutorService executor; // Auxiliary thread and internal task queue
        private final Path path;
        private final SampleWriter writer;

        public TempFile() throws IOException {
            this.executor = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
            this.path = Files.createTempFile("rascal-sampling-profiler", "");
            this.writer = new SampleWriter(path);
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

        public CompletableFuture<Stream<RawSample>> read() {     
            return schedule(() -> {
                writer.close(); // Cannot write and read at the same time
                var reader = new SampleReader(path, writer.getCount());
                var samples = StreamSupport.stream(reader.spliterator(), false);

                // The reader can be closed only when the stream has been fully consumed (outside this method)
                samples.onClose(() -> {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
                
                return samples;
            });
        }

        public void write(RawSample[] samples) {
            schedule(() -> {
                writer.write(samples);
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

        /**
         * Wrapper around {@link IValueWriter} that counts the number of samples that have been written
         */
        private static class SampleWriter implements Closeable {
            private final AtomicInteger count;
            private final IWireOutputStream stream;

            public SampleWriter(Path path) throws IOException {
                this.count = new AtomicInteger();
                this.stream = new BinaryWireOutputStream(new ZstdOutputStream(Files.newOutputStream(path), 3), WindowSizes.NORMAL_WINDOW.stringsWindow); // Buffered internally
            }

            @Override
            public void close() throws IOException {
                stream.close();
            }

            public int getCount() {
                return count.get();
            }

            public void write(RawSample[] samples) throws IOException {
                // Write the next values in a batch (to take advantage of the sharing features of the underlying stream)
                var batch = VF.listWriter();
                for (var sample : samples) {
                    batch.append(sample.toIValue());
                }
                IValueWriter.write(stream, VF, WindowSizes.NORMAL_WINDOW, batch.done());
                count.addAndGet(samples.length);
            }
        }

        /**
         * Wrapper around {@link IValueReader} that reads the number of samples as indicated by the provided count. If
         * the file at the provided path doesn't have that many values, then an exception will be thrown.
         */
        private static class SampleReader implements Closeable, Iterable<RawSample> {
            private final AtomicInteger count;
            private final IWireInputStream stream;
            private final TypeStore typeStore;

            private Iterator<IValue> batch; // Values are written in batches (of type `IList`), so they need to be read in batches, too.

            public SampleReader(Path path, int count) throws IOException {
                this.count = new AtomicInteger(count);
                this.stream = new BinaryWireInputStream(new ZstdInputStream(Files.newInputStream(path))); // Buffered internally
                this.typeStore = new TypeStore();
            }

            @Override
            public void close() throws IOException {
                stream.close();            
            }

            @Override
            public Iterator<RawSample> iterator() {
                return new Iterator<RawSample>() {
                    @Override
                    public boolean hasNext() {
                        return count.get() > 0;
                    }
                    @Override
                    public RawSample next() {
                        try {
                            return read();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    }
                };
            }

            public RawSample read() throws IOException {
                if (count.getAndDecrement() > 0) {
                    // Read the next non-empty batch from the file
                    while (batch == null || !batch.hasNext()) {
                        batch = ((IList) IValueReader.readValue(stream, VF, () -> typeStore)).iterator();
                    }
                    // Read the next value from the batch
                    return RawSample.of((ITuple) batch.next());
                } else {
                    throw new IllegalStateException("Cannot read more samples than the provided count");
                }
            }
        }
    }
}
