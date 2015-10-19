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
package org.rascalmpl.library.lang.aut;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

public class IO{
	private static final TypeFactory types = TypeFactory.getInstance();
	
	private final IValueFactory values;
	
	public IO(IValueFactory values){
		super();
		
		this.values = values;
	}
	
	/*
	 * Read relations from an AUT file. An AUT file contains ternary relations
	 * in the following format: "(" <int> "," <string> ","<int>")". readAUT
	 * takes an AUT file nameAUTFile and generates a rel[int, str, int]
	 */
	public IValue readAUT(IString nameAUTFile){
		Type strType = types.stringType();
		Type intType = types.integerType();
		Type tupleType = types.tupleType(intType, strType, intType);
		java.lang.String fileName = nameAUTFile.getValue();
		ISetWriter rw = values.relationWriter(tupleType);
		BufferedReader bufRead = null;
		try{
			FileReader input = new FileReader(fileName);
			bufRead = new BufferedReader(input);
			java.lang.String line = bufRead.readLine();
			line = bufRead.readLine();
			while(line != null){
				java.lang.String[] fields = line.split("\\\"");
				java.lang.String[] field0 = fields[0].split("[\\(\\s,]");
				java.lang.String[] field2 = fields[2].split("[\\)\\s,]");
				rw.insert(values.tuple(values.integer(field0[1]), values.string(fields[1]), values
						.integer(field2[1])));
				line = bufRead.readLine();
			}
		}catch(IOException ioex){
			throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
		}finally{
			if(bufRead != null){
				try{
					bufRead.close();
				}catch(IOException ioex){/* Ignore. */
				}
			}
		}
		return rw.done();
	}
	
	private int numberOfStates(ISet st){
		st.size();
		int r = 0;
		for(IValue v : st){
			ITuple t = (ITuple) v;
			IInteger from = (IInteger) t.get(0);
			IInteger to = (IInteger) t.get(2);
			if(from.intValue() > r) r = from.intValue();
			if(to.intValue() > r) r = to.intValue();
		}
		return r + 1;
	}
	
	private void printTransitions(PrintStream fos, ISet st){
		fos.println("des(0," + st.size() + "," + numberOfStates(st) + ")");
		for(IValue v : st){
			ITuple t = (ITuple) v;
			IInteger from = (IInteger) t.get(0);
			IString act = (IString) t.get(1);
			IInteger to = (IInteger) t.get(2);
			fos.print('(');
			fos.print(from.intValue());
			fos.print(',');
			fos.print("\"" + act.getValue() + "\"");
			fos.print(',');
			fos.print(to.intValue());
			fos.print(')');
			fos.println();
		}
	}
	
	public void writeAUT(IString nameAUTFile, ISet value){
		java.lang.String fileName = nameAUTFile.getValue();
		
		PrintStream fos = null;
		try{
			File file = new File(fileName);
			fos = new PrintStream(file);
			printTransitions(fos, value);
		}catch(IOException ioex){
			throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
		}finally{
			if(fos != null){
				fos.close();
			}
		}
	}
}
