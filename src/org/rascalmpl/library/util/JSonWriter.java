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
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.io.IValueTextWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/**
 * This class implements the JSon readable syntax for {@link IValue}'s. See also
 * {@link JSonReader}
 * 
 * Contributors:
 *    Bert Lisser  (Bert.Lisser@cwi.nl)
 *    Paul Klint	(Paul.Klint@cwi.nl) - added missing cases and made standards compliant
 * 
 **/

public class JSonWriter implements IValueTextWriter {

	static boolean typed = false;
	
	static boolean nodeTyped = false;

	static boolean debug = false;

	static final String name = "#name", args = "#args", annos = "#annos", keywords = "#keywords";
	
	static final IValueFactory vf = ValueFactoryFactory.getValueFactory();

	public void write(IValue value, java.io.Writer stream) throws IOException {
	  value.accept(new Writer(stream));
	  stream.close();
	}

	public void write(IValue value, java.io.Writer stream, TypeStore typeStore)
			throws IOException {
		write(value, stream);
	}

	private static class Writer implements IValueVisitor<IValue, IOException> {
		private java.io.Writer stream;

		private int inNode = 0;

		public Writer(java.io.Writer stream) {
			this.stream = stream;
		}

		private void append(String string) throws IOException {
		  stream.write(string);
		}

		private void append(char c) throws IOException {
		  stream.write(c);
		}

		public IValue visitBoolean(IBool boolValue) throws IOException {
			append(boolValue.getStringRepresentation());
			return boolValue;
		}

		public IValue visitConstructor(IConstructor o) throws IOException {
			return visitNode(o);
		}

		public IValue visitReal(IReal o) throws IOException {
			// append(o.getStringRepresentation());
			String s = o.toString();
			if (s.endsWith(".")) s = s.substring(0, s.length()-1);
			append(s); // JSon doesn't accept "1." it must be "1.0" or "1" 
			return o;
		}

		public IValue visitInteger(IInteger o) throws IOException {
			append(o.getStringRepresentation());
			return o;
		}

		// TODO: There probably isn't a good ATerm repr of rationals,
		// what should we do here?
		public IValue visitRational(IRational o) throws IOException {
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
				throws IOException {
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
		public IValue visitList(IList o) throws IOException {
			visitSequence(o.iterator());
			return o;
		}

		/* [expr,...] */
		public IValue visitSet(ISet o) throws IOException {
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
		public IValue visitTuple(ITuple o) throws IOException {
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
		public IValue visitRelation(ISet o) throws IOException {
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
		public IValue visitMap(IMap o) throws IOException {
			Iterator<IValue> mapIterator = o.iterator();
			if (o.getKeyType().isString()) {
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

		private boolean isParseTreeNode(INode node){
			Type tp = node.getType();
			return tp.isSubtypeOf(Factory.Tree) || tp.isSubtypeOf(Factory.Production) || tp.isSubtypeOf(Factory.Attributes)  ||
				   tp.isSubtypeOf(Factory.Attr) || tp.isSubtypeOf(Factory.Associativity) || tp.isSubtypeOf(Factory.Symbol) ||
				   tp.isSubtypeOf(Factory.Symbol) || tp.isSubtypeOf(Factory.CharRange)  || tp.isSubtypeOf(Factory.Condition) ;
		}
	
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.imp.pdb.facts.visitors.IValueVisitor#visitNode(org.eclipse
		 * .imp.pdb.facts.INode) {f:{args:[arg0,...],anno:[{name0:val0,...}]}}
		 */
		public IValue visitNode(INode o) throws IOException {
			Iterator<IValue> nodeIterator = o.iterator();
			
			if (nodeTyped) inNode++;
			append("{\"" + name + "\":");
			visitString(vf.string(o.getName()));
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
			
			if(o.isAnnotatable()){
				Map<String, IValue> annotations = o.asAnnotatable().getAnnotations();
				Iterator<String> annoIterator = annotations.keySet().iterator();
				if (annoIterator.hasNext()) {
					append(",\""+annos+"\":");
					// append('[');
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
					// append(']');
				}
			}
		    
		    // TODO: SOS horrible hack to prevent crash on writing parse trees
		    
		    if(!isParseTreeNode(o)){
		    	Map<String, IValue> keywordParameters = o.asWithKeywordParameters().getParameters();
		    	Iterator<String> keywordIterator = keywordParameters.keySet().iterator();
		    	if (keywordIterator.hasNext()) {
		    		append(",");
		    		//				// append('[');
		    		String key = keywordIterator.next();
		    		append("\""+key+"\"");
		    		append(':');
		    		keywordParameters.get(key).accept(this);
		    		while (keywordIterator.hasNext()) {
		    			append(',');
		    			key = keywordIterator.next();
		    			append("\""+key+"\"");
		    			append(':');
		    			keywordParameters.get(key).accept(this);
		    		}
		    		//append('}');
		    		//				// append(']');
		    	}
		    }
			append('}');
			if (nodeTyped) inNode--;
			return o;
		}

		public IValue visitSourceLocation(ISourceLocation o)
				throws IOException {
			if (typed || inNode > 0)
				append("{\"" + name + "\":\"#loc\",\"" + args + "\":[");
			append("\"" + o.getURI().toString() + "\"");
			if (typed || inNode > 0)
				append("]}");
			return o;
		}
		
		/*	(non-Javadoc)
		 * @see org.eclipse.imp.pdb.facts.visitors.IValueVisitor#visitString(org.eclipse.imp.pdb.facts.IString)
		 */
		public IValue visitString(IString o) throws IOException {
			// See http://www.json.org for string escapes
			append('\"');
			for(int i = 0; i < o.length(); i++){
				String s = o.substring(i, i+1).getValue();
				switch(s){
				
				case "\"":
					append("\\\"");
					break;
					
				case "\\":
					append("\\\\");
					break;
				case "/":
					append("\\/");
					break;
				case "\b":
					append("\\b");
					break;
					
				case "\f":
					append("\\f");
					break;
					
				case "\n":
					append("\\n");
					break;
					
				case "\r":
					append("\\r");
					break;
					
				case "\t":
					append("\\t");
					break;
				
				default:
					append(s);
				}
			}

//			append(o.getValue().replaceAll("\"", "\\\\\"")
//					.replaceAll("\n", "\\\\n"));
			append('\"');
			return o;
		}

		public IValue visitExternal(IExternalValue externalValue) {
			System.err.println("visitExternal:");
			System.err.println("visitExternal:" + externalValue);
			return externalValue;
		}

		public IValue visitDateTime(IDateTime o) throws IOException {
			if (typed || inNode > 0)
				append("{\"" + name + "\":\"#datetime\",\"" + args + "\":[");
			append('\"');
			
			SimpleDateFormat sd = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//			SimpleDateFormat sd = new SimpleDateFormat(
//					"yyyy-MM-dd HH:mm:ss.SSS");
			append(sd.format(new Date(o.getInstant())));
			//System.err.println("visitdateTime: " +new Date(o.getInstant()));
			append('\"');
			if (typed || inNode > 0)
				append("]}");
			return o;
		}

		@Override
		public IValue visitListRelation(IList o)
				throws IOException {
			// TODO Auto-generated method stub
			visitSequence(o.iterator());
			return o;
		}
	}

}
