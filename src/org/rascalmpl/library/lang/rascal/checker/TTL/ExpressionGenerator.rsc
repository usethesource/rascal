module lang::rascal::checker::TTL::ExpressionGenerator

import Prelude;
import Type;

import cobra::arbitrary;
import cobra::quickcheck;
import lang::rascal::checker::TTL::Library;
extend lang::rascal::checker::TTL::TTLsyntax;
import util::Math;

alias SymbolPair = tuple[Symbol l, Symbol r];
alias BinarySig = tuple[str operator, Symbol left, Symbol right];
alias UnarySig = tuple[str operator, Symbol left];

void generateSignatures(list[TestItem] infix, list[TestItem] prefix, list[TestItem] postfix){
   infix1 = intercalate(",\n", ["\<<operator>,  <toSymbolAsStr(sig.result)>, [<toSymbolAsStr(sig.left)>, <toSymbolAsStr(sig.right)>], false\>" | item <- infix, operator <- item.operators, sig <-item.bin_signatures]);
   prefix1 = intercalate(",\n", ["\<<operator>,  <toSymbolAsStr(sig.result)>, [<toSymbolAsStr(sig.left)>], false\>" | item <- prefix, operator <- item.operators, sig <-item.un_signatures]);
   postfix1 = intercalate(",\n", ["\<<operator>,  <toSymbolAsStr(sig.result)>, [<toSymbolAsStr(sig.left)>], true\>" | item <- postfix, operator <- item.operators, sig <-item.un_signatures]);

   code = "module <modulePrefix>::Signatures
          'import Type;
          'import lang::rascal::checker::TTL::TTLsyntax;
          'import lang::rascal::checker::TTL::ExpressionGenerator;
   	      'public lrel[str op, Symbol result, list[Symbol] args, bool postfix] signatures = fixTypeParameters([<infix1>, <prefix1>, <postfix1>]);
          ";
   
    writeFile(TTLRoot + "generated/Signatures.rsc", code);
}

public lrel[str op, Symbol result, list[Symbol] args, bool postfix] fixTypeParameters( lrel[str op, Symbol result, list[Symbol] args, bool postfix] signatures){
  return
    for(<op, result, args, postfix> <- signatures){
      env = (() | least(it, getParameters(s)) | s <- [result, *args]);
      append <op, replaceParameters(result, env), [replaceParameters(a, env) |a <- args], postfix>;
  }
}

map[str, Symbol] getParameters(Symbol t) { 
  res = ();
  visit(t){ case parameter(N1, T1): res[N1] = T1; };
  return res;
}

Symbol replaceParameters(Symbol t, map[str, Symbol] tenv) =
   visit(t){ case parameter(N1, T1) => parameter(N1, tenv[N1] ? T1) };

Symbol least(Symbol l, Symbol r) = subtype(l, r) ? l : r;

map[str, Symbol] least(map[str, Symbol] env1, map[str, Symbol] env2){
  renv = ();
  for(str name <- env1){
      renv[name] = env2[name]? ? least(env1[name], env2[name]) : env1[name];
  }
  for(str name <- env2){
      if(!env1[name]?)
         renv[name] = env2[name];
  }
  return renv;
}

lrel[str op, Symbol result, list[Symbol] args, bool postfix] SIGNATURES;
bool errorGenerated = false;

str generateExpression(type[&T] t, lrel[str op, Symbol result, list[Symbol] args, bool postfix] signatures, bool correct){
  SIGNATURES = signatures;
  errorGenerated = false;
  e = generateExpression(t.symbol, 0.0, correct);
  retry = 1;
  while(!correct && !errorGenerated && retry < 5){
     //println("RETRY #<retry>: <t>");
     errorGenerated = false;
     e = generateExpression(t.symbol, 0.0, correct);
     retry += 1;
  }
  return !correct && !errorGenerated ? arbNonVoidNonEqual(t.symbol) + ";" : e + ";";
}

list[&T] permute(list[&T] lst) {
  res = [];
  while(size(lst) > 0){
    <el, lst> = takeOneFrom(lst);
    res += el;
  }
  return res;
}

str generateExpression(Symbol t, real valueProbability, bool correct){
   //println("generateExpression: <t>, <valueProbability>, <correct>");
   if(arbReal(0.0, 1.0) > valueProbability){
       vp = valueProbability + 0.2;
	   for(sig <- permute(SIGNATURES)){
	       <m, env> = canMatch(t, sig.result, ());
	       if(m){
	          //println("sig = <sig>");
	          if(size(sig.args) == 2){
	             <left_type, env> = instantiate(sig.args[0], env);
	             <right_type, env> = instantiate(sig.args[1], env);
	             return "(<generateArg(left_type, vp, correct)>) <sig.op> (<generateArg(right_type, vp, correct)>)";
	          } else {
	             <left_type, env> = instantiate(sig.args[0], env);
	             e = generateArg(left_type, vp, correct);
	             return sig.postfix ? "(<e>) <sig.op>" : "<sig.op> (<e>)";
	         }
	      }
	   }
   }
   return arb(t);
}

tuple[Symbol, map[str, Symbol]] instantiate(Symbol t, map[str, Symbol] env){
  t = visit(t) { case Symbol::\parameter(str name, Symbol s): {
                if(env[name]?)
                   insert env[name];
                else {
                   nt = arbNonVoidType();
                   env[name] = nt;
                   insert nt;
                }
             }
           };
   return <t, env>;       
}

str generateArg(Symbol t,  real valueProbability, bool correct){
    if(!correct && arbInt(10) < 2){
       errorGenerated = true;
       return arbNonEqual(t);          
    } else {
	   return generateExpression(t, valueProbability, correct);
    }
}

str arb(Symbol t){
  g = getGenerator(type(t, ()));
  v = g(5);
  nt = typeOf(v);
  return /Symbol::\void() := nt || "<v>" == "" ? arb(t) : "<escape(v)>";
}

str arbNonEqual(Symbol t){
  g = getGenerator(#value);
  v = g(5);
  while(typeOf(v) == t || "<v>" == ""){
    v = g(5);
  }
  return "<escape(v)>";
}

str arbNonVoidNonEqual(Symbol t){
  g = getGenerator(#value);
  v = g(5);
  while(typeOf(v) == t ||  /Symbol::\void() := typeOf(v) || "<v>" == ""){
    v = g(5);
  }
  return "<escape(v)>";
}

Symbol arbNonVoidType(){
  g = getGenerator(#value);
  nt = typeOf(g(5));
  while(/Symbol::\void() := nt){
     nt = typeOf(g(5));
  }
  return nt;
}

alias TE = map[str name, Symbol typ];
alias MR = tuple[bool matches,TE tenv];

MR canMatch(\list(Symbol l), \list(Symbol r), TE tenv) = canMatch(l, r, tenv);

MR canMatch(\set(Symbol l), \set(Symbol r), TE tenv) = canMatch(l, r, tenv);

MR canMatch(\map(k1, v1), \map(k2, v2), TE tenv) {
   <m1, tenv1> = canMatch(k1, k2, tenv);
   return m1 ? canMatch(v1, v2, tenv1) : <false, tenv>;
}

MR canMatch(\rel(list[Symbol] ts1), \rel(list[Symbol] ts2), TE tenv) = canMatch(ts1, ts2, tenv);

MR canMatch(\lrel(list[Symbol] ts1), \rel(list[Symbol] ts2), TE tenv) = canMatch(ts1, ts2, tenv);

MR canMatch(Symbol::\tuple(list[Symbol] ts1), Symbol::\tuple(list[Symbol] ts2), TE tenv) = canMatch(ts1, ts2, tenv);

MR canMatch([], [], TE tenv) = <true,  tenv>;
MR canMatch([Symbol l], [Symbol r], TE tenv) = canMatch(l, r, tenv);
MR canMatch([Symbol l, *Symbol ls], [Symbol r, *Symbol rs], TE tenv) {
   <m1, tenv1> = canMatch(l, r, tenv);
   return m1 ? canMatch(ls, rs, tenv1) : <false, tenv>;
}


MR canMatch(Symbol l, \LUB(\parameter(N1, \value()), \parameter(N2, \value())), TE tenv) {
   rl = rlub(l);
   if(isEmpty(rl))
      return <false, ()>;
   <s1, s2> = getOneFrom(rl);
   return <true, (N1 : s1, N2 : s2) + tenv>;
}

MR canMatch(Symbol l, \parameter(N1, \value()), TE tenv) {
   if(tenv[N1]?)
      return canMatch(l, tenv[N1], tenv);
   return <true, tenv + (N1 : l)>;
}

default MR canMatch(Symbol l, Symbol r, TE tenv) = <l == r, tenv>;


set[SymbolPair] rlub(Symbol::\real()) = {/* <\void(), \real()>,<\real(), \void()>,*/ <Symbol::\int(), Symbol::\real()>, <Symbol::\real(), Symbol::\int()>, <Symbol::\rat(), Symbol::\real()>, <Symbol::\real(), Symbol::\rat()>, <Symbol::\real(), Symbol::\real()>};
set[SymbolPair] rlub(Symbol::\rat()) = {/* <Symbol::\void(), Symbol::\rat()>,<Symbol::\rat(), Symbol::\void()>,*/ <Symbol::\int(), Symbol::\rat()>, <Symbol::\rat(), Symbol::\int()>, <Symbol::\rat(), Symbol::\rat()>};

set[SymbolPair] rlub(Symbol::\num()) = {/*<Symbol::\void(), Symbol::\num()>,<Symbol::\num(), Symbol::\void()>,*/ <Symbol::\int(), Symbol::\num()>, <Symbol::\rat(), Symbol::\num()>, <Symbol::\real(), Symbol::\num()>, <Symbol::\num(), Symbol::\int()>, <Symbol::\num(), Symbol::\rat()>, <Symbol::\num(), Symbol::\real()>, <Symbol::\num(), Symbol::\num()>};

set[SymbolPair] rlub(Symbol::\set(Symbol s)) = /*{ <Symbol::\void(), Symbol::\set(s)>, <Symbol::\set(s), Symbol::\void()>} + */ {<Symbol::\set(l), Symbol::\set(r)> | <l, r> <- rlub(s)};
set[SymbolPair] rlub(Symbol::\rel(ts)) = /*{<Symbol::\void(), Symbol::\rel(ts)>, <Symbol::\rel(ts), Symbol::\void()>} + */ {<Symbol::\rel([l]), Symbol::\rel([r])> | a <- rlub(ts), <l, r> <- a };

set[SymbolPair] rlub(Symbol::\list(Symbol s)) = /* {<Symbol::\void(), Symbol::\list(s)>, <Symbol::\list(s), Symbol::\void()>} + */{<Symbol::\list(l), Symbol::\list(r)> | <l, r> <- rlub(s)};
set[SymbolPair] rlub(Symbol::\lrel(list[Symbol] ls)) = /* {<Symbol::\void(), Symbol::\lrel(s)>, <Symbol::\lrel(s), Symbol::\void()>} + */ {<Symbol::\lrel([l]), Symbol::\lrel([r])> | a <- rlub(ls), <Symbol l, Symbol r> <- a};

set[SymbolPair] rlub(Symbol::\map(Symbol k, Symbol v)) = /* {<Symbol::\void(), Symbol::\map(k, v)>, <Symbol::\map(k, v), Symbol::\void()>} + */ {<Symbol::\map(kl, vl), Symbol::\map(kr, vr)> | <kl, kr> <- rlub(k), <vl, vr> <- rlub(v) };

set[SymbolPair] rlub(Symbol::\tuple(list[Symbol] ts)) = /* {<Symbol::\void(), Symbol::\tuple(ts)>, <Symbol::\tuple(ts), Symbol::\void()>} + */ {<Symbol::\tuple([l]), Symbol::\tuple([r])> | a <- rlub(ts), <l, r> <- a};

default set[SymbolPair] rlub(Symbol s) = {<s, s>};

list[set[SymbolPair]] rlub([]) = [];
list[set[SymbolPair]] rlub([Symbol s]) = [rlub(s)];
list[set[SymbolPair]] rlub([Symbol s, *Symbol sl]) = [rlub(s), *rlub(sl)];


