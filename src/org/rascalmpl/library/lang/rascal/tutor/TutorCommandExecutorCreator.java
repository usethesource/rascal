package org.rascalmpl.library.lang.rascal.tutor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class TutorCommandExecutorCreator {
    private final IRascalValueFactory vf;
    private final Type promptType;
    private final Type resetType;
    private final Type evalType;
    private final Type stdoutType;
    private final Type stderrType;
    private final Type htmlType;
    private final Type execConstructor;

    public TutorCommandExecutorCreator(IRascalValueFactory vf, TypeFactory tf, TypeStore ts) {
        this.vf = vf;
        promptType = tf.functionType(tf.stringType(), tf.tupleEmpty(), tf.tupleEmpty());
        resetType  = tf.functionType(tf.voidType(), tf.tupleEmpty(), tf.tupleEmpty());
        evalType = tf.functionType(tf.stringType(), tf.tupleType(tf.sourceLocationType(), tf.stringType()), tf.tupleEmpty());
        stdoutType = promptType;
        stderrType = stdoutType;
        htmlType = promptType;
        execConstructor = ts.lookupConstructor(ts.lookupAbstractDataType("CommandExecutor"), "executor").iterator().next();
    }
    
    public IConstructor createExecutor(IConstructor cons) {
        try {
            PathConfig pcfg = new PathConfig(cons);
            TutorCommandExecutor repl = new TutorCommandExecutor(pcfg);
            return vf.constructor(execConstructor,
                cons,
                prompt(repl),
                reset(repl),
                eval(repl),
                stdout(repl),
                stderr(repl),
                html(repl)
            );
        }
        catch (IOException | URISyntaxException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()));
        }
    }

    IFunction prompt(TutorCommandExecutor exec) {
        return vf.function(promptType, (args,kwargs) -> {
            return vf.string(exec.getPrompt());
        });
    }

    IFunction stdout(TutorCommandExecutor exec) {
        return vf.function(stdoutType, (args, kwargs) -> {
            try {
                return vf.string(exec.getPrintedOutput());
            }
            catch (UnsupportedEncodingException e) {
                throw RuntimeExceptionFactory.io(vf.string(e.getMessage()));
            }
        });
    }

    IFunction stderr(TutorCommandExecutor exec) {
        return vf.function(stderrType, (args, kwargs) -> {
            return vf.string(exec.getErrorOutput());
        });
    }

    IFunction html(TutorCommandExecutor exec) {
        return vf.function(htmlType, (args, kwargs) -> {
            return vf.string(exec.getHTMLOutput());
        });
    }

    IFunction reset(TutorCommandExecutor exec) {
        return vf.function(resetType, (args, kwargs) -> {
            exec.reset();
            return null;
        });
    }

    IFunction eval(TutorCommandExecutor exec) {
        return vf.function(evalType, (args, kwargs) -> {
            ISourceLocation cwd = (ISourceLocation) args[0];
            IString command = (IString) args[1];
            return vf.string(exec.eval(cwd.getPath(), command.getValue()));
        });
    }
}
