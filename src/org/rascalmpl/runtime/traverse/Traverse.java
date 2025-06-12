/*
 * Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.runtime.traverse;

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