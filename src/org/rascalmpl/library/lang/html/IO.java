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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Stack;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIResolverRegistry;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
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
    private final Type textConstructor;

    public IO(IValueFactory factory, TypeStore store) {
        this.factory = factory;
        this.store = store;
        this.HTMLElement = store.lookupAbstractDataType("HTMLElement");
        this.textConstructor = store.lookupConstructor(HTMLElement, "text").iterator().next();
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

    public IValue readHTMLFile(ISourceLocation file, IString encoding) {
        try (InputStream reader = URIResolverRegistry.getInstance().getInputStream(file)) {
            Document doc = Jsoup.parse(reader, encoding.getValue(), file.getURI().toString());
            
            return toConstructorTree(doc);
        } catch (MalformedURLException e) {
            throw RuntimeExceptionFactory.malformedURI(file.getURI().toASCIIString());
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(factory.string(e.getMessage()));
        }
    }

    private IValue toConstructorTree(Document doc) {
        Type cons = store.lookupConstructor(HTMLElement, "html").iterator().next();
        return factory.constructor(cons, toConstructorTree(doc.head()), toConstructorTree(doc.body()));
    }

    private IValue toConstructorTree(Node node) {
        if (node instanceof TextNode) {
            return toTextConstructor((TextNode) node);
        }
        
        Element elem = (Element) node;
        Type cons = store.lookupConstructor(HTMLElement, elem.tagName()).iterator().next();

        Map<String,IValue> kws = new HashMap<>();
        
        for (Attribute a : elem.attributes()) {
            kws.put(a.getKey(), factory.string(a.getValue()));
        }

        IListWriter w = factory.listWriter();
        for (Node n : elem.children()) {
            w.append(toConstructorTree(n));
        }

        return factory.constructor(cons, w.done());
    }

    private IValue toTextConstructor(TextNode elem) {
        return factory.constructor(textConstructor, factory.string(elem.getWholeText()));
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
           
            return factory.string(doc.outerHtml());
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
            out.write(doc.outerHtml().getBytes("UTF-8"));
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e.getMessage());
        }
    }

    /**
     * Translates a constructor tree to a jdom DOM tree, adding nodes where necessary to complete a html element.
     */
    private Document createHTMLDocument(IConstructor cons) throws IOException {
        Document doc = new Document("http://localhost");
        doc.appendChild(createElement(cons));
        return doc;
    }

    private Node createElement(IConstructor cons) {
        if (cons.getConstructorType().getArity() == 0) {
            // nullary nodes do not require recursion
            return emptyElementWithAttributes(cons);
        } 
        else if (cons.getName().equals("text")) {
            // text nodes are flattened into the parent element, no recursion required
            return new TextNode(((IString) cons.get(0)).getValue());
        }
        else {
            // normal elements require recursion
            Element elem = emptyElementWithAttributes(cons);
      
            for (IValue child : (IList) cons.get(0)) {
                elem.appendChild(createElement((IConstructor) child));
            }

            return elem;
        }
    }

    private Element emptyElementWithAttributes(IConstructor cons) {
        Element elem = new Element(cons.getName());

        for (Entry<String, IValue> e : cons.asWithKeywordParameters().getParameters().entrySet()) {
            elem = elem.attr(e.getKey(), ((IString) e.getValue()).getValue());
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
                    // _implied_ is used as a dummy attribute by the EditorKit HTML parser
                    // that we are using here. It leaks a bit and so we ignore it here.
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
                System.out.println("cons: " + cons);
                IConstructor node = cons.getArity() == 1 
                    ? factory.constructor(cons, factory.list(a))
                    : factory.constructor(cons)
                    ;
                node = node.asWithKeywordParameters().setParameters(attributes.pop());
                System.out.println("node: " + node);
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
