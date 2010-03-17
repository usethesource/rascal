package org.rascalmpl.library;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Comment;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.EntityRef;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.xml.Factory;

public class XMLDOM {
	private final IValueFactory vf;
	
	public XMLDOM(IValueFactory vf) {
		this.vf = vf;
	}
	
	@SuppressWarnings("serial")
	private static class Skip extends Exception { }

	public IConstructor readXMLDOMTrim(ISourceLocation file) throws IOException, JDOMException {
		return readXMLDOM(file, true);
	}

	public IConstructor readXMLDOM(ISourceLocation file) throws IOException, JDOMException {
		return readXMLDOM(file, false);
	}
	
	private IConstructor readXMLDOM(ISourceLocation file, boolean trim) throws IOException, JDOMException {
		SAXBuilder builder = new SAXBuilder();
		InputStream stream = URIResolverRegistry.getInstance().getInputStream(file.getURI());
		Document doc = builder.build(stream);
		return convertDocument(doc, trim);
	}
	
	
	
	public void writeXMLRaw(ISourceLocation file, IConstructor node) throws IOException {
		writeXML(file, node, Format.getRawFormat());
	}
	
	public void writeXMLPretty(ISourceLocation file, IConstructor node) throws IOException {
		writeXML(file, node, Format.getPrettyFormat());
	}

	public void writeXMLCompact(ISourceLocation file, IConstructor node) throws IOException {
		writeXML(file, node, Format.getCompactFormat());
	}
	
	private void writeXML(ISourceLocation file, IConstructor node, Format format) throws IOException {
		XMLOutputter outputter = new XMLOutputter(format);
		OutputStream stream = URIResolverRegistry.getInstance().getOutputStream(file.getURI(), false);
		Document doc = nodeToDocument(node);
		outputter.output(doc, stream);
		stream.close();
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
			new EntityRef(((IString)n.get(0)).getValue());
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
		IListWriter kids = vf.listWriter(Factory.Node);
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
