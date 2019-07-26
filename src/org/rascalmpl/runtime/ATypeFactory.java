package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.util.Arrays;

import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class ATypeFactory {
	// Factories and stores
	public final static IValueFactory $VF = ValueFactoryFactory.getValueFactory();
	public final static TypeFactory $TF = TypeFactory.getInstance();
	public final static TypeStore $TS = new TypeStore();

	// ADTs needed for AType, Tree and related types
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
	
	public static final boolean isReified(IValue v) {
		return v.getType() == ReifiedAType;
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

}
