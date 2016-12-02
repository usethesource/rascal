package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.IOException;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.lang.rascal.boot.Kernel;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class RascalTests {

	/**
	 * Main function for rascalTests command: rascalTests
	 * 
	 * @param args	list of command-line arguments
	 * @throws NoSuchRascalFunction 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws IOException, NoSuchRascalFunction, URISyntaxException {
		
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		
		CommandOptions cmdOpts = new CommandOptions("rascalTests");
		cmdOpts
			.locsOption("src")		
			.locsDefault(cmdOpts.getDefaultStdlocs().isEmpty() ? vf.list(cmdOpts.getDefaultStdlocs()) : cmdOpts.getDefaultStdlocs())
			.respectNoDefaults()
			.help("Add (absolute!) source location, use multiple --src arguments for multiple locations")
		
			.locsOption("lib")		
			.locsDefault((co) -> vf.list(co.getCommandLocOption("bin")))
			.respectNoDefaults()
			.help("Add new lib location, use multiple --lib arguments for multiple locations")
		
			.locOption("boot")		
			.locDefault(cmdOpts.getDefaultBootLocation())
			.help("Rascal boot directory")
		
			.locOption("bin") 		
			.help("Directory for Rascal binaries")
			
			.boolOption("recompile")
			.help("Recompile before running tests, when false existing binary is used")
			
			.boolOption("help")
			.help("Print help message for this command")
			
			.boolOption("trace")
			.help("Print Rascal functions during execution of compiler")
			
			.boolOption("profile")
			.help("Profile execution of compiler")
			
			.boolOption("verbose")		
			.help("Make the compiler verbose")
			
			 .boolOption("enableAsserts")
			 .boolDefault(true)
	         .help("Enable checking of assertions")
			
			.modules("Rascal modules with tests")
			
			.handleArgs(args);
		
		RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(ValueFactoryFactory.getValueFactory(), cmdOpts.getCommandLocOption("boot"))
				.customSearchPath(cmdOpts.getPathConfig().getRascalSearchPath())
				.setTrace(cmdOpts.getCommandBoolOption("trace"))
				.setProfile(cmdOpts.getCommandBoolOption("profile"))
				.forModule(cmdOpts.getModule().getValue())
                .setVerbose(cmdOpts.getCommandBoolOption("verbose"))
				.build();

		Kernel kernel = new Kernel(vf, rex, cmdOpts.getCommandLocOption("boot"));
		try {
		  if(!cmdOpts.getCommandLocOption("bin").getPath().contains("/rascal/bootstrap/")){ // transient for boot
		    IBool success = (IBool) kernel.rascalTests(
		            cmdOpts.getModules(),
		            cmdOpts.getCommandLocsOption("src"),
		            cmdOpts.getCommandLocsOption("lib"),
		            cmdOpts.getCommandLocOption("boot"),
		            cmdOpts.getCommandLocOption("bin"), 
		            cmdOpts.getCommandBoolOption("recompile"), 
		            cmdOpts.getModuleOptionsAsMap());

		    System.exit(success.getValue() ? 0 : 1);
		  }
		}
		catch (Throwable e) {
		    e.printStackTrace();
		    System.exit(1);
		}
	}
}
