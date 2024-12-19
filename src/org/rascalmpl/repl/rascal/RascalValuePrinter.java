package org.rascalmpl.repl.rascal;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Function;

import org.rascalmpl.interpreter.result.IRascalResult;
import org.rascalmpl.repl.http.REPLContentServer;
import org.rascalmpl.repl.http.REPLContentServerManager;
import org.rascalmpl.repl.output.IAnsiCommandOutput;
import org.rascalmpl.repl.output.ICommandOutput;
import org.rascalmpl.repl.output.IErrorCommandOutput;
import org.rascalmpl.repl.output.IHtmlCommandOutput;
import org.rascalmpl.repl.output.IOutputPrinter;
import org.rascalmpl.repl.output.IWebContentOutput;
import org.rascalmpl.repl.output.MimeTypes;
import org.rascalmpl.repl.output.impl.StringOutputPrinter;
import org.rascalmpl.repl.streams.LimitedLineWriter;
import org.rascalmpl.repl.streams.LimitedWriter;
import org.rascalmpl.repl.streams.ReplTextWriter;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.io.StandardTextWriter;
import io.usethesource.vallang.type.Type;

/**
 * Printing Rascal values to different outputs.
 */
public abstract class RascalValuePrinter {

    private final static int LINE_LIMIT = 200;
    private final static int CHAR_LIMIT = LINE_LIMIT * 20;

    private final REPLContentServerManager contentManager = new REPLContentServerManager();
    private static final StandardTextWriter ansiIndentedPrinter = new ReplTextWriter(true);
    private static final StandardTextWriter plainIndentedPrinter = new StandardTextWriter(true);

    /**
     * Make a generic closure out of a rascal IFunction (might need to wrap the call with a lock on the evaluator)
     */
    protected abstract Function<IValue, IValue> liftProviderFunction(IFunction func);

    @FunctionalInterface
    public static interface ThrowingWriter {
        void write(PrintWriter writer, StandardTextWriter prettyPrinter) throws IOException;
    }

    public IErrorCommandOutput outputError(ThrowingWriter writer) {
        return new IErrorCommandOutput() {
            @Override
            public ICommandOutput getError() {
                return new DoubleOutput(writer);
            }

            @Override
            public IOutputPrinter asPlain() {
                return new ParameterizedPrinterOutput(writer, plainIndentedPrinter, MimeTypes.PLAIN_TEXT);
            }
        };
    }

    public ICommandOutput outputResult(IRascalResult result) {
        if (result == null || result.getValue() == null) {
            return () -> new StringOutputPrinter("ok", MimeTypes.PLAIN_TEXT);
        }
        IValue value = result.getValue();
        Type type = result.getStaticType();

        if (type.isSubtypeOf(RascalValueFactory.Content) && !type.isBottom()) {
            return serveContent((IConstructor)value);
        }

        ThrowingWriter resultWriter;
        if (type.isAbstractData() && type.isStrictSubtypeOf(RascalValueFactory.Tree) && !type.isBottom()) {
            resultWriter = (w, sw) -> {
                w.write("(" + type.toString() +") `");
                TreeAdapter.yield((IConstructor)value, sw == ansiIndentedPrinter, w);
                w.write("`");
            };
        }
        else if (type.isString()) {
            resultWriter = (w, sw) -> {
                // TODO: do something special for the reader version of IString, when that is released
                // for now, we only support write

                try (Writer wrt = new LimitedWriter(new LimitedLineWriter(w, LINE_LIMIT), CHAR_LIMIT)) {
                    sw.write(value, wrt);
                }
                catch (/*IOLimitReachedException*/ RuntimeException e) {
                    // ignore since this is what we wanted
                    // if we catch IOLimitReachedException we get an IllegalArgument exception instead
                    // "Self-suppression not permitted"
                }
                w.println();
                w.println("---");
                try (Writer wrt = new LimitedWriter(new LimitedLineWriter(w, LINE_LIMIT), CHAR_LIMIT)) {
                    ((IString) value).write(wrt);
                }
                catch (/*IOLimitReachedException*/ RuntimeException e) {
                    // ignore since this is what we wanted
                    // if we catch IOLimitReachedException we get an IllegalArgument exception instead
                    // "Self-suppression not permitted"
                }
                w.println();
                w.print("---");
            };
        }
        else {
            resultWriter = (w, sw) -> {
                try (Writer wrt = new LimitedWriter(new LimitedLineWriter(w, LINE_LIMIT), CHAR_LIMIT)) {
                    sw.write(value, wrt);
                }
                catch (/*IOLimitReachedException*/ RuntimeException e) {
                    // ignore since this is what we wanted
                    // if we catch IOLimitReachedException we get an IllegalArgument exception instead
                    // "Self-suppression not permitted"
                }
            };
        }

        ThrowingWriter typePrefixed = (w, sw) -> {
            w.write(type.toString());
            w.write(": ");
            resultWriter.write(w, sw);
            w.println();
        };

        return new DoubleOutput(typePrefixed);
    }

    private static class DoubleOutput implements IAnsiCommandOutput {
        private ThrowingWriter writer;
        
        DoubleOutput(ThrowingWriter writer) {
            this.writer = writer;
        }

        @Override
        public IOutputPrinter asAnsi() {
            return new ParameterizedPrinterOutput(writer, ansiIndentedPrinter, MimeTypes.ANSI);
        }

        @Override
        public IOutputPrinter asPlain() {
            return new ParameterizedPrinterOutput(writer, plainIndentedPrinter, MimeTypes.PLAIN_TEXT);
        }
    }

    private ICommandOutput serveContent(IConstructor provider) {
        String id;
        Function<IValue, IValue> target;
        
        if (provider.has("id")) {
            id = ((IString) provider.get("id")).getValue();
            target = liftProviderFunction(((IFunction) provider.get("callback")));
        }
        else {
            id = "*static content*";
            target = (r) -> provider.get("response");
        }

        try {
            // this installs the provider such that subsequent requests are handled.
            REPLContentServer server = contentManager.addServer(id, target);

            // now we need some HTML to show
            
            IWithKeywordParameters<? extends IConstructor> kp = provider.asWithKeywordParameters();
            String title = kp.hasParameter("title") ? ((IString) kp.getParameter("title")).getValue() : id;
            int viewColumn = kp.hasParameter("viewColumn") ? ((IInteger)kp.getParameter("viewColumn")).intValue() : 1;
            URI serverUri = new URI("http", null, "localhost", server.getListeningPort(), "/", null, null);

            return new HostedWebContentOutput(id, serverUri, title, viewColumn);

        }
        catch (IOException e) {
            return outputError((w, sw) -> {
                w.println("Could not start webserver to render html content: ");
                w.println(e.getMessage());
            });
        }
        catch (URISyntaxException e) {
            return outputError((w, sw) -> {
                w.println("Could not start build the uri: ");
                w.println(e.getMessage());
            });
        }
    }

    private static class HostedWebContentOutput implements IWebContentOutput, IHtmlCommandOutput {
        private final String id;
        private final URI uri;
        private final String title;
        private final int viewColumn;

        HostedWebContentOutput(String id, URI uri, String title, int viewColumn) {
            this.id = id;
            this.uri = uri;
            this.title = title;
            this.viewColumn = viewColumn;
        }

        @Override
        public IOutputPrinter asPlain() {
            return new IOutputPrinter() {
                @Override
                public void write(PrintWriter target) {
                    target.print("Serving \'");
                    target.print(id);
                    target.print("\' at |");
                    target.print(uri.toASCIIString());
                    target.println("|");
                }
                @Override
                public String mimeType() {
                    return MimeTypes.PLAIN_TEXT;
                }
            };
        }

        @Override
        public IOutputPrinter asHtml() {
            return new IOutputPrinter() {
                @Override
                public void write(PrintWriter target) {
                    target.print("<iframe class=\"rascal-content-frame\"");
                    target.print(" style=\"display: block; width: 100%; height: 100%; resize: both\"");
                    target.print(" src=\"");
                    target.print(uri);
                    target.println("\"></iframe>");
                }

                @Override
                public String mimeType() {
                    return MimeTypes.HTML;
                }
            };
        }

        @Override
        public URI webUri() {
            return uri;
        }

        @Override
        public String webTitle() {
            return title;
        }

        @Override
        public int webviewColumn() {
            return viewColumn;
        }
    }
    

    private static class ParameterizedPrinterOutput implements IOutputPrinter {
        private final ThrowingWriter internalWriter;
        private final StandardTextWriter prettyPrinter;
        private final String mimeType;

        public ParameterizedPrinterOutput(ThrowingWriter internalWriter, StandardTextWriter prettyPrinter, String mimeType) {
            this.internalWriter = internalWriter;
            this.prettyPrinter = prettyPrinter;
            this.mimeType = mimeType;
        }

        @Override
        public String mimeType() {
            return mimeType;
        }

        @Override
        public void write(PrintWriter target) {
            try {
                internalWriter.write(target, prettyPrinter);
            }
            catch (IOException e) {
                target.println("Internal failure: printing exception failed with:");
                target.println(e.toString());
                e.printStackTrace(target);
            }
        }
    }

}
