package org.rascalmpl.repl;

import java.io.IOException;
import java.io.StringWriter;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;
import org.fusesource.jansi.Ansi.Color;
import org.rascalmpl.interpreter.utils.LimitedResultWriter;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextWriter;

public class ReplTextWriter extends StandardTextWriter {

    private static final String RESET = Ansi.ansi().reset().toString();
    private static final String RESET_RED = Ansi.ansi().reset().fg(Color.RED).toString();
    private static final String SOURCE_LOCATION_PREFIX = Ansi.ansi().reset().fg(Color.BLUE).a(Attribute.UNDERLINE).toString();
    public ReplTextWriter() {
        super(true);
    }

    public ReplTextWriter(boolean indent) {
        super(indent);
    }

    public static String valueToString(IValue value) {
        try(StringWriter stream = new StringWriter()) {
            new ReplTextWriter().write(value, stream);
            return stream.toString();
        } catch (IOException ioex) {
            throw new RuntimeException("Should have never happened.", ioex);
        }
    }
    
    private static final class GroupingWriter extends java.io.Writer {
        
        private final java.io.Writer original;
        private StringBuilder groupingBuffer;

        public GroupingWriter(java.io.Writer original) {
            this.original = original;
            groupingBuffer = null;
        }
        
        public boolean writingToErrorStream() {
            return original instanceof LimitedResultWriter;
        }

        @Override
        public void write(String str, int off, int len) throws IOException {
            if (groupingBuffer != null) {
                groupingBuffer.append(str, off, len);
            }
            else {
                original.write(str, off, len);
            }
        }
        
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            if (groupingBuffer != null) {
                groupingBuffer.append(cbuf, off, len);
            }
            else {
                original.write(cbuf, off, len);
            }
        }

        @Override
        public void flush() throws IOException {
            if (groupingBuffer != null) {
                original.flush();
            }
        }

        @Override
        public void close() throws IOException {
            stopGrouping();
            original.close();
        }


        public void stopGrouping() throws IOException {
            if (groupingBuffer != null) {
                original.write(groupingBuffer.toString());
                groupingBuffer = null;
            }
        }
        
        public void startGrouping() throws IOException {
            if (groupingBuffer == null) {
                groupingBuffer = new StringBuilder(256);
            }
        }
        
    }

    @Override
    public void write(IValue value, final java.io.Writer stream) throws IOException {
        GroupingWriter groupingWriter = new GroupingWriter(stream);

        value.accept(new Writer(groupingWriter, this.indent, this.tabSize) {
            @Override
            public IValue visitSourceLocation(ISourceLocation o) throws IOException {
                groupingWriter.startGrouping(); 
                try {
                    groupingWriter.write(SOURCE_LOCATION_PREFIX);
                    IValue result = super.visitSourceLocation(o);
                    groupingWriter.write(groupingWriter.writingToErrorStream() ? RESET_RED : RESET);
                    return result;
                }
                finally {
                    groupingWriter.stopGrouping();
                }
            }
        });
    }


}
