/*******************************************************************************
 * Copyright (c) 2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse public static License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Anya Helene Bagge - anya@ii.uib.no - UiB
 *
 * Based on code by:
 *
 *   * Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation
 *******************************************************************************/
package org.rascalmpl.value.impl.func;

import java.util.Iterator;
import java.util.Map.Entry;

import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

public final class MapFunctions {

    @SuppressWarnings("unused")
	private final static TypeFactory TF = TypeFactory.getInstance();

	public static IMap compose(IValueFactory vf, IMap map1, IMap map2) {
		Type newMapType;

		if (map1.getType().hasFieldNames() && map2.getType().hasFieldNames()) {
			newMapType = TypeFactory.getInstance().mapType(
					map1.getType().getKeyType(), map1.getType().getKeyLabel(),
					map2.getType().getValueType(),
					map2.getType().getValueLabel());
		} else {
			newMapType = TypeFactory.getInstance().mapType(map1.getKeyType(),
					map2.getValueType());
		}

		IMapWriter w = vf.mapWriter(newMapType);

		Iterator<Entry<IValue, IValue>> iter = map1.entryIterator();
		while (iter.hasNext()) {
			Entry<IValue, IValue> e = iter.next();
			IValue value = map2.get(e.getValue());
			if (value != null) {
				w.put(e.getKey(), value);
			}
		}

		return w.done();
	}
   
	public static IMap put(IValueFactory vf, IMap map1, IValue key, IValue value) {
		IMapWriter sw = vf.mapWriter(map1.getType());
		sw.putAll(map1);
		sw.put(key, value);
		return sw.done();
	}

	public static IMap join(IValueFactory vf, IMap map1, IMap map2) {
		IMapWriter sw = vf.mapWriter(map1.getType().lub(map2.getType()));
		sw.putAll(map1);
		sw.putAll(map2);
		return sw.done();
	}

	public static IMap common(IValueFactory vf, IMap map1, IMap map2) {
		IMapWriter sw = vf.mapWriter(map1.getType().lub(map2.getType()));

		for (IValue key : map1) {
			IValue thisValue = map1.get(key);
			IValue otherValue = map2.get(key);
			if (otherValue != null && thisValue.isEqual(otherValue)) {
				sw.put(key, thisValue);
			}
		}
		return sw.done();
	}

	public static IMap remove(IValueFactory vf, IMap map1, IMap map2) {
		IMapWriter sw = vf.mapWriter(map1.getType());
		for (IValue key : map1) {
			if (!map2.containsKey(key)) {
				sw.put(key, map1.get(key));
			}
		}
		return sw.done();
	}

	public static boolean isSubMap(IValueFactory vf, IMap map1, IMap map2) {
		for (IValue key : map1) {
			if (!map2.containsKey(key)) {
				return false;
			}
			if (!map2.get(key).isEqual(map1.get(key))) {
				return false;
			}
		}
		return true;
	}

    public static int hashCode(IValueFactory vf, IMap map1) {
        int hash = 0;

        Iterator<IValue> keyIterator = map1.iterator();
        while (keyIterator.hasNext()) {
            IValue key = keyIterator.next();
            hash ^= key.hashCode();
        }

        return hash;
    }

	public static boolean equals(IValueFactory vf, IMap map1, Object other){
        if (other == map1) return true;
        if (other == null) return false;

        if (other instanceof IMap) {
            IMap map2 = (IMap) other;

            if (map1.getType() != map2.getType()) return false;

            if (hashCode(vf, map1) != hashCode(vf, map2)) return false;

            if (map1.size() == map2.size()) {

				for (IValue k1 : map1) {
					if (containsKeyWithEquals(vf, map2, k1) == false) { // call to Object.equals(Object)
						return false;
					} else if (map2.get(k1).equals(map1.get(k1)) == false) { // call to Object.equals(Object)
						return false;
					}
				}

                return true;
            }
        }

        return false;	
	}
	
	public static boolean isEqual(IValueFactory vf, IMap map1, IValue other){
        if (other == map1) return true;
        if (other == null) return false;

        if (other instanceof IMap) {
          IMap map2 = (IMap) other;

          if (map1.size() == map2.size()) {

            for (IValue k1 : map1) {
              if (containsKey(vf, map2, k1) == false) { // call to IValue.isEqual(IValue)
                return false;
              } else if (map2.get(k1).isEqual(map1.get(k1)) == false) { // call to IValue.isEqual(IValue)
                return false;
              }
            }

            return true;
          }
        }

        return false;
	
	}

	public static IValue get(IValueFactory valueFactory, IMap map1, IValue key) {
		for (Iterator<Entry<IValue, IValue>> iterator = map1.entryIterator(); iterator.hasNext();) {
			Entry<IValue, IValue> entry = iterator.next();
			if (entry.getKey().isEqual(key)) {
				return entry.getValue();
			}
		}
		return null;
	}

	public static boolean containsKeyWithEquals(IValueFactory valueFactory, IMap map1, IValue key) {
		for (Iterator<IValue> iterator = map1.iterator(); iterator.hasNext();) {
			if (iterator.next().equals(key)) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsKey(IValueFactory valueFactory, IMap map1, IValue key) {
		for (Iterator<IValue> iterator = map1.iterator(); iterator.hasNext();) {
			if (iterator.next().isEqual(key)) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsValueWithEquals(IValueFactory valueFactory, IMap map1,
					IValue value) {
		for (Iterator<IValue> iterator = map1.valueIterator(); iterator.hasNext();) {
			if (iterator.next().equals(value)) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsValue(IValueFactory valueFactory, IMap map1, IValue value) {
		for (Iterator<IValue> iterator = map1.valueIterator(); iterator.hasNext();) {
			if (iterator.next().isEqual(value)) {
				return true;
			}
		}
		return false;
	}

}
