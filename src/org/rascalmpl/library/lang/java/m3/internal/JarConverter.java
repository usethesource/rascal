/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Ashim Shahi -  - CWI
 *   * Ferry Rietveld - - UvA 
 *   * Chiel Peters - - UvA
 *   * Omar Pakker - - UvA
 *   * Maria Gouseti - - UvA
 *   
 * This code was developed in the Software Evolution course of the Software Engineering master.
 * 
 *******************************************************************************/
package org.rascalmpl.library.lang.java.m3.internal;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jdt.core.Signature;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.jar.JarURIResolver;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;

public class JarConverter extends M3Converter {
    
    private final int CLASSE = 0;
    private final int METHODE = 1;
    private final int FIELDE = 2;
    
    private ISet packages;
    private ISet compilationUnits;
    private ClassReader cr;
    private ClassNode cn;
    private String qualifiedName;
    private String scheme;
    
    
    private ISourceLocation jarLocOld;
    private String jarFileOld;
    private String classFileOld;
    private String logPathOld;
    private String classSchemeOld;
    private String classNameOld;
    private String packageNameOld;
    private boolean classIsEnumOld;
    

    public JarConverter(LimitedTypeStore typeStore, Map<String, ISourceLocation> cache) {
        super(typeStore, cache);
    }
    
    @SuppressWarnings("unchecked")
    public void convert(ISourceLocation jarLoc) {
        this.loc = jarLoc;
        try {
            ISetWriter packagesWriter = values.setWriter();
            ISetWriter compUnitsWriter = values.setWriter();
            
            resolvePackages(loc, packagesWriter, compUnitsWriter);
            this.packages = packagesWriter.done();
            this.compilationUnits = compUnitsWriter.done();
            
            setPackageRelations();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void resolvePackages(ISourceLocation uri, ISetWriter packagesWriter, ISetWriter classesWriter) 
        throws IOException, URISyntaxException {
        JarURIResolver resolver = new JarURIResolver(URIResolverRegistry.getInstance());
        String[] content = resolver.list(uri);
            
        for(String path : content) {
            ISourceLocation local = URIUtil.changePath(uri, uri.getPath() + "/" + path);
            
            if(resolver.isFile(local) && local.getPath().endsWith(".class")) {
                packagesWriter.insert(uri);
                classesWriter.insert(local);
                setCompUnitContainment(local);
            }
            else if(resolver.isDirectory(local)) {
                resolvePackages(local, packagesWriter, classesWriter);
            }
        }
    }
    
    private void setCompUnitContainment(ISourceLocation uri) throws URISyntaxException {
        String qualifiedName = uri.getPath().substring(uri.getPath().lastIndexOf("!") + 1).replace(".class", ".java");
        String currentPackage = qualifiedName.substring(0,qualifiedName.lastIndexOf("/"));
        int packages = currentPackage.length() - currentPackage.replace("/", "").length();
        
        this.insert(this.containment, values.sourceLocation("java+package", "", qualifiedName.substring(0, qualifiedName.lastIndexOf("/"))), 
            values.sourceLocation("java+compilationUnit", "", qualifiedName));
        this.insert(this.declarations, values.sourceLocation("java+compilationUnit", "", qualifiedName),
            URIUtil.changePath(loc, loc.getPath() + qualifiedName));
        
        for(int i = 1; i < packages; i++) {
            String parentPackage = currentPackage.substring(0, currentPackage.lastIndexOf("/"));
            
            this.insert(this.containment, values.sourceLocation("java+package", "", parentPackage),
                values.sourceLocation("java+package", "", currentPackage));
            this.insert(this.declarations, values.sourceLocation("java+package", "", currentPackage),
                URIUtil.changePath(loc, loc.getPath() + currentPackage));
            
            if(i == packages - 1) {
                this.insert(this.declarations, values.sourceLocation("java+package", "", parentPackage),
                    URIUtil.changePath(loc, loc.getPath() + parentPackage));
            }
            
            currentPackage = parentPackage;
        }
    }
    
    private void setPackageRelations() throws URISyntaxException {
        Iterator<IValue> iterator = packages.iterator();
        
        while(iterator.hasNext()) {
            ISourceLocation local = (ISourceLocation) iterator.next();
        }
    }
}
