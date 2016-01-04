package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

public class RascalC {

	/**
	 * Main function for compile command: rascalc
	 * 
	 * @param args	list of command-line arguments
	 */
	public static void main(String[] args) {
		
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		CommandOptions cmdOpts = new CommandOptions();
		cmdOpts
				.dirOption("kernel", 		cmdOpts.getDefaultKernelLocation(), "Rascal Kernel file to be used")
				.pathOption("srcPath", 		cmdOpts.getDefaultStdPath(), 		"Add new source path, use multiple --srcPaths for multiple paths")
				.pathOption("libPath", 		(co) -> vf.list(co.getCommandDirOption("binDir")),
																				"Add new lib paths, use multiple --libPaths for multiple paths")
				.dirOption("bootDir", 		cmdOpts.getDefaultBootLocation(), 	"Rascal boot directory")
				.dirOption("binDir", 		(co) -> co.requiredDir("binDir"),	"Directory for Rascal binaries")
				.boolOption("noLinking",	false, 								"Do not link compiled modules")   
				.boolOption("noDefaults", 	true, 								"Do not use defaults for srcPath, libPath and binDir")
				.boolOption("help", 		false, 								"Print help message for this command")
				.boolOption("trackCalls", 	false, 								"Print Rascal functions during execution of compiler")
				.boolOption("profile", 		false, 								"Profile execution of compiler")
				.boolOption("jvm", 			false, 								"Generate JVM code")
				.boolOption("verbose", 		false, 								"Make the compiler verbose")
				.rascalModule("Module to be compiled")
				.handleArgs("rascalc", args);
		
		RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(ValueFactoryFactory.getValueFactory(), new PrintWriter(System.out, true), new PrintWriter(System.err, true))
				.setTrackCalls(cmdOpts.getCommandBoolOption("trackCalls"))
                .setProfiling(cmdOpts.getCommandBoolOption("profile"))
                .setJVM(cmdOpts.getCommandBoolOption("jvm"))
                .forModule(cmdOpts.getRascalModule().getValue())
                .build();
		
		RVM rvmKernel = null;
		try {
			rvmKernel = RVM.readFromFileAndInitialize(cmdOpts.getDefaultKernelLocation(), rex);
		} catch (Exception e) {
			System.err.println("Cannot initialize kernel: " + e.getMessage());
			System.exit(-1);
		}
		TypeFactory tf = TypeFactory.getInstance();
		Type argType = tf.tupleType(tf.stringType(),
			  	   					tf.listType(tf.sourceLocationType()),
			  	   					tf.listType(tf.sourceLocationType()),
			  	   					tf.sourceLocationType(),
			  	   					tf.sourceLocationType()
				   		);
		Function compileFunction = 
				cmdOpts.getCommandBoolOption("noLinking") ? rvmKernel.getFunction("compile", tf.abstractDataType(new TypeStore(), "RVMModule"), argType)
														  : rvmKernel.getFunction("compileAndLink", tf.abstractDataType(new TypeStore(), "RVMProgram"), argType);
		if(compileFunction == null){
			System.err.println("Cannot find compile function");
			System.exit(-1);;
		}
		
		IValue[] mainWithPostionalArgs = new IValue[] {
				cmdOpts.getRascalModule(),
				cmdOpts.getCommandPathOption("srcPath"),
				cmdOpts.getCommandPathOption("libPath"),
				cmdOpts.getCommandDirOption("bootDir"),
				cmdOpts.getCommandDirOption("binDir"),
		};
		rvmKernel.executeFunction(compileFunction, mainWithPostionalArgs, cmdOpts.getMainOptions());
	}
}
