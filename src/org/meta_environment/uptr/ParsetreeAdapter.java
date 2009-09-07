package org.meta_environment.uptr;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.meta_environment.errors.SummaryAdapter;
import org.meta_environment.uptr.TreeAdapter.PositionAnnotator;

public class ParsetreeAdapter {
	
	private ParsetreeAdapter() {
		super();
	}
	
	public static IConstructor addPositionInformation(IConstructor parseTree, String filename) {
		if (isParseTree(parseTree)) {
			IConstructor tree = (IConstructor) parseTree.get("top");
			tree = new PositionAnnotator(tree).addPositionInformation(filename);
			return parseTree.set("top", tree);
		}
		
		return parseTree;
	}
	
	public static boolean isErrorSummary(IConstructor parseTree) {
		return parseTree.getConstructorType() == Factory.ParseTree_Summary;
	}
	
	public static boolean isParseTree(IConstructor parseTree) {
		return parseTree.getConstructorType() == Factory.ParseTree_Top;
	}
	
	public static IConstructor getTop(IConstructor parseTree) {
		return (IConstructor) parseTree.get("top");
	}
	
	public static SummaryAdapter getSummary(IConstructor parseTree) {
		return new SummaryAdapter(parseTree);
	}
	
	public static boolean hasAmbiguities(IConstructor parseTree) {
		return ((IInteger) parseTree.get("amb_cnt")).intValue() != 0;
	}
}
