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
// https://github.com/cwi-swat/rascal/issues/450
// https://github.com/cwi-swat/rascal/issues/481
// https://github.com/cwi-swat/rascal/issues/491
// https://github.com/cwi-swat/rascal/issues/502
// https://github.com/cwi-swat/rascal/issues/503
// https://github.com/cwi-swat/rascal/issues/547

test bool Issue547(){												
	writeModule("M1", "import M2;");		 
	writeModule("M2", "import Type;
					  public data MuExp = muCallJava( str name, Symbol parameterTypes);");
	return checkOK("true;", importedModules=["M1", "M2"]);
}

// https://github.com/cwi-swat/rascal/issues/550
test bool Issue550(){												
	writeModule("M1", "import lang::rascal::\\syntax::Rascal;

						public int tmpVar = -1;  
						
						public str nextTmp(){
						    tmpVar += 1;
						    return \"TMP\<tmpVar\>\";
						}
						
						str getLabel(Label label) =
						  (label is \\default) ? \"\<label.name\>\" : nextTmp();");		 
	writeModule("M2", "import M1;");
	return checkOK("true;", importedModules=["M1", "M2"]);
}