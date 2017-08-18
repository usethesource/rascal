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
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;

public class JarConverter extends M3Converter {
    
    private final static String CLASS_SCHEME = "java+class";
    private final static String COMP_UNIT_SCHEME = "java+compilationUnit";
    private final static String CONSTRUCTOR_SCHEME = "java+constructor";
    private final static String ENUM_SCHEME = "java+enum";
    private final static String ENUM_CONSTANT_SCHEME = "java+enumConstant";
    private final static String FIELD_SCHEME = "java+field";
    private final static String INTERFACE_SCHEME = "java+interface";
    private final static String METHOD_SCHEME = "java+method";
    private final static String PACKAGE_SCHEME = "java+package";
    private final static String COMPILED_CONSTRUCTOR_NAME = "<init>";
    
    private IMap modifiersMap;
    private ISourceLocation compUnitPhysical;

    public JarConverter(LimitedTypeStore typeStore, Map<String, ISourceLocation> cache) {
        super(typeStore, cache);
    }
    
    @SuppressWarnings("unchecked")
    public void convert(ISourceLocation jarLoc) {
        this.loc = jarLoc;
        initializeModifiers();
        
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
    
 // org.objectweb.asm.Type.BOOLEAN_TYPE.getDescriptor()
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
    
    private void createM3(ISourceLocation uri) 
        throws IOException, URISyntaxException {
        URIResolverRegistry resgistry = URIResolverRegistry.getInstance();
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
    
    private void setCompilationUnitRelations(String compUnitRelative) throws URISyntaxException {
        IString compUnitName = values.string(compUnitRelative);
        ISourceLocation packageLogical = values.sourceLocation(PACKAGE_SCHEME, "", compUnitRelative.substring(0, compUnitRelative.lastIndexOf("/")));
        ISourceLocation compUnitLogical = values.sourceLocation(COMP_UNIT_SCHEME, "", compUnitRelative);
        
        addToContainment(packageLogical, compUnitLogical);
        //TODO: same offset of the contained class?
        addToDeclarations(compUnitPhysical, compUnitLogical);
        //TODO: M3 from directory does not add it to the names relation. Check.
        addToNames(compUnitName, compUnitLogical);
    }
    
    private void setPackagesRelations(String compUnitRelative) throws URISyntaxException {
        String currentPackage = compUnitRelative.substring(0,compUnitRelative.lastIndexOf("/"));
        int packages = currentPackage.length() - currentPackage.replace("/", "").length();
        
        for(int i = 1; i < packages; i++) {
            String parentPackage = currentPackage.substring(0, currentPackage.lastIndexOf("/"));
            
            IString currentName = values.string(currentPackage.substring(currentPackage.lastIndexOf("/") + 1));
            ISourceLocation parentPkgLogical = values.sourceLocation(PACKAGE_SCHEME, "", parentPackage);
            ISourceLocation parentPkgPhysical = URIUtil.changePath(loc, loc.getPath() + parentPackage);
            ISourceLocation currentPkgLogical = values.sourceLocation(PACKAGE_SCHEME, "", currentPackage);
            ISourceLocation currentPkgPhysical = URIUtil.changePath(loc, loc.getPath() + currentPackage);
            
            addToContainment(parentPkgLogical, currentPkgLogical);
            addToDeclarations(currentPkgPhysical, currentPkgLogical);
            addToNames(currentName, currentPkgLogical);
            
            if(i == packages - 1) {
                IString parentName = values.string(parentPackage.substring(parentPackage.lastIndexOf("/") + 1));
                addToDeclarations(parentPkgPhysical, parentPkgLogical);
                addToNames(parentName, parentPkgLogical);
            }
            
            currentPackage = parentPackage;
        }
    }
    
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
        addToDeclarations(classPhysical, classLogical);
        addToNames(className, classLogical);
        
        setInnerClassRelations(cn, classLogical);
        setClassExtendsRelation(cn, classLogical);
        setClassImplementsRelation(cn, classLogical);
        
        setModifiers(cn.access, classLogical, true);
        setAnnotations(composeAnnotations(cn.visibleAnnotations, cn.invisibleAnnotations), classLogical);
        setMethodRelations(cn, classLogical);
        setFieldRelations(cn, classLogical);
    }

    private void setInnerClassRelations(ClassNode cn, ISourceLocation classLogical) throws URISyntaxException {
        for(int i = 0; i < cn.innerClasses.size(); i++) {
            InnerClassNode icn = (InnerClassNode) cn.innerClasses.get(i);
            String innerClassPath = icn.name.replace("$", "/");
            
            if(!innerClassPath.equals(cn.name.replace("$", "/"))) {
                ISourceLocation innerClassLogical = values.sourceLocation(getClassScheme(icn.access), "", innerClassPath);
                addToContainment(classLogical, innerClassLogical);
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
        if(cn.interfaces != null) {
            for(int i = 0; i < cn.interfaces.size(); i++) {
                ISourceLocation implementsLogical = values.sourceLocation(INTERFACE_SCHEME, "", 
                    ((String) cn.interfaces.get(i)).replace("$", "/"));
                insert(implementsRelations, classLogical, implementsLogical);
            }
        }
    }
    
    //TODO: set offset and length
    private void setMethodRelations(ClassNode cn, ISourceLocation classLogical) throws URISyntaxException, IOException {
        if(cn.methods != null) {
            for(int i = 0; i < cn.methods.size(); i++) {
                MethodNode mn = (MethodNode) cn.methods.get(i);
                //ListIterator<AbstractInsnNode> iterator = mn.instructions.iterator();
                
                // TODO: check
                if(!getClassScheme(cn.access).equals(ENUM_SCHEME) && !(mn.name.equalsIgnoreCase("values") || 
                    mn.name.equalsIgnoreCase("valueOf"))) {
                    String signature = getMethodSignature(mn, getClassName(classLogical));
                    
                    IString methodName = values.string(signature.substring(0,signature.indexOf("(")));
                    ISourceLocation methodLogical = values.sourceLocation(getMethodScheme(mn), "", 
                        classLogical.getPath() + "/" + signature);
                    //TODO: check offset + length
                    ISourceLocation methodPhysical = compUnitPhysical;
                    
                    addToContainment(classLogical, methodLogical);
                    addToDeclarations(methodLogical, methodPhysical);
                    addToNames(methodName, methodLogical);
                    
                    setModifiers(mn.access, methodLogical);
                    setAnnotations(composeAnnotations(mn.visibleAnnotations, mn.invisibleAnnotations), methodLogical);
                    setMethodOverridesRelations(cn, mn, methodLogical);
                    //TODO: we do not have access to parameters names - Check
                    setParameterRelations(mn, methodLogical);
                    //setVariableRelations(mn, methodLogical);
                }
            }
        }
    }

    private void setMethodOverridesRelations(ClassNode cn, MethodNode mn, ISourceLocation methodLogical) throws IOException, URISyntaxException {
        if(cn.superName != null && !cn.superName.isEmpty()) {
            setMethodOverridesRelation(cn.superName, mn, methodLogical);
        }
        if(cn.interfaces != null) {
            for(int i = 0; i < cn.interfaces.size(); i++) {
                setMethodOverridesRelation((String) cn.interfaces.get(i), mn, methodLogical);
            }
        }
    }
    
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
                        setMethodOverridesRelations(cn, mn, methodLogical);
                    }
                }
            }
    }
    
    //Containment:
    //<|java+constructor:///uniandes/cupi2/lineasTelefonicas/interfaz/PanelLineaTelefonica/PanelLineaTelefonica(uniandes.cupi2.lineasTelefonicas.interfaz.InterfazLineasTelefonicas,int)|,
    //|java+parameter:///uniandes/cupi2/lineasTelefonicas/interfaz/PanelLineaTelefonica/PanelLineaTelefonica(uniandes.cupi2.lineasTelefonicas.interfaz.InterfazLineasTelefonicas,int)/scope(pNumeroLinea)/scope(0)/pNumeroLinea|>
    private void setParameterRelations(MethodNode mn, ISourceLocation methodLogical) throws URISyntaxException {
        String[] parameters = getMethodParameters(mn);
        for(int i = 0; i < parameters.length; i++) {
            IString parameterName = values.string("param" + 1);
            ISourceLocation parameterLogical = URIUtil.changePath(methodLogical, methodLogical.getPath() + "/" + parameterName.getValue());
            ISourceLocation parameterPhysical = compUnitPhysical;
            
            addToContainment(methodLogical, parameterLogical);
            addToDeclarations(parameterPhysical, parameterLogical);
            addToNames(parameterName, parameterLogical);
        }
    }
    
    private void setVariableRelations(MethodNode mn, ISourceLocation methodLogical) {
        if(mn.localVariables != null) {
            for(int i = 0; i < mn.localVariables.size(); i++) {
                LocalVariableNode vn = (LocalVariableNode) mn.localVariables.get(i);
                System.out.println(vn.desc + " - From: " + vn.start + " To: " + vn.end + " - " + vn.name + " - " + vn.signature);
            }
        }
    }
    
    private void setFieldRelations(ClassNode cn, ISourceLocation classLogical) throws URISyntaxException {
        if(cn.fields != null) {
            for(int i = 0; i < cn.fields.size(); i++) {
                FieldNode fn = (FieldNode) cn.fields.get(i);
                
                if((fn.access & Opcodes.ACC_SYNTHETIC) == 0) {
                    IString fieldName = values.string(fn.name);
                    ISourceLocation fieldLogical = values.sourceLocation(getFiledScheme(fn), "", 
                        classLogical.getPath() + "/" + fn.name);
                    //TODO: check offset + length
                    ISourceLocation fieldPhysical = compUnitPhysical;
                    
                    addToContainment(classLogical, fieldLogical);
                    addToDeclarations(fieldLogical, fieldPhysical);
                    addToNames(fieldName, fieldLogical);
                    
                    setModifiers(fn.access, fieldLogical);
                    setAnnotations(composeAnnotations(fn.visibleAnnotations, fn.invisibleAnnotations), fieldLogical);
                }
            }
        }
    }    
    
    //TODO: SourceConverter does not consider abstract modifier. Check.
    //TODO: Use parseModifiers(int modifiers) -> JavaToRascalConverter?
    private void setModifiers(int access, ISourceLocation logical, boolean...isClass) {
        for(int i = 0; i < 15; i++) {
            // Identify modifiers by filtering the access flags
            int shift = 0x1 << i;
            IConstructor modifier = (IConstructor) modifiersMap.get(values.integer(shift));
            
            if((access & shift) != 0 && modifier != null && (isClass.length >= 1 && shift != Opcodes.ACC_SYNCHRONIZED)) {
                insert(modifiers, logical, modifier);
            }
        }
    }
    
    private void setAnnotations(List<AnnotationNode> annotations, ISourceLocation classLogical) throws URISyntaxException {
        for(AnnotationNode node : annotations) {
            ISourceLocation annotationLogical = values.sourceLocation(INTERFACE_SCHEME, "", 
                org.objectweb.asm.Type.getType(node.desc).getInternalName());
            insert(this.annotations, classLogical, annotationLogical);
        }
    }
    
    private List<AnnotationNode> composeAnnotations(List<AnnotationNode> ann1, List<AnnotationNode> ann2) {
        List<AnnotationNode> annotations = (ann1 != null) ? new ArrayList<AnnotationNode>(ann1) : new ArrayList<AnnotationNode>();
        if(ann2 != null) {
            annotations.addAll(ann2);
        }
        return annotations;
    }
    
    private void addToContainment(ISourceLocation parent, ISourceLocation child) {
        insert(containment, parent, child);
    }
    
    private void addToDeclarations(ISourceLocation physical, ISourceLocation logical) {
        insert(declarations, logical, physical);
    }
    
    private void addToNames(IString name, ISourceLocation logical) {
        insert(names, name, logical);
    }
    
    private ClassReader getClassReader(String className) throws IOException, URISyntaxException {
        try {
            return new ClassReader(URIResolverRegistry.getInstance().getInputStream(getPhysicalLoc(loc, className + ".class")));
        }
        catch(IOException e) {
            return new ClassReader(className);
        }
    }
    
    private String getCompilationUnitRelativePath() {
        String abs = compUnitPhysical.getPath().substring(compUnitPhysical.getPath().lastIndexOf("!") + 1).replace(".class", "");
        return abs.substring(abs.indexOf("/"));
    }
    
    private String getClassRelativePath(ClassNode cn) {
        return cn.name.replace("$", "/");
    }
    
    private IString getClassName(ClassNode cn) {
        String classPath = cn.name.replace("$", "/");
        return values.string(classPath.substring(classPath.lastIndexOf("/") + 1));
    }
    
    private String getClassName(ISourceLocation classLogical) {
        return classLogical.getPath().substring(classLogical.getPath().lastIndexOf("/") + 1);
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
        org.objectweb.asm.Type[] arguments = org.objectweb.asm.Type.getType(mn.desc).getArgumentTypes();
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
    
    private String getFiledScheme(FieldNode fn) {
        return ((fn.access & Opcodes.ACC_ENUM) != 0) ? ENUM_CONSTANT_SCHEME : FIELD_SCHEME;
    }
}
