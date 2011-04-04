/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
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
		return false;
	}
	
	public static boolean isLayout(IConstructor tree) {
		return SymbolAdapter.isLayouts(getRhs(tree));
	}
	
	public static String getSortName(IConstructor tree) {
		IConstructor rhs = getRhs(tree);
		
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
		return tree.getConstructorType() == Factory.Production_Regular
		    && SymbolAdapter.isAnyList(getRhs(tree)); 
	}
	
	public static boolean isOpt(IConstructor tree) {
		return tree.getConstructorType() == Factory.Production_Regular
			&& SymbolAdapter.isOpt(getRhs(tree));
	}
	
	public static boolean isDefault(IConstructor tree) {
		return tree.getConstructorType() == Factory.Production_Default;
	}
	
	public static boolean isRegular(IConstructor tree) {
		return tree.getConstructorType() == Factory.Production_Regular;
	}

	public static boolean isSeparatedList(IConstructor tree) {
		IConstructor rhs = getRhs(tree);
		return SymbolAdapter.isIterPlusSeps(rhs) || SymbolAdapter.isIterStarSeps(rhs);
	}

	public static boolean isLexical(IConstructor tree) {
		return hasLexAttribute(tree);
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

	public static boolean hasAttribute(IConstructor tree, IValue wanted) {
		for (IValue attr : getAttributes(tree)) {
			if (attr.isEqual(wanted)) {
				return true;
			}
			// TODO: quick hack to work around the fact that attrs are sometimes "nodes" and sometimes constructors
			if (attr.toString().equals(wanted.toString())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasLexAttribute(IConstructor tree) {
		return hasAttribute(tree, Factory.Attribute_Lex) || hasAttribute(tree, Factory.Attribute_LexX) /* TODO: remove the first assmit */;
	}

	public static boolean shouldFlatten(IConstructor surrounding, IConstructor nested) {
		if (ProductionAdapter.isList(nested)) {
			IConstructor nestedRhs = ProductionAdapter.getRhs(nested);
			IConstructor surroundingRhs = ProductionAdapter.getRhs(surrounding);
			
			if (surroundingRhs.isEqual(nestedRhs)) {
				return true;
			}
			
			if ((SymbolAdapter.isIterPlus(surroundingRhs) && SymbolAdapter.isIterStar(nestedRhs)) || (SymbolAdapter.isIterStar(surroundingRhs) && SymbolAdapter.isIterPlus(nestedRhs))) {
				return SymbolAdapter.getSymbol(surroundingRhs).isEqual(SymbolAdapter.getSymbol(nestedRhs)) && SymbolAdapter.getSeparators(surroundingRhs).isEqual(SymbolAdapter.getSeparators(nestedRhs));
			}
			
			if ((SymbolAdapter.isIterPlusSeps(surroundingRhs) && SymbolAdapter.isIterStarSeps(nestedRhs)) || (SymbolAdapter.isIterStarSeps(surroundingRhs) && SymbolAdapter.isIterPlusSeps(nestedRhs))) {
				return SymbolAdapter.getSymbol(surroundingRhs).isEqual(SymbolAdapter.getSymbol(nestedRhs)) && SymbolAdapter.getSeparators(surroundingRhs).isEqual(SymbolAdapter.getSeparators(nestedRhs));
			}
		}
		return false;
	}
}
