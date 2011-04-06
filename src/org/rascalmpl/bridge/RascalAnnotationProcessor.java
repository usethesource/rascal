/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
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
