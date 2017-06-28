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
import java.util.List;
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

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;

public class JarConverter extends M3Converter {
  private final int CLASSE = 0;
  private final int METHODE = 1;
  private final int FIELDE = 2;

  private String jarFile;
  private String ClassFile;
  private String LogPath;
  private String classScheme;
  private String className;
  private boolean classIsEnum;

  JarConverter(LimitedTypeStore typeStore, Map<String, ISourceLocation> cache) {
    super(typeStore, cache);
  }

  private String extractJarName(ISourceLocation jarLoc) {
    String tmp = jarLoc.getPath().substring(0, jarLoc.getPath().indexOf("!"));
    return tmp.substring(tmp.lastIndexOf("/") + 1);
  }

  private String extractClassName(ISourceLocation jarLoc) {
    return jarLoc.getPath().substring(jarLoc.getPath().indexOf("!") + 1);
  }

  @SuppressWarnings("unchecked")
  public void convert(ISourceLocation jarLoc) {

      this.loc = jarLoc;
    this.jarFile = extractJarName(jarLoc);
    this.ClassFile = extractClassName(jarLoc);
    this.LogPath = this.ClassFile.replace(".class", "");
    String packageName;

    // System.out.println(this.ClassFile);
    packageName = LogPath.substring(0, LogPath.lastIndexOf("/"));
    if (this.LogPath.contains("$")) {
      this.LogPath = LogPath.replace("$", "/");
    }
    try {
      ClassReader cr = new ClassReader(URIResolverRegistry.getInstance().getInputStream(jarLoc));
      ClassNode cn = new ClassNode();

      cr.accept(cn, ClassReader.SKIP_DEBUG);

      this.className = cn.name.replace("$", "/");
      classIsEnum = false;
      if ((cn.access & Opcodes.ACC_INTERFACE) != 0)
        classScheme = "java+interface";
      else if ((cn.access & Opcodes.ACC_ENUM) != 0) {
        classScheme = "java+enum";
        classIsEnum = true;
      }
      else
        this.classScheme = "java+class";

      this.insert(this.containment, values.sourceLocation(classScheme, "", "/" + className),
          values.sourceLocation("java+compilationUnit", "", "/jar:///" + jarFile));
      this.insert(this.containment, values.sourceLocation("java+package", "", "/" + packageName),
          values.sourceLocation("java+compilationUnit", "", "/jar:///" + jarFile));
      this.insert(this.containment, values.sourceLocation("java+compilationUnit", "", "/jar:///" + jarFile),
          values.sourceLocation("java+class", "", "/" + LogPath));

      // <|java+package:///Main|,|java+compilationUnit:///src/Main/BaseInt.java|>,

      this.insert(this.declarations, values.sourceLocation(classScheme, "", "/" + className),
          values.sourceLocation(jarFile + "!" + ClassFile));

      if (cn.superName != null
          && !(cn.superName.equalsIgnoreCase("java/lang/Object") || cn.superName.equalsIgnoreCase("java/lang/Enum"))) {
        this.insert(this.extendsRelations, values.sourceLocation(classScheme, "", "/" + className),
            values.sourceLocation(classScheme, "", cn.superName));
      }

      for (int fs = 0; fs < 15; fs++) {
        if ((cn.access & (0x0001 << fs)) != 0) {
          IConstructor cons = mapFieldAccesCode(0x0001 << fs, CLASSE);
          if (cons != null)
            this.insert(this.modifiers, values.sourceLocation(classScheme, "", "/" + className), cons);
        }
      }

      // Deprecated method emit type annotation dependency Deprecated.
      if ((cn.access & 0x20000) == 0x20000)
        this.insert(this.annotations, values.sourceLocation(classScheme, "", "/" + className),
            values.sourceLocation("java+interface", "", "/java/lang/Deprecated"));



      // @implements={<|java+class:///m3startv2/viaInterface|,|java+interface:///m3startv2/m3Interface|>},
      for (int i = 0; i < cn.interfaces.size(); ++i) {
        String iface = (String) cn.interfaces.get(i);
        // System.out.println(iface);
        this.insert(this.implementsRelations, values.sourceLocation(classScheme, "", "/" + className),
            values.sourceLocation("java+interface", "", "/" + iface));
      }

      for (int fs = 0; fs < cn.innerClasses.size(); fs++) {
        InnerClassNode a = (InnerClassNode) cn.innerClasses.get(fs);
        String parsedName = a.name.replace("$", "/");
        this.insert(this.containment, values.sourceLocation(classScheme, "", "/" + className),
            values.sourceLocation(classScheme, "", "/" + parsedName));
      }


      emitMethods(cn.methods);
      emitFields(cn.fields);

    }
    catch (IOException e) {
      e.printStackTrace();
    }
    catch (URISyntaxException e) {
      throw new RuntimeException("Should not happen", e);
    }
  }

  private void emitMethods(List<MethodNode> methods) {
    try {
      for (int i = 0; i < methods.size(); ++i) {
        MethodNode method = methods.get(i);
        // System.out.println(new String("Signature :") + className + " " + method.name + " " +
        // method.signature + "  " + method.desc);

        if (classIsEnum && (method.name.equalsIgnoreCase("values") || method.name.equalsIgnoreCase("valueOf"))) {
          continue;
        }

        if (method.name.contains("<")) {
          String name = LogPath.substring(LogPath.lastIndexOf("/"));
          insertDeclMethod("java+constructor", method.signature, eliminateOutterClass(method.desc), name, method.access);
        }
        else {
          insertDeclMethod("java+method", method.signature, method.desc, method.name, method.access);
        }
      }

    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void insertDeclMethod(String type, String signature, String desc, String name, int access)
      throws URISyntaxException {
    String sig;
    if (signature != null) {
      sig = extractSignature(signature);
      // TypeVariables
      SignatureReader sr = new SignatureReader(signature);
      sr.accept(new SigVisitor(Opcodes.ASM4));
    }
    else {
      sig = extractSignature(desc);
    }
    // Typedepency methods
    String TypeSig = sig;
    // Loop over all parameters in the signature
    String[] params;

    if ((TypeSig != null) && (!TypeSig.equals(""))) {
      params = TypeSig.split(",");
      for (int i = 0; i < params.length; i++) {
        this.insert(this.typeDependency,
            values.sourceLocation("java+parameter", "", LogPath + "/" + name + "(" + sig + ")" + "/" + params[i] + i),
            values.sourceLocation(printParameterType(params[i]), "", params[i]));
      }
    }

    // Return type
    if (type.equals("java+constructor")) {
      this.insert(this.typeDependency,
          values.sourceLocation("java+constructor", "", LogPath + "/" + name + "(" + sig + ")"),
          values.sourceLocation("java+class", "", LogPath));
    }
    else {
      String rType = null;
      if (signature != null) {
        rType = Signature.toString(signature);
      }
      else {
        rType = Signature.toString(desc);
      }
      rType = rType.substring(0, rType.indexOf(' '));
      this.insert(this.typeDependency,
          values.sourceLocation("java+method", "", LogPath + "/" + name + "(" + sig + ")"),
          values.sourceLocation(printParameterType(rType), "", rType));
    }

    this.insert(this.declarations, values.sourceLocation(type, "", LogPath + "/" + name + "(" + sig + ")"),
        values.sourceLocation(jarFile + "!" + ClassFile));
    for (int fs = 0; fs < 15; fs++) {
      if ((access & (0x0001 << fs)) != 0) {
        this.insert(this.modifiers, values.sourceLocation(type, "", LogPath + "/" + name + "(" + sig + ")"),
            mapFieldAccesCode(0x0001 << fs, METHODE));
      }
    }

    // Containment of methods.
    this.insert(this.containment, values.sourceLocation(classScheme, "", LogPath),
        values.sourceLocation(type, "", LogPath + "/" + name + "(" + sig + ")"));


    // Deprecated method emit type annotation dependency Deprecated.
    if ((access & 0x20000) == 0x20000)
      this.insert(this.annotations, values.sourceLocation("java+method", "", LogPath + "/" + name + "(" + sig + ")"),
          values.sourceLocation("java+interface", "", "/java/lang/Deprecated"));
    // <|java+method:///Main/Main/FindMe(java.lang.String)|,|java+interface:///java/lang/Deprecated|>,

  }

  private String eliminateOutterClass(String desc) {
    // Find the end of the first class argument
    int semi = desc.indexOf(';');
    String outter = null;

    // Create the possible path
    if (semi > 0) {
      outter = desc.substring(desc.indexOf('(') + 2, semi) + "$";
    }

    // if the first argument is contained in the class path, remove it
    if ((outter != null) && ClassFile.contains(outter))
      return "(" + desc.substring(semi + 1);
    else
      return desc;
  }

  private String printParameterType(String t) {
    if (t != null) {
      switch (t) {
        case "void":
        case "boolean":
        case "char":
        case "byte":
        case "short":
        case "int":
        case "float":
        case "long":
        case "double":
          return "java+primitiveType";
        default:
          return "java+class";
      }
    }
    throw new RuntimeException("This should not happen, because i know everything");
  }

  private String extractSignature(String sig) {
    String args = Signature.toString(sig);
    args = args.substring(args.indexOf("(") + 1, args.indexOf(")"));
    args = args.replaceAll("\\s+", "");
    args = args.replaceAll("/", ".");
    return args;
  }

  private IConstructor mapFieldAccesCode(int code, int where) {
    // Check the original M3 implementation for possible IConstructor types.
    switch (code) {
      case Opcodes.ACC_PUBLIC:
        return constructModifierNode("public");
      case Opcodes.ACC_PRIVATE:
        return constructModifierNode("private");
      case Opcodes.ACC_PROTECTED:
        return constructModifierNode("protected");
      case Opcodes.ACC_STATIC:
        return constructModifierNode("static");
      case Opcodes.ACC_FINAL:
        return constructModifierNode("final");
      case Opcodes.ACC_SYNCHRONIZED:
        if (where == CLASSE)
          return null;
        return constructModifierNode("synchronized");
      case Opcodes.ACC_ABSTRACT:
        return constructModifierNode("abstract");
      case Opcodes.ACC_VOLATILE:
        return constructModifierNode("volatile");
      case Opcodes.ACC_TRANSIENT:
        return constructModifierNode("transient");
      case Opcodes.ACC_NATIVE:
        return constructModifierNode("native");


        // TODO: GIT PULL/MERGE ORIGINAL RASCAL VERSION < 2013-11-30 (Shahin commit)
        // case Opcodes.ACC_DEPRECATED:
        // return constructModifierNode("deprecated");


      default:
        return null;
    }
  }

  // <|java+field:///m3startv2/Main/intField|,|project://m3startv2/src/m3startv2/Main.java|(54,13,<5,12>,<5,25>)>,
  private void emitFields(List<FieldNode> fields) {
    try {
      for (int i = 0; i < fields.size(); ++i) {
        FieldNode field = fields.get(i);

        if ((field.access & Opcodes.ACC_SYNTHETIC) != 0)
          continue;

        if (field.name.startsWith("this$")) {
          if ((field.desc.length() > 0)
              && (className.contains(field.desc.substring(1, field.desc.length() - 1).replace('$', '/') + "/")))

            break;
        }

        boolean isEnum = (field.access & Opcodes.ACC_ENUM) != 0;
        String fieldScheme = isEnum ? "java+enumConstant" : "java+field";

        // System.out.println("Debug......." + field.name);
        this.insert(this.declarations, values.sourceLocation(fieldScheme, "", LogPath + "/" + field.name),
            values.sourceLocation(jarFile + "!" + ClassFile));

        // Containment of fields.
        this.insert(this.containment, values.sourceLocation(classScheme, "", LogPath),
            values.sourceLocation(fieldScheme, "", LogPath + "/" + field.name));

        if (!isEnum) {
          // The jvm acces codes specify 15 different modifiers (more then in the Java language
          // itself)
          for (int fs = 0; fs < 15; fs++) {
            if ((field.access & (0x0001 << fs)) != 0) {
              this.insert(this.modifiers, values.sourceLocation("java+field", "", LogPath + "/" + field.name),
                  mapFieldAccesCode(1 << fs, FIELDE));
            }
          }
        }
        // Put deprecated field in the annotations anno.
        if ((field.access & 0x20000) == 0x20000)
          this.insert(this.annotations, values.sourceLocation("java+field", "", LogPath + "/" + field.name),
              values.sourceLocation("java+interface", "", "/java/lang/Deprecated"));
        // <|java+method:///Main/Main/FindMe(java.lang.String)|,|java+interface:///java/lang/Deprecated|>,

      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private class SigVisitor extends SignatureVisitor {

    public SigVisitor(int api) {
      super(api);
      // TODO Auto-generated constructor stub
    }

    public void visitFormalTypeParameter(String name) {
      try {
        // System.out.println(name);
        JarConverter.this.insert(JarConverter.this.declarations,
            values.sourceLocation("java+typeVariable", "", LogPath + "/" + name),
            values.sourceLocation(jarFile + "!" + ClassFile));
      }
      catch (URISyntaxException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    public void visitBaseType(char descriptor) {
      // System.out.println(descriptor);
    }

  }
}
