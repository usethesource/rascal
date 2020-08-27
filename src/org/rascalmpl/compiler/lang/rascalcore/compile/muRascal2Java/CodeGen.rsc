module lang::rascalcore::compile::muRascal2Java::CodeGen

import lang::rascal::\syntax::Rascal;

import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::BasicRascalConfig;
import Location;
import List;
import Set;
import Relation;
import String;
import Map;
import Node;
import IO;
import Type;
import util::Math;
import util::Reflective;

import lang::rascalcore::compile::muRascal2Java::Primitives;

import lang::rascalcore::compile::muRascal2Java::JGenie;
import lang::rascalcore::compile::muRascal2Java::Conversions;
import lang::rascalcore::compile::muRascal2Java::Tests;

import lang::rascalcore::compile::util::Names;

bool debug = false;

// ---- globals ---------------------------------------------------------------

map[str, MuFunction] muFunctions = ();
map[loc, MuFunction] muFunctionsByLoc = ();
map[str,str] resolved2overloaded = ();
map[str,AType] resolved2type = ();
map[str, list[MuExp]] resolved2external = ();

// ---- muModule --------------------------------------------------------------

// Generate code and test class for a single Rascal module

tuple[JCode, JCode] muRascal2Java(MuModule m, map[str,TModel] tmodels, map[str,loc] moduleLocs){
for(f <- m.functions){println("<f.name>, <f.uniqueName>, <f.ftype>, <f.scopeIn>"); }
    muFunctions = (f.uniqueName : f | f <- m.functions);
    muFunctionsByLoc = (f.src : f | f <- m.functions);
    resolved2overloaded = ();
    iprintln(m.overloaded_functions);
    moduleScopes = range(moduleLocs); //{ s |tm <- range(tmodels), s <- tm.scopes, tm.scopes[s] == |global-scope:///| };
    
    jg = makeJGenie(m, tmodels, moduleLocs, muFunctions);
    <typestore, kwpDecls> = generateTypeStoreAndKwpDecls(m.ADTs, m.constructors);
    <mergedFuns, ovlFuns> = mergeOverloadedFunctions(m.overloaded_functions);
    jg.addMergedOverloads(mergedFuns);
    resolvers = genResolvers(ovlFuns, moduleScopes, jg);
    
    bool hasMainFunction = false;
    str mainName = "main";
    AType mainType = avoid();
    MuFunction mainFunction;
    for(f <- m.functions){
        if(isOuterScopeName(f.scopeIn) && isMainName(f.uniqueName)) {
            hasMainFunction = true;
            mainFunction = f;
            mainType = f.ftype;
 //           mainName = f.qname;
        }
        jg.addExternalVars(f.externalVars);
        jg.addLocalRefs(f.localRefs);
    }
    moduleName = m.name;
    className = getClassName(moduleName);    
    packageName = getPackageName(moduleName);
    
    module_variables  = "<for(var <- m.module_variables){>
                        '<trans(var, jg)><}>";
                       
    functions         = "<for(f <- m.functions){>
                        '<trans(f, jg)>
                        '<}>";
                        
    str compilerVersionOfLibrary(str qname){
        libpref = "org.rascalmpl.library";
        if(contains(qname, libpref)){
            libpost = qname[findFirst(qname, libpref) + size(libpref) + 1 ..];
            tmp = |project://rascal-core/src/org/rascalmpl/core/library/<replaceAll(libpost, ".", "/")>Compiled.java|;
            if(exists(tmp)){
                return "org.rascalmpl.core.library.<libpost>Compiled";
            }
        }
        return qname;
    }
               
    library_inits     = "<for(class <- jg.getImportedLibraries()){
                            libclass = compilerVersionOfLibrary(getQualClassName(class));>
                            // TODO JURGEN: should be: javaBridge.getJavaClassInstance(\"<libclass>\", $VF, $monitor, $err, $out, this);
                         'final <libclass> <getBaseClass(class)> = new <libclass>($VF); 
                         '<}>";
                        
    module_extends    =  ""; //!isEmpty(m.extends) ? ", " + intercalate(", ",[ module2class(ext) | ext <- m.extends]) : "";
                        
    module_imports    = "<for(imp <- toSet(m.imports + m.extends), contains(module2class(imp), ".")){>
                         'import <module2class(imp)>;
                        '<}>";
                       
    imp_ext_decls     = "<for(imp <- toSet(m.imports + m.extends)){>
                        '<getClassRef(imp, moduleName)> <module2field(imp)>;
                        '<}>";
                           
    module_ext_inits  = "<for(ext <- m.extends){>
                        '<module2field(ext)> = <module2class(ext)>.extend<getBaseClass(ext)>(this);
                        '<}>";
    iprintln(m.initialization);
    class_constructor = "public <className>(){
                        '    this(new ModuleStore());
                        '}
                        '
                        '@SuppressWarnings(\"unused\")
                        'public <className>(ModuleStore store){
                        '   this.$me = this;
                        '   <for(imp <- m.imports, imp notin m.extends){>
                        '   <module2field(imp)> = store.importModule(<getClassRef(imp, moduleName)>.class, <getClassRef(imp, moduleName)>::new);
                        '   <}> 
                        '   <for(ext <- m.extends){>
                        '   <module2field(ext)> = new <getBaseClass(ext)>(store);
                        '   <}>
                        '   <kwpDecls>
                        '   <for(exp <- m.initialization){><trans(exp, jg)>
                        '   <}>
                        '}";
    externalArgs = "";                 
    if(hasMainFunction && !isEmpty(mainFunction.externalVars)){
      externalArgs = intercalate(", ", [ "new ValueRef\<<jtype>\>(<var.name>)" | var <- sort(mainFunction.externalVars), var.pos >= 0, jtype := atype2javatype(var.atype)]);
    }              
    main_method       = hasMainFunction ? (mainFunction.ftype.ret == avoid() ? "public static void main(String[] args) {
                                                                    'new <className>().<mainName>(<externalArgs>);
                                                                    '}"
                                                                  : "public static void main(String[] args) {
                                                                    'IValue res = new <className>().<mainName>(<externalArgs>); 
                                                                    'if(res == null) throw new RuntimeException(\"Main function failed\"); else System.out.println(res);
                                                                    '}")
                                        : "public static void main(String[] args) {
                                          'throw new RuntimeException(\"No function `main` found in Rascal program `<m.name>`\");
                                          '}";
    
    the_class =         "<if(!isEmpty(packageName)){>package <packageName>;<}>
                        'import java.io.PrintWriter;
                        'import java.io.StringWriter;
                        'import java.util.*;
                        'import java.util.regex.Matcher;
                        'import io.usethesource.vallang.*;
                        'import io.usethesource.vallang.type.*;
                        'import org.rascalmpl.ast.AbstractAST;  // Needed for exceptions, should be removed
                        'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.*;
                        'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.RascalExecutionContext;
                        'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.*;
                        'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse.*;
                        'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils.*;
                        'import org.rascalmpl.exceptions.Throw; // Dependency on interpreter, make standalone
                        '//import org.rascalmpl.interpreter.result.util.MemoizationCache;
                        'import org.rascalmpl.exceptions.RuntimeExceptionFactory;
                        'import org.rascalmpl.util.ExpiringFunctionResultCache;
                        '
                        '<module_imports>
                        '
                        'public class <className> extends org.rascalmpl.core.library.lang.rascalcore.compile.runtime.$RascalModule <module_extends> {
                        '    final Traverse $TRAVERSE = new Traverse($VF);
                        '    private <className> $me;
                        '    private RascalExecutionContext rex = new RascalExecutionContext(new PrintWriter(System.out), new PrintWriter(System.err), null, null);
                        '    <typestore>
                        '    <library_inits>
                        '    <imp_ext_decls>
                        '    <module_variables>
                        '    <class_constructor>
                        '    <jg.getConstants()>
                        '    <resolvers>
                        '    <functions>
                        '    <main_method>
                        '}";
                       
      the_test_class = generateTestClass(packageName, className, m.functions, jg);
      
      return <the_class, the_test_class>;
}

// Generate a TypeStore and declarations for keyword parameters

tuple[str,str] generateTypeStoreAndKwpDecls(set[AType] ADTs, set[AType] constructors){
    adtTypeDecls = "";
    seenAdtNames = {};
    for(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole) <- ADTs){
        if(adtName notin seenAdtNames){
            adtTypeDecls += "final io.usethesource.vallang.type.Type <getADTName(adtName)> = $TF.abstractDataType($TS, \"<adtName>\");\n";
            seenAdtNames += adtName;
        }
    }
    consTypeDecls = "";
    kwpTypeDecls = "";
    
    for(c: acons(AType adt, list[AType] fields, list[Keyword] kwpFields) <- constructors){
        adt_cons = atype2idpart(c);
        hasFieldNames = all(fld <- fields, !isEmpty(fld.label));
        fieldDecls = hasFieldNames ? [ "<atype2vtype(fld)>, \"<fld.label>\"" | fld <- fields ] : [ "<atype2vtype(fld)>" | fld <- fields ];
        consTypeDecls += "final io.usethesource.vallang.type.Type <adt_cons> = $TF.constructor($TS, <getADTName(adt.adtName)>, \"<c.label>\"<isEmpty(fieldDecls) ? "" : ", <intercalate(", ", fieldDecls)>">);\n";
        for(kwpField <- kwpFields){
            kwpTypeDecls += "$TS.declareKeywordParameter(<adt_cons>,\"<kwpField.fieldType.label>\", <atype2vtype(kwpField.fieldType)>);\n";
        }
    }    
    return <"<adtTypeDecls>
            '<consTypeDecls>
            '",
            kwpTypeDecls>;
}

// ---- Overloading resolvers -------------------------------------------------
//
// An overloaded named F can have
// - a single (S) or multiple (M) signatures
// - be defined in the current module (C) or in (one or more) imported modules or both
//
// Generate a resolver method for overloaded functions. Distinguish
// - a S-resolver resolves calls to a function with the same argument types and more than one definition
// - a M-resolver resolves calls to a function with definitions with different argument types
//   (it checks argument types and dispatches to the appropriate S-resolver)

// For each S-overloaded function, there are the following cases:

// - All overloads are defined in the current module
//   ==> generate a new S-resolver method F

// - All overloads are defined in one of the imported modules
//   ==> use the resolver in that module

// - The overloads are defined in more than one imported module and possibly also in the current module
//   ==> generate a new S-resolver method F

// For each M-overloaded function:
//   ==> generate an M-resolver method F


alias OF5 = tuple[str name, AType funType, str oname, list[loc] ofunctions, list[loc] oconstructors];

tuple[rel[str,AType,str], list[OF5]] mergeOverloadedFunctions(list[OF5] overloadedFunctions){
    iprintln(overloadedFunctions);
    overloadsWithName = [ <ovl.name> + ovl | ovl <- overloadedFunctions ];
    mergedNames = {};
    allFuns = [];
    for(fname <- toSet(overloadsWithName<0>)){
        <merged, funs> = mergeSimilar(overloadsWithName[fname]);
        mergedNames += merged;
        allFuns += funs;
    }
    return <mergedNames, allFuns>;
}

tuple[rel[str,AType,str], list[OF5]] mergeSimilar(list[OF5] overloadedFunctions){
    resultingOverloadedFuns = [];
    mergedFuns = {};
    while(!isEmpty(overloadedFunctions)){
        ovl1 = overloadedFunctions[0];
        overloadedFunctions = overloadedFunctions[1..];
        similar = [ovl2 | ovl2 <- overloadedFunctions, leadToSameJavaType(ovl1.funType, ovl2.funType)];
        if(!isEmpty(similar)){
            similar = ovl1 + similar;
            mergedName = intercalate("_MERGED_", [ovl.oname | ovl <- similar]);
            merged = <ovl1.name, 
                      lubList([ovl.funType | ovl <- similar]), 
                      mergedName,
                      [*ovl.ofunctions | ovl <- similar],
                      [*ovl.oconstructors | ovl <- similar]
                     >;
            resultingOverloadedFuns += merged;
            mergedFuns = { <ovl.oname, ovl.funType, mergedName> | ovl <- similar };
            overloadedFunctions -= similar;            
        } else {
            resultingOverloadedFuns += ovl1;
        }
    }
    
    return <mergedFuns, resultingOverloadedFuns>;
}

str genResolvers(list[OF5] overloadedFunctions, set[loc] moduleScopes, JGenie jg){
    overloadsWithName = [ <ovl.name> + ovl | ovl <- overloadedFunctions ];
    iprintln(overloadedFunctions, lineLimit=10000);
    all_resolvers = "";
    for(fname <- toSet(overloadsWithName<0>), /*!isClosureName(fname),*/ fname != "type", !isMainName(fname)){
        for(OF5 overload <- overloadsWithName[fname]){ 
            all_resolvers += genSingleResolver(overload, moduleScopes, jg);
        }  
    }
    return all_resolvers;
}

// Determine whether two atypes lead to the same Java type in the generated code

bool leadToSameJavaType(AType t1, AType t2) {
    r = leadToSameJavaType1(t1, t2);
    println("leadToSameJavaType(<t1>, <t2>) ==\> <r>");
    return r;
}

bool leadToSameJavaType1(alist(AType t1), alist(AType t2)) = !equivalent(t1, t2);
bool leadToSameJavaType1(aset(AType t1), aset(AType t2)) = !equivalent(t1, t2);
bool leadToSameJavaType1(abag(AType t1), abag(AType t2)) = !equivalent(t1, t2);
bool leadToSameJavaType1(arel(atypeList(list[AType] ts1)), arel(atypeList(list[AType] ts2))) = size(ts1) != size(ts2);
bool leadToSameJavaType1(arel(atypeList(list[AType] ts1)), aset(AType t2)) = true;
bool leadToSameJavaType1(aset(AType t1), arel(atypeList(list[AType] ts2))) = true;

bool leadToSameJavaType1(alrel(atypeList(list[AType] ts1)), alrel(atypeList(list[AType] ts2))) = size(ts1) != size(ts2);
bool leadToSameJavaType1(alrel(atypeList(list[AType] ts1)), alist(AType t1)) = true;
bool leadToSameJavaType1(alist(AType t1), alrel(atypeList(list[AType] ts1))) = true;

bool leadToSameJavaType1(atuple(atypeList(ts1)), atuple(atypeList(ts2))) = size(ts1) != size(ts2);

bool leadToSameJavaType1(amap(AType k1, AType v1), amap(AType k2, AType v2)) = !equivalent(k1,k2) || !equivalent(v1, v2);
bool leadToSameJavaType1(afunc(AType ret1, list[AType] formals1, list[Keyword] kwFormals1), afunc(AType ret2, list[AType] formals2, list[Keyword] kwFormals2))
    = size(formals1) == size(formals2) && (any(int i <- index(formals1), leadToSameJavaType(formals1[i], formals2[i]))/* || leadToSameJavaType(ret1, ret2) */);
    
bool leadToSameJavaType1(aparameter(str pname1, AType bound1), aparameter(str pname2, AType bound2)) = leadToSameJavaType(bound1, bound2);

bool leadToSameJavaType1(areified(AType atype1), areified(AType atype2)) = !equivalent(atype1, atype2);
bool leadToSameJavaType1(areified(AType atype1), aadt(str adtName1, list[AType] parameters1, SyntaxRole syntaxRole1)) = true;
bool leadToSameJavaType1(aadt(str adtName1, list[AType] parameters1, SyntaxRole syntaxRole1), areified(AType atype1)) = true;
bool leadToSameJavaType1(aadt(str adtName1, list[AType] parameters1, SyntaxRole syntaxRole1), aadt(str adtName2, list[AType] parameters2, SyntaxRole syntaxRole2)) = true;
    
default bool leadToSameJavaType1(AType t1, AType t2) = false;

bool leadToSameJavaType(atypeList(list[AType] elms1), atypeList(list[AType] elms2)) = size(elms1) != size(elms2) || any(i <- index(elms1), leadToSameJavaType(elms1[i], elms2[i]));

// Generate a resolver for a function that is S-overloaded

str genSingleResolver(tuple[str name, AType funType, str oname, list[loc] ofunctions, list[loc] oconstructors] overload, set[loc] moduleScopes, JGenie jg){
    if(overload.name == "type") return <"","">;
    
    if(isSyntheticFunctionName(overload.name)){
        println(overload.name);
    }
   
    funType = unsetRec(overload.funType);
    returns_void = funType has ret && funType.ret == avoid();
    formalTypes = getFormals(funType);
    returnType = atype2javatype(getResult(funType));
   
    if(jg.isResolved(overload) && !jg.usesLocalFunctions(overload) /*|| getArity(funType) == 0*/) return "";
    currentModuleLoc = jg.getModuleLoc();
    if(all(def <- overload.ofunctions + overload.oconstructors, !isContainedIn(def, currentModuleLoc))){
        return "";
    }
    
    isInnerFunction = any(ovl <- overload.ofunctions + overload.oconstructors, jg.getDefine(ovl).scope notin moduleScopes);
      
    anyKwParams =    !isEmpty(overload.ofunctions) && any(ovl <- overload.ofunctions, hasKeywordParameters(jg.getType(ovl)))
                  || !isEmpty(overload.oconstructors) && any(ovl <- overload.oconstructors, hasKeywordParameters(jg.getType(ovl)) || jg.hasCommonKeywordFields(jg.getType(ovl)));
   
    concretePatterns = any(ovl <- overload.ofunctions /*+ overload.oconstructors*/, jg.getType(ovl).isConcreteArg);
  
    resolverName = overload.oname;
    //for(ovl <- overload.ofunctions){
    //    if(muFunctionsByLoc[ovl]?){
    //        fn = muFunctionsByLoc[ovl];
    //        if(!isOuterScopeName(fn.scopeIn)){
    //            resolverName = "<fn.scopeIn>_<resolverName>";
    //            break;
    //        }
    //    }
    //}
    
    resolverName = getJavaName(resolverName);
    overload.oname = resolverName;
   
    argTypes = intercalate(", ", ["<atype2javatype(f)> $<i>" | i <- index(formalTypes), f := formalTypes[i]]);
    if(anyKwParams){
        kwpActuals = "java.util.Map\<java.lang.String,IValue\> $kwpActuals";
        argTypes = isEmpty(argTypes) ? kwpActuals : "<argTypes>, <kwpActuals>";
    }
   
    externalVars = sort(toList({*jg.getExternalVars(ofun) | ofun <- overload.ofunctions }));
    if(!isEmpty(externalVars)){
        argTypes += (isEmpty(formalTypes) ? "" : ", ") +  intercalate(", ", [ "ValueRef\<<jtype>\> <varName(var, jg)>" | var <- externalVars, jtype := atype2javatype(var.atype)]);
    }
   
    name_resolver = "<replaceAll(overload.name, "::", "_")>_<atype2idpart(overload.funType)>";
    signature = "public <returnType> <getJavaName(resolverName)>(<argTypes>)";
    isignature = "public <returnType> <getJavaName(overload.name)>(<argTypes>)";
   
    imethod = "";
    if(!isInnerFunction && !isSyntheticFunctionName(overload.name)){
        actuals = intercalate(", ", ["$<i>" |  i <- index(formalTypes)]);
        if(anyKwParams){
            kwpActuals = "$kwpActuals";
            actuals = isEmpty(actuals) ? kwpActuals : "<actuals>, <kwpActuals>";
        }
         if(!isEmpty(externalVars)){
            actuals += (isEmpty(formalTypes) ? "" : ", ") +  intercalate(", ", [ varName(var, jg) | var <- sort(externalVars), jtype := atype2javatype(var.atype)]);
        }
        imethod = "<isignature>{ // interface method <overload.name>: <prettyAType(funType)>
                          '  <returns_void ? "" : "return "><getJavaName(resolverName)>(<actuals>);
                          '}";
    }
   
    canFail = any(of <- overload.ofunctions, di := jg.getDefine(of).defInfo, di has canFail, di.canFail);
    map[int,str] cases = ();
    defaultFunctions = "";
    for(of <- overload.ofunctions){
        ft = jg.getType(of);
        if(ft.isDefault){
            defaultFunctions += makeCallInResolver(funType, of, jg);
        }
    }
    for(of <- overload.ofunctions){
        ft = jg.getType(of);
        fp = ft.abstractFingerprint;
        if(!ft.isDefault){
            if(cases[fp]?){
                cases[fp] += makeCallInResolver(funType, of, jg);
            } else {
                cases[fp] = makeCallInResolver(funType, of, jg);
            }
        }
    }
   
    conses = "<for(of <- overload.oconstructors){>
             '  <makeCallInResolver(funType, of, jg)>
             '<}>";
     
    body = "";
    if(size(cases) == 0){
        body = defaultFunctions;
    } else if(size(cases) == 1){
        for(c <- cases){
            body = cases[c] + defaultFunctions;
            break;
        }
    } else if(size(cases) > 1){
        body = "switch(ToplevelType.getFingerprint($0, <concretePatterns>)){
               '<for(caseLab <- cases, caseLab != 0){>
               '         case <caseLab>: { 
               '             <cases[caseLab]> 
               '             break;
               '         }
               '    <}>
               '}
               '<cases[0]? ? (cases[0]) : "">
               '<defaultFunctions>
               ";
    }
    
    jg.addResolver(overload, externalVars);
   
    return "<imethod>
           '
           '<signature>{ // Single-resolver for <prettyAType(funType)> <overload.name>
           '    <if(true /*canFail*/ && !returns_void){><atype2javatype(getResult(funType))> res;<}>
           '    <body><conses>
           '    <if(/*canFail &&*/ isEmpty(conses) || contains(conses, "if(")){>throw new RuntimeException(\"Cannot resolve call to `<overload.name>`\");<}>
           '}
           '";
}

JCode makeCallInResolver(AType resolverFunType, loc of, JGenie jg){
    funType = jg.getType(of);
    println("funType = <funType>");
    kwpActuals = "$kwpActuals";
    formalTypes = getFormals(funType);
    arityFormalTypes = size(formalTypes);
    returns_void = funType has ret && funType.ret == avoid();
    resolverFormalTypes = getFormals(resolverFunType);
    arityResolverFormalTypes = size(resolverFormalTypes);
    
    if(!isEmpty(formalTypes) && any(int i <- index(formalTypes), (i < arityResolverFormalTypes && (unsetRec(formalTypes[i]) != unsetRec(resolverFormalTypes[i]))))){
        // The formal types of this overload are not equal to those of the resolver, so generate conditions to check the parameters
        conds = [];
        actuals = [];
        for(int i <- index(resolverFormalTypes)){
            if(i < arityFormalTypes && unsetRec(formalTypes[i]) != resolverFormalTypes[i]){
                conds +=  atype2istype("$<i>", formalTypes[i]);
                actuals += "(<atype2javatype(formalTypes[i])>) $<i>";
            } else {
                actuals += "$<i>";
            }
        }
     
        base_call = "";
        if(isConstructorType(funType)){
            base_call = "return <makeConstructorCall(funType, actuals, hasKeywordParameters(funType) || jg.hasCommonKeywordFields(funType) ? [kwpActuals] : [], jg)>;";
        } else {
            if(hasKeywordParameters(funType)){
                actuals = isEmpty(actuals) ? [kwpActuals] : actuals + kwpActuals;
            }
            externalVars = jg.getExternalVars(of);
            if(!isEmpty(externalVars)){
                argTypes += (isEmpty(formalTypes) ? "" : ", ") +  intercalate(", ", [ varName(var, jg) | var <- sort(externalVars), jtype := atype2javatype(var.atype)]);
             }
            call_code = "<jg.getAccessorInResolver(of)>(<intercalate(", ", actuals)>)";
            di = jg.getDefine(of).defInfo;
            returns_void = aprod(_) !:= funType && funType.ret == avoid();
            
            base_call = di has canFail && true /*di.canFail*/ ? (returns_void ? "try { <call_code>; return; } catch (FailReturnFromVoidException e){}\n"
                                                                     : "res = <call_code>;
                                                                       'if(res != null) return <returns_void ? "" : "res">;"
                                                       )
                                                       
                                                     : ( returns_void ? "<call_code>; return;"
                                                                      : "return <call_code>;"
                                                       );
        }
        if(isEmpty(conds)){
            return base_call;
        } else {
            return "if(<intercalate(" && ", conds)>){
                   '    <base_call>
                   '}\n";
        }
    } else {
        actuals = ["$<i>" | i <- index(formalTypes), f := formalTypes[i]];
        if(isConstructorType(funType)){
            return "return <makeConstructorCall(funType, actuals, hasKeywordParameters(funType) || jg.hasCommonKeywordFields(funType) ? [kwpActuals] : [], jg)>;";
        
        } else {
            if(hasKeywordParameters(funType)){
                actuals += kwpActuals;
            }
            externalVars = jg.getExternalVars(of);
            if(!isEmpty(externalVars)){
                actuals +=  [ varName(var, jg) | var <- sort(externalVars), jtype := atype2javatype(var.atype)];
             }
            call_code = "<jg.getAccessorInResolver(of)>(<intercalate(", ", actuals)>)";
            di = jg.getDefine(of).defInfo;
            returns_void = getResult(funType) == avoid();
            return (di has canFail &&true /*di.canFail*/) ? ( returns_void ? "try { <call_code>; return; } catch (FailReturnFromVoidException e){};\n"
                                                                   : "res = <call_code>;
                                                                     'if(res != null) return <returns_void ? "" : "res">;\n"
                                                    )
                                                  : ( returns_void ? "<call_code>; return;\n"
                                                                   : "return <call_code>;\n"
                                                    );
        }
    }
}

JCode makeConstructorCall(AType consType, list[str] actuals, list[str] kwargs, JGenie jg){
    if(!isEmpty(kwargs)){
        return "$VF.constructor(<atype2idpart(consType)>, new IValue[]{<intercalate(", ", actuals)>}, <kwargs[0]>)";
    } else {
     return "$VF.constructor(<atype2idpart(consType)>, new IValue[]{<intercalate(", ", actuals)>})";
    }
}

// ---- muModuleVar ----------------------------------------------------------

JCode trans(MuModuleVar var, JGenie jg){
       return "<atype2javatype(var.atype)> <getJavaName(var.name)>;";
}

// ---- muFunction ------------------------------------------------------------

bool constantDefaults(lrel[str name, AType atype, MuExp defaultExp] kwpDefaults){
    return all(<str name, AType atype, MuExp defaultExp> <- kwpDefaults, muCon(_) := defaultExp);
}

tuple[str argTypes, str constantKwpDefaults, str nonConstantKwpDefaults] getArgTypes(MuFunction fun, JGenie jg){   
    shortName = getJavaName(getUniqueFunctionName(fun)); 
    argTypes = intercalate(", ", [ "<atype2javatype(fun.ftype.formals[i])> <varName(fun.formals[i], jg)>" | i <- index(fun.formals) ]);          
    if(!isEmpty(fun.externalVars)){
        ext_actuals = intercalate(", ", ["ValueRef\<<atype2javatype(var.atype)>\> <varName(var, jg)>" | var <- fun.externalVars, var.pos >= 0]);
        argTypes = isEmpty(fun.formals) ? ext_actuals : (isEmpty(ext_actuals) ? argTypes : "<argTypes>, <ext_actuals>");
    }
    kwpActuals = "java.util.Map\<java.lang.String,IValue\> $kwpActuals";
    kwpDefaults = fun.kwpDefaults;
    constantKwpDefaults = "";
    nonConstantKwpDefaults = "";
     if(!isEmpty(fun.ftype.kwFormals)){
        argTypes = isEmpty(argTypes) ? kwpActuals : "<argTypes>, <kwpActuals>";
          
        if(constantDefaults(kwpDefaults)){
            kwpDefaultsName = "$kwpDefaults_<shortName>";
            jg.setKwpDefaults(kwpDefaultsName);
            mapCode = "Util.kwpMap(<intercalate(", ", [ *["\"<getJavaName(key)>\"", trans(defaultExp,jg)] | <str key, AType tp, MuExp defaultExp> <- kwpDefaults ])>);\n";
            constantKwpDefaults = "final java.util.Map\<java.lang.String,IValue\> <kwpDefaultsName> = <mapCode>";
         } else {
            jg.setKwpDefaults("$kwpDefaults");
            
            nonConstantKwpDefaults =  "java.util.Map\<java.lang.String,IValue\> $kwpDefaults = Util.kwpMap();\n";
            for(<str name, AType tp, MuExp defaultExp> <- kwpDefaults){
                escapedName = getJavaName(name);
                nonConstantKwpDefaults += "<trans(muConInit(muVar("$kwpDefault_<escapedName>","", 10, tp), defaultExp),jg)>$kwpDefaults.put(\"<escapedName>\", $kwpDefault_<escapedName>);";
            }
         }   
    }
    return <argTypes, constantKwpDefaults, nonConstantKwpDefaults>;
}
tuple[int secondsTimeout, int maxSize] getMemoSettings(str memoString){
    secondsTimeout = -1;
    maxSize = -1;
    if(/maximumSize\(<s:[0-9]+>\)/ := memoString) {
        maxSize = toInt(s);
    }
    if(/expireAfter\(params:<s:[^)]*>\)/ := memoString){
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


JCode trans(MuFunction fun, JGenie jg){
    iprintln(fun);
    println("trans: <fun.src>, <jg.getModuleLoc()>");
    
    if(!isContainedIn(fun.src, jg.getModuleLoc()) )return "";
    
    if(ignoreCompiler(fun.tags)) return "";
    
    ftype = fun.ftype;
    jg.setFunction(fun);
    shortName = getJavaName(getUniqueFunctionName(fun));
    visibility = isSyntheticFunctionName(shortName) ? "private " : "public ";
    uncheckedWarning = "";
    if(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals) := ftype){
        returnType = atype2javatype(ftype.ret);
        <argTypes, constantKwpDefaults, nonConstantKwpDefaults> = getArgTypes(fun, jg);
        memoCache = "";
        if(fun.isMemo){
            <secondsTimeout, maxSize> = getMemoSettings(fun.tags["memo"] ? "");
            memoCache = "private final ExpiringFunctionResultCache\<IValue\> $memo_<shortName> = new ExpiringFunctionResultCache\<IValue\>(<secondsTimeout>, <maxSize>);\n";
        }
        body = trans2Void(fun.body, jg);
        containsVisit = /muVisit(_,_,_,_,_) := fun.body;
        if(containsVisit){
            body = "try {
                   '    <body>
                   '} catch (ReturnFromTraversalException e) {
                   '    return (<returnType>) e.getValue();
                   '}\n";
        }
        kwpActuals = "";
        if(!isEmpty(fun.keywordParameterRefs) && !contains(argTypes, "$kwpActuals")){
            kwpActuals = "java.util.Map\<java.lang.String,IValue\> $kwpActuals";
        }
        argTypes = isEmpty(argTypes) ? kwpActuals : (((isEmpty(kwpActuals) || contains(argTypes, "$kwpActuals")) ? argTypes : "<argTypes>, <kwpActuals>"));
        return isEmpty(kwFormals) ? "<memoCache><visibility><returnType> <shortName>(<argTypes>){
                                    '    <body>
                                    '}"
                                  : "<constantKwpDefaults><memoCache>
                                    '<visibility><returnType> <shortName>(<argTypes>){
                                    '    <nonConstantKwpDefaults>
                                    '    <body>
                                    '}";
    //} else
    //if(acons(AType adt, list[AType] fields, list[Keyword] kwFields) := ftype){
    //    returnType = "IConstructor";
    //    qname = getJavaName(ftype.label);
    //    argTypes = intercalate(", ", [ "<atype2javatype(f)> <varName(fields[i], jg)>" | i <- index(fields)]);
    //    kwpActuals = "java.util.Map\<java.lang.String,IValue\> $kwpActuals";
    //    kwpDefaults = fun.kwpDefaults;
    //    constantKwpDefaults = "";
    //    nonConstantKwpDefaults = "";
    //    mapCode = "Maps.builder()<for(<str key, AType tp, MuExp defaultExp> <- kwpDefaults){>.key(\"<key>\").value(<trans(defaultExp,jg)>)<}>.build();\n";
    //    if(!isEmpty(kwFields)){
    //        uncheckedWarning = "@SuppressWarnings(\"unchecked\")";
    //        argTypes = isEmpty(argTypes) ? kwpActuals : "<argTypes>, <kwpActuals>";
    //        if(constantDefaults(kwpDefaults)){
    //            kwpDefaultsName = "<qname>_$kwpDefaults";
    //            jg.setKwpDefaults(kwpDefaultsName);
    //            constantKwpDefaults = "final java.util.Map\<java.lang.String,IValue\> <kwpDefaultsName> = <mapCode>";
    //         } else {
    //            jg.setKwpDefaults("$kwpDefaults");
    //            nonConstantKwpDefaults =  "java.util.Map\<java.lang.String,IValue\> $kwpDefaults = <mapCode>";
    //         }   
    //    }
    //    return "<constantKwpDefaults>
    //           '<uncheckedWarning>
    //           '<returnType> <qname>(<argTypes>){
    //           '     <nonConstantKwpDefaults>
    //           '     <trans(fun.body, jg)>
    //           '}";
    } else
        throw "trans MuFunction: <ftype>";
}

JCode transGetter(MuFunction fun, JGenie jg){
    ftype = fun.ftype;
    jg.setFunction(fun);
    shortName = getJavaName(getUniqueFunctionName(fun));
    
    if(afunc(AType ret, acons(AType adt, list[AType] fields, list[Keyword] kwFields), []) := ftype){
        returnType = atype2javatype(ftype.ret);
        <argTypes, constantKwpDefaults, nonConstantKwpDefaults> = getArgTypes(fun, jg);
        return isEmpty(kwFormals) ? "public <returnType> <shortName>(<argTypes>){
                                    '    <trans2Void(fun.body, jg)>
                                    '}"
                                  : "<constantKwpDefaults>
                                    'public <returnType> <shortName>(<argTypes>){
                                    '    <nonConstantKwpDefaults>
                                    '    <trans2Void(fun.body, jg)>
                                    '}";
     }
     throw "transGetter: <ftype>";

}

JCode call(MuFunction fun, list[str] actuals, JGenie jg){
    return "<fun.qname>(<intercalate(", ", actuals)>)";
}

default JCode trans(MuExp exp, JGenie jg){
    throw "Cannot translate <exp>";
}

// ---- muCon -----------------------------------------------------------------                                       

JCode trans(muCon(value v), JGenie jg) = jg.shareConstant(v);

JCode trans(muATypeCon(AType t, map[AType, set[AType]] definitions), JGenie jg) {
    return jg.shareATypeConstant(t, definitions);
} 
                      
JCode trans(muFun(loc uid, AType ftype), JGenie jg){
   
    fun = muFunctionsByLoc[uid];
    //ftype = fun.ftype;
    uid = fun.src;
    //ftype = jg.getType(uid);
    externalVars = jg.getExternalVars(uid);
    currentFun = jg.getFunction();
    externalVarsCurrentFun = jg.getExternalVars(currentFun.src);
    
    nformals = size(ftype.formals);
    sep = nformals > 0 ? "," : "";
    
    funInstance = "new FunctionInstance<nformals>\<IValue<sep><intercalate(",", ["IValue" | ft <- ftype.formals])>\>";
    
    bare_actuals = intercalate(", ", ["$<i>" | i <- [0..nformals]]);
    actuals = intercalate(", ", ["(<atype2javatype(ftype.formals[i])>)$<i>" | i <- [0..nformals]]);
    
    ext_actuals = actuals;
    if(!isEmpty(externalVars)){
           ext_actuals = intercalate(", ", [ "new ValueRef\<<jtype>\>(<varName(var, jg)>)" | var <- externalVars, jtype := atype2javatype(var.atype)]);
           ext_actuals = isEmpty(actuals) ? ext_actuals : "<actuals>, <ext_actuals>";
    }
    return "<funInstance>((<bare_actuals>) -\> { return <jg.getAccessor(uid)>(<ext_actuals>); })";
}          
// ---- muOFun ----------------------------------------------------------------
       
JCode trans(muOFun(str fuid, AType ftype), JGenie jg){
    overloading = false;
    fname = "??";
    if(muFunctions[fuid]?){
        fun = muFunctions[fuid];
        fname = jg.finalResolverName(fuid, ftype);
    } else {
        overloading = true;
        fname = jg.finalResolverName(fuid, ftype);
    }
    nformals = size(getFormals(ftype));
    sep = nformals > 0 ? "," : "";
    
    funInstance = "new FunctionInstance<nformals>\<<"IValue"><sep><intercalate(",", ["IValue" | ft <- getFormals(ftype)])>\>";
    
    bare_formals = intercalate(", ", ["$<i>" | i <- [0..nformals]]);
    formals = intercalate(", ", ["(<atype2javatype(getFormals(ftype)[i])>)$<i>" | i <- [0..nformals]]);
    ext_formals = formals;
    if(!overloading){
        fun = muFunctions[fuid];
        if(!isEmpty(fun.externalVars)){
           ext_actuals = intercalate(", ", [varName(v, jg) | v <- fun.externalVars]);
           formals = isEmpty(formals) ? ext_actuals : "<actuals>, <ext_actuals>";
        }
    }
    return "<funInstance>((<bare_formals>) -\> { return <fname>(<formals>); })";
}

// ---- muConstr --------------------------------------------------------------

str trans(muConstr(AType ctype), JGenie jg){
    nformals = size(ctype.fields);
    sep = nformals > 0 ? "," : "";
    
    funInstance = "new FunctionInstance<nformals>\<<"IValue"><sep><intercalate(",", ["IValue" | ft <- ctype.fields])>\>";
    
    bare_formals = ["$<i>" | i <- [0..nformals]];
    return "<funInstance>((<intercalate(", ", bare_formals)>) -\> { return <makeConstructorCall(ctype,  bare_formals, hasKeywordParameters(ctype) || jg.hasCommonKeywordFields(ctype) ? [kwpActuals] : [], jg)>; })";
}

// Variables

//// ---- muModuleVar -----------------------------------------------------------
//
//JCode trans(var:muModuleVar(str name, AType atype), JGenie jg{
//    return name;
//}

// ---- muVar -----------------------------------------------------------------

str varName(muVar(str name, str fuid, int pos, AType atype), JGenie jg){
    return (name[0] != "$") ? "<getJavaName(name)><(pos >= 0 || name == "_") ? "_<abs(pos)>" : "">" : getJavaName(name);
}
        
JCode trans(var:muVar(str name, str fuid, int pos, AType atype), JGenie jg){
   return jg.isRef(var) && pos >= 0 ? "<varName(var, jg)>.getValue()" 
                                    : ( pos >= 0 ? varName(var, jg)
                                                 : "<fuid == jg.getFunctionName() ? "" : fuid == jg.getModuleName() ? "$me." : "<module2field(fuid)>."><varName(var, jg)>"
                                      );
       //= jg.isExternalVar(var) && pos >= 0 ? "<varName(var, jg)>.value" : varName(var, jg);
}
// ---- muTmpIValue -----------------------------------------------------------------

JCode trans(var: muTmpIValue(str name, str fuid, AType atype), JGenie jg)
    = jg.isRef(var) ? "<name>.getValue()" : name;
        //= jg.isExternalVar(var) ? "<name>.value" : name;
    
JCode trans(var: muTmpNative(str name, str fuid, NativeKind nkind), JGenie jg)
    = jg.isRef(var) ? "<name>.getValue()" : name;
      //= jg.isExternalVar(var) ? "<name>.value" : name;
  
// ---- muVarDecl --------------------------------------------------------------

JCode trans(muVarDecl(v: muVar(str name, str fuid, int pos, AType atype)), JGenie jg){
    jtype = atype2javatype(atype);
    return jg.isRef(v) ? "ValueRef\<<jtype>\> <varName(v, jg)> = null;\n"
                       : "<jtype> <varName(v, jg)> = null;\n";  
}

JCode trans(muVarDecl(v: muTmpIValue(str name, str fuid, AType atype)), JGenie jg){
    jtype = atype2javatype(atype);
    return jg.isRef(v) ? "ValueRef\<<jtype>\> <name> = null;\n"
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

JCode trans(muVarInit(v: muVar(str name, str fuid, int pos, AType atype), MuExp exp), JGenie jg){
    jtype = atype2javatype(atype);
    if(exp == muNoValue()){
        return jg.isRef(v) ? "ValueRef\<<jtype>\> <varName(v, jg)> = null;\n"
                           : "<jtype> <varName(v, jg)> = null;\n";  
    } else {    
        return jg.isRef(v) ? "final ValueRef\<<jtype>\> <varName(v, jg)> = new ValueRef\<<jtype>\>(<transWithCast(atype,exp,jg)>);\n"
                           : "<jtype> <varName(v, jg)> = (<jtype>)<parens(trans(exp, jg))>;\n";  
    }
}

JCode trans(muVarInit(v: muTmpIValue(str name, str fuid, AType atype), MuExp exp), JGenie jg){
    jtype = atype2javatype(atype);
    return jg.isRef(v) ? "final ValueRef\<<jtype>\> <name> = new ValueRef\<<jtype>\>(<transWithCast(atype,exp,jg)>);\n"
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
     nativeGuardedIValue()  : <"GuardedIValue", "GuardedIValueRef">
     );
     
str native2ivalue(nativeInt(), str exp) = exp;
//str native2ivalue(nativeInt(), str exp) = "$VF.integer(<exp>)";
//str native2ivalue(nativeBool(), str exp) = "$VF.bool(<exp>)";
//str native2ivalue(nativeListWriter(), str exp) = "<exp>.done()";
//str native2ivalue(nativeSetWriter(), str exp) = "<exp>.done()";
//str native2ivalue(nativeLMapWriter(), str exp) = "<exp>.done()";
//str native2ivalue(nativeLStrWriter(), str exp) = "$VF.string(<exp>.toString()";

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

 JCode trans(muConInit(v:muVar(str name, str fuid, int pos, AType atype), MuExp exp), JGenie jg){
    jtype = atype2javatype(atype);
    if(jg.isRef(v)){  
        return "final ValueRef\<<jtype>\> <varName(v, jg)> = new ValueRef\<<jtype>\>(<transWithCast(atype,exp,jg)>);\n";
    }
    return "<jtype> <varName(v, jg)> = <transWithCast(atype, exp, jg)>;\n";
}
    
JCode trans(muConInit(v:muTmpIValue(str name, str fuid, AType atype), MuExp exp), JGenie jg){
    jtype = atype2javatype(atype);
    return jg.isRef(v) ? "final ValueRef\<<jtype>\> <varName(v,jg)> = new ValueRef\<<jtype>\>(<transWithCast(atype,exp,jg)>);\n"
                       : "final <jtype> <name> = <transWithCast(atype, exp, jg)>;\n";
}

JCode trans(muConInit(var:muTmpNative(str name, str fuid, NativeKind nkind), MuExp exp), JGenie jg){
    rhs = muCon(value v) := exp ? "<v>" : trans(exp, jg);
    <base, ref> = native2ref[nkind];
    return jg.isRef(var) ? "final <ref> <varName(var, jg)> = new <ref>(<rhs>);\n"
                         : "final <base> <name> = <rhs>;\n";
}

str transWithCast(AType atype, con:muCon(c), JGenie jg) = trans(con, jg);

str transWithCast(AType atype, kwp:muKwpActuals(_), JGenie jg) = trans(kwp, jg);

default str transWithCast(AType atype, MuExp exp, JGenie jg) {
    code = trans(exp, jg);
    if(producesNativeBool(exp)){
        return "$VF.bool(<code>)";
    }
    if(producesNativeInt(exp)){
        return "$VF.integer(<code>)";
    }
    //if(producesNativeString(exp)){
    //    return "$VF.string(<code>)";
    //}
    
    if(producesFunctionInstance(code)){
        return code;
    }
      
    exptype = getType(exp);
    isequivalent = false;
    try {
         isequivalent = equivalent(exptype,atype);
    } catch _ : /* ignore failure */;
    
    return isequivalent ? code : "((<atype2javatype(atype)>)<parens(code)>)";
}

bool producesFunctionInstance(str code)
    = startsWith(code, "new FunctionInstance");

// ---- muAssign --------------------------------------------------------------

JCode trans(muAssign(v:muVar(str name, str fuid, int pos, AType atype), MuExp exp), JGenie jg){
    return jg.isRef(v) ? "<varName(v, jg)>.setValue(<transWithCast(atype, exp, jg)>);\n"
                       : "<varName(v, jg)> = <transWithCast(atype, exp, jg)>;\n";
}
    
JCode trans(muAssign(v:muTmpIValue(str name, str fuid, AType atype), MuExp exp), JGenie jg)
    = jg.isRef(v) ? "<name>.setValue(<trans(exp, jg)>);\n"
                  : "<name> = <trans(exp, jg)>;\n";

JCode trans(muAssign(v:muTmpNative(str name, str fuid, NativeKind nkind), MuExp exp), JGenie jg)
    = jg.isRef(v) ? "<name>.setValue(<trans2Native(exp, nkind, jg)>);\n"
                  : "<name> = <trans2Native(exp, nkind, jg)>;\n";

// muGetAnno
JCode trans(muGetAnno(MuExp exp, AType resultType, str annoName), JGenie jg)
    = "$annotation_get(<trans(exp, jg)>,\"<annoName>\")";

// muGuardedGetAnno
JCode trans(muGuardedGetAnno(MuExp exp, AType resultType, str annoName), JGenie jg)
    = "$guarded_annotation_get(<trans(exp, jg)>,\"<annoName>\")";
    
// muSetAnno
JCode trans(muSetAnno(MuExp exp, AType resultType, str annoName, MuExp repl), JGenie jg)
    = "<trans(exp, jg)>.asWithKeywordParameters().setParameter(\"<annoName>\",<trans(repl, jg)>)";

// Call/Apply/return      

JCode trans(muCall(MuExp fun, AType ftype, list[MuExp] largs, lrel[str kwpName, MuExp exp] kwargs), JGenie jg){

    argTypes = getFunctionOrConstructorArgumentTypes(ftype);
    //actuals = getActuals(getFunctionOrConstructorArgumentTypes(ftype), largs, jg);
    
    <actuals, kwactuals> = getPositionalAndKeywordActuals(ftype, largs, kwargs, jg);
    
    if(muConstr(AType ctype) := fun){
        <actuals, kwactuals> = getPositionalAndKeywordActuals(ctype, largs, kwargs, jg);
        return makeConstructorCall(ctype, actuals, kwactuals, jg);        
    }
    //if(muConstrCompanion(str fname) := fun){
    //    return call(muFunctions[fname], actuals, jg);
    //}
    if(muCon(str s) := fun){
        if(map[str,value] kwmap := actuals[-1]){
            actuals = getActuals(argTypes, largs, jg);
            return makeNode(s, actuals[0..-1], keywordParameters = kwmap);
        }
        throw "muCall: kwmap, <actuals>";
    }
    if(muVar(str name, str fuid, int pos, AType atype) := fun){
        <actuals, kwactuals> = getPositionalAndKeywordActuals(ftype, largs, kwargs, jg);
        return "<trans(fun, jg)>(<intercalate(", ", actuals + kwactuals)>)";
    }
    
    <actuals, kwactuals> = getPositionalAndKeywordActuals(ftype, largs, kwargs, jg);
    //kwActuals = "";
    //
    //if(!isEmpty(ftype.kwFormals)){
    //    if(muKwpActuals(_) := largs[-1]){
    //        kwActuals = trans(largs[-1], jg);
    //        largs = largs[..-1];
    //    }
    //}
    //varActuals = "";
    //if(ftype.varArgs){
    //    n = size(ftype.formals);
    //    varElemType = getElementType(ftype.formals[-1]);
    //    vargs = [ trans(e, jg) | e <- largs[n -1 .. ] ];
    //    argTypes = argTypes[ .. n - 1];
    //    if(isEmpty(vargs)){
    //        varActuals = "$VF.list()";
    //    } else {
    //        lastArgIsList = size(largs) == n && isListType(getType(largs[-1]));
    //        largs = largs[ .. n - 1];
    //        varActuals = lastArgIsList ? intercalate(",", vargs) : "$VF.list(<intercalate(",", vargs)>)";
    //    }
    //}
    //actuals = getActuals(argTypes, largs, jg);
    //if(ftype.varArgs){
    //    actuals += varActuals;
    //}
    //if(!isEmpty(kwActuals)){
    //    actuals += kwActuals;
    //}
    
    all_actuals = actuals + kwactuals;
    if(muFun(loc uid, _) := fun){
        externalVars = jg.getExternalVars(uid);
        if(!isEmpty(externalVars)){
           all_actuals += [ varName(var, jg)| var <- externalVars, var.pos >= 0 ];
        }
        
       if(isContainedIn(uid, jg.getModuleLoc())){
         fn = muFunctionsByLoc[uid];
         return "<getJavaName(getUniqueFunctionName(fn))>(<intercalate(", ", all_actuals)>)";
       } else {  
        //if(!isEmpty(ftype.kwFormals) && isEmpty(kwActuals)){
        //    actuals += "Collections.emptyMap()";
        //}  
         call_code = "<jg.getAccessor(uid)>(<intercalate(", ", all_actuals)>)";
         return call_code;
       }
    }
    
    throw "muCall: <fun>";
}

// ---- muOCall3 --------------------------------------------------------------

list[JCode] getActuals(list[AType] argTypes, list[MuExp] largs, JGenie jg)
    = [ i < size(argTypes) ? transWithCast(argTypes[i], largs[i], jg) : trans(largs[i], jg) | i <- index(largs) ];
    
JCode getKwpActuals(lrel[str name, MuExp exp] kwpActuals, JGenie jg){
    if(isEmpty(kwpActuals)) return "Util.kwpMap()"; //"Collections.emptyMap()";
    return "Util.kwpMap(<intercalate(", ",  [ *["\"<getJavaName(key)>\"", trans(exp, jg)] | <str key,  MuExp exp> <- kwpActuals])>)";
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
        return <resulting_actuals, [getKwpActuals(kwpActuals, jg)]>;
    }
}

tuple[list[JCode], list[JCode]] getPositionalAndKeywordActuals(consType:acons(AType adt, list[AType] fields, list[Keyword] kwFields), list[MuExp] actuals, lrel[str name, MuExp exp] kwpActuals, JGenie jg){
    resulting_actuals = getActuals(fields, actuals, jg);
    
     if(isEmpty(consType.kwFields) && !jg.hasCommonKeywordFields(consType)){
        return <resulting_actuals, []>;
     } else {
            return <resulting_actuals, [getKwpActuals(kwpActuals, jg)]>;
    }
}

JCode trans(muOCall3(MuExp fun, AType ftype, list[MuExp] largs, lrel[str kwpName, MuExp exp] kwargs, src), JGenie jg){
println("muOCall3((<fun>, <ftype>, ..., <src>");
    argTypes = getFunctionOrConstructorArgumentTypes(ftype);
    if(muOFun(str fname, AType _) := fun){
        externalVars = jg.getExternalVarsResolver(fname);
        actuals = getActuals(argTypes, largs, jg) + [ varName(var, jg) | var <- sort(externalVars), jtype := atype2javatype(var.atype)];
        return "<jg.getAccessorOverloaded(fname, ftype)>(<intercalate(", ", actuals)>)";
    }
    
    if(muFun(loc uid, _) := fun){
        return "<jg.getAccessor(uid)>(<intercalate(", ", getActuals(argTypes, largs, jg))>)";
    }
    if(muCon(str s) := fun){
        return "$VF.node(<intercalate(", ", getActuals(argTypes, largs, jg))>)";
    }
    
    cst = (getResult(ftype) == avoid()) ? "" : "(<atype2javatype(getResult(ftype))>)";

    return "<cst><trans(fun, jg)>.call(<intercalate(", ", getActuals(argTypes, largs, jg))>)";
}

// ---- muGetKwField ------------------------------------------------------

JCode trans(muGetKwField(AType resultType,  adtType:aadt(_,_,_), MuExp cons, str fieldName), JGenie jg){
     return "$get_<adtType.adtName>_<getJavaName(fieldName)>(<transWithCast(adtType, cons, jg)>)";
}

JCode trans(muGetKwField(AType resultType,  consType:acons(AType adt, list[AType] fields, list[Keyword] kwFields), MuExp cons, str fieldName), JGenie jg){
     isConsKwField = fieldName in {kwf.fieldType.label | kwf <- kwFields};
     return isConsKwField ? "$get_<adt.adtName>_<consType.label>_<getJavaName(fieldName)>(<transWithCast(consType, cons, jg)>)"
                          : "$get_<adt.adtName>_<getJavaName(fieldName)>(<transWithCast(consType, cons, jg)>)";
}
// ---- muGetField ---------------------------------------------------------

JCode trans(muGetField(AType resultType, aloc(), MuExp exp, str fieldName), JGenie jg)
    = "$aloc_get_field(<transWithCast(aloc(),exp,jg)>, \"<fieldName>\")";

JCode trans(muGetField(AType resultType, adatetime(), MuExp exp, str fieldName), JGenie jg)
    = "$adatetime_get_field(<transWithCast(adatetime(),exp,jg)>, \"<fieldName>\")";
 
JCode trans(muGetField(AType resultType, anode(_), MuExp exp, str fieldName), JGenie jg)
    = "$anode_get_field(<transWithCast(anode([]),exp,jg)>, \"<getJavaName(fieldName)>\")";

JCode trans(muGetField(AType resultType, adt:aadt(_,_,_), MuExp exp, str fieldName), JGenie jg)
    = "$aadt_get_field(<transWithCast(adt,exp,jg)>, \"<getJavaName(fieldName)>\")";
            
JCode trans(muGetField(AType resultType, areified(AType atype), MuExp exp, str fieldName), JGenie jg)
    = "$areified_get_field(<trans(exp,jg)>, \"<getJavaName(fieldName)>\")";

default JCode trans(muGetField(AType resultType, AType consType, MuExp cons, str fieldName), JGenie jg){
    base = transWithCast(consType, cons, jg);
    qFieldName = "\"<fieldName>\"";
    println("muGetField: <resultType>, <consType>, <fieldName>");
    isConsKwField = fieldName in {kwf.fieldType.label | kwf <- consType.kwFields};
    return isConsKwField ? "$get_<consType.adt.adtName>_<getJavaName(consType.label)>_<getJavaName(fieldName)>(<base>)"
                         : "$get_<consType.adt.adtName>_<getJavaName(fieldName)>(<base>)";
}
 
 // ---- muGuardedGetField -------------------------------------------------
 
 JCode trans(muGuardedGetField(AType resultType, aloc(), MuExp exp, str fieldName), JGenie jg)
    = "$guarded_aloc_get_field(<trans(exp,jg)>, \"<fieldName>\")";

JCode trans(muGuardedGetField(AType resultType, adatetime(), MuExp exp, str fieldName), JGenie jg)
    = "$guarded_adatetime_get_field(<trans(exp,jg)>, \"<fieldName>\")";
    
JCode trans(muGuardedGetField(AType resultType, anode(_), MuExp exp, str fieldName), JGenie jg)
    = "$guarded_anode_get_field(<trans(exp,jg)>, \"<getJavaName(fieldName)>\")";
    
JCode trans(muGuardedGetField(AType resultType, atuple(_), MuExp exp, str fieldName), JGenie jg)
    = "$guarded_atuple_get_field(<trans(exp,jg)>, \"<getJavaName(fieldName)>\")";

JCode trans(muGuardedGetField(AType resultType, aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole), MuExp exp, str fieldName), JGenie jg)
    = "$guarded_aadt_get_field(<trans(exp,jg)>,  \"<getJavaName(fieldName)>\")";
 
default JCode trans(muGuardedGetField(AType resultType, consType:acons(AType adt, list[AType] fields, list[Keyword] kwFields), MuExp cons, str fieldName), JGenie jg){
    base = trans(cons, jg);
    qFieldName = "\"<getJavaName(fieldName)>\"";
    for(field <- fields){
        if(fieldName == field.label){
            return "((<atype2javatype(field)>)<base>.get(<qFieldName>))";
        }
    }
   
    for(<AType kwType, Expression exp> <- kwFields){
        if(fieldName == kwType.label){
            expCode = trans(exp, jg);
            if(muCon(_) := expCode){
                return "<base>.asWithKeywordParameters().hasParameter(<qFieldName>) ? <base>.asWithKeywordParameters().getParameter(<qFieldName>) : <expCode>";
            } else {
                return "<base>.asWithKeywordParameters().getParameter(<qFieldName>)";
            }
        }
    }
     throw "muGuardedGetField <resultType>, <consType>, <fieldName>";
 }
// ---- muGuardedGetKwField -------------------------------------------------

// ---- muSetField ------------------------------------------------------------

JCode trans(muSetField(AType resultType, aloc(), MuExp baseExp, str fieldName, MuExp repl), JGenie jg)
    = "$aloc_field_update(<transWithCast(aloc(), baseExp, jg)>, \"<fieldName>\", <trans(repl, jg)>)";

JCode trans(muSetField(AType resultType, adatetime(), MuExp baseExp, str fieldName, MuExp repl), JGenie jg)
    = "$adatetime_field_update(<transWithCast(adatetime(), baseExp, jg)>, \"<fieldName>\", <trans(repl, jg)>)";
    
JCode trans(muSetField(AType resultType, anode(_), MuExp baseExp, str fieldName, MuExp repl), JGenie jg)
    = "$anode_field_update(<transWithCast(anode([]), baseExp, jg)>, \"<getJavaName(fieldName)>\", <trans(repl, jg)>)";    

default JCode trans(muSetField(AType resultType, AType baseType, MuExp baseExp, str fieldName, MuExp repl), JGenie jg)
    = "<trans(baseExp, jg)>.set(\"<getJavaName(fieldName)>\", <trans(repl, jg)>)";
    
JCode trans(muSetField(AType resultType, AType baseType, MuExp baseExp, int fieldIndex, MuExp repl), JGenie jg)
    = "$atuple_update(<trans(baseExp, jg)>, <fieldIndex>,  <trans(repl, jg)>)" when isTupleType(resultType);
    
JCode trans(muSetField(AType resultType, aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole), MuExp baseExp, str fieldName, MuExp repl), JGenie jg)
    = "$aadt_field_update(<trans(baseExp,jg)>,  \"<getJavaName(fieldName)>\", <trans(repl, jg)>)";

    
//// ---- muCallPrim2 -----------------------------------------------------------
//
//JCode trans(muCallPrim2(str name, loc src), JGenie jg){
//    return transPrim(name, [], jg);
//}

// ---- muCallPrim3 -----------------------------------------------------------

JCode trans(muCallPrim3(str name, AType result, list[AType] details, list[MuExp] exps, loc src), JGenie jg){
    actuals = transPrimArgs(name, result, details, exps, jg);
    return transPrim(name, result, details, actuals, jg);
}

JCode trans(muCallJava(str name, str class, AType funType, int reflect, list[MuExp] largs, str enclosingFun), JGenie jg){
    jg.addImportedLibrary(class);
  
    actuals = [ trans(arg, jg) | arg <- largs ];
    
    if(!isEmpty(funType.kwFormals)){
        kwpDefaultsVar = jg.getKwpDefaults();
        kwpActuals = "$kwpActuals"; 
        for(kwFormal <- funType.kwFormals){
            escapedKwName = getJavaName(kwFormal.fieldType.label);
            actuals += "(<atype2javatype(kwFormal.fieldType)>)(<kwpActuals>.containsKey(\"<escapedKwName>\") ? <kwpActuals>.get(\"<escapedKwName>\") : <kwpDefaultsVar>.get(\"<escapedKwName>\"))";
        }
    }
    if(reflect == 1){
        actuals += "rex";
    }
    code = "<getClassName(class)>.<name>(<intercalate(", ", actuals)>)";
    return funType.ret == avoid() ? code : "(<atype2javatype(funType.ret)>)<code>";
}

// ---- muReturn0 -------------------------------------------------------------

JCode trans(muReturn0(), JGenie jg){
    return "return;";
}

JCode trans(muReturn0FromVisit(), JGenie jg)
    = "$traversalState.setLeavingVisit(true);
      'return;\n";

str semi(str code)
    = /[;}][ \n]*/ := code ? code : "<code>;";
   // = (endsWith(code, ";") || endsWith(code, ";\n") || endsWith(code, "}") || endsWith(code, "}\n")) ? code : "<code>;";

str semi_nl(str code)
    = (endsWith(code, ";") || endsWith(code, ";\n") || endsWith(code, "}") || endsWith(code, "}\n")) ? code : "<code>;\n";
 
JCode trans2Void(MuExp exp, JGenie jg)
   = "/* <exp> */"
   when getName(exp) in {"muCon", "muVar", "muTmp"};

JCode trans2Void(MuExp exp, JGenie jg)
   =  semi_nl(trans(exp, jg))
   when getName(exp) in {"muOCall3", "muCall", "muReturn0", "muReturn1"};
   
JCode trans2Void(muBlock(list[MuExp] exps), JGenie jg)
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
    return "IValue $visitResult = <trans(v, jg)>
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
    if(isEmpty(kwpActuals)) return "Collections.emptyMap()";
    return "Util.kwpMap(<intercalate(", ",  [ *["\"<key>\"", trans(exp, jg)] | <str key,  MuExp exp> <- kwpActuals])>)";
}

// ---- muKwpMap --------------------------------------------------------------

JCode trans(muKwpMap(lrel[str kwName, AType atype, MuExp defaultExp] kwpDefaults), JGenie jg){
    kwpDefaultsVar = jg.getKwpDefaults();
    kwpActuals = "$kwpActuals"; 
    return "
           '    <kwpActuals>.isEmpty() ? <kwpDefaultsVar>
           '                           : Util.kwpMap(<for(<str key, AType atype, MuExp exp> <- kwpDefaults, muCon(_) !:= exp){>\"<key>\", <kwpActuals>.containsKey(\"<getJaveName(key)>\") ? ((<atype2javatype(atype)>) <kwpActuals>.get(\"<getJavaName(key)>\")) : <transCast(atype,exp,jg)>)<}>)";
}

// ---- muVarKwp --------------------------------------------------------------

JCode trans(var:muVarKwp(str name, str fuid, AType atype),  JGenie jg)
    = "((<atype2javatype(atype)>) ($kwpActuals.containsKey(\"<getJavaName(name)>\") ? $kwpActuals.get(\"<getJavaName(name)>\") : <jg.getKwpDefaults()>.get(\"<getJavaName(name)>\")))";

JCode trans(muAssign(muVarKwp(str name, str fuid, AType atype), MuExp exp), JGenie jg)
    = "$kwpActuals.put(\"<getJavaName(name)>\", <trans(exp, jg)>);\n";

JCode trans(muIsKwpDefined(MuExp exp, str kwpName), JGenie jg)
    = "<trans(exp, jg)>.asWithKeywordParameters().hasParameter(\"<getJavaName(kwpName)>\")";

JCode trans(muHasKwp(MuExp exp, str kwName), JGenie jg)
    = "<trans(exp, jg)>.asWithKeywordParameters().hasParameter(\"<getJavaName(kwName)>\")";

JCode trans(muGetKwp(MuExp exp, AType atype, str kwpName), JGenie jg){
   if(acons(AType adt, list[AType] fields, list[Keyword] kwFields) := atype){
        return "$get_<atype.adtName>_<getJavaName(kwpName)>(<trans(exp, jg)>)";
   } else if(anode(_) := atype){
       return "<trans(exp, jg)>.asWithKeywordParameters().getParameter(\"<getJavaName(kwpName)>\")";
   } else if(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole) := atype){
       return "$get_<adtName>_<getJavaName(kwpName)>(<trans(exp, jg)>)";
   }
   throw "muGetKwp: <atype>, <kwpName>";
}
// ---- muGetKwFieldFromConstructor

JCode trans(muGetKwFieldFromConstructor(AType resultType, MuExp exp, str kwpName), JGenie jg)
    = "<trans(exp, jg)>.asWithKeywordParameters().getParameter(\"<getJavaName(kwpName)>\")";
 
// ---- muGetFieldFromConstructor

JCode trans(muGetFieldFromConstructor(AType resultType, AType consType, MuExp exp, str fieldName), JGenie jg){
     i = indexOf([fld.label | fld <- consType.fields], fieldName);
     return "<trans(exp, jg)>.get(<i>)";
 }

JCode trans(muInsert(AType atype, MuExp exp), JGenie jg)
    = "$traversalState.setMatchedAndChanged(true, true);
      'return <trans(exp, jg)>;\n";

// Assignment, If and While

JCode trans(muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart), JGenie jg){
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

default JCode trans(muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart), JGenie jg)
   = "(<trans2NativeBool(cond, jg)> ? <trans(thenPart, jg)> : <trans(elsePart, jg)>)";
 
JCode trans(muIf(MuExp cond, MuExp thenPart), JGenie jg){
    return "if(<trans2NativeBool(cond, jg)>){
            '   <trans2Void(thenPart, jg)>
            '}\n";
}

JCode trans(muIfEqualOrAssign(MuExp var, MuExp other, MuExp body), JGenie jg){
    return "if(<trans(var, jg)> == null){
           '    <trans(muAssign(var, other), jg)>
           '    <trans(body, jg)>
           '} else {
           '    <trans(muAssign(var, other), jg)>
           '    if(<trans(var, jg)>.equals(<trans(other, jg)>)){
           '       <trans(body, jg)>
           '    }
           '}\n";
}

JCode trans(muWhileDo(str label, MuExp cond, MuExp body), JGenie jg){
    cond_code = trans2NativeBool(cond, jg);
    if(muEnter(btscope, MuExp exp) := body){
       return
            "<isEmpty(label) ? "" : "<getJavaName(label)>:">
            '//<btscope>:
            '    while(<cond_code>){
            '    <btscope>: 
            '       do {
            '         <trans2Void(exp, jg)>
            '          break <getJavaName(label)>;
            '       } while(false);\n
            '    }\n";
    }
    return "<isEmpty(label) ? "" : "<getJavaName(label)>:">
           '    while(<cond_code>){
           '        <trans2Void(body, jg)>
           '    }\n";
}

JCode trans(muDoWhile(str label, MuExp body, MuExp cond), JGenie jg){
    return "<isEmpty(label) ? "" : "<getJavaName(label)>:">
           '    do{
           '        <trans2Void(body, jg)>
           '    } while(<trans2NativeBool(cond, jg)>);\n";
}

JCode trans(mw: muForAll(str btscope, MuExp var, AType iterType, MuExp iterable, MuExp body), JGenie jg){

    noInnerLoops = false; ///muForAll(_,_,_,_,_) := body;
    iterCode = (muCallPrim3("subsets", _, _, _, _) := iterable ||  muDescendantMatchIterator(_,_) := iterable) ? trans(iterable, jg) : transWithCast(iterType, iterable, jg);

    return
        (muDescendantMatchIterator(_,_) := iterable) ? 
            ("<isEmpty(btscope) ? "" : "<btscope>:">
             'for(IValue <var.name> : <iterCode>){
             '    <trans2Void(body, jg)>
             '}
             '<noInnerLoops ? "throw new RuntimeException(\"muForAll exhausted\");" : "">\n
             ")
        :
            ("<isEmpty(btscope) ? "" : "<btscope>:">
            'for(IValue <var.name>_for : <iterCode>){
            '    <atype2javatype(var.atype)> <var.name> = (<atype2javatype(var.atype)>) <var.name>_for;
            '    <trans2Void(body, jg)>
            '}
            '<noInnerLoops ? "throw new RuntimeException(\"muForAll exhausted\");" : "">\n");
   
}

//JCode trans(muEnter(btscope, muBlock([*exps, muSucceed(btscope)])), JGenie jg)
//    = "<trans(muBlock(exps), jg)>";
    
JCode trans(e:muEnter(btscope, muFail(btscope)), JGenie jg)
    = "/*<e>*/";
    
JCode trans(e:muEnter(btscope, muFailEnd(btscope)), JGenie jg)
    = "/*<e>*/";
    
JCode trans(muEnter(btscope, asg:muAssign(_,_)), JGenie jg)
    = trans(asg, jg);
    
JCode trans(muEnter(btscope,ret: muReturn1(_,_)), JGenie jg)
    = trans(ret, jg);

default JCode trans(muEnter(btscope, MuExp exp), JGenie jg){
    return
      "<isEmpty(btscope) ? "" : "<getJavaName(btscope)>:"> 
      '    do {
      '        <trans2Void(exp, jg)>
      '    } while(false);\n";
}
JCode trans(muSucceed(str label), JGenie jg)
    = "break <getJavaName(label)>;";

JCode trans(muFail(str label), JGenie jg){
    if(startsWith(jg.getFunctionName(), label)){    // TODO:this is brittle, solve in JGenie
        println(jg.getFunction().ftype);
        return jg.getFunction().ftype.ret == avoid() ? "throw new FailReturnFromVoidException();"
                                                     : "return null;";
    }
    //if(/^CASE[0-9]+$/ := label){
    //    return "break <label>;";
    //}
    return "continue <getJavaName(label)>;";   
}
    
JCode trans(muBreak(str label), JGenie jg)
    = "break <getJavaName(label)>;\n";
    
JCode trans(muContinue(str label), JGenie jg)
    = "continue <getJavaName(label)>;\n";

JCode trans(muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp exp), JGenie jg){
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
    
    if(muCon(_) := first) fst = trans(first, jg); else fstContrib = "final <atype2javatype(var.atype)> <fst> = <trans(first, jg)>;\n";
    if(muCon(_) := last) lst = trans(last, jg); else lstContrib = "final <atype2javatype(var.atype)> <lst> = <trans(last, jg)>;\n";
    
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
    
    if(muCon(int f) := first && muCon(int s) := second){
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
    
    loop = "<isEmpty(label) ? "" : "<getJavaName(label)>:">for(<atype2javatype(var.atype)> <var.name> = <fst>; <testCode>; <var.name> = <transPrim("add", var.atype, [var.atype, var.atype], [trans(var,jg), deltaVal], jg)>){
           '    <trans2Void(exp, jg)>}";
           
    if(!isEmpty(deltaContrib)){
        loop =  "if(<dir> ? $lessequal(<delta>, $VF.integer(0)).not().getValue() : $less(<delta>, $VF.integer(0)).getValue()) {
                '   <loop>
                '}";
    }
    return 
    "<fstContrib><lstContrib><dirContrib><deltaContrib>
    '<loop>
    '";
}

JCode trans(muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp exp), JGenie jg){
    return 
    "<isEmpty(label) ? "" : "<getJavaName(label)>:">
    'for(int <var.name> = <ifirst>; <var.name> \<= <trans(last, jg)>; <var.name> += <istep>){
    '   <trans(exp, jg)>
    '}
    '";
}
         
JCode trans(muSwitch(str label, MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint), JGenie jg){
    return "<getJavaName(label)>: switch(Util.getFingerprint(<exp.name>, <useConcreteFingerprint>)){
           '<for(muCase(int fingerprint, MuExp exp1) <- cases){>
           '    case <fingerprint>:
           '        <trans(exp1, jg)>
           '        <trans(defaultExp, jg)>
           '<}>
           '    default: <defaultExp == muBlock([]) ? "" : trans(defaultExp, jg)>
           '}\n
           ";
}

JCode genDescendantDescriptor(DescendantDescriptor descendant, JGenie jg){
    definitions = descendant.definitions;
    
    useConcreteFingerprint = "$VF.bool(<descendant.useConcreteFingerprint>)";
    reachable_atypes = "new io.usethesource.vallang.type.Type[]{<intercalate(", ", [atype2vtype(t) | t <- descendant.reachable_atypes])>}";
    reachable_aprods = "new io.usethesource.vallang.IConstructor[]{<intercalate(", ", [jg.shareATypeConstant(t, definitions) | t <- descendant.reachable_aprods])>}";
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
      '     });\n";
}  

JCode trans(muDescendantMatchIterator(MuExp subject, DescendantDescriptor ddescriptor), JGenie jg)
    = "new DescendantMatchIterator(<trans(subject, jg)>, 
      '    <genDescendantDescriptor(ddescriptor, jg)>)";

JCode trans(muFailCase(str switchName), JGenie jg)
    = "break <switchName>;\n";
    
JCode trans(muSucceedSwitchCase(str switchName), JGenie jg)
    = "break <switchName>;";
    
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
    cache = "$memo_<jg.getFunctionName()>";
    kwpActuals = isEmpty(funType.kwFormals) ? "Collections.emptyMap()" : "$kwpActuals";
    returnCode = funType.ret == avoid() ? "return" : "return (<atype2javatype(funType.ret)>) $memoVal";
    return "final IValue[] $actuals = new IValue[] {<intercalate(",", [trans(arg, jg) | arg <- args])>};
           'IValue $memoVal = <cache>.lookup($actuals, <kwpActuals>);
           'if($memoVal != null) <returnCode>;
           '<trans(body, jg)>";
}

// ---- muMemoReturn ----------------------------------------------------------

JCode trans(muMemoReturn0(AType funType, list[MuExp] args), JGenie jg){
    cache = "$memo_<jg.getFunctionName()>";
    kwpActuals = isEmpty(funType.kwFormals) ? "Collections.emptyMap()" : "$kwpActuals";
    return "$memoVal = $VF.bool(true);
           '<cache>.store($actuals, <kwpActuals>, $memoVal);
           'return;";
}

JCode trans(muMemoReturn1(AType funType, list[MuExp] args, MuExp functionResult), JGenie jg){
    cache = "$memo_<jg.getFunctionName()>";
    kwpActuals = isEmpty(funType.kwFormals) ? "Collections.emptyMap()" : "$kwpActuals";
    return "$memoVal = <trans(functionResult, jg)>;
           '<cache>.store($actuals, <kwpActuals>, $memoVal);
           'return (<atype2javatype(funType.ret)>)$memoVal;";
}
           
// Lists of expressions

JCode trans(muBlock(list[MuExp] exps), JGenie jg){
    return "<for(exp <- exps){><trans2Void(exp, jg)><}>";
}
   
JCode trans(muValueBlock(AType t, list[MuExp] exps), JGenie jg){
    return "<for(exp <- exps[0..-1]){><trans2Void(exp, jg)><}> 
           '<trans(exps[-1], jg)>";
}

// Exceptions
       
JCode trans(muThrow(muTmpNative(str name, str fuid, nativeException()), loc src), JGenie jg){
    return "throw <name>;";
}

JCode trans(muThrow(muCall(muConstr(ac: acons(aadt("RuntimeException",[],dataSyntax()), _, _)), acons(_, _, _), list[MuExp] args, list[Keyword] kwargs), loc src), JGenie jg){
    exceptionName = ac.label == "IO" ? "io" : toLowerCase(ac.label[0]) + ac.label[1..];
    return "throw RuntimeExceptionFactory.<exceptionName>(<intercalate(",", [trans(arg, jg) | arg <- args])>);";
}

default JCode trans(muThrow(MuExp exp, loc src), JGenie jg){
    return "throw new Throw(<trans(exp, jg)>, (AbstractAST)null, null);";
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
           '    <trans(body, jg)>
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
    = "<trans(exp, jg)><producesNativeInt(exp) ? "" : ".intValue()">";
    
JCode trans2IInteger(MuExp exp, JGenie jg)
    = producesNativeInt(exp) ? "$VF.integer(<trans(exp, jg)>)" : trans(exp, jg);
    
JCode trans2IBool(MuExp exp, JGenie jg)
    = producesNativeBool(exp) ? "$VF.bool(<trans(exp, jg)>)" : trans(exp, jg);

// ----

JCode trans2NativeStr(muCon(str s), JGenie jg)
    = "\"" + escapeForJ(s) + "\"";
default JCode trans2NativeStr(MuExp exp, JGenie jg)
    = "<transWithCast(astr(), exp, jg)>.getValue()";
        
 JCode trans2NativeRegExpStr(muCon(str s), JGenie jg){
    res = "\"" + escapeForJRegExp(s) + "\"";
    return res;
} 
default JCode trans2NativeRegExpStr(MuExp exp, JGenie jg){
    return trans(exp, jg);
    //return trans(muCallPrim3("str_escape_for_regexp", astr(), [astr()], [exp], |unknown:///|), jg);
    //return transWithCast(astr(), exp, jg);
}

// -----

JCode trans(muRequireNonNegativeBound(MuExp idx), JGenie jg)
    = "if(<trans2NativeInt(idx, jg)> \<= 0){
      ' throw RuntimeExceptionFactory.indexOutOfBounds(<trans2IInteger(idx, jg)>);
      '}\n";
 
JCode trans(muEqual(MuExp exp1, MuExp exp2), JGenie jg)
    = "<trans(exp1, jg)>.equals(<trans(exp2, jg)>)";
    
JCode trans(muMatch(MuExp exp1, MuExp exp2), JGenie jg)
    = "<trans(exp1, jg)>.match(<trans(exp2, jg)>)";
      
JCode trans(muEqualNativeInt(MuExp exp1, MuExp exp2), JGenie jg)
    = "<trans2NativeInt(exp1, jg)> == <trans2NativeInt(exp2, jg)>";
    
JCode trans(muValueIsSubType(MuExp exp, AType tp), JGenie jg){
    return !isVarOrTmp(exp) && exp has atype && exp.atype == tp ? "true"
                      : "<trans(exp, jg)>.getType().isSubtypeOf(<jg.shareType(tp)>)";
}
JCode trans(muValueIsSubTypeOfValue(MuExp exp1, MuExp exp2), JGenie jg)
    ="<trans(exp1, jg)>.getType().isSubtypeOf(<trans(exp2, jg)>.getType())";

JCode trans(muValueIsComparable(MuExp exp, AType tp), JGenie jg){
    return !isVarOrTmp(exp) && exp has atype && exp.atype == tp ? "true"
                      : "$isComparable(<trans(exp, jg)>.getType(), <jg.shareType(tp)>)";   
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
    t = atype2javatype(atype);
    v = trans(exp, jg);
    switch(consType){
        case aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole):
            return "((IConstructor)<v>).arity() == <arity> && ((IConstructor)<v>).getType() == <getADTName(adtName)>";
        case acons(AType adt, list[AType] fields, list[Keyword] kwFields):
            //return "<v> instanceof IConstructor && ((IConstructor)<v>).arity() == <arity> && ((IConstructor)<v>).getName().equals(\"<name>\")";
            return "((IConstructor)<v>).getConstructorType() == <atype2idpart(consType)>";
        //case anode(_):
        default:
            return "<v> instanceof INode && ((INode)<v>).arity() == <arity> && ((INode)<v>).getName().equals(<trans2NativeStr(name,jg)>)";
        //default:
        //    throw "muHasNameAndArity: <atype>, <name>, <arity>, <exp>";
    }
}

JCode trans(muIsInitialized(MuExp exp), JGenie jg){
    return "<trans(exp, jg)> != null";
}

JCode trans(muIsDefinedValue(MuExp exp), JGenie jg)
    = "$is_defined_value(<trans(exp, jg)>)";

JCode trans(muGetDefinedValue(MuExp exp, AType atype), JGenie jg)
    = "((<atype2javatype(atype)>)$get_defined_value(<trans(exp, jg)>))";

JCode trans(muSize(MuExp exp, atype:aset(_)), JGenie jg)
    = "<transWithCast(atype, exp, jg)>.size()";
    
default JCode trans(muSize(MuExp exp, AType atype), JGenie jg){
    return "<transWithCast(atype, exp, jg)>.length()";
}
 
// muHasField
JCode makeConsList(set[AType] consesWithField, JGenie jg){
    return isEmpty(consesWithField) ? "" : ", " + intercalate(", ", [ jg.shareType(tp) | tp <- consesWithField ]);
}

JCode trans(muHasField(MuExp exp, AType tp, str fieldName, set[AType] consesWithField), JGenie jg)
    = isADTType(tp) ? "$aadt_has_field(<trans(exp, jg)>,\"<fieldName>\"<makeConsList(consesWithField, jg)>)" 
                    : "$anode_has_field(<trans(exp, jg)>,\"<fieldName>\")";

// muHasSubcscript
JCode trans(muSubscript(MuExp exp, MuExp idx), JGenie jg){
    return "$subject_subscript(<trans(exp, jg)>, <trans2NativeInt(idx, jg)>)";
}

JCode trans(muIncNativeInt(MuExp var, MuExp exp), JGenie jg)
    = muCon(int n) := exp ? "<trans(var, jg)> += <n>;\n" :  "<trans(var, jg)> += <trans(exp, jg)>;\n";
    
JCode trans(muSubNativeInt(MuExp exp1, MuExp exp2), JGenie jg)
    = "<trans2NativeInt(exp1, jg)> - <trans2NativeInt(exp2, jg)>";
    
JCode trans(muAddNativeInt(MuExp exp1, MuExp exp2), JGenie jg)
    = "<trans2NativeInt(exp1, jg)> + <trans2NativeInt(exp2, jg)>";
    
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
    
JCode trans(muTemplateAdd(MuExp template, muCon(str s)), JGenie jg)
    = "<trans(template, jg)>.addStr(\"<escapeAsJavaString(s)>\");\n";
    
JCode trans(muTemplateAdd(MuExp template, str s), JGenie jg){
    if(isEmpty(s)) return "";
    return "<trans(template, jg)>.addStr(\"<escapeAsJavaString(s)>\");\n";
}
    
default JCode trans(muTemplateAdd(MuExp template, MuExp exp), JGenie jg){
    if(isStrType(getType(exp))){
        return "<trans(template, jg)>.addStr(<trans2NativeStr(exp,jg)>);\n";
    }
    return "<trans(template, jg)>.addVal(<trans(exp,jg)>);\n";
}

JCode trans(muTemplateClose(MuExp template), JGenie jg)
    = "<trans(template, jg)>.close()";
    
// ---- Misc ------------------------------------------------------------------

JCode trans(muResetLocs(list[int] positions), JGenie jg)
    = "";