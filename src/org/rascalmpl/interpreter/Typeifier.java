package org.rascalmpl.interpreter;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.types.ReifiedType;

/**
 * This class helps transforming reified types back to types and to extract type
 * declarations from reified types.
 * 
 * See also {@link TypeReifier}.
 */
public class Typeifier {
	
	private Typeifier(){
		super();
	}

	/**
	 * Retrieve the type that is reified by the given value
	 * 
	 * @param typeValue a reified type value produced by {@link TypeReifier}.
	 * @return the plain Type that typeValue represented
	 */
	public static Type toType(IConstructor typeValue) {
		Type anonymous = typeValue.getType();

		if (anonymous instanceof ReifiedType) {
			ReifiedType reified = (ReifiedType) anonymous;

			return reified.getTypeParameters().getFieldType(0);
		}
		
		throw new UnsupportedOperationException("Not a reified type: " + typeValue.getType());
	}

	/**
	 * Locate all declared types in a reified type value, such as abstract data types
	 * constructors and aliases and stores them in the given TypeStore.
	 * 
	 * @param typeValue a reified type which is produced by {@link TypeReifier}
	 * @param store     a TypeStore to collect declarations in
	 * @return the plain Type that typeValue represented
	 */
	public static Type declare(IConstructor typeValue, final TypeStore store) {
		final List<IConstructor> todo = new LinkedList<IConstructor>();
		todo.add(typeValue);

		while (!todo.isEmpty()) {
			final IConstructor next = todo.get(0); todo.remove(0);
			Type type = toType(next);
			
			// We dispatch on the real type which is isomorphic to the typeValue.
			type.accept(new ITypeVisitor<Type>() {
				private final TypeFactory tf = TypeFactory.getInstance();

				public Type visitAbstractData(Type type) {
					declareADT(next);
					declareADTParameters(next);
					declareConstructors(type, next);
					return type;
				}

				public Type visitAlias(Type type) {
					IConstructor aliased = getAliased(next);
					todo.add(aliased);
					// TODO: type parameterized aliases are broken still
					declareAliasParameters(aliased);
					return type;
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
				
				public Type visitNumber(Type type) {
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
					return parameterType;
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
					for (IValue child : (IList) next.get(0)) {
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

				public Type visitDateTime(Type type) {
					return type;
				}
				
				private void declareADT(IConstructor next) {
					IString name = (IString) next.get("name");
					IList bindings = (IList) next.get("bindings");
					Type[] parameters = new Type[bindings.length()];
					int i = 0;
					
					for (IValue elem : bindings) {
						ITuple tuple = (ITuple) elem;
						parameters[i++] = toType((IConstructor) tuple.get(0));
					}
					tf.abstractDataType(store, name.getValue(), parameters);
				}
				
				private void declareADTParameters(IConstructor next) {
					IList bindings = (IList) next.get("bindings");
					for (IValue elem : bindings) {
						ITuple tuple = (ITuple) elem;
						declare((IConstructor) tuple.get(1), store);
					}
				}
				private void declareAliasParameters(IConstructor next) {
					if (next.has("parameters")) {
						for (IValue p : ((IList) next.get("parameters"))) {
							todo.add((IConstructor) p);
						}
					}
				}

				private void declareConstructors(Type adt, IConstructor next) {
					IList constructors = getConstructors(next);
					
					for (IValue c : constructors) {
						IConstructor cons = (IConstructor) c;
						IList fields = (IList) cons.get(1);
						String name = getName(cons);
						Object[] args = new Object[fields.length() * 2];

						int i = 0;
						for (IValue field : fields) {
							ITuple tuple = (ITuple) field;
							IConstructor fieldType = (IConstructor) tuple.get(0);
							todo.add(fieldType);
							args[i++] = toType(fieldType);
							args[i++] = ((IString) tuple.get(1)).getValue();
						}
						
						tf.constructor(store, adt, name, args);
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
