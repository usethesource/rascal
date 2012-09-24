package org.rascalmpl.library.experiments.resource.results;

import java.net.URI;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.ResourceResult;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.uri.FileURIResolver;
import org.rascalmpl.values.ValueFactoryFactory;

public class FileResult extends ResourceResult {

	public FileResult(Type type, IValue value, IEvaluatorContext ctx, ISourceLocation fullURI, String displayURI) {
		super(type, value, ctx, fullURI, displayURI);
		Prelude prelude = new Prelude(ctx.getValueFactory());
		URI uri = FileURIResolver.constructFileURI(fullURI.getURI().getPath());
		this.value = prelude.readFile(ValueFactoryFactory.getValueFactory().sourceLocation(uri), ctx);
	}

}
