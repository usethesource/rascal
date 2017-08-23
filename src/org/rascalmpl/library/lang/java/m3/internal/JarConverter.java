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
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;

public class JarConverter extends M3Converter {
    
    //------------------------------------------------------------
    // Constants
    //------------------------------------------------------------
    
    /**
     * Constants with common sour location schemes.
     */
    private final static String CLASS_SCHEME = "java+class";
    private final static String COMP_UNIT_SCHEME = "java+compilationUnit";
    private final static String CONSTRUCTOR_SCHEME = "java+constructor";
    private final static String ENUM_SCHEME = "java+enum";
    private final static String ENUM_CONSTANT_SCHEME = "java+enumConstant";
    private final static String FIELD_SCHEME = "java+field";
    private final static String INTERFACE_SCHEME = "java+interface";
    private final static String METHOD_SCHEME = "java+method";
    private final static String PACKAGE_SCHEME = "java+package";
    private final static String PARAMETER_SCHEME = "java+parameter";
    private final static String PRIMITIVE_TYPE_SCHEME = "java+primitiveType";
    
    /**
     * Constant with the name of consructor methods in Java bytecode.
     */
    private final static String COMPILED_CONSTRUCTOR_NAME = "<init>";
    
    
    //------------------------------------------------------------
    // Fields
    //------------------------------------------------------------
    
    /**
     * Map relating modifiers opcodes with modifier nodes.
     */
    private IMap modifiersMap;
    
    /**
     * Map relating primitive type descriptors with their corresponding type names.
     */
    private IMap primitiveTypesMap;
    
    /**
     * Physical source location of the current compilation unit. A .class file
     * is considered as a computational unit.
     */
    private ISourceLocation compUnitPhysical;
    
    /**
     * URI resolver registry
     */
    private URIResolverRegistry resgistry;

    
    //------------------------------------------------------------
    // Methods
    //------------------------------------------------------------
    
    public JarConverter(LimitedTypeStore typeStore, Map<String, ISourceLocation> cache) {
        super(typeStore, cache);
    }
    
    /**
     * Orchestrates the generation of a M3 model from a Jar file. 
     * @param jarLoc - the source location is delivered with a 
     * jar scheme
     */
    @SuppressWarnings("unchecked")
    public void convert(ISourceLocation jarLoc) {
        loc = jarLoc;
        resgistry = URIResolverRegistry.getInstance();
        
        initializeModifiers();
        initializePrimitiveTypes();
        
        try {            
            createM3(loc);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Initialized the modifiers map by considering modifier opcodes
     * and their corresponding modifier nodes.
     */
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
    
    /**
     * Initializes the primitive types map by considering Java bytecode
     * descriptors and their corresponding type names.
     */
    private void initializePrimitiveTypes() {
        IMapWriter writer = values.mapWriter();
        writer.put(values.string(Type.BOOLEAN_TYPE.getDescriptor()), values.string(Type.BOOLEAN_TYPE.getClassName()));
        writer.put(values.string(Type.CHAR_TYPE.getDescriptor()), values.string(Type.CHAR_TYPE.getClassName()));
        writer.put(values.string(Type.DOUBLE_TYPE.getDescriptor()), values.string(Type.DOUBLE_TYPE.getClassName()));
        writer.put(values.string(Type.INT_TYPE.getDescriptor()), values.string(Type.INT_TYPE.getClassName()));
        writer.put(values.string(Type.LONG_TYPE.getDescriptor()), values.string(Type.LONG_TYPE.getClassName()));
        writer.put(values.string(Type.SHORT_TYPE.getDescriptor()), values.string(Type.SHORT_TYPE.getClassName()));
        primitiveTypesMap = writer.done();
    }
    
    /**
     * From a physical source location the method searches recursively for all existing 
     * .class files. Once a file is found the compilation unit is set and 
     * relations related to it, to related packages and classes are generated.
     */
    private void createM3(ISourceLocation uri) 
        throws IOException, URISyntaxException {
        String[] content = resgistry.listEntries(uri);
            
        for(String path : content) {
            ISourceLocation local = getPhysicalLoc(uri, path);
            
            if(resgistry.isFile(local) && local.getPath().endsWith(".class")) {     
                compUnitPhysical = local;
                String compUnit = getCompilationUnitRelativePath();
                
                setCompilationUnitRelations(compUnit);
                setPackagesRelations(compUnit);
                setClassRelations(compUnit);
            }
            else if(resgistry.isDirectory(local)) {
                createM3(local);
            }
        }
    }
    
    /**
     * Generates compilation unit relations given a compilation unit relative
     * path. Considered relations: containment, declarations, and names.
     */
    private void setCompilationUnitRelations(String compUnitRelative) throws URISyntaxException {
        IString compUnitName = values.string(compUnitRelative);
        ISourceLocation packageLogical = getPreviousPackageLogicalLoc(compUnitRelative);
        ISourceLocation compUnitLogical = values.sourceLocation(COMP_UNIT_SCHEME, "", compUnitRelative);
        
        addToContainment(packageLogical, compUnitLogical);
        //TODO: same offset of the contained class?
        addToDeclarations(compUnitLogical, compUnitPhysical);
        //TODO: M3 from directory does not add it to the names relation. Check.
        //addToNames(compUnitName, compUnitLogical);
    }
    
    /**
     * Generates package relations given a compilation unit relative path. 
     * Considered relations: containment, declarations, and names.
     */
    private void setPackagesRelations(String compUnitRelative) throws URISyntaxException {
        ISourceLocation currentPkgLogical = getPreviousPackageLogicalLoc(compUnitRelative);
        int packages = currentPkgLogical.getPath().length() - currentPkgLogical.getPath().replace("/", "").length();
        
        for(int i = 1; i < packages; i++) {
            IString currentName = getName(currentPkgLogical);
            ISourceLocation parentPkgLogical = getPreviousPackageLogicalLoc(currentPkgLogical.getPath());
            ISourceLocation parentPkgPhysical = getPhysicalLoc(loc, parentPkgLogical.getPath());
            ISourceLocation currentPkgPhysical = getPhysicalLoc(loc, currentPkgLogical.getPath());
            
            addToContainment(parentPkgLogical, currentPkgLogical);
            addToDeclarations(currentPkgLogical, currentPkgPhysical);
            addToNames(currentName, currentPkgLogical);
            
            if(i == packages - 1) {
                IString parentName = getName(parentPkgLogical);
                addToDeclarations(parentPkgLogical, parentPkgPhysical);
                addToNames(parentName, parentPkgLogical);
            }
            
            currentPkgLogical = parentPkgLogical;
        }
    }
    
    /**
     * Generates class relations given a compilation unit relative path. 
     * Then, inner class, field, and method relations are generated.
     * Considered relations: containment, declarations, names, extends,
     * implements, modifiers, and annotations.    
     */
    private void setClassRelations(String compUnitRelative) throws IOException, URISyntaxException {
        ClassReader cr = getClassReader(compUnitRelative);
        ClassNode cn = new ClassNode();
        cr.accept(cn, ClassReader.SKIP_DEBUG);
        
        IString className = getClassName(cn);
        ISourceLocation compUnitLogical = values.sourceLocation(COMP_UNIT_SCHEME, "", compUnitRelative);
        ISourceLocation classLogical = values.sourceLocation(getClassScheme(cn.access), "", getClassRelativePath(cn));
        //TODO: check the offset and length info. 
        ISourceLocation classPhysical = values.sourceLocation(compUnitPhysical, cr.header, cr.b.length);
        
        addToContainment(compUnitLogical, classLogical);
        addToDeclarations(classLogical, classPhysical);
        addToNames(className, classLogical);
        addToExtends(cn, classLogical);
        addToImplements(cn, classLogical);
        addToModifiers(cn.access, classLogical, true);
        addToAnnotations(composeAnnotations(cn.visibleAnnotations, cn.invisibleAnnotations), classLogical);
        
        setInnerClassRelations(cn, classLogical);
        setFieldRelations(cn, classLogical);
        setMethodRelations(cn, classLogical);
    }

    /**
     * Generates containment relations among the input class and
     * its inner classes.
     */
    private void setInnerClassRelations(ClassNode cn, ISourceLocation classLogical) throws URISyntaxException {
        if(cn.innerClasses != null) {
            for(int i = 0; i < cn.innerClasses.size(); i++) {
                InnerClassNode icn = (InnerClassNode) cn.innerClasses.get(i);
                String innerClassPath = icn.name.replace("$", "/");
                
                if(!innerClassPath.equals(cn.name.replace("$", "/"))) {
                    ISourceLocation innerClassLogical = values.sourceLocation(getClassScheme(icn.access), "", innerClassPath);
                    addToContainment(classLogical, innerClassLogical);
                }
            }
        }
    }
    
    /**
     * Generates field relations given an ASM class node and its logical 
     * source location. Considered relations: containment, declarations, 
     * names, modifiers, annotations, and typeDependency.
     */
    private void setFieldRelations(ClassNode cn, ISourceLocation classLogical) throws URISyntaxException {
        if(cn.fields != null) {
            for(int i = 0; i < cn.fields.size(); i++) {
                FieldNode fn = (FieldNode) cn.fields.get(i);
                
                if((fn.access & Opcodes.ACC_SYNTHETIC) == 0) {
                    IString fieldName = values.string(fn.name);
                    ISourceLocation fieldLogical = values.sourceLocation(getFieldScheme(fn), "", 
                        classLogical.getPath() + "/" + fn.name);
                    //TODO: check offset + length
                    ISourceLocation fieldPhysical = compUnitPhysical;
                    
                    addToContainment(classLogical, fieldLogical);
                    addToDeclarations(fieldLogical, fieldPhysical);
                    addToNames(fieldName, fieldLogical);
                    addToModifiers(fn.access, fieldLogical);
                    addToAnnotations(composeAnnotations(fn.visibleAnnotations, fn.invisibleAnnotations), fieldLogical);
                    addToTypeDependency(fieldLogical, fn.desc);
                }
            }
        }
    }
    
    /**
     * Generates method relations given an ASM class node and its logical 
     * source location. Then, parameter and variable relations are generated. 
     * Considered relations: containment, declarations, names, modifiers, 
     * annotations, typeDependency, and methodOverrides.
     */
    private void setMethodRelations(ClassNode cn, ISourceLocation classLogical) throws URISyntaxException, IOException {
        if(cn.methods != null) {
            for(int i = 0; i < cn.methods.size(); i++) {
                MethodNode mn = (MethodNode) cn.methods.get(i);
                
                // TODO: check
                if(!getClassScheme(cn.access).equals(ENUM_SCHEME) && !(mn.name.equalsIgnoreCase("values") || 
                    mn.name.equalsIgnoreCase("valueOf"))) {
                    String signature = getMethodSignature(mn, getName(classLogical).getValue());
                    
                    IString methodName = values.string(signature.substring(0,signature.indexOf("(")));
                    ISourceLocation methodLogical = values.sourceLocation(getMethodScheme(mn), "", 
                        classLogical.getPath() + "/" + signature);
                    //TODO: check offset + length
                    ISourceLocation methodPhysical = compUnitPhysical;
                    
                    addToContainment(classLogical, methodLogical);
                    addToDeclarations(methodLogical, methodPhysical);
                    addToNames(methodName, methodLogical);
                    addToModifiers(mn.access, methodLogical);
                    addToAnnotations(composeAnnotations(mn.visibleAnnotations, mn.invisibleAnnotations), methodLogical);
                    addToTypeDependency(methodLogical, Type.getType(mn.desc).getReturnType().getDescriptor());
                    addMethodOverrides(cn, mn, methodLogical);
                    
                    //TODO: we do not have access to parameters names - Check
                    setParameterRelations(mn, methodLogical);
                    //setVariableRelations(mn, methodLogical);
                }
            }
        }
    }

    /**
     * Generates method overrides relations among and input method
     * node and super class and interfaces methods.
     */
    private void addMethodOverrides(ClassNode cn, MethodNode mn, ISourceLocation methodLogical) throws IOException, URISyntaxException {
        if(cn.superName != null && !cn.superName.isEmpty()) {
            setMethodOverridesRelation(cn.superName, mn, methodLogical);
        }
        if(cn.interfaces != null) {
            for(int i = 0; i < cn.interfaces.size(); i++) {
                setMethodOverridesRelation((String) cn.interfaces.get(i), mn, methodLogical);
            }
        }
    }
    
    /**
     * Generates method overrides relations among and input method
     * node and super class and interfaces methods.
     */
    private void setMethodOverridesRelation(String classRelative, MethodNode mn, ISourceLocation methodLogical) throws IOException, URISyntaxException {
            ClassReader cr = getClassReader(classRelative);
            ClassNode cn = new ClassNode();
            cr.accept(cn, ClassReader.SKIP_DEBUG);
            
            if(cn.methods != null) {
                for(int i = 0; i < cn.methods.size(); i++) {
                    MethodNode mnSuper = (MethodNode) cn.methods.get(i);
                    if(mnSuper.name.equals(mn.name) && mnSuper.desc.equals(mn.desc)) {  
                        String signature = getMethodSignature(mnSuper, ((IString)getClassName(cn)).getValue());
                        ISourceLocation superLogical = values.sourceLocation(getClassScheme(cn.access), "", cn.name.replace("$", "/"));
                        ISourceLocation methodSuperLogical = values.sourceLocation(getMethodScheme(mnSuper), "", 
                            superLogical.getPath() + "/" + signature);
                        
                        insert(methodOverrides, methodLogical, methodSuperLogical);
                        addMethodOverrides(cn, mn, methodLogical);
                    }
                }
            }
    }
    
    /**
     * Generates paramater relations given an ASM method node and its logical 
     * source location. Considered relations: containment, declarations, 
     * names, and typeDependency.
     */
    private void setParameterRelations(MethodNode mn, ISourceLocation methodLogical) throws URISyntaxException {
        Type[] parameters = Type.getType(mn.desc).getArgumentTypes();
        
        for(int i = 0; i < parameters.length; i++) {
            IString parameterName = values.string("param" + i);
            ISourceLocation parameterLogical = values.sourceLocation(PARAMETER_SCHEME, "", 
                methodLogical.getPath() + "/" + parameterName.getValue());
            //TODO: offset + length
            ISourceLocation parameterPhysical = compUnitPhysical;
            
            addToContainment(methodLogical, parameterLogical);
            addToDeclarations(parameterLogical, parameterPhysical);
            addToNames(parameterName, parameterLogical);
            addToTypeDependency(parameterLogical, parameters[i].getDescriptor());
        }
    }
    
    private void setVariableRelations(MethodNode mn, ISourceLocation methodLogical) {
        if(mn.instructions != null) {
            ListIterator<AbstractInsnNode> iterator = mn.instructions.iterator();
            
            while(iterator.hasNext()) {
                AbstractInsnNode n = iterator.next();
            }
        }
    }
    
    /**
     * Adds a parent-child tuple to the containment relation.
     */
    private void addToContainment(ISourceLocation parent, ISourceLocation child) {
        insert(containment, parent, child);
    }
    
    /**
     * Adds a logical-physical location tuple to the declarations relation.
     */
    private void addToDeclarations(ISourceLocation logical, ISourceLocation physical) {
        insert(declarations, logical, physical);
    }
    
    /**
     * Adds a name-logical location tuple to the names relation.
     */
    private void addToNames(IString name, ISourceLocation logical) {
        insert(names, name, logical);
    }
    
    //TODO: SourceConverter does not consider abstract modifier. Check.
    //TODO: Use parseModifiers(int modifiers) -> JavaToRascalConverter?
    /**
     * Adds a logical-modifier node tuple to the modifiers relation.
     */
    private void addToModifiers(int access, ISourceLocation logical, boolean...isClass) {
        for(int i = 0; i < 15; i++) {
            // Identify modifiers by filtering the access flags
            int shift = 0x1 << i;
            IConstructor modifier = (IConstructor) modifiersMap.get(values.integer(shift));
            
            if((access & shift) != 0 && modifier != null && !(isClass.length >= 1 && shift == Opcodes.ACC_SYNCHRONIZED)) {
                insert(modifiers, logical, modifier);
            }
        }
    }
    
    /**
     * Adds a logical-annotation tuple to the annotations relation.
     */
    private void addToAnnotations(List<AnnotationNode> annotations, ISourceLocation classLogical) throws URISyntaxException {
        for(AnnotationNode node : annotations) {
            ISourceLocation annotationLogical = values.sourceLocation(INTERFACE_SCHEME, "", 
                Type.getType(node.desc).getInternalName());
            insert(this.annotations, classLogical, annotationLogical);
        }
    }
    
    /**
     * Adds a logical-type tuple to the type dependency relation.
     * "desc" refers to a type descriptor identified by ASM library.
     */
    private void addToTypeDependency(ISourceLocation logical, String desc) throws URISyntaxException {
        if(!desc.equals(Type.VOID_TYPE.getDescriptor())) {
            ISourceLocation classLogical = getLogicalLoc(desc);
            insert(typeDependency, logical, classLogical);
        }
    }
    
    /**
     * Adds a class-super class location tuple to the extends relation.
     */
    private void addToExtends(ClassNode cn, ISourceLocation classLogical) throws URISyntaxException {
        if(cn.superName != null && !(cn.superName.equalsIgnoreCase(Object.class.getName().replace(".", "/")) ||
            cn.superName.equalsIgnoreCase(Enum.class.getName().replace(".", "/")))) {
            //TODO: check class scheme (interfaces)
            ISourceLocation extendsLogical = values.sourceLocation(classLogical.getScheme(), "", cn.superName.replace("$", "/"));
            insert(extendsRelations, classLogical, extendsLogical);
        }
    }
    
    /**
     * Adds a class-interface location tuple to the implements relation.
     */
    private void addToImplements(ClassNode cn, ISourceLocation classLogical) throws URISyntaxException {
        if(cn.interfaces != null) {
            for(int i = 0; i < cn.interfaces.size(); i++) {
                ISourceLocation implementsLogical = values.sourceLocation(INTERFACE_SCHEME, "", 
                    ((String) cn.interfaces.get(i)).replace("$", "/"));
                insert(implementsRelations, classLogical, implementsLogical);
            }
        }
    }
    
    private List<AnnotationNode> composeAnnotations(List<AnnotationNode> ann1, List<AnnotationNode> ann2) {
        List<AnnotationNode> annotations = (ann1 != null) ? new ArrayList<AnnotationNode>(ann1) : new ArrayList<AnnotationNode>();
        if(ann2 != null) {
            annotations.addAll(ann2);
        }
        return annotations;
    }
    
    private ClassReader getClassReader(String className) throws IOException, URISyntaxException {
        try {
            return new ClassReader(resgistry.getInputStream(getPhysicalLoc(loc, className + ".class")));
        }
        catch(IOException e) {
            return new ClassReader(className);
        }
    }
    
    private String getCompilationUnitRelativePath() {
        String abs = compUnitPhysical.getPath().substring(compUnitPhysical.getPath().lastIndexOf("!") + 1).replace(".class", "");
        return abs.substring(abs.indexOf("/"));
    }
    
    private ISourceLocation getPreviousPackageLogicalLoc(String relativePath) throws URISyntaxException {
        return values.sourceLocation(PACKAGE_SCHEME, "", relativePath.substring(0, relativePath.lastIndexOf("/")));
    }
    
    private String getClassRelativePath(ClassNode cn) {
        return cn.name.replace("$", "/");
    }
    
    private IString getClassName(ClassNode cn) {
        String classPath = cn.name.replace("$", "/");
        return values.string(classPath.substring(classPath.lastIndexOf("/") + 1));
    }
    
    private IString getName(ISourceLocation logical) {
        return values.string(logical.getPath().substring(logical.getPath().lastIndexOf("/") + 1));
    }
    
    //TODO: All fields cannot have a clear scheme (e.g. interface). "java+class" will be the default scheme.
    private ISourceLocation getLogicalLoc(String desc) throws URISyntaxException {
        String scheme = (primitiveTypesMap.containsKey(values.string(desc))) ? PRIMITIVE_TYPE_SCHEME : CLASS_SCHEME;
        ISourceLocation classLogical = values.sourceLocation(scheme, "", 
            org.objectweb.asm.Type.getType(desc).getClassName().replace(".", "/"));
        return classLogical;
    }
    
    private ISourceLocation getPhysicalLoc(ISourceLocation uri, String path) throws URISyntaxException {
        return URIUtil.changePath(uri, uri.getPath() + "/" + path);
    }
    
    private String getMethodSignature(MethodNode mn, String constructorName) {
        String signature = (mn.name.equals(COMPILED_CONSTRUCTOR_NAME)) ? constructorName + "(" : mn.name + "(";
        String[] parameters = getMethodParameters(mn);
        for(String parameter : parameters) {
            signature += (signature.endsWith("(")) ? parameter : "," + parameter;
        }
        signature += ")";
        return signature;
    }
    
    private String[] getMethodParameters(MethodNode mn) {
        org.objectweb.asm.Type[] arguments = Type.getType(mn.desc).getArgumentTypes();
        String[] parameters = new String[arguments.length];
        
        for(int i = 0; i < arguments.length; i++) {
            parameters[i] = arguments[i].getClassName();
        }
        
        return parameters;
    }
    
    private String getClassScheme(int access) {
        return ((access & Opcodes.ACC_INTERFACE) != 0) ? INTERFACE_SCHEME : 
            ((access & Opcodes.ACC_ENUM) != 0) ? ENUM_SCHEME : CLASS_SCHEME;
    }
    
    private String getMethodScheme(MethodNode mn) {
        return (mn.name.equals(COMPILED_CONSTRUCTOR_NAME)) ? CONSTRUCTOR_SCHEME : METHOD_SCHEME;
    }
    
    private String getFieldScheme(FieldNode fn) {
        return ((fn.access & Opcodes.ACC_ENUM) != 0) ? ENUM_CONSTANT_SCHEME : FIELD_SCHEME;
    }
}
