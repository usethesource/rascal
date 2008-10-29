package org.meta_environment.uptr;

import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.type.FactTypeError;

public class SymbolAdapter {
	private ITree tree;

	public SymbolAdapter(ITree tree) {
		if (tree.getType() != Factory.Symbol) {
			throw new FactTypeError("TreeWrapper will only wrap UPTR symbols, not " +  tree.getType());
		}
		this.tree = tree;
	}

	public boolean isCf() {
		return tree.getTreeNodeType() == Factory.Symbol_Cf;
	}

	public boolean isLex() {
		return tree.getTreeNodeType() == Factory.Symbol_Lex;
	}
	
	public boolean isSort() {
		return tree.getTreeNodeType() == Factory.Symbol_Sort;
	}

	public SymbolAdapter getSymbol() {
		if (isCf() || isLex()) {
			return new SymbolAdapter((ITree) tree.get("symbol"));
		}
		else {
			throw new FactTypeError("Symbol does not have a child named symbol: " + tree);
		}
	}
}
