package org.rascalmpl.library.experiments.resource.resources.file;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.ResourceResult;
import org.rascalmpl.library.experiments.resource.resources.BaseResource;
import org.rascalmpl.library.experiments.resource.results.FileResult;

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
