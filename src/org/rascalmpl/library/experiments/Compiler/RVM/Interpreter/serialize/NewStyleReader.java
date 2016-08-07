package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util.FieldKind;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util.LinearCircularLookupWindow;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util.TaggedInt;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.InvalidProtocolBufferException;

public class NewStyleReader implements Closeable {

    private final boolean debug = false;
    public static enum ReaderPosition {
        VALUE_START,
        FIELD,
        VALUE_END
    }

    private static final byte[] WIRE_VERSION = new byte[] { 1, 0, 0 };
    private final InputStream __stream;
    private final LinearCircularLookupWindow<String> stringsRead;
    private CodedInputStream stream;
    private boolean closed = false;
    private ReaderPosition current;
    private int valueID;
    private int fieldType;
    private int fieldID;
    private String stringValue;
    private long longValue;
    private byte[] bytesValue;

    public NewStyleReader(InputStream stream) throws IOException {
        this.__stream = stream;
        byte[] header = new byte[WIRE_VERSION.length];
        this.__stream.read(header);
        if (!Arrays.equals(WIRE_VERSION, header)) {
            throw new IOException("Unsupported wire format");
        }
        this.stream = CodedInputStream.newInstance(stream);
        int stringReadSize = this.stream.readRawVarint32();
        this.stringsRead = new LinearCircularLookupWindow<>(stringReadSize);
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            __stream.close();
        }
        else {
            throw new IOException("Already closed");
        }
    }
    
    public boolean isAtEnd() throws IOException{
        return stream.isAtEnd();
    }

    public ReaderPosition next() throws IOException {
        int next;
        try {
            next = stream.readRawVarint32();
        } 
        catch (InvalidProtocolBufferException e) {
            if(debug) System.out.println("reader: EOF");
            throw new EOFException();
        }
        if (next == 0) {
            if(debug) System.out.println("reader: endValue " + valueID);
            return current = ReaderPosition.VALUE_END;
        }
        fieldID = TaggedInt.getOriginal(next);
        fieldType = TaggedInt.getTag(next);
        switch (fieldType) {
            case 0:
                // special case that signals starts of values
                valueID = fieldID;
                if(debug) System.out.println("reader: startValue " + valueID);
                return current = ReaderPosition.VALUE_START;
            case FieldKind.STRING:
                stream.resetSizeCounter();
                stringValue = stream.readString();
                stringsRead.read(stringValue);
                if(debug) System.out.println("reader: string field " + fieldID + ", " + stringValue);
                break;
            case FieldKind.LONG:
                longValue = stream.readRawVarint64();
                if(debug) System.out.println("reader: long field " + fieldID + ", " + longValue);
                break;
            case FieldKind.BYTES:
                stream.resetSizeCounter();
                bytesValue = stream.readByteArray();
                if(debug) System.out.println("reader: bytes field " + fieldID + ", " + bytesValue);
                break;
            case FieldKind.PREVIOUS_STR:
                int reference = stream.readRawVarint32();
                fieldType = TaggedInt.getTag(reference);
                assert fieldType == FieldKind.STRING;
                stringValue = stringsRead.lookBack(TaggedInt.getOriginal(reference));
                if(debug) System.out.println("reader: previous field " + fieldID + ", " + stringValue);
                break;
            default:
                throw new IOException("Unexpected wire type: " + fieldType);
        }
        return current = ReaderPosition.FIELD;
    }

    public ReaderPosition current() {
        return current;
    }

    public int value() {
        assert current == ReaderPosition.VALUE_START;
        return valueID;
    }

    public int field() {
        assert current == ReaderPosition.FIELD;
        return fieldID;
    }
    
    public long getLong() {
        assert fieldType == FieldKind.LONG;
        return longValue;
    }

    public String getString() {
        assert fieldType == FieldKind.STRING;
        return stringValue;
    }
    
    public byte[] getBytes() {
        assert fieldType == FieldKind.BYTES;
        return bytesValue;
    }
    
    public int getFieldType() {
        assert current == ReaderPosition.FIELD;
        return fieldType;
    }

    public void skipValue() throws IOException {
        int toSkip = 1;
        while (toSkip != 0) {
            switch (next()) {
                case VALUE_START:
                    toSkip++;
                    break;
                case VALUE_END:
                    toSkip--;
                    break;
                default:
                    break;
            }
        }
    }

  
}
