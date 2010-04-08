package org.rascalmpl.ast;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.values.uptr.TreeAdapter;

public abstract class AbstractAST implements IVisitable {
	protected INode node;
	protected ASTStatistics stats = new ASTStatistics();
	protected Type _type = null;
	
	public Type _getType() {
	  return _type;
	}
	
	public void _setType(Type nonterminalType) {
	  if (_type != null) {
	    throw new ImplementationError("why set a type twice?");
	  }
	  this._type = nonterminalType;
	}
	
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
