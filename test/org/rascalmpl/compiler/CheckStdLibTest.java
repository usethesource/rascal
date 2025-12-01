package org.rascalmpl.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.jline.terminal.impl.DumbTerminal;
import org.junit.Test;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.shell.RascalCompile;
import org.rascalmpl.test.infrastructure.RascalJunitConsoleMonitor;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.file.MavenRepositoryURIResolver;
import org.rascalmpl.uri.jar.JarURIResolver;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class CheckStdLibTest {
    @Test
    public void checkerSupportsStandardLib() throws IOException, URISyntaxException {
        try (var term = new DumbTerminal(InputStream.nullInputStream(), System.out)) {
            var vf = ValueFactoryFactory.getValueFactory();
            simulateShadedTypePal();


            var args = new HashMap<String,IValue>();
            var preludeModule = URIUtil.createFromURI(URIUtil.fromURL(Prelude.class.getResource("/org/rascalmpl/library/Prelude.rsc")).toString());
            var stdLibRoot = URIUtil.getParentLocation(preludeModule);
            args.put("modules", calculateModules(vf, stdLibRoot));
            args.put("pcfg", buildPathConfig(stdLibRoot));
            args.put("logPathConfig", vf.bool(true));
            args.put("verbose", vf.bool(false));

            args.put("parallel", vf.bool(true));
            args.put("parallelMax", vf.integer(4));
            args.put("parallelPreChecks", vf.list(preludeModule));

            var monitor = RascalJunitConsoleMonitor.getInstance();
            assertEquals("Standard library checker should not find errors", 0, RascalCompile.runMain(args, term, monitor, term.writer(), term.writer()));
        }
        finally {
            cleanSimulatedTypePal();
        }
    }

    private void simulateShadedTypePal() throws IOException {
        // the main runner assumes it's running inside of a shaded jar
        // so intead we're going to copy the contents of typepal to 
        // the target folder of where this test is running
        var typepal = PathConfig.resolveProjectOnClasspath("typepal");
        typepal = MavenRepositoryURIResolver.mavenize(typepal);
        typepal = JarURIResolver.jarify(typepal);
        
        URIResolverRegistry.getInstance().copy(typepal, URIUtil.getChildLocation(PathConfig.resolveCurrentRascalRuntimeJar(), "org/rascalmpl/typepal"), true, true);
    }

    private void cleanSimulatedTypePal() throws IOException {
        URIResolverRegistry.getInstance().remove(URIUtil.getChildLocation(PathConfig.resolveCurrentRascalRuntimeJar(), "org/rascalmpl/typepal"), true);
    }

    private IValue buildPathConfig(ISourceLocation stdLibRoot) throws IOException {
        var projectRoot = stdLibRoot;
        while (!projectRoot.getPath().endsWith("rascal")) {
            var newRoot = URIUtil.getParentLocation(projectRoot);
            if (newRoot.equals(projectRoot)) {
                break;
            }
            projectRoot = newRoot;
            
        }
        var pcfg = new PathConfig(projectRoot);
        pcfg = pcfg.addSourceLoc(stdLibRoot);
        var binDir = URIUtil.correctLocation("memory", "std-lib-test", "/bins/");
        URIResolverRegistry.getInstance().mkDirectory(binDir);
        pcfg = pcfg.setBin(binDir);
        return pcfg.asConstructor();
    }

    private IList calculateModules(IValueFactory vf, ISourceLocation stdLibRoot) throws IOException {
        var result = vf.listWriter();
        findModules(stdLibRoot, result, URIResolverRegistry.getInstance());
        var list = result.done();
        return list;
    }

    private void findModules(ISourceLocation dir, IListWriter result, URIResolverRegistry reg) throws IOException {
        switch (URIUtil.getLocationName(dir)) {
            case "experiments":
            case "resource":
            case "tests":
                return;
        }
        for (var e : reg.list(dir)) {
            if (reg.isDirectory(e)) {
                findModules(e, result, reg);
            }
            else if (URIUtil.getExtension(e).equals("rsc")) {
                result.append(e);
            }
        }
    }

    
}