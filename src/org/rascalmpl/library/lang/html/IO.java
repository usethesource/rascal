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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIResolverRegistry;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeStore;


public class IO {
    private static final String XHTML_DTD_LOCATION = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
    private static final String XHTML_CONTENT_ID = "-//W3C//DTD XHTML 1.0 Strict//EN";
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
     * Escapes, encodings, etc. all are maintained by these classes from org.jdom.
     */
    public IString writeHTMLString(IConstructor cons) {
        try (StringWriter out = new StringWriter()) {
            Document doc = createHTMLDocument(cons);
           
            new XMLOutputter(Format.getPrettyFormat()).output(doc, out);

            return factory.string(out.toString());
        }
        catch (IOException e) {
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
            Document doc = createHTMLDocument(cons);
            
            new XMLOutputter(Format.getPrettyFormat()).output(doc, out);
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e.getMessage());
        }
    }

    /**
     * Translates a constructor tree to a jdom DOM tree, adding nodes where necessary to complete a html element.
     */
    private Document createHTMLDocument(IConstructor cons) throws IOException {
        return new Document(completeDocument(createElement(cons)),
                new DocType("html", XHTML_CONTENT_ID, XHTML_DTD_LOCATION));
    }
    
    /**
     * If this is not a complete HTML document, then we add nodes to
     * wrap it until it is.
     * @param createElement
     * @return
     */
    private Element completeDocument(Content content) throws IOException {
        if (content instanceof Element) {
            Element elem = (Element) content;
            
            switch (elem.getName()) {
                case "html":
                    return elem;
                case "body":
                    return new Element("html").addContent(elem);
                case "head":
                    return new Element("html").addContent(elem);
                case "meta":
                    return completeDocument(new Element("head").addContent(elem));
                default:
                    return completeDocument(new Element("body").addContent(elem));
            } 
        }
        else if (content instanceof Text) {
            return completeDocument(new Element("p").addContent(content));
        }
        else {
            throw new IOException("unknown element encountered");
        }
    }

    private Content createElement(IConstructor cons) {
        if (cons.getConstructorType().getArity() == 0) {
            // nullary nodes do not require recursion
            return emptyElementWithAttributes(cons);
        } 
        else if (cons.getName().equals("text")) {
            // text nodes are flattened into the parent element, no recursion required
            return new Text(((IString) cons.get(0)).getValue());
        }
        else {
            // normal elements require recursion
            Element e = new Element(cons.getName()).setAttributes(createAttributes(cons.asWithKeywordParameters().getParameters()));

            for (IValue child : (IList) cons.get(0)) {
                e.addContent(createElement((IConstructor) child));
            }

            return e;
        }
    }

    private Element emptyElementWithAttributes(IConstructor cons) {
        return new Element(cons.getName()).setAttributes(createAttributes(cons.asWithKeywordParameters().getParameters()));
    }

    private Collection<? extends Attribute> createAttributes(Map<String, IValue> parameters) {
        Set<Attribute> attributes = new HashSet<>();
        
        for (Entry<String,IValue> entry : parameters.entrySet()) {
            attributes.add(new Attribute(entry.getKey(), ((IString) entry.getValue()).getValue()));
        }

        return attributes;
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
