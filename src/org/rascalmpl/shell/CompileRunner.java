package org.rascalmpl.shell;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

public class CompileRunner implements ShellRunner {

    private final PrintWriter stdout;
    private final PrintWriter stderr;
    private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
    private static final TypeFactory tf = TypeFactory.getInstance();

    public CompileRunner(PrintWriter stdout, PrintWriter stderr) {
        this.stdout = stdout;
        this.stderr = stderr;
    }

    private static final String COMPILERMODULENAME = "compiler__console";
    
    @Override
    public void run(String[] args) throws IOException {
        assert args[0].equals("-compile");

        String[] modules = getModules(args);

        if (modules.length == 0) {
            stderr.println("Please provide at least one rascal module to compile");
            System.exit(1);
            return;
        }

        IValue pathConfig = buildPathConfig(args);
        
        stdout.println(pathConfig.toString());
        stdout.flush();

        stderr.println("Loading compiler framework");

        Function compileAndLink = null;
        RVM rvmCompiler = null;
        RascalExecutionContext rex = prepateExecutionContext();
        try {
            rvmCompiler = RVM.readFromFileAndInitialize(vf.sourceLocation("compressed+boot", "", "Kernel.rvm.ser.gz"), rex);
            //compileAndLink(str moduleName,  PathConfig pcfg, bool useJVM=false, bool serialize=true, bool verbose = false)
            compileAndLink = rvmCompiler.getFunction("compileAndLink", 
                    tf.abstractDataType(new TypeStore(), "RVMProgram"), 
                    tf.tupleType(tf.stringType(), tf.abstractDataType(new TypeStore(), "PathConfig")));
        }
        catch (URISyntaxException e) {
            throw new RuntimeException("Could not load compiler framework", e);
        }
        if(compileAndLink == null){
            throw new RuntimeException("Cannot find compileAndLinkIncremental function");
        }
        stderr.println("Compiler loaded");
        
        
        
        for (String mod: modules) {
            stderr.println("Compiling: " + mod);
            IValue[] compileArgs = new IValue[] { vf.string(mod), pathConfig };
            rvmCompiler.executeFunction(compileAndLink, compileArgs, new HashMap<>());
        }
    }
    
    private IValue buildPathConfig(String[] args) {
        try {
            ISourceLocation binaryDir = getBinaryDir(args, "bin/");
            List<ISourceLocation> libPath = getLibraryPath(args, ".");
            List<ISourceLocation> srcPath = getSourcePath(args, ".");
            
            TypeStore store = new TypeStore();
            Type adt = tf.abstractDataType(store, "PathConfig");
            Type constructor = tf.constructor(store, adt, "pathConfig", new Type[]{});
            
            Map<String, IValue> config = new HashMap<>();
            config.put("srcPath", toList(srcPath, URIUtil.rootLocation("cwd"), URIUtil.rootLocation("std")));
            config.put("libPath", toList(libPath, URIUtil.rootLocation("boot"), URIUtil.rootLocation("std")));
            config.put("binDir", binaryDir);
            return vf.constructor(constructor, new IValue[]{}, config);
        }
        catch (RuntimeException e) {
            stderr.println("Can't process arguments for the paths: " + e.getMessage());
            e.printStackTrace(stderr);
            System.exit(1);
            return null;
        }
    }

    private IValue toList(List<ISourceLocation> initial, ISourceLocation... extra) {
        IListWriter result = vf.listWriter();
        result.appendAll(initial);
        result.append(extra);
        return result.done();
    }

    private String[] getModules(String[] args) {
        List<String> result = new ArrayList<>();
        int i = 0;
        while (i < args.length) {
            String arg = args[i];
            if (arg.equals("-compile")) {
                i++;
            }
            else if (arg.startsWith("-")) {
                i += 2;
            }
            else {
                arg = arg.replace("/","::");
                arg = arg.replace(".rsc", "");
                result.add(arg);
                i++;
            }
        }
        return result.toArray(new String[]{});
    }


    private static ISourceLocation uncheckedToFileLocation(String file) {
        try {
            if (file.startsWith("/")) {
                return vf.sourceLocation("file","", file);
            }
            return vf.sourceLocation("cwd", "", file);
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        
    }

    private List<ISourceLocation> getSourcePath(String[] args, String... defaults) {
        return getPaths(args, "srcPath", defaults).stream()
                .map(p -> uncheckedToFileLocation(p))
                .collect(Collectors.toList());
    }

    private List<ISourceLocation> getLibraryPath(String[] args, String... defaults) {
        return getPaths(args, "libPath", defaults).stream()
                .map(p -> uncheckedToFileLocation(p))
                .collect(Collectors.toList());
    }

    private ISourceLocation getBinaryDir(String[] args, String defaultDir) {
        return uncheckedToFileLocation(getPaths(args, "binDir", defaultDir).get(0));
    }

    private List<String> getPaths(String[] args, String param, String... defaultPath) {
        List<String> result = new ArrayList<>();
        int currentArg = 0;
        while (currentArg < args.length) {
            if (args[currentArg].equals("-" + param) && currentArg + 1 < args.length) {
                result.add(args[currentArg + 1]);
                currentArg++;
            }
            currentArg++;
        }
        if (result.isEmpty()) {
            for (String def: defaultPath) {
                result.add(def);
            }
        }
        return result;
    }

    private RascalExecutionContext prepateExecutionContext() {
        IMapWriter w = vf.mapWriter();
        w.put(vf.string("bootstrapParser"), vf.string(""));
        IMap CompiledRascalShellModuleTags = w.done();
        
        w = vf.mapWriter();
        w.put(vf.string(COMPILERMODULENAME), CompiledRascalShellModuleTags);
        IMap moduleTags = w.done();
        
        RascalExecutionContext rex = new RascalExecutionContext(vf, this.stdout, this.stderr, moduleTags, null, null, false, false, false, /*profile*/false, false, false, false, null, null, null);
        rex.setCurrentModuleName(COMPILERMODULENAME);
        return rex;
    }

}
