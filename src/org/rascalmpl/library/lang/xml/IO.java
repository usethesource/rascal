package org.rascalmpl.library.lang.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.jsoup.Jsoup;
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
import io.usethesource.vallang.IDateTime;
import io.usethesource.vallang.IExternalValue;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.IRational;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.visitors.IValueVisitor;

public class IO {
    private final IValueFactory vf;

    public IO(IValueFactory vf) {
        this.vf = vf;
    }
    
    public IValue readXML(ISourceLocation loc, IBool trackOrigins, IString charset, IBool inferCharset) {
        try {
            if (inferCharset.getValue()) {
                charset = vf.string(URIResolverRegistry.getInstance().getCharset(loc).toString());
            }
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()));
        }

        try (InputStream reader = URIResolverRegistry.getInstance().getInputStream(loc)) {
            Parser htmlParser = Parser.htmlParser()
                .settings(new ParseSettings(false, false))
                .setTrackPosition(trackOrigins.getValue())
                ;
            
            Document doc = Jsoup.parse(reader, charset.getValue(), loc.getURI().toString(), htmlParser);
            
            return toINode(doc, trackOrigins.getValue() ? loc : null);
        } catch (MalformedURLException e) {
            throw RuntimeExceptionFactory.malformedURI(loc.getURI().toASCIIString());
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()));
        }
    }


    public IValue readXML(IString string, ISourceLocation src, IBool trim, IBool fullyQualify, IBool trackOrigins) {
        if (string.length() == 0) {
            throw RuntimeExceptionFactory.io("empty XML document");
        }

        Parser xmlParser = Parser.xmlParser()
                .settings(new ParseSettings(false, false))
                .setTrackPosition(trackOrigins.getValue())
                ;
             
        Document doc = Jsoup.parse(string.getValue(), src.getURI().toString(), xmlParser);
            
        return toINode(doc, trackOrigins.getValue() ? src : null);        
    }

    public IValue readXMLFile(ISourceLocation file, ISourceLocation base, IBool trackOrigins) {
        try (InputStream reader = URIResolverRegistry.getInstance().getInputStream(file)) {
            Parser xmlParser = Parser.xmlParser()
                .settings(new ParseSettings(false, false))
                .setTrackPosition(trackOrigins.getValue())
                ;
            
            Document doc = Jsoup.parse(reader, "UTF-8", base.getURI().toString(), xmlParser);
            
            return toINode(doc, trackOrigins.getValue() ? file : null);
        } catch (MalformedURLException e) {
            throw RuntimeExceptionFactory.malformedURI(file.getURI().toASCIIString());
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()));
        }
    }

    private IValue toINode(Document doc, ISourceLocation file) {
        return toINode(doc.body(), file);
    }

    private IValue toINode(Node node, ISourceLocation file) {
        if (node instanceof TextNode) {
            return toIString((TextNode) node, file);
        }
        else if (node instanceof DataNode) {
            return toIString((DataNode) node, file);
        }
        else {
            assert node instanceof Element;
        }
        
        Element elem = (Element) node;

        Map<String,IValue> kws = 
            StreamSupport.stream(elem.attributes().spliterator(), false)
                .collect(Collectors.toMap(a -> a.getKey(), a -> vf.string(a.getValue())));
        
        IValue[] args = elem.childNodes().stream()
            .map(n -> toINode(n, file))
            .toArray(IValue[]::new);

        if (file != null) {
            kws.put("src", nodeToLoc(node, file));
        }

        return vf.node(node.nodeName(), args).asWithKeywordParameters().setParameters(kws);
    }

    private ISourceLocation nodeToLoc(Node node, ISourceLocation file) {
        Range r = node.sourceRange();
        if (r.start().pos() < 0) {
            return file;
        }
        
        ISourceLocation src = vf.sourceLocation(file, 
            r.start().pos(), 
            r.end().pos() - r.start().pos(),
            r.start().lineNumber(),
            r.end().lineNumber(),
            r.start().columnNumber(),
            r.end().columnNumber()
            );
        return src;
    }

    private IValue toIString(DataNode node, ISourceLocation file) {
        return vf.string(node.getWholeData());
    }

    private IValue toIString(TextNode elem, ISourceLocation file) {
        return vf.string(elem.getWholeText());
    }

    /**
     * Produces a XML string output from an arbitrary Rascal value
     * 
     * Why go through all the trouble of building a DOM? The only reason is compliance.
     * Escapes, encodings, etc. all are maintained by these classes from jsoup
     */
    public IString writeXMLString(IValue cons, IString charset, IBool outline, IBool prettyPrint, IInteger indentAmount, IInteger maxPaddingWidth) {
        try {
            Document doc = createXMLDocument(cons);
            doc = doc.outputSettings(createOutputSettings(charset.getValue(), outline.getValue(), prettyPrint.getValue(), indentAmount.intValue(), maxPaddingWidth.intValue()));
            
            return vf.string(doc.outerHtml());
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e.getMessage());
        }
    }

    /**
     * Produces a XML string output from an node value or algebraic data-type value.
     * 
     * Why go through all the trouble of building a DOM? The only reason is compliance.
     * Escapes, encodings, etc. all are maintained by these classes from JSoup
     */
    public void writeXMLFile(ISourceLocation file, IValue cons, IString charset, IBool outline, IBool prettyPrint, IInteger indentAmount, IInteger maxPaddingWidth) {
        
        try (Writer out = URIResolverRegistry.getInstance().getCharacterWriter(file, charset.getValue(), false)) {
            Document doc = createXMLDocument(cons);
            doc = doc.outputSettings(createOutputSettings(charset.getValue(), outline.getValue(), prettyPrint.getValue(), indentAmount.intValue(), maxPaddingWidth.intValue()));
            out.write(doc.outerHtml());
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e.getMessage());
        }
    }

    private OutputSettings createOutputSettings(String charset, boolean outline, boolean prettyPrint, int indentAmount, int maxPaddingWidth) {
        return new OutputSettings()
            .charset(charset)
            .escapeMode(EscapeMode.base)
            .outline(outline)
            .prettyPrint(prettyPrint)
            .indentAmount(indentAmount)
            .maxPaddingWidth(maxPaddingWidth)
            .syntax(Syntax.xml);
    }

    /**
     * Translates a constructor tree to a xml DOM tree
     */
    private Document createXMLDocument(IValue cons) throws IOException {
        Document doc = new Document("http://localhost");

        Node node = cons.accept(new ElementCreator());
        
        return (Document) doc.appendChild(node);
    }

    private static class ElementCreator implements IValueVisitor<Node, RuntimeException> {

        @Override
        public Node visitString(IString o) throws RuntimeException {
            return new TextNode(o.getValue());
        }

        @Override
        public Node visitReal(IReal o) throws RuntimeException {
            Element real = new Element("real");
            real.attr("val", o.toString());
            return real;
        }

        @Override
        public Node visitRational(IRational o) throws RuntimeException {
            Element rat = new Element("rat");
            rat.attr("numerator", o.numerator().toString());
            rat.attr("denominator", o.denominator().toString());
            return rat;
        }

        @Override
        public Node visitList(IList o) throws RuntimeException {
            Element list = new Element("list");
            o.stream().forEach(e -> list.appendChild(e.accept(this)));
            return list;
        }

        @Override
        public Node visitSet(ISet o) throws RuntimeException {
            Element list = new Element("set");
            o.stream().forEach(e -> list.appendChild(e.accept(this)));
            return list;
        }

        @Override
        public Node visitSourceLocation(ISourceLocation o) throws RuntimeException {
            Element loc = new Element("loc");

            loc.attr("uri", o.getURI().toString());

            if (o.hasOffsetLength()) {
                loc.attr("offset", Integer.toString(o.getOffset()));
                loc.attr("length", Integer.toString(o.getLength()));
            }

            if (o.hasLineColumn()) {
                loc.attr("beginLine", Integer.toString(o.getBeginLine()));
                loc.attr("endLine", Integer.toString(o.getEndLine()));
                loc.attr("beginColumn", Integer.toString(o.getBeginColumn()));
                loc.attr("endColumn", Integer.toString(o.getEndColumn()));
            }

            return loc;
        }

        @Override
        public Node visitTuple(ITuple o) throws RuntimeException {
            Element tuple = new Element("tuple");
            StreamSupport.stream(o.spliterator(), false)
                .forEach(e -> tuple.appendChild(e.accept(this)));
            return tuple;
        }

        @Override
        public Node visitNode(INode o) throws RuntimeException {
            Element node = new Element(o.getName());

            o.asWithKeywordParameters().getParameters().entrySet().stream()
                .forEach(e -> node.attr(e.getKey(), e.getValue().toString()));

            StreamSupport.stream(o.spliterator(), false)
                .forEach(e -> node.appendChild(e.accept(this)));

            return node;
        }

        @Override
        public Node visitConstructor(IConstructor o) throws RuntimeException {
            Element cons = new Element(o.getName());

            o.asWithKeywordParameters().getParameters().entrySet().stream()
                .forEach(e -> cons.attr(e.getKey(), e.getValue().toString()));

            StreamSupport.stream(o.spliterator(), false)
                .forEach(e -> cons.appendChild(e.accept(this)));
                
            return cons;
        }

        @Override
        public Node visitInteger(IInteger o) throws RuntimeException {
            Element integer = new Element("integer");
            integer.attr("val", o.toString());
            return integer;
        }

        @Override
        public Node visitMap(IMap o) throws RuntimeException {
            Element map = new Element("map");

            for (Entry<IValue, IValue> e : ((Iterable<Entry<IValue,IValue>>) () -> o.entryIterator())) {
                map.appendChild(
                    new Element("entry")
                        .appendChild(e.getKey().accept(this))
                        .appendChild(e.getValue().accept(this))
                );
            }
            
            return map;
        }

        @Override
        public Node visitBoolean(IBool boolValue) throws RuntimeException {
            Element bool = new Element("bool");
            bool.attr("val", boolValue.toString());
            return bool;
        }

        @Override
        public Node visitExternal(IExternalValue externalValue) throws RuntimeException {
            return new TextNode(externalValue.toString());
        }

        @Override
        public Node visitDateTime(IDateTime o) throws RuntimeException {
            Element datetime = new Element("datetime");
            datetime.attr("val", o.toString());
            return datetime;
        }
    }
}
