package org.rascalmpl.parser.uptr.preprocessing;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.preprocessing.ExpectBuilder;
import org.rascalmpl.parser.gtd.util.IntegerMap;
import org.rascalmpl.parser.gtd.util.ObjectKeyedIntegerMap;

public class RascalExpectBuilder extends ExpectBuilder<IConstructor>{
	private final ObjectKeyedIntegerMap<Object> robustProductions;

	public RascalExpectBuilder(IntegerMap resultStoreMappings, ObjectKeyedIntegerMap<Object> robustProductions){
		super(resultStoreMappings);
		
		this.robustProductions = robustProductions;
	}

	protected boolean isSharable(IConstructor production){
		return (robustProductions != null && !robustProductions.contains(production));
	}
}
