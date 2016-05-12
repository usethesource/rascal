package org.rascalmpl.library.experiments.Compiler.Examples;

import java.io.IOException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class Fac {
	private final IValueFactory vf;
	private final IFac facProgram;

	private interface IFac {
	    IInteger fac(IInteger n);
	}
	
	public Fac (IValueFactory vf) throws IOException {
	    this.vf = vf;
	    RVMCore rvm = ExecutionTools.initializedRVM(URIUtil.correctLocation("std", "", "experiments/Compiler/Examples/Fac.rvm.ser.gz"));
        this.facProgram = rvm.asInterface(IFac.class);
	}

    public int fac(int n) {
        return facProgram.fac(vf.integer(n)).intValue(); 
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println(new Fac(ValueFactoryFactory.getValueFactory()).fac(6));
    }
}
