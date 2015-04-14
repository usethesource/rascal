package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.io.SerializableValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.values.ValueFactoryFactory;

public class SerializeExperiment implements Serializable {

	private static final long serialVersionUID = 3406889681206696083L;

	transient static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	transient static TypeFactory tf = TypeFactory.getInstance();
	//transient RascalExecutionContext rex;
	//transient static TypeStore store;
	Types types = new Types(vf);
	//TypeReifier tr = new TypeReifier(vf);
	private static TypeSerializer typeserializer;

	int n = 123;
	IString s = vf.string("abc");
	Type t = tf.listType(tf.stringType());
	//|project://rascal/src/org/rascalmpl/library/experiments/Compiler/Benchmarks/BFib.rsc|(100,3,<3,52>,<3,55>)
	ISourceLocation sloc;
	
	SerializeExperiment(RascalExecutionContext rex) {
		typeserializer = new TypeSerializer(rex.getTypeStore());
		try {
			sloc = vf.sourceLocation(vf.sourceLocation("project", "rascal", "/src/org/rascalmpl/library/experiments/Compiler/Benchmarks/BFib.rsc"),
					100, 3, 3, 52, 3, 55);
		} catch (URISyntaxException e) {
			System.out.println("Cannot create sloc");
		}
		System.out.println("sloc = " + sloc);
	}

	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream stream)
			throws IOException, ClassNotFoundException {
		n = (Integer) stream.readObject();
		s = ((SerializableRascalValue<IString>) stream.readObject()).getValue();
		
		t = typeserializer.readType(stream);
		sloc = ((SerializableRascalValue<ISourceLocation>) stream.readObject()).getValue();
	}

	private void writeObject(java.io.ObjectOutputStream stream)
			throws IOException {
		stream.writeObject(n);
		stream.writeObject(new SerializableRascalValue<IString>(s));
		typeserializer.writeType(stream, t);
		stream.writeObject(new SerializableRascalValue<ISourceLocation>(sloc));
	}

	public void write() {
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream("/tmp/serialize.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			fileOut.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static SerializeExperiment read(TypeStore ts) {
		SerializeExperiment ser = null;
		vf = ValueFactoryFactory.getValueFactory();
		typeserializer = new TypeSerializer(ts);
		try {
			FileInputStream fileIn = new FileInputStream("/tmp/serialize.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			ser = (SerializeExperiment) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();

		} catch (ClassNotFoundException c) {
			System.out.println("SerializeExperiment class not found: " + c);
			c.printStackTrace();
		}
		return ser;
	}

	public void experiment(RascalExecutionContext rex) {
		
		SerializeExperiment se1 = new SerializeExperiment(rex);
		se1.write();
		SerializeExperiment se2 = read(rex.getTypeStore());
		System.out.println("Results: " 
							+ (se1.n == se2.n) + "; "
							+ se1.s.equals(se2.s) + "; "
							+ se2.t.equals(se2.t) + "; "
							+ se2.sloc.equals(se2.sloc)
							);
		System.out.println("Returned: " + se2.n + "; " + se2.s + "; " + se2.t + "; " + se2.sloc);
	}

}
