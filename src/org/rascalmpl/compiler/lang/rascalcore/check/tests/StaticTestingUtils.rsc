@bootstrapParser
module lang::rascalcore::check::tests::StaticTestingUtils

/*
 * Utilities for writing tests for the Rascal Type Checker:
 * - all utilities depend on rascalTModelForLocs
 * - all utilities match for the occurence of certain phrases in the generated error messages.
 * - (in a next phase we may want to introduce a more specific error reporting datatype)
 */

import IO;
import String;
import Message;
import Set;
import util::Reflective;
import ParseTree;
import util::SystemAPI;
import lang::rascalcore::check::RascalConfig;
import lang::rascal::\syntax::Rascal;

import lang::rascalcore::check::Checker;

PathConfig pathConfigForTesting() { 
  return getDefaultPathConfig(); 
}

str abbrev(str s) { return size(s) < 120 ? s : "<s[0..117]> ..."; }

bool matches(str subject, str pat){
    pat = uncapitalize(pat);
    subject = uncapitalize(subject);
    return all(p <- split("_", pat), contains(subject, p));
}

loc buildModule(str stmts,  list[str] importedModules = [], list[str] initialDecls = []){
    return makeModule(
        "TestModule",
        "<intercalate("\n", ["import <impname>;" | impname <-importedModules])>
        '<intercalate("\n", initialDecls)>
        'value main(){
        '    <stmts>\n
        '    return true;
        '}");  
}

loc makeModule(str name, str body){
    mloc = |memory://test-modules/<name>.rsc|;
    writeFile(mloc, "@bootstrapParser
                     'module <name>
                     '<body>");
    //println("module: <name>\n<body>");
    return mloc;
}

set[Message] getErrorMessages(ModuleStatus r)
    =  { m | m <- getAllMessages(r), m is error };

set[Message] getWarningMessages(ModuleStatus r)
    = { m | m <- getAllMessages(r), m is warning };

set[Message] getAllMessages(ModuleStatus r)
    = { m | mname <- r.tmodels, m <- r.tmodels[mname].messages };


ModuleStatus checkStatements(str stmts, list[str] importedModules = [], list[str] initialDecls = [], bool verbose=true){
    mloc = buildModule(stmts, importedModules=importedModules, initialDecls=initialDecls);
   return rascalTModelForLocs([mloc], rascalTypePalConfig(pathConfigForTesting()), getRascalCompilerConfig(), dummy_compile1);
}

bool check(str stmts, list[str] expected, list[str] importedModules = [], list[str] initialDecls = []){
	bool verbose=false;
     errors = getAllMessages(checkStatements(stmts, importedModules=importedModules, initialDecls=initialDecls, verbose=verbose));
	 if (verbose) {
     	println(errors);
	 }
     for(eitem <- errors, str exp <- expected){
         if(matches(eitem.msg, exp))
               return true;          
     }
     throw abbrev("<errors>");
}

bool checkOK(str stmts, list[str] importedModules = [], list[str] initialDecls = []){
     println("Imported: <importedModules>");
     errors = getErrorMessages(checkStatements(stmts, importedModules=importedModules, initialDecls=initialDecls));
     if(size(errors) == 0)
        return true;
     throw errors;
}

bool checkModuleOK(loc moduleToCheck){
<<<<<<< HEAD
	//rascalTModelForLocs([moduleToCheck], testingConfig(), rascalTypePalConfig());
     errors = getErrorMessages(rascalTModelForLocs([moduleToCheck], rascalTypePalConfig(rascalPathConfig=testingConfig()), getRascalCompilerConfig(), dummy_compile1));
=======
     errors = getErrorMessages(rascalTModelForLocs([moduleToCheck], rascalTypePalConfig(pathConfigForTesting()), getRascalCompilerConfig(), dummy_compile1));
>>>>>>> e1f02a88 (Changed keyword parameter rascalPathConfig to positional parameter)
     if(size(errors) == 0)
        return true;
     throw abbrev("<errors>");
}

bool unexpectedType(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
	    "Incompatible type _",
	    "Comparison not defined on _",
	    "Initialization of _ should be _",
	    "Undefined anno _",
	    "No definition found for _",
	    "Undefined _",
	    "Cannot assign value of type _ to _",
	    "_ is defined as _ and cannot be applied to argument",
	    "Missing return statement",
	    "Return type _ expected, found _",
	    "Type of generator should be _, found _",
	    "Pattern should be comparable with _, found _",
	    "Argument of _ should be _, found _",
	    "_ not defined on _ and _",
	    "_ not defined on _",
	    "Condition should be `bool`, found _",
	    "Field _ requires _, found _",
	    "Invalid type: expected a binary relation, found _",
	    "Type _ does not allow fields",
	    "Bound should have type _, found _",
	    "Assertion should be `bool`, found _",
	    "Expected subscript of type _, found _",
	    "Tuple index must be between _ and _",
	    "Cannot assign righthand side of type _ to lefthand side of type _",
	    "Field subscript _ out of range",
	    "Types _ and _ do not match",
	    "Cannot instantiate formal parameter type _",
	    "Bounds _ and _ are not comparable"
		//"_ not defined for _ and _", 
		//"not defined on _ and _", 
		//"not declared on",
		//"Invalid type: expected _, found _", 
		//"Invalid type _, expected expression of type _",
		//"Expected type _, found _", 
		//"Expected _, found _",
		//"Unable to bind subject type _ to assignable", 
		//"Unable to bind result type _ to assignable",
		//"not assignable to",
		//"Cannot use type", 
		//"expected return type",
		//"Expected subscript of type _, not _", 
		//"Cannot subscript assignable of type", 
		//"Unexpected type _, expected type _", 
		//"Unexpected type _, generator should be an expression of type bool",
		//"Type of bound should be", 
		//"_ and _ incomparable", 
		//"must have an actual type",
		//"Cannot assign value of type", 
		//"Cannot assign pattern of type",
		//"does not allow fields",
		//"Tuple index must be between", 
		//"out of range",
		//"Cannot add append information, no valid surrounding context found",
		//"Cannot match an expression of type: _ against a pattern of type _",
		//"Cannot subscript map of type _ using subscript of type _",
		//"Inserted type _ must be a subtype of case type _"
	], importedModules=importedModules, initialDecls=initialDecls);
	
// NOTE: type checker does not yet support analysis of uninitialized variables, therefore this check always succeeds, for now.

bool uninitialized(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = true;

//bool uninitialized(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	//check(stmts, [
	//	"Unable to bind", 
	//	"Cannot initialize", 
	//	"must have an actual type before assigning"
	//], importedModules=importedModules, initialDecls=initialDecls);

bool undeclaredVariable(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
		"Undefined _",
		"Unresolved type for _",
		"Arguments of `||` should introduce same variables, _"
	], importedModules=importedModules, initialDecls=initialDecls);

bool undeclaredType(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
	   "Undefined _"
		//"Type _ not declared"
	], importedModules=importedModules, initialDecls=initialDecls);

bool undefinedField(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
		"Field _ does not exist on type _"
	], importedModules=importedModules, initialDecls=initialDecls);

bool argumentMismatch(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
	      "Undefined keyword argument _;",
	      "Expected _ argument(s),",
	      "Argument _ should have type _ found _",
	      "Return type _ expected, found _",
	      "Keyword argument _ has type _, expected _"
	   
	      //"Function of type _ cannot be called with argument types _", 
		  //"Constructor of type _ cannot be built with argument types _",
		  //"Constructor accepts _ arguments but was given only _ arguments",
		  //"Keyword parameter of type _ cannot be assigned argument of type _",
		  //"Unknown keyword parameter passed: _"
	], importedModules=importedModules, initialDecls=initialDecls);

bool redeclaredVariable(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
	      "Undefined _ due to double declaration",
	      "Double declaration of _"
		  //"Cannot re-declare name that is already declared in the current function or closure",
		  //"redeclaration",
		  //"has already been declared"
	], importedModules=importedModules, initialDecls=initialDecls);

bool cannotMatch(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
	      "Pattern of type _ cannot be used to enumerate over _",
	      "Type _ is not enumerable",
	      "Incompatible type in assignment to variable _, expected _, found _",
	      "Pattern should be subtype of _, found _",
	      "Pattern should be comparable with _, found _",
	      "Expected tuple pattern with _ elements, found _",
	      "Pattern variable _ has been introduced before, add explicit declaration of its type"
		  //"Cannot match an expression of type: _ against a pattern of type", 
		  //"Cannot assign pattern of type", 
		  //"is not enumerable"
	], importedModules=importedModules, initialDecls=initialDecls);

bool declarationError(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
	      "Initialization of _ should be subtype of _",
	      "Invalid initialization of _",
	      "Undefined _",
	      "Double declaration of _",
	      "Constructor _ clashes with other declaration with comparable fields",
	      "Unresolved type for _",
	       "Constructor _ in formal parameter should be unique",
	       "Type _ should have one type argument"
		  //"Constructor _ overlaps existing constructors in the same datatype", 
		  //"Initializer type",
		  //"Errors present in constructor parameters, cannot add constructor to scope"
	], importedModules=importedModules, initialDecls=initialDecls);
	
bool missingModule(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
	      "Reference to name _ cannot be resolved"
		  //"Cannot import module _",
		  //"Could not parse and prepare config for base module to check: IO"
	], importedModules=importedModules, initialDecls=initialDecls);

bool illegalUse(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
    check(stmts, [
          "Append outside a while"
    ], importedModules=importedModules, initialDecls=initialDecls);

bool nonVoidType(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
    check(stmts, [
          "Contribution to _ comprehension should not have type `void`"
          //"Non-void type required"
    ], importedModules=importedModules, initialDecls=initialDecls);
    
bool unsupported(str stmts, list[str] importedModules = [], list[str] initialDecls = []) =
    check(stmts, [
          "Unsupported _"
    ], importedModules=importedModules, initialDecls=initialDecls);
