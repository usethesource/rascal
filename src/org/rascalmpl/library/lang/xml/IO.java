/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.lang.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

class XMLIOThing{
	private final static TypeFactory tf = TypeFactory.getInstance();
	
	private final static Type STRING_TYPE = tf.stringType();
	private final static Type ATTRIBUTES_NODE = tf.mapType(tf.stringType(), tf.stringType());
	private final IMap noAttributes;

	private final IValueFactory vf;
	
	private final Type anyType;
	
	private final TypeStore typeStore;
	private final Document document;
	
	public XMLIOThing(IValueFactory vf, TypeStore typeStore, Document document){
		super();
		
		this.vf = vf;
		this.anyType = tf.abstractDataType(typeStore, "anyType");
		this.typeStore = typeStore;
		this.document = document;
		
		noAttributes = vf.map(STRING_TYPE, STRING_TYPE);
	}
	
	public IConstructor transform(){
		return visitElement(document.getDocumentElement());
	}
	
	private IConstructor visitElement(Element e){
		TypeInfo typeInfo = e.getSchemaTypeInfo();
		java.lang.String typeName = typeInfo.getTypeName();
		
		Type adt = anyType;
		if(typeName != null && !typeName.contains("#AnonType")){
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
		if(!e.hasAttributes()) return noAttributes;
		
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
	
	/*public void declareTypes(XSModel schema){
		XSNamedMap elementDeclarations = schema.getComponents(XSConstants.ELEMENT_DECLARATION);
		int length = elementDeclarations.getLength();
		for(int i = length - 1; i >= 0; i--){
			XSElementDecl elementDecl = (XSElementDecl) elementDeclarations.item(i);
			System.err.println(elementDecl.getTypeDefinition().getBaseType().getName());
		}
		
		System.err.println();
		
		XSNamedMap typeDefinitions = schema.getComponents(XSConstants.TYPE_DEFINITION);
		length = typeDefinitions.getLength();
		for(int i = length - 1; i >= 0; i--){
			System.err.println(typeDefinitions.item(i).getName());
		}
	}*/
}

public class IO{
	private final IValueFactory vf;
	
	public IO(IValueFactory vf){
		super();
		
		this.vf = vf;
	}
	
	public IConstructor readXMLFile(ISourceLocation file, IEvaluatorContext ctx) throws IOException, SAXException, ParserConfigurationException{
		
		ErrorHandler errorHandler = new ErrorHandler(){
			public void fatalError(SAXParseException exception) throws SAXException{
				// Do nothing
				System.err.println("fatal "+exception.getMessage());
			}
			
			public void error(SAXParseException exception) throws SAXException{
				// Do nothing
				System.err.println("error "+exception.getMessage());
			}
			
			public void warning(SAXParseException exception) throws SAXException{
				// Do nothing
				System.err.println("warn "+exception.getMessage());
			}
		};
		
		System.setProperty(DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMXSImplementationSourceImpl");
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setValidating(true);
		dbf.setAttribute("http://apache.org/xml/features/validation/schema", java.lang.Boolean.TRUE);
		dbf.setAttribute("http://apache.org/xml/properties/dom/document-class-name", "org.apache.xerces.dom.PSVIDocumentImpl");
		
		DocumentBuilder parser = dbf.newDocumentBuilder();
		parser.setErrorHandler(errorHandler);
		Document document = parser.parse(ctx.getResolverRegistry().getInputStream(file.getURI()));
		
		TypeStore typeStore = new TypeStore();
		XMLIOThing xmlToPDB = new XMLIOThing(vf, typeStore, document);
		
		//xmlToPDB.declareTypes(((ElementPSVI) document.getDocumentElement()).getSchemaInformation());
		
		return xmlToPDB.transform();
	}
}
