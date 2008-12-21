package org.meta_environment.rascal.locations;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.ISourceRange;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

public class Factory {
	private static TypeFactory tf = TypeFactory.getInstance();

	public static final Type Location = tf.namedTreeType("Location");
	public static final Type Area = tf.namedTreeType("Area");

	public static final Type Location_File = tf.treeNodeType(Location, "file", tf.stringType(), "filename");
	public static final Type Location_Area = tf.treeNodeType(Location, "area", Area, "area");
	public static final Type Location_AreaInFile = tf.treeNodeType(Location, "area-in-file", tf.stringType(), "filename", Area, "area");

	public static final Type Area_Area = tf.treeNodeType(Area, "area", tf.integerType(), "begin-line", tf.integerType(), "begin-column", tf.integerType(), "end-line", tf.integerType(), "end-column", tf.integerType(), "offset", tf.integerType(), "length");
	
	private static final class InstanceHolder {
		public final static Factory factory = new Factory();
	}
	  
	public static Factory getInstance() {
		return InstanceHolder.factory;
	}
	
	private Factory() {}
	
	public ISourceLocation toSourceLocation(IValueFactory factory, INode loc) {
		Type type = loc.getType();
		
		if (type == Location_File) {
		  String filename = ((IString) loc.get("filename")).getValue();
		  ISourceRange range = factory.sourceRange(0, 0, 0, 0, 0, 0);
		  return factory.sourceLocation(filename, range);
		}
		else if (type == Location_Area) {
		   String filename = "/";
		   ISourceRange range = toSourceRange(factory, (INode) loc.get("area"));
		   return factory.sourceLocation(filename, range);
		}
		else if (type == Location_AreaInFile) {
			String filename = ((IString) loc.get("filename")).getValue();
			ISourceRange range = toSourceRange(factory, (INode) loc.get("area"));
			return factory.sourceLocation(filename, range);
		}
		
		throw new FactTypeError("This is not a Location: " + loc);
	}
	
	public ISourceRange toSourceRange(IValueFactory factory, INode area) {
		if (area.getType() == Area_Area) {
		   int offset = ((IInteger) area.get("offset")).getValue();
		   int startLine = ((IInteger) area.get("begin-line")).getValue();
		   int endLine = ((IInteger) area.get("end-line")).getValue();
		   int startCol = ((IInteger) area.get("begin-columm")).getValue();
		   int endCol = ((IInteger) area.get("end-column")).getValue();
		   int length = ((IInteger) area.get("length")).getValue();
		   
		   return factory.sourceRange(offset, length, startLine, endLine, startCol, endCol);
		}
		
		throw new FactTypeError("This is not an Area: " + area);
	}
	
}
