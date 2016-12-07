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

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValueFactory;

/**
 * Top level controller for the Java -> Rascal interfacing. It uses a builder to create the actual bridge
 * and set options for Rascal execution. All Rascal functions, constructors and keyword interfaces
 * are part of a (generated) interface RascalInterfaceModule. All calls are made through this interface and
 * are type safe.
 * 
 * Example (for the generated interface ISampleFuns):
 * 
 * ISampleFuns sf = Java2Rascal.Builder.bridge(vf, new PathConfig(), ISampleFuns.class).setTrace().build();
 */
public class Java2Rascal<RascalInterfaceModule> {
  private final IValueFactory vf;
  private final PathConfig pcfg;
  private final Class<RascalInterfaceModule> interface2Rascal;
  private boolean trace = false;
  private boolean profile = false;
  private boolean verbose = false;
  private boolean jvm = true;
  
  private Java2Rascal(Builder<RascalInterfaceModule> b) {
    this.vf = b.vf;
    this.pcfg = b.pcfg;
    this.interface2Rascal = b.interface2Rascal;
   }
  
  public static class Builder<IM2> {
    IValueFactory vf;
    PathConfig pcfg;
    Class<IM2> interface2Rascal;
    boolean trace = false;
    boolean profile = false;
    boolean verbose = false;
    boolean jvm = true;

    static public <IM3> Builder<IM3> bridge(IValueFactory vf, PathConfig pcfg, Class<IM3> interface2Rascal) { 
      return new Builder<IM3>(vf, pcfg, interface2Rascal); 
    }

    private Builder(IValueFactory vf, PathConfig pcfg, Class<IM2> interface2Rascal){
      this.vf = vf;
      this.pcfg = pcfg;
      this.interface2Rascal = interface2Rascal;
    }

    public Builder<IM2> profile(boolean profile){
      this.profile = profile;
      return this;
    }

    public Builder<IM2> trace(boolean trace){
      this.trace = trace;
      return this;
    }
    public Builder<IM2> verbose(boolean verbose){
      this.verbose = verbose;
      return this;
    }
    
    public Builder<IM2> jvm(boolean jvm){
      this.jvm = jvm;
      return this;
    }

    public IM2 build() throws IOException{
      return new Java2Rascal<IM2>(this).makeBridge();
    }
  }

  private RascalInterfaceModule makeBridge() throws IOException{
    if(trace && profile){
      throw new RuntimeException("Either 'trace' or 'profile' can be set, not both");
    }
    RascalExecutionContext rex = 
        RascalExecutionContextBuilder.normalContext(vf, pcfg.getBoot(), System.out, System.err)
            .setTrace(trace)
            .setProfile(profile)
            .setVerbose(verbose)
            .setJVM(jvm)
            .build();
    ISourceLocation bootDir = pcfg.getBoot();
    String moduleName = null;
    for(Annotation annotation : interface2Rascal.getAnnotations()){
      if(annotation instanceof RascalModule){
        moduleName = ((RascalModule) annotation).value();
        break;
      }
    }
    if(moduleName == null){
      throw new RuntimeException("RascalModule annotation required for interface2Rascal class");
    }
    
    String modulePath = "/" + moduleName.replaceAll("::", "/") + ".rvm.ser.gz";
    
    RVMCore rvm = ExecutionTools.initializedRVM(URIUtil.correctLocation("compressed+" + bootDir.getScheme(), "", bootDir.getPath() + modulePath), rex);

    return (RascalInterfaceModule) rvm.asInterface(interface2Rascal);
  }
  
}
