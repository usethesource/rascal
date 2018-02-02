/*******************************************************************************
 * Copyright (c) 2011-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.core.parser.gtd.result.action;

/**
 * A dummy environment.
 */
public class VoidEnvironment {
	public final static VoidEnvironment ROOT_VOID_ENVIRONMENT = new VoidEnvironment();
	
	private VoidEnvironment(){
		super();
	}
}
