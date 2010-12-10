package org.rascalmpl.bridge;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes("org.rascalmpl.bridge.Rascal")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
/**
 * @see {@link Rascal}
 */
public class RascalAnnotationProcessor extends AbstractProcessor {
	@Override
	public boolean process(Set<? extends TypeElement> classes, RoundEnvironment env) {
		for (TypeElement e : classes) {
			Rascal r = e.getAnnotation(Rascal.class);
			processingEnv.getMessager().printMessage(Kind.NOTE, "The Rascal annotation is not implemented yet: " + e.getClass().getName());
		}
		return true;
	}
}
