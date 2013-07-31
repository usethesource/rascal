module lang::rascal::checker::TTL::TTLGen

//extend lang::rascal::\syntax::Rascal;
extend lang::rascal::checker::TTL::TTLsyntax;
import IO;
import String;
import List;
import lang::rascal::checker::TTL::Library;
import lang::rascal::checker::TTL::PatternGenerator;
import lang::rascal::checker::TTL::ExpressionGenerator;

private int tcnt = 0;

private int genSym() { tcnt += 1; return tcnt; }

list[TestItem] infix = [];
list[TestItem] prefix = [];
list[TestItem] postfix = [];

// Main: compile all TTL specifications to Rascal tests

void main() { 
  tcnt = 0;
  infix = prefix = postfix = [];
  for(ttl <- (TTLRoot + "specs").ls, ttl.extension == TTL)
      generate(ttl); 
  generateSignatures(infix, prefix, postfix);
}

str basename(loc l) = l.file[ .. findFirst(l.file, ".")];

str basename(TestItem item) = basename(item@\loc);

str addModulePrefix(Module m){
  if(/\s*module\s+<mname:[a-zA-Z0-9]+><rest:.*$>/ := "<m>"){
    return "module <modulePrefix>::<mname>\n<rest>";
  }
  return "Malformed Module";
}
// Generate tests for one TTL file
void generate(loc src){
   println("generate: <src>");
   spec = parse(#TTL, src);
   map[Name, Declaration] decls = ();
   map[Name, Module] modules = ();
   str tests = "";
   for(TestItem item <- spec.items){
       if(defMod(name, moduleText) := item){// Was: item is defMod){
       	     if(decls[name]?) throw "Ambiguous name <name> at <item@\loc>";
       	     if(modules[name]?) throw "Redeclared module name <name> at <item@\loc>";
             modules[name] = item.moduleText;
             writeFile(TTLRoot + "generated/<item.name>.rsc", addModulePrefix(item.moduleText)); // TODO: Imports from different ttl files could conflict
       } else if(defDecl(name, declaration) := item){
       		if(modules[name]?) throw "Ambiguous name <name> at <item@\loc>";
       	     if(decls[name]?) throw "Redeclared declaration name <name> at <item@\loc>";
             decls[name] = declaration;
       } else if(item is GeneralTest){
          tests += genGeneralTest(item, decls, modules);
       } else if(item is InfixTest){
          tests += genInfixTest(item);
          infix += item;
       } else if(item is PrefixTest){
         tests += genUnaryTest(item, true);
         prefix += item;
       } else if(item is PostfixTest){
         tests += genUnaryTest(item, false);
         postfix += item;
       } else if(item is PatternTest){
         tests += genPatternTest(item);
       } else {
         println("Skipped: <item>");
       }
   }
   generatedTests = "TypeCheckTests";
   typechecker = "Typechecker";
   code = "module lang::rascal::checker::TTL::generated::<basename(src)>
          'import lang::rascal::checker::TTL::Library;
          'import Type;
          'import IO;
          'import List;
          'import Set;
          'import Message;
          'import util::Eval;
          'import lang::rascal::types::AbstractName;
          'import lang::rascal::types::TestChecker;
          'import lang::rascal::checker::TTL::PatternGenerator;
          'public bool verbose = true;
          '<tests>
          '";
   writeFile(TTLRoot + "generated/<basename(src)>.rsc", code);
}

// Generate code for a single TTL test
str genGeneralTest(TestItem item,  map[Name, Declaration] declarations,  map[Name, Module] modules){
  tname = "<basename(item)><genSym()>";
  nargs = toInt("<item.nargs>");
  args = intercalate(", ", [ "&T<i> arg<i>" | i <- [0 .. nargs]]);
  vtypes = "[" + intercalate(", ", [ "type(typeOf(arg<i>), ())" | i <- [0 .. nargs]]) + "]";
  
  <imports, decls> = expandUsedNames(getUsedNames(item.use), declarations, modules);
  <inferred, messages, exception> = getExpectations([e | e <- item.expectations]);
 
  escapedChars =  ("\"" : "\\\"", "\\" : "\\\\");
  decls = [escape(d, escapedChars) | d <- decls];
  code = escape("<item.statements>", escapedChars);

  if(!isEmpty(exception))
  	exception = "@expect{<exception>}";
  	
  inferredChecks = "";
  for(<var, tp> <- inferred){
    v = "<var>";
    if(v[0] == "_") v = v[1..];
    if(nargs > 0 && "<tp>"[1] == "T"){  // _T<i>
       i = toInt("<tp>"[2]);
       inferredChecks += "if(!subtype(getTypeForName(checkResult.conf, \"<v>\"), typeOf(arg<i>))) return false;";    
    } else {
      btp = buildType("<tp>");
      inferredChecks += "if(!subtype(getTypeForName(checkResult.conf, \"<v>\"), normalize(<btp>, vsenv))) return false;";
    }
  }
  
  messageCheckConditions = intercalate(" || ", ["<msg> := m.msg" | msg <- messages]);
  messageChecks = "validatedMessages = {};\n";
  if(!isEmpty(messageCheckConditions)){
     messageChecks += "for(m \<-  getFailureMessages(checkResult)){
		 			  '      if(<messageCheckConditions>)
		 			  '	        validatedMessages += m;
					  '}
				 	  'for(m \<-  getWarningMessages(checkResult)){
		 			  '      if(<messageCheckConditions>)
		 			  '	        validatedMessages += m;
					  '}
					  '";
  }
  messageChecks +=  "unvalidatedMessages = getAllMessages(checkResult) - validatedMessages;
                    'if(size(unvalidatedMessages) \> 0) { println(\"[<tname>] *** Unexpected messages: \<unvalidatedMessages\>\"); return false;}";
 
  checks = (isEmpty(inferredChecks) ? "" : inferredChecks) +
  		   (isEmpty(messageChecks) ? "" : "\n" + messageChecks);
 
  
  return "
  		 '/* <item> */ <exception>
  		 'test bool <tname>(<args>){
  		 '  vtypes = <vtypes>; 
  		 '  venv = ( );
  		 '  <for(i <- [0 .. nargs]){>venv[\"<i>\"] = \< vtypes[<i>], arg<i> \>; <}>
  		 '  vsenv = (\"T\<id\>\" : vtypes[id].symbol | id \<- index(vtypes) );
  		 '  stats = buildStatements(\"<code>\", venv);
  		 '  if(verbose) println(\"[<tname>] stats: \" + stats);
         '  checkResult = checkStatementsString(stats, importedModules=<imports>, initialDecls = <decls>);
         '  <checks>
         '  return true;
         '}
         '";
}

list[Name] getUsedNames(Use u){
    return (u is use) ? [name | name <- u.names] : [];
}

tuple[list[str],list[str]] expandUsedNames(list[Name] names, map[Name, Declaration] declarations,  map[Name, Module] modules){
    imports = [];
    decls = [];
    for(name <- names){
      if(modules[name]?)
         imports += "<modulePrefix>::<name>";
      else if(declarations[name]?)
         decls += "<declarations[name]>";
      else
          throw "Undefined name <name> at <name@\loc>";
    }
    return <imports, decls>;
}

tuple[lrel[Name,Type],list[RegExpLiteral],str] getExpectations(list[Expect] expect){
  inferred = [];
  list[RegExpLiteral] message = [];
  exception = "";
  for(e <- expect){
      if(e is inferred){
         inferred += <e.name, e.expectedType>;
      } else if (e is message){
      	 message += e.regexp;
      } else {
        exception = "<e.name>";
      }
   }
   return <inferred, message, exception>;
}

str genCondition(sig){
    typeCondition = "\n";
    if(nonempty(name, typeName) := sig.condition){//sig has condition && sig.condition is nonempty){
       tname = "<typeName>";
       tname = toUpperCase(tname[0]) + tname[1..];
	   typeCondition = "if(is<tname>Type(bindings[\"<name>\"])) return true;\n";
    }
    return typeCondition;
}

str buildType(str txt){   // TODO remove this limitation to 5 variables

   for(int id <- [0 .. 5]){
      txt = replaceAll(txt, "_T<id>", "#&T<id>.symbol");
   }
   txt = replaceAll(txt, "LUB", "lub");
   return startsWith(txt, "lub") || (txt[0] == "#") ? txt : "#<txt>.symbol";          
}

// Generete code for TTL test for infix operator
str genInfixTest(TestItem item){
  tests = "";
  for(operator <- item.operators){
	  operatorName = "<operator>"[1..-1]; 
	  for(sig <- item.signatures){
	     typeCondition = "";
	     if(sig.condition is condition){
	        tname = "<sig.condition.typeName>";
	        tname = toUpperCase(tname[0]) + tname[1..];
	        typeCondition = "if(is<tname>Type(bindings[\"<sig.condition.name>\"])) return true;";
	     }
	    
	     tname = "<basename(item)><genSym()>";
	     tests += "// Testing infix <item.name> <operatorName> for <sig>
	     		  'test bool <tname>(<sig.left> arg1, <sig.right> arg2){ 
	              '  ltype = typeOf(arg1);
	              '  rtype = typeOf(arg2);
	              '  if(isDateTimeType(ltype) || isDateTimeType(rtype))
	              '		return true;
	     		  '  <genArgument(sig.left, "l")>
	       		  '  <genArgument(sig.right, "r")>
				  '  if(lmatches && rmatches){
	              '     bindings = merge(lbindings, rbindings); 
	              '     <genCondition(sig)>
	              '     expression = \"(\<escape(arg1)\>) <operatorName> (\<escape(arg2)\>);\";
	              '     if(verbose) println(\"[<tname>] exp: \<expression\>\");
	              '     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
	              '     actualType = checkResult.res; 
	              '     expectedType = normalize(<toSymbolAsStr(sig.result)>, bindings);
	              '     return validate(\"<tname>\", expression, actualType, expectedType, arg1, arg2, <escape("signature <sig>")>);
	              '  }
	              '  return false;
	              '}\n";
	  }
  }
  return tests;
}

// Generete code for TTL test for unary (prefix or postfix) operator
str genUnaryTest(TestItem item, bool prefix){
  tests = "";
  for(operator <- item.operators){
	  operatorName = "<operator>"[1..-1]; 
	  for(sig <- item.signatures){
	     expression = prefix ? "\"<operatorName> (\<escape(arg1)\>);\"" : "\"(\<escape(arg1)\>) <operatorName>;\"";
	     tname = "<basename(item)><genSym()>";
	     tests += "// Testing <prefix ? "prefix" : "postfix"> <item.name> <operatorName> for <sig>
	     		  'test bool <tname>(<sig.left> arg1){ 
	              '  ltype = typeOf(arg1);
	              '  if(isDateTimeType(ltype))
	              '		return true;
	     		  '  <genArgument(sig.left, "l")>
				  '  if(lmatches){
				  '     <genCondition(sig)>
				  '     if(verbose) println(\"[<tname>] exp: \" + <expression>);
				  '	    checkResult = checkStatementsString(<expression>, importedModules=[], initialDecls = []); // apply the operator to its arguments
	              '     actualType = checkResult.res; 
	              '     expectedType = normalize(<toSymbolAsStr(sig.result)>, lbindings);
	              '     return validate(\"<tname>\", <expression>, actualType, expectedType, arg1, <escape("signature <sig>")>);
	              '  }
	              '  return false;
	              '}\n";
	  }
  }
  return tests;
}

str genArgument(ExtendedType a, str side) = "\<<side>matches, <side>bindings\> = bind(<toSymbolAsStr(a)>, <side>type);\n";


str genPatternTest(TestItem item){
  nargs = toInt("<item.nargs>");
  args = intercalate(", ", [ "&T<i> arg<i>" | i <- [0 .. nargs]]);
  ptypes = "[" + intercalate(", ", [ "type(typeOf(arg<i>), ())" | i <- [0 .. nargs]]) + "]";
  exp = escape("<item.expression>;");
  tname = "<basename(item)><genSym()>";
  return   "// Testing <item.expression>
   	 	   'test bool <tname>(<args>){
           '     ptypes = <ptypes>; 
           '     \<penv, expected_pvars\> = generatePatterns(ptypes); 
           '     exp = buildExpr(<exp>, penv);
           '     if(verbose) println(\"[<tname>] exp = \<exp\>\");
           '     checkResult = checkStatementsString(exp);
           '
           '     pvars = getPatternVariables(checkResult.conf);
           '
    	   '     for(v \<- expected_pvars){
           '         try {
           '             expectedType = expected_pvars[v].symbol;
           '             actualType = pvars[RSimpleName(v)];
           '             if(inferred(_) := actualType){
           '                 println(\"[<tname>] *** No type found for variable \<v\>; expected type \<expectedType\>; exp = \<exp\>\");
           '             } else {
           '             if(!validate(\"<tname>\", exp, actualType, expectedType, \"variable \<v\>\"))
           '                return false;
           '             }
           '         } catch: {
           '           println(\"[<tname>] *** Variable \<v\> unbound; expected type \<expected_pvars[v].symbol\>; exp = \<exp\>\");
           '           return false;
           '         }
           '     }
           '     return true;
           '}\n";
}
  