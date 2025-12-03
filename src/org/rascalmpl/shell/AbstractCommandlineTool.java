package org.rascalmpl.shell;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.jline.terminal.Terminal;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.file.MavenRepositoryURIResolver;
import org.rascalmpl.uri.jar.JarURIResolver;

import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextWriter;

/**
 * A reusable setup for any commandline tool in the Rascal project that runs a Rascal `main` function.
 */
public abstract class AbstractCommandlineTool {

    /**
     * This method should be called by a `public static void main(String[] args)` method directly.
     * It parses the commandline parametres according to the signature of the provided main function name (and module).
     * 
     * @param mainModule     which main module must be imported to begin
     * @param sourceFolders  where to find Rascal source modules to load into the interpreter
     * @param args           the String[] args of the calling static main method
     */
    public static int main(String mainModule, String[] sourceFolders, String[] args, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) {
        try {   
            var eval = ShellEvaluatorFactory.getBasicEvaluator(term.reader(), out, err, monitor);
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
            
            if (result == null) {
                // void main
                return 0;
            }
            else if (result.getType().isInteger()) {
                return ((IInteger) result).intValue();
            }
            else {
                return 0;
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

            return 1;
        }
        catch (Throwable e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * A main for which the commandine parameters have already been parsed.
     */
    public static int main(String mainModule, String[] sourceFolders, Map<String, IValue> args, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) {
        try {   
            var eval = ShellEvaluatorFactory.getBasicEvaluator(term.reader(), out, err, monitor);
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
            
            if (result == null) {
                // void main
                return 0;
            }
            else if (result.getType().isInteger()) {
                return ((IInteger) result).intValue();
            }
            else {
                return 0;
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

            return 1;
        }
        catch (Throwable e) {
            e.printStackTrace();
            return 1;
        }
    }
}
