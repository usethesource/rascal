package org.meta_environment.rascal.std;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.io.ATermReader;
import org.eclipse.imp.pdb.facts.io.ATermWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.Typeifier;
import org.meta_environment.rascal.interpreter.types.ReifiedType;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.uri.URIResolverRegistry;

public class ATermIO {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	
	public static IValue readTextATermFile(IConstructor type, ISourceLocation loc){
		Type start = ((ReifiedType) type.getType()).getTypeParameters().getFieldType(0);
		TypeStore store = new TypeStore();
		Typeifier.declare(type, store);
		
		InputStream in = null;
		try{
			in = URIResolverRegistry.getInstance().getInputStream(loc.getURI());
			return new ATermReader().read(values, store, start, in);
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
	
	public static void writeTextATermFile(ISourceLocation loc, IValue value){
		OutputStream out = null;
		try{
			out = URIResolverRegistry.getInstance().getOutputStream(loc.getURI());
			new ATermWriter().write(value, out);
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
