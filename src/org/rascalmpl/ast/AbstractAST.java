package org.rascalmpl.ast;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.AssignableEvaluator;
import org.rascalmpl.interpreter.BooleanEvaluator;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.PatternEvaluator;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.values.uptr.TreeAdapter;

public abstract class AbstractAST implements IVisitable {
	protected final INode node;
	protected ASTStatistics stats = new ASTStatistics();
	protected Type _type = null;
	
	AbstractAST(INode node) {
		this.node = node;
	}
	
	public Type _getType() {
	  return _type;
	}
	
	public void _setType(Type nonterminalType) {
//	  if (_type != null) {
//	    throw new ImplementationError("why set a type twice?");
//	  }
		if (_type != null && (! _type.equals(nonterminalType))) {
			// For debugging purposes
			System.err.println("In _setType, found two unequal types: " + _type.toString() + " and " + nonterminalType.toString());
		}
		this._type = nonterminalType;
	}
	
//	abstract public <T> T accept(IASTVisitor<T> v);
	public <T> T accept(IASTVisitor<T> v) {
		int x = 3;
		x = x + 1;
		return null;
	}

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

	public <T> T __evaluate(NullASTVisitor<T> eval) {
		// TODO Auto-generated method stub
		return null;
	}

	public Result<IValue> __evaluate(Evaluator eval) {
		// TODO Auto-generated method stub
		return null;
	}

	public Result<IValue> __evaluate(AssignableEvaluator eval) {
		// TODO Auto-generated method stub
		return null;
	}

	public Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor eval) {
		// TODO Auto-generated method stub
		return null;
	}

	public Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator eval) {
		// TODO Auto-generated method stub
		return null;
	}

	public IMatchingResult __evaluate(PatternEvaluator eval) {
		// TODO Auto-generated method stub
		return null;
	}

	public IBooleanResult __evaluate(BooleanEvaluator eval) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
