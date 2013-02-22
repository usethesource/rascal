@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI}
module lang::saf::ImplodeAlgebraically

import lang::saf::AST;
import lang::saf::Implode;
import lang::saf::Parse;
import lang::saf::SAF;

import ParseTree;
import ParseTreeToAdt;

public FFighter[Tree,Tree] parseToFighter(Tree tree, type[Fighter] \type = #Fighter) = parseToAdt(#FFighter[Tree,Tree], tree);
public FSpec[Tree,Tree,Tree,Tree] parseToSpec(Tree tree, type[Spec] \type = #Spec) = parseToAdt(#FSpec[Tree,Tree,Tree,Tree], tree);
public FCond[Tree,Tree] parseToCond(Tree tree, type[Cond] \type = #Cond) = parseToAdt(#FCond[Tree,Tree], tree);
public FAction[Tree,Tree] parseToAction(Tree tree, type[Action] \type = #Action) = parseToAdt(#FAction[Tree,Tree], tree);
public list[tuple[Tree,Tree]] parseToListOfSpec(Tree tree, type[list[Spec]] \type = #list[Spec]) = parseToList(tree);
public list[tuple[Tree,Tree]] parseToListOfString(Tree tree, type[list[str]] \type = #list[str]) = parseToList(tree);
public int parseToInteger(Tree tree, type[int] \type = #int) = parseToInt(tree);
public str parseToString(Tree tree, type[str] \type = #str) = parseToStr(tree);

private list[str] fighters = [
	"Challenging { kickReach = 15 stronger [choose(jump run_away) choose(kick_low block_low)] far or much_weaker [choose(crouch run_towards) choose(punch_low punch_high)] always [crouch kick_low]}"
 	,
 	"chicken { kickReach  = 9 punchReach = 1 kickPower  = 2 punchPower = 2 far [run_towards kick_low] near [run_away kick_low] near [crouch punch_low] }"
 	,
	"chuck { kickReach = 1 punchReach = 1 punchPower = 9 kickPower = 9 always [walk_towards punch_high] near [jump kick_low] }"
	,
	"JackieChan { kickPower = 7 punchPower = 5 kickReach = 3 punchReach = 9 far [run_towards punch_high] near [choose(stand crouch) kick_high] much_stronger [walk_towards punch_low] weaker [run_away choose(block_high block_low)] always [walk_towards block_high] }"
	,
	"Kicking { kickPower = 10 always [walk_towards kick_high] }"
	,
	"unbeatable { punchReach = 9 even [choose(crouch walk_towards) choose(block_high punch_low)] always [crouch block_low] }"
	];

@doc{Tests the rascal algebraic implode against the java implode}
public list[Fighter] test0() {
	list[Tree] fightersTrees = [ parse(ftr) | str ftr <- fighters ];
	
	// Trees to adts with the java implode
	list[Fighter] fightersAsts = [ implode(#Fighter, ftr) | Tree ftr <- fightersTrees ];
	
	// Trees to adts with the algebraic visit based implode
	Fighter (Tree) f = fvisit[<#Fighter>, <parseToFighter, parseToSpec, parseToCond, parseToAction, parseToListOfSpec, parseToListOfString, parseToInteger, parseToString>];
	list[Fighter] fightersAstsAlgebraically = [ f(ftr) | Tree ftr <- fightersTrees ];
	
	assert(fightersAstsAlgebraically == fightersAsts);
	
	return fightersAstsAlgebraically;
}