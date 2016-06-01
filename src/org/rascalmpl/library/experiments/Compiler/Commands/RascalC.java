package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.IOException;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CompilerError;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.lang.rascal.boot.Kernel;
import org.rascalmpl.value.IConstructor;
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
            .pathOption("srcPath")		.pathDefault(cmdOpts.getDefaultStdPath().isEmpty() ? vf.list(cmdOpts.getDefaultStdPath()) : cmdOpts.getDefaultStdPath())
            .respectNoDefaults()
            .help("Add (absolute!) source path, use multiple --srcPaths for multiple paths")

            .pathOption("libPath")		.pathDefault((co) -> vf.list(co.getCommandLocOption("binDir")))
            .respectNoDefaults()
            .help("Add new lib path, use multiple --libPaths for multiple paths")

            .locOption("bootDir")		.locDefault(cmdOpts.getDefaultBootLocation())
            .help("Rascal boot directory")

            .locOption("binDir") 		.respectNoDefaults()
            .help("Directory for Rascal binaries")

            .boolOption("noLinking")	.help("Do not link compiled modules")

            .boolOption("help") 		.help("Print help message for this command")

            .boolOption("trace") 		.help("Print Rascal functions during execution of compiler")

            .boolOption("profile") 		.help("Profile execution of compiler")

            //.boolOption("jvm") 			.help("Generate JVM code")

            .boolOption("verbose") 		.help("Make the compiler verbose")

            .rascalModule("Module to be compiled")

            .handleArgs(args);

            RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(ValueFactoryFactory.getValueFactory())
                    .customSearchPath(cmdOpts.getPathConfig().getRascalSearchPath())
                    .setTrace(cmdOpts.getCommandBoolOption("trace"))
                    .setProfile(cmdOpts.getCommandBoolOption("profile"))
                    //.setJVM(cmdOpts.getCommandBoolOption("jvm"))
                    .forModule(cmdOpts.getRascalModule().getValue())
                    .build();

            Kernel kernel = new Kernel(vf, rex, cmdOpts.getCommandLocOption("bootDir"));

            if (cmdOpts.getCommandBoolOption("noLinking")) {
                IConstructor program = kernel.compile(
                        cmdOpts.getRascalModule(),
                        cmdOpts.getCommandPathOption("srcPath"),
                        cmdOpts.getCommandPathOption("libPath"),
                        cmdOpts.getCommandLocOption("bootDir"),
                        cmdOpts.getCommandLocOption("binDir"), 
                        cmdOpts.getModuleOptionsAsIMap()); 
                handleMessages(program);
            } 
            else {
                IConstructor program = kernel.compileAndLink(
                        cmdOpts.getRascalModule(),
                        cmdOpts.getCommandPathOption("srcPath"),
                        cmdOpts.getCommandPathOption("libPath"),
                        cmdOpts.getCommandLocOption("bootDir"),
                        cmdOpts.getCommandLocOption("binDir"), 
                        cmdOpts.getModuleOptionsAsIMap());
                handleMessages(program);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);;
        }
    }

    private static void handleMessages(IConstructor program) {
        if (program.has("main_module")) {
            program = (IConstructor) program.get("main_module");
        }

        if (!program.has("messages")) {
            throw new CompilerError("unexpected output of compiler, has no messages field");
        }

        ISet messages = (ISet) program.get("messages");

        boolean failed = false;
        for (IValue val : messages) {
            IConstructor msg = (IConstructor) val;
            if (msg.getName().equals("error")) {
                failed = true;
            }

            // TODO: improve error reporting notation
            System.err.println(msg);
        }

        System.exit(failed ? 1 : 0);
    }
}
