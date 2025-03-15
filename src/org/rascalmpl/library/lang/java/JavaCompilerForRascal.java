package org.rascalmpl.library.lang.java;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import org.rascalmpl.interpreter.utils.JavaCompiler;
import org.rascalmpl.interpreter.utils.JavaCompilerException;
import org.rascalmpl.interpreter.utils.JavaFileObjectImpl;
import org.rascalmpl.library.Messages;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.SourceLocationClassLoader;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class JavaCompilerForRascal {
	
	private final IValueFactory vf;
	
	public JavaCompilerForRascal(IValueFactory vf) {
		this.vf = vf;
	}

	private ISourceLocation safeResolve(ISourceLocation l) {
		try {
			return URIResolverRegistry.getInstance().logicalToPhysical(l);
		}
		catch (IOException e) {
			return l;
		}
	}	

	/**
	 * Here we float on the precondition that every element of the classpath
	 * must point to an absolute file:/// folder or jar file
	 */
	private String resolveDependenciesAsClasspath(IList classpath) {
		return classpath.stream()
			.map(ISourceLocation.class::cast)
			.map(this::safeResolve)
			.filter(l -> l.getScheme().equals("file"))
			.map(l -> l.getPath())
			.collect(Collectors.joining(File.pathSeparator));
	}

	/**
	 * Main entry method for calling the JavaC compiler from Rascal
	 * @param sourcesMap  map from fully qualifiedname to the source code of a corresponding Java file
	 * @param bin        target folder/jar
	 * @param classpath  list of depedencies (folders and jars containing .class files)
	 * @return list of error messages and warnings by the Java compiler
	 */
	public IList compileJava(ISet sourcesMap, ISourceLocation bin, IList classpath) {
		var cl = new SourceLocationClassLoader(classpath, System.class.getClassLoader());

		try {
			// watch out, if you start sharing this compiler, classes will not be able to reload
			List<String> commandline = Arrays.asList(new String[] {"-proc:none", "-cp", resolveDependenciesAsClasspath(classpath)});
			JavaCompiler<?> javaCompiler = new JavaCompiler<Object>(cl, null, commandline);
			var errors = new DiagnosticCollector<JavaFileObject>();

			javaCompiler.compileTo(sourcesMap, cl, bin, errors);
			
			return convertDiagnostics(errors);
		} 
		catch (JavaCompilerException e) {
			return convertDiagnostics(e.getDiagnostics());
		}
		catch (URISyntaxException e) {
			return vf.list(Messages.error(e.getMessage(), URIUtil.unknownLocation()));
		}
	}

	private IList convertDiagnostics(DiagnosticCollector<JavaFileObject> errors) {
		return errors.getDiagnostics().stream()
			.map(this::convertDiagnostic)
			.collect(vf.listWriter());
	}

	private IValue convertDiagnostic(Diagnostic<? extends JavaFileObject> d) {
		ISourceLocation loc = convertToLoc(d);
		switch (d.getKind()) {
			case ERROR:
				return Messages.error(d.getMessage(null), loc);
			case WARNING:
			case MANDATORY_WARNING:
				return Messages.warning(d.getMessage(null), loc);
			case NOTE:
			case OTHER:
			default:
				return Messages.info(d.getMessage(null), loc);
		}
	}

	private ISourceLocation convertToLoc(Diagnostic<? extends JavaFileObject> d) {
		ISourceLocation uri = ((JavaFileObjectImpl) d.getSource()).getSloc();
		var offset = (int) d.getStartPosition();
		if (offset >= 0) {
			return vf.sourceLocation(uri, offset, ((int) d.getEndPosition()) - offset);
		}
		else {
			return uri;
		}
	}
				
}
