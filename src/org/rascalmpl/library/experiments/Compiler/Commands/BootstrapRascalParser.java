package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.IOException;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.lang.rascal.boot.Kernel;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class BootstrapRascalParser {

	/**
	 * This command is used by Bootstrap only.
	 *  
	 * @param args	list of command-line arguments
	 * @throws NoSuchRascalFunction 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) {
	    try {
	        IValueFactory vf = ValueFactoryFactory.getValueFactory();
	        CommandOptions cmdOpts = new CommandOptions("generateParser");
	        cmdOpts
	        .pathOption("srcPath")		.pathDefault(cmdOpts.getDefaultStdPath().isEmpty() ? vf.list(cmdOpts.getDefaultStdPath()) : cmdOpts.getDefaultStdPath())
	        .respectNoDefaults()
	        .help("Add (absolute!) source path, use multiple --srcPaths for multiple paths")
	        .locOption("bootDir")		.locDefault(cmdOpts.getDefaultBootLocation())
	        .help("Rascal boot directory")
	        .noModuleArgument()
	        .handleArgs(args);

	        RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(ValueFactoryFactory.getValueFactory())
	                .customSearchPath(cmdOpts.getPathConfig().getRascalSearchPath())
	                .setTrace(cmdOpts.getCommandBoolOption("trace"))
	                .setProfile(cmdOpts.getCommandBoolOption("profile"))
	                .build();

	        Kernel kernel = new Kernel(vf, rex, cmdOpts.getCommandLocOption("bootDir"));

	        kernel.bootstrapRascalParser(cmdOpts.getCommandPathOption("srcPath"));
	    }
		catch (Throwable e) {
		    e.printStackTrace();
		    System.exit(1);
		}
	}
}
