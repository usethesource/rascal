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
                value.append("| ");
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
