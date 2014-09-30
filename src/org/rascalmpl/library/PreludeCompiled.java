package org.rascalmpl.library;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.values.uptr.TreeAdapter;

/*
 * This class overrides methods from Prelude that need to be handled differenty in compiled code.
 * In most (all?) cases this will be library function with a @reflect{...} tag that makes them dependent on
 * IEvaluatorContext, the context of the Rascal interpreter.
 */
public class PreludeCompiled extends Prelude {

	public PreludeCompiled(IValueFactory values) {
		super(values);
	}
	
	@Override
	// public java &T<:Tree parse(type[&T<:Tree] begin, str input);
	public IValue parse(IValue start, ISourceLocation input, IEvaluatorContext ctx) {
		return RascalPrimitive.getParsingTools().parse(super.values.string("XXX"), start, input);
	}
	
	@Override
	// public java &T<:Tree parse(type[&T<:Tree] begin, str input, loc origin);
	public IValue parse(IValue start, IString input, IEvaluatorContext ctx) {
		return RascalPrimitive.getParsingTools().parse(super.values.string("XXX"), start, input, null);
	}
	
	private TypeStore typeStore = new TypeStore();
	
	@Override
	public IConstructor makeConstructor(Type returnType, String name, IEvaluatorContext ctx, IValue ...args) {
		// TODO: in general, the following should be the call to an overloaded function
		IValue value = values.constructor(typeStore.lookupConstructor(returnType, name, TypeFactory.getInstance().tupleType(args)), args, new HashMap<String, IValue>());
		Type type = value.getType();
		if (type.isAbstractData()) {
			return (IConstructor)value;
		}
		throw RuntimeExceptionFactory.implodeError("Calling of constructor " + name + " did not return a constructor", null, null);
	}
	
	@Override
	public IValue implode(IValue reifiedType, IConstructor tree, IEvaluatorContext ctx) {
		typeStore = new TypeStore();
		Type type = tr.valueToType((IConstructor) reifiedType, typeStore);
		try {
			IValue result = implode(typeStore, type, tree, false, ctx); 
			if (isUntypedNodeType(type) && !type.isTop() && (TreeAdapter.isList(tree) || TreeAdapter.isOpt(tree))) {
				result = values.node("", result);
			}
			return result;
		}
		catch (Backtrack b) {
			throw b.exception;
		}
	}

}
