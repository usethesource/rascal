package org.rascalmpl.values;

public interface IOrgStringVisitor {
	void visit(Chunk chunk);
	void visit(Concat concat);
	void visit(NoOrg noOrg);
	void visit(Insincere insincere);
}
