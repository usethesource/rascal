/** 
 * Copyright (c) 2019, Lina Ochoa, Centrum Wiskunde & Informatica (NWOi - CWI) 
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
package org.rascalmpl.library.lang.java.m3.internal;

import static org.rascalmpl.library.lang.java.m3.internal.M3Constants.CLASS_SCHEME;
import static org.rascalmpl.library.lang.java.m3.internal.M3Constants.COMPILED_CONSTRUCTOR_NAME;
import static org.rascalmpl.library.lang.java.m3.internal.M3Constants.COMPILED_STATIC_CONSTRUCTOR_NAME;
import static org.rascalmpl.library.lang.java.m3.internal.M3Constants.CONSTRUCTOR_SCHEME;
import static org.rascalmpl.library.lang.java.m3.internal.M3Constants.ENUM_CONSTANT_SCHEME;
import static org.rascalmpl.library.lang.java.m3.internal.M3Constants.ENUM_SCHEME;
import static org.rascalmpl.library.lang.java.m3.internal.M3Constants.FIELD_SCHEME;
import static org.rascalmpl.library.lang.java.m3.internal.M3Constants.INITIALIZER_SCHEME;
import static org.rascalmpl.library.lang.java.m3.internal.M3Constants.INTERFACE_SCHEME;
import static org.rascalmpl.library.lang.java.m3.internal.M3Constants.M3_STATIC_CONSTRUCTOR_NAME;
import static org.rascalmpl.library.lang.java.m3.internal.M3Constants.METHOD_SCHEME;
import static org.rascalmpl.library.lang.java.m3.internal.M3Constants.PARAMETER_SCHEME;
import static org.rascalmpl.library.lang.java.m3.internal.M3Constants.PRIMITIVE_TYPE_SCHEME;
import static org.rascalmpl.library.lang.java.m3.internal.M3Constants.UNRESOLVED_SCHEME;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ParameterNode;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.TypeFactory;


public class ASMNodeResolver implements NodeResolver {
    
    //---------------------------------------------
    // Fields
    //---------------------------------------------
    
    /**
     * Supports the creation of Rascal values.
     */
    private static final IValueFactory valueFactory = ValueFactoryFactory.getValueFactory();
    
    /**
     * Supports the creation of Rascal types.
     */
    private static final TypeFactory typeFactory = TypeFactory.getInstance();
    
    /**
     * Relates primitive type descriptors with their corresponding 
     * type names.
     */
    private Map<String, String> primitiveTypes;
    
    /**
     * Store type schemes to improve performance
     */
    private Map<String, String> typeSchemes;
    
    /**
     * Looks up for ADTs or constructors.
     */
    private LimitedTypeStore typeStore;
    
    /**
     * Supports URI resolution.
     */
    private URIResolverRegistry registry;
    
    /**
     * URI of the JAR file
     */
    private ISourceLocation uri;
    
    /**
     * List of locations pointing to the classpath of the 
     * main JAR (only JAR files supported).
     */
    private List<ISourceLocation> classPath;
    
    
    //---------------------------------------------
    // Methods
    //---------------------------------------------
    
    /**
     * ASMNodeResolver constructor
     * @param typeStore
     */
    public ASMNodeResolver(ISourceLocation uri, IList classPath, final LimitedTypeStore typeStore) {
        this.typeStore = typeStore;
        this.uri = uri;
        this.registry = URIResolverRegistry.getInstance();
        this.classPath = initializeClassPath(classPath);
        this.typeSchemes = new HashMap<String, String>();
        initializePrimitiveTypes();
    }
    
    /**
     * Relates primitive type descriptors to their names, as defined
     * in the ASM Type class.
     */
    private void initializePrimitiveTypes() {
        primitiveTypes = new HashMap<String, String>();
        primitiveTypes.put(Type.BOOLEAN_TYPE.getDescriptor(), Type.BOOLEAN_TYPE.getClassName());
        primitiveTypes.put(Type.CHAR_TYPE.getDescriptor(), Type.CHAR_TYPE.getClassName());
        primitiveTypes.put(Type.DOUBLE_TYPE.getDescriptor(), Type.DOUBLE_TYPE.getClassName());
        primitiveTypes.put(Type.INT_TYPE.getDescriptor(), Type.INT_TYPE.getClassName());
        primitiveTypes.put(Type.LONG_TYPE.getDescriptor(), Type.LONG_TYPE.getClassName());
        primitiveTypes.put(Type.SHORT_TYPE.getDescriptor(), Type.SHORT_TYPE.getClassName());
    }
    
    /**
     * Initializes the list of JAR files in the classpath. It 
     * includes the main JAR and all nested JARs.
     * @param classPath - list of JAR files conforming the classpath 
     * of the main JAR.
     * @return list of locations pointing to JARs in the classpath
     */
    private List<ISourceLocation> initializeClassPath(IList classPath) {
        List<ISourceLocation> cp = new ArrayList<ISourceLocation>();
        
        try {
            ISourceLocation mainJar = toJarSrcLocation(uri);
            cp.add(mainJar);
            cp.addAll(getNestedJars(mainJar));
            classPath.forEach(loc -> {
                try {
                    ISourceLocation jarLoc = toJarSrcLocation((ISourceLocation) loc);
                    cp.add(jarLoc);
                    cp.addAll(getNestedJars(jarLoc));
                }
                catch (IOException | URISyntaxException e) {
                    throw new RuntimeException("Cannot gather nested JARs.", e);
                }
            });
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Cannot gather nested JARs.", e);
        }
        return cp;
    }
    
    /**
     * Given a location pointing to a JAR file, the method adds
     * "jar+" to its scheme and the "!" at the end of the path 
     * if needed.
     * @param uri - location pointing to a JAR file 
     * @return location with a "jar+<scheme>" or "jar" scheme,
     * and a path ending with "!"
     */
    private ISourceLocation toJarSrcLocation(ISourceLocation uri) {
        try {
            if (uri.getPath().endsWith(".jar")) {
                uri = (uri.getScheme().startsWith("jar")) ? uri : URIUtil.changeScheme(uri, "jar+" + uri.getScheme());
                return (uri.getScheme().endsWith("!")) ? uri : URIUtil.changePath(uri, uri.getPath() + "!");
            }
        }
        catch (URISyntaxException e) {
            throw new RuntimeException("The location " + uri + " does not reference a JAR file.", e);
        }
        throw new RuntimeException("The location " + uri + " does not reference a JAR file.");
    }
    
    /**
     * Gathers nested JARs in another JAR file passed as parameter.
     * @param uri - location of JAR file
     * @return list of nested JARs within the JAR file passed as 
     * parameter.
     * @throws IOException
     * @throws URISyntaxException
     */
    private List<ISourceLocation> getNestedJars(ISourceLocation uri) throws IOException, URISyntaxException {
        List<ISourceLocation> cp = new ArrayList<ISourceLocation>();
        ISourceLocation[] entries = registry.list(uri);
        for (ISourceLocation entry : entries) {
            
            if (registry.isDirectory(entry)) {
                cp.addAll(getNestedJars(entry));
            }
            
            if (entry.getPath().endsWith(".jar")) {
                entry = URIUtil.changeScheme(entry, "jar+" + uri.getScheme());
                entry = URIUtil.changePath(entry, entry.getPath() + "!");
                cp.add(entry);
            }
        }
        return cp;
    }
    
    @Override
    public ISourceLocation resolveBinding(Object node, ISourceLocation parent) {
        if (resolveInputValidation(node, parent)) {
            if (node instanceof FieldNode) {
                return resolveBinding((FieldNode) node, parent);
            }
            else if (node instanceof MethodNode) {
                return resolveBinding((MethodNode) node, parent);
            }
            else if (node instanceof ParameterNode) {
                return resolveBinding((ParameterNode) node, parent);
            }
        }
        
        if (node != null) {
            if (node instanceof AnnotationNode) {
                return resolveBinding((AnnotationNode) node);
            }
            else if (node instanceof ClassNode) {
                return resolveBinding((ClassNode) node);
            }
            // FieldInsNode represents a field loading or storing instruction.
            else if (node instanceof FieldInsnNode) {
                return resolveBinding((FieldInsnNode) node);
            }
            // MethodInsNode represents a method invocation instruction.
            else if (node instanceof MethodInsnNode) {
                return resolveBinding((MethodInsnNode) node);
            }
            else if (node instanceof Type) {
                return resolveBinding((Type) node);
            }
        }
        return M3LocationUtil.makeLocation(UNRESOLVED_SCHEME, null, null);
    }
    
    /**
     * Checks if both object and locations parameters are not null.
     * It also checks if the location path is not empty.
     * @param node - ASM object
     * @param parent - parent logical location of the ASM object
     * @return
     */
    private boolean resolveInputValidation(Object node, ISourceLocation parent) {
        return node != null && parent != null && !parent.getPath().isEmpty();
    }

    /**
     * Returns the location of an annotation node.
     * @param node - annotation node
     * @return logical location
     */
    private ISourceLocation resolveBinding(AnnotationNode node) {
        String path = Type.getType(node.desc).getInternalName();
        return M3LocationUtil.makeLocation(INTERFACE_SCHEME, "", path);
    }
    
    /**
     * Returns the location of a class node.
     * @param node - class node
     * @return logical location
     */
    private ISourceLocation resolveBinding(ClassNode node) {
        return M3LocationUtil.makeLocation(resolveClassScheme(node), "", node.name);
    }
    
    /**
     * Returns the location of a field store/access node.
     * @param node - field instruction node
     * @return logical location
     */
    private ISourceLocation resolveBinding(FieldInsnNode node) {
        String path = node.owner + "/" + node.name;
        return M3LocationUtil.makeLocation(FIELD_SCHEME, "", path);
    }
    
    /**
     * Returns the location of a field node.
     * @param node - field node
     * @param parent - parent logical location (class or interface)
     * @return logical location
     */
    private ISourceLocation resolveBinding(FieldNode node, ISourceLocation parent) {
        if (!parent.getScheme().equals(CLASS_SCHEME)
            && !parent.getScheme().equals(INTERFACE_SCHEME)) {
            return M3LocationUtil.makeLocation(UNRESOLVED_SCHEME, null, null);
        }
        else {
            String scheme = getFieldScheme(node.access);
            String path = parent.getPath() + "/" + node.name;
            return M3LocationUtil.makeLocation(scheme, "", path);
        }
    }
    
    /**
     * Returns a field scheme based on the field's access flags.
     * java+enumConstant and java+field are the possible schemes.
     */
    private String getFieldScheme(int access) {
        return ((access & Opcodes.ACC_ENUM) != 0) 
            ? ENUM_CONSTANT_SCHEME 
            : FIELD_SCHEME;
    }

    /**
     * Returns the location of a method invocation node.
     * @param node - method invocation node
     * @return logical location
     */
    private ISourceLocation resolveBinding(MethodInsnNode node) {
        return resolveMethodBinding(node.name, node.desc, node.owner);
    }
    
    /**
     * Returns the location of a method node.
     * @param node - method node
     * @param parent - parent logical location (class or interface)
     * @return logical location
     */
    private ISourceLocation resolveBinding(MethodNode node, ISourceLocation parent) {
        if (!parent.getScheme().equals(CLASS_SCHEME)
            && !parent.getScheme().equals(INTERFACE_SCHEME)) {
            return M3LocationUtil.makeLocation(UNRESOLVED_SCHEME, null, null);
        }
        else {
            return resolveMethodBinding(node.name, node.desc, parent);
        }
    }
    
    @Override
    public ISourceLocation resolveMethodBinding(String name, String desc, ISourceLocation classLoc) {
        return resolveMethodBinding(name, desc, classLoc.getPath());
    }
    
    /**
     * Returns a location of a method node given its name, 
     * descriptor, and class path.
     * @param name - name of the method
     * @param desc - bytecode descriptor of the method
     * @param typePath - path of the owner type
     * @return logical location
     */
    private ISourceLocation resolveMethodBinding(String name, String desc, String typePath) {
        String typeName = ((IString) M3LocationUtil.getLocationName(typePath)).getValue();
        String signature = getMethodSignature(name, desc, typeName);
        String path = typePath + "/" + signature;
        
        return M3LocationUtil.makeLocation(getMethodScheme(name), "", path);
    }
    
    /**
     * Creates a M3 method signature from a method's name,
     * descriptor, and class/type name.
     * @param name - name of the method
     * @param desc - bytecode descriptor of the method
     * @param typeName - name of the owner type
     * @return logical location
     */
    private String getMethodSignature(String name, String desc, String typeName) {
        if (name.equals(COMPILED_STATIC_CONSTRUCTOR_NAME)) {
            return createStaticInitializerName(typeName);
        }
        
        /* If the name of the method node is "<init>", then the method name 
         * is replaced with the name of the parent class. */
        String signature = (name.equals(COMPILED_CONSTRUCTOR_NAME)) ? typeName + "(" : name + "(";
        Type[] arguments = Type.getType(desc).getArgumentTypes();
        
        for (Type argument : arguments) {
            String argumentName = argument.getClassName();
            signature += (signature.endsWith("(")) ? argumentName : "," + argumentName;
        }
        return signature + ")";
    }
    
    /**
     * Returns the M3 name of a static initializer given a 
     * class/type name.
     * @param typeName - name of the type
     * @return name of the static initializer in M3
     */
    private String createStaticInitializerName(String typeName) {
        return typeName + M3_STATIC_CONSTRUCTOR_NAME;
    }
    
    /**
     * Returns the scheme of a method given the method name.
     * If "<init>" -> java+constructor
     * If "<clinit>" -> java+initializer
     * Otherwise -> java+method 
     * @param name - method name
     * @return method's scheme
     */
    private String getMethodScheme(String name) {
        return (name.equals(COMPILED_CONSTRUCTOR_NAME)) ? CONSTRUCTOR_SCHEME 
            : (name.equals(COMPILED_STATIC_CONSTRUCTOR_NAME)) ? INITIALIZER_SCHEME 
            : METHOD_SCHEME;
    }

    /**
     * Returns the location of a parameter node.
     * @param node - parameter node
     * @param parent - parent logical location (method)
     * @return logical location
     */
    private ISourceLocation resolveBinding(ParameterNode node, ISourceLocation parent) {
        if (!parent.getScheme().equals(METHOD_SCHEME)
            && !parent.getScheme().equals(CONSTRUCTOR_SCHEME)
            && !parent.getScheme().equals(INITIALIZER_SCHEME)) {
            return M3LocationUtil.makeLocation(UNRESOLVED_SCHEME, null, null);
        }
        else {
            String defaultName = "param";
            String path = parent.getPath() + "/" + defaultName;
            return M3LocationUtil.makeLocation(PARAMETER_SCHEME, "", path);
        }
    }
    
    /**
     * Returns the location of an ASM type.
     * @param type - ASM type
     * @return logical location
     */
    private ISourceLocation resolveBinding(Type type) {
        // Ignoring arrays
        String descriptor = type.getDescriptor()
            .replace("[", "")
            .replace("%5B", "");
        String scheme = getTypeScheme(descriptor);
        
        if (type.getClassName() == null) {
            return M3LocationUtil.makeLocation(scheme, "", descriptor);
        }
        
        /* According to the JVM specification we deal with base/primitive (B, C, D, F, I, J, S, Z),
         * object (L<className>;), and array ([<type>) types. java/lang/Object*/
        String path = type.getClassName()
            .replace(".", "/")
            .replace("[", "")
            .replace("]", "");
        
        return M3LocationUtil.makeLocation(scheme, "", path);        
    }

    /**
     * Returns the scheme of a type given its descriptor. It could be
     * a primitive, enum, interface, or class scheme. If the descriptor
     * points to a type that cannot be resolved, the method returns the
     * class scheme by default.
     * @param descriptor - descriptor of the type as described by the 
     * JVM specification
     * @return scheme of the type
     */
    private String getTypeScheme(String descriptor) {
        if (primitiveTypes.containsKey(descriptor)) {
            return PRIMITIVE_TYPE_SCHEME;
        }
        
        String className = descriptor
            .replace("[", "")
            .replace("%5B", "");
        if (className.startsWith("L") && className.endsWith(";")) {
            className = className.substring(1, descriptor.length() - 1);
        }
        
        typeSchemes.computeIfAbsent(className, k -> resolveClassScheme(k));
        return typeSchemes.get(className);
    }
    
    /**
     * Returns a class scheme based on the class name.
     * @param className - name of the class
     * @return scheme of the type
     */
    private String resolveClassScheme(String className) {
        ClassReader cr = buildClassReader(className);
        return (cr != null) ? resolveClassScheme(cr.getAccess()) : CLASS_SCHEME;
    }
    
    /**
     * Returns a class scheme based on the class' access flags.
     */
    private String resolveClassScheme(int access) {
        return ((access & Opcodes.ACC_INTERFACE) != 0) ? INTERFACE_SCHEME 
            : ((access & Opcodes.ACC_ENUM) != 0) ? ENUM_SCHEME 
            : CLASS_SCHEME;
    }
    
    @Override
    public String resolveClassScheme(ClassNode node) {
        typeSchemes.computeIfAbsent(node.name, k -> resolveClassScheme(node.access));
        return resolveClassScheme(node.access);
    }
    
    @Override
    public IConstructor resolveType(Object node, ISourceLocation parent) {
        ISourceLocation uri = resolveBinding(node, parent);
        
        if (resolveInputValidation(node, parent)) {
            if (node instanceof FieldNode) {
                return resolveType((FieldNode) node, uri);
            }
            else if (node instanceof MethodNode) {
                return resolveType((MethodNode) node, uri);
            }
        }
        
        if (node != null) {
            if (node instanceof ClassNode) {
                return resolveType((ClassNode) node, uri);
            }
            else if (node instanceof Type) {
                return resolveType((Type) node);
            }
        }
        
        return unresolvedSym();
    }

    /**
     * Returns the Rascal constructor of a class node.
     * @param node - class node
     * @param uri - class logical location
     * @return Rascal constructor (type symbol)
     */
    private IConstructor resolveType(ClassNode node, ISourceLocation uri) {
        // TODO: check cases where this list is not empty.
        IList typeParameterSymbols = valueFactory.list();
        
        if (uri.getScheme().equals(INTERFACE_SCHEME)) {
            return interfaceSymbol(uri, typeParameterSymbols);
        }
        // Default: class scheme
        else {
            return classSymbol(uri, typeParameterSymbols);
        }
    }

    /**
     * Returns the Rascal constructor of a field node.
     * @param node - field node
     * @param uri - field logical location
     * @return Rascal constructor (type symbol)
     */
    private IConstructor resolveType(FieldNode node, ISourceLocation uri) {
        Type type = Type.getType(node.desc);
        return resolveType(type);
    }

    /**
     * Returns the Rascal constructor of a method node.
     * @param node - method node
     * @param uri - method logical location
     * @return Rascal constructor (type symbol)
     */
    private IConstructor resolveType(MethodNode node, ISourceLocation uri) {
        Type descriptorType = Type.getType(node.desc);
        IList parametersSymbols = computeTypes(descriptorType.getArgumentTypes());
        
        // We are not considering static initializers
        if (uri.getScheme().equals(CONSTRUCTOR_SCHEME)) {
            return constructorSymbol(uri, parametersSymbols);
        }
        else {
            // TODO: check cases where this list is not empty.
            IList typeParametersSymbols = valueFactory.list();
            IConstructor returnSymbol = resolveType(descriptorType.getReturnType());
            return methodSymbol(uri, typeParametersSymbols, returnSymbol, parametersSymbols);
        }
    }

    /**
     * Returns a list with Rascal constructors from an array
     * of ASM types. Useful when considering method parameters.
     * @param types - array of ASM types
     * @return list with Rascal constructors (type symbols)
     */
    private IList computeTypes(Type[] types) {
        IListWriter writer = valueFactory.listWriter();
        for (Type type : types) {
            IConstructor cons = resolveType(type);
            writer.append(cons);
        }
        return writer.done();
    }
    
    /**
     * Returns the Rascal constructor of an ASM type.
     * @param type - ASM type
     * @return Rascal constructor (type symbol)
     */
    private IConstructor resolveType(Type type) {
        String descriptor = type.getDescriptor();
        
        /* According to the JVM specification we deal with base/primitive (B, C, D, F, I, J, S, Z),
         * void (V), object (L<className>;), and array ([<type>) types. */
        if(descriptor.equals(Type.VOID_TYPE.getDescriptor())) {
            return voidSymbol();
        }
        else if (primitiveTypes.containsKey(descriptor)) {
            return primitiveSymbol(primitiveTypes.get(descriptor));
        }
        else if (descriptor.startsWith("[")) { 
            return arraySymbol(resolveType(type.getElementType()), type.getDimensions());
        }
        else if (descriptor.startsWith("%5B")) {
            // Considering percent encoding: %5B == [
            return arraySymbol(resolveType(type.getElementType()), type.getDimensions());
        }
        else if (descriptor.startsWith("L")) {
            ISourceLocation uri = resolveBinding(type);
            // TODO: check cases where this list is not empty.
            IList typeParameters = valueFactory.list();
            return classSymbol(uri, typeParameters);
        }
        
        return unresolvedSym();
    }

    //TODO: move to an abstract class?
    private IConstructor arraySymbol(IConstructor elem, int dimensions) {
        io.usethesource.vallang.type.Type cons = typeStore.lookupConstructor(getTypeSymbol(), "array", typeFactory.tupleType(elem.getType(), typeFactory.integerType()));
        return valueFactory.constructor(cons, elem, valueFactory.integer(dimensions));
    }
    
    private IConstructor classSymbol(ISourceLocation uri, IList typeParameters) {
        if (uri.getPath().equals("/java/lang/Object")) {
            io.usethesource.vallang.type.Type cons = typeStore.lookupConstructor(getTypeSymbol(), "object", typeFactory.voidType());
            return valueFactory.constructor(cons);
        }
        else {
            io.usethesource.vallang.type.Type cons = typeStore.lookupConstructor(getTypeSymbol(), "class", typeFactory.tupleType(uri.getType(), typeParameters.getType()));
            return valueFactory.constructor(cons, uri, typeParameters);
        }
    }
    
    private IConstructor constructorSymbol(ISourceLocation uri, IList parametersSymbols) {
        io.usethesource.vallang.type.Type cons = typeStore.lookupConstructor(getTypeSymbol(), "constructor", typeFactory.tupleType(uri.getType(), parametersSymbols.getType()));
        return valueFactory.constructor(cons, uri, parametersSymbols);
    }
    
    private IConstructor interfaceSymbol(ISourceLocation uri, IList typeParameterSymbols) {
        io.usethesource.vallang.type.Type cons = typeStore.lookupConstructor(getTypeSymbol(), "interface", typeFactory.tupleType(uri.getType(), typeParameterSymbols.getType()));
        return valueFactory.constructor(cons, uri, typeParameterSymbols);
    }
    
    private IConstructor methodSymbol(ISourceLocation uri, IList typeParametersSymbols, IConstructor returnSymbol, IList parametersSymbols) {
        io.usethesource.vallang.type.Type cons = typeStore.lookupConstructor(getTypeSymbol(), "method", typeFactory.tupleType(uri.getType(), typeParametersSymbols.getType(), returnSymbol.getType(), parametersSymbols.getType()));
        return valueFactory.constructor(cons, uri, typeParametersSymbols, returnSymbol, parametersSymbols);
    }
    
    private IConstructor primitiveSymbol(String name) {
        io.usethesource.vallang.type.Type cons = typeStore.lookupConstructor(getTypeSymbol(), name, typeFactory.voidType());
        return valueFactory.constructor(cons);
    }
    
    private IConstructor unresolvedSym() {
        io.usethesource.vallang.type.Type cons = typeStore.lookupConstructor(getTypeSymbol(), "unresolved", typeFactory.voidType());
        return valueFactory.constructor(cons);
    }
    
    private IConstructor voidSymbol() {
        io.usethesource.vallang.type.Type cons = typeStore.lookupConstructor(getTypeSymbol(), "void", typeFactory.voidType());
        return valueFactory.constructor(cons);
    }
    
    private io.usethesource.vallang.type.Type getTypeSymbol() {
        return typeStore.lookupAbstractDataType("TypeSymbol");
    }
    
    @Override
    public ClassReader buildClassReader(String className) {
        try {
            return new ClassReader(className);
        }
        catch (IOException | IllegalArgumentException e) {
            return buildClassReaderFromStream(className);
        }
    }

    /**
     * Returns an ASM ClassReader from a compilation unit location 
     * or name. It creates a stream from one of the JARs in the classpath.
     * If the class definition is not found, it returns null.
     * @param className - class/comilation unit name/path (<pkg>/<name>)
     * @return ASM ClassReader, null if the compilation unit is not found
     */
    private ClassReader buildClassReaderFromStream(String className) { 
        try {
            for (ISourceLocation entry : classPath) {
                ISourceLocation loc = URIUtil.getChildLocation(entry, className + ".class");
                if (registry.exists(loc)) {
                    InputStream stream = registry.getInputStream(loc);
                    return buildClassReader(stream);
                }
            }
        }
        catch (IOException e) {
            // Nothing to do
        }
        return null;
    }
    
    @Override
    public ClassReader buildClassReader(InputStream classStream) throws IOException {
        return new ClassReader(classStream);
    }
    
}
