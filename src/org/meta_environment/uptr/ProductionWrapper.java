package org.meta_environment.uptr;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;

public class ProductionWrapper {
	private ITree tree;

	public ProductionWrapper(ITree tree) {
		if (tree.getType() != Factory.Production) {
			throw new FactTypeError("ProductionWrapper only wraps UPTR productions, not " + tree.getType());
		}
		this.tree = tree;
	}
	
	public String getConstructorName() {
		for (IValue attr : getAttributes()) {
			if (attr.getType() == Factory.Attr_Term) {
				return ((IString) ((ITree) ((ITree) attr).get("value")).get("name")).getValue();
			}
		}
		
		return null;
	}
	
	public SymbolWrapper getRhs() {
		return new SymbolWrapper((ITree) tree.get("rhs"));
	}
	
	public IList getLhs() {
		return (IList) tree.get("lhs");
	}
	
	public boolean isContextFree() {
		return getRhs().isCf();
	}
	
	public IList getContextFreeLhs() {
		if (!isContextFree()) {
			throw new FactTypeError("This is not a context-free production: " + tree);
		}
		
		IList children = getLhs();
		IList result = ValueFactory.getInstance().list(Factory.Args);
		
		for (int i = 0; i < children.length(); i++) {
			result.append(children.get(i));
			// skip layout
			i++;
		}
		
		return result;
	}
	
	public String getSortName() {
		SymbolWrapper rhs = getRhs();
		
		if (rhs.isCf() || rhs.isLex()) {
			rhs = rhs.getSymbol();
			if (rhs.isSort()) {
				return ((IString) ((ITree) rhs).get("name")).getValue();
			}
		}
			
		throw new FactTypeError("Production does not have a sort name: " + tree);
	}
	
	public IList getAttributes() {
		ITree attributes = (ITree) tree.get("attributes");
		
		if (attributes.is(Factory.Attributes_Attrs)) {
			return (IList) attributes.get("attrs");
		}
		else {
			return null;
		}
	}

}
