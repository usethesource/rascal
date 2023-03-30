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
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Range;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIResolverRegistry;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
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
    private final Type dataConstructor;
    private final Type htmlConstructor;

    public IO(IValueFactory factory, TypeStore store) {
        this.factory = factory;
        this.store = store;
        this.HTMLElement = store.lookupAbstractDataType("HTMLElement");
        this.textConstructor = store.lookupConstructor(HTMLElement, "text").iterator().next();
        this.dataConstructor = store.lookupConstructor(HTMLElement, "data").iterator().next();
        this.htmlConstructor = store.lookupConstructor(HTMLElement, "html").iterator().next();
    }
    
    public IValue readHTMLString(IString string, ISourceLocation base, IBool trackOrigins, IBool includeEndTags, ISourceLocation src) {
        if (string.length() == 0) {
            throw RuntimeExceptionFactory.io("empty HTML document");
        }

        Parser htmlParser = Parser.htmlParser()
                .settings(new ParseSettings(false, false))
                .setTrackPosition(trackOrigins.getValue())
                ;
             
        Document doc = Jsoup.parse(string.getValue(), base.getURI().toString(), htmlParser);
            
        return toConstructorTree(doc, trackOrigins.getValue() ? src : null, includeEndTags.getValue());        
    }

    public IValue readHTMLFile(ISourceLocation file, ISourceLocation base, IBool trackOrigins, IBool includeEndTags) {
        try (InputStream reader = URIResolverRegistry.getInstance().getInputStream(file)) {
            Parser htmlParser = Parser.htmlParser()
                .settings(new ParseSettings(false, false))
                .setTrackPosition(trackOrigins.getValue())
                ;
            
            Document doc = Jsoup.parse(reader, "UTF-8", base.getURI().toString(), htmlParser);
            
            return toConstructorTree(doc, trackOrigins.getValue() ? file : null, includeEndTags.getValue());
        } catch (MalformedURLException e) {
            throw RuntimeExceptionFactory.malformedURI(file.getURI().toASCIIString());
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(factory.string(e.getMessage()));
        }
    }

    private IValue toConstructorTree(Document doc, ISourceLocation file, boolean includeEndTags) {
        IConstructor result = factory.constructor(htmlConstructor, 
                    factory.list(
                        toConstructorTree(doc.head(), file, includeEndTags), 
                        toConstructorTree(doc.body(), file, includeEndTags)
                    )
                );

        if (file != null) {
            return result.asWithKeywordParameters().setParameter("src", file);
        }

        return result;
    }

    private IValue toConstructorTree(Node node, ISourceLocation file, boolean includeEndTags) {
        if (node instanceof TextNode) {
            return toTextConstructor((TextNode) node, file);
        }
        else if (node instanceof DataNode) {
            return toDataConstructor((DataNode) node, file);
        }
        else {
            assert node instanceof Element;
        }
        
        Element elem = (Element) node;
        Type cons = store.lookupConstructor(HTMLElement, elem.tagName()).iterator().next();

        Map<String,IValue> kws = new HashMap<>();
        
        for (Attribute a : elem.attributes()) {
            kws.put(a.getKey(), factory.string(a.getValue()));
        }

        IListWriter w = factory.listWriter();
        for (Node n : elem.childNodes()) {
            w.append(toConstructorTree(n, file, includeEndTags));
        }

        IConstructor result = cons.getArity() > 0
            ? factory.constructor(cons, new IValue[] { w.done() }, kws)
            : factory.constructor(cons, new IValue[0], kws);
        
        if (file != null) {
            ISourceLocation src = nodeToLoc(elem.sourceRange(), elem.endSourceRange(), file, includeEndTags);
            return result.asWithKeywordParameters().setParameter("src", src);
        } else {
            return result;
        }
    }

    private ISourceLocation nodeToLoc(Range r, Range e, ISourceLocation file, boolean includeEndTags) {
        if (r.start().pos() < 0) {
            return file;
        }

        return includeEndTags
            ? factory.sourceLocation(file,
                r.start().pos(),
                e.end().pos() - r.start().pos(),
                r.start().lineNumber(),
                e.end().lineNumber(),
                r.start().columnNumber() - 1,
                e.end().columnNumber() - 1
            )
            : factory.sourceLocation(file, 
                r.start().pos(), 
                r.end().pos() - r.start().pos(),
                r.start().lineNumber(),
                r.end().lineNumber(),
                r.start().columnNumber() - 1,
                r.end().columnNumber() - 1
                );
    }

    private IValue toDataConstructor(DataNode node, ISourceLocation file) {
        IConstructor cons = factory.constructor(dataConstructor, factory.string(node.getWholeData()));
        return file != null 
            ? cons.asWithKeywordParameters().setParameter("src", nodeToLoc(node.sourceRange(), node.sourceRange(), file, false))
            : cons;
    }

    private IValue toTextConstructor(TextNode elem, ISourceLocation file) {
        IConstructor cons = factory.constructor(textConstructor, factory.string(elem.getWholeText()));
        return file != null 
            ? cons.asWithKeywordParameters().setParameter("src", nodeToLoc(elem.sourceRange(), elem.sourceRange(), file, false))
            : cons;
    }

    /**
     * Produces a HTML string output from an HTMLElement AST.
     * 
     * Why go through all the trouble of building a DOM? The only reason is compliance.
     * Escapes, encodings, etc. all are maintained by these classes from org.jdom.
     */
    public IString writeHTMLString(IConstructor cons, IString charset, IConstructor escapeMode, IBool outline, IBool prettyPrint, IInteger indentAmount, IInteger maxPaddingWidth, IConstructor syntax ) {
        try {
            Document doc = createHTMLDocument(cons);
            doc = doc.outputSettings(createOutputSettings(charset.getValue(), escapeMode.getName(), outline.getValue(), prettyPrint.getValue(), indentAmount.intValue(), maxPaddingWidth.intValue(), syntax.getName()));
            
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
    public void writeHTMLFile(ISourceLocation file, IConstructor cons, IString charset, IConstructor escapeMode, IBool outline, IBool prettyPrint, IInteger indentAmount, IInteger maxPaddingWidth, IConstructor syntax ) {
        
        try (Writer out = URIResolverRegistry.getInstance().getCharacterWriter(file, charset.getValue(), false)) {
            Document doc = createHTMLDocument(cons);
            doc = doc.outputSettings(createOutputSettings(charset.getValue(), escapeMode.getName(), outline.getValue(), prettyPrint.getValue(), indentAmount.intValue(), maxPaddingWidth.intValue(), syntax.getName()));
            out.write(doc.outerHtml());
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e.getMessage());
        }
    }

    private OutputSettings createOutputSettings(String charset, String escapeMode, boolean outline, boolean prettyPrint, int indentAmount,
        int maxPaddingWidth, String syntax) {
        return new OutputSettings()
            .charset(charset)
            .escapeMode(EscapeMode.valueOf(escapeMode.replaceAll("Mode", "")))
            .outline(outline)
            .prettyPrint(prettyPrint)
            .indentAmount(indentAmount)
            .maxPaddingWidth(maxPaddingWidth)
            .syntax(Syntax.valueOf(syntax.replaceAll("Syntax", "")));
    }

    /**
     * Translates a constructor tree to a jdom DOM tree, adding nodes where necessary to complete a html element.
     */
    private Document createHTMLDocument(IConstructor cons) throws IOException {
        Document doc = new Document("http://localhost");

        Node node = normalise(cons, createElement(cons));
        
        doc.appendChild(node);
        return doc.normalise();
    }

    private Node normalise(IConstructor cons, Node elem) {
        switch (cons.getName()) {
            case "html":
                return elem;
            case "head":
                return new Element("html").appendChild(elem);
            case "body":
                return new Element("html").appendChild(elem);   
            default:
                return new Element("html").appendChild(new Element("body").appendChild(elem));
        }
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
        else if (cons.getName().equals("data")) {
            // data nodes are flattened into the parent element, no recursion required
            return new DataNode(((IString) cons.get(0)).getValue());
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
}
