@doc{this module is under construction}
@contributor{Jurgen Vinju}
@contributor{Paul Klint}
@contributor{Ashim Shahi}
@contributor{Bas Basten}
module lang::java::style::StyleChecker

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;
import IO;
import lang::xml::DOM;
import Relation;
import Set;
import List;
import Node;
import util::Benchmark;
import util::Math;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import lang::java::style::Utils;

import lang::java::style::BlockChecks;
import lang::java::style::ClassDesign;
import lang::java::style::Coding;
import lang::java::style::Imports;
import lang::java::style::Metrics;
import lang::java::style::Miscellaneous;
import lang::java::style::NamingConventions;
import lang::java::style::SizeViolations;
import lang::java::style::Strings;

// Specification of checkers:
// - we dinstinguish checkers for declarations, statements and expressions.
// - for each category, a map is declared that associates the set of Java AST constructors
//   for which the check has to be applied.
//   IMPORTANT: this set has to be indentical to the constructors used in the rules defining each check
// - this map is inverted by the builder functions that create a map from constructor 
//   to all the checks they have to be applied to it.

// --- Declaration Checking -------------------------------------------------*/

// DeclarationChecker has as parameters:
// - decl, the current declaration to be processed
// - parents, the enclosing declrations
// - ast of the compilationUnit
// - M3 model
// and returns
// - a list of messages

alias DeclarationChecker = list[Message] (Declaration decl,  list[Declaration] parents, node ast, M3 model);

map[DeclarationChecker, set[str]] declarationChecker2Cons = (
	avoidStarImport: 			// Imports
		{"import1"},
	avoidStaticImport:			// Imports  	
		{"import1"},
	avoidStringBufferField: 	// Strings
		{ "field2" },
	classFanOutComplexity: 		// Metrics
		{ "class4" },
	fieldCount: 				// SizeViolations
		{ "field2" },
	fileLength:					// SizeViolations
		{ "compilationUnit2", 
		  "compilationUnit3" 
		},
	finalClass: 				// ClassDesign
		{ "class4" },
	illegalImport:  			// Imports
		{"import1"},
	methodLength: 				// SizeViolations
		{ "method5" },
	methodCount: 				// SizeViolations
		{ "method4", 
		  "method5"
		},
	mutableException: 			// ClassDesign
		{ "class4" },
	namingConventions: 			// NamingComventions
		{ "class4",
		  "compilationUnit2", 
		  "compilationUnit3", 
		  "enum4", 
		  "enumConstant2", 
		  "enumConstant3", 
		  "field2",
		  "method4", 
		  "method5",
		  "parameter3", 
		  "typeParameter2", 
		  "variables2"
		},
	noClone: 					// Coding
		{ "method4", 
		  "method5"
		},
	noFinalizer: 				// Coding
		{ "method4",
		  "method5"
		},
	outerTypeFilename: 			// Miscellaneous
		{ "class4", 
		  "interface4", 
		  "enum4"
		},
	parameterNumber: 			// SizeViolations
		{ "method4", 
		  "method5"
		},
	publicCount: 				// SizeViolations
		{ "field2", 
		  "method4", 
		  "method5",
		  "variables2"
		},
	// redundantImports			// Imports
	unCommentedMain: 			// Miscellaneous
		{ "method4", 
		  "method5" 
		},
	visibilityModifier: 		// ClassDesign
		{ "class4" }
);

// Build the map for all declaration checkers

map[str, set[DeclarationChecker]] declarationCheckers = ();

void buildDeclarationCheckers(){
	declarationCheckers = ();
	for(DeclarationChecker dc <- declarationChecker2Cons){
		cons = declarationChecker2Cons[dc];
		for(c <- cons){
			if(!declarationCheckers[c]?){
				declarationCheckers[c] = {dc};
			} else {
			  declarationCheckers[c] =  declarationCheckers[c] + {dc};
			}
		}
	}
}

// Run all checks associated with the constructor of the current declaration

list[Message] check(Declaration d, list[Declaration] parents,  node ast, M3 model){
	msgs = [];
	cons = getConstructor(d);
	if(!declarationCheckers[cons]?)
		return [];
	for(checker <- declarationCheckers[cons]){
		msgs += checker(d, parents, ast, model);
	}
	return msgs;
}

/* --- Statement Checking ---------------------------------------------------*/

// StatementChecker has as parameters:
// - stat, the current statement to be processed
// - parents, the enclosing statements
// - ast of the compilationUnit
// - M3 model
// and returns
// - a list of messages

alias StatementChecker = list[Message] (Statement stat,  list[Statement] parents, node ast, M3 model);
map[StatementChecker, set[str]] statementChecker2Cons = (
	avoidNestedBlocks: 				// BlockChecks
		{ "block1" },
	defaultComesLast: 				// Coding
		{ "switch2" },
	emptyBlock: 					// BlockChecks
		{ "do2",
		  "for3",
		  "for4",
		  "if2",
		  "if3",
		  "try2",
		  "try3",
		  "while2"
		},
	fallThrough: 					// Coding
		{ "switch2" },
	missingSwitchDefault: 			// Coding
		{ "switch2" },
	needBraces:						// BlockChecks
		{ "do2",
		  "for3",
		  "for4",
		  "if2",
		  "if3",
		  "while2"
		},
	nestedForDepth: 				// Coding
		{ "foreach3",
		  "for3",
		  "for4"
		},
	nestedIfDepth: 					// Coding
		{ "if2",
		  "if3"
		},
	nestedTryDepth: 				// Coding
		{ "try2",
		  "try3"
		},
	returnCount: 					// Coding
		{ "return0",
		  "return1"
		},
	simplifyBooleanReturn: 			// Coding
		{ "if3" },
	throwsCount: 					// ClassDesign
		{ "throw1" }
);

// Build the map for all statement checkers

map[str, set[StatementChecker]] statementCheckers = ();

void buildStatementCheckers(){
	statementCheckers = ();
	for(StatementChecker sc <- statementChecker2Cons){
		cons = statementChecker2Cons[sc];
		for(c <- cons){
			if(!statementCheckers[c]?){
				statementCheckers[c] = {};
			}
			statementCheckers[c] = statementCheckers[c] + {sc};
		}
	}
}

// Run all checks associated with the constructor of the current statement

list[Message] check(Statement s, list[Statement] parents,  node ast, M3 model){
	msgs = [];
	cons = getConstructor(s);
	if(!statementCheckers[cons]?)
		return [];
	for(checker <- statementCheckers[cons]){
		msgs += checker(s, parents, ast, model);
	}
	return msgs;
}

/* --- Expression Checking --------------------------------------------------*/

// ExpressionChecker has as parameters:
// - exp, the current expression to be processed
// - parents, the enclosing expressions
// - ast of the compilationUnit
// - M3 model
// and returns
// - a list of messages

alias ExpressionChecker = list[Message] (Expression exp,  list[Expression] parents, node ast, M3 model);

map[ExpressionChecker, set[str]] expressionChecker2Cons = (
	appendCharacterWithChar: 
		{ "methodCall4" },
	avoidInlineConditionals: 		// Coding
		{ "conditional3" },
	booleanExpressionComplexity: 	// Metrics
		{ "infix3",
		  "prefix2"
		},
	classDataAbstractionCoupling: 	// Metrics
		{ "newObject2",
		  "newObject3",
		  "newObject4"
		},
	consecutiveLiteralAppends:		// Strings  
		{ "methodCall4" },
	inefficientEmptyStringCheck:	// Strings 
		{ "methodCall4" },
	inefficientStringBuffering:		// Strings 
		{ "newObject2",
		  "methodCall4"
		},
	magicNumber: 					// Coding
		{ "number1" },
	multipleStringLiterals: 		// Coding
		{ "stringLiteral1" },
	simplifyBooleanExpression: 		// Coding
		{ "infix3",
		  "prefix2"
		},
	stringInstantiation: 			// Strings
	 	{ "newObject2" },
	stringLiteralEquality: 			// Coding
		{ "infix3" },
	stringToString: 				// Strings
		{ "methodCall4" },
	unnnessaryCaseChange: 			// Strings
		{ "methodCall4" },
	useIndexOfChar: 				// Strings
		{ "methodCall4" },
	useStringBufferLength: 			// Strings
		{ "methodCall4" }
);

// Build the map for all expression checkers

map[str, set[ExpressionChecker]] expressionCheckers = ();

void buildExpressionCheckers(){
	expressionCheckers = ();
	for(ExpressionChecker ec <- expressionChecker2Cons){
		cons = expressionChecker2Cons[ec];
		for(c <- cons){
			if(!expressionCheckers[c]?){
				expressionCheckers[c] = {};
			}
			expressionCheckers[c] = expressionCheckers[c] + {ec};
		}
	}
}

// Run all checks associated with the constructor of the current expression

list[Message] check(Expression e, list[Expression] parents,  node ast, M3 model){
	msgs = [];
	cons = getConstructor(e);
	if(!expressionCheckers[cons]?)
		return [];
	for(checker <- expressionCheckers[cons]){
		msgs += checker(e, parents, ast, model);
	}
	return msgs;
}

/* --- Toplevel check functions ---------------------------------------------*/

// checkALL: initialize and call actual checkALL

list[Message] checkAll(node ast, M3 model){
	buildDeclarationCheckers();
	buildStatementCheckers();
	buildExpressionCheckers();
	
	initCheckStates();
	
	registerCheckState("throwsCount", {"method5", "constructor4", "initializer1"},	0, updateThrowsCount, finalizeThrowsCount);
	registerCheckState("returnCount", {"method5", "constructor4"}, 					0, updateReturnCount, finalizeReturnCount);
	registerCheckState("methodCount", {"class4", "interface4", "enum4"}, 			0, updateMethodCount, finalizeMethodCount);
	registerCheckState("fieldCount",  {"class4", "interface4", "enum4"},			0, updateFieldCount, finalizeFieldCount);
	registerCheckState("publicCount",  {"class4", "interface4", "enum4"},			0, updatePublicCount, finalizePublicCount);
	registerCheckState("classDataAbstractionCoupling", {"class4", "enum4"}, 		{}, updateClassDataAbstractionCoupling, finalizeClassDataAbstractionCoupling);
	
	return  checkAll(ast, model, [], [], []);
}	

// The toplevel check function checkAll:
// - performs a top-down traversal of the compilationUnit
// - invokes the checks for the declrations/statements/expressions being encountered
// - accumalates all messages and returns them

list[Message] checkAll(node ast, M3 model, list[Declaration] declParents, list[Statement] statParents, list[Expression] expParents){
	msgs = [];
	isDeclaration = false;

	switch(ast){
	
		case Declaration d:
			{ msgs += check(d, declParents, ast, model); 
				declParents = d + declParents; 
				enterDeclaration(d); 
				isDeclaration = true;
			}
		
		case Statement s: 
			{ msgs += check(s, statParents, ast, model); 
			  statParents = s + statParents; 
			}
		
		case Expression e:
			{ //println(e);
			  msgs += check(e, expParents, ast, model); 
			  expParents = e + expParents; 
			}
		
		case Type t:  /* ignore for the moment */;
		
		default:
			println("Other: <ast>");
	}
	
	for(child <- getChildren(ast)){
		switch(child){
		case list[Declaration] decls:
			for(d <- decls){
				msgs += checkAll(d, model, declParents, statParents, expParents);
			}
		case list[Statement] stats:
			for(s <- stats){
				msgs += checkAll(s, model, declParents, statParents, expParents);
			}
		case node nd:
			msgs += checkAll(nd, model, declParents, statParents, expParents);
			
		case list[node] nds:
			for(nd <- nds){
				msgs += checkAll(nd, model, declParents, statParents, expParents);
			}
		default:
			;//println("ignore child: <child> of <ast>");
		}
	}
	if(isDeclaration){
		msgs += leaveDeclaration(head(declParents));
	}
	return msgs;
}

@doc{For testing on the console; we should assume only a model for the current AST is in the model}
list[Message] styleChecker(M3 model, set[node] asts){
	nlines = 0;
	msgs = [];
    for(Declaration ast <- asts){
    	nlines += ast@src.end.line;
		msgs += checkAll(ast, model);
    }
    println("Checked <nlines> of Java code, found <size(msgs)> issues");
    return msgs;
 }
   
@doc{For integration into OSSMETER, we get the models and the ASTs per file}   
list[Message] styleChecker(map[loc, M3] models, map[loc, node] asts) 
  = [*checker(asts[f], models[f]) | f <- models, checker <- checkers];  

real tosec(int t1, int t2) = round((t2 - t1)/1000.0, 0.1); 

list[Message] main(loc dir = |project://java-checkstyle-tests|){
  t1 = realTime();
  m3model = createM3FromEclipseProject(dir);
  t2 = realTime();
  asts = createAstsFromEclipseProject(dir, true);
  t3 = realTime();
  msgs = styleChecker(m3model, asts);
  t4 = realTime();
  println("Create M3 model: <tosec(t1, t2)> sec.");
  println("Create ASTs    : <tosec(t2, t3)> sec.");
  println("Perform checks : <tosec(t3, t4)> sec.");
  println("Total time     : <tosec(t1, t4)> sec.");
  return msgs;
} 

// temporary functions for regression testing with checkstyle

@doc{measure if Rascal reports issues that CheckStyle also does}
test bool precision() {
  rascal = main();
  checkstyle = getCheckStyleMessages();
  
  println("comparing checkstyle:
          '  <size(checkstyle)> messages: <checkstyle>
          'with rascal:
          '  <size(rascal)> messages: <rascal>
          '");

  rascalPerFile = index({< path, mr> | mr <- rascal, /.*src\/<path:.*>$/ := mr.pos.path});
  checkstylePerFile = index({< path, mc> | mc:<l,_> <- checkstyle, /.*src\/<path:.*>$/ := l.path});
  
  missingFiles = rascalPerFile<0> - checkstylePerFile<0>;
  
  println("Rascal found errors in <size(rascalPerFile<0>)> files
          'Checkstyle found errors in <size(checkstylePerFile<0>)> files
          '<if (size(missingFiles) > 0) {>and these <size(missingFiles)> files are missing from checkstyle: 
          '  <missingFiles>
          '  counting for <(0 | it + size(rascalPerFile[m]) | m <- missingFiles)> missing messages.<} else {>and no files are missing.<}>
          '");
          
  rascalCategories = { mr.category | mr <- rascal};
  checkstyleCategories = { c | mc:<l,c> <- checkstyle};
  missingCategories = rascalCategories - checkstyleCategories;
  
  println("Rascal generated <size(rascalCategories)> different categories.
          'Checkstyle generated <size(checkstyleCategories)> different categories.
          '<if (size(missingCategories) > 0) {>and these <size(missingCategories)> are missing from checkstyle:
          '  <sort(missingCategories)>
          '  (compare to <sort(checkstyleCategories - rascalCategories)>)<}>
          '");   
  
  // filter for common files and report per file
  
  for (path <- rascalPerFile, path in checkstylePerFile, bprintln("analyzing file <path>")) {
       rascalPerCategory = index({<mr.category, <mr.pos.begin.line, mr.pos.end.line>> | mr <- rascalPerFile[path]});
       checkstylePerCategory = index({ <cat, mc.begin.line> | <mc,cat> <- checkstylePerFile[path]});
       missingCategories = rascalPerCategory<0> - checkstylePerCategory<0>;
       
       println("  Rascal found errors in <size(rascalPerCategory<0>)> categories.
               '  Checkstyle found errors in <size(checkstylePerCategory<0>)> categories.
               '  <if (size(missingCategories) > 0) {>and these <size(missingCategories)> categories are missing from checkstyle: 
               '  <missingCategories>
               '  counting for <(0 | it + size(rascalPerCategory[m]) | m <- missingCategories)> missing messages in <checkstylePerCategory><} else {>  and no categories are missing.<}>
               '");
       
       int matched = 0;
       int notmatched = 0;
               
       for (cat <- rascalPerCategory, cat in checkstylePerCategory, bprintln("  analyzing category <cat>")) {
         for (<sl, el> <- rascalPerCategory[cat], !any(l <- checkstylePerCategory[cat], l >= sl && l <= el)) {
            println("    line number not matched by checkstyle: <cat>, <path>, <rascalPerCategory[cat]>");
            notmatched += 1;
         }
         
         for (cat in checkstylePerCategory, <sl, el> <- rascalPerCategory[cat], l <- checkstylePerCategory[cat], l >= sl && l <= el) {
            println("    match found: <cat>, <path>, <l>");
            matched += 1;
         }
       }
       
       if (matched > 0 && notmatched > 0)  {
         println("  of the <matched + notmatched> messages, there were <matched> matches and <notmatched> missed messages in <path>
                 '");
       }        
  }
  
  return false;        
}

rel[loc, str] getCheckStyleMessages(loc checkStyleXmlOutput = |project://java-checkstyle-tests/lib/output.xml|) {
   txt = readFile(checkStyleXmlOutput);
   dom = parseXMLDOM(txt);
   str fix(str x) {
     return /^.*\.<y:[A-Za-z]*>Check$/ := x ? y : x;
   }
   r =  { <|file:///<fname>|(0,0,<toInt(l),0>,<toInt(l),0>), fix(ch)> 
        | /element(_, "file", cs:[*_,attribute(_,"name", fname),*_]) := dom
        , /e:element(_, "error", as) := cs
        , {*_,attribute(_, "source", ch), attribute(_,"line", l)} := {*as}
        };
   return r;
}


