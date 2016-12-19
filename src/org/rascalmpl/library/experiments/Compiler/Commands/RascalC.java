package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CompilerError;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.ApiGen;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.Java2Rascal;
import org.rascalmpl.library.lang.rascal.boot.IKernel;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
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
            
            .locOption("reloc")       
            .locDefault(cmdOpts.getDefaultRelocLocation())
            .help("Relocate source locations")

            .boolOption("noLinking")	
            .help("Do not link compiled modules")
            
            .strOption("apigen")
            .strDefault("")
            .help("Package name for generating api for Java -> Rascal")
            
            .locOption("src-gen")
            .locDefault((co) -> (ISourceLocation) co.getCommandLocsOption("src").get(0))
            .help("Target directory for generated source code")

            .boolOption("help") 		
            .help("Print help message for this command")

            .boolOption("trace") 		
            .help("Print Rascal functions during execution of compiler")

            .boolOption("profile") 		
            .help("Profile execution of compiler")
            
            .boolOption("optimize")
            .boolDefault(true)
            .help("Apply code optimizations")
            
            .boolOption("enableAsserts")
            .help("Enable checking of assertions")

            //.boolOption("jvm") 			.help("Generate JVM code")

            .boolOption("verbose")
            .help("Make the compiler verbose")

            .modules("Modules to be compiled")

            .handleArgs(args);
            
            ISourceLocation srcGen = cmdOpts.getCommandLocOption("src-gen");
            
            if(!cmdOpts.getCommandStringOption("apigen").isEmpty()){
              if(cmdOpts.getCommandBoolOption("noLinking")){
                System.err.println("Option --apigen cannot be combined with --noLinking");
                System.exit(1);
              }
              if(!srcGen.getScheme().equals("file")){
                System.err.println("Can only write generated APIs to a --src-gen with a file schema");
                System.exit(1);;
              }
            }
            PathConfig pcfg = cmdOpts.getPathConfig();
            RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(pcfg)
//                    .customSearchPath(pcfg.getRascalSearchPath())
                    .trace(cmdOpts.getCommandBoolOption("trace"))
                    .profile(cmdOpts.getCommandBoolOption("profile"))
                    //.setJVM(cmdOpts.getCommandBoolOption("jvm"))
                    .forModule(cmdOpts.getModule().getValue())
                    .verbose(cmdOpts.getCommandBoolOption("verbose"))
                    .build();

            //Kernel kernel = new Kernel(vf, rex, cmdOpts.getCommandLocOption("boot"));
            IKernel kernel = Java2Rascal.Builder.bridge(vf, pcfg, IKernel.class).build();

            boolean ok = true;
            
            if (cmdOpts.getCommandBoolOption("noLinking")) {
                IList programs = kernel.compile(cmdOpts.getModules(), pcfg.asConstructor(kernel),
                                                kernel.kw_compile().reloc(cmdOpts.getCommandLocOption("reloc"))
                                                );
                ok = handleMessages(programs);
                System.exit(ok ? 0 : 1);
            } 
            else {
                IList programs = kernel.compileAndLink(cmdOpts.getModules(), pcfg.asConstructor(kernel),
                                                       kernel.kw_compileAndLink()
                                                       .reloc(cmdOpts.getCommandLocOption("reloc"))
                                                       );
                ok = handleMessages(programs);
                if(!ok){
                  System.exit(1);
                }
                String pckg = cmdOpts.getCommandStringOption("apigen");
                if(!pckg.isEmpty()){
                 
                  
                  for(IValue mod : cmdOpts.getModules()){
                    String moduleName = ((IString) mod).getValue();
                    ISourceLocation binary = Rascal.findBinary(cmdOpts.getCommandLocOption("bin"), moduleName);
                    RVMExecutable exec = RVMExecutable.read(binary, rex.getTypeStore());
                      
                    try {
                      String api = ApiGen.generate(exec, moduleName, pckg);
                      String modulePath;
                      
                      int i = moduleName.lastIndexOf("::");
                      if(i >= 0){
                        modulePath = moduleName.substring(0, i+2) + "I" + moduleName.substring(i+2);
                        modulePath = modulePath.replaceAll("::",  "/");
                      } else {
                        modulePath = "I" + moduleName;
                      }
                      
                      String path = srcGen.getPath() + "/" + modulePath + ".java";
                      ISourceLocation apiLoc = URIUtil.correctLocation(srcGen.getScheme(), srcGen.getAuthority(), path);
                      System.err.println(apiLoc);
                      System.err.println(api);
                      OutputStream apiOut = URIResolverRegistry.getInstance().getOutputStream(apiLoc, false);
                      apiOut.write(api.getBytes());
                      apiOut.close();
                    } catch (Exception e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                    }
                  }
                }
                System.exit(0);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);;
        }
    }

    public static boolean handleMessages(IList programs) {
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

    	return !failed;
    }
}
