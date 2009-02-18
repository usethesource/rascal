package org.meta_environment.rascal.std;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.RSFReader;

public class RSF {
	

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
		return RSFReader.readRSF(nameRSFFile.getValue());
	}
}
