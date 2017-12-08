package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Set;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.InternalCompilerError;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.ApiGen;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.Java2Rascal;
import org.rascalmpl.library.lang.rascal.boot.IKernel;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.shell.RascalShell;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
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
        System.err.println("Rascal compiler version: " + RascalShell.getVersionNumber());
        
        try {
            IValueFactory vf = ValueFactoryFactory.getValueFactory();
            CommandOptions cmdOpts = new CommandOptions("rascalc");
            
            cmdOpts.pathConfigOptions()
        
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

                   .boolOption("verbose")
                   .help("Make the compiler verbose")

                   .modules("List of module names to be compiled or a single location for a directory to compile all modules from")

                   .handleArgs(args);
            
            ISourceLocation srcGen = cmdOpts.getCommandLocOption("src-gen");
            
            if(!cmdOpts.getCommandStringOption("apigen").isEmpty()){
              if(cmdOpts.getCommandBoolOption("noLinking")){
                System.err.println("Option --apigen cannot be combined with --noLinking");
                System.exit(1);
              }
              Set<String> outputs = URIResolverRegistry.getInstance().getRegisteredOutputSchemes();
              if(!outputs.contains(srcGen.getScheme())){
                  System.err.println("Can only write generated APIs to a --src-gen with theses schemes: ");
                  outputs.stream().forEach(t -> System.err.println("\t " + t));
                  System.exit(1);
              }
            }
           
            PathConfig pcfg = cmdOpts.getPathConfig();
            IKernel kernel = Java2Rascal.Builder.bridge(vf, pcfg, IKernel.class)
                                                .trace(cmdOpts.getCommandBoolOption("trace"))
                                                .profile(cmdOpts.getCommandBoolOption("profile"))
                                                .verbose(cmdOpts.getCommandBoolOption("verbose"))
                                                .build();

            boolean ok = true;
            
            IList modules = cmdOpts.getModules();
            
            if (modules.length() == 1 && ((IString) modules.get(0)).getValue().startsWith("|")) {
                // compiling a whole directory assumes no linking
                String loc = ((IString) modules.get(0)).getValue();
                IList programs = kernel.compileAll(vf.sourceLocation(URIUtil.createFromEncoded(loc.substring(1, loc.length() - 1))), pcfg.asConstructor(kernel),
                    kernel.kw_compile().reloc(cmdOpts.getCommandLocOption("reloc")));
                System.exit(handleMessages(programs, pcfg) ? 0 : 1);
            }
            
            if (cmdOpts.getCommandBoolOption("noLinking")) {
                IList programs = kernel.compile(modules, pcfg.asConstructor(kernel),
                    kernel.kw_compile().reloc(cmdOpts.getCommandLocOption("reloc")));
                System.exit(handleMessages(programs, pcfg) ? 0 : 1);
            } 
            else {
                IList programs = kernel.compileAndLink(modules, pcfg.asConstructor(kernel),
                                                       kernel.kw_compileAndLink()
                                                             .reloc(cmdOpts.getCommandLocOption("reloc"))
                                                       );
                
                ok = handleMessages(programs, pcfg);
                if(!ok){
                  System.exit(1);
                }
                String pckg = cmdOpts.getCommandStringOption("apigen");
                if(!pckg.isEmpty()){
                 
                  
                  for(IValue mod : modules){
                    String moduleName = ((IString) mod).getValue();
                    ISourceLocation binary = Rascal.findBinary(cmdOpts.getCommandLocOption("bin"), moduleName);

                    RVMExecutable exec = RVMExecutable.read(binary);
  
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
                      e.printStackTrace();
                      System.exit(1);
                    }
                  }
                }
                System.exit(0);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static boolean handleMessages(IList programs, PathConfig pcfg) {
        boolean failed = false;

        for(IValue iprogram : programs){
            IConstructor program = (IConstructor) iprogram;
            if (program.has("main_module")) {
                program = (IConstructor) program.get("main_module");
            }

            if (!program.has("messages")) {
                throw new InternalCompilerError("unexpected output of compiler, has no messages field");
            }

            ISet messages = (ISet) program.get("messages");

            failed |= handleMessages(pcfg, messages);
        }
        
        return failed;
    }
    	
    	public static boolean handleMessages(PathConfig pcfg, ISet messages) {
    	    boolean failed = false;
    	    int maxLine = 0;
    	    int maxColumn = 0;

    	    for (IValue val : messages) {
    	        ISourceLocation loc = (ISourceLocation) ((IConstructor) val).get("at");
    	        maxLine = Math.max(loc.getBeginLine(), maxLine);
    	        maxColumn = Math.max(loc.getBeginColumn(), maxColumn);
    	    }


    	    int lineWidth = (int) Math.log10(maxLine + 1) + 1;
    	    int colWidth = (int) Math.log10(maxColumn + 1) + 1;

    	    for (IValue val : messages) {
    	        IConstructor msg = (IConstructor) val;
    	        if (msg.getName().equals("error")) {
    	            failed = true;
    	        }

    	        ISourceLocation loc = (ISourceLocation) msg.get("at");
    	        int col = loc.getBeginColumn();
    	        int line = loc.getBeginLine();

    	        System.err.println(msg.getName() + "@" + abbreviate(loc, pcfg) 
    	        + ":" 
    	        + String.format("%0" + lineWidth + "d", line)
    	        + ":"
    	        + String.format("%0" + colWidth + "d", col)
    	        + ": "
    	        + ((IString) msg.get("msg")).getValue()
    	            );
    	    }

    	    return !failed;
    }

    private static String abbreviate(ISourceLocation loc, PathConfig pcfg) {
        for (IValue src : pcfg.getSrcs()) {
            String path = ((ISourceLocation) src).getURI().getPath();
            
            if (loc.getURI().getPath().startsWith(path)) {
                return loc.getURI().getPath().substring(path.length()); 
            }
        }
        
        return loc.getURI().getPath();
    }
}
