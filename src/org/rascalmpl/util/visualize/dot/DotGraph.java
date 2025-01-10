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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Loosely follow (a subset of) the DotGraph syntax definition:
https://graphviz.org/doc/info/lang.html
*/

public class DotGraph {
    private static final String COLOR_HIGHLIGHT = "red";

    private String id;
    private boolean digraph;
    private List<DotStatement> statements;
    private Map<NodeId, DotNode> nodes;
    
    public DotGraph(String id, boolean digraph) {
        this.id = id;
        this.digraph = digraph;
        statements = new ArrayList<>();
        nodes = new HashMap<>();
    }

    private void addStatement(DotStatement statement) {
        statements.add(statement);
    }

    public boolean containsNode(NodeId id) {
        return nodes.containsKey(id);
    }

    public boolean addNode(DotNode node) {
        if (nodes.containsKey(node.getId())) {
            return false;
        }

        addStatement(node);
        nodes.put(node.getId(), node);
        return true;
    }

    public boolean addNode(String id, String label) {
        return addNode(new NodeId(id), label);
    }

    public boolean addNode(NodeId id, String label) {
        if (nodes.containsKey(id)) {
            return false;
        }

        DotNode node = new DotNode(id);
        node.addAttribute(DotAttribute.ATTR_LABEL, label);
        return addNode(node);
    }

    public void addArrayNode(NodeId id, int size) {
        DotNode node = new DotNode(id);
        node.addAttribute(DotAttribute.ATTR_NODE_SHAPE, DotAttribute.NODE_SHAPE_RECORD);
        node.addAttribute(DotAttribute.createArrayLabel(size));
        addNode(node);
    }

    public void addRecordNode(NodeId id, DotRecord dotRecord) {
        DotNode node = new DotNode(id);
        node.addAttribute(DotAttribute.ATTR_NODE_SHAPE, DotAttribute.NODE_SHAPE_RECORD);
        node.addAttribute(DotAttribute.createRecordLabel(dotRecord));
        addNode(node);
    }

    public void addAttribute(NodeId id, String name, String value) {
        DotNode node = nodes.get(id);
        if (node != null) {
            node.addAttribute(name, value);
        }
    }

    public void highlight(NodeId id, String color) {
        addAttribute(id, DotAttribute.ATTR_COLOR, color);
    }

    public void highlight(NodeId id) {
        highlight(id, COLOR_HIGHLIGHT);
    }

    public void addEdge(String from, String to) {
        addEdge(new NodeId(from), new NodeId(to));
    }

    public void addEdge(NodeId from, NodeId to) {
        addEdge(new DotEdge(from, true, to));
    }

    public void addEdge(NodeId from, NodeId to, String label, String color) {
        DotEdge edge = new DotEdge(from, true, to);
        edge.addAttribute(DotAttribute.ATTR_LABEL, label);
        if (color != null) {
            edge.addAttribute(DotAttribute.ATTR_COLOR, color);
        }
        addEdge(edge);
    }

    public void addEdge(NodeId from, NodeId to, String label) {
        addEdge(from, to, label, null);
    }

    public void addEdge(DotEdge edge) {
        addStatement(edge);
    }

    public void writeSource(PrintWriter writer) {
        writer.write(digraph ? "digraph" : "graph");
        if (id != null) {
            writer.write(" ");
            writer.write(id);
        }
        writer.println(" {");
        writer.println("ordering = out;");
        for (DotStatement statement : statements) {
            statement.writeSource(writer);
            writer.println(";");
        }
        writer.println("}");
    }

    public String toString() {
        StringWriter writer = new StringWriter();
        writeSource(new PrintWriter(writer));
        return writer.toString();
    }
}
