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
import java.util.Objects;

public class NodeId {
    public static void writeId(PrintWriter stream, String id) {
        stream.write("\"");
        writeString(stream, id);
        stream.write("\"");
    }

    public static void writeString(PrintWriter stream, String s) {
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"') {
                stream.append('\\');
            }
            if (c == '\n') {
                stream.append("\\n");
                continue;
            }
            stream.append(c);
        }
    }

    private String id;
    private String portId;
    private CompassPoint direction;

    public NodeId(String id) {
        this(id, null, null);
    }

    public NodeId(NodeId original, String portId) {
        this(original.id, portId, null);
    }

    public NodeId(String id, String portId, CompassPoint direction) {
        this.id = id;
        this.portId = portId;
        this.direction = direction;
    }

    public NodeId(NodeId original, String portId, CompassPoint direction) {
        this.id = original.id;
        this.portId = portId;
        this.direction = direction;
    }

    public String getId() {
        return id;
    }

    public String getPortId() {
        return portId;
    }

    public CompassPoint getDirection() {
        return direction;
    }

    void writeSource(PrintWriter writer) {
        writeId(writer, id);
        if (portId != null) {
            writer.write(":");
            writeId(writer, portId);
            if (direction != null) {
                writer.write(":");
                writer.write(direction.name().toLowerCase());
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((portId == null) ? 0 : portId.hashCode());
        result = prime * result + ((direction == null) ? 0 : direction.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NodeId other = (NodeId) obj;

        return Objects.equals(id, other.id)
            && Objects.equals(portId, other.portId)
            && direction == other.direction;
    }

    @Override
    public String toString() {
        String result = id;

        if (portId != null) {
            result += ":" + portId;
        }

        if (direction != null) {
            result += ":" + direction;
        }

        return result;
    }


}
