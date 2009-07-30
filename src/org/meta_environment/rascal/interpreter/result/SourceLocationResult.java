package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFieldError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class SourceLocationResult extends ElementResult<ISourceLocation> {

	protected SourceLocationResult(Type type, ISourceLocation loc, IEvaluatorContext ctx) {
		super(type, loc, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, IEvaluatorContext ctx) {
		return that.equalToSourceLocation(this, ctx);
	}

	
	@Override
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store, IEvaluatorContext ctx) {
		if (name.equals("length")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getLength()), ctx);
		} 
		else if (name.equals("offset")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getOffset()), ctx);
		} 
		else if (name.equals("beginLine")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getBeginLine()), ctx);
		} 
		else if (name.equals("beginColumn")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getBeginColumn()), ctx);
		} 
		else if (name.equals("endLine")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getEndLine()), ctx);
		} 
		else if (name.equals("endColumn")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getEndColumn()), ctx);
		} 
		else if (name.equals("url")) {
			return makeResult(getTypeFactory().stringType(), getValueFactory()
					.string(getValue().getURL().toString()), ctx);
		} 
		else {
			throw new UndeclaredFieldError(name, getTypeFactory().sourceLocationType(), ctx.getCurrentAST());
		}
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(String name, Result<V> repl, TypeStore store, IEvaluatorContext ctx) {
		ISourceLocation loc = getValue();
		int iLength = loc.getLength();
		int iOffset = loc.getOffset();
		int iBeginLine = loc.getBeginLine();
		int iBeginColumn = loc.getBeginColumn();
		int iEndLine = loc.getEndLine();
		int iEndColumn = loc.getEndColumn();
		String urlText = loc.getURL().toString();
		
		Type replType = repl.getType();
		IValue replValue = repl.getValue();
		if (name.equals("url")) {
			if (!replType.isStringType()) {
				throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
			}
			urlText = ((IString)repl.getValue()).getValue();
		} 
		else {
			if (!replType.isIntegerType()) {
				throw new UnexpectedTypeError(getTypeFactory().integerType(), replType, ctx.getCurrentAST());
			}
			if (name.equals("length")){
				iLength = ((IInteger) replValue).intValue();
			} 
			else if (name.equals("offset")){
				iOffset = ((IInteger) replValue).intValue();
			} 
			else if (name.equals("beginLine")){
				iBeginLine = ((IInteger) replValue).intValue();
			} 
			else if (name.equals("beginColumn")){
				iBeginColumn = ((IInteger) replValue).intValue();
			} 
			else if (name.equals("endLine")){
				iEndLine = ((IInteger) replValue).intValue();
			} 
			else if (name.equals("endColumn")){
				iEndColumn = ((IInteger) replValue).intValue();
			} 
			else {
				// TODO: is this the right exception? How so "undeclared"?
				throw new UndeclaredFieldError(name, getTypeFactory().sourceLocationType(), ctx.getCurrentAST());
			}
		}
		try {
			URL url = new URL(urlText);
			ISourceLocation nloc = getValueFactory().sourceLocation(url, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
			return makeResult(getType(), nloc, ctx);
		} 
		catch (MalformedURLException e) {
			throw new SyntaxError("URL", ctx.getCurrentAST().getLocation());
		} 
		catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.illegalArgument(ctx.getCurrentAST(), null);
		}
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result, IEvaluatorContext ctx) {
		return result.compareSourceLocation(this, ctx);
	}
	
	/////
	
	@Override
	protected <U extends IValue> Result<U> equalToSourceLocation(SourceLocationResult that, IEvaluatorContext ctx) {
		return that.equalityBoolean(this, ctx);
	}

	@Override
	protected <U extends IValue> Result<U> compareSourceLocation(SourceLocationResult that, IEvaluatorContext ctx) {
		// Note reverse of args
		ISourceLocation left = that.getValue();
		ISourceLocation right = this.getValue();
		if (left.isEqual(right)) {
			return makeIntegerResult(0, ctx);
		}
		int compare = left.getURL().toString().compareTo(right.getURL().toString());
		if (compare != 0) {
			return makeIntegerResult(compare, ctx);
		}
		int lBeginLine = left.getBeginLine();
		int rBeginLine = right.getBeginLine();

		int lEndLine = left.getEndLine();
		int rEndLine = right.getEndLine();

		int lBeginColumn = left.getBeginColumn();
		int rBeginColumn = right.getBeginColumn();

		int lEndColumn = left.getEndColumn();
		int rEndColumn = right.getEndColumn();
			
		if ((lBeginLine > rBeginLine || (lBeginLine == rBeginLine && lBeginColumn > rBeginColumn)) &&
				(lEndLine < rEndLine || ((lEndLine == rEndLine) && lEndColumn < rEndColumn))) {
			return makeIntegerResult(-1, ctx);
		} 
		return makeIntegerResult(1, ctx);
	}
	

	
}
