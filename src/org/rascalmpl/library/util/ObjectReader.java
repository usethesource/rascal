package org.rascalmpl.library.util;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

/**
 * Use this class to create values from arbitrary Java objects.
 */
public class ObjectReader {
  private static final IValue[] EMPTY = new IValue[] { };
  private final IValueFactory vf;

  public ObjectReader(IValueFactory vf) {
    this.vf = vf;
  }

  private Map<String,IValue> getFields(Object o, Class<?> clazz, Set<Class<?>> includes, Map<Object,IValue> cache, Stack<Object> stack) {
    Map<String,IValue> results = new HashMap<>();

    while (clazz != Object.class) {
      Field[] fields = clazz.getDeclaredFields();

      for (Field f : fields) {
        try {
          f.setAccessible(true);
          IValue res = readObject(f.get(o), includes, cache, stack);

          if (res != null) {
            results.put(f.getName(), res);
          }
        } 
        catch (IllegalArgumentException | IllegalAccessException e) {
          // for robustness' sake we ignore the exceptions here.
          // the model will be incomplete, but since we generate an untyped model
          // this should not break any code. null values are ignored by the
          // surrounding containers.
          return null;
        } 
      }

      clazz = clazz.getSuperclass();
    }

    return results;
  }

  public IValue readObject(Object o, Set<String> types, ClassLoader loader) {
    Set<Class<?>> included = new HashSet<>();
    for (String name : types) {
      try {
        included.add(loader.loadClass(name));
      } catch (ClassNotFoundException e) {
        // it may happen and we gracefully ignore this here for robustness' sake
      }
    }

    return readObject(o, included, new HashMap<Object,IValue>(), new Stack<Object>());
  }

  private IValue readObject(Object o, Set<Class<?>> includes, Map<Object,IValue> cache, Stack<Object> stack) {
    if (o == null) {
      return null;
    }

    IValue result = cache.get(o);

    if (result != null) {
      return result;
    }

    if (stack.contains(o)) {
      // detected a cycle
      return null;
    }
    else {
      stack.push(o);
    }

    Class<?> clazz = o.getClass();
    
    if (clazz.isArray()) {
      result = readArray((Object[]) o, includes, cache, stack);
    }
    else if (clazz == URI.class) {
      result = vf.sourceLocation((URI) o);
    }
    else if (clazz == File.class){
      result = vf.sourceLocation(((File) o).getAbsolutePath());
    }
    else if (clazz == int.class || clazz == Integer.class) {
      result = vf.integer((Integer) o);
    }
    else if (clazz == long.class || clazz == Long.class){
      result = vf.integer((Long) o);
    }
    else if (clazz == char.class || clazz == Character.class){
      result = vf.integer((Character) o);
    }
    else if (clazz == byte.class || clazz == Byte.class){
      result = vf.integer((Byte) o);
    }
    else if (clazz == boolean.class || clazz == Boolean.class){
      result = vf.bool(((Boolean) o));
    }
    else if (clazz == float.class || clazz == Float.class){
      result = vf.real((Float) o);
    }
    else if (clazz == double.class || clazz == Double.class){
      result = vf.real((Double) o);
    }
    else if (clazz == String.class) {
      result = vf.string(((String) o));
    }
    else if (o instanceof IValue) {
      return (IValue) o;
    }
    else if (o instanceof Set<?>) {
      result = readSet((Set<?>) o, includes, cache, stack);
    }
    else if (o instanceof Map<?,?>) {
      result = readMap((Map<?,?>) o, includes, cache, stack);
    }
    else if (o instanceof List<?>) {
      result = readList((List<?>) o, includes, cache, stack);
    }
    else if (instanceOfCheck(o, includes)) {
      Map<String, IValue> fields = getFields(o, o.getClass(), includes, cache, stack);
      result = vf.node(clazz.getCanonicalName(), EMPTY, fields);
    }

    cache.put(o, result);
    stack.pop();
    return result;
  }

  private IValue readList(List<?> o, Set<Class<?>> includes, Map<Object, IValue> cache, Stack<Object> stack) {
    IValue result;
    IListWriter w = vf.listWriter();

    for (Object e : o) {
      IValue elem = readObject(e, includes, cache, stack);

      if (elem != null) {
        w.insert(elem);
      }
    }

    result = w.done();
    return result;
  }

  private IValue readMap(Map<?,?> o, Set<Class<?>> includes, Map<Object, IValue> cache, Stack<Object> stack) {
    IValue result;
    IMapWriter w = vf.mapWriter();

    try {
      for (Entry<?,?> e : o.entrySet()) {
        Object ok = e.getKey();
        IValue key = readObject(ok, includes, cache, stack);

        if (key == null) {
          continue;
        }

        Object ov = e.getValue();
        IValue val = readObject(ov, includes, cache, stack);

        if (val == null) {
          continue;
        }

        w.put(key, val);
      }

      result = w.done();
    }
    catch (UnsupportedOperationException e) {
      // some maps don't support the full map interface, so we default to normal objects
      String name = o.getClass().getCanonicalName();
      Map<String,IValue> fields = getFields(o, o.getClass(), includes, cache, stack);
      result = vf.node(name, EMPTY, fields);
    }
    return result;
  }

  private IValue readSet(Set<?> o, Set<Class<?>> includes, Map<Object, IValue> cache, Stack<Object> stack) {
    IValue result;
    ISetWriter w = vf.setWriter();

    for (Object e : o) {
      IValue elem = readObject(e, includes, cache, stack);

      if (elem != null) {
        w.insert(elem);
      }
    }

    result = w.done();
    return result;
  }

  private IValue readArray(Object[] o, Set<Class<?>> includes, Map<Object, IValue> cache, Stack<Object> stack) {
    IValue result;
    IListWriter w = vf.listWriter();
    for (Object e : o) {
      IValue elem = readObject(e, includes, cache, stack);

      if (elem != null) {
        w.insert(elem);
      }
    }

    result = w.done();
    return result;
  }

  private boolean instanceOfCheck(Object o, Set<Class<?>> includes) {
    for (Class<?> clazz : includes) {
      if (clazz.isAssignableFrom(o.getClass())) {
        return true;
      }
    }

    return false;
  }
}
