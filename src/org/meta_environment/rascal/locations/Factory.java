package org.meta_environment.rascal.locations;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.ISourceRange;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.UnexpectedConstructorTypeException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;

public class Factory {
	private static TypeFactory tf = TypeFactory.getInstance();
	private static TypeStore ts = new TypeStore();

	public static final Type Location = ts.abstractDataType("Location");
	public static final Type Area = ts.abstractDataType("Area");

	public static final Type Location_File = ts.constructor(Location, "file", tf.stringType(), "filename");
	public static final Type Location_Area = ts.constructor(Location, "area", Area, "area");
	public static final Type Location_AreaInFile = ts.constructor(Location, "area-in-file", tf.stringType(), "filename", Area, "area");

	public static final Type Area_Area = ts.constructor(Area, "area", tf.integerType(), "beginLine", tf.integerType(), "beginColumn", tf.integerType(), "endLine", tf.integerType(), "endColumn", tf.integerType(), "offset", tf.integerType(), "length");
	
	private static final class InstanceHolder {
		public final static Factory factory = new Factory();
	}
	  
	public static Factory getInstance() {
		return InstanceHolder.factory;
	}
	
	public static TypeStore getStore() {
		return ts;
	}
	
	private Factory() {}
	
	public ISourceLocation toSourceLocation(IValueFactory factory, IConstructor loc) {
		Type type = loc.getConstructorType();
		
		if (type == Location_File) {
		  String filename = ((IString) loc.get("filename")).getValue();
		  ISourceRange range = factory.sourceRange(0, 0, 0, 0, 0, 0);
		  return factory.sourceLocation(filename, range);
		}
		else if (type == Location_Area) {
		   String filename = "/";
		   ISourceRange range = toSourceRange(factory, (IConstructor) loc.get("area"));
		   return factory.sourceLocation(filename, range);
		}
		else if (type == Location_AreaInFile) {
			String filename = ((IString) loc.get("filename")).getValue();
			ISourceRange range = toSourceRange(factory, (IConstructor) loc.get("area"));
			return factory.sourceLocation(filename, range);
		}
		
		throw new UnexpectedConstructorTypeException(Location, type);
	}
	
	public ISourceRange toSourceRange(IValueFactory factory, IConstructor area) {
		if (area.getConstructorType() == Area_Area) {
		   int offset = ((IInteger) area.get("offset")).getValue();
		   int startLine = ((IInteger) area.get("beginLine")).getValue();
		   int endLine = ((IInteger) area.get("endLine")).getValue();
		   int startCol = ((IInteger) area.get("beginColumn")).getValue();
		   int endCol = ((IInteger) area.get("endColumn")).getValue();
		   int length = ((IInteger) area.get("length")).getValue();
		   
		   return factory.sourceRange(offset, length, startLine, endLine, startCol, endCol);
		}
		
		throw new UnexpectedConstructorTypeException(Area, area.getType());
	}
	
}
