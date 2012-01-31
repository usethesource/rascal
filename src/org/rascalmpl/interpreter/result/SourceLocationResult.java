/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredFieldError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperationError;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class SourceLocationResult extends ElementResult<ISourceLocation> {
	private final Type intTuple;

	public SourceLocationResult(Type type, ISourceLocation loc, IEvaluatorContext ctx) {
		super(type, loc, ctx);
		intTuple = getTypeFactory().tupleType(getTypeFactory().integerType(), "line", getTypeFactory().integerType(), "column");
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToSourceLocation(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
		return that.nonEqualToSourceLocation(this);
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] actuals) {
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
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store) {
		IValueFactory vf = getValueFactory();
		URI uri = getValue().getURI();
		if (name.equals("scheme")) {
			return makeResult(getTypeFactory().stringType(), vf.string(uri.getScheme()), ctx);
		}
		else if (name.equals("authority")) {
			String authority = uri.getAuthority();
			return makeResult(getTypeFactory().stringType(), vf.string(authority != null ? authority : ""), ctx);
		}
		else if (name.equals("host")) {
			String host = uri.getHost();
			return makeResult(getTypeFactory().stringType(), vf.string(host != null ? host : ""), ctx);
		}
		else if (name.equals("path")) {
			String path = uri.getPath();
			return makeResult(getTypeFactory().stringType(), vf.string(path != null ? path : ""), ctx);
		}
		else if (name.equals("parent")) {
			String path = uri.getPath();
			if (path.equals("")) {
				throw RuntimeExceptionFactory.noParent(getValue(), ctx.getCurrentAST(), ctx.getStackTrace());
			}
			int i = path.lastIndexOf("/");
			
			if (i != -1) {
				path = path.substring(0, i);
				return fieldUpdate("path", makeResult(getTypeFactory().stringType(), vf.string(path), ctx), store);
//				return makeResult(getTypeFactory().stringType(), vf.string(path), ctx);
			}
			
			throw RuntimeExceptionFactory.noParent(getValue(), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		else if (name.equals("file")) {
			String path = uri.getPath();
			
			if (path.equals("")) {
				throw RuntimeExceptionFactory.noParent(getValue(), ctx.getCurrentAST(), ctx.getStackTrace());
			}
			int i = path.lastIndexOf("/");
			
			if (i != -1) {
				path = path.substring(i+1);
				return makeResult(getTypeFactory().stringType(), vf.string(path), ctx); 
			}
			
			return makeResult(getTypeFactory().stringType(), vf.string(path), ctx);
		}
		else if (name.equals("ls")) {
			try {
				IListWriter w = ctx.getValueFactory().listWriter();
				Type stringType = getTypeFactory().stringType();
				
				for (String elem : ctx.getResolverRegistry().listEntries(uri)) {
					w.append(this.add(makeResult(stringType, vf.string(elem), ctx)).getValue());
				}
				
				IList result = w.done();
				// a list of loc's
				return makeResult(result.getType(), result, ctx);
				
			} catch (IOException e) {
				throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), ctx.getCurrentAST(), ctx.getStackTrace());
			}
		}
		else if (name.equals("extension")) {
			String path = uri.getPath();
			int i = path.lastIndexOf('.');
			if (i != -1) {
				return makeResult(getTypeFactory().stringType(), vf.string(path.substring(i + 1)), ctx);
			}
			return makeResult(getTypeFactory().stringType(), vf.string(""), ctx);
		}
		else if (name.equals("fragment")) {
			String fragment = uri.getFragment();
			return makeResult(getTypeFactory().stringType(), vf.string(fragment != null ? fragment : ""), ctx);
		}
		else if (name.equals("query")) {
			String query = uri.getQuery();
			return makeResult(getTypeFactory().stringType(), vf.string(query != null ? query : ""), ctx);
		}
		else if (name.equals("user")) {
			String user = uri.getUserInfo();
			return makeResult(getTypeFactory().stringType(), vf.string(user != null ? user : ""), ctx);
		}
		else if (name.equals("port")) {
			return makeResult(getTypeFactory().integerType(), vf.integer(uri.getPort()), ctx);
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
					.string(uri.toString()), ctx);
		} 
		else if (name.equals("top")) {
			return makeResult(getTypeFactory().sourceLocationType(), vf.sourceLocation(uri), ctx);
		} 
		else {
			throw new UndeclaredFieldError(name, getTypeFactory().sourceLocationType(), ctx.getCurrentAST());
		}
	}
	
	private URI newURI(String scheme,
            String userInfo, String host, int port,
            String path, String query, String fragment)
	throws URISyntaxException{
		String h  = host == null ? "" : host;
		return new URI(scheme, userInfo, h, port, path, query, fragment);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(String name, Result<V> repl, TypeStore store) {
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

				uri = newURI(((IString) repl.getValue()).getValue(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
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
				uri = newURI(uri.getScheme(), uri.getUserInfo(), ((IString) repl.getValue()).getValue(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
			}
			else if (name.equals("path")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				String path = ((IString) repl.getValue()).getValue();
				if(!path.startsWith("/"))
					path = "/" + path;
				uri = newURI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), path, uri.getQuery(), uri.getFragment());
			}
			else if (name.equals("file")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				
				String path = uri.getPath();
				int i = path.lastIndexOf("/");
				
				if (i != -1) {
					uri = newURI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), path.substring(0, i) + "/" + ((IString) repl.getValue()).getValue(), uri.getQuery(), uri.getFragment());
				}
				else {
					uri = newURI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), path + "/" + ((IString) repl.getValue()).getValue(), uri.getQuery(), uri.getFragment());	
				}
			}
			else if (name.equals("parent")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				
				String path = uri.getPath();
				int i = path.lastIndexOf("/");
				String parent = ((IString) repl.getValue()).getValue();
				
				if (!parent.startsWith("/")) {
					parent = "/" + parent;
				}
				if (i != -1) {
					uri = newURI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), parent + path.substring(i), uri.getQuery(), uri.getFragment());
				}
				else {
					uri = newURI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), parent, uri.getQuery(), uri.getFragment());	
				}
			}
			else if (name.equals("ls")) {
				throw new UnsupportedOperationError("can not update the children of a location", ctx.getCurrentAST());
			}
			else if (name.equals("extension")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				String path = uri.getPath();
				String ext = ((IString) repl.getValue()).getValue();
				
				if (path.length() > 1) {
					int index = path.lastIndexOf('.');

					if (index == -1 && !ext.isEmpty()) {
						path = path + (!ext.startsWith(".") ? "." : "") + ext;
					}
					else if (!ext.isEmpty()) {
						path = path.substring(0, index) + (!ext.startsWith(".") ? "." : "") + ext;
					}
					else {
						path = path.substring(0, index);
					}
					
					uri = newURI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), path, uri.getQuery(), uri.getFragment());
				}
			}
			else if (name.equals("top")) {
				// TODO: don't know what to do here yet
				throw new NotYetImplemented("replacement of top field");
			}
			else if (name.equals("fragment")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				uri = newURI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), ((IString) repl.getValue()).getValue());
			}
			else if (name.equals("query")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				uri = newURI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), ((IString) repl.getValue()).getValue(), uri.getFragment());
			}
			else if (name.equals("user")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				
				if (uri.getHost() != null) {
					uri = newURI(uri.getScheme(), ((IString) repl.getValue()).getValue(),  uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
				}
			}
			else if (name.equals("port")) {
				if (!replType.isIntegerType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				
				if (uri.getHost() != null) {
					int port = Integer.parseInt(((IInteger) repl.getValue()).getStringRepresentation());
					uri = newURI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), port, uri.getPath(), uri.getQuery(), uri.getFragment());
				}
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
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result) {
		return result.compareSourceLocation(this);
	}
	
	/////
	
	@Override
	protected <U extends IValue> Result<U> equalToSourceLocation(SourceLocationResult that) {
		return that.equalityBoolean(this);
	}

	@Override
	protected <U extends IValue> Result<U> nonEqualToSourceLocation(SourceLocationResult that) {
		return that.nonEqualityBoolean(this);
	}

	@Override
	protected <U extends IValue> Result<U> compareSourceLocation(SourceLocationResult that) {
		// Note reverse of args
		ISourceLocation left = that.getValue();
		ISourceLocation right = this.getValue();
		if (left.isEqual(right)) {
			return makeIntegerResult(0);
		}
		int compare = left.getURI().toString().compareTo(right.getURI().toString());
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
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> that) {
		return that.addSourceLocation(this);
	}
}
