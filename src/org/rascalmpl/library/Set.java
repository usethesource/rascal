/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;


public class Set {
	private static final TypeFactory types = TypeFactory.getInstance();
	private final IValueFactory values;
	private final Random random;
	
	public Set(IValueFactory values){
		super();
		
		this.values = values;
		random = new Random();
	}

	public IValue getOneFrom(ISet st)
	// @doc{getOneFrom -- pick a random element from a set}
	{
		int sz = st.size();

		if (sz == 0) {
			throw RuntimeExceptionFactory.emptySet(null, null);
		}
		int k = random.nextInt(sz);
		int i = 0;

		for (IValue v : st) {
			if (i == k) {
				return v;
			}
			i++;
		}
		
		throw RuntimeExceptionFactory.emptySet(null, null);
	}

	public IValue isEmpty(ISet st)
	//@doc{isEmpty -- is set empty?}
	{
		return values.bool(st.size() == 0);
	}
	
	public IValue size(ISet st)
	// @doc{size -- number of elements in a set}
	{
		return values.integer(st.size());
	}

	public IValue takeOneFrom(ISet st)
	// @doc{takeOneFrom -- remove an arbitrary element from a set,
	//      returns the element and the modified set}
	{
		int n = st.size();

		if (n > 0) {
			int i = 0;
			int k = random.nextInt(n);
			IValue pick = null;
			ISetWriter w = st.getType().writer(values);

			for (IValue v : st) {
				if (i == k) {
					pick = v;
				} else {
					w.insert(v);
				}
				i++;
			}
			return values.tuple(pick, w.done());
		}
		throw RuntimeExceptionFactory.emptySet(null, null);
	}

	public IValue toList(ISet st)
	// @doc{toList -- convert a set to a list}
	{
		Type resultType = types.listType(st.getElementType());
		IListWriter w = resultType.writer(values);

		for (IValue v : st) {
			w.insert(v);
		}

		return w.done();
	}

	public IValue toMap(IRelation st)
	// @doc{toMap -- convert a set of tuples to a map; value in old map is associated with a set of keys in old map}
	{
		Type tuple = st.getElementType();
		Type keyType = tuple.getFieldType(0);
		Type valueType = tuple.getFieldType(1);
		Type valueSetType = types.setType(valueType);

		HashMap<IValue,ISetWriter> hm = new HashMap<IValue,ISetWriter>();

		for (IValue v : st) {
			ITuple t = (ITuple) v;
			IValue key = t.get(0);
			IValue val = t.get(1);
			ISetWriter wValSet = hm.get(key);
			if(wValSet == null){
				wValSet = valueSetType.writer(values);
				hm.put(key, wValSet);
			}
			wValSet.insert(val);
		}
		
		Type resultType = types.mapType(keyType, valueSetType);
		IMapWriter w = resultType.writer(values);
		for(IValue v : hm.keySet()){
			w.put(v, hm.get(v).done());
		}
		return w.done();
	}
	
	public IValue toMapUnique(IRelation st)
	// @doc{toMapUnique -- convert a set of tuples to a map; keys are unique}
	{
		Type tuple = st.getElementType();
		Type resultType = types.mapType(tuple.getFieldType(0), tuple
				.getFieldType(1));

		IMapWriter w = resultType.writer(values);
		HashSet<IValue> seenKeys = new HashSet<IValue>();

		for (IValue v : st) {
			ITuple t = (ITuple) v;
			IValue key = t.get(0);
			IValue val = t.get(1);
			if(seenKeys.contains(key)) 
				throw RuntimeExceptionFactory.MultipleKey(key, null, null);
			seenKeys.add(key);
			w.put(t.get(0), t.get(1));
		}
		return w.done();
	}

	public IValue toString(ISet st)
	// @doc{toString -- convert a set to a string}
	{
		return values.string(st.toString());
	}
}
