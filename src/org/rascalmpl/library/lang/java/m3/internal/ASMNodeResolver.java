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

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ParameterNode;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.TypeFactory;

import static org.rascalmpl.library.lang.java.m3.internal.M3Constants.*;


public class ASMNodeResolver implements NodeResolver {
    
    //---------------------------------------------
    // Fields
    //---------------------------------------------
    
    private static final IValueFactory valueFactory = ValueFactoryFactory.getValueFactory();
    
    private static final TypeFactory typeFactory = TypeFactory.getInstance();
    
    // Relates primitive type descriptors with their corresponding type names.
    private Map<String, String> primitiveTypes;
    
    private LimitedTypeStore typeStore;
    
    
    //---------------------------------------------
    // Methods
    //---------------------------------------------
    
    public ASMNodeResolver(final LimitedTypeStore typeStore) {
        this.typeStore = typeStore;
        initializePrimitiveTypes();
    }
    
    private void initializePrimitiveTypes() {
        primitiveTypes = new HashMap<String, String>();
        primitiveTypes.put(Type.BOOLEAN_TYPE.getDescriptor(), Type.BOOLEAN_TYPE.getClassName());
        primitiveTypes.put(Type.CHAR_TYPE.getDescriptor(), Type.CHAR_TYPE.getClassName());
        primitiveTypes.put(Type.DOUBLE_TYPE.getDescriptor(), Type.DOUBLE_TYPE.getClassName());
        primitiveTypes.put(Type.INT_TYPE.getDescriptor(), Type.INT_TYPE.getClassName());
        primitiveTypes.put(Type.LONG_TYPE.getDescriptor(), Type.LONG_TYPE.getClassName());
        primitiveTypes.put(Type.SHORT_TYPE.getDescriptor(), Type.SHORT_TYPE.getClassName());
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
    
    private boolean resolveInputValidation(Object node, ISourceLocation parent) {
        return node != null && parent != null && !parent.getPath().isEmpty();
    }

    private ISourceLocation resolveBinding(AnnotationNode node) {
        String path = Type.getType(node.desc).getInternalName();
        return M3LocationUtil.makeLocation(INTERFACE_SCHEME, "", path);
    }
    
    private ISourceLocation resolveBinding(ClassNode node) {
        return M3LocationUtil.makeLocation(getClassScheme(node.access), "", node.name);
    }
    
    private ISourceLocation resolveBinding(FieldInsnNode node) {
        String path = node.owner + "/" + node.name;
        return M3LocationUtil.makeLocation(FIELD_SCHEME, "", path);
    }
    
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

    private ISourceLocation resolveBinding(MethodInsnNode node) {
        return resolveMethodBinding(node.name, node.desc, node.owner);
    }
    
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
    
    private ISourceLocation resolveMethodBinding(String name, String desc, String classPath) {
        String className = ((IString) M3LocationUtil.getLocationName(classPath)).getValue();
        String signature = getMethodSignature(name, desc, className);
        String path = classPath + "/" + signature;
        
        return M3LocationUtil.makeLocation(getMethodScheme(name), "", path);
    }
    
    private String getMethodSignature(String name, String desc, String className) {
        if (name.equals(COMPILED_STATIC_CONSTRUCTOR_NAME)) {
            return createStaticInitializerName(className);
        }
        
        /* If the name of the method node is "<init>", then the method name 
         * is replaced with the name of the parent class. */
        String signature = (name.equals(COMPILED_CONSTRUCTOR_NAME)) ? className + "(" : name + "(";
        Type[] arguments = Type.getType(desc).getArgumentTypes();
        
        for (Type argument : arguments) {
            String argumentName = argument.getClassName();
            signature += (signature.endsWith("(")) ? argumentName : "," + argumentName;
        }
        return signature + ")";
    }
    
    private String createStaticInitializerName(String className) {
        return className + M3_STATIC_CONSTRUCTOR_NAME;
    }
    
    private String getMethodScheme(String name) {
        // If "<init>" -> java+constructor
        // If "<clinit>" -> java+initializer
        // Otherwise -> java+method 
        return (name.equals(COMPILED_CONSTRUCTOR_NAME)) ? CONSTRUCTOR_SCHEME 
            : (name.equals(COMPILED_STATIC_CONSTRUCTOR_NAME)) ? INITIALIZER_SCHEME 
            : METHOD_SCHEME;
    }

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
    
    private ISourceLocation resolveBinding(Type type) {
        // Ignoring arrays
        String descriptor = type.getDescriptor().replace("[", "").replace("%5B", "");
        if (type.getClassName() == null) {
            return M3LocationUtil.makeLocation(CLASS_SCHEME, "", descriptor);
        }
        
        /* According to the JVM specification we deal with base/primitive (B, C, D, F, I, J, S, Z),
         * object (L<className>;), and array ([<type>) types. java/lang/Object*/
        String path = type.getClassName()
            .replace(".", "/")
            .replace("[", "")
            .replace("]", "");
        if (primitiveTypes.containsKey(descriptor)) {
            return M3LocationUtil.makeLocation(PRIMITIVE_TYPE_SCHEME, "", path);
        }
        else {
            return M3LocationUtil.makeLocation(CLASS_SCHEME, "", path);
        }
    }

    /**
     * Returns a class scheme based on the class' access flags.
     * java+interface, java+enum, and java+class are the possible schemes.
     */
    private String getClassScheme(int access) {
        return ((access & Opcodes.ACC_INTERFACE) != 0) ? INTERFACE_SCHEME 
            : ((access & Opcodes.ACC_ENUM) != 0) ? ENUM_SCHEME 
            : CLASS_SCHEME;
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
            else if (node instanceof ParameterNode) {
                return resolveType((ParameterNode) node, uri);
            }
        }
        
        if (node != null) {
            if (node instanceof AnnotationNode) {
                return resolveType((AnnotationNode) node, uri);
            }
            else if (node instanceof ClassNode) {
                return resolveType((ClassNode) node, uri);
            }
            else if (node instanceof Type) {
                return resolveType((Type) node, uri);
            }
        }
        
        return unresolvedSym();
    }
    
    private IConstructor resolveType(AnnotationNode node, ISourceLocation uri) {
        // TODO Auto-generated method stub
        return null;
    }

    private IConstructor resolveType(ClassNode node, ISourceLocation uri) {
        // TODO Auto-generated method stub
        return null;
    }

    private IConstructor resolveType(FieldNode node, ISourceLocation uri) {
        // TODO Auto-generated method stub
        return null;
    }

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

    private IList computeTypes(Type[] params) {
        IListWriter writer = valueFactory.listWriter();
        for (Type param : params) {
            IConstructor cons = resolveType(param);
            writer.append(cons);
        }
        return writer.done();
    }
    
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
    
    private IConstructor resolveType(ParameterNode node, ISourceLocation uri) {
        // TODO Auto-generated method stub
        return null;
    }

    // TODO: move to a separate abstract class?
    private IConstructor unresolvedSym() {
        io.usethesource.vallang.type.Type cons = typeStore.lookupConstructor(getTypeSymbol(), "unresolved", typeFactory.voidType());
        return valueFactory.constructor(cons);
    }
    
    private IConstructor primitiveSymbol(String name) {
        io.usethesource.vallang.type.Type cons = typeStore.lookupConstructor(getTypeSymbol(), name, typeFactory.voidType());
        return valueFactory.constructor(cons);
    }
    
    private IConstructor constructorSymbol(ISourceLocation uri, IList parametersSymbols) {
        io.usethesource.vallang.type.Type cons = typeStore.lookupConstructor(getTypeSymbol(), "constructor", typeFactory.tupleType(uri.getType(), parametersSymbols.getType()));
        return valueFactory.constructor(cons, uri, parametersSymbols);
    }
    
    private IConstructor methodSymbol(ISourceLocation uri, IList typeParametersSymbols, IConstructor returnSymbol, IList parametersSymbols) {
        io.usethesource.vallang.type.Type cons = typeStore.lookupConstructor(getTypeSymbol(), "method", typeFactory.tupleType(uri.getType(), typeParametersSymbols.getType(), returnSymbol.getType(), parametersSymbols.getType()));
        return valueFactory.constructor(cons, uri, typeParametersSymbols, returnSymbol, parametersSymbols);
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
    
    private IConstructor arraySymbol(IConstructor elem, int dimensions) {
        io.usethesource.vallang.type.Type cons = typeStore.lookupConstructor(getTypeSymbol(), "array", typeFactory.tupleType(elem.getType(), typeFactory.integerType()));
        return valueFactory.constructor(cons, elem, valueFactory.integer(dimensions));
    } 
    
    private IConstructor voidSymbol() {
        io.usethesource.vallang.type.Type cons = typeStore.lookupConstructor(getTypeSymbol(), "void", typeFactory.voidType());
        return valueFactory.constructor(cons);
    }
    private io.usethesource.vallang.type.Type getTypeSymbol() {
        return typeStore.lookupAbstractDataType("TypeSymbol");
    }
}
