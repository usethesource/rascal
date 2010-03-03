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
	public static final Type Content = tf.abstractDataType(xml, "Content");
		
	public static final Type Content_element = tf.constructor(xml, Content, "element", 
			tf.stringType(), "name",
			tf.mapType(tf.stringType(), tf.valueType()), "attrs",
			tf.listType(Content), "contents");
	public static final Type Content_charData = tf.constructor(xml, Content, "charData",
			tf.stringType(), "text");
	public static final Type Content_cdata = tf.constructor(xml, Content, "cdata",
			tf.stringType(), "text");
	public static final Type Content_comment = tf.constructor(xml, Content, "comment",
			tf.stringType(), "text");
	public static final Type Content_pi = tf.constructor(xml, Content, "pi",
			tf.stringType(), "name",
			tf.stringType(), "text");
	
	public static final Type Content_entityRef = tf.constructor(xml, Content, "entityRef",
			tf.stringType(), "name");

	public static final Type Content_charRef = tf.constructor(xml, Content, "charRef",
			tf.integerType(), "code");
	
	public static final Type Document_documentRoot = tf.constructor(xml, Document, "document", 
			Content_element, "root");

	public static final Type Document_documentVersion = tf.constructor(xml, Document, "document", 
			tf.stringType(), "version", Content_element, "root");

	public static final Type Document_documentVersionEncoding = tf.constructor(xml, Document, "document", 
			tf.stringType(), "version", tf.stringType(), "encoding", Content_element, "root");

	public static final Type Document_documentVersionStandalone = tf.constructor(xml, Document, "document", 
			tf.stringType(), "version", tf.boolType(), "standalone", Content_element, "root");

	public static final Type Document_documentVersionEncodingStandalone = tf.constructor(xml, Document, "document", 
			tf.stringType(), "version", tf.stringType(), "encoding", tf.boolType(), "standalone", Content_element, "root");

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
