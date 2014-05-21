module tests::functionality::AliasTests
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
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/

import util::Eval;

public test bool q () {
   return eval("1==1;")==result(true);
   }
		
public test bool  usingAliases1()= eval("alias INTEGER = int;INTEGER I = 3; I == 3;")==result(true);
public test bool  usingAliases2()= eval("alias INTEGER = int;INTEGER I = 3; INTEGER J = I ; J == 3;")==result(true);
public test bool  usingAliases3()= eval("alias INTEGER = int;list[INTEGER] LI = [1,2,3]; LI == [1,2,3];")==result(true);
public test bool  usingAliases4()= eval("alias INTEGER = int;set[INTEGER] SI = {1,2,3}; SI == {1,2,3};")==result(true);
public test bool  usingAliases5()= eval("alias INTEGER = int; map[INTEGER,INTEGER] MI = (1:10,2:20);MI == (1:10,2:20);")==result(true);
public test bool  usingAliases6()= eval("alias INTEGER = int; rel[INTEGER,INTEGER] RI = {\<1,10\>,\<2,20\>}; RI == {\<1,10\>,\<2,20\>};")==result(true);

public test bool  usingIndirectAliases1()= eval("
        alias INTEGER0 = int; alias INTEGER1 = INTEGER0;
        alias INTEGER = INTEGER1;INTEGER I = 3; I == 3;")==result(true);
public test bool  usingIndirectAliases2()= eval("
        alias INTEGER0 = int; alias INTEGER1 = INTEGER0;
        alias INTEGER = INTEGER1;INTEGER I = 3; INTEGER J = I ; J == 3;")==result(true);
public test bool  usingIndirectAliases3()= eval("
        alias INTEGER0 = int; alias INTEGER1 = INTEGER0;
        alias INTEGER = INTEGER1;list[INTEGER] LI = [1,2,3]; LI == [1,2,3];")==result(true);
public test bool  usingIndirectAliases4()= eval("
       alias INTEGER0 = int; alias INTEGER1 = INTEGER0;
       alias INTEGER = INTEGER1;set[INTEGER] SI = {1,2,3}; SI == {1,2,3};")==result(true);
public test bool  usingIndirectAliases5()= eval("
       alias INTEGER0 = int; alias INTEGER1 = INTEGER0;
       alias INTEGER = INTEGER1; map[INTEGER,INTEGER] MI = (1:10,2:20);MI == (1:10,2:20);")==result(true);
public test bool  usingIndirectAliases6()= eval("
       alias INTEGER0 = int; alias INTEGER1 = INTEGER0;
       alias INTEGER = INTEGER1; rel[INTEGER,INTEGER] RI = {\<1,10\>,\<2,20\>}; RI == {\<1,10\>,\<2,20\>};")==result(true);


public test bool  aliasAndADT1() =
		eval("data INTEGER1 = f(int);alias INTEGER0 = INTEGER1;
		     INTEGER0 x = f(0); x == f(0);")==result(true);
		
public test bool aliasAndADT2() = 
        eval("
		     alias StateId = int;
		     alias Permutation = list[int];
		     alias StatedId = int;
		     alias Symbol = int;
		     map[list[Permutation], StateId] allStates = ();
		     rel[StateId from,StateId to,Symbol symbol] Transitions = {};  
		     Transitions = {\<1,2,3\>}; true;
		     ")==result(true);	

public test bool  transitiveAliasAcrossTuples() = eval("
				 alias trans = tuple[str, str, str];	
				 alias block = set[trans];
				 alias partition = set[block];
		         block aBlock = {\<\"a\", \"b\", \"c\"\>};
				 aBlock == {\<\"a\", \"b\", \"c\"\>};
				 ")==result(true);	
	

