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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IExternalValue;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.UnsupportedTypeException;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This IValueWriter serializes values to XML documents.  
 * It will not serialize all IValues, see <code>XMLReader</code> for limitations. 
 */
public class XMLWriter implements IValueTextWriter {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	private DocumentBuilder fBuilder;
	
	public void write(IValue value, java.io.Writer stream) throws IOException {
		try {
			fBuilder = dbf.newDocumentBuilder();
			Document doc = fBuilder.newDocument();
			
			Node top = yield(value, doc);
			doc.appendChild(top);
			
			Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");  
            
			t.transform(new DOMSource(doc), new StreamResult(stream));
		} catch (ParserConfigurationException e) {
			throw new IOException("XML configuration is invalid: " + e.getMessage());
		} catch (TransformerException e) {
			throw new IOException("Exception while serializing XML: " + e.getMessage());
		}
	}
	
	public void write(IValue value, java.io.Writer stream, TypeStore typeStore) throws IOException {
		write(value, stream);
	}

	private Node yield(IValue value, Document doc) {
		Type type = value.getType();
		
		if (type.isAbstractData()) {
			Type node = ((IConstructor) value).getConstructorType();
			
			if (isListWrapper(node)) {
				return yieldList((INode) value,  doc);
			}
			else if (isSetWrapper(node)) {
				return yieldSet((INode) value, doc);
			}
			else if (isRelationWrapper(node)) {
				return yieldRelation((INode) value, doc);
			}
			else if (isMapWrapper(node)) {
				return yieldMap((INode) value, doc);
			}
			else {
			  return yieldTree((INode) value, doc);
			}
		}
		else if (type.isString()) {
			return yieldString((IString) value, doc);
		}
		else if (type.isInteger()) {
			return yieldInt((IInteger) value, doc);
		}
		else if (type.isRational()) {
			return yieldRational((IRational) value, doc);
		}
		else if (type.isReal()) {
			return yieldDouble((IReal) value, doc);
		}
		else if (type.isExternalType()) {
			return yieldExternal((IExternalValue) value, doc);
		}

		throw new UnsupportedTypeException(
				"Outermost or nested tuples, lists, sets, relations or maps are not allowed.", type);
	}
	
	private boolean isListWrapper(Type nodeType) {
			return nodeType.getArity() == 1
					&& nodeType.getFieldTypes().getFieldType(0).isList();
	}

	private boolean isSetWrapper(Type nodeType) {
			return nodeType.getArity() == 1
					&& nodeType.getFieldTypes().getFieldType(0).isSet();
	}

	private boolean isRelationWrapper(Type nodeType) {
			return nodeType.getArity() == 1
					&& nodeType.getFieldTypes().getFieldType(0)
							.isRelation();
	}

	private boolean isMapWrapper(Type nodeType) {
			return nodeType.getArity() == 1
					&& nodeType.getFieldTypes().getFieldType(0).isMap();
	}

	
	private Node yieldDouble(IReal value, Document doc) {
		return doc.createTextNode(value.toString());
	}

	private Node yieldInt(IInteger value, Document doc) {
		return doc.createTextNode(value.toString());
	}

	private Node yieldRational(IRational value, Document doc) {
/*		Element element = doc.createElementNS("values", "rat");
		element.setAttribute("num", value.numerator().toString());
		element.setAttribute("denom", value.denominator().toString());
		return element;
*/	
		return null;
	}

	private Node yieldString(IString value, Document doc) {
		return doc.createTextNode(value.getValue());
	}
	
	private Node yieldExternal(IExternalValue value, Document doc) {
		return doc.createTextNode(value.toString());
	}

	private Node yieldMap(INode node, Document doc) {
		Element treeNode = doc.createElement(node.getName());
		IMap map = (IMap) node.get(0);
		
		for (IValue key : map) {
			IValue value = map.get(key);
			
			if (key.getType().isTuple()) {
				appendTupleElements(doc, treeNode, key);
			}
			else {
			  treeNode.appendChild(yield(key, doc));
			}
			
			if (value.getType().isTuple()) {
                appendTupleElements(doc, treeNode, value);
			}
			else {
			  treeNode.appendChild(yield(value, doc));
			}
		}

		return treeNode;
	}

	private void appendTupleElements(Document doc, Element treeNode, IValue tupleValue) {
		ITuple tuple = (ITuple) tupleValue;
		
		for (IValue element : tuple) {
			treeNode.appendChild(yield(element, doc));
		}
	}

	private Node yieldRelation(INode node, Document doc) {
		Element treeNode = doc.createElement(node.getName());
		ISet relation = (ISet) node.get(0);
		assert (relation.getType().isRelation());
		 
		for (IValue tuple : relation) {
			appendTupleElements(doc, treeNode, tuple);
		}

		return treeNode;
	}
	
	private Node yieldSet(INode node, Document doc) {
		Element treeNode = doc.createElement(node.getName());
		ISet set = (ISet) node.get(0);
		
		for (IValue elem : set) {
			if (elem.getType().isTuple()) {
			  appendTupleElements(doc, treeNode, elem);
			}
			else {
			  treeNode.appendChild(yield(elem, doc));
			}
		}

		return treeNode;
	}

	private Node yieldList(INode node, Document doc) {
		Element treeNode = doc.createElement(node.getName());
		IList list = (IList) node.get(0);
		
		for (IValue elem : list) {
			if (elem.getType().isTuple()) {
				appendTupleElements(doc, treeNode, elem);
			}
			else {
			  treeNode.appendChild(yield(elem, doc));
			}
		}

		return treeNode;
	}

	private Node yieldTree(INode value,  Document doc) {
		Element treeNode = doc.createElement(value.getName());
		
		for (IValue child : value) {
			if (child.getType().isTuple()) {
				appendTupleElements(doc, treeNode, child);
			}
			else {
			  treeNode.appendChild(yield(child, doc));
			}
		}
		
		return treeNode;
	}
}
