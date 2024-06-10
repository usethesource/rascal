/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Ashim Shahi - - CWI
 *   * Ferry Rietveld - - UvA 
 *   * Chiel Peters - - UvA
 *   * Omar Pakker - - UvA
 *   * Maria Gouseti - - UvA
 *   * Lina Ochoa - - CWI
 *   
 * This code was developed in the Software Evolution course of the Software Engineering master.
 * 
 *******************************************************************************/
package org.rascalmpl.library.lang.java.m3.internal;

import static org.rascalmpl.library.lang.java.m3.internal.M3Constants.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ModuleNode;
import org.objectweb.asm.tree.ModuleOpenNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.jar.JarURIResolver;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;


//TODO: check offset + length in physical locations.
//TODO: change when JarInputStream problem is solved.
public class JarConverter extends M3Converter {
     /**
     * Physical source location of the current compilation unit. 
     * A .class file is considered as a compilation unit.
     */
    private ISourceLocation compUnitPhysical;
    
    /**
     * Supports the resolution of locations and types based on
     * ASM nodes and types.
     */
    private NodeResolver resolver;
    
    /**
     * Relatates modifiers opcodes with modifier nodes.
     */
    private Map<Integer, IConstructor> modifiersOpcodes;

    /**
     * Supports URI resolution.
     */
    private URIResolverRegistry registry;
    
    /**
     * JarConverter constructor 
     * @param typeStore
     * @param cache
     */
    public JarConverter(LimitedTypeStore typeStore, Map<String, ISourceLocation> cache) {
        super(typeStore, cache);
        this.registry = URIResolverRegistry.getInstance();
        initializeModifiers();
    }

    /**
     * Initializes the modifiersOpcodes map. Its keys refer to modifiers
     * opcodes declared in the Opcodes ASM interface. Its values are
     * Rascal modifiers data types.
     */
    private void initializeModifiers() {
        modifiersOpcodes = new HashMap<Integer, IConstructor>();
        modifiersOpcodes.put(Opcodes.ACC_ABSTRACT, constructModifierNode("abstract"));
        modifiersOpcodes.put(Opcodes.ACC_FINAL, constructModifierNode("final"));
        modifiersOpcodes.put(Opcodes.ACC_NATIVE, constructModifierNode("native"));
        modifiersOpcodes.put(Opcodes.ACC_PRIVATE, constructModifierNode("private"));
        modifiersOpcodes.put(Opcodes.ACC_PROTECTED, constructModifierNode("protected"));
        modifiersOpcodes.put(Opcodes.ACC_PUBLIC, constructModifierNode("public"));
        modifiersOpcodes.put(Opcodes.ACC_STATIC, constructModifierNode("static"));
        modifiersOpcodes.put(Opcodes.ACC_STRICT, constructModifierNode("strictfp"));
        modifiersOpcodes.put(Opcodes.ACC_SYNCHRONIZED, constructModifierNode("synchronized"));
        modifiersOpcodes.put(Opcodes.ACC_TRANSIENT, constructModifierNode("transient"));
        modifiersOpcodes.put(Opcodes.ACC_VOLATILE, constructModifierNode("volatile"));
        modifiersOpcodes.put(Opcodes.ACC_TRANSITIVE, constructModifierNode("transitive"));
    }
    
    /**
     * Creates a M3 model from a Jar file.
     * @param jar - Jar file location
     */
    public void convertJar(ISourceLocation jar, IList classPath) {
        loc = jar;
        resolver = new ASMNodeResolver(loc, classPath, typeStore);
        createM3();
    }
    
    /**
     * Creates a M3 model from a file within a Jar.
     * @param classFile
     * @param className
     */
    public void convertJarFile(ISourceLocation classFile, String className, IList classpath) {
        loc = classFile;
        createSingleClassM3(className, classpath);
    }
    
    /**
     * Creates a M3 model from a location that points to a Jar file. 
     * Jar scheme is not supported.
     */
    private void createM3() {
        try {                 
            try (JarInputStream jarStream = new JarInputStream(registry.getInputStream(loc))) {
                JarEntry entry = jarStream.getNextJarEntry();
                while (entry != null) {
                    compUnitPhysical = URIUtil.getChildLocation(JarURIResolver.jarify(loc), entry.getName());
            
                    if (entry.getName().endsWith(".class")) {
                        String compUnit = getCompilationUnitRelativePath();
                        ClassReader classReader = resolver.buildClassReader(jarStream);
                        
                        setCompilationUnitRelations(compUnit);
                        setPackagesRelations(compUnit);
                        setClassRelations(classReader, compUnit);
                    }
                    entry = jarStream.getNextJarEntry();
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Error while managing Jar stream.", e);
        }
    }
    
    /**
     * Creates a M3 model from a file within a Jar. The creation of 
     * relations associated to the compilation unit, its related class,
     * and parent packages is triggered.
     * @param className
     */
    private void createSingleClassM3(String className, IList classpath) {
        if (resolver == null) {
            resolver = new ASMNodeResolver(loc, classpath, typeStore);
        }

        String compUnit = className.replaceAll("\\.", "/");
        ClassReader classReader = resolver.buildClassReader(className);

        this.compUnitPhysical = URIUtil.getChildLocation(JarURIResolver.jarify(loc), compUnit + ".class");
        setCompilationUnitRelations(compUnit);
        setPackagesRelations(compUnit);
        setClassRelations(classReader, compUnit);
    } 
    
    /**
     * Returns a compilation unit relative path with regards to the Jar
     * file location.
     * @return compilation unit relative path
     */
    private String getCompilationUnitRelativePath() {
        int beginningIndex = loc.getPath().length();
        String absolutePath = compUnitPhysical.getPath().substring(beginningIndex).replace(".class", "");
        return absolutePath.substring(absolutePath.indexOf("/"));
    }

    /**
     * Sets compilation unit M3 relations.
     * @param compUnitRelative - compilation unit name/relative path
     */
    private void setCompilationUnitRelations(String compUnitRelative) {
        ISourceLocation packageLogical = createParentPackageLogicalLoc(compUnitRelative);
        ISourceLocation compUnitLogical = M3LocationUtil.makeLocation(COMP_UNIT_SCHEME, "", compUnitRelative);

        addToContainment(packageLogical, compUnitLogical);
        addToDeclarations(compUnitLogical, compUnitPhysical);
        //TODO: M3 from directory does not add it to the names relation. Check.
        //IString compUnitName = values.string(compUnitRelative);
        //addToNames(compUnitName, compUnitLogical);
    }

    /**
     * Sets packages M3 relations. The method considers all parent 
     * packages where the compilation unit is located.
     * @param compUnitRelative - compilation unit name/relative path
     */
    private void setPackagesRelations(String compUnitRelative) {
        ISourceLocation packageLogical = createParentPackageLogicalLoc(compUnitRelative);
        String packagePath = packageLogical.getPath();
        int packages = packagePath.length() - packagePath.replace("/", "").length() - 1;

        for (int i = 0; i < packages; i++) {
            IString name = M3LocationUtil.getLocationName(packageLogical);
            String parentPkgPath = packagePath.substring(0, packagePath.lastIndexOf("/"));
            ISourceLocation packagePhysical = M3LocationUtil.extendPath(loc, packagePath);
            ISourceLocation parentPkgLogical = M3LocationUtil.makeLocation(PACKAGE_SCHEME, "", parentPkgPath);
            ISourceLocation parentPkgPhysical = M3LocationUtil.extendPath(loc, parentPkgPath);

            addToContainment(parentPkgLogical, packageLogical);
            addToDeclarations(packageLogical, packagePhysical);
            addToNames(packageLogical, name);

            if (i == packages - 1) {
                IString parentName = M3LocationUtil.getLocationName(parentPkgLogical);
                addToDeclarations(parentPkgLogical, parentPkgPhysical);
                addToNames(parentPkgLogical, parentName);
            }

            packageLogical = parentPkgLogical;
            packagePath = parentPkgPath;
        }
    }

    /**
     * Returns the logical location of the current package (cf. relativePath)
     * parent. A relative path with regards to the Jar location is required.
     * @param relativePath - package relative path
     * @return M3 package location
     */
    private ISourceLocation createParentPackageLogicalLoc(String relativePath) {
        String path = relativePath.substring(0, relativePath.lastIndexOf("/"));
        return M3LocationUtil.makeLocation(PACKAGE_SCHEME, "", path);
    }
    
    private ISourceLocation resolveInternalTypeName(String name) {
        ClassReader classReader = resolver.buildClassReader(name);

        if (classReader != null) {
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, ClassReader.SKIP_DEBUG);
            return resolver.resolveBinding(classNode, null);
        }

        return bindingsResolver.makeBinding("java+classOrInterface", name, name);
    }
    /**
     * Sets class M3 relations. The creation of relations associated
     * to inner classes, fields, and methods is triggered.
     * @param classReader
     * @param compUnitRelative
     */
    private void setClassRelations(ClassReader classReader, String compUnitRelative) {
        if (classReader != null) {
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, ClassReader.SKIP_DEBUG);
            
            if (classNode.module != null) {
                addModuleRelations(classNode.module);
                // TODO: check if this is ok; we are skipping everything else here!
                return;
            }

            IString className = M3LocationUtil.getLocationName(classNode.name);
            ISourceLocation compUnitLogical = M3LocationUtil.makeLocation(COMP_UNIT_SCHEME, "", compUnitRelative);
            
            ISourceLocation classLogical = resolver.resolveBinding(classNode, null);
            ISourceLocation classPhysical = M3LocationUtil.makeLocation(compUnitPhysical, classReader.header, classReader.b.length);
            IConstructor cons = resolver.resolveType(classNode, null);
            List<AnnotationNode> annotations = composeAnnotations(classNode.visibleAnnotations, classNode.invisibleAnnotations);

            addToContainment(compUnitLogical, classLogical);
            addToDeclarations(classLogical, classPhysical);
            addToNames(classLogical, className);
            addToExtends(classLogical, classNode);
            addToImplements(classLogical, classNode);
            addToModifiers(classLogical, classNode.access, true);
            addToAnnotations(classLogical, annotations);
            addToTypes(classLogical, cons);
            
            setInnerClassRelations(classNode, classLogical); 
            setFieldRelations(classNode, classLogical);
            setMethodRelations(classNode, classLogical);
            setLanguages(resolver.resolveLanguageVersion(classNode));
        }
    }

    private void addModuleRelations(ModuleNode module) {
        ISourceLocation modLoc = resolveBinding(module);
        addToDeclarations(modLoc, compUnitPhysical);

        if (module.exports != null) {
            for (var export : module.exports) {
                var pkgLoc = resolveBinding(export.packaze);

                if (export.modules.isEmpty()) {
                    // export to all
                    insert(moduleExportsPackage, modLoc, pkgLoc, URIUtil.rootLocation("java+module"));
                }
                else {
                    // export to specific modules
                    for (var to : export.modules) {
                        insert(moduleExportsPackage, modLoc, pkgLoc, M3LocationUtil.makeLocation("java+module", "", to));
                    }
                }
            }
        }

        if (module.provides != null) {
            for (var provides : module.provides) {
                var service = resolveInternalTypeName(provides.service);
                for (var to : provides.providers) {
                    insert(moduleProvidesService, modLoc, service, resolveInternalTypeName(to));
                }
            }
        }

        if (module.uses != null) {
            for (var uses : module.uses) {
                var service = resolveInternalTypeName(uses);
                insert(moduleProvidesService, modLoc, service);
            }
        }

        if (module.requires != null) {
            for (var requires : module.requires) {
                var required = M3LocationUtil.makeLocation("java+module", "", requires.module);
                insert(moduleRequiresModule, modLoc, required);
            }
        }

        if (module.opens != null) {
            for (ModuleOpenNode opens : module.opens) {
                var pkg = resolveBinding(opens.packaze);
                
                if (opens.modules.isEmpty()) {
                    // open to all
                    insert(moduleOpensPackage, pkg, URIUtil.rootLocation("java+module"));
                }
                else {
                    // open to specific 
                    for (var to : opens.modules) {
                        insert(moduleOpensPackage, modLoc, pkg, resolveInternalTypeName(to));
                    }
                }
            }
        }

        return;
    }

    /**
     * Sets M3 relations of all inner classes of a given class node.
     * @param classNode - parent class node where inner classes are declared
     * @param classLogical - parent class location
     */
    private void setInnerClassRelations(ClassNode classNode, ISourceLocation classLogical) {
        // cn.innerClasses and cn.outerClass are not providing consistent information. 
        List<InnerClassNode> innerClasses = classNode.innerClasses;
        
        if (innerClasses != null) {
            for (int i = 0; i < innerClasses.size(); i++) {
                InnerClassNode innerClass = (InnerClassNode) innerClasses.get(i);
                String classPath = classLogical.getPath();
                
                if (innerClass.name.equals(classNode.name) && classPath.contains("$")) {
                    String outerClassPath = classPath.substring(0, classPath.lastIndexOf("$"));
                    ISourceLocation outerClassLogical = M3LocationUtil.changePath(classLogical, outerClassPath);
                    
                    if (classNode.outerMethod != null && !classNode.outerMethod.isEmpty()) {
                        ISourceLocation methodLogical = resolver.resolveMethodBinding(classNode.outerMethod, classNode.outerMethodDesc, outerClassLogical);
                        addToContainment(methodLogical, classLogical);
                    }
                    else {
                        addToContainment(outerClassLogical, classLogical);
                    }
                    
                    addToModifiers(classLogical, innerClass.access, true);
                }
            }
        }
    }

    /**
     * Sets M3 relations of all fields of a given class node.
     * @param classNode - class node where fields are declared
     * @param classLogical - class location
     */
    private void setFieldRelations(ClassNode classNode, ISourceLocation classLogical) {
        List<FieldNode> fields = classNode.fields;
        
        if (fields != null) {
            for (int i = 0; i < fields.size(); i++) {
                FieldNode fieldNode = (FieldNode) fields.get(i);

                if ((fieldNode.access & Opcodes.ACC_SYNTHETIC) == 0) {
                    IString fieldName = values.string(fieldNode.name);
                    ISourceLocation fieldLogical = resolver.resolveBinding(fieldNode, classLogical);
                    ISourceLocation fieldPhysical = compUnitPhysical;
                    IConstructor cons = resolver.resolveType(fieldNode, classLogical);
                    List<AnnotationNode> annotations = composeAnnotations(fieldNode.visibleAnnotations, fieldNode.invisibleAnnotations);
                    
                    addToContainment(classLogical, fieldLogical);
                    addToDeclarations(fieldLogical, fieldPhysical);
                    addToNames(fieldLogical, fieldName);
                    addToModifiers(fieldLogical, fieldNode.access);
                    addToAnnotations(fieldLogical, annotations);
                    addToTypeDependency(fieldLogical, Type.getType(fieldNode.desc));
                    addToTypes(fieldLogical, cons);
                }
            }
        }
    }

    private void setLanguages(IConstructor version) {
       addToLanguages(version);
    }

    /**
     * Sets M3 relations of all methods of a given class node. The
     * creation of relations associated to parameters and bytecode
     * is triggered.
     * @param classNode - class node where fields are declared
     * @param classLogical - class location
     */
    private void setMethodRelations(ClassNode classNode, ISourceLocation classLogical) {
        List<MethodNode> methods = classNode.methods;
        
        if (methods != null) {    
            for (int i = 0; i < methods.size(); i++) {
                MethodNode methodNode = (MethodNode) methods.get(i);
                Type methodType = Type.getType(methodNode.desc).getReturnType();
                
                ISourceLocation methodLogical = resolver.resolveBinding(methodNode, classLogical);
                ISourceLocation methodPhysical = compUnitPhysical;
                IString methodName = getMethodName(methodLogical);
                IConstructor cons = resolver.resolveType(methodNode, classLogical);
                List<AnnotationNode> annotations = composeAnnotations(methodNode.visibleAnnotations, methodNode.invisibleAnnotations);
                
                addToContainment(classLogical, methodLogical);
                addToDeclarations(methodLogical, methodPhysical);
                addToNames(methodLogical, methodName);
                addToModifiers(methodLogical, methodNode.access);
                addToAnnotations(methodLogical, annotations);
                addToTypeDependency(methodLogical, methodType);
                addToMethodOverrides(classNode, methodNode, methodLogical);
                addToTypes(methodLogical, cons);

                //TODO: we do not have access to parameters names - Check
                setExceptionRelations(methodNode, methodLogical);
                setParameterRelations(methodNode, methodLogical);
                setInstructionRelations(methodNode, methodLogical);
            }
        }
    }

    /**
     * Sets methodOverrides relation considering a method node.
     * @param superClass - class qualified name
     * @param methodNode - method node (presumably overridden)
     * @param methodLogical - logical location of the method
     */
    private void setMethodOverridesRelation(String superClass, MethodNode methodNode, ISourceLocation methodLogical) {
        ClassReader classReader = resolver.buildClassReader(superClass);

        if (classReader != null) {
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, ClassReader.SKIP_DEBUG);
            List<MethodNode> superMethods = classNode.methods;
            
            if (superMethods != null) {
                for (MethodNode superMethodNode : superMethods) {   
                    
                    if ((superMethodNode.access & Opcodes.ACC_STATIC) != 0) {
                        // static methods do not override
                        continue;
                    }

                    if ((superMethodNode.access & Opcodes.ACC_FINAL) != 0) {
                        // final methods can not be overriden
                        continue;
                    }

                    if (superMethodNode.name.equals(methodNode.name) 
                        && superMethodNode.desc.equals(methodNode.desc)) {  
                        
                        ISourceLocation superClassLogical = resolver.resolveBinding(classNode, null);
                        ISourceLocation methodSuperLogical = resolver.resolveBinding(superMethodNode, superClassLogical);

                        insert(methodOverrides, methodLogical, methodSuperLogical);
                        addToMethodOverrides(classNode, methodNode, methodLogical);
                    }
                    else if (superMethodNode.name.equals(methodNode.name)) {
                        // if the parameters and the name are the same, we can have co-variant return types (non-primitive)
                        if (Type.getArgumentTypes(superMethodNode.desc).equals(Type.getArgumentTypes(methodNode.desc))) {
                            // so here we assume the code was generated by a correct Java compiler
                            ISourceLocation superClassLogical = resolver.resolveBinding(classNode, null);
                            ISourceLocation methodSuperLogical = resolver.resolveBinding(superMethodNode, superClassLogical);
    
                            insert(methodOverrides, methodLogical, methodSuperLogical);
                            addToMethodOverrides(classNode, methodNode, methodLogical);
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets M3 relations of exceptions of a given method.
     * @param methodNode - method node
     * @param methodLogical - logical location of the method
     */
    private void setExceptionRelations(MethodNode methodNode, ISourceLocation methodLogical) {
        List<String> exceptions = methodNode.exceptions;
        
        for (String exception : exceptions) {
            //TODO: check for interfaces
            ISourceLocation exceptionLogical = M3LocationUtil.makeLocation(CLASS_SCHEME, "", exception);
            addToTypeDependency(methodLogical, exceptionLogical);
        }
    }
    
    /**
     * Sets M3 relations of all parameters of a given method.
     * @param methodNode - method node
     * @param methodLogical - logical location of the method
     */
    private void setParameterRelations(MethodNode methodNode, ISourceLocation methodLogical) {
        Type[] parameters = Type.getType(methodNode.desc).getArgumentTypes();

        for (int i = 0; i < parameters.length; i++) {
            IString parameterName = values.string("param" + i);
            String path = methodLogical.getPath() + "/" + parameterName.getValue();
            IConstructor cons = resolver.resolveType(parameters[i], null);
            
            ISourceLocation parameterLogical = M3LocationUtil.makeLocation(PARAMETER_SCHEME, "", path);
            ISourceLocation parameterPhysical = compUnitPhysical;

            addToContainment(methodLogical, parameterLogical);
            addToDeclarations(parameterLogical, parameterPhysical);
            addToNames(parameterLogical, parameterName);
            addToTypeDependency(parameterLogical, parameters[i]);
            addToTypes(parameterLogical, cons);
        }
    }

    /**
     * Sets M3 relations associated to method, field, and type instructions.
     * @param methodNode - method node
     * @param methodLogical - logical location of the method
     */
    private void setInstructionRelations(MethodNode methodNode, ISourceLocation methodLogical) {
        InsnList instructions = methodNode.instructions;
        
        if (instructions != null) {
            ListIterator<AbstractInsnNode> iterator = instructions.iterator();

            while (iterator.hasNext()) {
                AbstractInsnNode node = iterator.next();

                // MethodInsNode represents a method invocation instruction.
                if (node instanceof MethodInsnNode) {
                    setInstructionRelations(methodNode, methodLogical, (MethodInsnNode) node);
                }
                // FieldInsNode represents a field loading or storing instruction.
                else if (node instanceof FieldInsnNode) {
                    setInstructionRelations(methodNode, methodLogical, (FieldInsnNode) node);
                }
                // TypeInsnNode which represents a type instruction (receives a type descriptor).
                else if (node instanceof TypeInsnNode) {
                    setInstructionRelations(methodNode, methodLogical, (TypeInsnNode) node);
                }
            }
        }
    }
    
    /**
     * Sets M3 relations associated to a method invocation instruction.
     * @param methodNode - invoker method
     * @param methodLogical - logical location of the method
     * @param instructionNode - method invocation instruction node
     */
    private void setInstructionRelations(MethodNode methodNode, ISourceLocation methodLogical, MethodInsnNode instructionNode) {
        ISourceLocation methodInvocationLogical = resolver.resolveBinding(instructionNode, methodLogical);

        
        addToMethodInvocation(methodLogical, methodInvocationLogical);
        // The class of the current method may also have a dependency on the same type.
        addToTypeDependency(methodLogical, Type.getObjectType(instructionNode.owner));
    }

    /**
     * Sets M3 relations associated to a field loading or storing 
     * instruction.
     * @param methodName - owner method
     * @param methodLogical - logical location of the method
     * @param instructionNode - field loading/storing instruction node
     */
    //TODO: default scheme: java+field. Constants are not considered.
    private void setInstructionRelations(MethodNode methodName, ISourceLocation methodLogical, FieldInsnNode instructionNode) {
        ISourceLocation fieldLogical = resolver.resolveBinding(instructionNode, methodLogical);
        addToFieldAccess(methodLogical, fieldLogical);
        addToTypeDependency(methodLogical, Type.getObjectType(instructionNode.owner));
    }

    /**
     * Sets M3 relations associated to a type instruction.
     * @param methodNode - owner method
     * @param methodLogical - logical location of the method
     * @param instructionNode - type instruction node
     */
    private void setInstructionRelations(MethodNode methodNode, ISourceLocation methodLogical, TypeInsnNode instructionNode) {
        addToTypeDependency(methodLogical, Type.getObjectType(instructionNode.desc));
    }

    /**
     * Adds a new tuple to the M3 annotation relation. Relates a 
     * list of annotation nodes to a parent location.
     * @param parent - parent location (class, method, or field)
     * @param annotationNodes - list of annotation nodes
     */
    private void addToAnnotations(ISourceLocation parent, List<AnnotationNode> annotationNodes) {
        for (AnnotationNode node : annotationNodes) {
            ISourceLocation child = resolver.resolveBinding(node, null);
            insert(annotations, parent, child);
        }
    }
    
    /**
     * Adds a new tuple to the M3 containment relation. Relates a 
     * container (parent location) to a contained element (child location).
     * @param parent - parent location (container)
     * @param child - child location (contained)
     */
    private void addToContainment(ISourceLocation parent, ISourceLocation child) {
        insert(containment, parent, child);
    }

    /**
     * Adds a new tuple to the M3 declarations relation. Relates a logical
     * location of an element to a physical location.
     * @param logical - logical location
     * @param physical - physical location
     */
    private void addToDeclarations(ISourceLocation logical, ISourceLocation physical) {
        insert(declarations, logical, physical);
    }

    /**
     * Adds a new tuple to the M3 extends relation. Relates a class with
     * its super class. 
     * @param subclassNode - subclass node
     * @param subclassLogical - subclass logical location
     */
    private void addToExtends(ISourceLocation subclassLogical, ClassNode subclassNode) {        
        if (subclassNode.superName != null 
            && !(subclassNode.superName.equalsIgnoreCase(OBJECT_CLASS_PATH) 
            || subclassNode.superName.equalsIgnoreCase(ENUM_CLASS_PATH))) {
            
            //TODO: check class scheme (interfaces)
            ISourceLocation extendsLogical = M3LocationUtil.makeLocation(subclassLogical.getScheme(), "", subclassNode.superName);
            insert(extendsRelations, subclassLogical, extendsLogical);
        }
    }
    
    /**
     * Adds a new tuple to the M3 fieldAcces relation. Relates a method with
     * an accessed field. 
     * @param methodLogical - method logical location
     * @param fieldLogical - field logical location
     */
    private void addToFieldAccess(ISourceLocation methodLogical, ISourceLocation fieldLogical) {
        insert(fieldAccess, methodLogical, fieldLogical);
    }
    
    /**
     * Adds a new tuple to the M3 implements relation. Relates a class with
     * an interface. 
     * @param classNode - class node
     * @param classLogical - class logical location
     */
    private void addToImplements(ISourceLocation classLogical, ClassNode classNode) {
        List<String> interfaces = classNode.interfaces;
        
        if (interfaces != null) {
            ISetWriter writer = (resolver.resolveClassScheme(classNode) == INTERFACE_SCHEME) ? extendsRelations :  implementsRelations;
            for (String path : interfaces) {
                ISourceLocation implementsLogical = M3LocationUtil.makeLocation(INTERFACE_SCHEME, "", path);
                insert(writer, classLogical, implementsLogical);
            }
        }
    }
    
    /**
     * Adds a new tuple to the M3 methodInvocation relation. Relates an
     * invoker method with an invoked method.
     * @param methodLogical - invoker method logical location
     * @param methodInvocationLogical - invoked method logical location
     */
    private void addToMethodInvocation(ISourceLocation methodLogical, ISourceLocation methodInvocationLogical) {
        insert(methodInvocation, methodLogical, methodInvocationLogical);
    }
    
    /**
     * Adds a new tuple to the M3 methodOverrides relation. Relates an
     * overridden method with its super method.
     * @param classNode - owner class node
     * @param methodNode - overridden method node
     * @param methodLogical - overridden method logical location
     */
    private void addToMethodOverrides(ClassNode classNode, MethodNode methodNode, ISourceLocation methodLogical) {
        if (classNode.superName != null && !classNode.superName.isEmpty()) {
            setMethodOverridesRelation(classNode.superName, methodNode, methodLogical);
        }
        
        List<String> interfaces = classNode.interfaces;
        if (interfaces != null) {
            for (String interfac :interfaces) {
                setMethodOverridesRelation(interfac, methodNode, methodLogical);
            }
        }
    }
    
    /**
     * Adds a new tuple to the M3 modifiers relation. Relates an element
     * with a modifier.
     * @param logical - element logical location
     * @param access - modifier opcode (cf. Opcodes interface)
     * @param isClass - it must be set to true if there is a class modifier
     */
    //TODO: SourceConverter does not consider abstract modifier. Check.
    //TODO: Use parseModifiers(int modifiers) -> JavaToRascalConverter?
    private void addToModifiers(ISourceLocation logical, int access, boolean...isClass) {
        for (int i = 0; i < 15; i++) {
            
            // Identify modifiers by filtering the access flags
            int shift = 0x1 << i;
            IConstructor modifier = (IConstructor) modifiersOpcodes.get(shift);

            if ((access & shift) != 0 && modifier != null 
                && !(isClass.length >= 1 && shift == Opcodes.ACC_SYNCHRONIZED)) {
                insert(modifiers, logical, modifier);
            }
        }
    }
    
    /**
     * Adds a new tuple to the M3 names relation. Relates a name
     * with its corresponding element.
     * @param logical - element logical location
     * @param name - element name
     */
    private void addToNames(ISourceLocation logical, IString name) {
        insert(names, name, logical);
    }

    /**
     * Adds a new tuple to the M3 type dependency relation based
     * on the element logical location and the depending type
     * descriptor.
     * @param logical - element logical location
     * @param descriptor - type descriptor identified by ASM
     */
    private void addToTypeDependency(ISourceLocation logical, Type type) {
        if (!type.equals(Type.VOID_TYPE)) {
            ISourceLocation typeLogical = resolver.resolveBinding(type, null);
            addToTypeDependency(logical, typeLogical);
        }
    }
    
    /**
     * Adds a new tuple to the M3 type dependency relation. Relates a 
     * element logical location with its corresponding type (defined as
     * a M3 logical location).
     * @param logical - element logical location
     * @param typeLogical - type logical location
     */
    private void addToTypeDependency(ISourceLocation logical, ISourceLocation typeLogical) {
        insert(typeDependency, logical, typeLogical);
    }
    
    /**
     * Adds a new tuple to the M3 types relation. Considers a Rascal
     * constructor (AST).
     * @param logical
     * @param cons
     */
    private void addToTypes(ISourceLocation logical, IConstructor cons) {
        insert(types, logical, cons);
    }

    private void addToLanguages(IConstructor lang) {
        insert(languages, lang);
    }

    /**
     * Composes two lists of annotation nodes.
     * @param ann1 - first annotation list
     * @param ann2 - second annotation list
     * @return composed list of annotations
     */
    private List<AnnotationNode> composeAnnotations(List<AnnotationNode> ann1, List<AnnotationNode> ann2) {
        List<AnnotationNode> annotations = new ArrayList<AnnotationNode>();
        if (ann1 != null) {
            annotations.addAll(ann1);
        }
        if (ann2 != null) {
            annotations.addAll(ann2);
        }
        return annotations;
    }
    
    /**
     * Returns the name of a method from a logical location.
     * E.g. from java+method://<path>/<methodName>(<params>), it returns
     * <methodName>.
     * @param methodLogical - method logical location
     * @return method name
     */
    private IString getMethodName(ISourceLocation methodLogical) {
        String signature = M3LocationUtil.getLocationName(methodLogical).getValue();
        IString name = (signature.contains("(")) 
            ? values.string(signature.substring(0,signature.indexOf("("))) 
            : values.string(signature);
            
        return name;
    }  
}

