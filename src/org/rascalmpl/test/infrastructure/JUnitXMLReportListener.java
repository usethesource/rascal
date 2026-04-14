package org.rascalmpl.test.infrastructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.rascalmpl.interpreter.ITestResultListener;

import io.usethesource.vallang.ISourceLocation;

public class JUnitXMLReportListener implements ITestResultListener {
    private XMLStreamWriter out;
    private final File folder;
    private final Map<String, File> modules;
    private String current = null;

    public JUnitXMLReportListener(File outputFolder, Map<String, File> modules) {
        this.folder = new File(outputFolder, "surefire-reports");
        this.modules = modules;
    }

    @Override
    public void start(String context, int count) {
        try {
            current = context;
            out = XMLOutputFactory.newDefaultFactory().createXMLStreamWriter(new FileOutputStream(targetXML(context)));
            out.writeStartDocument();
            out.writeStartElement("testsuite");    
        }
        catch (FileNotFoundException | XMLStreamException e) {
            System.err.println("unexpected failure during test reporting");
            throw new RuntimeException(e);   
        }
    }

    private File targetXML(String context) {
        return new File(folder, context.replaceAll("::", File.separator).concat(".xml"));
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
        }
        catch (XMLStreamException e) {
            System.err.println("unexpected error during test reporting");
            throw new RuntimeException(e);
        }
    }
}
