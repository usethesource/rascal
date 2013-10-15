package org.eclipse.imp.pdb.facts.impl.primitive;

public interface IOrgStringVisitor {
	void visit(Chunk chunk);
	void visit(Concat concat);
	void visit(NoOrg noOrg);
}
