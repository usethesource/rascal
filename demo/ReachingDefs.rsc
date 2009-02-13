module ReachingDefs

import Relation;
import IO;

alias stat = int;
alias var = str;
alias def  = tuple[stat, var];


set[stat] predecessor(rel[stat,stat] P, stat S)
@doc{predecessor -- of statement S in cfg P}
{
	return invert(P)[S];
}

set[stat] successor(rel[stat,stat] P, stat S)
@doc{successor -- of statement S in cfg P}
{
	return P[S];
}

rel[stat,def] kill(rel[stat,var] DEFS) { 
	return {<S1, <S2, V>> | <stat S1, var V> : DEFS, <stat S2, V> : DEFS, S1 != S2};
}

rel[stat,def] definition(rel[stat,var] DEFS){
	return {<S,<S,V>> | <stat S, var V> : DEFS};
}

rel[stat,def] use(rel[stat, var] USES){
	return {<S, <S, V>> | <stat S, var V> : USES};
}

rel[stat, def] reachingDefinitions(rel[stat,var] DEFS, rel[stat,stat] PRED){
	set[stat] STATEMENT = carrier(PRED);
	rel[stat,def] DEF  = definition(DEFS);
	rel[stat,def] KILL = kill(DEFS);

	// The set of mutually recursive dataflow equations that has to be solved:

	with
		rel[stat,def] IN = {};
		rel[stat,def] OUT = DEF;
	solve {
       		IN =  {<S, D> | int S : STATEMENT, stat P : predecessor(PRED,S), def D : OUT[P]};
        	OUT = {<S, D> | int S : STATEMENT, def D : DEF[S] + (IN[S] - KILL[S])};
	};
	return IN;
}

public rel[stat,def] liveVariables(rel[stat,var] DEFS, rel[stat, var] USES, rel[stat,stat] PRED){
	set[stat] STATEMENT = carrier(PRED);
	rel[stat,def] DEF  = definition(DEFS);
	rel[stat,def] USE = use(USES);
	with
		rel[stat,def] LIN = {};
		rel[stat,def] LOUT = DEF;
 	solve {
		LIN  =  { < S, D> | stat S : STATEMENT,  def D : USE[S] + (LOUT[S] - (DEF[S]))};
		LOUT =  { < S, D> | stat S : STATEMENT,  stat Succ : successor(PRED,S), def D : LIN[Succ] };
	}
	return LIN;
}

public bool testReaching1(){

	// Reaching definitions, example ASU, p626

	rel[stat,stat] PRED = { <1,2>, <2,3>, <3,4>, <4,5>, <5,6>, <5,7>, <6,7>, <7,4>};
	rel[stat, var] DEFS = { <1, "i">, <2, "j">, <3, "a">, <4, "i">, <5, "j">, <6, "a">, <7, "i">};
	rel[stat, var] USES = { <1, "m">, <2, "n">, <3, "u1">, <4, "i">, <5, "j">, <6, "u2">, <7, "u3">};
	
	assert "kill": kill(DEFS) ==  {<1, <4, "i">>, <1, <7, "i">>, <2, <5, "j">>, <3, <6, "a">>, 
                         <4, <1, "i">>, <4, <7, "i">>, <5, <2, "j">>, <6, <3, "a">>, 
                         <7, <1, "i">>, <7, <4, "i">>};

	rel[stat,def] RES = reachingDefinitions(DEFS, PRED);
	println("RES = <RES>");


	return RES ==   {<2, <1, "i">>, <3, <2, "j">>, <3, <1, "i">>, <4, <3, "a">>, 
                     <4, <2, "j">>, <4, <1, "i">>, <4, <7, "i">>, <4, <5, "j">>, 
                     <4, <6, "a">>, <5, <4, "i">>, <5, <3, "a">>, <5, <2, "j">>, 
                     <5, <5, "j">>, <5, <6, "a">>, <6, <5, "j">>, <6, <4, "i">>, 
                     <6, <3, "a">>, <6, <6, "a">>, <7, <5, "j">>, <7, <4, "i">>, 
                     <7, <3, "a">>, <7, <6, "a">>};

/* assert "OUT": OUT == {<1, <1, "i">>, <2, <2, "j">>, <2, <1, "i">>, <3, <3, "a">>,
                      <3, <2, "j">>, <3, <1, "i">>, <4, <4, "i">>, <4, <3, "a">>,
                      <4, <2, "j">>, <4, <5, "j">>, <4, <6, "a">>, <5, <5, "j">>,
                      <5, <4, "i">>, <5, <3, "a">>, <5, <6, "a">>, <6, <6, "a">>,
                      <6, <5, "j">>, <6, <4, "i">>, <7, <7, "i">>, <7, <5, "j">>,
                      <7, <3, "a">>, <7, <6, "a">>};
*/
                      
}

bool testReaching2(){

// Example from Aho, Sethi, Ullman, p619

//	1: i := m-2				i := m-2
//        |
//  2: j := n				j := n
//        |
//  3: a := u1				a := u1
//	      |					do
//	      V
//	4: i :=i+1  <--------------			i := i+1
//        |                        |
//      5: j :=j-1                 |			j := j-1
// 	 /          \                  |			if e1 then
//	/            \                 |			   a := u2
//  V              V               |			else
//  6: a := u2 ---> 7: i := u3 ---			   i := u3
//						                while e2

	int ROOT = 1;

	rel[int,int] PRED= { <1,2>, <2,3>, <3,4>, <4,5>, <5,6>,<5,7>,<6,7>,<7,4>};

	rel[str,int] DEF= { <"i",1>,<"j",2>,<"a",3>,<"i",4>,<"j",5>,<"a",6>,<"i",7>};

	rel[str,int] USE= {<"m",1>,<"n",2>,<"u1",3>,<"i",4>,<"j",5>,<"u2",6>,<"u3",7>};

	return true;
}

public bool testLive(){

	rel[stat,stat] PRED = { <1,2>, <2,3>, <3,4>, <4,5>, <5,6>, <5,7>, <6,7>, <7,4>};
	rel[stat, var] DEFS = { <1, "i">, <2, "j">, <3, "a">, <4, "i">, <5, "j">, <6, "a">, <7, "i">};
	rel[stat, var] USES = { <1, "m">, <2, "n">, <3, "u1">, <4, "i">, <5, "j">, <6, "u2">, <7, "u3">};
	
	return liveVariables(DEFS, USES, PRED) ==
	 	      {<1, <6, "u2">>, <1, <7, "u3">>, <1, <5, "j">>, <1, <4, "i">>,
		       <1, <3, "u1">>, <1, <2, "n">>, <1, <1, "m">>, 
		       <2, <7, "u3">>, <2, <6, "u2">>, <2, <5, "j">>, <2, <4, "i">>, 
		       <2, <3, "u1">>, <2, <2, "n">>, 
		       <3, <7, "u3">>, <3, <6, "u2">>, <3, <5, "j">>, <3, <4, "i">>, 
		       <3, <3, "u1">>, 
		       <5, <4, "i">>, <5, <7, "u3">>, <5, <6, "u2">>, <5, <5, "j">>, 
		       <6, <5, "j">>, <6, <4, "i">>, <6, <7, "u3">>, <6, <6, "u2">>, 
		       <7, <6, "u2">>, <7, <5, "j">>, <7, <4, "i">>, <7, <7, "u3">>, 
		       <4, <7, "u3">>, <4, <6, "u2">>, <4, <5, "j">>, <4, <4, "i">>};
}

public bool test(){
	return testReaching1() && testReaching2() && testLive();
}
