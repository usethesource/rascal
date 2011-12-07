package org.rascalmpl.library.experiments.resource;

import java.util.List;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.ResourceResult;

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
