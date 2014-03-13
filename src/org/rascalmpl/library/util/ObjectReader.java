package org.rascalmpl.library.util;

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
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

/**
 * Use this class to create OIL expressions from arbitrary Java objects.
 */
public class ObjectReader {
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
          results.put(f.getName(), vf.node("error", vf.string(e.getMessage())));
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

    included.add(Integer.class);
    included.add(Boolean.class);
    included.add(Character.class);
    included.add(Byte.class);
    included.add(Long.class);
    included.add(Float.class);
    included.add(Double.class);
    included.add(int.class);
    included.add(char.class);
    included.add(byte.class);
    included.add(long.class);
    included.add(boolean.class);
    included.add(float.class);
    included.add(double.class);
    included.add(String.class);
    included.add(Map.class);
    included.add(List.class);
    included.add(Set.class);
    included.add(URI.class);
    included.add(IValue.class);

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

    Class<?> clazz = o.getClass();
    int i = -1;

    if (!instanceOfCheck(o, includes)) {
      return null;
    }

    if (o == TypeFactory.getInstance()) {
      INode err = vf.node("error", vf.string("Can not serialize the TypeFactory"));
      cache.put(o, err);
      return err;
    }
    else if ((i = stack.indexOf(o)) != -1) {
      // detected a cycle
      return vf.node("cycle", vf.string(clazz.getCanonicalName()), vf.integer(stack.size() - i));
    }

    stack.push(o);

    if (clazz.isArray()) {
      IListWriter w = vf.listWriter();
      for (Object e : (Object[]) o) {
        IValue elem = readObject(e, includes, cache, stack);

        if (elem != null) {
          w.insert(elem);
        }
      }

      result = w.done();
    }
    else if (clazz == URI.class) {
      result = vf.sourceLocation((URI) o);
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
      ISetWriter w = vf.setWriter();

      for (Object e : (Set<?>) o) {
        IValue elem = readObject(e, includes, cache, stack);

        if (elem != null) {
          w.insert(elem);
        }
      }

      result = w.done();
    }
    else if (o instanceof Map) {
      IMapWriter w = vf.mapWriter();

      for (Entry<?,?> e : ((Map<?,?>) o).entrySet()) {
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
    else if (o instanceof List) {
      IListWriter w = vf.listWriter();

      for (Object e : (List<?>) o) {
        IValue elem = readObject(e, includes, cache, stack);

        if (elem != null) {
          w.insert(elem);
        }
      }

      result = w.done();
    }
    else {
      String name = clazz.getCanonicalName();
      Map<String,IValue> fields = getFields(o, o.getClass(), includes, cache, stack);
      result = vf.node(name, new IValue[] { }, fields);
    }

    cache.put(o, result);
    stack.pop();
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
