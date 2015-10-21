/*******************************************************************************
* Copyright (c) 2008 CWI.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    jurgen@vinju.org
*******************************************************************************/
package org.rascalmpl.value.visitors;

import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IDateTime;
import org.rascalmpl.value.IExternalValue;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.visitors.IValueVisitor;

/**
 * This abstract class does nothing except returning null. Extend it
 * to easily implement a visitor that visits selected types of IValues.
 * 
 */
public abstract class NullVisitor<T, E extends Throwable> implements IValueVisitor<T, E> {
	public T visitReal(IReal o)  throws E{
		return null;
	}

	public T visitInteger(IInteger o)  throws E{
		return null;
	}

	public T visitRational(IRational o)  throws E{
		return null;
	}

	public T visitList(IList o)  throws E{
		return null;
	}

	public T visitMap(IMap o)  throws E{
		return null;
	}

	public T visitRelation(ISet o)  throws E{
		return null;
	}

	public T visitSet(ISet o)  throws E{
		return null;
	}

	public T visitSourceLocation(ISourceLocation o)  throws E{
		return null;
	}

	public T visitString(IString o)  throws E{
		return null;
	}

	public T visitNode(INode o)  throws E{
		return null;
	}

	public T visitConstructor(IConstructor o) throws E {
		return null;
	}
	
	public T visitTuple(ITuple o)  throws E{
		return null;
	}
	
	public T visitBoolean(IBool boolValue) throws E {
		return null;
	}
	
	public T visitExternal(IExternalValue externalValue) throws E {
		return null;
	}
	
	public T visitDateTime(IDateTime o) throws E {
		return null;
	}
	
	public T visitListRelation(IList o) throws E {
	  return null;
	}
}
