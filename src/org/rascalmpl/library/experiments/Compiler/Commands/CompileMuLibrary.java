package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.IOException;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.Java2Rascal;
import org.rascalmpl.library.lang.rascal.boot.IKernel;
import org.rascalmpl.library.util.PathConfig;
import io.usethesource.vallang.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class CompileMuLibrary {

    /**
     * Compile the MuLibrary (used by Bootstrap only).
     * 
     * @param args list of command-line arguments
     * @throws NoSuchRascalFunction 
     * @throws IOException 
     * @throws URISyntaxException 
     */
    public static void main(String[] args)  {
        try {
            IValueFactory vf = ValueFactoryFactory.getValueFactory();
            CommandOptions cmdOpts = new CommandOptions("compileMuLibrary");
            
            cmdOpts.pathConfigOptions()
            
                   .locOption("reloc")       
                   .locDefault(cmdOpts.getDefaultRelocLocation())
                   .help("Relocate source locations")
        
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

            PathConfig pcfg = cmdOpts.getPathConfig();
            IKernel kernel = Java2Rascal.Builder.bridge(vf, pcfg, IKernel.class)
                                                .trace(cmdOpts.getCommandBoolOption("trace"))
                                                .profile(cmdOpts.getCommandBoolOption("profile"))
                                                .verbose(cmdOpts.getCommandBoolOption("verbose"))
                                                .build();
            System.err.println("CompileMuLibary.java: reloc = " + cmdOpts.getCommandLocOption("reloc"));
            kernel.compileMuLibrary(pcfg.asConstructor(kernel), kernel.kw_compileMu().reloc(cmdOpts.getCommandLocOption("reloc")));
        }
        catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
