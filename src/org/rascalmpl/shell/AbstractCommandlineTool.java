package org.rascalmpl.shell;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.repl.streams.StreamUtil;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.jar.JarURIResolver;

import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextWriter;

/**
 * A reusable setup for any commandline tool in the Rascal project that runs a Rascal `main` function.
 */
public abstract class AbstractCommandlineTool {

    /**
     * This method should be called by a `public static void main(String[] args)` method directly.
     * @param mainModule     which main module must be imported to begin
     * @param sourceFolders  where to find Rascal source modules to load into the interpreter
     * @param args           the String[] args of the calling static main method
     */
    public static void main(String mainModule, String[] sourceFolders, String[] args) {
        try {
            RascalShell.setupJavaProcessForREPL();
            
            var term = RascalShell.connectToTerminal();
            var monitor = IRascalMonitor.buildConsoleMonitor(term);
            var err = (monitor instanceof Writer) ?  StreamUtil.generateErrorStream(term, (Writer)monitor) : new PrintWriter(System.err, true);
            var out = (monitor instanceof PrintWriter) ? (PrintWriter) monitor : new PrintWriter(System.out, false);
        
            try {   
                var eval = ShellEvaluatorFactory.getDefaultEvaluator(term.reader(), out, err, monitor);
                var rascalJar = JarURIResolver.jarify(PathConfig.resolveCurrentRascalRuntimeJar());

                for (String folder : sourceFolders) {
                    var src = URIUtil.getChildLocation(rascalJar, folder);
                    if (URIResolverRegistry.getInstance().exists(src)) {
                        eval.addRascalSearchPath(src);
                    }
                    else {
                        throw new FileNotFoundException(src.toString());
                    }
                }

                eval.doImport(monitor, mainModule);
                
                IValue result = eval.main(monitor, mainModule, "main", args);
                
                if (result.getType().isInteger()) {
                    System.exit(((IInteger) result).intValue());
                }
                else {
                    System.exit(0);
                }
            }
            catch (Throw e) {
                try {
                    err.println(e.getLocalizedMessage());
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

}
