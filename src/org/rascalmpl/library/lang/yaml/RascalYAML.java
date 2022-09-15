/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Tijs van der Storm - storm@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.lang.yaml;

import static org.rascalmpl.library.lang.yaml.YAMLTypeFactory.Node_mapping;
import static org.rascalmpl.library.lang.yaml.YAMLTypeFactory.Node_reference;
import static org.rascalmpl.library.lang.yaml.YAMLTypeFactory.Node_scalar;
import static org.rascalmpl.library.lang.yaml.YAMLTypeFactory.Node_sequence;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.types.TypeReifier;
import org.yaml.snakeyaml.Yaml;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IDateTime;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class RascalYAML {
	private static final String ANCHOR_ANNO = "anchor";
	private final IValueFactory values;
	private final TypeReifier reifier;
    private final TypeStore definitions;
	
	private static final TypeFactory tf = TypeFactory.getInstance();
	
	public RascalYAML(IValueFactory values, TypeStore definitions) {
		super();
		this.values = values;
		this.reifier = new TypeReifier(values);
		this.definitions = definitions;
	}
	
	public IConstructor loadYAML(IString src) {
		Yaml yaml = new Yaml();
		Object obj = yaml.load(src.getValue());
		if (obj == null) {
			throw RuntimeExceptionFactory.illegalArgument(src, null, null);
		}
		IdentityHashMap<Object, Integer> anchors = new IdentityHashMap<Object, Integer>();
		computeAnchors(obj, anchors, 0);
		return loadRec(obj, anchors, new IdentityHashMap<Object, Boolean>());
	}
	
	@SuppressWarnings("unchecked")
	private int computeAnchors(Object obj, IdentityHashMap<Object, Integer> anchors, int a) {
		if (anchors.containsKey(obj)) {
			anchors.put(obj, a);
			return a + 1;
		}
		anchors.put(obj, -1);
		
		if (obj instanceof Object[]) {
			for (Object elt: (Object[])obj) {
				a = computeAnchors(elt, anchors, a);
			}
		}
		else if (obj instanceof List) {
			for (Object elt: (List<Object>)obj) {
				a = computeAnchors(elt, anchors, a);
			}
		}
		else if (obj instanceof Map) {
			Map<Object, Object> m = (Map<Object,Object>)obj;
			for (Map.Entry<Object,Object> e: m.entrySet()) {
				a = computeAnchors(e.getKey(), anchors, a);
				a = computeAnchors(e.getValue(), anchors, a);
			}
		}
		return a;
	}
	
	@SuppressWarnings("unchecked")
	private IConstructor loadRec(Object obj, IdentityHashMap<Object, Integer> anchors, IdentityHashMap<Object, Boolean> visited) {
	    IMap empty = values.mapWriter().done();
	    
		if (obj instanceof Integer) {
			return values.constructor(Node_scalar, values.integer((Integer)obj))
					.asWithKeywordParameters().setParameter("tag", reifier.typeToValue(tf.integerType(), definitions, empty));
		}
		if (obj instanceof Long) {
			return	values.constructor(Node_scalar, values.integer((Long)obj))
					.asWithKeywordParameters().setParameter("tag", reifier.typeToValue(tf.integerType(), definitions, empty));
		}
		if (obj instanceof Double) {
			return values.constructor(Node_scalar, values.real((Double)obj))
					.asWithKeywordParameters().setParameter("tag", reifier.typeToValue(tf.realType(), definitions, empty));
		}
		if (obj instanceof Float) {
			return values.constructor(Node_scalar, values.real((Float)obj))
					.asWithKeywordParameters().setParameter("tag", reifier.typeToValue(tf.realType(), definitions, empty));
		}
		if (obj instanceof String) {
			return values.constructor(Node_scalar, values.string((String)obj))
					.asWithKeywordParameters().setParameter("tag", reifier.typeToValue(tf.stringType(), definitions, empty));
		}
		if (obj instanceof Boolean) {
			return values.constructor(Node_scalar, values.bool((Boolean)obj))
					.asWithKeywordParameters().setParameter("tag", reifier.typeToValue(tf.boolType(), definitions, empty));
		}
		if (obj instanceof Date) {
			return values.constructor(Node_scalar, values.datetime(((Date)obj).getTime()))
					.asWithKeywordParameters().setParameter("tag", reifier.typeToValue(tf.dateTimeType(), definitions, empty));
		}
		if (obj instanceof URI) {
			return values.constructor(Node_scalar, values.sourceLocation((URI)obj))
					.asWithKeywordParameters().setParameter("tag", reifier.typeToValue(tf.sourceLocationType(), definitions, empty));
		}

		// Structural types may be shared.
		if (visited.containsKey(obj)) {
			assert anchors.get(obj) != - 1;
			return values.constructor(Node_reference, values.integer(anchors.get(obj)));
		}
		visited.put(obj, true);
		
		IConstructor result;
		if (obj instanceof Object[]) {
			IListWriter w = values.listWriter();
			for (Object elt: (Object[])obj) {
				w.append(loadRec(elt, anchors, visited));
			}
			result = values.constructor(Node_sequence, w.done());
		}
		else if (obj instanceof List) {
			IListWriter w = values.listWriter();
			for (Object elt: (List<Object>)obj) {
				w.append(loadRec(elt, anchors, visited));
			}
			result = values.constructor(Node_sequence, w.done());
		}
		else if (obj instanceof Map) {
			IMapWriter w = values.mapWriter();
			Map<Object, Object> m = (Map<Object,Object>)obj;
			for (Map.Entry<Object,Object> e: m.entrySet()) {
				w.put(loadRec(e.getKey(), anchors, visited), loadRec(e.getValue(), anchors, visited));
			}
			result = values.constructor(Node_mapping, w.done());
		}
		else {
			if (obj == null) {
				throw RuntimeExceptionFactory.illegalTypeArgument(values.string("missing input"));		
			}
			else {
				throw RuntimeExceptionFactory.illegalArgument(
						values.string(obj.toString() + " (class=" + obj.getClass() + ")"));
			}
		}
		if (anchors.get(obj) != -1) {
			result = result.asWithKeywordParameters().setParameter(ANCHOR_ANNO, values.integer(anchors.get(obj)));
		}
		return result;
	}
	
	public IString dumpYAML(IConstructor yaml) {
		Map<Integer, Object> visited = new HashMap<Integer, Object>();
		Object obj = dumpYAMLrec(yaml, visited);
		Yaml y = new Yaml();
		return values.string(y.dump(obj));
	}

	private Object dumpYAMLrec(IConstructor yaml, Map<Integer, Object> visited) {
		// in valid YAML anchors always occur before any references
		// this should also hold in our YAML data type 
		if (yaml.getConstructorType() == Node_reference) {
			int id = ((IInteger)yaml.get(0)).intValue();
			if (!visited.containsKey(id)) {
				throw RuntimeExceptionFactory.indexOutOfBounds(values.integer(id), null, null);
			}
			return visited.get(id);
		}
		if (yaml.getConstructorType() == Node_scalar) {
			// we're ignoring the tag annotations right now.
			IValue value = yaml.get(0);
			if (value.getType() == tf.integerType()) {
				// TODO: detect Long, BigInt etc.
				return new Integer(((IInteger)value).intValue());
			}
			if (value.getType() == tf.realType()) {
				return new Double(((IReal)value).doubleValue());
			}
			if (value.getType() == tf.stringType()) {
				return new String((((IString)value).getValue()));
			}
			if (value.getType() == tf.boolType()) {
				return new Boolean((((IBool)value).getValue()));
			}
			if (value.getType() == tf.dateTimeType()) {
				return new Date(((IDateTime)value).getInstant());
			}
			if (value.getType() == tf.sourceLocationType()) {
				return ((ISourceLocation)value).getURI();
			}
			throw RuntimeExceptionFactory.illegalArgument(yaml, null, null);
		}
		if (yaml.getConstructorType() == Node_sequence) {
			List<Object> l = new ArrayList<Object>();
			if (yaml.asWithKeywordParameters().hasParameter(ANCHOR_ANNO)) {
				visited.put(((IInteger)yaml.asWithKeywordParameters().getParameter(ANCHOR_ANNO)).intValue(), l);
			}
			for (IValue v: (IList)yaml.get(0)) {
				l.add(dumpYAMLrec((IConstructor)v, visited));
			}
			return l;
		}
		if (yaml.getConstructorType() == Node_mapping) {
			Map<Object, Object> m = new IdentityHashMap<Object, Object>();
			if (yaml.asWithKeywordParameters().hasParameter(ANCHOR_ANNO)) {
				visited.put(((IInteger)yaml.asWithKeywordParameters().getParameter(ANCHOR_ANNO)).intValue(), m);
			}
			Iterator<Entry<IValue, IValue>> iter = ((IMap)yaml.get(0)).entryIterator();
			while (iter.hasNext()) {
				Entry<IValue, IValue> e = iter.next();
				m.put(dumpYAMLrec((IConstructor)e.getKey(), visited), 
						dumpYAMLrec((IConstructor)e.getValue(), visited));
			}
			return m;
		}
		throw new ImplementationError("Invalid YAML data type: " + yaml);
	}
	
}
