package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.IOException;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.Java2Rascal;
import org.rascalmpl.library.lang.rascal.boot.IKernel;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.shell.RascalShell;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
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
	    System.err.println("Rascal test runner version: " + RascalShell.getVersionNumber());
	    
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		
		CommandOptions cmdOpts = new CommandOptions("rascalTests");
		cmdOpts.pathConfigOptions()

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

		PathConfig pcfg = cmdOpts.getPathConfig();
		IKernel kernel = Java2Rascal.Builder.bridge(vf, pcfg, IKernel.class).build();
		
		try {
		    IValue outcome =
		                kernel.rascalTests(cmdOpts.getModules(), pcfg.asConstructor(kernel),
		                                   kernel.kw_rascalTests()
		                                         .recompile(cmdOpts.getCommandBoolOption("recompile"))
		                                         .trace(cmdOpts.getCommandBoolOption("trace"))
		                                         .profile(cmdOpts.getCommandBoolOption("profile"))
		                                         .verbose(cmdOpts.getCommandBoolOption("verbose"))
		        );
		    IBool success = outcome.getType().isBool() ? (IBool) outcome
		                                               : (IBool) ((ITuple) outcome).get(0);

		    System.exit(success.getValue() ? 0 : 1);
		  }
		catch (Throwable e) {
		    e.printStackTrace();
		    System.exit(1);
		}
	}
}
