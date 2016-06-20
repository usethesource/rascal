package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.IOException;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.lang.rascal.boot.Kernel;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class CompileMuLibrary {

    /**
     * This command is used by Bootstrap only.
     * 
     * @param args	list of command-line arguments
     * @throws NoSuchRascalFunction 
     * @throws IOException 
     * @throws URISyntaxException 
     */
    public static void main(String[] args)  {
        try {
            IValueFactory vf = ValueFactoryFactory.getValueFactory();
            CommandOptions cmdOpts = new CommandOptions("compileMuLibrary");
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
            
            .boolOption("help") 		
            .help("Print help message for this command")
            
            .boolOption("trace") 		
            .help("Print Rascal functions during execution of compiler")
            
            .boolOption("profile") 		
            .help("Profile execution of compiler")
           
            .boolOption("verbose") 		
            .help("Make the compiler verbose")
            
            .noModuleArgument()
            .handleArgs(args);

            RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(ValueFactoryFactory.getValueFactory())
                    .customSearchPath(cmdOpts.getPathConfig().getRascalSearchPath())
                    .setTrace(cmdOpts.getCommandBoolOption("trace"))
                    .setProfile(cmdOpts.getCommandBoolOption("profile"))
                    //.setJVM(cmdOpts.getCommandBoolOption("jvm"))
                    .build();

            Kernel kernel = new Kernel(vf, rex);

            kernel.compileMuLibrary(
                    cmdOpts.getCommandlocsOption("src"),
                    cmdOpts.getCommandlocsOption("lib"),
                    cmdOpts.getCommandLocOption("boot"),
                    cmdOpts.getCommandLocOption("bin"), 
                    cmdOpts.getModuleOptionsAsIMap());
        }
        catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
