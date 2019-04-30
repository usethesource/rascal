package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils.RascalException;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils.RascalExceptionFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ToplevelType;
import org.rascalmpl.uri.SourceLocationURICompare;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IDateTime;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.IRational;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.exceptions.InvalidDateTimeException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public abstract class $RascalModule {
	
	// Factories and stores
	public final static IValueFactory $VF = ValueFactoryFactory.getValueFactory();
	public final static TypeFactory $TF = TypeFactory.getInstance();
	public final static TypeStore $TS = new TypeStore();
	
	// DAT needed for AType, Tree and related types
	public static final Type AType = $TF.abstractDataType($TS, "AType");
	public static final Type ReifiedAType = $TF.abstractDataType($TS, "ReifiedAType");
	
	public static final Type Keyword = $TF.abstractDataType($TS, "Keyword");
	public static final Type Associativity = $TF.abstractDataType($TS, "Associativity");
	public static final Type AProduction = $TF.abstractDataType($TS, "AProduction");
	public static final Type SyntaxRole = $TF.abstractDataType($TS, "SyntaxRole");
	public static final Type Attr = $TF.abstractDataType($TS, "Attr");
	public static final Type Tree = $TF.abstractDataType($TS, "Tree");
	public static final Type ACharRange = $TF.abstractDataType($TS, "ACharRange");
	public static final Type ACondition = $TF.abstractDataType($TS, "ACondition");
	private static final Type str = $TF.stringType();	// convenience type
	
	
	
	public static final IBool Rascal_TRUE =  $VF.bool(true);
	public static final IBool Rascal_FALSE =  $VF.bool(false);
	
	// avoid
	public static final Type AType_avoid = $TF.constructor($TS, AType, "avoid");
	public static final Type AType_avoid_lab = $TF.constructor($TS, AType, "avoid", str, "label");
	
	public static IConstructor avoid() { return $VF.constructor(AType_avoid); }
	public static IConstructor avoid(IString label) { return $VF.constructor(AType_avoid_lab, label); }
	
	// abool
	public static final Type AType_abool = $TF.constructor($TS, AType, "abool");
	public static final Type AType_abool_lab = $TF.constructor($TS, AType, "abool", str, "label");
	
	public static IConstructor abool() { return $VF.constructor(AType_abool); }
	public static IConstructor abool(IString label) { return $VF.constructor(AType_abool_lab, label); }
	
	// aint
	public static final Type AType_aint = $TF.constructor($TS, AType, "aint");
	public static final Type AType_aint_lab = $TF.constructor($TS, AType, "aint", str, "label");
	public static IConstructor aint() { return $VF.constructor(AType_aint); }
	public static IConstructor aint(IString label) { return $VF.constructor(AType_aint_lab, label); }
	
	// areal
	public static final Type AType_areal = $TF.constructor($TS, AType, "areal");
	public static final Type AType_areal_lab = $TF.constructor($TS, AType, "areal", str, "label");
	
	public static IConstructor areal() { return $VF.constructor(AType_areal); }
	public static IConstructor areal(IString label) { return $VF.constructor(AType_areal_lab, label); }
	
	// arat
	public static final Type AType_arat = $TF.constructor($TS, AType, "arat");
	public static final Type AType_arat_lab = $TF.constructor($TS, AType, "arat", str, "label");
	
	public static IConstructor arat() { return $VF.constructor(AType_arat); }
	public static IConstructor arat(IString label) { return $VF.constructor(AType_arat_lab, label); }
	
	// anum	
	public static final Type AType_anum = $TF.constructor($TS, AType,  "anum");
	public static final Type AType_anum_lab = $TF.constructor($TS, AType,  "anum", str, "label");
	
	public static IConstructor anum() { return $VF.constructor(AType_anum); }
	public static IConstructor anum(IString label ) { return $VF.constructor(AType_anum_lab, label); }
	
	// astr
	public static final Type AType_astr = $TF.constructor($TS, AType,  "astr");
	public static final Type AType_astr_lab = $TF.constructor($TS, AType,  "astr", str, "label");
	
	public static IConstructor astr() { return $VF.constructor(AType_astr); }
	public static IConstructor astr(IString label) { return $VF.constructor(AType_astr_lab, label); }
	
	// aloc
	public static final Type AType_aloc = $TF.constructor($TS, AType,  "aloc");
	public static final Type AType_aloc_lab = $TF.constructor($TS, AType,  "aloc", str, "label");
	
	public static IConstructor aloc() { return $VF.constructor(AType_aloc); }
	public static IConstructor aloc(IString label) { return $VF.constructor(AType_aloc_lab, label); }

	// adatetime
	public static final Type AType_adatetime = $TF.constructor($TS, AType,  "adatetime");
	public static final Type AType_adatetime_lab = $TF.constructor($TS, AType,  "adatetime", str, "label");
	
	public static IConstructor adatetime() { return $VF.constructor(AType_adatetime); }
	public static IConstructor adatetime(IString label) { return $VF.constructor(AType_adatetime_lab, label); }
	
	// alist
	public static final Type AType_alist = $TF.constructor($TS, AType, "alist", AType, "elmType");
	public static final Type AType_alist_lab = $TF.constructor($TS, AType, "alist", AType, "elmType", str, "label");
	
	public static IConstructor alist(IConstructor t) { return $VF.constructor(AType_alist, t); }
	public static IConstructor alist(IConstructor t, IString label) { return $VF.constructor(AType_alist_lab, t, label); }
	
	// abag
	public static final Type AType_abag = $TF.constructor($TS, AType, "abag", AType, "elmType");
	public static final Type AType_abag_lab = $TF.constructor($TS, AType, "abag", AType, "elmType", str, "label");
	
	public static IConstructor abag(IConstructor t) { return $VF.constructor(AType_abag, t); }
	public static IConstructor abag(IConstructor t, IString label) { return $VF.constructor(AType_abag_lab, t, label); }
	
	// aset
	public static final Type AType_aset = $TF.constructor($TS, AType, "aset", AType, "elmType");
	public static final Type AType_aset_lab = $TF.constructor($TS, AType, "aset", AType, "elmType", str, "label");
	
	public static IConstructor aset(IConstructor t) { return $VF.constructor(AType_aset, t); }
	public static IConstructor aset(IConstructor t, IString label) { return $VF.constructor(AType_aset_lab, t, label); }
	
	// arel
	public static final Type AType_arel = $TF.constructor($TS, AType, "arel", AType, "elmType");
	public static final Type AType_arel_lab = $TF.constructor($TS, AType, "arel", AType, "elmType", str, "label");
	
	public static IConstructor arel(IConstructor[] ts) { return $VF.constructor(AType_arel, ts); }
	public static IConstructor arel(IConstructor[] ts, IString label) { 
			IValue[] vals = Arrays.copyOf(ts, ts.length+1);
			vals[ts.length]= label; 
			return $VF.constructor(AType_arel_lab, vals);
	}
	
	// alrel
	public static final Type AType_alrel = $TF.constructor($TS, AType, "alrel",AType, "elmType");
	public static final Type AType_alrel_lab = $TF.constructor($TS, AType, "alrel",AType, "elmType", str, "label");
	
	public static IConstructor alrel(IConstructor[] ts) { return $VF.constructor(AType_alrel, ts); }
	public static IConstructor alrel(IConstructor[] ts, IString label) {
		IValue[] vals = Arrays.copyOf(ts, ts.length+1);
		vals[ts.length]= label; 
		return $VF.constructor(AType_alrel, vals); 
	}
	
	// atuple
	public static final Type AType_atuple = $TF.constructor($TS, AType, "atuple", AType, "elmType");
	public static final Type AType_atuple_lab = $TF.constructor($TS, AType, "atuple", AType, "elmType", str, "label");
	
	public static IConstructor atuple(IConstructor[] ts) { return $VF.constructor(AType_atuple, ts); }
	public static IConstructor atuple(IConstructor[] ts, IString label) {
		IValue[] vals = Arrays.copyOf(ts, ts.length+1);
		vals[ts.length]= label; 
		return $VF.constructor(AType_atuple, vals); 
	}
	
	// amap
	public static final Type AType_amap = $TF.constructor($TS, AType, "amap", AType, "from", AType, "to");
	public static final Type AType_amap_lab = $TF.constructor($TS, AType, "amap", AType, "from", AType, "to", str, "label");
	
	public static IConstructor amap(IConstructor k, IConstructor v) { return $VF.constructor(AType_amap, k, v); }
	public static IConstructor amap(IConstructor k, IConstructor v, IString label) { return $VF.constructor(AType_amap_lab, k, v, label); }
	
	// afunc
	public static final Type AType_afunc = $TF.constructor($TS, AType, "afunc", AType, "ret", $TF.listType(AType), "formals", $TF.listType(Keyword), "kwFormals");
	//TODO: bool varArgs=false, str deprecationMessage="", bool isConcreteArg=false, int abstractFingerprint=0, int concreteFingerprint=0)
	//public static IConstructor afunc(IConstructor ret, IConstructor[] formals, IConstructor[] keywords) { return $VF.constructor(AType_afunc, formals, keywords)); }

	// anode
	public static final Type AType_anode = $TF.constructor($TS, AType,  "anode");
	public static final Type AType_anode_lab = $TF.constructor($TS, AType,  "anode", str, "label");
	
	public static IConstructor anode() { return $VF.constructor(AType_anode); }
	public static IConstructor anode(IString label) { return $VF.constructor(AType_anode_lab, label); }
	
	// aadt
	public static final Type AType_aadt = $TF.constructor($TS, AType, "aadt", str, "name", $TF.listType(AType), "parameters");
	public static final Type AType_aadt_lab = $TF.constructor($TS, AType, "aadt", str, "name", $TF.listType(AType), "parameters", str, "label");
	
	public static IConstructor aadt(IString adtName, IList parameters, IConstructor syntaxRole) { return $VF.constructor(AType_aadt, adtName, parameters, syntaxRole); }
	public static IConstructor aadt(IString adtName, IList parameters, IConstructor syntaxRole, IString label) { return $VF.constructor(AType_aadt_lab, adtName, parameters, syntaxRole, label); }

	// acons
	public static final Type AType_acons = $TF.constructor($TS, AType, "acons", AType, "aadt", $TF.listType(AType), "fields", $TF.listType(Keyword), "kwFields");
	public static final Type AType_acons_lab = $TF.constructor($TS, AType, "acons", AType, "aadt", $TF.listType(AType), "fields", $TF.listType(Keyword), "kwFields", str, "label");
	
	public static IConstructor acons(IConstructor adt, IList fields, IList kwFields) { return $VF.constructor(AType_acons, adt, fields, kwFields); }
	public static IConstructor acons(IConstructor adt, IList fields, IList kwFields, IString label) { return $VF.constructor(AType_acons_lab, adt, fields, kwFields, label); }
	
	// aprod
	public static final Type AType_aprod = $TF.constructor($TS, AType,  "aprod", AProduction, "production");
	public static final Type AType_aprod_lab = $TF.constructor($TS, AType,  "aprod", AProduction, "production", str, "label");
	
	public static IConstructor aprod(IConstructor production) { return $VF.constructor(AType_aprod, production); }
	public static IConstructor aprod(IConstructor production, IString label) { return $VF.constructor(AType_aprod_lab, production, label); }
	
	// aparameter
	public static final Type AType_aparameter = $TF.constructor($TS, AType, "aparameter", str , "pname", AType, "bound");
	
	public static IConstructor aparameter(IString pname, IConstructor bound) { return $VF.constructor(AType_aparameter, pname, bound); }
	
	// areified
	public static final Type AType_areified = $TF.constructor($TS, AType, "areified", AType, "atype");
	public static final Type AType_areified_lab = $TF.constructor($TS, AType, "areified", AType, "atype", str, "label");
	
	public static IConstructor areified(IConstructor t) { return $VF.constructor(AType_areified, t); }
	public static IConstructor areified(IConstructor t, IString label) { return $VF.constructor(AType_areified_lab, t, label); }
	
	// avalue
	public static final Type AType_avalue = $TF.constructor($TS, AType,  "avalue");
	public static final Type AType_avalue_lab = $TF.constructor($TS, AType,  "avalue", str, "label");
	
	public static IConstructor avalue() { return $VF.constructor(AType_avalue); }
	public static IConstructor avalue(IString label) { return $VF.constructor(AType_avalue_lab, label); }
	
	// atype (reified type constructor)
    static final Type AType_atype = $TF.constructor($TS, AType, "atype", ReifiedAType, "symbol", $TF.mapType(ReifiedAType,$TF.setType(ReifiedAType)), "definitions");
    
	public static final IConstructor reifiedAType(IConstructor t, IMap definitions) {
		IConstructor res = $VF.constructor(AType_atype, t, definitions);
		System.err.println(res);
		return res;
	}
	
	// ---- SyntaxRole --------------------------------------------------------
	
	public static final Type SyntaxRole_dataSyntax = $TF.constructor($TS,  SyntaxRole, "dataSyntax");
	public static final Type SyntaxRole_contextFreeSyntax = $TF.constructor($TS,  SyntaxRole, "contextfreeSyntax");
	public static final Type SyntaxRole_lexicalSyntax = $TF.constructor($TS,  SyntaxRole, "lexicalSyntax");
	public static final Type SyntaxRole_keywordSyntax = $TF.constructor($TS,  SyntaxRole, "keywordSyntax");
	public static final Type SyntaxRole_layoutSyntax = $TF.constructor($TS,  SyntaxRole, "layoutSyntax");
	public static final Type SyntaxRole_illegalSyntax = $TF.constructor($TS,  SyntaxRole, "illegalSyntax");
	
	public static IConstructor dataSyntax = $VF.constructor(SyntaxRole_dataSyntax);
	public static IConstructor contextFreeSyntax = $VF.constructor(SyntaxRole_contextFreeSyntax);
	public static IConstructor lexicalSyntax = $VF.constructor(SyntaxRole_lexicalSyntax);
	public static IConstructor keywordSyntax = $VF.constructor(SyntaxRole_keywordSyntax);
	public static IConstructor layoutSyntax = $VF.constructor(SyntaxRole_layoutSyntax);
	public static IConstructor illegalSyntax = $VF.constructor(SyntaxRole_illegalSyntax);
	
	/*************************************************************************/
	/*		Parse Trees														 */
	/*************************************************************************/
	
	// ---- Associativity -----------------------------------------------------

	// left
	public static final Type Associativity_left = $TF.constructor($TS,  Associativity, "left");
	
	public static IConstructor left() { return $VF.constructor(Associativity_left); }
	
	// right
	public static final Type Associativity_right = $TF.constructor($TS,  Associativity, "right");
	
	public static IConstructor right() { return $VF.constructor(Associativity_right); }
	
	// assoc
	public static final Type Associativity_assoc = $TF.constructor($TS,  Associativity, "assoc");
	
	public static IConstructor assoc() { return $VF.constructor(Associativity_assoc); }
	
	// non-assoc
	public static final Type Associativity_non_assoc = $TF.constructor($TS,  Associativity, "non_assoc");
	
	public static IConstructor non_assoc() { return $VF.constructor(Associativity_non_assoc); }
	
	// ---- Attr --------------------------------------------------------------

	// tag
	public static final Type Attr_tag = $TF.constructor($TS,  Attr, "tag", $TF.valueType(), "tag");
	
	public static IConstructor tag(IValue tag) { return $VF.constructor(Attr_tag, tag); }
	
	// assoc
	public static final Type Attr_assoc = $TF.constructor($TS,  Attr, "assoc", Associativity, "assoc");
	
	public static IConstructor assoc(IConstructor assoc) { return $VF.constructor(Attr_assoc, assoc); }
	
	// bracket
	public static final Type Attr_bracket = $TF.constructor($TS,  Attr, "bracket");
	
	public static IConstructor bracket() { return $VF.constructor(Attr_bracket); }
	
	// ---- Tree --------------------------------------------------------------
	
	// appl
	public static final Type Tree_appl = $TF.constructor($TS,  Tree, "appl",  AProduction, "aprod",  $TF.listType(Tree), "args");
	public static final Type Tree_appl_loc = $TF.constructor($TS,  Tree, "appl",  AProduction, "aprod",  $TF.listType(Tree), "args", $TF.sourceLocationType(), "src");
	
	public static IConstructor appl(IConstructor aprod, IList args) { return $VF.constructor(Tree_appl, aprod, args); }
	public static IConstructor appl(IConstructor aprod, IList args, ISourceLocation src) { return $VF.constructor(Tree_appl_loc, aprod, args, src); }
	
	// cycle
	public static final Type Tree_cycle = $TF.constructor($TS,  Tree, "cycle",  AType, "atype",  $TF.integerType(), "cyclelength");
	
	public static IConstructor cycle(IConstructor atype, IInteger cyclelength) { return $VF.constructor(Tree_cycle, atype, cyclelength);  }
	
	// amb
	public static final Type Tree_amb = $TF.constructor($TS,  Tree, "amb",  $TF.setType(Tree), "alternatives");
	
	public static IConstructor amb(ISet alternatives) { return $VF.constructor(Tree_amb, alternatives);  }
	
	// char
	public static final Type Tree_char = $TF.constructor($TS,  Tree, "char",  $TF.integerType(), "character");
	
	public static IConstructor tchar(IInteger character) { return $VF.constructor(Tree_char, character);  }	// TODO: char clashes with Java keyword
	
	// ---- AProduction -------------------------------------------------------
	
	// choice
	public static final Type AProduction_choice = $TF.constructor($TS,  AProduction, "choice", AType, "def", $TF.setType(AProduction), "alternatives");
	
	public static IConstructor choice(IConstructor def, ISet alternatives) { return $VF.constructor(AProduction_choice, def, alternatives); }
	
	// prod
	public static final Type AProduction_prod = $TF.constructor($TS,  AProduction, "prod", AType, "def", $TF.listType(AType), "atypes");
	public static final Type AProduction_prod_attr = $TF.constructor($TS,  AProduction, "prod", AType, "def", $TF.listType(AType), "atypes", $TF.listType(Attr), "attributes");
	public static final Type AProduction_prod_src = $TF.constructor($TS,  AProduction, "prod", AType, "def", $TF.listType(AType), "atypes", $TF.sourceLocationType(), "src");
	public static final Type AProduction_prod_attr_src = $TF.constructor($TS,  AProduction, "prod", AType, "def", $TF.listType(AType), "atypes", $TF.listType(Attr), "attributes", $TF.sourceLocationType(), "src");
	
	public static IConstructor prod(IConstructor def, IList atypes) { return $VF.constructor(AProduction_prod, def, atypes); }
	public static IConstructor prod(IConstructor def, IList atypes, IConstructor attributes) { return $VF.constructor(AProduction_prod_attr, def, atypes, attributes); }
	public static IConstructor prod(IConstructor def, IList atypes, ISourceLocation src) { return $VF.constructor(AProduction_prod_src, def, atypes, src); }
	public static IConstructor prod(IConstructor def, IList atypes, IConstructor attributes, ISourceLocation src) { return $VF.constructor(AProduction_prod_attr_src, def, atypes, src); }
	
	// regular
	public static final Type AProduction_regular = $TF.constructor($TS,  AProduction, "regular", AType, "def");
	
	public static IConstructor regular(IConstructor def) { return $VF.constructor(AProduction_regular, def); }

	//error
	public static final Type AProduction_error = $TF.constructor($TS,  AProduction, "error", AProduction, "prod", $TF.integerType(), "dot");
	
	public static IConstructor error(IConstructor prod, IInteger dot) { return $VF.constructor(AProduction_error, prod, dot); }
	
	// skipped
	public static final Type AProduction_skipped = $TF.constructor($TS,  AProduction, "skipped");
	
	public static IConstructor skipped() { return $VF.constructor(AProduction_skipped); }
	
	// priority
	public static final Type AProduction_priority = $TF.constructor($TS,  AProduction, "priority", AType, "def", $TF.listType(AProduction), "choices");
	
	public static IConstructor priority(IConstructor def, IList choices) { return $VF.constructor(AProduction_priority, def, choices); }
	
	// associativity
	public static final Type AProduction_associativity = $TF.constructor($TS,  AProduction, "associativity", AType, "def", Associativity, "assoc", $TF.setType(AProduction), "alternatives");
	
	public static IConstructor associativity(IConstructor def, IConstructor assoc, IList alternatives) { return $VF.constructor(AProduction_priority, def, assoc, alternatives); }
	
	// others
	public static final Type AProduction_others = $TF.constructor($TS, AProduction, "others", AType, "def");
	
	public static IConstructor others(IConstructor def) { return $VF.constructor(AProduction_others, def); }
	
	// reference
	public static final Type AProduction_reference = $TF.constructor($TS, AProduction, "reference", AType, "def", str, "cons");
	
	public static IConstructor reference(IConstructor def, IString cons) { return $VF.constructor(AProduction_reference, def, cons); }

	// ---- ACharRange --------------------------------------------------------
	
	public static final Type CharRange_range = $TF.constructor($TS, ACharRange, "range", $TF.integerType(), "begin", $TF.integerType(), "end");
	
	public static IConstructor range(IInteger begin, IInteger end) { return $VF.constructor(CharRange_range, begin, end); }
	
	// ---- AType extensions for parse trees ----------------------------------
	
	// lit
	public static final Type AType_lit = $TF.constructor($TS, AType, "lit", str, "string");
	
	public static IConstructor lit(IString string) {  return $VF.constructor(AType_lit, string); }
	
	// cilit
	public static final Type AType_cilit = $TF.constructor($TS, AType, "cilit", str, "string");
	
	public static IConstructor cilit(IString string) {  return $VF.constructor(AType_cilit, string); }
	
	// char-class
	public static final Type AType_char_class = $TF.constructor($TS, AType, "char_class", $TF.listType(ACharRange), "ranges");
	
	public static IConstructor char_class(IList ranges) {  return $VF.constructor(AType_char_class, ranges); }
	
	// empty
	public static final Type AType_empty = $TF.constructor($TS, AType, "empty");
	
	public static IConstructor empty() {  return $VF.constructor(AType_empty); }
	
	// opt
	public static final Type AType_opt = $TF.constructor($TS, AType, "opt", AType, "atype");
	
	public static IConstructor opt(IConstructor atype) {  return $VF.constructor(AType_opt, atype); }
	
	// iter
	public static final Type AType_iter = $TF.constructor($TS, AType, "iter", AType, "atype");
	
	public static IConstructor iter(IConstructor atype) {  return $VF.constructor(AType_iter, atype); }
	
	// iter-star
	public static final Type AType_iter_star = $TF.constructor($TS, AType, "iter_star", AType, "atype");
	
	public static IConstructor iter_star(IConstructor atype) {  return $VF.constructor(AType_iter_star, atype); }
	
	// iter-seps
	public static final Type AType_iter_seps= $TF.constructor($TS, AType, "iter_seps", AType, "atype", $TF.listType(AType), "separators");
	
	public static IConstructor iter_seps(IConstructor atype, IList separators) {  return $VF.constructor(AType_iter_seps, atype, separators); }
	
	// iter-star-seps
	public static final Type AType_iter_star_seps= $TF.constructor($TS, AType, "iter_start_seps", AType, "atype", $TF.listType(AType), "separators");
	
	public static IConstructor iter_start_seps(IConstructor atype, IList separators) {  return $VF.constructor(AType_iter_star_seps, atype, separators); }
	
	// alt
	public static final Type AType_alt = $TF.constructor($TS, AType, "alt", AType, "atype", $TF.setType(AType), "alternatives");
	
	public static IConstructor alt(IConstructor atype, ISet alternatives) {  return $VF.constructor(AType_alt, atype, alternatives); }
	
	// seq
	public static final Type AType_seq = $TF.constructor($TS, AType, "seq", AType, "atype", $TF.listType(AType), "atypes");
	
	public static IConstructor seq(IConstructor atype, IList atypes) {  return $VF.constructor(AType_seq, atype, atypes); }
	
	// start
	public static final Type AType_start = $TF.constructor($TS, AType, "start", AType, "atype");
	
	public static IConstructor start(IConstructor atype) {  return $VF.constructor(AType_start, atype); }
	
	// ---- ACondition --------------------------------------------------------
	
	// follow
	public static final Type ACondition_follow = $TF.constructor($TS, ACondition, "follow", AType, "atype");
	
	public static IConstructor follow(IConstructor atype) {  return $VF.constructor(ACondition_follow, atype); }
	
	// not_follow
	public static final Type ACondition_not_follow = $TF.constructor($TS, ACondition, "not_follow", AType, "atype");
	
	public static IConstructor not_follow(IConstructor atype) {  return $VF.constructor(ACondition_not_follow, atype); }
	
	// precede
	public static final Type ACondition_precede = $TF.constructor($TS, ACondition, "precede", AType, "atype");
	
	public static IConstructor precede(IConstructor atype) {  return $VF.constructor(ACondition_precede, atype); }
	
	// not_precede
	public static final Type ACondition_not_precede = $TF.constructor($TS, ACondition, "not_precede", AType, "atype");
	
	public static IConstructor not_precede(IConstructor atype) {  return $VF.constructor(ACondition_not_precede, atype); }
	
	// delete
	public static final Type ACondition_delete = $TF.constructor($TS, ACondition, "delete", AType, "atype");
	
	public static IConstructor delete(IConstructor atype) {  return $VF.constructor(ACondition_delete, atype); }
	
	// at_column
	public static final Type ACondition_at_column = $TF.constructor($TS, ACondition, "at_column", $TF.integerType(), "column");
	
	public static IConstructor at_column(IInteger column) {  return $VF.constructor(ACondition_at_column, column); }
	
	// begin_of_line
	public static final Type ACondition_begin_of_line= $TF.constructor($TS, ACondition, "begin_of_line");
	
	public static IConstructor begin_of_line() {  return $VF.constructor(ACondition_begin_of_line); }
	
	// except
	public static final Type ACondition_except = $TF.constructor($TS, ACondition, "except", str, "label");
	
	public static IConstructor except(IString label) {  return $VF.constructor(ACondition_except, label); }
	
	
	/*************************************************************************/
	/*		Utilities for generated code									 */
	/*************************************************************************/
	
	// ---- ModuleStore -------------------------------------------------------
	
    protected static final class ModuleStore {
        
        private final ConcurrentMap<Class<?>, Object> loadedModules = new ConcurrentHashMap<>();

        public ModuleStore() {
			// TODO Auto-generated constructor stub
		}

		@SuppressWarnings("unchecked")
        public <T> T importModule(Class<T> module, Function<ModuleStore, T> builder) {
            T result = (T)loadedModules.get(module);
            if (result == null) {
                // we have to compute and then merge, computeIfAbstent can not be used, as we'll have to use the map during the compute.
                T newResult = builder.apply(this);
                // we merge, most cases we won't get a merge, but if we do, we keep the one in the store
                return (T)loadedModules.merge(module, newResult, (a, b) -> (a == newResult) ? b : a);
            }
            return result;
        }
    }
  
	// ---- utility methods ---------------------------------------------------

	public static final IMap buildMap(final IValue...values){
		IMapWriter w = $VF.mapWriter();
		if(values.length % 2 != 0) throw new InternalCompilerError("$RascalModule: buildMap should have even number of arguments");
		for(int i = 0; i < values.length; i += 2) {
			w.put(values[i], values[i+1]);
		}
		return w.done();
	}
	
	/*************************************************************************/
	/*		Rascal primitives called by generated code						 */
	/*************************************************************************/

	// ---- add ---------------------------------------------------------------

	public static final IValue add(final IValue lhs, final IValue rhs) {
		ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
		ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
		switch (lhsType) {
		case INT:
			switch (rhsType) {
			case INT:
				return ((IInteger) lhs).add((IInteger) rhs);
			case NUM:
				return ((IInteger) lhs).add((INumber) rhs);
			case REAL:
				return ((IInteger) lhs).add((IReal) rhs);
			case RAT:
				return ((IInteger) lhs).add((IRational) rhs);
			case LIST:
				return ((IList) rhs).insert(lhs);
			case SET:
				return ((ISet) rhs).insert(lhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case NUM:
			switch (rhsType) {
			case INT:
				return ((INumber) lhs).add((IInteger) rhs);
			case NUM:
				return ((INumber) lhs).add((INumber) rhs);
			case REAL:
				return ((INumber) lhs).add((IReal) rhs);
			case RAT:
				return ((INumber) lhs).add((IRational) rhs);
			case LIST:
				return ((IList) rhs).insert(lhs);
			case SET:
				return ((ISet) rhs).insert(lhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case REAL:
			switch (rhsType) {
			case INT:
				return ((IReal) lhs).add((IInteger) rhs);
			case NUM:
				return ((IReal) lhs).add((INumber) rhs);
			case REAL:
				return ((IReal) lhs).add((IReal) rhs);
			case RAT:
				return ((IReal) lhs).add((IRational) rhs);
			case LIST:
				return ((IList) rhs).insert(lhs);
			case SET:
				return ((ISet) rhs).insert(lhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case RAT:
			switch (rhsType) {
			case INT:
				return  ((IRational) lhs).add((IInteger) rhs);
			case NUM:
				return ((IRational) lhs).add((INumber) rhs);
			case REAL:
				return ((IRational) lhs).add((IReal) rhs);
			case RAT:
				return ((IRational) lhs).add((IRational) rhs);
			case LIST:
				return ((IList) rhs).insert(lhs);
			case SET:
				return ((ISet) rhs).insert(lhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case SET:
			return ((ISet) lhs).insert(rhs);

		case LIST:
			return ((IList) lhs).append(rhs);

		case LOC:
			switch (rhsType) {
			case STR:
				return aloc_add_astr((ISourceLocation) lhs, (IString) rhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case LREL:
			switch (rhsType) {
			case LIST:
			case LREL:
				return ((IList) lhs).concat((IList)rhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case MAP:
			switch (rhsType) {
			case MAP:
				return ((IMap) lhs).compose((IMap) rhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case REL:
			switch (rhsType) {
			case SET:
			case REL:
				return ((ISet) lhs).union((ISet) rhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case STR:
			switch (rhsType) {
			case STR:
				return ((IString) lhs).concat((IString) rhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case TUPLE:
			switch (rhsType) {
			case TUPLE:
				return atuple_add_atuple((ITuple) lhs, (ITuple) rhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		default:
			switch (rhsType) {
			case LIST:
				return ((IList) rhs).insert(lhs);
			case SET:
				return ((ISet) rhs).insert(lhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		}
	}
	
	public static final IInteger aint_add_aint(final IInteger lhs, final IInteger rhs) {
		return lhs.add(rhs);
	}
	
	public static final IReal aint_add_areal(final IInteger lhs, final IReal rhs) {
		return lhs.add(rhs);
	}
	
	public static final INumber aint_add_arat(final IInteger lhs, final IRational rhs) {
		return lhs.add(rhs);
	}
	
	public static final INumber aint_add_anum(final IInteger lhs, final INumber rhs) {
		return lhs.add(rhs);
	}
	
	public static final INumber areal_add_aint(final IReal lhs, final IInteger rhs) {
		return lhs.add(rhs);
	}
	
	public static final INumber areal_add_areal(final IReal lhs, final IReal rhs) {
		return lhs.add(rhs);
	}
	
	public static final INumber areal_add_areal(final IReal lhs, final IRational rhs) {
		return lhs.add(rhs);
	}
	
	public static final INumber areal_add_anum(final IReal lhs, final INumber rhs) {
		return lhs.add(rhs);
	}
	
	public static final INumber arat_add_aint(final IRational lhs, final IInteger rhs) {
		return lhs.add(rhs);
	}
	
	public static final INumber arat_add_areal(final IRational lhs, final IReal rhs) {
		return lhs.add(rhs);
	}
	
	public static final IRational arat_add_arat(final IRational lhs, final IRational rhs) {
		return lhs.add(rhs);
	}
	
	public static final INumber arat_add_anum(final IRational lhs, final INumber rhs) {
		return lhs.add(rhs);
	}
	
	public static final INumber anum_add_aint(final INumber lhs, final IInteger rhs) {
		return lhs.add(rhs);
	}
	
	public static final INumber anum_add_areal(final INumber lhs, final IReal rhs) {
		return lhs.add(rhs);
	}
	
	public static final INumber anum_add_arat(final INumber lhs, final IRational rhs) {
		return lhs.add(rhs);
	}
	
	public static final INumber anum_add_anum(final INumber lhs, final INumber rhs) {
		return lhs.add(rhs);
	}
	
	public static IString astr_add_astr(final IString lhs, final IString rhs) {
		return lhs.concat(rhs);
	}
	
	public static final ISourceLocation aloc_add_astr(final ISourceLocation sloc, final IString s) {
		String path = sloc.hasPath() ? sloc.getPath() : "";
		if(!path.endsWith("/")){
			path = path + "/";
		}
		path = path.concat(s.getValue());
		return aloc_field_update(sloc, "path", $VF.string(path));
	}
	
	public static final ITuple atuple_add_atuple(final ITuple t1, final ITuple t2) {
		int len1 = t1.arity();
		int len2 = t2.arity();
		IValue elems[] = new IValue[len1 + len2];
		for(int i = 0; i < len1; i++)
			elems[i] = t1.get(i);
		for(int i = 0; i < len2; i++)
			elems[len1 + i] = t2.get(i);
		return $VF.tuple(elems);
	}
	
	public static final IList alist_add_alist(final IList lhs, final IList rhs) {
		return lhs.concat(rhs);
	}
	
	public static final IList alist_add_elm(final IList lhs, final IValue rhs) {
		return lhs.append(rhs);
	}
	
	public static final IList elm_add_alist(final IValue lhs, final IList rhs) {
		return rhs.insert(lhs);
	}
	
	public static final ISet aset_add_aset(final ISet lhs, final ISet rhs) {
		return lhs.union(rhs);
	}
	
	public static final ISet aset_add_elm(final ISet lhs, final IValue rhs) {
		return lhs.insert(rhs);
	}
	
	public static final ISet elm_add_aset(final IValue lhs, final ISet rhs) {
		return rhs.insert(lhs);
	}
	
	public static final IMap amap_add_amap(final IMap lhs, final IMap rhs) {
		return lhs.compose(rhs);
	}

	// ---- annotation_get ----------------------------------------------------

	public static final IValue  annotation_get(final IValue val, final String label) {
		try {
			IValue result = val.asAnnotatable().getAnnotation(label);

			if(result == null) {
				throw RascalExceptionFactory.noSuchAnnotation(label);
			}
			return result;
		} catch (FactTypeUseException e) {
			throw  RascalExceptionFactory.noSuchAnnotation(label);
		}
	}

	public static final GuardedIValue guarded_annotation_get(final IValue val, final String label) {
		try {
			IValue result = val.asAnnotatable().getAnnotation(label);

			if(result == null) {
				return UNDEFINED;
			}
			return new GuardedIValue(result);
		} catch (FactTypeUseException e) {
			return UNDEFINED;
		}
	}

	// ---- assert_fails ------------------------------------------------------

	public static final void assert_fails(final IString message) {
		throw RascalExceptionFactory.assertionFailed(message);
	}

	// ---- create ------------------------------------------------------------

	public static final ISourceLocation create_aloc(final IString uri) {
		try {
			return URIUtil.createFromURI(uri.getValue());
		} 
		catch (URISyntaxException e) {
			// this is actually an unexpected run-time exception since Rascal prevents you from 
			// creating non-encoded 
			throw RascalExceptionFactory.malformedURI(uri.getValue());
		}
		catch (UnsupportedOperationException e) {
			throw RascalExceptionFactory.malformedURI(uri.getValue() + ":" + e.getMessage());
		}
	}


	/**
	 * Create a loc with given offsets and length
	 */
	public static final ISourceLocation create_aloc_with_offset(final ISourceLocation loc, final IInteger offset, final IInteger length, final ITuple begin, final ITuple end) {
		int beginLine = ((IInteger) begin.get(0)).intValue();
		int beginCol = ((IInteger) begin.get(1)).intValue();

		int endLine = ((IInteger) end.get(0)).intValue();
		int endCol = ((IInteger)  end.get(1)).intValue();
		return $VF.sourceLocation(loc, offset.intValue(), length.intValue(), beginLine, endLine, beginCol, endCol);
	}

	// ---- divide ------------------------------------------------------------

	public static final IValue divide(final IValue lhs, final IValue rhs) {
		ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
		ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
		switch (lhsType) {
		case INT:
			switch (rhsType) {
			case INT:
				return aint_divide_aint((IInteger) lhs, (IInteger) rhs);
			case NUM:
				return aint_divide_anum((IInteger) lhs, (INumber) rhs);
			case REAL:
				return aint_divide_areal((IInteger) lhs, (IReal) rhs);
			case RAT:
				return aint_divide_arat((IInteger) lhs, (IRational) rhs);
			default:
				throw new InternalCompilerError("$RascalModule divide: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case NUM:
			switch (rhsType) {
			case INT:
				return anum_divide_aint((INumber) lhs, (IInteger) rhs);
			case NUM:
				return anum_divide_anum((INumber) lhs, (INumber) rhs);
			case REAL:
				return anum_divide_areal((INumber) lhs, (IReal) rhs);
			case RAT:
				return anum_divide_arat((INumber) lhs,  (IRational) rhs);
			default:
				throw new InternalCompilerError("$RascalModule divide: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case REAL:
			switch (rhsType) {
			case INT:
				return areal_divide_aint((IReal) lhs, (IInteger) rhs);
			case NUM:
				return areal_divide_anum((IReal) lhs, (INumber) rhs);
			case REAL:
				return areal_divide_areal((IReal) lhs, (IReal) rhs);
			case RAT:
				return areal_divide_arat((IReal) lhs, (IRational) rhs);
			default:
				throw new InternalCompilerError("$RascalModule divide: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case RAT:
			switch (rhsType) {
			case INT:
				return arat_divide_aint((IRational) lhs, (IInteger) rhs);
			case NUM:
				return arat_divide_anum((IRational) lhs, (INumber) rhs);
			case REAL:
				return arat_divide_areal((IRational) lhs, (IReal) rhs);
			case RAT:
				return arat_divide_arat((IRational) lhs, (IRational) rhs);
			default:
				throw new InternalCompilerError("$RascalModule divide: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		default:
			throw new InternalCompilerError("$RascalModule divide: Illegal type combination: " + lhsType + " and " + rhsType);
		}
	}

	public static final IInteger aint_divide_aint(final IInteger a, final IInteger b) {
		try {
			return a.divide(b);
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static final INumber aint_divide_areal(final IInteger a, final IReal b) {
		try {
			return a.multiply($VF.real(1.0)).divide(b,  $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static final IRational aint_divide_arat(final IInteger a, final IRational b) {
		try {
			return a.toRational().divide(b);
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static final INumber aint_divide_anum(final IInteger a, final INumber b) {
		try {
			return a.multiply($VF.real(1.0)).divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static final IReal areal_divide_aint(final IReal a, final IInteger b) {
		try {
			return (IReal) a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static final IReal areal_divide_areal(final IReal a, final IReal b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static final IReal areal_divide_arat(IReal a, IRational b) {
		try {
			return (IReal) a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static final INumber areal_divide_anum(final IReal a, final INumber b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static final IRational arat_divide_aint(final IRational a, final IInteger b) {
		try {
			return a.divide(b);
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static final IReal arat_divide_areal(final IRational a, final IReal b) {
		try {
			return a.multiply($VF.real(1.0)).divide(b,  $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static final IRational arat_divide_arat(final IRational a, final IRational b) {
		try {
			return a.toRational().divide(b);
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static final INumber arat_divide_anum(final IRational a, final INumber b) {
		try {
			return a.multiply($VF.real(1.0)).divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static final INumber anum_divide_aint(final INumber a, final IInteger b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static final INumber anum_divide_areal(final INumber a, final IReal b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}
	public static final INumber anum_divide_arat(final INumber a, final IRational b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static final INumber anum_divide_anum(final INumber a, final INumber b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	// ---- equal -------------------------------------------------------------

	public static final IBool equal(final IValue left, final IValue right) {
		if(left.getType().isNumber() && right.getType().isNumber()){
			return ((INumber) left).equal((INumber) right);
		} else if(left.getType().isNode() && right.getType().isNode()){
			return ((INode) left).isEqual((INode) right) ? Rascal_TRUE : Rascal_FALSE;
		} else {
			return $VF.bool(left.isEqual(right));
		}
	}

	// ---- get_field ---------------------------------------------------------

	public static final IValue aloc_get_field(final ISourceLocation sloc, final String field) {
		IValue v;
		switch (field) {

		case "scheme":
			String s = sloc.getScheme();
			v = $VF.string(s == null ? "" : s);
			break;

		case "authority":
			v = $VF.string(sloc.hasAuthority() ? sloc.getAuthority() : "");
			break;

		case "host":
			if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
				throw RascalExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the host field, use authority instead.");
			}
			s = sloc.getURI().getHost();
			v = $VF.string(s == null ? "" : s);
			break;

		case "path":
			v = $VF.string(sloc.hasPath() ? sloc.getPath() : "/");
			break;

		case "parent":
			String path = sloc.getPath();
			if (path.equals("") || path.equals("/")) {
				throw RascalExceptionFactory.noParent(sloc);
			}
			int i = path.lastIndexOf("/");

			if (i != -1) {
				path = path.substring(0, i);
				if (sloc.getScheme().equalsIgnoreCase("file")) {
					// there is a special case for file references to windows paths.
					// the root path should end with a / (c:/ not c:)
					if (path.lastIndexOf((int)'/') == 0 && path.endsWith(":")) {
						path += "/";
					}
				}
				v = aloc_field_update(sloc, "path", $VF.string(path));
			} else {
				throw RascalExceptionFactory.noParent(sloc);
			}
			break;	

		case "file": 
			path = sloc.hasPath() ? sloc.getPath() : "";

			i = path.lastIndexOf((int)'/');

			if (i != -1) {
				path = path.substring(i+1);
			}
			v = $VF.string(path);	
			break;

		case "ls":
			ISourceLocation resolved = sloc;
			if(URIResolverRegistry.getInstance().exists(resolved) && URIResolverRegistry.getInstance().isDirectory(resolved)){
				IListWriter w = $VF.listWriter();

				try {
					for (ISourceLocation elem : URIResolverRegistry.getInstance().list(resolved)) {
						w.append(elem);
					}
				}
				catch (FactTypeUseException | IOException e) {
					throw RascalExceptionFactory.io($VF.string(e.getMessage()));
				}

				v = w.done();
				break;
			} else {
				throw RascalExceptionFactory.io($VF.string("You can only access ls on a directory, or a container."));
			}

		case "extension":
			path = sloc.hasPath() ? sloc.getPath() : "";
			i = path.lastIndexOf('.');
			if (i != -1) {
				v = $VF.string(path.substring(i + 1));
			} else {
				v = $VF.string("");
			}
			break;

		case "fragment":
			v = $VF.string(sloc.hasFragment() ? sloc.getFragment() : "");
			break;

		case "query":
			v = $VF.string(sloc.hasQuery() ? sloc.getQuery() : "");
			break;

		case "params":
			String query = sloc.hasQuery() ? sloc.getQuery() : "";
			IMapWriter res = $VF.mapWriter();

			if (query.length() > 0) {
				String[] params = query.split("&");
				for (String param : params) {
					String[] keyValue = param.split("=");
					res.put($VF.string(keyValue[0]), $VF.string(keyValue[1]));
				}
			}
			v = res.done();
			break;

		case "user":
			if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
				throw RascalExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the user field, use authority instead.");
			}
			s = sloc.getURI().getUserInfo();
			v = $VF.string(s == null ? "" : s);
			break;

		case "port":
			if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
				throw RascalExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the port field, use authority instead.");
			}
			int n = sloc.getURI().getPort();
			v = $VF.integer(n);
			break;	

		case "length":
			if(sloc.hasOffsetLength()){
				v = $VF.integer(sloc.getLength());
				break;
			} else {
				throw RascalExceptionFactory.unavailableInformation("length");
			}

		case "offset":
			if(sloc.hasOffsetLength()){
				v = $VF.integer(sloc.getOffset());
				break;
			} else {
				throw RascalExceptionFactory.unavailableInformation("offset");
			}

		case "begin":
			if(sloc.hasLineColumn()){
				v = $VF.tuple($VF.integer(sloc.getBeginLine()), $VF.integer(sloc.getBeginColumn()));
				break;
			} else {
				throw RascalExceptionFactory.unavailableInformation("begin");
			}
		case "end":
			if(sloc.hasLineColumn()){
				v = $VF.tuple($VF.integer(sloc.getEndLine()), $VF.integer(sloc.getEndColumn()));
				break;
			} else {
				throw RascalExceptionFactory.unavailableInformation("end");
			}

		case "uri":
			v = $VF.string(sloc.getURI().toString());
			break;

		case "top":
			v = sloc.top();
			break;

		default:
			throw RascalExceptionFactory.noSuchField(field);
		}

		return v;
	}

	public static final GuardedIValue guarded_aloc_get_field(final ISourceLocation sloc, final String field) {
		try {
			IValue result = aloc_get_field(sloc, field);
			return new GuardedIValue(result);
		} catch (RascalException e) {
			return UNDEFINED;
		}
	}

	public static final IValue adatetime_get_field(final IDateTime dt, final String field) {
		IValue v;
		try {
			switch (field) {
			case "isDate":
				v = $VF.bool(dt.isDate());
				break;
			case "isTime":
				v = $VF.bool(dt.isTime());
				break;
			case "isDateTime":
				v = $VF.bool(dt.isDateTime());
				break;
			case "century":
				if (!dt.isTime()) {
					v = $VF.integer(dt.getCentury());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the century on a time value");
			case "year":
				if (!dt.isTime()) {
					v = $VF.integer(dt.getYear());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the year on a time value");

			case "month":
				if (!dt.isTime()) {
					v = $VF.integer(dt.getMonthOfYear());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the month on a time value");
			case "day":
				if (!dt.isTime()) {
					v = $VF.integer(dt.getDayOfMonth());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the day on a time value");
			case "hour":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getHourOfDay());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the hour on a date value");
			case "minute":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getMinuteOfHour());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the minute on a date value");
			case "second":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getSecondOfMinute());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the second on a date value");
			case "millisecond":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getMillisecondsOfSecond());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the millisecond on a date value");
			case "timezoneOffsetHours":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getTimezoneOffsetHours());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the timezone offset hours on a date value");
			case "timezoneOffsetMinutes":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getTimezoneOffsetMinutes());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the timezone offset minutes on a date value");

			case "justDate":
				if (!dt.isTime()) {
					v = $VF.date(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the date component of a time value");
			case "justTime":
				if (!dt.isDate()) {
					v = $VF.time(dt.getHourOfDay(), dt.getMinuteOfHour(), dt.getSecondOfMinute(), 
							dt.getMillisecondsOfSecond(), dt.getTimezoneOffsetHours(),
							dt.getTimezoneOffsetMinutes());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the time component of a date value");
			default:
				throw RascalExceptionFactory.noSuchField(field);
			}
			return v;

		} catch (InvalidDateTimeException e) {
			throw RascalExceptionFactory.invalidArgument(dt);
		}
	}
	
	public static final GuardedIValue guarded_datetime_get_field(final IDateTime dt, final String field) {
		try {
			IValue result = adatetime_get_field(dt, field);
			return new GuardedIValue(result);
		} catch (RascalException e) {
			return UNDEFINED;
		}
	}
	
	public static final IValue areified_get_field(final IConstructor rt, final String field) {
		return rt.get(field);
	}

	// ---- field_project -----------------------------------------------------

	@SuppressWarnings("deprecation")
	public static final IValue atuple_field_project(final ITuple tup, final IValue... fields) {
		int n = fields.length;
		IValue [] newFields = new IValue[n];
		for(int i = 0; i < n; i++){
			IValue field = fields[i];
			newFields[i] = field.getType().isInteger() ? tup.get(((IInteger) field).intValue())
					: tup.get(((IString) field).getValue());
		}
		return (n - 1 > 1) ? $VF.tuple(newFields) : newFields[0];
	}
	
	public static final GuardedIValue guarded_atuple_field_project(final ITuple tup, final IValue... fields) {
		try {
			return new GuardedIValue(atuple_field_project(tup, fields));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	public static final ISet amap_field_project (final IMap map, final IValue... fields) {
		ISetWriter w = $VF.setWriter();
		int indexArity = fields.length;
		int intFields[] = new int[indexArity];
		for(int i = 1; i < indexArity; i++){
			intFields[i]  = ((IInteger) fields[i]).intValue();
		}
		IValue[] elems = new IValue[indexArity];
		Iterator<Entry<IValue,IValue>> iter = map.entryIterator();
		while (iter.hasNext()) {
			Entry<IValue,IValue> entry = iter.next();
			for(int j = 0; j < fields.length; j++){
				elems[j] = intFields[j] == 0 ? entry.getKey() : entry.getValue();
			}
			w.insert((indexArity > 1) ? $VF.tuple(elems) : elems[0]);
		}
		return w.done();
	}
	
	public static final GuardedIValue guarded_amap_field_project(final IMap map, final IValue... fields) {
		try {
			return new GuardedIValue(amap_field_project(map, fields));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	public static final ISet arel_field_project(final ISet set, final IValue... fields) {
		int indexArity = fields.length;
		int intFields[] = new int[indexArity];
		for(int i = 1; i < indexArity; i++){
			intFields[i]  = ((IInteger) fields[i]).intValue();
		}

		return set.asRelation().project(intFields);
	}
	
	public static final GuardedIValue guarded_arel_field_project(final ISet set, final IValue... fields) {
		try {
			return new GuardedIValue(arel_field_project(set, fields));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	public static final IList alrel_field_project(final IList lrel, final IValue... fields) {
		int indexArity = fields.length;
		int intFields[] = new int[indexArity];
		for(int i = 1; i < indexArity; i++){
			intFields[i]  = ((IInteger) fields[i]).intValue();
		}
		IListWriter w = $VF.listWriter();
		IValue[] elems = new IValue[indexArity];
		for(IValue vtup : lrel){
			ITuple tup = (ITuple) vtup;
			for(int j = 0; j < fields.length; j++){
				elems[j] = tup.get(intFields[j]);
			}
			w.append((indexArity > 1) ? $VF.tuple(elems) : elems[0]);
		}
		return w.done();
	}
	
	public static final GuardedIValue guarded_alrel_field_project(final IList lrel, final IValue... fields) {
		try {
			return new GuardedIValue(alrel_field_project(lrel, fields));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	// ---- field_update ------------------------------------------------------

	private static ISourceLocation aloc_field_update(final ISourceLocation sloc, final String field, final IValue repl) {		
		Type replType = repl.getType();

		int iLength = sloc.hasOffsetLength() ? sloc.getLength() : -1;
		int iOffset = sloc.hasOffsetLength() ? sloc.getOffset() : -1;
		int iBeginLine = sloc.hasLineColumn() ? sloc.getBeginLine() : -1;
		int iBeginColumn = sloc.hasLineColumn() ? sloc.getBeginColumn() : -1;
		int iEndLine = sloc.hasLineColumn() ? sloc.getEndLine() : -1;
		int iEndColumn = sloc.hasLineColumn() ? sloc.getEndColumn() : -1;
		URI uri;
		boolean uriPartChanged = false;
		String scheme = sloc.getScheme();
		String authority = sloc.hasAuthority() ? sloc.getAuthority() : "";
		String path = sloc.hasPath() ? sloc.getPath() : null;
		String query = sloc.hasQuery() ? sloc.getQuery() : null;
		String fragment = sloc.hasFragment() ? sloc.getFragment() : null;

		try {
			String newStringValue = null;
			if(replType.isString()){
				newStringValue = ((IString)repl).getValue();
			}

			switch (field) {

			case "uri":
				uri = URIUtil.createFromEncoded(newStringValue);
				// now destruct it again
				scheme = uri.getScheme();
				authority = uri.getAuthority();
				path = uri.getPath();
				query = uri.getQuery();
				fragment = uri.getFragment();
				uriPartChanged = true;
				break;

			case "scheme":
				scheme = newStringValue;
				uriPartChanged = true;
				break;

			case "authority":
				authority = newStringValue;
				uriPartChanged = true;
				break;

			case "host":
				if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
					throw RascalExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the host field, use authority instead.");
				}
				uri = URIUtil.changeHost(sloc.getURI(), newStringValue);
				authority = uri.getAuthority();
				uriPartChanged = true;
				break;

			case "path":
				path = newStringValue;
				uriPartChanged = true;
				break;

			case "file": 
				int i = path.lastIndexOf("/");

				if (i != -1) {
					path = path.substring(0, i) + "/" + newStringValue;
				}
				else {
					path = path + "/" + newStringValue;	
				}	
				uriPartChanged = true;
				break;

			case "parent":
				i = path.lastIndexOf("/");
				String parent = newStringValue;
				if (i != -1) {
					path = parent + path.substring(i);
				}
				else {
					path = parent;	
				}
				uriPartChanged = true;
				break;	

			case "ls":
				throw RascalExceptionFactory.noSuchField("Cannot update the children of a location");

			case "extension":
				String ext = newStringValue;

				if (path.length() > 1) {
					int index = path.lastIndexOf('.');

					if (index == -1 && !ext.isEmpty()) {
						path = path + (!ext.startsWith(".") ? "." : "") + ext;
					}
					else if (!ext.isEmpty()) {
						path = path.substring(0, index) + (!ext.startsWith(".") ? "." : "") + ext;
					}
					else {
						path = path.substring(0, index);
					}
				}
				uriPartChanged = true;
				break;

			case "top":
				if (replType.isString()) {
					uri = URIUtil.assumeCorrect(newStringValue);
					scheme = uri.getScheme();
					authority = uri.getAuthority();
					path = uri.getPath();
					query = uri.getQuery();
					fragment = uri.getFragment();
				}
				else if (replType.isSourceLocation()) {
					ISourceLocation rep = (ISourceLocation) repl;
					scheme = rep.getScheme();
					authority = rep.hasAuthority() ? rep.getAuthority() : null;
					path = rep.hasPath() ? rep.getPath() : null;
					query = rep.hasQuery() ? rep.getQuery() : null;
					fragment = rep.hasFragment() ? rep.getFragment() : null;
				}
				uriPartChanged = true;
				break;

			case "fragment":
				fragment = newStringValue;
				uriPartChanged = true;
				break;

			case "query":
				query = newStringValue;
				uriPartChanged = true;
				break;

			case "user":
				if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
					throw RascalExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the user field, use authority instead.");
				}
				uri = sloc.getURI();
				if (uri.getHost() != null) {
					uri = URIUtil.changeUserInformation(uri, newStringValue);
				}

				authority = uri.getAuthority();
				uriPartChanged = true;
				break;

			case "port":
				if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
					throw RascalExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the port field, use authority instead.");
				}
				if (sloc.getURI().getHost() != null) {
					int port = Integer.parseInt(((IInteger) repl).getStringRepresentation());
					uri = URIUtil.changePort(sloc.getURI(), port);
				}
				authority = sloc.getURI().getAuthority();
				uriPartChanged = true;
				break;	

			case "length":
				iLength = ((IInteger) repl).intValue();
				if (iLength < 0) {
					throw RascalExceptionFactory.invalidArgument(repl);
				}
				break;

			case "offset":
				iOffset = ((IInteger) repl).intValue();
				if (iOffset < 0) {
					throw RascalExceptionFactory.invalidArgument(repl);
				}
				break;

			case "begin":
				iBeginLine = ((IInteger) ((ITuple) repl).get(0)).intValue();
				iBeginColumn = ((IInteger) ((ITuple) repl).get(1)).intValue();

				if (iBeginColumn < 0 || iBeginLine < 0) {
					throw RascalExceptionFactory.invalidArgument(repl);
				}
				break;
			case "end":
				iEndLine = ((IInteger) ((ITuple) repl).get(0)).intValue();
				iEndColumn = ((IInteger) ((ITuple) repl).get(1)).intValue();

				if (iEndColumn < 0 || iEndLine < 0) {
					throw RascalExceptionFactory.invalidArgument(repl);
				}
				break;			

			default:
				throw RascalExceptionFactory.noSuchField("Modification of field " + field + " in location not allowed");
			}

			ISourceLocation newLoc = sloc;
			if (uriPartChanged) {
				newLoc = $VF.sourceLocation(scheme, authority, path, query, fragment);
			}

			if (sloc.hasLineColumn()) {
				// was a complete loc, and thus will be now
				return $VF.sourceLocation(newLoc, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
			}

			if (sloc.hasOffsetLength()) {
				// was a partial loc

				if (iBeginLine != -1 || iBeginColumn != -1) {
					//will be complete now.
					iEndLine = iBeginLine;
					iEndColumn = iBeginColumn;
					return $VF.sourceLocation(newLoc, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
				}
				else if (iEndLine != -1 || iEndColumn != -1) {
					// will be complete now.
					iBeginLine = iEndLine;
					iBeginColumn = iEndColumn;
					return $VF.sourceLocation(newLoc, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
				}
				else {
					// remains a partial loc
					return $VF.sourceLocation(newLoc, iOffset, iLength);
				}
			}

			// used to have no offset/length or line/column info, if we are here

			if (iBeginColumn != -1 || iEndColumn != -1 || iBeginLine != -1 || iBeginColumn != -1) {
				// trying to add line/column info to a uri that has no offset length
				throw RascalExceptionFactory.invalidUseOfLocation("Can not add line/column information without offset/length");
			}

			// trying to set offset that was not there before, adding length automatically
			if (iOffset != -1 ) {
				if (iLength == -1) {
					iLength = 0;
				}
			}

			// trying to set length that was not there before, adding offset automatically
			if (iLength != -1) {
				if (iOffset == -1) {
					iOffset = 0;
				}
			}

			if (iOffset != -1 || iLength != -1) {
				// used not to no offset/length, but do now
				return $VF.sourceLocation(newLoc, iOffset, iLength);
			}

			// no updates to offset/length or line/column, and did not used to have any either:
			return newLoc;

		} catch (IllegalArgumentException e) {
			throw RascalExceptionFactory.invalidArgument();
		} catch (URISyntaxException e) {
			throw RascalExceptionFactory.malformedURI(e.getMessage());
		}
	}

	public static final IDateTime adatetime_field_update(final IDateTime dt, final String field, final IValue repl) {
		// Individual fields
		int year = dt.getYear();
		int month = dt.getMonthOfYear();
		int day = dt.getDayOfMonth();
		int hour = dt.getHourOfDay();
		int minute = dt.getMinuteOfHour();
		int second = dt.getSecondOfMinute();
		int milli = dt.getMillisecondsOfSecond();
		int tzOffsetHour = dt.getTimezoneOffsetHours();
		int tzOffsetMin = dt.getTimezoneOffsetMinutes();

		try {
			switch (field) {

			case "year":
				if (dt.isTime()) {
					throw RascalExceptionFactory.invalidUseOfTime("Can not update the year on a time value");
				}
				year = ((IInteger)repl).intValue();
				break;

			case "month":
				if (dt.isTime()) {
					throw RascalExceptionFactory.invalidUseOfTime("Can not update the month on a time value");
				}
				month = ((IInteger)repl).intValue();
				break;

			case "day":
				if (dt.isTime()) {
					throw RascalExceptionFactory.invalidUseOfTime("Can not update the day on a time value");
				}	
				day = ((IInteger)repl).intValue();
				break;

			case "hour":
				if (dt.isDate()) {
					throw RascalExceptionFactory.invalidUseOfDate("Can not update the hour on a date value");
				}	
				hour = ((IInteger)repl).intValue();
				break;

			case "minute":
				if (dt.isDate()) {
					throw RascalExceptionFactory.invalidUseOfDate("Can not update the minute on a date value");
				}
				minute = ((IInteger)repl).intValue();
				break;

			case "second":
				if (dt.isDate()) {
					throw RascalExceptionFactory.invalidUseOfDate("Can not update the second on a date value");
				}
				second = ((IInteger)repl).intValue();
				break;

			case "millisecond":
				if (dt.isDate()) {
					throw RascalExceptionFactory.invalidUseOfDate("Can not update the millisecond on a date value");
				}
				milli = ((IInteger)repl).intValue();
				break;

			case "timezoneOffsetHours":
				if (dt.isDate()) {
					throw RascalExceptionFactory.invalidUseOfDate("Can not update the timezone offset hours on a date value");
				}
				tzOffsetHour = ((IInteger)repl).intValue();
				break;

			case "timezoneOffsetMinutes":
				if (dt.isDate()) {
					throw RascalExceptionFactory.invalidUseOfDate("Can not update the timezone offset minutes on a date value");
				}
				tzOffsetMin = ((IInteger)repl).intValue();
				break;			

			default:
				throw RascalExceptionFactory.noSuchField(field);
			}
			IDateTime newdt = null;
			if (dt.isDate()) {
				newdt = $VF.date(year, month, day);
			} else if (dt.isTime()) {
				newdt = $VF.time(hour, minute, second, milli, tzOffsetHour, tzOffsetMin);
			} else {
				newdt = $VF.datetime(year, month, day, hour, minute, second, milli, tzOffsetHour, tzOffsetMin);
			}
			return newdt;
		}
		catch (IllegalArgumentException e) {
			throw RascalExceptionFactory.invalidArgument(repl, "Cannot update field " + field + ", this would generate an invalid datetime value");
		}
		catch (InvalidDateTimeException e) {
			throw RascalExceptionFactory.invalidArgument(dt, e.getMessage());
		}
	}
	
	// ---- has ---------------------------------------------------------------
	
	/**
	 * Runtime check whether a node has a named field
	 * 
	 */
	public static final boolean anode_has_field(final INode nd, final String fieldName) {
		if ((nd.mayHaveKeywordParameters() && nd.asWithKeywordParameters().getParameter(fieldName) != null)){
			return true;
		} else {
			if(nd.isAnnotatable()){
				return nd.asAnnotatable().getAnnotation(fieldName) != null;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * Runtime check whether given constructor has a named field (positional or keyword).
	*/

	public static final boolean aadt_has_field(final IConstructor cons, final String fieldName) {

		Type consType = cons.getConstructorType();

		// Does fieldName exist as positional field?
		if(consType.hasField(fieldName)){
			return true;
		}

		// Check for keyword parameter
		String[] fieldNames = consType.getFieldNames();
		if(fieldNames == null){
			fieldNames = new String[0];
		}

		IListWriter w = $VF.listWriter();
		w.append($VF.string(cons.getName()));
		for(String fname : fieldNames){
			w.append($VF.string(fname));
		}
		
		if($TS.hasKeywordParameter(consType, fieldName)) {
			return true;
		}

		if(TreeAdapter.isTree(cons) && TreeAdapter.isAppl((ITree) cons)) {
			IConstructor prod = ((ITree) cons).getProduction();

			for(IValue elem : ProductionAdapter.getSymbols(prod)) {
				IConstructor arg = (IConstructor) elem;
				if (SymbolAdapter.isLabel(arg) && SymbolAdapter.getLabel(arg).equals(fieldName)) {
					return true;			        }
			}
		}
		if(cons.isAnnotatable()){
			return cons.asAnnotatable().getAnnotation(fieldName) != null;
		} else {
			return false;
		}
	}
	
	// TODO nonterminal

	// ---- intersect ---------------------------------------------------------

	public static final IValue intersect(final IValue left, final IValue right) {
		Type leftType = left.getType();
		Type rightType = right.getType();

		switch (ToplevelType.getToplevelType(leftType)) {
		case LIST:
			switch (ToplevelType.getToplevelType(rightType)) {
			case LIST:
			case LREL:
				return ((IList) left).intersect((IList) right);
			default:
				throw new InternalCompilerError("intersect: illegal combination " + leftType + " and " + rightType);
			}
		case SET:
			switch (ToplevelType.getToplevelType(rightType)) {
			case SET:
			case REL:
				return ((ISet) left).intersect((ISet) right);
			default:
				throw new InternalCompilerError("intersect: illegal combination " + leftType + " and " + rightType);
			}
		case MAP:
			return ((IMap) left).common((IMap) right);

		default:
			throw new InternalCompilerError("intersect: illegal combination " + leftType + " and " + rightType);
		}
	}
	
	// ---- is -----------------------------------------------------------------
	
	public static final boolean is(final IValue val, final IString sname) {
		Type tp = val.getType();
		String name = sname.getValue();
		if(tp.isAbstractData()){
			if(tp.getName().equals("Tree")){
				IConstructor cons = (IConstructor) val;
				if(cons.getName().equals("appl")){
					IConstructor prod = (IConstructor) cons.get(0);
					IConstructor def = (IConstructor) prod.get(0);
					if(def.getName().equals("label")){
						return ((IString) def.get(0)).getValue().equals(name);
					}
				}
			} else {
				String consName = ((IConstructor)val).getConstructorType().getName();
				if(consName.startsWith("\\")){
					consName = consName.substring(1);
				}
				return consName.equals(name);
			}
		} else if(tp.isNode()){
			String nodeName = ((INode) val).getName();
			if(nodeName.startsWith("\\")){
				nodeName = nodeName.substring(1);
			}
			return nodeName.equals(name);
		} 
		return false;
	}

	// ---- is_defined_value and get_defined_value -----------------------------

	private static final GuardedIValue UNDEFINED = new GuardedIValue();

	public static final boolean is_defined_value(final GuardedIValue val) {
		return val.defined;
	}

	public static final IValue get_defined_value(final GuardedIValue val) {
		return val.value;
	}

	// ---- join --------------------------------------------------------------

	public static final IList alist_join_alrel(final IList left, final IList right){
		if(left.length() == 0){
			return left;
		}
		if(right.length() == 0){
			return right;
		}
		Type rightType = right.get(0).getType();
		assert rightType.isTuple();

		int rarity = rightType.getArity();
		IValue fieldValues[] = new IValue[1 + rarity];
		IListWriter w = $VF.listWriter();

		for (IValue lval : left){
			fieldValues[0] = lval;
			for (IValue rtuple: right) {
				for (int i = 0; i < rarity; i++) {
					fieldValues[i + 1] = ((ITuple)rtuple).get(i);
				}
				w.append($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	public static final IList alrel_join_alrel(final IList left, final IList right){
		if(left.length() == 0){
			return left;
		}
		if(right.length() == 0){
			return right;
		}
		Type leftType = left.get(0).getType();
		Type rightType = right.get(0).getType();
		assert leftType.isTuple();
		assert rightType.isTuple();

		int larity = leftType.getArity();
		int rarity = rightType.getArity();
		IValue fieldValues[] = new IValue[larity + rarity];
		IListWriter w = $VF.listWriter();

		for (IValue ltuple : left){
			for (IValue rtuple: right) {
				for (int i = 0; i < larity; i++) {
					fieldValues[i] = ((ITuple)ltuple).get(i);
				}
				for (int i = larity; i < larity + rarity; i++) {
					fieldValues[i] = ((ITuple)rtuple).get(i - larity);
				}
				w.append($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	public static final IList alrel_join_alist(final IList left, final IList right){
		if(left.length() == 0){
			return left;
		}
		if(right.length() == 0){
			return right;
		}
		Type leftType = left.get(0).getType();
		assert leftType.isTuple();

		int larity = leftType.getArity();
		IValue fieldValues[] = new IValue[larity + 1];
		IListWriter w = $VF.listWriter();

		for (IValue ltuple : left){
			for (IValue rval: right) {
				for (int i = 0; i < larity; i++) {
					fieldValues[i] = ((ITuple)ltuple).get(i);
				}
				fieldValues[larity] = rval;
				w.append($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	public static final ISet aset_join_arel(final ISet left, final ISet right){
		if(left.size() == 0){
			return left;
		}
		if(right.size() == 0){
			return right;
		}
		Type rightType = right.getElementType();
		assert rightType.isTuple();

		int rarity = rightType.getArity();
		IValue fieldValues[] = new IValue[1 + rarity];
		ISetWriter w = $VF.setWriter();

		for (IValue lval : left){
			for (IValue rtuple: right) {
				fieldValues[0] = lval;
				for (int i = 0; i <  rarity; i++) {
					fieldValues[i + 1] = ((ITuple)rtuple).get(i);
				}
				w.insert($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	public static final ISet arel_join_arel(final ISet left, final ISet right){
		if(left.size() == 0){
			return left;
		}
		if(right.size() == 0){
			return right;
		}
		Type leftType = left.getElementType();
		Type rightType = right.getElementType();
		assert leftType.isTuple();
		assert rightType.isTuple();

		int larity = leftType.getArity();
		int rarity = rightType.getArity();
		IValue fieldValues[] = new IValue[larity + rarity];
		ISetWriter w = $VF.setWriter();

		for (IValue ltuple : left){
			for (IValue rtuple: right) {
				for (int i = 0; i < larity; i++) {
					fieldValues[i] = ((ITuple)ltuple).get(i);
				}
				for (int i = larity; i < larity + rarity; i++) {
					fieldValues[i] = ((ITuple)rtuple).get(i - larity);
				}
				w.insert($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	public static final ISet arel_join_aset(final ISet left, final ISet right){

		if(left.size() == 0){
			return left;
		}
		if(right.size() == 0){
			return right;
		}
		Type leftType = left.getElementType();
		assert leftType.isTuple();

		int larity = leftType.getArity();
		IValue fieldValues[] = new IValue[larity + 1];
		ISetWriter w = $VF.setWriter();

		for (IValue ltuple : left){
			for (IValue rval: right) {
				for (int i = 0; i < larity; i++) {
					fieldValues[i] = ((ITuple)ltuple).get(i);
				}
				fieldValues[larity] = rval;
				w.insert($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	// ---- less --------------------------------------------------------------

	public static final IBool less(final IValue left, final IValue right){

		Type leftType = left.getType();
		Type rightType = right.getType();

		if (leftType.isSubtypeOf($TF.numberType()) && rightType.isSubtypeOf($TF.numberType())) {
			return ((INumber)left).less((INumber)right);
		}

		if(!leftType.comparable(rightType)){
			return Rascal_FALSE;
		}

		switch (ToplevelType.getToplevelType(leftType)) {
		// TODO: is this really faster than a TypeVisitor?? No because getTopLevelType includes a TypeVisitor itself.
		// TODO: check type of right
		case BOOL:
			return abool_less_abool((IBool)left, (IBool)right);
		case STR:
			return astr_less_astr((IString)left, (IString)right);
		case DATETIME:
			return adatetime_less_adatetime((IDateTime)left, (IDateTime)right);
		case LOC:
			return aloc_less_aloc((ISourceLocation)left, (ISourceLocation)right);
		case LIST:
		case LREL:
			return alist_less_alist((IList)left, (IList)right);
		case SET:
		case REL:
			return aset_less_aset((ISet)left, (ISet)right);
		case MAP:
			return amap_less_amap((IMap)left, (IMap)right);
		case CONSTRUCTOR:
		case NODE:
			return anode_less_anode((INode)left, (INode)right);
		case ADT:
			//return aadt_less_aadt((IAbstractDataType)left, right);
		case TUPLE:
			return atuple_less_atuple((ITuple)left, (ITuple)right);
		default:
			throw new InternalCompilerError("less: unexpected type " + leftType);
		}
	}
	
	
	
	
	public static final IBool aint_less_aint(final IInteger a, final IInteger b) {
		return a.less(b);
	}

	public static final IBool aint_less_areal(final IInteger a, final IReal b) {
		return a.less(b);
	}

	public static final IBool aint_less_arat(final IInteger a, final IRational b) {
		return a.toRational().less(b);
	}

	public static final IBool aint_less_anum(final IInteger a, final INumber b) {
		return a.less(b);
	}

	public static final IBool areal_less_aint(final IReal a, final IInteger b) {
		return a.less(b);
	}

	public static final IBool areal_less_areal(final IReal a, final IReal b) {
		return a.less(b);
	}

	public static final IBool areal_less_arat(IReal a, IRational b) {
		return a.less(b);
	}

	public static final IBool areal_less_anum(final IReal a, final INumber b) {
		return a.less(b);
	}

	public static final IBool arat_less_aint(final IRational a, final IInteger b) {
		return a.less(b);
	}

	public static final IBool arat_less_areal(final IRational a, final IReal b) {
		return a.less(b);
	}

	public static final IBool arat_less_arat(final IRational a, final IRational b) {
		return a.toRational().less(b);
	}

	public static final IBool arat_less_anum(final IRational a, final INumber b) {
		return a.less(b);
	}

	public static final IBool anum_less_aint(final INumber a, final IInteger b) {
		return a.less(b);
	}

	public static final IBool anum_less_areal(final INumber a, final IReal b) {
		return a.less(b);
	}
	public static final IBool anum_less_arat(final INumber a, final IRational b) {
		return a.less(b);
	}

	public static final IBool anum_less_anum(final INumber a, final INumber b) {
		return a.less(b);
	}
	
	
	

	public static final IBool abool_less_abool(final IBool left, final IBool right) {
		return  $VF.bool(!left.getValue() && right.getValue());
	}

	public static final IBool astr_less_astr(final IString left, final IString right) {
		return $VF.bool(left.compare(right) == -1);
	}

	public static final IBool adatetime_less_adatetime(final IDateTime left, final IDateTime right) {
		return $VF.bool(left.compareTo(right) == -1);
	}

	public static final IBool aloc_less_aloc(final ISourceLocation left, final ISourceLocation right) {
		int compare = SourceLocationURICompare.compare(left, right);
		if (compare < 0) {
			return Rascal_TRUE;
		}
		else if (compare > 0) {
			return Rascal_FALSE;
		}

		// but the uri's are the same
		// note that line/column information is superfluous and does not matter for ordering

		if (left.hasOffsetLength()) {
			if (!right.hasOffsetLength()) {
				return Rascal_FALSE;
			}

			int roffset = right.getOffset();
			int rlen = right.getLength();
			int loffset = left.getOffset();
			int llen = left.getLength();

			if (loffset == roffset) {
				return $VF.bool(llen < rlen);
			}
			return $VF.bool(roffset < loffset && roffset + rlen >= loffset + llen);
		}
		else if (compare == 0) {
			return Rascal_FALSE;
		}

		if (!right.hasOffsetLength()) {
			throw new InternalCompilerError("offset length missing");
		}
		return Rascal_FALSE;
	}

	public static final IBool atuple_less_atuple(final ITuple left, final ITuple right) {
		int leftArity = left.arity();
		int rightArity = right.arity();

		for (int i = 0; i < Math.min(leftArity, rightArity); i++) {
			Object result;
			if(leftArity < rightArity || i < leftArity - 1)
				result = equal(left.get(i), right.get(i));
			else
				result = less(left.get(i), right.get(i));

			if(!((IBool)result).getValue()){
				return Rascal_FALSE;
			}
		}

		return $VF.bool(leftArity <= rightArity);
	}

	public static final IBool anode_less_anode(final INode left, final INode right) {
		int compare = left.getName().compareTo(right.getName());

		if (compare <= -1) {
			return Rascal_TRUE;
		}

		if (compare >= 1){
			return Rascal_FALSE;
		}

		// if the names are not ordered, then we order lexicographically on the arguments:

		int leftArity = left.arity();
		int rightArity = right.arity();

		Object result =  Rascal_FALSE;
		for (int i = 0; i < Math.min(leftArity, rightArity); i++) {

			if(leftArity < rightArity || i < leftArity - 1)
				result = lessequal(left.get(i), right.get(i));
			else
				result = less(left.get(i), right.get(i));

			if(!((IBool)result).getValue()){
				return Rascal_FALSE;
			}
		}

		if (!left.mayHaveKeywordParameters() && !right.mayHaveKeywordParameters()) {
			if (left.asAnnotatable().hasAnnotations() || right.asAnnotatable().hasAnnotations()) {
				// bail out 
				return Rascal_FALSE;
			}
		}

		if (!left.asWithKeywordParameters().hasParameters() && right.asWithKeywordParameters().hasParameters()) {
			return Rascal_TRUE;
		}

		if (left.asWithKeywordParameters().hasParameters() && !right.asWithKeywordParameters().hasParameters()) {
			return Rascal_FALSE;
		}

		if (left.asWithKeywordParameters().hasParameters() && right.asWithKeywordParameters().hasParameters()) {
			Map<String, IValue> paramsLeft = left.asWithKeywordParameters().getParameters();
			Map<String, IValue> paramsRight = right.asWithKeywordParameters().getParameters();
			if (paramsLeft.size() < paramsRight.size()) {
				return Rascal_TRUE;
			}
			if (paramsLeft.size() > paramsRight.size()) {
				return Rascal_FALSE;
			}
			if (paramsRight.keySet().containsAll(paramsLeft.keySet()) && !paramsRight.keySet().equals(paramsLeft.keySet())) {
				return Rascal_TRUE;
			}
			if (paramsLeft.keySet().containsAll(paramsLeft.keySet()) && !paramsRight.keySet().equals(paramsLeft.keySet())) {
				return Rascal_FALSE;
			}
			//assert paramsLeft.keySet().equals(paramsRight.keySet());
			for (String k: paramsLeft.keySet()) {
				result = less(paramsLeft.get(k), paramsRight.get(k));

				if(!((IBool)result).getValue()){
					return Rascal_FALSE;
				}
			}
		}

		return $VF.bool((leftArity < rightArity) || ((IBool)result).getValue());
	}

	public static final IBool alist_less_alist(final IList left, final IList right) {
		if(left.length() > right.length()){
			return Rascal_FALSE;
		}
		OUTER:for (int l = 0, r = 0; l < left.length(); l++) {
			for (r = Math.max(l, r) ; r < right.length(); r++) {
				if (left.get(l).isEqual(right.get(r))) {
					r++;
					continue OUTER;
				}
			}
			return Rascal_FALSE;
		}
		return $VF.bool(left.length() != right.length());
	}

	public static final IBool aset_less_aset(final ISet left, final ISet right) {
		return $VF.bool(!left.isEqual(right) && left.isSubsetOf(right));
	}

	public static final IBool amap_less_amap(final IMap left, final IMap right) {
		return $VF.bool(left.isSubMap(right) && !right.isSubMap(left));
	}

	// ---- lessequal ---------------------------------------------------------

	public static final IBool lessequal(final IValue left, final IValue right){

		Type leftType = left.getType();
		Type rightType = right.getType();

		if (leftType.isSubtypeOf($TF.numberType()) && rightType.isSubtypeOf($TF.numberType())) {
			return ((INumber)left).lessEqual((INumber)right);
		}

		if(!leftType.comparable(rightType)){
			return Rascal_FALSE;
		}

		switch (ToplevelType.getToplevelType(leftType)) {
		// TODO: is this really faster than a TypeVisitor?? No because getTopLevelType includes a TypeVisitor itself.
		// TODO: check type of right
		case BOOL:
			return abool_lessequal_abool((IBool)left, (IBool)right);
		case STR:
			return astr_lessequal_astr((IString)left, (IString)right);
		case DATETIME:
			return adatetime_lessequal_adatetime((IDateTime)left, (IDateTime)right);
		case LOC:
			return aloc_lessequal_aloc((ISourceLocation)left, (ISourceLocation)right);
		case LIST:
		case LREL:
			return alist_lessequal_alist((IList)left, (IList)right);
		case SET:
		case REL:
			return aset_lessequal_aset((ISet)left, (ISet)right);
		case MAP:
			return amap_lessequal_amap((IMap)left, (IMap)right);
		case CONSTRUCTOR:
		case NODE:
			return anode_lessequal_anode((INode)left, (INode)right);
		case ADT:
			//return aadt_lessequal_aadt((IAbstractDataType)left, right);
		case TUPLE:
			return atuple_lessequal_atuple((ITuple)left, (ITuple)right);
		default:
			throw new InternalCompilerError("less: unexpected type " + leftType);
		}
	}
	
	public static final IBool aint_lessequal_aint(final IInteger a, final IInteger b) {
		return a.lessEqual(b);
	}

	public static final IBool aint_lessequal_areal(final IInteger a, final IReal b) {
		return a.lessEqual(b);
	}

	public static final IBool aint_lessequal_arat(final IInteger a, final IRational b) {
		return a.lessEqual(b);
	}

	public static final IBool aint_lessequal_anum(final IInteger a, final INumber b) {
		return a.lessEqual(b);
	}

	public static final IBool areal_lessequal_aint(final IReal a, final IInteger b) {
		return a.lessEqual(b);
	}

	public static final IBool areal_lessequal_areal(final IReal a, final IReal b) {
		return a.lessEqual(b);
	}

	public static final IBool areal_lessequal_arat(IReal a, IRational b) {
		return a.lessEqual(b);
	}

	public static final IBool areal_lessequal_anum(final IReal a, final INumber b) {
		return a.lessEqual(b);
	}

	public static final IBool arat_lessequal_aint(final IRational a, final IInteger b) {
		return a.lessEqual(b);
	}

	public static final IBool arat_lessequal_areal(final IRational a, final IReal b) {
		return a.lessEqual(b);
	}

	public static final IBool arat_lessequal_arat(final IRational a, final IRational b) {
		return a.lessEqual(b);
	}

	public static final IBool arat_lessequal_anum(final IRational a, final INumber b) {
		return a.lessEqual(b);
	}

	public static final IBool anum_lessequal_aint(final INumber a, final IInteger b) {
		return a.lessEqual(b);
	}

	public static final IBool anum_lessequal_areal(final INumber a, final IReal b) {
		return a.lessEqual(b);
	}
	public static final IBool anum_lessequal_arat(final INumber a, final IRational b) {
		return a.lessEqual(b);
	}

	public static final IBool anum_lessequal_anum(final INumber a, final INumber b) {
		return a.lessEqual(b);
	}
	
	
	

	public static final IBool abool_lessequal_abool(final IBool left, final IBool right) {
		boolean l = left.getValue();
		boolean r = right.getValue();
		return $VF.bool((!l && r) || (l == r));
	}

	public static final IBool astr_lessequal_astr(final IString left, final IString right) {
		int c = right.compare(left);
		return $VF.bool(c == -1 || c == 0);
	}

	public static final IBool adatetime_lessequal_adatetime(final IDateTime left, final IDateTime right) {
		int c = left.compareTo(right);
		return $VF.bool(c== -1 || c == 0);
	}


	public static final IBool aloc_lessequal_aloc(final ISourceLocation left, final ISourceLocation right) {
		int compare = SourceLocationURICompare.compare(left, right);
		if (compare < 0) {
			return Rascal_TRUE;
		}
		else if (compare > 0) {
			return Rascal_FALSE;
		}

		// but the uri's are the same
		// note that line/column information is superfluous and does not matter for ordering

		if (left.hasOffsetLength()) {
			if (!right.hasOffsetLength()) {
				return Rascal_FALSE;
			}

			int roffset = right.getOffset();
			int rlen = right.getLength();
			int loffset = left.getOffset();
			int llen = left.getLength();

			if (loffset == roffset) {
				return $VF.bool(llen <= rlen);
			}
			return $VF.bool(roffset < loffset && roffset + rlen >= loffset + llen);
		}
		else if (compare == 0) {
			return Rascal_TRUE;
		}

		if (!right.hasOffsetLength()) {
			throw new InternalCompilerError("missing offset length");
		}
		return Rascal_FALSE;
	}

	public static final IBool anode_lessequal_anode(final INode left, final INode right) {
		int compare = left.getName().compareTo(right.getName());

		if (compare <= -1) {
			return Rascal_TRUE;
		}

		if (compare >= 1){
			return Rascal_FALSE;
		}

		// if the names are not ordered, then we order lexicographically on the arguments:

		int leftArity = left.arity();
		int rightArity = right.arity();

		for (int i = 0; i < Math.min(leftArity, rightArity); i++) {
			if(!lessequal(left.get(i), right.get(i)).getValue()){
				return Rascal_FALSE;
			}
		}
		return $VF.bool(leftArity <= rightArity);
	}

	public static final IBool atuple_lessequal_atuple(final ITuple left, final ITuple right) {
		int leftArity = left.arity();
		int rightArity = right.arity();

		for (int i = 0; i < Math.min(leftArity, rightArity); i++) {			
			if(!lessequal(left.get(i), right.get(i)).getValue()){
				return Rascal_FALSE;
			}
		}

		return $VF.bool(leftArity <= rightArity);
	}

	public static final IBool alist_lessequal_alist(final IList left, final IList right) {
		if (left.length() == 0) {
			return Rascal_TRUE;
		}
		else if (left.length() > right.length()) {
			return Rascal_FALSE;
		}

		OUTER:for (int l = 0, r = 0; l < left.length(); l++) {
			for (r = Math.max(l, r) ; r < right.length(); r++) {
				if (left.get(l).isEqual(right.get(r))) {
					continue OUTER;
				}
			}
			return Rascal_FALSE;
		}

		return $VF.bool(left.length() <= right.length());
	}

	public static final IBool aset_lessequal_aset(final ISet left, final ISet right) {
		return $VF.bool(left.size() == 0 || left.isEqual(right) || left.isSubsetOf(right));
	}

	public static final IBool amap_lessequal_amap(final IMap left, final IMap right) {
		return $VF.bool(left.isSubMap(right));
	}

	// ---- product -----------------------------------------------------------

	public static final IValue product(final IValue lhs, final IValue rhs) {
		ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
		ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
		switch (lhsType) {
		case INT:
			switch (rhsType) {
			case INT:
				return ((IInteger) lhs).multiply((IInteger) rhs);
			case NUM:
				return ((IInteger) lhs).multiply((INumber) rhs);
			case REAL:
				return ((IInteger) lhs).multiply((IReal) rhs);
			case RAT:
				return ((IInteger) lhs).multiply((IRational) rhs);
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case NUM:
			switch (rhsType) {
			case INT:
				return ((INumber) lhs).multiply((IInteger) rhs);
			case NUM:
				return ((INumber) lhs).multiply((INumber) rhs);
			case REAL:
				return ((INumber) lhs).multiply((IReal) rhs);
			case RAT:
				return ((INumber) lhs).multiply((IRational) rhs);
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case REAL:
			switch (rhsType) {
			case INT:
				return ((IReal) lhs).multiply((IInteger) rhs);
			case NUM:
				return ((IReal) lhs).multiply((INumber) rhs);
			case REAL:
				return ((IReal) lhs).multiply((IReal) rhs);
			case RAT:
				return ((IReal) lhs).multiply((IRational) rhs);
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case RAT:
			switch (rhsType) {
			case INT:
				return ((IRational) lhs).multiply((IInteger) rhs);
			case NUM:
				return ((IRational) lhs).multiply((INumber) rhs);
			case REAL:
				return ((IRational) lhs).multiply((IReal) rhs);
			case RAT:
				return ((IRational) lhs).multiply((IRational) rhs);
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		default:
			throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
		}
	}

	public static final IInteger aint_product_aint(final IInteger a, final IInteger b) {
		return a.multiply(b);
	}

	public static final INumber aint_product_areal(final IInteger a, final IReal b) {
		return a.multiply(b);
	}

	public static final IRational aint_product_arat(final IInteger a, final IRational b) {
		return a.toRational().multiply(b);
	}

	public static final INumber aint_product_anum(final IInteger a, final INumber b) {
		return a.multiply(b);
	}

	public static final IReal areal_product_aint(final IReal a, final IInteger b) {
		return (IReal) a.multiply(b);
	}

	public static final IReal areal_product_areal(final IReal a, final IReal b) {
		return a.multiply(b);
	}

	public static final IReal areal_product_arat(IReal a, IRational b) {
		return (IReal) a.multiply(b);
	}

	public static final INumber areal_product_anum(final IReal a, final INumber b) {
		return a.multiply(b);
	}

	public static final INumber arat_product_aint(final IRational a, final IInteger b) {
		return a.multiply(b);
	}

	public static final IReal arat_product_areal(final IRational a, final IReal b) {
		return a.multiply(b);
	}

	public static final IRational arat_product_arat(final IRational a, final IRational b) {
		return a.toRational().multiply(b);
	}

	public static final INumber arat_product_anum(final IRational a, final INumber b) {
		return a.multiply(b);
	}

	public static final INumber anum_product_aint(final INumber a, final IInteger b) {
		return a.multiply(b);
	}

	public static final INumber anum_product_areal(final INumber a, final IReal b) {
		return a.multiply(b);
	}
	public static final INumber anum_product_arat(final INumber a, final IRational b) {
		return a.multiply(b);
	}

	public static final INumber anum_product_anum(final INumber a, final INumber b) {
		return a.multiply(b);
	}

	public static final IList alist_product_alist(final IList left, final IList right) {
		IListWriter w = $VF.listWriter();
		for(IValue l : left){
			for(IValue r : right){
				w.append($VF.tuple(l,r));
			}
		}
		return w.done();
	}

	public static final ISet aset_product_aset(final ISet left, final ISet right) {
		ISetWriter w = $VF.setWriter();
		for(IValue l : left){
			for(IValue r : right){
				w.insert($VF.tuple(l,r));
			}
		}
		return w.done();
	}
	
	// ---- regexp ------------------------------------------------------------
	
	public static final Matcher regExpCompile(String pat, String subject) {
		Pattern p = Pattern.compile(pat);
		return p.matcher(subject);
	}

	// ---- slice -------------------------------------------------------------

	public static final IString astr_slice(final IString str,  final Integer first,final  Integer second,final Integer end){
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, str.length());
		StringBuilder buffer = new StringBuilder();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
					buffer.appendCodePoint(str.charAt(i));
				}
			} else {
				for(int j = sd.first; j >= 0 && j > sd.end && j < str.length(); j += increment){
					buffer.appendCodePoint(str.charAt(j));
				}
			}
		return $VF.string(buffer.toString());
	}

	public static final IList anode_slice(final INode node,  final Integer first, final Integer second, final Integer end){
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, node.arity());
		IListWriter w = $VF.listWriter();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
					w.append(node.get(i));
				}
			} else {
				for(int j = sd.first; j >= 0 && j > sd.end && j < node.arity(); j += increment){
					w.append(node.get(j));
				}
			}

		return w.done();
	}


	public IList alist_slice(IList lst, Integer first, Integer second, Integer end){
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		IListWriter w = $VF.listWriter();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
					w.append(lst.get(i));
				}
			} else {
				for(int j = sd.first; j >= 0 && j > sd.end && j < lst.length(); j += increment){
					w.append(lst.get(j));
				}
			}
		return w.done();
	}

	public static final IList $makeSlice(final INode node, final Integer first, final Integer second,final Integer end){
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, node.arity());
		IListWriter w = $VF.listWriter();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
					w.append(node.get(i));
				}
			} else {
				for(int j = sd.first; j >= 0 && j > sd.end && j < node.arity(); j += increment){
					w.append(node.get(j));
				}
			}

		return w.done();
	}

	private static SliceDescriptor makeSliceDescriptor(final Integer first, final Integer second, final Integer end, final int len) {
		int firstIndex = 0;
		int secondIndex = 1;
		int endIndex = len;

		if(first != null){
			firstIndex = first;
			if(firstIndex < 0)
				firstIndex += len;
		}
		if(end != null){
			endIndex = end;
			if(endIndex < 0){
				endIndex += len;
			}
		}

		if(second == null){
			secondIndex = firstIndex + ((firstIndex <= endIndex) ? 1 : -1);
		} else {
			secondIndex = second;
			if(secondIndex < 0)
				secondIndex += len;
			if(!(first == null && end == null)){
				if(first == null && secondIndex > endIndex)
					firstIndex = len - 1;
				if(end == null && secondIndex < firstIndex)
					endIndex = -1;
			}
		}

		if(len == 0 || firstIndex >= len){
			firstIndex = secondIndex = endIndex = 0;
		} else if(endIndex > len){
			endIndex = len;
		} 
		//		else if(endIndex == -1){
		//			endIndex = 0;
		//		}

		return new SliceDescriptor(firstIndex, secondIndex, endIndex);
	}

	public static final IString astr_slice_replace(final IString str, final Integer first, final Integer second, final Integer end, final IString repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, str.length());
		return  str.replace(sd.first, sd.second, sd.end, repl);
	}

	public static final INode anode_slice_replace(final INode node, final Integer first, final Integer second, final Integer end, final IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, node.arity());
		return  node.replace(sd.first, sd.second, sd.end, repl);
	}

	public static final IList alist_slice_replace(final IList lst, final Integer first, final Integer second, final Integer end, final IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		return  updateListSlice(lst, sd, SliceOperator.replace, repl);
	}

	public static final IList alist_slice_add(final IList lst, final Integer first, final Integer second, final Integer end, final IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		return  updateListSlice(lst, sd, SliceOperator.add, repl);
	}

	public static final IList alist_slice_subtract(final IList lst, final Integer first, final Integer second, final Integer end, final IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		return  updateListSlice(lst, sd, SliceOperator.subtract, repl);
	}

	public static final IList alist_slice_product(final IList lst, final Integer first, final Integer second, final Integer end, final IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		return  updateListSlice(lst, sd, SliceOperator.product, repl);
	}

	public static final IList alist_slice_divide(final IList lst, final Integer first, final Integer second, final Integer end, final IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		return  updateListSlice(lst, sd, SliceOperator.divide, repl);
	}

	public static final IList updateListSlice(final IList lst, final SliceDescriptor sd, final SliceOperator op, final IList repl){
		IListWriter w = $VF.listWriter();
		int increment = sd.second - sd.first;
		int replIndex = 0;
		int rlen = repl.length();
		boolean wrapped = false;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				assert increment > 0;
				int listIndex = 0;
				while(listIndex < sd.first){
					w.append(lst.get(listIndex++));
				}
				while(listIndex >= 0 && listIndex < sd.end){
					w.append(op.execute(lst.get(listIndex), repl.get(replIndex++)));
					if(replIndex == rlen){
						replIndex = 0;
						wrapped = true;
					}
					for(int q = 1; q < increment && listIndex + q < sd.end; q++){
						w.append(lst.get(listIndex + q));
					}
					listIndex += increment;
				}
				listIndex = sd.end;
				if(!wrapped){
					while(replIndex < rlen){
						w.append(repl.get(replIndex++));
					}
				}
				while(listIndex < lst.length()){
					w.append(lst.get(listIndex++));
				}
			} else {
				assert increment < 0;
				int j = lst.length() - 1;
				while(j > sd.first){
					w.insert(lst.get(j--));
				}
				while(j >= 0 && j > sd.end && j < lst.length()){
					w.insert(op.execute(lst.get(j), repl.get(replIndex++)));
					if(replIndex == rlen){
						replIndex = 0;
						wrapped = true;
					}
					for(int q = -1; q > increment && j + q > sd.end; q--){
						w.insert(lst.get(j + q));
					}
					j += increment;
				}
				j = sd.end;
				if(!wrapped){
					while(replIndex < rlen){
						w.insert(repl.get(replIndex++));
					}
				}

				while(j >= 0){
					w.insert(lst.get(j--));
				}

			}
		return w.done();
	}



	// ---- splice ------------------------------------------------------------
	/**
	 * Splice elements in a list writer
	 * 
	 * IListWriter w, IListOrISet val  => w with val's elements spliced in
	 */
	public static final IListWriter listwriter_splice(final IListWriter writer, final IValue val) {
		if(val instanceof IList){
			IList lst = (IList) val;
			for(IValue v : lst){
				writer.append(v);
			}
		} else if(val instanceof ISet){
			ISet set = (ISet) val;
			for(IValue v : set){
				writer.append(v);
			}
		} else {
			writer.append((IValue) val);
		}
		return writer;
	}

	/**
	 * Splice elements in a set writer
	 * 
	 * ISetWriter w, IListOrISet val => w with val's elements spliced in
	 */

	public static final ISetWriter setwriter_splice(final ISetWriter writer, final IValue val) {
		if(val instanceof IList){
			IList lst = (IList) val;
			for(IValue v : lst){
				writer.insert(v);
			}
		} else if(val instanceof ISet){
			ISet set = (ISet) val;
			for(IValue v : set){
				writer.insert(v);
			}
		} else {
			writer.insert((IValue) val);
		}
		return writer;
	}


	// ---- subscript ---------------------------------------------------------

	public static final IString astr_subscript_int(final IString str, final int idx) {
		try {
			return (idx >= 0) ? str.substring(idx, idx+1)
					: str.substring(str.length() + idx, str.length() + idx + 1);
		} catch(IndexOutOfBoundsException e) {
			throw RascalExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}

	public static final GuardedIValue guarded_astr_subscript_int(final IString str, final int idx){
		try {
			IString res = (idx >= 0) ? str.substring(idx, idx+1)
					: str.substring(str.length() + idx, str.length() + idx + 1);
			return new GuardedIValue(res);
		} catch(IndexOutOfBoundsException e) {
			return UNDEFINED;
		}
	}

	public static final GuardedIValue guarded_list_subscript(final IList lst, final int idx) {
		try {
			return new GuardedIValue(lst.get((idx >= 0) ? idx : (lst.length() + idx)));
		} catch(IndexOutOfBoundsException e) {
			return UNDEFINED;
		}
	}

	public static final GuardedIValue guarded_map_subscript(final IMap map, final IValue idx) {
		IValue v = map.get(idx);
		return v == null? UNDEFINED : new GuardedIValue();
	}

	public static final IValue atuple_subscript_int(final ITuple tup, final int idx) {
		try {
			return tup.get((idx >= 0) ? idx : tup.arity() + idx);
		} catch(IndexOutOfBoundsException e) {
			throw RascalExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}

	public static final GuardedIValue guarded_atuple_subscript_int(final ITuple tup, final int idx) {
		try {
			IValue res = tup.get((idx >= 0) ? idx : tup.arity() + idx);
			return new GuardedIValue(res);
		} catch(IndexOutOfBoundsException e) {
			return UNDEFINED;
		}
	}

	public static final IValue anode_subscript_int(final INode node, int idx) {
		try {
			if(idx < 0){
				idx =  node.arity() + idx;
			}
			return node.get(idx);  
		} catch(IndexOutOfBoundsException e) {
			throw RascalExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}

	public static final GuardedIValue guarded_anode_subscript_int(final INode node, int idx) {
		try {
			if(idx < 0){
				idx =  node.arity() + idx;
			}
			IValue res = node.get(idx);  
			return new GuardedIValue(res);
		} catch(IndexOutOfBoundsException e) {
			return UNDEFINED;
		}
	}

	public static final IValue aadt_subscript_int(final IConstructor cons, final int idx) {
		try {
			return cons.get((idx >= 0) ? idx : (cons.arity() + idx));
		} catch(IndexOutOfBoundsException e) {
			throw RascalExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}

	public static final GuardedIValue guarded_aadt_subscript_int(final IConstructor cons, final int idx) {
		try {
			IValue res = cons.get((idx >= 0) ? idx : (cons.arity() + idx));
			return new GuardedIValue(res);
		} catch(IndexOutOfBoundsException e) {
			return UNDEFINED;
		}
	}

	/**
	 * Subscript of a n-ary rel with a single subscript (no set and unequal to _)
	 */
	
	public static final ISet arel_subscript1_noset(final ISet rel, final IValue idx) {
		if(rel.isEmpty()){
			return rel;
		}
		return rel.asRelation().index(idx);
	}
	
	public static final GuardedIValue guarded_arel_subscript1_noset(final ISet rel, final IValue idx) {
		try {
			return  new GuardedIValue(arel_subscript1_noset(rel, idx));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	/**
	 * Subscript of a binary rel with a single subscript (a set but unequal to _)
	 */
	public static final ISet arel2_subscript1_aset(final ISet rel, final ISet idx) {
		if(rel.isEmpty()){
			return rel;
		}
		ISetWriter wset = $VF.setWriter();

		for (IValue v : rel) {
			ITuple tup = (ITuple)v;

			if((((ISet) idx).contains(tup.get(0)))){
				wset.insert(tup.get(1));
			} 
		}
		return wset.done();
	}
	
	public static final GuardedIValue guarded_arel2_subscript1_aset(final ISet rel, final ISet idx) {
		try {
			return  new GuardedIValue(arel2_subscript1_aset(rel, idx));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	/**
	 * Subscript of an n-ary (n > 2) rel with a single subscript (a set and unequal to _)
	 */
	public static final ISet arel_subscript1_aset(final ISet rel, final ISet index) {
		if(rel.isEmpty()){
			return rel;
		}
		int relArity = rel.getElementType().getArity();		

		ISetWriter wset = $VF.setWriter();
		IValue args[] = new IValue[relArity - 1];

		for (IValue v : rel) {
			ITuple tup = (ITuple)v;

			if((((ISet) index).contains(tup.get(0)))){
				for (int i = 1; i < relArity; i++) {
					args[i - 1] = tup.get(i);
				}
				wset.insert($VF.tuple(args));
			} 
		}
		return wset.done();
	}
	
	public static final GuardedIValue guarded_arel_subscript1_aset(final ISet rel, final ISet index) {
		try {
			return  new GuardedIValue(arel_subscript1_aset(rel, index));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}

	/**
	 * Subscript of rel, general case
	 * subsDesc is a subscript descriptor: an array with integers: 0: noset, 1: set, 2: wildcard
	 */

	public static final ISet arel_subscript (final ISet rel, final IValue[] idx, final int[] subsDesc) {
		if(rel.isEmpty()){
			return rel;
		}
		int indexArity = idx.length;
		int relArity = rel.getElementType().getArity();

		ISetWriter wset = $VF.setWriter();

		if(relArity - indexArity == 1){	// Return a set
			allValues:
				for (IValue v : rel) {
					ITuple tup = (ITuple)v;
					for(int k = 0; k < indexArity; k++){
						switch(subsDesc[k]){
						case 0: 
							if(!tup.get(k).isEqual(idx[k])) continue allValues; 
							continue;
						case 1: 
							if(!(((ISet)idx[k]).contains(tup.get(k)))) continue allValues;
						}
					}
					wset.insert(tup.get(indexArity));
				}
		} else {						// Return a relation
			IValue args[] = new IValue[relArity - indexArity];
			allValues:
				for (IValue v : rel) {
					ITuple tup = (ITuple)v;
					for(int k = 0; k < indexArity; k++){
						switch(subsDesc[k]){
						case 0: 
							if(!tup.get(k).isEqual(idx[k])) continue allValues; 
							continue;
						case 1: 
							if(!((ISet)idx[k]).contains(tup.get(k))) continue allValues;
						}
					}

					for (int i = indexArity; i < relArity; i++) {
						args[i - indexArity] = tup.get(i);
					}
					wset.insert($VF.tuple(args));
				}
		}

		return wset.done();
	}
	
	public static final GuardedIValue guarded_arel_subscript (final ISet rel, final IValue[] idx, final int[] subsDesc) {
		try {
			return  new GuardedIValue(arel_subscript(rel, idx, subsDesc));
		} catch (Exception e) {
			return UNDEFINED;
		}
	}
	
	public static final IValue subject_subscript(final IList lst, final int idx) {
		return lst.get(idx);
	}
	
	public static final IValue subject_subscript(final ITuple tup, final int idx) {
		return tup.get(idx);
	}
	
	public static final IString subject_subscript(final IString str, final int idx) {
		return str.substring(idx, 1);
	}
	
	public static final IValue subject_subscript(final IValue subject, final int idx) {
		if(subject.getType().isList()) {
			return ((IList) subject).get(idx);
		}
		if(subject.getType().isTuple()) {
			return ((ITuple) subject).get(idx);
		}
		if(subject.getType().isString()) {
			return ((IString) subject).substring(idx, 1);
		}
		throw new RuntimeException("Unsupported subject subscript");
		
	}

	// ---- subtract ----------------------------------------------------------

	public static final IValue subtract(final IValue lhs, final IValue rhs) {
		ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
		ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
		switch (lhsType) {
		case INT:
			switch (rhsType) {
			case INT:
				return ((IInteger) lhs).subtract((IInteger)rhs);
			case NUM:
				return ((IInteger) lhs).subtract((INumber)rhs);
			case REAL:
				return ((IInteger) lhs).subtract((IReal)rhs);
			case RAT:
				return ((IInteger) lhs).subtract((IRational)rhs);
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case NUM:
			switch (rhsType) {
			case INT:
				return ((INumber) lhs).subtract((IInteger)rhs);
			case NUM:
				return ((INumber) lhs).subtract((INumber)rhs);
			case REAL:
				return ((INumber) lhs).subtract((IReal)rhs);
			case RAT:
				return ((INumber) lhs).subtract((IRational)rhs);
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case REAL:
			switch (rhsType) {
			case INT:
				return ((IReal) lhs).subtract((IInteger)rhs);
			case NUM:
				return ((IReal) lhs).subtract((INumber)rhs);
			case REAL:
				return ((IReal) lhs).subtract((IReal)rhs);
			case RAT:
				return ((IReal) lhs).subtract((IRational)rhs);
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case RAT:
			switch (rhsType) {
			case INT:
				return ((IRational) lhs).subtract((IInteger)rhs);
			case NUM:
				return ((IRational) lhs).subtract((INumber)rhs);
			case REAL:
				return ((IRational) lhs).subtract((IReal)rhs);
			case RAT:
				return ((IRational) lhs).subtract((IRational)rhs);
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		default:
			throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
		}
	}


	// ---- update ------------------------------------------------------------

	/**
	 * Update list element
	 * 
	 */

	public static final IList alist_update(final IList lst, int n, final IValue v) {
		if(n < 0){
			n = lst.length() + n;
		}
		try {
			return lst.put(n, v);
		} catch (IndexOutOfBoundsException e){
			throw RascalExceptionFactory.indexOutOfBounds($VF.integer(n));

		}
	}

	/**
	 * Update map element
	 * 
	 */
	public static final IMap amap_update(IMap map, IValue key, IValue v) {
		return map.put(key, v);
	}

	/**
	 * Update tuple element
	 * 
	 */

	public static final ITuple atuple_update(final ITuple tup, final int n, final IValue v) {
		try {
			return tup.set(n, v);
		} catch (IndexOutOfBoundsException e){
			throw RascalExceptionFactory.indexOutOfBounds($VF.integer(n));

		}
	}

	/**
	 * Update argument of adt constructor by its field name
	 * 
	 */

	public static final IConstructor aadt_update(final IConstructor cons, final IString field, final IValue v) {
		return cons.set(field.getValue(), v);
	}
}

enum SliceOperator {
	replace(0) {
		@Override
		public IValue execute(final IValue left, final IValue right) {
			return right;
		}
	},
	add(1) {
		@Override
		public IValue execute(final IValue left, final IValue right) {
			return $RascalModule.add(left, right);
		}
	},
	subtract(2){
		@Override
		public IValue execute(final IValue left, final IValue right) {
			return $RascalModule.subtract(left, right);
		}
	}, 
	product(3){
		@Override
		public IValue execute(final IValue left, final IValue right) {
			return $RascalModule.product(left, right);
		}
	}, 

	divide(4){
		@Override
		public IValue execute(final IValue left, final IValue right) {
			return $RascalModule.divide(left, right);
		}
	}, 

	intersect(5){
		@Override
		public IValue execute(final IValue left, final IValue right) {
			return $RascalModule.intersect(left, right);
		}
	};

	final int operator;

	public static final SliceOperator[] values = SliceOperator.values();

	public static final SliceOperator fromInteger(int n) {
		return values[n];
	}

	public abstract IValue execute(final IValue left, final IValue right);

	public static final SliceOperator replace() {
		return values[0];
	}

	public static final SliceOperator add() {
		return values[1];
	}

	public static final SliceOperator subtract() {
		return values[2];
	}

	public static final SliceOperator product() {
		return values[3];
	}

	public static final SliceOperator divide() {
		return values[4];
	}

	public static final SliceOperator intersect() {
		return values[5];
	}

	SliceOperator(int op) {
		this.operator = op;
	}
}