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
import org.meta_environment.rascal.interpreter.RuntimeExceptionFactory;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFieldError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;

import org.meta_environment.rascal.ast.AbstractAST;

public class SourceLocationResult extends ElementResult<ISourceLocation> {

	protected SourceLocationResult(Type type, ISourceLocation loc, AbstractAST ast) {
		super(type, loc, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, AbstractAST ast) {
		return that.equalToSourceLocation(this, ast);
	}

	
	@Override
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store, AbstractAST ast) {
		if (name.equals("length")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getLength()), ast);
		} 
		else if (name.equals("offset")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getOffset()), ast);
		} 
		else if (name.equals("beginLine")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getBeginLine()), ast);
		} 
		else if (name.equals("beginColumn")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getBeginColumn()), ast);
		} 
		else if (name.equals("endLine")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getEndLine()), ast);
		} 
		else if (name.equals("endColumn")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getEndColumn()), ast);
		} 
		else if (name.equals("url")) {
			return makeResult(getTypeFactory().stringType(), getValueFactory()
					.string(getValue().getURL().toString()), ast);
		} 
		else {
			throw new UndeclaredFieldError(name, getTypeFactory().sourceLocationType(), ast);
		}
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(String name, Result<V> repl, TypeStore store, AbstractAST ast) {
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
				throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ast);
			}
			urlText = ((IString)repl.getValue()).getValue();
		} 
		else {
			if (!replType.isIntegerType()) {
				throw new UnexpectedTypeError(getTypeFactory().integerType(), replType, ast);
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
				throw new UndeclaredFieldError(name, getTypeFactory().sourceLocationType(), ast);
			}
		}
		try {
			URL url = new URL(urlText);
			ISourceLocation nloc = getValueFactory().sourceLocation(url, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
			return makeResult(getType(), nloc, ast);
		} 
		catch (MalformedURLException e) {
			throw new SyntaxError("URL", ast.getLocation());
		} 
		catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.illegalArgument(ast);
		}
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result, AbstractAST ast) {
		return result.compareSourceLocation(this, ast);
	}
	
	/////
	
	@Override
	protected <U extends IValue> Result<U> equalToSourceLocation(SourceLocationResult that, AbstractAST ast) {
		return that.equalityBoolean(this, ast);
	}

	@Override
	protected <U extends IValue> Result<U> compareSourceLocation(SourceLocationResult that, AbstractAST ast) {
		// Note reverse of args
		ISourceLocation left = that.getValue();
		ISourceLocation right = this.getValue();
		if (left.isEqual(right)) {
			return makeIntegerResult(0);
		}
		int compare = left.getURL().toString().compareTo(right.getURL().toString());
		if (compare != 0) {
			return makeIntegerResult(compare);
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
			return makeIntegerResult(-1);
		} 
		return makeIntegerResult(1);
	}
	

	
}
