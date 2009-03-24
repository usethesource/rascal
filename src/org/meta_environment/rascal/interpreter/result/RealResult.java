package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;

import org.meta_environment.rascal.ast.AbstractAST;

public class RealResult extends ElementResult<IReal> {
	private static final int PRECISION = 80*80; // ONE PAGE OF DIGITS

	public RealResult(IReal real) {
		this(real.getType(), real);
	}
	
	public RealResult(Type type, IReal real) {
		super(type, real);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result, AbstractAST ast) {
		return result.addReal(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> result, AbstractAST ast) {
		return result.multiplyReal(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> divide(Result<V> result, AbstractAST ast) {
		return result.divideReal(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result, AbstractAST ast) {
		return result.subtractReal(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> modulo(Result<V> result, AbstractAST ast) {
		return result.moduloReal(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, AbstractAST ast) {
		return that.equalToReal(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, AbstractAST ast) {
		return that.nonEqualToReal(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result, AbstractAST ast) {
		return result.lessThanReal(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result, AbstractAST ast) {
		return result.lessThanOrEqualReal(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result, AbstractAST ast) {
		return result.greaterThanReal(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result, AbstractAST ast) {
		return result.greaterThanOrEqualReal(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result, AbstractAST ast) {
		return result.compareReal(this, ast);
	}
	
	/// real impls start here
	
	@Override
	public <U extends IValue> Result<U> negative(AbstractAST ast) {
		return makeResult(type, getValue().negate());
	}
	
	
	@Override
	protected <U extends IValue> Result<U> addInteger(IntegerResult n, AbstractAST ast) {
		return n.widenToReal().add(n, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractInteger(IntegerResult n, AbstractAST ast) {
		// Note reversed args: we need n - this
		return n.widenToReal().subtract(n, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyInteger(IntegerResult n, AbstractAST ast) {
		return n.widenToReal().multiply(n, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> divideInteger(IntegerResult n, AbstractAST ast) {
		// Note reversed args: we need n / this
		return n.widenToReal().divide(this, ast);
	}
	
	@Override  
	protected <U extends IValue> Result<U> addReal(RealResult n, AbstractAST ast) {
		return makeResult(type, getValue().add(n.getValue()));
	}
	
	@Override 
	protected <U extends IValue> Result<U> subtractReal(RealResult n, AbstractAST ast) {
		// note the reverse subtraction.
		return makeResult(type, n.getValue().subtract(getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> multiplyReal(RealResult n, AbstractAST ast) {
		return makeResult(type, getValue().multiply(n.getValue()));
	}

	@Override
	protected <U extends IValue> Result<U> divideReal(RealResult n, AbstractAST ast) {
		// note the reverse division
		return makeResult(type, n.getValue().divide(getValue(), PRECISION));
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToReal(RealResult that, AbstractAST ast) {
		return that.equalityBoolean(this);
	}

	@Override
	protected <U extends IValue> Result<U> nonEqualToReal(RealResult that, AbstractAST ast) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanReal(RealResult that, AbstractAST ast) {
		// note reversed args: we need that < this
		return bool(that.comparisonInts(this, ast) < 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualReal(RealResult that, AbstractAST ast) {
		// note reversed args: we need that <= this
		return bool(that.comparisonInts(this, ast) <= 0);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanReal(RealResult that, AbstractAST ast) {
		// note reversed args: we need that > this
		return bool(that.comparisonInts(this, ast) > 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualReal(RealResult that, AbstractAST ast) {
		// note reversed args: we need that >= this
		return bool(that.comparisonInts(this, ast) >= 0);
	}

	@Override
	protected <U extends IValue> Result<U> equalToInteger(IntegerResult that, AbstractAST ast) {
		return that.widenToReal().equals(this, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToInteger(IntegerResult that, AbstractAST ast) {
		return that.widenToReal().nonEquals(this, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanInteger(IntegerResult that, AbstractAST ast) {
		// note reversed args: we need that < this
		return that.widenToReal().lessThan(this, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualInteger(IntegerResult that, AbstractAST ast) {
		// note reversed args: we need that <= this
		return that.widenToReal().lessThanOrEqual(this, ast);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanInteger(IntegerResult that, AbstractAST ast) {
		// note reversed args: we need that > this
		return that.widenToReal().greaterThan(this, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualInteger(IntegerResult that, AbstractAST ast) {
		// note reversed args: we need that >= this
		return that.widenToReal().greaterThanOrEqual(this, ast);
	}

	
	@Override
	protected <U extends IValue> Result<U> compareReal(RealResult that, AbstractAST ast) {
		// note reverse arguments
		IReal left = that.getValue();
		IReal right = this.getValue();
		int result = left.compare(right);
		return makeIntegerResult(result);
	}

	
}
