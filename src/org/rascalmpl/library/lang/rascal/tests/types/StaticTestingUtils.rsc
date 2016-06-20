@bootstrapParser
module lang::rascal::tests::types::StaticTestingUtils

/*
 * Utilities for writing tests for the Rascal Type Checker:
 * - all utilities depend on checkStatementsString
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
import lang::rascal::checker::ParserHelper;
import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::CheckerConfig;
import lang::rascal::\syntax::Rascal;

PathConfig testingConfig = pathConfig(srcs=[|test-modules:///|, |std:///|], bin=|home:///c1bin|, libs=[|home:///c1bin|]);

str abbrev(str s) { return size(s) < 120 ? s : "<s[0..117]> ..."; }

bool matches(str subject, str pat){
    pat = uncapitalize(pat);
    subject = uncapitalize(subject);
    return all(p <- split("_", pat), contains(subject, p));
}

bool check(str stmts, list[str] expected, list[str] importedModules = [], list[str] initialDecls = []){
     errors = getAllMessages(checkStatementsString(stmts, testingConfig, importedModules=importedModules, initialDecls=initialDecls));
     println(errors);
     for(eitem <- errors, str exp <- expected){
         if(matches(eitem.msg, exp))
               return true;          
     }
     throw abbrev("<errors>");
}

bool checkOK(str stmts, list[str] importedModules = [], list[str] initialDecls = []){
     errors = getFailureMessages(checkStatementsString(stmts, testingConfig, importedModules=importedModules, initialDecls=initialDecls));
     println(errors);
     if(size(errors) == 0)
        return true;
     throw abbrev("<errors>");
}

bool checkModuleOK(loc moduleToCheck){
	c = newConfiguration(testingConfig);							// Copied from checkStatementsString
	try {
		pt = parse(#start[Module], moduleToCheck);
		if (pt has top && Module m := pt.top) {
			c = checkModule(m, c);
		} else {
			c = addScopeError(c, "Unexpected parse result for module to check: <pt>", moduleToCheck); 
		}
	} catch perror : {
		c = addScopeError(c, "Could not parse and prepare config for base module to check: <perror>", moduleToCheck);
	}
     errors = c.messages;
     println(errors);
     if(size(errors) == 0)
        return true;
     throw abbrev("<errors>");
}

bool unexpectedType(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
		"_ not defined for _ and _", 
		"not defined on _ and _", 
		"not declared on",
		"Invalid type: expected _, found _", 
		"Invalid type _, expected expression of type _",
		"Expected type _, found _", 
		"Expected _, found _",
		"Unable to bind subject type _ to assignable", 
		"Unable to bind result type _ to assignable",
		"not assignable to",
		"Cannot use type", 
		"expected return type",
		"Expected subscript of type _, not _", 
		"Cannot subscript assignable of type", 
		"Unexpected type _, expected type _", 
		"Unexpected type _, generator should be an expression of type bool",
		"Type of bound should be", 
		"_ and _ incomparable", 
		"must have an actual type",
		"Cannot assign value of type", 
		"Cannot assign pattern of type",
		"does not allow fields",
		"Tuple index must be between", 
		"out of range",
		"Cannot add append information, no valid surrounding context found",
		"Cannot match an expression of type: _ against a pattern of type _",
		"Cannot subscript map of type _ using subscript of type _",
		"Inserted type _ must be a subtype of case type _"
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
		"Name _ is not in scope", 
		"Only constructors or productions with a different arity are available"
	], importedModules=importedModules, initialDecls=initialDecls);

bool undeclaredType(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
		"Type _ not declared"
	], importedModules=importedModules, initialDecls=initialDecls);

bool undefinedField(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
		"Field _ does not exist on type _"
	], importedModules=importedModules, initialDecls=initialDecls);

bool argumentMismatch(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
		"Function of type _ cannot be called with argument types _", 
		"Constructor of type _ cannot be built with argument types _",
		"Keyword parameter of type _ cannot be assigned argument of type _",
		"Unknown keyword parameter passed: _"
	], importedModules=importedModules, initialDecls=initialDecls);

bool redeclaredVariable(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
		"Cannot re-declare name that is already declared in the current function or closure",
		"redeclaration",
		"has already been declared"
	], importedModules=importedModules, initialDecls=initialDecls);

bool cannotMatch(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
		"Cannot match an expression of type: _ against a pattern of type", 
		"Cannot assign pattern of type", 
		"is not enumerable"
	], importedModules=importedModules, initialDecls=initialDecls);

bool declarationError(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
		"Constructor overlaps existing constructors in the same datatype", 
		"Initializer type",
		"Errors present in constructor parameters, cannot add constructor to scope"
	], importedModules=importedModules, initialDecls=initialDecls);
	
bool missingModule(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
	check(stmts, [
		"Cannot import module _",
		"Could not parse and prepare config for base module to check: IO"
	], importedModules=importedModules, initialDecls=initialDecls);

bool illegalUse(str stmts, list[str] importedModules = [], list[str] initialDecls = []) = 
    check(stmts, [
        "Cannot append, no valid surrounding context found"
    ], importedModules=importedModules, initialDecls=initialDecls);

	
void makeModule(str name, str body){
	mloc = |test-modules:///<name>.rsc|;
    writeFile(mloc, "module <name>
                     <body>");
}
