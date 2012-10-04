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
 *   * Davy Landman - Davy.Landman@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.bool;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredFieldError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperationError;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIUtil;

public class SourceLocationResult extends ElementResult<ISourceLocation> {
	private final Type intTuple;

	public SourceLocationResult(Type type, ISourceLocation loc, IEvaluatorContext ctx) {
		super(type, loc, ctx);
		intTuple = getTypeFactory().tupleType(getTypeFactory().integerType(), "line", getTypeFactory().integerType(), "column");
	}

	
	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] actuals) {
		if (actuals.length >= 2) {
			if (!argTypes[0].isSubtypeOf(getTypeFactory().integerType())) {
				throw new UnexpectedTypeError(getTypeFactory().integerType(), argTypes[0], ctx.getCurrentAST());
			}
			if (!argTypes[1].isSubtypeOf(getTypeFactory().integerType())) {
				throw new UnexpectedTypeError(getTypeFactory().integerType(), argTypes[1], ctx.getCurrentAST());
			}
			
			if (actuals.length == 4) {
				if (!argTypes[2].isSubtypeOf(intTuple)) {
					throw new UnexpectedTypeError(intTuple, argTypes[2], ctx.getCurrentAST());
				}
				if (!argTypes[3].isSubtypeOf(intTuple)) {
					throw new UnexpectedTypeError(intTuple, argTypes[3], ctx.getCurrentAST());
				}
			}
			else if (actuals.length != 2) {
				throw new SyntaxError("location constructor", ctx.getCurrentAST().getLocation());
			}
		}
		else {
			throw new SyntaxError("location constructor", ctx.getCurrentAST().getLocation());
		}
		
		URI uri = getValue().getURI();

		int iLength = Integer.parseInt(actuals[1].toString());
		int iOffset = Integer.parseInt(actuals[0].toString());
		
		if (iLength < 0) {
			throw RuntimeExceptionFactory.illegalArgument(actuals[1], ctx.getCurrentAST(), ctx.getStackTrace());
		}
		
		if (iOffset < 0) {
			throw RuntimeExceptionFactory.illegalArgument(actuals[0], ctx.getCurrentAST(), ctx.getStackTrace());
		}
			
		if (actuals.length == 4) {
			int iBeginLine = Integer.parseInt(((ITuple) actuals[2]).get(0).toString());
			int iBeginColumn = Integer.parseInt(((ITuple) actuals[2]).get(1).toString());
			int iEndLine = Integer.parseInt(((ITuple) actuals[3]).get(0).toString());
			int iEndColumn = Integer.parseInt(((ITuple) actuals[3]).get(1).toString());
			
			if (iBeginLine < 0) {
				throw RuntimeExceptionFactory.illegalArgument(((ITuple) actuals[2]).get(0), ctx.getCurrentAST(), ctx.getStackTrace());
			}
			if (iBeginColumn < 0) {
				throw RuntimeExceptionFactory.illegalArgument(((ITuple) actuals[2]).get(1), ctx.getCurrentAST(), ctx.getStackTrace());
			}
			if (iEndLine < 0) {
				throw RuntimeExceptionFactory.illegalArgument(((ITuple) actuals[3]).get(0), ctx.getCurrentAST(), ctx.getStackTrace());
			}
			if (iEndColumn < 0) {
				throw RuntimeExceptionFactory.illegalArgument(((ITuple) actuals[3]).get(1), ctx.getCurrentAST(), ctx.getStackTrace());
			}

			return makeResult(getTypeFactory().sourceLocationType(), getValueFactory().sourceLocation(uri, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn), ctx);
		}
		else {
			return makeResult(getTypeFactory().sourceLocationType(), getValueFactory().sourceLocation(uri, iOffset, iLength), ctx);
		}
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
			if (!ctx.getResolverRegistry().supportsHost(uri)) {
				throw new UndeclaredFieldError(name, "The scheme " + uri.getScheme() + " does not support the host field, use authority instead.", getTypeFactory().sourceLocationType(), ctx.getCurrentAST());
			}
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
		else if (name.equals("params")) {
			String query = uri.getQuery();
			IMapWriter res = vf.mapWriter(getTypeFactory().stringType(), getTypeFactory().stringType());
			
			if (query != null && query.length() > 0) {
				String[] params = query.split("&");
				for (String param : params) {
					String[] keyValue = param.split("=");
					res.put(vf.string(keyValue[0]), vf.string(keyValue[1]));
				}
			}
			
			IMap map = res.done();
			return makeResult(map.getType(), map, ctx);
		}
		else if (name.equals("user")) {
			if (!ctx.getResolverRegistry().supportsHost(uri)) {
				throw new UndeclaredFieldError(name, "The scheme " + uri.getScheme() + " does not support the user field, use authority instead.", getTypeFactory().sourceLocationType(), ctx.getCurrentAST());
			}
			String user = uri.getUserInfo();
			return makeResult(getTypeFactory().stringType(), vf.string(user != null ? user : ""), ctx);
		}
		else if (name.equals("port")) {
			if (!ctx.getResolverRegistry().supportsHost(uri)) {
				throw new UndeclaredFieldError(name, "The scheme " + uri.getScheme() + " does not support the port field, use authority instead.", getTypeFactory().sourceLocationType(), ctx.getCurrentAST());
			}
			return makeResult(getTypeFactory().integerType(), vf.integer(uri.getPort()), ctx);
		}
		else if (name.equals("length")) {
			if (getValue().hasOffsetLength()) {
				return makeResult(getTypeFactory().integerType(), vf
						.integer(getValue().getLength()), ctx);
			}
			else {
				throw RuntimeExceptionFactory.unavailableInformation(ctx.getCurrentAST(), ctx.getStackTrace());
			}
		} 
		else if (name.equals("offset")) {
			if (getValue().hasOffsetLength()) {
				return makeResult(getTypeFactory().integerType(), vf
						.integer(getValue().getOffset()), ctx);
			}
			else {
				throw RuntimeExceptionFactory.unavailableInformation(ctx.getCurrentAST(), ctx.getStackTrace());
			}
		} 
		else if (name.equals("begin")) {
			if (getValue().hasLineColumn()) {
				return makeResult(intTuple, vf.tuple(vf.integer(getValue().getBeginLine()), vf.integer(getValue().getBeginColumn())), ctx);
			}
			else {
				throw RuntimeExceptionFactory.unavailableInformation(ctx.getCurrentAST(), ctx.getStackTrace());
			}
		}
		else if (name.equals("end")) {
			if (getValue().hasLineColumn()) {
				return makeResult(intTuple, vf.tuple(vf.integer(getValue().getEndLine()), vf.integer(getValue().getEndColumn())), ctx);
			}
			else {
				throw RuntimeExceptionFactory.unavailableInformation(ctx.getCurrentAST(), ctx.getStackTrace());
			}
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

	@Override
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(String name, Result<V> repl, TypeStore store) {
		ISourceLocation loc = getValue();
		int iLength = loc.hasOffsetLength() ? loc.getLength() : -1;
		int iOffset = loc.hasOffsetLength() ? loc.getOffset() : -1;
		int iBeginLine = loc.hasLineColumn() ? loc.getBeginLine() : -1;
		int iBeginColumn = loc.hasLineColumn() ? loc.getBeginColumn() : -1;
		int iEndLine = loc.hasLineColumn() ? loc.getEndLine() : -1;
		int iEndColumn = loc.hasLineColumn() ? loc.getEndColumn() : -1;
		URI uri = loc.getURI();

		Type replType = repl.getType();
		IValue replValue = repl.getValue();

		try {
			if (name.equals("uri")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				uri = URIUtil.createFromEncoded(((IString)repl.getValue()).getValue());
			} 
			else if (name.equals("scheme")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				uri = URIUtil.changeScheme(uri, ((IString) repl.getValue()).getValue());
			}
			else if (name.equals("authority")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				uri = URIUtil.changeAuthority(uri, ((IString) repl.getValue()).getValue());
			}
			else if (name.equals("host")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				if (!ctx.getResolverRegistry().supportsHost(uri)) {
					throw new UndeclaredFieldError(name, "The scheme " + uri.getScheme() + " does not support the host field, use authority instead.", getTypeFactory().sourceLocationType(), ctx.getCurrentAST());
				}
				uri = URIUtil.changeHost(uri, ((IString) repl.getValue()).getValue());
			}
			else if (name.equals("path")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				String path = ((IString) repl.getValue()).getValue();
				if(!path.startsWith("/"))
					path = "/" + path;
				uri = URIUtil.changePath(uri, path);
			}
			else if (name.equals("file")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				
				String path = uri.getPath();
				int i = path.lastIndexOf("/");
				
				if (i != -1) {
					uri = URIUtil.changePath(uri, path.substring(0, i) + "/" + ((IString) repl.getValue()).getValue());
				}
				else {
					uri = URIUtil.changePath(uri, path + "/" + ((IString) repl.getValue()).getValue());	
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
					uri = URIUtil.changePath(uri, parent + path.substring(i));
				}
				else {
					uri = URIUtil.changePath(uri, parent);	
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
					
					uri = URIUtil.changePath(uri, path);
				}
			}
			else if (name.equals("top")) {
				if (replType.isStringType()) {
					uri = URIUtil.assumeCorrect(((IString) repl.getValue()).getValue());
				}
				else if (replType.isSourceLocationType()) {
					uri = ((ISourceLocation) repl.getValue()).getURI();
				}
				else {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
			}
			else if (name.equals("fragment")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				uri = URIUtil.changeFragment(uri, ((IString) repl.getValue()).getValue());
			}
			else if (name.equals("query")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				uri = URIUtil.changeFragment(uri, ((IString) repl.getValue()).getValue());
			}
			else if (name.equals("user")) {
				if (!replType.isStringType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				if (!ctx.getResolverRegistry().supportsHost(uri)) {
					throw new UndeclaredFieldError(name, "The scheme " + uri.getScheme() + " does not support the user field, use authority instead.", getTypeFactory().sourceLocationType(), ctx.getCurrentAST());
				}
				if (uri.getHost() != null) {
					uri = URIUtil.changeUserInformation(uri, ((IString) repl.getValue()).getValue());
				}
			}
			else if (name.equals("port")) {
				if (!replType.isIntegerType()) {
					throw new UnexpectedTypeError(getTypeFactory().stringType(), replType, ctx.getCurrentAST());
				}
				
				if (!ctx.getResolverRegistry().supportsHost(uri)) {
					throw new UndeclaredFieldError(name, "The scheme " + uri.getScheme() + " does not support the port field, use authority instead.", getTypeFactory().sourceLocationType(), ctx.getCurrentAST());
				}
				if (uri.getHost() != null) {
					int port = Integer.parseInt(((IInteger) repl.getValue()).getStringRepresentation());
					uri = URIUtil.changePort(uri, port);
				}
			}
			else if (name.equals("length")){
				if (!replType.isIntegerType()) {
					throw new UnexpectedTypeError(getTypeFactory().integerType(), replType, ctx.getCurrentAST());
				}
				iLength = ((IInteger) replValue).intValue();
				
				if (iLength < 0) {
					throw RuntimeExceptionFactory.illegalArgument(replValue, ctx.getCurrentAST(), ctx.getStackTrace());
				}
			} 
			else if (name.equals("offset")){
				if (!replType.isIntegerType()) {
					throw new UnexpectedTypeError(getTypeFactory().integerType(), replType, ctx.getCurrentAST());
				}
				iOffset = ((IInteger) replValue).intValue();
				
				if (iOffset < 0) {
					RuntimeExceptionFactory.illegalArgument(replValue, ctx.getCurrentAST(), ctx.getStackTrace());
				}
			} 
			else if (name.equals("begin")) {
				if (!replType.isSubtypeOf(intTuple)) {
					throw new UnexpectedTypeError(intTuple, replType, ctx.getCurrentAST());
				}
				iBeginLine = ((IInteger) ((ITuple) replValue).get(0)).intValue();
				iBeginColumn = ((IInteger) ((ITuple) replValue).get(1)).intValue();
				
				if (iBeginColumn < 0 || iBeginLine < 0) {
					throw RuntimeExceptionFactory.illegalArgument(replValue, ctx.getCurrentAST(), ctx.getStackTrace());
				}
			}
			else if (name.equals("end")) {
				if (!replType.isSubtypeOf(intTuple)) {
					throw new UnexpectedTypeError(intTuple, replType, ctx.getCurrentAST());
				}
				iEndLine = ((IInteger) ((ITuple) replValue).get(0)).intValue();
				iEndColumn = ((IInteger) ((ITuple) replValue).get(1)).intValue();
				
				if (iEndLine < 0 || iEndColumn < 0) {
					throw RuntimeExceptionFactory.illegalArgument(replValue, ctx.getCurrentAST(), ctx.getStackTrace());
				}
			}
			else {
				// TODO: is this the right exception? How so "undeclared"?
				throw new UndeclaredFieldError(name, getTypeFactory().sourceLocationType(), ctx.getCurrentAST());
			}
			
			if (loc.hasLineColumn()) {
				// was a complete loc, and thus will be now
				return makeResult(getType(), getValueFactory().sourceLocation(uri, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn), ctx);
			}
			
			if (loc.hasOffsetLength()) {
				// was a partial loc
				
				if (iBeginLine != -1 || iBeginColumn != -1) {
					//will be complete now.
					iEndLine = iBeginLine;
					iEndColumn = iBeginColumn;
					return makeResult(getType(), getValueFactory().sourceLocation(uri, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn), ctx);
				}
				else if (iEndLine != -1 || iEndColumn != -1) {
					// will be complete now.
					iBeginLine = iEndLine;
					iBeginColumn = iEndColumn;
					return makeResult(getType(), getValueFactory().sourceLocation(uri, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn), ctx);
				}
				else {
					// remains a partial loc
					return makeResult(getType(), getValueFactory().sourceLocation(uri, iOffset, iLength), ctx);
				}
			}

			// used to have no offset/length or line/column info, if we are here
			
			if (iBeginColumn != -1 || iEndColumn != -1 || iBeginLine != -1 || iBeginColumn != -1) {
				// trying to add line/column info to a uri that has no offset length
				throw RuntimeExceptionFactory.invalidUseOfLocation("Can not add line/column information without offset/length", ctx.getCurrentAST(), ctx.getStackTrace());
			}
			
			// trying to set offset that was not there before, adding length automatically
			if (iOffset != -1 ) {
				if (iLength == -1) {
					iLength = 0;
				}
			}
			
			// trying to set length that was not there before, adding offset automatically
			if (iLength != -1) {
				if (iOffset == -1) {
					iOffset = 0;
				}
			}
			
			if (iOffset != -1 || iLength != -1) {
				// used not to no offset/length, but do now
				return makeResult(getType(), getValueFactory().sourceLocation(uri, iOffset, iLength), ctx);
			}
			
			// no updates to offset/length or line/column, and did not used to have any either:
			return makeResult(getType(), getValueFactory().sourceLocation(uri), ctx);
		} 
		catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.illegalArgument(ctx.getCurrentAST(), null);
		} catch (URISyntaxException e) {
			throw RuntimeExceptionFactory.parseError(ctx.getCurrentAST().getLocation(), ctx.getCurrentAST(), ctx.getStackTrace());
		}
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
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that) {
		return that.lessThanSourceLocation(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that) {
		return that.lessThanOrEqualSourceLocation(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that) {
		return that.greaterThanSourceLocation(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that) {
		return that.greaterThanOrEqualSourceLocation(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanSourceLocation(SourceLocationResult that) {
		// note reversed args: we need that < this
		return bool((that.compareSourceLocationInt(this) < 0), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualSourceLocation(SourceLocationResult that) {
		// note reversed args: we need that <= this
		return bool((that.compareSourceLocationInt(this) <= 0), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanSourceLocation(SourceLocationResult that) {
		// note reversed args: we need that > this
		return bool((that.compareSourceLocationInt(this) > 0), ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualSourceLocation(SourceLocationResult that) {
		// note reversed args: we need that >= this
		return bool((that.compareSourceLocationInt(this) >= 0), ctx);
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
		return makeIntegerResult(that.compareSourceLocationInt(this));
	}

	protected int compareSourceLocationInt(SourceLocationResult that) {
		// Note args have already been reversed.
		
		ISourceLocation left = this.getValue();
		ISourceLocation right = that.getValue();
		if (left.isEqual(right)) {
			return 0;
		}
		
		// they are not the same
		int compare = left.getURI().toString().compareTo(right.getURI().toString());
		if (compare != 0) {
			return compare;
		}
		
		// but the uri's are the same
		// note that line/column information is superfluous and does not matter for ordering
		
		if (left.hasOffsetLength()) {
			if (!right.hasOffsetLength()) {
				return 1;
			}
			int roffset = right.getOffset();
			int rlen = right.getLength();
			int loffset = left.getOffset();
			int llen = left.getLength();
			if(loffset == roffset){
				return (llen < rlen) ? -1 : ((llen == rlen) ? 0 : 1);
			}
			if(roffset < loffset && roffset + rlen >= loffset + llen)
				return -1;
			else
				return 1;
		}
		
		if (!right.hasOffsetLength()) {
			throw new ImplementationError("assertion failed");
		}
		
		return -1;
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> that) {
		return that.addSourceLocation(this);
	}
}
