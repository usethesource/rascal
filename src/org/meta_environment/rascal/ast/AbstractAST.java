package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.meta_environment.uptr.TreeAdapter;

public abstract class AbstractAST implements IVisitable {
	protected INode node;
	protected ASTStatistics stats = new ASTStatistics();

	abstract public <T> T accept(IASTVisitor<T> v);

	public ISourceLocation getLocation() {
		return TreeAdapter.getLocation((IConstructor) node);
	}

	public ASTStatistics getStats() {
		return stats;
	}
	
	public void setStats(ASTStatistics stats) {
		this.stats = stats;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (getClass() == obj.getClass()) {
			if (obj == this) {
				return true;
			}

			AbstractAST other = (AbstractAST) obj;

			if (other.node == node) {
				return true;
			}

			if (other.node.equals(node)) {
				return other.node.getAnnotation("loc").isEqual(
						node.getAnnotation("loc"));
			}
		}
		return false;
	}

	public INode getTree() {
		return node;
	}

	@Override
	public int hashCode() {
		return node.hashCode();
	}

	@Override
	public String toString() {
		return TreeAdapter.yield((IConstructor) node);
	}
}
