@bootstrapParser
module experiments::Compiler::Rascal2muRascal::TypeUtils

import IO;
import ValueIO;
import Set;
import Map;
import Node;
import Relation;
import String;
import util::Reflective;

import lang::rascal::\syntax::Rascal;
import experiments::Compiler::muRascal::AST;

import lang::rascal::grammar::definition::Symbols;

import lang::rascal::types::CheckerConfig;
import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;

import experiments::Compiler::Rascal2muRascal::TypeReifier;
import experiments::Compiler::Rascal2muRascal::TmpAndLabel;

//alias KeywordParamMap = map[RName kpName, Symbol kpType]; // TODO: duplicate of CheckerConfig!!!!

//import experiments::Compiler::Rascal2muRascal::RascalType;

/*
 * This module provides a bridge to the "Configuration" delivered by the type checker
 * See declaration of Configuration in lang::rascal::types::CheckTypes.
 * It contains (type, scope, use, def) information collected by the type checker.
 * This module consists of three parts:
 * Part I:		Defines the function extractScopes that extracts information from a Configuration
 * 				and transforms it into a representation that is suited for the compiler.
  *             Initializes the type reifier
 * Part II: 	Defines other functions to access this type information.
 * Part III:	Type-related code generation functions.
 * Part IV:		Translate Rascal types
 * 
 * Some details:
 * - the typechecker generates a unique identifier (uid, an integer) for every entity it encounters and this uid
 *   is connected to all information about this entity.
 */

// NOTE from JJV: this looks suspiciously like an M3 model, if you leave qualified name locs
// instead of ints everywhere and include one mapping from these to ints.
// We might move towards actually using M3 for compatibility's sake?
 
/********************************************************************/
/*     Part I: Extract and convert Type checker Configuration       */
/********************************************************************/

// A set of global values to represent the extracted information

private Configuration config;						// Config returned by the type checker

alias UID = int;                                    // A UID is a unique identifier in the type checker configuration
                                                    // with (integer) values in domain(config.store)

/*
 * We will use FUID (for Flexible UID) to create a readable string representation for 
 * any enity of interest. Typically a FUID consists of:
 * - the name of the entity
 * - its type
 * - optional modifiers (to indicate a specific use, case, etc)
 *
 * CUID, PUID, ... are variants of the above for constructors, productions, etc.
 */

alias FUID = str; 

private Configuration getConfiguration() { return config; }

public map[UID uid, tuple[FUID fuid, int pos] fuid2pos] uid2addr = ();	
													// map uids to FUIDs and positions
private map[loc \loc,int uid] loc2uid = ();			// map a source code location of an entity to its uid

public int getLoc2uid(loc l){
    if(loc2uid[l]?){
    	return loc2uid[l];
    }
    throw "getLoc2uid: <l>";
 	//l = normalize(l);
  //  //println("getLoc2uid: <l>");
  //  //iprintln(loc2uid);
  //  assert loc2uid[l]? : "getLoc2uid <l>";
  //  return loc2uid[l];
}

public loc normalize(loc l) {

    if(l.scheme == "std"){
      
  	   res = getSearchPathLocation(l.path);
  	   try {
  	   		res = res(l.offset, l.length, l.begin,l.end);
  	   } catch: ;
  	   
  	  println("**** normalize: <l> =\> <res>");
  	   return res;
    }
    println("**** normalize: unchanged: <l>");
    return l;
}

private set[UID] modules = {};

private set[UID] functions = {};						// declared functions

public bool isFunction(UID uid) = uid in functions;

private set[UID] defaultFunctions = {};				// declared default functions

public bool isDefaultFunction(UID uid) = uid in defaultFunctions;

private map[Symbol, UID] datatypes = ();            // declared datatypes

private set[UID] constructors = {};					// declared constructors

private map[Symbol, map[str, map[str,value]]] constructorConstantDefaultExpressions;
private map[Symbol, map[str,set[str]]] constructorFields;

public bool isConstructor(UID uid) = uid in constructors;
//public set[UID] getConstructors() = constructors;

public set[UID] variables = {};						// declared variables

private map[str,int] module_var_init_locals = ();	        // number of local variables in module variable initializations

int getModuleVarInitLocals(str mname) {
	assert module_var_init_locals[mname]? : "getModuleVarInitLocals <mname>";
	return module_var_init_locals[mname];
}
public set[UID] keywordParameters = {};				// declared keyword parameters
                                                    // common keyword fields declared on datatypes
public set[UID] ofunctions = {};					// declared overloaded functions

public set[UID] outerScopes= {};					// outermost scopes, i.e. scopes directly contained in the module scope;

public set[str] moduleNames = {};					// encountered module names

public map[UID uid,str name] uid2name = (); 		// map uid to simple names, used to recursively compute qualified names

@doc{Counters for different scopes}

private map[UID uid,int n] blocks = ();             // number of blocks within a scope
private map[UID uid,int n] closures = ();           // number of closures within a scope
private map[UID uid,int n] bool_scopes = ();        // number of boolean scopes within a scope
private map[UID uid,int n] sig_scopes = ();         // number of signature scopes within a scope
private map[loc, UID] blockScopes = ();				// map from location to blockscope.

@doc{Handling nesting}
private rel[UID,UID] declares = {};
private rel[UID,UID] containment = {};
private rel[UID,UID] containmentPlus = {};			// containment+

private map[UID,UID] declaredIn = ();				// inverse of declares
private rel[UID,UID] declaredInPlus = {};			// declaredIn+
private map[UID,UID] containedIn = ();				// inverse of containment
private rel[UID,UID] containedInPlus = {};			// inverse of containment+

private rel[UID, UID] containedOrDeclaredInPlus = {};

private set[UID] importedModuleScopes = {};
private map[tuple[list[UID], UID], list[UID]] accessibleFunctions = ();
private map[UID, set[UID]] accessibleScopes = ();

private map[tuple[UID,UID], bool] funInnerScopes = ();

alias OFUN = tuple[str name, Symbol funType, str fuid, list[UID] alts];		// An overloaded function and all its possible resolutions

public map[UID,str] uid2str = ();					// map uids to str

public map[UID,Symbol] uid2type = ();				// We need to perform more precise overloading resolution than provided by the type checker

private map[str,int] overloadingResolver = ();		// map function name to overloading resolver
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

public list[OFUN] getOverloadedFunctions() = overloadedFunctions;

public map[str,int] getOverloadingResolver() = overloadingResolver;

bool hasOverloadingResolver(FUID fuid) = overloadingResolver[fuid]?;

OFUN getOverloadedFunction(FUID fuid) {
	assert overloadingResolver[fuid]? : "No overloading resolver defined for <fuid>";
	resolver = overloadingResolver[fuid];
	//println("getOverloadedFunction(<fuid>) ==\> <overloadedFunctions[resolver]>");
	return overloadedFunctions[resolver];
}

// Reset the above global variables, when compiling the next module.

public void resetScopeExtraction() {
	uid2addr = ();
	loc2uid = ();
	
	modules = {};
	functions = {};
	defaultFunctions = {};
	datatypes = ();
	constructors = {};
	constructorConstantDefaultExpressions = ();
	constructorFields = ();
	variables = {};
	module_var_init_locals = ();
	keywordParameters = {};
	ofunctions = {};
	outerScopes = {};
	
	uid2name = ();
	
	blocks = ();
	closures = ();
	bool_scopes = ();
	sig_scopes = ();
	blockScopes = ();
	declares = {};
	containment = {};
	containmentPlus = {};
	
	declaredIn = ();
	declaredInPlus = {};
	containedIn = ();
	containedInPlus = {};
	importedModuleScopes = {};
	containedOrDeclaredInPlus = {};
	accessibleFunctions = ();
	accessibleScopes = ();
	
	funInnerScopes = ();
	
	uid2str = ();
	uid2type = ();
	
	overloadingResolver = ();
	overloadedFunctions = [];
}

int getFormals(UID fuid) = size(uid2type[fuid].parameters) + 1;       // '+ 1' accounts for keyword arguments
int getFormals(loc l)    = size(uid2type[loc2uid[l]].parameters) + 1; // '+ 1' accounts for keyword arguments

// Compute the scope size, excluding declared nested functions, closures and keyword parameters
int getScopeSize(str fuid) =  
    // r2mu translation of functions introduces variables in place of formal parameter patterns
    // and uses patterns to match these variables 
    { 
      // TODO: invertUnique is a proper choice; 
      //       the following is a workaround to the current handling of 'extend' by the type checker
      set[UID] uids = invert(uid2str)[fuid];
      //println("getScopeSize(<fuid>): <uids>");
      
      nparams = size(uid2type[getFirstFrom(uids)].parameters);
      if(size(uids) != 1){
        for(uid <- uids){
            if(size(uid2type[uid].parameters) != nparams){
              println("uids = <uids>");
               throw "getScopeSize: different arities for <fuid>";
            }
        }
      }
      
      //assert size({ config.store[uid] | UID uid <- uids }) == 1: "getScopeSize";
      nparams;
    }
    + size({ pos | int pos <- range(uid2addr)[fuid], pos != -1 })
    + 2 // '+ 2' accounts for keyword arguments and default values of keyword parameters 
    ;

// extractScopes: extract and convert type information from the Configuration delivered by the type checker.
						    
void extractScopes(Configuration c){
	// Inspect all items in config.store and construct the sets
	// - modules, modulesNames
	// - functions, ofunctions
	// - constructors
	// - datatypes
	// - variables
	
	// the relations 
	// - declares
	// - containment
	
	// and the mappings:
	// - uid2name
	// - uid2addr
	// - loc2uid
	// - uid2type
	// - uid2str

   config = c;

   for(uid <- sort(toList(domain(config.store)))){
      item = config.store[uid];
      //println("<uid>: <item>");
      switch(item){
        case function(rname,rtype,keywordParams,_,inScope,_,_,src): { 
         	 //println("<uid>: <item>, scope: <inScope>");
	         functions += {uid};
	         declares += {<inScope, uid>}; 
             loc2uid[src] = uid;
             for(l <- config.uses[uid]) {
                 loc2uid[l] = uid;
             }
             //println("loc2uid: <src> : <loc2uid[src]>");
             // Fill in uid2name
             
             fname = getSimpleName(rname);
             suffix = fname == "main" || endsWith(fname, "_init") || endsWith(fname, "testsuite") ? 0 : src.begin.line;
  
             uid2name[uid] = getFUID(getSimpleName(rname),rtype,suffix);;
        	 
             // Fill in uid2type to enable more precise overloading resolution
             uid2type[uid] = rtype;
             // Check if the function is default
             //println(config.store[uid]);
             //println(config.functionModifiers[uid]);
             if(defaultModifier() in config.functionModifiers[uid]) {
             	defaultFunctions += {uid};
             }
        }
        case overload(_,_): {
             //println("<uid>: <item>");
		     ofunctions += {uid};
		     for(l <- config.uses[uid]) {
		     	//println("loc2uid already defined=<loc2uid[l]?>,  add loc2uid[<l>] = <uid>");
		     	loc2uid[l] = uid;
		     } 
    	}
        case variable(_,_,_,inScope,src):  { 
        	 //println("<uid>: <item>");
			 variables += {uid};
			 declares += {<inScope, uid>};
			 loc2uid[src] = uid;
             for(l <- config.uses[uid]) {
                 loc2uid[l] = uid;
             }
             //for(l <- loc2uid){
            	// if(/Exception/ !:= "<l>")
            	//	println("<l> : <loc2uid[l]>");
             //}	
        }
        case constructor(rname,rtype,_,inScope,src): { 
             //println("<uid>: <item>");
			 constructors += {uid};
			 declares += {<inScope, uid>};
			 loc2uid[src] = uid;
			 for(l <- config.uses[uid]) {
			     loc2uid[l] = uid;
			 }
			 // Fill in uid2name
		     uid2name[uid] = getCUID(getSimpleName(rname),rtype);
		     // Fill in uid2type to enable more precise overloading resolution
		     uid2type[uid] = rtype;
        }
        
        case datatype(RName name, Symbol rtype, KeywordParamMap keywordParams, int containedIn, set[loc] ats): {
            //println("<uid>: <item>");
            datatypes[rtype] = uid;
        }
        case production(rname, rtype, inScope, p, src): {
             //println("<uid>: <item>");
             if(!isEmpty(getSimpleName(rname))) {
             	constructors += {uid};
             	declares += {<inScope, uid>};
             	loc2uid[src] = uid;
             	for(l <- config.uses[uid]) {
                  loc2uid[l] = uid;
             	}
             	// Fill in uid2name
             	uid2name[uid] = getPUID(getSimpleName(rname),rtype);
             	// Fill in uid2type to enable more precise overloading resolution
             	uid2type[uid] = rtype;
             }
        }
        case blockScope(inScope,src): { 
             //println("<uid>: <item>");
		     containment += {<inScope, uid>};
			 loc2uid[src] = uid;
			 // Fill in uid2name
			 if(blocks[inScope]?) {
			  	blocks[inScope] = blocks[inScope] + 1;
			 } else {
			  	blocks[inScope] = 0;
			 }
			 uid2name[uid] = "blk#<blocks[inScope]>";
			 if(inScope == 0){
			 	outerScopes += uid;
			 }
			 blockScopes[src] = uid;
        }
        case booleanScope(inScope,src): { 
		     containment += {<inScope, uid>}; 
			 loc2uid[src] = uid;
			 // Fill in uid2name
			 if(bool_scopes[inScope]?) {
			    bool_scopes[inScope] = bool_scopes[inScope] + 1;
			 } else {
			    bool_scopes[inScope] = 0;
			 }
			 uid2name[uid] = "bool_scope#<bool_scopes[inScope]>";
			 if(inScope == 0){
			 	outerScopes += uid;
			 }
        }
        case signatureScope(inScope,src): {
             //println("<uid>: <item>");
             containment += {<loc2uid[src], uid>};  //redirect to the actual declaration
             // Fill in uid2name
             if(sig_scopes[inScope]?) {
                sig_scopes[inScope] = sig_scopes[inScope] + 1;
             } else {
                sig_scopes[inScope] = 0;
             }
            uid2name[uid] = "sig_scope#<sig_scopes[inScope]>";
             if(inScope == 0){
                outerScopes += uid;
             }
        }
        case closure(rtype,keywordParams,inScope,src): {
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
			 uid2type[uid] = rtype;
        }
        case \module(RName rname, loc at):  {
			 modules += uid;
			 moduleNames += prettyPrintName(rname);
			 // Fill in uid2name
			 uid2name[uid] = prettyPrintName(rname);
        }
        default: ; //println("extractScopes: skipping <uid>: <item>");
      }
    }
    
    // Make sure that the original and the normalized location is present.
    //for(l <- config.locationTypes){
    //	config.locationTypes[l] = config.locationTypes[l];
    //}
    
    // Precompute some derived values for efficiency:
    containmentPlus = containment+;
    containedInPlus = invert(containmentPlus);
    declaredIn = toMapUnique(invert(declares));
    declaredInPlus = invert(declares)+;
	containedIn = toMapUnique(invert(containment));
	
	//println("containment: <containment>");
	//println("containmentPlus: <containmentPlus>");
	//println("declares: <declares>");
	
	containedOrDeclaredInPlus = (invert(declares) + invert(containment))+;
	
	//println("containedOrDeclaredInPlus: <containedOrDeclaredInPlus>");
	importedModuleScopes = range(config.modEnv);
    
    for(muid <- modules){
        module_name = uid2name[muid];
        nmodule_var_init_locals = 0;
    	// First, fill in variables to get their positions right
    	// Sort variable declarations to ensure that formal parameters get first positions preserving their order 
    	topdecls = sort([ uid | uid <- declares[muid], variable(_,_,_,_,_) := config.store[uid] ]);
    	
    	//println("topdecls:");
    	//for(td <- topdecls){ println(td); }
    	
 		fuid_module_init = getFUID(convert2fuid(muid),"#<module_name>_init",Symbol::func(Symbol::\value(),[Symbol::\list(Symbol::\value())]),0);
 		
    	for(i <- index(topdecls)) {
    		// Assign a position to module variables
            uid2addr[topdecls[i]] = <fuid_module_init, i + 1>;
            // Assign local positions to variables occurring in module variable initializations
            for(os <- outerScopes){
                //println("os = <os>, <config.store[os].at>, <config.store[topdecls[i]].at>, <config.store[os].at < config.store[topdecls[i]].at>");
                
            	if(config.store[os].at < config.store[topdecls[i]].at){
            		decls_inner_vars = sort([ uid | UID uid <- declares[os], variable(RName name,_,_,_,_) := config.store[uid] ]);
            		//println("decls_inner_vars: <decls_inner_vars>");
    			    for(int j <- index(decls_inner_vars)) {
        			    uid2addr[decls_inner_vars[j]] = <fuid_module_init, 2 + nmodule_var_init_locals>;
        			    //println("add1: uid2ddr[<decls_inner_vars[j]> = <uid2addr[decls_inner_vars[j]]>");
        			    nmodule_var_init_locals += 1;
        		    }
            	}
            }
    	}
    	module_var_init_locals[module_name] = nmodule_var_init_locals;
    	
    	// Then, functions
    	
    	topdecls = [ uid | uid <- declares[muid], 
    	                      function(_,_,_,_,_,_,_,_) := config.store[uid] 
    	                   || closure(_,_,_,_)          := config.store[uid] 
    	                   || constructor(_,_,_,_,_)    := config.store[uid] 
    	                   || ( production(rname,_,_,_,_) := config.store[uid] && !isEmpty(getSimpleName(rname)) ) 
    	                   || variable(_,_,_,_,_)       := config.store[uid] 
    	           ];
    	for(i <- index(topdecls)) {
    		// functions and closures are identified by their qualified names, and they do not have a position in their scope
    		// only the qualified name of their enclosing module or function is significant 
    		
    		mvname = (variable(rname,_,_,_,_) := config.store[topdecls[i]]) ? (":" + prettyPrintName(rname)) : "";
    		uid2addr[topdecls[i]] = <convert2fuid(muid) + mvname, -1>;
    		//println("add2 uid2addr[<topdecls[i]>] = <uid2addr[topdecls[i]]>");
    	}
    }

	// Fill in mapping of function uids to qualified names (enables invert mapping)
	for(UID uid <- functions + constructors) {
		if(!uid2str[uid]?){
			uid2str[uid] = convert2fuid(uid);
		} else {
			throw "extractScopes: Duplicate entry in uid2str for <uid>, <convert2fuid(uid)>";
		}	
	}
	
	//println("constructors: <constructors>");
	//
	//for(cns <- constructors){
	//   println("constructor: <cns>, keyword parameters: <config.store[cns].keywordParams>");
	//
	//}
	
    for(UID fuid1 <- functions) {
    	nformals = getFormals(fuid1); // ***Note: Includes keyword parameters as a single map parameter 
        innerScopes = {fuid1} + containmentPlus[fuid1];
        // First, fill in variables to get their positions right
        keywordParams = config.store[fuid1].keywordParams;
        
        // Filter all the non-keyword variables within the function scope
        // ***Note: Filtering by name is possible only when shadowing of local variables is not permitted
        // Sort variable declarations to ensure that formal parameters get first positions preserving their order
        decls_non_kwp = sort([ uid | UID uid <- declares[innerScopes], variable(RName name,_,_,_,_) := config.store[uid], name notin keywordParams ]);
        
        fuid_str = uid2str[fuid1];
        for(int i <- index(decls_non_kwp)) {
        	// Note: we need to reserve positions for variables that will replace formal parameter patterns
        	// '+ 1' is needed to allocate the first local variable to store default values of keyword parameters
        	uid2addr[decls_non_kwp[i]] = <fuid_str, i + nformals + 1>;
        }
        // Filter all the keyword variables (parameters) within the function scope
        decls_kwp = sort([ uid | UID uid <- declares[innerScopes], variable(RName name,_,_,_,_) := config.store[uid], name in keywordParams ]);
       
        for(int i <- index(decls_kwp)) {
            keywordParameters += decls_kwp[i];
            uid2addr[decls_kwp[i]] = <fuid_str, -1>; // ***Note: keyword parameters do not have a position
        }
        // Then, functions
        decls = [ uid | uid <- declares[innerScopes], function(_,_,_,_,_,_,_,_) := config.store[uid] ||
        											  closure(_,_,_,_) := config.store[uid]
        											   ];
        for(i <- index(decls)) {
            uid2addr[decls[i]] = <uid2str[fuid1], -1>;
        }
    }
    
    for(UID fuid1 <- constructors){
        nformals = getFormals(fuid1); // ***Note: Includes keyword parameters as a single map parameter 
        // First, fill in variables to get their positions right
        //println("fuid1 = <fuid1>");
        if(config.store[fuid1] has keywordParams){
            keywordParams = config.store[fuid1].keywordParams;
           
            its_adt = config.store[fuid1].rtype.\adt;
            uid_adt = datatypes[its_adt];
            dataKeywordParams = config.dataKeywordDefaults[uid_adt];
            
            if(size(keywordParams) > 0 || size(dataKeywordParams) > 0){
                //println("*** <keywordParams>");
                //println("*** <dataKeywordParams>");
                
                innerScopes = {fuid1} + containmentPlus[fuid1];
                
                // There may be default expressions with variables, so introduce variable addresses inside the companion function
                // println("fuid1 = <fuid1>, nformals = <nformals>, innerScopes = <innerScopes>, keywordParams = <keywordParams>");
                // Filter all the non-keyword variables within the function scope
                // ***Note: Filtering by name is possible only when shadowing of local variables is not permitted
                // Sort variable declarations to ensure that formal parameters get first positions preserving their order
                decls_non_kwp = sort([ uid | UID uid <- declares[innerScopes], variable(RName name,_,_,_,_) := config.store[uid], name notin keywordParams ]);
                
                fuid_str = getCompanionDefaultsForUID(fuid1);
                //println("fuid_str = <fuid_str>, decls_non_kwp = <decls_non_kwp>, declared[innerSopes] = <declares[innerScopes]>");
                for(int i <- index(decls_non_kwp)) {
                    // Note: we need to reserve positions for variables that will replace formal parameter patterns
                    // '+ 1' is needed to allocate the first local variable to store default values of keyword parameters
                    uid2addr[decls_non_kwp[i]] = <fuid_str, i>;
                }
                // Filter all the keyword variables (parameters) within the function scope
                //println("keywordParams = <keywordParams>");
                //println("domain(dataKeywordParams): <domain(dataKeywordParams)>");
                //println("declares[innerScopes]: <declares[innerScopes]>");
                //println("keywordParams + domain(dataKeywordParams): <keywordParams + domain(dataKeywordParams)>");
                decls_kwp = sort([ uid | UID uid <- declares[innerScopes], variable(RName name,_,_,_,_) := config.store[uid], name in domain(keywordParams) + domain(dataKeywordParams) ]);
                //println("^^^ adding <decls_kwp>");
                for(int i <- index(decls_kwp)) {
                    keywordParameters += decls_kwp[i];
                    uid2addr[decls_kwp[i]] = <fuid_str, -1>; // ***Note: keyword parameters do not have a position
                }
            }
        }

    }
    
    // Fill in uid2addr for overloaded functions;
    for(UID fuid2 <- ofunctions) {
        set[UID] funs = config.store[fuid2].items;
    	if(UID fuid3 <- funs, production(rname,_,_,_,_) := config.store[fuid3] && isEmpty(getSimpleName(rname)))
    	    continue;
    	 if(UID fuid4 <- funs,   annotation(_,_,_,_,_) := config.store[fuid4])
    	 continue; 
    	    
    	set[str] scopes = {};
    	str scopeIn = convert2fuid(0);
    	for(UID fuid5 <- funs) {
    		//println("<fuid5>: <config.store[fuid5]>");
    	    funScopeIn = uid2addr[fuid5].fuid;
    		if(funScopeIn notin moduleNames) {
    			scopes += funScopeIn;
    		}
    	}
    	// The alternatives of the overloaded function may come from different scopes 
    	// but only in case of module scopes;
    	//assert size(scopes) == 0 || size(scopes) == 1 : "extractScopes";
    	uid2addr[fuid2] = <scopeIn,-1>;
    }
     extractConstantDefaultExpressions();
}

set[str] getAllFields(UID cuid){
    a_constructor = config.store[cuid];
    //println("getAllFields(<cuid>): <a_constructor>");
    set[str] result = {};
    if(a_constructor is constructor){
        its_adt = a_constructor.rtype.\adt;
        uid_adt = datatypes[its_adt];
        dataKeywordParams = config.dataKeywordDefaults[uid_adt];
        result = { prettyPrintName(field) | field <- domain(dataKeywordParams)} +
                 { prettyPrintName(field) | field <- domain(a_constructor.keywordParams) } +
                 { fieldName | field <- a_constructor.rtype.parameters, label(fieldName, _) := field };
    }
    //println("getAllFields(<cuid>) =\> <result>");
    return result;
}

set[str] getAllKeywordFields(UID cuid){
    a_constructor = config.store[cuid];
    //println("getAllKeywordFields(<cuid>): <a_constructor>");
    set[str] result = {};
    if(a_constructor is constructor){
        its_adt = a_constructor.rtype.\adt;
        uid_adt = datatypes[its_adt];
        dataKeywordParams = config.dataKeywordDefaults[uid_adt];
        result = { prettyPrintName(field) | field <- domain(dataKeywordParams)} +
                 { prettyPrintName(field) | field <- domain(a_constructor.keywordParams) };
    }
    //println("getAllKeywordFields(<cuid>) =\> <result>");
    return result;
} 
 

map[RName,Symbol] getAllKeywordFieldsAndTypes(UID cuid){
    a_constructor = config.store[cuid];
    //println("getAllKeywordFieldsAndTypes(<cuid>): <a_constructor>");
    map[RName,Symbol] result = ();
    if(a_constructor is constructor){
        its_adt = config.store[cuid].rtype.\adt;
        uid_adt = datatypes[its_adt];
        dt = config.store[uid_adt];
        result = dt.keywordParams + a_constructor.keywordParams;
    }
    //println("getAllKeywordFieldsAndTypes(<cuid>) =\> <result>");
    return result;
}

lrel[RName,value] getAllKeywordFieldDefaults(UID cuid){
    a_constructor = config.store[cuid];
    //println("getAllKeywordDefaults(<cuid>): <a_constructor>");
    result = [];
    if(a_constructor is constructor){
        its_adt = config.store[cuid].rtype.\adt;
        uid_adt = datatypes[its_adt];
        result = toList(config.dataKeywordDefaults[uid_adt] +  config.dataKeywordDefaults[cuid]);
        result = sort(result, bool(tuple[RName,value] a, tuple[RName,value] b) { return Expression aExp := a[1] && Expression bExp := b[1] && aExp@\loc.offset < bExp@\loc.offset; });
    }
    //println("getAllKeywordDefaults(<cuid>) =\> <result>");
    return result;
}  

// extractConstantDefaultExpressions:
// For every ADT, for every constructor, find the default fields with constant default expression
// Note: the notion of "constant" is weaker than used in other parts of the compiler and is here equated to "literal"
//       as a consequence, e.g. "abc" + "def" will not be classified as constant.

void extractConstantDefaultExpressions(){

     // TODO: the following hacks are needed to convince the interpreter of the correct type.
     constructorConstantDefaultExpressions = (adt("XXX", []) : ("xxxc1" : ("xxxf1": true, "xxxf2" : 0)));
     constructorFields = (adt("XXX", []) : ("xxxc1" : {"xxxa", "xxxb"}));
     for(cuid <- constructors){
        a_constructor = config.store[cuid];
       
        consName = prettyPrintName(a_constructor.name);
        if(a_constructor is constructor){
           fieldSet = getAllFields(cuid);
           if(constructorFields[a_constructor.rtype.\adt]?){
              constructorFields[a_constructor.rtype.\adt] += (consName : fieldSet);                 // TODO: Using X ? () += Y gives type error in interpreter
           } else {
              constructorFields[a_constructor.rtype.\adt] =  (consName : fieldSet);
           }
        } else if (a_constructor is production){
            pr = a_constructor.p;
            fieldSet = { fieldName | field <- pr.symbols, label(fieldName, _) := field };
            if(constructorFields[a_constructor.rtype]?){
               constructorFields[a_constructor.rtype] +=(consName : fieldSet);
            } else {
               constructorFields[a_constructor.rtype] = (consName : fieldSet);
            }
        }  
     }
     for(tp <- config.dataKeywordDefaults){
        uid = tp[0];
        dt = config.store[uid];
        if(dt is datatype){
           the_adt = dt.rtype;
           kwParamMap = dt.keywordParams;
           if(kwParamMap != ()){
               fieldsForAdt = constructorFields[the_adt];
               //println("fieldsForAdt: <fieldsForAdt>");
               kwNames = {prettyPrintName(kwn) | kwn <- domain(kwParamMap)};
               //println("domain(kwParamMap): <kwNames>");
               constructorFields[the_adt] = (c : fieldsForAdt[c] + kwNames | c <- fieldsForAdt);
           }
        }
     }
     
     //println("&&& constructorFields:");
     //iprintln(constructorFields);
     
    
     for(tp <- config.dataKeywordDefaults){
         uid = tp[0];
         the_constructor = config.store[uid];   // either constructor or datatype
         //println("the_constructor: <the_constructor>");
         //println("the_constructor.rtype: <the_constructor.rtype>");
        if(!(the_constructor is datatype)){
             
             Symbol the_adt = (the_constructor.rtype has adt) ? the_constructor.rtype.\adt : the_constructor.rtype;
             //println("the_adt = <the_adt>");
             str the_cons = the_constructor.rtype.name;
             str fieldName = prettyPrintName(tp[1]);
             
             map[str, set[str]] adtFieldMap = constructorFields[the_adt] ? ();
             set[str] fieldSet = adtFieldMap[the_cons] ? {};
             adtFieldMap[the_cons] = fieldSet + fieldName;
             constructorFields += (the_adt : adtFieldMap);
             
             //println("added: <the_adt>, <adtFieldMap>");
             
             defaultVal = tp[2];
             if(Expression defaultExpr := defaultVal &&  defaultExpr is literal){
                try {
                   constValue = getConstantValue(defaultExpr.literal);
                   map[str, map[str,value]] adtMap = constructorConstantDefaultExpressions[the_adt] ? ();
                   map[str,value] consMap = adtMap[the_cons] ? ();
                   
                   consMap[fieldName] = constValue;
                   adtMap[the_cons] = consMap;
                   constructorConstantDefaultExpressions += (the_adt : adtMap);
                   
                } catch:
                    ;// ok, non-constant
             } 
         }
    }
    ////println("constructorConstantDefaultExpressions");
    //println(constructorConstantDefaultExpressions);
  
}

// Identify constant expressions and compute their value

value getConstantValue((Literal) `<BooleanLiteral b>`) = 
    "<b>" == "true" ? true : false;

// -- integer literal  -----------------------------------------------
 
value getConstantValue((Literal) `<IntegerLiteral n>`) = 
    toInt("<n>");

// -- string literal  ------------------------------------------------
    
value getConstantValue((StringLiteral)`<StringConstant constant>`) =
    readTextValueString("<constant>");

value getConstantValue((Literal) `<LocationLiteral src>`) = 
    readTextValueString("<src>");

default value getConstantValue((Literal) `<Literal s>`) = 
    readTextValueString("<s>");
    
default value getConstantValue(Expression e) {
    throw "Not constant";
}

int declareGeneratedFunction(str name, str fuid, Symbol rtype, loc src){
	//println("declareGeneratedFunction: <name>, <rtype>, <src>");
    uid = config.nextLoc;
    config.nextLoc = config.nextLoc + 1;
    // TODO: all are placed in scope 0, is that ok?
    config.store[uid] = function(RSimpleName(name), rtype, (), false, 0, [], false, src);
    functions += {uid};
    //declares += {<inScope, uid>}; TODO: do we need this?
     
    // Fill in uid2name
    uid2name[uid] = fuid;
    //////loc2uid[normalize(src)] = uid;
    loc2uid[src] = uid;
    // Fill in uid2type to enable more precise overloading resolution
    uid2type[uid] = rtype;
    if(!uid2str[uid]?){
    	uid2str[uid] = fuid;
    } else {
    	throw "declareGeneratedFunction: duplicate entry in uid2str for <uid>, <fuid>";
    }
    return uid;
}

/********************************************************************/
/*     Part II: Retrieve type information                           */
/********************************************************************/

// Get the type of an expression as Symbol
Symbol getType(loc l) {
   
    if(config.locationTypes[l]?){
    	return config.locationTypes[l];
    }
    //////l = normalize(l);
    //iprintln(config.locationTypes);
    assert config.locationTypes[l]? : "getType for <l>";
	//println("getType(<l>) = <config.locationTypes[l]>");
	return config.locationTypes[l];
}	

// Get the type of an expression as string
str getType(Tree e) = "<getType(e@\loc)>";

// Get the outermost type constructor of an expression as string
str getOuterType(Tree e) { 
    tp = getType(e@\loc);
	if(parameter(str _, Symbol bound) := tp) {
		return "<getName(bound)>";
	}
	if(label(_, Symbol sym) := tp){
	   return "<getName(sym)>";
	}
	if(\start(Symbol sym) := tp || sort(_) := tp){
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
Symbol getFunctionType(loc l) {  
   UID uid = getLoc2uid(l);
   fun = config.store[uid];
   if(function(_,Symbol rtype,_,_,_,_,_,_) := fun) {
       return rtype;
   } else {
       throw "Looked up a function, but got: <fun> instead";
   }
}

Symbol getClosureType(loc l) {
   UID uid = getLoc2uid(l);
   cls = config.store[uid];
   if(closure(Symbol rtype,_,_,_) := cls) {
       return rtype;
   } else {
       throw "Looked up a closure, but got: <cls> instead";
   }
}

AbstractValue getAbstractValueForQualifiedName(QualifiedName name){
	rn = convertName(name);
	// look up the name in the type environment
	return config.store[config.typeEnv[rn]];
}
					
KeywordParamMap getKeywords(loc location) = config.store[getLoc2uid(location)].keywordParams;

map[str, map[str, value]] getConstantConstructorDefaultExpressions(loc location){
    tp = getType(location);
    return constructorConstantDefaultExpressions[tp] ? ();
}

map[str, set[str]] getAllConstructorFields(loc location){
    tp = getType(location);
    //println("getAllConstructorFields: <tp>, <constructorFields[tp]>");
    return constructorFields[tp] ? ();
}

tuple[str fuid,int pos] getVariableScope(str name, loc l) {
  //println("getVariableScope: <name>, <l>)");
  //for(l1 <- loc2uid){
  //          	if(/Exception/ !:= "<l1>")
  //          		println("<l1> : <loc2uid[l1]>");
  //          	if(l1 == l) println("EQUAL");
  //          }
  //println(	loc2uid[l] );
  uid = getLoc2uid(l);
  //println(uid2addr);
  tuple[str fuid,int pos] addr = uid2addr[uid];
  return addr;
}

// Create unique symbolic names for functions, constructors and productions

str getFUID(str fname, Symbol \type) { 
    res = "<fname>(<for(p<-\type.parameters){><p>;<}>)";
    //println("getFUID: <fname>, <\type> =\> <res>");
    return res;
}

str getField(Symbol::label(l, t)) = "<t> <l>";
default str getField(Symbol t) = "<t>";

str getFUID(str fname, Symbol \type, int case_num) =
  "<fname>(<for(p<-\type.parameters){><p>;<}>)#<case_num>";
  	
str getFUID(str modName, str fname, Symbol \type, int case_num) = 
	"<modName>/<fname>(<for(p<-\type.parameters){><p>;<}>)#<case_num>";

// NOTE: was "<\type.\adt>::<cname>(<for(label(l,t)<-tparams){><t> <l>;<}>)"; but that did not cater for unlabeled fields
str getCUID(str cname, Symbol \type) = "<\type.\adt>::<cname>(<for(p<-\type.parameters){><getField(p)>;<}>)";
str getCUID(str modName, str cname, Symbol \type) = "<modName>/<\type.\adt>::<cname>(<for(p <-\type.parameters){><getField(p)>;<}>)";

str getPUID(str pname, Symbol \type) = "<\type.\sort>::<pname>(<for(p <-\type.parameters){><getField(p)>;<}>)";
str getPUID(str modName, str pname, Symbol \type) = "<modName>/<\type.\sort>::<pname>(<for(p <-\type.parameters){><getField(p)>;<}>)";


@doc{Generates a unique scope id: non-empty 'funNames' list implies a nested function}
/*
 * NOTE: Given that the muRascal language does not support overloading, the dependency of function uids 
 *       on the number of formal parameters has been removed 
 */
str getUID(str modName, lrel[str,int] funNames, str funName, int nformals) {
	// Due to the current semantics of the implode
	modName = replaceAll(modName, "::", "");
	return "<modName>/<for(<f,n> <- funNames){><f>(<n>)/<}><funName>"; 
}
str getUID(str modName, [ *tuple[str,int] funNames, <str funName, int nformals> ]) 
	= "<modName>/<for(<f,n> <- funNames){><f>(<n>)/<}><funName>";


str getCompanionForUID(UID uid) = uid2str[uid] + "::companion";

str getCompanionDefaultsForUID(UID uid) = uid2str[uid] + "::companion-defaults";


str getCompanionDefaultsForADTandField(str ADTName, str fld) {
    return "<ADTName>::<fld>-companion-default";
}


str qualifiedNameToPath(QualifiedName qname){
    str path = replaceAll("<qname>", "::", "/");
    return replaceAll(path, "\\","");
}

str convert2fuid(UID uid) {
	if(!uid2name[uid]?) {
		throw "uid2str is not applicable for <uid>!";
	}
	str name = uid2name[uid];
	
	if(containedIn[uid]?) {
		name = convert2fuid(containedIn[uid]) + "/" + name;
	} else if(declaredIn[uid]?) {
	    val = config.store[uid];
	    if( (function(_,_,_,_,inScope,_,_,src) := val || 
	         constructor(_,_,_,inScope,src) := val || 
	         production(_,_,inScope,_,src) := val ), 
	        \module(RName _,loc at) := config.store[inScope]) {
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
        	    //println("convert2fuid(<uid>) =\> <name>");
        	    return name;
			}
        }
		name = convert2fuid(declaredIn[uid]) + "/" + name;
	}
	//println("convert2fuid(<uid>) =\> <name>");
	return name;
}

public MuExp getConstructor(str cons) {
   cons = unescape(cons);
   uid = -1;
   for(c <- constructors){
     //println("c = <c>, uid2name = <uid2name[c]>, uid2str = <convert2fuid(c)>");
     if(cons == getSimpleName(getConfiguration().store[c].name)){
        //println("c = <c>, <config.store[c]>,  <uid2addr[c]>");
        uid = c;
        break;
     }
   }
   if(uid < 0)
      throw("No definition for constructor: <cons>");
   return muConstr(convert2fuid(uid));
}

public bool isDataType(AbstractValue::datatype(_,_,_,_,_)) = true;
public default bool isDataType(AbstractValue _) = false;

public bool isNonTerminalType(sorttype(_,_,_,_)) = true;
public default bool isNonTerminalType(AbstractValue _) = false;

public bool isAlias(AbstractValue::\alias(_,_,_,_)) = true;
public default bool isAlias(AbstractValue a) = false;

//public bool hasField(Symbol s, str fieldName){
//    //println("hasField: <s>, <fieldName>");
//
//    //if(isADTType(s)){
//    //   s2v = symbolToValue(s /*, config*/);
//    //   println("s2v = <s2v>");
//    //}
//    s1 = symbolToValue(s);
//    // TODO: this is too liberal, restrict to outer type.
//    visit(s1){
//       case label(fieldName2, _):	if(unescape(fieldName2) == fieldName) return true;
//    }
//    return false;
//}

public int getTupleFieldIndex(Symbol s, str fieldName) = 
    indexOf(getTupleFieldNames(s), fieldName);

public rel[str fuid,int pos] getAllVariablesAndFunctionsOfBlockScope(loc l) {
     //l1 = normalize(l);
     //containmentPlus = containment+;
     set[UID] decls = {};
     //if(UID uid <- config.store, blockScope(int _, l) := config.store[uid]) {
     try {
         UID uid = blockScopes[l];
         set[UID] innerScopes = containmentPlus[uid];
         for(UID inScope <- innerScopes) {
             decls = decls + declares[inScope];
         }
         return { addr | UID decl <- decls, tuple[str fuid,int pos] addr := uid2addr[decl] };
     } catch:
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
	= muFun1("<modName>/<fname>");

// Generate a MuExp to access a variable

// Sort available overloading alternatives as follows (trying to maintain good compatibility with the interpreter):
// - First non-default functions (inner scope first, most recent last), 
// - then default functions (also most inner scope first, then most recent last).

bool funFirst(int n, int m) = preferInnerScope(n,m); // || n < m; // n > m; //config.store[n].at.begin.line < config.store[m].at.begin.line;

list[int] sortOverloadedFunctions(set[int] items){

	defaults = [i | i <- items, i in defaultFunctions];
	res = sort(toList(items) - defaults, funFirst) + sort(defaults, funFirst);
	//println("sortOverloadedFunctions: <items> =\> <res>");
	return res;
}

bool preferInnerScope(int n, int m) {
    key = <n, m>;
    if(funInnerScopes[key]?){
       //println("preferInnerScope <key> =\> <funInnerScopes[key]> (cached)");
       return funInnerScopes[key];
    }
    nContainer = config.store[n].containedIn;
    nContainers = containedOrDeclaredInPlus[nContainer];
    mContainer = config.store[m].containedIn;
    mContainers = containedOrDeclaredInPlus[mContainer];
    
    bool res = false;
   
    if(nContainers == {} && mContainers == {}) { // global global
      	  res = n < m;
     } else
     if(nContainers == {} && mContainers != {}){ // global non-global
          res = false; //nContainer notin mContainers;
     } else
     if(nContainers != {} && mContainers == {}) { // non-global global
       res = true; //mContainer in nContainers;
     } else {							  // non-global non-global 
       res =  nContainer in mContainers;// && mContainer notin nContainers;
     }
	funInnerScopes[key] = res;
	//println("preferInnerScope <key> =\> <res>");
	return res;
}

//bool funInnerScope(int n, int m) {
//    key = <n, m>;
//    if(funInnerScopes[key]?){
//       return funInnerScopes[key];
//    }
//	res = config.store[n].containedIn in containmentPlus[config.store[m].containedIn];
//	funInnerScopes[key] = res;
//	return res;
//}

//public list[UID] sortFunctionsByRecentScope(list[UID] funs){
//	return sort(funs, funInnerScope);
//}

//public set[UID] accessibleScopes(loc luse) {
//  luse = normalize(luse);
//  println("accessibleScopes, luse = <luse>, <loc2uid[luse]>");
//  println("containmentPlus = <containmentPlus>");
//   println("declares = <declares>");
//   println("xxx <invert(containmentPlus)[loc2uid[luse]]>");
//  
//	 res1 = {0, 1} +
//     { uid | UID uid <- config.store, AbstractValue av := config.store[uid], av has at
//               //(  blockScope(int scope, loc l) := av 
//               //|| booleanScope(int scope, loc l) := av
//               //|| function(_,_,_, _, int scope, _, _, loc l) := av
//               //|| constructor(_,_,_,int scope, loc l) := av
//               //|| label(_,functionLabel(), int scope, loc l) := av
//               //|| closure(_,_, int scope, loc l)  := av
//               //|| \module(_, loc l) := av
//               //)
//               , luse < av.at || av.at.path != luse.path
//               };
//  res2 = {0, 1} + { uid | uid <- carrier(containment), AbstractValue av := config.store[uid], av has at, luse < av.at || av.at.path != luse.path};
//  if(res1 != res2){
//  	println("res1 = <res1>");
//  	println("res2 = <res2>");
//  }
//  return res1;
//} 
// 
public UID declaredScope(UID uid) {
	if(config.store[uid]?){
		res = config.store[uid].containedIn;
		//println("declaredScope[<uid>] = <res>");
		return res;
	}
	println("declaredScope[<uid>] = 0 (generated)");
	return 0;
}

public list[UID] accessibleAlts(list[UID] uids, loc luse){
  //////luse = normalize(luse);
  inScope = config.usedIn[luse] ? 0; // All generated functions are placed in scope 0
  
  key = <uids, inScope>;
  if(accessibleFunctions[key]?){
  	res = accessibleFunctions[key];
  	//println("CACHED ALTS: accessibleAlts(<uids>, <luse>): <res>");
  	return res;
  }
  
  set[UID] accessible = {};
  bool cachedScope = true;
  if(accessibleScopes[inScope]?){
     accessible = accessibleScopes[inScope];
  } else {
     accessible = {0, 1, inScope} + containedOrDeclaredInPlus[inScope] + importedModuleScopes;
     accessibleScopes[inScope] = accessible;
     cachedScope = false;
  }
  
  res = [ alt | UID alt <- uids, declaredScope(alt) in accessible ];
  accessibleFunctions[key] = res;
  //println("<cachedScope ? "CACHED SCOPES: " : "">accessibleAlts(<uids>, <luse>): <res>");
  return res;
}
 
MuExp mkVar(str name, loc l) {
  //////l = normalize(l);
  //name = unescape(name);
  //println("mkVar: <name>, <l>");
  uid = getLoc2uid(l);
  //println("uid = <uid>");
  //iprintln(uid2addr);
  tuple[str fuid,int pos] addr = uid2addr[uid];
  //println("addr = <addr>");
  
  // Pass all the functions through the overloading resolution
  if(uid in functions || uid in constructors || uid in ofunctions) {
    // Get the function uids of an overloaded function
    //println("config.store[<uid>] = <config.store[uid]>");
    list[int] ofuids = (uid in functions || uid in constructors) ? [uid] : sortOverloadedFunctions(config.store[uid].items);
    //println("@@@ mkVar: <name>, <l>, ofuids = <ofuids>");
    //for(nnuid <- ofuids){
    //	println("<nnuid>: <config.store[nnuid]>");
    //}
    // Generate a unique name for an overloaded function resolved for this specific use
    str ofuid = convert2fuid(config.usedIn[l]) + "/use:<name>#<l.begin.line>-<l.offset>";
    
 
    addOverloadedFunctionAndResolver(ofuid, <name, config.store[uid].rtype, addr.fuid, ofuids>);
  	return muOFun(ofuid);
  }
  
  // Keyword parameters
  if(uid in keywordParameters) {
     if(contains(topFunctionScope(), "companion")){
        // While compiling a companion function, force all references to keyword fields to be local
        return muLocKwp(name);
     } else {
  	   //println("return <topFunctionScope()>, <muVarKwp(addr.fuid,name)>");
       return muVarKwp(addr.fuid, name);
     }
  }
  
  //println("return : <muVar(name, addr.fuid, addr.pos)>");
  return muVar(name, addr.fuid, addr.pos);
}

// Generate a MuExp to reference a variable

MuExp mkVarRef(str name, loc l){
  //////l = normalize(l);
  <fuid, pos> = getVariableScope("<name>", l);
  return muVarRef("<name>", fuid, pos);
}

// Generate a MuExp for an assignment

MuExp mkAssign(str name, loc l, MuExp exp) {
  uid = getLoc2uid(l);
  tuple[str fuid, int pos] addr = uid2addr[uid];
  if(uid in keywordParameters) {
      return muAssignKwp(addr.fuid,name,exp);
  }
  return muAssign(name, addr.fuid, addr.pos, exp);
}

public list[MuFunction] lift(list[MuFunction] functions, str fromScope, str toScope, map[tuple[str,int],tuple[str,int]] mapping) {
    return [ (func.scopeIn == fromScope || func.scopeIn == toScope) 
	         ? { func.scopeIn = toScope; func.body = lift(func.body,fromScope,toScope,mapping); func; } 
	         : func 
	       | MuFunction func <- functions 
	       ];
}
public MuExp lift(MuExp body, str fromScope, str toScope, map[tuple[str,int],tuple[str,int]] mapping) {

    return visit(body) {
	    case muAssign(str name,fromScope,int pos,MuExp exp)    => muAssign(name,toScope,newPos,exp) 
	                                                              when <fromScope,pos> in mapping && <_,int newPos> := mapping[<fromScope,pos>]
	    case muVar(str name,fromScope,int pos)                 => muVar(name,toScope,newPos)
	                                                              when <fromScope,pos> in mapping && <_,int newPos> := mapping[<fromScope,pos>]
	    case muVarRef(str name, fromScope,int pos)             => muVarRef(name,toScope,newPos)
	                                                              when <fromScope,pos> in mapping && <_,int newPos> := mapping[<fromScope,pos>]
        case muAssignVarDeref(str name,fromScope,int pos,MuExp exp) 
        													   => muAssignVarDeref(name,toScope,newPos,exp)
                                                                  when <fromScope,pos> in mapping && <_,int newPos> := mapping[<fromScope,pos>]
	    case muFun2(str fuid,fromScope)                         => muFun2(fuid,toScope)
	    case muCatch(str id,fromScope,Symbol \type,MuExp body2) => muCatch(id,toScope,\type,body2)
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


Symbol getElementType(\list(Symbol et)) = et;
Symbol getElementType(\set(Symbol et)) = et;
Symbol getElementType(\bag(Symbol et)) = et;
Symbol getElementType(Symbol t) = Symbol::\value();

/*
 * translateType: translate a concrete (textual) type description to a Symbol
 */

Symbol translateType((BasicType) `value`) 		= Symbol::\value();
Symbol translateType(t: (BasicType) `loc`) 		= Symbol::\loc();
Symbol translateType(t: (BasicType) `node`) 	= Symbol::\node();
Symbol translateType(t: (BasicType) `num`) 		= Symbol::\num();
Symbol translateType(t: (BasicType) `int`) 		= Symbol::\int();
Symbol translateType(t: (BasicType) `real`) 	= Symbol::\real();
Symbol translateType(t: (BasicType) `rat`)      = Symbol::\rat();
Symbol translateType(t: (BasicType) `str`) 		= Symbol::\str();
Symbol translateType(t: (BasicType) `bool`) 	= Symbol::\bool();
Symbol translateType(t: (BasicType) `void`) 	= Symbol::\void();
Symbol translateType(t: (BasicType) `datetime`)	= Symbol::\datetime();

Symbol translateType(t: (StructuredType) `bag [ <TypeArg arg> ]`) 
												= \bag(translateType(arg)); 
Symbol translateType(t: (StructuredType) `list [ <TypeArg arg> ]`) 
												= \list(translateType(arg)); 
Symbol translateType(t: (StructuredType) `map[ <TypeArg arg1> , <TypeArg arg2> ]`) 
												= \map(translateType(arg1), translateType(arg2)); 
Symbol translateType(t: (StructuredType) `set [ <TypeArg arg> ]`)
												= \set(translateType(arg)); 
Symbol translateType(t: (StructuredType) `rel [ <{TypeArg ","}+ args> ]`) 
												= \rel([ translateType(arg) | arg <- args]);
Symbol translateType(t: (StructuredType) `lrel [ <{TypeArg ","}+ args> ]`) 
												= \lrel([ translateType(arg) | arg <- args]);
Symbol translateType(t: (StructuredType) `tuple [ <{TypeArg ","}+ args> ]`)
												= \tuple([ translateType(arg) | arg <- args]);
Symbol translateType(t: (StructuredType) `type [ < TypeArg arg> ]`)
												= \reified(translateType(arg));      

Symbol translateType(t : (Type) `(<Type tp>)`) 
												= translateType(tp);
Symbol translateType(t : (Type) `<UserType user>`) 
												= translateType(user);
Symbol translateType(t : (Type) `<FunctionType function>`) 
												= translateType(function);
Symbol translateType(t : (Type) `<StructuredType structured>`)  
												= translateType(structured);
Symbol translateType(t : (Type) `<BasicType basic>`)  
												= translateType(basic);
Symbol translateType(t : (Type) `<DataTypeSelector selector>`)  
												{ throw "DataTypeSelector"; }
Symbol translateType(t : (Type) `<TypeVar typeVar>`) 
												= translateType(typeVar);
Symbol translateType(t : (Type) `<Sym symbol>`)  
												= insertLayout(sym2symbol(symbol));		// make sure concrete lists have layout defined
								 							   
Symbol translateType(t : (TypeArg) `<Type tp>`) 
												= translateType(tp);
Symbol translateType(t : (TypeArg) `<Type tp> <Name name>`) 
												= \label(getSimpleName(convertName(name)), translateType(tp));

Symbol translateType(t: (FunctionType) `<Type tp> (<{TypeArg ","}* args>)`) 
												= \func(translateType(tp), [ translateType(arg) | arg <- args]);
									
Symbol translateType(t: (UserType) `<QualifiedName name>`) {
	// look up the name in the type environment
	val = getAbstractValueForQualifiedName(name);
	
	if(isDataType(val) || isNonTerminalType(val) || isAlias(val)) {
		return val.rtype;
	}
	throw "The name <name> is not resolved to a type: <val>.";
}
Symbol translateType(t: (UserType) `<QualifiedName name>[<{Type ","}+ parameters>]`) {
	// look up the name in the type environment
	val = getAbstractValueForQualifiedName(name);
	
	if(isDataType(val) || isNonTerminalType(val) || isAlias(val)) {
		// instantiate type parameters
		val.rtype.parameters = [ translateType(param) | param <- parameters];
		return val.rtype;
	}
	throw "The name <name> is not resolved to a type: <val>.";
}  
									
Symbol translateType(t: (TypeVar) `& <Name name>`) 
												= \parameter(getSimpleName(convertName(name)), Symbol::\value());  
Symbol translateType(t: (TypeVar) `& <Name name> \<: <Type bound>`) 
												= \parameter(getSimpleName(convertName(name)), translateType(bound));  

default Symbol translateType(Type t) {
	throw "Cannot translate type <t>";
}

