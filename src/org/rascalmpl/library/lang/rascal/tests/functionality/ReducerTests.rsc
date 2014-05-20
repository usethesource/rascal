module tests::functionality::ReducerTests
/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
*******************************************************************************/




	public test bool testCount() = ( 0 | it + 1 | x <- [1,2,3] ) == 3;
	

	public test bool testMax() = ( 0 | x > it ? x : it | x <- [1,2,3] ) == 3;
	
	
	public test bool testSum() = ( 0 | it + x  | x <- [1,2,3] ) == 6;
	
	public test bool testFlatMap() =
		( {} | it + x  | x <- {{1,2}, {2,3,4}} ) == {1,2,3,4};
	
