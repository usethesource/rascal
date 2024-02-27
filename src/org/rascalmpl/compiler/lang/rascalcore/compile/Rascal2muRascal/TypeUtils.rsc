@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::TypeUtils

import IO;
import Set;
import Map;
import Node;
import Relation;
import Set;
import String;
import ListRelation;
import util::Math;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::compile::util::Names;  // TODO: merge name utils
import lang::rascalcore::check::NameUtils;
import lang::rascalcore::compile::Rascal2muRascal::TmpAndLabel;

import Location;

import lang::rascalcore::check::BasicRascalConfig;
import lang::rascalcore::check::BuiltinFields;
import Type;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;

import lang::rascalcore::check::ATypeInstantiation;



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

//private loc moduleScope = |unknown:///|;
//
//void setModuleScope(loc l){
//    moduleScope = convert2fuid(l);
//}  
//
//loc getModuleScope()
//    = moduleScope;

alias UID = loc;                                    // A UID is a unique identifier determined by the source location of a construct

/*
 * We will use FUID (for Function UID) to create a readable string representation for any enity of interest. 
 */

alias FUID = str; 

TModel current_tmodel = tmodel();                   // TModel for current module

TModel getTModel() = current_tmodel;                // Get the (possibly updated) TModel

private AGrammar current_grammar = grammar({}, ()); // Grammar for current module

AGrammar getGrammar() = current_grammar;            // Get the grammar

rel[AType, AType] reachableConcreteTypes = {};      // Transitive closure of type usage in grammar

private set[AType] ADTs = {};                       // ADTs defined in current module

set[AType] getADTs() = ADTs;                        // Get ADTs

Scopes scopes = ();                                 // All scopes
rel[UID,UID] td_reachable_scopes = {};              // Transitive closure of top down reachable scopes
set[UID] module_scopes = {};                        // Scopes of all modules

map[UID,UID] declaredIn = ();                       // Map from declared enity to its declaring function or module
rel[UID,UID] declares = {};                         // Relation from declaring functionn or module to the entities declared in it.

map[UID,AType] facts = ();                          // All facts derived by the type checker
map[UID,AType] specializedFacts = ();               // Facts resulting from overloading resolutiom
    
rel[UID, UID] useDef = {};                          // Relation from uses to definitions
rel[UID, UID] defUses = {};                         // Relation from defintion to all its uses

map[UID, Define] definitions = ();                  // All definitions
map[UID, Define]  getDefinitions() = definitions;

map[loc,loc] physical2logical = ();
                         
map[UID,int]  position_in_container = ();           // Position of variable in containing function or module
map[UID,set[Define]] vars_per_scope = ();           // The variables introduced per scope
map[UID, list[Define]] vars_per_fun = ();           // Variables (including formals) per function;

set[UID] functions = {};                            // Declared functions

bool isFunction(UID uid) = uid in functions;

set[UID] defaultFunctions = {};                     // Declared default functions

bool isDefaultFunction(UID uid) = uid in defaultFunctions;

set[UID] constructors = {};                            // Declared constructors
map[AType, set[AType]] adt_constructors = ();          // Map from ADT to its constructors
rel[str consName, AType adt, AType argType] adt_uses_type = {} ;  // Constructor of ADT has argument of given type
rel[AType, AType] reachableTypes = {};                 // Transitive closure of type usage
map[AType, list[Keyword]] adt_common_keyword_fields = (); // all common keyword fields that may have been declared in separate data declarations
map[AType, map[str,AType]] adt_common_keyword_fields_name_and_type = ();

set[AType] getConstructors()
    = { getDefType(uid) | uid <- constructors};

map[AType, set[AType]] getConstructorsMap()
    = adt_constructors;
    
map[AType, map[str,AType]] getCommonKeywordFieldsNameAndType(){
    return adt_common_keyword_fields_name_and_type;
}
 
private map[str,int] module_var_init_locals = ();	// number of local variables in module variable initializations

int getModuleVarInitLocals(str mname) {
	//assert module_var_init_locals[mname]? : "getModuleVarInitLocals <mname>";
	return module_var_init_locals[mname] ? 0;
}

private map[tuple[UID,UID], bool] funInnerScopes = ();

str unescape(str name) = name[0] == "\\" ? name[1..] : name;

// Reset the above global variables, when compiling the next module.

public void resetScopeExtraction() {  
    current_tmodel = tmodel();
    functions = {}; 
    defaultFunctions = {};
    module_var_init_locals = ();
    declares = {};
    declaresMap = ();
    declaredIn = ();
    funInnerScopes = ();
    scopes = ();
    module_scopes = {};
    facts = (); 
    specializedFacts = ();
    useDef = {};
    defUses = {};
    definitions = ();
    physical2logical = ();
    position_in_container = ();
    vars_per_scope = ();
    vars_per_fun = ();
    functions = {};
    constructors = {};
    adt_constructors = ();
    adt_uses_type = {};
    reachableTypes = {};
    reachableConcreteTypes = {};
    adt_common_keyword_fields = ();
    adt_common_keyword_fields_name_and_type = ();
}

bool isDefinition(UID d){
    return definitions[d]?;
}

void addDefineAndType(Define def, AType tp){
    definitions[def.defined] = def;
    current_tmodel.definitions[def.defined] = def;
    facts[def.defined] = tp;
    current_tmodel.facts[def.defined] = tp;
}

Define getDefine(UID d)
    = definitions[d];
    
loc getDefinition(UID d){
    return definitions[d].defined;
}

bool isUse(UID u){
    return !isEmpty(useDef[u]);
}

private AType getDefType(UID d){
    di = definitions[d].defInfo;
    if(defType(AType atype) := di) return atype;
    if(defType(Tree tree) := di) return getDefType(getLoc(tree));
    if(defType(loc l) := di) return facts[l];
    throw "getDefType: <d>";
}

AType getTypeFromDef(Define def){ // Move to Solver
    di = def.defInfo;
    if(defType(AType atype) := di) return atype;
    if(defType(Tree tree) := di) return getDefType(getLoc(tree));
    if(defType(loc l) := di) return facts[l];
    throw "getTypeFromDef: <def>";
}


private str getScope(UID uid){
    return convert2fuid(declaredIn[uid]);
}
 
bool is_formal(Define d) = d.idRole in formalRoles;
bool is_positional_formal(Define d) = d.idRole in positionalFormalRoles;
bool is_outer_formal(Define d) = d.idRole in outerFormalRoles;
bool is_variable(Define d) = d.idRole in variableRoles + fieldId();
bool is_non_keyword_variable(Define d) = d.idRole in variableRoles - {keywordFormalId()};
bool is_module_variable(Define d) = d.idRole == moduleVariableId() && d.scope in module_scopes;
bool is_module_or_function(loc l) = definitions[l]? && definitions[l].idRole in {moduleId(), functionId()};
bool is_module(Define d) = d.idRole in {moduleId()};
bool is_assignable(Define d) = d.idRole in assignableRoles;

bool is_declared_in_module(UID uid) = definitions[getFirstFrom(useDef[uid])].idRole == moduleId();

private loc findContainer(Define d){
    //println("findContainer for <d>");
    if(is_module(d)) return d.defined;
    cscope = d.scope;
    while(!is_module_or_function(cscope) || cscope == |global-scope:///|) { 
        //println("cscope = <cscope>");
        if(scopes[cscope]?){
            if(cscope == scopes[cscope]){
                println("WARNING: findContainer");
                println("scopes:"); iprintln(scopes);
                println("d: <d>");
                println("cscope: <cscope>");
                return cscope;
            }
            cscope = scopes[cscope];
        } else {
            return cscope;
        }
    }
    return cscope;
}

str findDefiningModule(loc l){
    for(ms <- module_scopes,isContainedIn(l, ms)){
        return definitions[ms].id;
    }
    throw "No module found for <l>";
}

// Identify "dummy" formals in function types that are to be ignored
list[AType] dummyFormalsInReturnType(af: afunc(ret, _, _)){
    return [ *formalsList1 | /af2:afunc(_, formalsList1, _) := ret, af2 != af ];
}

// Identify "dummy" formals in function return type that are to be ignored

default list[AType] dummyFormalsInReturnType(AType t) =[];

list[AType] dummyFormalsInType(AType t){
    result = [];
    top-down-break visit(t){
        case afunc(_, formalsList1, _): result += formalsList1;
    }
    return result;
}
    
// extractScopes: extract and convert type information from the TModel delivered by the type checker.
void extractScopes(TModel tm){
    tm = convertTModel2PhysicalLocs(tm);
    current_tmodel = tm;
    physical2logical = invertUnique(tm.logical2physical);
    
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
    set[loc] modules = {};
    //setModuleScope(tm.moduleLocs[tm.modelName]);
    
    if([*AGrammar gs] := tm.store[key_grammar]){
        current_grammar = gs[0];
        //println("extractScopes:"); iprintln(current_grammar);
    } else {
        iprintln(tm.store[key_grammar]);
        throw "`grammar` has incorrect format in store";
    }
    
    reachableConcreteTypes =  {<nt, unset(t, "alabel")> | nt <- current_grammar.rules, /AType t := current_grammar.rules[nt] }+;
    
    if([*set[AType] adts] := tm.store[key_ADTs]){
        ADTs = adts[0];
    } else if({} := tm.store[key_ADTs]){
        ADTs = {};
    } else {
        throw "`ADTs` has incorrect format in store";
    }
   
    if([*lrel[AType,KeywordFormal] commons] := tm.store[key_common_keyword_fields], !isEmpty(commons)){
        common = commons[0];
        //println("Common keyword parameters");
        //iprintln(common);
        adt_common_keyword_fields = ( adtType : [ <getType(kwf.\type)[alabel="<kwf.name>"], kwf.expression> | kwf <- common[adtType]] | adtType <- domain(common) );
        adt_common_keyword_fields_name_and_type = ( adtType : ( "<kwf.name>" : getType(kwf.\type) | kwf <- common[adtType]) | adtType <- domain(common) );
    }
    
    for(def <- defines){
        switch(def.idRole){
            case functionId(): {
                functions += def.defined;
                if(def.defInfo has modifiers && "default" in def.defInfo.modifiers) defaultFunctions += def.defined;
            }  
            case constructorId(): {
                 constructors += def.defined;
                 consType = def.defInfo.atype;
                 consName = consType.alabel;
                 consAdtType = consType.adt;
                 adt_uses_type +=   { <consName, consAdtType, unsetRec(fieldType)> | /AType fieldType := consType.fields }
                                  + { <consName, consAdtType, unsetRec(kwFieldType)> | /AType kwFieldType := consType.kwFields };
                 if(adt_constructors[consAdtType]?){
                    adt_constructors[consAdtType] = adt_constructors[consAdtType] + consType;
                 } else {
                    adt_constructors[consAdtType] = {consType};
                 }
                 }
            case moduleId():
                 modules += def.defined;
            case moduleVariableId():
                 ;
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
    for(/consType: acons(AType adt, list[AType] _fields, list[Keyword] _kwFields) := facts){
        consName = consType.alabel;
        if(adt_constructors[adt]?){
           adt_constructors[adt] = adt_constructors[adt] + consType;
        } else {
           adt_constructors[adt] = {consType};
        }
    }
       
    reachableTypes = (adt_uses_type<1,2>)+;
    
    // Determine position of module variables
    for(loc m <- modules){
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
    
    
   
    
    // Determine position of variables inside functions
    
    //iprintln(functions);
    for(fun <- functions){   
        fundef = definitions[fun];
        ftype = defType(AType atype) := fundef.defInfo ? atype : avalue();
        dummies = dummyFormalsInReturnType(ftype);
        //println("td_reachable_scopes[fundef.defined]: <td_reachable_scopes[fundef.defined]>");
       // println("vars_per_scope:"); iprintln(vars_per_scope);
        locally_defined = { *(vars_per_scope[sc] ? {}) | sc <- td_reachable_scopes[fundef.defined], (facts[sc]? && isFunctionAType(getType(sc))) ? sc == fun : true};
        //locally_defined = { *(vars_per_scope[sc] ? {}) | sc <- td_reachable_scopes[fundef.defined], facts[sc]? ? isFunctionAType(getType(sc)) ==> sc == fun : true};
        locally_defined = { v | v <- locally_defined, v.defInfo.atype notin dummies };
        
        vars = sort([v | v <- locally_defined, is_variable(v)], bool(Define a, Define b){ return a.defined.offset < b.defined.offset;});
        formals = [v | v <- vars, is_formal(v)];
        
        vars_per_fun[fun] = formals;
        //outer_formals = [v | v <- formals, is_outer_formal(v) ];
        //non_formals = [v | v <- vars, !is_formal(v)];
    
        ftype = defType(AType atype) := fundef.defInfo ? atype : avalue();
        kwpDelta = (ftype has kwFormals && size(ftype.kwFormals) > 0 ||
                    ftype has kwFields && size(ftype.kwFields) > 0) ? 1 : 0;
        formal_base = /*size(formals) + */kwpDelta; 
        for(int i <- index(vars)){
            vdefine = vars[i];
            if(vdefine.idRole != keywordFormalId()){
                position_in_container[vdefine.defined] = (is_formal(vdefine) ? 0 : formal_base) + i;
            }
        }
        //println("position_in_container:");
        //iprintln(position_in_container);
    }
}

//private list[str] abbreviate(list[loc] locs){
//    return for(l <- locs){
//               k = findLast(l.path, "/");
//               append "<l.path[k+1 .. -4]>_<l.begin.line>A<l.offset>";
//           };
//}

loc declareGeneratedFunction(str _name, str _fuid, AType _rtype, loc src){
	//println("declareGeneratedFunction: <name>, <rtype>, <src>");
    uid = src;
    functions += {uid};
   
    return src;
}

/********************************************************************/
/*     Part II: Retrieve type information                           */
/********************************************************************/

// Get the type of an expression as Symbol
private AType getType0(loc l) {
   //println("getType(<l>)");
    //assert specializedFacts[l]? || facts[l]? : "getType for <l>";
    if(specializedFacts[l]?){
        return specializedFacts[l];
    }
    if(facts[l]?){
    	return facts[l];
    }
    if(definitions[l]?){
        return getDefType(l);
    }
    //println("*** getType0 ***");
    //iprintln(facts, lineLimit=10000);
    throw "getType0 cannot find type for <l>";
}	
AType getType(loc l) {
    //println("getType: <l>");
    tp = getType0(l);
    tp = visit(tp) { case tvar(u): { insert u != l ? getType(u) : avalue();} };
    //if(tvar(u) := tp, u != l) return getType(u);
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
   if(afunc(AType _, list[AType] _, list[Keyword] _) := tp) {
       return tp;
   } else {
       throw "Looked up a function, but got: <tp> instead";
   }
}

AType getClosureType(UID uid) {
   tp = getType(uid);
   if(afunc(_,_,_) := tp){
        return tp;
   } else {
       throw "Looked up a closure, but got: <tp> instead";
   }
}

map[AType, list[Keyword]] getCommonKeywordFieldsMap()
    = adt_common_keyword_fields;

tuple[str moduleName, AType atype, bool isKwp] getConstructorInfo(AType adtType, AType fieldType, str fieldName){
    //println("getConstructorInfo: <adtType>, <fieldType>, <fieldName>");
    assert isADTAType(adtType) : "getConstructorInfo: <adtType>";
    adtType = unsetRec(adtType);
    is_start = false;
    if(isStartNonTerminalType(adtType)){
        adtType = getStartNonTerminalType(adtType);
        is_start = true;
    }
    adtType1 = adtType;  // TODO: this is to ensure that adType is a constant inside visit below
    fieldType = fieldType[alabel=fieldName];
    if(adtType1 in domain(getBuiltinFieldMap())){
        res = <"", adtType1 /*getBuiltinFieldMap()[adtType1][fieldName]*/, false>;
        return res;
    }
    set[AType] constructors = {};
   
    adt_arity = size(adtType1.parameters ? []);
    if(adt_arity == 0){
        constructors = adt_constructors[adtType1] ? {};
    } else { // a parameterized ADT, find it and substitute actual parameters (also in fieldType)
        for(adt <- adt_constructors){
            if(adt.adtName == adtType1.adtName && size(adt.parameters) == adt_arity){
                pnames = (adt.parameters[i] : i | i <- [0..adt_arity]);
                constructors = adt_constructors[adt];
                <constructors, fieldType> = 
                    visit(<constructors, fieldType>) { 
                        case p:aparameter(str _pname, _): { if(pnames[p]?){
                                                                repl = adtType1.parameters[pnames[p]];
                                                                if(p.alabel?) repl = repl[alabel=p.alabel];
                                                                insert repl;
                                                           }
                                                          } 
                    };
                break;
            }
        }
    }
    
    // Positonal or kw field of constructor?
    for(AType consType <-  constructors){
        for(declaredFieldType <- consType.fields){
            if(declaredFieldType.alabel == fieldName && asubtype(fieldType, declaredFieldType)){
                return <"", consType,false>;
            }
        }
        for(<AType kwType, Expression defaultExp> <- consType.kwFields){
            if(kwType == fieldType){
                return <findDefiningModule(getLoc(defaultExp)), consType, true>;
            }
         }      
    }
    
    // Common kw field of the ADT?
    
    for(Keyword kw <- adt_common_keyword_fields[adtType1] ? []){
        if("<kw.fieldType.alabel>" == fieldName){
            return <findDefiningModule(getLoc(kw.defaultExp)), adtType1, true>;
        }
    }
    
    // Field of nonterminal?
    if(getGrammar().rules[adtType1]?){
        productions = getGrammar().rules[adtType1].alternatives;
        for(prod(AType _, list[AType] atypes) <- productions){
            for(a <- atypes, a.alabel?, a.alabel == fieldName){
                return <"", is_start ? \start(a) : a, false>;
            }
        }
    }
    
    // Common kw field of concrete type?
    if(asubtype(adtType1, treeType)){
        //if(fieldName == "src"){         // TODO: remove when @\loc is gone
        //    return <"ParseTree", adtType1, true>;
        //}
        for(Keyword kw <- adt_common_keyword_fields[treeType] ? []){
            if("<kw.fieldType.alabel>" == fieldName){
                return <findDefiningModule(getLoc(kw.defaultExp)), is_start ? \start(adtType1) : adtType1, true>;
            }
        }
    }
    
    return <"", is_start ? \start(adtType1) : adtType1, false>;
    
    //throw "getConstructorInfo, no constructor found for <adtType1>, <fieldType>, <fieldName>";
}
	
alias KeywordParamMap = map[str kwName, AType kwType];
	
KeywordParamMap getKeywords(Parameters parameters){
    KeywordFormals kwfs = parameters.keywordFormals;
    return ("<kwf.name>" : kwtp | kwf <- kwfs.keywordFormalList, kwtp := getType(kwf));
}

list[MuExp] getExtendedFunctionFormals(loc funsrc, str scopeName){
    vars = vars_per_fun[funsrc];
    return  [muVar(var.id, scopeName, i, unsetRec(getTypeFromDef(var), "alabel"), formalId()) | i <- index(vars), var := vars[i]];
}

tuple[str fuid, int pos] getVariableScope(str name, loc l) {
  //iprintln(definitions);
  //println("getVariableScope: <name>, <l>, <definitions[l] ? "???">, <declaredIn[l] ? "???">, <useDef[l] ? "???">)");
  container = |global-scope:///|;
  if(definitions[l]?) container = findContainer(definitions[l]);
  else {
    bool found = false;
    for(c <- functions){
        if(isContainedIn(l, c)){ container = c; found = true; break; }
    }
    if(!found){
        for(c <- module_scopes){
         if(isContainedIn(l, c)){ container = c; found = true; break; }
        }
    }
    if(!found) throw "No container found for <name>, <l>";
  }
  
  cdef = definitions[container];
  if(cdef.idRole == functionId()) return <convert2fuid(container), getPositionInScope(name, l)>;
  if(cdef.idRole == moduleId()) return <replaceAll(cdef.id, "::", "_"), getPositionInScope(name, l)>;
  throw "getVariableScope fails for <name>, <l>";
}

int getPositionInScope(str _name, loc l){
    uid = l in definitions ? l : getFirstFrom(useDef[l]); 
    res = position_in_container[uid] ? 0;
    //println("getPositionInScope:<_name>, <l>, <res>");
    return res;
}

str convert2fuid(UID uid) {
	if(uid == |global-scope:///|)
	   return "global-scope";
	   
	str name = definitions[uid]? ? definitions[uid].id : "XXX";

    if(declaredIn[uid]?) {
       def = definitions[uid];
       if(physical2logical[def.defined]?){
          lg = physical2logical[def.defined];
          path = lg.path;
          if(path[0] == "/"){
            path = path[1..];
          }
          name = replaceAll(path, "/", "_");
       }
    }
	return name;
}

public int getTupleFieldIndex(AType s, str fieldName) = 
    indexOf(getTupleFieldNames(s), fieldName);

public rel[loc fuid,int pos] getAllVariablesAndFunctionsOfBlockScope(loc block) {
    locally_defined = { v.defined | sc <- td_reachable_scopes[block], v <- (vars_per_scope[sc] ? {}) };
    return { <declaredIn[decl], position_in_container[decl]> | UID decl <-locally_defined, position_in_container[decl]?};
    //return { <convert2fuid(declaredIn[decl]), position_in_container[decl]> | UID decl <-locally_defined, position_in_container[decl]?};
}

set[loc] getDefiningScopes(AType root)
    =  { definitions[defloc].scope 
       | defloc <- definitions,
         getDefType(defloc) == root, 
         def := definitions[defloc], 
         def.idRole in syntaxRoles
       } & module_scopes;

// Get the governing layout rule
AType getLayouts(AType root, set[AType] adts){
    root_scopes = getDefiningScopes(root);
    accessible_scopes = root_scopes + (current_tmodel.paths<0,2>*)[root_scopes];
    
    for(adt <- adts){
        if(isLayoutAType(adt)) {
            layout_scopes = getDefiningScopes(adt);
            if(layout_scopes <= accessible_scopes) return layouts(getADTName(adt));
        }
    }
    return layouts("$default$");
}

set[AType] getAccessibleADTs(AType root){
    root_scopes = getDefiningScopes(root);
    accessible_scopes = root_scopes + (current_tmodel.paths<0,2>*)[root_scopes];
    res = { adt | adt <- getADTs(), getDefiningScopes(adt) <= accessible_scopes };
    return res;
}

// Collect all types that are reachable from a given type

map[AType,set[AType]] collectNeededDefs(AType t){
    map[AType, set[AType]] my_definitions = ();
    my_grammar_rules = current_grammar.rules;
    list[AType] tparams = [];
    is_start = false;
    syntax_type = false;
    base_t = t;
    if(\start(AType t2) := base_t){
        is_start = true;
        base_t = t2;
    }
    
    if(isReifiedAType(t)){
        base_t = getReifiedType(t);
    }
    
    if(isADTAType(base_t)){
        tparams = getADTTypeParameters(base_t);
        syntax_type = base_t.syntaxRole != dataSyntax();
    }
    
    root_scopes = getDefiningScopes(base_t);
    accessible_scopes = root_scopes + (current_tmodel.paths<0,2>*)[root_scopes];
    
    allADTs = { unsetRec(adt) | adt <- ADTs };
    if(syntax_type){
        allADTs = { adt | adt <- allADTs, adt.syntaxRole != dataSyntax(), getDefiningScopes(adt) <= accessible_scopes};
        allADTs += { unsetRec(adt) |  /adt:aadt(str _, list[AType] _, SyntaxRole _) := my_grammar_rules };
    }
  
    instantiatedADTs = { adt | adt <- allADTs, params := getADTTypeParameters(adt), !isEmpty(params), all(p <- params, !isTypeParameter(p)) };
    
    parameterized_uninstantiated_ADTs = { adt | adt <- allADTs, params := getADTTypeParameters(adt), !isEmpty(params), all(p <- params, isTypeParameter(p)) };
    
    AType uninstantiate(AType t){
        if(t in instantiatedADTs){
            iparams = getADTTypeParameters(t);
            for(uadt <- parameterized_uninstantiated_ADTs){
                uadtParams = getADTTypeParameters(uadt);
                if(t.adtName == uadt.adtName && size(iparams) == size(uadtParams)){
                    return uadt;                  
                }
            }
        }
        return t;
    }
      
    my_definitions = ( adt1 : syntaxRole == dataSyntax() ? adt_constructors[adt1] : (my_grammar_rules[adt1]? ? {aprod(my_grammar_rules[adt1])} : {})
                  | /adt:aadt(str _, list[AType] _, SyntaxRole syntaxRole) := base_t, adt1 := unset(uninstantiate(adt), "alabel")
                  );             
    
    if(syntax_type){
     // Auxiliary rules for uses of instantiated parameterized nonterminals are never used, add them explicitly    
     
        definedLayouts = getLayouts(base_t, allADTs);   
        my_definitions += ( adt : {aprod(my_grammar_rules[adt])} | adt <- allADTs, my_grammar_rules[adt]? );
               
        if(is_start){
            my_definitions += (\start(base_t) : { aprod(choice(\start(base_t), { prod(\start(base_t), [ definedLayouts, base_t[alabel="top"], definedLayouts]) })) });
        }
        if(!isEmpty(parameterized_uninstantiated_ADTs)){ // add generic parameter type Tree
            my_definitions += (treeType : {});
        }
    }
   
    solve(my_definitions){
        //for(/adt:aadt(str _, list[AType] _, SyntaxRole syntaxRole) := my_definitions){
        //    adt1 = uninstantiate(unsetRec(adt));
        //    if((syntax_type ? syntaxRole != dataSyntax() : true) && !my_definitions[adt1]? && !my_grammar_rules[adt1]?){
        //        my_definitions += ( adt1 : syntax_type ? {/*aprod(my_grammar_rules[adt1])*/} : (adt_constructors[adt1] ? {/*aprod(my_grammar_rules[adt1])*/})) ;
        //     }
        // }

        my_definitions = my_definitions + ( adt1 : syntax_type ? {aprod(my_grammar_rules[adt1])} : (adt_constructors[adt1] ? {aprod(my_grammar_rules[adt1])}) 
                                    | /adt:aadt(str _, list[AType] _, SyntaxRole syntaxRole) := my_definitions,
                                      adt1 := uninstantiate(unsetRec(adt)),
                                      syntax_type ? syntaxRole != dataSyntax() : true,
                                      !my_definitions[adt1]?,
                                      !my_grammar_rules[adt1]?
                                );
    }
    
    return my_definitions;
}

/********************************************************************/
/*     Part III: Type-related code generation functions             */
/********************************************************************/

// Sort available overloading alternatives as follows (trying to maintain good compatibility with the interpreter):
// - inner scope first, most recent last
// - then default functions (also most inner scope first, then most recent last).

// Occurs location before before location after?
bool occursBefore(loc before, loc after){
    return before.path == after.path && before.offset + before.length < after.offset;
}

//private bool funFirst(UID n, UID m) {
//    if(n == m) return false;
//    if(n.path != m.path) return n.path < m.path;
//    if(isContainedIn(n, m)) return true;
//    return occursBefore(n, m);
//}

//private list[loc] sortFunctions(list[UID] items){
//  res = sort(items, funFirst);
//  //println("sortOverloadedFunctions: <items> =\> <res>");
//  return res;
//}

//public UID declaredScope(UID uid) {
//    return declaredIn[uid];
//}

// Generate a MuExp to access a variable

MuExp mkVar(str name, loc l) {
  //println("<name>, <l>");
 
  uqname = asUnqualifiedName(name);
  name_type = getType(l);
  defs = useDef[l];
  if(size(defs) > 1){
    if(all(d <- defs, definitions[d].idRole in {functionId(), constructorId()})){ //: "Only functions or constructors can have multiple definitions" ;
        ftype = unsetRec(getType(l));
        return muOFun([definitions[d].defined | d <- defs], ftype);
    }
  }
  
  Define def;
  UID uid = l;
  if(isEmpty(defs)){
    //println("mkvar: <name>, <l>");
    if(definitions[l]?){
        uid = l;
        def = definitions[l];
    } else if(isWildCard(name)){
        return muVar("_", "", -1, name_type, variableId());
    } else {
        throw "mkVar: <uqname> at <l>";
    }   
  } else {
    uid = getFirstFrom(defs);
    def = definitions[uid];
  }
  
  // Keyword parameters
  if(def.idRole == keywordFormalId()) {
       return muVarKwp(uqname, getScope(uid), name_type);
  }
  
  if(def.idRole == fieldId()) {
    scp = getScope(uid);
    //pos = getPositionInScope(name, uid);
    return /*scp in modules ? muModuleVar(name, scp, pos) :*/ muVar(uqname, scp, -1, name_type, def.idRole);
  }
  
  if(def.idRole == keywordFieldId()){
       return muVarKwp(uqname, getScope(uid), name_type);
  }
  
  if(def.idRole in variableRoles){
    scp = getScope(uid);
    pos = is_module_variable(def) ? - 1 : getPositionInScope(uqname, uid);
    return /*scp in modules ? muModuleVar(name, scp, pos) :*/ muVar(uqname, scp, pos, getType(def.defined), def.idRole);
  }
  
  if(def.idRole == constructorId()){
    return muConstr(getType(l));
  
  }
  if(def.idRole == functionId()){ 
    return muFun(uid, name_type);
  }
  throw "mkVar no case for <uqname> at <l>: <def>";
}

// Generate a MuExp for an assignment

MuExp mkAssign(str name, loc l, MuExp exp) {
    Define def;
    UID uid;
    if(l in definitions){
    	uid = l;
        def = definitions[l];
    } else {
        defs = [ d | d <- useDef[l], is_assignable(definitions[d])];
        if(isEmpty(defs)){
            throw "mkAssign: no assignable define for <name>, <l>";
        }
        if(size(defs) > 1){
            throw "mkAssig: ambiguous define for <name>, <l>, <defs>";
        }
        uid = defs[0];
        def = definitions[defs[0]];
    }
    
    if(def.idRole == keywordFormalId()) {
        return muAssign(muVarKwp(name, getScope(uid), filterOverloads(getType(l), {keywordFormalId()})), exp);
    }
    if(def.idRole in positionalFormalRoles){
        return muAssign(muVar(name, getScope(uid), getPositionInScope(name, uid), filterOverloads(getType(l), positionalFormalRoles), def.idRole), exp);   // TODO
   }

    if(def.idRole in variableRoles){
         pos = def.scope in module_scopes ? -1 : getPositionInScope(name, uid);
        
        return l == def.defined ? muVarInit(muVar(name, getScope(uid), pos, filterOverloads(getType(l), variableRoles), def.idRole), exp)
                                : muAssign(muVar(name, getScope(uid), pos, filterOverloads(getType(l), variableRoles), def.idRole), exp);
    }
    iprintln(useDef[l]);
    throw "mkAssign fails for <name>, <l>, <exp>, <def>";
}

// Reachability

public map[AType,AProduction] getReifiedDefinitions() {
    return getGrammar().rules;
    //return (); // TODO
  //    // Collect all symbols
  //    set[AType] symbols = types + domain(constructors) + carrier(productions) + domain(current_grammar);
  //    
  //    map[AType,Production] definitions  = (() | collectDefs(symbol, it) | AType symbol <- symbols);
    //
    //return definitions;
}


public tuple[set[AType], set[AProduction]] getReachableTypes(AType subjectType, set[str] consNames, set[AType] patternTypes, bool concreteMatch){
    //println("getReachableTypes: <subjectType>, <consNames>, <patternTypes>, <concreteMatch>");
    
    consNames = {unescape(name) | name <- consNames};
    if(concreteMatch){
        return getReachableConcreteTypes(subjectType, consNames, patternTypes);
    } else {
        return getReachableAbstractTypes(subjectType, consNames, patternTypes);
    }
}

// PM: take (common) key word parameters into account!

private  tuple[set[AType], set[AProduction]] getReachableAbstractTypes(AType subjectType, set[str] consNames, set[AType] patternTypes){
    desiredPatternTypes = { unset(s, "alabel") | /AType s := patternTypes};
    desiredSubjectTypes = { unset(s, "alabel") | /AType s := subjectType};
    desiredTypes = desiredSubjectTypes + desiredPatternTypes;
    
    //println("desiredTypes:"); iprintln(desiredTypes);
    
    if(any(t <- desiredTypes, isSyntaxType(t) || /*isLexicalType(t) ||*/ asubtype(t, treeType))){
      // We just give up when abstract and concrete symbols occur together
      //println("descend_into (abstract) [1]: {value()}");
       return <{avalue()}, {}>;
    }
    
    prunedReachableTypes = reachableTypes ;
    if(avalue() notin desiredSubjectTypes){
        // if specific subject types are given, the reachability relation can be further pruned
        //println("reachableTypes[desiredSubjectTypes]:");
        //iprintln(reachableTypes[desiredSubjectTypes]);
        //prunedReachableTypes = reachableTypes[desiredSubjectTypes];
        prunedReachableTypes = rangeR(reachableTypes,reachableTypes[desiredSubjectTypes]);
        //println("removed from reachableTypes:"); //[<size(reachableTypes - prunedReachableTypes)>]"); 
        //for(x <- reachableTypes - prunedReachableTypes){println("\t<x>");}
    }
    
    //println("prunedReachableTypes: "); /*[<size(prunedReachableTypes)>]"); */for(x <- prunedReachableTypes){println("\t<x>");}
    descend_into = desiredTypes;
    
    domPrunedReachableTypes = domain(prunedReachableTypes);
    for(<AType from, AType to> <- prunedReachableTypes){
    //println("\<<from>, <to>\>");
        if(to in domPrunedReachableTypes){     // TODO || here was the cause 
            descend_into += {from, to};
        } else if(any(AType t <- domPrunedReachableTypes, asubtype(t, to))){
            descend_into += {from, to};
        } else if(c:acons(AType \adtsym, list[AType] _, list[Keyword] _) := from  && // TODO: check
                            (\adtsym in patternTypes || c.alabel in consNames)){
                  descend_into += {from, to};   
        } else if(c:acons(AType \adtsym, list[AType] _, list[Keyword] _) := to  && 
                            (\adtsym in patternTypes || c.alabel in consNames)){
                  descend_into += {from, to};  
        } else if(/AType s := to && s in domPrunedReachableTypes){
                descend_into += {from, to}; 
        }
        
    }
    if(avalue() in descend_into || size(descend_into) > 50){
        //println("replace by value, descend_into [<size(descend_into)>]:"); for(elm <- descend_into){println("\t<elm>");};
      descend_into = {avalue()};
    }
    tuples = { atuple(symbols) | arel(symbols) <- descend_into } + { atuple(symbols) | alrel(symbols) <- descend_into };
    // TODO: the above replaces the code below due to compiler issue:
    //tuples = { atuple(symbols) | sym <- descend_into, arel(symbols) := sym || alrel(symbols) := sym };
    
    descend_into += tuples;
    descend_into = {sym | sym <- descend_into/*, alabel(_,_) !:= sym*/ };
    //println("descend_into (abstract):"); // [<size(descend_into)>]:"); 
    //for(elm <- descend_into){println("\t<elm>");};
    
    return <descend_into, {}>;
}

set [AProduction] addAlts(AProduction alts){
    set [AProduction] descend_into = {};
    for(/AProduction p := alts){
        switch(p){
           case choice(_, choices): descend_into += choices;
           case associativity(_, _, set[AProduction] choices): descend_into += choices;
           case priority(_, list[AProduction] choices): descend_into += toSet(choices);
           default: {
                descend_into += p;
            }
        }
    }
    return descend_into;
}

AProduction instantiate(list[AType] formals, list[AType] actuals, AProduction p){
    for(int i <- index(formals)){
        fi = formals[i];
        p = visit(p) { case fi => actuals[i] };
    }
    return p;
}

// Extract the reachable concrete types

tuple[set[AType], set[AProduction]] getReachableConcreteTypes(AType subjectType, set[str] _consNames, set[AType] patternTypes){
    desiredPatternTypes = { unset(s, "alabel")  | /AType s := patternTypes};
    desiredSubjectTypes = { unset(s, "alabel")  | /AType s := subjectType};
    desiredTypes = desiredPatternTypes + desiredSubjectTypes;
    
    //println("desiredTypes = <desiredTypes>");
    reachableFromDesired = reachableConcreteTypes[desiredTypes] + desiredTypes;
    
    //println("reachableFromDesired:"); iprintln(reachableFromDesired);
    
    prunedReachableConcreteTypes = reachableConcreteTypes;
    //if(avalue() notin desiredTypes){
    //    // if specific subject types are given, the reachability relation can be further pruned
    //    prunedReachableConcreteTypes = rangeR(reachableConcreteTypes, reachableFromDesired);
    //    println("removed from reachableConcreteTypes:"); for(x <- reachableConcreteTypes - prunedReachableConcreteTypes){println("\t<x>");}
    //}
    prunedReachableConcreteTypes += desiredTypes;
    set [AProduction] descend_into = {};
    
    // Find all productions that can lead to a desired type
    for(<AType sym, AType _tp> <- prunedReachableConcreteTypes /*tp in desiredTypes*/){
       //println("\<<sym>, <tp>\>");
       list[AType] syms = [sym];
       list[list[AType]] bindings = [];
       if(isParameterizedNonTerminalType(sym) && size(sym.parameters) > 0){
            nparams =  size(sym.parameters);
            adtName = sym.adtName;
            bindings = [ps | /aadt(adtName, ps, _) := current_grammar, size(ps) == nparams, all(p <- ps, aparameter(_,_) !:= p ) ];
       }
     
       alts = current_grammar.rules[sym];
       if(isEmpty(bindings)){
            descend_into += addAlts(alts);
       } else {
        for(list[AType] bds <- bindings){
            descend_into += addAlts(instantiate(sym.parameters, bds, alts));
        }
       }
    } 
    
    set [AProduction] descend_into1 = {};
    
    for(w <- descend_into){
      visit(w){
          case itr:\iter(AType s): {
               descend_into1 += regular(unset(itr, "alabel"));
               if(isAltOrSeq(s)) descend_into1 += regular(unset(s, "alabel"));
          }
          
          case itr:\iter-star(AType s):{
               descend_into1 += regular(unset(itr, "alabel"));
               if(isAltOrSeq(s)) descend_into1 += regular(unset(s, "alabel"));
          }
          
          case itr:\iter-seps(AType s,_):{
               descend_into1 += regular(unset(itr, "alabel"));
               if(isAltOrSeq(s)) descend_into1 += regular(unset(s, "alabel"));
          }
          
          case itr:\iter-star-seps(AType s,_):{
               descend_into1 += regular(unset(itr, "alabel"));
               if(isAltOrSeq(s)) descend_into1 += regular(unset(s, "alabel"));
          }
      }
      descend_into1 += w;
    }
    
    //println("descend_into (concrete) [<size(descend_into)>]: "); for(s <- descend_into) println("\t<s>"); 
    //println("descend_into1 (concrete) [<size(descend_into1)>]: "); for(s <- descend_into1) println("\t<s>");
    return <{}, descend_into + descend_into1>;
}

private bool isAltOrSeq(AType s) = alt(_) := s || seq(_) := s;    
