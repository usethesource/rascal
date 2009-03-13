package org.meta_environment.rascal.interpreter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;


public class RSFReader {
	
	/*
	 * Read relations from an RSF file. An RSF file contains tuples of binary relations
	 * in the following format:
	 * 		RelationName Arg1 Arg2
	 * where each field is separated by a tabulation character (\t). One file may contain tuples
	 * for more than one relation.
	 * 
	 * readRSF takes an RSF file nameRSFFile and generates a map[str,rel[str,str]] that maps
	 * each relation name to the actual relation.
	 */

	public static IMap readRSF(String nameRSFFile) {
		HashMap<String, IRelationWriter> table = new HashMap<String, IRelationWriter>();
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		TypeFactory tf = TypeFactory.getInstance();
		Type strType = tf.stringType();
		Type tupleType = tf.tupleType(strType, strType);

		try {
			FileReader input = new FileReader(nameRSFFile);
			BufferedReader bufRead = new BufferedReader(input);
			String line = bufRead.readLine();

			while (line != null) {
				String[] fields = line.split("\t");
				String name = fields[0];
				//System.err.println(fields[0] + "|" + fields[1] + "|" + fields[2]);
				if (!table.containsKey(name)) {
					table.put(name, vf.relationWriter(tupleType));
				}
				IRelationWriter rw = table.get(name);
				rw.insert(vf.tuple(vf.string(fields[1]), vf.string(fields[2])));
				line = bufRead.readLine();
			}
			bufRead.close();

		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(ValueFactoryFactory.getValueFactory().string(e.getMessage()));
		}

		IMapWriter mw = vf.mapWriter(strType, tf.relType(strType, strType));

		for (String key : table.keySet()) {
			mw.insert(vf.tuple(vf.string(key), table.get(key).done()));
		}
		return mw.done();
	}
}
