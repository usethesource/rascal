@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::TypeUtils

import IO;
import Set;
import Map;
import Node;
import Relation;
import String;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::compile::muRascal::AST;

import lang::rascalcore::check::BasicRascalConfig;
import Type;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;

/*
 * This module provides a bridge to the "TModel" delivered by the type checker

 * It contains (type, scope, use, def) information collected by the type checker.
 * This module consists of three parts:
 * Part I:		Defines the function extractScopes that extracts information from a TModel
 * 				and transforms it into a representation that is suited for the compiler.
  *             Initializes the type reifier
 * Part II: 	Defines other functions to access this type information.
 * Part III:	Type-related code generation functions.
 * 
 * Some details:
 * - We use the source location of each entity as its unique identifier UID uid.
 *   This uid is connected to all information about this entity.
 */
 
/********************************************************************/
/*     Part I: Extract and convert Type checker TModel              */
/********************************************************************/

// A set of global values to represent the extracted information
// TODO: wrap this in a function

alias UID = loc;                                    // A UID is a unique identifier determined by the source location of a construct

/*
 * We will use FUID (for Function UID) to create a readable string representation for 
 * any enity of interest. Typically a FUID consists of:
 * - the name of the entity
 * - its type
 * - optional modifiers (to indicate a specific use, case, etc)
 *
 */

alias FUID = str; 

private AGrammar grammar = grammar({}, ());

AGrammar getGrammar() = grammar;

private set[AType] ADTs = {};

set[AType] getADTs() = ADTs;

Scopes scopes = ();                                 // All scopes
rel[UID,UID] td_reachable_scopes = {};              // Transitive closure of top down reachable scopes

map[UID,UID] declaredIn = ();                       // Map from declared enity to its declaring function or module
rel[UID,UID] declares = {};                         // Relation from declaring functionn or module to the entities declared in it.

map[UID,AType] facts = ();                          // All facts derived by the type chcekr
map[UID,AType] specializedFacts = ();               // Facts resulting from overloading resolutiom
    
rel[UID, UID] useDef = {};                          // Relation from uses to definitions
rel[UID, UID] defUses = {};                         // Relation from defintion to all its uses

map[UID, Define] definitions = ();                  // All definitions
map[UID, Define]  getDefinitions() = definitions;
                         
map[UID,int]  position_in_container = ();           // Position of variable in containing function or module
map[UID,set[Define]] vars_per_scope = ();           // The variables introduced per scope

set[UID] functions = {};                            // Declared functions

bool isFunction(UID uid) = uid in functions;

set[UID] defaultFunctions = {};                     // Declared default functions

bool isDefaultFunction(UID uid) = uid in defaultFunctions;

set[UID] constructors = {};                         // Declared constructors
map[AType, set[AType]] adt_constructors = ();       // Map from ADT to its constructors

bool isConstructor(UID uid) = uid in constructors;

set[AType] getConstructors()
    = { getDefType(uid) | uid <- constructors};

private map[str,int] module_var_init_locals = ();	// number of local variables in module variable initializations

int getModuleVarInitLocals(str mname) {
	//assert module_var_init_locals[mname]? : "getModuleVarInitLocals <mname>";
	return module_var_init_locals[mname] ? 0;
}
public set[UID] keywordFormals = {};				// declared keyword parameters & common keyword fields declared on datatypes

private map[tuple[UID,UID], bool] funInnerScopes = ();

alias OFUN = tuple[str name, AType funType, str fuid, list[loc] ofunctions, list[loc] oconstructors];		// An overloaded function and all its possible resolutions

private map[str,int] overloadingResolver = ();		// map function name to overloading resolver
private map[tuple[str fname, AType ftype],str] overloadedTypeResolver = (); // map overloaded function types to the name of their resolver
private list[OFUN] overloadedFunctions = [];		// list of overloaded functions 

str unescape(str name) = name[0] == "\\" ? name[1..] : name;

void addOverloadedFunctionAndResolver(OFUN fundescr) = addOverloadedFunctionAndResolver(fundescr.fuid, fundescr);

void addOverloadedFunctionAndResolver(str fuid1, OFUN fundescr){
	int n = indexOf(overloadedFunctions, fundescr);
	if(n < 0){
		n = size (overloadedFunctions);
		overloadedFunctions += fundescr;
	}
	//println("addOverloadedFunctionAndResolver: <n>, <fuid1>, <fundescr>, <overloadingResolver[fuid1]? ? overloadingResolver[fuid1] : -1>");
	assert !overloadingResolver[fuid1]? || overloadingResolver[fuid1] == n: "Cannot redefine overloadingResolver for <fuid1>, <overloadingResolver[fuid1]>, <fundescr>";
	overloadingResolver[fuid1] = n;
}

list[OFUN] getOverloadedFunctions() = overloadedFunctions;

bool hasOverloadingResolver(FUID fuid) = overloadingResolver[fuid]?;

OFUN getOverloadedFunction(FUID fuid) {
	assert overloadingResolver[fuid]? : "No overloading resolver defined for <fuid>";
	resolver = overloadingResolver[fuid];
	//println("getOverloadedFunction(<fuid>) ==\> <overloadedFunctions[resolver]>");
	return overloadedFunctions[resolver];
}

// Reset the above global variables, when compiling the next module.

public void resetScopeExtraction() {  
    functions = {}; 
    keywordFormals = {};
    defaultFunctions = {};
    module_var_init_locals = ();
    declares = {};
    declaresMap = ();
    declaredIn = ();
    funInnerScopes = ();
    overloadingResolver = ();
    overloadedFunctions = [];
    scopes = ();
    facts = (); 
    specializedFacts = ();
    useDef = {};
    defUses = {};
    definitions = ();
    position_in_container = ();
    vars_per_scope = ();
    functions = {};
    constructors = {};
    adt_constructors = ();
}

bool isDefinition(UID d){
    return definitions[d]?;
}

bool isUse(UID u){
    return !isEmpty(useDef[u]);
}

AType getDefType(UID d){
    di = definitions[d].defInfo;
    if(defType(AType atype) := di) return atype;
    if(defType(Tree tree) := di) return getDefType(getLoc(tree));
    if(defType(loc l) := di) return facts[l];
    throw "getDefType: <d>";
}

AType getTypeFromDef(Define def){
    di = def.defInfo;
    if(defType(AType atype) := di) return atype;
    if(defType(Tree tree) := di) return getDefType(getLoc(tree));
    if(defType(loc l) := di) return facts[l];
    throw "getTypeFromDef: <def>";
}

int getFormals(UID fuid) {
    tp = getDefType(fuid);
    kwmap = size(tp.kwFormals) > 0 ? 1 : 0;
    return size(tp.formals) + kwmap;
}

str getScope(UID uid){
    return convert2fuid(declaredIn[uid]);
}
    
// Compute the scope size, excluding declared nested functions, closures and keyword parameters   
// r2mu translation of functions introduces variables in place of formal parameter patterns and uses patterns to match these variables  
int getScopeSize(UID uid){
    vars = [];
    if(definitions[uid]?){
        def = definitions[uid];
        assert def.idRole == functionId();
        vars = [ v | v <- declares[uid], is_non_keyword_variable(definitions[v]) ];
    } else {
        locally_defined = { *(vars_per_scope[sc] ? {}) | sc <- td_reachable_scopes[uid] }; //TODO declaredIn[sc] == fun
        vars = [v | v <- locally_defined, is_non_keyword_variable(v)];
    }
       return size(vars) + 2; // '+ 2' accounts for keyword arguments and default values of keyword parameters  
}
 
bool is_formal(Define d) = d.idRole in formalRoles;
bool is_positional_formal(Define d) = d.idRole in positionalFormalRoles;
bool is_outer_formal(Define d) = d.idRole in outerFormalRoles;
bool is_variable(Define d) = d.idRole in variableRoles + fieldId();
bool is_non_keyword_variable(Define d) = d.idRole in variableRoles - {keywordFormalId()};
bool is_module_variable(Define d) = d.idRole == variableId();
bool is_module_or_function(loc l) = definitions[l]? && definitions[l].idRole in {moduleId(), functionId()};
bool is_module(Define d) = d.idRole in {moduleId()};

bool is_declared_in_module(UID uid) = definitions[getFirstFrom(useDef[uid])].idRole == moduleId();

loc findContainer(Define d){
    //println(d);
    if(is_module(d)) return d.defined;
    cscope = d.scope;
    while(!is_module_or_function(cscope)) cscope = scopes[cscope];
    return cscope;
}
    
// extractScopes: extract and convert type information from the Configuration delivered by the type checker.
void extractScopes(TModel tm){
    //iprintln(tm);
    scopes = tm.scopes;
    module_scopes = { s | s <- scopes, scopes[s] == |global-scope:///|};

    facts = tm.facts;
    specializedFacts = tm.specializedFacts;
    defines = tm.defines;
    definitions = ( def.defined : def | Define def <- defines );
    position_in_container = ();
    vars_per_scope = ();
    useDef = tm.useDef;
    defUses = invert(useDef);
    modules = {};
    
    if(AGrammar g := tm.store["grammar"]){
        grammar = g;
    }
    
    if(set[AType] adts := tm.store["ADTs"]){
        ADTs = adts;
    }
    
    for(def <- defines){
        switch(def.idRole){
            case functionId(): {
                functions += def.defined;
                if(def.defInfo has modifiers && "default" in def.defInfo.modifiers) defaultFunctions += def.defined;
            }  
            case constructorId(): {
                 constructors += def.defined;
                 consType = getDefType(def.defined);
                 adtType = consType.adt;
                 if(adt_constructors[adtType]?){
                    adt_constructors[adtType] += consType;
                 } else {
                    adt_constructors[adtType] = {consType};
                 }
                 }
            case keywordFormalId():
                 keywordFormals += def.defined;
            case moduleId():
                 modules += def.defined;
            case variableId():
                 vars_per_scope[def.scope] = {def} + (vars_per_scope[def.scope] ? {});
            case formalId():
                 vars_per_scope[def.scope] = {def} + (vars_per_scope[def.scope] ? {});
            case nestedFormalId():
                 vars_per_scope[def.scope] = {def} + (vars_per_scope[def.scope] ? {});
            case patternVariableId():
                 vars_per_scope[def.scope] = {def} + (vars_per_scope[def.scope] ? {});
            case fieldId():
                vars_per_scope[def.scope] = {def} + (vars_per_scope[def.scope] ? {});
       }
    }
    
    //println("vars_per_scope"); iprintln(vars_per_scope);
    
    // Determine position of module variables
    for(m <- modules){
        module_vars_set = vars_per_scope[m] ? {};
        module_vars_list = toList(module_vars_set);
        for(int i <- index(module_vars_list)){
            position_in_container[module_vars_list[i].defined] = i;
        }
    }
    
    // Scopes goes inside out, compute an inverted version and take transitive closure
    scopes_rel = toRel(scopes);
    td_reachable_scopes = invert(scopes_rel)*;
    declaredIn = (d.defined : findContainer(d) | d <- defines, d.id != "type");
    declares = invert(toRel(declaredIn));
    
    //println("declaredIn"); iprintln(declaredIn);
    
    // Determine position of variables inside functions
    
    for(fun <- functions){   
        fundef = definitions[fun];
        println("td_reachable_scopes[fundef.defined]: <td_reachable_scopes[fundef.defined]>");
        println("vars_per_scope:"); iprintln(vars_per_scope);
        locally_defined = { *(vars_per_scope[sc] ? {}) | sc <- td_reachable_scopes[fundef.defined], bprintln(sc), facts[sc]? ? isFunctionType(getType(sc)) ==> sc == fun : true};
        vars = sort([v | v <- locally_defined, is_variable(v)], bool(Define a, Define b){ return a.defined.offset < b.defined.offset;});
        formals = [v | v <- vars, is_formal(v)];
        outer_formals = [v | v <- formals, is_outer_formal(v) ];
        non_formals = [v | v <- vars, !is_formal(v)];
    
        /* frame organization: outer_formals, kw_map, kw_defaults, formals, variables */
        // ***Note: Includes keyword parameters as a single map parameter when applicable
        ftype = defInfo(AType atype) := fundef.defInfo ? atype : avalue();
        kwpDelta = (ftype has kwFormals && size(ftype.kwFormals) > 0 ||
                    ftype has kwFields && size(ftype.kwFields) > 0) ? 1 : 0;
        formal_base = /*size(formals) + */kwpDelta; 
        for(int i <- index(vars)){
            vdefine = vars[i];
            if(vdefine.idRole != keywordFormalId()){
                position_in_container[vdefine.defined] = formal_base + i;
            }
        }
    }
   
   sorted_functions_and_constructors  = sort(functions+constructors);
   //functions_and_constructors_to_int = (sorted_functions_and_constructors[i]: i | int i <- index(sorted_functions_and_constructors));
   //functions_and_constructors_to_fuid =  (l : convert2fuid(l) | loc l <- sorted_functions_and_constructors);
  
  // create overloading resolvers ...
   
   funNameAndDef = { <def.id, def> | cf <- functions+constructors, def := definitions[cf] };
    
   noverloaded = 0;
   
   // ... all overloaded functions
   
   bool compatible(afunc(AType ret1, list[AType] formals1, list[Keyword] kwFormals1),
                   afunc(AType ret2, list[AType] formals2, list[Keyword] kwFormals2))
    = ret1 == ret2 && formals1 == formals2;
    
   bool compatible(acons(AType adt1, list[AType] fields1, list[Keyword] kwFields1),
                   acons(AType adt2, list[AType] fields2, list[Keyword] kwFields2))
    = adt1 == adt2 && fields1 == fields2;
    
  bool compatible(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals),
                  acons(AType adt, list[AType] fields, list[Keyword] kwFields))
    = ret == adt && formals == fields;
    
  bool compatible(acons(AType adt, list[AType] fields, list[Keyword] kwFields),
                  afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
    = ret == adt && formals == fields;
    
   default bool compatible(AType t1, AType t2) = false;
   
   for(fname <- domain(funNameAndDef)){
        defs = funNameAndDef[fname];
        types = {};
        for(def <- defs){
            tp = unsetRec(getDefType(def.defined));
            types += tp;
        }
        for(ftype <- types){
            ovl_non_defaults = [];
            ovl_defaults = [];
            ovl_constructors = [];
            reduced_overloads = {};
       
            resType = avoid();
            formalsType = avoid();
            
            for(def <- defs){
                tp = unsetRec(getDefType(def.defined));
                if(compatible(tp, ftype)){
                     reduced_overloads += <def.defined, def.idRole, tp>;
                     resType = alub(resType, getResult(tp));
                     formalsType = alub(formalsType, atypeList(getFormals(tp)));
                    if(def.idRole == constructorId()){
                        ovl_constructors += def.defined;
                    } else if(def.idRole == functionId()){
                        if(def.defined in defaultFunctions) 
                            ovl_defaults += def.defined;
                        else 
                            ovl_non_defaults += def.defined;
                    }
               }
            }
            sorted_non_defaults = sortFunctions(ovl_non_defaults);
            sorted_defaults = sortFunctions(ovl_defaults);
            sorted_constructors = sortFunctions(ovl_constructors);
            
            funs = sorted_non_defaults+sorted_defaults;
            seenCannotFail = false;
            cannotFailFun = |unknown:///|;
            for(f <- funs){
                if(seenCannotFail){ 
                    println ("****WARNING**** function <f> will never be called, overruled by <cannotFailFun>");
                } else {
                    cf = definitions[f].defInfo.canFail;
                    if(!cf){
                        seenCannotFail = true;
                        cannotFailFun = f;
                    }
                }
            }
            
            oname = "<fname>_AT_<intercalate("_OR_",  abbreviate(sorted_non_defaults + sorted_defaults + sorted_constructors))>";
            ofun = <fname,  ftype, oname, sorted_non_defaults + sorted_defaults, sorted_constructors>;
            if(!overloadingResolver[oname]?){
                overloadedFunctions += ofun;
                overloadingResolver[oname] = noverloaded;
                noverloaded += 1;
                overloadedTypeResolver[<fname, afunc(resType, formalsType.atypes, [])>] = oname;
            }
        }
   }
}

list[str] abbreviate(list[loc] locs){
    return for(l <- locs){
               k = findLast(l.path, "/");
               append "<l.path[k+1 .. -4]>_<l.begin.line>";
           };
}

loc declareGeneratedFunction(str name, str fuid, AType rtype, loc src){
	//println("declareGeneratedFunction: <name>, <rtype>, <src>");
    uid = src;
    functions += {uid};
   
    return  src; //-1; //overloadingResolver[fuid];
}

/********************************************************************/
/*     Part II: Retrieve type information                           */
/********************************************************************/

// Get the type of an expression as Symbol
private AType getType0(loc l) {
   //println("getType(<l>)");
    if(definitions[l]?){
        return getDefType(l);
    }
    assert specializedFacts[l]? || facts[l]? : "getType for <l>";
    if(specializedFacts[l]?){
        return specializedFacts[l];
    }
    if(facts[l]?){
    	return facts[l];
    }
    throw "getType0 cannot find type for <l>";
}	
AType getType(loc l) {
    tp = getType0(l);
    if(tvar(u) := tp, u != l) return getType(u);
    return tp;
}

AType getType(Tree e) {
    return getType(e@\loc);
}

// Get the type of an expression as string
// str getType(Tree e) = "<getType(e@\loc)>";

// Get the outermost type constructor of an expression as string
str getOuterType(Tree e) { 
    tp = getType(e@\loc);
	if(aparameter(str _, AType bound) := tp) {
		return "<getName(bound)>";
	}
	//if(label(_, Symbol sym) := tp){
	//   return "<getName(sym)>";
	//}
	if(aadt(_, _, sr) := tp && isConcreteSyntaxRole(sr)){
	   return "nonterminal";
	}
	return "<getName(tp)>";
}

/* 
 * Get the type of a function.
 * Getting a function type by name is problematic in case of nested functions,
 * given that 'fcvEnv' does not contain nested functions;
 * Additionally, it does not allow getting types of functions that are part of an overloaded function;
 * Alternatively, the type of a function can be looked up by its @loc;   
 */
AType getFunctionType(loc l) {  
   tp = getDefType(l);
   if(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals) := tp) {
       return tp;
   } else {
       throw "Looked up a function, but got: <tp> instead";
   }
}

list[MuExp] getNestedParameters(loc l){
    fundef = definitions[l];
    //println("getNestedParameterNames: <l>");
    locally_defined = { *(vars_per_scope[sc] ? {}) | sc <- td_reachable_scopes[fundef.defined] }; //TODO declaredIn[sc] == fun
    positionals = sort([v | v <- locally_defined, is_positional_formal(v)], bool(Define a, Define b){ return a.defined.offset < b.defined.offset;});
   iprintln(positionals);
    return [ muVar(vdef.id, "<vdef.scope>", getPositionInScope(vdef.id, vdef.defined),  getTypeFromDef(vdef)) | vdef <- positionals, vdef.idRole == formalId(), hasPositionInScope(vdef.id, vdef.defined) ];
    return [ vdef.id | vdef <- positionals ];
    //return [definitions[v].id | v <- containment[l], is_positional_formal(v)];
}

list[MuExp] getNestedParameters(Define fundef){
    //println("getNestedParameterNames: <l>");
    locally_defined = { *(vars_per_scope[sc] ? {}) | sc <- td_reachable_scopes[fundef.defined] }; //TODO declaredIn[sc] == fun
    positionals = sort([v | v <- locally_defined, is_positional_formal(v)], bool(Define a, Define b){ return a.defined.offset < b.defined.offset;});
   iprintln(positionals);
    return [ muVar(vdef.id, "<vdef.scope>", getPositionInScope(vdef.id, vdef.defined),  getTypeFromDef(vdef)) | vdef <- positionals, vdef.idRole == formalId(), hasPositionInScope(vdef.id, vdef.defined) ];
    return [ vdef.id | vdef <- positionals ];
    //return [definitions[v].id | v <- containment[l], is_positional_formal(v)];
}

//list[str] getNestedParameterNames(loc l){
//    fundef = definitions[l];
//    //println("getNestedParameterNames: <l>");
//    locally_defined = { *(vars_per_scope[sc] ? {}) | sc <- td_reachable_scopes[fundef.defined] }; //TODO declaredIn[sc] == fun
//    positionals = sort([v | v <- locally_defined, is_positional_formal(v)], bool(Define a, Define b){ return a.defined.offset < b.defined.offset;});
//    return [ vdef.id | vdef <- positionals ];
//    //return [definitions[v].id | v <- containment[l], is_positional_formal(v)];
//}

AType getClosureType(UID uid) {
   tp = getType(uid);
   if(afunc(_,_,_) := tp){
        return tp;
   } else {
       throw "Looked up a closure, but got: <tp> instead";
   }
}

tuple[AType atype, bool isKwp] getConstructorInfo(AType adtType, AType fieldType){
    adtType = unsetRec(adtType);
    for(uid <- constructors){
        consType = getDefType(uid);
        //iprintln(consType);
        if(unsetRec(consType.adt) == adtType){
            if(fieldType in consType.fields){
                return <consType,false>;
            } else {
                for(<AType kwType, Expression defaultExp> <- consType.kwFields){
                    if(kwType == fieldType){
                        return <consType, true>;
                    }
                 }
            }
        }
    }
    throw "getConstructor, no constructor found for <adtType>, <fieldType>";
}
	
alias KeywordParamMap = map[str kwName, AType kwType];
	
KeywordParamMap getKeywords(Parameters parameters){
    KeywordFormals kwfs = parameters.keywordFormals;
    return ("<kwf.name>" : kwtp | kwf <- kwfs.keywordFormalList, kwtp := getType(kwf));
}

map[str, map[str, value]] getConstantConstructorDefaultExpressions(loc location){
    tp = getType(location);
    return constructorConstantDefaultExpressions[tp] ? ();
}

tuple[str fuid, int pos] getVariableScope(str name, loc l) {
iprintln(definitions);
  println("getVariableScope: <name>, <l>, <definitions[l] ? "QQQ">, <declaredIn[l] ? "XXX">, <useDef[l] ? "YYY">)");
  container = |global-scope:///|;
  if(definitions[l]?) container = findContainer(definitions[l]);
  else {
    bool found = false;
    for(c <- functions){
        if(containedIn(l, c)){ container = c; found = true; break; }
    }
    if(!found){
        for(c <- modules){
         if(containedIn(l, c)){ container = c; found = true; break; }
        }
    }
    if(!found) throw "No container found for <name>, <l>";
  }
  
  cdef = definitions[container];
  if(cdef.idRole == functionId()) return <convert2fuid(container), getPositionInScope(name, l)>;
  if(cdef.idRole == moduleId()) return <cdef.id, getPositionInScope(name, l)>;
  throw "getVariableScope fails for <name>, <l>";
}

int getPositionInScope(str name, loc l){
    println("getPositionInScope:<name>, <l>");
    iprintln(position_in_container);
    uid = l in definitions ? l : getFirstFrom(useDef[l]); 
    return position_in_container[uid] ? 0;
}

bool hasPositionInScope(str name, loc l){
    uid = l in definitions ? l : getFirstFrom(useDef[l]); 
    return position_in_container[uid]?;
}

// Create unique symbolic names for functions, constructors and productions

str getFUID(str fname, AType tp) { 
    res = "<fname>(<for(p<-tp.formals){><p>;<}>)";
    //println("getFUID: <fname>, <\type> =\> <res>");
    return res;
}
  	
str getFUID(str modName, str fname, AType tp, int case_num) = 
	"<modName>/<fname>(<for(p<-tp.formals?[]){><p>;<}>)#<case_num>";


str getMakerForConstructor(UID uid) {
    consType = getType(uid);
    return "$make_<consType.adt.adtName>_<consType.label>";
    //convert2fuid(uid) + "_companion";
}

str getGetterForKwpField(UID uid, str fieldName){
    consType = getType(uid);
    return "$get_<consType.adt.adtName>_<consType.label>_<fieldName>";
}

str convert2fuid(UID uid) {
	if(uid == |global-scope:///|)
	   return "global-scope";
	tp = getType(uid);
	str name = definitions[uid]? ? definitions[uid].id : "XXX";

    if(declaredIn[uid]?) {
       if(declaredIn[uid] != |global-scope:///| && declaredIn[uid] != uid) name = name + "_<uid.begin.line>_<uid.end.line>";
	   //if(declaredIn[uid] != |global-scope:///| && declaredIn[uid] != uid) name = convert2fuid(declaredIn[uid]) + "$" + name + "_<uid.begin.line>_<uid.end.line>";
    }
	return name;
}

public int getTupleFieldIndex(AType s, str fieldName) = 
    indexOf(getTupleFieldNames(s), fieldName);

public rel[str fuid,int pos] getAllVariablesAndFunctionsOfBlockScope(loc block) {
    locally_defined = { v.defined | sc <- td_reachable_scopes[block], v <- (vars_per_scope[sc] ? {}) };
    return { <convert2fuid(declaredIn[decl]), position_in_container[decl]> | UID decl <-locally_defined, position_in_container[decl]?};
}

// Collect all types that are reachable from a given type

map[AType,set[AType]] collectNeededDefs(AType t){
   map[AType, set[AType]] definitions = (adt : adt_constructors[adt] ? {aprod(grammar.rules[adt])} | /adt:aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole) := t);
   solve(definitions){
    definitions = definitions + (adt1 : adt_constructors[adt1] ? {aprod(grammar.rules[adt1])} | /adt:aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole) := definitions, adt1 := unsetRec(adt), !definitions[adt1]?);
   }
   return definitions;
}

/********************************************************************/
/*     Part III: Type-related code generation functions             */
/********************************************************************/

@doc{Generate a MuExp that calls a library function given its name, module's name and number of formal parameters}
/*
 * NOTE: Given that the muRascal language does not support overloading, the dependency of function uids 
 *       on the number of formal parameters has been removed 
 */
public MuExp mkCallToLibFun(str modName, str fname)
	= muFun1("<modName>/<fname>");



// Sort available overloading alternatives as follows (trying to maintain good compatibility with the interpreter):
// - inner scope first, most recent last
// - then default functions (also most inner scope first, then most recent last).

// Is inner location textually contained in outer location?
bool containedIn(loc inner, loc outer){
    return inner.path == outer.path && inner.offset >= outer.offset && inner.offset + inner.length <= outer.offset + outer.length;
}

// Occurs location before before location after?
bool occursBefore(loc before, loc after){
    return before.path == after.path && before.offset + before.length < after.offset;
}

bool funFirst(UID n, UID m) {
    if(n == m) return false;
    if(n.path != m.path) return n.path < m.path;
    if(containedIn(n, m)) return true;
    return occursBefore(n, m);
}

list[loc] sortFunctions(list[UID] items){
  res = sort(items, funFirst);
  //println("sortOverloadedFunctions: <items> =\> <res>");
  return res;
}

public UID declaredScope(UID uid) {
    return declaredIn[uid];
}

// Generate a MuExp to access a variable

MuExp mkVar(str name, loc l) {
  defs = useDef[l];
  if(size(defs) > 1){
    assert all(d <- defs, definitions[d].idRole in {functionId(), constructorId()}) : "Only functions can have multiple definitions" ;
   println("overloadedTypeResolver:"); iprintln(overloadedTypeResolver);
   println("<name>, <l>");
    ftype = unsetRec(getType(l));
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := ftype){
        resType = avoid();
        formalsType = avoid();
        for(<def, idrole, tp> <- overloads){
            println("<def>, <idrole>, <tp>");
            resType = alub(resType, getResult(tp));
            formalsType = alub(formalsType, atypeList(getFormals(tp)));
        }
        ftype = atypeList(atypes) := formalsType ? afunc(resType, formalsType.atypes, []) : afunc(resType, [formalsType], []);
    }
    //iprintln(overloadedTypeResolver);
    return muOFun(overloadedTypeResolver[<name,ftype>]);
  }
  uid = getFirstFrom(defs);
  def = definitions[uid];
  
  // Keyword parameters
  if(def.idRole == keywordFormalId()) {
       return muVarKwp(name, getScope(uid), getType(l));
  }
  
  if(def.idRole == fieldId()) {
    scp = getScope(uid);
    //pos = getPositionInScope(name, uid);
    return /*scp in modules ? muModuleVar(name, scp, pos) :*/ muVar(name, scp, -1, getType(l));
  }
  
  if(def.idRole == keywordFieldId()){
       return muVarKwp(name, getScope(uid), getType(l));
  }
  
  if(def.idRole in variableRoles){
    scp = getScope(uid);
    pos = getPositionInScope(name, uid);
    return /*scp in modules ? muModuleVar(name, scp, pos) :*/ muVar(name, scp, pos, getType(l));
  }
  
  if(def.idRole == constructorId()){
    return muConstr(getType(l));
  
  }
  if(def.idRole == functionId()){ 
    //tp = getType(l);
    //if(uid in overloadedFunctions) return muOFun(overloadedTypeResolver[unsetRec(getType(l))]);
    return muFun1(uid); //muFun1(functions_and_constructors_to_fuid[uid]);
  }
    throw "End of mkVar reached for <name> at <l>: <def>";
}

// Generate a MuExp for an assignment

MuExp mkAssign(str name, loc l, MuExp exp) {

    uid = l in definitions ? l : getFirstFrom(useDef[l]); // TODO: what if more than one def?
    def = definitions[uid];
    if(def.idRole == keywordFormalId()) {
        return muAssign(muVarKwp(name, getScope(uid), getType(l)), exp);
    }
    if(def.idRole in positionalFormalRoles){
        return muAssign(muVar(name, getScope(uid), getPositionInScope(name, uid), getType(l)), exp);   // TODO
   }

    if(def.idRole in variableRoles){
        //println("mkAssign: <l>");
        //println("mkAssign, scope: <getScope(uid)>");
        //println("mkAssign: <def>");
        //println("mkAssign: isinit: <l == def.defined>");
        
        return l == def.defined ? muVarInit(muVar(name, getScope(uid), getPositionInScope(name, uid), getType(l)), exp)
                                : muAssign(muVar(name, getScope(uid), getPositionInScope(name, uid), getType(l)), exp);
    }
    throw "mkAssign fails for <name>, <l>, <exp>";
}

// TODO: the following functions belong in ParseTree, but that gives "No definition for \"ParseTree/size(list(parameter(\\\"T\\\",value()));)#0\" in functionMap")

@doc{Determine the size of a concrete list}
int size(appl(regular(\iter(Symbol symbol)), list[Tree] args)) = size(args);
int size(appl(regular(\iter-star(Symbol symbol)), list[Tree] args)) = size(args);

int size(appl(regular(\iter-seps(Symbol symbol, list[Symbol] separators)), list[Tree] args)) = size_with_seps(size(args), size(separators));
int size(appl(regular(\iter-star-seps(Symbol symbol, list[Symbol] separators)), list[Tree] args)) = size_with_seps(size(args), size(separators));

int size(appl(prod(Symbol symbol, list[Symbol] symbols), list[Tree] args)) = 
	\label(str label, Symbol symbol1) := symbol && [Symbol itersym] := symbols
	? size(appl(prod(symbol1, symbols), args))
	: size(args[0]);

default int size(Tree t) {
    iprintln(t);
    throw "Size of tree not defined for \"<t>\"";
}

private int size_with_seps(int len, int lenseps) = (len == 0) ? 0 : 1 + (len / (lenseps + 1));


AType getElementType(alist(AType et)) = et;
AType getElementType(aset(AType et)) = et;
AType getElementType(abag(AType et)) = et;
AType getElementType(AType t) = avalue();