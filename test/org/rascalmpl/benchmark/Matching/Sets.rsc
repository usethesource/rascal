module Matching::Sets

import IO;
import Math;
import util::Benchmark;

import Grammar;import ParseTree;import IO;

private set[int] genset(int n){
   return { arbInt() | int i <- [0 .. n+1] };
}

public int fac(int N)
{
	if(N <= 0)
		return 1;
	else
		return N * fac(N - 1);
}


public void m(){
   N = 1000;
   for(int i <- [0, 100 .. N]){
       S = genset(i);
       begin = realTime();
	   for(int j <- [1 .. 10000]){
	       {L1*, L} := S;
	   }
	   used = (realTime() - begin);
       println("<i>:\t<used>");
   }
      
}



public set[Condition] expandKeywords(Grammar g, set[Condition] conds)
{  
   	done = {};  
   	println("expanding a set of <conds>");  
	// find any condition defined by a keyword sort  
	// we use '/' to skip over 'meta' wrappers  
	while ({other*, cond} := conds, keywords(name) := cond.symbol) {    
		// now look up the definition of the keyword sort and weave it in.    
		conds = other + {cond[symbol=s] | choice(_, alts) := g.rules[cond.symbol], prod(_,[s],_) <- alts};    
		println("conds: <conds>");  
	}
    println("done with <conds>");  return conds;  
}

// g is the original (no termination observed)
public Grammar g =
grammar({},(keywords("RascalKeywords"):choice(keywords("RascalKeywords"),{prod(keywords("RascalKeywords"),[lit("tuple")],{}),prod(keywords("RascalKeywords"),[lit("constructor")],{}),prod(keywords("RascalKeywords"),[lit("int")],{}),prod(keywords("RascalKeywords"),[lit("fail")],{}),prod(keywords("RascalKeywords"),[lit("switch")],{}),prod(keywords("RascalKeywords"),[lit("rule")],{}),prod(keywords("RascalKeywords"),[lit("throw")],{}),prod(keywords("RascalKeywords"),[lit("alias")],{}),prod(keywords("RascalKeywords"),[lit("default")],{}),prod(keywords("RascalKeywords"),[lit("throws")],{}),prod(keywords("RascalKeywords"),[lit("module")],{}),prod(keywords("RascalKeywords"),[lit("private")],{}),prod(keywords("RascalKeywords"),[lit("true")],{}),prod(keywords("RascalKeywords"),[lit("map")],{}),prod(keywords("RascalKeywords"),[lit("test")],{}),prod(keywords("RascalKeywords"),[lit("start")],{}),prod(keywords("RascalKeywords"),[lit("import")],{}),prod(keywords("RascalKeywords"),[lit("loc")],{}),prod(keywords("RascalKeywords"),[lit("assert")],{}),prod(keywords("RascalKeywords"),[lit("insert")],{}),prod(keywords("RascalKeywords"),[lit("anno")],{}),prod(keywords("RascalKeywords"),[lit("public")],{}),prod(keywords("RascalKeywords"),[lit("void")],{}),prod(keywords("RascalKeywords"),[lit("try")],{}),prod(keywords("RascalKeywords"),[lit("value")],{}),prod(keywords("RascalKeywords"),[lit("non-terminal")],{}),prod(keywords("RascalKeywords"),[lit("list")],{}),prod(keywords("RascalKeywords"),[lit("dynamic")],{}),prod(keywords("RascalKeywords"),[lit("tag")],{}),prod(keywords("RascalKeywords"),[lit("data")],{}),prod(keywords("RascalKeywords"),[lit("extend")],{}),prod(keywords("RascalKeywords"),[lit("append")],{}),prod(keywords("RascalKeywords"),[lit("notin")],{}),prod(keywords("RascalKeywords"),[lit("type")],{}),prod(keywords("RascalKeywords"),[lit("catch")],{}),prod(keywords("RascalKeywords"),[lit("one")],{}),prod(keywords("RascalKeywords"),[lit("node")],{}),prod(keywords("RascalKeywords"),[lit("str")],{}),prod(keywords("RascalKeywords"),[lit("repeat")],{}),prod(keywords("RascalKeywords"),[lit("visit")],{}),prod(keywords("RascalKeywords"),[lit("fun")],{}),prod(keywords("RascalKeywords"),[lit("non-assoc")],{}),prod(keywords("RascalKeywords"),[lit("if")],{}),prod(keywords("RascalKeywords"),[lit("return")],{}),prod(keywords("RascalKeywords"),[lit("else")],{}),prod(keywords("RascalKeywords"),[lit("in")],{}),prod(keywords("RascalKeywords"),[lit("it")],{}),prod(keywords("RascalKeywords"),[lit("join")],{}),prod(keywords("RascalKeywords"),[lit("for")],{}),prod(keywords("RascalKeywords"),[lit("continue")],{}),prod(keywords("RascalKeywords"),[lit("bracket")],{}),prod(keywords("RascalKeywords"),[lit("set")],{}),prod(keywords("RascalKeywords"),[lit("assoc")],{}),prod(keywords("RascalKeywords"),[lit("bag")],{}),prod(keywords("RascalKeywords"),[lit("num")],{}),prod(keywords("RascalKeywords"),[lit("datetime")],{}),prod(keywords("RascalKeywords"),[lit("filter")],{}),prod(keywords("RascalKeywords"),[lit("while")],{}),prod(keywords("RascalKeywords"),[lit("case")],{}),prod(keywords("RascalKeywords"),[lit("layout")],{}),prod(keywords("RascalKeywords"),[lit("bool")],{}),prod(keywords("RascalKeywords"),[lit("any")],{}),prod(keywords("RascalKeywords"),[lit("finally")],{}),prod(keywords("RascalKeywords"),[lit("real")],{}),prod(keywords("RascalKeywords"),[lit("all")],{}),prod(keywords("RascalKeywords"),[lit("false")],{}),prod(keywords("RascalKeywords"),[lit("break")],{}),prod(keywords("RascalKeywords"),[lit("rel")],{}),prod(keywords("RascalKeywords"),[sort("BasicType")],{}),prod(keywords("RascalKeywords"),[lit("adt")],{}),prod(keywords("RascalKeywords"),[lit("solve")],{}),prod(keywords("RascalKeywords"),[lit("rat")],{})})));

// g2 is a much smaller version (terminates quickly)
public Grammar g2
= grammar({},(keywords("RascalKeywords"):choice(keywords("RascalKeywords"),{prod(keywords("RascalKeywords"),[lit("tuple")],{}),prod(keywords("RascalKeywords"),[lit("constructor")],{}),prod(keywords("RascalKeywords"),[lit("int")],{}),prod(keywords("RascalKeywords"),[lit("fail")],{}),prod(keywords("RascalKeywords"),[lit("switch")],{}),prod(keywords("RascalKeywords"),[lit("rule")],{})})));

// g3 is larger than g2 (terminates)

public Grammar g3 =
grammar({},(keywords("RascalKeywords"):choice(keywords("RascalKeywords"),{prod(keywords("RascalKeywords"),[lit("tuple")],{}),prod(keywords("RascalKeywords"),[lit("constructor")],{}),prod(keywords("RascalKeywords"),[lit("int")],{}),prod(keywords("RascalKeywords"),[lit("fail")],{}),prod(keywords("RascalKeywords"),[lit("switch")],{}),prod(keywords("RascalKeywords"),[lit("rule")],{}),prod(keywords("RascalKeywords"),[lit("throw")],{}),prod(keywords("RascalKeywords"),[lit("alias")],{}),prod(keywords("RascalKeywords"),[lit("default")],{}),prod(keywords("RascalKeywords"),[lit("throws")],{}),prod(keywords("RascalKeywords"),[lit("module")],{}),prod(keywords("RascalKeywords"),[lit("private")],{})})));

// g4 is larger than g3 (does not terminate)
public Grammar g4 =
grammar({},(keywords("RascalKeywords"):choice(keywords("RascalKeywords"),{prod(keywords("RascalKeywords"),[lit("tuple")],{}),prod(keywords("RascalKeywords"),[lit("constructor")],{}),prod(keywords("RascalKeywords"),[lit("int")],{}),prod(keywords("RascalKeywords"),[lit("fail")],{}),prod(keywords("RascalKeywords"),[lit("switch")],{}),prod(keywords("RascalKeywords"),[lit("rule")],{}),prod(keywords("RascalKeywords"),[lit("throw")],{}),prod(keywords("RascalKeywords"),[lit("alias")],{}),prod(keywords("RascalKeywords"),[lit("default")],{}),prod(keywords("RascalKeywords"),[lit("throws")],{}),prod(keywords("RascalKeywords"),[lit("module")],{}),prod(keywords("RascalKeywords"),[lit("private")],{}),prod(keywords("RascalKeywords"),[lit("true")],{}),prod(keywords("RascalKeywords"),[lit("map")],{}),prod(keywords("RascalKeywords"),[lit("test")],{}),prod(keywords("RascalKeywords"),[lit("start")],{}),prod(keywords("RascalKeywords"),[lit("import")],{}),prod(keywords("RascalKeywords"),[lit("loc")],{}),prod(keywords("RascalKeywords"),[lit("assert")],{}),prod(keywords("RascalKeywords"),[lit("insert")],{}),prod(keywords("RascalKeywords"),[lit("anno")],{}),prod(keywords("RascalKeywords"),[lit("public")],{}),prod(keywords("RascalKeywords"),[lit("void")],{}),prod(keywords("RascalKeywords"),[lit("try")],{}),prod(keywords("RascalKeywords"),[lit("value")],{}),prod(keywords("RascalKeywords"),[lit("non-terminal")],{})})));

// g5 is in between g3 and g4 (terminates very very slowly...)

public
Grammar g5 = grammar({},(keywords("RascalKeywords"):choice(keywords("RascalKeywords"),{prod(keywords("RascalKeywords"),[lit("tuple")],{}),prod(keywords("RascalKeywords"),[lit("constructor")],{}),prod(keywords("RascalKeywords"),[lit("int")],{}),prod(keywords("RascalKeywords"),[lit("fail")],{}),prod(keywords("RascalKeywords"),[lit("switch")],{}),prod(keywords("RascalKeywords"),[lit("rule")],{}),prod(keywords("RascalKeywords"),[lit("throw")],{}),prod(keywords("RascalKeywords"),[lit("alias")],{}),prod(keywords("RascalKeywords"),[lit("default")],{}),prod(keywords("RascalKeywords"),[lit("throws")],{}),prod(keywords("RascalKeywords"),[lit("module")],{}),prod(keywords("RascalKeywords"),[lit("private")],{}),prod(keywords("RascalKeywords"),[lit("true")],{}),prod(keywords("RascalKeywords"),[lit("map")],{}),prod(keywords("RascalKeywords"),[lit("test")],{}),prod(keywords("RascalKeywords"),[lit("start")],{}),prod(keywords("RascalKeywords"),[lit("import")],{}),prod(keywords("RascalKeywords"),[lit("loc")],{}),prod(keywords("RascalKeywords"),[lit("assert")],{})})));
public Condition c = delete(keywords("RascalKeywords"));
public test bool testG() {  
	x = expandKeywords(g2, {c});  println("g2 terminates");    
	x = expandKeywords(g3, {c});  println("g3 terminates");    
	x = expandKeywords(g5, {c});  println("g5 terminates very sloweeeeellyy");    
	x = expandKeywords(g4, {c});  println("g4 terminates (usually does not)");   
	x = expandKeywords(g, {c});   println("g terminates (usually does not)");   
	return true;
}
