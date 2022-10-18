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
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Stack;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIResolverRegistry;

import io.usethesource.vallang.IConstructor;
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
