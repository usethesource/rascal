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
        setupProjectResolver(projectRoot, monitor);
        return getDefaultEvaluatorForPathConfig(projectRoot, pcfg, input, stdout, stderr, monitor, ModuleEnvironment.SHELL_MODULE);
    }
    
    private static Evaluator getDefaultEvaluatorForPathConfig(ISourceLocation projectRoot, PathConfig pcfg, Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor, String rootEnvironment) {
        var evaluator = getBasicEvaluator(input, stdout, stderr, monitor, rootEnvironment);
        
        for (var srcPath : pcfg.getSrcs()) {
            // TODO: question why mavenize so late here and not while configuring the pathConfig?
            evaluator.addRascalSearchPath(MavenRepositoryURIResolver.mavenize((ISourceLocation)srcPath));
        }

        var isRascal = projectRoot != null && new RascalManifest().getProjectName(projectRoot).equals("rascal");
        var libs = isRascal ? pcfg.getLibs() : pcfg.getLibsAndTarget();
        for (var lib : libs) {
            evaluator.addRascalSearchPath((ISourceLocation)lib);
        }
        evaluator.addClassLoader(new SourceLocationClassLoader(libs, ClassLoader.getSystemClassLoader()));

        pcfg.reportConfigurationInfo();
        
        if (!pcfg.getMessages().isEmpty()) {
            if (monitor instanceof IDEServices) {
                ((IDEServices) monitor).registerDiagnostics(pcfg.getMessages(), pcfg.getProjectRoot());
            } 
            else {
                Messages.write(pcfg.getMessages(), pcfg.getProjectRoot(), stdout);
            }
        }

        return evaluator;
    }

    private static void setupProjectResolver(ISourceLocation projectRoot, IRascalMonitor monitor) {
        if (monitor instanceof IDEServices) {
            registerProjectAndTargetResolver(((IDEServices) monitor)::resolveProjectLocation);
        } else {
            registerProjectAndTargetResolver(projectRoot);
        }
    }

    public static Evaluator getDefaultEvaluatorForLocation(ISourceLocation projectFile, Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor) {
        return getDefaultEvaluatorForLocation(projectFile, input, stdout, stderr, monitor, ModuleEnvironment.SHELL_MODULE);
    }

    public static Evaluator getDefaultEvaluatorForLocation(ISourceLocation projectFile, Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor, String rootEnvironment) {
        var projectRoot = PathConfig.inferProjectRoot(projectFile);
        setupProjectResolver(projectRoot, monitor);
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

    private static void registerProjectAndTargetResolver(Function<ISourceLocation,ISourceLocation> resolver) {
        var reg = URIResolverRegistry.getInstance();
        reg.registerLogical(new IDEProjectURIResolver(resolver));
        reg.registerLogical(new IDETargetURIResolver(resolver));
    }

}
