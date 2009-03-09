package org.meta_environment.uptr;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.meta_environment.errors.SummaryAdapter;
import org.meta_environment.rascal.interpreter.exceptions.ImplementationException;

public class ParsetreeAdapter {
	IConstructor parseTree;
	
	public ParsetreeAdapter(IConstructor pt) {
		if (pt.getType() != Factory.ParseTree) {
			throw new ImplementationException("ParsetreeAdapter will only wrap UPTR ParseTree, not " + pt.getType());
		}
		this.parseTree = pt;
	}
	
	public IConstructor addPositionInformation(String filename) {
		if (isParseTree()) {
			IConstructor tree = (IConstructor) parseTree.get("top");
			tree = new TreeAdapter(tree).addPositionInformation(filename);
			return parseTree.set("top", tree);
		}
		else {
			return parseTree;
		}
	}
	
	public boolean isErrorSummary() {
		return parseTree.getConstructorType() == Factory.ParseTree_Summary;
	}
	
	public boolean isParseTree() {
		return parseTree.getConstructorType() == Factory.ParseTree_Top;
	}
	
	public SummaryAdapter getSummary() {
		return new SummaryAdapter(parseTree);
	}
	
	public boolean hasAmbiguities() {
		return ((IInteger) parseTree.get("amb_cnt")).intValue() != 0;
	}

}
