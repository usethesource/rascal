/*
 * Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
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
package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse;

import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;

public abstract class TraverseOnce {

	protected final IValueFactory vf;
	
	public TraverseOnce(IValueFactory vf) {
		this.vf = vf;
	}
	
	/*
	 * traverseTop: traverse the outermost symbol of the subject.
	 */
	
	public IValue traverseTop(IValue subject, final TraversalState tr) {
		IValue res = tr.execute(subject);
		if(tr.isLeavingVisit()){
			throw new ReturnFromTraversalException(res);
		}
		return res;
	}
	
	/*
	 * Datatype specific traverse functions that will be implemented by
	 * rebuilding or non-rebuilding versions.
	 */
	
	abstract IValue traverseStringOnce(IValue subject, final TraversalState tr);

	abstract IValue traverseTupleOnce(IValue subject, final TraversalState tr);

	abstract IValue traverseADTOnce(IValue subject, final TraversalState tr);
	
	abstract IValue traverseConcreteTreeOnce(IValue subject, final TraversalState tr);
	
	abstract IValue traverseMapOnce(IValue subject, final TraversalState tr);

	abstract IValue traverseSetOnce(IValue subject, final TraversalState tr); 

	abstract IValue traverseListOnce(IValue subject, final TraversalState tr);

	abstract IValue traverseNodeOnce(IValue subject, final TraversalState tr);
	
	private IValue traverseOnce(final Type subjectType, final IValue subject,  final TraversalState tr) {

		if (subjectType.isAbstractData()){
			return traverseADTOnce(subject,tr);
		} else if (subjectType.isNode()){
			return traverseNodeOnce(subject, tr);
		} else if(subjectType.isList()){
			return traverseListOnce(subject,  tr);
		} else if(subjectType.isSet()){
			return traverseSetOnce(subject,  tr);
		} else if (subjectType.isMap()) {
			return traverseMapOnce(subject, tr);
		} else if(subjectType.isTuple()){
			return traverseTupleOnce(subject, tr);
		} else {
			return subject;
		}
//		switch(ToplevelType.getToplevelType(subjectType)) {
//		case ADT:
//			return traverseADTOnce(subject,tr);
//		case NODE:
//			return traverseNodeOnce(subject, tr);
//		case LIST:
//			return  traverseListOnce(subject,  tr);
//		case SET:
//			return traverseSetOnce(subject,  tr);
//		case MAP:
//			return traverseMapOnce(subject, tr);
//		case TUPLE:
//			return traverseTupleOnce(subject, tr);
//		default:
//			return subject;
//		}
	}
	
	/*
	 * Here are 16 specializations of the traverseOnce function for the following variants:
	 * - BottomUp or TopDown
	 * - Continuing or Breaking
	 * - FixedPoint or NoFixedPoint
	 * - Concrete or Abstract
	 * 
	 * The unspecialized version of the code is given at the bottom of this file.
	 */
	
	public IValue traverseOnceBottomUpContinuingFixedPointConcrete(IValue subject, final TraversalState tr){
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;

		if(tr.shouldDescentInConcreteValue((ITree)subject)){
			result = traverseConcreteTreeOnce(subject, tr);
		}

		hasMatched = tr.hasMatched();
		hasChanged = tr.hasChanged();

		tr.setMatchedAndChanged(false, false);

		result = traverseTop(result, tr);

		if (tr.hasChanged()) {
			do {
				tr.setMatchedAndChanged(false, false);
				result = traverseTop(result, tr);
			} while (tr.hasChanged());

			tr.setMatchedAndChanged(true, true);
		}

		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
								tr.hasChanged() | hasChanged);
		return result;
	}

	
	public IValue traverseOnceBottomUpContinuingFixedPointAbstract(IValue subject, final TraversalState tr){
		Type subjectType = subject.getType();
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;

		if(tr.shouldDescentInAbstractValue(subject)){
			result = traverseOnce(subjectType, subject, tr);
		}

		hasMatched = tr.hasMatched();
		hasChanged = tr.hasChanged();

		tr.setMatchedAndChanged(false, false);

		result = traverseTop(result, tr);

		if (tr.hasChanged()) {
			do {
				tr.setMatchedAndChanged(false, false);
				result = traverseTop(result, tr);
			} while (tr.hasChanged());

			tr.setMatchedAndChanged(true, true);
		}

		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
				tr.hasChanged() | hasChanged);
		return result;
	}
	
	public IValue traverseOnceBottomUpContinuingNoFixedPointConcrete(IValue subject, final TraversalState tr){
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;

		if(tr.shouldDescentInConcreteValue((ITree)subject)){
			result = traverseConcreteTreeOnce(subject, tr);
		}

		hasMatched = tr.hasMatched();
		hasChanged = tr.hasChanged();

		tr.setMatchedAndChanged(false, false);

		result = traverseTop(result, tr);

		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
								tr.hasChanged() | hasChanged);
		return result;
	}
	
	public IValue traverseOnceBottomUpContinuingNoFixedPointAbstract(final IValue subject, final TraversalState tr){
		final Type subjectType = subject.getType();
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;

		if(tr.shouldDescentInAbstractValue(subject)){
			result = traverseOnce(subjectType, subject, tr);
		}

		hasMatched = tr.hasMatched();
		hasChanged = tr.hasChanged();

		tr.setMatchedAndChanged(false, false);

		result = traverseTop(result, tr);

		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched, tr.hasChanged() | hasChanged);
		return result;
	}
	
	
	public IValue traverseOnceBottomUpBreakingFixedPointConcrete(IValue subject, final TraversalState tr){
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;

		if(tr.shouldDescentInConcreteValue((ITree)subject)){
			result = traverseConcreteTreeOnce(subject, tr);
		}

		if (tr.hasMatched()) {
			return result;
		}

		hasMatched = tr.hasMatched();
		hasChanged = tr.hasChanged();

		tr.setMatchedAndChanged(false, false);

		result = traverseTop(result, tr);

		if (tr.hasChanged()) {
			do {
				tr.setMatchedAndChanged(false, false);
				result = traverseTop(result, tr);
			} while (tr.hasChanged());

			tr.setMatchedAndChanged(true, true);
		}

		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
								tr.hasChanged() | hasChanged);
		return result;
	}
	
	public IValue traverseOnceBottomUpBreakingFixedPointAbstract(IValue subject, final TraversalState tr){
		Type subjectType = subject.getType();
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;

		if(tr.shouldDescentInAbstractValue(subject)){
			result = traverseOnce(subjectType, subject, tr);			
		}

		if (tr.hasMatched()) {
			return result;
		}

		hasMatched = tr.hasMatched();
		hasChanged = tr.hasChanged();

		tr.setMatchedAndChanged(false, false);

		result = traverseTop(result, tr);

		if (tr.hasChanged()) {
			do {
				tr.setMatchedAndChanged(false, false);
				result = traverseTop(result, tr);
			} while (tr.hasChanged());

			tr.setMatchedAndChanged(true, true);
		}

		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
								tr.hasChanged() | hasChanged);
		return result;
	}

	
	
	public IValue traverseOnceBottomUpBreakingNoFixedPointConcrete(IValue subject, final TraversalState tr){
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;

		if(tr.shouldDescentInConcreteValue((ITree)subject)){
			result = traverseConcreteTreeOnce(subject, tr);
		}

		if (tr.hasMatched()) {
			return result;
		}

		hasMatched = tr.hasMatched();
		hasChanged = tr.hasChanged();

		tr.setMatchedAndChanged(false, false);

		result = traverseTop(result, tr);

		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
								tr.hasChanged() | hasChanged);
		return result;
	}
	
	public IValue traverseOnceBottomUpBreakingNoFixedPointAbstract(IValue subject, final TraversalState tr){
		Type subjectType = subject.getType();
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;

		if(tr.shouldDescentInAbstractValue(subject)){
			result = traverseOnce(subjectType, subject, tr);	
		}

		if (tr.hasMatched()) {
			return result;
		}

		hasMatched = tr.hasMatched();
		hasChanged = tr.hasChanged();

		tr.setMatchedAndChanged(false, false);

		result = traverseTop(result, tr);

		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
								tr.hasChanged() | hasChanged);
		return result;
	}
	
	public IValue traverseOnceTopDownContinuingFixedPointConcrete(IValue subject, final TraversalState tr){
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;

		IValue newTop = traverseTop(subject, tr);

		if (tr.hasChanged()) {
			do {
				tr.setChanged(false);

				newTop = traverseTop(newTop, tr);
			} while (tr.hasChanged());
			tr.setChanged(true);
			subject = newTop;
		}
		else {
			subject = newTop;
		}

		hasMatched = tr.hasMatched();
		hasChanged = tr.hasChanged();

		if(tr.shouldDescentInConcreteValue((ITree)subject)){
			result = traverseConcreteTreeOnce(subject, tr);
		}

		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
								tr.hasChanged() | hasChanged);
		return result;
	}
	
	public IValue traverseOnceTopDownContinuingFixedPointAbstract(IValue subject, final TraversalState tr){
		Type subjectType = subject.getType();
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;

		IValue newTop = traverseTop(subject, tr);

		if (tr.hasChanged()) {
			do {
				tr.setChanged(false);

				newTop = traverseTop(newTop, tr);
			} while (tr.hasChanged());
			tr.setChanged(true);
			subject = newTop;
		}
		else {
			subject = newTop;
		}

		hasMatched = tr.hasMatched();
		hasChanged = tr.hasChanged();

		if(tr.shouldDescentInAbstractValue(subject)){
			result = traverseOnce(subjectType, subject, tr);
		}

		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
								tr.hasChanged() | hasChanged);
		return result;
	}
	
	public IValue traverseOnceTopDownContinuingNoFixedPointConcrete(IValue subject, final TraversalState tr){
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;
	
		subject = traverseTop(subject, tr);

		hasMatched = tr.hasMatched();
		hasChanged = tr.hasChanged();

		if(tr.shouldDescentInConcreteValue((ITree)subject)){
			result = traverseConcreteTreeOnce(subject, tr);
		}

		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
								tr.hasChanged() | hasChanged);
		return result;
	}
	
	public IValue traverseOnceTopDownContinuingNoFixedPointAbstract(IValue subject, final TraversalState tr){
		Type subjectType = subject.getType();
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;

		subject = traverseTop(subject, tr);

		hasMatched = tr.hasMatched();
		hasChanged = tr.hasChanged();

		if(tr.shouldDescentInAbstractValue(subject)){
			result = traverseOnce(subjectType, subject, tr);
		}

		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
								tr.hasChanged() | hasChanged);
		return result;
	}
	
	public IValue traverseOnceTopDownBreakingFixedPointConcrete(IValue subject, final TraversalState tr){
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;


		IValue newTop = traverseTop(subject, tr);

		if (tr.hasMatched()) {
			return newTop;
		}
		else if (tr.hasChanged()) {
			do {
				tr.setChanged(false);

				newTop = traverseTop(newTop, tr);
			} while (tr.hasChanged());
			tr.setChanged(true);
			subject = newTop;
		}
		else {
			subject = newTop;
		}

		hasMatched = tr.hasMatched();
		hasChanged = tr.hasChanged();

		if(tr.shouldDescentInConcreteValue((ITree)subject)){
			result = traverseConcreteTreeOnce(subject, tr);
		}

		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
								tr.hasChanged() | hasChanged);
		return result;
	}

	
	public IValue traverseOnceTopDownBreakingFixedPointAbstract(IValue subject, final TraversalState tr){
		Type subjectType = subject.getType();
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;

		IValue newTop = traverseTop(subject, tr);

		if (tr.hasMatched()) {
			return newTop;
		}
		else if (tr.hasChanged()) {
			do {
				tr.setChanged(false);

				newTop = traverseTop(newTop, tr);
			} while (tr.hasChanged());
			tr.setChanged(true);
			subject = newTop;
		}
		else {
			subject = newTop;
		}

		hasMatched = tr.hasMatched();
		hasChanged = tr.hasChanged();

		if(tr.shouldDescentInAbstractValue(subject)){
			result = traverseOnce(subjectType, subject, tr);
		}

		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
								tr.hasChanged() | hasChanged);
		return result;
	}
	
	public IValue traverseOnceTopDownBreakingNoFixedPointConcrete(IValue subject, final TraversalState tr){
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;

		IValue newTop = traverseTop(subject, tr);

		if (tr.hasMatched()) {
			return newTop;
		}
		else {
			subject = newTop;
		}

		hasMatched = tr.hasMatched();
		hasChanged = tr.hasChanged();

		if(tr.shouldDescentInConcreteValue((ITree)subject)){
			result = traverseConcreteTreeOnce(subject, tr);
		}

		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
								tr.hasChanged() | hasChanged);
		return result;
	}

	public IValue traverseOnceTopDownBreakingNoFixedPointAbstract(IValue subject, final TraversalState tr){
		Type subjectType = subject.getType();
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;

		IValue newTop = traverseTop(subject, tr);

		if (tr.hasMatched()) {
			return newTop;
		}
		else {
			subject = newTop;
		}

		hasMatched = tr.hasMatched();
		hasChanged = tr.hasChanged();

		if(tr.shouldDescentInAbstractValue(subject)){
			result = traverseOnce(subjectType, subject, tr);
		}

		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
								tr.hasChanged() | hasChanged);
		return result;
	}

/*
 * The original version of traverseOnce from which the above specialization have been derived
	
	IValue traverseOnce(IValue subject, final TraversalState tr){
		Type subjectType = subject.getType();
		IValue result = subject;

		boolean hasMatched = false;
		boolean hasChanged = false;
		
		if (tr.isTopDown()){
			IValue newTop = traverseTop(subject, tr);

			if (tr.isBreaking() && tr.hasMatched()) {
				return newTop;
			}
			else if (tr.isFixedPoint() && tr.hasChanged()) {
				do {
					tr.setChanged(false);
				
					newTop = traverseTop(newTop, tr);
				} while (tr.hasChanged());
				tr.setChanged(true);
				subject = newTop;
			}
			else {
				subject = newTop;
			}
			
			hasMatched = tr.hasMatched();
			hasChanged = tr.hasChanged();
		}
		
		if(tr.isConcreteMatch()){
			if(tr.shouldDescentInConcreteValue((ITree)subject)){
				result = traverseConcreteTreeOnce(subject, tr);
			}
		} else {
			if(tr.shouldDescentInAbstractValue(subject)){

				if (subjectType.isAbstractData()){
					result = traverseADTOnce(subject,tr);
				} else if (subjectType.isNode()){
					result = traverseNodeOnce(subject, tr);
				} else if(subjectType.isList()){
					result = traverseListOnce(subject,  tr);
				} else if(subjectType.isSet()){
					result = traverseSetOnce(subject,  tr);
				} else if (subjectType.isMap()) {
					result = traverseMapOnce(subject, tr);
				} else if(subjectType.isTuple()){
					result = traverseTupleOnce(subject, tr);
				} else {
					result = subject;
				}
			}
		}
		
		if (tr.isTopDown()) {
			tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
							        tr.hasChanged() | hasChanged);
		}

		if (tr.isBottomUp()) {
			if ((tr.isBreaking() && tr.hasMatched())) {
				return result;
			}

			hasMatched = tr.hasMatched();
			hasChanged = tr.hasChanged();
			
			tr.setMatchedAndChanged(false, false);
			
			result = traverseTop(result, tr);
			
			if (tr.hasChanged() && tr.isFixedPoint()) {
				do {
					tr.setMatchedAndChanged(false, false);
					result = traverseTop(result, tr);
				} while (tr.hasChanged());
				
				tr.setMatchedAndChanged(true, true);
			}
			
			tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
									tr.hasChanged() | hasChanged);
		}
		
		return result;
	}
*/
}

