package org.rascalmpl.shell;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Map;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.repl.streams.StreamUtil;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextWriter;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

/**
 * Runs all the tests in the modules given bij de -modules parameter (and their imported/extend modules)
 * Given a -project parameter, the path configuration will be constructed automatically
 */
public class RascalTest extends AbstractCommandlineTool {
    private static final IRascalValueFactory vf = IRascalValueFactory.getInstance();

    public static void main(String[] args) {
        try {
            RascalShell.setupJavaProcessForREPL();
            
            var term = RascalShell.connectToTerminal();
            var monitor = IRascalMonitor.buildConsoleMonitor(term);
            var err = (monitor instanceof Writer) ?  StreamUtil.generateErrorStream(term, (Writer)monitor) : new PrintWriter(System.err, true);
            var out = (monitor instanceof PrintWriter) ? (PrintWriter) monitor : new PrintWriter(System.out, false);
        
            try {
                var parser = new CommandlineParser(out);
                var parsedArgs = parser.parseKeywordCommandLineArgs("RascalTest", args, parameterTypes());  
                var pcfgCons = (IConstructor) parsedArgs.get("pcfg");
                PathConfig pcfg = pcfgCons != null ? new PathConfig(pcfgCons) : new PathConfig();
                var projectRoot = pcfg.getProjectRoot().getScheme().equals("unknown") ? URIUtil.rootLocation("cwd") : pcfg.getProjectRoot();
                var eval = ShellEvaluatorFactory.getDefaultEvaluatorForPathConfig(projectRoot, pcfg, term.reader(), out, err, monitor);
                
                var modules = listParameter(parsedArgs, "modules");

                var modNames = new LinkedList<String>();
                for (IValue m : modules) {
                    var l = (ISourceLocation) m;
                    for (var src: pcfg.getSrcs()) {
                        var rel = URIUtil.relativize((ISourceLocation) src, l);
                        if (rel.getScheme().equals("relative")) {
                            rel = URIUtil.changeExtension(rel, "");
                            var mod = rel.getPath().substring(1).replaceAll("/", "::");
                            modNames.add(mod);
                        }
                    }
                }

                eval.doImport(monitor, modNames.stream().toArray(String[]::new));

                boolean reporting = vf.bool(true).equals(parsedArgs.get("reporting");

                if (reporting) {
                    eval.setTestResultListener(new JunitXMLReporter());
                }

                if (!eval.runTests(eval.getMonitor())) {
                    System.exit(1);
                }
                else {
                    System.exit(0);
                }
            }
            catch (Throw e) {
                try {
                    err.println(e.getException());
                    e.getTrace().prettyPrintedString(err, new StandardTextWriter());
                }
                catch (IOException ioe) {
                    err.println(ioe.getMessage());
                }

                System.exit(1);
            }
            catch (Throwable e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static Type parameterTypes() {
		var tf = TypeFactory.getInstance();
		var ll = tf.listType(tf.sourceLocationType());
		
		return tf.tupleType(
			PathConfig.PathConfigType, "pcfg",
			ll, "modules"
		);
	}

    private static IList listParameter(Map<String, IValue> args, String arg) {
		return args.get(arg) == null ? vf.list() : (IList) args.get(arg);
	}
}
