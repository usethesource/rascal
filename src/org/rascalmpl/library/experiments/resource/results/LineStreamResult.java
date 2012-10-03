package org.rascalmpl.library.experiments.resource.results;

import java.net.URI;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.ResourceResult;
import org.rascalmpl.library.experiments.resource.results.buffers.LazyList;
import org.rascalmpl.library.experiments.resource.results.buffers.LineStreamFiller;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;

public class LineStreamResult extends ResourceResult {

	public LineStreamResult(Type type, IValue value, IEvaluatorContext ctx, ISourceLocation fullURI, String displayURI) {
		super(type, value, ctx, fullURI, displayURI);
		String str = fullURI.getURI().getPath();
		String newHost = str.substring(1,str.indexOf("/",1));
		String newPath = str.substring(str.indexOf("/",1)+1);
		URI uri = URIUtil.assumeCorrect(newHost, "", newPath);
		this.value = new LazyList(25, new LineStreamFiller(ValueFactoryFactory.getValueFactory().sourceLocation(uri), ctx), type.getElementType());
	}

}
