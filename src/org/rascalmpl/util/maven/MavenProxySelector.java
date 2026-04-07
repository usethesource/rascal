/*
 * Copyright (c) 2025, Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.util.maven;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.apache.maven.settings.Proxy;
import org.rascalmpl.library.Messages;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.IValue;

class MavenProxySelector extends ProxySelector {
    private static class FilteredProxy {
        private final java.net.Proxy proxy;
        private final List<Predicate<String>> nonProxyHosts;

        public FilteredProxy(java.net.Proxy proxy, String nonProxyHostSpec) {
            this.proxy = proxy;
            nonProxyHosts = new ArrayList<>();

            for (String pattern : nonProxyHostSpec.split("\\|")) {
                String regex = pattern.replace("*", ".*");
                nonProxyHosts.add(Pattern.compile(regex).asMatchPredicate());
            }
        }

        public boolean isProxyFor(String host) {
            for (Predicate<String> isNonProxyHost : nonProxyHosts) {
                if (isNonProxyHost.test(host)) {
                    return false;
                }
            }

            return true;
        }

        java.net.Proxy getProxy() {
            return proxy;
        }
    }

    // Proxy 
    private final List<FilteredProxy> filteredProxies;

    public MavenProxySelector(List<Proxy> mavenProxies, List<IValue> messages) {
        filteredProxies = new ArrayList<>();
        for (Proxy mavenProxy : mavenProxies) {
            if (mavenProxy.isActive()) {
                if (mavenProxy.getUsername() != null || mavenProxy.getPassword() != null) {
                    messages.add(Messages.warning("Ignoring proxy '" + mavenProxy.getId() + "' in settings.xml that contains name/password authentication which we currently do not support.", URIUtil.unknownLocation()));
                    continue;
                }
                java.net.Proxy.Type type =
                    mavenProxy.getProtocol() == "socks5" ? java.net.Proxy.Type.SOCKS : java.net.Proxy.Type.HTTP;
                java.net.Proxy proxy =
                    new java.net.Proxy(type, new InetSocketAddress(mavenProxy.getHost(), mavenProxy.getPort()));
                filteredProxies.add(new FilteredProxy(proxy, mavenProxy.getNonProxyHosts()));
            }
        }
    }


    @Override
    public List<java.net.Proxy> select(URI uri) {
        List<java.net.Proxy> proxies = new ArrayList<>();

        for (FilteredProxy proxy : filteredProxies) {
            if (proxy.isProxyFor(uri.getHost())) {
                proxies.add(proxy.getProxy());
            }
        }

        if (proxies.isEmpty()) {
            proxies.add(java.net.Proxy.NO_PROXY);
        }

        return proxies;
    }

    @Override
    public void connectFailed(URI uri, java.net.SocketAddress sa, java.io.IOException ioe) {
        // Log or handle connection failure if needed
    }

}