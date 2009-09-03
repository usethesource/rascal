package org.meta_environment.rascal.interpreter;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.asserts.NotYetImplemented;
import org.meta_environment.rascal.interpreter.types.FunctionType;
import org.meta_environment.rascal.interpreter.types.ReifiedType;

/**
 * This class helps transforming reified types back to types and to extract type
 * declarations from reified types.
 * 
 * See also {@link TypeReifier}.
 */
public class Typeifier {

	/**
	 * Retrieve the type that is reified by the given value
	 * 
	 * @param typeValue a reified type value produced by {@link TypeReifier}.
	 * @return the plain Type that typeValue represented
	 */
	public Type toType(IConstructor typeValue) {
		Type anonymous = typeValue.getType();

		if (anonymous instanceof ReifiedType) {
			ReifiedType reified = (ReifiedType) anonymous;

			return reified.getTypeParameters().getFieldType(0);
		}
		// TODO: add functionality to reconstruct a reified type from a node or
		// a adt that
		// follows the signature of reified types?
		else {
			throw new ImplementationError("Not a reified type: " + typeValue);
		}
	}

	/**
	 * Locate all declared types in a reified type value, such as abstract data types
	 * constructors and aliases and stores them in the given TypeStore.
	 * 
	 * @param typeValue a reified type which is produced by {@link TypeReifier}
	 * @param store     a TypeStore to collect declarations in
	 * @return the plain Type that typeValue represented
	 */
	public Type declare(IConstructor typeValue, final TypeStore store) {
		final List<IConstructor> todo = new LinkedList<IConstructor>();
		todo.add(typeValue);

		while (!todo.isEmpty()) {
			final IConstructor next = todo.get(0); todo.remove(0);
			Type type = toType(next);
			
			// We dispatch on the real type which is isomorphic to the typeValue.
			type.accept(new ITypeVisitor<Type>() {
				private final TypeFactory tf = TypeFactory.getInstance();

				public Type visitAbstractData(Type type) {
					store.declareAbstractDataType(type);
					
					switch (next.arity()) {
					case 1: // stub
						break;
					case 2: // either parameterized stub, or unparameterized adt with constructors
						if (next.has("parameters")) {
							declareParameters(next);
						}
						if (next.has("constructors")) {
							declareConstructors(type, next);
						}
						break;
					case 3: // parameterized adt with constructors
						declareParameters(next);
						declareConstructors(type, next);
					default:
						throw new ImplementationError("Unexpected reified type representation: " + next);
					}
					
					return type;
				}

				public Type visitAlias(Type type) {
					String name = getName(next);
					IConstructor aliased = getAliased(next);
					todo.add(aliased);
					// TODO deal with parameterized
					return tf.aliasType(store, name, toType(aliased));
				}

				public Type visitBool(Type boolType) {
					return boolType;
				}

				public Type visitConstructor(Type type) {
					throw new ImplementationError("should not have to typeify this: " + type);
				}

				public Type visitExternal(Type externalType) {
					throw new ImplementationError("should not have to typeify this: " + externalType);
				}

				public Type visitInteger(Type type) {
					return type;
				}

				public Type visitList(Type type) {
					todo.add(getElement(next));
					return type;
				}

				public Type visitMap(Type type) {
					todo.add(getKey(next));
					todo.add(getValue(next));
					return type;
				}

				public Type visitNode(Type type) {
					return type;
				}

				public Type visitParameter(Type parameterType) {
					throw new ImplementationError("reified parameter types are not supported");
				}

				public Type visitReal(Type type) {
					return type;
				}

				public Type visitRelationType(Type type) {
					for (IValue child : next) {
						todo.add((IConstructor) child);
					}
					return type;
				}

				public Type visitSet(Type type) {
					todo.add(getElement(next));
					return type;
				}

				public Type visitSourceLocation(Type type) {
					return type;
				}

				public Type visitString(Type type) {
					return type;
				}

				public Type visitTuple(Type type) {
					for (IValue child : next) {
						todo.add((IConstructor) child);
					}
					return type;
				}

				public Type visitValue(Type type) {
					return type;
				}

				public Type visitVoid(Type type) {
					return type;
				}

				private void declareParameters(IConstructor next) {
					for (IValue p : ((IList) next.get("parameters"))) {
						todo.add((IConstructor) p);
					}
				}

				// TODO deal with labels
				private void declareConstructors(Type adt, IConstructor next) {
					IList constructors = getConstructors(next);
					
					for (IValue c : constructors) {
						IConstructor cons = (IConstructor) c;
						String name = getName(cons);
						Type[] args = new Type[cons.arity() - 1];
						
						for (int i = 1; i < cons.arity(); i++) {
							IConstructor arg = (IConstructor) cons.get(i);
							todo.add(arg);
							args[i - 1] = toType(arg);
						}
						
						tf.constructor(store, adt, name, tf.tupleType(args));
					}
				}

				private IConstructor getElement(IConstructor next) {
					return (IConstructor) next.get("element");
				}

				private String getName(final IConstructor next) {
					return ((IString) next.get("name")).getValue();
				}

				private IConstructor getValue(IConstructor next) {
					return (IConstructor) next.get("key");
				}

				private IConstructor getAliased(IConstructor next) {
					return (IConstructor) next.get("aliased");
				}

				private IConstructor getKey(IConstructor next) {
					return (IConstructor) next.get("value");
				}

				private IList getConstructors(IConstructor next) {
					return (IList) next.get("constructors");
				}
			});
		}
		
		return toType(typeValue);
	}
}
