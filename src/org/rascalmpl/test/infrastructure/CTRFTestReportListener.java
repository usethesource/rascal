package org.rascalmpl.test.infrastructure;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import com.google.gson.FormattingStyle;
import com.google.gson.stream.JsonWriter;

import io.usethesource.vallang.ISourceLocation;

/*
 * This generates reports in the language independent CTRF format.
 * See https://ctrf.io/ . This is like surefire reports in XML, but
 * better parametrized and more generic to cover an PL.
 */
public class CTRFTestReportListener implements ITestResultListener {
    private final ISourceLocation folder;
    private String current = null;
    private int tests = 0;
    private int errors = 0;
    private int failures = 0;
    private int ignored = 0;
    private long timestamp = 0L;
    private List<Report> reports = new LinkedList<>();

    public CTRFTestReportListener(ISourceLocation outputFolder) {
        this.folder = URIUtil.getChildLocation(outputFolder, "surefire-reports");
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

        public void write(JsonWriter out) throws IOException {
            out.beginObject();
            out.name("name");
            out.value(test);
            out.name("suite");
            out.beginArray();
            out.value(loc.getPath());
            out.endArray();
            out.name("status");
            out.value(ignored ? "skipped" : (successful ? "passed" : "failed"));
            out.name("filePath");
            out.value(loc.getPath());
            out.name("line");
            out.value(loc.getBeginLine());
            if (!successful && exception != null) {
                out.name("trace");
                if (exception instanceof Throw) {
                    out.value(((Throw) exception).getTrace().toString());
                }
                else {
                    try (StringWriter sw = new StringWriter(); PrintWriter w = new PrintWriter(sw)) {
                        exception.printStackTrace(w);
                        out.value(sw.toString());
                    }
                }
            }

            if (message == null && exception != null) {
                message = exception.getMessage();
            }

            if (!successful && message != null) {
                out.name("message");
                out.value(message);
            }
            
            out.endObject();
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

    private ISourceLocation targetJSON(String context) {
        return URIUtil.getChildLocation(folder, context.replaceAll("::", ".").concat(".json"));
    }

    @Override
    public void report(boolean successful, String test, ISourceLocation loc, String message, Throwable exception) {
        tests++;
        if (exception != null) {
            errors += 1;
        }
        else if (!successful) {
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
        long stop = System.currentTimeMillis();
        ISourceLocation file = targetJSON(current);
        try (JsonWriter out = new JsonWriter(new OutputStreamWriter(URIResolverRegistry.getInstance().getOutputStream(file, false)))) {
            out.setFormattingStyle(FormattingStyle.PRETTY);
            out.beginObject();
            out.name("reportFormat");
            out.value("CTRF");
            out.name("specVersion");
            out.value("1.0.0");
            out.name("timestamp");
            out.value(DateFormat.getInstance().format(Date.from(Instant.ofEpochMilli(timestamp))));
            
            out.name("results");
            out.beginObject();

            out.name("tool");
            out.beginObject();
            out.name("name");
            out.value("Rascal test runner");
            out.name("version");
            out.value(RascalManifest.getRascalVersionNumber());
            out.endObject();

            out.name("summary");
            out.beginObject();
            out.name("tests");
            out.value(tests);
            out.name("pending");
            out.value(0);
            out.name("other");
            out.value(0);
            out.name("passed");
            out.value(tests - ignored - failures - errors);
            out.name("skipped");
            out.value(ignored);
            out.name("failed");
            out.value(failures + errors);
            out.name("start");
            out.value(timestamp);
            out.name("stop");
            out.value(stop);
            out.name("duration");
            out.value(stop - timestamp);
            out.name("suites");
            out.value(1);
            out.endObject();

            out.name("tests");
            out.beginArray();
            for(Report r : reports) {
                r.write(out);
            }
            out.endArray();
            
            out.endObject();
            out.endObject();
        }
        catch (IOException e) {
            System.err.println("unexpected error during test reporting");
            throw new RuntimeException(e);
        }
    }
}
