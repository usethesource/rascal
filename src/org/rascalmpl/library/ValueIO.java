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
import org.rascalmpl.interpreter.Typeifier;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class ValueIO{
	private final IValueFactory values;
	
	public ValueIO(IValueFactory values){
		super();
		
		this.values = values;
	}
	
	public IValue readBinaryValueFile(IConstructor type, ISourceLocation loc, IEvaluatorContext ctx){
		Type start = ((ReifiedType) type.getType()).getTypeParameters().getFieldType(0);
		TypeStore store = new TypeStore();
		Typeifier.declare(type, store);
		
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
	
	public IValue readTextValueFile(IConstructor type, ISourceLocation loc, IEvaluatorContext ctx){
		Type start = ((ReifiedType) type.getType()).getTypeParameters().getFieldType(0);
		TypeStore store = new TypeStore();
		Typeifier.declare(type, store);
		
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
	
	public IValue readTextValueString(IConstructor type, IString input) {
		Type start = ((ReifiedType) type.getType()).getTypeParameters().getFieldType(0);
		TypeStore store = new TypeStore();
		Typeifier.declare(type, store);
		
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
