package org.meta_environment.rascal.std;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.RuntimeExceptionFactory;

public class RSF {
	
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final TypeFactory types = TypeFactory.getInstance();
	
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

	public static IValue readRSF(IString nameRSFFile)
	//@doc{readRSF -- read an RSF file}
	{
		HashMap<java.lang.String, IRelationWriter> table = new HashMap<java.lang.String, IRelationWriter>();
	
		Type strType = types.stringType();
		Type tupleType = types.tupleType(strType, strType);
		java.lang.String fileName = nameRSFFile.getValue();

		try {
			FileReader input = new FileReader(fileName);
			BufferedReader bufRead = new BufferedReader(input);
			java.lang.String line = bufRead.readLine();

			while (line != null) {
				java.lang.String[] fields = line.split("\\s+");
				java.lang.String name = fields[0];
				//System.err.println(fields[0] + "|" + fields[1] + "|" + fields[2]);
				if (!table.containsKey(name)) {
					table.put(name, values.relationWriter(tupleType));
				}
				IRelationWriter rw = table.get(name);
				rw.insert(values.tuple(values.string(fields[1]), values.string(fields[2])));
				line = bufRead.readLine();
			}
			bufRead.close();

		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(ValueFactoryFactory.getValueFactory().string(e.getMessage()), null, null);
		}

		IMapWriter mw = values.mapWriter(strType, types.relType(strType, strType));

		for (java.lang.String key : table.keySet()) {
			mw.insert(values.tuple(values.string(key), table.get(key).done()));
		}
		return mw.done();
	}
	
	
}
