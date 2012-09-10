/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.uptr.Factory;

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
		HashMap<java.lang.String, IRelationWriter> table = new HashMap<java.lang.String, IRelationWriter>();
	
		Type strType = types.stringType();
		Type tupleType = types.tupleType(strType, strType);

		InputStream input = null;
		try {
			input = ctx.getResolverRegistry().getInputStream(nameRSFFile.getURI());
			BufferedReader bufRead = new BufferedReader(new InputStreamReader(input));
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
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}

		IMapWriter mw = values.mapWriter(strType, types.relType(strType, strType));

		for (Map.Entry<java.lang.String, IRelationWriter> entry : table.entrySet()) {
			mw.insert(values.tuple(values.string(entry.getKey()), entry.getValue().done()));
		}
		return mw.done();
	}
	
	/*
	 * Convert a string element from an RSF tuple to a typed Rascal value
	 */
	
	private IValue getElementAsTypedValue(String elem, Type type){
		if(type.isStringType())
			return values.string(elem);
		if(type.isIntegerType())
			return values.integer(elem);
		if(type.isRealType())
			return values.real(elem);
		if(type.isBoolType())
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
		InputStream in = null;
		
		Type resultType = tr.valueToType((IConstructor) result, new TypeStore());
	
		while (resultType.isAliasType()) {
			resultType = resultType.getAliased();
		}
		
		if(!resultType.isRelationType() || (resultType.getArity() != 2)){
			throw RuntimeExceptionFactory.illegalArgument(
					values.string("Type of an RSF relation should be a binary relation"),
					ctx.getCurrentAST(), ctx.getStackTrace());
		}
		
		Type elem1Type = resultType.getFieldType(0);
		Type elem2Type = resultType.getFieldType(1);
		IRelationWriter rw = values.relationWriter(resultType.getElementType());
		String rname = relName.getValue();

		try {
			in = ctx.getResolverRegistry().getInputStream(loc.getURI());
			
			java.lang.String line = readLine(in);

			while (!line.isEmpty()) {
				java.lang.String[] fields = line.split("\\s+");
				java.lang.String name = fields[0];
				if(name.equals(rname)){
					//System.err.println(fields[0] + "|" + fields[1] + "|" + fields[2]);
					IValue v1 = getElementAsTypedValue(fields[1], elem1Type);
					IValue v2 = getElementAsTypedValue(fields[2], elem2Type);
					rw.insert(values.tuple(v1, v2));
				}
				line = readLine(in);
			}
			in.close();

		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
		finally {
			if (in != null){
				try {
					in.close();
				} catch (IOException e){
					throw RuntimeExceptionFactory.io(values.string(e.getMessage()), ctx.getCurrentAST(), ctx.getStackTrace());
				}
			}
		}
		
		return rw.done();
	}
	
	private boolean isEOL(int c) {
		return c == '\n' || c == '\r';
	}
	
	private String readLine(InputStream in) throws IOException{
		StringWriter sw = new StringWriter();
		int lastChar = in.read();
		while(lastChar != -1){
			if(isEOL(lastChar)){
				return sw.toString();
			}
			sw.append((char)lastChar);
			lastChar = in.read();
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
	
		Type strType = types.stringType();
		Type symbolType = Factory.Symbol;
		
		InputStream in = null;

		try {
			in = ctx.getResolverRegistry().getInputStream(loc.getURI());
		
			java.lang.String line = readLine(in);

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
							
				line = readLine(in);
			}
			in.close();

		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
		finally {
			if (in != null){
				try {
					in.close();
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
