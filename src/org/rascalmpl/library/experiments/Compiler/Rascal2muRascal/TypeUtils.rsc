@bootstrapParser
module experiments::Compiler::Rascal2muRascal::TypeUtils

import Prelude;
import lang::rascal::\syntax::Rascal;
import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
import experiments::Compiler::Rascal2muRascal::RascalType;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::Rascal2muRascal::TypeReifier;
import experiments::Compiler::Rascal2muRascal::RascalModule;  // for getQualifiedFunctionName, need better structure

/*
 * This module provides a bridge to the "Configuration" delivered by the type checker
 * See declaration of Configuration in lang::rascal::types::CheckTypes.
 * It contains (type, scope, use, def) information collected by the type checker.
 * This module consists of three parts:
 * Part I:		defines the function is extractScopes that extracts information from a configuration
 * 				and transformations it in a representation that is suited for the compiler.
 * Part II: 	defines other functions to access this type information.
 * Part III:	Type-related code generation functions.
 * 
 * Some details:
 * - the typechecker generates a unique identifier (uid, an integer) for every entity it encounters and this uid
 *   is connected to all information about this entity.
 */
 
/********************************************************************/
/*     Part I: Extract and convert Type checker Configuration       */
/********************************************************************/

// A set of global values to represent the extracted information

public Configuration config = newConfiguration();

public map[int uid,tuple[str fuid,int pos] fuid2pos] uid2addr = ();	
													// map uids to qualified function names and positions
public map[loc \loc,int uid] loc2uid = ();			// map a source code location of an entity to its uid

public set[int] modules = {};
public set[int] functions = {};						// declared functions
public set[int] defaultFunctions = {};				// declared default functions
public set[int] constructors = {};					// declared constructors
public set[int] variables = {};						// declared variables
public set[int] keywordParameters = {};				// declared keyword parameters
public set[int] ofunctions = {};					// declared overloaded functions

public set[str] moduleNames = {};					// encountered module names


public map[int uid,str name] uid2name = (); 		// map uid to simple names, used to recursively compute qualified names
@doc{Counters for different scopes}
public map[int uid,map[str,int] name2n] cases = (); // number of functions with the same type within a scope
public map[int uid,int n] blocks = ();              // number of blocks within a scope
public map[int uid,int n] closures = ();            // number of closures within a scope
public map[int uid,int n] bscopes = ();             // number of boolean scopes within a scope

@doc{Handling nesting}
public rel[int,int] declares = {};
public rel[int,int] containment = {};


public map[int,str] fuid2str = ();					// map qualified names back to uids

public map[int,Symbol] fuid2type = ();				// We need to perform more precise overloading resolution than provided by the type checker

public map[str,int] overloadingResolver = ();		// map function name to overloading resolver
public lrel[str,set[int]] overloadedFunctions = [];	// list of overloaded functions

// Reset the above global variables, when compiling the next module.

public void resetScopeExtraction() {
	uid2addr = ();
	loc2uid = ();
	
	modules = {};
	functions = {};
	defaultFunctions = {};
	constructors = {};
	variables = {};
	keywordParameters = {};
	ofunctions = {};
	
	uid2name = ();
	
	cases = ();
	blocks = ();
	closures = ();
	bscopes = ();
	declares = {};
	containment = {};
	
	fuid2str = ();
	fuid2type = ();
	
	overloadingResolver = ();
	overloadedFunctions = [];
}

int getFormals(int fuid) = size(fuid2type[fuid].parameters) + 1;       // '+ 1' accounts for keyword arguments
int getFormals(loc l)    = size(fuid2type[loc2uid[l]].parameters) + 1; // '+ 1' accounts for keyword arguments

// Compute the scope size, excluding declared nested functions, closures and keyword parameters
int getScopeSize(str fuid) =  
    // r2mu translation of functions introduces variables in place of formal parameter patterns
    // and uses patterns to match these variables 
    { 
      // TODO: invertUnique is a proper choice; 
      //       the following is a workaround to the current handling of 'extend' by the type checker
      set[int] uids = invert(fuid2str)[fuid];
      assert size({ config.store[uid] | int uid <- uids }) == 1;
      size(fuid2type[getOneFrom(uids)].parameters); 
    }
    + size({ pos | int pos <- range(uid2addr)[fuid], pos != -1 })
    + 2 // '+ 2' accounts for keyword arguments and default values of keyword parameters 
    ;

// extractScopes: extract and convert type information from the Configuration delivered by the type checker.
						    
void extractScopes(){
   for(uid <- config.store){
      item = config.store[uid];
      switch(item){
        case function(rname,rtype,
                      keywordParams,_,
        			  inScope,_,_,src):      { 
        							         functions += {uid};
        							         declares += {<inScope, uid>}; 
                                             loc2uid[src] = uid;
                                             for(l <- config.uses[uid]) {
                                                 loc2uid[l] = uid;
                                             }
                                             // Fill in uid2name
                                             name = getFUID(getSimpleName(rname),rtype);
                                             if(cases[inScope]?) {
                                                 if(cases[inScope][name]?) {
                                                     cases[inScope][name] = cases[inScope][name] + 1;
                                                 } else {
                                                    cases[inScope] = cases[inScope] + (name:0);
                                                 }
                                             } else {
                                                cases[inScope] = (name:0);
                                             }
                                             name = getFUID(getSimpleName(rname),rtype,cases[inScope][name]);
                                             uid2name[uid] = name;
                                             // Fill in fuid2type to enable more precise overloading resolution
                                             fuid2type[uid] = rtype;
                                             // Check if the function is default
                                             //println(config.store[uid]);
                                             //println(config.functionModifiers[uid]);
                                             if(defaultModifier() in config.functionModifiers[uid]) {
                                             	defaultFunctions += {uid};
                                             }
                                           }
        case overload(_,_):                {
        								     ofunctions += {uid};
        								     for(l <- config.uses[uid]) {
        								     	loc2uid[l] = uid;
        								     } 
        								   }
        case variable(_,_,_,inScope,src):  { 
        									 variables += {uid};
        									 declares += {<inScope, uid>}; 
        									 loc2uid[src] = uid;
                                             for(l <- config.uses[uid]) {
                                                 loc2uid[l] = uid;
                                             }
                                           }
        case constructor(rname,rtype,_,
                         inScope,src):     { 
        									 constructors += {uid};
        									 declares += {<inScope, uid>};
        									 loc2uid[src] = uid;
        									 for(l <- config.uses[uid]) {
        									     loc2uid[l] = uid;
        									 }
        									 // Fill in uid2name
        								     uid2name[uid] = getCUID(getSimpleName(rname),rtype);
        								     // Fill in fuid2type to enable more precise overloading resolution
        								     fuid2type[uid] = rtype;
        								   }
        case production(rname,rtype,
                        inScope,src):      {
                                             if(!isEmpty(getSimpleName(rname))) {
                                             	constructors += {uid};
                                             	declares += {<inScope, uid>};
                                             	loc2uid[src] = uid;
                                             	for(l <- config.uses[uid]) {
                                                  loc2uid[l] = uid;
                                             	}
                                             	// Fill in uid2name
                                             	uid2name[uid] = getPUID(getSimpleName(rname),rtype);
                                             	// Fill in fuid2type to enable more precise overloading resolution
                                             	fuid2type[uid] = rtype;
                                             }
                                           }
        case blockScope(inScope,src):      { 
        								     containment += {<inScope, uid>};
        									 loc2uid[src] = uid;
        									 // Fill in uid2name
        									 if(blocks[inScope]?) {
        									  	blocks[inScope] = blocks[inScope] + 1;
        									 } else {
        									  	blocks[inScope] = 0;
        									 }
        									 uid2name[uid] = "blk#<blocks[inScope]>";
        								   }
        case booleanScope(inScope,src):    { 
        								     containment += {<inScope, uid>}; 
        									 loc2uid[src] = uid;
        									 // Fill in uid2name
        									 if(bscopes[inScope]?) {
        									    bscopes[inScope] = bscopes[inScope] + 1;
        									 } else {
        									    bscopes[inScope] = 0;
        									 }
        									 uid2name[uid] = "bscope#<bscopes[inScope]>";
        								   }
        case closure(rtype,keywordParams,
                       inScope,src):       {
                                             functions += {uid};
                                             declares += {<inScope, uid>};
        									 loc2uid[src] = uid;
        									 // Fill in uid2name
        									 if(closures[inScope]?) {
        									    closures[inScope] = closures[inScope] + 1;
        									 } else {
        									    closures[inScope] = 0;
        									 }
        									 uid2name[uid] = "closure#<closures[inScope]>";
        									 fuid2type[uid] = rtype;
        								   }
        case \module(RName rname, loc at):  {
        									 modules += uid;
        									 moduleNames += prettyPrintName(rname);
        									 // Fill in uid2name
        									 uid2name[uid] = prettyPrintName(rname);
        								   }
      }
    }
    
    containmentPlus = containment+;
    
    for(muid <- modules){
        module_name = uid2name[muid];
    	// First, fill in variables to get their positions right
    	// Sort variable declarations to ensure that formal parameters get first positions preserving their order 
    	topdecls = sort([ uid | uid <- declares[muid], variable(_,_,_,_,_) := config.store[uid] ]);
    	for(i <- index(topdecls)) {
            uid2addr[topdecls[i]] = <getFUID(uid2str(muid),"#<module_name>_init",Symbol::func(Symbol::\value(),[Symbol::\list(\value())]),0), i + 1>;
    	}
    	// Then, functions
    	topdecls = [ uid | uid <- declares[muid], function(_,_,_,_,_,_,_,_) := config.store[uid] ||
    											  closure(_,_,_,_)        := config.store[uid] ||
    											  constructor(_,_,_,_,_)  := config.store[uid] ||
    											( production(rname,_,_,_) := config.store[uid] 
    											    && !isEmpty(getSimpleName(rname)) )        ||
    											  variable(_,_,_,_,_)     := config.store[uid] ];
    	for(i <- index(topdecls)) {
    		// functions and closures are identified by their qualified names, and they do not have a position in their scope
    		// only the qualified name of their enclosing module or function is significant 
    		
    		mvname = (variable(rname,_,_,_,_) := config.store[topdecls[i]]) ? (":" + prettyPrintName(rname)) : "";
    		   
    		uid2addr[topdecls[i]] = <uid2str(muid) + mvname, -1>;
    	}
    }

	// Fill in mapping of function uids to qualified names (enables invert mapping)
	for(int uid <- functions + constructors) {
		fuid2str[uid] = uid2str(uid);
	}
	
    for(int fuid <- functions) {
    	nformals = getFormals(fuid); // ***Note: Includes keyword parameters as a single map parameter 
        innerScopes = {fuid} + containmentPlus[fuid];
        // First, fill in variables to get their positions right
        keywordParams = config.store[fuid].keywordParams;
        // Filter all the non-keyword variables within the function scope
        // ***Note: Filtering by name is possible only when shadowing of local variables is not permitted
        // Sort variable declarations to ensure that formal parameters get first positions preserving their order
        decls_non_kwp = sort([ uid | int uid <- declares[innerScopes], variable(name,_,_,_,_) := config.store[uid], name notin keywordParams ]);
        fuid_str = fuid2str[fuid];
        for(int i <- index(decls_non_kwp)) {
        	// Note: we need to reserve positions for variables that will replace formal parameter patterns
        	// '+ 1' is needed to allocate the first local variable to store default values of keyword parameters
        	uid2addr[decls_non_kwp[i]] = <fuid_str, i + nformals + 1>;
        }
        // Filter all the keyword variables (parameters) within the function scope
        decls_kwp = sort([ uid | int uid <- declares[innerScopes], variable(name,_,_,_,_) := config.store[uid], name in keywordParams ]);
        for(int i <- index(decls_kwp)) {
            keywordParameters += decls_kwp[i];
            uid2addr[decls_kwp[i]] = <fuid_str, -1>; // ***Note: keyword parameters do not have the position
        }
        // Then, functions
        decls = [ uid | uid <- declares[innerScopes], function(_,_,_,_,_,_,_,_) := config.store[uid] ||
        											  closure(_,_,_,_) := config.store[uid] ];
        for(i <- index(decls)) {
        	uid2addr[decls[i]] = <fuid2str[fuid], -1>;
        }
    }
    
    // Fill in uid2addr for overloaded functions;
    for(int fuid <- ofunctions) {
        set[int] funs = config.store[fuid].items;
    	if(int fuid <- funs, production(rname,_,_,_) := config.store[fuid] && isEmpty(getSimpleName(rname)))
    	    continue;
    	set[str] scopes = {};
    	str scopeIn = uid2str(0);
    	for(int fuid <- funs) {
    	    funScopeIn = uid2addr[fuid].fuid;
    		if(funScopeIn notin moduleNames) {
    			scopes += funScopeIn;
    		}
    	}
    	// The alternatives of the overloaded function may come from different scopes 
    	// but only in case of module scopes;
    	assert size(scopes) == 0 || size(scopes) == 1;
    	uid2addr[fuid] = <scopeIn,-1>;
    }
}

/********************************************************************/
/*     Part II: Retrieve type information                           */
/********************************************************************/

// Get the type of an expression as Symbol
Symbol getType(loc l) = config.locationTypes[l];

// Get the type of an expression as string
str getType(Tree e) = "<getType(e@\loc)>";

// Get the outermost type constructor of an expression as string
str getOuterType(Tree e) { 
	if(parameter(str _, Symbol bound) := getType(e@\loc)) {
		return "<getName(bound)>";
	}
	return "<getName(getType(e@\loc))>";
}

/* 
 * Get the type of a function.
 * Getting a function type by name is problematic in case of nested functions,
 * given that 'fcvEnv' does not contain nested functions;
 * Additionally, it does not allow getting types of functions that are part of an overloaded function;
 * Alternatively, the type of a function can be looked up by @loc;   
 */
Symbol getFunctionType(loc l) { 
   int uid = loc2uid[l];
   fun = config.store[uid];
   if(function(_,Symbol rtype,_,_,_,_,_,_) := fun) {
       return rtype;
   } else {
       throw "Looked up a function, but got: <fun> instead";
   }
}

Symbol getClosureType(loc l) {
   int uid = loc2uid[l];
   cls = config.store[uid];
   if(closure(Symbol rtype,_,_,_) := cls) {
       return rtype;
   } else {
       throw "Looked up a closure, but got: <cls> instead";
   }
}
					
KeywordParamMap getKeywords(loc location) = config.store[loc2uid[location]].keywordParams;

tuple[str fuid,int pos] getVariableScope(str name, loc l) {
  tuple[str fuid,int pos] addr = uid2addr[loc2uid[l]];
  return addr;
}

str getFUID(str fname, Symbol \type) { 
    //println("getFUID: <fname>, <\type>");
    return "<fname>(<for(p<-\type.parameters){><p>;<}>)";
}

str getFUID(str fname, Symbol \type, int case_num) = "<fname>(<for(p<-\type.parameters){><p>;<}>)#<case_num>";
str getFUID(str modName, str fname, Symbol \type, int case_num) = "<modName>/<fname>(<for(p<-\type.parameters){><p>;<}>)#<case_num>";

str getCUID(str cname, Symbol \type) = "<\type.\adt>::<cname>(<for(Symbol::label(l,t)<-\type.parameters){><t> <l>;<}>)";
str getCUID(str modName, str cname, Symbol \type) = "<modName>/<\type.\adt>::<cname>(<for(Symbol::label(l,t)<-\type.parameters){><t> <l>;<}>)";

str getPUID(str pname, Symbol \type) = "<\type.\sort>::<pname>(<for(Symbol::label(l,t)<-\type.parameters){><t> <l>;<}>)";
str getPUID(str modName, str pname, Symbol \type) = "<modName>/<\type.\sort>::<pname>(<for(Symbol::label(l,t)<-\type.parameters){><t> <l>;<}>)";

str uid2str(int uid) {
	if(!uid2name[uid]?) {
		throw "uid2str is not applicable!";
	}
	name = uid2name[uid];
	declaredIn = toMapUnique(invert(declares));
	containedIn = toMapUnique(invert(containment));
	if(containedIn[uid]?) {
		name = uid2str(containedIn[uid]) + "/" + name;
	} else if(declaredIn[uid]?) {
	    val = config.store[uid];
	    if( (function(_,_,_,_,inScope,_,_,src) := val || constructor(_,_,_,inScope,src) := val || production(_,_,inScope,src) := val ), 
	        \module(value _,loc at) := config.store[inScope]) {
        	if(at.path != src.path) {
        	    str path = replaceAll(src.path, ".rsc", "");
        	    path = replaceFirst(path, "/", "");
        	    if(src.authority != "") {
        	        path = substring(path, findFirst(path, "/") + 1);
        	        // Taking care of a special case 
        	        path = replaceFirst(path, "org/rascalmpl/library/", "");
        	    }
        	    name = replaceAll(path, "/", "::") + "/" + name;
        	    // println("QUALIFIED NAME IN CASE OF EXTEND: inScope: <at>; src: <src>; qname: <name>");
        	    return name;
			}
        }
		name = uid2str(declaredIn[uid]) + "/" + name;
	}
	return name;
}

public bool isDataType(AbstractValue::datatype(_,_,_,_)) = true;
public default bool isDataType(AbstractValue _) = false;

public bool isNonTerminalType(AbstractValue::sorttype(_,_,_,_)) = true;
public default bool isNonTerminalType(AbstractValue _) = false;

public bool isAlias(AbstractValue::\alias(_,_,_,_)) = true;
public default bool isAlias(AbstractValue a) = false;

public bool hasField(Symbol s, str fieldName){
    //println("hasField: <s>, <fieldName>");

    if(isADTType(s)){
       s2v = symbolToValue(s, config);
       //println("s2v = <s2v>");
    }
    // TODO: this is too liberal, restrict to outer type.
    visit(s){
       case label(fieldName, _):	return true;
    }
    return false;
}

public int getTupleFieldIndex(Symbol s, str fieldName) = 
    indexOf(getTupleFieldNames(s), fieldName);

public rel[str fuid,int pos] getAllVariablesAndFunctionsOfBlockScope(loc l) {
     containmentPlus = containment+;
     set[int] decls = {};
     if(int uid <- config.store, blockScope(int _, l) := config.store[uid]) {
         set[int] innerScopes = containmentPlus[uid];
         for(int inScope <- innerScopes) {
             decls = decls + declares[inScope];
         }
         return { addr | int decl <- decls, tuple[str fuid,int pos] addr := uid2addr[decl] };
     }
     throw "Block scope at <l> has not been found!";
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
	= muFun("<modName>/<fname>");

// Generate a MuExp to access a variable

MuExp mkVar(str name, loc l) {
  uid = loc2uid[l];
  tuple[str fuid,int pos] addr = uid2addr[uid];
  
  // Pass all the functions through the overloading resolution
  if(uid in functions || uid in constructors || uid in ofunctions) {
    // Get the function uids of an overloaded function
    set[int] ofuids = (uid in functions || uid in constructors) ? { uid } : config.store[uid].items;
    // Generate a unique name for an overloaded function resolved for this specific use
    str ofuid = uid2str(config.usedIn[l]) + "/use:" + name;
    
    bool exists = <addr.fuid,ofuids> in overloadedFunctions;
    int i = size(overloadedFunctions);
    if(!exists) {
    	overloadedFunctions += <addr.fuid,ofuids>;
    } else {
    	i = indexOf(overloadedFunctions, <addr.fuid,ofuids>);
    }   
    overloadingResolver[ofuid] = i;
  	return muOFun(ofuid);
  }
  
  // Keyword parameters
  if(uid in keywordParameters) {
      return muVarKwp(addr.fuid,name);
  }
  
  return muVar(name, addr.fuid, addr.pos);
}

// Generate a MuExp to reference a variable

MuExp mkVarRef(str name, loc l){
  <fuid, pos> = getVariableScope("<name>", l);
  return muVarRef("<name>", fuid, pos);
}

// Generate a MuExp for an assignment

MuExp mkAssign(str name, loc l, MuExp exp) {
  uid = loc2uid[l];
  tuple[str fuid, int pos] addr = uid2addr[uid];
  if(uid in keywordParameters) {
      return muAssignKwp(addr.fuid,name,exp);
  }
  return muAssign(name, addr.fuid, addr.pos, exp);
}

public list[MuFunction] lift(list[MuFunction] functions, str fromScope, str toScope, map[tuple[str,int],tuple[str,int]] mapping) {
    return [ (func.scopeIn == fromScope || func.scopeIn == toScope) 
	             ? { func.scopeIn = toScope; func.body = lift(func.body,fromScope,toScope,mapping); func; } 
	             : func | func <- getFunctionsInModule() ];
}
public MuExp lift(MuExp body, str fromScope, str toScope, map[tuple[str,int],tuple[str,int]] mapping) {
    return visit(body) {
	    case muAssign(str name,fromScope,int pos,MuExp exp)    => muAssign(name,toScope,newPos,exp) 
	                                                              when <fromScope,pos> in mapping && <_,int newPos> := mapping[<fromScope,pos>]
	    case muVar(str name,fromScope,int pos)                 => muVar(name,toScope,newPos)
	                                                              when <fromScope,pos> in mapping && <_,int newPos> := mapping[<fromScope,pos>]
	    case muVarRef(str name, fromScope,int pos)              => muVarRef(name,toScope,newPos)
	                                                              when <fromScope,pos> in mapping && <_,int newPos> := mapping[<fromScope,pos>]
        case muAssignVarDeref(str name,fromScope,int pos,
                                                  MuExp exp) => muAssignVarDeref(name,toScope,newPos,exp)
                                                                  when <fromScope,pos> in mapping && <_,int newPos> := mapping[<fromScope,pos>]
	    case muFun(str fuid,fromScope)                         => muFun(fuid,toScope)
	    case muCatch(str id,fromScope,Symbol \type,MuExp body) => muCatch(id,toScope,\type,body)
	}
}

// TODO: the following functions belong in ParseTree, but that gives "No definition for \"ParseTree/size(list(parameter(\\\"T\\\",value()));)#0\" in functionMap")

@doc{Determine the size of a concrete list}
int size(appl(regular(\iter(Symbol symbol)), list[Tree] args)) = size(args);
int size(appl(regular(\iter-star(Symbol symbol)), list[Tree] args)) = size(args);

int size(appl(regular(\iter-seps(Symbol symbol, list[Symbol] separators)), list[Tree] args)) = size_with_seps(size(args), size(separators));
int size(appl(regular(\iter-star-seps(Symbol symbol, list[Symbol] separators)), list[Tree] args)) = size_with_seps(size(args), size(separators));

int size(appl(prod(Symbol symbol, list[Symbol] symbols , attrs), list[Tree] args)) = 
	\label(str label, Symbol symbol1) := symbol && [Symbol itersym] := symbols
	? size(appl(prod(symbol1, symbols, attrs), args))
	: size(args[0]);

default int size(Tree t) {
    throw "Size of tree not defined for \"<t>\"";
}

private int size_with_seps(int len, int lenseps) = (len == 0) ? 0 : 1 + (len / (lenseps + 1));
