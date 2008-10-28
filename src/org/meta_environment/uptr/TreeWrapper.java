package org.meta_environment.uptr;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;

public class TreeWrapper {
	private ITree tree;
	private ProductionWrapper prod;
	
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
		if (prod == null) {
		  prod = new ProductionWrapper((ITree) tree.get("production"));
		}
		
		return prod;
	}
	
	public String getSortName() throws FactTypeError {
		return getProduction().getSortName();
	}
	
	public String getConstructorName() {
		return getProduction().getConstructorName();
	}
	
	public boolean isProduction(String sortName, String consName) {
		ProductionWrapper prod = getProduction();
		return prod.getSortName().equals(sortName) &&
		prod.getConstructorName().equals(consName);
	}
	
	public boolean isContextFree() {
		return getProduction().isContextFree();
	}
	
	public IList getArgs() {
		return (IList) tree.get("args");
	}
	
	public IList getContextFreeArgs() {
		if (!isContextFree()) {
			throw new FactTypeError("This is not a context-free production: "
					+ tree);
		}

		IList children = getArgs();
		IList result = ValueFactory.getInstance().list(Factory.Args);

		for (int i = 0; i < children.length(); i++) {
			result.append(children.get(i));
			// skip layout
			i++;
		}

		return result;
	}
}
