package org.rascalmpl.interpreter.result.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import io.usethesource.vallang.IValue;

public class MemoizationCache<TResult> {
	
	private static final int PRIME1 = (int) 2654435761L;
	private static final int PRIME2 = (int) 2246822519L;
	private static final int PRIME3 = (int) 3266489917L;
	private static final int PRIME4 = 668265263;
	private static final int PRIME5 = 0x165667b1;
	
	private static int calculateHash(IValue[] params, Map<String, IValue> keyArgs) {
		//xxHash
		int h;
		int len = params.length;
		int i = 0;
		if (len >= 4) {
			int v1 = PRIME5 + PRIME1 + PRIME2;
			int v2 = PRIME5 + PRIME2;
			int v3 = PRIME5 + 0;
			int v4 = PRIME5 - PRIME1;
			int limit = len - 4;
			do {
				v1 += params[i++].hashCode() * PRIME2;
				v1 = Integer.rotateLeft(v1, 13) * PRIME1;
				v2 += params[i++].hashCode() * PRIME2;
				v2 = Integer.rotateLeft(v2, 13) * PRIME1;
				v3 += params[i++].hashCode() * PRIME2;
				v3 = Integer.rotateLeft(v3, 13) * PRIME1;
				v4 += params[i++].hashCode() * PRIME2;
				v4 = Integer.rotateLeft(v4, 13) * PRIME1;
			} while(i < limit);
			h = Integer.rotateLeft(v1, 1)
			  + Integer.rotateLeft(v2, 7)
			  + Integer.rotateLeft(v3, 12)
			  + Integer.rotateLeft(v4, 18);
		}
		else {
			h = PRIME5;
		}
		
		while (i < len) {
			h += params[i++].hashCode() * PRIME3;
			h = Integer.rotateLeft(h, 17) * PRIME4;
		}
		
		if (keyArgs != null) {
			h += keyArgs.hashCode() * PRIME3;
			h = Integer.rotateLeft(h, 17) * PRIME4;
		}

		h ^= h >>> 15;
		h *= PRIME2;
		h ^= h >>> 13;
		h *= PRIME3;
		h ^= h >>> 16;
		
		return h;
	}
	
	private static class CacheKey {
		private final int storedHash;
		@SuppressWarnings("rawtypes")
		private final KeySoftReference[] params;
		private final int keyArgsSize;
		private final Map<String, KeySoftReference<IValue>> keyArgs;
		
		public CacheKey(IValue[] params, Map<String, IValue> keyArgs, @SuppressWarnings("rawtypes") ReferenceQueue queue) {
			this.storedHash = calculateHash(params, keyArgs);
			
			this.params = new KeySoftReference[params.length];
			for (int i = 0; i < params.length; i++) {
				this.params[i] = new KeySoftReference<IValue>(params[i], this, queue);
			}
			
			if (keyArgs != null) {
				this.keyArgs = new HashMap<>(keyArgs.size());
				for (Entry<String, IValue> e : keyArgs.entrySet()) {
					this.keyArgs.put(e.getKey(), new KeySoftReference<IValue>(e.getValue(), this, queue));
				}
				this.keyArgsSize = keyArgs.size();
			}
			else {
				this.keyArgs = null;
				this.keyArgsSize = 0;
			}
			
		}
		
		@Override
		public int hashCode() {
			return storedHash;
		}
		
        @Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			// yes, this reads a bit weird. But this is right
			// we use a different class for lookup (LookupKey)
			// that doesn't incur the runtime penalty of creating all kinds of
			// SoftReferences. Since this code is in the hot path of the call
			// overhead of the evaluator, it's worth it.
			// The reason is not comparing to it's own class is that the
			// store method should only be called for cases where we know
			// the entry is not in the map yet. So to keep this code small,
			// we don't include a whole bunch of code to handle a case of something 
			// that should never happen (except in case of hash collision, and then
			// a false is the right answer)
			if (obj instanceof LookupKey) {
				return ((LookupKey)obj).equals(this);
			}
			return false;
		}
	}
	
	// Special Version of the Key data
	// need to make sure the lookup key references
	// aren't released during lookup
	// and avoid creating extra SoftReferences
	private static class LookupKey {
		
		private final int storedHash;
		private final IValue[] params;
		private final Map<String, IValue> keyArgs;
		private final int keyArgsSize;

		public LookupKey(IValue[] params, Map<String, IValue> keyArgs) {
			this.storedHash = calculateHash(params, keyArgs);
			this.params = params;
			this.keyArgs = keyArgs;
			this.keyArgsSize = keyArgs == null ? 0 : keyArgs.size();
		}
		
		@Override
		public int hashCode() {
			return storedHash;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof CacheKey) {
				CacheKey other = (CacheKey)obj;
				if (other.storedHash != this.storedHash) 
					return false;
				if (other.params.length != this.params.length)
					return false;
				for (int i = 0; i < params.length; i++) {
					IValue tp = params[i];
					IValue op = (IValue)other.params[i].get();
					if (tp == null || op == null) 
						return false; 
					if (!tp.equals(op))
						return false;
				}
				
				if (other.keyArgsSize != keyArgsSize)
					return false;
				if (keyArgsSize > 0) {
					for (Entry<String, IValue> kv: keyArgs.entrySet()) {
						IValue tp = kv.getValue();
						IValue op = other.keyArgs.get(kv.getKey()).get();
						if (tp == null || op == null) 
							return false; 
						if (!tp.equals(op))
							return false;
					}
				}
				return true;
			}
			return false;
		}
	}
	
	// Special SoftReference to have a reference to the Key in the HashMap
	private static class KeySoftReference<K> extends SoftReference<K> {
		private CacheKey key;

		@SuppressWarnings("unchecked")
		public KeySoftReference(K ref, CacheKey key, @SuppressWarnings("rawtypes") ReferenceQueue queue) {
			super(ref, queue);
			this.key = key;
		}
		
	}
	
	// Extra class to store CacheKeys in the HashSet
	// should only use instance equality.
	private static class CacheKeyWrapper {
		private final CacheKey key;

		public CacheKeyWrapper(CacheKey key) {
			this.key = key;
		}
		
		@Override
		public int hashCode() {
			return key.storedHash;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof CacheKeyWrapper) {
				return ((CacheKeyWrapper)obj).key == this.key;
			}
			return false;
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void cleanupCache() {
		Set<CacheKeyWrapper> toCleanup = new HashSet<>();
		synchronized (queue) {
			Reference cleared = null;
			do {
				cleared = queue.poll();
				if (cleared != null && cleared instanceof KeySoftReference<?>) {
					toCleanup.add(new CacheKeyWrapper(((KeySoftReference<?>)cleared).key));
				}
			}
			while (cleared != null);
		}
		for (CacheKeyWrapper ckw : toCleanup) {
			CacheKey cl = ckw.key;
			cache.remove(cl);
			if (cl.keyArgs != null) {
				cl.keyArgs.clear();
			}
			for (int i =0; i < cl.params.length; i++) 
				cl.params[i] = null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	private final ReferenceQueue queue = new ReferenceQueue();
	private final Map<Object, KeySoftReference<TResult>> cache = new HashMap<>();
	
	public TResult getStoredResult(IValue[] params, Map<String, IValue> keyArgs) {
		cleanupCache();
		KeySoftReference<TResult> result = cache.get(new LookupKey(params, keyArgs));
		return result == null ? null : result.get();
	}
	
	public void clear() {
	    for (Entry<Object,KeySoftReference<TResult>> e : cache.entrySet()) {
	        e.getValue().clear();
	    }
	    
	    cache.clear();
	}
	

	/**
	 * This method assumes that the getStoredResult is first called to assure there was no result already there beforehand.
	 */
	public TResult storeResult(IValue[] params, Map<String, IValue> keyArgs, TResult result) {
		cleanupCache();
		CacheKey newKey = new CacheKey(params, keyArgs, queue);
		cache.put(newKey, new KeySoftReference<TResult>(result, newKey, queue));
		return result;
	}
}
