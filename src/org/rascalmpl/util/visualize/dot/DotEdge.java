package org.rascalmpl.util.visualize.dot;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DotEdge implements DotStatement {
    private NodeId from;
    private List<NodeId> to; 
    private boolean directed;
    private List<DotAttribute> attributes;

    public DotEdge(NodeId from) {
        this(from, true);
    }

    public DotEdge(NodeId from, boolean directed) {
        this.from = from;
        this.directed = directed;
        to = new ArrayList<>();
        attributes = new ArrayList<>();
    }

    public DotEdge(NodeId from, NodeId... to) {
        this(from, true, to);
    }

    public DotEdge(NodeId from, boolean directed, NodeId... to) {
        this(from, directed);
        for (NodeId node : to) {
            addTo(node);
        }
    }

    public void addTo(NodeId node) {
        to.add(node);
    }

    public void addAttribute(String property, String value) {
        attributes.add(new DotAttribute(property, value));
    }

    @Override
   public  void writeSource(PrintWriter writer) {
        from.writeSource(writer);
        for (NodeId node : to) {
            if (directed) {
                writer.write(" -> ");
            } else {
                writer.write(" -- ");
            }

            node.writeSource(writer);
        }
        DotAttribute.writeAttributes(writer, attributes);
    }

    public static DotEdge createArrayEdge(NodeId array, int index, NodeId element) {
        return new DotEdge(new NodeId(array, String.valueOf(index)), true, element); 
    }

    public static DotEdge createArrayEdge(NodeId array, int index, CompassPoint direction, NodeId element) {
        return new DotEdge(new NodeId(array.getId(), String.valueOf(index), direction), true, element); 
    }
}

