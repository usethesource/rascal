package org.rascalmpl.library;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Comment;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.EntityRef;
import org.jdom.JDOMException;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;

import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.xml.Factory;

public class XMLDOM {
	private final IValueFactory vf;
	private final TypeFactory tf;
	
	public XMLDOM(IValueFactory vf) {
		this.vf = vf;
		this.tf = TypeFactory.getInstance();
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
	
	private IConstructor convertDocument(Document doc, boolean trim) {
		IConstructor root = convertElement(doc.getRootElement(), trim);
		return vf.constructor(Factory.Document_documentRoot, root);
	}

	private IConstructor convertElement(Element e, boolean trim) {
		IMapWriter attrs = vf.mapWriter(tf.stringType(), tf.stringType());
		for (Object o: e.getAttributes()) {
			Attribute attr = (Attribute)o;
			
			// TODO: namespaces
			IString key = vf.string(attr.getName());
			
			// Strings for now.
			IString val = vf.string(attr.getValue());
			
			attrs.put(key, val);
		}
		
		// TODO: namespace
		IString name = vf.string(e.getName());
	
		int len = e.getContentSize();
		IListWriter kids = vf.listWriter(Factory.Content);
		for (int i = 0; i < len; i++) {
			try {
				kids.append(convertContent(e.getContent(i), trim));
			}
			catch (Skip c) { // Ugh, terrible, but I'm in hurry
				continue;
			}
		}
		return vf.constructor(Factory.Content_element, name, attrs.done(), kids.done());
	}

	private IConstructor convertContent(Content content, boolean trim) throws Skip {
		if (content instanceof Element) {
			return convertElement((Element)content, trim);
		}
		if (content instanceof CDATA) {
			CDATA cdata = (CDATA)content;
			return vf.constructor(Factory.Content_cdata, getString(trim, cdata));
		}
		if (content instanceof Text) {
			Text text = (Text)content;
			return vf.constructor(Factory.Content_charData, getString(trim, text));
		}
		if (content instanceof Comment) {
			Comment comment = (Comment)content;
			IString data = vf.string(comment.getText());
			return vf.constructor(Factory.Content_comment, data);
		}
		if (content instanceof ProcessingInstruction) {
			ProcessingInstruction pi = (ProcessingInstruction)content;
			IString data = vf.string(pi.getData());
			return vf.constructor(Factory.Content_pi, data);
		}
		if (content instanceof EntityRef) {
			EntityRef er = (EntityRef)content;
			IString data = vf.string(er.getName());
			return vf.constructor(Factory.Content_entityRef, data);
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
