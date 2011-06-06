/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 */
package org.rascalmpl.parser.gtd.result.uptr.action;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.gtd.result.action.VoidActionExecutor;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

/**
 * This action executor implements the post disambiguation filters for the Rascal
 * syntax in Java. This is needed for bootstrapping the parsing of Rascal files
 * without having to parse and execute Rascal action code first.
 * 
 * Caveat: the implementation in this class co-evolves with the Rascal syntax definition
 * in a very fine grained manner.
 */
public class BootRascalActionExecutor extends VoidActionExecutor {
	private static final IValueFactory VF = ValueFactoryFactory.getValueFactory();
	private static final IConstructor EXP = (IConstructor) Factory.Symbol_Sort.make(VF, VF.string("Expression"));
	private static final IConstructor COM = (IConstructor) Factory.Symbol_Sort.make(VF, VF.string("Command"));
	private static final IConstructor PAT = (IConstructor) Factory.Symbol_Sort.make(VF, VF.string("Pattern"));
	private static final IConstructor STAT = (IConstructor) Factory.Symbol_Sort.make(VF, VF.string("Statement"));
	private static final IConstructor TYPE = (IConstructor) Factory.Symbol_Sort.make(VF, VF.string("Type"));
	private static final IConstructor MAP_EXP = (IConstructor) Factory.Symbol_ParameterizedSort.make(VF, VF.string("Mapping"), VF.list(EXP));
	private static final IConstructor MAP_PAT = (IConstructor) Factory.Symbol_ParameterizedSort.make(VF, VF.string("Mapping"), VF.list(PAT));

	@Override
	public IConstructor filterProduction(IConstructor tree,
			Object environment) {
		IConstructor prod = TreeAdapter.getProduction(tree);
		IConstructor sym = ProductionAdapter.getRhs(prod);
		
		if (sym.isEqual(STAT)) {
			return filterStatement(tree, prod);
		}
		
		if (sym.isEqual(TYPE)) {
			return filterType(tree, prod);
		}
		
		if (sym.isEqual(MAP_EXP) || sym.isEqual(MAP_PAT)) {
			return filterMapping(tree, prod);
		}
		
		if (sym.isEqual(COM)) {
			return filterCommand(tree, prod);
		}
		
		// TODO: include basic filtering of embedded concrete syntax fragments here.
		
		return tree;
	}

	private IConstructor filterArg(IConstructor tree, IConstructor prod, String father, int pos, String... children) {
		String cons = ProductionAdapter.getConstructorName(prod);
		
		if (cons.equals(father)) {
			IConstructor arg = (IConstructor) TreeAdapter.getArgs(tree).get(pos);
			
			if (TreeAdapter.isAppl(arg)) {
				String constructorName = TreeAdapter.getConstructorName(arg);
				
				if (constructorName != null) {
					for (String child : children) {
						if (constructorName.equals(child)) {
							return null;
						}
					}
				}
			}
			else if (TreeAdapter.isAmb(arg)) {
				// now filter this cluster with the given cons name
				ISet alts = TreeAdapter.getAlternatives(arg);
				ISetWriter w = VF.setWriter();
				
				next:for (IValue alt : alts) {
					String constructorName = TreeAdapter.getConstructorName((IConstructor) alt);
					
					if (constructorName != null) {
						for (String child : children) {
							if (constructorName.equals(child)) {
								continue next;
							}
						}
					}
					
					w.insert(alt);
				}
				
				return arg.set("alternatives", w.done());
			}
		}
		
		return tree;
	}
	
	private IConstructor filterStatement(IConstructor tree, IConstructor prod) {
		return filterArg(tree, prod, "Expression", 0, "NonEmptyBlock", "Visit");
	}

	private IConstructor filterCommand(IConstructor tree, IConstructor prod) {
		if (filterArg(tree, prod, "Expression", 0, "NonEmptyBlock") == null) {
			return null;
		}
	
		return filterArg(tree, prod, "Statement", 0, "VariableDeclaration", "FunctionDeclaration", "Visit");
	}

	private IConstructor filterMapping(IConstructor tree, IConstructor prod) {
		return filterArg(tree, prod, "Default", 0, "IfDefinedOtherwise");
	}

	private IConstructor filterType(IConstructor tree, IConstructor prod) {
		return filterArg(tree, prod, "Symbol", 0, "Nonterminal", "Labeled", "Parametrized", "Parameter");
	}
	
	@Override
	public boolean mayHaveSideEffects(IConstructor rhs) {
		return false;
	}
}
