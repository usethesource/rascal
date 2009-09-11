package org.meta_environment.locations;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class URIResolverRegistry implements IURIResolver {
	private static Map<String,IURIResolver> resolvers = new HashMap<String, IURIResolver>();
	
	private static class InstanceKeeper {
		public final static URIResolverRegistry sInstance = new URIResolverRegistry();
	}
	
	private URIResolverRegistry() { }
	
	public static URIResolverRegistry getInstance() {
		return InstanceKeeper.sInstance;
	}
	
	public void register(String scheme, IURIResolver resolver) {
		resolvers.put(scheme, resolver);
	}
	
	public IURIResolver getResolver(String scheme) {
		return resolvers.get(scheme);
	}
	
	public InputStream resolve(URI uri) throws IOException {
		IURIResolver resolver = resolvers.get(uri.getScheme());
		
		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}
		
		return resolver.resolve(uri);
	}
}
