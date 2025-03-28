package org.rascalmpl.util.maven;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.settings.Proxy;
import org.junit.Assert;
import org.junit.Test;

import io.usethesource.vallang.IValue;

public class MavenProxySelectorTest {
    @Test
    public void testFiltering() throws URISyntaxException {
        Proxy proxy = new Proxy();
        proxy.setNonProxyHosts("www.google.com|*.example.com");
        proxy.setActive(true);
        proxy.setProtocol("http");
        proxy.setHost("www.doesnotexist.com");
        MavenProxySelector selector = new MavenProxySelector(Arrays.asList(proxy), new ArrayList<>());
        List<java.net.Proxy> proxies = selector.select(new URI("https://www.google.com/some/artifact"));
        Assert.assertSame(java.net.Proxy.NO_PROXY, proxies.get(0));

        proxies = selector.select(new URI("https://www.example.com/some/artifact"));
        Assert.assertSame(java.net.Proxy.NO_PROXY, proxies.get(0));

        proxies = selector.select(new URI("https://www.valid.com/some/artifact"));
        Assert.assertEquals(java.net.Proxy.Type.HTTP, proxies.get(0).type());
    }

    @Test
    public void testWarningOnAuth() throws URISyntaxException {
        Proxy proxy = new Proxy();
        proxy.setNonProxyHosts("www.google.com|*.example.com");
        proxy.setActive(true);
        proxy.setProtocol("http");
        proxy.setHost("www.doesnotexist.com");
        proxy.setUsername("blaat");

        List<IValue> messages = new ArrayList<>();
        MavenProxySelector selector = new MavenProxySelector(Arrays.asList(proxy), messages);
        Assert.assertEquals(1, messages.size());
        String msg = messages.get(0).toString();
        Assert.assertTrue(msg.contains("that contains name/password authentication"));

        List<java.net.Proxy> proxies = selector.select(new URI("https://www.valid.com/some/artifact"));
        Assert.assertSame(java.net.Proxy.NO_PROXY, proxies.get(0));
    }

}
