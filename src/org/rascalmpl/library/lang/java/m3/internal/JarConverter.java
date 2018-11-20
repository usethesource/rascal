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
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
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
    private final static String INITIALIZER_SCHEME = "java+initializer";
    private final static String INTERFACE_SCHEME = "java+interface";
    private final static String METHOD_SCHEME = "java+method";
    private final static String PACKAGE_SCHEME = "java+package";
    private final static String PARAMETER_SCHEME = "java+parameter";
    private final static String PRIMITIVE_TYPE_SCHEME = "java+primitiveType";

    /**
     * Constant with the name of consructor methods in Java bytecode.
     */
    private final static String COMPILED_CONSTRUCTOR_NAME = "<init>";
    private final static String COMPILED_STATIC_CONSTRUCTOR_NAME = "<clinit>";

    /**
     * Constants with M3-specific names.
     */
    private final static String M3_STATIC_CONSTRUCTOR_NAME = "$initializer";

    /**
     * Source locations related constants.
     */
    private final static String FILE_SCHEME = "file";
    private final static String JAR_SCHEME = "jar";
    
    
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
    private URIResolverRegistry registry;


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
    public void convertJar(ISourceLocation jarLoc) {
        try {          
            loc = cleanJarLoc(jarLoc);
            registry = URIResolverRegistry.getInstance();

            initializeModifiers();
            initializePrimitiveTypes();
            
            createM3(loc);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void convertSingleClassFile(ISourceLocation classFile, String className) {
        try {          
            loc = classFile;
            registry = URIResolverRegistry.getInstance();

            initializeModifiers();
            initializePrimitiveTypes();
            
            createSingleClassM3(loc, className);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a new source location to create the input stream.
     */
    //TODO: manage nested locations
    private ISourceLocation cleanJarLoc(ISourceLocation jarLoc) throws URISyntaxException {
        if(jarLoc.getScheme().equals(JAR_SCHEME) && jarLoc.getPath().endsWith("!")) {
            return values.sourceLocation(FILE_SCHEME, "", jarLoc.getPath().substring(0, jarLoc.getPath().lastIndexOf("!")));
        }
        else if (jarLoc.getScheme().startsWith(JAR_SCHEME + "+")) {
            return (jarLoc.getPath().endsWith("!")) ? values.sourceLocation(jarLoc.getScheme().substring(jarLoc.getScheme().lastIndexOf("+") + 1), 
                "", jarLoc.getPath().substring(0, jarLoc.getPath().lastIndexOf("!"))) :
                URIUtil.changeScheme(jarLoc, jarLoc.getScheme().substring(jarLoc.getScheme().lastIndexOf("+") + 1));
        }
        else {
            return jarLoc;
        }
    }
    
    /**
     * Initializes the modifiers map by considering modifier opcodes
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
    //TODO: change when JarInputStream problem is solved.
    private void createM3(ISourceLocation uri) 
        throws IOException, URISyntaxException {
        //ISourceLocation jarLocation = values.sourceLocation("file", "", loc.getPath().substring(0,loc.getPath().indexOf("!")));
        InputStream is = registry.getInputStream(uri);
        JarInputStream jarStream = new JarInputStream(is);
        JarEntry entry = jarStream.getNextJarEntry();

        while (entry != null) {
            compUnitPhysical = getPhysicalLoc(uri, entry.getName());
            if(entry.getName().endsWith(".class")) {
                String compUnit = getCompilationUnitRelativePath();
                ClassReader cr = getClassReader(jarStream);
                
                setCompilationUnitRelations(compUnit);
                setPackagesRelations(compUnit);
                setClassRelations(cr, compUnit);
            }
            entry = jarStream.getNextJarEntry();
        }
        
        jarStream.close();
        is.close();
    }
    
    private void createSingleClassM3(ISourceLocation uri, String className) 
        throws IOException, URISyntaxException {
        String compUnit = className;
        ClassReader cr = getClassReader(className);

        setCompilationUnitRelations(compUnit);
        setPackagesRelations(compUnit);
        setClassRelations(cr, compUnit);
    } 

    /**
     * Generates compilation unit relations given a compilation unit relative
     * path. Considered relations: containment, declarations, and names.
     */
    private void setCompilationUnitRelations(String compUnitRelative) throws URISyntaxException {
//        IString compUnitName = values.string(compUnitRelative);
        ISourceLocation packageLogical = getParentPackageLogicalLoc(compUnitRelative);
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
        ISourceLocation currentPkgLogical = getParentPackageLogicalLoc(compUnitRelative);
        int packages = currentPkgLogical.getPath().length() - currentPkgLogical.getPath().replace("/", "").length();

        for(int i = 0; i < packages; i++) {
            IString currentName = getName(currentPkgLogical);
            ISourceLocation parentPkgLogical = getParentPackageLogicalLoc(currentPkgLogical.getPath());
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
    @SuppressWarnings("unchecked")
    private void setClassRelations(ClassReader cr, String compUnitRelative) throws IOException, URISyntaxException {
        //TODO: change when JarInputStream problem is solved.
        //ClassReader cr = getClassReader(compUnitRelative);

        if(cr != null) {
            ClassNode cn = new ClassNode();
            cr.accept(cn, ClassReader.SKIP_DEBUG);
            
            IString className = getClassName(cn.name);
            ISourceLocation compUnitLogical = values.sourceLocation(COMP_UNIT_SCHEME, "", compUnitRelative);
            ISourceLocation classLogical = values.sourceLocation(getClassScheme(cn.access), "", cn.name);
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
    }

    /**
     * Generates containment relations among the input class and its
     * inner classes.
     * Note: cn.innerClasses and cn.outerClass are not providing consistent 
     * information. Check if a new version of ASM is used. 
     */
    private void setInnerClassRelations(ClassNode cn, ISourceLocation classLogical) throws URISyntaxException {
        if(cn.innerClasses != null) {
            for(int i = 0; i < cn.innerClasses.size(); i++) {
                InnerClassNode icn = (InnerClassNode) cn.innerClasses.get(i);
                String path = classLogical.getPath();
                
                if(icn.name.equals(cn.name) && path.contains("$")) {
                    ISourceLocation outerClassLogical = URIUtil.changePath(classLogical, path.substring(0,path.lastIndexOf("$")));
                    
                    if(cn.outerMethod != null && !cn.outerMethod.isEmpty()) {
                        ISourceLocation methodLogical = getMethodLogicalLoc(cn.outerMethod, cn.outerMethodDesc, outerClassLogical);
                        addToContainment(methodLogical, classLogical);
                    }
                    else {
                        addToContainment(outerClassLogical, classLogical);
                    }
                }
            }
        }
    }

    /**
     * Generates field relations given an ASM class node and its logical 
     * source location. Considered relations: containment, declarations, 
     * names, modifiers, annotations, and typeDependency.
     */
    @SuppressWarnings("unchecked")
    private void setFieldRelations(ClassNode cn, ISourceLocation classLogical) throws URISyntaxException {
        if(cn.fields != null) {
            for(int i = 0; i < cn.fields.size(); i++) {
                FieldNode fn = (FieldNode) cn.fields.get(i);

                if((fn.access & Opcodes.ACC_SYNTHETIC) == 0) {
                    IString fieldName = values.string(fn.name);
                    ISourceLocation fieldLogical = values.sourceLocation(getFieldScheme(fn.access), "", 
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
    @SuppressWarnings("unchecked")
    private void setMethodRelations(ClassNode cn, ISourceLocation classLogical) throws URISyntaxException, IOException {
        if(cn.methods != null) {
            for(int i = 0; i < cn.methods.size(); i++) {
                MethodNode mn = (MethodNode) cn.methods.get(i);

                String signature = getMethodSignature(mn.name, mn.desc, getName(classLogical).getValue());
                IString methodName = (signature.contains("(")) ? values.string(signature.substring(0,signature.indexOf("("))) 
                    : values.string(signature);
                ISourceLocation methodLogical = getMethodLogicalLoc(mn.name, mn.desc, classLogical);
                //TODO: check offset + length
                ISourceLocation methodPhysical = compUnitPhysical;

                addToContainment(classLogical, methodLogical);
                addToDeclarations(methodLogical, methodPhysical);
                addToNames(methodName, methodLogical);
                addToModifiers(mn.access, methodLogical);
                addToAnnotations(composeAnnotations(mn.visibleAnnotations, mn.invisibleAnnotations), methodLogical);
                addToTypeDependency(methodLogical, Type.getType(mn.desc).getReturnType().getDescriptor());
                addToMethodOverrides(cn, mn, methodLogical);

                //TODO: we do not have access to parameters names - Check
                setParameterRelations(mn, methodLogical);
                setInstructionRelations(mn, methodLogical);
            }
        }
    }

    /**
     * Generates method overrides relations among and input method
     * node and super class and interfaces methods.
     */
    private void addToMethodOverrides(ClassNode cn, MethodNode mn, ISourceLocation methodLogical) throws IOException, URISyntaxException {
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

        if(cr != null) {
            ClassNode cn = new ClassNode();
            cr.accept(cn, ClassReader.SKIP_DEBUG);

            if(cn.methods != null) {
                for(int i = 0; i < cn.methods.size(); i++) {
                    MethodNode mnSuper = (MethodNode) cn.methods.get(i);
                    if(mnSuper.name.equals(mn.name) && mnSuper.desc.equals(mn.desc)) {  
                        String signature = getMethodSignature(mnSuper.name, mn.desc, ((IString)getClassName(cn.name)).getValue());
                        ISourceLocation superLogical = values.sourceLocation(getClassScheme(cn.access), "", cn.name);
                        ISourceLocation methodSuperLogical = values.sourceLocation(getMethodScheme(mnSuper.name), "", 
                            superLogical.getPath() + "/" + signature);

                        insert(methodOverrides, methodLogical, methodSuperLogical);
                        addToMethodOverrides(cn, mn, methodLogical);
                    }
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

    /**
     * Navigates the set of instructions related to a given method node.
     * Depending on the instruction type an action is defined.
     */
    private void setInstructionRelations(MethodNode mn, ISourceLocation methodLogical) throws URISyntaxException {
        if(mn.instructions != null) {
            @SuppressWarnings("unchecked")
            ListIterator<AbstractInsnNode> iterator = mn.instructions.iterator();

            while(iterator.hasNext()) {
                AbstractInsnNode n = iterator.next();

                if(n instanceof MethodInsnNode) {
                    visit(mn, methodLogical, (MethodInsnNode) n);
                }
                else if(n instanceof FieldInsnNode) {
                    visit(mn, methodLogical, (FieldInsnNode) n);
                }
                else if(n instanceof TypeInsnNode) {
                    visit(mn, methodLogical, (TypeInsnNode) n);
                }
            }
        }
    }

    /**
     * Visits an ASM MethodInsNode which represents a method invocation 
     * instruction. Generates the method invocation relation and adds the 
     * corresponding type dependency tuple.
     */
    private void visit(MethodNode mn, ISourceLocation methodLogical, MethodInsnNode n) throws URISyntaxException {
        String signature = getMethodSignature(n.name, n.desc, ((IString)getClassName(n.owner)).getValue());
        ISourceLocation methodInvocationLogical = values.sourceLocation(getMethodScheme(n.name), "", 
            n.owner + "/" + signature);
        addToMethodInvocation(methodLogical, methodInvocationLogical);
        // The class of the current method may also have a dependency on the same type.
        addToTypeDependency(methodLogical, n.owner);
    }

    //TODO: default scheme: java+field. Constants are not considered.
    /**
     * Visits an ASM FieldInsNode which represents a field loading or storing 
     * instruction. Generates the field access relation and adds the corresponding
     * type dependency tuple.
     */
    private void visit(MethodNode mn, ISourceLocation methodLogical, FieldInsnNode n) throws URISyntaxException {
        ISourceLocation fieldLogical = values.sourceLocation(FIELD_SCHEME, "", n.owner + "/" + n.name);
        addToFieldAccess(methodLogical, fieldLogical);
        addToTypeDependency(methodLogical, n.owner);
    }

    /**
     * Visits an ASM TypeInsnNode which represents a type instruction (receives 
     * a type descriptor. Generates the type dependency relation.
     */
    private void visit(MethodNode mn, ISourceLocation methodLogical, TypeInsnNode n) throws URISyntaxException {
        addToTypeDependency(methodLogical, n.desc);
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
     * If there is a class modifier the "isClass" flag must be set to true.
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
            ISourceLocation classLogical = getTypeLogicalLoc(desc);
            insert(typeDependency, logical, classLogical);
        }
    }

    /**
     * Adds a class-superclass location tuple to the extends relation.
     */
    private void addToExtends(ClassNode cn, ISourceLocation classLogical) throws URISyntaxException {
        if(cn.superName != null && !(cn.superName.equalsIgnoreCase(Object.class.getName().replace(".", "/")) ||
            cn.superName.equalsIgnoreCase(Enum.class.getName().replace(".", "/")))) {
            //TODO: check class scheme (interfaces)
            ISourceLocation extendsLogical = values.sourceLocation(classLogical.getScheme(), "", cn.superName);
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
                    ((String) cn.interfaces.get(i)));
                insert(implementsRelations, classLogical, implementsLogical);
            }
        }
    }

    /**
     * Adds a method-method location tuple to the method invocation relation.
     */
    private void addToMethodInvocation(ISourceLocation methodLogical, ISourceLocation methodInvocationLogical) {
        insert(methodInvocation, methodLogical, methodInvocationLogical);
    }

    /**
     * Adds a method-field location tuple to the field access relation.
     */
    private void addToFieldAccess(ISourceLocation methodLogical, ISourceLocation fieldLogical) {
        insert(fieldAccess, methodLogical, fieldLogical);
    }

    /**
     * Composes two annotation node lists. It manages null values.
     */
    private List<AnnotationNode> composeAnnotations(List<AnnotationNode> ann1, List<AnnotationNode> ann2) {
        List<AnnotationNode> annotations = (ann1 != null) ? new ArrayList<AnnotationNode>(ann1) : new ArrayList<AnnotationNode>();
        if(ann2 != null) {
            annotations.addAll(ann2);
        }
        return annotations;
    }

    /**
     * Returns an ASM ClassReader based on compilation unit location or name. 
     */
    private ClassReader getClassReader(String className) {
        try {
            return new ClassReader(className);
        }
        catch(Exception e) {
            return null;
        }
    }

    /**
     * Returns an ASM ClassReader based on an input stream. 
     */
    private ClassReader getClassReader(InputStream classStream) throws IOException, URISyntaxException {
        try {
            return new ClassReader(classStream);
        }
        catch(Exception e) {
            return null;
        }
    }

    /**
     * Returns a compilation unit relative path with regards to the Jar
     * physical location.
     */
    private String getCompilationUnitRelativePath() {
        String abs = compUnitPhysical.getPath().substring(loc.getPath().length()).replace(".class", "");
        return abs.substring(abs.indexOf("/"));
    }

    /**
     * Returns the logical location of the current package (cf. relativePath)
     * parent. A relative path with regards to the Jar location ir required.
     */
    private ISourceLocation getParentPackageLogicalLoc(String relativePath) throws URISyntaxException {
        return values.sourceLocation(PACKAGE_SCHEME, "", relativePath.substring(0, relativePath.lastIndexOf("/")));
    }

    /**
     * Returns a vallang String with the name of a class node. 
     */
    private IString getClassName(String name) {
        String classPath = name;
        return values.string(classPath.substring(classPath.lastIndexOf("/") + 1));
    }

    /**
     * Returns a vallang String based on a logical location. It considers
     * the last fragment of the location path.
     */
    private IString getName(ISourceLocation logical) {
        return values.string(logical.getPath().substring(logical.getPath().lastIndexOf("/") + 1));
    }

    /**
     * Returns a type logical location based on its descriptor (cf. ASM library).
     */
    //TODO: All fields cannot have a clear scheme (e.g. interface). "java+class" will be the default scheme.
    private ISourceLocation getTypeLogicalLoc(String desc) throws URISyntaxException {
        desc = desc.replace("[", "").replace("%5B", "");
        String scheme = (primitiveTypesMap.containsKey(values.string(desc))) ? PRIMITIVE_TYPE_SCHEME : CLASS_SCHEME;
        return (scheme == CLASS_SCHEME && !desc.startsWith("L")) ? values.sourceLocation(scheme, "", desc) :
            (scheme == CLASS_SCHEME && desc.startsWith("L")) ? values.sourceLocation(scheme, "", Type.getType(desc).getClassName().replace(".", "/")) :
                values.sourceLocation(scheme, "", Type.getType(desc.toUpperCase()).getClassName().replace(".", "/"));
    }

    /**
     * Returns a physical location based on an existing physicial location
     * and a relative path. E.g. uri: |jar+file://path/to/jar/file.jar!|,
     * path: "path/to/folder/in/the/jar".
     */
    private ISourceLocation getPhysicalLoc(ISourceLocation uri, String path) throws URISyntaxException {
        return URIUtil.changePath(uri, uri.getPath() + "/" + path);
    }

    /**
     * Returns a String with a method signature. If the name of the method 
     * node is "<init>", then the method name is replaced with the name of
     * the parent class.
     */
    private String getMethodSignature(String name, String desc, String constructorName) {
        String signature = (name.equals(COMPILED_CONSTRUCTOR_NAME)) ? constructorName + "(" : 
            (name.equals(COMPILED_STATIC_CONSTRUCTOR_NAME)) ? getStaticInitializerName(constructorName) : name + "(";
        String[] parameters = getMethodParameters(desc);
        for(String parameter : parameters) {
            signature += (signature.endsWith("(")) ? parameter : "," + parameter;
        }
        signature += (name.equals(COMPILED_STATIC_CONSTRUCTOR_NAME)) ? "" : ")";
        return signature;
    }

    /**
     * Returns a String array with the a method parameters' type names.
     */
    private String[] getMethodParameters(String desc) {
        Type[] arguments = Type.getType(desc).getArgumentTypes();
        String[] parameters = new String[arguments.length];

        for(int i = 0; i < arguments.length; i++) {
            parameters[i] = arguments[i].getClassName();
        }

        return parameters;
    }
    
    private ISourceLocation getMethodLogicalLoc(String name, String desc, ISourceLocation classLogical) throws URISyntaxException {
        String signature = getMethodSignature(name, desc, getName(classLogical).getValue());
        return values.sourceLocation(getMethodScheme(name), "", classLogical.getPath() + "/" + signature);
    }

    /**
     * Returns a class scheme based on the class' access flags.
     * java+interface, java+enum, and java+class are the possible schemes.
     */
    private String getClassScheme(int access) {
        return ((access & Opcodes.ACC_INTERFACE) != 0) ? INTERFACE_SCHEME : 
            ((access & Opcodes.ACC_ENUM) != 0) ? ENUM_SCHEME : CLASS_SCHEME;
    }

    /**
     * Returns a method scheme based on the method node name.
     * If "<init>" the java+constructor scheme is returned, java+method
     * otherwise.
     */
    private String getMethodScheme(String name) {
        return (name.equals(COMPILED_CONSTRUCTOR_NAME)) ? CONSTRUCTOR_SCHEME : 
            (name.equals(COMPILED_STATIC_CONSTRUCTOR_NAME)) ? INITIALIZER_SCHEME : METHOD_SCHEME;
    }

    /**
     * Returns a field scheme based on the field's access flags.
     * java+enumConstant and java+field are the possible schemes.
     */
    private String getFieldScheme(int access) {
        return ((access & Opcodes.ACC_ENUM) != 0) ? ENUM_CONSTANT_SCHEME : FIELD_SCHEME;
    }
    
    /**
     * Returns the name of a static initializer.
     */
    private String getStaticInitializerName(String className) {
        return className + M3_STATIC_CONSTRUCTOR_NAME;
    }
}
