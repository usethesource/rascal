@bootstrapParser
module lang::rascalcore::check::tests::AllStaticIssuesUsingStdLib

import lang::rascalcore::check::tests::StaticTestingUtils;
	
// 	https://github.com/cwi-swat/rascal/issues/448

test bool Issue448a() =
	checkOK("true;", importedModules = ["Exception", "List"],
					 initialDecls = ["bool tstMapper(list[int] L) {
  										int incr(int x) { return x + 1; };
  										return mapper(L, incr) == [x + 1 | x \<- L];
									  }"]);
	
test bool Issue448b() =
	checkOK("true;", importedModules = ["Exception", "List"],
 					 initialDecls =    ["list[&U] mapper(tuple[list[&T] lst, &U (&T) fun] t) = [ t.fun(elem) | elem \<- t.lst ];",

										"value tstMapper(list[int] L) {
  											int incr(int x) { return x + 1; };
  											return mapper(\<L, incr\>);
										 }"]);	
										 
// https://github.com/cwi-swat/rascal/issues/449	

test bool Issue449() =
	checkOK("true;", importedModules = ["Exception", "ParseTree"],
					 initialDecls =   ["syntax A = a: \"a\";",

										"test bool tstA(){
    										pt = parse(#A, \"a\");
    										return a() := pt && pt is a;
										 }"]);
										 
// https://github.com/cwi-swat/rascal/issues/450

test bool Issue450() =
	checkOK("true;", importedModules = ["Exception", "List", "ParseTree"],
					 initialDecls =   ["syntax A = a: \"a\";",
					 
					 					"syntax As = as: A+ alist;",

										"syntax C = c: A a \"x\" As as;",

										"test bool tstA(){
   										 pt = [A] \"a\";
   											 return a() := pt ;
										}"]);

// https://github.com/cwi-swat/rascal/issues/452

test bool Issue452() =
	checkOK("true;", importedModules = ["ParseTree"]);

// https://github.com/cwi-swat/rascal/issues/459

test bool Issue459() =
	checkOK("true;",  importedModules = ["Exception", "Set"],
					initialDecls = ["value main() = toMap({});"]);

// https://github.com/cwi-swat/rascal/issues/467

test bool Issue467() =
	checkOK("true;",  importedModules = ["Exception", "List"]);
																	 


// https://github.com/cwi-swat/rascal/issues/481

@ignore{temporarily disabled due to recent type-checker issue (JV/PK June 1st 2021)}
test bool Issue481() =
	checkOK("true;", importedModules=["ParseTree"],
					initialDecls = ["syntax A = a: \"a\";",

									"syntax As = as: A+ alist;",

									"syntax C = c: A a \"x\" As as;",

									"bool tstAs(){
    									pt = parse(#As, \"aaa\");
    									return as(al) := pt && pt is as && pt.alist == al;
									}",
									
									"bool tstC(){
    									pt = parse(#C, \"axaaa\");
    									return c(A a, As as) := pt && pt.a == a && pt.as == as && size([x | x \<- as.alist]) == 3;
									}"]);

	
// https://github.com/cwi-swat/rascal/issues/483
@ignore
test bool Issue483() =                                       // TODO: it is possible that there are also real errors in Ambiguity
	checkModuleOK(|std:///analysis::grammars::Ambiguity.rsc|);
	
	
// https://github.com/cwi-swat/rascal/issues/491
@ignore{TODO}
test bool Issue491() =
	checkOK("true;", importedModules=[" util::Math"],
					initialDecls = ["public map[&T \<: num, int] distribution(rel[&U event, &T \<: num bucket] input, &T \<: num bucketSize) {
  										map[&T,int] result = ();
  										for (\<&U event, &T bucket\> \<- input) {
    										result[round(bucket, bucketSize)]?0 += 1;
  										}
 										return result;
									}"]);
	
// https://github.com/cwi-swat/rascal/issues/492
// https://github.com/cwi-swat/rascal/issues/493

// These tests are related to the Exp demo. We will test that separately

// https://github.com/cwi-swat/rascal/issues/494
	
//test bool Issue494() =                                       // TODO
//	checkModuleOK(|std:///demo/lang/Func/Test.rsc|);
	
// https://github.com/cwi-swat/rascal/issues/495

//test bool Issue495() =
//	checkModuleOK(|std:///demo/lang/Func/Parse.rsc|);

// https://github.com/cwi-swat/rascal/issues/496
@ignore
test bool Issue496a() = 
	checkModuleOK(|std:///lang/java/m3/AST.rsc|);

@ignore
test bool Issue496b(){
	makeModule("MMM", "import lang::java::m3::AST;
					 import analysis::m3::TypeSymbol;");
	return checkOK("true;", importedModules=["MMM"]);
}
@ignore
test bool Issue496c(){
	makeModule("MMM", "import lang::java::m3::AST;
					 import analysis::m3::TypeSymbol;
					 import Message;");
	return checkOK("true;", importedModules=["MMM"]);
}

// https://github.com/cwi-swat/rascal/issues/502

test bool Issue502(){
	makeModule("MMM", "import Type;
					 lexical Num = \\int: [0-9]+;");
	return checkOK("true;", importedModules=["MMM"]);
}
	
// https://github.com/cwi-swat/rascal/issues/503

test bool Issue503(){												
	makeModule("M1", "import M2;
				  	  import ParseTree;");		 
	makeModule("M2", "extend ParseTree;");
	return checkOK("true;", importedModules=["M1", "M2"]);
}	
	
// https://github.com/cwi-swat/rascal/issues/547

test bool Issue547(){												
	makeModule("M1", "import M2;");		 
	makeModule("M2", "import Type;
					  public data MuExp = muCallJava( str name, Symbol parameterTypes);");
	return checkOK("true;", importedModules=["M1", "M2"]);
}

// https://github.com/cwi-swat/rascal/issues/550
@ignore
test bool Issue550(){												
	makeModule("M1", "import lang::rascal::\\syntax::Rascal;

						public int tmpVar = -1;  
						
						public str nextTmp(){
						    tmpVar += 1;
						    return \"TMP\<tmpVar\>\";
						}
						
						str getLabel(Label label) =
						  (label is \\default) ? \"\<label.name\>\" : nextTmp();");		 
	makeModule("M2", "import M1;");
	return checkOK("true;", importedModules=["M1", "M2"]);
}