package org.rascalmpl.shell;

import java.io.PrintWriter;
import java.io.Reader;
import java.util.function.Function;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.Messages;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.library.util.PathConfig.RascalConfigMode;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.SourceLocationClassLoader;
import org.rascalmpl.uri.file.MavenRepositoryURIResolver;
import org.rascalmpl.uri.project.IDEProjectURIResolver;
import org.rascalmpl.uri.project.IDETargetURIResolver;
import org.rascalmpl.uri.project.ProjectURIResolver;
import org.rascalmpl.uri.project.TargetURIResolver;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;

public class ShellEvaluatorFactory {

    public static Evaluator getBasicEvaluator(Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor) {
        return getBasicEvaluator(input, stdout, stderr, monitor, ModuleEnvironment.SHELL_MODULE);
    }

    public static Evaluator getBasicEvaluator(Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor, String rootEnvironment) {
        var heap = new GlobalEnvironment();
        var root = heap.addModule(new ModuleEnvironment(rootEnvironment, heap));
        var evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), input, stderr, stdout, root, heap, monitor);
        evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());

        return evaluator;
    }

    public static Evaluator getDefaultEvaluator(Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor) {
        return getDefaultEvaluator(input, stdout, stderr, monitor, ModuleEnvironment.SHELL_MODULE);
    }

    public static Evaluator getDefaultEvaluator(Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor, String rootEnvironment) {
        var reg = URIResolverRegistry.getInstance();
        if (!reg.getRegisteredInputSchemes().contains("project") && !reg.getRegisteredLogicalSchemes().contains("project")) {
            return getDefaultEvaluatorForLocation(URIUtil.rootLocation("cwd"), input, stdout, stderr, monitor, rootEnvironment);
        }
        return getBasicEvaluator(input, stdout, stderr, monitor, rootEnvironment);
    }

    public static Evaluator getDefaultEvaluatorForPathConfig(ISourceLocation projectRoot, PathConfig pcfg, Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor) {
        return getDefaultEvaluatorForPathConfig(projectRoot, pcfg, input, stdout, stderr, monitor, ModuleEnvironment.SHELL_MODULE);
    }
    
    public static Evaluator getDefaultEvaluatorForPathConfig(ISourceLocation projectRoot, PathConfig pcfg, Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor, String rootEnvironment) {
        if (monitor instanceof IDEServices) {
            registerProjectAndTargetResolver(((IDEServices) monitor)::resolveProjectLocation);
        } else {
            registerProjectAndTargetResolver(projectRoot);
        }
        
        var evaluator = getBasicEvaluator(input, stdout, stderr, monitor, rootEnvironment);
        
        stdout.println("Rascal " + RascalManifest.getRascalVersionNumber());
        stdout.println("Rascal search path:");
        for (var srcPath : pcfg.getSrcs()) {
            var path = MavenRepositoryURIResolver.mavenize((ISourceLocation)srcPath);
            stdout.println("- " + path);
            evaluator.addRascalSearchPath(path);
        }

        var isRascal = projectRoot != null && new RascalManifest().getProjectName(projectRoot).equals("rascal");
        var libs = isRascal ? pcfg.getLibs() : pcfg.getLibsAndTarget();
        stdout.println("Rascal classloader path:");
        for (var lib : libs) {
            var path = (ISourceLocation)lib;
            stdout.println("- " + lib);
            evaluator.addRascalSearchPath(path);
        }
        evaluator.addClassLoader(new SourceLocationClassLoader(libs, ClassLoader.getSystemClassLoader()));

        if (!pcfg.getMessages().isEmpty()) {
            stdout.println("PathConfig messages:");
            Messages.write(pcfg.getMessages(), pcfg.getSrcs(), stdout);
        }

        return evaluator;
    }

    public static Evaluator getDefaultEvaluatorForLocation(ISourceLocation projectFile, Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor) {
        return getDefaultEvaluatorForLocation(projectFile, input, stdout, stderr, monitor, ModuleEnvironment.SHELL_MODULE);
    }

    public static Evaluator getDefaultEvaluatorForLocation(ISourceLocation projectFile, Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor, String rootEnvironment) {
        var projectRoot = PathConfig.inferProjectRoot(projectFile);
        var pcfg = PathConfig.fromSourceProjectRascalManifest(projectRoot, RascalConfigMode.INTERPRETER, true);
        return getDefaultEvaluatorForPathConfig(projectRoot, pcfg, input, stdout, stderr, monitor, rootEnvironment);
    }

    private static void registerProjectAndTargetResolver(ISourceLocation projectFile) {
        var reg = URIResolverRegistry.getInstance();
        var projectRoot = PathConfig.inferProjectRoot(projectFile);
        if (projectRoot != null) {
            var projectName = new RascalManifest().getProjectName(projectRoot);
            if (!projectName.equals("")) {
                reg.registerLogical(new ProjectURIResolver(projectRoot, projectName));
                reg.registerLogical(new TargetURIResolver(projectRoot, projectName));
            }
        }
    }

    public static void registerProjectAndTargetResolver(Function<ISourceLocation,ISourceLocation> resolver) {
        var reg = URIResolverRegistry.getInstance();
        reg.registerLogical(new IDEProjectURIResolver(resolver));
        reg.registerLogical(new IDETargetURIResolver(resolver));
    }

}
