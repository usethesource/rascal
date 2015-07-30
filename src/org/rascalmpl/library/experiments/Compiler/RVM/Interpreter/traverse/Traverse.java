package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CompilerError;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.FunctionInstance;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Reference;

public class Traverse {
	public enum DIRECTION  {BottomUp, TopDown}	// Parameters for traversing trees
	public enum FIXEDPOINT {Yes, No}
	public enum PROGRESS   {Continuing, Breaking}
	public enum REBUILD    {Yes, No};
	
	private final IValueFactory vf;

	public Traverse(IValueFactory vf) {
		this.vf = vf;
	}
	
	public IValue traverse(DIRECTION direction, PROGRESS progress, FIXEDPOINT fixedpoint, REBUILD rebuild, IValue subject, FunctionInstance phi, Reference refMatched, Reference refChanged, Reference refLeaveVisit, Reference refBegin, Reference refEnd, RVM rvm, DescendantDescriptor descriptor) {

		refMatched.setValue(vf.bool(false));
		refChanged.setValue(vf.bool(false));
		refLeaveVisit.setValue(vf.bool(false));
		
		Type subjectType = subject.getType();
		TraversalState tr =  new TraversalState(rvm, phi, refMatched, refChanged, refLeaveVisit, refBegin, refEnd, descriptor);
		
		ITraverseSpecialization traversals = rebuild == REBUILD.Yes ? new TraverseOnceRebuild(vf) :new TraverseOnceNoRebuild(vf);
		
		try {
			if(subjectType.isString()){
				IValue result = traversals.traverseStringOnce(subject, tr);
				return result;
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
			throw new CompilerError("Traversal specialization not found: " + direction + ", "
					+ progress + ", " + fixedpoint + ", " + rebuild + ", concreteMatch = " + descriptor.isConcreteMatch());

		} catch (ReturnFromTraversalException e) {
			return e.getValue();
		}

	}
}