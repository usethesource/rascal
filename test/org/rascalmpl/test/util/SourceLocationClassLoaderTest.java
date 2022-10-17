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
    private IValueFactory vf = IRascalValueFactory.getInstance();

    private IList getVallangRascalLoadersList() throws URISyntaxException {
        ISourceLocation vallang = vallangJarLocation();
        ISourceLocation rascal = rascalTargetClassesLocation();
        IList loaders = vf.list(vallang, rascal);
        return loaders;
    }

    private ISourceLocation rascalTargetClassesLocation() {
        return URIUtil.correctLocation("lib", "rascal","");
    }

    private ISourceLocation vallangJarLocation() throws URISyntaxException {
        URL url = IValueFactory.class.getProtectionDomain().getCodeSource().getLocation();
        ISourceLocation vallang = URIUtil.createFromURI(url.toURI().toString());
        return vallang;
    }

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
            IList loaders = getVallangRascalLoadersList();
          
            SourceLocationClassLoader cl = new SourceLocationClassLoader(loaders, System.class.getClassLoader());
    
            assertTrue(cl.loadClass("org.rascalmpl.values.IRascalValueFactory") != null);
        }
        catch (URISyntaxException | ClassNotFoundException | NoSuchMethodError e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testReversedComplexClassLoading() {
        try {
            IList loaders = getVallangRascalLoadersList().reverse();
          
            SourceLocationClassLoader cl = new SourceLocationClassLoader(loaders, System.class.getClassLoader());
    
            assertTrue(cl.loadClass("org.rascalmpl.values.IRascalValueFactory") != null);
        }
        catch (URISyntaxException | ClassNotFoundException | NoSuchMethodError e) {
            fail(e.getMessage());
        }
    }
}
