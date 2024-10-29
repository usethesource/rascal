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
import lang::rascalcore::check::RascalConfig;

import lang::rascalcore::check::Checker;

bool verbose = false;

PathConfig pathConfigForTesting() {
  return getDefaultTestingPathConfig();
}

str abbrev(str s) { return size(s) < 120 ? s : "<s[0..117]> ..."; }

bool matches(str subject, str pat){
    pat = uncapitalize(pat);
    subject = uncapitalize(subject);
    return all(p <- split("_", pat), contains(subject, p));
}

tuple[str,str] extractModuleNameAndBody(str moduleText){
	txt = trim(moduleText);
	if(/module\s*<nm:[A-Z][A-Za-z0-9:]*>\s*<body:.*$>/s := txt){
		return <nm, body>;
	}
	throw "Cannot extract module name from <moduleText>";
}

loc composeModule(str stmts){
    return writeModule(
        "module TestModule
        'value main(){
        '    <stmts>\n
        '    return true;
        '}");
}

void clearMemory() {
	remove(|memory:///test-modules/| recursive = true);
}
str cleanName(str name)
	= name[0] == "\\" ? name[1..] : name;

loc writeModule(str moduleText){
	<mname, mbody> = extractModuleNameAndBody(moduleText);
    mloc = |memory:///test-modules/<cleanName(mname)>.rsc|;
    writeFile(mloc, moduleText);
    return mloc;
}

void writeModules(str modules...){
	for(mname <- modules){
		writeModule(mname);
	}
}

void removeModule(str mname){
	pcfg = getDefaultTestingPathConfig();
	name = cleanName(mname);
	remove(|memory:///test-modules/<name>.rsc|);
	remove(pcfg.resources + "<name>.tpl");
}

set[Message] getErrorMessages(ModuleStatus r)
    =  { m | m <- getAllMessages(r), m is error };

set[Message] getWarningMessages(ModuleStatus r)
    = { m | m <- getAllMessages(r), m is warning };

set[Message] getAllMessages(ModuleStatus r)
	= { m | mname <- r.messages, m <- r.messages[mname] };

ModuleStatus checkStatements(str stmts) {
	mloc = composeModule(stmts);
   	return rascalTModelForLocs([mloc], rascalCompilerConfig(pathConfigForTesting())[infoModuleChecked=true], dummy_compile1);
}

bool checkStatementsAndFilter(str stmts, list[str] expected) {
     msgs = getAllMessages(checkStatements(stmts));
	 if (verbose) {
     	println(msgs);
	 }
     for(eitem <- msgs, str exp <- expected){
         if(matches(eitem.msg, exp))
               return true;
     }
     throw abbrev("<msgs>");
}
bool checkModuleAndFilter(str moduleText, list[str] expected, bool matchAll = false, bool errorsAllowed = true, PathConfig pathConfig = pathConfigForTesting()) {
	mloc = writeModule(moduleText);
	return checkModuleAndFilter(mloc, expected, matchAll=matchAll, errorsAllowed=errorsAllowed, pathConfig=pathConfig);
}
bool checkModuleAndFilter(loc mloc, list[str] expected, bool matchAll = false, bool errorsAllowed = true, PathConfig pathConfig = pathConfigForTesting()) {
    msgs = getAllMessages(rascalTModelForLocs([mloc], rascalCompilerConfig(pathConfig)[infoModuleChecked=true], dummy_compile1));
	if (verbose) {
     	println(msgs);
	 }
	 if(!errorsAllowed && !isEmpty(msgs) && any(error(_,_) <- msgs)) return false;
	 matched = {};
     for(eitem <- msgs, str exp <- expected){
         if(matches(eitem.msg, exp)){
		 	if(matchAll){
				matched += eitem.msg;
			} else
               return true;
		 }
     }
	 if(matchAll) {
		return all(e <- expected, e in matched);
	 }
     throw abbrev("<msgs>");
}

bool checkOK(str stmts) {
     errors = getErrorMessages(checkStatements(stmts));
     if(size(errors) == 0)
        return true;
     throw errors;
}

bool checkModuleOK(loc moduleToCheck, PathConfig pathConfig = pathConfigForTesting()) {
     errors = getErrorMessages(rascalTModelForLocs([moduleToCheck], rascalCompilerConfig(pathConfig)[infoModuleChecked=true], dummy_compile1));
     if(size(errors) == 0)
        return true;
     throw abbrev("<errors>");
}

bool checkModuleOK(str moduleText, PathConfig pathConfig = pathConfigForTesting()){
	<mname, mbody> = extractModuleNameAndBody(moduleText);
	pathConfig.srcs += pathConfigForTesting().srcs;
	mloc = writeModule(moduleText);
	return checkModuleOK(mloc, pathConfig=pathConfig);
}

// bool checkModuleOK(str mbody){
// 	mloc = writeModule("TestModule", mbody);
// 	return checkModuleOK(mloc);
// }

// ---- unexpectedType --------------------------------------------------------

list[str] unexpectedTypeMsgs = [
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
		"Return expression does not bind _",
	    "Type of generator should be _, found _",
	    "Pattern should be comparable with _, found _",
	    "Argument of _ should be _, found _",
		"Cannot call _ with _ argument(s), _",
	    "_ not defined on _ and _",
	    "_ not defined on _",
	    "Condition should be `bool`, found _",
	    "Field _ requires _, found _",
	    "Invalid type: expected a binary relation over equivalent types, found _",
	    "Type _ does not allow fields",
	    "Bound should have type _, found _",
	    "Assertion should be `bool`, found _",
		"Assertion message should be `str`, found _",
	    "Expected subscript of type _, found _",
	    "Tuple index must be between _ and _",
	    "Cannot assign righthand side of type _ to lefthand side of type _",
	    "Field subscript _ out of range",
	    "Types _ and _ do not match",
		"Types _ and _ are not comparable",
	    "Cannot instantiate formal parameter type _",
	    "Bounds _ and _ are not comparable",
		"Type parameter(s) _ in return type of function _ not bound by its formal parameters",
	    "Returned type _ is not always a subtype of expected return type _",
		"Type parameter _ should be less than _, found _",
		"Ambiguous pattern type _",
		"Splice operator not allowed inside a tuple pattern",
		"Is defined operator `?` can only be applied to _",
		"Expected _ in slice assignment, found _",
		"The slice step must be of type _, found _",
		"The last slice index must be of type _, found _",
		"A pattern of type _ cannot be replaced by _",
		"Insert found outside replacement context",
		"Insert type should be subtype of _, found _",
		"Expected _ type parameter(s) for _, found _",
		"Type _ cannot be parameterized, found _ parameter(s)",
		"Expected a non-terminal type, found _",
		"Expected a binary relation, found _"
	];

bool unexpectedTypeInModule(str moduleText)
	= checkModuleAndFilter(moduleText, unexpectedTypeMsgs);

bool unexpectedType(str stmts)
	= checkStatementsAndFilter(stmts, unexpectedTypeMsgs);

// ---- unitialized -----------------------------------------------------------

// NOTE: type checker does not yet support analysis of uninitialized variables, therefore this check always succeeds, for now.

bool uninitializedInModule(str moduleText) = true;
bool uninitialized(str stmts) = true;

//bool uninitialized(str stmts) =
	//checkStatementsAndFilter(stmts, [
	//	"Unable to bind",
	//	"Cannot initialize",
	//	"must have an actual type before assigning"
	//]);

// ---- undeclaredVariable ----------------------------------------------------

list[str] undeclaredVariableMsgs = [
	"Undefined _",
	"Unresolved type for _",
	"Undefined typeVar _",
	"Variable(s) _ and _ should be introduced on both sides of `||` operator"
];

bool undeclaredVariableInModule(str moduleText)
	= checkModuleAndFilter(moduleText, undeclaredVariableMsgs);

bool undeclaredVariable(str stmts) =
	checkStatementsAndFilter(stmts, undeclaredVariableMsgs);

// ---- undeclaredType --------------------------------------------------------

list[str] undeclaredTypeMsgs = [
	"Undefined _"
];
bool undeclaredTypeInModule(str moduleText)
	= checkModuleAndFilter(moduleText, undeclaredTypeMsgs);

bool undeclaredType(str stmts) =
	checkStatementsAndFilter(stmts, undeclaredTypeMsgs);

// ---- undefinedField --------------------------------------------------------

bool undefinedField(str stmts) =
	checkStatementsAndFilter(stmts, [
		"Field _ does not exist on type _"
	]);

// ---- argumentMismatch ------------------------------------------------------

list[str] argumentMismatchMsgs = [
	"Undefined keyword argument _;",
	"Expected _ argument(s),",
	"Expected _ type parameter(s)",
	"Argument _ should have type _, found _",
	"Return type _ expected, found _",
	"Keyword argument _ has type _, expected _"
];

bool argumentMismatchInModule(str moduleText)
	= checkModuleAndFilter(moduleText, argumentMismatchMsgs);

bool argumentMismatch(str stmts) =
	checkStatementsAndFilter(stmts, argumentMismatchMsgs);

// ---- redeclaredVariable ----------------------------------------------------

list[str] redeclaredVariableMsgs = [
	"Undefined _ due to double declaration",
	"Double declaration of _ _"
];

bool redeclaredVariableInModule(str moduleText)
	= checkModuleAndFilter(moduleText, redeclaredVariableMsgs);

bool redeclaredVariable(str stmts) =
	checkStatementsAndFilter(stmts, redeclaredVariableMsgs);

// ---- cannotMatch -----------------------------------------------------------

list[str] cannotMatchMsgs = [
	"Pattern of type _ cannot be used to enumerate over _",
	"Type _ is not enumerable",
	"Incompatible type in assignment to variable _, expected _, found _",
	"Pattern should be subtype of _, found _",
	"Pattern should be comparable with _, found _",
	"Expected tuple pattern with _ elements, found _",
	"Pattern variable _ has been introduced before, add explicit declaration of its type"
];

bool cannotMatchInModule(str moduleText)
	= checkModuleAndFilter(moduleText, cannotMatchMsgs);

bool cannotMatch(str stmts) =
	checkStatementsAndFilter(stmts, cannotMatchMsgs);

// ---- unexpectedDeclaration ------------------------------------------------------

list[str] unexpectedDeclarationMsgs = [
	"Initialization of _ should be subtype of _",
	"Invalid initialization of _",
	"Undefined _",
	"Double declaration of _",
	"Constructor _ overlaps with other declaration for type _, see _",
	"Constructor _ is used without qualifier and overlaps with other declaration, _",
	"Unresolved type for _",
	"Constructor _ in formal parameter should be unique",
	"Type _ should have one type argument",
	"On use add a qualifier to constructor _",
	"Remove code clone for _",
	"Ambiguous pattern type _",
	"Field name _ ignored, field names must be provided for _ fields or for none",
	"Layout type _ not allowed at _ of production",
	"Consecutive layout types _ and _ not allowed",
	"Only literals allowed in keyword declaration, found _",
	"Exclude (`\\`) requires keywords as right argument, found _",
	"Nested iteration",
	"Element name _ ignored",
	"Non-well-formed _ type, labels must be distinct"
];

bool unexpectedDeclarationInModule(str moduleText)
	= checkModuleAndFilter(moduleText, unexpectedDeclarationMsgs);

bool unexpectedDeclaration(str stmts) =
	checkStatementsAndFilter(stmts, unexpectedDeclarationMsgs);

// ---- missingModule ---------------------------------------------------------

list[str] missingModuleMsgs = [
	"Reference to name _ cannot be resolved",
	 "Undefined module _",
	 "Module _ not found_"
];
bool missingModuleInModule(str moduleText)
	= checkModuleAndFilter(moduleText, missingModuleMsgs);

bool missingModule(str stmts) =
	checkStatementsAndFilter(stmts, missingModuleMsgs);

// ---- illegalUse ------------------------------------------------------------

list[str] illegalUseMsgs = [
	"Append outside a while",
	"Right-hand side of assignment does not always have a value"
];

bool illegalUseInModule(str moduleText)
	= checkModuleAndFilter(moduleText, illegalUseMsgs);

bool illegalUse(str stmts) =
    checkStatementsAndFilter(stmts, illegalUseMsgs);

// ---- nonVoidType ----------------------------------------------------------

list[str] nonVoidTypeMsgs = [
	"Contribution to _ comprehension should not have type `void`"
];

bool nonVoidTypeInModule(str moduleText)
	= checkModuleAndFilter(moduleText, nonVoidTypeMsgs);

bool nonVoidType(str stmts) =
    checkStatementsAndFilter(stmts, nonVoidTypeMsgs);

 // ---- unsupported ---------------------------------------------------------

list[str] unsupportedMsgs = [
	"Unsupported _"
];

bool unsupportedInModule(str moduleText)
	= checkModuleAndFilter(moduleText, unsupportedMsgs);

bool unsupported(str stmts) =
    checkStatementsAndFilter(stmts, unsupportedMsgs);

// ---- expectChecks ----------------------------------------------------------

bool expectReChecks(str moduleText, list[str] moduleNames, PathConfig pathConfig = pathConfigForTesting()){
	mloc = writeModule(moduleText);
	pathConfig.srcs +=  pathConfigForTesting().srcs;
	return expectReChecks(mloc, moduleNames, pathConfig=pathConfig);
}

bool expectReChecks(loc mloc, list[str] moduleNames, PathConfig pathConfig = pathConfigForTesting()){
	msgs = [ "Checked <nm>" | nm <- moduleNames ];
	return checkModuleAndFilter(mloc, msgs, matchAll=true, errorsAllowed=false, pathConfig = pathConfig);
}

bool expectReChecksWithErrors(str moduleText, list[str] moduleNames, PathConfig pathConfig = pathConfigForTesting()){
	mloc = writeModule(moduleText);
	return return expectReChecksWithErrors(mloc, moduleNames, pathConfig=pathConfig);
}

bool expectReChecksWithErrors(loc mloc, list[str] moduleNames, PathConfig pathConfig = pathConfigForTesting()){
	msgs = [ "Checked <nm>" | nm <- moduleNames ];
	return checkModuleAndFilter(mloc, msgs, matchAll=true, errorsAllowed=true, pathConfig=pathConfig);
}