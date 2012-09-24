package org.rascalmpl.library.experiments.resource.results;

import java.net.URI;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.ResourceResult;
import org.rascalmpl.library.experiments.resource.results.buffers.CharStreamFiller;
import org.rascalmpl.library.experiments.resource.results.buffers.LazyList;
import org.rascalmpl.uri.FileURIResolver;
import org.rascalmpl.values.ValueFactoryFactory;

public class CharStreamResult extends ResourceResult {

	public CharStreamResult(Type type, IValue value, IEvaluatorContext ctx, ISourceLocation fullURI, String displayURI) {
		super(type, value, ctx, fullURI, displayURI);
		URI uri = FileURIResolver.constructFileURI(fullURI.getURI().getPath());
		this.value = new LazyList(80, new CharStreamFiller(ValueFactoryFactory.getValueFactory().sourceLocation(uri), ctx), type.getElementType());
	}

}
