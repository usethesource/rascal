module lang::rascal::tests::functionality::AliasTests
/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
import Type;

alias INTEGER0 = int;
		
test bool  usingAliases1() { INTEGER0 I = 3; return I == 3; }
test bool  usingAliases2() { INTEGER0 I = 3; INTEGER0 J = I ; return J == 3; }
test bool  usingAliases3() { list[INTEGER0] LI = [1,2,3]; return LI == [1,2,3]; }
test bool  usingAliases4() { set[INTEGER0] SI = {1,2,3}; return SI == {1,2,3}; }
test bool  usingAliases5() { map[INTEGER0,INTEGER0] MI = (1:10,2:20); return MI == (1:10,2:20); }
test bool  usingAliases6() { rel[INTEGER0,INTEGER0] RI = {<1,10>,<2,20>}; return RI == {<1,10>,<2,20>}; }
 
alias INTEGER1 = INTEGER0;

test bool  usingIndirectAliases1() { INTEGER1 I = 3; return I == 3; }       
test bool  usingIndirectAliases2() { INTEGER1 I = 3; INTEGER1 J = I ; return J == 3; }        
test bool  usingIndirectAliases3() { list[INTEGER1] LI = [1,2,3]; return LI == [1,2,3]; }      
test bool  usingIndirectAliases4() { set[INTEGER1] SI = {1,2,3}; return SI == {1,2,3}; }      
test bool  usingIndirectAliases5() { map[INTEGER1,INTEGER1] MI = (1:10,2:20); return MI == (1:10,2:20); }
test bool  usingIndirectAliases6() { rel[INTEGER1,INTEGER1] RI = {<1,10>,<2,20>}; return RI == {<1,10>,<2,20>}; }

alias INTEGER2 = INTEGER1;
test bool  usingVeryIndirectAliases1() { INTEGER2 I = 3; return I == 3; }       
test bool  usingVeryIndirectAliases2() { INTEGER2 I = 3; INTEGER2 J = I ; return J == 3; }        
test bool  usingVeryIndirectAliases3() { list[INTEGER2] LI = [1,2,3]; return LI == [1,2,3]; }      
test bool  usingVeryIndirectAliases4() { set[INTEGER2] SI = {1,2,3}; return SI == {1,2,3}; }      
test bool  usingVeryIndirectAliases5() { map[INTEGER2,INTEGER2] MI = (1:10,2:20); return MI == (1:10,2:20); }
test bool  usingVeryIndirectAliases6() { rel[INTEGER2,INTEGER2] RI = {<1,10>,<2,20>}; return RI == {<1,10>,<2,20>}; }

alias INTEGER4 = INTEGER3;
alias INTEGER3 = int;

test bool outofOrderDeclaration() { INTEGER4 x = 0; return x == 0; }

alias ADT0 = ADT1;
data ADT1 = f(int);

test bool  aliasAndADT1() { ADT0 x = f(0); return x == f(0); }
	
alias StateId = int;
alias Permutation = list[int];
alias StatedId = int;
alias Sym = int;
             	
test bool aliasAndADT2() {
    map[list[Permutation], StateId] allStates = ();
    rel[StateId from,StateId to,Sym symbol] Transitions = {};  
    Transitions = {<1,2,3>}; 
    return true;
}
		  
alias trans = tuple[str, str, str]; 
alias block = set[trans];
alias partition = set[block];
                 
test bool  transitiveAliasAcrossTuples() {
    block aBlock = {<"a", "b", "c">};
    return aBlock == {<"a", "b", "c">};
}	

@ignoreCompiler{since aliases are fully expanded in compiled code}
test bool reifiedAlias1a() = 
  #partition == 
  type(
  \alias(
    "partition",
    [],
    \set(\alias(
        "block",
        [],
        \rel([
            \str(),
            \str(),
            \str()
          ])))),
  ());

@ignoreInterpreter{since aliases are preserved in interpreted code}
test bool reifiedAlias1b() = 
  #partition ==  
  type(
  \alias(
    "partition",
    [],
    \set(\rel([
          \str(),
          \str(),
          \str()
        ]))),
  ());
	

alias STRING = str;

data DATA1 = d1(STRING s);

test bool reifiedAlias2() = #DATA1 ==
type(
  adt(
    "DATA1",
    []),
  (adt(
      "DATA1",
      []):choice(
      adt(
        "DATA1",
        []),
      {cons(
          label(
            "d1",
            adt(
              "DATA1",
              [])),
          [label(
              "s",
              \str())],
          [],
          {})})));
          

data DATA2 = d2(DATA1(STRING) fun);

@ignoreCompiler{since aliases are fully expanded in compiled code}
test bool reifiedAlias3a() = #DATA2 ==
type(
  adt(
    "DATA2",
    []),
  (
    adt(
      "DATA2",
      []):choice(
      adt(
        "DATA2",
        []),
      {cons(
          label(
            "d2",
            adt(
              "DATA2",
              [])),
          [label(
              "fun",
              func(
                adt(
                  "DATA1",
                  []),
                [\alias(
                    "STRING",
                    [],
                    \str())]))],
          [],
          {})}),
    adt(
      "DATA1",
      []):choice(
      adt(
        "DATA1",
        []),
      {cons(
          label(
            "d1",
            adt(
              "DATA1",
              [])),
          [label(
              "s",
              \str())],
          [],
          {})})
  ));

@ignoreInterpreter{since aliases are preserved in interpreted code}
test bool reifiedAlias3b() = #DATA2 ==
type(
  adt(
    "DATA2",
    []),
  (
    adt(
      "DATA2",
      []):choice(
      adt(
        "DATA2",
        []),
      {cons(
          label(
            "d2",
            adt(
              "DATA2",
              [])),
          [label(
              "fun",
              func(
                adt(
                  "DATA1",
                  []),
                [\str()]))],
          [],
          {})}),
    adt(
      "DATA1",
      []):choice(
      adt(
        "DATA1",
        []),
      {cons(
          label(
            "d1",
            adt(
              "DATA1",
              [])),
          [label(
              "s",
              \str())],
          [],
          {})})
  ));