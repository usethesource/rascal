package org.rascalmpl.util.visualize.dot;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DotSubgraph implements DotStatement {
    private String id;
    private List<DotStatement> statements;

    public DotSubgraph(String id) {
        this.id = id;
        statements = new ArrayList<>();
    }
    
    public void addStatement(DotStatement statement) {
        statements.add(statement);
    }
    
    public String getId() {
        return id;
    }

    @Override
    public void writeSource(PrintWriter writer) {
        writer.write("subgraph");
        if (id != null) {
            NodeId.writeId(writer, id);
            writer.write(" ");
        }
        writer.println(" {");
        for (DotStatement statement : statements) {
            writer.write("    ");
            statement.writeSource(writer);
            writer.println(";");
        }
    
        writer.println("    }");
    }
}
