@bootstrapParser
module lang::rascalcore::compile::muRascal2Java::CodeGen

import lang::rascal::\syntax::Rascal;

import lang::rascalcore::compile::muRascal::AST;

extend lang::rascalcore::check::CheckerCommon;

import Location;
import List;
import Set;
//import Relation;
import String;
import Map;
import Node;
import IO;
import Type;
import util::Math;
import util::Reflective;
import util::UUID;

import lang::rascalcore::compile::muRascal2Java::Primitives;
import lang::rascalcore::compile::Rascal2muRascal::RascalExpression;

import lang::rascalcore::compile::muRascal2Java::JGenie;
import lang::rascalcore::compile::muRascal2Java::Conversions;
import lang::rascalcore::compile::muRascal2Java::Tests;
import lang::rascalcore::compile::muRascal2Java::Interface;
import lang::rascalcore::compile::muRascal2Java::Resolvers;
//import lang::rascalcore::compile::muRascal2Java::SameJavaType;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;

import lang::rascalcore::compile::util::Names;

bool debug = false;

// ---- globals ---------------------------------------------------------------

map[str, MuFunction] muFunctions = ();
map[loc, MuFunction] loc2muFunction = ();

int naux = 0;

// ---- muModule --------------------------------------------------------------

// Generate code and test class for a single Rascal module

tuple[JCode, JCode, JCode, list[value]] muRascal2Java(MuModule m, map[str,TModel] tmodels, map[str,loc] moduleLocs){

    naux = 0;
    moduleName = m.name;
    locsModule = invertUnique(moduleLocs);
    module_scope = moduleLocs[moduleName];
    tm = tmodels[moduleName];
    //iprintln(tm);
    
    extends = { locsModule[m2loc] | <module_scope, extendPath(), m2loc> <- tmodels[moduleName].paths };
    
    imports = { locsModule[m2loc] | <module_scope, importPath(), m2loc> <- tmodels[moduleName].paths };
    imports += { locsModule[m2loc] | imp <- imports, impLoc := moduleLocs[imp], <impLoc, extendPath(), m2loc> <- tmodels[moduleName].paths};
    
    loc2muFunction = (f.src : f | f <- m.functions);
    
    // Iteratively propagate external dependencies of functions
    functions = m.functions;
    //for(f <- m.functions){ println("Before <f.name>, <f.scopeIn>: <f.externalRefs>, <f.localRefs>"); }
    solve(functions){
        functions  = [ addTransitiveRefs(f) | f <- functions ];
    }
    m.functions = functions;
    
    //for(f <- m.functions){ println("After <f.name>, <f.scopeIn>: <f.externalRefs>, <f.localRefs>"); }
    
    muFunctions = (f.uniqueName : f | f <- m.functions);
 
    jg = makeJGenie(m, tmodels, moduleLocs, muFunctions);
    resolvers = generateResolvers(moduleName, loc2muFunction, imports, extends, tmodels, moduleLocs, jg);
    
    moduleScopes = range(moduleLocs); //{ s |tm <- range(tmodels), s <- tm.scopes, tm.scopes[s] == |global-scope:///| };
    facts = tm.facts;
    cons_in_module = { def.defInfo.atype | Define def <-range(tm.definitions), def.idRole == constructorId(), isContainedIn(def.scope, module_scope) }
                     + { t | loc k <- facts, /AType t:acons(AType adt, list[AType] fields, list[Keyword] kwFields) := facts[k],
                           !isEmpty(adt.parameters), any(p <- adt.parameters, !isTypeParameter(p))
                       };
    adt_in_module = getADTs();
    
    declareTypes(adt_in_module, cons_in_module, jg);
    bool hasMainFunction = false;
    str mainName = "main";
    AType mainType = avoid();
    MuFunction mainFunction;
    for(f <- m.functions){
        if(isOuterScopeName(f.scopeIn) && isMainName(f.name)) {
            hasMainFunction = true;
            mainFunction = f;
            mainType = f.ftype;
            mainName = getUniqueFunctionName(f);
        }
        jg.addExternalRefs(f.externalRefs); //TODO: remove both?
        jg.addLocalRefs(f.localRefs);
    }
 
    className = asClassName(moduleName);  
    baseClassName = asBaseClassName(moduleName); 
    packageName = asPackageName(moduleName);
    baseInterfaceName = asBaseInterfaceName(moduleName);
    
    module_variables  = "<for(var <- m.module_variables){>
                        'public <trans(var, jg)><}>";
                       
    function_decls    = "<for(f <- m.functions){>
                        '<trans(f, jg)>
                        '<}>";
               
    library_decls     = "<for(class <- jg.getImportedLibraries()){>
                        'final <asQualifiedClassName(class)> <asBaseClassName(class)>; // TODO: asBaseClassName will generate name collisions if there are more of the same name in different packages
                        '<}>";
    
    library_inits     = "<for(class <- jg.getImportedLibraries()){>
                        '<asBaseClassName(class)> = $initLibrary(\"<asQualifiedClassName(class)>\"); 
                        '<}>";
    module_implements =  "implements <intercalate(",", [ "\n\t<module2interface(ext)>" | ext <- moduleName + extends])>";
                        
    module_imports    = "<for(imp <- imports + extends, contains(module2class(imp), ".")){>
                         'import <module2class(imp)>;
                        '<}>";
                       
    imp_ext_decls     = "<for(imp <- imports + extends){>
                        'public final <asClassRef(imp)> <module2field(imp)>;<}>
                        '";
                           
    module_ext_inits  = "<for(ext <- extends){>
                        '<module2field(ext)> = <module2class(ext)>.extend<asBaseClassName(ext)>(this);
                        '<}>";
    module_var_inits =  "<for(exp <- m.initialization){><trans(exp, jg)><}>";
    <constant_decls, constant_inits, constants> = jg.getConstants();
    
    packagePath = replaceAll(asPackagePath(moduleName),".","/");
    constantsFile = "rascal/" +  packagePath + (isEmpty(packagePath) ? "" : "/") + "<baseClassName>.constants";
  
    class_constructor = "public <baseClassName>(RascalExecutionContext rex){
                        '    this(rex, null);
                        '}
                        '
                        'public <baseClassName>(RascalExecutionContext rex, Object extended){
                        '   super(rex);
                        '   this.$me = extended == null ? this : (<baseInterfaceName>)extended;
                        '   ModuleStore mstore = rex.getModuleStore();
                        '   mstore.put(<asClassRef(moduleName)>.class, this);
                        '   <for(imp <- imports, imp notin extends){>
                        '   mstore.importModule(<asClassRef(imp)>.class, rex, <asClassRef(imp)>::new);<}> 
                        '   <for(imp <- imports, imp notin extends){>
                        '   <module2field(imp)> = mstore.getModule(<asClassRef(imp)>.class);<}> 
                        '   <for(ext <- extends){>
                        '   <module2field(ext)> = mstore.extendModule(<asClassRef(ext)>.class, rex, <asClassRef(ext)>::new, $me);<}>
                      
                        '   <for(imp <- imports+extends){>
                        '   $TS.importStore(<module2field(imp)>.$TS);<}>
                        '   <library_inits>
                        '   $constants = readBinaryConstantsFile(this.getClass(), \"<constantsFile>\");
                        '   <constant_inits>
                        '   <module_var_inits>
                        '}";
    
    externalArgs = "";                 
    if(hasMainFunction && !isEmpty(mainFunction.externalRefs)){
      externalArgs = intercalate(", ", [ newValueRef(var.name, jtype, var.name) /*"new ValueRef\<<jtype>\>(<var.name>)"*/ | var <- mainFunction.externalRefs, var.pos >= 0, jtype := atype2javatype(var.atype)]);
    }              
        
    main_method = "public static void main(String[] args) {
                  '  throw new RuntimeException(\"No function `main` found in Rascal program `<m.name>`\");
                  '}";
                    
    if (hasMainFunction) {
      mainIsVoid     = mainFunction.ftype.ret == avoid();
      hasListStrArgs = [alist(astr())] := mainFunction.ftype.formals;
      hasDefaultArgs = mainFunction.ftype.kwFormals != [];
      //RascalExecutionContext rex, ModuleStore mstore, TypeStore tstore
      main_method = "public static void main(String[] args) {
                    '  long start_time = System.currentTimeMillis();
                    '  RascalExecutionContext rex = new RascalExecutionContext(System.in, System.out, System.err, null, null, <packageName>.<baseClassName>.class);
                    '  <baseClassName> instance = new <baseClassName>(rex);
                    '  long init_time = System.currentTimeMillis();
                    '  <if (!hasListStrArgs && !hasDefaultArgs) {>
                    '  <if (!mainIsVoid) {>IValue res = <}>instance.<mainName>();
                    '  <}><if (hasListStrArgs) {>
                    '  <if (!mainIsVoid) {>IValue res = <}>instance.<mainName>(java.util.Arrays.stream(args).map(a -\> $VF.string(a)).collect($VF.listWriter()));
                    '  <}><if (hasDefaultArgs) {>
                    '  <if (!mainIsVoid) {>IValue res = <}>instance.<mainName>($parseCommandlineParameters(\"<baseClassName>\", args, <atype2vtype(atuple(atypeList([t |  <t,_> <- mainFunction.ftype.kwFormals])), jg)>));
                    '  <}>
                    '  long end_time = System.currentTimeMillis();
                    '  <if (!mainIsVoid) {>if (res == null) {
                    '     throw new RuntimeException(\"Main function failed\"); 
                    '  } else {
                    '     System.out.println(res);
                    '  }
                    '  System.err.println(\"Running <className>: init: \" + (init_time - start_time) + \" ms, exec: \" + (end_time - init_time) + \" ms, total: \" + (end_time - start_time) + \" ms\"); 
                    <}>
                    '}";
    }
    
    the_class =         "<if(!isEmpty(packageName)){>package <packageName>;<}>
                        'import java.io.PrintWriter;
                        'import java.io.StringWriter;
                        'import java.util.*;
                        'import java.util.regex.Matcher;
                        'import io.usethesource.vallang.*;
                        'import io.usethesource.vallang.type.*;
                        'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.*;
                        'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.RascalExecutionContext;
                        'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.*;
                        'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse.*;
                        'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils.*;
                        'import org.rascalmpl.exceptions.Throw; 
                        'import org.rascalmpl.types.NonTerminalType;
                        'import org.rascalmpl.types.RascalTypeFactory;
                        'import org.rascalmpl.exceptions.RuntimeExceptionFactory;
                        'import org.rascalmpl.util.ExpiringFunctionResultCache;
                        'import org.rascalmpl.values.RascalValueFactory;
                        'import org.rascalmpl.values.ValueFactoryFactory;
                        'import org.rascalmpl.values.parsetrees.ITree;
                        'import org.rascalmpl.values.parsetrees.TreeAdapter;
                        '
                        '<module_imports>
                        '
                        '@SuppressWarnings(\"unused\")
                        'public class <baseClassName> 
                        '    extends
                        '        org.rascalmpl.core.library.lang.rascalcore.compile.runtime.$RascalModule
                        '    <module_implements> {
                        '
                        '    private final <baseInterfaceName> $me;
                        '    private final IList $constants;
                        '    <imp_ext_decls>
                        '    <library_decls>
                        '    <module_variables>
                        '    <constant_decls>
                        '    <class_constructor>
                        '    <resolvers>
                        '    <function_decls>
                        '    <main_method>
                        '}";
                       
      the_test_class = generateTestClass(packageName, baseClassName, m.functions, jg);
      
      the_interface = generateInterface(moduleName, packageName, m.functions, imports, extends, tmodels, jg);
      
      return <the_interface, the_class, the_test_class, constants>;
}

str newValueRef(str name, str jtype, str code)
    = "new ValueRef\<<jtype>\>(\"<name>\", <code>)";
    
str newValueRef(str vname, AType atype, v:muVar(str name, str _, int pos, AType _, IdRole idRole), JGenie jg)
    = jg.isRef(v) ? "new ValueRef\<<atype2javatype(atype)>\>(\"<vname>\", <varName(v, jg)>.getValue())" : newValueRef(vname, atype2javatype(atype), varName(v, jg))
    ;

default str newValueRef(str name, AType atype, MuExp exp, JGenie jg)
    = "new ValueRef\<<atype2javatype(atype)>\>(\"<name>\", <transWithCast(atype,exp,jg)>)";
    
set[MuExp] filteredExternalRefs(MuFunction fun)
    = { ev | ev <- fun.externalRefs /*, ev.fuid != fun.uniqueName*/ } - toSet(fun.extendedFormalVars);
    
MuFunction addTransitiveRefs(MuFunction fun){
    //if(fun.scopeIn == "") return fun;
    usedFunDefs = {};
    visit (fun.body){
      case muFun(loc uid, AType _atype): usedFunDefs += uid;
      case muOFun(list[loc] uids, AType atype): usedFunDefs += toSet(uids);
    }
    usedFuns = { loc2muFunction[uid] | uid <- usedFunDefs, loc2muFunction[uid]? };
    
    externalRefs = { *filteredExternalRefs(f) | f <- usedFuns };// - toSet(fun.extendedFormalVars);
    //externalRefs = { *(f.externalRefs - toSet(f.extendedFormalVars) )| f <- usedFuns};
    
    deltaExternalRefs = externalRefs - fun.externalRefs ;
    deltaLocalRefs =  { e | e <- externalRefs, e.fuid == fun.uniqueName } - fun.localRefs;
   
    if(!(isEmpty(deltaExternalRefs))){
        fun.externalRefs += deltaExternalRefs;
        loc2muFunction[fun.src] = fun;
    }
    if(!(isEmpty(deltaLocalRefs))){
        fun.localRefs += deltaLocalRefs;
        loc2muFunction[fun.src] = fun;
    }
    
    return fun;
}


void declareTypes(set[AType] ADTs, set[AType] constructors, JGenie jg){
    for(a <- ADTs) jg.shareType(a);
    for(c <- constructors) jg.shareType(c);
}

// Generate a TypeStore and declarations for keyword parameters

tuple[str adtTypeDecls, str adtTypeInits, str consTypeDecls, str consTypeInits, str kwpTypeDecls] generateTypeStoreAndKwpDecls(set[AType] ADTs, set[AType] constructors, JGenie jg){
    adtTypeDecls = "";
    adtTypeInits = "";
    adtTypeInstantiateInits = "";
    seenAdtNames = {};
    parameterized_ADTs = { a | /a:aadt(str adtName, list[AType] parameters, SyntaxRole _) := ADTs, !isEmpty(parameters), all(p <- parameters, isTypeParameter(p)) };
    
    for(a <- ADTs) jg.shareType(a);
    //for(aadt(str adtName, list[AType] parameters, SyntaxRole _) <- parameterized_ADTs){
    //    aname = "<adtName>_<intercalate("_", [atype2idpart(p) | p <- parameters])>";
    //    params = intercalate(", ", [ atype2vtype(p, jg) | p <- parameters]);
    //    adtTypeInits += "<getADTName(aname)> = $TF.abstractDataType($TS, \"<aname>\", <params>); /*CodeGen1b*/\n";
    //}
    //
    //for(/a:aadt(str adtName, list[AType] parameters, SyntaxRole _) := ADTs, a notin parameterized_ADTs){
    //    a = unset(a, "alabel");
    //    if(/*isEmpty(jg.getATypeAccessor(a)) && */!jg.isSharedType(a)){
    //        aname = isEmpty(parameters) ? adtName : "<adtName>_<intercalate("_", [atype2idpart(p) | p <- parameters])>";
    //
    //        if(aname notin seenAdtNames/*, isEmpty(jg.getATypeAccessor(a))*/){
    //            adtTypeDecls += "public io.usethesource.vallang.type.Type <getADTName(aname)>; /*CodeGen1a: <a>*/\n";
    //            if(isEmpty(parameters)){
    //            //params = isEmpty(parameters) ? "" : ", <intercalate(", ", [aparameter(str pname, AType bound) := p ? "$TF.valueType()" : atype2vtype(p, jg) | p <- parameters])>";
    //                adtTypeInits += "<getADTName(aname)> = $TF.abstractDataType($TS, \"<aname>\"); /*CodeGen1b*/\n";
    //            } else {
    //                bindings = "";
    //                for(pa <- parameterized_ADTs){
    //                    if(pa.adtName == adtName && size(parameters) == size(pa.parameters)){
    //                        for(int i <- index(parameters)){
    //                             bindings += "<atype2vtype(pa.parameters[i], jg)>, <atype2vtype(parameters[i], jg)>";
    //                        }
    //                        break;
    //                    }
    //                }
    //                adtTypeInstantiateInits += "<getADTName(aname)> = <getADTName(adtName)>_.instantiate(Map.of(<bindings>));\n";
    //            }
    //            seenAdtNames += aname;
    //        }
    //    }
    //}
    //adtTypeInits += adtTypeInstantiateInits;
    
    consTypeDecls = "";
    consTypeInits = "";
    kwpTypeDecls = "";
    seenConsNames = {};
    
    for(c <- constructors) jg.shareType(c);
    //for(c: acons(AType adt, list[AType] fields, list[Keyword] kwpFields) <- constructors, isEmpty(jg.getATypeAccessor(c))){
    //    //println(c);
    //    cname = atype2idpart(c);
    //    
    //    hasFieldNames = all(fld <- fields, !isEmpty(fld.alabel));
    //    fieldDecls = hasFieldNames ? [ "<atype2vtype(fld,jg)>, \"<fld.alabel>\"" | fld <- fields ] : [ "<atype2vtype(fld, jg)>" | fld <- fields ];
    //    if(cname notin seenConsNames){
    //        //jg.shareType(c);
    //        consTypeDecls += "public io.usethesource.vallang.type.Type <jg.getATypeAccessor(c)><cname>; /*CodeGen2a*/\n";
    //        aname = isEmpty(adt.parameters) ? adt.adtName : "<adt.adtName>_<intercalate("_", [atype2idpart(p) | p <- adt.parameters])>";
    //        if(aname notin seenAdtNames && !jg.isSharedType(adt)){
    //            params = isEmpty(adt.parameters) ? "" : ", <intercalate(", ", [aparameter(str pname, AType bound) := p ? "$TF.valueType()" : atype2vtype(p, jg) | p <- adt.parameters])>";
    //            adtTypeInits += "<getADTName(aname)> = $TF.abstractDataType($TS, \"<aname>\"<params>);/*CodeGen2b*/\n";
    //            seenAdtNames += aname;
    //        }
    //    }
    //    if(cname notin seenConsNames){
    //        //if(isEmpty(adt.parameters) || !any(p <- adt.parameters, isTypeParameter(p))){
    //            adt_name = isEmpty(adt.parameters) ? adt.adtName : "<adt.adtName>_<intercalate("_", [atype2idpart(p) | p <- adt.parameters])>";
    //            consTypeInits += "<cname> = $TF.constructor($TS, <jg.getATypeAccessor(c)><getADTName(adt_name)>, \"<getUnqualifiedName(c.alabel)>\"<isEmpty(fieldDecls) ? "" : ", <intercalate(", ", fieldDecls)>">);/*CodeGen3*/\n";
    //            seenConsNames += cname;
    //        //}
    //    }
    //    for(kwpField <- kwpFields){
    //        kwpTypeDecls += xxxf(<cname>,\"<kwpField.fieldType.alabel>\", <atype2vtype(kwpField.fieldType, jg)>);\n";
    //    }
    //}    
    return <adtTypeDecls, adtTypeInits,
            consTypeDecls, consTypeInits,
            kwpTypeDecls>;
}

JCode makeConstructorCall(AType consType, list[str] actuals, list[str] kwargs, JGenie jg){
    if(!isEmpty(kwargs)){
        return "$VF.constructor(<jg.getATypeAccessor(consType)><atype2idpart(consType)>, new IValue[]{<intercalate(", ", actuals)>}, <kwargs[0]>)";
    } else {
     return "$VF.constructor(<jg.getATypeAccessor(consType)><atype2idpart(consType)>, new IValue[]{<intercalate(", ", actuals)>})";
    }
}

// ---- muModuleVar ----------------------------------------------------------

JCode trans(MuModuleVar var, JGenie _jg){
       return "<atype2javatype(var.atype)> <asJavaName(var.name)>;";
}

// ---- muFunction ------------------------------------------------------------

bool constantDefaults(lrel[str name, AType atype, MuExp defaultExp] kwpDefaults){
    return all(<str _, AType _, MuExp defaultExp> <- kwpDefaults, muCon(_) := defaultExp);
}

//str makeArgType(AType t, MuExp formal, set[MuExp] externals){
//    if(formal in externals){
//        return "ValueRef\<<atype2javatype(t)>\>";
//    } else {
//        return atype2javatype(t);
//    }
//}

tuple[str argTypes, str constantKwpDefaults, str nonConstantKwpDefaults, str refInits] getArgTypes(MuFunction fun, JGenie jg){   
    shortName = asJavaName(getUniqueFunctionName(fun)); 

    argTypeList = [];
    refInits = "";
    formalsUsedAsRef =  {ev | ev <- fun.externalRefs + fun.localRefs, ev in fun.extendedFormalVars};
    for(i <- index(fun.formals)){
        formal = fun.formals[i];
        jtype = atype2javatype(fun.ftype.formals[i]);
        varName = varName(fun.formals[i], jg);
        if(formal in formalsUsedAsRef){
            aux = "$aux_<varName>";
            argTypeList += "<jtype> <aux>";
            refInits += "ValueRef\<<jtype>\> <varName> = <newValueRef(varName, jtype, aux)>;\n";  //new ValueRef\<<jtype>\>(<aux>);\n";
        } else {
            argTypeList += "<jtype> <varName>";
        }
    }

    argTypes = intercalate(", ", argTypeList);
       
    if(!isEmpty(fun.externalRefs) && !isEmpty(fun.scopeIn) /*!isMainName(fun.name)*/){
        ext_actuals = intercalate(", ", [/*var in assignableRoles ?*/ "ValueRef\<<atype2javatype(var.atype)>\> <varName(var, jg)>" /*: "<atype2javatype(var.atype)> <varName(var, jg)>"*/ | var <- sort(fun.externalRefs), var.pos >= 0, var notin fun.extendedFormalVars]);
        argTypes = isEmpty(fun.formals) ? ext_actuals : (isEmpty(ext_actuals) ? argTypes : "<argTypes>, <ext_actuals>");
    }
    kwpActuals = "java.util.Map\<java.lang.String,IValue\> $kwpActuals";
    kwpDefaults = jg.collectKwpDefaults(fun);
    constantKwpDefaults = "";
    nonConstantKwpDefaults = "";
  
    if(!isEmpty(kwpDefaults)){
        argTypes = isEmpty(argTypes) ? kwpActuals : "<argTypes>, <kwpActuals>";
          
        if(constantDefaults(kwpDefaults)){
            
            kwpDefaultsName =  "$kwpDefaults_<shortName>";
            jg.setKwpDefaultsName(kwpDefaultsName);
            mapCode = "Util.kwpMap(<intercalate(", ", [ *["\"<unescape(key)>\"", trans(defaultExp,jg)] | <str key, AType _, MuExp defaultExp> <- kwpDefaults ])>);\n";
            
            constantKwpDefaults = "final java.util.Map\<java.lang.String,IValue\> <kwpDefaultsName> = <mapCode>";
         } else {
            jg.setKwpDefaultsName("$kwpDefaults");
            
            nonConstantKwpDefaults =  "java.util.Map\<java.lang.String,IValue\> $kwpDefaults = Util.kwpMap();\n";
            for(<str name, AType tp, MuExp defaultExp> <- kwpDefaults){
                escapedName = asJavaName(name);
                nonConstantKwpDefaults += "<trans(muConInit(muVar("$kwpDefault_<escapedName>","", 10, tp,  keywordFieldId()), defaultExp),jg)>$kwpDefaults.put(\"<escapedName>\", $kwpDefault_<escapedName>);";
            }
         }   
    } else if(!isEmpty(fun.scopeIn) && !isClosureName(fun.name)){
        if(!isEmpty(jg.collectDeclaredKwps(fun))){
            argTypes = isEmpty(argTypes) ? kwpActuals : "<argTypes>, <kwpActuals>";
            jg.setKwpDefaultsName("$kwpDefaults");
            nonConstantKwpDefaults = "java.util.Map\<java.lang.String,IValue\> $kwpDefaults = Util.kwpMap();\n";
        }
    }
    if(!isEmpty(constantKwpDefaults)){
        constantKwpDefaults += " // TODO: move outside this function";
    }
    return <argTypes, constantKwpDefaults, nonConstantKwpDefaults, refInits>;
}

tuple[int secondsTimeout, int maxSize] getMemoSettings(str memoString){
    secondsTimeout = -1;
    maxSize = -1;
    if(/maximumSize\(<s:[0-9]+>\)/ := memoString) {
        maxSize = toInt(s);
    }
    if(/expireAfter\(<params:[^)]*>\)/ := memoString){
        switch(params){
        case /seconds\s=\s<s:[0-9]+>/:
            secondsTimeout = toInt(s);
        case /minutes\s=\s<s:[0-9]+>/:
            secondsTimeout = 60 * toInt(s);
        case /hours\s=\s<s:[0-9]+>/:
            secondsTimeout = 3600 * toInt(s);
        }
    }
    return <secondsTimeout, maxSize>;
}

// Copy from RascalDeclaration, move to separate module

bool ignoreCompiler(map[str,str] tagsMap)
    = !isEmpty(domain(tagsMap) &  {"ignore", "Ignore", "ignoreCompiler", "IgnoreCompiler"});

str getMemoCache(MuFunction fun)
    = "$memo_<asJavaName(getUniqueFunctionName(fun))>";
    
JCode trans(MuFunction fun, JGenie jg){
    //println("trans <fun.name>, <fun.ftype>");
    //println("trans: <fun.src>, <jg.getModuleLoc()>");
    //iprintln(fun); // print function
    
    if(!isContainedIn(fun.src, jg.getModuleLoc())) return "";
    
    if(ignoreCompiler(fun.tags)) return "";
    
    ftype = fun.ftype;
    jg.setFunction(fun);
    
    shortName = asJavaName(getUniqueFunctionName(fun));
    
    visibility = "public "; // isSyntheticFunctionName(shortName) ? "private " : "public "; $getkw_ should be public
    uncheckedWarning = "";
    if(afunc(AType _, list[AType] _, list[Keyword] _) := ftype){
        returnType = atype2javatype(ftype.ret);
        <argTypes, constantKwpDefaults, nonConstantKwpDefaults, refInits> = getArgTypes(fun, jg);
        //if(!isEmpty(ftype.formals) && isSyntaxType(ftype.formals[0])) return "";
        redeclaredKwps = jg.collectRedeclaredKwps(fun);
        removeRedeclaredKwps = "";
        
        //if(!isEmpty(kwFormals) ){
        //    removeRedeclaredKwps = isEmpty(redeclaredKwps) ? "" : "$kwpActuals = Util.kwpMapRemoveRedeclared($kwpActuals, <intercalate(", ", ["\"<asJavaName(key)>\"" | str key <- redeclaredKwps ])>);";
        //}
 
        //if(!isEmpty(nonConstantKwpDefaults) ){
        //    removeRedeclaredKwps = isEmpty(redeclaredKwps) ? "" : "$kwpActuals = Util.kwpMapRemoveRedeclared($kwpActuals, <intercalate(", ", ["\"<asJavaName(key)>\"" | str key <- redeclaredKwps ])>);";
        //}
        memoCache = "";
        if(fun.isMemo){
            <secondsTimeout, maxSize> = getMemoSettings(fun.tags["memo"] ? "");
            memoCache = "private final ExpiringFunctionResultCache\<IValue\> <getMemoCache(fun)> = new ExpiringFunctionResultCache\<IValue\>(<secondsTimeout>, <maxSize>);\n";
        }
        body = refInits + trans2Void(fun.body, jg);
        containsVisit = /muVisit(_,_,_,_,_) := fun.body;
        if(containsVisit){
            body = "try {
                   '    <body>
                   '} catch (ReturnFromTraversalException e) {
                   '    return <returnType == "void" ? "" : "(<returnType>) e.getValue()">;
                   '}\n";
        }
        kwpActuals = "";
        if(!isEmpty(fun.keywordParameterRefs) /*|| !isEmpty(jg.collectKwpFormals(fun))*/ && !contains(argTypes, "$kwpActuals")){
            kwpActuals = "java.util.Map\<java.lang.String,IValue\> $kwpActuals";
        }
        argTypes = isEmpty(argTypes) ? kwpActuals : (((isEmpty(kwpActuals) || contains(argTypes, "$kwpActuals")) ? argTypes : "<argTypes>, <kwpActuals>"));
        
        return "<memoCache><visibility><returnType> <shortName>(<argTypes>){ // by CodeGen: <ftype> 
               '    <constantKwpDefaults><nonConstantKwpDefaults><removeRedeclaredKwps>
               '    <body>
               '}";
    } else
        throw "trans MuFunction: <ftype>";
}

JCode call(MuFunction fun, list[str] actuals, JGenie _jg){
    return "<fun.uniqueName>(<intercalate(", ", actuals)>)";
}

default JCode trans(MuExp exp, JGenie jg){
    throw "Cannot translate <exp>";
}

JCode trans(muComment(str text), JGenie jg)
    = "/* <text> */";

// ---- muNoValue -------------------------------------------------------------

JCode trans(muNoValue(), JGenie jg) = "null";

// ---- muCon -----------------------------------------------------------------                                       

JCode trans(muCon(value v), JGenie jg) = jg.shareConstant(v);

JCode trans(muATypeCon(AType t, map[AType, set[AType]] definitions), JGenie jg) {
    // here we translate the types back to the old symbols, to be able
    // to bootstrap on the old parser generator, and also the client code
    // that use the definitions in the Type and ParseTree modules.
    x = atype2symbol(t);
    y = adefinitions2definitions(definitions);
    return jg.shareReifiedConstant(atype2symbol(t), adefinitions2definitions(definitions));
} 
                      
JCode trans(muFun(loc uid, AType ftype), JGenie jg){
    nformals = size(ftype.formals);
    sep = nformals > 0 ? "," : "";
    uniq = uid.offset;
   
    formals = intercalate(", ", ["$<uniq>_<i>" | i <- [0..nformals]]);
   
    actuals = intercalate(", ", ["(<atype2javatype(ftype.formals[i])>)$<uniq>_<i>" | i <- [0..nformals]]);
    
    if(!isEmpty(ftype.kwFormals)){
        actuals = isEmpty(actuals) ?  "$kwpActuals" : "<actuals>, $kwpActuals";
    } else if(loc2muFunction[uid]?){
        fun = loc2muFunction[uid];
        if(fun.scopeIn != "" && !isEmpty(fun.keywordParameterRefs)){ //!isEmpty(jg.collectKwpFormals(jg.getFunction()))){
            actuals = isEmpty(actuals) ?  "$kwpActuals" : "<actuals>, $kwpActuals";
        }
    }
    
    externalRefs = jg.getExternalRefs(uid);
    ext_actuals = actuals;
    if(!isEmpty(externalRefs)){
        ext_actuals = "";
        if(loc2muFunction[uid]?){
            fun = loc2muFunction[uid];
            current_fun = jg.getFunction();
            
            ext_actuals = intercalate(", ", [ fun.scopeIn == var.fuid ? ((jg.isRef(var) || var.idRole notin assignableRoles)  ? "<var.name>_<var.pos>" : newValueRef(var.name, atype2javatype(var.atype), "<var.name>_<var.pos>"))
                                                                      : newValueRef(var.name, var.atype, var, jg) | var <- externalRefs ]);
            
            //ext_actuals = intercalate(", ", [ fun.scopeIn == var.fuid ? "<var.name>_<var.pos>" : newValueRef(var.name, var.atype, var, jg) | var <- externalRefs ]);
            //ext_actuals = intercalate(", ", [ fun.scopeIn == var.fuid ? (jg.isLocalRef(var) ? "<var.name>_<var.pos>" : newValueRef(var.name, atype2javatype(var.atype), "<var.name>_<var.pos>"))
            //                                                          : newValueRef(var.name, var.atype, var, jg) | var <- externalRefs ]);

        } else {
           ext_actuals = intercalate(", ", [ var.idRole notin assignableRoles ? "<var.name>_<var.pos>" : newValueRef(var.name, var.atype, var, jg) | var <- externalRefs ]);
        }
        ext_actuals = isEmpty(actuals) ? ext_actuals : "<actuals>, <ext_actuals>";
    }
    
    funInstance = "new TypedFunctionInstance<nformals>\<IValue<sep><intercalate(",", ["IValue" | int _ <- [0..nformals]] )>\>";
    
    reta = isVoidType(ftype.ret) ? "" : "return ";
    retb = isVoidType(ftype.ret) ? "return null;" : "";
    res = "<funInstance>((<formals>) -\> { <reta><jg.getAccessor([uid])>(<ext_actuals>);<retb> }, <jg.accessType(ftype)>)";
    return res;
}          
// ---- muOFun ----------------------------------------------------------------
       
JCode trans(muOFun(list[loc] srcs, AType ftype), JGenie jg){
    overloading = false;
    fname = jg.getAccessor(srcs);
   
    nformals = size(getFormals(ftype));
    sep = nformals > 0 ? "," : "";
    uniq = abs(uuidi());
    
    formals = intercalate(", ", ["$<uniq>_<i>" | i <- [0..nformals]]);
    formalsWithCast = intercalate(", ", ["(<atype2javatype(getFormals(ftype)[i])>)$<uniq>_<i>" | i <- [0..nformals]]);
    nexternals = 0;
    if(size(srcs) == 1 && !isSyntheticFunctionName(fname) && loc2muFunction[srcs[0]]?){
        fun = loc2muFunction[srcs[0]];
        if(!isEmpty(fun.externalRefs)){
           nexternals = size(fun.externalRefs);
           ext_actuals = intercalate(", ", [varName(v, jg) | v <- fun.externalRefs]);
           formals = isEmpty(formals) ? ext_actuals : "<actuals>, <ext_actuals>";
        }
    }
    funInstance = "new TypedFunctionInstance<nformals>\<<"IValue"><sep><intercalate(",", ["IValue" | int _ <- [0 ..nformals]])>\>";
    
    return "<funInstance>((<formals>) -\> { return <fname>(<formalsWithCast>); }, <jg.accessType(ftype)>)";
}

// ---- muComposedFun ---------------------------------------------------------

str trans(muComposedFun(MuExp left, MuExp right, AType leftType, AType rightType, AType resultType), JGenie jg){

    if(overloadedAType(rel[loc, IdRole, AType] overloads) := leftType){
        leftType = lubList(toList(overloads<2>));
    }
    
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := rightType){
        rightType = lubList(toList(overloads<2>));
    }
 
    nformals = size(getFormals(rightType));
    sep = nformals > 0 ? "," : "";
    uniq = abs(uuidi());
    
    funInstance = "new TypedFunctionInstance<nformals>\<<"IValue"><sep><intercalate(",", ["IValue" | _ <- getFormals(rightType)])>\>";
    formals = intercalate(", ", ["$<uniq>_<i>" | i <- [0..nformals]]);
    formalsWithCast = intercalate(", ", ["(<atype2javatype(getFormals(rightType)[i])>)$<uniq>_<i>" | i <- [0..nformals]]);
    rightCall = "<trans(right, jg)>";
     
    rightCall = "<trans(right, jg)>.typedCall(<formalsWithCast>)";
    body = "return <trans(left, jg)>.typedCall(<rightCall>);";
    resultFunType = rightType[ret = leftType.ret];
    
    return "<funInstance>((<formals>) -\> { <body> }, <jg.accessType(resultFunType)>)";
}

// ---- muConstr --------------------------------------------------------------

str trans(muConstr(AType ctype), JGenie jg){
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := ctype){
        ovl = [ atype | <loc l, constructorId(), AType atype> <- overloads ];
        if(size(ovl) != 1) throw "muConstr: overloaded constructor type: <ovl>";
        ctype = ovl[0];
    }
    nformals = size(ctype.fields);
    sep = nformals > 0 ? "," : "";
    
    funInstance = "new TypedFunctionInstance<nformals>\<<"IValue"><sep><intercalate(",", ["IValue" | _ <- ctype.fields])>\>";
    
    pos_formals = ["$<i>" | i <- [0..nformals]];
    needKwps = hasKeywordParameters(ctype) || jg.hasCommonKeywordFields(ctype);
    if(needKwps) pos_formals += "final java.util.Map\<java.lang.String,IValue\> $kwpActuals";
    all_formals = needKwps ? (pos_formals + "final java.util.Map\<java.lang.String,IValue\> $kwpActuals") :  pos_formals;
    //TODO: was: kwpActual, fix it
    return "<funInstance>((<intercalate(", ", all_formals)>) -\> { return <makeConstructorCall(ctype,  pos_formals, needKwps ? ["$kwpActuals"] : [], jg)>; }, <jg.accessType(ctype)>)";
}

// ---- Tree operations -------------------------------------------------------

str trans(muTreeAppl(MuExp p, MuExp argList, loc src), JGenie jg) 
  = "$RVF.appl(<trans(p, jg)>, <trans(argList, jg)>).asWithKeywordParameters().setParameter(\"src\", <jg.shareConstant(src)>)";


str trans(muTreeAppl(MuExp p, list[MuExp] args, loc src), JGenie jg) 
  = "$RVF.appl(<trans(p, jg)>, $VF.list(<intercalate(",", [trans(a, jg) | a <- args])>)).asWithKeywordParameters().setParameter(\"src\", <jg.shareConstant(src)>)";
  
str trans(muTreeChar(int ch), JGenie jg) 
  = "$RVF.character(<ch>)";  

str trans(muTreeGetProduction(MuExp t), JGenie jg) {
    return  "((org.rascalmpl.values.parsetrees.ITree) <trans(t,jg)>).getProduction()";
}

str trans(muTreeIsProductionEqual(MuExp tree, MuExp production), JGenie jg){
    return  "$isTreeProductionEqual((IConstructor)<trans(tree, jg)>, <trans(production, jg)>)";
}

str trans(muTreeGetArgs(MuExp t), JGenie jg) {
    return "((org.rascalmpl.values.parsetrees.ITree) <trans(t, jg)>).getArgs()";
}

str trans(muTreeUnparse(MuExp t), JGenie jg) = "$VF.string(org.rascalmpl.values.parsetrees.TreeAdapter.yield(<trans(t, jg)>))";
str trans(muTreeUnparseToLowerCase(MuExp t), JGenie jg) = "$VF.string(org.rascalmpl.values.parsetrees.TreeAdapter.yield(<trans(t, jg)>).toLowerCase())";

// ---- Type parameters ---------------------------------------------------------

str trans(muTypeParameterMap(set[AType] parameters), JGenie jg){
    return "HashMap\<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type\> $typeBindings = new HashMap\<\>();
           '<for(p <- parameters){>
           '$typeBindings.put(<atype2vtype(p, jg)>, $TF.voidType());
           '<}>";
}

str trans(c:muComposedFun(MuExp left, MuExp right, AType leftType, AType rightType, AType resultType), JGenie jg){
    throw "muComposedFun: <c>";
}

// Variables

//// ---- muModuleVar -----------------------------------------------------------
//
//JCode trans(var:muModuleVar(str name, AType atype), JGenie jg{
//    return name;
//}

// ---- muVar -----------------------------------------------------------------

str varName(muVar(str name, str _, int pos, AType _, IdRole idRole), JGenie _jg){
    return (name[0] != "$") ? "<asJavaName(name)><(pos >= 0 || isWildCard(name)) ? "_<abs(pos)>" : "">" : asJavaName(name);
}
        
JCode trans(var:muVar(str name, str fuid, int pos, AType atype, IdRole idRole), JGenie jg){
    //var = unsetRec(var, "alabel");
    
    
    return jg.isRef(var) //&& !jg.varHasLocalScope(var)
                                    ? "<varName(var, jg)>.getValue()" 
                                    : ( pos >= 0 ? varName(var, jg)
                                                 : "<fuid == jg.getFunctionName() ? "" : fuid == jg.getModuleName() ? "" : "<module2field(fuid)>."><varName(var, jg)>"
                                      );
   
    //return jg.varHasLocalScope(var)
    //       ? varName(var, jg)
    //       : (jg.varHasGlobalScope(var) ? "<fuid == jg.getFunctionName() ? "" : fuid == jg.getModuleName() ? "" : "<module2field(fuid)>."><varName(var, jg)>"
    //                        
    //       :  "<varName(var, jg)>.getValue()"
    //       );

   //return jg.isRef(var) && pos >= 0 ? "<varName(var, jg)>.getValue()" 
   //                                 : ( pos >= 0 ? varName(var, jg)
   //                                              : "<fuid == jg.getFunctionName() ? "" : fuid == jg.getModuleName() ? "" : "<module2field(fuid)>."><varName(var, jg)>"
   //                                   );
}
// ---- muTmpIValue -----------------------------------------------------------------

JCode trans(var: muTmpIValue(str name, str fuid, AType atype), JGenie jg)
    = jg.isRef(var) ? "<name>.getValue()" : name;
        //= jg.isExternalVar(var) ? "<name>.value" : name;
    
JCode trans(var: muTmpNative(str name, str fuid, NativeKind nkind), JGenie jg)
    = jg.isRef(var) ? "<name>.getValue()" : name;
      //= jg.isExternalVar(var) ? "<name>.value" : name;
  
// ---- muVarDecl --------------------------------------------------------------

JCode trans(muVarDecl(v: muVar(str name, str fuid, int pos, AType atype, IdRole idRole)), JGenie jg){
    jtype = atype2javatype(atype);
    return jg.isRef(v) ? "ValueRef\<<jtype>\> <varName(v, jg)> = new ValueRef\<<jtype>\>();\n"
                       : "<jtype> <varName(v, jg)> = null;\n";  
}

JCode trans(muVarDecl(v: muTmpIValue(str name, str fuid, AType atype)), JGenie jg){
    jtype = atype2javatype(atype);
    return jg.isRef(v) ? "ValueRef\<<jtype>\> <name> = new ValueRef\<<jtype>\>();\n"
                       : "<jtype> <name> = null;\n";
}

JCode trans(muVarDecl(var: muTmpNative(str name, str fuid, NativeKind nkind)), JGenie jg){
    <base, ref> = native2ref[nkind];
    return jg.isRef(var) ? "<ref> <name> = null;\n"
                         : "<base> <name> = null;\n";
}
  
// ---- muVarInit --------------------------------------------------------------

str parens(str code)
    = endsWith(code, ";\n") ? "(<code[0..-2]>)" : "(<code>)";

JCode trans(muVarInit(v: muVar(str name, str fuid, int pos, AType atype, IdRole idRole), MuExp exp), JGenie jg){
    //if(unsetRec(v, "alabel") == unsetRec(exp, "alabel")) return "";
    jtype = atype2javatype(atype);
    if(exp == muNoValue()){
        return jg.isRef(v) ? "ValueRef\<<jtype>\> <varName(v, jg)> = new ValueRef\<<jtype>\>();\n"
                           : "<jtype> <varName(v, jg)> = null;\n";  
    } else {    
        return jg.isRef(v) ? "final ValueRef\<<jtype>\> <varName(v, jg)> = <newValueRef(name, atype, exp, jg)>;\n" //new ValueRef\<<jtype>\>(<transWithCast(atype,exp,jg)>);\n"
                            : "<jtype> <varName(v, jg)> = <transWithCast(atype, exp, jg)>;\n";  
                           //: "<jtype> <varName(v, jg)> = (<jtype>)<parens(trans(exp, jg))>;\n";  
    }
}

JCode trans(muVarInit(v: muTmpIValue(str name, str fuid, AType atype), MuExp exp), JGenie jg){
    jtype = atype2javatype(atype);
    return jg.isRef(v) ? "final ValueRef\<<jtype>\> <name> = <newValueRef(name, atype, exp, jg)>;\n" //new ValueRef\<<jtype>\>(<transWithCast(atype,exp,jg)>);\n"
                       : "<jtype> <name> = (<jtype>)<parens(trans(exp, jg))>;\n";
}

map[NativeKind, tuple[str,str]] native2ref =
    (nativeInt()            : <"int", "IntRef">,
     nativeBool()           : <"boolean", "BoolRef">,
     nativeListWriter()     : <"IListWriter", "IListWriterRef">,
     nativeSetWriter()      : <"ISetWriter", "ISetWriterRef">,
     nativeMapWriter()      : <"IMapWriter", "IMapWriterRef">,
     nativeMatcher()        : <"Matcher", "MatcherRef">,
     nativeStrWriter()      : <"StringWriter", "StringWriterRef">,
     nativeDescendantIterator()
                            : <"DescendantMatchIterator", "DescendantMatchIteratorRef">,
     nativeTemplate()       : <"Template", "TemplateRef">,
     nativeException()      : <"Exception", "ExceptionRef">,
     nativeGuardedIValue()  : <"GuardedIValue", "GuardedIValueRef">,
     nativeITree()          : <"ITree", "ITreeRef">
     );

JCode trans(muVarInit(var: muTmpNative(str name, str fuid, NativeKind nkind), MuExp exp), JGenie jg){
    rhs = muCon(value v) := exp ? "<v>" : trans(exp, jg);   // TODO does not work for all constants, e.g. datetime
    if(nkind == nativeGuardedIValue() && !producesNativeGuardedIValue(exp)){
        rhs = "new GuardedIValue(<rhs>)";
    }
    <base, ref> = native2ref[nkind];
    return jg.isRef(var) ? "final <ref> <name> = new <ref>(<rhs>);\n"
                         : "<base> <name> = <rhs>;\n";
}

// --- muConInit --------------------------------------------------------------

 JCode trans(muConInit(v:muVar(str name, str fuid, int pos, AType atype, IdRole idRole), MuExp exp), JGenie jg){
    jtype = atype2javatype(atype);
    if(jg.isRef(v)){  
        return "final ValueRef\<<jtype>\> <varName(v, jg)> = <newValueRef(name, atype, exp, jg)>;\n"; //new ValueRef\<<jtype>\>(<transWithCast(atype,exp,jg)>);\n";
    }
    return "<jtype> <varName(v, jg)> = <transWithCast(atype, exp, jg)>;\n";
}
    
JCode trans(muConInit(v:muTmpIValue(str name, str fuid, AType atype), MuExp exp), JGenie jg){
    jtype = atype2javatype(atype);
    return jg.isRef(v) ? "final ValueRef\<<jtype>\> <varName(v,jg)> = <newValueRef(name, atype, exp, jg)>;\n" //new ValueRef\<<jtype>\>(<transWithCast(atype,exp,jg)>);\n"
                       : "final <jtype> <name> = <transWithCast(atype, exp, jg)>;\n";
}

JCode trans(muConInit(var:muTmpNative(str name, str fuid, NativeKind nkind), MuExp exp), JGenie jg){
    rhs = muCon(value v) := exp ? "<v>" : trans(exp, jg);
    <base, ref> = native2ref[nkind];
    return jg.isRef(var) ? "final <ref> <varName(var, jg)> = new <ref>(<rhs>);\n"
                         : "final <base> <name> = (<base>)<rhs>;\n";
}

str transWithCast(AType atype, con:muCon(c), JGenie jg) = trans(con, jg);

str transWithCast(AType atype1, v:muVar(str name, str fuid, int pos, AType atype2, IdRole idRole), JGenie jg){
    //v = unsetRec(v, "alabel");
    return jg.isRef(v) ? trans(v, jg) : /*(equivalent(atype1, atype2) ? trans(v,jg) :*/ "((<atype2javatype(atype1)>)<trans(v,jg)>)";
}

str transWithCast(AType atype, kwp:muKwpActuals(_), JGenie jg) = trans(kwp, jg);

default str transWithCast(AType atype, MuExp exp, JGenie jg) {
    code = trans(exp, jg);
    if(producesNativeBool(exp)){
        return "$VF.bool(<code>)";
    }
    if(producesNativeInt(exp)){
        return "$VF.integer(<code>)";
    }
    if(producesNativeStr(exp)){
        return "$VF.string(<code>)";
    }
    
    if(producesFunctionInstance(code)){
        return code;
    }
      
    expType = getType(exp);
    isequivalent = false;
    try {
         isequivalent = equivalent(expType,atype) && isEmpty(collectRascalTypeParams(expType));
    } catch _ : /* ignore failure */;
    
    //return isequivalent ? code : "((<atype2javatype(atype)>)<parens(code)>)";
    return "((<atype2javatype(atype)>)<parens(code)>)";
}

bool producesFunctionInstance(str code)
    = startsWith(code, "new TypedFunctionInstance");

// ---- muAssign --------------------------------------------------------------

JCode trans(muAssign(v:muVar(str name, str fuid, int pos, AType atype, IdRole idRole), MuExp exp), JGenie jg){
    if(jg.isRef(v)){
        if(muPrim(op1, assignable_type, argtypes:[assignable_type, rhs_type],  [v, rhs], src) := exp, muCon(_) !:= rhs){
            // Case x += exp, where x is an external variable. Ensure proper execution order by executing exp first.
            naux += 1;
            aux = "$aux_<naux>";
            rhs_code = transPrim(op1, assignable_type, argtypes, ["<varName(v, jg)>.getValue()", aux], jg);
            return "<atype2javatype(rhs_type)> <aux> = <transWithCast(rhs_type, rhs, jg)>;
                   '<varName(v, jg)>.setValue(<rhs_code>);\n";
        }
        return "<varName(v, jg)>.setValue(<transWithCast(atype, exp, jg)>);\n";
    } else {
        return "<varName(v, jg)> = <transWithCast(atype, exp, jg)>;\n";
    }

    //return jg.isRef(v) ? "<varName(v, jg)>.setValue(<transWithCast(atype, exp, jg)>);\n"
    //                   : "<varName(v, jg)> = <transWithCast(atype, exp, jg)>;\n";
}
    
JCode trans(muAssign(v:muTmpIValue(str name, str fuid, AType atype), MuExp exp), JGenie jg)
    = jg.isRef(v) ? "<name>.setValue(<trans(exp, jg)>);\n"
                  : "<name> = <transWithCast(atype, exp, jg)>;\n";

JCode trans(muAssign(v:muTmpNative(str name, str fuid, NativeKind nkind), MuExp exp), JGenie jg)
    = jg.isRef(v) ? "<name>.setValue(<trans2Native(exp, nkind, jg)>);\n"
                  : "<name> = <trans2Native(exp, nkind, jg)>;\n";

// muGetAnno
JCode trans(muGetAnno(MuExp exp, AType resultType, str annoName), JGenie jg){
    if(annoName == "loc") annoName = "src";// TODO: remove when @\loc is gone
    return "$annotation_get(((INode)<trans(exp, jg)>),\"<annoName>\")";
}
// muGuardedGetAnno
JCode trans(muGuardedGetAnno(MuExp exp, AType resultType, str annoName), JGenie jg){
    if(annoName == "loc") annoName = "src";// TODO: remove when @\loc is gone
    return "$guarded_annotation_get(((INode)<trans(exp, jg)>),\"<annoName>\")";
}    

// ---- muOCall --------------------------------------------------------------

bool anyKwpFormalsInScope(JGenie jg)
    = !isEmpty(jg.collectKwpDefaults(jg.getFunction()));

list[JCode] getActuals(list[AType] argTypes, list[MuExp] largs, JGenie jg) {
    res = [ i < size(argTypes) ? transWithCast(argTypes[i], largs[i], jg) : trans(largs[i], jg) | i <- index(largs) ];
    return res;
}
    
JCode getKwpActuals(list[Keyword] kwFormals, lrel[str name, MuExp exp] kwpActuals, JGenie jg, bool isConstructor = false){
    noKwFormals = !anyKwpFormalsInScope(jg); // isEmpty(jg.getFunction().ftype.kwFormals);
    shouldNotExtend = noKwFormals || isConstructor;
    if(isEmpty(kwpActuals)) return shouldNotExtend ? "Util.kwpMap()" : "$kwpActuals";
    
    kwpActualsCode = intercalate(", ",  [ *["\"<unescape(key)>\"", trans(exp, jg)] | <str key,  MuExp exp> <- kwpActuals]);
    if(shouldNotExtend) return "Util.kwpMap(<kwpActualsCode>)";
    
    declaredKwps = jg.collectDeclaredKwps(jg.getFunction());
    redeclaredKwps = declaredKwps & [tp.alabel | <tp, _> <- kwFormals];
    kwpActualsPossiblyRedeclared = "$kwpActuals";
    if(!isEmpty(redeclaredKwps))
        kwpActualsPossiblyRedeclared =  "Util.kwpMapRemoveRedeclared($kwpActuals, <intercalate(", ", ["\"<asJavaName(key)>\"" | str key <- redeclaredKwps ])>)";
    return "Util.kwpMapExtend(<kwpActualsPossiblyRedeclared>, <kwpActualsCode>)";
}

tuple[list[JCode], list[JCode]] getPositionalAndKeywordActuals(funType:afunc(AType ret, list[AType] formals, list[Keyword] kwFormals, varArgs=varArgs), list[MuExp] actuals, lrel[str name, MuExp exp] kwpActuals, JGenie jg){
    resulting_actuals = [];
    if(varArgs){
        n = size(formals) - 1;
        resulting_actuals = getActuals(formals[0..n], actuals[0..n], jg);
        varElemType = getElementType(funType.formals[-1]);
        vargs = [ trans(e, jg) | e <- actuals[n .. ] ];
        if(isEmpty(vargs)){
            resulting_actuals += "$VF.list()";
        } else {
            lastArgIsList = size(actuals) == size(formals) && isListType(getType(actuals[-1]));
            resulting_actuals += lastArgIsList ? vargs : "$VF.list(<intercalate(",", vargs)>)";
        }
    } else {
        resulting_actuals = getActuals(formals, actuals, jg);
    }
   
    if(isEmpty(kwFormals)){
        return <resulting_actuals, []>;
    } else {
        return <resulting_actuals, [getKwpActuals(kwFormals, kwpActuals, jg)]>;
    }
}

tuple[list[JCode], list[JCode]] getPositionalAndKeywordActuals(consType:acons(AType adt, list[AType] fields, list[Keyword] kwFields), list[MuExp] actuals, lrel[str name, MuExp exp] kwpActuals, JGenie jg){
    resulting_actuals = getActuals(fields, actuals, jg);
    
     if(isEmpty(consType.kwFields) && !jg.hasCommonKeywordFields(consType)){
        return <resulting_actuals, []>;
     } else {
            return <resulting_actuals, [getKwpActuals(kwFields, kwpActuals, jg, isConstructor=true)]>;
    }
}

JCode trans(muOCall(MuExp fun, AType ftype, list[MuExp] largs, lrel[str kwpName, MuExp exp] kwargs, src), JGenie jg){
//  println("muOCall((<fun>, <ftype>, ..., <src>");
    argTypes = getFunctionOrConstructorArgumentTypes(ftype);
    actuals = getActuals(argTypes, largs, jg);
    cst = (getResult(ftype) == avoid()) ? "" : "(<atype2javatype(getResult(ftype))>)";
    if(muOFun(list[loc] srcs, AType _) := fun){   
        kwactuals = hasKeywordParameters(ftype) ? getKwpActuals(ftype has kwFields ? ftype.kwFields : getFunctionOrConstructorKeywords(ftype), kwargs, jg) : [];
        externalRefs = { *jg.getExternalRefs(fsrc) | fsrc <- srcs };
        externals = [ varName(var, jg) | var <- sort(externalRefs)/*, var notin ftype.formals*//*, jtype := atype2javatype(var.atype)*/];
        return "<jg.getAccessor(srcs)>(<intercalate(", ", actuals + kwactuals + externals)>)";
    }
    
    if(muFun(loc uid, _) := fun){
        <actuals, kwactuals> = getPositionalAndKeywordActuals(ftype, largs, kwargs, jg);
        externalRefs = jg.getExternalRefs(uid);
        externals = [ varName(var, jg) | var <- sort(externalRefs)/*, var notin fun.formals*/];
    
        if(isContainedIn(uid, jg.getModuleLoc())){
            fn = loc2muFunction[uid];
            kwactuals1 = jg.collectKwpFormals(fn);
            if(isEmpty(kwactuals) && !isEmpty(kwactuals1)) kwactuals = ["$kwpActuals"];
            externalRefs -= fn.formals;
            externals = [ varName(var, jg) | var <- sort(externalRefs) ];
            
            //externals = [ var.fuid == fn.scopeIn ? newValueRef(var.atype, var, jg) /*"new ValueRef\<<atype2javatype(var.atype)>\>(<var.name>_<var.pos>)"*/ : varName(var, jg) | var <- sort(externalRefs) ];
            arg_list = "(<intercalate(", ", actuals + kwactuals + externals)>)"; 
            
            fun_name = isEmpty(fn.scopeIn) ? "$me.<getFunctionName(fn)>" : "<fn.scopeIn>_<fn.name>";
            //fun_name = isEmpty(fn.scopeIn) ? "$me.<getFunctionName(fn)>" : "<fn.scopeIn>_<fn.name><isClosureName(fn.name) ? "" : "_<fn.src.begin.line>A<fn.src.offset>">";
            
            result = "<asJavaName(fun_name)><arg_list>";
            
            return result; //isEmpty(cst) ? result : "<cst><result>";
       } else {
            return "<jg.getAccessor([uid])>(<intercalate(", ", actuals + kwactuals + externals)>)";
       }
    }
    
    if(muConstr(AType ctype) := fun){
        <actuals, kwactuals> = getPositionalAndKeywordActuals(ctype, largs, kwargs, jg);
        return makeConstructorCall(ctype, actuals, kwactuals, jg);        
    }
    //if(muCon(str s) := fun){
    //    return "$VF.node(<intercalate(", ", actuals)>)";
    //}
    
    //cst = (getResult(ftype) == avoid()) ? "" : "(<atype2javatype(getResult(ftype))>)";
    if(muComposedFun(MuExp left, MuExp right, AType _, AType _, AType resultType) := fun){
        rightCall = "<trans(right, jg)>.typedCall(<intercalate(", ", actuals)>)";
        cst = "(<atype2javatype(resultType)>)";
        return "(<cst><trans(left, jg)>).typedCall(<rightCall>)";
    }
    cst ="(<atype2javatype(ftype)>)";
    fun_code = trans(fun, jg);
    typed_call = ".typedCall(<intercalate(", ", actuals)>)";
    call_code = isEmpty(cst) ? "<fun_code><typed_call>" : "(<cst><fun_code>)<typed_call>";
    
    return call_code;
    //return "(<cst><trans(fun, jg)>).typedCall(<intercalate(", ", actuals)>)";
}

JCode trans(muReturnFirstSucceeds(list[str] formals, list[MuExp] exps), JGenie jg){
    return 
    "<for(exp <- exps){>
    'try {
    '   <trans(exp, jg)>
    '} catch (Throw e) {
    '   if(!((IConstructor)e.getException()).getName().equals(\"CallFailed\")) throw e;
    '}<}>
    'throw RuntimeExceptionFactory.callFailed($VF.list(<intercalate(", ", formals)>));
    ";
}

// ---- muGetKwField ------------------------------------------------------

str prefix(str moduleName, JGenie jg){
    res = (isEmpty(moduleName) || moduleName == jg.getModuleName()) ? "" : (module2field(moduleName) + ".");
    return res;
}
JCode trans(muGetKwField(AType resultType,  adtType:aadt(_,_,_), MuExp cons, str fieldName, str moduleName), JGenie jg){
    //adtName = adtType.adtName;
    adtName = isEmpty(adtType.parameters) ? adtType.adtName : "<adtType.adtName>_<intercalate("_", [atype2idpart(p) | p <- adtType.parameters])>";
    if(asubtype(adtType, treeType)){
        if(fieldName == "loc") fieldName = "src"; // TODO: remove when @\loc is gone
        adtName = "Tree";
    }
    return "<prefix(moduleName,jg)>$getkw_<adtName>_<asJavaName(fieldName,completeId=false)>(<transWithCast(adtType, cons, jg)>)";
}

JCode trans(muGetKwField(AType resultType,  consType:acons(AType adt, list[AType] fields, list[Keyword] kwFields), MuExp cons, str fieldName, str moduleName), JGenie jg){
    //adtName = adt.adtName;
    //adtName = isEmpty(adt.parameters) ? adt.adtName : "<adt.adtName>_<intercalate("_", [atype2idpart(p) | p <- adt.parameters])>";
    adtName = getUniqueADTName(adt);
    if(asubtype(adt, treeType)){
        if(fieldName == "loc") fieldName = "src";   // TODO: remove when @\loc is gone
        adtName = "Tree";
    }
    isConsKwField = fieldName in {kwf.fieldType.alabel | kwf <- kwFields};
    return isConsKwField ? "<prefix(moduleName,jg)>$getkw_<adtName>_<asJavaName(consType.alabel,completeId=false)>_<asJavaName(fieldName,completeId=false)>(<transWithCast(consType, cons, jg)>)"
                         : "<prefix(moduleName,jg)>$getkw_<adtName>_<asJavaName(fieldName,completeId=false)>(<transWithCast(consType, cons, jg)>)";
}
// ---- muGetField ---------------------------------------------------------

JCode trans(muGetField(AType resultType, aloc(), MuExp exp, str fieldName), JGenie jg)
    = castArg(resultType, "$aloc_get_field(<transWithCast(aloc(),exp,jg)>, \"<fieldName>\")");

JCode trans(muGetField(AType resultType, adatetime(), MuExp exp, str fieldName), JGenie jg)
    = castArg(resultType, "$adatetime_get_field(<transWithCast(adatetime(),exp,jg)>, \"<fieldName>\")");
 
JCode trans(muGetField(AType resultType, anode(_), MuExp exp, str fieldName), JGenie jg)
    = castArg(resultType, "$anode_get_field(<transWithCast(anode([]),exp,jg)>, \"<unescape(fieldName)>\")");
    
JCode trans(muGetField(AType resultType, tup:atuple(_), MuExp exp, str fieldName), JGenie jg){
    n = getTupleFieldIndex(tup, fieldName);
    return castArg(resultType, "$atuple_get_field_by_index(<transWithCast(tup,exp,jg)>, <n>)");
   //return castArg(resultType, "$atuple_get_field(<transWithCast(tup,exp,jg)>, \"<unescape(fieldName)>\")");
}
  
JCode trans(muGetField(AType resultType, areified(AType atype), MuExp exp, str fieldName), JGenie jg)
    = castArg(resultType, "$areified_get_field(<trans(exp,jg)>, \"<unescape(fieldName)>\")");

//JCode trans(muGetField(AType resultType, adt:aadt(adtName,_,_), MuExp exp, str fieldName), JGenie jg) {
//    return asubtype(adt, treeType) && !isNonTerminalType(adt) 
//            ? "$get_Tree_<asJavaName(fieldName)>(<transWithCast(adt,exp,jg)>)"
//            : "org.rascalmpl.values.parsetrees.TreeAdapter.getLabeledField((org.rascalmpl.values.parsetrees.ITree) <trans(cons, jg)>, \"<fieldName>\").tree";
//            //: castArg(resultType, "$aadt_get_field(<transWithCast(adt,exp,jg)>, \"<asJavaName(fieldName)>\")");
//}
          
default JCode trans(mg: muGetField(AType resultType, AType consType, MuExp cons, str fieldName), JGenie jg){
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := consType){
        ovl = [ atype | <loc l, constructorId(), AType atype> <- overloads, hasField(atype, fieldName) ];
        if(size(ovl) != 1) throw "muGetField: overloaded constructor type: <ovl>";
        consType = ovl[0];
    }
    qFieldName = "\"<fieldName>\"";
    
    if(isStartNonTerminalType(consType) && fieldName == "top"){
        return castArg(resultType, "org.rascalmpl.values.parsetrees.TreeAdapter.getLabeledField((org.rascalmpl.values.parsetrees.ITree) <trans(cons, jg)>, <qFieldName>).tree");
    }
    
    consType = isStartNonTerminalType(consType) ? getStartNonTerminalType(consType) : consType;
    
    if(isNonTerminalType(consType)){
        //return castArg(resultType, "org.rascalmpl.values.parsetrees.TreeAdapter.getLabeledField((org.rascalmpl.values.parsetrees.ITree) <trans(cons, jg)>, <qFieldName>).tree");
        return castArg(resultType,  "$aadt_get_field(<transWithCast(consType, cons, jg)>, <qFieldName>)");
    } else if(isIterType(consType)){
        //return castArg(resultType,  "$aadt_get_field(<trans(cons, jg)>, <qFieldName>)");
        return castArg(resultType, "org.rascalmpl.values.parsetrees.TreeAdapter.getLabeledField((org.rascalmpl.values.parsetrees.ITree) <trans(cons, jg)>, <qFieldName>).tree");
    } else if(isTerminalType(consType)){
        return castArg(resultType,  "$aadt_get_field(<transWithCast(consType, cons, jg)>, <qFieldName>)");
        //return castArg(resultType, "org.rascalmpl.values.parsetrees.TreeAdapter.getLabeledField((org.rascalmpl.values.parsetrees.ITree) <trans(cons, jg)>, <qFieldName>).tree");
       
   } else if(isRegExpType(consType)){
        return castArg(resultType,  "$aadt_get_field(<transWithCast(consType, cons, jg)>, <qFieldName>)");
        //return castArg(resultType, "org.rascalmpl.values.parsetrees.TreeAdapter.getLabeledField((org.rascalmpl.values.parsetrees.ITree) <trans(cons, jg)>, <qFieldName>).tree");
    } else 
    if(isConstructorType(consType) || isADTType(consType)){
        return castArg(resultType, "$aadt_get_field(<transWithCast(consType, cons, jg)>, <qFieldName>)");
    } else {
        throw mg;
        //isConsKwField = fieldName in {kwf.fieldType.alabel | kwf <- consType.kwFields};
        //return isConsKwField ? "$getkw_<consType.adt.adtName>_<asJavaName(consType.alabel)>_<asJavaName(fieldName)>(<base>)"
        //                     : "$get_<consType.adt.adtName>_<asJavaName(fieldName)>(<base>)";
    }
}
 
 // ---- muGuardedGetField -------------------------------------------------
 
 JCode trans(muGuardedGetField(AType resultType, al:aloc(), MuExp exp, str fieldName), JGenie jg)
    = "$guarded_aloc_get_field(<transWithCast(al,exp,jg)>, \"<fieldName>\")";

JCode trans(muGuardedGetField(AType resultType, ad:adatetime(), MuExp exp, str fieldName), JGenie jg)
    = "$guarded_adatetime_get_field(<transWithCast(ad, exp,jg)>, \"<fieldName>\")";
    
JCode trans(muGuardedGetField(AType resultType, an:anode(_), MuExp exp, str fieldName), JGenie jg)
    = "$guarded_anode_get_field(<transWithCast(an, exp, jg)>, \"<asJavaName(fieldName)>\")";
    
JCode trans(muGuardedGetField(AType resultType, at:atuple(_), MuExp exp, str fieldName), JGenie jg)
    = "$guarded_atuple_get_field(<transWithCast(at, exp,jg)>, \"<asJavaName(fieldName)>\")";

JCode trans(muGuardedGetField(AType resultType, adtType: aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole), MuExp exp, str fieldName), JGenie jg)
    = "$guarded_aadt_get_field(<transWithCast(adtType, exp,jg)>,  \"<asJavaName(fieldName)>\")";
 
default JCode trans(muGuardedGetField(AType resultType, consType:acons(AType adt, list[AType] fields, list[Keyword] kwFields), MuExp cons, str fieldName), JGenie jg){
    base = transWithCast(consType, cons, jg);
    qFieldName = "\"<asJavaName(fieldName)>\"";
    for(field <- fields){
        if(fieldName == field.alabel){
        
            return "((<atype2javatype(field)>)<base>.get(<qFieldName>))";
        }
    }
   
    //for(<AType kwType, Expression exp> <- kwFields){
    //    if(fieldName == kwType.alabel){
    //        expCode = trans(exp, jg);
    //        if(muCon(_) := expCode){
    //            return "<base>.asWithKeywordParameters().hasParameter(<qFieldName>) ? <castArg(kwType, "<base>.asWithKeywordParameters().getParameter(<qFieldName>)")> : <expCode>";
    //        } else {
    //            return castArg(kwType, "<base>.asWithKeywordParameters().getParameter(<qFieldName>)");
    //        }
    //    }
    //}
    throw "muGuardedGetField <resultType>, <consType>, <fieldName>";
 }
// ---- muGuardedGetKwField -------------------------------------------------

JCode trans(muGuardedGetKwField(AType resultType, aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole), MuExp exp, str fieldName, str moduleName), JGenie jg)
    = "$guarded_aadt_get_field(<trans(exp,jg)>,  \"<asJavaName(fieldName)>\")";

JCode trans(muGuardedGetKwField(AType resultType, consType:acons(AType adt, list[AType] fields, list[Keyword] kwFields), MuExp cons, str fieldName, str moduleName), JGenie jg){
    base = trans(cons, jg);
    qFieldName = "\"<unescape(fieldName)>\"";
    for(<AType kwType, Expression exp> <- kwFields){
        if(fieldName == kwType.alabel){
            expCode = translate(exp);
            if(muCon(_) := expCode){
                return "<base>.asWithKeywordParameters().hasParameter(<qFieldName>) ? <castArg(kwType, "<base>.asWithKeywordParameters().getParameter(<qFieldName>)")> : <expCode>";
            } else {
                return castArg(kwType, "<base>.asWithKeywordParameters().getParameter(<qFieldName>)");
            }
        }
    }
    throw "muGuardedGetKwField <resultType>, <consType>, <fieldName>";
}
 
// ---- muSetField ------------------------------------------------------------

JCode trans(muSetField(AType resultType, aloc(), MuExp baseExp, str fieldName, MuExp repl), JGenie jg)
    = "$aloc_field_update(\"<fieldName>\", <trans(repl, jg)>, <transWithCast(aloc(), baseExp, jg)>)";

JCode trans(muSetField(AType resultType, adatetime(), MuExp baseExp, str fieldName, MuExp repl), JGenie jg)
    = "$adatetime_field_update(\"<fieldName>\", <trans(repl, jg)>, <transWithCast(adatetime(), baseExp, jg)>)";
    
JCode trans(muSetField(AType resultType, anode(_), MuExp baseExp, str fieldName, MuExp repl), JGenie jg)
    = "$anode_field_update(\"<asJavaName(fieldName)>\", <trans(repl, jg)>, <transWithCast(anode([]), baseExp, jg)>)";    

JCode trans(muSetField(AType resultType, AType baseType, MuExp baseExp, int fieldIdentity, MuExp repl), JGenie jg)
    = "$atuple_update(<fieldIdentity>,  <trans(repl, jg)>, <trans(baseExp, jg)>)" when isTupleType(baseType);
 
JCode trans(muSetField(AType resultType, a:aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole), MuExp baseExp, str fieldName, MuExp repl), JGenie jg)
    = castArg(a, "$aadt_field_update(\"<asJavaName(fieldName)>\", <trans(repl, jg)>, <transWithCast(a, baseExp,jg)>)");

default JCode trans(muSetField(AType resultType, AType baseType, MuExp baseExp, str fieldName, MuExp repl), JGenie jg){
    return asubtype(baseType, treeType) ? castArg(baseType, "$aadt_field_update(\"<asJavaName(fieldName)>\", <trans(repl, jg)>, (IConstructor)<trans(baseExp,jg)>)")
                                        : "<transWithCast(baseType, baseExp, jg)>.set(\"<asJavaName(fieldName)>\", <trans(repl, jg)>)"; /* order? */
}      

// ---- muPrim -----------------------------------------------------------

JCode trans(muPrim(str name, AType result, list[AType] details, list[MuExp] exps, loc src), JGenie jg){
    actuals = transPrimArgs(name, result, details, exps, jg);
    return transPrim(name, result, details, actuals, jg);
}

JCode trans(muCallJava(str name, str class, AType funType, list[MuExp] largs, str enclosingFun), JGenie jg){
    jg.addImportedLibrary(class);
  
    actuals = [ trans(arg, jg) | arg <- largs ];
    
    if(!isEmpty(funType.kwFormals)){
        kwpDefaultsName = jg.getKwpDefaultsName();
        kwpActuals = "$kwpActuals"; 
        for(kwFormal <- funType.kwFormals){
            escapedKwName = unescape(kwFormal.fieldType.alabel);
            actuals += "(<atype2javatype(kwFormal.fieldType)>)(<kwpActuals>.containsKey(\"<escapedKwName>\") ? <kwpActuals>.get(\"<escapedKwName>\") : <kwpDefaultsName>.get(\"<escapedKwName>\"))";
        }
    }
    
    code = "<asBaseClassName(class)>.<name>(<intercalate(", ", actuals)>)";
    return funType.ret == avoid() ? code : "(<atype2javatype(funType.ret)>)<code>";
}

// ---- muReturn0 -------------------------------------------------------------

JCode trans(muReturn0(), JGenie jg){
    return "return;";
}

JCode trans(muReturn0FromVisit(), JGenie jg)
    = "$traversalState.setLeavingVisit(true);
      'return;\n";

str semi(str code){
    if(/\)$/ := code) return "<code>;" ;
    if(/[;}][\ \t]*(\/\/.*$)?[\ \t \n]*/ := code) return code;
    if(/\/\*.*\*\// := code) return code;
    return "<code>;";
    
    
    //return /\)$/ := code ? "<code>;" 
    //                     : /[;}][\ \t]*(\/\/.*$)?[\ \t \n]*/ := code ? code 
    //                                                                 : "<code>;";
}

str semi_nl(str code)
    = "<semi(code)>\n";
 
JCode trans2Void(MuExp exp, JGenie jg)
   = "/* void:  <exp> */"
   when getName(exp) in {"muCon", "muVar", "muTmp"};

JCode trans2Void(MuExp exp, JGenie jg)
   =  semi_nl(trans(exp, jg))
   when getName(exp) in {"muOCall", "muCallJava", "muReturn0", "muReturn1"};
   
JCode trans2Void(muBlock(list[MuExp] exps), JGenie jg)
    = "<for(exp <- exps){><trans2Void(exp, jg)><}>";
    
JCode trans2Void(muValueBlock(AType t, list[MuExp] exps), JGenie jg)
    = "<for(exp <- exps){><trans2Void(exp, jg)><}>";


JCode trans2Void(muIfExp(MuExp cond, muBlock([]), MuExp elsePart), JGenie jg){
   return 
     "if(!<trans2NativeBool(cond, jg)>){
     '  <trans2Void(elsePart, jg)>
     '}\n";
}
 
 JCode trans2Void(muIfExp(MuExp cond, MuExp thenPart, muBlock([])), JGenie jg)
   = "if(<trans2NativeBool(cond, jg)>){
     '  <trans2Void(thenPart, jg)>
     '}\n";

default JCode trans2Void(muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart), JGenie jg)
   = "if(<trans2NativeBool(cond, jg)>){
     '  <trans2Void(thenPart, jg)>
     '} else {
     '  <trans2Void(elsePart, jg)>
     '}\n";
 
default JCode trans2Void(MuExp exp, JGenie jg){
    return semi(trans(exp, jg));
}  

// ---- muNot -----------------------------------------------------------------

JCode trans(muNot(MuExp exp), JGenie jg){
    actuals = transPrimArgs("not", abool(), [abool()], [exp], jg);
    return transPrim("not", abool(), [abool()], actuals, jg);
} 
// ---- muReturn1 -------------------------------------------------------------

JCode trans2IValue(MuExp exp, JGenie jg){
    if(producesNativeBool(exp))
        return trans2IBool(exp, jg);
    if(producesNativeInt(exp))
        return trans2IInteger(exp, jg);
    return trans(exp, jg);
}

JCode trans(r: muReturn1(AType t, muBlock([])), JGenie jg){
    return "/*<r>*/"; //"return null;";
}

JCode trans(muReturn1(AType result, v:muVisit(_,_,_,_,_)), JGenie jg){
    return "IValue $visitResult = <trans(v, jg)>;
           'return (<atype2javatype(result)>)$visitResult;";
}

default JCode trans(muReturn1(AType result, MuExp exp), JGenie jg){
    if(result == avoid()){
        return "<trans(exp, jg)>; 
               'return;\n";
    }
    return "return <transWithCast(result, exp, jg)>;";
}

JCode trans(muReturn1FromVisit(AType result, MuExp exp), JGenie jg)
    = "$traversalState.setLeavingVisit(true);
      'return <transWithCast(result, exp, jg)>;\n";

//          | muFilterReturn()                                    // Return for filter statement

// ---- muKwpActuals ----------------------------------------------------------

JCode trans(muKwpActuals(lrel[str name, MuExp exp] kwpActuals), JGenie jg){
    anyKwp = anyKwpFormalsInScope(jg);
    if(isEmpty(kwpActuals)) return anyKwp ? "$kwpActuals" : "Collections.emptyMap()";
    return "Util.kwpMap<anyKwp ? "Extend" : "">(<anyKwp ? "$kwpActuals," : ""><intercalate(", ",  [ *["\"<key>\"", trans(exp, jg)] | <str key,  MuExp exp> <- kwpActuals])>)";
}

// ---- muKwpMap --------------------------------------------------------------

JCode trans(muKwpMap(lrel[str kwName, AType atype, MuExp defaultExp] kwpDefaults), JGenie jg){
    anyKwp = anyKwpFormalsInScope(jg);
    kwpDefaultsName = jg.getKwpDefaultsName();
    kwpActuals = "$kwpActuals"; 
    return "
           '    <kwpActuals>.isEmpty() ? <kwpDefaultsName>
           '                           : Util.kwpMap<anyKwp ? "Extend" : "">(<anyKwp ? "$kwpActuals, " : ""><for(<str key, AType atype, MuExp exp> <- kwpDefaults, muCon(_) !:= exp){>\"<key>\", <kwpActuals>.containsKey(\"<unescape(key)>\") ? ((<atype2javatype(atype)>) <kwpActuals>.get(\"<unescape(key)>\")) : <transWithCast(atype,exp,jg)>)<}>)";
}

// ---- muVarKwp --------------------------------------------------------------

JCode trans(var:muVarKwp(str name, str fuid, AType atype),  JGenie jg)
    = "((<atype2javatype(atype)>) ($kwpActuals.containsKey(\"<unescape(name)>\") ? $kwpActuals.get(\"<unescape(name)>\") : <jg.getKwpDefaultsName()>.get(\"<unescape(name)>\")))";


JCode trans(muIsVarKwpDefined(muVarKwp(str name, str fuid, AType atype)),  JGenie jg)
    = "$kwpActuals.containsKey(\"<unescape(name)>\")";
    
JCode trans(muAssign(muVarKwp(str name, str fuid, AType atype), MuExp exp), JGenie jg)
    = "$kwpActuals.put(\"<unescape(name)>\", <trans(exp, jg)>);\n";

JCode trans(muIsKwpConstructorDefined(MuExp exp, str kwpName), JGenie jg)
    = "<trans(exp, jg)>.asWithKeywordParameters().hasParameter(\"<unescape(kwpName)>\")";

JCode trans(muHasKwp(MuExp exp, str kwName), JGenie jg)
    = "<trans(exp, jg)>.asWithKeywordParameters().hasParameter(\"<unescape(kwName)>\")";

JCode trans(muGetKwp(MuExp exp, AType atype, str kwpName), JGenie jg){
   if(acons(AType adtType, list[AType] _, list[Keyword] _) := atype){
        adtName = getUniqueADTName(adtType);
        return "$getkw_<adtName>_<asJavaName(kwpName,completeId=false)>(<transWithCast(adtType, exp, jg)>)";
   } else if(anode(_) := atype){
       return "<trans(exp, jg)>.asWithKeywordParameters().getParameter(\"<unescape(kwpName)>\")";
   } else if(a: aadt(str _, list[AType] _, SyntaxRole _) := atype){
       adtName = getUniqueADTName(a);
       return "<jg.getATypeAccessor(atype)>$getkw_<adtName>_<asJavaName(kwpName,completeId=false)>(<transWithCast(a, exp, jg)>)";
   } else {
        return "((<atype2javatype(atype)>)<trans(exp, jg)>.asWithKeywordParameters().getParameter(\"<unescape(kwpName)>\"))";
   }
   //throw "muGetKwp: <atype>, <kwpName>";
}
// ---- muGetKwFieldFromConstructor

JCode trans(muGetKwFieldFromConstructor(AType resultType, MuExp exp, str kwpName), JGenie jg)
    = "((<atype2javatype(resultType)>)<trans(exp, jg)>.asWithKeywordParameters().getParameter(\"<unescape(kwpName)>\"))";
 
// ---- muGetFieldFromConstructor

JCode trans(muGetFieldFromConstructor(AType resultType, AType consType, MuExp exp, str fieldName), JGenie jg){
    if(isSyntaxType(consType)){
        return "((<atype2javatype(resultType)>) org.rascalmpl.values.parsetrees.TreeAdapter.getLabeledField((ITree)<trans(exp, jg)>, \"<fieldName>\").tree)";
    } else {
        i = indexOf([fld.alabel | fld <- consType.fields], fieldName);
        return "((<atype2javatype(resultType)>)<trans(exp, jg)>.get(<i>))";
     }
 }

JCode trans(muInsert(AType atype, MuExp exp), JGenie jg)
    = "$traversalState.setMatchedAndChanged(true, true);
      'return <trans(exp, jg)>;\n";

// Assignment, If and While

JCode trans(muIfElse(MuExp cond, MuExp thenPart, MuExp elsePart), JGenie jg){
    return "if(<trans2NativeBool(cond, jg)>){
           '   <trans2Void(thenPart, jg)>
           '} else {
           '   <trans2Void(elsePart, jg)>
           '}";
}

JCode trans(muIfExp(MuExp cond, muBlock([]), MuExp elsePart), JGenie jg)
   = "if(!<trans2NativeBool(cond, jg)>){
     '  <trans2Void(elsePart, jg)>
     '}";
   
JCode trans(muIfExp(MuExp cond, MuExp thenPart, muBlock([])), JGenie jg)
   = "if(<trans2NativeBool(cond, jg)>){
     '  <trans2Void(thenPart, jg)>
     '}\n";

default JCode trans(exp: muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart), JGenie jg){
    if(!producesNativeBool(exp)){
      if(producesNativeBool(thenPart)) transThen = trans2IValue;
      if(producesNativeBool(elsePart)) transElse = trans2IValue;
      return "(<trans2NativeBool(cond, jg)> ? <trans2IValue(thenPart, jg)> : <trans2IValue(elsePart, jg)>)";
    }
    return "(<trans2NativeBool(cond, jg)> ? <trans(thenPart, jg)> : <trans(elsePart, jg)>)";
}

JCode trans(muIf(MuExp cond, MuExp thenPart), JGenie jg){
    tcond = trans2NativeBool(cond, jg);
    switch(tcond){
        case "true": return trans2Void(thenPart, jg);
        case "false": return "";
        default:
            return "if(<tcond>){
                   '   <trans2Void(thenPart, jg)>
                   '}\n";
    }
}

//JCode trans(muIfEqualOrAssign(MuExp var, MuExp other, MuExp body), JGenie jg){
//    return "if(<trans(var, jg)> == null){
//           '    <trans(muAssign(var, other), jg)>
//           '    <trans(body, jg)>
//           '} else {
//           '    <trans(muAssign(var, other), jg)>
//           '    if(<trans(var, jg)>.equals(<trans(other, jg)>)){
//           '       <trans(body, jg)>
//           '    }
//           '}\n";
//}

JCode trans(muWhileDo(str label, MuExp cond, MuExp body), JGenie jg){
    cond_code = trans2NativeBool(cond, jg);
    if(muExists(btscope, MuExp exp) := body){
       return
            "<isEmpty(label) ? "" : "<asJavaName(label)>:">
            '//<btscope>:
            '    while(<cond_code>){
            '    <btscope>: 
            '       do {
            '         <trans2Void(exp, jg)>
            '          //break <asJavaName(label)>; //muWhileDo
            '       } while(false);\n
            '    }\n";
    }
    return "<isEmpty(label) ? "" : "<asJavaName(label)>:">
           '    while(<cond_code>){
           '        <trans2Void(body, jg)>
           '    }\n";
}

JCode trans(muDoWhile(str label, MuExp body, MuExp cond), JGenie jg){
    return "<isEmpty(label) ? "" : "<asJavaName(label)>:">
           '    do{
           '        <trans2Void(body, jg)>
           '    } while(<trans2NativeBool(cond, jg)>);\n";
}

JCode trans(mw: muForAll(str btscope, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont), JGenie jg){
    iterCode = (muPrim("subsets", _, _, _, _) := iterable ||  muDescendantMatchIterator(_,_) := iterable) ? trans(iterable, jg) : transWithCast(iterType, iterable, jg);

    if(isIterType(iterType)){
        iterCode = "org.rascalmpl.values.parsetrees.TreeAdapter.getArgs(<iterCode>)";
    }
    return
        (muDescendantMatchIterator(_,_) := iterable) ? 
            ("<isEmpty(btscope) ? "" : "<btscope>:">
             'for(IValue <var.name> : <iterCode>){
             '    <trans2Void(body, jg)>
             '}
             '<trans2Void(falseCont, jg)>
             ")
        :
            ("<isEmpty(btscope) ? "" : "<btscope>:">
            'for(IValue <var.name>_for : <iterCode>){
            '    <atype2javatype(var.atype)> <var.name> = (<atype2javatype(var.atype)>) <var.name>_for;
            '    <trans2Void(body, jg)>
            '}
            '<trans2Void(falseCont, jg)>
            ");
   
}

JCode trans(mw: muForAny(str btscope, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont), JGenie jg){
    iterCode = (muPrim("subsets", _, _, _, _) := iterable ||  muDescendantMatchIterator(_,_) := iterable) ? trans(iterable, jg) : transWithCast(iterType, iterable, jg);

    if(isIterType(iterType)){
        iterCode = "org.rascalmpl.values.parsetrees.TreeAdapter.getArgs(<iterCode>)";
    }
    return
        (muDescendantMatchIterator(_,_) := iterable) ? 
            ("<isEmpty(btscope) ? "" : "<btscope>:">
             'for(IValue <var.name> : <iterCode>){
             '    <trans2Void(body, jg)>
             '}
             '<trans2Void(falseCont, jg)>
             ")
        :
            ("<isEmpty(btscope) ? "" : "<btscope>:">
            'for(IValue <var.name>_for : <iterCode>){
            '    <atype2javatype(var.atype)> <var.name> = (<atype2javatype(var.atype)>) <var.name>_for;
            '    <trans2Void(body, jg)>
            '}
            '<trans2Void(falseCont, jg)>
            ");
   
}

// muExists
    
JCode trans(e:muExists(btscope, muFail(btscope)), JGenie jg)
    = "/*muExists <e>*/";
    
JCode trans(muExists(btscope, asg:muAssign(_,_)), JGenie jg)
    = trans(asg, jg);
    
JCode trans(muExists(btscope,ret: muReturn1(_,_)), JGenie jg)
    = trans(ret, jg);

default JCode trans(muExists(btscope, MuExp exp), JGenie jg){
    return
      "/*muExists*/<isEmpty(btscope) ? "" : "<asJavaName(btscope)>:"> 
      '    do {
      '        <trans2Void(exp, jg)>
      '    } while(false);\n";
}

// muAll
    
JCode trans(e:muAll(btscope, muFail(btscope)), JGenie jg)
    = "/*muAll <e>*/";
    
JCode trans(muAll(btscope, asg:muAssign(_,_)), JGenie jg)
    = trans(asg, jg);
    
JCode trans(muAll(btscope,ret: muReturn1(_,_)), JGenie jg)
    = trans(ret, jg);

default JCode trans(muAll(btscope, MuExp exp), JGenie jg){
    return
      "/*muAll*/<isEmpty(btscope) ? "" : "<asJavaName(btscope)>:"> 
      '    do {
      '        <trans2Void(exp, jg)>
      '    } while(false);\n";
}

JCode trans(muSucceed(str label), JGenie jg)
    = "break <asJavaName(label)>; // muSucceed";

JCode trans(mf: muFail(str label), JGenie jg){
    if(startsWith(jg.getFunctionName(), label)){    // TODO:this is brittle, solve in JGenie
       // println(jg.getFunction().ftype);
        return jg.getFunction().ftype.ret == avoid() ? "throw new FailReturnFromVoidException();"
                                                     : "return null;";
    }
    //if(/^CASE[0-9]+$/ := label){
    //    return "break <label>;";
    //}
    return "continue <asJavaName(label)>;" + (mf.comment? ? "/*<mf.comment>*/" : "");   
}
    
JCode trans(muBreak(str label), JGenie jg)
    = "break <asJavaName(label)>; // muBreak\n";
    
JCode trans(muContinue(str label), JGenie jg)
    = "continue <asJavaName(label)>;\n";

JCode trans(muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp body, MuExp falseCont), JGenie jg){
    //TODO: cover all combinations int/real
    base = getName(var.atype);
    fst = jg.newTmp("fst"); fstContrib = "";
    scd = jg.newTmp("scd"); scdContrib = "";
    lst = jg.newTmp("lst"); lstContrib = "";
    dir = jg.newTmp("dir"); dirContrib = ""; dirKnown = false; dirUp = true;
    delta = jg.newTmp("delta"); deltaContrib = "";
    deltaVal = delta;
    
    if(muCon(int n) := first && getType(last) == areal()){
        first = muCon(1.0 * n);
    }
    
    if(muCon(_) := first) fst = trans(first, jg); else fstContrib = "final <atype2javatype(var.atype)> <fst> = <transWithCast(var.atype, first, jg)>;\n";
    if(muCon(_) := last) lst = trans(last, jg); else lstContrib = "final <atype2javatype(var.atype)> <lst> = <transWithCast(var.atype, last, jg)>;\n";
    
    testCode = "";
    if(muCon(int f) := first && muCon(int l) := last){
        dirKnown = true;
        dirUp = f < l;
        dirContrib = "final boolean <dir> = <f < l>;\n";
        testCode = dirUp ? "<transPrim("less", abool(), [var.atype, var.atype], [trans(var,jg), lst], jg)>.getValue()"
                         : "<transPrim("greater", abool(), [var.atype, var.atype], [trans(var,jg), lst], jg)>.getValue()";
    } else {
        dirContrib = "final boolean <dir> = <fst>.less(<lst>).getValue();\n";
        testCode = "<dir> ? <transPrim("less", abool(), [var.atype, var.atype], [trans(var,jg), lst], jg)>.getValue() 
                          : <transPrim("greater",abool(), [var.atype, var.atype], [trans(var,jg), lst], jg)>.getValue()";
    }
    
    if(muCon(int _) := first && muCon(int s) := second){
        if(s == 0){
            if(dirKnown){
               dirContrib = "";
               deltaVal = dirUp ? trans(muCon(1), jg) : trans(muCon(-1), jg);
            } else {
               deltaVal = "<dir> ? <trans(muCon(1), jg)> : <trans(muCon(-1), jg)>";
            }
        } else {
            deltaContrib = "final <atype2javatype(var.atype)> <delta> = <trans(second, jg)>.subtract(<fst>);\n";
        }
    
    } else {
        oneUp = muCon(var.atype == areal() ? 1.0 : 1);
        oneDown = muCon(var.atype == areal() ? -1.0 : -1);
       
        deltaCode = muCon(0) := second ? "<dir> ? <trans(oneUp, jg)> : <trans(oneDown, jg)>" : "<trans(second, jg)>.subtract(<fst>)";
        deltaContrib = "final <atype2javatype(var.atype)> <delta> = <deltaCode>;\n";
    }
    
    loop = "<isEmpty(label) ? "" : "<asJavaName(label)>:\n">for(<atype2javatype(var.atype)> <var.name> = <fst>; <testCode>; <var.name> = <transPrim("add", var.atype, [var.atype, var.atype], [trans(var,jg), deltaVal], jg)>){
           '    <trans2Void(body, jg)>}";
           
    if(!isEmpty(deltaContrib)){
        loop =  "if(<dir> ? $lessequal(<delta>, $VF.integer(0)).not().getValue() : $less(<delta>, $VF.integer(0)).getValue()) {
                '   <loop>
                '}";
    }
    return 
    "<fstContrib><lstContrib><dirContrib><deltaContrib>
    '<loop>
    '<trans2Void(falseCont, jg)>
    '";
}

JCode trans(muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp body, MuExp falseCont), JGenie jg){
    return 
    "<isEmpty(label) ? "" : "<asJavaName(label)>:\n">
    'for(int <var.name> = <ifirst>; <var.name> \<= <trans(last, jg)>; <var.name> += <istep>){
    '   <trans(body, jg)>
    '}
    '<trans2Void(falseCont, jg)>
    '";
}
         
JCode trans(muSwitch(str label, MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint), JGenie jg){
    noCaseMatched = "noCaseMatched_<exp.name>";
    return "boolean <noCaseMatched> = true;
           '<asJavaName(label)>: switch(Util.getFingerprint(<exp.name>, <useConcreteFingerprint>)){
           '<for(muCase(int fingerprint, MuExp exp1) <- cases){>
           '    case <fingerprint>:
           '        if(<noCaseMatched>){
           '            <noCaseMatched> = false;
           '            <trans(exp1, jg)>
           '        }
           '        
           '<}>
           '    default: <defaultExp == muBlock([]) ? "" : semi(trans(defaultExp, jg))>
           '}\n
           ";
    //return "<asJavaName(label)>: switch(Util.getFingerprint(<exp.name>, <useConcreteFingerprint>)){
    //       '<for(muCase(int fingerprint, MuExp exp1) <- cases){>
    //       '    case <fingerprint>:
    //       '        <trans(exp1, jg)>
    //       '        <trans(defaultExp, jg)>
    //       '<}>
    //       '    default: <defaultExp == muBlock([]) ? "" : trans(defaultExp, jg)>
    //       '}\n
    //       ";
}

JCode genDescendantDescriptor(DescendantDescriptor descendant, JGenie jg){
    definitions = descendant.definitions;
    
    useConcreteFingerprint = "$VF.bool(<descendant.useConcreteFingerprint>)";
    reachable_atypes = "new io.usethesource.vallang.type.Type[]{<intercalate(", ", [atype2vtype(t, jg) | t <- descendant.reachable_atypes])>}";
    reachable_aprods = "new io.usethesource.vallang.IConstructor[]{<intercalate(", ", [jg.shareReifiedConstant(atype2symbol(t), adefinitions2definitions(definitions)) | t <- descendant.reachable_aprods])>}";
    return "new DescendantDescriptor(<reachable_atypes>, 
           '                         <reachable_aprods>, 
           '                         <useConcreteFingerprint>)";
}

JCode trans(muVisit(str visitName, MuExp exp, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor), JGenie jg){
    direction  = vdescriptor.direction  ? "BottomUp"   : "TopDown";
    progress   = vdescriptor.progress   ? "Continuing" : "Breaking";
    fixedpoint = vdescriptor.fixedpoint ? "Yes"        : "No";
    rebuild    = vdescriptor.rebuild    ? "Yes"        : "No";
    
    defaultCode = defaultExp == muBlock([]) ? "" : 
                  "    default: 
                  '        <trans(defaultExp, jg)>";
    return
      "$TRAVERSE.traverse(DIRECTION.<direction>, PROGRESS.<progress>, FIXEDPOINT.<fixedpoint>, REBUILD.<rebuild>, 
      '     <genDescendantDescriptor(vdescriptor.descendant, jg)>,
      '     <trans(exp.exp,jg)>,
      '     (IVisitFunction) (IValue <exp.var.name>, TraversalState $traversalState) -\> {
      '         <visitName>:switch(Util.getFingerprint(<exp.var.name>, <vdescriptor.descendant.useConcreteFingerprint>)){
      '         <for(muCase(int fingerprint, MuExp exp) <- cases){>
      '             case <fingerprint>:
      '                 <trans(exp, jg)>
      '         <}>
      '         <defaultCode>
      '         }
      '         return <exp.var.name>;
      '     })";
}  

JCode trans(muDescendantMatchIterator(MuExp subject, DescendantDescriptor ddescriptor), JGenie jg)
    = "new DescendantMatchIterator(<trans(subject, jg)>, 
      '    <genDescendantDescriptor(ddescriptor, jg)>)";

JCode trans(muFailCase(str switchName), JGenie jg)
    = "break <switchName>;// switch\n";
    
JCode trans(muSucceedSwitchCase(str switchName), JGenie jg)
    = "break <switchName>;// succeedSwitch";
    
JCode trans(muSucceedVisitCase(str visitName), JGenie jg)
    = "$traversalState.setMatched(true); 
      'break <visitName>;";

// ---- muFailReturn ----------------------------------------------------------

JCode trans(muFailReturn(AType funType),  JGenie jg){
    currentFunctionName = jg.getFunction().name;
    if(startsWith(currentFunctionName, "$get")){
        idx = findLast(currentFunctionName, "_");
        fieldName = "??";
        if(idx > 0){
            fieldName = currentFunctionName[idx+1 .. ];
        }
        return "throw RuntimeExceptionFactory.noSuchField(\"<fieldName>\")";
    }
    return funType has ret && funType.ret == avoid() ? "throw new FailReturnFromVoidException();"
                             : "return null;";
 }   
// ---- muCheckMemo -----------------------------------------------------------

JCode trans(muCheckMemo(AType funType, list[MuExp] args/*, map[str,value] kwargs*/, MuExp body), JGenie jg){
    cache = getMemoCache(jg.getFunction());
    kwpActuals = isEmpty(funType.kwFormals) ? "Collections.emptyMap()" : "$kwpActuals";
    returnCode = funType.ret == avoid() ? "return" : "return (<atype2javatype(funType.ret)>) $memoVal";
    return "final IValue[] $actuals = new IValue[] {<intercalate(",", [trans(arg, jg) | arg <- args])>};
           'IValue $memoVal = <cache>.lookup($actuals, <kwpActuals>);
           'if($memoVal != null) <returnCode>;
           '<trans(body, jg)>";
}

// ---- muMemoReturn ----------------------------------------------------------

JCode trans(muMemoReturn0(AType funType, list[MuExp] args), JGenie jg){
    cache = getMemoCache(jg.getFunction());
    kwpActuals = isEmpty(funType.kwFormals) ? "Collections.emptyMap()" : "$kwpActuals";
    return "$memoVal = $VF.bool(true);
           '<cache>.store($actuals, <kwpActuals>, $memoVal);
           'return;";
}

JCode trans(muMemoReturn1(AType funType, list[MuExp] args, MuExp functionResult), JGenie jg){
    cache = getMemoCache(jg.getFunction());
    kwpActuals = isEmpty(funType.kwFormals) ? "Collections.emptyMap()" : "$kwpActuals";
    return "$memoVal = <trans(functionResult, jg)>;
           '<cache>.store($actuals, <kwpActuals>, $memoVal);
           'return (<atype2javatype(funType.ret)>)$memoVal;";
}
           
// Lists of expressions

JCode trans(muBlock(list[MuExp] exps), JGenie jg){
    return "<for(exp <- exps){><trans2Void(exp, jg)><}>";
}

//JCode trans(muAltBlock(list[MuExp] exps), JGenie jg){
//    return "<for(exp <- exps){><trans2Void(exp, jg)><}>";
//}
   
JCode trans(muValueBlock(AType t, list[MuExp] exps), JGenie jg){
    return "<for(exp <- exps[0..-1]){><trans2Void(exp, jg)><}> 
           '<trans(exps[-1], jg)>";
}

// Exceptions
       
JCode trans(muThrow(muTmpNative(str name, str fuid, nativeException()), loc src), JGenie jg){
    return "throw <name>;";
}

default JCode trans(muThrow(MuExp exp, loc src), JGenie jg){
    return "throw new Throw(<trans(exp, jg)>);";
}

JCode trans(muTry(MuExp exp, MuCatch \catch, MuExp \finally), JGenie jg){
    finallyCode = trans(\finally, jg);
    return "try {
           '     <semi(trans(exp, jg))>
           '}<trans(\catch, jg)>
           '<if(!isEmpty(finallyCode)){>finally { 
           '    <finallyCode>} <}>
           ";
}  

JCode trans(muCatch(MuExp thrown_as_exception, MuExp thrown, MuExp body), JGenie jg){
    return " catch (Throw <thrown_as_exception.name>) {
           '    IValue <thrown.name> = <thrown_as_exception.name>.getException();
           '   
           '    <semi(trans(body, jg))>
           '}";
}

str getIntegerFor(MuExp exp)
    = producesNativeInt(exp) ? "" : ".getValue()";
    
// ---- trans2Native ---------------------------------------------------------
 
 JCode trans2Native(MuExp exp, nativeInt(), JGenie jg)
    = trans2NativeInt(exp, jg);
    
JCode trans2Native(MuExp exp, nativeBool(), JGenie jg)
    = trans2NativeBool(exp, jg);  
    
JCode trans2Native(MuExp exp, NativeKind nkind, JGenie jg) 
    = trans(exp, jg);
    
JCode trans2NativeBool(muCon(bool b), JGenie jg)
    = "<b>";
 
default JCode trans2NativeBool(MuExp exp, JGenie jg)
    = producesNativeBool(exp) ? trans(exp, jg) : "(<transWithCast(abool(), exp, jg)>).getValue()";
  
JCode trans2NativeInt(muCon(int n), JGenie jg)
    = "<n>";
    
default JCode trans2NativeInt(MuExp exp, JGenie jg)
    = producesNativeInt(exp) ? trans(exp, jg) : "<transWithCast(aint(),exp, jg)>.intValue()";
    //= "<trans(exp, jg)><producesNativeInt(exp) ? "" : ".intValue()">";
    
JCode trans2IInteger(MuExp exp, JGenie jg)
    = producesNativeInt(exp) ? "$VF.integer(<trans(exp, jg)>)" : trans(exp, jg);
    
JCode trans2IBool(MuExp exp, JGenie jg)
    = producesNativeBool(exp) ? "$VF.bool(<trans(exp, jg)>)" : trans(exp, jg);

// ----

JCode trans2NativeStr(muCon(str s), JGenie jg)
    = "\"" + escapeForJ(s) + "\"";
    
JCode trans2NativeStr(MuExp exp, JGenie jg)
    = "org.rascalmpl.values.parsetrees.TreeAdapter.yield(<trans(exp, jg)>)"
    when isSyntaxType(getType(exp));
    
default JCode trans2NativeStr(MuExp exp, JGenie jg)
    = "<transWithCast(astr(), exp, jg)>.getValue()";
        
 JCode trans2NativeRegExpStr(muCon(str s), JGenie jg){
    res = "\"" + escapeForJRegExp(s) + "\"";
    return res;
} 
default JCode trans2NativeRegExpStr(MuExp exp, JGenie jg){
    return trans(exp, jg);
    //return trans(muPrim("str_escape_for_regexp", astr(), [astr()], [exp], |unknown:///|), jg);
    //return transWithCast(astr(), exp, jg);
}

// -----

JCode trans(muRequireNonNegativeBound(MuExp idx), JGenie jg)
    = "if(<trans2NativeInt(idx, jg)> \<= 0){
      ' throw RuntimeExceptionFactory.indexOutOfBounds(<trans2IInteger(idx, jg)>);
      '}\n";
 
JCode trans(muEqual(MuExp exp1, MuExp exp2), JGenie jg){
    return "<trans(exp1, jg)>.equals(<trans(exp2, jg)>)";
}
    
JCode trans(muMatch(MuExp exp1, MuExp exp2), JGenie jg)
    = "<trans(exp1, jg)>.match(<trans(exp2, jg)>)";
    
    
JCode trans(muMatchAndBind(MuExp exp1, AType tp), JGenie jg)
    = "<atype2vtype(tp, jg)>.match(<trans(exp1, jg)>.getType(), $typeBindings)";
      
JCode trans(muEqualNativeInt(MuExp exp1, MuExp exp2), JGenie jg)
    = "<trans2NativeInt(exp1, jg)> == <trans2NativeInt(exp2, jg)>";
    
JCode trans(muValueIsSubtypeOf(MuExp exp, AType tp), JGenie jg){
    return !isVarOrTmp(exp) && exp has atype && asubtype(exp.atype, tp)
           ? "true"
           : "$isSubtypeOf(<trans(exp, jg)>.getType(),<jg.accessType(tp)>)";
}
JCode trans(muValueIsSubtypeOfValue(MuExp exp1, MuExp exp2), JGenie jg)
    ="$isSubtypeOf(<trans(exp1, jg)>.getType(),<trans(exp2, jg)>.getType())";

JCode trans(muValueIsSubtypeOfInstantiatedType(MuExp exp, AType tp), JGenie jg){ 
    return !isVarOrTmp(exp) && exp has atype && asubtype(exp.atype, tp) 
           ? "true"
           : "$isSubtypeOf(<trans(exp, jg)>.getType(),<jg.accessType(tp)>.instantiate($typeBindings))";   
} 

JCode trans(m:muValueIsComparable(MuExp exp, AType tp), JGenie jg){
    return !isVarOrTmp(exp) && exp has atype && asubtype(exp.atype, tp) 
           ? "true"
           : "$isComparable(<trans(exp, jg)>.getType(), <jg.accessType(tp)>)";   
} 

JCode trans(muValueIsComparableWithInstantiatedType(MuExp exp, AType tp), JGenie jg){ 
    return !isVarOrTmp(exp) && exp has atype && comparable(exp.atype, tp) 
           ? "true"
           : "$isComparable(<trans(exp, jg)>.getType(), <jg.accessType(tp)>.instantiate($typeBindings))";   
} 

JCode trans(muHasTypeAndArity(AType atype, int arity, MuExp exp), JGenie jg){
    v = trans(exp, jg);
    t = atype2javatype(atype);
    switch(getName(atype)){
        case "atuple": return "<v> instanceof <t> && ((<t>)<v>).arity() == <arity>";
    }
    throw "muHasTypeAndArity: <atype>, <arity>";
}

JCode trans(muHasNameAndArity(AType atype, AType consType, MuExp name, int arity, MuExp exp), JGenie jg){
    expType = getType(exp);
    t = atype2javatype(atype);
    v = trans(exp, jg);
    switch(consType){
        case a: aadt(str adtName, list[AType] _, SyntaxRole _):{
            full_adt_name = "<jg.getATypeAccessor(consType)><getUniqueADTName(a)>";
            return "<v> instanceof IConstructor && (((IConstructor)<v>).arity() == <arity> && ((IConstructor)<v>).getType().equivalent(<full_adt_name>))";
            }
        case acons(AType adt, list[AType] fields, list[Keyword] _):
            if(isNonTerminalType(consType)){
                 return "<v> instanceof IConstructor && TreeAdapter.isTree((IConstructor)<v>) && $nonterminal_has_name_and_arity((ITree) <v>, \"<asUnqualifiedName(consType.alabel)>\", <size(fields)>)";
            } else
            if(isNonTerminalType(expType)){
                return "$nonterminal_has_name_and_arity((ITree) <v>, \"<asUnqualifiedName(consType.alabel)>\", <size(fields)>)";
            } else {
                return "<v> instanceof IConstructor && ((IConstructor)<v>).getConstructorType().equivalent(<jg.getATypeAccessor(consType)><atype2idpart(consType)>)";
            }
        default:
            return "<v> instanceof INode && ((INode)<v>).arity() == <arity> && ((INode)<v>).getName().equals(<trans2NativeStr(name,jg)>)";
        //default:
        //    throw "muHasNameAndArity: <atype>, <name>, <arity>, <exp>";
    }
}

JCode trans(muIsInitialized(MuExp exp), JGenie jg){
    return "(<trans(exp, jg)> != null)";
}

JCode trans(muIsDefinedValue(MuExp exp), JGenie jg)
    = "$is_defined_value(<trans(exp, jg)>)";

JCode trans(muGetDefinedValue(MuExp exp, AType atype), JGenie jg)
    = "((<atype2javatype(atype)>)$get_defined_value(<trans(exp, jg)>))";

JCode trans(muSize(MuExp exp, atype:aset(_)), JGenie jg)
    = "<transWithCast(atype, exp, jg)>.size()";

JCode trans(muSize(MuExp exp, atype:\iter(_)), JGenie jg)
    = "<transWithCast(atype, exp, jg)>.getArgs().length()";
    
JCode trans(muSize(MuExp exp, atype:\iter-seps(_,_)), JGenie jg)
    = "<transWithCast(atype, exp, jg)>.getArgs().length()";

JCode trans(muSize(MuExp exp, atype:\iter-star(_)), JGenie jg)
    = "<transWithCast(atype, exp, jg)>.getArgs().length()";
    
JCode trans(muSize(MuExp exp, atype:\iter-star-seps(_,_)), JGenie jg)
    = "<transWithCast(atype, exp, jg)>.getArgs().length()";
    
JCode trans(muSize(MuExp exp, atype:\opt(_)), JGenie jg){
   res = "<transWithCast(atype, exp, jg)>.getArgs().length()";
   return res;
}
 
default JCode trans(muSize(MuExp exp, AType atype), JGenie jg){
   return "<transWithCast(atype, exp, jg)>.length()";
}

JCode trans(muTreeListSize(MuExp exp, AType atype), JGenie jg){
    tr = trans(exp, jg);
    return "org.rascalmpl.values.parsetrees.TreeAdapter.getArgs((ITree)<tr>).length()";
}

// muHasField
JCode makeConsList(set[AType] consesWithField, JGenie jg){
    return isEmpty(consesWithField) ? "" : ", " + intercalate(", ", [" <jg.accessType(tp)>" | tp <- consesWithField ]);
}

JCode trans(muHasField(MuExp exp, AType tp, str fieldName, set[AType] consesWithField), JGenie jg){
    return  isADTType(tp) ? "$aadt_has_field(<transWithCast(tp, exp, jg)>,\"<fieldName>\"<makeConsList(consesWithField, jg)>)" 
                          : "$anode_has_field(<transWithCast(tp, exp, jg)>,\"<fieldName>\")";
}
// muSubscript
JCode trans(muSubscript(MuExp exp, MuExp idx), JGenie jg){
    return "$subject_subscript(<trans(exp, jg)>, <trans2NativeInt(idx, jg)>)";
}

JCode trans(muIncNativeInt(MuExp var, MuExp exp), JGenie jg)
    = muCon(int n) := exp ? "<trans(var, jg)> += <n>;\n" :  "<trans(var, jg)> += <trans(exp, jg)>;\n";
    
JCode trans(muSubNativeInt(MuExp exp1, MuExp exp2), JGenie jg)
    = "<trans2NativeInt(exp1, jg)> - <trans2NativeInt(exp2, jg)>";
    
JCode trans(muAddNativeInt(MuExp exp1, MuExp exp2), JGenie jg)
    = "<trans2NativeInt(exp1, jg)> + <trans2NativeInt(exp2, jg)>";
    
JCode trans(muMulNativeInt(MuExp exp1, MuExp exp2), JGenie jg)
    = "(<trans2NativeInt(exp1, jg)>) * (<trans2NativeInt(exp2, jg)>)"; 

JCode trans(muAbsNativeInt(MuExp exp), JGenie jg)
    = "Math.abs(<trans2NativeInt(exp, jg)>)";
    
JCode trans(muGreaterEqNativeInt(MuExp exp1, MuExp exp2), JGenie jg)
    = "<trans2NativeInt(exp1, jg)> \>= <trans2NativeInt(exp2, jg)>";

JCode trans(muLessNativeInt(MuExp exp1, MuExp exp2), JGenie jg)
    = "<trans2NativeInt(exp1, jg)> \< <trans2NativeInt(exp2, jg)>";

JCode trans(muToNativeInt(MuExp exp), JGenie jg)
    = trans2NativeInt(exp, jg);
    
JCode trans(muAndNativeBool(MuExp exp1, MuExp exp2), JGenie jg){
    v1 = trans2NativeBool(exp1, jg);
    v2 = trans2NativeBool(exp2, jg);
    return v1 == "true" ? v2
                        : (v2 == "true" ? v1 : "<v1> && <v2>");
}

JCode trans(muNotNativeBool(MuExp exp), JGenie jg){
    v = trans2NativeBool(exp, jg);
    return v == "true" ? "false" : "!<v>";                              
}

JCode trans(muSubList(MuExp lst, MuExp from, MuExp len), JGenie jg)
    = "<trans(lst, jg)>.sublist(<trans(from, jg)>, <trans(len, jg)>)";

JCode trans(muConcreteSubList(MuExp lst, MuExp from, MuExp len, MuExp delta), JGenie jg)
    = "$concreteSubList(<trans(lst, jg)>, <trans2NativeInt(from, jg)>, <trans2NativeInt(len, jg)>, <trans2NativeInt(delta, jg)>)";
    
// Regular expressions

JCode trans(muRegExpCompile(MuExp regExp, MuExp subject), JGenie jg){
    return "$regExpCompile(<trans2NativeRegExpStr(regExp, jg)>, <trans2NativeStr(subject, jg)>)";
}    
JCode trans(muRegExpBegin(MuExp matcher), JGenie jg)
    = "<trans(matcher, jg)>.start()";
    
JCode trans(muRegExpEnd(MuExp matcher), JGenie jg)
    = "<trans(matcher, jg)>.end()";

JCode trans(muRegExpFind(MuExp matcher), JGenie jg)
    =  "<trans(matcher, jg)>.find()";

JCode trans(muRegExpSetRegion(MuExp matcher, int begin, int end), JGenie jg)
    =  "<trans(matcher, jg)>.region(<begin>, <end>)";

JCode trans(muRegExpGroup(MuExp matcher, int n), JGenie jg)
    = "$VF.string(<trans(matcher, jg)>.group(<n>))";
 
 JCode trans(muRegExpSetRegionInVisit(MuExp matcher), JGenie jg)
    = "<trans(matcher, jg)>.region($traversalState.getBegin(),$traversalState.getEnd());\n";
    
 JCode trans(muRegExpSetMatchedInVisit(MuExp matcher), JGenie jg)
    = "$traversalState.setBegin(<trans(matcher, jg)>.start());
      '$traversalState.setEnd(<trans(matcher, jg)>.end());\n";

JCode trans(muStringSetMatchedInVisit(int len), JGenie jg)
    = "$traversalState.setEnd($traversalState.getBegin() + <len>);\n";
    
// String templates

JCode trans(muTemplate(str initial), JGenie jg){
    return "new Template($VF, \"<escapeAsJavaString(initial)>\")";
}

JCode trans(muTemplateBeginIndent(MuExp template, str indent), JGenie jg)
    = "<trans(template, jg)>.beginIndent(\"<indent>\");\n";
    
JCode trans(muTemplateEndIndent(MuExp template, str unindent), JGenie jg)
    = "<trans(template, jg)>.endIndent(\"<escapeAsJavaString(unindent)>\");\n";
    
JCode trans(muTemplateAdd(MuExp template, AType atype, muCon(str s)), JGenie jg)
    = "<trans(template, jg)>.addStr(\"<escapeAsJavaString(s)>\");\n";
    
JCode trans(muTemplateAdd(MuExp template, AType atype, str s), JGenie jg){
    if(isEmpty(s)) return "";
    return "<trans(template, jg)>.addStr(\"<escapeAsJavaString(s)>\");\n";
}
    
default JCode trans(muTemplateAdd(MuExp template, AType atype, MuExp exp), JGenie jg){
    if(isStrType(getType(exp))){
        return "<trans(template, jg)>.addStr(<trans2NativeStr(exp,jg)>);\n";
    }
    if(isStrType(atype)){
        return "<trans(template, jg)>.addStr(<transWithCast(atype, exp, jg)>.getValue());\n";
    }
    return "<trans(template, jg)>.addVal(<trans(exp,jg)>);\n";
}

JCode trans(muTemplateClose(MuExp template), JGenie jg)
    = "<trans(template, jg)>.close()";
    
// ---- Misc ------------------------------------------------------------------

//JCode trans(muResetLocs(list[int] positions), JGenie jg)
//    = "";