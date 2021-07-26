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
	IValue traverseTupleOnce(IValue subject, final TraversalState tr) {
		ITuple tuple = (ITuple) subject;
		int arity = tuple.arity();

		boolean hasMatched = false;
		boolean hasChanged = false;


		IValue args[] = new IValue[arity];
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

		IValue args[] = new IValue[arity];

		for (int i = 0; i < arity; i++){
			IValue child = cons.get(i);
			tr.setMatchedAndChanged(false, false);
			args[i] = tr.traverse.once(child, tr);
			hasChanged |= tr.hasChanged();
			hasMatched |= tr.hasMatched();
		}
		Map<String, IValue> kwParams = new HashMap<>();
		if (hasKwParams) {
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
			return rebuild(subject, args, kwParams);
		}
		else {
			return subject;
		}
	}
	
	@Override
	public
	IValue traverseConcreteTreeOnce(IValue subject, final TraversalState tr) {
		ITree tree = (ITree)subject;
		
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
			boolean isTop = TreeAdapter.isTop(tree);

			if (isTop) {
				w.append(list.get(0)); // copy layout before
				tr.setMatchedAndChanged(false, false);
				w.append(tr.traverse.once(list.get(1), tr));
				hasChanged |= tr.hasChanged();
				hasMatched |= tr.hasMatched();
				w.append(list.get(2)); // copy layout after
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

    private IValue traverseAmbOnce(final TraversalState tr, ITree tree) {
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
	IValue traverseMapOnce(IValue subject, final TraversalState tr) {
		IMap map = (IMap) subject;
		if(!map.isEmpty()){
			Iterator<Entry<IValue,IValue>> iter = map.entryIterator();
			boolean hasChanged = false;
			boolean hasMatched = false;
			int mapSize = map.size();

			IValue[] keys = new IValue[mapSize];
			IValue[] vals = new IValue[mapSize];
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
	IValue traverseSetOnce(IValue subject, final TraversalState tr) {
		ISet set = (ISet) subject;
		if(!set.isEmpty()){
			boolean hasChanged = false;
			boolean hasMatched = false;
			int setSize = set.size();

			IValue[] vals = new IValue[setSize];

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
	IValue traverseListOnce(IValue subject, final TraversalState tr) {
		IList list = (IList) subject;
		int len = list.length();
		if (len > 0){
			boolean hasChanged = false;
			boolean hasMatched = false;

			IListWriter w = vf.listWriter();

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

		IValue args[] = new IValue[node.arity()];

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
	 * At the end, the subject string including all replacements is returned.
	 * 
	 * Performance issue: we create a lot of garbage by producing all these substrings.
	 */

	private IValue traverseString(IValue subject, final TraversalState tr){
		IString subjectIString = (IString) subject;
		String subjectString = subjectIString.getValue();
		int len = subjectIString.length();
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
			IConstructor cons1 = (IConstructor) subject;
			IConstructor cons2 = vf.constructor(cons1.getConstructorType(), args, givenKwParams);
			if(changedKwParams.size() > 0){
				cons2 = cons2.asWithKeywordParameters().setParameters(changedKwParams);
			}
			return cons2;
		} else {
			INode node1 = (INode) subject;
			INode node2 = vf.node(node1.getName(), args, givenKwParams);
			if(changedKwParams.size() > 0){
				node2 = node2.asWithKeywordParameters().setParameters(changedKwParams);
			}
			return node2;
		}
	}
}