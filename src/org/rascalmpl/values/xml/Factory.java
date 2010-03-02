package org.rascalmpl.values.xml;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;

public class Factory {
	public static TypeStore xml = new TypeStore(
			org.rascalmpl.values.errors.Factory.getStore(), 
			org.rascalmpl.values.locations.Factory.getStore());
	
	private static TypeFactory tf = TypeFactory.getInstance();
	
	public static final Type Document = tf.abstractDataType(xml, "Document");
	public static final Type XMLDecl = tf.abstractDataType(xml, "XMLDecl");
	public static final Type Element = tf.abstractDataType(xml, "Element");
	public static final Type Content = tf.abstractDataType(xml, "Content");


	public static final Type XMLDecl_xml1 = tf.constructor(xml, XMLDecl, "xml", 
			tf.stringType(), "version");
	public static final Type XMLDecl_xml2 = tf.constructor(xml, XMLDecl, "xml", 
			tf.stringType(), "version",
			tf.stringType(), "encoding");
	public static final Type XMLDecl_xml3 = tf.constructor(xml, XMLDecl, "xml", 
			tf.stringType(), "version",
			tf.boolType(), "standalone");
	public static final Type XMLDecl_xml4 = tf.constructor(xml, XMLDecl, "xml", 
			tf.stringType(), "version",
			tf.stringType(), "encoding",
			tf.boolType(), "standalone");
		
	public static final Type Content_element = tf.constructor(xml, Element, "element", 
			tf.stringType(), "name",
			tf.mapType(tf.stringType(), tf.stringType()), "attrs",
			tf.listType(Content), "contents");
	public static final Type Content_charData = tf.constructor(xml, Content, "charData",
			tf.stringType(), "data");
	public static final Type Content_cdata = tf.constructor(xml, Content, "cdata",
			tf.stringType(), "data");
	public static final Type Content_comment = tf.constructor(xml, Content, "comment",
			tf.stringType(), "data");
	public static final Type Content_pi = tf.constructor(xml, Content, "pi",
			tf.stringType(), "name",
			tf.stringType(), "data");
	
	public static final Type Content_entityRef = tf.constructor(xml, Content, "entityRef",
			tf.stringType(), "name");

	public static final Type Content_charRef = tf.constructor(xml, Content, "charRef",
			tf.integerType(), "code");
	
	public static final Type Document_document = tf.constructor(xml, Document, "document", 
			XMLDecl, "xml", Content_element, "root");

	private static final class InstanceHolder {
		public final static Factory factory = new Factory();
	}
	  
	public static Factory getInstance() {
		return InstanceHolder.factory;
	}
	
	private Factory() {
	}
	
	public static TypeStore getStore() {
		return xml;
	}
}
