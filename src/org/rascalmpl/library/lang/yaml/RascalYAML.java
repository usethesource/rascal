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

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.yaml.snakeyaml.Yaml;

public class RascalYAML {
	private static final String ANCHOR_ANNO = "anchor";
	private final IValueFactory values;
	private final TypeReifier reifier;
	
	private static final TypeFactory tf = TypeFactory.getInstance();
	
	public RascalYAML(IValueFactory values) {
		super();
		this.values = values;
		this.reifier = new TypeReifier(values);
	}
	
	public IConstructor loadYAML(IString src, IEvaluatorContext ctx) {
		Yaml yaml = new Yaml();
		Object obj = yaml.load(src.getValue());
		if (obj == null) {
			throw RuntimeExceptionFactory.illegalArgument(src, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		IdentityHashMap<Object, Integer> anchors = new IdentityHashMap<Object, Integer>();
		computeAnchors(obj, anchors, 0);
		return loadRec(obj, anchors, new IdentityHashMap<Object, Boolean>(), ctx);
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
	private IConstructor loadRec(Object obj, IdentityHashMap<Object, Integer> anchors, IdentityHashMap<Object, Boolean> visited, IEvaluatorContext ctx) {
		if (obj instanceof Integer) {
			return values.constructor(Node_scalar, values.integer((Integer)obj))
					.asWithKeywordParameters().setParameter("tag", reifier.typeToValue(tf.integerType(), ctx).getValue());
		}
		if (obj instanceof Long) {
			return	values.constructor(Node_scalar, values.integer((Long)obj))
					.asWithKeywordParameters().setParameter("tag", reifier.typeToValue(tf.integerType(), ctx).getValue());
		}
		if (obj instanceof Double) {
			return values.constructor(Node_scalar, values.real((Double)obj))
					.asWithKeywordParameters().setParameter("tag", reifier.typeToValue(tf.realType(), ctx).getValue());
		}
		if (obj instanceof Float) {
			return values.constructor(Node_scalar, values.real((Float)obj))
					.asWithKeywordParameters().setParameter("tag", reifier.typeToValue(tf.realType(), ctx).getValue());
		}
		if (obj instanceof String) {
			return values.constructor(Node_scalar, values.string((String)obj))
					.asWithKeywordParameters().setParameter("tag", reifier.typeToValue(tf.stringType(), ctx).getValue());
		}
		if (obj instanceof Boolean) {
			return values.constructor(Node_scalar, values.bool((Boolean)obj))
					.asWithKeywordParameters().setParameter("tag", reifier.typeToValue(tf.boolType(), ctx).getValue());
		}
		if (obj instanceof Date) {
			return values.constructor(Node_scalar, values.datetime(((Date)obj).getTime()))
					.asWithKeywordParameters().setParameter("tag", reifier.typeToValue(tf.dateTimeType(), ctx).getValue());
		}
		if (obj instanceof URI) {
			return values.constructor(Node_scalar, values.sourceLocation((URI)obj))
					.asWithKeywordParameters().setParameter("tag", reifier.typeToValue(tf.sourceLocationType(), ctx).getValue());
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
				w.append(loadRec(elt, anchors, visited, ctx));
			}
			result = values.constructor(Node_sequence, w.done());
		}
		else if (obj instanceof List) {
			IListWriter w = values.listWriter();
			for (Object elt: (List<Object>)obj) {
				w.append(loadRec(elt, anchors, visited, ctx));
			}
			result = values.constructor(Node_sequence, w.done());
		}
		else if (obj instanceof Map) {
			IMapWriter w = values.mapWriter();
			Map<Object, Object> m = (Map<Object,Object>)obj;
			for (Map.Entry<Object,Object> e: m.entrySet()) {
				w.put(loadRec(e.getKey(), anchors, visited, ctx), loadRec(e.getValue(), anchors, visited, ctx));
			}
			result = values.constructor(Node_mapping, w.done());
		}
		else {
			throw RuntimeExceptionFactory.illegalArgument(
					values.string(obj.toString() + " (class=" + obj.getClass() + ")"), 
					ctx.getCurrentAST(), ctx.getStackTrace());
		}
		if (anchors.get(obj) != -1) {
			result = result.asWithKeywordParameters().setParameter(ANCHOR_ANNO, values.integer(anchors.get(obj)));
		}
		return result;
	}
	
	public IString dumpYAML(IConstructor yaml, IEvaluatorContext ctx) {
		Map<Integer, Object> visited = new HashMap<Integer, Object>();
		Object obj = dumpYAMLrec(yaml, visited, ctx);
		Yaml y = new Yaml();
		return values.string(y.dump(obj));
	}

	private Object dumpYAMLrec(IConstructor yaml, Map<Integer, Object> visited, IEvaluatorContext ctx) {
		// in valid YAML anchors always occur before any references
		// this should also hold in our YAML data type 
		if (yaml.getConstructorType() == Node_reference) {
			int id = ((IInteger)yaml.get(0)).intValue();
			if (!visited.containsKey(id)) {
				throw RuntimeExceptionFactory.indexOutOfBounds(values.integer(id), ctx.getCurrentAST(), ctx.getStackTrace());
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
			throw RuntimeExceptionFactory.illegalArgument(yaml, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		if (yaml.getConstructorType() == Node_sequence) {
			List<Object> l = new ArrayList<Object>();
			if (yaml.asAnnotatable().hasAnnotation(ANCHOR_ANNO)) {
				visited.put(((IInteger)yaml.asAnnotatable().getAnnotation(ANCHOR_ANNO)).intValue(), l);
			}
			for (IValue v: (IList)yaml.get(0)) {
				l.add(dumpYAMLrec((IConstructor)v, visited, ctx));
			}
			return l;
		}
		if (yaml.getConstructorType() == Node_mapping) {
			Map<Object, Object> m = new IdentityHashMap<Object, Object>();
			if (yaml.asAnnotatable().hasAnnotation(ANCHOR_ANNO)) {
				visited.put(((IInteger)yaml.asAnnotatable().getAnnotation(ANCHOR_ANNO)).intValue(), m);
			}
			Iterator<Entry<IValue, IValue>> iter = ((IMap)yaml.get(0)).entryIterator();
			while (iter.hasNext()) {
				Entry<IValue, IValue> e = iter.next();
				m.put(dumpYAMLrec((IConstructor)e.getKey(), visited, ctx), 
						dumpYAMLrec((IConstructor)e.getValue(), visited, ctx));
			}
			return m;
		}
		throw new ImplementationError("Invalid YAML data type: " + yaml);
	}
	
}
