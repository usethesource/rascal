package org.rascalmpl.library.lang.java.annotations;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public class JavacAnnotationsOptionsProvider implements AnnotationsOptionsProvider {
	private final StandardJavaFileManager fm;

	public JavacAnnotationsOptionsProvider() {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		
		if (compiler != null) {
			this.fm = compiler.getStandardFileManager(null, null, null);
		}
		else {
			this.fm = null;
		}
	}
	
	@Override
	public Iterable<? extends File> getSourcePath() {
		return fm != null ? fm.getLocation(StandardLocation.SOURCE_PATH) : Collections.emptyList();
	}

	@Override
	public Iterable<? extends File> getClassPath() {
		return fm != null ? fm.getLocation(StandardLocation.CLASS_PATH) : Collections.emptyList();
	}

	@Override
	public Map<String, String> getOptions() {
		return Collections.emptyMap();
	}
}
