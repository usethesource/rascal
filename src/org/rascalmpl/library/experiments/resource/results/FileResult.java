package org.rascalmpl.library.experiments.resource.results;

import java.net.URI;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.ResourceResult;
import org.rascalmpl.library.IO;
import org.rascalmpl.values.ValueFactoryFactory;

public class FileResult extends ResourceResult {

	public FileResult(Type type, IValue value, IEvaluatorContext ctx, ISourceLocation fullURI, String displayURI) {
		super(type, value, ctx, fullURI, displayURI);
		IO io = new IO(ctx.getValueFactory());
		URI uri = URI.create("file://" + fullURI.getURI().getPath());
		this.value = io.readFile(ValueFactoryFactory.getValueFactory().sourceLocation(uri), ctx);
	}

}
