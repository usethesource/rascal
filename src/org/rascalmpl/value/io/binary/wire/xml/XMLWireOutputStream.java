/** 
 * Copyright (c) 2016, Davy Landman, Centrum Wiskunde & Informatica (CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.value.io.binary.wire.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.rascalmpl.value.io.binary.wire.IWireOutputStream;

public class XMLWireOutputStream implements IWireOutputStream {

    private static final XMLOutputFactory fac = XMLOutputFactory.newFactory();
    private final XMLStreamWriter stream;

    public XMLWireOutputStream(OutputStream stream) {
        try {
            this.stream = fac.createXMLStreamWriter(stream);
            this.stream.writeStartDocument();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        };
    }
    @Override
    public void close() throws IOException {
        try {
            stream.writeEndDocument();
            stream.close();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void flush() throws IOException {
        try {
            stream.flush();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void startMessage(int messageId) throws IOException {
        try {
            stream.writeStartElement("Message");
            stream.writeAttribute("id", Integer.toString(messageId));
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void endMessage() throws IOException {
        try {
            stream.writeEndElement();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void writeField(int fieldId, int value) throws IOException {
        writeField("Integer", fieldId, Integer.toString(value));
    }
    @Override
    public void writeField(int fieldId, String value) throws IOException {
        writeField("String", fieldId, value);
    }
    @Override
    public void writeField(int fieldId, byte[] value) throws IOException {
        writeField("Bytes", fieldId, Base64.getEncoder().encodeToString(value));
    }

    private void writeField(String name, int fieldId, String value) throws IOException {
        try {
            stream.writeStartElement(name);
            stream.writeAttribute("id", Integer.toString(fieldId));
            stream.writeCharacters(value);
            stream.writeEndElement();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }


    @Override
    public void writeField(int fieldId, int[] values) throws IOException {
        writeField("Integers", fieldId, Arrays.stream(values).mapToObj(Integer::toString).toArray(String[]::new));
    }
    @Override
    public void writeField(int fieldId, String[] values) throws IOException {
        writeField("Strings", fieldId, values);
    }

    private void writeField(String name, int fieldId, String[] values) throws IOException {
        try {
            stream.writeStartElement(name);
            stream.writeAttribute("id", Integer.toString(fieldId));
            stream.writeAttribute("length", Integer.toString(values.length));
            for (String v : values) {
                stream.writeStartElement("Value");
                stream.writeCharacters(v);
                stream.writeEndElement();
            }
            stream.writeEndElement();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }


    @Override
    public void writeNestedField(int fieldId) throws IOException {
        try {
            stream.writeStartElement("Nested");
            stream.writeAttribute("id", Integer.toString(fieldId));
            stream.writeEndElement();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void writeRepeatedNestedField(int fieldId, int numberOfNestedElements)
            throws IOException {
        try {
            stream.writeStartElement("Nesteds");
            stream.writeAttribute("id", Integer.toString(fieldId));
            stream.writeAttribute("arity", Integer.toString(numberOfNestedElements));
            stream.writeEndElement();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }
}
