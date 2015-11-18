package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class Command {
	
    public static void main(String[] args, String programName, Type returnType) {
	    boolean verbose = false;
	    for (String a : args) {
	        verbose |= a.equals("--verbose");
	    }
		
	    IValueFactory vf = ValueFactoryFactory.getValueFactory();
	    ISourceLocation kernelBinaryLocation = null;
	    try {
	        if (args.length > 1 && args[0].equals("--kernel") && args[1].endsWith(".gz")) {
	            String bootPath = args[1];
	            if (new File(bootPath).isAbsolute()) {
	                kernelBinaryLocation = vf.sourceLocation("compressed+file","", bootPath);
	            }
	            else {
	                kernelBinaryLocation = vf.sourceLocation("compressed+cwd","", "/" + bootPath);
	            }
	            args = Arrays.copyOfRange(args, 2, args.length);
	        }
	        else {
	            kernelBinaryLocation = vf.sourceLocation("compressed+boot", "", "Kernel.rvm.ser.gz");
	        }
	    }
	    catch(URISyntaxException e) {
			System.err.println("Cannot construct path too kernel: " + e.getMessage());
			System.exit(-1);
	    }
		
		RascalExecutionContext rex = new RascalExecutionContext("rascal", vf, System.out, System.err);

		if (verbose) {
		    System.err.println("Loading rvm kernel");
		}

		RVM rvmKernel = null;
		try {
		    rvmKernel = RVM.readFromFileAndInitialize(kernelBinaryLocation, rex);
		} catch (Exception e) {
			System.err.println("Cannot initialize kernel: " + e.getMessage());
			System.exit(-1);
		}
		
		if (verbose) {
		    System.err.println("starting rascal");
		}
		TypeFactory tf = TypeFactory.getInstance();
		Function programToRun = rvmKernel.getFunction(programName, returnType, tf.tupleType(tf.stringType()));

		if (programToRun == null) {
			System.err.println("Cannot find rascal in kernel");
			System.exit(-1);
		}
		
		String argsAsOne = "";
		for (String a : args) {
		    if (!argsAsOne.isEmpty()) {
		        argsAsOne += " ";
		    }
		    argsAsOne += a;
		}
		try {
		    Object result  = rvmKernel.executeFunction(programToRun, new IValue[]{ vf.string(argsAsOne) }, new HashMap<>());
		    System.out.println("Result: " + result.toString());
		    System.exit(0);
		}
		catch (Exception e) {
			System.err.println("rascal failed: " + e.getMessage());
			System.exit(-1);
		}
	}
}
