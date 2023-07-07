@contributor{Jurgen Vinju}
@contributor{Paul Klint}
module lang::rascal::tests::functionality::Reification

import ParseTree;

data P = prop(str name) | and(P l, P r) | or(P l, P r) | not(P a) | t() | f() | axiom(P mine = t());
data D[&T] = d1(&T fld);

test bool reifyBool() = #bool.symbol == \bool();
test bool reifyStr() = #str.symbol == \str();
test bool reifyInt() = #int.symbol == \int();
test bool reifyReal() = #real.symbol == \real();
test bool reifyRat() = #rat.symbol == \rat();
test bool reifyNum() = #num.symbol == \num();
test bool reifyLoc() = #loc.symbol == \loc();
test bool reifyDatetime() = #datetime.symbol == \datetime();
test bool reifyNode() = #node.symbol == \node();
test bool reifyVoid() = #void.symbol == \void();
test bool reifyValue() = #value.symbol == \value();
test bool reifyList() = #list[int].symbol == \list(\int());
test bool reifySet() = #set[int].symbol == \set(\int());

@ignore{
Not implemented
}
test bool reifyBag() = #bag[int].symbol == \bag(\int());

test bool reifyMap() = #map[int,str].symbol == \map(\int(),\str());
test bool reifyMapWithLabels() = #map[int k,str v].symbol == \map(label("k",\int()),label("v",\str()));

test bool reifyFunction() = #int (int).symbol == \func(\int(),[\int()],[]);
test bool reifyFunctionWithLabel() = #int (int a).symbol == \func(\int(),[label("a", \int())],[]);

test bool reifyParameter() = #&T.symbol == \parameter("T", \value());
test bool reifyParameterWithBound() = #&T <: list[&U].symbol == \parameter("T", \list(\parameter("U",\value())));

test bool reifyTuple() = #tuple[int,str].symbol == \tuple([\int(),\str()]);
test bool reifyTupleWithLabels() = #tuple[int a,str b].symbol == \tuple([\label("a", \int()), \label("b", \str())]);

test bool reifyRel() = #rel[int, int].symbol == \set(\tuple([\int(),\int()]));
test bool reifyRelWithLabels() = #rel[int a, int b].symbol == \set(\tuple([label("a", \int()),label("b", \int())]));

test bool reifyLrel() = #lrel[int, int].symbol == \list(\tuple([\int(),\int()]));
test bool reifyLrelWithLabels() = #lrel[int a, int b].symbol == \list(\tuple([label("a", \int()),label("b", \int())]));

test bool reifyReified1() = #type[int].symbol == \reified(\int());
test bool reifyReified2() = #type[P].symbol == \reified(\adt("P", []));
test bool reifyReified3() = #type[D[int]].symbol == \reified(\adt("D", [\int()]));

test bool everyTypeCanBeReifiedWithoutExceptions(&T u) = _ := typeOf(u);

test bool allConstructorsAreDefined() 
  = (0 | it + 1 | /cons(_,_,_,_) := #P.definitions) == 7;

test bool allConstructorsForAnAlternativeDefineTheSameSort() 
  = !(/choice(def, /cons(label(_,def),_,_,_)) !:= #P.definitions);
  
test bool typeParameterReificationIsStatic1(&F _) = #&F.symbol == \parameter("F",\value());
test bool typeParameterReificationIsStatic2(list[&F] _) = #list[&F].symbol == \list(\parameter("F",\value()));

@ignore{
issue #1007
}
test bool typeParameterReificationIsStatic3(&T <: list[&F] f) = #&T.symbol == \parameter("T", \list(\parameter("F",\value())));

test bool dynamicTypesAreAlwaysGeneric(value v) = !(type[value] _ !:= type(typeOf(v),()));

// New tests which can be enabled after succesful bootstrap
data P(int size = 0);

@ignore{
Does not work after changed TypeReifier in compiler
}
test bool allConstructorsHaveTheCommonKwParam()
  =  all(/choice(def, /cons(_,_,kws,_)) := #P.definitions, label("size", \int()) in kws);
   
@ignoreCompiler{
Does not work after changed TypeReifier in compiler
}  
test bool axiomHasItsKwParam()
  =  /cons(label("axiom",_),_,kws,_) := #P.definitions && label("mine", \adt("P",[])) in kws;  

@ignore{
Does not work after changed TypeReifier in compiler
}  
test bool axiomsKwParamIsExclusive()
  =  all(/cons(label(!"axiom",_),_,kws,_) := #P.definitions, label("mine", \adt("P",[])) notin kws);
  
  
  
