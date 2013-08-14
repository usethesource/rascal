package org.rascalmpl.library.experiments.CoreRascal.RVM;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.SymbolAdapter;

public class Types {
	
	private final IValueFactory vf;
	private final TypeFactory tf = TypeFactory.getInstance();
	
	public Types(IValueFactory vf) {
		this.vf = vf;
	}
	
	public Type symbolToType(IConstructor symbol, TypeStore store) {
		Type cons = symbol.getConstructorType();
		
		if (cons == Factory.Symbol_Int) {
			return tf.integerType();
		}
		else if (cons == Factory.Symbol_Real) {
			return tf.realType();
		}
		else if (cons == Factory.Symbol_Rat) {
			return tf.rationalType();
		}
		else if (cons == Factory.Symbol_Bool) {
			return tf.boolType();
		}
		else if (cons == Factory.Symbol_Datetime) {
			return tf.dateTimeType();
		}
		else if (cons == Factory.Symbol_Num) {
			return tf.numberType();
		}
		else if (cons == Factory.Symbol_Loc) {
			return tf.sourceLocationType();
		}
		else if (cons == Factory.Symbol_Adt) {
			return adtToType(symbol, store);
		}
		else if (cons == Factory.Symbol_Alias){
			return aliasToType(symbol, store);
		}
		else if (cons == Factory.Symbol_Bag) {
			throw new NotYetImplemented("bags are not implemented yet");
		}
		else if (cons == Factory.Symbol_Cons) {
			return consToType(symbol, store);
		}
		else if (cons == Factory.Symbol_Func) {
			return funcToType(symbol, store);
		}
		else if (cons == Factory.Symbol_Label) {
			return symbolToType((IConstructor) symbol.get("symbol"), store);
		}
		else if (cons == Factory.Symbol_Map) {
			return mapToType(symbol, store);
		}
		else if (cons == Factory.Symbol_Node) {
			return tf.nodeType();
		}
		else if (cons == Factory.Symbol_Parameter) {
			return tf.parameterType(((IString) symbol.get("name")).getValue());
		}
		else if (cons == Factory.Symbol_BoundParameter) {
			return tf.parameterType(((IString) symbol.get("name")).getValue(), symbolToType((IConstructor) symbol.get("bound"), store));
		}
		else if (cons == Factory.Symbol_ReifiedType) {
			return RascalTypeFactory.getInstance().reifiedType(symbolToType((IConstructor) symbol.get("reified"), store));
		}
		else if (cons == Factory.Symbol_Rel) {
			return tf.relTypeFromTuple(symbolsToTupleType((IList) symbol.get("symbols"), store));
		}
		else if (cons == Factory.Symbol_ListRel) {
			return tf.lrelTypeFromTuple(symbolsToTupleType((IList) symbol.get("symbols"), store));
		}
		else if (cons == Factory.Symbol_Set) {
			return tf.setType(symbolToType((IConstructor) symbol.get("symbol"), store));
		}
		else if (cons == Factory.Symbol_List) {
			return tf.listType(symbolToType((IConstructor) symbol.get("symbol"), store));
		}
		else if (cons == Factory.Symbol_Str) {
			return tf.stringType();
		}
		else if (cons == Factory.Symbol_Tuple) {
			return tupleToType(symbol, store);
		}
		else if (cons == Factory.Symbol_Void) {
			return tf.voidType();
		}
		else if (cons == Factory.Symbol_Value) {
			return tf.valueType();
		}
		else {
			// We assume the other types are one of the non-terminal symbols
			return RascalTypeFactory.getInstance().nonTerminalType(symbol);
		}
	}

	private Type tupleToType(IConstructor symbol, TypeStore store) {
		return symbolsToTupleType((IList) symbol.get("symbols"), store);
	}

	private Type symbolsToTupleType(IList symbols, TypeStore store) {
		boolean allLabels = true;
		Type[] types = new Type[symbols.length()];
		String[] labels = new String[symbols.length()];
		
		for (int i = 0; i < symbols.length(); i++) {
			IConstructor elem = (IConstructor) symbols.get(i);
			if (elem.getConstructorType() == Factory.Symbol_Label) {
				labels[i] = ((IString) elem.get("name")).getValue();
				elem = (IConstructor) elem.get("symbol");
			}
			else {
				allLabels = false;
			}
			
			types[i] = symbolToType(elem, store);
		}
		
		if (allLabels) {
			return tf.tupleType(types, labels);
		}
		else {
			return tf.tupleType(types);
		}
	}
	
	private Type mapToType(IConstructor symbol, TypeStore store) {
		IConstructor from = (IConstructor) symbol.get("from");
		IConstructor to = (IConstructor) symbol.get("to");
		String fromLabel = null;
		String toLabel = null;
		
		if (SymbolAdapter.isLabel(from)) {
			fromLabel = SymbolAdapter.getLabel(from);
			from = (IConstructor) from.get("symbol");
		}
		if (SymbolAdapter.isLabel(to)) {
			toLabel = SymbolAdapter.getLabel(to);
			to = (IConstructor) to.get("symbol");
		}
		if (fromLabel != null && toLabel != null) {
			return tf.mapType(symbolToType(from, store), fromLabel, symbolToType(to, store), toLabel);
		}
		else {
			return tf.mapType(symbolToType(from, store), symbolToType(to, store));
		}
	}

	private Type funcToType(IConstructor symbol, TypeStore store) {
		Type returnType = symbolToType((IConstructor) symbol.get("ret"), store);
		Type parameters = symbolsToTupleType((IList) symbol.get("parameters"), store);
		return RascalTypeFactory.getInstance().functionType(returnType, parameters);
	}

	private Type consToType(IConstructor symbol, TypeStore store) {
		Type adt = symbolToType((IConstructor) symbol.get("adt"), store);
		IList parameters = (IList) symbol.get("parameters");
		String name = ((IString) symbol.get("name")).getValue();
		Type tupleType = symbolsToTupleType(parameters, store);
		Type t = tf.constructorFromTuple(store, adt, name, tupleType);
		return t;
		
	}

	private Type aliasToType(IConstructor symbol, TypeStore store) {
		String name = ((IString) symbol.get("name")).getValue();
		Type aliased = symbolToType((IConstructor) symbol.get("aliased"), store);
		IList parameters = (IList) symbol.get("parameters");
		
		if (parameters.isEmpty()) {
			return tf.aliasType(store, name, aliased);
		}
		else {
			return tf.aliasTypeFromTuple(store, name, aliased, symbolsToTupleType(parameters, store));
		}
	}

	private Type adtToType(IConstructor symbol, TypeStore store) {
		String name = ((IString) symbol.get("name")).getValue();
		Type adt = store.lookupAbstractDataType(name);
		
		if (adt == null) {
			Type params = symbolsToTupleType((IList) symbol.get("parameters"), store);
			if (params.isBottom() || params.getArity() == 0) {
				adt = tf.abstractDataType(store, name);
			}
			else {
				adt = tf.abstractDataTypeFromTuple(store, name, params);
			}
		}
		
		return adt;
	}


}
