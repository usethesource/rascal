package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFieldError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class SourceLocationResult extends ElementResult<ISourceLocation> {
	private final Type intTuple;

	protected SourceLocationResult(Type type, ISourceLocation loc, IEvaluatorContext ctx) {
		super(type, loc, ctx);
		intTuple = getTypeFactory().tupleType(getTypeFactory().integerType(), "line", getTypeFactory().integerType(), "column");
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, IEvaluatorContext ctx) {
		return that.equalToSourceLocation(this, ctx);
	}

	@Override
	public Result<?> call(Type[] argTypes, IValue[] actuals,
			IEvaluatorContext ctx) {
		if (actuals.length != 4) {
			throw new SyntaxError("location constructor", ctx.getCurrentAST().getLocation());
		}
		
		if (!argTypes[0].isSubtypeOf(getTypeFactory().integerType())) {
			throw new UnexpectedTypeError(getTypeFactory().integerType(), argTypes[0], ctx.getCurrentAST());
		}
		if (!argTypes[1].isSubtypeOf(getTypeFactory().integerType())) {
			throw new UnexpectedTypeError(getTypeFactory().integerType(), argTypes[1], ctx.getCurrentAST());
		}
		if (!argTypes[2].isSubtypeOf(intTuple)) {
			throw new UnexpectedTypeError(intTuple, argTypes[2], ctx.getCurrentAST());
		}
		if (!argTypes[3].isSubtypeOf(intTuple)) {
			throw new UnexpectedTypeError(intTuple, argTypes[3], ctx.getCurrentAST());
		}
		
		int iLength = Integer.parseInt(actuals[1].toString());
		int iOffset = Integer.parseInt(actuals[0].toString());
		int iBeginLine = Integer.parseInt(((ITuple) actuals[2]).get(0).toString());
		int iBeginColumn = Integer.parseInt(((ITuple) actuals[2]).get(1).toString());
		int iEndLine = Integer.parseInt(((ITuple) actuals[3]).get(0).toString());
		int iEndColumn = Integer.parseInt(((ITuple) actuals[3]).get(1).toString());
		URI uri = getValue().getURI();
		
		return makeResult(getTypeFactory().sourceLocationType(), getValueFactory().sourceLocation(uri, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn), ctx);
		
	}
	
	@Override
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store, IEvaluatorContext ctx) {
		IValueFactory vf = getValueFactory();
		if (name.equals("scheme")) {
			return makeResult(getTypeFactory().stringType(), vf.string(getValue().getURI().getScheme()), ctx);
		}
		else if (name.equals("authority")) {
			String authority = getValue().getURI().getAuthority();
			return makeResult(getTypeFactory().stringType(), vf.string(authority != null ? authority : ""), ctx);
		}
		else if (name.equals("host")) {
			String host = getValue().getURI().getHost();
			return makeResult(getTypeFactory().stringType(), vf.string(host != null ? host : ""), ctx);
		}
		else if (name.equals("path")) {
			String path = getValue().getURI().getPath();
			return makeResult(getTypeFactory().stringType(), vf.string(path != null ? path : ""), ctx);
		}
		else if (name.equals("fragment")) {
			String fragment = getValue().getURI().getFragment();
			return makeResult(getTypeFactory().stringType(), vf.string(fragment != null ? fragment : ""), ctx);
		}
		else if (name.equals("query")) {
			String query = getValue().getURI().getQuery();
			return makeResult(getTypeFactory().stringType(), vf.string(query != null ? query : ""), ctx);
		}
		else if (name.equals("user")) {
			String user = getValue().getURI().getUserInfo();
			return makeResult(getTypeFactory().stringType(), vf.string(user != null ? user : ""), ctx);
		}
		else if (name.equals("port")) {
			return makeResult(getTypeFactory().integerType(), vf.integer(getValue().getURI().getPort()), ctx);
		}
		else if (name.equals("length")) {
			return makeResult(getTypeFactory().integerType(), vf
					.integer(getValue().getLength()), ctx);
		} 
		else if (name.equals("offset")) {
			return makeResult(getTypeFactory().integerType(), vf
					.integer(getValue().getOffset()), ctx);
		} 
		else if (name.equals("begin")) {
			return makeResult(intTuple, vf.tuple(vf.integer(getValue().getBeginLine()), vf.integer(getValue().getBeginColumn())), ctx);
		}
		else if (name.equals("end")) {
			return makeResult(intTuple, vf.tuple(vf.integer(getValue().getEndLine()), vf.integer(getValue().getEndColumn())), ctx);
		}
		else if (name.equals("uri")) {
			return makeResult(getTypeFactory().stringType(), vf
					.string(getValue().getURI().toString()), ctx);
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
		URI uri = loc.getURI();

		Type replType = repl.getType();
		IValue replValue = repl.getValue();

		try {
			if (name.equals("uri")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				uri = new URI(((IString)repl.getValue()).getValue());
			} 
			else if (name.equals("scheme")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}

				uri = new URI(((IString) repl.getValue()).getValue(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
			}
			else if (name.equals("authority")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				uri = new URI(uri.getScheme(), ((IString) repl.getValue()).getValue(), uri.getPath(), uri.getQuery(), uri.getFragment());
			}
			else if (name.equals("host")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				uri = new URI(uri.getScheme(), uri.getUserInfo(), ((IString) repl.getValue()).getValue(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
			}
			else if (name.equals("path")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				uri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), ((IString) repl.getValue()).getValue(), uri.getQuery(), uri.getFragment());
			}
			else if (name.equals("fragment")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				uri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), ((IString) repl.getValue()).getValue());
			}
			else if (name.equals("query")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				uri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), ((IString) repl.getValue()).getValue(), uri.getFragment());
			}
			else if (name.equals("user")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				uri = new URI(uri.getScheme(), ((IString) repl.getValue()).getValue(),  uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
			}
			else if (name.equals("port")) {
				if (!replType.isIntegerType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				int port = Integer.parseInt(((IInteger) repl.getValue()).getStringRepresentation());
				uri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), port, uri.getPath(), uri.getQuery(), uri.getFragment());
			}
			else if (name.equals("length")){
				if (!replType.isIntegerType()) {
					throw new UnexpectedTypeError(getTypeFactory().integerType(), replType, ctx.getCurrentAST());
				}
				iLength = ((IInteger) replValue).intValue();
			} 
			else if (name.equals("offset")){
				if (!replType.isIntegerType()) {
					throw new UnexpectedTypeError(getTypeFactory().integerType(), replType, ctx.getCurrentAST());
				}
				iOffset = ((IInteger) replValue).intValue();
			} 
			else if (name.equals("begin")) {
				if (!replType.isSubtypeOf(intTuple)) {
					throw new UnexpectedTypeError(intTuple, replType, ctx.getCurrentAST());
				}
				iBeginLine = ((IInteger) ((ITuple) replValue).get(0)).intValue();
				iBeginColumn = ((IInteger) ((ITuple) replValue).get(1)).intValue();
			}
			else if (name.equals("end")) {
				if (!replType.isSubtypeOf(intTuple)) {
					throw new UnexpectedTypeError(intTuple, replType, ctx.getCurrentAST());
				}
				iEndLine = ((IInteger) ((ITuple) replValue).get(0)).intValue();
				iEndColumn = ((IInteger) ((ITuple) replValue).get(1)).intValue();
			}
			else {
				// TODO: is this the right exception? How so "undeclared"?
				throw new UndeclaredFieldError(name, getTypeFactory().sourceLocationType(), ctx.getCurrentAST());
			}

			ISourceLocation nloc = getValueFactory().sourceLocation(uri, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
			return makeResult(getType(), nloc, ctx);
		} 
		catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.illegalArgument(ctx.getCurrentAST(), null);
		} catch (URISyntaxException e) {
			throw RuntimeExceptionFactory.parseError(ctx.getCurrentAST().getLocation(), ctx.getCurrentAST(), ctx.getStackTrace());
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
		int compare = left.getURI().toString().compareTo(right.getURI().toString());
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
