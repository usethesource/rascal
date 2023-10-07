package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;

public class Traverse {	
	private final IValueFactory vf;

	public Traverse(IValueFactory vf) {
		this.vf = vf;
	}
	
	public IValue traverse(DIRECTION direction, PROGRESS progress, FIXEDPOINT fixedpoint, REBUILD rebuild,  IDescendantDescriptor descriptor, IValue subject, IVisitFunction phi) {
		
		Type subjectType = subject.getType();
		TraversalState tr =  new TraversalState(phi, descriptor);
		
		ITraverseSpecialization traversals = rebuild == REBUILD.Yes ? new TraverseOnceRebuild(vf) :new TraverseOnceNoRebuild(vf);

		if(subjectType.isString()){
			return traversals.traverseStringOnce(subject, tr);
		}

		switch(direction){
		case BottomUp:
			switch(progress){
			case Continuing:
				switch(fixedpoint){
				case Yes:
					if(descriptor.isConcreteMatch()){
						tr.traverse = (IValue s, TraversalState t) -> traversals.traverseOnceBottomUpContinuingFixedPointConcrete(s,t);
						return tr.traverse.once(subject, tr);
					} else {
						tr.traverse = (IValue s, TraversalState t) -> traversals.traverseOnceBottomUpContinuingFixedPointAbstract(s, t);
						return tr.traverse.once(subject, tr);
					}
				case No:
					if(descriptor.isConcreteMatch()){
						tr.traverse = (IValue s, TraversalState t) -> traversals.traverseOnceBottomUpContinuingNoFixedPointConcrete(s, t);
						return tr.traverse.once(subject, tr);
					} else {
						tr.traverse = (IValue s, TraversalState t) -> traversals.traverseOnceBottomUpContinuingNoFixedPointAbstract(s, t);
						return tr.traverse.once(subject, tr);
					}
				}
				break;
			case Breaking:
				switch(fixedpoint){
				case Yes:
					if(descriptor.isConcreteMatch()){
						tr.traverse = (IValue s, TraversalState t) -> traversals.traverseOnceBottomUpBreakingFixedPointConcrete(s, t);
						return tr.traverse.once(subject, tr);
					} else {
						tr.traverse = (IValue s, TraversalState t) -> traversals.traverseOnceBottomUpBreakingFixedPointAbstract(s, t);
						return tr.traverse.once(subject, tr);
					}
				case No:
					if(descriptor.isConcreteMatch()){
						tr.traverse = (IValue s, TraversalState t) -> traversals.traverseOnceBottomUpBreakingNoFixedPointConcrete(subject, tr);
						return tr.traverse.once(subject, tr);
					} else {
						tr.traverse = (IValue s, TraversalState t) -> traversals.traverseOnceBottomUpBreakingNoFixedPointAbstract(s, t);
						return tr.traverse.once(subject, tr);
					}
				}
			}
			break;
		case TopDown:
			switch(progress){
			case Continuing:
				switch(fixedpoint){
				case Yes:
					if(descriptor.isConcreteMatch()){
						tr.traverse = (IValue s, TraversalState t) -> traversals.traverseOnceTopDownContinuingFixedPointConcrete(s, t);
						return tr.traverse.once(subject, tr);
					} else {
						tr.traverse = (IValue s, TraversalState t) -> traversals.traverseOnceTopDownContinuingFixedPointAbstract(s, t);
						return tr.traverse.once(subject, tr);
					}
				case No:
					if(descriptor.isConcreteMatch()){
						tr.traverse = (IValue s, TraversalState t) -> traversals.traverseOnceTopDownContinuingNoFixedPointConcrete(s, t);
						return tr.traverse.once(subject, tr);
					} else {
						tr.traverse = (IValue s, TraversalState t) -> traversals.traverseOnceTopDownContinuingNoFixedPointAbstract(s, t);
						return tr.traverse.once(subject, tr);
					}
				}
				break;
			case Breaking:
				switch(fixedpoint){
				case Yes:
					if(descriptor.isConcreteMatch()){
						tr.traverse = (IValue s, TraversalState t) -> traversals.traverseOnceTopDownBreakingFixedPointConcrete(s, t);
						return tr.traverse.once(subject, tr);
					} else {
						tr.traverse = (IValue s, TraversalState t) -> traversals.traverseOnceTopDownBreakingFixedPointAbstract(s, t);
						return tr.traverse.once(subject, tr);
					}
				case No:
					if(descriptor.isConcreteMatch()){
						tr.traverse = (IValue s, TraversalState t) -> traversals.traverseOnceTopDownBreakingNoFixedPointConcrete(s, t);
						return tr.traverse.once(subject, tr);
					} else {
						tr.traverse = (IValue s, TraversalState t) -> traversals.traverseOnceTopDownBreakingNoFixedPointAbstract(s, t);
						return tr.traverse.once(subject, tr);
					}
				}
			}
		}
		throw new RuntimeException("Traversal specialization not found: " + direction + ", "
				+ progress + ", " + fixedpoint + ", " + rebuild + ", concreteMatch = " + descriptor.isConcreteMatch());
	}
}