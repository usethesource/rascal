package org.meta_environment.rascal.library;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class HTMLIO {
	private final IValueFactory factory;

	public HTMLIO(IValueFactory factory) {
		this.factory = factory;
	}
	
	public IValue readHTMLFile(ISourceLocation file) {
		InputStream in;
		try {
			in = file.getURI().toURL().openStream();
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
		private Stack<java.util.List<IValue>> children = new Stack<java.util.List<IValue>>();
		
		public Constructor() {
			children.add(new ArrayList<IValue>(1));
		}
		
		public IValue getValue() {
			return children.peek().get(0);
		}
		
		@Override
		public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
			children.push(new ArrayList<IValue>(1));
		}
		
		@Override
		public void handleEndTag(Tag t, int pos) {
			java.util.List<IValue> kids = children.pop();
			IValue[] a = new IValue[kids.size()];
			kids.toArray(a);
			INode node = factory.node(t.toString(), factory.list(a));
			children.peek().add(node);
		}
		
		@Override
		public void handleSimpleTag(Tag t, MutableAttributeSet a, int pos) {
			INode node = factory.node(t.toString());
			children.peek().add(node);
		}
		
		@Override
		public void handleText(char[] data, int pos) {
			children.peek().add(factory.node("text", factory.string(new java.lang.String(data))));
		}
		
	}
}
