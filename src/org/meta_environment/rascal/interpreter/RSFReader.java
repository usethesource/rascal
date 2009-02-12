package org.meta_environment.rascal.interpreter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.exceptions.RascalRunTimeError;

public class RSFReader {
	
	public static void main(String[] args){
		IValue res = readRSF("/ufs/paulk/workspace/rascal/src/benchmark/RSF/JHotDraw52.rsf");
		System.err.println(res);
	}

	public static IValue readRSF(String fileName) {
		HashMap<String, IRelationWriter> table = new HashMap<String, IRelationWriter>();
		ValueFactory vf = ValueFactory.getInstance();
		TypeFactory tf = TypeFactory.getInstance();
		Type strType = tf.stringType();
		Type tupleType = tf.tupleType(strType, strType);

		try {
			FileReader input = new FileReader(fileName);
			BufferedReader bufRead = new BufferedReader(input);
			String line = bufRead.readLine();

			while (line != null) {
				String[] fields = line.split("\t");
				String name = fields[0];
				System.err.println(fields[0] + "|" + fields[1] + "|" + fields[2]);
				if (!table.containsKey(name)) {
					table.put(name, vf.relationWriter(tupleType));
				}
				IRelationWriter rw = table.get(name);
				rw.insert(vf.tuple(vf.string(fields[1]), vf.string(fields[2])));
				line = bufRead.readLine();
			}
			bufRead.close();

		} catch (IOException e) {
			throw new RascalRunTimeError("Can not find file " + fileName);
		}

		IMapWriter mw = vf.mapWriter(strType, tf.relType(strType, strType));

		for (String key : table.keySet()) {
			mw.insert(vf.string(key), table.get(key).done());
		}
		return mw.done();
	}
}
