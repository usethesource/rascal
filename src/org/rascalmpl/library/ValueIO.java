/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.PBFReader;
import org.eclipse.imp.pdb.facts.io.PBFWriter;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class ValueIO{
	private final IValueFactory values;
	private final TypeReifier tr;
	
	public ValueIO(IValueFactory values){
		super();
		
		this.values = values;
		this.tr = new TypeReifier(values);
	}
	
	public IValue readBinaryValueFile(IValue type, ISourceLocation loc, IEvaluatorContext ctx){
		TypeStore store = new TypeStore();
		Type start = tr.valueToType((IConstructor) type, store);
		
		InputStream in = null;
		try{
			in = ctx.getResolverRegistry().getInputStream(loc.getURI());
			return new PBFReader().read(values, store, start, in);
		}catch(IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}catch(Exception e){
			e.printStackTrace();
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}
	
	public IValue readTextValueFile(IValue type, ISourceLocation loc, IEvaluatorContext ctx){
		TypeStore store = new TypeStore();
		Type start = tr.valueToType((IConstructor) type, store);
		
		InputStream in = null;
		try{
			in = ctx.getResolverRegistry().getInputStream(loc.getURI());
			return new StandardTextReader().read(values, store, start, in);
		}catch(IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}
	
	public IValue readTextValueString(IValue type, IString input) {
		TypeStore store = new TypeStore();
		Type start = tr.valueToType((IConstructor) type, store);
		
		ByteArrayInputStream in = new ByteArrayInputStream(input.getValue().getBytes());
		try {
			return new StandardTextReader().read(values, store, start, in);
		} catch (FactTypeUseException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	
	public void writeBinaryValueFile(ISourceLocation loc, IValue value, IEvaluatorContext ctx){
		OutputStream out = null;
		try{
			out = ctx.getResolverRegistry().getOutputStream(loc.getURI(), false); 
			new PBFWriter().write(value, out);
		}catch (IOException ioex){
			throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
		}finally{
			if(out != null){
				try{
					out.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}
	
	public void writeTextValueFile(ISourceLocation loc, IValue value, IEvaluatorContext ctx){
		OutputStream out = null;
		try{
			out = ctx.getResolverRegistry().getOutputStream(loc.getURI(), false);
			new StandardTextWriter().write(value, out);
		}catch(IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}finally{
			if(out != null){
				try{
					out.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}
}
