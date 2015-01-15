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
import experiments::Compiler::Rascal2muRascal::RascalModule;  // for getFunctionsInModule, need better structure

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

public Configuration getConfiguration() { return config; }

public map[UID uid, tuple[FUID fuid, int pos] fuid2pos] uid2addr = ();	
													// map uids to FUIDs and positions
public map[loc \loc,int uid] loc2uid = ();			// map a source code location of an entity to its uid

private set[UID] modules = {};

private set[UID] functions = {};						// declared functions

public bool isFunction(UID uid) = uid in functions;

private set[UID] defaultFunctions = {};				// declared default functions

public bool isDefaultFunction(UID uid) = uid in defaultFunctions;

private set[UID] constructors = {};					// declared constructors

public bool isConstructor(UID uid) = uid in constructors;
public set[UID] getConstructors() = constructors;

public set[UID] variables = {};						// declared variables

private map[str,int] module_var_init_locals = ();	        // number of local variables in module variable initializations

int getModuleVarInitLocals(str mname) {
	assert module_var_init_locals[mname]? : "getModuleVarInitLocals <mname>";
	return module_var_init_locals[mname];
}
public set[UID] keywordParameters = {};				// declared keyword parameters
public set[UID] ofunctions = {};					// declared overloaded functions

public set[UID] outerScopes= {};					// outermost scopes, i.e. scopes directly contained in the module scope;

public set[str] moduleNames = {};					// encountered module names


public map[UID uid,str name] uid2name = (); 		// map uid to simple names, used to recursively compute qualified names
@doc{Counters for different scopes}

private map[UID uid,int n] blocks = ();             // number of blocks within a scope
private map[UID uid,int n] closures = ();           // number of closures within a scope
private map[UID uid,int n] bool_scopes = ();        // number of boolean scopes within a scope
private map[UID uid,int n] sig_scopes = ();         // number of signature scopes within a scope

@doc{Handling nesting}
public rel[UID,UID] declares = {};
public rel[UID,UID] containment = {};

//public map[UID,UID] declaredIn = ();
//public map[UID,UID] containedIn = ();

alias OFUN = tuple[str fuid, list[UID] alts];		// An overloaded function and all its possible resolutions

public map[UID,str] uid2str = ();					// map uids to str

public map[UID,Symbol] uid2type = ();				// We need to perform more precise overloading resolution than provided by the type checker

private map[str,int] overloadingResolver = ();		// map function name to overloading resolver
private list[OFUN] overloadedFunctions = [];	// list of overloaded functions

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
	constructors = {};
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
	declares = {};
	containment = {};
	
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
      assert size({ config.store[uid] | UID uid <- uids }) == 1;
      size(uid2type[getOneFrom(uids)].parameters); 
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
      switch(item){
        case function(rname,rtype,keywordParams,_,inScope,_,_,src): { 
         	 //println("<uid>: <item>");
	         functions += {uid};
	         declares += {<inScope, uid>}; 
             loc2uid[src] = uid;
             for(l <- config.uses[uid]) {
                 loc2uid[l] = uid;
             }
             // Fill in uid2name
             
             fname = getSimpleName(rname);
             suffix = fname == "main" || endsWith(fname, "_init") || endsWith(fname, "testsuite") ? 0 : src.begin.line;
               
             name = getFUID(getSimpleName(rname),rtype,suffix);
             uid2name[uid] = name;
             
        	 // println("<uid>: <rname>, <rtype>, inScope=<inScope>, <src>");
        	 // println("name = <name>, uid2name[<uid>] = <uid2name[uid]>");
        	 	
        	 
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
		     	//println("add loc2uid[<l>] = <uid>");
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
    
    containmentPlus = containment+;
    
    for(muid <- modules){
        module_name = uid2name[muid];
        nmodule_var_init_locals = 0;
    	// First, fill in variables to get their positions right
    	// Sort variable declarations to ensure that formal parameters get first positions preserving their order 
    	topdecls = sort([ uid | uid <- declares[muid], variable(_,_,_,_,_) := config.store[uid] ]);
    	
 		fuid_module_init = getFUID(convert2fuid(muid),"#<module_name>_init",Symbol::func(Symbol::\value(),[Symbol::\list(Symbol::\value())]),0);
 		
    	for(i <- index(topdecls)) {
    		// Assign a position to module variables
            uid2addr[topdecls[i]] = <fuid_module_init, i + 1>;
            // Assign local positions to variables occurring in module variable initializations
            for(os <- outerScopes){
            	if(config.store[os].at < config.store[topdecls[i]].at){
            		decls_inner_vars = sort([ uid | UID uid <- declares[os], variable(RName name,_,_,_,_) := config.store[uid] ]);
    			    for(int j <- index(decls_inner_vars)) {
        			    uid2addr[decls_inner_vars[j]] = <fuid_module_init, 2 + nmodule_var_init_locals>;
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
    	}
    }

	// Fill in mapping of function uids to qualified names (enables invert mapping)
	for(UID uid <- functions + constructors) {
		uid2str[uid] = convert2fuid(uid);
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
        innerScopes = {fuid1} + containmentPlus[fuid1];
        // First, fill in variables to get their positions right

        if(config.store[fuid1] has keywordParams){
            keywordParams = config.store[fuid1].keywordParams;
            
            if(size(keywordParams) > 0){
                // There may be default expressions with variables, so introduce avariable ddresses inside the companion function
               // println("fuid1 = <fuid1>, nformals = <nformals>, innerScopes = <innerScopes>, keywordParams = <keywordParams>");
                // Filter all the non-keyword variables within the function scope
                // ***Note: Filtering by name is possible only when shadowing of local variables is not permitted
                // Sort variable declarations to ensure that formal parameters get first positions preserving their order
                decls_non_kwp = sort([ uid | UID uid <- declares[innerScopes], variable(RName name,_,_,_,_) := config.store[uid], name notin keywordParams ]);
                
                fuid_str = getCompanionForUID(fuid1);
                //println("fuid_str = <fuid_str>, decls_non_kwp = <decls_non_kwp>, declared[innerSopes] = <declares[innerScopes]>");
                for(int i <- index(decls_non_kwp)) {
                    // Note: we need to reserve positions for variables that will replace formal parameter patterns
                    // '+ 1' is needed to allocate the first local variable to store default values of keyword parameters
                    uid2addr[decls_non_kwp[i]] = <fuid_str, i>;
                }
                // Filter all the keyword variables (parameters) within the function scope
                decls_kwp = sort([ uid | UID uid <- declares[innerScopes], variable(RName name,_,_,_,_) := config.store[uid], name in keywordParams ]);
                //println("decls_kwp: <decls_kwp>");
                for(int i <- index(decls_kwp)) {
                    keywordParameters += decls_kwp[i];
                    uid2addr[decls_kwp[i]] = <fuid_str, -1>; // ***Note: keyword parameters do not have a position
                }
            }
        }

    }
    
    //println("ofunctions = <ofunctions>");
    
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
    	assert size(scopes) == 0 || size(scopes) == 1;
    	uid2addr[fuid2] = <scopeIn,-1>;
    }
    
    //for(int uid <- uid2addr){
    //	if(uid in ofunctions)
    //		println("uid2addr[<uid>] = <uid2addr[uid]>, <config.store[uid]>");
    //}
    
 //   declaredIn = toMapUnique(invert(declares));
	//containedIn = toMapUnique(invert(containment));
    
    // Finally, extract all declarations for the benefit of the type reifier
    
    getDeclarationInfo(config);
}

int declareGeneratedFunction(str name, Symbol rtype){
    uid = config.nextLoc;
    config.nextLoc = config.nextLoc + 1;
    functions += {uid};
    //declares += {<inScope, uid>}; 
     
    // Fill in uid2name
    //name = getFUID(name,rtype);
//println("name = <name>");
     
    uid2name[uid] = name;
    // Fill in uid2type to enable more precise overloading resolution
    uid2type[uid] = rtype;
    uid2str[uid] = name;
    return uid;
}

/********************************************************************/
/*     Part II: Retrieve type information                           */
/********************************************************************/

// Get the type of an expression as Symbol
Symbol getType(loc l) = config.locationTypes[l];

// Get the type of an expression as string
str getType(Tree e) = "<getType(e.origin)>";

// Get the outermost type constructor of an expression as string
str getOuterType(Tree e) { 
    tp = getType(e.origin);
	if(parameter(str _, Symbol bound) := tp) {
		return "<getName(bound)>";
	}
	if(label(_, Symbol sym) := tp){
	   return "<getName(sym)>";
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
   assert loc2uid[l]? : "getFunctionType <l>";
   UID uid = loc2uid[l];
   fun = config.store[uid];
   if(function(_,Symbol rtype,_,_,_,_,_,_) := fun) {
       return rtype;
   } else {
       throw "Looked up a function, but got: <fun> instead";
   }
}

Symbol getClosureType(loc l) {
   assert loc2uid[l]? : "getClosureType <l>";
   UID uid = loc2uid[l];
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
					
KeywordParamMap getKeywords(loc location) = config.store[loc2uid[location]].keywordParams;

tuple[str fuid,int pos] getVariableScope(str name, loc l) {
  //println("getVariableScope: <name>, <l>)");
  //for(l1 <- loc2uid){
  //          	if(/Exception/ !:= "<l1>")
  //          		println("<l1> : <loc2uid[l1]>");
  //          	if(l1 == l) println("EQUAL");
  //          }
  //println(	loc2uid[l] );
  assert loc2uid[l]? : "getVariableScope <l>";
  uid = loc2uid[l];
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


str getCompanionForUID(UID uid) = uid2str[uid] + "::companion";

str qualifiedNameToPath(QualifiedName qname){
    str path = replaceAll("<qname>", "::", "/");
    return replaceAll(path, "\\","");
}

str convert2fuid(UID uid) {
	if(!uid2name[uid]?) {
		throw "uid2str is not applicable for <uid>!";
	}
	str name = uid2name[uid];
	declaredIn = toMapUnique(invert(declares));
	containedIn = toMapUnique(invert(containment));
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
        	    return name;
			}
        }
		name = convert2fuid(declaredIn[uid]) + "/" + name;
	}
	//println("convert2fuid(<uid>) =\> <name>");
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
       s2v = symbolToValue(s /*, config*/);
       //println("s2v = <s2v>");
    }
    // TODO: this is too liberal, restrict to outer type.
    visit(s){
       case label(fieldName2, _):	if(unescape(fieldName2) == fieldName) return true;
    }
    return false;
}

public int getTupleFieldIndex(Symbol s, str fieldName) = 
    indexOf(getTupleFieldNames(s), fieldName);

public rel[str fuid,int pos] getAllVariablesAndFunctionsOfBlockScope(loc l) {
     containmentPlus = containment+;
     set[UID] decls = {};
     if(UID uid <- config.store, blockScope(int _, l) := config.store[uid]) {
         set[UID] innerScopes = containmentPlus[uid];
         for(UID inScope <- innerScopes) {
             decls = decls + declares[inScope];
         }
         return { addr | UID decl <- decls, tuple[str fuid,int pos] addr := uid2addr[decl] };
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
	= muFun1("<modName>/<fname>");

// Generate a MuExp to access a variable

bool compareScopes(int n, int m) = config.store[n].at.begin.line < config.store[m].at.begin.line;

list[int] sortOverloadedFunctions(set[int] items){

	//println("sortOverloadedFunctions: <items>");
	defaults = [i | i <- items, i in defaultFunctions];
	return sort(toList(items) - defaults, compareScopes) + sort(defaults, compareScopes);
}

MuExp mkVar(str name, loc l) {
  //name = unescape(name);
  //println("mkVar: <name>, <l>");
  uid = loc2uid[l];
  tuple[str fuid,int pos] addr = uid2addr[uid];
  
  // Pass all the functions through the overloading resolution
  if(uid in functions || uid in constructors || uid in ofunctions) {
    // Get the function uids of an overloaded function
    //println("config.store[<uid>] = <config.store[uid]>");
    list[int] ofuids = (uid in functions || uid in constructors) ? [uid] : sortOverloadedFunctions(config.store[uid].items);
    //println("ofuids = <ofuids>");
    //for(nnuid <- ofuids){
    //	println("<nnuid>: <config.store[nnuid]>");
    //}
    // Generate a unique name for an overloaded function resolved for this specific use
    str ofuid = convert2fuid(config.usedIn[l]) + /*"/use:<name>";   // */ "/use:<name>#<l.begin.line>";
    
 
    addOverloadedFunctionAndResolver(ofuid, <addr.fuid,ofuids>);
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

//bool isConcreteListVar(appl(prod(Symbol symbol1, [\iter(Symbol symbol2)]), list[Tree] args)) = true;
//bool isConcreteListVar(appl(prod(Symbol symbol1, [\iter-star(Symbol symbol2)]), list[Tree] args)) = true;
//
//bool isConcreteListVar(appl(prod(Symbol symbol1, [\iter-seps(Symbol symbol2, list[Symbol] separators)]), list[Tree] args)) = true;
//bool isConcreteListVar(appl(prod(Symbol symbol1, [\iter-star-seps(Symbol symbol2, list[Symbol] separators)]), list[Tree] args)) = true;
//default bool isConcreteListVar(Tree t) = false;

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
