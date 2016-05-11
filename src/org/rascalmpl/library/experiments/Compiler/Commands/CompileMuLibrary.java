package org.rascalmpl.library.experiments.Compiler.Commands;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.lang.rascal.boot.Kernel;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class CompileMuLibrary {

	/**
	 * Main function for compile command: rascalc
	 * 
	 * @param args	list of command-line arguments
	 */
	public static void main(String[] args) {
		
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		CommandOptions cmdOpts = new CommandOptions("compileMuLibrary");
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
			.boolOption("help") 		.help("Print help message for this command")
			.boolOption("trace") 		.help("Print Rascal functions during execution of compiler")
			.boolOption("profile") 		.help("Profile execution of compiler")
			//.boolOption("jvm") 			.help("Generate JVM code")
			.boolOption("verbose") 		.help("Make the compiler verbose")
						
			.handleArgs(args);
		
		RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(ValueFactoryFactory.getValueFactory())
				.customSearchPath(cmdOpts.getPathConfig().getRascalSearchPath())
				.setTrace(cmdOpts.getCommandBoolOption("trace"))
                .setProfile(cmdOpts.getCommandBoolOption("profile"))
                //.setJVM(cmdOpts.getCommandBoolOption("jvm"))
                .build();
		
		Kernel kernel = new Kernel(vf, rex);
		
		kernel.compileMuLibrary(
				cmdOpts.getCommandPathOption("srcPath"),
				cmdOpts.getCommandPathOption("libPath"),
				cmdOpts.getCommandLocOption("bootDir"),
				cmdOpts.getCommandLocOption("binDir"), 
				cmdOpts.getModuleOptionsAsIMap());
	}
}
