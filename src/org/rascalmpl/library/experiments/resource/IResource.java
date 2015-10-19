/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.experiments.resource;

import java.util.List;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.ResourceResult;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.type.Type;

public interface IResource {

	public ResourceResult createResource(IEvaluatorContext ctx, ISourceLocation uri, Type t);
	
	public Type getResourceType(IEvaluatorContext ctx, ISourceLocation uri);
	
	public String getProviderString();
	
	public List<String> getPathItems();
	
	public List<String> getQueryParameters();
	
	public List<Type> getQueryParameterTypes();
	
	public List<String> getOptionalQueryParameters();
	
	public List<Type> getOptionalQueryParameterTypes();
}
