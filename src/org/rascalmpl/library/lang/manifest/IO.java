package org.rascalmpl.library.lang.manifest;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValueFactory;

public class IO {
	private final IValueFactory vf;

	public IO(IValueFactory vf) {
		this.vf = vf;
	}
	
	public IMap readManifest(ISourceLocation input) {
		try {
			Manifest mf = new Manifest(URIResolverRegistry.getInstance().getInputStream(input));
			IMapWriter m = vf.mapWriter();
			for (Entry<Object, Object> e : mf.getMainAttributes().entrySet()) {
				m.put(vf.string(((Name) e.getKey()).toString()), vf.string((String) e.getValue()));
			}
			
			return m.done();
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
		}
	}
}
