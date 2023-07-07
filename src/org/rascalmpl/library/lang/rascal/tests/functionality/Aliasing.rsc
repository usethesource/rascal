@license{
   Copyright (c) 2009-2015 CWI
   All rights reserved. This program and the accompanying materials
   are made available under the terms of the Eclipse Public License v1.0
   which accompanies this distribution, and is available at
   http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

module lang::rascal::tests::functionality::Aliasing

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
    rel[StateId from,StateId to,Sym symbol] Transitions = {};  
    Transitions = {<1,2,3>}; 
    return true;
}
		  
alias trans = tuple[str, str, str]; 
alias block = set[trans];
alias partition = set[block];

data P[&T] = p(&T a);
                 
test bool  transitiveAliasAcrossTuples() {
    block aBlock = {<"a", "b", "c">};
    return aBlock == {<"a", "b", "c">};
}	

@ignoreCompiler{
Representation differs
}
test bool reifiedAlias1() = 
  #partition == 
  type(
  \set(\set(\tuple([
          \str(),
          \str(),
          \str()
        ]))),
  ());

@ignoreCompiler{
Representation differs
}
test bool reifiedAlias2() =
  #P[partition] ==
   type(
  adt(
    "P",
    [\set(\set(\tuple([
              \str(),
              \str(),
              \str()
            ])))]),
  (adt(
      "P",
      [parameter(
          "T",
          \value())]):choice(
      adt(
        "P",
        [parameter(
            "T",
            \value())]),
      {cons(
          label(
            "p",
            adt(
              "P",
              [parameter(
                  "T",
                  \value())])),
          [label(
              "a",
              parameter(
                "T",
                \value()))],
          [],
          {})})));
          
alias LIST[&T] = list[&T];

@ignoreCompiler{
Representation differs
}
test bool reifiedAlias3() =
   #LIST[LIST[int]].symbol == \list(\list(\int()));

@ignoreCompiler{
Representation differs
}   
test bool reifiedAlias4() =
   #LIST[LIST[LIST[int]]].symbol == \list(\list(\list(\int())));

alias TUPLELIST[&T] = tuple[LIST[&T], LIST[&T]];

@ignoreCompiler{
Representation differs
}
test bool reifiedAlias5() =
    #TUPLELIST[int].symbol == \tuple([\list(\int()), \list(\int())]);

alias STRING = str;

data DATA1 = d1(STRING s);

@ignoreCompiler{
Representation differs
}
test bool reifiedAlias6() = #DATA1 ==
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

@ignoreCompiler{
Representation differs
}
test bool reifiedAlias7() = #DATA2 ==
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
                [\str()],[]))],
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

@ignoreCompiler{
Representation differs
}
test bool reifiedAlias8() = #DATA2 ==
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
                [\str()],[]))],
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
  
alias SET[&T] = set[&T];

test bool setOrRel1() = {rel[int,int] R = {<1,2>}; SET[tuple[int,int]] ST = {<1,2>}; R == ST; };
test bool setOrRel2() = {rel[int,int] R = {<1,2>}; SET[tuple[int,int]] ST = R; R == ST; };
test bool setOrRel3() = {rel[int,int] R = {<1,2>}; SET[tuple[int,int]] ST = {<3,4>}; R + ST == {<1,2>, <3,4>};};
test bool setOrRel4() = {rel[int,int] R = {<1,2>}; SET[tuple[int,int]] ST = {<3,4>}; ST + R == {<1,2>, <3,4>};};
test bool setOrRel5() = {SET[tuple[int,int]] ST = {<3,4>}; <1,2> + ST== {<1,2>, <3,4>};};
test bool setOrRel6() = {SET[tuple[int,int]] ST = {<3,4>}; ST + <1,2> == {<1,2>, <3,4>};};
test bool setOrRel7() = {SET[tuple[int,int]] ST = {<3,4>}; rel[int,int] R = ST + <1,2>; R == {<1,2>, <3,4>};};
test bool setOrRel8() = {SET[tuple[int,int]] ST = {<3,4>}; rel[int,int] R = <1,2> + ST; R == {<1,2>, <3,4>};};

test bool listOrLrel1() = {lrel[int,int] LR = [<1,2>]; LIST[tuple[int,int]] LT = [<1,2>]; LR == LT; };
test bool listOrLrel2() = {lrel[int,int] LR = [<1,2>]; LIST[tuple[int,int]] LT = LR; LR == LT; };
test bool listOrLrel3() = {lrel[int,int] LR = [<1,2>]; LIST[tuple[int,int]] LT = [<3,4>]; LR + LT == [<1,2>, <3,4>];};
test bool listOrLrel4() = {lrel[int,int] LR = [<1,2>]; LIST[tuple[int,int]] LT = [<3,4>]; LT + LR == [<3,4>, <1,2>];};
test bool listOrLrel5() = {LIST[tuple[int,int]] LT = [<3,4>]; <1,2> + LT== [<1,2>, <3,4>];};
test bool listOrLrel6() = {LIST[tuple[int,int]] LT = [<3,4>]; LT + <1,2> == [<3,4>, <1,2>];};
test bool listOrLrel7() = {LIST[tuple[int,int]] LT = [<3,4>]; lrel[int,int] LR = LT + <1,2>; LR == [<3,4>, <1,2>];};
test bool listOrLrel8() = {LIST[tuple[int,int]] LT = [<3,4>]; lrel[int,int] LR = <1,2> + LT; LR == [<1,2>, <3,4>];};

@synopsis{this triggered issue #1595}
test bool enumerableAlias() {
  SET[int] tmp = {1,2,3};
  
  x = for (i <- tmp) append i;
  
  return {*x} == tmp;
}

alias T[&T] = tuple[&T, &T];

@synopsis{this triggered #1811}
test bool assignableTupleAlias() {
  T[int] x = <0,1>;
  <a,b> = x; // this would throw an exception
  return a == 0 && b == 1;
}

@synopsis{this tests if the solution for #1811 still checks the arity of the tuple}
@expected{
UnexpectedType
}
test bool assignableTupleAliasError() {
  T[int] x = <0,1>;
  <a,b,c> = x; // this should throw an exception
  return a == 0 && b == 1;
}