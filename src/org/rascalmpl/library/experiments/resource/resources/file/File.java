/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.experiments.resource.resources.file;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.ResourceResult;
import org.rascalmpl.library.experiments.resource.resources.BaseResource;
import org.rascalmpl.library.experiments.resource.results.FileResult;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

public class File extends BaseResource {

	@Override
	public ResourceResult createResource(IEvaluatorContext ctx, ISourceLocation uri, Type t) {
		return new FileResult(t, null, ctx, uri, "file");
	}

	@Override
	public Type getResourceType(IEvaluatorContext ctx, ISourceLocation uri) {
		return TypeFactory.getInstance().stringType();
	}

	@Override
	public String getProviderString() {
		return "file";
	}
}
