package org.meta_environment.uptr;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.type.FactTypeError;

public class ParsetreeAdapter {
	IConstructor parseTree;
	
	public ParsetreeAdapter(IConstructor pt) {
		if (pt.getType() != Factory.ParseTree) {
			throw new FactTypeError("ParsetreeAdapter will only wrap UPTR ParseTree, not " + pt.getType());
		}
		this.parseTree = pt;
	}
	
	public IConstructor addPositionInformation(String filename) {
		IConstructor tree = (IConstructor) parseTree.get("top");
		tree = new TreeAdapter(tree).addPositionInformation(filename);
		return parseTree.set("top", tree);
	}
	
	public boolean hasAmbiguities() {
		return ((IInteger) parseTree.get("amb_cnt")).getValue() != 0;
	}

}
