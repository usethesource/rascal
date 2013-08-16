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
		private final IValue[] params;
		private final Map<String, IValue> keyArgs;
		
		public Key(IValue[] params, Map<String, IValue> keyArgs) {
			storedHash = Arrays.hashCode(params) + (keyArgs == null ? 0 : keyArgs.hashCode());
			
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
					if (other.params.length != this.params.length)
						return false;
					for (int i = 0; i < params.length; i++) {
						if (!other.params[i].isEqual(params[i]))
							return false;
					}
					if (other.keyArgs == null && keyArgs == null) 
						return true;
					if (other.keyArgs == null ^ keyArgs == null)
						return false;
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
	public Result<IValue> getStoredResult(IValue[] params, Map<String, IValue> keyArgs) {
		return store.get(new Key(params, keyArgs));
	}
	
	public void storeResult(IValue[] params, Map<String, IValue> keyArgs, Result<IValue> result) {
		store.put(new Key(params, keyArgs), result);
	}
}
