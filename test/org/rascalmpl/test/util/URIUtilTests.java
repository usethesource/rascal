package org.rascalmpl.test.util;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

public class URIUtilTests {

    private static final IValueFactory VF = ValueFactoryFactory.getValueFactory();

    private static ISourceLocation toLoc(URL url) throws URISyntaxException {
        return VF.sourceLocation(URIUtil.fromURL(url));
    }

    @Test
    public void javaResourceUrl() throws MalformedURLException, URISyntaxException {
        var loc = toLoc(new URL("jar:file:/a/b.jar!/c"));
        assertEquals(VF.sourceLocation("jar+file", "", "/a/b.jar!/c"), loc);
    }

    @Test
    public void javaWindowsResourceUrl() throws MalformedURLException, URISyntaxException {
        var loc = toLoc(new URL("jar:file:/C:/a/b.jar!/c"));
        assertEquals(VF.sourceLocation("jar+file", "", "/C:/a/b.jar!/c"), loc);
    }

    @Test
    public void jarUrl() throws MalformedURLException, URISyntaxException {
        var loc = toLoc(new URL("file:/C:/a/b.jar"));
        assertEquals(VF.sourceLocation("file", "", "/C:/a/b.jar"), loc);
    }

}
