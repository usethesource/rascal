package org.meta_environment.uptr;

import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.type.FactTypeError;

public class TreeWrapper {
	private ITree tree;
	
	public TreeWrapper(ITree tree) {
		if (tree.getType() != Factory.Tree) {
			throw new FactTypeError("TreeWrapper will only wrap UPTR Trees, not " +  tree.getType());
		}
		this.tree = tree;
	}
	
	public boolean isAppl() {
		return tree.getTreeNodeType() == Factory.Tree_Appl;
	}
	
	public boolean isAmb() {
		return tree.getTreeNodeType() == Factory.Tree_Amb;
	}
	
	public boolean isChar() {
		return tree.getTreeNodeType() == Factory.Tree_Char;
	}
	
	public boolean isCycle() {
		return tree.getTreeNodeType() == Factory.Tree_Cycle;
	}
	
	public ProductionWrapper getProduction() {
		return new ProductionWrapper((ITree) tree.get("production"));
	}
	
	public String getConstructorName() {
		return getProduction().getConstructorName();
	}
	
	public boolean isProduction(String sortName, String consName) {
		ProductionWrapper prod = getProduction();
		return prod.getSortName().equals(sortName) &&
		prod.getConstructorName().equals(consName);
	}

}
