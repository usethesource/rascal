package org.rascalmpl.util.visualize.dot;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DotNode implements DotStatement {
    public static DotNode createArrayNode(NodeId id, int size) {
        DotNode node = new DotNode(id);
        node.addAttribute(DotAttribute.createArrayLabel(size));
        node.addAttribute(DotAttribute.ATTR_NODE_SHAPE, DotAttribute.NODE_SHAPE_RECORD);
        return node;
    }

    private NodeId id;
    private List<DotAttribute> attributes;

    public DotNode(String id) {
        this(new NodeId(id));
    }

    public DotNode(NodeId id) {
        this.id = id;
        attributes = new ArrayList<>();
    }

    public NodeId getId() {
        return id;
    }

    public void addAttribute(String property, String value) {
        addAttribute(new DotAttribute(property, value));
    }

    public void addAttribute(DotAttribute attribute) {
        attributes.add(attribute);
    }

    public void setAttribute(String property, String value) {
        for (DotAttribute attribute : attributes) {
            if (attribute.getProperty().equals(property)) {
                attribute.setValue(value);
                return;
            }
        }

        addAttribute(property, value);
    }

    public String getAttributeValue(String property) {
        for (DotAttribute attribute : attributes) {
            if (attribute.getProperty().equals(property)) {
                return attribute.getValue();
            }
        }

        return null;
    }

    public void setLabel(String label) {
        setAttribute(DotAttribute.ATTR_LABEL, label);
    }

    @Override
    public void writeSource(PrintWriter writer) {
        id.writeSource(writer);
        DotAttribute.writeAttributes(writer, attributes);
    }
}
