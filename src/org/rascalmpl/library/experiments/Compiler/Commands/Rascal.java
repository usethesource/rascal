package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class Rascal {
	
	static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	static ISourceLocation findBinary(ISourceLocation binDir, String moduleName){
		StringWriter sw = new StringWriter();
		sw.append(binDir.getPath())
		  .append("/")
		  .append(moduleName.replaceAll("::", "/"))
		  .append(".rvm.ser.gz");
		try {
			return vf.sourceLocation("compressed+" + binDir.getScheme(), binDir.getAuthority(), sw.toString());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
	}
	
	/**
	 * Main function for execute command: rascal
	 * 
	 * @param args	list of command-line arguments
	 */
	public static void main(String[] args) {
		
		CommandOptions cmdOpts = new CommandOptions();
		cmdOpts
				.pathOption("libPath", 		(co) -> vf.list(co.getCommandLocOption("binDir")),
																				"Add new lib paths, use multiple --libPaths for multiple paths")
				.locOption("bootDir", 		cmdOpts.getDefaultBootLocation(), 	"Rascal boot directory")
				.locOption("binDir", 		(co) -> co.requiredDir("binDir"), 	"Directory for Rascal binaries")
				.boolOption("jvm", 			false, 								"Generate JVM code")
				.boolOption("verbose", 		false, 								"Print compilation steps")
				.boolOption("help", 		false, 								"Print help message for this command")
				.boolOption("trackCalls", 	false, 								"Print Rascal functions during execution")
				.boolOption("profile", 		false, 								"Profile execution of Rascal program")
				.rascalModule("RascalModule::main() to be executed")
				.handleArgs("rascalc", args);
		
		RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(ValueFactoryFactory.getValueFactory(), new PrintWriter(System.out, true), new PrintWriter(System.err, true))
				.setTrackCalls(cmdOpts.getCommandBoolOption("trackCalls"))
                .setProfiling(cmdOpts.getCommandBoolOption("profile"))
                .forModule(cmdOpts.getRascalModule().getValue())
                .build();
		
		ISourceLocation binary = findBinary(cmdOpts.getCommandLocOption("binDir"), cmdOpts.getRascalModule().getValue());
		try {
			System.out.println(RVM.readFromFileAndExecuteProgram(binary, cmdOpts.getModuleOptionsAsIMap(), rex));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("rascal: cannot execute program: " + e.getMessage());
			System.exit(-1);
		}
	}
}
