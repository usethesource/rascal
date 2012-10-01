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
package org.rascalmpl.library.lang.html;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class IO {
	private final IValueFactory factory;

	public IO(IValueFactory factory) {
		this.factory = factory;
	}
	
	public IValue readHTMLFile(ISourceLocation file, IEvaluatorContext ctx) {
		InputStream in;
		try {
			in = ctx.getResolverRegistry().getInputStream(file.getURI());
			Constructor cons = new Constructor();
			new ParserDelegator().parse(new InputStreamReader(in), cons, true);
			return cons.getValue();
		} catch (MalformedURLException e) {
			throw RuntimeExceptionFactory.malformedURI(file.getURI().toASCIIString(), null, null);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(factory.string(e.getMessage()), null, null);
		}
	}
	
	private class Constructor extends ParserCallback {
		private Stack<java.util.List<IValue>> stack = new Stack<java.util.List<IValue>>();
		private Stack<java.util.Map<java.lang.String,IValue>> attributes = new Stack<java.util.Map<java.lang.String, IValue>>();
		
		public Constructor() {
			stack.push(new ArrayList<IValue>(1));
		}
		
		public IValue getValue() {
			return stack.peek().get(0);
		}
		
		@Override
		public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
			stack.push(new ArrayList<IValue>(1));
//			attributes.push(new HashMap<java.lang.String, IValue>());
			storeAttributes(a);
		}
		
		private void storeAttributes(MutableAttributeSet set) {
			attributes.push(new HashMap<java.lang.String,IValue>());
			for (Enumeration<?> names = set.getAttributeNames(); names.hasMoreElements(); ) {				
				Object label = names.nextElement();
				Object value = set.getAttribute(label);
				attributes.peek().put(label.toString(), factory.string(value.toString()));
			}
		}

		@Override
		public void handleEndTag(Tag t, int pos) {
			java.util.List<IValue> kids = stack.pop();
			IValue[] a = new IValue[kids.size()];
			kids.toArray(a);
			INode node = factory.node(t.toString(), factory.list(a));
			node = node.setAnnotations(attributes.pop());
			stack.peek().add(node);
		}
		
		@Override
		public void handleSimpleTag(Tag t, MutableAttributeSet a, int pos) {
			INode node = factory.node(t.toString());
			stack.peek().add(node);
		}
		
		@Override
		public void handleText(char[] data, int pos) {
			stack.peek().add(factory.node("text", factory.string(new java.lang.String(data))));
		}
		
	}
}
