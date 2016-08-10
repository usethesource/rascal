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

public class RSFReader implements Closeable {

    private static void log(String msg) {
        //System.err.println(msg);
    }

    public static enum ReaderPosition {
        MESSAGE_START,
        FIELD,
        MESSAGE_END {
            @Override
            public boolean isEnd() {
                return true;
            }
        };

        public boolean isEnd() {
            return false;
        }
    }

    private static final byte[] WIRE_VERSION = new byte[] { 1, 0, 0 };
    private final InputStream __stream;
    private final LinearCircularLookupWindow<String> stringsRead;
    private CodedInputStream stream;
    private boolean closed = false;
    private ReaderPosition current;
    private int messageID;
    private int fieldType;
    private int fieldID;
    private String stringValue;
    private long longValue;
    private int intValue;
    private byte[] bytesValue;

    public RSFReader(InputStream stream) throws IOException {
        this.__stream = stream;
        byte[] header = new byte[WIRE_VERSION.length];
        this.__stream.read(header);
        if (!Arrays.equals(WIRE_VERSION, header)) {
            throw new IOException("Unsupported wire format");
        }
        this.stream = CodedInputStream.newInstance(stream);
        int stringReadSize = this.stream.readRawVarint32();
        this.stringsRead = new LinearCircularLookupWindow<>(stringReadSize);
        this.stream.setSizeLimit(Integer.MAX_VALUE); // TODO: how to handle very large files (as in over the Integer.MAX_VALUE bytes?)
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

    public ReaderPosition next() throws IOException {
        int next;
        try {
            next = stream.readRawVarint32();
        } 
        catch (InvalidProtocolBufferException e) {
            log("reader: EOF");
            throw new EOFException();
        }
        if (next == 0) {
            log("reader: endMessage " + messageID);
            return current = ReaderPosition.MESSAGE_END;
        }
        fieldID = TaggedInt.getOriginal(next);
        fieldType = TaggedInt.getTag(next);
        switch (fieldType) {
            case 0:
                // special case that signals starts of values
                messageID = fieldID;
                log("reader: startMessage " + messageID);
                return current = ReaderPosition.MESSAGE_START;
            case FieldKind.STRING:
                stream.resetSizeCounter();
                stringValue = stream.readString();
                stringsRead.read(stringValue);
                log("reader: string field " + fieldID + ", " + stringValue);
                break;
            case FieldKind.LONG:
                longValue = stream.readRawVarint64();
                log("reader: long field " + fieldID + ", " + longValue);
                break;
            case FieldKind.BYTES:
                stream.resetSizeCounter();
                bytesValue = stream.readByteArray();
                log("reader: bytes field " + fieldID + ", " + bytesValue);
                break;
            case FieldKind.PREVIOUS_STR:
                int reference = stream.readRawVarint32();
                fieldType = TaggedInt.getTag(reference);
                assert fieldType == FieldKind.STRING;
                stringValue = stringsRead.lookBack(TaggedInt.getOriginal(reference));
                log("reader: previous field " + fieldID + ", " + stringValue);
                break;
            default:
                throw new IOException("Unexpected wire type: " + fieldType);
        }
        return current = ReaderPosition.FIELD;
    }


    public ReaderPosition current() {
        return current;
    }

    public int message() {
        assert current == ReaderPosition.MESSAGE_START;
        return messageID;
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

    public void skipMessage() throws IOException {
        int toSkip = 1;
        while (toSkip != 0) {
            switch (next()) {
                case MESSAGE_START:
                    toSkip++;
                    break;
                case MESSAGE_END:
                    toSkip--;
                    break;
                default:
                    break;
            }
        }
    }

  
}
