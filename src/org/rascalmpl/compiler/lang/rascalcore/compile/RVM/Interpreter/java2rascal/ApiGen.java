/** 
 * Copyright (c) 2016, paulklint, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.rascalmpl.interpreter.types.DefaultRascalTypeVisitor;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.RascalType;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.InternalCompilerError;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import org.rascalmpl.values.ValueFactoryFactory;

public class ApiGen {

  static final String keywords[] = { "abstract", "assert", "boolean",
      "break", "byte", "case", "catch", "char", "class", "const",
      "continue", "default", "do", "double", "else", "extends", "false",
      "final", "finally", "float", "for", "goto", "if", "implements",
      "import", "instanceof", "int", "interface", "long", "native",
      "new", "null", "package", "private", "protected", "public",
      "return", "short", "static", "strictfp", "super", "switch",
      "synchronized", "this", "throw", "throws", "transient", "true",
      "try", "void", "volatile", "while" };

  public static boolean isJavaKeyword(String keyword) {
    return (Arrays.binarySearch(keywords, keyword) >= 0);
  }
  
  public static String escapeJavaKeyword(String name){
    return isJavaKeyword(name) ? name + "$" : name.replaceAll("-",  "_");
  }

  public static String generate(RVMExecutable rvmExec, String moduleName, String javaPackage) throws Exception{
    StringWriter sw = new StringWriter();
    
    int b = moduleName.lastIndexOf("::");
    String baseName = b < 0 ? moduleName : moduleName.substring(b + 2);
    
    String packageContrib = (b < 0 ? "" :  "." + moduleName.substring(0, b)).replaceAll("::", ".");
    sw.append("package " + javaPackage + packageContrib + ";\n\n");
    
    sw.append("import io.usethesource.vallang.*;\n\n");
    sw.append("import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.RascalKeywordParameters;\n");
    sw.append("import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.RascalModule;\n\n");

    sw.append("/* Automatically generated code; do not change */\n\n");
    sw.append("@RascalModule(\"" + moduleName + "\")\n");
    
    sw.append("public interface I").append(baseName ).append(" {\n");
    ArrayList<String> lines = new ArrayList<>();
    
    String kwclasses = "";
    
    HashMap<Type, String> generatedKWClasses = new HashMap<>();
  
    for(Function fun : rvmExec.getFunctionStore()){
      String funName = fun.getName();
      System.err.println(funName);
      
      if(fun.ftype instanceof FunctionType 
         && !funName.endsWith("companion-defaults") 
         && !funName.contains("closure#") 
         && (funName.contains(moduleName) || funName.endsWith("companion"))){
        
        FunctionType ftype = (FunctionType) fun.ftype;
        Type returnType;
        Type argTypes;
        boolean isConstructor = false;
        
        int k = funName.lastIndexOf("::companion");
        if(k >  0){ 
          isConstructor = true;
          String consName = funName.substring(0, k);
          int cn = rvmExec.getConstructorMap().get(consName);
          Type consType =  rvmExec.getConstructorStore()[cn];
          returnType = consType.getAbstractDataType();
          argTypes = consType.getFieldTypes();
        } else {
          returnType = ftype.getReturnType();
          argTypes = ftype.getArgumentTypes();
        }
        
        int arity = argTypes.getArity();
        
        StringWriter sw2 = new StringWriter();
        sw2.append("\t");
        sw2.append(toJavaType(returnType));
        sw2.append(" ");
        if(isConstructor){
          sw2.append(removeTypeParams(returnType)).append("_");
        }
        sw2.append(escapeJavaKeyword(fun.getPrintableName()));
        sw2.append("(");
        
        IMap localNames = fun.localNames;
        System.err.println("localNames: " + localNames);
        IValueFactory vf = ValueFactoryFactory.getValueFactory();
 
        for(int i = 0; i < arity; i++){
          if(i > 0){
            sw2.append(", ");
          }
        
          sw2.append(toJavaType(argTypes.getFieldType(i)));
          sw2.append(" ");
          if(isConstructor){
            sw2.append(escapeJavaKeyword(argTypes.getFieldName(i)));
          } else {
            IValue argName = localNames.get(vf.integer(i));
            sw2.append(argName == null ? "arg" + i : escapeJavaKeyword(((IString)argName).getValue()));
          }
        }
        Type kwType = fun.kwType;
        if(kwType.getArity() > 0){
          if(arity > 0){
            sw2.append(", ");
          }
          String gc = generatedKWClasses.get(kwType);
          if(gc == null){
            sw2.append("KW").append(fun.getPrintableName()).append(" kwArgs");
            kwclasses += KWClass(fun.getPrintableName(), kwType);
            generatedKWClasses.put(kwType, "KW" +  fun.getPrintableName());
          } else {
            sw2.append(gc).append(" kwArgs");
          }
        }
        
        sw2.append(");\n");
        String line = sw2.toString();
        if(!lines.contains(line)){
          lines.add(line);
        }
      }
    }
    for(Type consType :rvmExec.getConstructorStore()){
      System.err.println(consType);
      Type argTypes = consType.getFieldTypes();
      int arity = argTypes.getArity();
      
      StringWriter sw3 = new StringWriter();
      sw3.append("\t");
      sw3.append(escapeJavaKeyword(toJavaType(consType.getAbstractDataType())));
      sw3.append(" ");
      sw3.append(removeTypeParams(consType.getAbstractDataType())).append("_").append(escapeJavaKeyword(consType.getName()));
      sw3.append("(");
      
      for(int i = 0; i < arity; i++){
        if(i > 0){
          sw3.append(", ");
        }
      
        sw3.append(toJavaType(argTypes.getFieldType(i)));
        sw3.append(" ");
        sw3.append(escapeJavaKeyword(argTypes.getFieldName(i)));
      }
      sw3.append(");\n");
      String line = sw3.toString();
      if(!lines.contains(line)){
        lines.add(line);
      }
    }
    for(String line : lines){
      sw.append(line);
    }
    sw.append(kwclasses);
    sw.append("}\n");
    
    return sw.toString();
  }
  
  static String removeTypeParams(Type t){
    String rt = t.toString();
    int j = rt.indexOf("[");
    if(j >= 0){
      rt = rt.substring(0, j);
    }
    return rt;
  }
  
  static String KWClass(String funName, Type kwType) throws Exception{
    StringWriter sw = new StringWriter();
    String className = "KW" + funName;
    sw.append("\n\t@RascalKeywordParameters\n")
      .append("\tinterface ").append(className).append(" {\n");
    
    for(int i = 0; i < kwType.getArity(); i++){
      String fieldName = kwType.getFieldName(i);
      Type fieldType = kwType.getFieldType(i);
      sw.append("\t\t").append(className).append(" ").append(escapeJavaKeyword(fieldName)).append("(")
        .append(toJavaType(fieldType)).append(" ").append("val);\n");
    }
    sw.append("\t}\n");
    sw.append("\t").append(className).append(" kw_").append(funName).append("();\n");
    return sw.toString();
  }
  
  static String toJavaType(Type t) throws Exception{
    return t.accept(new DefaultRascalTypeVisitor<String,RuntimeException>("") {

      @Override
      public String visitReal(Type type) throws RuntimeException {
          return "double";
      }

      @Override
      public String visitInteger(Type type) throws RuntimeException {
          return "int";
      }

      @Override
      public String visitRational(Type type)
              throws RuntimeException {
          return "IRational";
      }

      @Override
      public String visitList(Type type) throws RuntimeException {
          return "IList";
      }

      @Override
      public String visitMap(Type type) throws RuntimeException {
          return "IMap";
      }

      @Override
      public String visitNumber(Type type) throws RuntimeException {
          return "INumber";
      }

      @Override
      public String visitAlias(Type type) throws RuntimeException {
          throw new InternalCompilerError("Alias cannot occur as interface type");
      }

      @Override
      public String visitSet(Type type) throws RuntimeException {
          return "ISet";
      }

      @Override
      public String visitSourceLocation(Type type)
              throws RuntimeException {
          return "ISourceLocation";
      }

      @Override
      public String visitString(Type type) throws RuntimeException {
          return "String";
      }

      @Override
      public String visitNode(Type type) throws RuntimeException {
          return "INode";
      }

      @Override
      public String visitConstructor(Type type)
              throws RuntimeException {
          return "IConstructor";
      }

      @Override
      public String visitAbstractData(Type type)
              throws RuntimeException {
          return "IConstructor";
      }

      @Override
      public String visitTuple(Type type) throws RuntimeException {
          return "ITuple";
      }

      @Override
      public String visitValue(Type type) throws RuntimeException {
          return "IValue";
      }

      @Override
      public String visitVoid(Type type) throws RuntimeException  {
          return "void";
      }

      @Override
      public String visitBool(Type type) throws RuntimeException {
          return "boolean";
      }

      @Override
      public String visitParameter(Type type)
              throws RuntimeException {
        return "IValue";
      }

      @Override
      public String visitExternal(Type type)
          throws RuntimeException {
        if(type instanceof FunctionType){
          return "IValue";
        } else if(type instanceof ReifiedType){
          return "IConstructor";
        } else if(type instanceof NonTerminalType){
          return "IConstructor";
        }

        throw new InternalCompilerError("External cannot occur as interface type: " + type);
      }
      
      @Override
      public String visitNonTerminal(RascalType type)
              throws RuntimeException {
          return "IConstructor";
      }
      
      @Override
      public String visitReified(RascalType type)
              throws RuntimeException {
          return "IConstructor";
      }
      
      @Override
      public String visitFunction(RascalType type)
              throws RuntimeException {
          return "IValue";
      }
      
      @Override
      public String visitOverloadedFunction(RascalType type)
              throws RuntimeException {
        throw new InternalCompilerError("External cannot occur as interface type: " + type);
      }

      @Override
      public String visitDateTime(Type type)
              throws RuntimeException {
          return "IDateTime";
      }});
  }
}
