/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.strategy;

import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class Visitable implements IVisitable {
  private final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	private final static IVisitable instance = new Visitable();

	private Visitable() {
		super();
	}

	public static IVisitable getInstance() {
		return instance;
	}

	public IValue getChildAt(IValue iValue, int i) throws IndexOutOfBoundsException {
		if (iValue instanceof INode) {
			return ((INode) iValue).get(i);
		} else if (iValue instanceof ITuple) {
			return ((ITuple) iValue).get(i);
		} else if (iValue instanceof IMap) {
			int index = 0;
			Iterator<IValue> entries = ((IMap) iValue).iterator();
			while(entries.hasNext()) {
				IValue e = entries.next();
				if (index==i) return e;
				index ++;
			}
		} else if (iValue instanceof ISet) {
			int index = 0;
			Iterator<IValue> entries = ((ISet) iValue).iterator();
			while(entries.hasNext()) {
				IValue e = entries.next();
				if (index==i) return e;
				index ++;
			}
		} else if (iValue instanceof IList) {
			int index = 0;
			Iterator<IValue> entries = ((IList) iValue).iterator();
			while(entries.hasNext()) {
				IValue e = entries.next();
				if (index==i) return e;
				index ++;
			}
		} 
		throw new IndexOutOfBoundsException();
	}

	public int getChildrenNumber(IValue iValue) {
		if (iValue instanceof INode) {
			return ((INode) iValue).arity();
		} else if (iValue instanceof ITuple) {
			return ((ITuple) iValue).arity();
		} else if (iValue instanceof IMap) {
			return ((IMap) iValue).size();
		} else if (iValue instanceof IList) {
			return ((IList) iValue).length();
		} else if (iValue instanceof ISet) {
			return ((ISet) iValue).size();
		}	
		return 0;
	}

	@SuppressWarnings("unchecked")
	public <T extends IValue> T setChildren(T v, List<IValue> newchildren)
	throws IndexOutOfBoundsException {
		if (newchildren.size() != getChildrenNumber(v)) {
			throw new IndexOutOfBoundsException();
		}
		if (v instanceof INode) {
			INode res = (INode) v;
			for (int j = 0; j < res.arity(); j++) {
				res = res.set(j,newchildren.get(j));
			}
			return (T) res;
		} else if (v instanceof ITuple) {
			ITuple res = (ITuple) v;
			for (int j = 0; j < res.arity(); j++) {
				res = res.set(j,newchildren.get(j));
			}
			return (T) res;		
		} else if (v instanceof IMap) {
			IMap map = (IMap) v;
			IMapWriter writer = VF.mapWriter(map.getKeyType(), map.getValueType());
			for (int j = 0; j < map.size(); j++) {
				ITuple newtuple = (ITuple) (newchildren.get(j));
				writer.put(newtuple.get(0), newtuple.get(1));
			}
			return (T) writer.done();
		} else if (v instanceof ISet) {
			ISet set = (ISet) v;
			ISetWriter writer = VF.setWriter(set.getElementType());
			for (int j = 0; j < set.size(); j++) {
				writer.insert(newchildren.get(j));
			}
			return (T) writer.done();
		} else if (v instanceof IList) {
			IList list = (IList) v;
			IListWriter writer = VF.listWriter(list.getElementType());
			for (int j = 0; j < list.length(); j++) {
				writer.append(newchildren.get(j));
			}
			return (T) writer.done();
		} 
		throw new IndexOutOfBoundsException();

	}

	@SuppressWarnings("unchecked")
	public <T extends IValue> T setChildAt(T iValue, int i, IValue newchild) {
		if (iValue instanceof INode) {
			INode res = (INode) iValue;
			res = res.set(i,newchild);

			return (T) res;
		} else if (iValue instanceof ITuple) {
			ITuple res = (ITuple) iValue;
			res = res.set(i,newchild);

			return (T) res;		
		} else if (iValue instanceof IMap) {
			IMap map = (IMap) iValue;
			IMapWriter writer = VF.mapWriter(map.getKeyType(), map.getValueType());
			for (int j = 0; j < map.size(); j++) {
				if (j == i) {
					writer.insert(newchild);
				} else {
					writer.insert(getChildAt(map,j));
				}
			}
			return (T) writer.done();
		} else if (iValue instanceof ISet) {
			ISet set = (ISet) iValue;
			ISetWriter writer = VF.setWriter(set.getElementType());
			for (int j = 0; j < set.size(); j++) {
				if (j == i) {

					writer.insert(newchild);
				} else {
					writer.insert(getChildAt(set,j));
				}
			}
			return (T) writer.done();
		} else if (iValue instanceof IList) {
			IList list = (IList) iValue;
			IListWriter writer = VF.listWriter(list.getElementType());
			for (int j = 0; j < list.length(); j++) {
				if (j == i) {
					writer.append(newchild);
				} else {
					writer.append(getChildAt(list,j));
				}
			}
			return (T) writer.done();
		} 
		throw new IndexOutOfBoundsException();
	}

	public boolean init(IValue v) {
		// It's not necessary to implement this here.
		return false;
	}
	
	public void mark(IValue v){
		// It's not necessary to implement this here.
	}
}
