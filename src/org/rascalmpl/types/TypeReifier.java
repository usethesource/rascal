/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *******************************************************************************/
package org.rascalmpl.types;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.rascalmpl.library.Prelude;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.SymbolAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

/**
 * TypeReifier maps types to values that represent these types and their definitions. These values have
 * very specific types, namely 'type[&T]' where &T is bound to the type the value represents. 
<pre>
data type[&T] = type(Symbol symbol, map[Symbol, Production] definitions);
</pre>
 */
public class TypeReifier {
    private final IValueFactory vf;

    public TypeReifier(IValueFactory valueFactory) {
        this.vf = valueFactory;
    }

    public IConstructor typeToValue(Type t, TypeStore store, IMap syntax) {
        // after this definitions contains a relation from adt to constructors
        ISetWriter definitions = vf.setWriter();
        IConstructor symbol = reify(t, definitions, store, syntax);

        // now we hash the relation on adt, resulting in a map from adt to set of constructors
        IMap index = Prelude.index(vf, definitions.done()); // TODO: bring index functionality to rascal-value library 
        IMapWriter grammar = vf.mapWriter(); 
        
        // and here we wrap the set of constructors in a choice operator
        for (IValue sym : index) {
            ISet alts = (ISet) index.get(sym);
            grammar.put(sym, vf.constructor(RascalValueFactory.Production_Choice, sym, alts));
        }
        
        return IRascalValueFactory.getInstance().reifiedType(symbol, grammar.done());
    }
    
    // TODO experimental; if you know the symbol you need to 
    // generate a TypeStore for:
    public TypeStore buildTypeStore(IConstructor root, IMap symbol_definitions) {
        TypeStore ts = new TypeStore();
        TypeFactory.getInstance().fromSymbol(root, new TypeStore(), x -> getAlternatives(symbol_definitions, x));
        return ts;
    }
    
    // TODO experimental; if need to convert an entire grammar to a TypeStore:
    public TypeStore buildTypeStore(IMap symbol_definitions) {
        TypeStore ts = new TypeStore();
        
        // TODO this looks slow, but fromSymbol will actually not recursive
        // through sorts which have already been stored in the TypeStore before!
        for (IValue sort : symbol_definitions) {
            TypeFactory.getInstance().fromSymbol((IConstructor) sort, ts, x -> getAlternatives(symbol_definitions, x));
        }
        
        return ts;
    }

    public Type valueToType(IConstructor typeValue) {
        return valueToType(typeValue, new TypeStore());
    }

    /**
     * Reconstruct a type from a reified type value and declare all types used.
     * @param typeValue the type value to restore
     * @param store     a possibly empty store that will be filled with the relevant type declarations.
     * @return the type corresponding to the reified type value 
     */
    public Type valueToType(IConstructor typeValue, TypeStore store) {
        if (typeValue.getType() instanceof ReifiedType) {
            /* Although the type of this value contains already a type, it may be more general
             * then the type represented by the symbol. So, we do have to map the symbol to
             * a type recursively.
             * 
             * We also need to construct a TypeStore from the declarations, such that the 
             * appropriate definitions for ADT's and aliases can be found.
             */
            IMap definitions = (IMap) typeValue.get("definitions");

            return TypeFactory.getInstance().fromSymbol((IConstructor) typeValue.get("symbol"), store, x -> getAlternatives(definitions, x));
        }

        throw new IllegalArgumentException(typeValue + " is not a reified type.");
    }
    
    /**
     * Converts a production  a constructor type,
     * all side-effects in typestores are discarded. 
     * @param prod  value which represents a constructor definition as produced by typeToValue or the compiler
     * @return a constructor type as if production by @{link TypeFactory}
     */
    public Type productionToConstructorType(IConstructor prod) {
        if (prod.getConstructorType() == RascalValueFactory.Symbol_Prod) {
            return productionToConstructorType(convertSymbolProdToProduction(prod));
        }
        
        // TODO remove check after bootstrap suc6
        IConstructor adt = (IConstructor) (prod.has("adt") ?  prod.get("adt") : prod.get("def"));
        return TypeFactory.getInstance().fromSymbol(prod, new TypeStore(), x -> x == adt ? Collections.singleton(prod) : Collections.emptySet()); 
    }

    /**
     * The type checker uses a production representation of type Symbol internally,
     * which we convert here to a production representation of type Production for further processing
     */
    private IConstructor convertSymbolProdToProduction(IConstructor prod) {
        IValue sort = prod.get("sort");
        IValue parameters = prod.get("parameters");
        IValue attributes = prod.get("attributes");
        return vf.constructor(RascalValueFactory.Production_Default, sort, parameters, attributes);
    }

    /**
     * Get the alternatives of a definitions from a grammar map.
     * Must take care to lookup parameterized non-terminals and data-types
     * by name instead of by instantiated symbol.
     * 
     * @param definitions rel[Symbol, Production.choice]
     * @param sort        a Symbol
     * @return
     */
    private Set<IConstructor> getAlternatives(IMap definitions, IConstructor sort) {
        if (SymbolAdapter.isParametrizableType(sort) && !SymbolAdapter.getParameters(sort).isEmpty()) {
            // slow path can not lookup symbols by structural id, but has to look
            // them up by name slowly
            return getParameterizedAlternatives(definitions, sort);
        }
        else {
            return getSimpleAlternatives(definitions, sort);
        }
    }

    private Set<IConstructor> getSimpleAlternatives(IMap definitions, IConstructor sort) {
        IConstructor choice = (IConstructor) definitions.get(sort);
        if (choice == null || choice.getConstructorType() != RascalValueFactory.Production_Choice) {
            return Collections.emptySet();
        }

        Set<IConstructor> result = new HashSet<>();
        for (IValue alt : ((ISet) choice.get("alternatives"))) {
            result.add((IConstructor) alt); 
        }

        return result;
    }

    private Set<IConstructor> getParameterizedAlternatives(IMap definitions, IConstructor sort) {
        String name = SymbolAdapter.getName(sort);

        // lookup the definition by name instead of by symbol
        for (IValue elem : definitions) {
            IConstructor symbol = (IConstructor) elem;
            if (SymbolAdapter.isADT(symbol) && SymbolAdapter.getName(symbol).equals(name)) {
                // reuse the lookup by using the declared open symbol now
                return getSimpleAlternatives(definitions, symbol);
            }
        }

        return Set.of();
    }

    /**
     * Reconstruct a type from a reified type value; without declaration side-effect.
     * If you need a TypeStore reconstructed see the other overload of symbolToType.  
     * @param typeValue the type value to restore
     * @param definitions the definition
     */
    public Type symbolToType(IConstructor symbol) {
        return TypeFactory.getInstance().fromSymbol(symbol, new TypeStore(), x -> Collections.emptySet());
    }
    
    /**
     * Reconstruct a type from a reified type value, and declare all types used first using the given definitions 
     * @param typeValue the type value to restore
     * @param definitions the definition
     */
    public Type symbolToType(IConstructor symbol, IMap definitions) {
        return TypeFactory.getInstance().fromSymbol(symbol, new TypeStore(), x -> getAlternatives(definitions, x));
    }

    private IConstructor reify(Type t, ISetWriter definitions, final TypeStore store, IMap syntax) {
        return t.asSymbol(vf, new TypeStoreWithSyntax(vf, store, syntax), definitions, new HashSet<>());
    }
    
    public static class TypeStoreWithSyntax extends TypeStore {
        private final IMap grammar;
        private final IValueFactory vf;

        public TypeStoreWithSyntax(IValueFactory vf, TypeStore parent, IMap grammar) {
            this.grammar = grammar;
            this.vf = vf;
            extendStore(parent);
        }
        
        public IMap getGrammar() {
            return grammar;
        }
        
        public ISet getRules(IConstructor nt) {
            IConstructor choice = (IConstructor) grammar.get(nt);
            
            return choice != null ? (ISet) choice.get("alternatives") : vf.setWriter().done();
        }
    }
}
