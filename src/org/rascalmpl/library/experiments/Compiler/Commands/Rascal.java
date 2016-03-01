package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMInterpreter;
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
			System.err.println(e.getMessage());
			System.exit(-1);
			return null;
		}
	}
	
	static {
		double[] ZZZZ = {2.5, 3.5};
	}
	
	/**
	 * Main function for execute command: rascal
	 * 
	 * @param args	list of command-line arguments
	 */
	public static void main(String[] args) {
		
		CommandOptions cmdOpts = new CommandOptions("rascal");
		cmdOpts
			.pathOption("libPath")		.pathDefault((co) -> vf.list(co.getCommandLocOption("binDir")))
										.help("Add new lib paths, use multiple --libPaths for multiple paths")
										
			.locOption("bootDir") 		.locDefault(cmdOpts.getDefaultBootLocation())
										.help("Rascal boot directory")
										
			.locOption("binDir") 		.help("Directory for Rascal binaries")
										
			.boolOption("jvm") 			.help("Generate JVM code")
			
			.boolOption("verbose")		.help("Print compilation steps")
			
			.boolOption("help")			.help("Print help message for this command")
			
			.boolOption("trackCalls")	.help("Print Rascal functions during execution")
			
			.boolOption("profile")		.help("Profile execution of Rascal program")
			
			.rascalModule("RascalModule::main() to be executed")
			
			.handleArgs(args);
		
		RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(ValueFactoryFactory.getValueFactory(), new PrintWriter(System.out, true), new PrintWriter(System.err, true))
				.setTrackCalls(cmdOpts.getCommandBoolOption("trackCalls"))
                .setProfiling(cmdOpts.getCommandBoolOption("profile"))
                .forModule(cmdOpts.getRascalModule().getValue())
                .build();
		
		ISourceLocation binary = findBinary(cmdOpts.getCommandLocOption("binDir"), cmdOpts.getRascalModule().getValue());
		try {
			System.out.println(RVMInterpreter.readFromFileAndExecuteProgram(binary, cmdOpts.getModuleOptionsAsIMap(), rex));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("rascal: cannot execute program: " + e.getMessage());
			System.exit(-1);
		}
	}
}
