/**
 * Copyright (c) 2024, NWO-I Centrum Wiskunde & Informatica (CWI)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/

 package org.rascalmpl.util.visualize.dot;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class DotAttribute {
    public static final String ATTR_LABEL = "label";
    public static final String ATTR_COLOR = "color";
    public static final String ATTR_NODE_SHAPE = "shape";
    public static final String NODE_SHAPE_RECORD = "record";

    String property;
    String value;

    public DotAttribute(String property, String value) {
        this.property = property;
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    void writeSource(PrintWriter writer) {
        NodeId.writeId(writer, property);
        writer.write("=");
        NodeId.writeId(writer, value);
    }

    static void writeAttributes(PrintWriter writer, List<DotAttribute> attributes) {
        if (!attributes.isEmpty()) {
            writer.write("[");
            boolean first = true;
            for (DotAttribute attribute : attributes) {
                if (first) {
                    first = false;
                } else {
                    writer.write(", ");
                }
                attribute.writeSource(writer);
            }
            writer.write("]");
        }
    }

    public static DotAttribute createRecordLabel(List<String> elements) {
        StringBuilder value = new StringBuilder();

        boolean first = true;
        for (String element : elements) {
            if (first) {
                first = false;
            } else {
                value.append(" | ");
            }

            value.append('<');
            value.append(element);
            value.append('>');
            value.append(' ');
            value.append(element);
        }

        return new DotAttribute(ATTR_LABEL, value.toString());
    }

    public static DotAttribute createArrayLabel(int size) {
        StringBuilder value = new StringBuilder();

        boolean first = true;
        for (int i=0; i<size; i++) {
            if (first) {
                first = false;
            } else {
                value.append(" | ");
            }

            value.append('<');
            value.append(i);
            value.append('>');
            value.append(' ');

            value.append(i);
        }

        return new DotAttribute(ATTR_LABEL, value.toString());
    }

    public static DotAttribute createRecordLabel(DotRecord rec) {
        StringWriter writer = new StringWriter();
        rec.writeSource(new PrintWriter(writer, true), true);
        return new DotAttribute(ATTR_LABEL, writer.toString());
    }

}
