package org.rascalmpl.parser;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

public class SerializableValue implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private IValue wrapped;

	public SerializableValue(IValue val) {
		this.wrapped = val;
	}

	public IValue get() {
		return wrapped;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		String s = wrapped.toString();
		out.writeInt(s.length());
		out.writeChar(':');
		out.writeChars(s);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		int len = in.readInt();
		char ch = in.readChar();
		assert ch == ':';

		StringBuffer b = new StringBuffer();

		for (int i = len; i > 0; i--) {
			b.append(in.readChar());
		}

		StandardTextReader reader = new StandardTextReader();
		this.wrapped = reader.read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Production, new StringReader(b.toString()));
	}

}
