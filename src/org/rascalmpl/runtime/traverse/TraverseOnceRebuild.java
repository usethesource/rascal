package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.IWithKeywordParameters;

import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;

public class TraverseOnceRebuild extends TraverseOnce implements ITraverseSpecialization {
	
	public TraverseOnceRebuild(IValueFactory vf) {
		super(vf);
	}

	private static final Map<String, IValue> emptyAnnotationsMap = new HashMap<String, IValue>();
	
	@Override
	public
	IValue traverseTupleOnce(final IValue subject, final TraversalState tr) {
		final ITuple tuple = (ITuple) subject;
		final int arity = tuple.arity();

		boolean hasMatched = false;
		boolean hasChanged = false;


		final IValue args[] = new IValue[arity];
		for (int i = 0; i < arity; i++){
			tr.setMatchedAndChanged(false, false);
			args[i] = tr.traverse.once(tuple.get(i), tr);
			hasMatched |= tr.hasMatched();
			hasChanged |= tr.hasChanged();
		}
		tr.setMatchedAndChanged(hasMatched, hasChanged);
		return vf.tuple(args);
	}
	
	@Override
	public
	IValue traverseADTOnce(final IValue subject, final TraversalState tr) {
		final IConstructor cons = (IConstructor)subject;
		final boolean hasKwParams = cons.mayHaveKeywordParameters() && cons.asWithKeywordParameters().hasParameters();
		final int arity = cons.arity();

		if (arity == 0 && !hasKwParams) {
			return subject; // constants have no children to traverse into
		} 

		boolean hasChanged = false;
		boolean hasMatched = false;

		final IValue args[] = new IValue[arity];

		for (int i = 0; i < arity; i++){
			IValue child = cons.get(i);
			tr.setMatchedAndChanged(false, false);
			args[i] = tr.traverse.once(child, tr);
			hasChanged |= tr.hasChanged();
			hasMatched |= tr.hasMatched();
		}
		Map<String, IValue> kwParams = null;
		if (hasKwParams) {
			kwParams = new HashMap<>();
			IWithKeywordParameters<? extends INode> consKw = cons.asWithKeywordParameters();
			for (String kwName : consKw.getParameterNames()) {
				IValue val = consKw.getParameter(kwName);
				tr.setMatchedAndChanged(false, false);
				IValue newVal = tr.traverse.once(val, tr);
				kwParams.put(kwName, newVal);
				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
			}
		}
		tr.setMatchedAndChanged(hasMatched, hasChanged);

		if (tr.hasChanged()) {
			return rebuild(subject, args, hasKwParams ? kwParams : emptyAnnotationsMap);
		}
		else {
			return subject;
		}
	}
	
	@Override
	public
	IValue traverseConcreteTreeOnce(final IValue subject, final TraversalState tr) {
		final ITree tree = (ITree)subject;
		
		if (tree.isAppl()) {
		    return traverseApplOnce(tr, tree);  
		}
		else if (tree.isAmb()) {
		    return traverseAmbOnce(tr, tree);
		}
		else if (tree.isChar()) {
		    return tree;
		}
		else {
		    assert tree.isCycle();
		    return tree;
		}
	}

    private IValue traverseApplOnce(final TraversalState tr, ITree tree) {
        // - Copy prod node verbatim to result
		// - Only visit non-layout nodes in argument list

		IList list = TreeAdapter.getArgs(tree);
		int len = list.length();

		IValue[] args = new IValue[2];
		if (len > 0) {
			args[0] = TreeAdapter.getProduction(tree);
			IListWriter w = vf.listWriter();
			boolean hasChanged = false;
			boolean hasMatched = false;

			if (TreeAdapter.isTop(tree)) {
				//w.append(list.get(0)); // copy layout before
				
				tr.setMatchedAndChanged(false, false);			// visit layout before
				w.append(tr.traverse.once(list.get(0), tr));
				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
				
				tr.setMatchedAndChanged(false, false);
				w.append(tr.traverse.once(list.get(1), tr));
				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
				
				//w.append(list.get(2)); // copy layout after
				tr.setMatchedAndChanged(false, false);			// visit layout after
				w.append(tr.traverse.once(list.get(2), tr));
				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
			} 
			else { 
				for (int i = 0; i < len; i++){
					IValue elem = list.get(i);
					if (i % 2 == 0) { // Recursion to all non-layout elements
						tr.setMatchedAndChanged(false, false);
						w.append(tr.traverse.once(elem, tr));
						hasChanged |= tr.hasChanged();
						hasMatched |= tr.hasMatched();
					} else { // Just copy layout elements
						w.append(list.get(i));
					}
				}
			}

			tr.setMatchedAndChanged(hasMatched, hasChanged);
			args[1] = w.done();
		} else {
			args[1] = list;
		}
		if(tr.hasChanged()){
			return vf.constructor(RascalValueFactory.Tree_Appl, args);
		} else {
			return tree;
		}
    }

    private IValue traverseAmbOnce(final TraversalState tr, final ITree tree) {
        tr.setMatchedAndChanged(false, false);
        boolean hasChanged = false;
        boolean hasMatched = false;
        ISetWriter newAlts = vf.setWriter();
        
        for (IValue alt : tree.getAlternatives()) {
            tr.setMatchedAndChanged(false, false);
            newAlts.insert(tr.traverse.once(alt, tr));
            hasChanged |= tr.hasChanged();
            hasMatched |= tr.hasMatched();
        }
        
        tr.setMatchedAndChanged(hasMatched, hasChanged);
        
        if (hasChanged) {
            return IRascalValueFactory.getInstance().amb(newAlts.done());
        }
        else {
            return tree;
        }
    } 
	
	@Override
	public
	IValue traverseMapOnce(final IValue subject, final TraversalState tr) {
		final IMap map = (IMap) subject;
		if(!map.isEmpty()){
			Iterator<Entry<IValue,IValue>> iter = map.entryIterator();
			boolean hasChanged = false;
			boolean hasMatched = false;
			final int mapSize = map.size();

			final IValue[] keys = new IValue[mapSize];
			final IValue[] vals = new IValue[mapSize];
			int i = 0;
			while (iter.hasNext()) {
				Entry<IValue,IValue> entry = iter.next();
				tr.setMatchedAndChanged(false, false);
				keys[i] = tr.traverse.once(entry.getKey(), tr);
				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
				tr.setMatchedAndChanged(false, false);
				vals[i] = tr.traverse.once(entry.getValue(), tr);
				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
				i++;
			}
			tr.setChanged(hasChanged);
			tr.setMatched(hasMatched);

			if(hasChanged){
				IMapWriter w = vf.mapWriter();
				for(int j = 0; j < mapSize; j++){
					w.put(keys[j], vals[j]);
				}
				return w.done();
			}
			return subject;

		} else {
			return subject;
		}
	}

	@Override
	public
	IValue traverseSetOnce(final IValue subject, final TraversalState tr) {
		ISet set = (ISet) subject;
		if(!set.isEmpty()){
			boolean hasChanged = false;
			boolean hasMatched = false;
			final int setSize = set.size();

			final IValue[] vals = new IValue[setSize];

			int i = 0;
			for (IValue v : set) {
				tr.setMatchedAndChanged(false, false);
				vals[i] = tr.traverse.once(v, tr);
				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
				i++;
			}

			tr.setMatchedAndChanged(hasMatched, hasChanged);
			if(hasChanged){
				ISetWriter w = vf.setWriter();
				for(int j = 0; j < setSize; j++){
					w.insert(vals[j]);
				}
				return w.done();
			}
			return subject;
		} else {
			return subject;
		}
	}

	@Override
	public
	IValue traverseListOnce(final IValue subject, final TraversalState tr) {
		final IList list = (IList) subject;
		final int len = list.length();
		if (len > 0){
			boolean hasChanged = false;
			boolean hasMatched = false;

			final IListWriter w = vf.listWriter();

			for (int i = 0; i < len; i++){
				IValue elem = list.get(i);
				tr.setMatchedAndChanged(false, false);
				elem = tr.traverse.once(elem, tr);
				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
				w.append(elem);
			}

			tr.setMatchedAndChanged(hasMatched, hasChanged);
			if(hasChanged){
				return w.done();
			} else {
				return subject;
			}

		} else {
			return subject;
		}
	}

	@Override
	public
	IValue traverseNodeOnce(final IValue subject, final TraversalState tr) {
		IValue result= subject;
		INode node = (INode)subject;
		final int arity = node.arity();
		final boolean hasKwParams = node.mayHaveKeywordParameters() && node.asWithKeywordParameters().hasParameters();

		if (arity == 0 && !hasKwParams){
			result = subject;
		} 

		boolean hasChanged = false;
		boolean hasMatched = false;

		final IValue args[] = new IValue[node.arity()];

		Map<String, IValue> kwParams = null;

		for (int i = 0; i < arity; i++){
			IValue child = node.get(i);
			tr.setMatchedAndChanged(false, false);
			args[i] = tr.traverse.once(child, tr);
			hasChanged |= tr.hasChanged();
			hasMatched |= tr.hasMatched();
		}
		if (hasKwParams) {
			kwParams = new HashMap<>();
			IWithKeywordParameters<? extends INode> nodeKw = node.asWithKeywordParameters();
			for (String kwName : nodeKw.getParameterNames()) {
				IValue val = nodeKw.getParameter(kwName);
				tr.setMatchedAndChanged(false, false);
				IValue newVal = tr.traverse.once(val, tr);
				kwParams.put(kwName, newVal);
				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
			}
		}

		tr.setMatchedAndChanged(hasMatched, hasChanged);

		if(hasChanged){
			result = (kwParams == null) ? vf.node(node.getName(), args)
										: vf.node(node.getName(), args, kwParams);
		}
		return result;
	}
	
	@Override
	public
	IValue traverseStringOnce(final IValue subject, final TraversalState tr) {
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
	 * At the end, the subject string including all replacements is returned.
	 * 
	 * Performance issue: we create a lot of garbage by producing all these substrings.
	 */

	private IValue traverseString(final IValue subject, final TraversalState tr){
		final IString subjectIString = (IString) subject;
		final String subjectString = subjectIString.getValue();
		final int len = subjectIString.length();
		int subjectCursor = 0;

		boolean hasMatched = false;
		boolean hasChanged = false;

		StringBuffer replacementString = new StringBuffer(len); 

		while (subjectCursor < len){
			tr.setMatchedAndChanged(false, false);
			tr.setBegin(0);
			tr.setEnd(len);

			String repl = ((IString) traverseTop(vf.string(subjectString.substring(subjectCursor, len)), tr)).getValue();

			if(tr.hasMatched()){
				if(tr.getBegin() > 0){
					replacementString.append(subjectString.substring(subjectCursor, subjectCursor + tr.getBegin()));
				}
				replacementString.append(repl);
				subjectCursor = subjectCursor + tr.getEnd();
			} else {
				replacementString.append(subjectString.substring(subjectCursor, subjectCursor + 1));
				subjectCursor++;
			}
			hasMatched |= tr.hasMatched();
			hasChanged |= tr.hasChanged();
		}
		tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
				tr.hasChanged() | hasChanged);

		if (!tr.hasChanged()) {
			return subject;
		}

		return vf.string(replacementString.toString());
	}
	
	private INode rebuild(IValue subject, IValue[] args, Map<String,IValue> changedKwParams) {
		Map<String, IValue> givenKwParams = subject.mayHaveKeywordParameters() ? subject.asWithKeywordParameters().getParameters() : emptyAnnotationsMap;
		// TODO: jurgen can be optimized for the ITree case
		if(subject.getType().isAbstractData()){
			return vf.constructor(((IConstructor) subject).getConstructorType(), args, changedKwParams.isEmpty() ? givenKwParams : changedKwParams);
		} else {
			return vf.node(((INode) subject).getName(), args, changedKwParams.isEmpty() ? givenKwParams : changedKwParams);
		}
	}
}