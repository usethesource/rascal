/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.types;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.types.TypeReifier.TypeStoreWithSyntax;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.ProductionAdapter;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.SymbolFactory;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeFactory.RandomTypesConfig;
import io.usethesource.vallang.type.TypeFactory.TypeReifier;
import io.usethesource.vallang.type.TypeFactory.TypeValues;
import io.usethesource.vallang.type.TypeStore;
import io.usethesource.vallang.visitors.BottomUpVisitor;
import io.usethesource.vallang.visitors.IdentityVisitor;

/**
 * This is an "extension" of the PDB's type system with a special kind of type
 * that implements the connection between Rascal's non-terminals and Rascal types. 
 */
public class NonTerminalType extends RascalType {
	private final IConstructor symbol;

	/*package*/ public NonTerminalType(IConstructor cons) {
		// TODO refactor this into different factory methods in RascalTypeFactory
		
		if (cons.getConstructorType() == RascalValueFactory.Tree_Appl) {
			// Note that here we go from * to + lists if the list is not empty
			this.symbol = TreeAdapter.getType((ITree) cons);
		}
		else if (cons.getConstructorType() == RascalValueFactory.Tree_Amb) {
			ISet alts = TreeAdapter.getAlternatives((ITree) cons);
			
			if (!alts.isEmpty()) {
				ITree first = (ITree) alts.iterator().next();
				this.symbol = TreeAdapter.getType(first);
			}
			else {
				this.symbol = IRascalValueFactory.getInstance().constructor(RascalValueFactory.Symbol_Empty);
			}
		}
		else if (cons.getConstructorType() == RascalValueFactory.Tree_Char) {
		    this.symbol = TreeAdapter.getType((ITree) cons);
		}
		else if (cons.getConstructorType() == RascalValueFactory.Tree_Cycle) {
			this.symbol = TreeAdapter.getType((ITree) cons);
		}
		else if (cons.getType() == RascalValueFactory.Symbol) {
			this.symbol = cons;
		}
		else if (cons.getType() == RascalValueFactory.Production) {
			this.symbol = ProductionAdapter.getType(cons);
		}
		else {
			throw new ImplementationError("Invalid concrete syntax type constructor:" + cons);
		}
	}
	
    /*package*/ NonTerminalType(org.rascalmpl.ast.Type type, boolean lex, String layout) {
		this(SymbolFactory.typeToSymbol(type, lex, layout));
	}
	
    public static class Reifier extends TypeReifier {

        public Reifier(TypeValues symbols) {
			super(symbols);
		}

		@Override
        public Set<Type> getSymbolConstructorTypes() {
            return Arrays.stream(new Type[] {
                   RascalValueFactory.Symbol_Label,
                   RascalValueFactory.Symbol_Start,
                   RascalValueFactory.Symbol_Lit,
                   RascalValueFactory.Symbol_Cilit,
                   RascalValueFactory.Symbol_Empty,
                   RascalValueFactory.Symbol_Seq,
                   RascalValueFactory.Symbol_Opt,
                   RascalValueFactory.Symbol_Alt,
                   RascalValueFactory.Symbol_Sort,
                   RascalValueFactory.Symbol_Lex,
                   RascalValueFactory.Symbol_Keywords,
                   RascalValueFactory.Symbol_Meta,
                   RascalValueFactory.Symbol_Conditional,
                   RascalValueFactory.Symbol_IterSeps,
                   RascalValueFactory.Symbol_IterStarSeps,
                   RascalValueFactory.Symbol_Iter,
                   RascalValueFactory.Symbol_IterStar,
                   RascalValueFactory.Symbol_ParameterizedSort,
                   RascalValueFactory.Symbol_ParameterizedLex,
                   RascalValueFactory.Symbol_Parameter,
                   RascalValueFactory.Symbol_Layouts,
                   RascalValueFactory.Symbol_CharClass,
                   RascalValueFactory.Production_Default
            }).collect(Collectors.toSet());
        }
        
        @Override
        public Type getSymbolConstructorType() {
            // this reifier is for multiple constructor types
            throw new UnsupportedOperationException();
        }

        @Override
        public Type fromSymbol(IConstructor symbol, TypeStore store, Function<IConstructor, Set<IConstructor>> grammar) {
            if (symbols().isLabel(symbol)) {
                symbol = symbols().getLabeledSymbol(symbol);
            }
            
            return RTF.nonTerminalType((IConstructor) symbol);
        }

        @Override
        public void asProductions(Type type, IValueFactory vf, TypeStore store, ISetWriter grammar,
                Set<IConstructor> done) {
            if (store instanceof TypeStoreWithSyntax) {
                TypeStoreWithSyntax syntax = (TypeStoreWithSyntax) store;
                addRulesForSort(vf, ((NonTerminalType) type).symbol, syntax, grammar, new HashSet<>());
            }
        }
        
        @Override
        public IConstructor toSymbol(Type type, IValueFactory vf, TypeStore store,  ISetWriter grammar, Set<IConstructor> done) {
            asProductions(type, vf, store, grammar, done);
            return ((NonTerminalType) type).symbol;
        }
        
        private void addRulesForSymbol(IValueFactory vf, IConstructor sort, TypeStoreWithSyntax syntax, ISetWriter grammar, Set<IConstructor> done) {
            if (SymbolAdapter.isLex(sort) || SymbolAdapter.isSort(sort) || SymbolAdapter.isKeyword(sort)) {
                addRulesForSort(vf, sort, syntax, grammar, done); 
            }
            else { // composite symbol needs to be traversed
                sort.accept(new BottomUpVisitor<>(new IdentityVisitor<RuntimeException>() {
                    @Override
                    public IValue visitConstructor(IConstructor o) throws RuntimeException {
                        if (o.getType() == RascalValueFactory.Symbol) {
                            addRulesForSort(vf, o, syntax, grammar, done);
                        }
                        return o;
                    }
                }, vf));
            }
        }
        
        private void addRulesForSort(IValueFactory vf, IConstructor sort, TypeStoreWithSyntax syntax, ISetWriter grammar, Set<IConstructor> done) {
            if (done.contains(sort)) {
                return;
            }
            
            for (IValue rule : syntax.getRules(sort)) {
                grammar.insert(vf.tuple(sort, rule));
                done.add(sort);
                
                if (ProductionAdapter.isDefault((IConstructor) rule)) {
                    for (IValue arg : ProductionAdapter.getSymbols((IConstructor) rule)) {
                        addRulesForSymbol(vf, (IConstructor) arg, syntax, grammar, done);
                    }
                }
            }
        }

        @Override
        public boolean isRecursive() {
            return true;
        }
        
        @Override
        public Type randomInstance(Supplier<Type> next, TypeStore store, RandomTypesConfig rnd) {
            // TODO: the interpreter breaks on random non-terminals still, so we return a string instead
            return TypeFactory.getInstance().stringType();
//            return RascalTypeFactory.getInstance().nonTerminalType(vf.constructor(RascalValueFactory.Symbol_Sort, vf.string(randomLabel(rnd))));
        }
    }
    
    @Override
    public TypeReifier getTypeReifier(TypeValues symbols) {
        return new Reifier(symbols);
    }
    
    @Override
    public boolean isNonterminal() {
    	return true;
    }
    
    @Override
    public boolean isAbstractData() {
        // because all instances of NonTerminalType values also simulate
        // Tree constructors like appl, amb, char and cycle and generic
        // functionality in the run-time on those trees trigger on this test.
        return true;
    }
    
    @Override
    public Type asAbstractDataType() {
    	return RascalValueFactory.Tree;
    }
    
	public IConstructor getSymbol() {
		return symbol;
	}
	
	public int getArity() {
		return symbol.arity();
	}
	
	public boolean isConcreteListType() {
		return SymbolAdapter.isAnyList(getSymbol());
	}
	
	public boolean isOptionalType() {
		return SymbolAdapter.isOpt(getSymbol());
	}
	
	@Override
	public Type getAbstractDataType() {
	  return RascalValueFactory.Tree;
	}
	
	@Override
	public boolean hasField(String fieldName) {
		// safe over-approximation
		return true;
	}
	
	@Override
	public boolean hasKeywordField(String fieldName, TypeStore store) {
	    return RascalValueFactory.Tree.hasKeywordField(fieldName, store);
	}
	
	@Override
	public String getName() {
		return RascalValueFactory.Tree.getName();
	}
	
	@Override
	public Type getTypeParameters() {
		return RascalValueFactory.Tree.getTypeParameters();
	}
	
	@Override
	public <T, E extends Throwable> T accept(IRascalTypeVisitor<T,E> visitor) throws E {
		return visitor.visitNonTerminal(this);
	}
	
	@Override
	protected boolean isSubtypeOfAbstractData(Type type) {
	  return type.equivalent(RascalValueFactory.Tree);
	}
	
	@Override
	protected boolean isSubtypeOfNode(Type type) {
	  return true;
	}
	
	@Override
	protected Type lubWithNode(Type type) {
	  return type;
	}
	
	@Override
	protected Type lubWithAbstractData(Type type) {
	  return type.equivalent(RascalValueFactory.Tree) ? type : TF.nodeType(); 
	}
	
	@Override
	protected Type lubWithConstructor(Type type) {
	  return type.getAbstractDataType().equivalent(RascalValueFactory.Tree) ? RascalValueFactory.Tree : TF.nodeType();
	}
	
	@Override
	protected Type glbWithNode(Type type) {
	  return this;
	}
	
	@Override
	protected Type glbWithAbstractData(Type type) {
	  return type.equivalent(RascalValueFactory.Tree) ? this : TF.voidType(); 
	}
	
	@Override
	protected Type glbWithConstructor(Type type) {
	  return TF.voidType();
	}

	
	@Override
	protected boolean isSupertypeOf(Type type) {
	  if (type instanceof NonTerminalType) {
	    return ((NonTerminalType) type).isSubtypeOfNonTerminal(this);
	  }
	  
	  return super.isSupertypeOf(type);
	}
	
	@Override
	protected boolean isSupertypeOf(RascalType type) {
	  return type.isSubtypeOfNonTerminal(this);
	}
	
	@Override
	protected Type lub(RascalType type) {
	  return type.lubWithNonTerminal(this);
	}
	
	@Override
	protected Type glb(RascalType type) {
		return type.glbWithNonTerminal(this);
	}
	
	@Override
	public boolean intersects(Type other) {
		if (other == RascalValueFactory.Tree) {
			return true;
		}
		else if (other instanceof NonTerminalType) {
			return ((NonTerminalType) other).intersectsWithNonTerminal(this);
		}
		else {
			return false;
		}
	}

	@Override
	protected boolean intersects(RascalType type) {
	    return type.intersectsWithNonTerminal(this);
	}

	@Override
	protected boolean intersectsWithNonTerminal(RascalType type) {
	    return this.comparable(type);
	}

	@Override
	protected boolean intersectsWithAbstractData(Type type) {
		if (type == RascalValueFactory.Tree) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	protected boolean intersectsWithNode(Type type) {
		return true;
	}
	
	@Override
	public boolean isSubtypeOfNonTerminal(RascalType other) {
		if (other == this) {
			return true;
		}
	  IConstructor otherSym = ((NonTerminalType)other).symbol;
	 
	  if ((SymbolAdapter.isIterPlus(symbol) && (SymbolAdapter.isIterStar(otherSym) || SymbolAdapter.isIterPlus(otherSym)))
		 || (SymbolAdapter.isIterStar(symbol) && SymbolAdapter.isIterStar(otherSym))) {
		  RascalType nt1 = (RascalType) RTF.nonTerminalType(SymbolAdapter.getSymbol(symbol));
		  RascalType nt2 = (RascalType) RTF.nonTerminalType(SymbolAdapter.getSymbol(otherSym));
		  return nt1.isSubtypeOfNonTerminal(nt2);
	  }
	  else if ((SymbolAdapter.isIterPlusSeps(symbol) && (SymbolAdapter.isIterStarSeps(otherSym) || SymbolAdapter.isIterPlusSeps(otherSym)))
			  || (SymbolAdapter.isIterStarSeps(symbol) && SymbolAdapter.isIterStarSeps(otherSym))) {
		  RascalType nt1 = (RascalType) RTF.nonTerminalType(SymbolAdapter.getSymbol(symbol));
		  RascalType nt2 = (RascalType) RTF.nonTerminalType(SymbolAdapter.getSymbol(otherSym));
		  
		  if (nt1.isSubtypeOfNonTerminal(nt2)) {
			  IList seps1 = SymbolAdapter.getSeparators(symbol);
			  IList seps2 = SymbolAdapter.getSeparators(otherSym);

			  // this works around broken regular prods in the RVM which have the wrong or missing layout symbols:
			  int sep1index = seps1.length() == 3 ? 1 : 0;
			  int sep2index = seps2.length() == 3 ? 1 : 0;

			  nt1 = (RascalType) RTF.nonTerminalType((IConstructor) seps1.get(sep1index));
			  nt2 = (RascalType) RTF.nonTerminalType((IConstructor) seps2.get(sep2index));
			  return nt1.isSubtypeOfNonTerminal(nt2);
		  }

		  return false;
	  }
	  else if (SymbolAdapter.isOpt(symbol) && SymbolAdapter.isOpt(otherSym)) {
		  RascalType nt1 = (RascalType) RTF.nonTerminalType(SymbolAdapter.getSymbol(symbol));
		  RascalType nt2 = (RascalType) RTF.nonTerminalType(SymbolAdapter.getSymbol(otherSym));
		  return nt1.isSubtypeOfNonTerminal(nt2);
	  }
	  else if (SymbolAdapter.isCharClass(symbol) && SymbolAdapter.isCharClass(otherSym)) {
	      // note that the ranges are normalized and ordered! see Characters.rsc
	      // we check if all ranges can be included in at least one of the ranges of the other class:
	      
	      OUTER:for (IValue elem : SymbolAdapter.getRanges(symbol)) {
	         IInteger from = (IInteger) ((IConstructor) elem).get("begin");
	         IInteger to = (IInteger) ((IConstructor) elem).get("end");
	         
	         for (IValue otherElem : SymbolAdapter.getRanges(otherSym)) {
	             IInteger otherFrom = (IInteger) ((IConstructor) otherElem).get("begin");
	             IInteger otherTo = (IInteger) ((IConstructor) otherElem).get("end");
	             
	             if (from.greaterEqual(otherFrom).getValue() && to.lessEqual(otherTo).getValue()) {
	                 // matched! continue with next range
	                 continue OUTER;
	             }
	         }
	         
	         // no match found
	         return false;
	      }
	      
	      // all ranges checked
	      return true;
	  }

	  if (SymbolAdapter.isParameter(otherSym)) {
		  RascalType bound = (RascalType) RTF.nonTerminalType((IConstructor) otherSym.get("bound"));
		  return isSubtypeOf(bound);
	  }
	  
	  // TODO co-variance for the other structured symbols (sequence, opt, list)
	  return SymbolAdapter.isEqual(otherSym, symbol);
	}
	
	@Override
	protected Type lubWithNonTerminal(RascalType other) {
		IConstructor otherSym = ((NonTerminalType)other).symbol;

		// * eats +
		if (SymbolAdapter.isIterPlus(symbol) && SymbolAdapter.isIterStar(otherSym)) {
			return other;
		}
		else if (SymbolAdapter.isIterPlus(otherSym) && SymbolAdapter.isIterStar(symbol)) {
			return this;
		}
		else if (SymbolAdapter.isIterPlusSeps(symbol) && SymbolAdapter.isIterStarSeps(otherSym)) {
			return other;
		}
		else if (SymbolAdapter.isIterPlusSeps(otherSym) && SymbolAdapter.isIterStarSeps(symbol)) {
			return this;
		}
		else if (SymbolAdapter.isCharClass(symbol) && SymbolAdapter.isCharClass(otherSym)) {
		    return RTF.nonTerminalType(SymbolAdapter.unionCharClasses(symbol, otherSym));
		}

		return SymbolAdapter.isEqual(otherSym, symbol) ? this : RascalValueFactory.Tree;
	}

	@Override
	protected Type glbWithNonTerminal(RascalType other) {
	    IConstructor otherSym = ((NonTerminalType)other).symbol;

	    if (SymbolAdapter.isIterPlus(symbol) && SymbolAdapter.isIterStar(otherSym)) {
	        return this;
	    }
	    else if (SymbolAdapter.isIterPlus(otherSym) && SymbolAdapter.isIterStar(symbol)) {
	        return other;
	    }
	    else if (SymbolAdapter.isIterPlusSeps(symbol) && SymbolAdapter.isIterStarSeps(otherSym)) {
	        return this;
	    }
	    else if (SymbolAdapter.isIterPlusSeps(otherSym) && SymbolAdapter.isIterStarSeps(symbol)) {
	        return other;
	    }
	    else if (SymbolAdapter.isCharClass(otherSym) && SymbolAdapter.isCharClass(symbol)) {
	        return RTF.nonTerminalType(SymbolAdapter.intersectCharClasses(symbol, otherSym));
	    }

	    return SymbolAdapter.isEqual(otherSym, symbol) ? other : TF.voidType();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (obj.getClass() == getClass()) {
			NonTerminalType other = (NonTerminalType) obj;
			return symbol.equals(other.symbol);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return 133333331 + 1331 * symbol.hashCode();
	}
	
	@Override
	public String toString() {
		return SymbolAdapter.toString(symbol, false);
	}
	
	@Override
	public IValue randomValue(Random random, IValueFactory vf, TypeStore store, Map<Type, Type> typeParameters,
	    int maxDepth, int maxBreadth) {
	    // TODO this should be made more carefully (use the grammar to generate a true random instance of the 
	    // given non-terminal
	    IRascalValueFactory rvf = (IRascalValueFactory) vf;
	    
	    // TODO: this generates an on-the-fly nullable production and returns a tree for that rule
	    return rvf.appl(vf.constructor(RascalValueFactory.Production_Default, symbol, vf.list(), vf.set()), vf.list());
	}
}
