package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse;

import io.usethesource.vallang.IValue;

public interface ITraverseSpecialization {
	
	IValue traverseOnceBottomUpContinuingFixedPointConcrete(IValue subject, TraversalState tr);

	IValue traverseOnceBottomUpContinuingFixedPointAbstract(IValue subject, TraversalState tr);

	IValue traverseOnceBottomUpContinuingNoFixedPointConcrete(IValue subject, TraversalState tr);

	IValue traverseOnceBottomUpContinuingNoFixedPointAbstract(IValue subject, TraversalState tr);

	IValue traverseOnceBottomUpBreakingFixedPointConcrete(IValue subject, TraversalState tr);

	IValue traverseOnceBottomUpBreakingFixedPointAbstract(IValue subject, TraversalState tr);

	IValue traverseOnceBottomUpBreakingNoFixedPointConcrete(IValue subject, TraversalState tr);

	IValue traverseOnceBottomUpBreakingNoFixedPointAbstract(IValue subject, TraversalState tr);

	IValue traverseOnceTopDownContinuingFixedPointConcrete(IValue subject, TraversalState tr);
	
	IValue traverseOnceTopDownContinuingFixedPointAbstract(IValue subject, TraversalState tr);

	IValue traverseOnceTopDownContinuingNoFixedPointConcrete(IValue subject, TraversalState tr);

	IValue traverseOnceTopDownContinuingNoFixedPointAbstract(IValue subject, TraversalState tr);

	IValue traverseOnceTopDownBreakingFixedPointConcrete(IValue subject, TraversalState tr);

	IValue traverseOnceTopDownBreakingFixedPointAbstract(IValue subject, TraversalState tr);
	
	IValue traverseOnceTopDownBreakingNoFixedPointConcrete(IValue subject, TraversalState tr);

	IValue traverseOnceTopDownBreakingNoFixedPointAbstract(IValue subject, TraversalState tr);
	
	IValue traverseStringOnce(IValue subject, TraversalState tr);
}
