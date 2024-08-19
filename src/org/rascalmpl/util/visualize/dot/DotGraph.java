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

    public void addNode(DotNode node) {
        if (!nodes.containsKey(node.getId())) {
            addStatement(node);
            nodes.put(node.getId(), node);
        }
    }

    public void addNode(String id, String label) {
        addNode(new NodeId(id), label);
    }

    public void addNode(NodeId id, String label) {
        DotNode node = new DotNode(id);
        node.addAttribute(DotAttribute.ATTR_LABEL, label);
        addNode(node);
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

    public void highlight(NodeId id) {
        DotNode node = nodes.get(id);
        if (node != null) {
            node.addAttribute(DotAttribute.ATTR_COLOR, COLOR_HIGHLIGHT);
        }
    }

    public void addEdge(String from, String to) {
        addEdge(new NodeId(from), new NodeId(to));
    }

    public void addEdge(NodeId from, NodeId to) {
        addEdge(new DotEdge(from, true, to));
    }

    public void addEdge(NodeId from, NodeId to, String label) {
        DotEdge edge = new DotEdge(from, true, to);
        edge.addAttribute(DotAttribute.ATTR_LABEL, label);
        addEdge(edge);
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
