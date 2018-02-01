package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.IOException;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.shell.RascalShell;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

public class Rascal {

    static IValueFactory vf = ValueFactoryFactory.getValueFactory();

    public static ISourceLocation findBinary(ISourceLocation bin, String moduleName) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(bin.getPath())
        .append("/")
        .append(moduleName.replaceAll("::", "/"))
        .append(".rvmx");
        
        try {
            return vf.sourceLocation(bin.getScheme(), bin.getAuthority(), sb.toString());
        }
        catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }
 
    /**
     * Main function for execute command: rascal
     * 
     * @param args	list of command-line arguments
     */
    public static void main(String[] args) {
        System.err.println("Rascal machine version: " + RascalShell.getVersionNumber());
        
        try {

            CommandOptions cmdOpts = new CommandOptions("rascal");
            cmdOpts.pathConfigOptions()
            
                   .boolOption("verbose")		
                   .help("Print compilation steps")

                   .boolOption("help")			
                   .help("Print help message for this command")

                   .boolOption("trace")		
                   .help("Print Rascal functions during execution")

                   .boolOption("profile")		
                   .help("Profile execution of Rascal program")

                   .module("RascalModule::main() to be executed")

                   .handleArgs(args);

            PathConfig pcfg = cmdOpts.getPathConfig();
            RascalExecutionContext rex = 
                RascalExecutionContextBuilder.normalContext(pcfg)
                                             .trace(cmdOpts.getCommandBoolOption("trace"))
                                             .profile(cmdOpts.getCommandBoolOption("profile"))
                                             .forModule(cmdOpts.getModule().getValue())
                                             .verbose(cmdOpts.getCommandBoolOption("verbose"))
                                             .build();

            ISourceLocation binary = findBinary(cmdOpts.getCommandLocOption("bin"), cmdOpts.getModule().getValue());
            System.out.println(RVMCore.readFromFileAndExecuteProgram(binary, cmdOpts.getModuleOptionsAsMap(), rex));
        } catch (Throwable e) {
            e.printStackTrace();
            System.err.println("rascal: cannot execute program: " + e.getMessage());
            System.exit(1);
        }
    }
}
