package org.meta_environment.uptr;

import org.eclipse.imp.pdb.facts.IString;
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
	
	public String getName() {
		if (isSort()) {
			return ((IString) tree.get("string")).getValue();
		}
		else {
			throw new FactTypeError("Symbol does not have a child named \"name\": " + tree);
		}
	}

	public boolean isLiteral() {
		return tree.getTreeNodeType() == Factory.Symbol_Lit;
	}

	public boolean isCILiteral() {
		return tree.getTreeNodeType() == Factory.Symbol_CiLit;
	}

	public boolean isIterPlusSep() {
		return tree.getTreeNodeType() == Factory.Symbol_IterPlusSep;
	}
	
	public boolean isIterStarSep() {
		return tree.getTreeNodeType() == Factory.Symbol_IterStarSep;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SymbolAdapter)) {
			return false;
		}
		return tree.equals(((SymbolAdapter)obj).tree);
	}
}
