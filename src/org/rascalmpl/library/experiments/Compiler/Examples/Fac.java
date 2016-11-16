package org.rascalmpl.library.experiments.Compiler.Examples;

import java.io.IOException;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class Fac {
	private final IValueFactory vf;
	private final IFac facProgram;

	private interface IFac {
	    IInteger fac(IInteger n);
	}
	
	public Fac (IValueFactory vf, PathConfig pcfg) throws IOException {
	    this.vf = vf;
	    RascalExecutionContext rex = 
            RascalExecutionContextBuilder.normalContext(vf, pcfg.getBoot() /* TODO needs a kernel location */, System.out, System.err)
                .setJVM(true)                   // options for complete repl
                .setTrace(false)
                .build();
	    ISourceLocation binDir = pcfg.getBin();
	    RVMCore rvm = ExecutionTools.initializedRVM(URIUtil.correctLocation("compressed+" + binDir.getScheme(), "", binDir.getPath() + "/experiments/Compiler/Examples/Fac.rvm.ser.gz"), rex);
        this.facProgram = rvm.asInterface(IFac.class);
	}

    public int fac(int n) {
        return facProgram.fac(vf.integer(n)).intValue(); 
    }
    
    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println(new Fac(ValueFactoryFactory.getValueFactory(), new PathConfig()).fac(6));
    }
}
