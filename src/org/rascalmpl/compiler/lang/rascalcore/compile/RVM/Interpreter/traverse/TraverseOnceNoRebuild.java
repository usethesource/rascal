package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse;

import java.util.Iterator;
import java.util.Map.Entry;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.IWithKeywordParameters;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.TreeAdapter;

public class TraverseOnceNoRebuild extends TraverseOnce implements ITraverseSpecialization {
	
	public TraverseOnceNoRebuild(IValueFactory vf) {
		super(vf);
	}
	
	@Override
	public
	IValue traverseTupleOnce(IValue subject, final TraversalState tr) {
		ITuple tuple = (ITuple) subject;
		int arity = tuple.arity();

		boolean hasMatched = false;
		boolean hasChanged = false;

		for (int i = 0; i < arity; i++){
			tr.setMatchedAndChanged(false, false);
			tr.traverse.once(tuple.get(i), tr);
			hasMatched |= tr.hasMatched();
			hasChanged |= tr.hasChanged();
		}

		tr.setMatchedAndChanged(hasMatched, hasChanged);
		return subject;
	}
	
	@Override
	public
	IValue traverseADTOnce(IValue subject, final TraversalState tr) {
		IConstructor cons = (IConstructor)subject;
		boolean hasKwParams = false;
		int arity = cons.arity();

		if (cons.mayHaveKeywordParameters() && cons.asWithKeywordParameters().hasParameters()) {
			hasKwParams = true;
		}
		if (arity == 0 && !hasKwParams) {
			return subject; // constants have no children to traverse into
		} 

		boolean hasChanged = false;
		boolean hasMatched = false;

		for (int i = 0; i < arity; i++){
			IValue child = cons.get(i);
			tr.setMatchedAndChanged(false, false);
			child = tr.traverse.once(child, tr);
			hasChanged |= tr.hasChanged();
			hasMatched |= tr.hasMatched();
		}
		if (hasKwParams) {
			IWithKeywordParameters<? extends INode> consKw = cons.asWithKeywordParameters();
			for (String kwName : consKw.getParameterNames()) {
				IValue val = consKw.getParameter(kwName);
				tr.setMatchedAndChanged(false, false);
				tr.traverse.once(val, tr);
				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
			}
		}
		tr.setMatchedAndChanged(hasMatched, hasChanged);

		return subject;
	}
	
	@Override
	public
	IValue traverseConcreteTreeOnce(IValue subject, final TraversalState tr) {
		ITree tree = (ITree)subject;

		// Only visit non-layout nodes in argument list

		IList list = TreeAdapter.getArgs(tree);
		int len = list.length();

		if (len > 0) {
			boolean hasChanged = false;
			boolean hasMatched = false;
			boolean isTop = TreeAdapter.isTop(tree);

			if (isTop) {
				tr.setMatchedAndChanged(false, false);
				tr.traverse.once(list.get(1), tr);

				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
			} 
			else { 
				for (int i = 0; i < len; i++){
					IValue elem = list.get(i);
					if (i % 2 == 0) { // Recursion to all non-layout elements
						tr.setMatchedAndChanged(false, false);
						tr.traverse.once(elem, tr);
						hasChanged |= tr.hasChanged();
						hasMatched |= tr.hasMatched();
					}
				}
			}

			tr.setMatchedAndChanged(hasMatched, hasChanged);
		}

		return subject;
	} 

	@Override
	public
	IValue traverseMapOnce(IValue subject, final TraversalState tr) {
		IMap map = (IMap) subject;
		if(!map.isEmpty()){
			Iterator<Entry<IValue,IValue>> iter = map.entryIterator();
			boolean hasChanged = false;
			boolean hasMatched = false;

			while (iter.hasNext()) {
				Entry<IValue,IValue> entry = iter.next();
				tr.setMatchedAndChanged(false, false);
				tr.traverse.once(entry.getKey(), tr);
				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
				tr.setMatchedAndChanged(false, false);
				tr.traverse.once(entry.getValue(), tr);
				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
			}
			tr.setMatchedAndChanged(hasMatched, hasChanged);

			return subject;

		} else {
			return subject;
		}
	}

	@Override
	public
	IValue traverseSetOnce(IValue subject, final TraversalState tr) {
		ISet set = (ISet) subject;
		if(!set.isEmpty()){
			boolean hasChanged = false;
			boolean hasMatched = false;

			for (IValue v : set) {
				tr.setMatchedAndChanged(false, false);
				tr.traverse.once(v, tr);
				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
			}

			tr.setMatchedAndChanged(hasMatched, hasChanged);
			return subject;
		} else {
			return subject;
		}
	}

	@Override
	public
	IValue traverseListOnce(IValue subject, final TraversalState tr) {
		IList list = (IList) subject;
		int len = list.length();
		if (len > 0){
			boolean hasChanged = false;
			boolean hasMatched = false;

			for (int i = 0; i < len; i++){
				IValue elem = list.get(i);
				tr.setMatchedAndChanged(false, false);
				tr.traverse.once(elem, tr);
				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
			}

			tr.setMatchedAndChanged(hasMatched, hasChanged);
			return subject;

		} else {
			return subject;
		}
	}

	@Override
	public
	IValue traverseNodeOnce(IValue subject, final TraversalState tr) {
		IValue result= subject;
		INode node = (INode)subject;
		int arity = node.arity();
		boolean hasKwParams = false;
		
		if(node.mayHaveKeywordParameters() && node.asWithKeywordParameters().hasParameters()){
			hasKwParams = true;
		}
		
		if (arity == 0 && !hasKwParams){
			result = subject;
		} 
		
		boolean hasChanged = false;
		boolean hasMatched = false;

		for (int i = 0; i < arity; i++){
			IValue child = node.get(i);
			tr.setMatchedAndChanged(false, false);
			tr.traverse.once(child, tr);
			hasChanged |= tr.hasChanged();
			hasMatched |= tr.hasMatched();
		}
		if (hasKwParams) {
			IWithKeywordParameters<? extends INode> nodeKw = node.asWithKeywordParameters();
			for (String kwName : nodeKw.getParameterNames()) {
				IValue val2 = nodeKw.getParameter(kwName);
				tr.setMatchedAndChanged(false, false);
				tr.traverse.once(val2, tr);
				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
			}
		}

		tr.setMatchedAndChanged(hasMatched, hasChanged);

		return result;
	}
	
	@Override
	public
	IValue traverseStringOnce(IValue subject, final TraversalState tr) {
		boolean hasMatched = tr.hasMatched();
		boolean hasChanged = tr.hasChanged();
		tr.setMatchedAndChanged(false, false);
		IValue res = traverseString(subject, tr);
		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
								tr.hasChanged() | hasChanged);
		return res;
	}
	
	/*
	 * traverseString implements a visit of a string subject by visiting subsequent substrings 
	 * subject[0,len], subject[1,len] ...and trying to match the cases. If a case matches
	 * the subject cursor is advanced by the length of the match and the matched substring may be replaced.
	 * 
	 * Performance issue: we create a lot of garbage by producing all these substrings.
	 */

	private IValue traverseString(IValue subject, final TraversalState tr){
		IString subjectIString = (IString) subject;
		int len = subjectIString.length();
		int subjectCursor = 0;

		boolean hasMatched = false;
		boolean hasChanged = false;

		while (subjectCursor < len){
			tr.setMatchedAndChanged(false, false);
			tr.setBegin(subjectCursor);
			tr.setEnd(len);

			traverseTop(subject, tr);

			if(tr.hasMatched()){
				subjectCursor = tr.getEnd();
			} else {
				subjectCursor++;
			}
			hasMatched |= tr.hasMatched();
			hasChanged |= tr.hasChanged();
		}
		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
							    tr.hasChanged() | hasChanged);

		return subject;
	}
}