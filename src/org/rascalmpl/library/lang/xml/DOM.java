/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.library.lang.xml;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class DOM {
	private final IValueFactory vf;
	
	public DOM(IValueFactory vf) {
		this.vf = vf;
	}
	
	private static class Skip extends Exception {
		private static final long serialVersionUID = -6330585199877497106L;
	}
	
	public IConstructor parseXMLDOMTrim(IString str) {
		return parseXMLDOM(str, true);
	}

	public IConstructor parseXMLDOM(IString str) {
		return parseXMLDOM(str, false);
	}
	
	public IString xmlRaw(IConstructor node) {
		return xmlToString(node, Format.getRawFormat());
	}
	
	public IString xmlPretty(IConstructor node) {
		return xmlToString(node, Format.getPrettyFormat());
	}

	public IString xmlCompact(IConstructor node) {
		return xmlToString(node, Format.getCompactFormat());
	}
	
	private IConstructor parseXMLDOM(IString str, boolean trim) {
	    try {
	        SAXBuilder builder = new SAXBuilder();
	        CharArrayReader reader = new CharArrayReader(str.getValue().toCharArray());
	        Document doc = builder.build(reader);
	        return convertDocument(doc, trim);
	    } 
	    catch(JDOMException e) {
	        throw RuntimeExceptionFactory.io(e.getMessage());
	    }
	    catch(IOException e) {
	         throw RuntimeExceptionFactory.io(e.getMessage());
	    }
	}
	
	private IString xmlToString(IConstructor node, Format format) {
		StringWriter writer = new StringWriter();
		writeXML(writer, nodeToDocument(node), format);
		return vf.string(writer.toString());
	}
	
	private void writeXML(Writer writer, Document document, Format format) {
		XMLOutputter outputter = new XMLOutputter(format);
		try {
            outputter.output(document, writer);
            writer.close();
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e.getMessage());
        }
	}
	
	
	private Document nodeToDocument(IConstructor node) {
		if (node.getType() != Factory.Node) {
			wellformednessError();
		}
		if (node.getConstructorType() != Factory.Node_document &&
				node.getConstructorType() != Factory.Node_element) {
			wellformednessError();
		}
		
		if (node.getConstructorType() == Factory.Node_document) {
			return new Document(nodeToElement((IConstructor) node.get(0)));
		}
		return new Document(nodeToElement(node));
	}

	private Element nodeToElement(IConstructor elt) {
		IConstructor ns = (IConstructor) elt.get(0);
		IString name = (IString) elt.get(1);
		IList kids = (IList) elt.get(2);
		Element e = new Element(name.getValue(), namespaceToNamespace(ns));
		for (IValue k: kids) {
			IConstructor n = (IConstructor) k;
			if (n.getConstructorType() == Factory.Node_attribute) {
				e.setAttribute(nodeToAttribute(n));
			}
			else {
				e.addContent(nodeToContent(n));
			}
		}
		return e;
	}

	private Content nodeToContent(IConstructor n) {
		if (n.getConstructorType() == Factory.Node_element) {
			return nodeToElement(n);
		}
		if (n.getConstructorType() == Factory.Node_pi) {
			IString target = (IString) n.get(0);
			IString data = (IString) n.get(1);
			return new ProcessingInstruction(target.getValue(), data.getValue());
			
		}
		if (n.getConstructorType() == Factory.Node_charRef) {
			IInteger code = (IInteger) n.get(0);
			int c = java.lang.Integer.parseInt(code.getStringRepresentation());
			return new Text(new java.lang.String(Character.toChars(c)));
		}
		if (n.getConstructorType() == Factory.Node_entityRef) {
			return new EntityRef(((IString)n.get(0)).getValue());
		}

		java.lang.String text = ((IString)n.get(0)).getValue();
		if (n.getConstructorType() == Factory.Node_cdata) {
			return new CDATA(text);
		}
		if (n.getConstructorType() == Factory.Node_charData) {
			return new Text(text);
		}
		if (n.getConstructorType() == Factory.Node_comment) {
			return new Comment(text);
		}
		
		wellformednessError();
		return null;
	}

	private Attribute nodeToAttribute(IConstructor n) {
		IConstructor ns = (IConstructor) n.get(0);
		IString name = (IString) n.get(1);
		IString data = (IString) n.get(2);
		return new Attribute(name.getValue(), data.getValue(), namespaceToNamespace(ns));
	}

	private Namespace namespaceToNamespace(IConstructor ns) {
		if (ns.getConstructorType() == Factory.Namespace_none) {
			return Namespace.NO_NAMESPACE;
		}
		IString prefix = (IString) ns.get(0);
		IString uri = (IString) ns.get(1);
		return Namespace.getNamespace(prefix.getValue(), uri.getValue());
	}

	private void wellformednessError() {
		throw new RuntimeException("Nonwellformed XML node (TODO: make Rascal runtime exception)");
	}

	
	private IConstructor convertDocument(Document doc, boolean trim) {
		IConstructor root = convertElement(doc.getRootElement(), trim);
		return vf.constructor(Factory.Node_document, root);
	}

	private IConstructor convertElement(Element e, boolean trim) {
		IListWriter kids = vf.listWriter();
		for (Object o: e.getAttributes()) {
			Attribute attr = (Attribute)o;
			IString key = vf.string(attr.getName());
			IString val = vf.string(attr.getValue());

			kids.insert(vf.constructor(Factory.Node_attribute, convertNamespace(attr.getNamespace()), key, val));
		}

		int len = e.getContentSize();
		for (int i = 0; i < len; i++) {
			try {
				kids.append(convertContent(e.getContent(i), trim));
			}
			catch (Skip c) { // Ugh, terrible, but I'm in hurry
				continue;
			}
		}
		
		IString name = vf.string(e.getName());
		return vf.constructor(Factory.Node_element, convertNamespace(e.getNamespace()), name, kids.done());
	}

	private IConstructor convertNamespace(Namespace ns) {
		if (ns == Namespace.NO_NAMESPACE) {
			return vf.constructor(Factory.Namespace_none);
		}
		IString prefix = vf.string(ns.getPrefix());
		IString uri = vf.string(ns.getURI());
		IConstructor nscon = vf.constructor(Factory.Namespace_namespace, prefix, uri);
		return nscon;
	}

	private IConstructor convertContent(Content content, boolean trim) throws Skip {
		if (content instanceof Element) {
			return convertElement((Element)content, trim);
		}
		if (content instanceof CDATA) { // CDATA first (is subtype of Text)
			CDATA cdata = (CDATA)content;
			return vf.constructor(Factory.Node_cdata, getString(trim, cdata));
		}
		if (content instanceof Text) {
			Text text = (Text)content;
			return vf.constructor(Factory.Node_charData, getString(trim, text));
		}
		if (content instanceof Comment) {
			Comment comment = (Comment)content;
			IString data = vf.string(comment.getText());
			return vf.constructor(Factory.Node_comment, data);
		}
		if (content instanceof ProcessingInstruction) {
			ProcessingInstruction pi = (ProcessingInstruction)content;
			IString data = vf.string(pi.getData());
			return vf.constructor(Factory.Node_pi, data);
		}
		if (content instanceof EntityRef) {
			EntityRef er = (EntityRef)content;
			IString data = vf.string(er.getName());
			return vf.constructor(Factory.Node_entityRef, data);
		}
		throw new AssertionError("cannot convert JDOM content type " + content.getClass());
	}

	private IString getString(boolean trim, Text text) throws Skip {
		if (trim) {
			java.lang.String s = text.getTextTrim();
			if ("".equals(s)) {
				throw new Skip();
			}
			return vf.string(s);
		}
		return vf.string(text.getText());
	}
}
