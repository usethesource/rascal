package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.values.uptr.IRascalValueFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;

/**
 * Experimental wrapper class for serializable IValues.
 * Your mileage may vary due to use of the BinaryWriter.
 */
public class SerializableRascalValue<T extends IValue> implements Serializable {
	private static final long serialVersionUID = -5507315290306212326L;
	private T value;
	private static TypeStore store;
	
	public static void initSerialization(TypeStore ts){
		store = ts;
		store.extendStore(RascalValueFactory.getStore());
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
		// Use text version (generates an extra string!):
		StringWriter sw = new StringWriter();
		new StandardTextWriter().write(value, sw);
		out.writeObject(sw.toString());
	}

	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		// Use text version  (generates an extra string!):
		String s = (String) in.readObject();
		try (StringReader sr = new StringReader(s)) {
			this.value = (T) new StandardTextReader().read(IRascalValueFactory.getInstance(), store, TypeFactory.getInstance().valueType(), sr);
		} 
		catch (FactTypeUseException e) {
			throw new IOException(e.getMessage());
		} 
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}
}
