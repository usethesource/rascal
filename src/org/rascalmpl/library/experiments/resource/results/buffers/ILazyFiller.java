package org.rascalmpl.library.experiments.resource.results.buffers;

import org.eclipse.imp.pdb.facts.IValue;

public interface ILazyFiller {

	public IValue[] refill(int pageSize);
	
	public ILazyFiller getBufferedFiller();	
}
