/** 
 * Copyright (c) 2016, Jurgen J. Vinju, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.library.lang.json.io;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.ITypeVisitor;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

/**
 * This class streams a JSON stream directly to an IValue representation and validates the content
 * to a given type as declared in a given type store.
 * 
 * In general:
 *   - Objects translate to maps
 *   - Arrays translate to lists
 *   - Booleans translate to bools
 * 
 * But if the expected type is different than the general `value`, then mapping and validation is influenced as follows:
 *   - node will try and read an object {names and args} as a keyword parameter map to a node ""(kwparams)
 *   - adt/cons will objects using the property name of the parent object as constructor name, and property names as keyword field names
 *   - datetime can be mapped from ints and dateformat strings (see setCalenderFormat())  
 *   - rationals will read array tuples [nom, denom] or {nominator: non, deminator: denom} objects
 *   - sets will read arrays
 *   - lists will read arrays
 *   - tuples read arrays
 *   - maps read objects
 *   - relations read arrays of arrays
 *   - locations will read strings which contain :/ as URI strings (encoded) and strings which do not contain :/ as absolute file names,
 *     also locations will read objects { scheme : str, authority: str?, path: str?, fragment: str?, query: str?, offset: int, length: int, begin: [bl, bc], end: [el, ec]}
 *   - reals will read strings or doubles
 *   - ints will read strings or doubles
 *   - nums will read like ints      
 */
public class JsonValueReader {
  private static final TypeFactory TF = TypeFactory.getInstance();
  private final TypeStore store;
  private final IValueFactory vf;
  private ThreadLocal<SimpleDateFormat> format;
  
  /**
   * @param vf     factory which will be used to construct values
   * @param store  type store to lookup constructors of abstract data-types in and the types of keyword fields
   */
  public JsonValueReader(IValueFactory vf, TypeStore store) {
    this.vf = vf;
    this.store = store;
    setCalendarFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  }
  
  public JsonValueReader(IValueFactory vf) {
    this(vf, new TypeStore());
  }
  
  /**
   * Builder method to set the format to use for all date-time values encoded as strings
   */
  public JsonValueReader setCalendarFormat(String format) {
    // SimpleDateFormat is not thread safe, so here we make sure
    // we can use objects of this reader in different threads at the same time
    this.format = new ThreadLocal<SimpleDateFormat>() {
      protected SimpleDateFormat initialValue() {
        return new SimpleDateFormat(format);
      }
    };
    return this;
  }

  public IValue read(JsonReader in, Type expected) throws IOException {
    return read(in, expected, null);
  }
  
  /**
   * Read and validate a Json stream as an IValue
   * @param  in       json stream
   * @param  expected type to validate against (recursively)
   * @return an IValue of the expected type
   * @throws IOException when either a parse error or a validation error occurs
   */
  public IValue read(JsonReader in, Type expected, String label) throws IOException {
    return expected.accept(new ITypeVisitor<IValue, IOException>() {
      @Override
      public IValue visitInteger(Type type) throws IOException {
        checkNull();
        
        switch (in.peek()) {
          case NUMBER:
            return vf.integer(in.nextLong());
          case STRING:
            return vf.integer(in.nextString());
          default:
              throw new IOException("Expected integer but got " + in.peek());
        }
      }
      
      public IValue visitReal(Type type) throws IOException {
        checkNull();
        
        switch (in.peek()) {
          case NUMBER:
            return vf.real(in.nextInt());
          case STRING:
            return vf.real(in.nextString());
          default:
              throw new IOException("Expected integer but got " + in.peek());
        }
      }
      
      @Override
      public IValue visitExternal(Type type) throws IOException {
        throw new IOException("External type " + type + "is not implemented yet by the json reader:" + in.getPath());
      }
      
      @Override
      public IValue visitString(Type type) throws IOException {
        checkNull();
        return vf.string(in.nextString());
      }
      
      @Override
      public IValue visitTuple(Type type) throws IOException {
        checkNull();

        List<IValue> l = new ArrayList<>();
        in.beginArray();
        
        if (type.hasFieldNames()) {
          for (int i = 0; i < type.getArity(); i++) {
            l.add(read(in, type.getFieldType(i), type.getFieldName(i)));
          }
        }
        else {
          for (int i = 0; i < type.getArity(); i++) {
            l.add(read(in, type.getFieldType(i)));
          }
        }

        in.endArray();
        return vf.tuple(l.toArray(new IValue[l.size()]));
      }
      
      @Override
      public IValue visitVoid(Type type) throws IOException {
        throw new IOException("Can not read json values of type void: " + in.getPath());
      }
      
      @Override
      public IValue visitSourceLocation(Type type) throws IOException {
        switch (in.peek()) {
          case STRING:
            return sourceLocationString();
          case BEGIN_OBJECT:
            return sourceLocationObject();
            default:
              throw new IOException("Could not find string or source location object here: " + in.getPath());
        }
      }        
      
      
        
      private IValue sourceLocationObject() throws IOException {
        String scheme = null;
        String authority = null;
        String path = null;
        String fragment = "";
        String query = "";
        int offset = -1;
        int length = -1;
        int beginLine = -1;
        int endLine = -1;
        int beginColumn = -1;
        int endColumn = -1;
        
        while (in.hasNext());
        String name = in.nextName();
        switch (name) {
          case "scheme":
            scheme = in.nextString();
            break;
          case "authority":
            authority = in.nextString();
            break;
          case "path":
            path = in.nextString();
            break;
          case "fragment":
            fragment = in.nextString();
            break;
          case "query":
            query = in.nextString();
            break;
          case "offset":
            offset = in.nextInt();
            break;
          case "length":
            length = in.nextInt();
            break;
          case "begin":
            in.beginArray();
            beginLine = in.nextInt();
            beginColumn = in.nextInt();
            in.endArray();
            break;
          case "end":
            in.beginArray();
            endLine = in.nextInt();
            endColumn = in.nextInt();
            in.endArray();
            break;
          default:
            throw new IOException("unexpected property name " + name + " :" + in.getPath());
        }

        if (path != null && offset != -1 && length != -1 && beginLine != -1 && endLine != -1 && beginColumn != -1 && endColumn != -1) {
            return vf.sourceLocation(path, offset, length, beginLine, endLine, beginColumn, endColumn);
        }
        try {
            if (scheme != null && authority != null && query != null && fragment != null) {
                return vf.sourceLocation(scheme, authority, path, query, fragment);
            }
            if (scheme != null) {
                return vf.sourceLocation(scheme, authority == null ? "" : authority, path);
            }
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }

        if (path != null) {
            return vf.sourceLocation(path);
        }
        
        throw new IOException("Could not parse complete source location: " + in.getPath());
      }
      
      @Override
      public IValue visitValue(Type type) throws IOException {
        checkNull();
        
        switch (in.peek()) {
          case NUMBER:
            return visitInteger(TF.integerType());
          case STRING:
            return visitString(TF.stringType());
          case BEGIN_ARRAY:
            return visitList(TF.listType(TF.valueType()));
          case BEGIN_OBJECT:
            return visitNode(TF.nodeType());
          case BOOLEAN:
            return visitBool(TF.nodeType());
          case NAME:
            // this would be weird though
            return vf.string(in.nextName());
          default:
            throw new IOException("Did not expect end of Json value here, while looking for " + type + " + at " + in.getPath());
        }
      }
      
      private IValue sourceLocationString() throws IOException {  
          try {
            String val = in.nextString();
            
            if (val.contains(":/")) {
              return vf.sourceLocation(URIUtil.createFromEncoded(val));
            }
            else {
              return vf.sourceLocation(val);
            }
          } catch (URISyntaxException e) {
            throw new IOException("could not parse URI:" + in.getPath(), e);
          }
      }
      
      public IValue visitRational(Type type) throws IOException {
        checkNull();
        
        switch (in.peek()) {
          case BEGIN_OBJECT:
            in.beginObject();
            IInteger nomO = null, denomO = null;
            while (in.hasNext()) {
              switch (in.nextName()) {
                case "nominator":
                  nomO = (IInteger) read(in, TF.integerType());
                case "denominator":
                  denomO = (IInteger) read(in, TF.integerType());
              }
            }
            
            in.endObject();
            
            if (nomO == null || denomO == null) {
              throw new IOException("Did not find all fields of expected rational at " + in.getPath());
            }
            
            return vf.rational(nomO, denomO);
          case BEGIN_ARRAY:
            in.beginArray();
            IInteger nomA = (IInteger) read(in, TF.integerType());
            IInteger denomA = (IInteger) read(in, TF.integerType());
            in.endArray();
            return vf.rational(nomA, denomA);
          case STRING:
            return vf.rational(in.nextString());
          default:
              throw new IOException("Expected integer but got " + in.peek());
        }
      }
      
      @Override
      public IValue visitMap(Type type) throws IOException {
        checkNull();
        IMapWriter w = vf.mapWriter();
        
        switch (in.peek()) {
          case BEGIN_OBJECT:
            in.beginObject();
            if (!type.getKeyType().isString()) {
              throw new IOException("Can not read JSon object as a map if the key type of the map (" + type + ") is not a string at " + in.getPath());
            }
            
            in.beginObject();
            while (in.hasNext()) {
              w.put(vf.string(in.nextName()), read(in, type.getValueType()));
            }
            in.endObject();
            return w.done();
          case BEGIN_ARRAY:
            in.beginArray();
            in.beginArray();
            while (in.hasNext()) {
              in.beginArray();
              IValue key = read(in, type.getKeyType());
              IValue value = read(in, type.getValueType());
              w.put(key,value);
              in.endArray();
            }
            in.endArray();
            return w.done();
          default:
            throw new IOException("Expected a map encoded as an object or an nested array to match " + type);  
        }
      }
      
      @Override
      public IValue visitAlias(Type type) throws IOException {
        while (type.isAliased()) {
          type = type.getAliased();
        }
        
        return type.accept(this);
      }
      
      @Override
      public IValue visitBool(Type type) throws IOException {
        checkNull();
        return vf.bool(in.nextBoolean());
      }
      
      @Override
      public IValue visitAbstractData(Type type) throws IOException {
        String consName = label;
        
        if (consName == null) {
          // this should not happen often, so let's go and find the first constructor for this adt, slowly
          for (Type cons : store.getConstructors()) {
            if (cons.getAbstractDataType() == type) {
              consName = cons.getName();
              break;
            }
          }
          
          if (consName == null) {
            throw new IOException("need a context with a constructor name to parse constructors (i.e. a property name of the parent or a unique single constructor exists the given ADT): " + in.getPath());
          }
        }
        
        Type cons = checkNameCons(type, consName);
        IValue[] args = new IValue[cons.getArity()];
        Map<String,IValue> kwParams = new HashMap<>();
        
        if (!cons.hasFieldNames()) {
          throw new IOException("For the object encoding constructors must have field names " + in.getPath());
        }
        
        in.beginObject();
        
        while (in.hasNext()) {
          String label = in.nextName();
          if (cons.hasField(label)) {
            int index = cons.getFieldIndex(label);
            // here we use the field name of a child to match a constructor name of the child:
            args[index] = read(in, cons.getFieldType(index), label);
          }
          else {
            Type kwType = store.getKeywordParameterType(cons, label);
            if (kwType != null) {
              kwParams.put(label, read(in, kwType, label));
            }
            else {
              kwParams.put(label, read(in, TF.valueType(), label));
            }
          }
        }
        in.endObject();
        
        return vf.constructor(type, args, kwParams);
      }
      
      @Override
      public IValue visitConstructor(Type type) throws IOException {
        return read(in, type.getAbstractDataType(), type.getName());
      }
      
      @Override
      public IValue visitNode(Type type) throws IOException {
        in.beginObject();
        
        Map<String,IValue> kws = new HashMap<>();
        
        while (in.hasNext()) {
          String kwName = in.nextName();
          kws.put(kwName, read(in, TF.valueType(), kwName));
        }
        
        in.endObject();

        return vf.node(label == null ? "object" : label, new IValue[] { }, kws);
      }
      
      @Override
      public IValue visitNumber(Type type) throws IOException {
        return visitInteger(type);
      }
      
      @Override
      public IValue visitParameter(Type type) throws IOException {
        return type.getBound().accept(this);
      }
      
      @Override
      public IValue visitDateTime(Type type) throws IOException {
        try {
          switch (in.peek()) {
            case STRING:
              return vf.datetime(format.get().parse(in.nextString()).toInstant().toEpochMilli());
            case NUMBER:
              return vf.datetime(in.nextLong());
            case BEGIN_OBJECT:
              in.beginObject();
              in.endObject();
            default:
              throw new IOException("Expected a datetime instant " + in.getPath());
          }
        } catch (ParseException e) {
          throw new IOException("Could not parse date: " + in.getPath());
        }
      }
      
      @Override
      public IValue visitList(Type type) throws IOException {
        checkNull();

        IListWriter w = vf.listWriter();
        in.beginArray();
        while (in.hasNext()) {
          w.append(read(in, type.getElementType()));
        }

        in.endArray();
        return w.done();
      }
      
      public IValue visitSet(Type type) throws IOException {
        checkNull();

        ISetWriter w = vf.setWriter();
        in.beginArray();
        while (in.hasNext()) {
          w.insert(read(in, type.getElementType()));
        }

        in.endArray();
        return w.done();
      }

      private void checkNull() throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          throw new IOException("JSon reader does not accept null values; at" + in.getPath());
        }
      }
      
      private Type checkNameCons(Type adt, String consName) throws IOException {
        Set<Type> alternatives = store.lookupConstructor(adt, consName);
        
        if (alternatives.size() == 0) {
          throw new IOException("No constructor with this name was declared for " + adt + ":" + in.getPath());
        }
        else if (alternatives.size() > 1) {
          throw new IOException("Overloading of constructor names is not supported (" + adt + "):" + in.getPath());
        }
        
        return alternatives.iterator().next();
      }
    });
  }
}