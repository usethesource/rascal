/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.lang.rsf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;

public class RSFIO {
	private static final TypeFactory types = TypeFactory.getInstance();
	private final IValueFactory values;
	private TypeReifier tr;
	
	public RSFIO(IValueFactory values){
		super();
		
		this.values = values;
		this.tr = new TypeReifier(values);
	}
	
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

	public IValue readRSF(ISourceLocation nameRSFFile, IEvaluatorContext ctx)
	//@doc{readRSF -- read an RSF file}
	{
		HashMap<java.lang.String, ISetWriter> table = new HashMap<java.lang.String, ISetWriter>();
	
		Reader input = null;
		try {
			input = URIResolverRegistry.getInstance().getCharacterReader(nameRSFFile);
			BufferedReader bufRead = new BufferedReader(input);
			java.lang.String line = bufRead.readLine();

			while (line != null) {
				java.lang.String[] fields = line.split("\\s+");
				java.lang.String name = fields[0];
				//System.err.println(fields[0] + "|" + fields[1] + "|" + fields[2]);
				if (!table.containsKey(name)) {
					table.put(name, values.setWriter());
				}
				ISetWriter rw = table.get(name);
				rw.insert(values.tuple(values.string(fields[1]), values.string(fields[2])));
				line = bufRead.readLine();
			}
			bufRead.close();

		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
		finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
						throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
				}
			}
		}

		IMapWriter mw = values.mapWriter();

		for (Map.Entry<java.lang.String, ISetWriter> entry : table.entrySet()) {
			mw.insert(values.tuple(values.string(entry.getKey()), entry.getValue().done()));
		}
		return mw.done();
	}
	
	/*
	 * Convert a string element from an RSF tuple to a typed Rascal value
	 */
	
	private IValue getElementAsTypedValue(String elem, Type type){
		if(type.isString())
			return values.string(elem);
		if(type.isInteger())
			return values.integer(elem);
		if(type.isReal())
			return values.real(elem);
		if(type.isBool())
			return values.bool(elem.toLowerCase().equals("true"));
		return null;
	}
	
	/*
	 * Read a named RSF relation, given its
	 * - type
	 * - name
	 * - location
	 */
	
	public IValue readRSFRelation(IValue result, IString relName, ISourceLocation loc, IEvaluatorContext ctx){
		
		Type resultType = tr.valueToType((IConstructor) result, new TypeStore());
	
		while (resultType.isAliased()) {
			resultType = resultType.getAliased();
		}
		
		if(!resultType.isRelation() || (resultType.getArity() != 2)){
			throw RuntimeExceptionFactory.illegalArgument(
					values.string("Type of an RSF relation should be a binary relation"),
					ctx.getCurrentAST(), ctx.getStackTrace());
		}
		
		Type elem1Type = resultType.getFieldType(0);
		Type elem2Type = resultType.getFieldType(1);
		ISetWriter rw = values.setWriter();
		String rname = relName.getValue();

		try (Reader reader = URIResolverRegistry.getInstance().getCharacterReader(loc)) {
			java.lang.String line = readLine(reader);

			while (!line.isEmpty()) {
				java.lang.String[] fields = line.split("\\s+");
				java.lang.String name = fields[0];
				if(name.equals(rname)){
					//System.err.println(fields[0] + "|" + fields[1] + "|" + fields[2]);
					IValue v1 = getElementAsTypedValue(fields[1], elem1Type);
					IValue v2 = getElementAsTypedValue(fields[2], elem2Type);
					rw.insert(values.tuple(v1, v2));
				}
				line = readLine(reader);
			}
			reader.close();

		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
		
		return rw.done();
	}
	
	private boolean isEOL(int c) {
		return c == '\n' || c == '\r';
	}
	
	private String readLine(Reader reader) throws IOException{
		StringWriter sw = new StringWriter();
		int lastChar = reader.read();
		while(lastChar != -1){
			if(isEOL(lastChar)){
				return sw.toString();
			}
			sw.append((char)lastChar);
			lastChar = reader.read();
		}
		return sw.toString();
	}
	
	/*
	 * Extract all relations and their types from an RSF file.
	 * Returns a map[str Symbol]
	 */
	
	public IValue getRSFTypes(ISourceLocation loc, IEvaluatorContext ctx)
	{
		HashMap<java.lang.String, Type> table = new HashMap<java.lang.String, Type>();
	
//		Type strType = types.stringType();
//		Type symbolType = Factory.Symbol;
		
		Reader reader = null;
		try {
			reader = URIResolverRegistry.getInstance().getCharacterReader(loc);
		
			java.lang.String line = readLine(reader);

			while (!line.isEmpty()) {
				System.err.println(line);
				java.lang.String[] fields = line.split("\\s+");
				java.lang.String name = fields[0];
				System.err.println(fields[0] + "|" + fields[1] + "|" + fields[2]);
				
				Type t1 = getElementType(fields[1]);
				Type t2 = getElementType(fields[2]);
				Type t1t2Tuple = types.tupleType(t1, t2);
				if (!table.containsKey(name)) {
						table.put(name, t1t2Tuple);
				} else
					table.put(name, table.get(name).lub(t1t2Tuple));
							
				line = readLine(reader);
			}
			reader.close();

		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
		finally {
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e){
					throw RuntimeExceptionFactory.io(values.string(e.getMessage()), ctx.getCurrentAST(), ctx.getStackTrace());
				}
			}
		}
		
		IMapWriter mr = values.mapWriter();
		
		for (Map.Entry<java.lang.String, Type> entry : table.entrySet()) {
			Type t = entry.getValue();
			mr.put(values.string(entry.getKey()), 
			       ((IConstructor) new TypeReifier(values).typeToValue(types.relType(t.getFieldType(0), t.getFieldType(1)), ctx).getValue()));	
		}
		return mr.done();
	}
	
	private Type getElementType(String elem){

		if(elem.isEmpty()){
			return types.voidType();
		} else
			if(elem.matches("^[+-]?[0-9]+$")){
				return types.integerType();
			} else
				if(elem.matches("[+-]?[0-9]+\\.[0-9]*")){
					return types.realType();
				} else
					if(elem.equals("true") || elem.equals("false")){
						return types.boolType();
					} else {
						return types.stringType();
					}
	}
	
}
