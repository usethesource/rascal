package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.rascalmpl.interpreter.TypeReifier;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;

public class Types {
    private final TypeReifier tr;
	
	public Types(IValueFactory vf) {
		this.tr = new TypeReifier(vf);
	}
	
	public IValue typeToValue(Type t, RascalExecutionContext rex) {
	    // TODO: check rex.getSymbolDefinitions(); don't know if this is correct
	    System.err.println("TODO: check correctness of typeToValue here");
		return tr.typeToValue(t, rex.getTypeStore(), rex.getSymbolDefinitions());
	}
	
	public Type symbolToType(IConstructor symbol) {
	    return tr.symbolToType(symbol);
	}
	
	public Type loadProduction(IConstructor prod) {
	    return tr.productionToConstructorType(prod);
        }
}
