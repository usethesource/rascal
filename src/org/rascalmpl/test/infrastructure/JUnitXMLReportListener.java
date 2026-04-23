package org.rascalmpl.test.infrastructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.ISourceLocation;

public class JUnitXMLReportListener implements ITestResultListener {
    private final ISourceLocation folder;
    private final Map<String, ISourceLocation> modules;
    private String current = null;
    private int tests = 0;
    private int errors = 0;
    private int failures = 0;
    private int ignored = 0;
    private long timestamp = 0L;
    private List<Report> reports = new LinkedList<>();

    public JUnitXMLReportListener(ISourceLocation outputFolder, Map<String, ISourceLocation> modules) {
        this.folder = URIUtil.getChildLocation(outputFolder, "surefire-reports");
        this.modules = modules;
    }

    private class Report {
        public boolean ignored;
        public boolean successful;
        public String test;
        public ISourceLocation loc;
        public String message;
        public Throwable exception;

        public Report(boolean ignored, boolean successful, String test, ISourceLocation loc, String message, Throwable exception) {
            this.ignored = ignored;
            this.successful = successful;
            this.test = test;
            this.loc = loc;
            this.message = message;
            this.exception = exception;
        }

        public void write(XMLStreamWriter out) throws XMLStreamException {
            out.writeStartElement("testcase");
            out.writeAttribute("classname", current);
            out.writeAttribute("name", test);
            out.writeAttribute("file", modules.get(current).toString());
            
            if (ignored) {
                out.writeEmptyElement("skipped");
                out.writeStartElement("system-out");
                out.writeCharacters("location: " + loc);
                out.writeEndElement(); // system-out
            }
            else {
                if (!successful) {
                    out.writeStartElement(exception != null ? "error" : "failure" );
                    out.writeAttribute("message", message);
                    out.writeEndElement(); // error or failure
                }

                out.writeStartElement("system-out");
                out.writeCharacters("location: " + loc);
                if (exception != null) {
                    StringWriter sw = new StringWriter();
                    exception.printStackTrace(new PrintWriter(sw));
                    out.writeCharacters("stacktrace:\n" + sw.toString());
                }
                out.writeEndElement(); // system-out
            }

            out.writeEndElement(); // testcase
        }
    }

    @Override
    public void start(String module, int count) {
        tests = 0;
        errors = 0;
        failures = 0;
        ignored = 0;
        timestamp = System.currentTimeMillis();
        reports = new LinkedList<>();
        current = module;  
    }

    private ISourceLocation targetXML(String context) {
        return URIUtil.getChildLocation(folder, context.replaceAll("::", ".").concat(".xml"));
    }

    @Override
    public void report(boolean successful, String test, ISourceLocation loc, String message, Throwable exception) {
        tests++;
        if (exception != null) {
            errors += 1;
        }
        else {
            failures += 1;
        }
        reports.add(new Report(false, successful, test, loc, message, exception));
    }

    @Override
    public void ignored(String test, ISourceLocation loc) {
        tests++;
        ignored++;
        reports.add(new Report(true, false, test, loc, "", null));
    }

    @Override
    public void done() {
        try {
            var xmlFile = targetXML(current);

            var out = XMLOutputFactory.newDefaultFactory()
                .createXMLStreamWriter(URIResolverRegistry.getInstance().getOutputStream(xmlFile, false));
            out.writeStartDocument();
            
            out.writeStartElement("testsuite");

            out.writeAttribute("time", Long.toString(System.currentTimeMillis() - timestamp));
            out.writeAttribute("timestamp", Long.toString(timestamp));
            out.writeAttribute("ignored", Integer.toString(ignored));
            out.writeAttribute("errors", Integer.toString(errors));
            out.writeAttribute("tests", Integer.toString(tests));
            out.writeAttribute("failures", Integer.toString(failures));

            for(Report r : reports) {
                r.write(out);
            }

            out.writeEndElement();

            out.writeEndDocument();
            out.flush();
            out.close();
        }
        catch (XMLStreamException | IOException e) {
            System.err.println("unexpected error during test reporting");
            throw new RuntimeException(e);
        }
    }
}
