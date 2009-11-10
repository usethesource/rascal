package org.meta_environment.rascal.library;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.values.ValueFactoryFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLIO{
	private final static TypeFactory tf = TypeFactory.getInstance();
	private final static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	private final static Type STRING_TYPE = tf.stringType();
	private final static Type ATTRIBUTES_NODE = tf.mapType(tf.stringType(), tf.stringType());
	private final static IMap NO_ATTRIBUTES = vf.map(STRING_TYPE, STRING_TYPE);
	
	private final Type anonymousType;
	
	private final TypeStore typeStore;
	private final Document document;
	
	public XMLIO(TypeStore typeStore, Document document){
		super();
		
		this.anonymousType = tf.abstractDataType(typeStore, "ANONYMOUS_TYPE");
		this.typeStore = typeStore;
		this.document = document;
	}
	
	public IConstructor transform(){
		return visitElement(document.getDocumentElement());
	}
	
	private IConstructor visitElement(Element e){
		TypeInfo typeInfo = e.getSchemaTypeInfo();
		java.lang.String typeName = typeInfo.getTypeName();
		
		Type adt = anonymousType;
		if(typeName != null){
			adt = tf.abstractDataType(typeStore, typeName);
		}
		
		NodeList childNodesList = e.getChildNodes();
		int nrOfChildNodes = childNodesList.getLength();
		
		List<Type> childrenTypesList = new ArrayList<Type>();
		childrenTypesList.add(ATTRIBUTES_NODE);
		
		List<IValue> childrenList = new ArrayList<IValue>();
		childrenList.add(getAttributes(e));
		
		for(int i = 0; i < nrOfChildNodes; i++){
			Node childNode =  childNodesList.item(i);
			if(childNode instanceof Element){ // Element
				Element c = (Element) childNode;
				IConstructor child = visitElement(c);
				childrenTypesList.add(child.getType());
				childrenList.add(child);
			}else{ // Text node
				java.lang.String textContent = childNode.getNodeValue();
				childrenTypesList.add(STRING_TYPE);
				childrenList.add(vf.string(textContent));
			}
		}
		
		int nrOfChildren = childrenTypesList.size();
		Type[] types = new Type[nrOfChildren];
		types = childrenTypesList.toArray(types);
		
		IValue[] children = new IValue[nrOfChildren];
		children = childrenList.toArray(children);
		
		java.lang.String name = e.getNodeName();
		Type consType = tf.constructor(typeStore, adt, name, types);
		
		return vf.constructor(consType, children);
	}
	
	private IMap getAttributes(Element e){
		if(!e.hasAttributes()) return NO_ATTRIBUTES;
		
		IMapWriter attributesWriter = vf.mapWriter(STRING_TYPE, STRING_TYPE);
		NamedNodeMap attributesMap = e.getAttributes();
		for(int i = attributesMap.getLength() - 1; i >= 0; i--){
			Node attribute = attributesMap.item(i);
			java.lang.String name = attribute.getNodeName();
			java.lang.String value = attribute.getNodeValue();
			attributesWriter.put(vf.string(name), vf.string(value));
		}
		
		return attributesWriter.done();
	}
	
	public static IConstructor parseXML(IString xmlFileName, IString xsdFileName) throws IOException, SAXException, ParserConfigurationException{
		File xmlFile = new File(xmlFileName.getValue());
		File xsdFile = new File(xsdFileName.getValue());
		
		ErrorHandler errorHandler = new ErrorHandler(){
			public void fatalError(SAXParseException exception) throws SAXException{
				// Do nothing
			}
			
			public void error(SAXParseException exception) throws SAXException{
				// Do nothing
			}
			
			public void warning(SAXParseException exception) throws SAXException{
				// Do nothing
			}
		};
		
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		schemaFactory.setErrorHandler(errorHandler);
		Schema schemaXSD = schemaFactory.newSchema(xsdFile);
		Validator validator = schemaXSD.newValidator();
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = parser.parse(xmlFile);
		validator.validate(new DOMSource(document));
		
		
		TypeStore typeStore = new TypeStore();
		XMLIO xmlToPDB = new XMLIO(typeStore, document);
		return xmlToPDB.transform();
	}
}
