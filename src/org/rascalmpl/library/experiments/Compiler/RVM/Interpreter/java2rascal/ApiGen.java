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
import java.util.HashMap;

import org.rascalmpl.interpreter.types.DefaultRascalTypeVisitor;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.RascalType;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CompilerError;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.values.ValueFactoryFactory;

public class ApiGen {

  public static String generate(RVMExecutable rvmExec, String moduleName, String javaPackage) throws Exception{
    StringWriter sw = new StringWriter();
    
    int b = moduleName.lastIndexOf("::");
    String baseName = b < 0 ? moduleName : moduleName.substring(b + 2);
    
    String packageContrib = (b < 0 ? "" :  "." + moduleName.substring(0, b)).replaceAll("::", ".");
    sw.append("package " + javaPackage + packageContrib + ";\n\n");
    
    sw.append("import org.rascalmpl.value.*;\n\n");    
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
      
      if(fun.ftype instanceof FunctionType && !funName.endsWith("companion-defaults") && (funName.contains(moduleName) || funName.endsWith("companion"))){
        
        FunctionType ftype = (FunctionType) fun.ftype;
        
        StringWriter sw2 = new StringWriter();
        sw2.append("\t");
        sw2.append(toJavaType(ftype.getReturnType()));
        sw2.append(" ");
        sw2.append(fun.getPrintableName());
        sw2.append("(");
        
        Type argTypes;
        boolean isConstructor = false;
        
        int k = funName.lastIndexOf("::companion");
        if(k >  0){ 
          isConstructor = true;
          String consName = funName.substring(0, k);
          int cn = rvmExec.getConstructorMap().get(consName);
          Type consType =  rvmExec.getConstructorStore().get(cn);
          argTypes = consType.getFieldTypes();
        } else {
          argTypes = ftype.getArgumentTypes();
         
        }
        
        int arity = argTypes.getArity();
        
        IMap localNames = fun.localNames;
        IValueFactory vf = ValueFactoryFactory.getValueFactory();
 
        for(int i = 0; i < arity; i++){
          if(i > 0){
            sw2.append(", ");
          }
        
          sw2.append(toJavaType(argTypes.getFieldType(i)));
          sw2.append(" ");
          if(isConstructor){
            sw2.append(argTypes.getFieldName(i));
          } else {
            IValue argName = localNames.get(vf.integer(i + arity + 2));
            sw2.append(argName == null ? "arg" + i : ((IString)argName).getValue());
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
    for(String line : lines){
      sw.append(line);
    }
    sw.append(kwclasses);
    sw.append("}\n");
    
    return sw.toString();
  }
  
  static String KWClass(String funName, Type kwType) throws Exception{
    StringWriter sw = new StringWriter();
    String className = "KW" + funName;
    sw.append("\n\t@RascalKeywordParameters\n")
      .append("\tinterface ").append(className).append(" {\n");
    
    for(int i = 0; i < kwType.getArity(); i++){
      String fieldName = kwType.getFieldName(i);
      Type fieldType = kwType.getFieldType(i);
      sw.append("\t\t").append(className).append(" ").append(fieldName).append("(")
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
          throw new CompilerError("Alias cannot occur as interface type");
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
          return "/*" + type.getName() + "*/ IConstructor";
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

        throw new CompilerError("External cannot occur as interface type: " + type);
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
        throw new CompilerError("External cannot occur as interface type: " + type);
      }

      @Override
      public String visitDateTime(Type type)
              throws RuntimeException {
          return "IDateTime";
      }});
  }
}
