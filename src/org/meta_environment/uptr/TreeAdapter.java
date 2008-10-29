package org.meta_environment.uptr;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;

public class TreeAdapter {
	private ITree tree;
	private ProductionAdapter prod;
	
	public TreeAdapter(ITree tree) {
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
	
	public ProductionAdapter getProduction() {
		if (prod == null) {
		  prod = new ProductionAdapter((ITree) tree.get("production"));
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
		ProductionAdapter prod = getProduction();
		return prod.getSortName().equals(sortName) &&
		prod.getConstructorName().equals(consName);
	}
	
	public boolean isContextFree() {
		return getProduction().isContextFree();
	}
	
	public IList getArgs() {
		if (isAppl()) {
		  return (IList) tree.get("args");
		}
		else {
			throw new FactTypeError("this tree has no args");
		}
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

	public ISet getAlternatives() {
		if (isAmb()) {
		  return (ISet) tree.get("alternatives");
		}
		else {
			throw new FactTypeError("this tree has no alternatives");
		}
	}
}
