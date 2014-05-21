module tests::functionality::StatementTCTests
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
 *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
*******************************************************************************/

import StaticTestingUtils;

public test bool assertError2() = unexpectedType("assert 3.5;");

public test bool assertError3() = unexpectedType("assert 3.5: \"Wrong expression type\";");

public test bool assertError4() = undeclaredVariable("assert X;");
 
public test bool assertError5() = undeclaredVariable("assert X: \"Wrong expression type\";");
  	
public test bool ifThenError1() = unexpectedType("if(3){n = 4;};");

public test bool ifThenElseError1() = unexpectedType("if(\"abc\") {n = 4;} else {n=5;}");
  
public test bool solveError1() = unexpectedType("rel[int,int] R1 = {\<1,2\>, \<2,3\>, \<3,4\>}; rel[int,int] T = R1; solve (T; true)  T = T + (T o R1);");

public test bool doWhileError1() = unexpectedType("do {n = 4;} while(3);");

public test bool whileError1() = unexpectedType("while(3){n = 4;}");	
  	
