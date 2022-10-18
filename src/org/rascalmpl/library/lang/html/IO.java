/*******************************************************************************
 * Copyright (c) 2009-2022 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.lang.html;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Map.Entry;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.SchemaFactory;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDOMImplementation;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.SAXException;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeStore;

public class IO {
    private final IValueFactory factory;
    private final TypeStore store;
    private final Type HTMLElement;

    public IO(IValueFactory factory, TypeStore store) {
        this.factory = factory;
        this.store = store;
        this.HTMLElement = store.lookupAbstractDataType("HTMLElement");
    }
    
    public IValue readHTMLString(IString string) {
        if (string.length() == 0) {
            throw RuntimeExceptionFactory.io("empty HTML document");
        }

        try (Reader reader = new StringReader(string.getValue())) {
            Constructor cons = new Constructor();
            new ParserDelegator().parse(reader, cons, true);
            return cons.getValue();
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(factory.string(e.getMessage()));
        }
    }

    public IValue readHTMLFile(ISourceLocation file) {
        try (Reader reader = URIResolverRegistry.getInstance().getCharacterReader(file)) {
            Constructor cons = new Constructor();
            new ParserDelegator().parse(reader, cons, true);
            return cons.getValue();
        } catch (MalformedURLException e) {
            throw RuntimeExceptionFactory.malformedURI(file.getURI().toASCIIString());
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(factory.string(e.getMessage()));
        }
    }

    /**
     * Produces a HTML string output from an HTMLElement AST.
     * 
     * Why go through all the trouble of building a DOM? The only reason is compliance.
     * Escapes, encodings, etc. all are maintained by these classes from org.w3c.dom.
     */
    public IString writeHTMLString(IConstructor cons) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            // dbf.setSchema(SchemaFactory.newDefaultInstance().newSchema(new URL("http://www.w3.org/1999/xhtml")));
            
            Document doc = db.newDocument();
            
            Element elem = doc.createElement("html");
            createDocument(doc, cons, elem);

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(elem.getFirstChild());
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            
            transformer.transform(source, result);

            return factory.string(writer.toString());
        }
        catch (ParserConfigurationException | TransformerException e) {
            throw RuntimeExceptionFactory.io(e.getMessage());
        }
    }

     /**
     * Produces a HTML string output from an HTMLElement AST.
     * 
     * Why go through all the trouble of building a DOM? The only reason is compliance.
     * Escapes, encodings, etc. all are maintained by these classes from org.w3c.dom.
     */
    public void writeHTMLFile(ISourceLocation file, IConstructor cons) {
        try (OutputStream out = URIResolverRegistry.getInstance().getOutputStream(file, false)) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // dbf.setSchema(SchemaFactory.newDefaultInstance().newSchema(new URL("http://www.w3.org/1999/xhtml")));
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            
            Element elem = doc.createElement("html");
            createDocument(doc, cons, elem);

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(elem.getFirstChild());
            StreamResult result = new StreamResult(out);
            transformer.transform(source, result);

            return;
        }
        catch (ParserConfigurationException | TransformerException e) {
            throw RuntimeExceptionFactory.io(e.getMessage());
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e.getMessage());
        }
    }
    
    private void createDocument(Document doc, IConstructor cons, Node elem) {
        if (cons.getConstructorType().getArity() == 0) {
            // nullary node does not require recursion
            elem.appendChild(createElement(doc, cons));
        }
        else if (cons.getName().equals("text")) {
            // text nodes are flattened into the element, no recursion required
            elem.appendChild(doc.createTextNode(((IString) cons.get(0)).getValue()));
        }
        else {
            // normal elements require recursion
            Element e = createElement(doc, cons);
            elem.appendChild(e);

            for (IValue child : (IList) cons.get(0)) {
                createDocument(doc, (IConstructor) child, e);
            }
        }
    }

    private Element createElement(Document doc, IConstructor cons) {
        Element elem = doc.createElement(cons.getName());
        Map<String, IValue> params = cons.asWithKeywordParameters().getParameters();
        for (Entry<String,IValue> entry : params.entrySet()) {
            elem.setAttribute(entry.getKey(), ((IString) entry.getValue()).getValue());
        }
        return elem;
    }

    private class Constructor extends ParserCallback {
        private Stack<java.util.List<IConstructor>> stack = new Stack<>();
        private Stack<java.util.Map<java.lang.String,IValue>> attributes = new Stack<>();
        
        public Constructor() {
            stack.push(new ArrayList<>(1));
        }
        
        public IValue getValue() {
            if (stack.isEmpty()) {
                throw RuntimeExceptionFactory.io("HTML has unbalanced brackets");
            }
            return stack.peek().get(0);
        }
        
        @Override
        public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
            stack.push(new ArrayList<>(1));
            storeAttributes(a);
        }
        
        private void storeAttributes(MutableAttributeSet set) {
            attributes.push(new HashMap<java.lang.String,IValue>());
            for (Enumeration<?> names = set.getAttributeNames(); names.hasMoreElements(); ) {				
                Object label = names.nextElement();
                Object value = set.getAttribute(label);
                if (!"_implied_".equals(label)) {
                    attributes.peek().put(label.toString(), factory.string(value.toString()));
                }
            }
        }

        @Override
        public void handleEndTag(Tag t, int pos) {
            java.util.List<IConstructor> kids = stack.pop();
            IValue[] a = new IValue[kids.size()];
            kids.toArray(a);

            try {   
                Type cons = store.lookupConstructor(HTMLElement, t.toString()).iterator().next();
                IConstructor node = cons.getArity() == 1 
                    ? factory.constructor(cons, factory.list(a))
                    : factory.constructor(cons)
                    ;
                node = node.asWithKeywordParameters().setParameters(attributes.pop());
                stack.peek().add(node);
            }
            catch (NoSuchElementException e) {
                Type cons = store.lookupConstructor(HTMLElement, "unknownElement").iterator().next();
                IConstructor node = factory.constructor(cons, a);
                // drop attributes
                
                node = node.asWithKeywordParameters().setParameters(attributes.pop());
                node = node.asWithKeywordParameters().setParameter("unknownTag", factory.string(t.toString()));

                stack.peek().add(node);
            }
        }
        
        @Override
        public void handleSimpleTag(Tag t, MutableAttributeSet a, int pos) {
            storeAttributes(a);
            try {
                Type cons = store.lookupConstructor(HTMLElement, t.toString()).iterator().next();
                IConstructor node = cons.getArity() == 1
                    ? factory.constructor(cons, factory.list())
                    : factory.constructor(cons)
                    ;
                node = node.asWithKeywordParameters().setParameters(attributes.pop());
                stack.peek().add(node);
            }
            catch (NoSuchElementException e) {
                Type cons = store.lookupConstructor(HTMLElement, "unknownElement").iterator().next();
                IConstructor node = factory.constructor(cons);
                
                node = node.asWithKeywordParameters().setParameters(attributes.pop());
                node = node.asWithKeywordParameters().setParameter("tag", factory.string(t.toString()));
                stack.peek().add(node);
            }
        }

        @Override
        public void handleText(char[] data, int pos) {
            Type cons = store.lookupConstructor(HTMLElement, "text").iterator().next();
            IConstructor node = factory.constructor(cons, factory.string(new java.lang.String(data)));           
            stack.peek().add(node);
        }
    }
}
