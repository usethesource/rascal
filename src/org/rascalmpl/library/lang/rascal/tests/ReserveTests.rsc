@doc{
This module collects all standard library modules which currently
have embedded tests to run. These are not supported by the typechecker
and compiler yet, so they are on the reserve list. In the future we will
run all tests for all modules in the standard library in the test suite.
} 
@license{
 Copyright (c) 2009-2014 CWI
 All rights reserved. This program and the accompanying materials
 are made available under the terms of the Eclipse License v1.0
 which accompanies this distribution, and is available at
 http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module lang::rascal::tests::ReserveTests

import demo::basic::Ackermann;
import demo::basic::Bubble;
import demo::basic::Factorial;
import demo::common::Calls;
import demo::common::ColoredTrees;
import demo::common::CountConstructors;
import demo::common::Cycles;
import demo::common::Derivative;
import demo::common::Lift;
import demo::common::StringTemplate;
import demo::common::Trans;
import demo::common::WordReplacement;
import demo::common::WordCount::CountInLine1;
import demo::common::WordCount::CountInLine2;
import demo::common::WordCount::CountInLine3;
import demo::common::WordCount::WordCount;
import demo::Dominators;
import demo::lang::Exp::Abstract::Eval;
import demo::lang::Exp::Combined::Automatic::Eval;
import demo::lang::Exp::Combined::Manual::Eval;
import demo::lang::Exp::Concrete::NoLayout::Eval;
import demo::lang::Exp::Concrete::WithLayout::Eval;
import demo::lang::Func::Test;
import demo::lang::Lisra::Test;
import demo::McCabe;
import demo::ReachingDefs;
import demo::Slicing;
import demo::Uninit;
import lang::rascal::\format::Escape;
import lang::rascal::\format::Grammar;
import lang::rascal::grammar::definition::Characters;
import lang::rascal::grammar::definition::Literals;
import lang::rascal::grammar::Lookahead;
import lang::rascal::grammar::ParserGenerator;
import lang::rascal::\syntax::tests::ConcreteSyntax;
import lang::rascal::\syntax::tests::ExpressionGrammars;
import lang::rascal::\syntax::tests::ImplodeTests;
import lang::rascal::\syntax::tests::KnownIssues;
import lang::rascal::\syntax::tests::ParsingRegressionTests;
import lang::rascal::\syntax::tests::PreBootstrap;
import lang::rascal::\syntax::tests::SolvedIssues;
import lang::yaml::Model;
import util::PriorityQueue;
import util::UUID;
