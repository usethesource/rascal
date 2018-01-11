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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;

import io.usethesource.vallang.IValue;

/**
 * Handle the invocation of Rascal functions from Java
 *
 */
public class RascalFunctionInvocationHandler implements InvocationHandler {
  private final RVMCore core;
  private Map<String,IValue> emptyKWParams;
  private Map<Method,OverloadedFunction> overloadedFunctionsByMethod;

  public RascalFunctionInvocationHandler(RVMCore core, Class<?> clazz) {
    assert clazz.isAnnotationPresent(RascalModule.class);
    this.core = core;
    emptyKWParams = new HashMap<>();
    overloadedFunctionsByMethod = new HashMap<>();
  }
  
  boolean hasKeywordParams(Object[] args){
    // All normal arguments are IValues or one of the supported marshalled primitive types, 
    // the last parameter could a keyword parameters proxy object.
    return args != null 
        && args.length > 0 
        && Proxy.isProxyClass(args[args.length - 1].getClass());
  }
  
  OverloadedFunction getOverloadedFunction(Method method, Object[] args) throws NoSuchRascalFunction{
    OverloadedFunction f = overloadedFunctionsByMethod.get(method);
    if(f != null){
      return f;
    }

    f = core.getOverloadedFunctionByNameAndArity(method.getName(), method.getParameterCount() - (hasKeywordParams(args) ? 1 : 0));
    overloadedFunctionsByMethod.put(method,  f);
    return f;
  }
  
  Map<String,IValue> getKWArgs(Object kwArgs) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
    return ((Java2RascalKWProxy.ProxyInvocationHandler) Proxy.getInvocationHandler(kwArgs)).asMap();
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if(method.getName().equals("shutdown")){
      core.shutdown();
      return null;
    }
    if(method.getName().equals("setFrameObserver")){
        core.setFrameObserver((IFrameObserver) args[0]); 
        return null;
    }
    try {
        core.increaseActivationDepth();
        Class<?> returnType = method.getReturnType();
        if(returnType.isAnnotationPresent(RascalKeywordParameters.class)){
            return returnType.cast(Proxy.newProxyInstance(RVMCore.class.getClassLoader(), new Class<?> [] { returnType }, new Java2RascalKWProxy.ProxyInvocationHandler(core.vf, returnType)));
        }
        OverloadedFunction f = getOverloadedFunction(method, args);
        boolean hka = hasKeywordParams(args);
        Map<String,IValue> kwArgs = hka ? getKWArgs(args[args.length - 1]) : emptyKWParams;
        return  Marshall.unmarshall(core.executeRVMFunction(f, marshallArgs(args, hka), kwArgs), method.getReturnType());
    } finally {
        core.decreaseActivationDepth();
    }
  }

  private IValue[] marshallArgs(Object[] args, boolean skipKeywordArguments) {
    if(args == null || args.length == 0){
      return new IValue[0];
    }
    int len = args.length - (skipKeywordArguments ? 1 : 0);
    IValue[] result = new IValue[len];

    for (int i = 0; i < len; i++) {
      result[i] = Marshall.marshall(core.vf, args[i]);
    }

    return result;
  }
}