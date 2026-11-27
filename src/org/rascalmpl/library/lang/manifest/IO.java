package org.rascalmpl.library.lang.manifest;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

public class IO {
	private final IValueFactory vf;

	public IO(IValueFactory vf) {
		this.vf = vf;
	}
	
	public IMap readManifest(ISourceLocation input) {
		try (var contents = URIResolverRegistry.getInstance().getInputStream(input)) {
			Manifest mf = new Manifest(contents);
			IMapWriter m = vf.mapWriter();
			for (Entry<Object, Object> e : mf.getMainAttributes().entrySet()) {
				m.put(vf.string(((Name) e.getKey()).toString()), vf.string((String) e.getValue()));
			}
			
			return m.done();
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(e);
		}
	}
}
