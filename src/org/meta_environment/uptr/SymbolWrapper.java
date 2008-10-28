package org.meta_environment.uptr;

import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.type.FactTypeError;

public class SymbolWrapper {
	private ITree tree;

	public SymbolWrapper(ITree tree) {
		if (tree.getType() != Factory.Symbol) {
			throw new FactTypeError("TreeWrapper will only wrap UPTR symbols, not " +  tree.getType());
		}
		this.tree = tree;
	}

	public boolean isCf() {
		return tree.is(Factory.Symbol_Cf);
	}

	public boolean isLex() {
		return tree.is(Factory.Symbol_Lex);
	}
	
	public boolean isSort() {
		return tree.is(Factory.Symbol_Sort);
	}

	public SymbolWrapper getSymbol() {
		if (isCf() || isLex()) {
			return new SymbolWrapper((ITree) tree.get("symbol"));
		}
		else {
			throw new FactTypeError("Symbol does not have a child named symbol: " + tree);
		}
	}
}
