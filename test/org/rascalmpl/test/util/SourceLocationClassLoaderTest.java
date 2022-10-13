package org.rascalmpl.test.util;

import java.io.IOException;
import java.net.URL;
import java.net.URISyntaxException;

import org.junit.Test;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.SourceLocationClassLoader;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

public class SourceLocationClassLoaderTest {
    
    @Test
    public void testSystemClassLoading() {
        IValueFactory vf = IRascalValueFactory.getInstance();
        IList loaders = vf.list(URIUtil.correctLocation("system", "",""));

      
        try {
            SourceLocationClassLoader cl = new SourceLocationClassLoader(loaders, System.class.getClassLoader());
    
            assertTrue(cl.loadClass("java.lang.Object") != null);
        }
        catch (ClassNotFoundException | NoSuchMethodError e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testComplexClassLoading() {
        try {
            IValueFactory vf = IRascalValueFactory.getInstance();
            URL url = IValueFactory.class.getProtectionDomain().getCodeSource().getLocation();
            ISourceLocation vallang = URIUtil.createFromURI(url.toURI().toString());
            ISourceLocation rascal = URIUtil.correctLocation("lib", "rascal","");
            IList loaders = vf.list(vallang, rascal);
          
            System.err.println(URIResolverRegistry.getInstance().logicalToPhysical((ISourceLocation) loaders.get(0)));
            System.err.println(URIResolverRegistry.getInstance().logicalToPhysical((ISourceLocation) loaders.get(1)));
            SourceLocationClassLoader cl = new SourceLocationClassLoader(loaders, System.class.getClassLoader());
    
            assertTrue(cl.loadClass("org.rascalmpl.values.IRascalValueFactory") != null);
        }
        catch (URISyntaxException | IOException | ClassNotFoundException | NoSuchMethodError e) {
            fail(e.getMessage());
        }
    }
}
