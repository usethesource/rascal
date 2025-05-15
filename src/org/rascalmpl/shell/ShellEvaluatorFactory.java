package org.rascalmpl.shell;

import java.io.PrintWriter;
import java.io.Reader;

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

    public static Evaluator getDefaultEvaluatorForPathConfig(PathConfig pcfg, ISourceLocation projectFile, Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor) {
        return getDefaultEvaluatorForPathConfig(pcfg, projectFile, input, stdout, stderr, monitor, ModuleEnvironment.SHELL_MODULE);
    }
    
    public static Evaluator getDefaultEvaluatorForPathConfig(PathConfig pcfg, ISourceLocation projectFile, Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor, String rootEnvironment) {
        var evaluator = getBasicEvaluator(input, stdout, stderr, monitor, rootEnvironment);
        var reg = URIResolverRegistry.getInstance();

        var projectRoot = PathConfig.inferProjectRoot(projectFile);
        var projectName = new RascalManifest().getProjectName(projectRoot);
        reg.registerLogical(new ProjectURIResolver(projectRoot, projectName));
        reg.registerLogical(new TargetURIResolver(projectRoot, projectName));

        stdout.println("Rascal " + RascalManifest.getRascalVersionNumber());
        stdout.println("Rascal search path:");
        for (var srcPath : pcfg.getSrcs()) {
            var path = MavenRepositoryURIResolver.mavenize((ISourceLocation)srcPath);
            stdout.println("- " + path);
            evaluator.addRascalSearchPath(path);
        }

        var libs = pcfg.getLibsAndTarget();
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
        var pcfg = PathConfig.fromSourceProjectMemberRascalManifest(projectFile, RascalConfigMode.INTERPRETER);
        return getDefaultEvaluatorForPathConfig(pcfg, projectFile, input, stdout, stderr, monitor, rootEnvironment);
    }

}
