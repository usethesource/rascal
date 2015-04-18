package org.rascalmpl.library.lang.java.annotations;

import java.io.File;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;

public interface AnnotationsOptionsProvider {
	void init(ProcessingEnvironment processingEnv);
	Iterable<? extends File> getSourcePath();
	Iterable<? extends File> getClassPath();
	Iterable<? extends File> getRascalPath();
	Map<String, String>      getOptions();
}