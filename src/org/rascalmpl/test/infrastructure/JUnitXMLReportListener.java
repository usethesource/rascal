package org.rascalmpl.test.infrastructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.ISourceLocation;

public class JUnitXMLReportListener implements ITestResultListener {
    private XMLStreamWriter out;
    private final ISourceLocation folder;
    private final Map<String, ISourceLocation> modules;
    private String current = null;

    public JUnitXMLReportListener(ISourceLocation outputFolder, Map<String, ISourceLocation> modules) {
        this.folder = URIUtil.getChildLocation(outputFolder, "surefire-reports");
        this.modules = modules;
    }

    @Override
    public void start(String context, int count) {
        try {
            current = context;  
            var xmlFile = targetXML(context);

            out = XMLOutputFactory.newDefaultFactory()
                .createXMLStreamWriter(URIResolverRegistry.getInstance().getOutputStream(xmlFile, false));
            out.writeStartDocument();
            out.writeStartElement("testsuite");    
        }
        catch (IOException | XMLStreamException e) {
            System.err.println("unexpected failure during test reporting");
            throw new RuntimeException(e);   
        }
    }

    private ISourceLocation targetXML(String context) {
        return URIUtil.getChildLocation(folder, context.replaceAll("::", ".").concat(".xml"));
    }

    @Override
    public void report(boolean successful, String test, ISourceLocation loc, String message, Throwable exception) {
        try {
            startTestCase(test);
            if (!successful) {
                out.writeStartElement("failure");
                    out.writeAttribute("message", message);
                    if (exception != null) {
                        StringWriter sw = new StringWriter();
                        exception.printStackTrace(new PrintWriter(sw));
                        out.writeCharacters(sw.toString());
                    }
                out.writeEndElement();
                out.writeStartElement("system-out");
                    out.writeCharacters("location: " + loc);
                out.writeEndElement();
            }
            out.writeEndElement();
        }
        catch (XMLStreamException e) {
            System.err.println("unexpected error during test reporting");
            throw new RuntimeException(e);
        }
    }

    private void startTestCase(String test) throws XMLStreamException {
        out.writeStartElement("testcase");
        out.writeAttribute("classname", current);
        out.writeAttribute("name", test);
        out.writeAttribute("file", modules.get(current).toString());
    }

    @Override
    public void ignored(String test, ISourceLocation loc) {
        try {
            startTestCase(test);
            out.writeEmptyElement("skipped");
            out.writeStartElement("system-out");
                out.writeCharacters("location: " + loc);
            out.writeEndElement();

            out.writeEndElement();
        }
        catch (XMLStreamException e) {
            System.err.println("unexpected error during test reporting");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void done() {
        try {
            out.writeEndElement();
            out.writeEndDocument();
            out.flush();
            out.close();
        }
        catch (XMLStreamException e) {
            System.err.println("unexpected error during test reporting");
            throw new RuntimeException(e);
        }
    }
}
