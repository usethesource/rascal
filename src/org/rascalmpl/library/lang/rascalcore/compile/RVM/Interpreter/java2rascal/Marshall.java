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

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;

/**
 * Marshall incoming Java values to IValues
 * Unmarshall returned IValues to a Java value
 *
 */
public class Marshall {
  public static IValue marshall(IValueFactory vf, Object object) {
    if (object instanceof Boolean) {
      return vf.bool((Boolean) object);
    }
    if (object instanceof Integer) {
      return vf.integer((Integer) object);
    }
    else if (object instanceof String) {
      return vf.string((String) object);
    }
    else if (object instanceof Float) {
      return vf.real((Float) object);
    }
    else if (object instanceof Double) {
      return vf.real((Double) object);
    }
    else if (object instanceof IValue) {
      return (IValue) object;
    }

    throw new IllegalArgumentException("parameter is not convertible to IValue:" + object.getClass().getCanonicalName());
  }
  
  public static Object unmarshall(IValue v, Class<?> returnType) {
    if(v == null){
        return null; // a void result
    }
    Type t = v.getType();
    if (t.isBool() && returnType.getName().equals("boolean")) {
      return ((IBool) v).getValue();
    }
    if (t.isInteger() && returnType.getName().equals("int")) {
      return ((IInteger) v).intValue();
    }
    else if (t.isString() && returnType.getName().equals("String")) {
      return ((IString) v).getValue();
    }
    else if (t.isReal() && returnType.getName().equals("Float")) {
      return ((IReal) v).floatValue();
    }
    else if (t.isReal() && returnType.getName().equals("Double")) {
      return ((IReal) v).doubleValue();
    }
    else {
      return v;
    }
  }
}
