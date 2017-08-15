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
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;

public class JarConverter extends M3Converter {
    
    private final static String CLASS_SCHEME = "java+class";
    private final static String COMP_UNIT_SCHEME = "java+compilationUnit";
    private final static String ENUM_SCHEME = "java+enum";
    private final static String INTERFACE_SCHEME = "java+interface";
    private final static String PACKAGE_SCHEME = "java+package";
    
    private IMap modifiersMap;
    private ISet packages;
    private ISet compilationUnits;

    public JarConverter(LimitedTypeStore typeStore, Map<String, ISourceLocation> cache) {
        super(typeStore, cache);
    }
    
    @SuppressWarnings("unchecked")
    public void convert(ISourceLocation jarLoc) {
        this.loc = jarLoc;
        initializeModifiers();
        
        try {
            ISetWriter packagesWriter = values.setWriter();
            ISetWriter compUnitsWriter = values.setWriter();
            
            resolvePackages(loc, packagesWriter, compUnitsWriter);
            this.packages = packagesWriter.done();
            this.compilationUnits = compUnitsWriter.done();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void initializeModifiers() {
        IMapWriter writer = values.mapWriter();
        writer.put(values.integer(Opcodes.ACC_ABSTRACT), constructModifierNode("abstract"));
        writer.put(values.integer(Opcodes.ACC_FINAL), constructModifierNode("final"));
        writer.put(values.integer(Opcodes.ACC_NATIVE), constructModifierNode("native"));
        writer.put(values.integer(Opcodes.ACC_PRIVATE), constructModifierNode("private"));
        writer.put(values.integer(Opcodes.ACC_PROTECTED), constructModifierNode("protected"));
        writer.put(values.integer(Opcodes.ACC_PUBLIC), constructModifierNode("public"));
        writer.put(values.integer(Opcodes.ACC_STATIC), constructModifierNode("static"));
        writer.put(values.integer(Opcodes.ACC_STRICT), constructModifierNode("strictfp"));
        writer.put(values.integer(Opcodes.ACC_SYNCHRONIZED), constructModifierNode("synchronized"));
        writer.put(values.integer(Opcodes.ACC_TRANSIENT), constructModifierNode("transient"));
        writer.put(values.integer(Opcodes.ACC_VOLATILE), constructModifierNode("volatile"));
        modifiersMap = writer.done();
    }
    
    private void resolvePackages(ISourceLocation uri, ISetWriter packagesWriter, ISetWriter classesWriter) 
        throws IOException, URISyntaxException {
        URIResolverRegistry resgistry = URIResolverRegistry.getInstance();
        String[] content = resgistry.listEntries(uri);
            
        for(String path : content) {
            ISourceLocation local = URIUtil.changePath(uri, uri.getPath() + "/" + path);
            
            if(resgistry.isFile(local) && local.getPath().endsWith(".class")) {
                packagesWriter.insert(uri);
                classesWriter.insert(local);
                
                setCompilationUnitRelations(local);
                setPackagesRelations(local);
                setClassRelations(local);
            }
            else if(resgistry.isDirectory(local)) {
                resolvePackages(local, packagesWriter, classesWriter);
            }
        }
    }
    
    //uri - compUnitUri
    private void setCompilationUnitRelations(ISourceLocation compUnitPhysical) throws URISyntaxException {
        String compUnit = compUnitPhysical.getPath().substring(compUnitPhysical.getPath().lastIndexOf("!") + 1);
        IString compUnitName = values.string(compUnit.substring(compUnit.lastIndexOf("/") + 1).replace(".class", ""));
       
        ISourceLocation packageLogical = values.sourceLocation(PACKAGE_SCHEME, "", compUnit.substring(0, compUnit.lastIndexOf("/")));
        ISourceLocation compUnitLogical = values.sourceLocation(COMP_UNIT_SCHEME, "", compUnit);
        
        insert(containment, packageLogical, compUnitLogical);
        //TODO: same offset of the contained class?
        insert(declarations, compUnitLogical, compUnitPhysical);
        insert(uses, compUnitPhysical, compUnitLogical);
        //TODO: M3 from directory does not add it to the names relation. Check.
        //insert(names, compUnitName, compUnitLogical); 
    }
    
    private void setPackagesRelations(ISourceLocation compUnitPhysical) throws URISyntaxException {
        String compUnit = compUnitPhysical.getPath().substring(compUnitPhysical.getPath().lastIndexOf("!") + 1);
        String currentPackage = compUnit.substring(0,compUnit.lastIndexOf("/"));
        int packages = currentPackage.length() - currentPackage.replace("/", "").length();
        
        for(int i = 1; i < packages; i++) {
            String parentPackage = currentPackage.substring(0, currentPackage.lastIndexOf("/"));
            
            IString currentName = values.string(currentPackage.substring(currentPackage.lastIndexOf("/") + 1));
            ISourceLocation parentPkgLogical = values.sourceLocation(PACKAGE_SCHEME, "", parentPackage);
            ISourceLocation parentPkgPhysical = URIUtil.changePath(loc, loc.getPath() + parentPackage);
            ISourceLocation currentPkgLogical = values.sourceLocation(PACKAGE_SCHEME, "", currentPackage);
            ISourceLocation currentPkgPhysical = URIUtil.changePath(loc, loc.getPath() + currentPackage);
            
            insert(containment, parentPkgLogical, currentPkgLogical);
            insert(declarations, currentPkgLogical, currentPkgPhysical);
            insert(uses, currentPkgPhysical, currentPkgLogical);
            insert(names, currentName, currentPkgLogical);
            
            if(i == packages - 1) {
                IString parentName = values.string(parentPackage.substring(parentPackage.lastIndexOf("/") + 1));
                insert(declarations, parentPkgLogical, parentPkgPhysical);
                insert(uses, parentPkgPhysical, parentPkgLogical);
                insert(names, parentName, parentPkgLogical);
            }
            
            currentPackage = parentPackage;
        }
    }
    
    private void setClassRelations(ISourceLocation compUnitPhysical) throws IOException, URISyntaxException {
        ClassReader cr = new ClassReader(URIResolverRegistry.getInstance().getInputStream(compUnitPhysical));
        ClassNode cn = new ClassNode();
        cr.accept(cn, ClassReader.SKIP_DEBUG);
        
        String compUnit = compUnitPhysical.getPath().substring(compUnitPhysical.getPath().lastIndexOf("!") + 1);
        String classPath = cn.name.replace("$", "/");
       
        IString className = values.string(classPath.substring(classPath.lastIndexOf("/") + 1));
        ISourceLocation compUnitLogical = values.sourceLocation(COMP_UNIT_SCHEME, "", compUnit);
        ISourceLocation classLogical = values.sourceLocation(getClassScheme(cn.access), "", classPath);
        //TODO: check the offset and length info. 
        ISourceLocation classPhysical = values.sourceLocation(compUnitPhysical, cr.header, cr.b.length);

        insert(containment, compUnitLogical, classLogical);
        insert(declarations, classLogical, classPhysical);
        insert(uses, classPhysical, classLogical);
        insert(names, className, classLogical);
        
        setInnerClassRelations(cn, classLogical);
        setClassExtendsRelation(cn, classLogical);
        setClassImplementsRelation(cn, classLogical);
        setClassModifiers(cn, classLogical);
    }

    private void setInnerClassRelations(ClassNode cn, ISourceLocation classLogical) throws URISyntaxException {
        for(int i = 0; i < cn.innerClasses.size(); i++) {
            InnerClassNode icn = (InnerClassNode) cn.innerClasses.get(i);
            String innerClassPath = icn.name.replace("$", "/");
            
            if(!innerClassPath.equals(cn.name.replace("$", "/"))) {
                ISourceLocation innerClassLogical = values.sourceLocation(getClassScheme(icn.access), "", innerClassPath);
                insert(containment, classLogical, innerClassLogical);
            }
        }
    }
    
    private void setClassExtendsRelation(ClassNode cn, ISourceLocation classLogical) throws URISyntaxException {
        if(cn.superName != null && !(cn.superName.equalsIgnoreCase(Object.class.getName().replace(".", "/")) ||
            cn.superName.equalsIgnoreCase(Enum.class.getName().replace(".", "/")))) {
            //TODO: check class scheme (interfaces)
            ISourceLocation extendsLogical = values.sourceLocation(classLogical.getScheme(), "", cn.superName.replace("$", "/"));
            insert(extendsRelations, classLogical, extendsLogical);
        }
    }
    
    private void setClassImplementsRelation(ClassNode cn, ISourceLocation classLogical) throws URISyntaxException {
        for(int i = 0; i < cn.interfaces.size(); i++) {
            ISourceLocation implementsLogical = values.sourceLocation(INTERFACE_SCHEME, "", 
                ((String) cn.interfaces.get(i)).replace("$", "/"));
            insert(implementsRelations, classLogical, implementsLogical);
        }
    }
    
    private void setClassModifiers(ClassNode cn, ISourceLocation classLogical) {
        for(int i = 0; i < 15; i++) {
            // Identify modifiers by filtering the access flags
            int shift = 0x1 << i;
            IConstructor modifier = (IConstructor) modifiersMap.get(values.integer(shift));
            if((cn.access & shift) != 0 && modifier != null && shift != Opcodes.ACC_SYNCHRONIZED) {
                insert(modifiers, classLogical, modifier);
            }
        }
    }
    
    private String getClassScheme(int access) {
        return ((access & Opcodes.ACC_INTERFACE) != 0) ? INTERFACE_SCHEME : 
            ((access & Opcodes.ACC_ENUM) != 0) ? ENUM_SCHEME : CLASS_SCHEME;
    }
}
