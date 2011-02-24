package org.rascalmpl.ast;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.AssignableEvaluator;
import org.rascalmpl.interpreter.BooleanEvaluator;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.PatternEvaluator;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public abstract class AbstractAST implements IVisitable {
	protected ASTStatistics stats = new ASTStatistics();
	protected Type _type = null;
	protected IConstructor _production = null;
	protected final TypeFactory TF = TypeFactory.getInstance();
	protected final RascalTypeFactory RTF = RascalTypeFactory.getInstance();
	protected final IValueFactory VF = ValueFactoryFactory.getValueFactory();
	protected final ISourceLocation _location;
	
	AbstractAST(ISourceLocation loc) {
		this._location = loc;
	}
	
	IConstructor getTree() {
		throw new NotYetImplemented("should reconstruct parse tree from AST for debugging purposes");
	}
	
	public static <T extends IValue> Result<T> makeResult(Type declaredType, IValue value, IEvaluatorContext ctx) {
		return ResultFactory.makeResult(declaredType, value, ctx);
	}
	
	public Type _getType() {
	  return _type;
	}

	public void _setProduction(IConstructor production) {
		this._production = production;
	}
	
	public IConstructor _getProduction() {
		return _production;
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
		return null;
	}

	public ISourceLocation getLocation() {
		return _location;
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
			return other.getLocation().isEqual(getLocation());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return 7 + 23 * _location.hashCode() + 17 * getClass().hashCode();
	}

	@Override
	/**
	 * For debugging purposes
	 */
	public String toString() {
		return "TODO: write a better toString for AST nodes";
	}

	public Result<IValue> interpret(Evaluator eval) {
		throw new NotYetImplemented(this);
	}

	public Result<IValue> assignment(AssignableEvaluator eval) {
		throw new NotYetImplemented(this);
	}

	/**
	 * Computes internal type representations for type literals and patterns. 
	 */
	public Type typeOf(Environment env) {
		throw new NotYetImplemented(this);
	}

	public Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator eval) {
		throw new NotYetImplemented(this);
	}

	public IMatchingResult buildMatcher(PatternEvaluator eval) {
		throw new UnsupportedPatternError(toString(), this);
	}

	public IBooleanResult buildBooleanBacktracker(BooleanEvaluator eval) {
		throw new NotYetImplemented(this);
	}
	
}
