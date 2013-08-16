package org.rascalmpl.interpreter.result.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.result.Result;

public class MemoizationCache {
	private class Key {
		private final int storedHash;
		private final Type[] types;
		private final IValue[] params;
		private final Map<String, IValue> keyArgs;
		
		public Key(Type[] types, IValue[] params, Map<String, IValue> keyArgs) {
			Object[] values = new Object[types.length + params.length + 1];
			System.arraycopy(types, 0, values, 0, types.length);
			System.arraycopy(params, 0, values, types.length, params.length);
			values[values.length-1] = keyArgs;
			storedHash = Arrays.hashCode(values);
			
			this.types = types;
			this.params = params;
			this.keyArgs = keyArgs;
		}
		
		@Override
		public int hashCode() {
			return storedHash;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Key) {
				Key other = (Key)obj;
				if (other.storedHash == this.storedHash) {
					if (other.types.length != this.types.length)
						return false;
					for (int i = 0; i < types.length; i++) {
						if (!other.types[i].equivalent(types[i]))
							return false;
					}
					if (other.params.length != this.params.length)
						return false;
					for (int i = 0; i < params.length; i++) {
						if (!other.params[i].isEqual(params[i]))
							return false;
					}
					if (other.keyArgs.size() != keyArgs.size()) 
						return false;
					for (String k: keyArgs.keySet()) {
						if (other.keyArgs.get(k) != keyArgs.get(k))
							return false;
					}
					return true;
				}
				
			}
			return false;
		}
	}
	private Map<Key, Result<IValue>> store = new HashMap<>();
	public Result<IValue> getStoredResult(Type[] types, IValue[] params, Map<String, IValue> keyArgs) {
		return store.get(new Key(types, params, keyArgs));
	}
	
	public void storeResult(Type[] types, IValue[] params, Map<String, IValue> keyArgs, Result<IValue> result) {
		store.put(new Key(types, params, keyArgs), result);
	}
}
