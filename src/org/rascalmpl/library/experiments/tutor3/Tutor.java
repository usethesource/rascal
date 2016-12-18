package org.rascalmpl.library.experiments.tutor3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.Commands.CommandOptions;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.help.HelpManager;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.BasicIDEServices;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class Tutor {
	
	public static void main(String[] args) throws IOException, NoSuchRascalFunction, URISyntaxException, InterruptedException {
	  
//	  IValueFactory vf = ValueFactoryFactory.getValueFactory();
//	    CommandOptions cmdOpts = new CommandOptions("CompiledRascalShell");
//	    try {
//	        cmdOpts
//	        .locOption("bin").locDefault(vf.sourceLocation("home", "", "bin"))
//	        .help("Directory for Rascal binaries")
//	        
//	        .boolOption("help")
//	        .help("Print help message for this command")
//	        .noModuleArgument()
//	        .handleArgs(args);
//	        
//	    } catch (URISyntaxException e1) {
//	        e1.printStackTrace();
//	        System.exit(1);
//	    }
	    IValueFactory vf = ValueFactoryFactory.getValueFactory();
	    CommandOptions cmdOpts = new CommandOptions("CompiledRascalShell");
	    try {
	        cmdOpts
	        .locsOption("src").locsDefault(cmdOpts.getDefaultStdlocs().isEmpty() ? vf.list(cmdOpts.getDefaultStdlocs()) : cmdOpts.getDefaultStdlocs())
	        .help("Add (absolute!) source location, use multiple --src arguments for multiple locations")

	        .locOption("bin").locDefault(vf.sourceLocation("home", "", "bin"))
	        .help("Directory for Rascal binaries")
	        
	        .locsOption("lib").locsDefault((co) -> vf.list(co.getCommandLocOption("bin")))
	        .help("Add new lib location, use multiple --lib arguments for multiple locations")

	        .locOption("boot").locDefault(cmdOpts.getDefaultBootLocation())
	        .help("Rascal boot directory")

	        .boolOption("help")
	        .help("Print help message for this command")
	        .noModuleArgument()
	        .handleArgs(args);
	        
	    } catch (URISyntaxException e1) {
	        e1.printStackTrace();
	        System.exit(1);
	    }
	 
	  PathConfig pcfg = new PathConfig(cmdOpts.getCommandLocsOption("src"), cmdOpts.getCommandLocsOption("lib"), cmdOpts.getCommandLocOption("bin"), cmdOpts.getCommandLocOption("boot"));
	  IDEServices ideServices = new BasicIDEServices();
	  HelpManager hm = new HelpManager(pcfg, new PrintWriter(System.out), new PrintWriter(System.err), ideServices);
	  
	  ideServices.browse(new URI("http://localhost:" + hm.getPort() + "/TutorHome/index.html"));
	  Thread.sleep(864000000);  // a hack a day keeps the doctor away (and the debugger close)
	}
}
