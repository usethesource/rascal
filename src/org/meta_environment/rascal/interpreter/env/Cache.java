package org.meta_environment.rascal.interpreter.env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public class Cache {

	
	protected final Map<VariableCacheEntry, Result> variables;
	protected final Map<FunctionCacheEntry, List<Lambda>> functions;
	private boolean enabled;
	
	
	public Cache() {
		variables = new HashMap<VariableCacheEntry, Result>();
		functions = new HashMap<FunctionCacheEntry, List<Lambda>>();
		enabled = true;
	}
	
	public void save(Map<String, List<Lambda>> env, String name, List<Lambda> closures) {
		List<Lambda> list = new ArrayList<Lambda>(closures);
		functions.put(new FunctionCacheEntry(env, name), list);
	}
	
	public void save(Map<String, Result> env, String name, Result result) {
		variables.put(new VariableCacheEntry(env, name), result);
	}
	
	public boolean containsVariable(Map<String, Result> env, String name) {
		return variables.containsKey(new VariableCacheEntry(env, name));
	}
	
	public boolean containsFunction(Map<String, List<Lambda>> env, String name) {
		return functions.containsKey(new FunctionCacheEntry(env, name));
	}
	
	public void commit() {
		if (!isEnabled()) {
			throw new ImplementationError("trying to disable cache when it's not enabled");
		}
		enabled = false;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void rollback() {
		if (!isEnabled()) {
			throw new ImplementationError("trying to restore cache which is not enabled.");
		}
		enabled = false;
		for (Map.Entry<VariableCacheEntry, Result> entry: variables.entrySet()) {
			Map<String, Result> env = entry.getKey().env;
			String name = entry.getKey().name;
			Result value = entry.getValue();
			// NULL indicates the variable was unbound before.
			if (value == null) {
				env.remove(name);
			}
			else {
				env.put(name, entry.getValue());
			}
		}
		for (Map.Entry<FunctionCacheEntry, List<Lambda>> entry: functions.entrySet()) {
			Map<String, List<Lambda>> env = entry.getKey().env;
			String name = entry.getKey().name;
			List<Lambda> value = entry.getValue();
			// NULL indicates name had no function bindings.
			if (value == null) {
				env.remove(name);
			}
			else {
				env.put(name, value);	
			}
		}
	}
	
	private class FunctionCacheEntry {
		private Map<String,List<Lambda>> env;
		private String name;

		public FunctionCacheEntry(Map<String, List<Lambda>> env, String name) {
			this.env = env;
			this.name = name;
		}
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof FunctionCacheEntry)) {
				return false;
			}
			FunctionCacheEntry entry = (FunctionCacheEntry)o;
			return env == entry.env && name.equals(entry.name);
		}
	}

	private class VariableCacheEntry {
		private Map<String, Result> env;
		private String name;

		public VariableCacheEntry(Map<String, Result> env, String name) {
			this.env = env;
			this.name = name;
		}
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof VariableCacheEntry)) {
				return false;
			}
			VariableCacheEntry entry = (VariableCacheEntry)o;
			return env == entry.env && name.equals(entry.name);
		}
	}

}
