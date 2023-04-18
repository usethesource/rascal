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
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
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
import io.usethesource.vallang.IMapWriter;
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
    
    public IValue readXML(ISourceLocation loc, IBool fullyQualify, IBool trackOrigins, IBool includeEndTags,  IBool ignoreComments, IBool ignoreWhitespace, IString charset, IBool inferCharset) {
        try {
            if (inferCharset.getValue()) {
                charset = vf.string(URIResolverRegistry.getInstance().getCharset(loc).toString());
            }
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()));
        }

        try (InputStream reader = URIResolverRegistry.getInstance().getInputStream(loc)) {
            Parser xmlParser = Parser.xmlParser()
                .settings(new ParseSettings(false, false))
                .setTrackPosition(trackOrigins.getValue())
                ;
            
            Document doc = Jsoup.parse(reader, charset.getValue(), loc.getURI().toString(), xmlParser);
            
            return toINode(doc, trackOrigins.getValue() ? loc : null, fullyQualify.getValue(), includeEndTags.getValue(), ignoreWhitespace.getValue(), ignoreComments.getValue());
        } catch (MalformedURLException e) {
            throw RuntimeExceptionFactory.malformedURI(loc.getURI().toASCIIString());
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()));
        }
    }


    public IValue readXML(IString string, ISourceLocation src, IBool fullyQualify, IBool trackOrigins, IBool includeEndTags, IBool ignoreComments, IBool ignoreWhitespace) {
        if (string.length() == 0) {
            throw RuntimeExceptionFactory.io("empty XML document");
        }

        Parser xmlParser = Parser.xmlParser()
                .settings(new ParseSettings(false, false))
                .setTrackPosition(trackOrigins.getValue())
                ;
             
        Document doc = Jsoup.parse(string.getValue(), src.getURI().toString(), xmlParser);
            
        return toINode(doc.firstChild(), trackOrigins.getValue() ? src : null, fullyQualify.getValue(),  includeEndTags.getValue(), ignoreWhitespace.getValue(), ignoreComments.getValue());        
    }

    private IValue toINode(Document doc, ISourceLocation file, boolean fullyQualify, boolean includeEndTags, boolean ignoreWhitespace, boolean ignoreComments) {
        return toINode(doc.body(), file, fullyQualify, includeEndTags, ignoreWhitespace, ignoreComments);
    }

    private IValue toINode(Node node, ISourceLocation file, boolean fullyQualify, boolean includeEndTags, boolean ignoreWhitespace, boolean ignoreComments) {
        if (node instanceof TextNode) {
            return toIString((TextNode) node, file);
        }
        else if (node instanceof DataNode) {
            return toIString((DataNode) node, file);
        }
        else if (node instanceof Comment) {
            return vf.node("comment", vf.string(((Comment) node).getData()));
        }
        else if (node instanceof Element) {            
            Element elem = (Element) node;

            IMapWriter namespaces = vf.mapWriter();
            
            Map<String,IValue> kws = 
                StreamSupport.stream(elem.attributes().spliterator(), false)
                    .filter(a -> { 
                        // collect the namespaces in a map
                        if (a.getKey().startsWith("xmlns:")) {
                            namespaces.put(vf.string(a.getKey().substring("xmlns:".length())), vf.string(a.getValue()));
                        } 
                        else if (a.getKey().equals("xmlns")) {
                            namespaces.put(vf.string("xml"), vf.string(a.getValue()));
                        }

                        // remove all the namespace attributes
                        return !a.getKey().startsWith("xmlns");
                    })
                    .map(a -> removeNamespace(a, elem.attributes(), fullyQualify))
                    .collect(Collectors.toMap(a -> normalizeAttr(a.getKey()), a -> vf.string(a.getValue())));
            
            if (fullyQualify) {
                IMap m = namespaces.done();

                if (m.size() > 0) {
                    kws.put("xmlns", m);
                }
            }

            IValue[] args = elem.childNodes().stream()
                .filter(p -> !ignoreComments || !(p instanceof Comment))
                .filter(p -> !ignoreWhitespace || !(p instanceof TextNode && ((TextNode) p).isBlank()))
                .map(n -> toINode(n, file, fullyQualify, includeEndTags, ignoreWhitespace, ignoreComments))
                .toArray(IValue[]::new);

            if (file != null) {
                assert !(kws.containsKey("src") && kws.containsKey("origin"));
                kws.put(kws.containsKey("src") ? "origin" : "src", nodeToLoc((Element) node, file, includeEndTags));
            }

            return vf.node(removeNamespace(node.nodeName(), fullyQualify), args).asWithKeywordParameters().setParameters(kws);
        }
        else {
            throw RuntimeExceptionFactory.illegalArgument(vf.string(node.toString()), vf.string("unexpected kind of XML node: " + node.getClass().getCanonicalName()));
        }
    }

    private static String removeNamespace(String name, boolean fullyQualify) {
        if (fullyQualify) {
            return name;
        }

        int index = name.indexOf(":");

        if (index == -1) {
            return name;
        }

        return name.substring(index+1);

    }
    private static Attribute removeNamespace(Attribute a, Attributes otherAttributes, boolean fullyQualify) {
        if (fullyQualify) {
            return a;
        }
        
        String key = a.getKey();
        int index = key.indexOf(":");

        if (index == -1) {
            return a;
        }

        String newKey = key.substring(index+1);

        if (otherAttributes.hasKey(newKey)) {
            // keep disambiguation if necessary
            return a;
        }

        return new Attribute(newKey, a.getValue());
    }

    private ISourceLocation nodeToLoc(Element node, ISourceLocation file, boolean includeEndTags) {
        Range startRange = node.sourceRange();
        if (!startRange.isTracked()) {
            return file;
        }
        
        Range endRange = node.endSourceRange();

        return includeEndTags && endRange.isTracked()
            ? vf.sourceLocation(file,
                startRange.start().pos(),
                endRange.end().pos() - startRange.start().pos(),
                startRange.start().lineNumber(),
                endRange.end().lineNumber(),
                startRange.start().columnNumber() - 1,
                endRange.end().columnNumber() - 1
            )
            : vf.sourceLocation(file, 
                startRange.start().pos(), 
                startRange.end().pos() - startRange.start().pos(),
                startRange.start().lineNumber(),
                startRange.end().lineNumber(),
                startRange.start().columnNumber() - 1,
                startRange.end().columnNumber() - 1
                );
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
            Element node = new Element(o.getName().replaceAll("-", ":"));

            Map<String, IValue> parameters = o.asWithKeywordParameters().getParameters();

            if (parameters.containsKey("xmlns")) {
                IMap ps = (IMap) parameters.get("xmlns");
                for (IValue e : ps) {
                    IString skey = (IString) e;
                    if (skey.getValue().equals("xml")) {
                        node.attr("xmlns", ((IString) ps.get(skey)).getValue());
                    }
                    else {
                        node.attr("xmlns:" + skey.getValue(), ((IString) ps.get(skey)).getValue());
                    }
                }

                parameters.remove("xmlns");
            }

            parameters.entrySet().stream()
                .forEach(e -> node.attr(deNormalizeAttr(e.getKey()), e.getValue().toString()));

            StreamSupport.stream(o.spliterator(), false)
                .forEach(e -> node.appendChild(e.accept(this)));

            return node;
        }

        @Override
        public Node visitConstructor(IConstructor o) throws RuntimeException {
            return visitNode(o);
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

    private static String normalizeAttr(String attr) {
        return attr.replaceAll(":", "-");
    }

    private static String deNormalizeAttr(String attr) {
        return attr.replaceAll("-", ":");
    }
}
