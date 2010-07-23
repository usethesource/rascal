package org.rascalmpl.library;

import java.io.File;
import java.net.URISyntaxException;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;

public class SystemAPI {

	private final IValueFactory values;

	public SystemAPI(IValueFactory values) {
		this.values = values;
	}

	public IValue getSystemProperty(IString v)
	{
		return values.string(java.lang.System.getProperty(v.getValue()));
	}
	
	public IValue getLibraryPath(IString g)
	{
		try {
			java.lang.String s = File.separator+ this.getClass().getCanonicalName().replaceAll("\\.", File.separator);
			s = s.substring(0, s.lastIndexOf(File.separatorChar));
			if (g!=null) s+=(File.separator+g.getValue());
			IValue v = values.sourceLocation(this.getClass().getResource(s).toURI());
			return v;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
