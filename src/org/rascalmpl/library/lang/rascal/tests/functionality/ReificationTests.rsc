@contributor{Jurgen Vinju}
module lang::rascal::tests::functionality::ReificationTests

import ParseTree;

test bool c() = #str.symbol == \str();
test bool i() = #int.symbol == \int();
test bool r() = #real.symbol == \real();
test bool n() = #num.symbol == \num();
test bool n() = #node.symbol == \node();
test bool v() = #void.symbol == \void();
test bool vl() = #value.symbol == \value();
test bool l() = #list[int].symbol == \list(\int());
test bool s() = #set[int].symbol == \set(\int());
test bool m() = #map[int,str].symbol == \map(\int(),\str());
test bool m() = #map[int k,str v].symbol == \map(label("k",\int()),label("v",\str()));
test bool f() = #int (int).symbol == \func(\int(),[\int()],[]);
test bool p() = #&T <: list[&U].symbol == \parameter("T", \list(\parameter("U",\value())));

@ignoreCompiler
test bool relLabels() = #rel[int a, int b].symbol == \set(\tuple([label("a", \int()),label("b", \int())]));

@ignoreInterpreter
test bool relLabels() = #rel[int a, int b].symbol == \rel([label("a", \int()),label("b", \int())]);

test bool everyTypeCanBeReifiedWithoutExceptions(&T u) = _ := typeOf(u);

data P = prop(str name) | and(P l, P r) | or(P l, P r) | not(P a) | t() | f();

test bool allConstructorsAreDefined() 
  = (0 | it + 1 | /cons(_,_,_,_) := #P.definitions) == 6;

test bool allConstructorsForAnAlternativeDefineTheSameSort() 
  = !(/choice(def, /cons(label(_,def),_,_,_)) !:= #P.definitions);
  
test bool typeParameterReificationIsStatic1(&F f) = #&F.symbol == \parameter("F",\value());
test bool typeParameterReificationIsStatic2(list[&F] f) = #list[&F].symbol == \list(\parameter("F",\value()));

@ignore{issue #1007}
test bool typeParameterReificationIsStatic3(&T <: list[&F] f) = #&T.symbol == \parameter("T", \list(\parameter("F",\value())));

test bool dynamicTypesAreAlwaysGeneric(value v) = !(type[value] _ !:= type(typeOf(v),()));

// New tests which can be enabled after succesful bootstrap
//data P(int size = 0);
//data P = axiom(P mine = t());
//
//test bool allConstructorsHaveTheCommonKwParam()
//  =  all(/choice(def, /cons(_,_,kws,_)) := #P.definitions, label(\int(),"size") in kws);
//  
//test bool axiomHasItsKwParam()
//  =  /cons(label(_,"axiom"),_,kws,_) := #P.definitions && label(\adt("P",[]),"mine") in kws;  
//  
//test bool axiomsKwParamIsExclusive()
//  =  all(/cons(label(_,!"axiom"),_,kws,_) := #P.definitions, label(\adt("P",[]),"mine") notin kws);
//  
  
  
  
  