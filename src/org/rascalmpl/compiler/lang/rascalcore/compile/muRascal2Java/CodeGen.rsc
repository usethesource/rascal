module lang::rascalcore::compile::muRascal2Java::CodeGen

import lang::rascal::\syntax::Rascal;

import lang::rascalcore::compile::muRascal::AST;

extend lang::rascalcore::check::CheckerCommon;

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
import util::UUID;

import lang::rascalcore::compile::muRascal2Java::Primitives;

import lang::rascalcore::compile::muRascal2Java::JGenie;
import lang::rascalcore::compile::muRascal2Java::Conversions;
import lang::rascalcore::compile::muRascal2Java::Tests;
import lang::rascalcore::compile::muRascal2Java::Interface;
import lang::rascalcore::compile::muRascal2Java::Resolvers;
//import lang::rascalcore::compile::muRascal2Java::SameJavaType;

import lang::rascalcore::compile::util::Names;

bool debug = false;

// ---- globals ---------------------------------------------------------------

map[str, MuFunction] muFunctions = ();
map[loc, MuFunction] loc2muFunction = ();

// ---- muModule --------------------------------------------------------------

// Generate code and test class for a single Rascal module

tuple[JCode, JCode, JCode, list[value]] muRascal2Java(MuModule m, map[str,TModel] tmodels, map[str,loc] moduleLocs){

    moduleName = m.name;
    locsModule = invertUnique(moduleLocs);
    module_scope = moduleLocs[moduleName];
    tm = tmodels[moduleName];
    //iprintln(tm);
    
    extends = { locsModule[m2loc] | <module_scope, extendPath(), m2loc> <- tmodels[moduleName].paths };
    
    imports = { locsModule[m2loc] | <module_scope, importPath(), m2loc> <- tmodels[moduleName].paths };
    imports += { locsModule[m2loc] | imp <- imports, impLoc := moduleLocs[imp], <impLoc, extendPath(), m2loc> <- tmodels[moduleName].paths};
    
    //for(f <- m.functions){ iprintln(f); }
    muFunctions = (f.uniqueName : f | f <- m.functions);
    loc2muFunction = (f.src : f | f <- m.functions);
    jg = makeJGenie(m, tmodels, moduleLocs, muFunctions);
    resolvers = generateResolvers(moduleName, loc2muFunction, imports, extends, tmodels, moduleLocs, jg);
    
    moduleScopes = range(moduleLocs); //{ s |tm <- range(tmodels), s <- tm.scopes, tm.scopes[s] == |global-scope:///| };
    
    cons_in_module = { def.defInfo.atype | def <- range(tm.definitions), def.idRole == constructorId(), isContainedIn(def.scope, module_scope) };
    adt_in_module = { def.defInfo.atype | def <- range(tm.definitions), def.idRole in dataOrSyntaxRoles, isContainedIn(def.scope, module_scope) };
    
    <adtTypeDecls, adtTypeInits, consTypeDecls, consTypeInits, kwpDecls> = generateTypeStoreAndKwpDecls(adt_in_module, cons_in_module, jg);
    
    bool hasMainFunction = false;
    str mainName = "main";
    AType mainType = avoid();
    MuFunction mainFunction;
    for(f <- m.functions){
        if(isOuterScopeName(f.scopeIn) && isMainName(f.name)) {
            hasMainFunction = true;
            mainFunction = f;
            mainType = f.ftype;
 //           mainName = f.qname;
        }
        jg.addExternalRefs(f.externalRefs);
        jg.addLocalRefs(f.localRefs);
    }
 
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
                        '    // TODO: getBaseClass will generate name collisions if there are more of the same name in different packages\n
                        'final <libclass> <getBaseClass(class)> = $initLibrary(\"<libclass>\"); 
                        '<}>";
    
    module_implements =  "implements <intercalate(",", [ "\n\t<module2interface(ext)>" | ext <- moduleName + extends])>";
                        
    module_imports    = "<for(imp <- imports + extends, contains(module2class(imp), ".")){>
                         'import <module2class(imp)>;
                        '<}>";
                       
    imp_ext_decls     = "<for(imp <- imports + extends){>
                        '<getClassRef(imp, moduleName)> <module2field(imp)>;<}>
                        '";
                           
    module_ext_inits  = "<for(ext <- extends){>
                        '<module2field(ext)> = <module2class(ext)>.extend<getBaseClass(ext)>(this);
                        '<}>";
    module_var_inits =  "<for(exp <- m.initialization){><trans(exp, jg)><}>";
    <constant_decls, constant_inits, constants> = jg.getConstants();
  
    class_constructor = "public <className>(){
                        '    this(new ModuleStore(), null);
                        '}
                        '
                        'public <className>(ModuleStore store){
                        '    this(store, null);
                        '}
                        '
                        'public <className>(ModuleStore store, $<className> extended){
                        '   this.$me = extended == null ? this : extended;
                        '   store.put(<getClassRef(getQualClassName(moduleName), moduleName)>.class, this);
                        '   <for(imp <- imports, imp notin extends){>
                        '   store.importModule(<getClassRef(imp, moduleName)>.class, <getClassRef(imp, moduleName)>::new);<}> 
                        '   <for(imp <- imports, imp notin extends){>
                        '   <module2field(imp)> = store.getModule(<getClassRef(imp, moduleName)>.class);<}> 
                        '   <for(ext <- extends){>
                        '   <module2field(ext)> = store.extendModule(<getClassRef(ext, moduleName)>.class, <getClassRef(ext, moduleName)>::new, this);<}>
                        '   <for(imp <- imports+extends){>
                        '   $TS.importStore(<module2field(imp)>.$TS);<}>
                        '   $constants = readBinaryConstantsFile(\"<replaceAll(packageName,".","/")>/<className>.constants\");
                        '   <adtTypeInits>
                        '   <constant_inits>
                        '   <consTypeInits> 
                        '   <kwpDecls><module_var_inits>
                        '}";
    
    externalArgs = "";                 
    if(hasMainFunction && !isEmpty(mainFunction.externalRefs)){
      externalArgs = intercalate(", ", [ "new ValueRef\<<jtype>\>(<var.name>)" | var <- mainFunction.externalRefs, var.pos >= 0, jtype := atype2javatype(var.atype)]);
    }              
        
    main_method = "public static void main(String[] args) {
                  '  throw new RuntimeException(\"No function `main` found in Rascal program `<m.name>`\");
                  '}";
                    
    if (hasMainFunction) {
      mainIsVoid     = mainFunction.ftype.ret == avoid();
      hasListStrArgs = [alist(astr())] := mainFunction.ftype.formals;
      hasDefaultArgs = mainFunction.ftype.kwFormals != [];
      
      main_method = "public static void main(String[] args) {
                    '  IValueFactory $VF = org.rascalmpl.values.ValueFactoryFactory.getValueFactory();
                    '  TypeFactory $TF = TypeFactory.getInstance();
                    '  <if (!hasListStrArgs && !hasDefaultArgs) {>
                    '  <if (!mainIsVoid) {>IValue res = <}>new <className>().<mainName>();
                    '  <}><if (hasListStrArgs) {>
                    '  <if (!mainIsVoid) {>IValue res = <}>new <className>().<mainName>(java.util.Arrays.stream(args).map(a -\> $VF.string(a)).collect($VF.listWriter()));
                    '  <}><if (hasDefaultArgs) {>
                    '  <if (!mainIsVoid) {>IValue res = <}>new <className>().<mainName>($parseCommandlineParameters(\"<className>\", args, <atype2vtype(atuple(atypeList([t |  <t,_> <- mainFunction.ftype.kwFormals])), jg)>));
                    '  <}><if (!mainIsVoid) {>if (res == null) {
                    '     throw new RuntimeException(\"Main function failed\"); 
                    '  } else {
                    '    System.out.println(res);
                    '  }<}>
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
                        'import org.rascalmpl.exceptions.RuntimeExceptionFactory;
                        'import org.rascalmpl.util.ExpiringFunctionResultCache;
                        'import org.rascalmpl.values.RascalValueFactory;
                        'import org.rascalmpl.values.parsetrees.ITree;
                        '
                        '<module_imports>
                        '
                        '@SuppressWarnings(\"unused\")
                        'public class <className> 
                        '    extends
                        '        org.rascalmpl.core.library.lang.rascalcore.compile.runtime.$RascalModule
                        '    <module_implements> {
                        '
                        '    private $<className> $me;
                        '    private RascalExecutionContext rex = new RascalExecutionContext(new PrintWriter(System.out), new PrintWriter(System.err), null, null);
                        '    final Traverse $TRAVERSE = new Traverse($VF);
                        '    private final IList $constants;
                        '    <imp_ext_decls>
                        '    <library_inits>
                        '    <module_variables>
                        '    <constant_decls>
                        '    <adtTypeDecls>
                        '    <consTypeDecls>
                        '    <class_constructor>
                        '    <resolvers>
                        '    <functions>
                        '    <main_method>
                        '}";
                       
      the_test_class = generateTestClass(packageName, className, m.functions, jg);
      
      the_interface = generateInterface(moduleName, packageName, className, m.functions, extends, tmodels, jg);
      
      return <the_interface, the_class, the_test_class, constants>;
}

// Generate a TypeStore and declarations for keyword parameters

tuple[str adtTypeDecls ,str adtTypeInits, str consTypeDecls, str consTypeInits, str kwpTypeDecls] generateTypeStoreAndKwpDecls(set[AType] ADTs, set[AType] constructors, JGenie jg){
    adtTypeDecls = "";
    adtTypeInits = "";
    seenAdtNames = {};
    for(a:aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole) <- ADTs){
        if(adtName notin seenAdtNames/*, isEmpty(jg.getATypeAccessor(a))*/){
            adtTypeDecls += "public io.usethesource.vallang.type.Type <getADTName(adtName)>;\n";
            adtTypeInits += "<getADTName(adtName)> = $TF.abstractDataType($TS, \"<adtName>\");\n";
            seenAdtNames += adtName;
        }
    }
    consTypeDecls = "";
    consTypeInits = "";
    kwpTypeDecls = "";
    
    for(c: acons(AType adt, list[AType] fields, list[Keyword] kwpFields) <- constructors, isEmpty(jg.getATypeAccessor(c))){
        //println(c);
        adt_cons = atype2idpart(c, jg);
        hasFieldNames = all(fld <- fields, !isEmpty(fld.label));
        fieldDecls = hasFieldNames ? [ "<atype2vtype(fld,jg)>, \"<fld.label>\"" | fld <- fields ] : [ "<atype2vtype(fld, jg)>" | fld <- fields ];
        consTypeDecls += "public io.usethesource.vallang.type.Type <jg.getATypeAccessor(c)><adt_cons>;\n";
        consTypeInits += "<adt_cons> = $TF.constructor($TS, <jg.getATypeAccessor(c)><getADTName(adt.adtName)>, \"<c.label>\"<isEmpty(fieldDecls) ? "" : ", <intercalate(", ", fieldDecls)>">);\n";
        
        for(kwpField <- kwpFields){
            kwpTypeDecls += "$TS.declareKeywordParameter(<adt_cons>,\"<kwpField.fieldType.label>\", <atype2vtype(kwpField.fieldType, jg)>);\n";
        }
    }    
    return <adtTypeDecls, adtTypeInits,
            consTypeDecls, consTypeInits,
            kwpTypeDecls>;
}

JCode makeConstructorCall(AType consType, list[str] actuals, list[str] kwargs, JGenie jg){
    if(!isEmpty(kwargs)){
        return "$VF.constructor(<atype2idpart(consType, jg)>, new IValue[]{<intercalate(", ", actuals)>}, <kwargs[0]>)";
    } else {
     return "$VF.constructor(<atype2idpart(consType, jg)>, new IValue[]{<intercalate(", ", actuals)>})";
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
    if(!isEmpty(fun.externalRefs)){
        ext_actuals = intercalate(", ", ["ValueRef\<<atype2javatype(var.atype)>\> <varName(var, jg)>" | var <- sort(fun.externalRefs), var.pos >= 0]);
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
            mapCode = "Util.kwpMap(<intercalate(", ", [ *["\"<unescape(key)>\"", trans(defaultExp,jg)] | <str key, AType tp, MuExp defaultExp> <- kwpDefaults ])>);\n";
            
            constantKwpDefaults = "final java.util.Map\<java.lang.String,IValue\> <kwpDefaultsName> = <mapCode>";
         } else {
            jg.setKwpDefaultsName("$kwpDefaults");
            
            nonConstantKwpDefaults =  "java.util.Map\<java.lang.String,IValue\> $kwpDefaults = Util.kwpMap();\n";
            for(<str name, AType tp, MuExp defaultExp> <- kwpDefaults){
                escapedName = getJavaName(name);
                nonConstantKwpDefaults += "<trans(muConInit(muVar("$kwpDefault_<escapedName>","", 10, tp), defaultExp),jg)>$kwpDefaults.put(\"<escapedName>\", $kwpDefault_<escapedName>);";
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

str getMemoCache(MuFunction fun)
    = "$memo_<getJavaName(getUniqueFunctionName(fun))>";
    
JCode trans(MuFunction fun, JGenie jg){
    //println("trans <fun.name>, <fun.ftype>");
    //println("trans: <fun.src>, <jg.getModuleLoc()>");
    //iprintln(fun);
    
    if(!isContainedIn(fun.src, jg.getModuleLoc())) return "";
    
    if(ignoreCompiler(fun.tags)) return "";
    
    ftype = fun.ftype;
    jg.setFunction(fun);
    shortName = getJavaName(getUniqueFunctionName(fun));
    
    visibility = "public "; // isSyntheticFunctionName(shortName) ? "private " : "public "; $getkw_ should be public
    uncheckedWarning = "";
    if(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals) := ftype){
        returnType = atype2javatype(ftype.ret);
        <argTypes, constantKwpDefaults, nonConstantKwpDefaults> = getArgTypes(fun, jg);
        //if(!isEmpty(ftype.formals) && isSyntaxType(ftype.formals[0])) return "";
        redeclaredKwps = jg.collectRedeclaredKwps(fun);
        removeRedeclaredKwps = "";
        
        //if(!isEmpty(kwFormals) ){
        //    removeRedeclaredKwps = isEmpty(redeclaredKwps) ? "" : "$kwpActuals = Util.kwpMapRemoveRedeclared($kwpActuals, <intercalate(", ", ["\"<getJavaName(key)>\"" | str key <- redeclaredKwps ])>);";
        //}
 
        //if(!isEmpty(nonConstantKwpDefaults) ){
        //    removeRedeclaredKwps = isEmpty(redeclaredKwps) ? "" : "$kwpActuals = Util.kwpMapRemoveRedeclared($kwpActuals, <intercalate(", ", ["\"<getJavaName(key)>\"" | str key <- redeclaredKwps ])>);";
        //}
        memoCache = "";
        if(fun.isMemo){
            <secondsTimeout, maxSize> = getMemoSettings(fun.tags["memo"] ? "");
            memoCache = "private final ExpiringFunctionResultCache\<IValue\> <getMemoCache(fun)> = new ExpiringFunctionResultCache\<IValue\>(<secondsTimeout>, <maxSize>);\n";
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
        return "<memoCache><visibility><returnType> <shortName>(<argTypes>){ // by CodeGen: <ftype> 
               '    <constantKwpDefaults><nonConstantKwpDefaults><removeRedeclaredKwps>
               '    <body>
               '}";
    } else
        throw "trans MuFunction: <ftype>";
}

JCode transGetter(MuFunction fun, JGenie jg){
    ftype = fun.ftype;
    jg.setFunction(fun);
    shortName = getJavaName(getUniqueFunctionName(fun));
    
    if(afunc(AType ret, acons(AType adt, list[AType] fields, list[Keyword] kwFormals), []) := ftype){
        returnType = atype2javatype(ftype.ret);
        <argTypes, constantKwpDefaults, nonConstantKwpDefaults> = getArgTypes(fun, jg);
        return isEmpty(kwFormals) ? "public <returnType> <shortName>(<argTypes>){
                                    '    <trans2Void(fun.body, jg)>
                                    '}"
                                  : "
                                    'public <returnType> <shortName>(<argTypes>){
                                    '    <constantKwpDefaults>
                                    '    <nonConstantKwpDefaults>
                                    '    <trans2Void(fun.body, jg)>
                                    '}";
     }
     throw "transGetter: <ftype>";
}

JCode call(MuFunction fun, list[str] actuals, JGenie jg){
    return "<fun.uniqueName>(<intercalate(", ", actuals)>)";
}

default JCode trans(MuExp exp, JGenie jg){
    throw "Cannot translate <exp>";
}

JCode trans(muComment(str text), JGenie jg)
    = "/* <text> */";

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
   
   //if(!loc2muFunction[uid]?){
   // return "";
   //}
   // fun = loc2muFunction[uid];
   // uid = fun.src;
    externalRefs = jg.getExternalRefs(uid);
    //currentFun = jg.getFunction();
    //externalRefsCurrentFun = jg.getExternalRefs(currentFun.src);
    
    nformals = size(ftype.formals);
    sep = nformals > 0 ? "," : "";
    uniq = uid.offset;
    
    //funInstance = "new TypedFunctionInstance<nformals>\<IValue<sep><intercalate(",", ["IValue" | ft <- ftype.formals] )>\>";
    //
    formals = intercalate(", ", ["$<uniq>_<i>" | i <- [0..nformals]]);
   
    //actuals = intercalate(", ", ["$<i>" | i <- [0..nformals]]);
    actuals = intercalate(", ", ["(<atype2javatype(ftype.formals[i])>)$<uniq>_<i>" | i <- [0..nformals]]);
    
    ext_actuals = actuals;
    if(!isEmpty(externalRefs)){
           ext_actuals = intercalate(", ", [ "new ValueRef\<<jtype>\>(<varName(var, jg)>)" | var <- externalRefs, jtype := atype2javatype(var.atype)]);
           ext_actuals = isEmpty(actuals) ? ext_actuals : "<actuals>, <ext_actuals>";
    }
    
    nexternals = size(externalRefs);
    
    funInstance = "new TypedFunctionInstance<nformals/*+nexternals*/>\<IValue<sep><intercalate(",", ["IValue" | int i <- [0..nformals/*+nexternals*/]] )>\>";
    
    
    reta = isVoidType(ftype.ret) ? "" : "return ";
    retb = isVoidType(ftype.ret) ? "return null;" : "";
    return "<funInstance>((<formals>) -\> { <reta><jg.getAccessor([uid])>(<ext_actuals>);<retb> }, <jg.shareType(ftype)>)";
}          
// ---- muOFun ----------------------------------------------------------------
       
JCode trans(muOFun(list[loc] srcs, AType ftype), JGenie jg){
    overloading = false;
    fname = jg.getAccessor(srcs);
   
    nformals = size(getFormals(ftype));
    sep = nformals > 0 ? "," : "";
    uniq = abs(uuidi());
    
    //funInstance = "new TypedFunctionInstance<nformals>\<<"IValue"><sep><intercalate(",", ["IValue" | ft <- getFormals(ftype)])>\>";
    
    formals = intercalate(", ", ["$<uniq>_<i>" | i <- [0..nformals]]);
    formalsWithCast = intercalate(", ", ["(<atype2javatype(getFormals(ftype)[i])>)$<uniq>_<i>" | i <- [0..nformals]]);
    nexternals = 0;
    if(size(srcs) == 1 && !isSyntheticFunctionName(fname) && muFunctions[srcs[0]]?){
        fun = muFunctions[srcs[0]];
        if(!isEmpty(fun.externalRefs)){
           nexternals = size(fun.externalRefs);
           ext_actuals = intercalate(", ", [varName(v, jg) | v <- fun.externalRefs]);
           formals = isEmpty(formals) ? ext_actuals : "<actuals>, <ext_actuals>";
        }
    }
    funInstance = "new TypedFunctionInstance<nformals/*+nexternals*/>\<<"IValue"><sep><intercalate(",", ["IValue" | int i <- [0 ..nformals/*+nexternals*/]])>\>";
    
    return "<funInstance>((<formals>) -\> { return <fname>(<formalsWithCast>); }, <jg.shareType(ftype)>)";
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
    
    funInstance = "new TypedFunctionInstance<nformals>\<<"IValue"><sep><intercalate(",", ["IValue" | ft <- getFormals(rightType)])>\>";
    formals = intercalate(", ", ["$<uniq>_<i>" | i <- [0..nformals]]);
    formalsWithCast = intercalate(", ", ["(<atype2javatype(getFormals(rightType)[i])>)$<uniq>_<i>" | i <- [0..nformals]]);
    rightCall = "<trans(right, jg)>";
     
    rightCall = "<trans(right, jg)>.typedCall(<formalsWithCast>)";
    body = "return <trans(left, jg)>.typedCall(<rightCall>);";
    resultFunType = rightType[ret = leftType.ret];
    
    return "<funInstance>((<formals>) -\> { <body> }, <jg.shareType(resultFunType)>)";
}

// ---- muConstr --------------------------------------------------------------

str trans(muConstr(AType ctype), JGenie jg){
    nformals = size(ctype.fields);
    sep = nformals > 0 ? "," : "";
    
    funInstance = "new TypedFunctionInstance<nformals>\<<"IValue"><sep><intercalate(",", ["IValue" | ft <- ctype.fields])>\>";
    
    bare_formals = ["$<i>" | i <- [0..nformals]];
    return "<funInstance>((<intercalate(", ", bare_formals)>) -\> { return <makeConstructorCall(ctype,  bare_formals, hasKeywordParameters(ctype) || jg.hasCommonKeywordFields(ctype) ? [kwpActuals] : [], jg)>; }, <jg.shareType(ctype)>)";
}

// ---- Tree operations -------------------------------------------------------

str trans(muTreeAppl(MuExp p, MuExp argList, loc src), JGenie jg) 
  = "$RVF.appl(<trans(p, jg)>, <trans(argList, jg)>).asWithKeywordParameters().setParameter(\"src\", <jg.shareConstant(src)>)";

str trans(muTreeAppl(MuExp p, list[MuExp] args, loc src), JGenie jg) 
  = "$RVF.appl(<trans(p, jg)>, $VF.list(<intercalate(",", [trans(a, jg) | a <- args])>)).asWithKeywordParameters().setParameter(\"src\", <jg.shareConstant(src)>)";
  
str trans(muTreeChar(int ch), JGenie jg) 
  = "$RVF.character(<ch>)";  

str trans(muTreeGetProduction(MuExp t), JGenie jg) = "((org.rascalmpl.values.parsetrees.ITree) <trans(t,jg)>).getProduction()";

str trans(muTreeGetArgs(MuExp t), JGenie jg) = "((org.rascalmpl.values.parsetrees.ITree) <trans(t, jg)>).getArgs()";

str trans(muTreeUnparse(MuExp t), JGenie jg) = "$VF.string(org.rascalmpl.values.parsetrees.TreeAdapter.yield(<trans(t, jg)>))";

// ---- Type parameters ---------------------------------------------------------

str trans(muTypeParameterMap(set[AType] parameters), JGenie jg){
    return "HashMap\<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type\> $typeBindings = new HashMap\<\>();
           '<for(p <- parameters){>
           '$typeBindings.put(<atype2vtype(p, jg)>, $TF.voidType());
           '<}>";
}

str trans(c:muCompose(MuExp left, MuExp right, AType leftType, AType rightType, AType resultType), JGenie jg){
    println(c);
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
                                                 : "<fuid == jg.getFunctionName() ? "" : fuid == jg.getModuleName() ? "" : "<module2field(fuid)>."><varName(var, jg)>"
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
                         : "final <base> <name> = (<base>)<rhs>;\n";
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
JCode trans(muGetAnno(MuExp exp, AType resultType, str annoName), JGenie jg){
    if(annoName == "loc") annoName = "src";// TODO: remove when @\loc is gone
    return "$annotation_get(((INode)<trans(exp, jg)>),\"<annoName>\")";
}
// muGuardedGetAnno
JCode trans(muGuardedGetAnno(MuExp exp, AType resultType, str annoName), JGenie jg){
    if(annoName == "loc") annoName = "src";// TODO: remove when @\loc is gone
    return "$guarded_annotation_get(((INode)<trans(exp, jg)>),\"<annoName>\")";
}    

// Call/Apply/return      

//JCode trans(muCall(MuExp fun, AType ftype, list[MuExp] largs, lrel[str kwpName, MuExp exp] kwargs), JGenie jg){
//
//    argTypes = getFunctionOrConstructorArgumentTypes(ftype);    
//    <actuals, kwactuals> = getPositionalAndKeywordActuals(ftype, largs, kwargs, jg);
//    
//    if(muConstr(AType ctype) := fun){
//        <actuals, kwactuals> = getPositionalAndKeywordActuals(ctype, largs, kwargs, jg);
//        return makeConstructorCall(ctype, actuals, kwactuals, jg);        
//    }
//    
//    all_actuals = actuals + kwactuals;
//    externals = [];
//    if(muFun(loc uid, _) := fun){
//        externalRefs = jg.getExternalRefs(uid);
//        if(!isEmpty(externalRefs)){
//           externals = [ varName(var, jg)| var <- externalRefs, var.pos >= 0 ];
//        }
//        
//       if(isContainedIn(uid, jg.getModuleLoc())){
//         fn = loc2muFunction[uid];
//         <actuals, kwactuals> = getPositionalAndKeywordActuals(fn, largs, kwargs, jg);
//         //if(!isEmpty(fn.scopeIn) && kwactuals == ["Util.kwpMap()"]){
//         //    all_actuals = actuals + "$kwpActuals" + externals;
//         //} else {
//            all_actuals = actuals + kwactuals + externals;
//         //}
//         return "<getJavaName(getUniqueFunctionName(fn))>(<intercalate(", ", all_actuals)>)"; // Unique or not ~ match fail checking
//       } else { 
//            all_actuals = actuals + kwactuals + externals;
//            call_code = "<jg.getAccessor([uid])>(<intercalate(", ", all_actuals)>)";
//            return call_code;
//       }
//    }
//    
//    throw "muCall: <fun>";
//}

// ---- muOCall --------------------------------------------------------------

bool anyKwpFormalsInScope(JGenie jg)
    = !isEmpty(jg.collectKwpDefaults(jg.getFunction()));

list[JCode] getActuals(list[AType] argTypes, list[MuExp] largs, JGenie jg)
    = [ i < size(argTypes) ? transWithCast(argTypes[i], largs[i], jg) : trans(largs[i], jg) | i <- index(largs) ];
    
JCode getKwpActuals(list[Keyword] kwFormals, lrel[str name, MuExp exp] kwpActuals, JGenie jg, bool isConstructor = false){
    noKwFormals = !anyKwpFormalsInScope(jg); // isEmpty(jg.getFunction().ftype.kwFormals);
    shouldNotExtend = noKwFormals || isConstructor;
    if(isEmpty(kwpActuals)) return shouldNotExtend ? "Util.kwpMap()" : "$kwpActuals";
    
    kwpActualsCode = intercalate(", ",  [ *["\"<unescape(key)>\"", trans(exp, jg)] | <str key,  MuExp exp> <- kwpActuals]);
    if(shouldNotExtend) return "Util.kwpMap(<kwpActualsCode>)";
    declaredKwps = jg.collectDeclaredKwps(jg.getFunction());
    redeclaredKwps = declaredKwps & [tp.label | <tp, _> <- kwFormals];
    kwpActuals = "$kwpActuals";
    if(!isEmpty(redeclaredKwps))
        kwpActuals =  "Util.kwpMapRemoveRedeclared($kwpActuals, <intercalate(", ", ["\"<getJavaName(key)>\"" | str key <- redeclaredKwps ])>)";
    return "Util.kwpMapExtend(<kwpActuals>, <kwpActualsCode>)";
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

tuple[list[JCode], list[JCode]] getPositionalAndKeywordActuals(MuFunction fun, list[MuExp] actuals, lrel[str name, MuExp exp] kwpActuals, JGenie jg){
    funType = fun.ftype;
    ret = funType.ret;
    formals = funType.formals;
    kwFormals = funType.kwFormals;
    varArgs = funType.varArgs;
  
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
    
    if(isEmpty(kwpActuals)){
        if(isEmpty(fun.scopeIn)){
            if(isEmpty(kwFormals)){
                return <resulting_actuals, []>;
            } else {
                return <resulting_actuals, ["Util.kwpMap()"]>; //["$kwpDefaults_<fun.uniqueName>"]>;
            }
        } else if(isEmpty(jg.collectKwpDefaults(fun))){
            return <resulting_actuals, isEmpty(kwFormals) ? [] : ["$kwpActuals"]>;
       } else {
            return <resulting_actuals, [getKwpActuals(kwFormals, kwpActuals, jg)]>;
       }
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
//println("muOCall((<fun>, <ftype>, ..., <src>");
    argTypes = getFunctionOrConstructorArgumentTypes(ftype);
    actuals = getActuals(argTypes, largs, jg);
    if(muOFun(list[loc] srcs, AType _) := fun){   
        kwactuals = hasKeywordParameters(ftype) ? getKwpActuals(ftype has kwFields ? ftype.kwFields : getFunctionOrConstructorKeywords(ftype), kwargs, jg) : [];
        externalRefs = { *jg.getExternalRefs(fsrc) | fsrc <- srcs };
        externals = [ varName(var, jg) | var <- sort(externalRefs), jtype := atype2javatype(var.atype)];
        return "<jg.getAccessor(srcs)>(<intercalate(", ", actuals + kwactuals + externals)>)";
    }
    
    if(muFun(loc uid, _) := fun){
        <actuals, kwactuals> = getPositionalAndKeywordActuals(ftype, largs, kwargs, jg);
        externalRefs = jg.getExternalRefs(uid);
        externals = [ varName(var, jg) | var <- sort(externalRefs), jtype := atype2javatype(var.atype)];
        
        if(isContainedIn(uid, jg.getModuleLoc())){
            fn = loc2muFunction[uid];
            <actuals, kwactuals> = getPositionalAndKeywordActuals(fn, largs, kwargs, jg);
            return "<getJavaName(isEmpty(fn.scopeIn) ? getFunctionName(fn) : getUniqueFunctionName(fn))>(<intercalate(", ", actuals + kwactuals + externals)>)"; // Unique or not ~ match fail checking
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
    
    cst = (getResult(ftype) == avoid()) ? "" : "(<atype2javatype(getResult(ftype))>)";
    if(muComposedFun(MuExp left, MuExp right, AType leftType, AType rightType, AType resultType) := fun){
        rightCall = "<trans(right, jg)>.typedCall(<intercalate(", ", actuals)>)";
        return "<cst><trans(left, jg)>.typedCall(<rightCall>)";
    }

    return "<cst><trans(fun, jg)>.typedCall(<intercalate(", ", actuals)>)";
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
    adtName = adtType.adtName;
    if(asubtype(adtType, treeType)){
        if(fieldName == "loc") fieldName = "src"; // TODO: remove when @\loc is gone
        adtName = "Tree";
    }
    return "<prefix(moduleName,jg)>$getkw_<adtName>_<getJavaName(fieldName)>(<transWithCast(adtType, cons, jg)>)";
}

JCode trans(muGetKwField(AType resultType,  consType:acons(AType adt, list[AType] fields, list[Keyword] kwFields), MuExp cons, str fieldName, str moduleName), JGenie jg){
    adtName = adt.adtName;
    if(asubtype(adt, treeType)){
        if(fieldName == "loc") fieldName = "src";   // TODO: remove when @\loc is gone
        adtName = "Tree";
    }
    isConsKwField = fieldName in {kwf.fieldType.label | kwf <- kwFields};
    return isConsKwField ? "<prefix(moduleName,jg)>$getkw_<adtName>_<consType.label>_<getJavaName(fieldName)>(<transWithCast(consType, cons, jg)>)"
                         : "<prefix(moduleName,jg)>$getkw_<adtName>_<getJavaName(fieldName)>(<transWithCast(consType, cons, jg)>)";
}
// ---- muGetField ---------------------------------------------------------

JCode trans(muGetField(AType resultType, aloc(), MuExp exp, str fieldName), JGenie jg)
    = castArg(resultType, "$aloc_get_field(<transWithCast(aloc(),exp,jg)>, \"<fieldName>\")");

JCode trans(muGetField(AType resultType, adatetime(), MuExp exp, str fieldName), JGenie jg)
    = castArg(resultType, "$adatetime_get_field(<transWithCast(adatetime(),exp,jg)>, \"<fieldName>\")");
 
JCode trans(muGetField(AType resultType, anode(_), MuExp exp, str fieldName), JGenie jg)
    = castArg(resultType, "$anode_get_field(<transWithCast(anode([]),exp,jg)>, \"<unescape(fieldName)>\")");
    
JCode trans(muGetField(AType resultType, tup:atuple(_), MuExp exp, str fieldName), JGenie jg)
    = castArg(resultType, "$atuple_get_field(<transWithCast(tup,exp,jg)>, \"<unescape(fieldName)>\")");
    
JCode trans(muGetField(AType resultType, areified(AType atype), MuExp exp, str fieldName), JGenie jg)
    = castArg(resultType, "$areified_get_field(<trans(exp,jg)>, \"<unescape(fieldName)>\")");

//JCode trans(muGetField(AType resultType, adt:aadt(adtName,_,_), MuExp exp, str fieldName), JGenie jg) {
//    return asubtype(adt, treeType) && !isNonTerminalType(adt) 
//            ? "$get_Tree_<getJavaName(fieldName)>(<transWithCast(adt,exp,jg)>)"
//            : "org.rascalmpl.values.parsetrees.TreeAdapter.getLabeledField((org.rascalmpl.values.parsetrees.ITree) <trans(cons, jg)>, \"<fieldName>\").tree";
//            //: castArg(resultType, "$aadt_get_field(<transWithCast(adt,exp,jg)>, \"<getJavaName(fieldName)>\")");
//}
          
default JCode trans(mg: muGetField(AType resultType, AType consType, MuExp cons, str fieldName), JGenie jg){
    base = transWithCast(consType, cons, jg);
    qFieldName = "\"<fieldName>\"";
    
    if(isStartNonTerminalType(consType) && fieldName == "top"){
        return castArg(resultType, "org.rascalmpl.values.parsetrees.TreeAdapter.getLabeledField((org.rascalmpl.values.parsetrees.ITree) <trans(cons, jg)>, <qFieldName>).tree");
    }
    
    consType = isStartNonTerminalType(consType) ? getStartNonTerminalType(consType) : consType;
    
    if(isNonTerminalType(consType)){
        //return castArg(resultType, "org.rascalmpl.values.parsetrees.TreeAdapter.getLabeledField((org.rascalmpl.values.parsetrees.ITree) <trans(cons, jg)>, <qFieldName>).tree");
        return castArg(resultType,  "$aadt_get_field(<trans(cons, jg)>, <qFieldName>)");
    } else if(isIterType(consType)){
        //return castArg(resultType,  "$aadt_get_field(<trans(cons, jg)>, <qFieldName>)");
        return castArg(resultType, "org.rascalmpl.values.parsetrees.TreeAdapter.getLabeledField((org.rascalmpl.values.parsetrees.ITree) <trans(cons, jg)>, <qFieldName>).tree");
    } else if(isTerminalType(consType)){
        return castArg(resultType,  "$aadt_get_field(<trans(cons, jg)>, <qFieldName>)");
        //return castArg(resultType, "org.rascalmpl.values.parsetrees.TreeAdapter.getLabeledField((org.rascalmpl.values.parsetrees.ITree) <trans(cons, jg)>, <qFieldName>).tree");
       
   } else if(isRegExpType(consType)){
        return castArg(resultType,  "$aadt_get_field(<trans(cons, jg)>, <qFieldName>)");
        //return castArg(resultType, "org.rascalmpl.values.parsetrees.TreeAdapter.getLabeledField((org.rascalmpl.values.parsetrees.ITree) <trans(cons, jg)>, <qFieldName>).tree");
    } else 
    if(isConstructorType(consType) || isADTType(consType)){
        return castArg(resultType, "$aadt_get_field(<base>, \"<fieldName>\")");
    } else {
    throw mg;
        isConsKwField = fieldName in {kwf.fieldType.label | kwf <- consType.kwFields};
        //return isConsKwField ? "$getkw_<consType.adt.adtName>_<getJavaName(consType.label)>_<getJavaName(fieldName)>(<base>)"
        //                     : "$get_<consType.adt.adtName>_<getJavaName(fieldName)>(<base>)";
    }
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
   
    //for(<AType kwType, Expression exp> <- kwFields){
    //    if(fieldName == kwType.label){
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
    = "$guarded_aadt_get_field(<trans(exp,jg)>,  \"<getJavaName(fieldName)>\")";

JCode trans(muGuardedGetKwField(AType resultType, consType:acons(AType adt, list[AType] fields, list[Keyword] kwFields), MuExp cons, str fieldName, str moduleName), JGenie jg){
    base = trans(cons, jg);
    qFieldName = "\"<unescape(fieldName)>\"";
    for(<AType kwType, Expression exp> <- kwFields){
        if(fieldName == kwType.label){
            expCode = trans(exp, jg);
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
    = "$aloc_field_update(<transWithCast(aloc(), baseExp, jg)>, \"<fieldName>\", <trans(repl, jg)>)";

JCode trans(muSetField(AType resultType, adatetime(), MuExp baseExp, str fieldName, MuExp repl), JGenie jg)
    = "$adatetime_field_update(<transWithCast(adatetime(), baseExp, jg)>, \"<fieldName>\", <trans(repl, jg)>)";
    
JCode trans(muSetField(AType resultType, anode(_), MuExp baseExp, str fieldName, MuExp repl), JGenie jg)
    = "$anode_field_update(<transWithCast(anode([]), baseExp, jg)>, \"<getJavaName(fieldName)>\", <trans(repl, jg)>)";    

JCode trans(muSetField(AType resultType, AType baseType, MuExp baseExp, int fieldIdentity, MuExp repl), JGenie jg)
    = "$atuple_update(<trans(baseExp, jg)>, <fieldIdentity>,  <trans(repl, jg)>)" when isTupleType(resultType);
    
JCode trans(muSetField(AType resultType, a:aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole), MuExp baseExp, str fieldName, MuExp repl), JGenie jg)
    = castArg(a, "$aadt_field_update(<trans(baseExp,jg)>,  \"<getJavaName(fieldName)>\", <trans(repl, jg)>)");

default JCode trans(muSetField(AType resultType, AType baseType, MuExp baseExp, str fieldName, MuExp repl), JGenie jg){
    return asubtype(baseType, treeType) ? castArg(baseType, "$aadt_field_update(<trans(baseExp,jg)>,  \"<getJavaName(fieldName)>\", <trans(repl, jg)>)")
                                        : "<trans(baseExp, jg)>.set(\"<getJavaName(fieldName)>\", <trans(repl, jg)>)";
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
            escapedKwName = unescape(kwFormal.fieldType.label);
            actuals += "(<atype2javatype(kwFormal.fieldType)>)(<kwpActuals>.containsKey(\"<escapedKwName>\") ? <kwpActuals>.get(\"<escapedKwName>\") : <kwpDefaultsName>.get(\"<escapedKwName>\"))";
        }
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

str semi(str code){
    return /\)$/ := code ? "<code>;" : /[;}][\ \t]*<comment:(\/\/.*$)?>[\ \t \n]*/ := code ? code : "<code>;<comment>";
}

str semi_nl(str code)
    = "<semi(code)>\n";
 
JCode trans2Void(MuExp exp, JGenie jg)
   = "/* <exp> */"
   when getName(exp) in {"muCon", "muVar", "muTmp"};

JCode trans2Void(MuExp exp, JGenie jg)
   =  semi_nl(trans(exp, jg))
   when getName(exp) in {"muOCall", "muCall", "muReturn0", "muReturn1"};
   
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
           '                           : Util.kwpMap<anyKwp ? "Extend" : "">(<anyKwp ? "$kwpActuals, " : ""><for(<str key, AType atype, MuExp exp> <- kwpDefaults, muCon(_) !:= exp){>\"<key>\", <kwpActuals>.containsKey(\"<unescape(key)>\") ? ((<atype2javatype(atype)>) <kwpActuals>.get(\"<unescape(key)>\")) : <transCast(atype,exp,jg)>)<}>)";
}

// ---- muVarKwp --------------------------------------------------------------

JCode trans(var:muVarKwp(str name, str fuid, AType atype),  JGenie jg)
    = "((<atype2javatype(atype)>) ($kwpActuals.containsKey(\"<unescape(name)>\") ? $kwpActuals.get(\"<unescape(name)>\") : <jg.getKwpDefaultsName()>.get(\"<unescape(name)>\")))";

JCode trans(muAssign(muVarKwp(str name, str fuid, AType atype), MuExp exp), JGenie jg)
    = "$kwpActuals.put(\"<unescape(name)>\", <trans(exp, jg)>);\n";

JCode trans(muIsKwpDefined(MuExp exp, str kwpName), JGenie jg)
    = "<trans(exp, jg)>.asWithKeywordParameters().hasParameter(\"<unescape(kwpName)>\")";

JCode trans(muHasKwp(MuExp exp, str kwName), JGenie jg)
    = "<trans(exp, jg)>.asWithKeywordParameters().hasParameter(\"<unescape(kwName)>\")";

JCode trans(muGetKwp(MuExp exp, AType atype, str kwpName), JGenie jg){
   if(acons(AType adt, list[AType] fields, list[Keyword] kwFields) := atype){
        return "$getkw_<atype.adtName>_<getJavaName(kwpName)>(<trans(exp, jg)>)";
   } else if(anode(_) := atype){
       return "<trans(exp, jg)>.asWithKeywordParameters().getParameter(\"<unescape(kwpName)>\")";
   } else if(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole) := atype){
       return "$getkw_<adtName>_<getJavaName(kwpName)>(<trans(exp, jg)>)";
   }
   throw "muGetKwp: <atype>, <kwpName>";
}
// ---- muGetKwFieldFromConstructor

JCode trans(muGetKwFieldFromConstructor(AType resultType, MuExp exp, str kwpName), JGenie jg)
    = "((<atype2javatype(resultType)>)<trans(exp, jg)>.asWithKeywordParameters().getParameter(\"<unescape(kwpName)>\"))";
 
// ---- muGetFieldFromConstructor

JCode trans(muGetFieldFromConstructor(AType resultType, AType consType, MuExp exp, str fieldName), JGenie jg){
    if(isSyntaxType(consType)){
        return "((<atype2javatype(resultType)>) org.rascalmpl.values.parsetrees.TreeAdapter.getLabeledField((ITree)<trans(exp, jg)>, \"<fieldName>\").tree)";
    } else {
        i = indexOf([fld.label | fld <- consType.fields], fieldName);
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
    transThen = transElse = trans;
    if(!producesNativeBool(exp)){
      if(producesNativeBool(thenPart)) transThen = trans2IValue;
      if(producesNativeBool(elsePart)) transElse = trans2IValue;
    }
    return "(<trans2NativeBool(cond, jg)> ? <transThen(thenPart, jg)> : <transElse(elsePart, jg)>)";
}

JCode trans(muIf(MuExp cond, MuExp thenPart), JGenie jg){
    return "if(<trans2NativeBool(cond, jg)>){
            '   <trans2Void(thenPart, jg)>
            '}\n";
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
    if(muEnter(btscope, MuExp exp) := body){
       return
            "<isEmpty(label) ? "" : "<getJavaName(label)>:">
            '//<btscope>:
            '    while(<cond_code>){
            '    <btscope>: 
            '       do {
            '         <trans2Void(exp, jg)>
            '          break <getJavaName(label)>; //muWhileDo
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
    = "break <getJavaName(label)>; // muSucceed";

JCode trans(muFail(str label), JGenie jg){
    if(startsWith(jg.getFunctionName(), label)){    // TODO:this is brittle, solve in JGenie
       // println(jg.getFunction().ftype);
        return jg.getFunction().ftype.ret == avoid() ? "throw new FailReturnFromVoidException();"
                                                     : "return null;";
    }
    //if(/^CASE[0-9]+$/ := label){
    //    return "break <label>;";
    //}
    return "continue <getJavaName(label)>;";   
}
    
JCode trans(muBreak(str label), JGenie jg)
    = "break <getJavaName(label)>; // muBreak\n";
    
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
    reachable_atypes = "new io.usethesource.vallang.type.Type[]{<intercalate(", ", [atype2vtype(t, jg) | t <- descendant.reachable_atypes])>}";
    reachable_aprods = "new io.usethesource.vallang.IConstructor[]{<intercalate(", ", [jg.shareReifiedConstant(t, definitions) | t <- descendant.reachable_aprods])>}";
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
   
JCode trans(muValueBlock(AType t, list[MuExp] exps), JGenie jg){
    return "<for(exp <- exps[0..-1]){><trans2Void(exp, jg)><}> 
           '<trans(exps[-1], jg)>";
}

// Exceptions
       
JCode trans(muThrow(muTmpNative(str name, str fuid, nativeException()), loc src), JGenie jg){
    return "throw <name>;";
}

//JCode trans(muBuiltinRuntimeExceptionThrow(str exceptionName, list[MuExp] args), JGenie jg){
//    return "throw RuntimeExceptionFactory.<exceptionName>(<intercalate(",", [trans(arg, jg) | arg <- args])>);";
//}

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
    return !isVarOrTmp(exp) && exp has atype && exp.atype == tp ? "true"
                      : "$isSubtypeOf(<trans(exp, jg)>.getType(),<jg.shareType(tp)>)";
}
JCode trans(muValueIsSubtypeOfValue(MuExp exp1, MuExp exp2), JGenie jg)
    ="$isSubtypeOf(<trans(exp1, jg)>.getType(),<trans(exp2, jg)>.getType())";

JCode trans(muValueIsSubtypeOfInstantiatedType(MuExp exp, AType tp), JGenie jg){ 
    return !isVarOrTmp(exp) && exp has atype && exp.atype == tp ? "true"
                      : "$isSubtypeOf(<trans(exp, jg)>.getType(),<jg.shareType(tp)>.instantiate($typeBindings))";   
} 

JCode trans(m:muValueIsComparable(MuExp exp, AType tp), JGenie jg){
    return !isVarOrTmp(exp) && exp has atype && exp.atype == tp ? "true"
                      : "$isComparable(<trans(exp, jg)>.getType(), <jg.shareType(tp)>)";   
} 

JCode trans(muValueIsComparableWithInstantiatedType(MuExp exp, AType tp), JGenie jg){ 
    return !isVarOrTmp(exp) && exp has atype && exp.atype == tp ? "true"
                      : "$isComparable(<trans(exp, jg)>.getType(), <jg.shareType(tp)>.instantiate($typeBindings))";   
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
        case aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole):{
            full_adt_name = "<jg.getATypeAccessor(consType)><getADTName(adtName)>";
            return "<v> instanceof IConstructor && (((IConstructor)<v>).arity() == <arity> && ((IConstructor)<v>).getType().equivalent(<full_adt_name>))";
            }
        case acons(AType adt, list[AType] fields, list[Keyword] kwFields):
            if(isNonTerminalType(expType)){
                return "$nonterminal_has_name_and_arity((ITree) <v>, \"<consType.label>\", <size(fields)>)";
            } else {
                return "<v> instanceof IConstructor && ((IConstructor)<v>).getConstructorType().equivalent(<atype2idpart(consType, jg)>)";
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
    
JCode trans(muSize(MuExp exp, atype:\opt(_)), JGenie jg)
    = "<transWithCast(atype, exp, jg)>.getArgs().length()";
 
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