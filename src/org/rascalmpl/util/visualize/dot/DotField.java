package org.rascalmpl.util.visualize.dot;

import java.io.PrintWriter;

public class DotField implements DotRecordEntry {
    private String label;
    private String portId;

    public DotField(String label, String portId) {
        this.label = label;
        this.portId = portId;
    }

    @Override
    public void writeSource(PrintWriter writer, boolean topLevel) {
        if (portId != null) {
            writer.write('<');
            writer.write(portId);
            writer.write("> ");
        }
        NodeId.writeString(writer, label);
    }
}
