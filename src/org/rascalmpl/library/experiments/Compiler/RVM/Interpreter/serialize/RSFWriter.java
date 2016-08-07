package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util.FieldKind;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util.MapLastWritten;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util.TaggedInt;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util.TrackLastWritten;

import com.google.protobuf.CodedOutputStream;

public class RSFWriter implements Closeable, Flushable {

    private static final byte[] WIRE_VERSION = new byte[] { 1, 0, 0 };
    private static final int STRING_WRITTEN_SIZE = 2 * 1024;
    private boolean closed = false;
    private final CodedOutputStream stream;
    private final OutputStream __stream;
    private final TrackLastWritten<String> stringsWritten;

    public RSFWriter(OutputStream stream) throws IOException {
        this.__stream = stream;
        this.stream = CodedOutputStream.newInstance(stream);
        this.stringsWritten = new MapLastWritten<>(STRING_WRITTEN_SIZE);
        this.__stream.write(WIRE_VERSION);
        this.stream.writeRawVarint32(STRING_WRITTEN_SIZE);
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            try (OutputStream closeJava = __stream) {
                flush();
            }
            finally {
                closed = true;
            }
        }
    }
    
    void assertNotClosed() throws IOException {
        if (closed) {
            throw new IOException("Stream already closed"); 
        }
    }

    @Override
    public void flush() throws IOException {
        assertNotClosed();
        stream.flush();
        __stream.flush();
    }

    void writeFieldTag(final int fieldID, final int type) throws IOException {
        stream.writeRawVarint32(TaggedInt.make(fieldID, type));
    }

    public void startValue(int valueID) throws IOException {
        assertNotClosed();
        writeFieldTag(valueID, 0);
    }

    public void writeField(int fieldID, String value) throws IOException {
        assertNotClosed();
        int alreadyWritten = stringsWritten.howLongAgo(value);
        if (alreadyWritten != -1) {
            writeFieldTag(fieldID, FieldKind.PREVIOUS_STR);
            stream.writeRawVarint64(TaggedInt.make(alreadyWritten, FieldKind.STRING));
        }
        else {
            writeFieldTag(fieldID, FieldKind.STRING);
            stream.writeStringNoTag(value);
            stringsWritten.write(value);
        }
    }
    
    public void writeField(int fieldID, long value) throws IOException {
        assertNotClosed();
        writeFieldTag(fieldID, FieldKind.LONG);
        stream.writeRawVarint64(value);
    }

    public void writeField(int fieldID, boolean value) throws IOException {
        assertNotClosed();
        writeFieldTag(fieldID, FieldKind.LONG);
        stream.writeRawVarint64(value ? 1 : 0);
    }
    
    public void writeField(int fieldID, byte[] value) throws IOException {
        assertNotClosed();
        writeFieldTag(fieldID, FieldKind.BYTES);
        stream.writeByteArrayNoTag(value);
    }
    
    public void writeField(int fieldID, int[] values) throws IOException{
        int n = values.length;
        writeFieldTag(fieldID, FieldKind.LONG);
        for(int i = 0; i < n; i++){
            stream.writeRawVarint64(values[i]);
        }
    }

    public void endValue() throws IOException {
        assertNotClosed();
        writeFieldTag(0, 0);
    }
}
