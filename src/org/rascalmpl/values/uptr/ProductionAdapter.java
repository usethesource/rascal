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
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.values.ValueFactoryFactory;

public class ProductionAdapter {
	
	private ProductionAdapter() {
		super();
	}

	/**
	 * @return a constructor name if present or null otherwise
	 */
	public static String getConstructorName(IConstructor tree) {
		IConstructor def = getDefined(tree);
		
		if (SymbolAdapter.isLabel(def)) {
			return SymbolAdapter.getLabel(def);
		}
		
		return null;
	}
	
	public static IConstructor getType(IConstructor tree) {
		return SymbolAdapter.delabel(getDefined(tree));
	}
	
	public static IConstructor getDefined(IConstructor tree) {
		if (isSkipped(tree)) {
			return (IConstructor) Factory.Symbol_Empty.make(ValueFactoryFactory.getValueFactory());
		}
		return (IConstructor) tree.get("def");
	}
	
	public static boolean isSkipped(IConstructor tree) {
		return tree.getConstructorType() == Factory.Production_Skipped;
	}
	
	public static boolean isError(IConstructor tree) {
		return tree.getConstructorType() == Factory.Production_Error;
	}

	public static IList getSymbols(IConstructor tree) {
		if (isDefault(tree)) {
			return (IList) tree.get("symbols");
		}
		return null;
	}
	
	public static IList getASTSymbols(IConstructor tree) {
		if (isLexical(tree)) {
			throw new ImplementationError("This is not a context-free production: " + tree);
		}

		IList children = getSymbols(tree);
		IListWriter writer = Factory.Args.writer(ValueFactoryFactory
				.getValueFactory());

		for (int i = 0; i < children.length(); i++) {
			IConstructor kid = (IConstructor) children.get(i);
			if (!SymbolAdapter.isLiteral(kid) && !SymbolAdapter.isCILiteral(kid)) {
				writer.append(kid);
			}
			// skip layout
			i++;
		}
		
		return writer.done();
	}
	
	public static boolean isContextFree(IConstructor tree) {
		return SymbolAdapter.isSort(getType(tree));
	}
	
	public static boolean isLayout(IConstructor tree) {
		return SymbolAdapter.isLayouts(getType(tree));
	}
	
	public static String getSortName(IConstructor tree) {
		IConstructor rhs = getType(tree);
		
		if (SymbolAdapter.isSort(rhs) 
				|| SymbolAdapter.isLex(rhs) 
				|| SymbolAdapter.isLayouts(rhs) 
				|| SymbolAdapter.isParameterizedSort(rhs)
				|| SymbolAdapter.isKeyword(rhs)) {
			return SymbolAdapter.getName(rhs);
		} 
		
		return "";
	}
	
	public static ISet getAttributes(IConstructor tree) {
		if (isDefault(tree)) {
			return (ISet) tree.get("attributes");
		}
		
		return ValueFactoryFactory.getValueFactory().set(Factory.Attr);
	}

	public static boolean isLiteral(IConstructor tree) {
		return SymbolAdapter.isLiteral(getType(tree));
	}

	public static boolean isCILiteral(IConstructor tree) {
		return SymbolAdapter.isCILiteral(getType(tree));
	}

	public static boolean isList(IConstructor tree) {
		return tree.getConstructorType() == Factory.Production_Regular
		    && SymbolAdapter.isAnyList(getType(tree)); 
	}
	
	public static boolean isOpt(IConstructor tree) {
		return tree.getConstructorType() == Factory.Production_Regular
			&& SymbolAdapter.isOpt(getType(tree));
	}
	
	public static boolean isDefault(IConstructor tree) {
		return tree.getConstructorType() == Factory.Production_Default;
	}
	
	public static boolean isRegular(IConstructor tree) {
		return tree.getConstructorType() == Factory.Production_Regular;
	}

	public static boolean isSeparatedList(IConstructor tree) {
		IConstructor rhs = getType(tree);
		return SymbolAdapter.isIterPlusSeps(rhs) || SymbolAdapter.isIterStarSeps(rhs);
	}

	public static boolean isLexical(IConstructor tree) {
		return SymbolAdapter.isLex(getType(tree));
	}
	
	public static boolean isSort(IConstructor tree) {
		return SymbolAdapter.isSort(getType(tree));
	}

	public static boolean isKeyword(IConstructor tree) {
		return SymbolAdapter.isKeyword(getType(tree));
	}

	public static String getCategory(IConstructor tree) {
		if (!isRegular(tree)) {
			for (IValue attr : getAttributes(tree)) {
				if (attr.getType().isAbstractDataType() && ((IConstructor) attr).getConstructorType() == Factory.Attr_Tag) {
					IValue value = ((IConstructor)attr).get("tag");
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
		return getAttributes(tree).contains(wanted);
	}
	
	public static boolean shouldFlatten(IConstructor surrounding, IConstructor nested) {
		if (ProductionAdapter.isList(nested)) {
			IConstructor nestedRhs = ProductionAdapter.getType(nested);
			IConstructor surroundingRhs = ProductionAdapter.getType(surrounding);
			
			if (SymbolAdapter.isEqual(surroundingRhs, nestedRhs)) {
				return true;
			}
			
			if ((SymbolAdapter.isIterPlus(surroundingRhs) && SymbolAdapter.isIterStar(nestedRhs)) || (SymbolAdapter.isIterStar(surroundingRhs) && SymbolAdapter.isIterPlus(nestedRhs))) {
				return SymbolAdapter.isEqual(SymbolAdapter.getSymbol(surroundingRhs), SymbolAdapter.getSymbol(nestedRhs)) && SymbolAdapter.isEqual(SymbolAdapter.getSeparators(surroundingRhs), SymbolAdapter.getSeparators(nestedRhs));
			}
			
			if ((SymbolAdapter.isIterPlusSeps(surroundingRhs) && SymbolAdapter.isIterStarSeps(nestedRhs)) || (SymbolAdapter.isIterStarSeps(surroundingRhs) && SymbolAdapter.isIterPlusSeps(nestedRhs))) {
				return SymbolAdapter.isEqual(SymbolAdapter.getSymbol(surroundingRhs), SymbolAdapter.getSymbol(nestedRhs)) && SymbolAdapter.isEqual(SymbolAdapter.getSeparators(surroundingRhs), SymbolAdapter.getSeparators(nestedRhs));
			}
		}
		return false;
	}
}
