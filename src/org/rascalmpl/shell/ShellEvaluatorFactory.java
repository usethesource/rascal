package org.rascalmpl.shell;

import java.io.File;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URISyntaxException;

import org.rascalmpl.debug.IRascalMonitor;
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
import org.rascalmpl.uri.project.ProjectURIResolver;
import org.rascalmpl.uri.project.TargetURIResolver;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class ShellEvaluatorFactory {

    public static Evaluator getBasicEvaluator(Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor) {
        return getBasicEvaluator(input, stdout, stderr, monitor, ModuleEnvironment.SHELL_MODULE);
    }

    public static Evaluator getBasicEvaluator(Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor, String rootModule) {
        GlobalEnvironment heap = new GlobalEnvironment();
        ModuleEnvironment root = heap.addModule(new ModuleEnvironment(rootModule, heap));
        IValueFactory vf = ValueFactoryFactory.getValueFactory();
        Evaluator evaluator = new Evaluator(vf, input, stderr, stdout, root, heap, monitor);
        evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());

        return evaluator;
    }

    public static Evaluator getDefaultEvaluator(Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor) {
        Evaluator evaluator = getBasicEvaluator(input, stdout, stderr, monitor);
        URIResolverRegistry reg = URIResolverRegistry.getInstance();

        if (!reg.getRegisteredInputSchemes().contains("project") && !reg.getRegisteredLogicalSchemes().contains("project")) {
            ISourceLocation rootFolder = PathConfig.inferProjectRoot(URIUtil.rootLocation("cwd"));
            if (rootFolder != null) {
                configureProjectEvaluator(evaluator, rootFolder);
            }
        }

        return evaluator;
    }

    public static Evaluator getDefaultEvaluatorForPathConfig(PathConfig pcfg, Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor) {
        var evaluator = getBasicEvaluator(input, stdout, stderr, monitor);
        var reg = URIResolverRegistry.getInstance();

        var srcs = pcfg.getSrcs();
        if (!srcs.isEmpty()) {
            var projectRoot = (ISourceLocation)srcs.get(0);
            var projectName = new RascalManifest().getProjectName(projectRoot);
            reg.registerLogical(new ProjectURIResolver(projectRoot, projectName));
            reg.registerLogical(new TargetURIResolver(projectRoot, projectName));
        }

        stdout.println("Rascal " + RascalManifest.getRascalVersionNumber());
        stdout.println("Rascal search path:");
        for (var srcPath : srcs) {
            var path = MavenRepositoryURIResolver.mavenize((ISourceLocation)srcPath);
            stdout.println("- " + path);
            evaluator.addRascalSearchPath(path);
        }

        //TODO: libs vs libs+target
        var libs = /*projectName == "rascal" ? pcfg.getLibs() :*/ pcfg.getLibsAndTarget();
        stdout.println("Rascal classloader path:");
        for (var lib : libs) {
            var path = (ISourceLocation)lib;
            stdout.println("- " + lib);
            evaluator.addRascalSearchPath(path);
        }
        evaluator.addClassLoader(new SourceLocationClassLoader(libs, ClassLoader.getSystemClassLoader()));

        if (!pcfg.getMessages().isEmpty()) {
            stdout.println("PathConfig messages:");
            Messages.write(pcfg.getMessages(), srcs, stdout);
        }

        return evaluator;
    }

    public static Evaluator getDefaultEvaluatorForLocation(ISourceLocation fileOrFolderInProject, Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor) {
        Evaluator evaluator = getBasicEvaluator(input, stdout, stderr, monitor);

        ISourceLocation rootFolder = PathConfig.inferProjectRoot(fileOrFolderInProject);
        if (rootFolder != null) {
            configureProjectEvaluator(evaluator, rootFolder);
        }

        return evaluator;
    }

    public static void configureProjectEvaluator(Evaluator evaluator, ISourceLocation projectRoot) {
        URIResolverRegistry reg = URIResolverRegistry.getInstance();

        String projectName = new RascalManifest().getProjectName(projectRoot);
        reg.registerLogical(new ProjectURIResolver(projectRoot, projectName));
        reg.registerLogical(new TargetURIResolver(projectRoot, projectName));

        PathConfig pcfg = PathConfig.fromSourceProjectRascalManifest(projectRoot, RascalConfigMode.INTERPRETER, true);

        for (IValue path : pcfg.getSrcs()) {
            evaluator.addRascalSearchPath((ISourceLocation) path); 
        }

        ClassLoader cl = new SourceLocationClassLoader(pcfg.getLibsAndTarget(), ShellEvaluatorFactory.class.getClassLoader());
        evaluator.addClassLoader(cl);  
        
        Messages.write(pcfg.getMessages(), pcfg.getSrcs(), evaluator.getOutPrinter());
    }
}
