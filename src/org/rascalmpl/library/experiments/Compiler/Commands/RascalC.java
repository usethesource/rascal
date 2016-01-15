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
		CommandOptions cmdOpts = new CommandOptions("rascalc");
		cmdOpts
			.pathOption("srcPath")		.pathDefault(cmdOpts.getDefaultStdPath().isEmpty() ? vf.list(cmdOpts.getDefaultStdPath()) : cmdOpts.getDefaultStdPath())
										.respectNoDefaults()
										.help("Add (absolute!) source path, use multiple --srcPaths for multiple paths")
										
			.pathOption("libPath")		.pathDefault((co) -> vf.list(co.getCommandLocOption("binDir")))
										.respectNoDefaults()
										.help("Add new lib path, use multiple --libPaths for multiple paths")
										
			.locOption("bootDir")		.locDefault(cmdOpts.getDefaultBootLocation())
										.help("Rascal boot directory")
										
			.locOption("binDir") 		.help("Directory for Rascal binaries")
				 
			.boolOption("noLinking")	.intDefault(10).help("Do not link compiled modules")
			
			.boolOption("noDefaults") 	.help("Do not use defaults for srcPath, libPath and binDir")
			
			.boolOption("help") 		.help("Print help message for this command")
			
			.boolOption("trackCalls") 	.help("Print Rascal functions during execution of compiler")
			
			.boolOption("profile") 		.help("Profile execution of compiler")
			
			.boolOption("jvm") 			.help("Generate JVM code")
			
			.boolOption("verbose") 		.help("Make the compiler verbose")
			
			.rascalModule("Module to be compiled")
			
			.handleArgs(args);
		
		RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(ValueFactoryFactory.getValueFactory(), new PrintWriter(System.out, true), new PrintWriter(System.err, true))
				.customSearchPath(cmdOpts.getPathConfig().getRascalSearchPath())
				.setTrackCalls(cmdOpts.getCommandBoolOption("trackCalls"))
                .setProfiling(cmdOpts.getCommandBoolOption("profile"))
                .setJVM(cmdOpts.getCommandBoolOption("jvm"))
                .forModule(cmdOpts.getRascalModule().getValue())
                .build();
		
		RVM rvmKernel = null;
		try {
			rvmKernel = RVM.readFromFileAndInitialize(cmdOpts.getKernelLocation(), rex);
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
				cmdOpts.getCommandLocOption("bootDir"),
				cmdOpts.getCommandLocOption("binDir"),
		};
		rvmKernel.executeFunction(compileFunction, mainWithPostionalArgs, cmdOpts.getModuleOptions());
	}
}
