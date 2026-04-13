package org.rascalmpl.shell;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.repl.streams.StreamUtil;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextWriter;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

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
                var eval = ShellEvaluatorFactory.getDefaultEvaluator(term.reader(), out, err, monitor);
                var parser = new CommandlineParser(out);
			    var kwParams = parameterTypes();
			    var parsedArgs = parser.parseKeywordCommandLineArgs("RascalTest", args, kwParams);
                
                var modules = listParameter(parsedArgs, "modules");

                for (IValue m : modules) {
                    eval.doImport(monitor, ((IString) m).getValue());
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
