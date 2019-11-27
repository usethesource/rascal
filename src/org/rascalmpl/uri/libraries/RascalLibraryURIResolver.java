/** 
 * Copyright (c) 2019, Jurgen J. Vinju, Centrum Wiskunde & Informatica (NWOi - CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.uri.libraries;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.uri.ISourceLocationInput;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

/**
 * For every META-INF/RASCAL.MF which includes a Project-Name: &ltname&gt field that this class can find via ClassLoader.getResources (via its own Classloader) the 
 * resolver will offer a |lib://&ltname&gt| root location. The idea is that the root of the jar in which any Rascal library is deployed is accessible via the lib schema.
 * 
 * <p>Next to this, if a "plugin" scheme is registered with URIResolverRegistry, then and only if a library
 * was not registered before with the previous method, this resolver will rewrite |lib://...| to |plugin://...|. The latter method is to allow
 * for app containers like MVN:EXEC, OSGI and SPRING to provide their own implementation of the plugin scheme and make resources findable for the lib:// scheme. </p> 
 * So, if a Rascal library is deployed into such a generic app container context, which offers dependency injection, the lib schema will again resolve to the 
 * root of the jar in which the library is deployed. Of course the proper working of the lib scheme depends wholly on the proper implementation of the plugin scheme
 * for every different app container.
 * 
 * <p>CAVEAT: this resolver drops the query and offset/length components of the incoming source locations</p>
 */
public class RascalLibraryURIResolver implements ISourceLocationInput {
    private final Map<String, ISourceLocation> libraries = new HashMap<>();
    private final Set<String> nonExistentPluginsCache = new HashSet<>();
    private final URIResolverRegistry reg;
    private boolean pluginsExist;
    private boolean pluginExistChecked; 
    
    public RascalLibraryURIResolver(URIResolverRegistry reg) {
        this.reg = reg;
        
        try {
            IValueFactory vf = ValueFactoryFactory.getValueFactory();
            RascalManifest mf = new RascalManifest();
            Enumeration<URL> mfs = getClass().getClassLoader().getResources(RascalManifest.META_INF_RASCAL_MF);

            Collections.list(mfs).forEach(url -> {
                try {
                    String libName = mf.getProjectName(url.openStream());
                    
                    if (libName != null && !libName.isEmpty()) {
                        ISourceLocation loc;

                        if (url.getProtocol().equals("jar") && url.getPath().startsWith("file:/")) {
                            loc = vf.sourceLocation("jar+file", null, url.getPath().substring("file:".length()));
                        }
                        else {
                            loc = vf.sourceLocation(URIUtil.fromURL(url));
                        }

                        loc = URIUtil.changePath(loc, loc.getPath().replace(RascalManifest.META_INF_RASCAL_MF, ""));

                        registerLibrary(libName, loc);
                    }
                }
                catch (IOException | URISyntaxException e) {
                    System.err.println("WARNING: could not load Rascal manifest for library resolution of: " + url);
                    e.printStackTrace();
                }
            });
        }
        catch (IOException e) {
            System.err.println("WARNING: could not resolve any Rascal library locations");
            e.printStackTrace();
        }
    }

    private void registerLibrary(String libName, ISourceLocation loc) {
        System.err.println("INFO: registered |lib://" + libName + "| at " + loc);
        libraries.put(libName, loc);
    }
    
    private ISourceLocation resolve(ISourceLocation uri) {
        assert uri.getScheme().equals(scheme());
        
        String libName = uri.getAuthority();
        
        if (libName == null || libName.isEmpty()) {
            return null;
        }
        
        ISourceLocation root = libraries.get(libName);
        
        if (root != null) {
            return URIUtil.getChildLocation(root, uri.getPath());    
        }
        else {
            return deferToPluginScheme(uri);
        }
    }
    
    private ISourceLocation deferToPluginScheme(ISourceLocation uri) {
        String libName = uri.getAuthority();
        
        if (pluginsExist() && !nonExistentPluginsCache.contains(libName)) {
            ISourceLocation deferred = URIUtil.correctLocation("plugin", libName, "");
            
            // store the resolved location for later quick-access, but only if a RASCAL.MF file can be found
            if (reg.exists(URIUtil.getChildLocation(deferred, RascalManifest.META_INF_RASCAL_MF))) {
                assert new RascalManifest().getProjectName(deferred).equals(libName) 
                     : "Project-Name in RASCAL.MF does not align with plugin name in the respective app container (OSGI?)";
                
                registerLibrary(libName, deferred);
                
                return URIUtil.getChildLocation(deferred, uri.getPath());
            }
            else {
                // don't want to check again and again that a plugin does not exist!
                nonExistentPluginsCache.add(libName);
                return null;
            }
        }
        else {
            // if either the plugin scheme does not exist, or this authority did not exist before with a RASCAL.MF file in it,
            // then we bail out quickly.
            return null;
        }
    }

    private boolean pluginsExist() {
        if (pluginExistChecked) {
            return pluginsExist;
        }
        else {
            // this locks the registry so we don't want to do this every time we try to address a lib:// location
            pluginsExist = reg.supportsInputScheme("plugin");
            pluginExistChecked = true;
            return pluginsExist;
        }
    }

    private ISourceLocation safeResolve(ISourceLocation uri) throws IOException {
        ISourceLocation resolved = resolve(uri);
        if (resolved == null) {
            throw new IOException("could not resolve " + uri);
        }
        return resolved;
    }
    
    @Override
    public InputStream getInputStream(ISourceLocation uri) throws IOException {
        return reg.getInputStream(safeResolve(uri));
    }

    

    @Override
    public Charset getCharset(ISourceLocation uri) throws IOException {
        return reg.getCharset(safeResolve(uri));
    }

    @Override
    public boolean exists(ISourceLocation uri) {
        ISourceLocation resolved = resolve(uri);
        if (resolved == null) {
            return false;
        }
        return reg.exists(resolved);
    }

    @Override
    public long lastModified(ISourceLocation uri) throws IOException {
        return reg.lastModified(safeResolve(uri));
    }

    @Override
    public boolean isDirectory(ISourceLocation uri) {
        try {
            return URIResolverRegistry.getInstance().isDirectory(safeResolve(uri));
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean isFile(ISourceLocation uri) {
        try {
            return URIResolverRegistry.getInstance().isFile(safeResolve(uri));
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String[] list(ISourceLocation uri) throws IOException {
        return reg.listEntries(safeResolve(uri));
    }

    @Override
    public String scheme() {
        return "lib";
    }

    @Override
    public boolean supportsHost() {
        return false;
    }
}
