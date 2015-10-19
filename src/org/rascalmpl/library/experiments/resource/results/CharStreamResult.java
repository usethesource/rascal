/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.experiments.resource.results;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.ResourceResult;
import org.rascalmpl.library.experiments.resource.results.buffers.CharStreamFiller;
import org.rascalmpl.library.experiments.resource.results.buffers.LazyList;
import org.rascalmpl.uri.file.FileURIResolver;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;

public class CharStreamResult extends ResourceResult {

	public CharStreamResult(Type type, IValue value, IEvaluatorContext ctx, ISourceLocation fullURI, String displayURI) {
		super(type, value, ctx, fullURI, displayURI);
		ISourceLocation uri = FileURIResolver.constructFileURI(fullURI.getPath());
		this.value = new LazyList(80, new CharStreamFiller(uri, ctx), type.getElementType());
	}

}
