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

