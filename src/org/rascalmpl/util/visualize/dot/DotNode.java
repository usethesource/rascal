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
