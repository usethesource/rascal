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
package org.rascalmpl.interpreter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.uptr.IRascalValueFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;

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
        IMap index = new Prelude(vf).index(definitions.done()); // TODO: bring index functionality to rascal.value library 
        IMapWriter grammar = vf.mapWriter(); 
        
        // and here we wrap the set of constructors in a choice operator
        for (IValue sym : index) {
            ISet alts = (ISet) index.get(sym);
            grammar.put(sym, vf.constructor(RascalValueFactory.Production_Choice, sym, alts));
        }
        
        return IRascalValueFactory.getInstance().reifiedType(symbol, grammar.done());
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

            return Type.fromSymbol((IConstructor) typeValue.get("symbol"), store, x -> getAlternatives(definitions, x));
        }

        throw new IllegalArgumentException(typeValue + " is not a reified type");
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
        
        IConstructor adt = (IConstructor) prod.get("def");
        return Type.fromSymbol(adt, new TypeStore(), x -> x == adt ? Collections.singleton(prod) : Collections.emptySet()); 
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

    private Set<IConstructor> getAlternatives(IMap definitions, IConstructor x) {
        IConstructor choice = (IConstructor) definitions.get(x);

        if (choice == null || choice.getConstructorType() != RascalValueFactory.Production_Choice) {
            return Collections.emptySet();
        }

        Set<IConstructor> result = new HashSet<>();
        for (IValue alt : ((ISet) choice.get("alternatives"))) {
            result.add((IConstructor) alt); 
        }

        return result;
    }

    /**
     * Reconstruct a type from a reified type value, and declare all types used first using the given definitions 
     * @param typeValue the type value to restore
     * @param definitions the definition
     */
    public Type symbolToType(IConstructor symbol, IMap definitions) {
        return Type.fromSymbol(symbol, new TypeStore(), x -> getAlternatives(definitions, x));
    }

    private IConstructor reify(Type t, ISetWriter definitions, final TypeStore store, IMap syntax) {
        return t.asSymbol(vf, new TypeStoreWithSyntax(store, syntax), definitions, new HashSet<>());
    }
    
    public static class TypeStoreWithSyntax extends TypeStore {
        private final IMap grammar;

        public TypeStoreWithSyntax(TypeStore parent, IMap grammar) {
            this.grammar = grammar;
            extendStore(parent);
        }
        
        public ISet getRules(IConstructor nt) {
            return (ISet) ((IConstructor) grammar.get(nt)).get("alternatives");
        }
    }
}
