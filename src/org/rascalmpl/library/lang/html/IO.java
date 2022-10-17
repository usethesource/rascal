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

    public IO(IValueFactory factory, TypeStore store) {
        this.factory = factory;
        this.store = store;
    }
    
    public IValue readHTMLString(IString string) {
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
            return stack.peek().get(0);
        }
        
        @Override
        public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
            stack.push(new ArrayList<>(1));
            System.out.println("pushing :" + toString());
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
            Type cons = store.lookupConstructor(store.lookupAbstractDataType("HTMLElement"), t.toString()).iterator().next();
            IConstructor node = factory.constructor(cons, factory.list(a));
            node = node.asWithKeywordParameters().setParameters(attributes.pop());
            stack.peek().add(node);
        }
        
        @Override
        public void handleSimpleTag(Tag t, MutableAttributeSet a, int pos) {
            Type cons = store.lookupConstructor(store.lookupAbstractDataType("HTMLElement"), t.toString()).iterator().next();
            IConstructor node = factory.constructor(cons, factory.list());
            System.out.println(cons);
            stack.peek().add(node);
        }
        
        @Override
        public void handleText(char[] data, int pos) {
            Type cons = store.lookupConstructor(store.lookupAbstractDataType("HTMLElement"), "text").iterator().next();
            IConstructor node = factory.constructor(cons, factory.string(new java.lang.String(data)));
           
            stack.peek().add(node);
        }
    }
}
