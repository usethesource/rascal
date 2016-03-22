package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.lang.rascal.boot.Kernel;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValueFactory;
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
										
			.locOption("binDir") 		.respectNoDefaults()
										.help("Directory for Rascal binaries")
										
			.boolOption("noLinking")	.help("Do not link compiled modules")
						
			.boolOption("help") 		.help("Print help message for this command")
			
			.boolOption("trackCalls") 	.help("Print Rascal functions during execution of compiler")
			
			.boolOption("profile") 		.help("Profile execution of compiler")
			
			//.boolOption("jvm") 			.help("Generate JVM code")
			
			.boolOption("verbose") 		.help("Make the compiler verbose")
			
			.rascalModule("Module to be compiled")
			
			.handleArgs(args);
		
		RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(ValueFactoryFactory.getValueFactory())
				.customSearchPath(cmdOpts.getPathConfig().getRascalSearchPath())
				.setTrackCalls(cmdOpts.getCommandBoolOption("trackCalls"))
                .setProfiling(cmdOpts.getCommandBoolOption("profile"))
                //.setJVM(cmdOpts.getCommandBoolOption("jvm"))
                .forModule(cmdOpts.getRascalModule().getValue())
                .build();
		
		Kernel kernel = new Kernel(vf, rex);
		
		kernel.compile(
				(IString)cmdOpts.getRascalModule(),
				(IList) cmdOpts.getCommandPathOption("srcPath"),
				(IList)cmdOpts.getCommandPathOption("libPath"),
				(ISourceLocation)cmdOpts.getCommandLocOption("bootDir"),
				(ISourceLocation)cmdOpts.getCommandLocOption("binDir"), 
				cmdOpts.getModuleOptionsAsIMap());
	}
}
