package org.rascalmpl.util.visualize.dot;

import java.io.PrintWriter;

public class AttributeStatement {
    enum Scope { GRAPH, NODE, EDGE }

    private Scope scope;
    private DotAttribute attribute;

    void writeSource(PrintWriter writer) {
        if (scope != null) {
            switch (scope) {
                case GRAPH:
                writer.write("graph ");
                break;
                case NODE:
                writer.write("node ");
                break;
                case EDGE:
                writer.write("edge ");
                break;
            }
        }

        attribute.writeSource(writer);
    }
}
