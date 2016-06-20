package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.IOException;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CompilerError;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.lang.rascal.boot.Kernel;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class RascalC {

    /**
     * Main function for compile command: rascalc
     * 
     * @param args	list of command-line arguments
     * @throws NoSuchRascalFunction 
     * @throws IOException 
     * @throws URISyntaxException 
     */
    public static void main(String[] args)  {
        try {
            IValueFactory vf = ValueFactoryFactory.getValueFactory();
            CommandOptions cmdOpts = new CommandOptions("rascalc");
            
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
            .respectNoDefaults()
            .help("Directory for Rascal binaries")

            .boolOption("noLinking")	
            .help("Do not link compiled modules")

            .boolOption("help") 		
            .help("Print help message for this command")

            .boolOption("trace") 		
            .help("Print Rascal functions during execution of compiler")

            .boolOption("profile") 		
            .help("Profile execution of compiler")
            
            .boolOption("optimize")
            .boolDefault(true)
            .help("Apply code optimizations")

            //.boolOption("jvm") 			.help("Generate JVM code")

            .boolOption("verbose")
            .help("Make the compiler verbose")

            .rascalModules("Modules to be compiled")

            .handleArgs(args);

            RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(ValueFactoryFactory.getValueFactory())
                    .customSearchPath(cmdOpts.getPathConfig().getRascalSearchPath())
                    .setTrace(cmdOpts.getCommandBoolOption("trace"))
                    .setProfile(cmdOpts.getCommandBoolOption("profile"))
                    //.setJVM(cmdOpts.getCommandBoolOption("jvm"))
                    .forModule(cmdOpts.getRascalModule().getValue())
                    .build();

            Kernel kernel = new Kernel(vf, rex, cmdOpts.getCommandLocOption("boot"));

            if (cmdOpts.getCommandBoolOption("noLinking")) {
                IList programs = kernel.compile(
                        cmdOpts.getRascalModules(),
                        cmdOpts.getCommandlocsOption("src"),
                        cmdOpts.getCommandlocsOption("lib"),
                        cmdOpts.getCommandLocOption("boot"),
                        cmdOpts.getCommandLocOption("bin"), 
                        cmdOpts.getModuleOptionsAsIMap()); 
                handleMessages(programs);
            } 
            else {
                IList programs = kernel.compileAndLink(
                        cmdOpts.getRascalModules(),
                        cmdOpts.getCommandlocsOption("src"),
                        cmdOpts.getCommandlocsOption("lib"),
                        cmdOpts.getCommandLocOption("boot"),
                        cmdOpts.getCommandLocOption("bin"), 
                        cmdOpts.getModuleOptionsAsIMap());
                handleMessages(programs);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);;
        }
    }

    private static void handleMessages(IList programs) {
    	boolean failed = false;

    	for(IValue iprogram : programs){
    		IConstructor program = (IConstructor) iprogram;
    		if (program.has("main_module")) {
    			program = (IConstructor) program.get("main_module");
    		}

    		if (!program.has("messages")) {
    			throw new CompilerError("unexpected output of compiler, has no messages field");
    		}

    		ISet messages = (ISet) program.get("messages");

    		for (IValue val : messages) {
    			IConstructor msg = (IConstructor) val;
    			if (msg.getName().equals("error")) {
    				failed = true;
    			}

    			// TODO: improve error reporting notation
    			System.err.println(msg);
    		}
    	}

    	System.exit(failed ? 1 : 0);
    }
}
