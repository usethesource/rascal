package org.rascalmpl.library.lang.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jdom2.JDOMConstants;
import org.jdom2.JDOMException;
import org.jdom2.input.sax.XMLReaders;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class IO {

    private final IValueFactory vf;

    public IO(IValueFactory vf) {
        this.vf = vf;
    }
    
    public INode readXML(ISourceLocation loc, IBool trim, IBool fullyQualify) {
        try (Reader content = URIResolverRegistry.getInstance().getCharacterReader(loc)) {
            return toNode(content, trim.getValue(), fullyQualify.getValue());
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
        }
    }


    public INode readXML(IString content, IBool trim, IBool fullyQualify) {
        try (Reader contentReader = new StringReader(content.getValue())) {
            return toNode(contentReader, trim.getValue(), fullyQualify.getValue());
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
        }
    }

    private INode toNode(Reader characterReader, boolean trim, boolean fullyQualify) throws IOException {
        try {
            XMLReader reader = XMLReaders.NONVALIDATING.createXMLReader();
            // XML reader reads the DTD to findout what it is reading at the moment.
            // this can mean if will download the full XHTML DTD with nested DTD's
            // everytime you try to parse a XHTML. (takes about 60 seconds for this to finish)
            // Or in general any other dtd that is hosted on the internet.
            // The following "hack" always returns an empty stream for any DTD requested.
            // Which works for most cases.
            reader.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String pid, String sid) throws SAXException {
                    return new InputSource(new ByteArrayInputStream(new byte[] {}));
                }
            });
            reader.setContentHandler(new ParseToRascalNode(trim, fullyQualify));
            addExtraContentHandlers(reader);
            reader.parse(new InputSource(characterReader));
            return ((ParseToRascalNode)reader.getContentHandler()).getResult();
        }
        catch (JDOMException|SAXException e) {
            throw new IOException(e);
        }
    }

    private void addExtraContentHandlers(XMLReader reader) {
        try {
            reader.setProperty(JDOMConstants.SAX_PROPERTY_LEXICAL_HANDLER, reader.getContentHandler());
        } catch (final SAXNotSupportedException|SAXNotRecognizedException e) {
        }
        try {
            reader.setProperty(JDOMConstants.SAX_PROPERTY_LEXICAL_HANDLER_ALT, reader.getContentHandler());
        } catch (final SAXNotSupportedException|SAXNotRecognizedException e) {
        }
    }

    private final class ParseToRascalNode implements ContentHandler {
        private final Stack<List<IValue>> stack = new Stack<>();
        private IMapWriter seenNamespaces = null; 
		private final Stack<Map<String,IValue>> attributes = new Stack<>();
        private final boolean trim;
        private final boolean fullyQualify;
		
        private INode result = null;

        public ParseToRascalNode(boolean trim, boolean fullyQualify) {
            this.trim = trim;
            this.fullyQualify = fullyQualify;
        }

        public INode getResult() {
            return result;
        }

        @Override
        public void startDocument() throws SAXException {
            assert stack.isEmpty();
            stack.push(new ArrayList<>(1));
        }

        @Override
        public void endDocument() throws SAXException {
            result = (INode) stack.pop().get(0);
        }

        private final Map<String, IValue> emptyMap = new HashMap<>(0);
        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            stack.push(new ArrayList<>());
            Map<String, IValue> newAttrs = emptyMap;
            if (atts.getLength() > 0 || seenNamespaces != null) {
                newAttrs = new HashMap<>(atts.getLength());
                for (int a = 0; a < atts.getLength(); a++) {
                    newAttrs.put(fullyQualify ? atts.getQName(a) : atts.getLocalName(a), vf.string(atts.getValue(a)));
                }
                if (seenNamespaces != null) {
                    newAttrs.put("xmlns", seenNamespaces.done());
                    seenNamespaces = null;
                }
            }
            attributes.push(newAttrs);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            IListWriter children = vf.listWriter();
            children.appendAll(stack.pop());
            stack.peek().add(vf.node( fullyQualify ? qName : localName, new IValue[]{ children.done() }, attributes.pop()));
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String currentString = new String(ch, start, length);
            if (trim) {
                currentString = currentString.trim();
                if (currentString.isEmpty()) {
                    return;
                }
            }
            stack.peek().add(vf.string(currentString));
        }


        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {
            if (fullyQualify) {
                if (seenNamespaces == null) {
                    seenNamespaces = vf.mapWriter();
                }
                seenNamespaces.put(vf.string(prefix), vf.string(uri));
            }
        }

        @Override
        public void endPrefixMapping(String prefix) throws SAXException {
        }

        @Override
        public void skippedEntity(String name) throws SAXException {
        }

        @Override
        public void setDocumentLocator(Locator locator) {
        }

        @Override
        public void processingInstruction(String target, String data) throws SAXException {
        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        }
    }
}
