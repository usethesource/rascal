package org.rascalmpl.library.lang.java.annotations;

import java.io.File;
import java.util.Map;

public interface AnnotationsOptionsProvider {
	Iterable<? extends File> getSourcePath();
	Iterable<? extends File> getClassPath();
	Iterable<? extends File> getRascalPath();
	Map<String, String>      getOptions();
}