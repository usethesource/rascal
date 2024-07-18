package org.rascalmpl.util.visualize.dot;

import java.io.PrintWriter;

public interface DotRecordEntry {
        void writeSource(PrintWriter writer, boolean topLevel);
}
