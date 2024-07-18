package org.rascalmpl.util.visualize.dot;

import java.io.PrintWriter;

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
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (portId == null) {
            if (other.portId != null)
                return false;
        }
        else if (!portId.equals(other.portId))
            return false;
        if (direction != other.direction)
            return false;
        return true;
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
