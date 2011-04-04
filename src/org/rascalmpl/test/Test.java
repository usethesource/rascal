/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int a = 9;
		a += (a = 3);									// first example
		System.out.println(a);
		int b = 9;
		b = b + (b = 3);									// second example
		System.out.println(b);
		int[] ar = new int[10];
		int i = 1;
		ar[i] = (i=3);
		System.out.println(ar[1]);

	}

}
