package org.rascalmpl.tutor.lang.rascal.tutor.repl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.repl.StopREPLException;
import org.rascalmpl.repl.output.IBinaryOutputPrinter;
import org.rascalmpl.repl.output.IErrorCommandOutput;
import org.rascalmpl.repl.output.IImageCommandOutput;
import org.rascalmpl.repl.output.IWebContentOutput;
import org.rascalmpl.repl.rascal.RascalInterpreterREPL;
import org.rascalmpl.shell.ShellEvaluatorFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.SourceLocationClassLoader;
import org.rascalmpl.uri.project.ProjectURIResolver;
import org.rascalmpl.uri.project.TargetURIResolver;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextWriter;

public class TutorCommandExecutor {
    private final RascalInterpreterREPL interpreter;
    private final StringWriter outWriter = new StringWriter();
    private final PrintWriter outPrinter = new PrintWriter(outWriter);
    private final StringWriter errWriter = new StringWriter();
    private final PrintWriter errPrinter = new PrintWriter(errWriter, true);
    private final ITutorScreenshotFeature screenshot;

    public TutorCommandExecutor(PathConfig pcfg) throws IOException, URISyntaxException{
        interpreter = new RascalInterpreterREPL() {
            @Override
            protected Evaluator buildEvaluator(Reader input, PrintWriter stdout, PrintWriter stderr, IDEServices services) {
                var eval = super.buildEvaluator(input, stdout, stderr, services);

                if (!pcfg.getSrcs().isEmpty()) {
                    ISourceLocation projectRoot = inferProjectRoot((ISourceLocation) pcfg.getSrcs().get(0));
                    String projectName = new RascalManifest().getProjectName(projectRoot);
                    URIResolverRegistry reg = URIResolverRegistry.getInstance();
                    reg.registerLogical(new ProjectURIResolver(projectRoot, projectName));
                    reg.registerLogical(new TargetURIResolver(projectRoot, projectName));

                    for (IValue path : pcfg.getSrcs()) {
                        eval.addRascalSearchPath((ISourceLocation) path); 
                    }
        
                    for (IValue path : pcfg.getLibs()) {
                        eval.addRascalSearchPath((ISourceLocation) path);
                    }
        
                    ClassLoader cl = new SourceLocationClassLoader(pcfg.getLibsAndTarget(), ShellEvaluatorFactory.class.getClassLoader());
                    eval.addClassLoader(cl);
                }
                else {
                    services.warning("No src path configured for tutor", URIUtil.rootLocation("unknown"));
                }

                return eval;
            }

            @Override
            protected IDEServices buildIDEService(PrintWriter err, IRascalMonitor monitor, Terminal term) {
                return (monitor instanceof IDEServices) ? (IDEServices)monitor : new TutorIDEServices(err);
            }
        };

        var terminal = TerminalBuilder.builder()
            .system(false)
            .streams(InputStream.nullInputStream(), OutputStream.nullOutputStream())
            .dumb(true)
            .color(false)
            .encoding(StandardCharsets.UTF_8)
            .build();

        interpreter.initialize(Reader.nullReader(), outPrinter, errPrinter, new TutorIDEServices(errPrinter), terminal);
        screenshot = loadScreenShotter();
    }

    private ITutorScreenshotFeature loadScreenShotter() {
        try {
            return (ITutorScreenshotFeature) getClass()
                .getClassLoader()
                .loadClass("org.rascalmpl.tutor.Screenshotter")
                .getDeclaredConstructor()
                .newInstance();
        }
        catch (ClassNotFoundException e) {
            // that is normal; we just don't have the feature available.
            return null;
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException  e) {
            throw new Error("WARNING: Could not load screenshot feature from org.rascalmpl.tutor.Screenshotter", e);
        }
    }

    private static ISourceLocation inferProjectRoot(ISourceLocation member) {
        ISourceLocation current = member;
        URIResolverRegistry reg = URIResolverRegistry.getInstance();
        while (current != null && reg.exists(current) && reg.isDirectory(current)) {
            if (reg.exists(URIUtil.getChildLocation(current, "META-INF/RASCAL.MF"))) {
                return current;
            }

            if (URIUtil.getParentLocation(current).equals(current)) {
                // we went all the way up to the root
                return reg.isDirectory(member) ? member : URIUtil.getParentLocation(member);
            }
            
            current = URIUtil.getParentLocation(current);
        }

        return current;
    }

    public void reset() {
        interpreter.cancelRunningCommandRequested();
        interpreter.cleanEnvironment();
        outPrinter.flush();
        outWriter.getBuffer().setLength(0);
        errPrinter.flush();
        errWriter.getBuffer().setLength(0);
    }
    
    public Map<String, String> eval(String line) throws InterruptedException, IOException {
        Map<String, String> result = new HashMap<>();
        try {
            var replResult = interpreter.handleInput(line);
            if (replResult instanceof IErrorCommandOutput) {
                ((IErrorCommandOutput)replResult).asPlain().write(errPrinter, true);
            }
            else if (replResult instanceof IImageCommandOutput) {
                var img = ((IImageCommandOutput)replResult).asImage();
                result.put(img.mimeType(), uuencode(img));
            }
            else if (replResult instanceof IWebContentOutput && screenshot != null) {
                var webResult = (IWebContentOutput)replResult;
                try {
                    String pngImage = screenshot.takeScreenshotAsBase64PNG(webResult.webUri().toASCIIString());

                    if (!pngImage.isEmpty()) {
                        result.put("application/rascal+screenshot", pngImage);
                    }
                }
                catch (Throwable e) {
                    errPrinter.write(e.getMessage());
                }
            }
            // we ignore IAnsiCommandOutput, as we know that we cannot render that. 
            // else if (replResult instanceof IAnsiCommandOutput) {}
            else if (replResult != null) {
                var txt = new StringWriter();
                var txtPrinter = new PrintWriter(txt, false);
                replResult.asPlain().write(txtPrinter, true);
                txtPrinter.flush();
                result.put("text/plain", txt.toString());
            }
            else {
                result.put("text/plain", "ok\n");
            }
        } catch (ParseError pe) {
            ReadEvalPrintDialogMessages.parseErrorMessage(errPrinter, line, interpreter.promptRootLocation().getScheme(), pe, new StandardTextWriter(true)); 
        }
        catch (StopREPLException e1) {
            errWriter.write("Quiting REPL");
        } finally {
            result.put("application/rascal+stdout", getPrintedOutput());
            result.put("application/rascal+stderr", getErrorOutput());
        }
        return result;
    }

    private String uuencode(IBinaryOutputPrinter content)  throws IOException {
        var result = new ByteArrayOutputStream();
        try (var wrapped = Base64.getEncoder().wrap(result)) {
            content.write(wrapped);
        }
        return result.toString(StandardCharsets.ISO_8859_1); // help java recognize the compact strings can be used
    }

    private String getPrintedOutput(){
        outPrinter.flush();
        String result = outWriter.toString();
        outWriter.getBuffer().setLength(0);
        return result;
    }

    private String getErrorOutput() {
        errPrinter.flush();
        String result = errWriter.toString();
        errWriter.getBuffer().setLength(0);
        return result;
    }
}
