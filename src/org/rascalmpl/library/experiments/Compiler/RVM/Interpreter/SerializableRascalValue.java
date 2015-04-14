package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.impl.AbstractValueFactoryAdapter;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.values.LocalSharingValueFactory;
import org.rascalmpl.values.uptr.Factory;

/**
 * Experimental wrapper class for serializable IValues.
 * Your mileage may vary due to use of the BinaryWriter.
 */
public class SerializableRascalValue<T extends IValue> implements Serializable {
	private static final long serialVersionUID = -5507315290306212326L;
	private static IValueFactory vf;
	private static TypeReifier tr;
	private T value;
	private static TypeStore store;
	
	public static void initSerialization(IValueFactory vfactory, TypeStore ts){
		vf = vfactory;
		store = new TypeStore();
		store.extendStore(ts);
		store.extendStore(Factory.getStore());
		tr = new TypeReifier(vf);
	}
	
	public SerializableRascalValue(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
	public void write(OutputStream out) throws IOException {
		new ObjectOutputStream(out).writeObject(this);
	}
	
	@SuppressWarnings("unchecked")
	public static <U extends IValue> SerializableRascalValue<U> read(InputStream in) throws IOException {
		try {
			return (SerializableRascalValue<U>) new ObjectInputStream(in).readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} 
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		String factoryName = vf.getClass().getName();
		out.write("factory".getBytes());
		out.write(':');
		out.writeInt(factoryName.length());
		out.write(':');
		out.write(factoryName.getBytes("UTF8"));
		out.write(':');
		
		// Use text version (generates an extra string!):
		StringWriter sw = new StringWriter();
		new StandardTextWriter().write(value, sw);
		out.writeObject(sw.toString());
	}

	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		try {
			in.read(new byte["factory".length()], 0, "factory".length());
			in.read(); // ':'
			int length = in.readInt();
			in.read(); // ':'
			byte[] factoryName = new byte[length];
			in.read(factoryName, 0, length);
			in.read(); // ':'
			Class<?> clazz = getClass().getClassLoader().loadClass(new String(factoryName, "UTF8"));
			this.vf = (IValueFactory) clazz.getMethod("getInstance").invoke(null, new Object[0]);
			
			// Use text version  (generates an extra string!):
			String s = (String) in.readObject();
			try (StringReader sr = new StringReader(s)) {
				this.value = (T) new StandardTextReader().read(new RascalValuesValueFactory(), store, TypeFactory.getInstance().valueType(), sr);
			} 
			catch (FactTypeUseException e) {
				throw new IOException(e.getMessage());
			} 
			catch (IOException e) {
				throw new IOException(e.getMessage());
			}
		}
		catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | ClassCastException e) {
			throw new IOException("Could not load IValueFactory", e);
		}
	}
	
	private class RascalValuesValueFactory extends AbstractValueFactoryAdapter {
		public RascalValuesValueFactory() {
			super(vf);
		}

		@Override
		public IConstructor constructor(Type constructor, IValue[] children,
				Map<String, IValue> keyArgValues) {
			IConstructor res = specializeType(constructor.getName(), children, keyArgValues);
			return res != null ? res: super.constructor(constructor, children, keyArgValues);
		}

		@Override
		public INode node(String name, IValue[] children,
				Map<String, IValue> keyArgValues) throws FactTypeUseException {
			IConstructor res = specializeType(name, children, keyArgValues);
			return res != null ? res: vf.node(name, children, keyArgValues);
		}

		public IConstructor specializeType(String name, IValue[] children,
				Map<String, IValue> keyArgValues) {
			if(name.equals("type") 
					&& children.length == 2
					&& children[0].getType().isSubtypeOf(Factory.Type_Reified.getFieldType(0))
					&& children[1].getType().isSubtypeOf(Factory.Type_Reified.getFieldType(1))) {

				java.util.Map<Type,Type> bindings = new HashMap<Type,Type>();
				bindings.put(Factory.TypeParam, tr.symbolToType((IConstructor) children[0], (IMap) children[1]));

				return vf.constructor(Factory.Type_Reified.instantiate(bindings), children[0], children[1]);
			}
			return null;
		}
	}
}
