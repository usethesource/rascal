/*******************************************************************************
 * Copyright (c) 2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Anya Helene Bagge - (UiB)
*******************************************************************************/
package org.rascalmpl.interpreter.env;

public class UnmodifiableModuleEnvironment extends ModuleEnvironment {

	public UnmodifiableModuleEnvironment(String name, GlobalEnvironment heap) {
		super(name, heap);
	}

}
