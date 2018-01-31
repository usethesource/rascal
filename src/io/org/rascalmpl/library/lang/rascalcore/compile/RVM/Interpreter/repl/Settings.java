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
package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.rascalmpl.library.util.PathConfig;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import org.rascalmpl.values.ValueFactoryFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Settings {

  JsonObject jobject;
  IValueFactory vf;

  Settings (boolean useSettings) {
      vf = ValueFactoryFactory.getValueFactory();
      Path cwd = Paths.get(System.getProperty("user.home"));
      Path settings = cwd.resolve("settings.json");
      JsonElement jsettings;
      if(useSettings){
          try {
              jsettings = new JsonParser().parse(new InputStreamReader(Files.newInputStream(settings)));
              jobject = jsettings.getAsJsonObject();
              return;
          } catch (JsonSyntaxException | IOException e) {
              System.err.println("Could not read settings, using defaults");
          }
      } else {
          System.err.println("Using default settings");
      }
      jsettings = new JsonParser().parse("{}");
      jobject = jsettings.getAsJsonObject();
  }

  boolean getBool(String accessor, boolean def){
    String names[] = accessor.split("\\.");
    JsonElement current = jobject;
    for(int i = 0; i < names.length; i++){
      if(i == names.length - 1){
        JsonElement result = current.getAsJsonObject().get(names[i]);
        return result == null ? def : result.getAsBoolean();
      }
      current  = current.getAsJsonObject().get(names[i]);
      if(current == null){
        return def;
      }
    }
    return def;
  }

  int getInt(String accessor, int def){
    String names[] = accessor.split("\\.");
    JsonElement current = jobject;
    for(int i = 0; i < names.length; i++){
      if(i == names.length - 1){
        JsonElement result = current.getAsJsonObject().get(names[i]);
        return result == null ? def : result.getAsInt();
      }
      current  = current.getAsJsonObject().get(names[i]);
      if(current == null){
        return def;
      }
    }
    return def;
  }

  String getInt(String accessor, String def){
    String names[] = accessor.split("\\.");
    JsonElement current = jobject;
    for(int i = 0; i < names.length; i++){
      if(i == names.length - 1){
        JsonElement result = current.getAsJsonObject().get(names[i]);
        return result == null ? def : result.getAsString();
      }
      current  = current.getAsJsonObject().get(names[i]);
      if(current == null){
        return def;
      }
    }
    return def;
  }

  ISourceLocation getLoc(String accessor, ISourceLocation def){
    String names[] = accessor.split("\\.");
    JsonElement current = jobject;
    for(int i = 0; i < names.length; i++){
      if(i == names.length - 1){
        JsonElement result = current.getAsJsonObject().get(names[i]);
        if(result == null){
          return def;
        }
        try {
          return vf.sourceLocation("file", "", result.getAsString());
        } catch (FactTypeUseException | URISyntaxException e) {
          e.printStackTrace();
        }
      }
      current  = current.getAsJsonObject().get(names[i]);
      if(current == null){
        return def;
      }
    }
    return def;
  }

  IList getLocs(String accessor, IList def){
    String names[] = accessor.split("\\.");
    JsonElement current = jobject;
    for(int i = 0; i < names.length; i++){
      if(i == names.length - 1){
        JsonArray locs = current.getAsJsonObject().getAsJsonArray(names[i]);
        if(locs == null){
          return def;
        }
        IListWriter w = vf.listWriter();
        for(JsonElement loc : locs){
          try {
            w.append(vf.sourceLocation("file", "", loc.getAsString()));
          } catch (FactTypeUseException | URISyntaxException e) {
            e.printStackTrace();
          }
        }
        return w.done();
      }
      current  = current.getAsJsonObject().get(names[i]);
      if(current == null){
        return def;
      }
    }
    return def;
  }

  PathConfig getPathConfig(PathConfig given) throws IOException {
      // TODO: this clone should be in PathConfig itself, because it co-evolves
    IList srcs = getLocs("srcs", given.getSrcs());
    IList libs = getLocs("libs", given.getLibs());
    ISourceLocation bin = getLoc("bin", given.getBin());
    ISourceLocation boot = getLoc("boot", given.getBoot());
    IList courses = getLocs("courses", given.getCourses());
    IList javaCompilerPath = getLocs("javaCompilerPath", given.getJavaCompilerPath());
    IList classloaders = getLocs("classloaders", given.getClassloaders());

    return new PathConfig(srcs, libs, bin, boot, courses, javaCompilerPath, classloaders);
  }

}
