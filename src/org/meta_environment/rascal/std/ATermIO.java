package org.meta_environment.rascal.std;

import java.io.IOException;

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
	
	public static IValue readTextATermFile(IConstructor type, ISourceLocation loc) {
		Type start = ((ReifiedType) type.getType()).getTypeParameters().getFieldType(0);
		TypeStore store = new TypeStore();
		new Typeifier().declare((IConstructor) type, store);

		try {
			return new ATermReader().read(values, store, start, URIResolverRegistry.getInstance().getInputStream(loc.getURI()));
		} 
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	
	public static void writeTextATermFile(ISourceLocation loc, IValue value) {
		try {
			new ATermWriter().write(value, URIResolverRegistry.getInstance().getOutputStream(loc.getURI()));
		}
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
}
