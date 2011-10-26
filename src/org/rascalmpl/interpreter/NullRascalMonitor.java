/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter;

public class NullRascalMonitor implements IRascalMonitor {
	public int endJob(boolean succeeded) {
		return 0;
	}

	public void event(String name) {
	}

	public void event(String name, int inc) {
	}

	public void event(int inc) {
	}

	public void startJob(String name, int totalWork) {
	}
	
	public void startJob(String name) {
	}
	
	public void startJob(String name, int workShare, int totalWork) {
	}
	
	public void todo(int work) {
	}
}
