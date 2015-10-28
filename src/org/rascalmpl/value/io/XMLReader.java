/*******************************************************************************
* Copyright (c) 2008 CWI.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    jurgen@vinju.org - initial API and implementation

*******************************************************************************/

package org.rascalmpl.value.io;

import java.io.IOException;
import java.io.Reader;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactParseError;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.exceptions.UnsupportedTypeException;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This IValueReader parses and validates certain forms of XML and deserializes
 * it as IValues. The forms of XML allowed are limited by a number of different
 * value types. In particular, it allows: <ul>
 *   <li> TreeSortTypes and TreeNodeTypes </li>
 *   <li> lists, sets, relations and maps, but not unless they are wrapped by a single
 *     ConstructorType. I.o.w. a container must be the only child of a tree node.
 *     Elements of containers are juxtapositioned as children of this node.</li>
 *   <li> tuples, but not nested ones. And tuples of containers are not allowed.
 *     elements of tuples are juxtapositioned in the xml files.</li>
 *   <li> basic types, such as str, int, double; with the same restriction as for
 *     container types, they must be the only child of a tree node.</li>
 *   <li> lists of tuples, sets of tuples and maps of tuples are allowed, but not
 *     lists of lists, tuples of tuples, lists in tuples, sets in tuples, etc.
 *     If such nesting is needed, it is required to use a wrapping tree node.</li>
 * </ul>
 * There is no support for NamedTypes yet, only TreeSortType and ConstructorType are 
 * allowed.
 *     
 * The limitations of this class are governed by wanting to avoid ambiguity
 * while validating XML using the pdb's type system and the inherent impedance
 * mismatch between the type system of pdb and the structure of XML.
 * 
 * Use this class to import many forms of XML data into PDB.
 * 
 */
public class XMLReader extends AbstractTextReader {
	private DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	private IValueFactory vf;
	private static final TypeFactory TF = TypeFactory.getInstance();
	private TypeStore ts;

	public IValue read(IValueFactory factory, TypeStore store, Type type, Reader stream)
			throws FactTypeUseException, IOException {
		this.vf = factory;
		this.ts = store;
		
		try {
			Document doc = domFactory.newDocumentBuilder().parse(new InputSource(stream));
			return parse(doc.getDocumentElement(), type);
		} catch (SAXException se) {
			throw new IOException("Parsing of value failed because XML was invalid: " + se.getMessage());
		} catch (ParserConfigurationException pce) {
			throw new IOException("Parsing of value failed because XML configuration is wrong: " + pce.getMessage());
		} catch (DOMException de) {
			throw new IOException("Parsing of value failed because of a XML document failure: " + de.getMessage());
		} catch (NumberFormatException nfe) {
			throw new FactParseError("Expected a number, got something different", nfe);
		}
	}
	
	private IValue parse(Node node, Type expected) {
		if (expected.isAbstractData()) {
			Type sort = expected;
			String name = node.getNodeName();
			
			if (isListWrapper(name,  sort)) {
				return parseList(node,  sort);
			}
			else if (isSetWrapper(name, sort)) {
				return parseSet(node, sort);
			}
			else if (isRelationWrapper(name, sort)) {
				return parseRelation(node, sort);
			}
			else if (isMapWrapper(name, sort)) {
				return parseMap(node, sort);
			}
			else {
			  return parseTreeSort(node, sort);
			}
		}
		else if (expected.equivalent(TF.stringType())) {
			return parseString(node);
		}
		else if (expected.equivalent(TF.integerType())) {
			return parseInt(node);
		}
		else if (expected.equivalent(TF.realType())) {
			return parseDouble(node);
		}
		else if (expected.equivalent(TF.rationalType())) {
			return parseRational(node);
		}
		else if (expected.isExternalType()) {
			// external types default to string
			return parseString(node);
		}

		throw new UnsupportedTypeException(
				"Outermost or nested tuples, lists, sets, relations or maps are not allowed.", expected);
	}

	private boolean isListWrapper(String name, Type expected) {
		Set<Type> nodeTypes = ts.lookupConstructor(expected, name);

		if (nodeTypes.size() > 0) {
			Type nodeType = nodeTypes.iterator().next();
			return nodeType.getArity() == 1
					&& nodeType.getFieldTypes().getFieldType(0).isSubtypeOf(TF.listType(TF.valueType()));
		}

		return false;
	}

	private boolean isSetWrapper(String name, Type expected) {
		Set<Type> nodeTypes = ts.lookupConstructor(expected, name);

		if (nodeTypes.size() > 0) {
			Type nodeType = nodeTypes.iterator().next();
			return nodeType.getArity() == 1
					&& nodeType.getFieldTypes().getFieldType(0).isSubtypeOf(TF.setType(TF.valueType()));
		}

		return false;
	}

	private boolean isRelationWrapper(String name, Type expected) {
		Set<Type> nodeTypes = ts.lookupConstructor(expected, name);

		if (nodeTypes.size() > 0) {
			Type nodeType = nodeTypes.iterator().next();
			return nodeType.getArity() == 1
					&& nodeType.getFieldTypes().getFieldType(0).isSubtypeOf(TF.setType(TF.valueType()))
					&& nodeType.getFieldTypes().getFieldType(0).getElementType().isFixedWidth();
		}

		return false;
	}

	private boolean isMapWrapper(String name, Type expected) {
		Set<Type> nodeTypes = ts.lookupConstructor(expected, name);

		if (nodeTypes.size() > 0) {
			Type nodeType = nodeTypes.iterator().next();
			return nodeType.getArity() == 1
					&& nodeType.getFieldTypes().getFieldType(0).isMap();
		}

		return false;
	}

	// TODO: implement this
	private IValue parseRational(Node node) {
		String contents = node.getNodeValue().trim();
		String[] parts = contents.split("r");
		if (parts.length == 2) {
			return vf.rational(vf.integer(Integer.parseInt(parts[0])), vf.integer(Integer.parseInt(parts[0])));
		}
		throw new FactParseError(contents, 0);
	}

	private IValue parseDouble(Node node) {
		return vf.real(Double.parseDouble(node.getNodeValue().trim()));
	}

	private IValue parseInt(Node node) {
		return vf.integer(Integer.parseInt(node.getNodeValue().trim()));
	}

	private IValue parseString(Node node) {
		return vf.string(node.getNodeValue());
	}

	private IValue parseMap(Node node, Type expected) {
		Set<Type> nodeTypes = ts.lookupConstructor(expected, node.getNodeName());
		// TODO: implement overloading
		Type nodeType = nodeTypes.iterator().next();
		Type mapType = nodeType.getFieldType(0);
		Type keyType = mapType.getKeyType();
		Type valueType = mapType.getValueType();
		NodeList children = node.getChildNodes();
		IMapWriter writer = vf.mapWriter();
		
		for (int i = 0; i + 1 < children.getLength(); ) {
			IValue key, value;
			
			if (keyType.isFixedWidth()) {
				Type tuple = keyType;
				IValue [] elements = new IValue[tuple.getArity()];
				for (int j = 0; j < tuple.getArity(); j++) {
					elements[i] = parse(children.item(i++), tuple.getFieldType(j));
				}
				
				key = vf.tuple(elements);
			}
			else {
			  key = parse(children.item(i++), keyType);
			}
			
			if (valueType.isFixedWidth()) {
				Type tuple = keyType;
				IValue [] elements = new IValue[tuple.getArity()];
				for (int j = 0; j < tuple.getArity(); j++) {
					elements[i] = parse(children.item(i++), tuple.getFieldType(j));
				}
				
				value = vf.tuple(elements);
			}
			else {
			  value = parse(children.item(i++), valueType);
			}
			
			writer.put(key, value);
		}
		
		
		return vf.constructor(nodeType, writer.done());
	}

	private IValue parseRelation(Node node, Type expected) {
		Set<Type> nodeTypes = ts.lookupConstructor(expected, node.getNodeName());
		// TODO implement overloading
		Type nodeType = nodeTypes.iterator().next();
		Type relType = nodeType.getFieldType(0);
		Type fields = relType.getFieldTypes();
		NodeList children = node.getChildNodes();
		ISetWriter writer = vf.setWriter();
		
		for (int i = 0; i < children.getLength(); ) {
			IValue[] elements = new IValue[fields.getArity()];
				
			for (int j = 0; i < children.getLength() && j < fields.getArity(); j++) {
			   elements[j] = parse(children.item(i++), fields.getFieldType(j));	
			}
				
				writer.insert(vf.tuple(elements));
		}
		
		return vf.constructor(nodeType, writer.done());
	}

	private IValue parseSet(Node node, Type expected) {
		Set<Type> nodeTypes = ts.lookupConstructor(expected, node.getNodeName());
		// TODO implement overloading
		Type nodeType = nodeTypes.iterator().next();
		Type setType = nodeType.getFieldType(0);
		Type elementType = setType.getElementType();
		NodeList children = node.getChildNodes();
		ISetWriter writer = vf.setWriter();
		
		if (!elementType.isFixedWidth()) {
			for (int i = 0; i < children.getLength(); i++) {
				writer.insert(parse(children.item(i), elementType));
			}
		} else {
		    Type tuple = elementType;
			for (int i = 0; i < children.getLength(); ) {
				IValue[] elements = new IValue[tuple.getArity()];
				
				for (int j = 0; i < children.getLength() && j < tuple.getArity(); j++) {
				  elements[j] = parse(children.item(i++), tuple.getFieldType(j));	
				}
				
				writer.insert(vf.tuple(elements));
			}
		}
		
		return vf.constructor(nodeType, writer.done());
	}

	private IValue parseList(Node node, Type expected) {
		Set<Type> nodeTypes = ts.lookupConstructor(expected, node.getNodeName());
		// TODO implement overloading
		Type nodeType = nodeTypes.iterator().next();
		Type listType = nodeType.getFieldType(0);
		Type elementType = listType.getElementType();
		NodeList children = node.getChildNodes();
		IListWriter writer = vf.listWriter();
		
		if (!elementType.isFixedWidth()) {
			for (int i = 0; i < children.getLength(); i++) {
				writer.append(parse(children.item(i), elementType));
			}
		} else {
			Type tuple = elementType;
			for (int i = 0; i < children.getLength(); ) {
				IValue[] elements = new IValue[tuple.getArity()];
				
				for (int j = 0; i < children.getLength() && j < tuple.getArity(); j++) {
				  elements[j] = parse(children.item(i++), tuple.getFieldType(j));	
				}
				
				writer.append(vf.tuple(elements));
			}
		}
		
		return vf.constructor(nodeType, writer.done());
	}
   
	private IValue parseTreeSort(Node node, Type expected) {
		// TODO deal with overloading
		Type  nodeType = ts.lookupConstructor(expected, node.getNodeName()).iterator().next();
		Type childrenTypes = nodeType.getFieldTypes();
		NodeList children = node.getChildNodes();
		
	    IValue[] values = new IValue[nodeType.getArity()];
	    
	    int sourceIndex = 0;
	    int targetIndex = 0;
	    
		while(sourceIndex < children.getLength() && targetIndex < nodeType.getArity()) {
			Type childType = childrenTypes.getFieldType(targetIndex);
			
			if (childType.isFixedWidth()) {
				Type tuple = childType;
				IValue[] elements = new IValue[tuple.getArity()];
			  
				for (int tupleIndex = 0; tupleIndex < tuple.getArity() && sourceIndex < children.getLength(); tupleIndex++, sourceIndex++) {
				  elements[tupleIndex] = parse(children.item(sourceIndex), tuple.getFieldType(tupleIndex));
			    }
				
				values[targetIndex++] = vf.tuple(elements);
			}
			else {
			  values[targetIndex++] = parse(children.item(sourceIndex++), childType);
			}
		}
		
		return vf.constructor(nodeType, values);
	}
}
