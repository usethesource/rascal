package org.rascalmpl.library.experiments.Compiler.Examples;

import java.io.IOException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class SampleFuns {
	private final IValueFactory vf;
	private final ISampleFuns sampleFunsProgram;

	private interface ISampleFuns {
	    IConstructor d1(IInteger n);
	    IConstructor d1(IString s);
	    IConstructor d2(IString s);
	    IInteger fun1(IInteger n);
	    IInteger fun1(IList n);
	}
	
	public SampleFuns (IValueFactory vf) throws IOException {
	    this.vf = vf;
	    RVMCore rvm = ExecutionTools.initializedRVM(URIUtil.rootLocation("boot"), URIUtil.correctLocation("std", "","experiments/Compiler/Examples/SampleFuns.rvm.ser.gz"));
        this.sampleFunsProgram = rvm.asInterface(ISampleFuns.class);
	}

    public IConstructor d1(int n) {
        return sampleFunsProgram.d1(vf.integer(n)); 
    }
    
    public IConstructor d1(String s) {
      return sampleFunsProgram.d1(vf.string(s)); 
    }
    
    public IConstructor d2(String s) {
      return sampleFunsProgram.d2(vf.string(s)); 
    }
    
    public int fun1(int n) {
      return sampleFunsProgram.fun1(vf.integer(n)).intValue();
    }
    
    public int fun1(IList lst) {
      return sampleFunsProgram.fun1(lst).intValue();
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println(new SampleFuns(ValueFactoryFactory.getValueFactory()).fun1(6));
    }
}
