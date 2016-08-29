package org.rascalmpl.value.io.binary;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

import org.rascalmpl.value.io.binary.util.FieldKind;
import org.rascalmpl.value.io.binary.util.MapLastWritten;
import org.rascalmpl.value.io.binary.util.TaggedInt;
import org.rascalmpl.value.io.binary.util.TrackLastWritten;

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

    void writeFieldTag(final int fieldId, final int type) throws IOException {
        stream.writeRawVarint32(TaggedInt.make(fieldId, type));
    }

    public void startMessage(int messageId) throws IOException {
        assertNotClosed();
        writeFieldTag(messageId, 0);
    }

    public void writeField(int fieldId, String value) throws IOException {
        assertNotClosed();
        int alreadyWritten = stringsWritten.howLongAgo(value);
        if (alreadyWritten != -1) {
            writeFieldTag(fieldId, FieldKind.PREVIOUS_STR);
            stream.writeRawVarint64(TaggedInt.make(alreadyWritten, FieldKind.STRING));
        }
        else {
            writeFieldTag(fieldId, FieldKind.STRING);
            stream.writeStringNoTag(value);
            stringsWritten.write(value);
        }
    }
    
    public void writeField(int fieldId, long value) throws IOException {
        assertNotClosed();
        writeFieldTag(fieldId, FieldKind.LONG);
        stream.writeRawVarint64(value);
    }
    
    public void writeField(int fieldId, byte[] value) throws IOException {
        assertNotClosed();
        writeFieldTag(fieldId, FieldKind.BYTES);
        stream.writeByteArrayNoTag(value);
    }
    
    public void writeField(int fieldId, int[] values) throws IOException {
        assertNotClosed();
        writeFieldTag(fieldId, FieldKind.REPEATED);
        throw new RuntimeException("Ask Davy");
    }
    
    public void writeField(int fieldId, long[] values) throws IOException {
        assertNotClosed();
        writeFieldTag(fieldId, FieldKind.REPEATED);
        throw new RuntimeException("Ask Davy");
    }
    
    public void writeField(int fieldId, String[] values) throws IOException {
        assertNotClosed();
        writeFieldTag(fieldId, FieldKind.REPEATED);
        throw new RuntimeException("Ask Davy");
    }
    
    public void endMessage() throws IOException {
        assertNotClosed();
        writeFieldTag(0, 0);
    }
    
    public void writeEmptyMessage(int messageId) throws IOException {
        startMessage(messageId);
        endMessage();
    }
}
