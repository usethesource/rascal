package org.rascalmpl.interpreter.env;

public class UnmodifiableModuleEnvironment extends ModuleEnvironment {

	public UnmodifiableModuleEnvironment(String name, GlobalEnvironment heap) {
		super(name, heap);
	}

}
