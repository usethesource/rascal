/*******************************************************************************
 * Copyright (c) CWI 2009
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jurgen Vinju (jurgenv@cwi.nl) - initial API and implementation
 *    Bert Lisser    - Bert.Lisser@cwi.nl
 *******************************************************************************/
package org.rascalmpl.library.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IRational;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.IValueTextWriter;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

/**
 * This class implements the JSon readable syntax for {@link IValue}'s. See also
 * {@link JSonReader}
 * 
 * @author bertl
 * 
 **/

public class JSonWriter implements IValueTextWriter {

	static boolean typed = true;

	static boolean debug = false;

	static final String name = "#name", args = "#args", annos = "#annos";

	public void write(IValue value, java.io.Writer stream) throws IOException {
		try {
			if (debug) System.err.println("JSonWriter:write:" + value);
			value.accept(new Writer(stream));
			stream.close();
		} catch (VisitorException e) {
			throw (IOException) e.getCause();
		}
	}

	public void write(IValue value, java.io.Writer stream, TypeStore typeStore)
			throws IOException {
		write(value, stream);
	}

	private static class Writer implements IValueVisitor<IValue> {
		private java.io.Writer stream;

		private int inNode = 0;

		public Writer(java.io.Writer stream) {
			this.stream = stream;
		}

		private void append(String string) throws VisitorException {
			try {
				stream.write(string);
			} catch (IOException e) {
				throw new VisitorException(e);
			}
		}

		private void append(char c) throws VisitorException {
			try {
				stream.write(c);
			} catch (IOException e) {
				throw new VisitorException(e);
			}
		}

		public IValue visitBoolean(IBool boolValue) throws VisitorException {
			append(boolValue.getStringRepresentation());
			return boolValue;
		}

		public IValue visitConstructor(IConstructor o) throws VisitorException {
			return visitNode(o);
		}

		public IValue visitReal(IReal o) throws VisitorException {
			append(o.getStringRepresentation());
			return o;
		}

		public IValue visitInteger(IInteger o) throws VisitorException {
			append(o.getStringRepresentation());
			return o;
		}

		// TODO: There probably isn't a good ATerm repr of rationals,
		// what should we do here?
		public IValue visitRational(IRational o) throws VisitorException {
			if (typed || inNode > 0)
				append("{\"" + name + "\":\"#rat\",\"" + args + "\":[");
			else
				append('[');
			o.numerator();
			o.numerator().accept(this);
			append(',');
			o.denominator().accept(this);
			if (typed || inNode > 0)
				append("]}");
			else
				append(']');
			return o;
		}

		private void visitSequence(Iterator<IValue> listIterator)
				throws VisitorException {
			append('[');
			if (listIterator.hasNext()) {
				IValue v = listIterator.next();
				if (debug)
					System.err.println("VisitList:" + v + " " + v.getClass()
							+ " " + v.getType());
				v.accept(this);
				while (listIterator.hasNext()) {
					append(',');
					listIterator.next().accept(this);
				}
			}
			append(']');
		}

		/* [expr,...] */
		public IValue visitList(IList o) throws VisitorException {
			visitSequence(o.iterator());
			return o;
		}

		/* [expr,...] */
		@SuppressWarnings("unused")
		public IValue visitSet(ISet o) throws VisitorException {
			if (debug)
				System.err.println("VisitSet:" + o);
			if (typed || inNode > 0)
				append("{\"" + name + "\":\"#set\",\"" + args + "\":");
			visitSequence(o.iterator());
			if (typed || inNode > 0)
				append('}');
			return o;
		}

		/* [expr,...] */
		@SuppressWarnings("unused")
		public IValue visitTuple(ITuple o) throws VisitorException {
			if (debug)
				System.err.println("VisitTuple:" + o);
			if (typed || inNode > 0)
				append("{\"" + name + "\":\"#tuple\",\"" + args + "\":");
			visitSequence(o.iterator());
			if (typed || inNode > 0)
				append('}');
			return o;
		}

		/* [expr,...] */
		public IValue visitRelation(IRelation o) throws VisitorException {
			if (typed || inNode > 0)
				append("{\"" + name + "\":\"#set\",\"" + args + "\":");
			visitSequence(o.iterator());
			if (typed || inNode > 0)
				append('}');
			return o;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.imp.pdb.facts.visitors.IValueVisitor#visitMap(org.eclipse
		 * .imp.pdb.facts.IMap) -> [[key, value]] or -> {str key:value, str
		 * key:value}
		 */
		public IValue visitMap(IMap o) throws VisitorException {
			Iterator<IValue> mapIterator = o.iterator();
			if (o.getKeyType().isStringType()) {
				append('{');
				if (mapIterator.hasNext()) {
					IValue key = mapIterator.next();
					key.accept(this);
					append(':');
					o.get(key).accept(this);
					while (mapIterator.hasNext()) {
						append(',');
						key = mapIterator.next();
						key.accept(this);
						append(':');
						o.get(key).accept(this);
					}
				}
				append('}');
			} else {
				if (typed || inNode > 0)
					append("{\"" + name + "\":\"#map\",\"" + args + "\":[");
				else
					append('[');
				if (mapIterator.hasNext()) {
					if (typed || inNode > 0)
						append("{\"" + name + "\":\"#tuple\",\"" + args
								+ "\":[");
					else
						append('[');
					IValue key = mapIterator.next();
					key.accept(this);
					append(',');
					o.get(key).accept(this);
					if (typed || inNode > 0)
						append("]}");
					else
						append(']');
					while (mapIterator.hasNext()) {
						append(',');
						if (typed || inNode > 0)
							append("{\"" + name + "\":\"#tuple\",\"" + args
									+ "\":[");
						else
							append('[');
						key = mapIterator.next();
						key.accept(this);
						append(',');
						o.get(key).accept(this);
						if (typed || inNode > 0)
							append("]}");
						else
							append(']');
					}
				}
				if (typed || inNode > 0)
					append("]}");
				else
					append(']');
			}
			return o;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.imp.pdb.facts.visitors.IValueVisitor#visitNode(org.eclipse
		 * .imp.pdb.facts.INode) {f:{args:[arg0,...],anno:[{name0:val0,...}]}}
		 */
		public IValue visitNode(INode o) throws VisitorException {
			Iterator<IValue> nodeIterator = o.iterator();
			Map<String, IValue> annotations = o.getAnnotations();
			Iterator<String> annoIterator = annotations.keySet().iterator();
			inNode++;
			append("{\"" + name + "\":");
			append('\"');
			append(o.getName().replaceAll("\"", "\\\\\"")
					.replaceAll("\n", "\\\\n"));
			append('\"');
			append(",\"" + args + "\":");
			append('[');
			if (nodeIterator.hasNext()) {
				IValue v = nodeIterator.next();
				v.accept(this);
				while (nodeIterator.hasNext()) {
					append(',');
					v = nodeIterator.next();
					v.accept(this);
				}
			}
			append(']');
		    if (annoIterator.hasNext()) {
			     append(",\""+annos+"\":");
//				// append('[');
				append('{');
				String key = annoIterator.next();
				append("\""+key+"\"");
				append(':');
				annotations.get(key).accept(this);
				while (annoIterator.hasNext()) {
					append(',');
					key = annoIterator.next();
					append("\""+key+"\"");
					append(':');
					annotations.get(key).accept(this);
				}
				append('}');
//				// append(']');
			}
			append('}');
			inNode--;
			return o;
		}

		public IValue visitSourceLocation(ISourceLocation o)
				throws VisitorException {
			if (typed || inNode > 0)
				append("{\"" + name + "\":\"#loc\",\"" + args + "\":[");
			append("\"" + o.getURI().toString() + "\"");
			if (typed || inNode > 0)
				append("]}");
			return o;
		}

		public IValue visitString(IString o) throws VisitorException {
			// TODO optimize this implementation and finish all escapes
			append('\"');
			append(o.getValue().replaceAll("\"", "\\\\\"")
					.replaceAll("\n", "\\\\n"));
			append('\"');
			return o;
		}

		public IValue visitExternal(IExternalValue externalValue) {
			System.err.println("visitExternal:");
			System.err.println("visitExternal:" + externalValue);
			return externalValue;
		}

		public IValue visitDateTime(IDateTime o) throws VisitorException {
			if (typed || inNode > 0)
				append("{\"" + name + "\":\"#datetime\",\"" + args + "\":[");
			append('\"');
			SimpleDateFormat sd = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSSZ");
			append(sd.format(new Date(o.getInstant())));
			append('\"');
			if (typed || inNode > 0)
				append("]}");
			return o;
		}
	}

}
