package org.rascalmpl.values;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;

public interface IRascalValueFactory extends IValueFactory {
	
	IString string(ISourceLocation loc, String str);
	IString string(ISourceLocation loc, int c);
	IString string(ISourceLocation loc, int[] chars);

}
