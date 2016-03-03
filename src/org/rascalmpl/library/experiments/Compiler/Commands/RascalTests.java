package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMInterpreter;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class RascalTests {

	/**
	 * Main function for compile command: rascalc
	 * 
	 * @param args	list of command-line arguments
	 */
	public static void main(String[] args) {
		
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		CommandOptions cmdOpts = new CommandOptions("rascalTests");
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
			
			.boolOption("help") 		.help("Print help message for this command")
			
			.boolOption("trackCalls")	.help("Print Rascal functions during execution of compiler")
			
			.boolOption("profile")		.help("Profile execution of compiler")
			
			.boolOption("jvm")			.help("Generate JVM code")
			
			.boolOption("verbose")		.help("Make the compiler verbose")
			
			.rascalModules("Rascal modules with tests")
			
			.handleArgs(args);
		
		RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(ValueFactoryFactory.getValueFactory(), new PrintWriter(System.out, true), new PrintWriter(System.err, true))
				.customSearchPath(cmdOpts.getPathConfig().getRascalSearchPath())
				.setTrackCalls(cmdOpts.getCommandBoolOption("trackCalls"))
                .setProfiling(cmdOpts.getCommandBoolOption("profile"))
                .setJVM(cmdOpts.getCommandBoolOption("jvm"))
                .forModule(cmdOpts.getRascalModule().getValue())
                .build();
		
		RVMCore rvmKernel = null;
		try {
			rvmKernel = RVMCore.readFromFileAndInitialize(cmdOpts.getKernelLocation(), rex);
		} catch (Exception e) {
			System.err.println("Cannot initialize kernel: " + e.getMessage());
			System.exit(-1);
		}
		TypeFactory tf = TypeFactory.getInstance();
		Type argType = tf.tupleType(tf.listType(tf.stringType()),
			  	   					tf.listType(tf.sourceLocationType()),
			  	   					tf.listType(tf.sourceLocationType()),
			  	   					tf.sourceLocationType(),
			  	   					tf.sourceLocationType()
				   		);
		Function rascalTestsFunction = 
				rvmKernel.getFunction("rascalTests", tf.valueType(), argType);
		if(rascalTestsFunction == null){
			System.err.println("Cannot find compile function");
			System.exit(-1);;
		}
		
		IValue[] mainWithPostionalArgs = new IValue[] {
				cmdOpts.getRascalModules(),
				cmdOpts.getCommandPathOption("srcPath"),
				cmdOpts.getCommandPathOption("libPath"),
				cmdOpts.getCommandLocOption("bootDir"),
				cmdOpts.getCommandLocOption("binDir"),
		};
		rvmKernel.executeRVMFunction(rascalTestsFunction, mainWithPostionalArgs, cmdOpts.getModuleOptions());
	}
}
