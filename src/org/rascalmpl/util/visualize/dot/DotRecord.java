package org.rascalmpl.util.visualize.dot;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DotRecord implements DotRecordEntry {
    private List<DotRecordEntry> entries;

    public DotRecord() {
        entries = new ArrayList<>();
    }

    public void addEntry(DotRecordEntry entry) {
        entries.add(entry);
    }

    @Override
    public void writeSource(PrintWriter writer, boolean topLevel) {
        if (!topLevel) {
            writer.write("{ ");
        }
        boolean first = true;
        for (DotRecordEntry entry : entries) {
            if (first) {
                first = false;
            } else {
                writer.write(" | ");
            }

            entry.writeSource(writer, false);
        }
        if (!topLevel) {
            writer.write("} ");
        }
    }
}
