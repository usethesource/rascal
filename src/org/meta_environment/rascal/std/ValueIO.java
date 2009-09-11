package org.meta_environment.rascal.std;

import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.io.PBFReader;
import org.eclipse.imp.pdb.facts.io.PBFWriter;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.locations.URIResolverRegistry;
import org.meta_environment.rascal.interpreter.Typeifier;
import org.meta_environment.rascal.interpreter.types.ReifiedType;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class ValueIO {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	
	public static IValue readBinaryValueFile(IConstructor type, ISourceLocation loc)	{
		Type start = ((ReifiedType) type.getType()).getTypeParameters().getFieldType(0);
		TypeStore store = new TypeStore();
		new Typeifier().declare((IConstructor) type, store);
		
		try {
			return new PBFReader().read(values, store, start, URIResolverRegistry.getInstance().getInputStream(loc.getURI()));
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		} catch (Exception e){
			e.printStackTrace();
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	
	public static IValue readTextValueFile(IConstructor type, ISourceLocation loc) {
		Type start = ((ReifiedType) type.getType()).getTypeParameters().getFieldType(0);
		TypeStore store = new TypeStore();
		new Typeifier().declare((IConstructor) type, store);
		
		try {
			return new StandardTextReader().read(values, store, start, URIResolverRegistry.getInstance().getInputStream(loc.getURI()));
		} 
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	
	public static void writeBinaryValueFile(ISourceLocation loc, IValue value) {
		try {
			new PBFWriter().write(value, URIResolverRegistry.getInstance().getOutputStream(loc.getURI()));
		} 
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	
	public static void writeTextValueFile(ISourceLocation loc, IValue value) {
		try {
			new StandardTextWriter().write(value, URIResolverRegistry.getInstance().getOutputStream(loc.getURI()));
		} 
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
}
