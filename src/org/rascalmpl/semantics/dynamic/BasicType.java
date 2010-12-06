package org.rascalmpl.semantics.dynamic;

import org.rascalmpl.values.uptr.Factory;

public abstract class BasicType extends org.rascalmpl.ast.BasicType {


public BasicType (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class ReifiedType extends org.rascalmpl.ast.BasicType.ReifiedType {


public ReifiedType (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("type should have at one type argument, like type[value].", this);
		
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getTypeArgument().getArity() == 1) {
			return org.rascalmpl.interpreter.types.RascalTypeFactory.getInstance().reifiedType(__eval.__getTypeArgument().getFieldType(0));
		}
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("type should have exactly one type argument, like type[value]", this);
	
}

}
static public class ReifiedNonTerminal extends org.rascalmpl.ast.BasicType.ReifiedNonTerminal {


public ReifiedNonTerminal (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getValueArguments() == null) {
			throw new org.rascalmpl.interpreter.asserts.ImplementationError("missing value arguments to construct non-terminal type");
		}
		
		if (__eval.__getValueArguments().length == 1) {
			if (__eval.__getValueArguments()[0].getType() == Factory.Symbol) {
				return org.rascalmpl.interpreter.types.RascalTypeFactory.getInstance().reifiedType(org.rascalmpl.interpreter.types.RascalTypeFactory.getInstance().nonTerminalType((org.eclipse.imp.pdb.facts.IConstructor) __eval.__getValueArguments()[0]));
			}
		}
		
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("a reified non-terminal type should look like non-terminal(Symbol symbol, this)", this);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("a reified non-terminal type should look like non-terminal(Symbol symbol, this)", this);
		
}

}
static public class String extends org.rascalmpl.ast.BasicType.String {


public String (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return org.rascalmpl.interpreter.TypeEvaluator.__getTf().stringType();
		
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getTypeArgument().getArity() == 0) {
			return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf().stringType();
		}
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("string cannot have type arguments.", this);
	
}

}
static public class Value extends org.rascalmpl.ast.BasicType.Value {


public Value (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getTypeArgument().getArity() == 0) {
			return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf().valueType();
		}
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("value cannot have type arguments.", this);
	
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return org.rascalmpl.interpreter.TypeEvaluator.__getTf().valueType();
		
}

}
static public class ReifiedFunction extends org.rascalmpl.ast.BasicType.ReifiedFunction {


public ReifiedFunction (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getTypeArgument().getArity() < 1) {
			throw new org.rascalmpl.interpreter.asserts.ImplementationError("a reified function type has at least a return type, as in fun(int).");
		}

		throw new org.rascalmpl.interpreter.asserts.NotYetImplemented(this);
	
}

}
static public class ReifiedTypeParameter extends org.rascalmpl.ast.BasicType.ReifiedTypeParameter {


public ReifiedTypeParameter (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Void extends org.rascalmpl.ast.BasicType.Void {


public Void (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getTypeArgument().getArity() == 0) {
			return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf().voidType();
		}
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("void cannot have type arguments.", this);
	
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return org.rascalmpl.interpreter.TypeEvaluator.__getTf().voidType();
		
}

}
static public class DateTime extends org.rascalmpl.ast.BasicType.DateTime {


public DateTime (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return org.rascalmpl.interpreter.TypeEvaluator.__getTf().dateTimeType();
		
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getTypeArgument().getArity() == 0) {
			return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf().dateTimeType();
		}
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("datetime cannot have type arguments.", this);
	
}

}
static public class ReifiedReifiedType extends org.rascalmpl.ast.BasicType.ReifiedReifiedType {


public ReifiedReifiedType (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getValueArguments() == null) {
			throw new org.rascalmpl.interpreter.asserts.ImplementationError("missing value arguments to construct non-terminal type");
		}
		
		if (__eval.__getValueArguments().length == 1) {
			if (__eval.__getValueArguments()[0].getType() instanceof org.rascalmpl.interpreter.types.ReifiedType) {
				return org.rascalmpl.interpreter.types.RascalTypeFactory.getInstance().reifiedType(org.rascalmpl.interpreter.Typeifier.toType((org.eclipse.imp.pdb.facts.IConstructor) __eval.__getValueArguments()[0]));
			}
		}
		
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("a reified reified type should look like reified(type[&T] arg)", this);
	
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("a reified reified type should look like reified(type[&T] arg)", this);
		
}

}
static public class ReifiedAdt extends org.rascalmpl.ast.BasicType.ReifiedAdt {


public ReifiedAdt (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("a reified adt should be one of adt(str name), adt(str name, list[type[&T]] parameters), adt(str name, list[Constructor] constructors).", this);
		
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		java.lang.String name;
		
		if (__eval.__getValueArguments() == null) {
			throw new org.rascalmpl.interpreter.asserts.ImplementationError("missing value arguments to construct adt type");
		}
		
		if (__eval.__getValueArguments().length >= 1) {
			if (__eval.__getValueArguments()[0].getType().isStringType()) {
				name = ((org.eclipse.imp.pdb.facts.IString) __eval.__getValueArguments()[0]).getValue();
				org.eclipse.imp.pdb.facts.type.Type adt = __eval.__getEnv().lookupAbstractDataType(name);
				
				if (adt == null) {
					// TODO __eval should be a dynamic error, not a static one
					throw new org.rascalmpl.interpreter.staticErrors.UndeclaredTypeError(name, this);
				}
			}
			else {
				throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("a reified adt should have a name as first argument, like adt(str name)", this);
			}
			
			if (__eval.__getValueArguments().length == 1) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf().abstractDataType(__eval.__getEnv().getStore(), name);
			}
			
			if (__eval.__getValueArguments().length == 2) {
				if (__eval.__getValueArguments()[1].getType().isListType()) {
					org.eclipse.imp.pdb.facts.IList list = (org.eclipse.imp.pdb.facts.IList) __eval.__getValueArguments()[1];
					org.eclipse.imp.pdb.facts.type.Type[] args = new org.eclipse.imp.pdb.facts.type.Type[list.length()];
					int i = 0;
					for (org.eclipse.imp.pdb.facts.IValue arg : list) {
						org.eclipse.imp.pdb.facts.type.Type argType = arg.getType();

						if (argType instanceof org.rascalmpl.interpreter.types.ReifiedType) {
							args[i++] = argType.getTypeParameters().getFieldType(0);
						}
						else {
							throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("type parameters of an adt should be reified types, as in adt(str name, list[type[value]] parameters", this);
						}
					}
					
					return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf().abstractDataType(__eval.__getEnv().getStore(), name, args);
				}
			}
		}
		
		
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("a reified adt should be one of adt(str name), adt(str name, list[type[value]] parameters).", this);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Lex extends org.rascalmpl.ast.BasicType.Lex {


public Lex (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("lex should have one type argument, like lex[Id].", this);
		
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.NotYetImplemented(this);
	
}

}
static public class Real extends org.rascalmpl.ast.BasicType.Real {


public Real (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return org.rascalmpl.interpreter.TypeEvaluator.__getTf().realType();
		
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getTypeArgument().getArity() == 0) {
			return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf().realType();
		}
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("real cannot have type arguments.", this);
	
}

}
static public class List extends org.rascalmpl.ast.BasicType.List {


public List (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("list should have one type argument, like list[value].", this);
		
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getTypeArgument().getArity() == 1) {
			return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf().listType(__eval.__getTypeArgument().getFieldType(0));
		}
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("list should have exactly one type argument, like list[value]", this);
	
}

}
static public class Map extends org.rascalmpl.ast.BasicType.Map {


public Map (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getTypeArgument().getArity() == 2) {
			return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf().mapTypeFromTuple(__eval.__getTypeArgument());
		}
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("map should have exactly two type arguments, like map[value,value]", this);
	
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("map should have at two type arguments, like map[value,value].", this);
		
}

}
static public class Relation extends org.rascalmpl.ast.BasicType.Relation {


public Relation (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("rel should have at least one type argument, like rel[value,value].", this);
		
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf().relTypeFromTuple(__eval.__getTypeArgument());
	
}

}
static public class Node extends org.rascalmpl.ast.BasicType.Node {


public Node (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return org.rascalmpl.interpreter.TypeEvaluator.__getTf().nodeType();
		
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getTypeArgument().getArity() == 0) {
			return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf().nodeType();
		}
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("node cannot have type arguments.", this);
	
}

}
static public class ReifiedConstructor extends org.rascalmpl.ast.BasicType.ReifiedConstructor {


public ReifiedConstructor (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.ImplementationError("Did not expect to handle constructors in type evaluator");
	
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("a reified constructor declaration should look like contructor(str name, type[&T1] arg1, ...)", this);
		
}

}
static public class Set extends org.rascalmpl.ast.BasicType.Set {


public Set (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("set should have one type argument, like set[value].", this);
		
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getTypeArgument().getArity() == 1) {
			return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf().setType(__eval.__getTypeArgument().getFieldType(0));
		}
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("set should have exactly one type argument, like set[value]", this);
	
}

}
static public class Loc extends org.rascalmpl.ast.BasicType.Loc {


public Loc (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return org.rascalmpl.interpreter.TypeEvaluator.__getTf().sourceLocationType();
		
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getTypeArgument().getArity() == 0) {
			return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf().sourceLocationType();
		}
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("loc cannot have type arguments.", this);
	
}

}
static public class Num extends org.rascalmpl.ast.BasicType.Num {


public Num (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return org.rascalmpl.interpreter.TypeEvaluator.__getTf().numberType();
		
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getTypeArgument().getArity() == 0) {
			return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf().numberType();
		}
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("num cannot have type arguments.", this);
	
}

}
static public class Bool extends org.rascalmpl.ast.BasicType.Bool {


public Bool (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return org.rascalmpl.interpreter.TypeEvaluator.__getTf().boolType();
		
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getTypeArgument().getArity() == 0) {
			return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf().boolType();
		}
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("bool cannot have type arguments.", this);
	
}

}
static public class Tuple extends org.rascalmpl.ast.BasicType.Tuple {


public Tuple (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("tuple should have type arguments, like tuple[value,value].", this);
		
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		return __eval.__getTypeArgument();
	
}

}
static public class Bag extends org.rascalmpl.ast.BasicType.Bag {


public Bag (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("bag should have one type argument, like bag[value].", this);
		
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.NotYetImplemented(this);
	
}

}
static public class Int extends org.rascalmpl.ast.BasicType.Int {


public Int (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		if (__eval.__getTypeArgument().getArity() == 0) {
			return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf().integerType();
		}
		throw new org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError("int cannot have type arguments.", this);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return org.rascalmpl.interpreter.TypeEvaluator.__getTf().integerType();
		
}

}
static public class Ambiguity extends org.rascalmpl.ast.BasicType.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.BasicType> __param2) {
	super(__param1,__param2);
}
@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.ImplementationError("Detected ambiguity in BasicType", this.getLocation());
	
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			throw new org.rascalmpl.interpreter.asserts.ImplementationError("Ambiguity detected in BasicType", this.getLocation());
		
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}