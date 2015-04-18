package org.rascalmpl.library.lang.java.annotations;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public class JavacAnnotationsOptionsProvider implements AnnotationsOptionsProvider {
	private StandardJavaFileManager fm;

	@Override
	public Iterable<? extends File> getSourcePath() {
		return fm != null ? fm.getLocation(StandardLocation.SOURCE_PATH) : Collections.<File>emptyList();
	}

	@Override
	public Iterable<? extends File> getRascalPath() {
		return getSourcePath();
	}
	
	@Override
	public Iterable<? extends File> getClassPath() {
		return fm != null ? fm.getLocation(StandardLocation.CLASS_PATH) : Collections.<File>emptyList();
	}

	@Override
	public Map<String, String> getOptions() {
		return Collections.<String,String>emptyMap();
	}

	@Override
	public void init(ProcessingEnvironment processingEnv) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		
		if (compiler != null) {
			fm = compiler.getStandardFileManager(null, null, null);
		}
		else {
			fm = null;
		}
	}
}
