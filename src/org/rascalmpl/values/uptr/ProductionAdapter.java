package org.rascalmpl.values.uptr;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.values.ValueFactoryFactory;

public class ProductionAdapter {
	
	private ProductionAdapter() {
		super();
	}
	
	public static String getConstructorName(IConstructor tree) {
		for (IValue attr : getAttributes(tree)) {
			if (attr.getType().isAbstractDataType() && ((IConstructor) attr).getConstructorType() == Factory.Attr_Term) {
				IValue value = ((IConstructor)attr).get("term");
				if (value.getType().isNodeType() && ((INode) value).getName().equals("cons")) {
					return ((IString) ((INode) value).get(0)).getValue();
				}
			}
		}
		return null;
	}
	
	public static IConstructor getRhs(IConstructor tree) {
		return (IConstructor) tree.get("rhs");
	}
	
	public static IList getLhs(IConstructor tree) {
		return (IList) tree.get("lhs");
	}
	
	public static boolean isContextFree(IConstructor tree) {
		return SymbolAdapter.isCf(getRhs(tree));
	}
	
	public static boolean isCfOptLayout(IConstructor tree) {
		return SymbolAdapter.isCfOptLayout(getRhs(tree));
	}
	
	public static boolean isLayout(IConstructor tree) {
		return SymbolAdapter.isLayout(getRhs(tree));
	}
	
	public static boolean hasSortName(IConstructor tree) {
		IConstructor rhs = getRhs(tree);
		if (SymbolAdapter.isCf(rhs) || SymbolAdapter.isLex(rhs)) {
			rhs = SymbolAdapter.getSymbol(rhs);
			if (SymbolAdapter.isSort(rhs) || SymbolAdapter.isParameterizedSort(rhs)){
				return true;
			}
		}
		return false;
	}
	
	public static String getSortName(IConstructor tree) {
		IConstructor rhs = getRhs(tree);
		
		if (SymbolAdapter.isCf(rhs) || SymbolAdapter.isLex(rhs)) {
			rhs = SymbolAdapter.getSymbol(rhs);
		}
		
		if (SymbolAdapter.isSort(rhs) || SymbolAdapter.isParameterizedSort(rhs)){
			return SymbolAdapter.getName(rhs);
		} 
		
		return "";
	}
	
	public static IList getAttributes(IConstructor tree) {
		if (isList(tree)) {
			return (IList) Factory.Attrs.make(ValueFactoryFactory.getValueFactory());
		}
		IConstructor attributes = (IConstructor) tree.get("attributes");
		
		if (attributes.getConstructorType() == Factory.Attributes_Attrs) {
			return (IList) attributes.get("attrs");
		}
		
		return (IList) Factory.Attrs.make(ValueFactoryFactory.getValueFactory());
	}

	public static boolean isLiteral(IConstructor tree) {
		return SymbolAdapter.isLiteral(getRhs(tree));
	}

	public static boolean isCILiteral(IConstructor tree) {
		return SymbolAdapter.isCILiteral(getRhs(tree));
	}

	public static boolean isList(IConstructor tree) {
		return tree.getConstructorType() == Factory.Production_List 
		    || tree.getConstructorType() == Factory.Production_Regular;
	}
	
	public static boolean isRegular(IConstructor tree) {
		return tree.getConstructorType() == Factory.Production_Regular;
	}

	public static boolean isSeparatedList(IConstructor tree) {
		IConstructor rhs = getRhs(tree);
		if (SymbolAdapter.isLex(rhs) || SymbolAdapter.isCf(rhs)) {
			rhs = SymbolAdapter.getSymbol(rhs);
		}
		return SymbolAdapter.isIterPlusSep(rhs) || SymbolAdapter.isIterStarSep(rhs) || SymbolAdapter.isIterPlusSeps(rhs) || SymbolAdapter.isIterStarSeps(rhs);
	}

	public static boolean isLexical(IConstructor tree) {
		return SymbolAdapter.isLex(getRhs(tree));
	}

	public static boolean isLexToCf(IConstructor tree) {
		if (!isContextFree(tree)) {
			return false;
		}
		if (isList(tree)) {
			return false;
		}
		
		IList lhs = getLhs(tree);
		if (lhs.length() != 1) {
			return false;
		}
		IConstructor lhsSym = (IConstructor) lhs.get(0);
		if (!SymbolAdapter.isLex(lhsSym)) {
			return false;
		}
		IConstructor rhsSym = getRhs(tree);
		return SymbolAdapter.getSymbol(lhsSym).equals(SymbolAdapter.getSymbol(rhsSym));
	}

	public static String getCategory(IConstructor tree) {
		if (!isList(tree)) {
			for (IValue attr : getAttributes(tree)) {
				if (attr.getType().isAbstractDataType() && ((IConstructor) attr).getConstructorType() == Factory.Attr_Term) {
					IValue value = ((IConstructor)attr).get("term");
					if (value.getType().isNodeType() && ((INode) value).getName().equals("category")) {
						return ((IString) ((INode) value).get(0)).getValue();
					}
				}
			}
		}
		return null;
	}

	public static IConstructor getTree(IConstructor tree) {
		return tree;
	}

	public static boolean hasPreferAttribute(IConstructor tree) {
		return hasAttribute(tree, Factory.Attr_Prefer.make(ValueFactoryFactory.getValueFactory()));
	}

	public static boolean hasAttribute(IConstructor tree, IValue wanted) {
		for (IValue attr : getAttributes(tree)) {
			if (attr.isEqual(wanted)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasLexAttribute(IConstructor tree) {
		return hasAttribute(tree, Factory.Attribute_Lex);
	}

	public static boolean hasAvoidAttribute(IConstructor tree) {
		return hasAttribute(tree, Factory.Attr_Avoid.make(ValueFactoryFactory.getValueFactory()));
	}

	public static boolean isNewSeparatedList(IConstructor production) {
		if (isRegular(production)) {
			IConstructor rhs = getRhs(production);
			return rhs.getConstructorType() == Factory.Symbol_IterStarSepX || rhs.getConstructorType() == Factory.Symbol_IterSepX;
		}
		return false;
	}
}
