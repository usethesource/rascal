package org.rascalmpl.library.experiments.Compiler.Examples;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.KWParams;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class SampleFuns {
	private final ISampleFuns sampleFuns;

	private interface ISampleFuns {
	    IConstructor d1(int n);
	    IConstructor d1(String s);
	    IConstructor d2(String s);
	    int fun1(int n, Map<String, IValue> kwparams);
	    double fun1(double d);
	    float fun1(float f);
	    int fun1(IList l);
	    int fun1(IList l, int n);
	}
	
	public SampleFuns (IValueFactory vf, PathConfig pcfg) throws IOException {
	    RascalExecutionContext rex = 
            RascalExecutionContextBuilder.normalContext(vf, pcfg.getboot(), System.out, System.err)
                .setJVM(true)                   // options for complete repl
                .setTrace(false)
                .setDebugRVM(false)
                .build();
        ISourceLocation binDir = pcfg.getBin();
	    RVMCore rvm = ExecutionTools.initializedRVM(URIUtil.correctLocation("compressed+" + binDir.getScheme(), "", binDir.getPath() + "/experiments/Compiler/Examples/SampleFuns.rvm.ser.gz"), rex);

	    this.sampleFuns = rvm.asInterface(ISampleFuns.class);
	}
    
    public static void main(String[] args) throws IOException, URISyntaxException {
      IValueFactory vals = ValueFactoryFactory.getValueFactory();
      IList lst1 = vals.list(vals.string("a"), vals.string("b"));
      IList lst2 = vals.list(vals.integer(1), vals.integer(2), vals.integer(3));
      ISampleFuns sampleFuns = new SampleFuns(vals, new PathConfig()).sampleFuns;
      System.out.println(sampleFuns.fun1(lst1));
      System.out.println(sampleFuns.fun1(lst2));
      System.out.println(sampleFuns.fun1(5, new KWParams(vals).add("delta",  3).build()));
    }
}
