module lang::rascalcore::compile::muRascal2Java::CodeGen

import lang::rascal::\syntax::Rascal;

import lang::rascalcore::compile::muRascal::AST;
//extend lang::rascalcore::check::AType;
//extend lang::rascalcore::check::ATypeUtils;
import List;
import Set;
import Relation;
import String;
import Map;
import Node;
import IO;
import Type;
import util::Reflective;

//extend analysis::typepal::TypePal;

//extend lang::rascalcore::check::TypePalConfig;

import lang::rascalcore::compile::muRascal2Java::Writers;
import lang::rascalcore::compile::muRascal2Java::Primitives;

import lang::rascalcore::compile::muRascal2Java::JGenie;

import lang::rascalcore::compile::util::Names;

bool debug = false;

// ---- globals ---------------------------------------------------------------

map[str, MuFunction] muFunctions = ();
map[str,str] resolved2overloaded = ();

// ---- muModule --------------------------------------------------------------

tuple[JCode, JCode, JCode] muRascal2Java(MuModule m, map[str,TModel] tmodels, map[str,loc] moduleLocs){
    muFunctions = (f.qname : f | f <- m.functions);
    resolved2overloaded = ();
    jg = makeJGenie(m.name, tmodels, moduleLocs);
    <typestore, kwpDecls> = generateTypeStoreAndKwpDecls(m.ADTs, m.constructors);
    <signatures, resolvers> = genResolvers(m.overloaded_functions, jg);
    
    bool hasMainFunction = false;
    for(f <- m.functions){
        if(contains(f.qname, "$main_")) hasMainFunction = true;
        jg.addExternalVars(f.externalVars);
    }
  
    className = split("::", m.name)[-1];
    
    packageName = module2package(m.name);
    interfaceName = "$<className>"; //module2interface(m.name);
    
    module_variables  = "<for(var <- m.module_variables){>
                        '<trans(var, jg)><}>";
                       
    functions         = "<for(f <- m.functions){>
                        '<trans(f, jg)>
                        '<}>";
                       
    library_imports   = "<for(class <- jg.getImportedLibraries()){>
                        'import <class>;
                        '<}>";
                       
    library_inits     = "<for(class <- jg.getImportedLibraries()){>
                         'final <getBaseClass(class)> $<getBaseClass(class)> = new <class>($VF);
                         '<}>";
                        
    module_extends    = !isEmpty(m.extends) ? "implements " + intercalate(", ",[ module2class(ext) | ext <- m.extends]) : "";
                        
    module_imports    = "<for(imp <- toSet(m.imports + m.extends)){>
                         'import <module2class(imp)>;
                        '<}>";
                       
    imp_ext_decls     = "<for(imp <- toSet(m.imports + m.extends)){>
                        'final <module2class(imp)> <module2ul(imp)>;
                        '<}>";
                                         
    module_imp_inits  = "<for(imp <- m.imports, imp notin m.extends){>
                        '<module2ul(imp)> = <module2class(imp)>.import<module2uqclass(imp)>();
                        '<}>";
    module_ext_inits  = "<for(ext <- m.extends){>
                        '<module2ul(ext)> = <module2class(ext)>.extend<module2uqclass(ext)>(this);
                        '<}>";
    
    constructor_body  = "<module_imp_inits>
                        '<module_ext_inits>
                        '<kwpDecls>
                        '<for(exp <- m.initialization){><trans(exp, jg)><}>
                        ";                   
    class_constructor = "private <className>(<interfaceName> me){
                        '    <constructor_body>
                        '    this.$me = me == null ? this : me;
                        '}
                        'private <className>(){
                        '    this(null);
                        '}";
                       
   instances          = "private static final class InstanceHolder {
                        '    public static <className> sInstance = new <className>();
                        '}
                        
                        'public static final <className> import<className>() {
                        '   return InstanceHolder.sInstance;
                        '}
                        '
                        'public static final <className> extend<className>($<className> newMe) {
                        '   return new <className>(newMe);    
                        '}";
    main_method       = "public static void main(String[] args) {
                        '   <if(hasMainFunction){>System.out.println(new <className>().main());<}else{>
                        '   throw new RuntimeException(\"No function `main` found in Rascal program `<m.name>`\");
                        '   <}>
                        '
                        '}";
    
    the_class =         "package <packageName>;
                        'import java.util.*;
                        'import io.usethesource.vallang.*;
                        'import io.usethesource.vallang.type.*;
                        'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.*;
                        'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.*;
                        'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse.*;
                        '
                        '<library_imports>
                        '<module_imports>
                        '
                        'public class <className> extends org.rascalmpl.core.library.lang.rascalcore.compile.runtime.$RascalModule <module_extends> implements <interfaceName> {
                        '    static final Traverse $TRAVERSE = new Traverse($VF);
                        '    private final <interfaceName> $me;
                        '    <typestore>
                        '    <library_inits>
                        '    <imp_ext_decls>
                        '    <module_variables>
                        '    <class_constructor>
                        '    <instances>
                        '    <jg.getConstants()>
                        '    <resolvers>
                        '    <functions>
                        '    <main_method>
                        '}";
      the_class = removeEmptyLines(the_class);
      
      the_interface =  "package <packageName>;
                       'import java.util.*;
                       'import io.usethesource.vallang.*;
                       'import io.usethesource.vallang.type.*;
                       'public interface $<className> {
                       '    <signatures>
                       '}\n";
                       
      the_test_class = generateTestClass(packageName, className, m.functions);
      
      return <the_interface, the_class, the_test_class>;
}

str generateTestClass(str packageName, str className, list[MuFunction] functions){
    return "package <packageName>;
           'import java.util.*;
           'import java.util.stream.Stream;
           'import io.usethesource.vallang.*;
           'import io.usethesource.vallang.type.*;
           '
           'import static org.junit.Assert.assertTrue;
           'import static org.junit.Assert.fail;
           'import static org.junit.jupiter.api.DynamicTest.dynamicTest;
           '
           'import org.junit.jupiter.api.Test;
           'import org.junit.jupiter.api.DynamicTest;
           'import org.junit.jupiter.api.TestFactory;
           'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils.*;
           '
           'class <className>Test {
           '    static final <className> MUT = <className>.import<className>();
           '    static final TypeFactory $TF = TypeFactory.getInstance();
           '    static final GenerateActuals generator = new GenerateActuals(5, 5, 10);
           '    <for(f <- functions){>
           '    <generateTestMethod(f)><}>
           '}\n";
}

str generateTestMethod(MuFunction f){
    if("test" notin f.modifiers) return "";
    
    fname = f.uqname;
    formals = f.ftype.formals;
    expected = f.tags["expected"] ? "";
    if(isEmpty(formals)){
        if(isEmpty(expected)){
            return "@Test
                   'void <fname>(){
                   '   assertTrue(MUT.<fname>().getValue());
                   '}\n";
        } else {
            return "@Test
                   'void <fname>(){
                   '    try {
                   '        MUT.<fname>();
                   '    } catch (RascalException e) {
                   '        if(((IConstructor) e.getValue()).getConstructorType() == RascalExceptionFactory.<expected>) {
                   '            assertTrue(true);
                   '            return;
                   '         }
                   '         fail(\"Expected `<expected>`, got: \" + e);
                   '    }
                   '    fail(\"Expected `<expected>`, but none thrown\");
                   '}\n";
        }
    }
    types = "new Type[] {<intercalate(", ", [atype2typestore(tp) | tp <- formals])>}";
    actuals = intercalate(", ", ["args[<i>]" | i <- index(formals)]) + ", Collections.emptyMap()";
    return "@TestFactory
           'Stream\<DynamicTest\> <fname>(){
           '    return generator.generateActuals(<types>).map((args) -\> dynamicTest(\"<fname>\", () -\> assertTrue(((IBool)MUT.<fname>(<actuals>)).getValue())));
           '}\n";
}

tuple[str,str] generateTypeStoreAndKwpDecls(set[AType] ADTs, set[AType] constructors){
    adtDecls = "";
    for(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole) <- ADTs){
        adtDecls += "static final Type <adtName> = $TF.abstractDataType($TS, \"<adtName>\");\n";
    }
    consDecls = "";
    kwpDecls = "";
    map[str, set[AType]] kwpField2Cons = ();
    map[str, AType] kwpField2Type = ();
    for(c: acons(AType adt, list[AType] fields, list[Keyword] kwpFields) <- constructors){
        adt_cons = atype2descriptor(c); //"<adt.adtName>_<c.label>_<size(fields)>";
        fieldDecls = [ "<atype2typestore(fld)>, \"<fld.label>\"" | fld <- fields ];
        consDecls += "static final Type <adt_cons> = $TF.constructor($TS, <adt.adtName>, \"<c.label>\"<isEmpty(fieldDecls) ? "" : ", <intercalate(", ", fieldDecls)>">);\n";
        for(kwpField <- kwpFields){
            kwpDecls += "$TS.declareKeywordParameter(<adt_cons>,\"<kwpField.fieldType.label>\", <atype2typestore(kwpField.fieldType)>);\n";
            fieldName = kwpField.fieldType.label;
            if(kwpField2Cons[fieldName]?){
                kwpField2Cons[fieldName] += {c};
                kwpField2Type[fieldName] =  alub(kwpField.fieldType, kwpField2Type[fieldName]);
            } else {
                kwpField2Cons[fieldName] = {c};
                kwpField2Type[fieldName] =  kwpField.fieldType;
           }
        }
    }
    consResolvers = "";
    
    for(kwpFieldName <-kwpField2Cons){
        consesWithField = kwpField2Cons[kwpFieldName];
       
        // find all ADTs that have this keyword field
        relevantADTs = { c.adt.adtName | AType c <- consesWithField };
       
        // ... and generate resolvers for th
        fieldType = avoid();
        code = "";
        for(adtName <- relevantADTs){
            kwpFieldType = kwpField2Type[kwpFieldName];
            for(c <- consesWithField){
                 if(c.adt.adtName == adtName){
                        code += "if($0.getConstructorType() == <adtName>_<c.label>){
                                '  return $get_<adtName>_<c.label>_<kwpFieldName>($0);
                                '}\n";
                 }
            }
            getterName = "$get_<adtName>_<kwpFieldName>";
            consResolvers += "<atype2java(kwpFieldType)> <getterName>(IConstructor $0){
                             '  <code>
                             '  throw new RuntimeException(\"<getterName> fails\");
                             '}\n";
        } 
    }
    
    return <"<adtDecls>
            '<consDecls>
            '
            '<consResolvers>
            '",
            kwpDecls>;
}

alias OF5 = tuple[str name, AType funType, str oname, list[loc] ofunctions, list[loc] oconstructors];
alias OF4 = tuple[AType funType, str oname, list[loc] ofunctions, list[loc] oconstructors];

bool larger(OF5 a, OF5 b){
   return a.name <= b.name &&
          size(a.ofunctions) + size(a.oconstructors) >
          size(b.ofunctions) + size(b.oconstructors);
}

list[OF5] sortOverloads(set[OF5] ofs){
    return sort(ofs, larger);
}

list[OF5] filterMostAlts(set[OF5] overloadedFunctions){
    sorted = sortOverloads(overloadedFunctions);
    int last = size(sorted) - 1;
    filtered = [ *((i == last || sorted[i].name != sorted[i+1].name || sorted[i].funType != sorted[i+1].funType) ? [sorted[i]] : []) | int i <- index(sorted)];
    return filtered;
}

tuple[str signatures, str methods] genResolvers(list[OF5] overloadedFunctions, JGenie jg){
    //overloadedFunctions = for(ovl <- overloadedFunctions) {  ovl.funType = unsetRec(ovl.funType); append ovl; }
    println("genResolvers:"); iprintln(overloadedFunctions);
    mostAlts = filterMostAlts(toSet(overloadedFunctions));
    all_signatures = ""; 
    all_resolvers = "";
    // For every function name + type, choose the one with the most alternatives
    fnames = toSet(mostAlts<0>);
    for(overload <- mostAlts){
        if(!resolved2overloaded[overload.oname]?){
            //overloaded_versions += <fname, ftype, overload>;
            println("add to resolved2overloaded: <overload.oname>, <overload.name>");
            resolved2overloaded[overload.oname] = overload.name;
            <signatures, resolvers> = genResolver(overload, jg);
            all_signatures += signatures;
            all_resolvers += resolvers;
        }
    }
  
    for(str fname <- fnames){
        <signatures, resolvers> = genGeneralResolver(fname, mostAlts[fname], jg);
        all_signatures += signatures;
        all_resolvers += resolvers;
    }
    return <all_signatures, all_resolvers>;
}

list[OF4] sortOverloads(list[OF4] overloads){
    return sort(overloads, bool(OF4 a, OF4 b){ return a.funType != b.funType && asubtype(b.funType, a.funType); });
}

tuple[str signatures, str resolvers] genGeneralResolver(str fname, list[OF4] overloads, JGenie jg){
    if(fname == "type") return <"","">;
    println("genGeneralResolver:"); iprintln(overloads);
    arities = { getArity(ovl.funType) | ovl <- overloads };
    all_signatures = "";
    all_resolvers = "";
    for(int i <- arities, i > 0){
        cases = "";
        canFail = any(ovl <- overloads, ftype := ovl.funType, getArity(ftype) == i, any(of <- ovl.ofunctions, di := jg.getDefine(of).defInfo, di has canFail, di.canFail));
   
        for(ovl <- sortOverloads(overloads), ftype := unsetRec(ovl.funType), getArity(ftype) == i){
           formalTypes = getFormals(ftype);
           if(i > 0 && !all(formalType <- formalTypes, formalType == avalue())){
               argTypes = "<intercalate(", ", [ "(<atype2java(tf)>)$<j>"  | j <- index(formalTypes), tf := formalTypes[j]])>";
               if(hasKeywordParameters (ftype)) argTypes += ", $kwpActuals";
               base_call = "<fname>_<atype2descriptor(ftype)>(<argTypes>)";
               call_code = canFail ? "res = <base_call>;
                                     'if(res != null) return res;\n"
                                   : "return <base_call>;\n";
               cases += "if(<intercalate(" && ", [ "$<j>.getType().<atype2istype(tf)>()"  | j <- index(formalTypes), tf := formalTypes[j]])>){
                        '   <call_code>
                        '}\n";
           }
        }
        signature = "public IValue <fname>(<intercalate(", ", ["IValue $<j>" | j <- [0..i] ])>, Map\<String,?\> $kwpActuals)";
        all_signatures += signature + ";\n";
        all_resolvers += "<signature>{
                             '  <if(canFail){>IValue res;<}>
                             '  <cases>
                             '  throw new RuntimeException(\"Cannot resolve call to `<fname>`\");
                             '}\n";
    }
    return <all_signatures, all_resolvers>;
}

tuple[str signatures, str resolvers] genResolver(tuple[str name, AType funType, str scope, list[loc] ofunctions, list[loc] oconstructors] overload, JGenie jg){
   if(overload.name == "type") return <"","">;
   println("genResolver:"); iprintln(overload);
   
   funType = unsetRec(overload.funType);
   formalTypes = getFormals(funType);
   if(jg.isResolved(overload) || !jg.usesLocalFunctions(overload) || getArity(funType) == 0) return <"", "">;
 
   if(getArity(funType) != 0 && all(formalType <-formalTypes, formalType == avalue())) return <"", "">;
   
   jg.addResolver(overload);
  
   anyKwParams = any(ovl <- overload.ofunctions + overload.oconstructors, hasKeywordParameters(jg.getType(ovl)));
   
   concretePatterns = any(ovl <- overload.ofunctions /*+ overload.oconstructors*/, jg.getType(ovl).isConcreteArg);
  
   returnType = atype2java(getResult(funType));
   argTypes = intercalate(", ", ["<atype2java(f)> $<i>" | i <- index(formalTypes), f := formalTypes[i]]);
   if(anyKwParams){
        kwpActuals = "Map\<String,?\> $kwpActuals";
        argTypes = isEmpty(argTypes) ? kwpActuals : "<argTypes>, <kwpActuals>";
   }
   println(replaceAll(overload.name, "::", "_"));
   println(atype2descriptor(overload.funType));
   name_resolver = "<replaceAll(overload.name, "::", "_")>_<atype2descriptor(overload.funType)>";
   signature = "public <returnType> <name_resolver>(<argTypes>)";
   canFail = any(of <- overload.ofunctions, di := jg.getDefine(of).defInfo, di has canFail, di.canFail);
   map[int,str] cases = ();
   for(of <- overload.ofunctions){
        fp = jg.getType(of).abstractFingerprint;
        if(cases[fp]?){
            cases[fp] += makeCall(funType, of, jg);
        } else {
            cases[fp] = makeCall(funType, of, jg);
        }
   }
   
   conses = "";
   for(of <- overload.oconstructors){
        conses += makeCall(funType, of, jg);
   }
     
   body = "";
   if(size(cases) == 1){
    for(c <- cases){
        body = cases[c];
        break;
    }
   } else if(size(cases) > 1){
        body = "switch(ToplevelType.getFingerprint($0, <concretePatterns>)){
               '<for(caseLab <- cases){>
               '         case <caseLab>: { 
               '             <cases[caseLab]> 
               '             <if(!startsWith(split("\n", cases[caseLab])[-1], "return")){> break;<}>
               '         }
               '    <}>
               '}";
   }
   
   return <signature + ";\n",
          "<signature>{
          '    <if(canFail){><atype2java(getResult(funType))> res;<}>
          '    <body>
          '    <conses>
          '    <if(canFail && isEmpty(conses)){>throw new RuntimeException(\"Cannot resolve call to `<name_resolver>`\");<}>
          '}
          '">;
}

JCode makeCall(AType resolverFunType, loc of, JGenie jg){
    funType = jg.getType(of);
    kwpActuals = "$kwpActuals";
    formalTypes = getFormals(funType);
    resolverFormalTypes = getFormals(resolverFunType);
    if(any(int i <- index(formalTypes), unsetRec(formalTypes[i]) != unsetRec(resolverFormalTypes[i]))){
        conds = [];
        actuals = [];
        for(int i <- index(resolverFormalTypes)){
            if(unsetRec(formalTypes[i]) != resolverFormalTypes[i]){
                conds +=  "$<i>.getType().<atype2istype(formalTypes[i])>()"; 
                actuals += "(<atype2java(formalTypes[i])>) $<i>";
            } else {
                actuals += "$<i>";
                 //actuals += "<resolverFunType.formals[i].label>$<i>";
            }
        } 
        if(hasKeywordParameters(funType)){
            actuals = isEmpty(actuals) ? [kwpActuals] : actuals + kwpActuals;
        }
     
        base_call = "";
        
        if(isConstructorType(funType)){
            return "return <makeConstructorCall(funType, actuals, jg)>;";
        } else {
            call_code = "<jg.getAccessorInResolver(of)>(<intercalate(", ", actuals)>)";
            di = jg.getDefine(of).defInfo;
            base_call = di has canFail && di.canFail ? "res = <call_code>;
                                                       'if(res != null) return res;
                                                       '"
                                                     : "return <call_code>;\n";
        }
        if(isEmpty(conds)){
            return base_call;
        } else {
            return "if(<intercalate(" && ", conds)>){
                   '    <base_call>
                   '}
                   '";
        }
    } else {
        actuals = ["$<i>" | i <- index(formalTypes), f := formalTypes[i]];
        if(hasKeywordParameters(funType)){
            actuals += kwpActuals;
        }
        if(isConstructorType(funType)){
            return "return <makeConstructorCall(funType, actuals, jg)>;";
            //if(isEmpty(funType.kwFields)){
            //    return "$VF.constructor(<funType.adt.adtName>_<funType.label>, new IValue[]{<intercalate(", ", actuals)>})";
            //} else {
            //    return "$VF.constructor(<funType.adt.adtName>_<funType.label>, new IValue[]{<intercalate(", ", actuals[0..-1])>}, <actuals[-1]>)";
            //}
        
        } else {
            call_code = "<jg.getAccessorInResolver(of)>(<intercalate(", ", actuals)>)";
            di = jg.getDefine(of).defInfo;
            return di has canFail && di.canFail ? "res = <call_code>;
                                                  'if(res != null) return res;
                                                  '"
                                                : "return <call_code>;\n";
        }
    }
}

JCode makeConstructorCall(AType funType, list[str] actuals, JGenie jg){
    if(isEmpty(funType.kwFields)){
        return "$VF.constructor(<atype2descriptor(funType)>, new IValue[]{<intercalate(", ", actuals)>})";
    } else {
        return "$VF.constructor(<atype2descriptor(funType)> new IValue[]{<intercalate(", ", actuals[0..-1])>}, <actuals[-1]>)";
    }
}

// ---- muModuleVar ----------------------------------------------------------

JCode trans(MuModuleVar var, JGenie jg){
       return "<atype2java(var.atype)> <var.name>;";
}

// ---- muFunction ------------------------------------------------------------

bool constantDefaults(lrel[str name, AType atype, MuExp defaultExp] kwpDefaults){
    return all(<str name, AType atype, MuExp defaultExp> <- kwpDefaults, muCon(_) := defaultExp);
}

JCode trans(MuFunction fun, JGenie jg){
    iprintln(fun);
    ftype = fun.ftype;
    qname = replaceAll(fun.qname, "::", "_");
    shortName = qname[findFirst(qname, "$")+1 .. ];
    if(isEmpty(ftype.formals)){ // remove line range from name, since parameterless functions is unique.
        shortName = shortName[0 .. findLast(shortName, "_")];
        shortName = shortName[0 .. findLast(shortName, "_")];
    }
    uncheckedWarning = "";
    if(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals) := ftype){
        returnType = atype2java(ftype.ret);
        argTypes = intercalate(", ", ["<atype2java(f)> <f.label? ? "<f.label>" : "$<i>">" | i <- index(ftype.formals), f := ftype.formals[i]]);
        if(!isEmpty(fun.externalVars)){
            ext_actuals = intercalate(", ", ["ValueRef\<<atype2java(v.atype)>\> <v.name>" | v <- fun.externalVars]);
            argTypes = isEmpty(formals) ? ext_actuals : "<argTypes>, <ext_actuals>";
        }
        kwpActuals = "Map\<String,IValue\> $kwpActuals";
        kwpDefaults = fun.kwpDefaults;
        constantKwpDefaults = "";
        nonConstantKwpDefaults = "";
        mapCode = "Util.kwpMap(<intercalate(", ", [ *["\"<key>\"", trans(defaultExp,jg)] | <str key, AType tp, MuExp defaultExp> <- kwpDefaults ])>);\n";
        if(!isEmpty(kwFormals)){
            argTypes = isEmpty(argTypes) ? kwpActuals : "<argTypes>, <kwpActuals>";
            if(constantDefaults(kwpDefaults)){
                kwpDefaultsName = "$kwpDefaults_<shortName>";
                jg.setKwpDefaults(kwpDefaultsName);
                constantKwpDefaults = "final Map\<String,IValue\> <kwpDefaultsName> = <mapCode>";
             } else {
                jg.setKwpDefaults("$kwpDefaults");
                nonConstantKwpDefaults =  "Map\<String,IValue\> $kwpDefaults = <mapCode>";
             }   
        }
        jg.setRefVars(getReferenceVars(fun));
        declaredLocalVars = ""; //getLocalVarDeclarations(fun, jg);
        return isEmpty(kwFormals) ? "<returnType> <shortName>(<argTypes>){
                                    '    <declaredLocalVars>
                                    '    <trans(fun.body, jg)>
                                    '}"
                                  : "<constantKwpDefaults>
                                    '<returnType> <shortName>(<argTypes>){
                                    '    <nonConstantKwpDefaults>
                                    '    <declaredLocalVars>
                                    '    <trans(fun.body, jg)>
                                    '}";

  
    } else
    if(acons(AType adt, list[AType] fields, list[Keyword] kwFields) := ftype){
        returnType = "IConstructor";
        argTypes = intercalate(", ", ["<atype2java(f)> <f.label? ? "<f.label>_<i>" : "$<i>">" | i <- index(ftype.fields), f := ftype.fields[i]]);
        kwpActuals = "Map\<String,?\> $kwpActuals";
        kwpDefaults = fun.kwpDefaults;
        constantKwpDefaults = "";
        nonConstantKwpDefaults = "";
        mapCode = "Maps.builder()<for(<str key, AType tp, MuExp defaultExp> <- kwpDefaults){>.key(\"<key>\").value(<trans(defaultExp,jg)>)<}>.build();\n";
        if(!isEmpty(kwFields)){
            uncheckedWarning = "@SuppressWarnings(\"unchecked\")";
            argTypes = isEmpty(argTypes) ? kwpActuals : "<argTypes>, <kwpActuals>";
            if(constantDefaults(kwpDefaults)){
                kwpDefaultsName = "<qname>_$kwpDefaults";
                jg.setKwpDefaults(kwpDefaultsName);
                constantKwpDefaults = "final Map\<String,?\> <kwpDefaultsName> = <mapCode>";
             } else {
                jg.setKwpDefaults("$kwpDefaults");
                nonConstantKwpDefaults =  "Map\<String,?\> $kwpDefaults = <mapCode>";
             }   
        }
        return "<constantKwpDefaults>
               '<uncheckedWarning>
               '<returnType> <qname>(<argTypes>){
               '     <nonConstantKwpDefaults>
               '     <trans(fun.body, jg)>
               '}";
    } else
        throw "trans MuFunction: <ftype>";
}

str varWithPos(MuExp var)
    = var.name;
    //= "<var.name><var has pos ? "_<var.pos>" : "">";

set[str] getReferenceVars(MuFunction fun){
   scopedBlocks = {mb | /mb:muValueBlock(_) := fun.body} 
                + {sb | /sb:muVisit(MuExp subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor) := fun.body }
               
                ;
                
   vars = {varWithPos(unsetRec(v)) |  sb <- scopedBlocks, /muAssign(v, _) := sb} 
        + {varWithPos(unsetRec(v)) |  sb <- scopedBlocks, /muVarInit(v, _) := sb}
        + {varWithPos(unsetRec(v)) | /v:muVar(str name, str fuid, int pos, AType atype) := fun.body, fuid != fun.qname }
        ;
   println(vars);
   return vars;
}



JCode call(MuFunction fun, list[str] actuals, JGenie jg){
    return "<fun.qname>(<intercalate(", ", actuals)>)";
}

// Constants

// ---- muBool ----------------------------------------------------------------
    
JCode trans(muBool(b), JGenie jg) = "<b>"; 

// ---- muInt -----------------------------------------------------------------      
                                      
JCode trans(muInt(int n), JGenie jg) = "<n>";  

// ---- muCon -----------------------------------------------------------------                                       

JCode trans(muCon(value v), JGenie jg) = jg.shareConstant(v);

                       
JCode trans(muFun1(loc uid), JGenie jg){ // TODO
   
    //fun = muFunctions[fuid];
    //ftype = fun.ftype;
    ftype = jg.getType(uid);
    externalVars = jg.getExternalVars(uid);
    nformals = size(ftype.formals);
    sep = nformals > 0 ? "," : "";
    
    funInstance = "new FunctionInstance<nformals>\<<atype2java(ftype.ret)><sep><intercalate(",", [atype2java(ft) | ft <- ftype.formals])>\>";
    
    formals = intercalate(", ", ["$<i>" | i <- [0..nformals]]);
    
    ext_formals = formals;
    if(!isEmpty(externalVars)){
           ext_actuals = intercalate(", ", [id | <id, d> <-externalVars, d < uid]);
           //ext_actuals = intercalate(", ", ["<v.name>_<v.pos>" | v <- fun.externalVars]);
           ext_formals = isEmpty(formals) ? ext_actuals : "<actuals>, <ext_actuals>";
    }
    
    return "<funInstance>((<formals>) -\> { return <jg.getAccessor(uid) >(<ext_formals>); })";
}          

          //| muFun2(str fuid, str scopeIn)                       // *muRascal* function constant: nested functions and closures
          //
          //| muOFun(str fuid)                                    // *Rascal* function, i.e., overloaded function at the root
       
JCode trans(muOFun(str fuid), JGenie jg){
    fun = muFunctions[fuid];
    ftype = fun.ftype;
    nformals = fun.nformals;
    sep = nformals > 0 ? "," : "";
    
    funInstance = "new FunctionInstance<nformals>\<<atype2java(ftype.ret)><sep><intercalate(",", [atype2java(ft) | ft <- ftype.formals])>\>";
    
    formals = intercalate(", ", ["$<i>" | i <- [0..fun.nformals]]);
    ext_formals = formals;
    if(!isEmpty(fun.externalVars)){
           ext_actuals = intercalate(", ", ["<v.name>_<v.pos>" | v <- fun.externalVars]);
           formals = isEmpty(formals) ? ext_actuals : "<actuals>, <ext_actuals>";
    }
    return "<funInstance>((<formals>) -\> { return <colon2ul(fuid)>(<formals>); })";
}

          //| muConstr(str fuid)                                  // Constructor
          
// Variables

set[str] varInstructions = {"muModuleVar", "muVar", "muTmp", "muTmpInt", "muTmpBool", "muTmpWriter", "muTmpMatcher", "muTmpStrWriter", "muTmpTemplate", "muTmpException"};


bool isVar(MuExp exp)
    = getName(exp) in varInstructions;

//// ---- muModuleVar -----------------------------------------------------------
//
//JCode trans(var:muModuleVar(str name, AType atype), JGenie jg{
//    return name;
//}

// ---- muVar -----------------------------------------------------------------
        
JCode trans(var:muVar(str name, str fuid, int pos, AType atype), JGenie jg)
    = jg.isRefVar("<name>_<pos>") ? "<name>.value" : "<name>";

// ---- muTmp -----------------------------------------------------------------

JCode trans(var: muTmp(str name, str fuid, AType atype), JGenie jg)
    = jg.isRefVar(name) ? "<name>.value" : "<name>";

// ---- muTmpInt --------------------------------------------------------------

 JCode trans(var: muTmpInt(str name, str fuid) , JGenie jg)
    =  jg.isRefVar(name) ? "<name>.value" : "<name>";
    
// ---- muTmpBool --------------------------------------------------------------

 JCode trans(var: muTmpBool(str name, str fuid) , JGenie jg)
    =  jg.isRefVar(name) ? "<name>.value" : "<name>";
    
// ---- muTmpWriter -----------------------------------------------------------

JCode trans(var: muTmpWriter(str name, str fuid) , JGenie jg)
    = jg.isRefVar(name) ? "<name>.value" : "<name>";
    
// ---- muTmpMatcher ----------------------------------------------------------

JCode trans(var: muTmpMatcher(str name, str fuid) , JGenie jg)
    =  jg.isRefVar(name) ? "<name>.value" : "<name>";
    
// ---- muTmpStrWriter --------------------------------------------------------

JCode trans(var: muTmpStrWriter(str name, str fuid) , JGenie jg)
    =  jg.isRefVar(name) ? "<name>.value" : "<name>";
    
// ---- muTmpTemplate --------------------------------------------------------

JCode trans(var: muTmpTemplate(str name, str fuid) , JGenie jg)
    =  jg.isRefVar(name) ? "<name>.value" : "<name>";
    
// ---- muTmpException -------------------------------------------------------

JCode trans(var: muTmpException(str name, str fuid) , JGenie jg)
    =  jg.isRefVar(name) ? "<name>.value" : "<name>.getValue()";

    
// ---- muVarInit --------------------------------------------------------------

str parens(str code)
    = endsWith(code, ";\n") ? "(<code[0..-2]>)" : code;

 JCode trans(muVarInit(v: muVar(str name, str fuid, int pos, AType atype), MuExp exp), JGenie jg){
    jtype = atype2java(atype);
    return jg.isRefVar("<name>_<pos>") || jg.isExternalVar(v)
                                       ? "final ValueRef\<<jtype>\> <name> = new ValueRef\<<jtype>\>(<trans(exp,jg)>);\n"
                                       : "<jtype> <name> = (<jtype>)<parens(trans(exp, jg))>;\n";  
}

JCode trans(muVarInit(muTmp(str name, str fuid, AType atype), MuExp exp), JGenie jg){
    jtype = atype2java(atype);
    return jg.isRefVar(name) ? "final ValueRef\<<jtype>\> <name> = new ValueRef\<<jtype>\>(<trans(exp,jg)>);\n"
                             : "<jtype> <name> = (<jtype>)<parens(trans(exp, jg))>;\n";
}

JCode trans(muVarInit(muTmpInt(str name, str fuid), MuExp exp), JGenie jg){
    rhs = muCon(int n) := exp ? "<n>" : trans(exp, jg);
    return jg.isRefVar(name) ? "final IntRef <name> = new IntRef(<rhs>);\n"
                             : "int <name> = <rhs>;\n";
}

JCode trans(muVarInit(muTmpBool(str name, str fuid), MuExp exp), JGenie jg){
    rhs = muCon(bool b) := exp ? "<b>" : trans(exp, jg);
    return jg.isRefVar(name) ? "final BoolRef <name> = new BoolRef(<rhs>);\n"
                             : "boolean <name> = <rhs>;\n";
}

str writerKind(str name)
    = startsWith(name, "list") ? "List" : (startsWith(name, "set") ? "Set" : "Map");
    
JCode trans(muVarInit(muTmpWriter(str name, str fuid), MuExp exp), JGenie jg)
    = "I<writerKind(name)>Writer <name> = <trans(exp, jg)>;\n";

JCode trans(muVarInit(muTmpMatcher(str name, str fuid), MuExp exp), JGenie jg){
    rhs = trans(exp, jg);
    return jg.isRefVar(name) ? "final MatcherRef <name> = new MatcherRef(<rhs>);\n"
                             : "Matcher <name> = <rhs>;\n";
}

JCode trans(muVarInit(muTmpStrWriter(str name, str fuid), MuExp exp), JGenie jg){
    rhs = trans(exp, jg);
    return jg.isRefVar(name) ? "final StringWriterRef <name> = new StringWriterRef(<rhs>);\n"
                             : "StringWriter <name> = <rhs>;\n";
}

JCode trans(muVarInit(muTmpTemplate(str name, str fuid), MuExp exp), JGenie jg){
    rhs = trans(exp, jg);
    return jg.isRefVar(name) ? "final TemplateRef <name> = new TemplateRef(<rhs>);\n"
                             : "Template <name> = <rhs>;\n";
}

// --- muConInit --------------------------------------------------------------

 JCode trans(muConInit(v:muVar(str name, str fuid, int pos, AType atype), MuExp exp), JGenie jg){
    jtype = atype2java(atype);
    if(jg.isExternalVar(v)){  
        return "final ValueRef\<<jtype>\> <name> = new ValueRef\<<jtype>\>(<trans(exp,jg)>);\n";
    }
    return "<jtype> <name> = <transWithCast(atype, exp, jg)>;\n";
}
    
JCode trans(muConInit(muTmp(str name, str fuid, AType atype), MuExp exp), JGenie jg){
    jtype = atype2java(atype);
    return jg.isRefVar(name) ? "final ValueRef\<<jtype>\> <name> = new ValueRef\<<jtype>\>(<trans(exp,jg)>);\n"
                             : "final <jtype> <name> = <transWithCast(atype, exp, jg)>;\n";
}

JCode trans(muConInit(muTmpInt(str name, str fuid), MuExp exp), JGenie jg){
    rhs = muCon(int n) := exp ? "<n>" : trans(exp, jg);
    return jg.isRefVar(name) ? "final IntRef <name> = new IntRef(<rhs>);\n"
                             : "final int <name> = <rhs>;\n";
}

JCode trans(muConInit(muTmpBool(str name, str fuid), MuExp exp), JGenie jg){
    rhs = muCon(bool b) := exp ? "<b>" : trans(exp, jg);
    return jg.isRefVar(name) ? "final BoolRef <name> = new BoolRef(<rhs>);\n"
                             : "final bool <name> = <rhs>;\n";
}
    
JCode trans(muConInit(muTmpWriter(str name, str fuid), MuExp exp), JGenie jg)
    = "final I<writerKind(name)>Writer <name> = <trans(exp, jg)>;\n";

str transWithCast(AType atype, con:muCon(c), JGenie jg) = trans(con, jg);

default str transWithCast(AType atype, MuExp exp, JGenie jg) = "(<atype2java(atype)>)<parens(trans(exp, jg))>";

JCode trans(muConInit(muTmpMatcher(str name, str fuid), MuExp exp), JGenie jg){
    rhs = trans(exp, jg);
    return jg.isRefVar(name) ? "final MatcherRef <name> = new MatcherRef(<rhs>);\n"
                             : "final Matcher <name> = <rhs>;\n";
}

JCode trans(muConInit(muTmpStrWriter(str name, str fuid), MuExp exp), JGenie jg){
    rhs = trans(exp, jg);
    return jg.isRefVar(name) ? "final StringWriterRef <name> = new StringWriterRef(<rhs>);\n"
                             : "final StringWriter <name> = <rhs>;\n";
}  

JCode trans(muConInit(muTmpTemplate(str name, str fuid), MuExp exp), JGenie jg){
    rhs = trans(exp, jg);
    return jg.isRefVar(name) ? "final TemplateRef <name> = new TemplateRef(<rhs>);\n"
                             : "final Template <name> = <rhs>;\n";
}  

// ---- muAssign --------------------------------------------------------------

JCode trans(muAssign(v:muVar(str name, str fuid, int pos, AType atype), MuExp exp), JGenie jg)
    = "<name><jg.isRefVar("<name>_<pos>") || jg.isExternalVar(v) ? ".value" : ""> = <transWithCast(atype, exp, jg)>;\n";
    
JCode trans(muAssign(muTmp(str name, str fuid, AType atype), MuExp exp), JGenie jg)
    = "<name><jg.isRefVar(name) ? ".value" : ""> = <trans(exp, jg)>;\n";

JCode trans(muAssign(muTmpInt(str name, str fuid), MuExp exp), JGenie jg){
    return"<name><jg.isRefVar(name) ? ".value" : ""> = <trans2NativeInt(exp, jg)>;\n";
}
   
JCode trans(muAssign(muTmpBool(str name, str fuid), MuExp exp), JGenie jg)
    = "<name><jg.isRefVar(name) ? ".value" : ""> = <trans2NativeBool(exp, jg)>;\n";
    
JCode trans(muAssign(muTmpWriter(str name, str fuid), MuExp exp), JGenie jg)
    = "<name><jg.isRefVar(name) ? ".value" : ""> = <trans(exp, jg)>;\n";

// Call/Apply/return      

JCode trans(muCall(MuExp fun, list[MuExp] largs), JGenie jg){
    actuals = [trans(arg, jg) | arg <- largs];
         
    if(muConstr(AType ctype) := fun){
        return makeConstructorCall(ctype, actuals, jg);        
    }
    if(muConstrCompanion(str fname) := fun){
        return call(muFunctions[fname], actuals, jg);
    }
    if(muCon(str s) := fun){
        if(map[str,value] kwmap := actuals[-1]){
            return makeNode(s, actuals[0..-1], keywordParameters = kwmap);
        }
        throw "muCall: kwmap, <actuals>";
    }
    if(muVar(str name, str fuid, int pos, AType atype):= fun){
        return "<trans(fun, jg)>(<intercalate(", ", actuals)>)";
    }
    if(muFun1(loc uid) := fun){
       externalVars = jg.getExternalVars(uid);
       if(!isEmpty(externalVars)){
            actuals += [ id | <id, d> <- externalVars ];
       }
       return "<jg.getAccessor(uid)>(<intercalate(", ", actuals)>)";
    }
    
    throw "muCall: <fun>";
}

// ---- muOCall3 --------------------------------------------------------------

JCode trans(muOCall3(MuExp fun, AType ftype, list[MuExp] largs, loc src), JGenie jg){
    actuals = for(arg <- largs){
                append trans(arg, jg);
            }
    if(muOFun(str fname) := fun){;
        //if(overloadedAType(overloads) := ftype){
            return "$me.<resolved2overloaded[fname]>_<atype2descriptor(ftype)>(<intercalate(", ", actuals)>)";
       // } else {
       //         return "<replaceAll(fname, "::", "_")>(<intercalate(", ", actuals)>)";
      //  }
    }
    
    if(muFun1(loc uid) := fun){
        return "<jg.getAccessor(uid)>(<intercalate(", ", actuals)>)";
    }
    if(muCon(str s) := fun){
        return "$VF.node(<intercalate(", ", actuals)>)";
        //} else {
        //    return "$VF.node((<intercalate(", ", actuals[0..-1])>, keywordParameters=<actuals[-1]>)";
        //}
        throw "mOCall3: kwmap, <actuals>";
    }
    
    return "((<atype2java(ftype)>)<trans(fun, jg)>).call(<intercalate(", ", actuals)>)";
  
    throw "muOCall3: <fun>";
}

JCode trans(muKwpFieldAccess("aadt", AType consType, MuExp cons, str fieldName), JGenie jg)
    = "$get_<consType.adt.adtName>_<fieldName>(<trans(cons, jg)>)";

JCode trans(muFieldAccess("aadt", AType consType, MuExp cons, str fieldName), JGenie jg){
    base = trans(cons, jg);
    qFieldName = "\"<fieldName>\"";
    for(field <- consType.fields){
        if(fieldName == field.label){
            return "((<atype2java(field)>)<base>.get(<qFieldName>))";
        }
    }
   
    for(<AType kwType, Expression exp> <- consType.kwFields){
        if(fieldName == kwType.label){
            expCode = trans(exp, jg);
            if(muCon(_) := expCode){
                "<base>.asWithKeywordParameters().hasParameter(<qFieldName>) ? <base>.asWithKeywordParameters().getParameter(<qFieldName>) : <expCode>";
            } else {
                return "<base>.asWithKeywordParameters().getParameter(<qFieldName>)";
            }
        }
    }
 }

JCode trans(muFieldUpdate("aadt", AType atype, MuExp cons, str fieldName, MuExp repl), JGenie jg)
    = "<trans(cons, jg)>.set(\"<fieldName>\", <trans(repl, jg)>)";
    
// ---- muCallPrim2 -----------------------------------------------------------

JCode trans(muCallPrim2(str name, loc src), JGenie jg){
    return transPrim(name, [], jg);
}

// ---- muCallPrim3 -----------------------------------------------------------

JCode trans(muCallPrim3(str name, list[MuExp] exps, loc src), JGenie jg){
    actuals = transPrimArgs(name, exps, jg);
    return transPrim(name, actuals, jg);
}

JCode trans(muCallJava(str name, str class, AType funType, int reflect, list[MuExp] largs, str enclosingFun), JGenie jg){
    jg.addImportedLibrary(class);
  
    actuals = [ trans(arg, jg) | arg <- largs ];
    if(!isEmpty(funType.kwFormals)){
        kwpDefaultsVar = jg.getKwpDefaults();
        kwpActuals = "$kwpActuals"; 
        for(kwFormal <- funType.kwFormals){
            kwName = kwFormal.fieldType.label;
            actuals += "(<atype2java(kwFormal.fieldType)>)(<kwpActuals>.containsKey(\"<kwName>\") ? <kwpActuals>.get(\"<kwName>\") : <kwpDefaultsVar>.get(\"<kwName>\"))";
        }
    }
     return "(<atype2java(funType.ret)>)$<getBaseClass(class)>.<name>(<intercalate(", ", actuals)>)";
}


// ---- muReturn0 -------------------------------------------------------------

JCode trans(muReturn0(), JGenie jg){
    return "return;";
}

JCode trans(muReturn0FromVisit(), JGenie jg)
    = "ts.setLeavingVisit(true);
      'return;\n";

str semi(str code)
    = (endsWith(code, ";") || endsWith(code, ";\n")) ? code : "<code>;";
 
JCode trans2Void(MuExp exp, JGenie jg)
   = ""
   when getName(exp) in {"muCon", "muVar", "muTmp"};

JCode trans2Void(MuExp exp, JGenie jg)
   = "<trans(exp, jg)>;\n"
   when getName(exp) in {"muOCall3"};
   
JCode trans2Void(muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart), JGenie jg)
   = "<trans2NativeBool(cond, jg)> ? <trans(thenPart, jg)> : <trans(elsePart, jg)>";
 
default JCode trans2Void(MuExp exp, JGenie jg){
    return trans(exp, jg);
}   
// ---- muReturn1 -------------------------------------------------------------

JCode trans(muReturn1(MuExp exp), JGenie jg){
    return "return <trans(exp, jg)>;\n";
}

JCode trans(muReturn1FromVisit(MuExp exp), JGenie jg)
    = "ts.setLeavingVisit(true);
      'return <trans(exp, jg)>;\n";

//          | muFilterReturn()                                    // Return for filer statement

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
           '                           : Util.kwpMap(<for(<str key,  AType atype, MuExp exp> <- kwpDefaults, muCon(_) !:= exp){>\"<key>\", <kwpActuals>.containsKey(\"<key>\") ? <kwpActuals>.get(\"<key>\") : <trans(exp,jg)>)<}>)";
}

// ---- muVarKwp --------------------------------------------------------------

JCode trans(var:muVarKwp(str name, str fuid, AType atype),  JGenie jg)
    = "(<atype2java(atype)>) ($kwpActuals.containsKey(\"<name>\") ? $kwpActuals.get(\"<name>\") : <jg.getKwpDefaults()>.get(\"<name>\"))";

JCode trans(muAssign(muVarKwp(str name, str fuid, AType atype), MuExp exp), JGenie jg)
    = "$kwpActuals.put(\"<name>\", <trans(exp, jg)>);\n";

JCode trans(muIsKwpDefined(MuExp exp, str kwpName), JGenie jg)
    = "<trans(exp, jg)>.asWithKeywordParameters().hasParameter(\"<kwpName>\")";

JCode trans(muHasKwp(MuExp exp, str kwName), JGenie jg)
    = "<trans(exp, jg)>.asWithKeywordParameters().hasParameter(\"<kwName>\")";

JCode trans(muGetKwp(MuExp exp, AType atype, str kwpName), JGenie jg){
   if(acons(AType adt, list[AType] fields, list[Keyword] kwFields) := atype){
        return "$get_<atype.adtName>_<kwpName>(<trans(exp, jg)>)";
   } else if(anode(_) := atype){
       return "<trans(exp, jg)>.asWithKeywordParameters().getParameter(\"<kwpName>\")";
   } else if(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole) := atype){
       return "$get_<adtName>_<kwpName>(<trans(exp, jg)>)";
   }
   throw "muGetKwp: <atype>, <kwpName>";
}

JCode trans(muGetKwpFromConstructor(MuExp exp, AType atype, str kwpName), JGenie jg)
    = "(<atype2java(atype)>)<trans(exp, jg)>.asWithKeywordParameters().getParameter(\"<kwpName>\")";
 

JCode trans(muInsert(MuExp exp), JGenie jg)
    = "ts.setMatchedAndChanged(true, true);
      'return <trans(exp, jg)>;\n";

// Assignment, If and While

JCode trans(muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart), JGenie jg){
    return "if(<trans2NativeBool(cond, jg)>){
            '   <trans(thenPart, jg)>
            '} else {
            '   <trans(elsePart, jg)>
            '}\n";
}

JCode trans(muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart), JGenie jg)
   = "(<trans2NativeBool(cond, jg)> ? <trans(thenPart, jg)> : <trans(elsePart, jg)>)";
 
JCode trans(muIf(MuExp cond, MuExp thenPart), JGenie jg){
    return "if(<trans2NativeBool(cond, jg)>){
            '   <trans(thenPart, jg)>
            '}\n";
}

JCode trans(muIfEqualOrAssign(MuExp var, MuExp other, MuExp body), JGenie jg){
    return "if(<trans(var, jg)> == null){
           '    <trans(muAssign(var, other), jg)>
           '    <trans(body, jg)>
           '} else {
           '    <trans(muAssign(var, other), jg)>
           '    if(<trans(var, jg)>.isEqual(<trans(other, jg)>){
           '       <trans(body, jg)>
           '    }
           '}\n";
}

JCode trans(muWhileDo(str label, MuExp cond, MuExp body), JGenie jg){
    return "<isEmpty(label) ? "" : "<label>:">
           '    while(<trans2NativeBool(cond, jg)>){
           '        <trans(body, jg)>
           '    }\n";
}

JCode trans(muDoWhile(str label, MuExp body, MuExp cond), JGenie jg){
    return "<isEmpty(label) ? "" : "<label>:">
           '    do{
           '        <trans(body, jg)>
           '    } while(<trans2NativeBool(cond, jg)>);\n";
}

JCode trans(mw: muForAll(str btscope, MuExp var, MuExp iterable, MuExp body), JGenie jg){
    return
    "<isEmpty(btscope) ? "" : "<btscope>:">
    'for(IValue <var.name> : <trans(iterable, jg)>){
    '    <trans(body, jg)>
    '}\n";
}

JCode trans(muEnter(btscope, muBlock([*exps, muSucceed(btscope)])), JGenie jg)
    = "<trans(muBlock(exps), jg)>";
    
JCode trans(muEnter(btscope, muFail(btscope)), JGenie jg)
    = "";
    
JCode trans(muEnter(btscope, asg:muAssign(_,_)), JGenie jg)
    = trans(asg, jg);
    
JCode trans(muEnter(btscope,ret: muReturn1(_)), JGenie jg)
    = trans(ret, jg);

default JCode trans(muEnter(btscope, MuExp exp), JGenie jg)
    = "<btscope>: 
      '    do {
      '        <trans(exp, jg)>
      '    } while(false)\n";

JCode trans(muSucceed(str label), JGenie jg)
    = "break <label>;";

JCode trans(muFail(str label), JGenie jg)
    = "continue <label>;";
    
JCode trans(muBreak(str label), JGenie jg)
    = "break <label>;\n";
    
JCode trans(muContinue(str label), JGenie jg)
    = "continue <label>;\n";

JCode trans(muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp exp), JGenie jg){
    //TODO: cover all combinations int/real
    base = getName(var.atype);
    fst = jg.newTmp("fst"); fstContrib = "";
    scd = jg.newTmp("scd"); scdContrib = "";
    lst = jg.newTmp("lst"); lstContrib = "";
    dir = jg.newTmp("dir"); dirContrib = ""; dirKnown = false; dirUp = true;
    delta = jg.newTmp("delta"); deltaContrib = "";
    deltaVal = delta;
    
    if(muCon(_) := first) fst = trans(first, jg); else fstContrib = "final <atype2java(var.atype)> <fst> = <trans(first, jg)>;\n";
    if(muCon(_) := last) lst = trans(last, jg); else lstContrib = "final <atype2java(var.atype)> <lst> = <trans(last, jg)>;\n";
    
    testCode = "";
    if(muCon(int f) := first && muCon(int l) := last){
        dirKnown = true;
        dirUp = f < l;
        dirContrib = "final boolean <dir> = <f < l>;";
        testCode = dirUp ? "<transPrim("<base>_less_<base>", [trans(var,jg), lst], jg)>.getValue()"
                         : "<transPrim("<base>_greater_<base>", [trans(var,jg), lst], jg)>.getValue()";
    } else {
        dirContrib = "final boolean <dir> = <fst>.less(<lst>).getValue();\n";
        testCode = "<dir> ? <transPrim("<base>_less_<base>", [trans(var,jg), lst], jg)>.getValue() : <transPrim("<base>_greater_<base>", [trans(var,jg), lst], jg)>.getValue()";
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
            deltaContrib = "final <atype2java(var.atype)> <delta> = <trans(second, jg)>.subtract(<fst>)";
        }
    
    } else {
        deltaCode = muCon(0) := second ? "<dir> ? <trans(muCon(1), jg)> : <trans(muCon(-1), jg)>" : "<trans(second, jg)>.subtract(<fst>)";
        deltaContrib = "final <atype2java(var.atype)> <delta> = <deltaCode>;\n";
    }
    
    return 
    "<fstContrib><lstContrib><dirContrib><deltaContrib>
    '<isEmpty(label) ? "" : "<label>:">
    'for(<atype2java(var.atype)> <var.name> = <fst>; <testCode>; <var.name> = <transPrim("<base>_add_<base>", [trans(var,jg), deltaVal], jg)>){
    '    <trans(exp, jg)>
    '}
    '";
}

JCode trans(muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp exp), JGenie jg){
    return 
    "<isEmpty(label) ? "" : "<label>:">
    'for(int <var.name> = <ifirst>; <var.name> \< <trans(last, jg)>; <var.name> += <istep>){
    '   <trans(exp, jg)>
    '}
    '";
}
         
JCode trans(muSwitch(MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint), JGenie jg){
    return "<trans(exp, jg)>
           'switch(Util.getFingerprint(<exp.var.name>, <useConcreteFingerprint>)){
           '<for(muCase(int fingerprint, MuExp exp) <- cases){>
           '    case <fingerprint>:
           '        <trans(exp, jg)>
           '        break;
           '<}>
           '    <defaultExp == muBlock([]) ? "" : "default: <trans(defaultExp, jg)>">
           '}\n";
}

JCode trans(muVisit(MuExp exp, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor), JGenie jg){
    direction  = vdescriptor.direction  ? "BottomUp"   : "TopDown";
    progress   = vdescriptor.progress   ? "Continuing" : "Breaking";
    fixedpoint = vdescriptor.fixedpoint ? "Yes"        : "No";
    rebuild    = vdescriptor.rebuild    ? "Yes"        : "No";
    
    defaultCode = defaultExp == muBlock([]) ? "" : 
                  "    default: 
                  '        <trans(defaultExp, jg)>";
    return
      "$TRAVERSE.traverse(DIRECTION.<direction>, PROGRESS.<progress>, FIXEDPOINT.<fixedpoint>, REBUILD.<rebuild>, 
      '     new DescendantDescriptor($VF.set(), $VF.set(), null, $VF.bool(false), null),
      '     <trans(exp.exp,jg)>,
      '     (IVisitFunction) (IValue <exp.var.name>, TraversalState ts) -\> {
      '         switch(Util.getFingerprint(<exp.var.name>, <vdescriptor.descendant.useConcreteFingerprint>)){
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
    = "new DescendantMatchIterator(<trans(subject, jg)>, new DescendantDescriptor($VF.set(), $VF.set(), null, $VF.bool(false), null))";


JCode trans(muFailCase(), JGenie jg)
    = "break;\n";
    
JCode trans(muSucceedSwitchCase(), JGenie jg)
    = "break;";
    
JCode trans(muSucceedVisitCase(), JGenie jg)
    = "ts.setMatched(true); break;";

// ---- muFailReturn ----------------------------------------------------------

JCode trans(muFailReturn(),  JGenie jg)
    = "return null;";
          
// Lists of expressions

JCode trans(muBlock(list[MuExp] exps), JGenie jg){
    return "<for(exp <- exps){><trans2Void(exp, jg)><}>";
}
   
JCode trans(muValueBlock(list[MuExp] exps), JGenie jg){
    return "((Block) () -\> {
           '<for(exp <- exps[0..-1]){><trans2Void(exp, jg)><}> 
           '<trans(muReturn1(exps[-1]), jg)>
           '}).compute()";
}

// Exceptions
      
JCode trans(muThrow(muTmpException(str name, str fuid), loc src), JGenie jg){
    return "throw <name>;";
}

default JCode trans(muThrow(MuExp exp, loc src), JGenie jg){
    return "throw new RascalException(<trans(exp, jg)>);";
}

JCode trans(muTry(MuExp exp, MuCatch \catch, MuExp \finally), JGenie jg){
println(exp);
println(\catch);
println(\finally);
finallyCode = trans(\finally, jg);
    return "try {
                <trans(exp, jg)>
           '} <trans(\catch, jg)>
           '<if(!isEmpty(finallyCode)){>finally { 
           '    <finallyCode>} <}>
           ";
}  

JCode trans(muCatch(MuExp thrown_as_exception, MuExp thrown, MuExp body), JGenie jg){
    jtype = atype2java(thrown.atype);
    return "catch (RascalException <thrown_as_exception.name>) {
           '    IValue <thrown.name> = <thrown_as_exception.name>.getValue();
           '   
           '    <trans(body, jg)>
           '}";
}

// ---- muCheckArgTypeAndCopy -------------------------------------------------

JCode trans(muCheckArgTypeAndCopy(str name, int from, AType tp, int to), JGenie jg)
    = "";
    //= "<atype2java(tp)> <name>;
    //  'if(<name>$<from>.getType().isSubtypeOf(<atype2typestore(tp)>)).getValue()){
    //  '   <name> = <name>$<from>;
    //  '} else {
    //  '   return null;
    //  '}";     

bool producesNativeBool(muCallPrim3(str name, list[MuExp] args, loc src)){
    if(name in {"equal", "notequal"}) return true;
    fail producesNativeBool;
}

bool producesNativeBool(MuExp exp)
    = getName(exp) in {"muTmpBool", "muEqual", "muEqualInt", "muNotNegative", "muIsKwpDefined", "muHasKwp", "muHasKwpWithValue", /*"muHasType",*/ "muHasTypeAndArity",
                  "muHasNameAndArity", "muValueIsSubType", "muValueIsSubTypeOfValue", "muGreaterEqInt", "muAnd", "muNot",
                  "muRegExpFind" };
                  
bool producesNativeInt(MuExp exp)
    = getName(exp) in {"muTmpInt", "muSize", "muAddInt", "muSubInt", "muRegExpBegin", "muRegExpEnd"};
    

str getIntegerFor(MuExp exp)
    = producesNativeInt(exp) ? "" : ".getValue()";
    
    
JCode trans2NativeBool(muCon(value b), JGenie jg)
    = "<b>";
    
default JCode trans2NativeBool(MuExp exp, JGenie jg)
    = "<trans(exp, jg)><producesNativeBool(exp) ? "" : ".getValue()">";
    
JCode trans2NativeInt(muCon(value n), JGenie jg)
    = "<n>";
    
default JCode trans2NativeInt(MuExp exp, JGenie jg)
    = "<trans(exp, jg)><producesNativeInt(exp) ? "" : ".getValue()">";
    
JCode trans2IInteger(MuExp exp, JGenie jg)
    = producesNativeInt(exp) ? "$VF.integer(<trans(exp, jg)>)" : trans(exp, jg);
    
JCode trans2IBool(MuExp exp, JGenie jg)
    = producesNativeBool(exp) ? "$VF.bool(<trans(exp, jg)>)" : trans(exp, jg);
    
// -----

JCode trans(muRequire(MuExp exp, str msg, loc src), JGenie jg)
    = "if(!(<trans2NativeBool(exp, jg)>)){
      ' throw new RuntimeException(\"<msg> at <src>\");
      '}\n";
 
JCode trans(muEqual(MuExp exp1, MuExp exp2), JGenie jg)
    = "<trans(exp1, jg)>.isEqual(<trans(exp2, jg)>)";
      
JCode trans(muEqualInt(MuExp exp1, MuExp exp2), JGenie jg)
    = "<trans2NativeInt(exp1, jg)> == <trans2NativeInt(exp2, jg)>";
    
JCode trans(muNotNegative(MuExp exp), JGenie jg)
    = "<trans2NativeInt(exp, jg)> \>= 0";

JCode trans(muValueIsSubType(MuExp exp, AType tp), JGenie jg){
    return !isVar(exp) && exp has atype && exp.atype == tp ? "true"
                      : "<trans(exp, jg)>.getType().isSubtypeOf(<jg.shareType(tp)>)";
}
JCode trans(muValueIsSubTypeOfValue(MuExp exp1, MuExp exp2), JGenie jg)
    ="<trans(exp1, jg)>.getType().isSubtypeOf(<trans(exp2, jg)>.getType())";
    

JCode trans(muHasTypeAndArity(AType atype, int arity, MuExp exp), JGenie jg){
    v = trans(exp, jg);
    t = atype2java(atype);
    switch(getName(atype)){
        case "atuple": return "<v> instanceof <t> && <v>.arity == <arity>";
    }
    throw "muHasTypeAndArity: <atype>, <arity>";
}

JCode trans(muHasNameAndArity(AType atype, str name, int arity, MuExp exp), JGenie jg){
    v = trans(exp, jg);
    if(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole) := atype){
       //return "<v>.getConstructorType() == <adtName>_<name>_<arity>";
       return "<v>.arity() == <arity> && <v>.getType() == <adtName>";
    } else {
        return "<v>.arity() == <arity> && <v>.getName().equals(\"<name>\")";
    }
  
}

JCode trans(muSize(MuExp exp, AType atype), JGenie jg)
    = "((<atype2java(atype)>)<trans(exp, jg)>).length()";

JCode trans(muSubscript(MuExp exp, MuExp idx), JGenie jg){
    return "<trans(exp, jg)>.get(<trans2NativeInt(idx, jg)>)";
}

JCode trans(muIncVar(MuExp var, MuExp exp), JGenie jg)
    = muCon(int n) := exp ? "<trans(var, jg)> += <n>;\n" :  "<trans(var, jg)> += <trans(exp, jg)>\n";
    
JCode trans(muSubInt(MuExp exp1, MuExp exp2), JGenie jg)
    = "<trans2NativeInt(exp1, jg)> - <trans2NativeInt(exp2, jg)>";
    
JCode trans(muAddInt(MuExp exp1, MuExp exp2), JGenie jg)
    = "<trans2NativeInt(exp1, jg)> + <trans2NativeInt(exp2, jg)>";
    
JCode trans(muGreaterEqInt(MuExp exp1, MuExp exp2), JGenie jg)
    = "<trans2NativeInt(exp1, jg)> \>= <trans2NativeInt(exp2, jg)>";

JCode trans(muAnd(MuExp exp1, MuExp exp2), JGenie jg){
    v1 = trans2NativeBool(exp1, jg);
    v2 = trans2NativeBool(exp2, jg);
    return v1 == "true" ? v2
                        : (v2 == "true" ? v1 : "<v1> && <v2>");
}

JCode trans(muNot(MuExp exp), JGenie jg){
    v = trans2NativeBool(exp, jg);
    return v == "true" ? "false" : "!<v>";                              
}

JCode trans(muSubList(MuExp lst, MuExp from, MuExp len), JGenie jg)
    = "<trans(lst, jg)>.sublist(<trans(from, jg)>, <trans(len, jg)>)";

// Regular expressions

JCode trans(muRegExpCompile(MuExp regExp, MuExp subject), JGenie jg)
    = "regExpCompile(<trans(regExp, jg)>, <trans(subject, jg)>)";
    
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
    
// String templates

JCode trans(muTemplate(str initial), JGenie jg)
    = "new Template($VF, \"<escapeAsJavaString(initial)>\")";

JCode trans(muTemplateBeginIndent(MuExp template, str indent), JGenie jg)
    = "<trans(template, jg)>.beginIndent(\"<indent>\");\n";
    
JCode trans(muTemplateEndIndent(MuExp template, str unindent), JGenie jg)
    = "<trans(template, jg)>.endIndent(\"<escapeAsJavaString(unindent)>\");\n";
    
JCode trans(muTemplateAdd(MuExp template, muCon(str s)), JGenie jg)
    = "<trans(template, jg)>.addStr(\"<escapeAsJavaString(s)>\");\n";
    
JCode trans(muTemplateAdd(MuExp template, str s), JGenie jg){
    if(isEmpty(s)) return "";
    s = replaceAll(s, "\n", "\\n");
    return "<trans(template, jg)>.addStr(\"<escapeAsJavaString(s)>\");\n";
}
    
default JCode trans(muTemplateAdd(MuExp template, MuExp exp), JGenie jg)
    = "<trans(template, jg)>.addVal(<trans(exp,jg)>);\n";

JCode trans(muTemplateClose(MuExp template), JGenie jg)
    = "<trans(template, jg)>.close()";
            
