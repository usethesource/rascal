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
	
	public IConstructor readXMLDOM(ISourceLocation file) throws IOException, JDOMException {
		SAXBuilder builder = new SAXBuilder();
		InputStream stream = URIResolverRegistry.getInstance().getInputStream(file.getURI());
		Document doc = builder.build(stream);
		return convertDocument(doc);
	}
	
	private IConstructor convertDocument(Document doc) {
		IConstructor root = convertElement(doc.getRootElement());
		return vf.constructor(Factory.Document_documentRoot, root);
	}

	private IConstructor convertElement(Element e) {
		IMapWriter attrs = vf.mapWriter(tf.stringType(), tf.valueType());
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
			kids.append(convertContent(e.getContent(i)));
		}
		return vf.constructor(Factory.Content_element, name, attrs.done(), kids.done());
	}

	private IConstructor convertContent(Content content) {
		if (content instanceof Element) {
			return convertElement((Element)content);
		}
		if (content instanceof CDATA) {
			CDATA cdata = (CDATA)content;
			IString data = vf.string(cdata.getText());
			return vf.constructor(Factory.Content_cdata, data);
		}
		if (content instanceof Text) {
			Text text = (Text)content;
			IString data = vf.string(text.getText());
			return vf.constructor(Factory.Content_charData, data);
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
}
