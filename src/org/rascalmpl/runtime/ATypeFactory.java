package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.util.Arrays;

import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

/*** NOT USED ANYMORE, NOT USED ANYMORE, NOT USED ANYMORE, NOT USED ANYMORE, NOT USED ANYMORE, NOT USED ANYMORE ***/
public class ATypeFactory {
	// Factories and stores
	public final IValueFactory $VF = ValueFactoryFactory.getValueFactory();
	public final TypeFactory $TF = TypeFactory.getInstance();
	public final RascalTypeFactory $RTF = RascalTypeFactory.getInstance();
	public TypeStore $TS = new TypeStore();
	//public final TypeReifier $TR = new TypeReifier($VF);	//Temporary, for conversions vtype <==> atype

	// ADTs needed for AType, Tree and related types
	public final Type AType = $TF.abstractDataType($TS, "AType");
	public final Type ReifiedAType = $TF.abstractDataType($TS, "ReifiedAType");

	public final Type Keyword = $TF.abstractDataType($TS, "Keyword");
	public final Type Associativity = $TF.abstractDataType($TS, "Associativity");
	public final Type AProduction = $TF.abstractDataType($TS, "AProduction");
	public final Type SyntaxRole = $TF.abstractDataType($TS, "SyntaxRole");
	public final Type Attr = $TF.abstractDataType($TS, "Attr");
	public final Type ATree = $TF.abstractDataType($TS, "ATree");
	public final Type ACharRange = $TF.abstractDataType($TS, "ACharRange");
	public final Type ACondition = $TF.abstractDataType($TS, "ACondition");
	private final Type str = $TF.stringType();	// convenience type

//	public final IBool Rascal_TRUE =  $VF.bool(true);
//	public final IBool Rascal_FALSE =  $VF.bool(false);

	// avoid
	public final Type AType_avoid = $TF.constructor($TS, AType, "avoid");
	public final Type AType_avoid_lab = $TF.constructor($TS, AType, "avoid", str, "alabel");
	public final boolean $is_avoid(Type t) { return t == AType_avoid || t == AType_avoid_lab; }

	public IConstructor $avoid() { return $VF.constructor(AType_avoid); }
	public IConstructor $avoid(IString label) { return $VF.constructor(AType_avoid_lab, label); }
	public IConstructor $avoid(String label) { return $avoid($VF.string(label)); }

	// abool
	public final Type AType_abool = $TF.constructor($TS, AType, "abool");
	public final Type AType_abool_lab = $TF.constructor($TS, AType, "abool", str, "alabel");
	public final boolean $is_abool(Type t) { return t == AType_abool || t == AType_abool_lab; }

	public IConstructor $abool() { return $VF.constructor(AType_abool); }
	public IConstructor $abool(IString label) { return $VF.constructor(AType_abool_lab, label); }
	public IConstructor $abool(String label) { return $abool($VF.string(label)); }

	// aint
	public final Type AType_aint = $TF.constructor($TS, AType, "aint");
	public final Type AType_aint_lab = $TF.constructor($TS, AType, "aint", str, "alabel");
	public final boolean $is_aint(Type t) { return t == AType_aint || t == AType_aint_lab; }
	
	public IConstructor $aint() { return $VF.constructor(AType_aint); }
	public IConstructor $aint(IString label) { return $VF.constructor(AType_aint_lab, label); }
	public IConstructor $aint(String label) { return $aint($VF.string(label)); }

	// areal
	public final Type AType_areal = $TF.constructor($TS, AType, "areal");
	public final Type AType_areal_lab = $TF.constructor($TS, AType, "areal", str, "alabel");
	public final boolean $is_areal(Type t) { return t == AType_areal || t == AType_areal_lab; }

	public IConstructor $areal() { return $VF.constructor(AType_areal); }
	public IConstructor $areal(IString label) { return $VF.constructor(AType_areal_lab, label); }
	public IConstructor $areal(String label) { return $areal($VF.string(label)); }

	// arat
	public final Type AType_arat = $TF.constructor($TS, AType, "arat");
	public final Type AType_arat_lab = $TF.constructor($TS, AType, "arat", str, "alabel");
	public final boolean $is_arat(Type t) { return t == AType_arat || t == AType_arat_lab; }

	public IConstructor $arat() { return $VF.constructor(AType_arat); }
	public IConstructor $arat(IString label) { return $VF.constructor(AType_arat_lab, label); }
	public IConstructor $arat(String label) { return $arat($VF.string(label)); }

	// anum	
	public final Type AType_anum = $TF.constructor($TS, AType,  "anum");
	public final Type AType_anum_lab = $TF.constructor($TS, AType,  "anum", str, "alabel");
	public final boolean $is_anum(Type t) { return t == AType_anum || t == AType_anum_lab; }

	public IConstructor $anum() { return $VF.constructor(AType_anum); }
	public IConstructor $anum(IString label ) { return $VF.constructor(AType_anum_lab, label); }
	public IConstructor $anum(String label ) { return $anum($VF.string(label)); }

	// astr
	public final Type AType_astr = $TF.constructor($TS, AType,  "astr");
	public final Type AType_astr_lab = $TF.constructor($TS, AType,  "astr", str, "alabel");
	public final boolean $is_astr(Type t) { return t == AType_astr || t == AType_astr_lab; }

	public IConstructor $astr() { return $VF.constructor(AType_astr); }
	public IConstructor $astr(IString label) { return $VF.constructor(AType_astr_lab, label); }
	public IConstructor $astr(String label) { return $VF.constructor(AType_astr_lab, $VF.string(label)); }

	// aloc
	public final Type AType_aloc = $TF.constructor($TS, AType,  "aloc");
	public final Type AType_aloc_lab = $TF.constructor($TS, AType,  "aloc", str, "alabel");
	public final boolean $is_aloc(Type t) { return t == AType_aloc || t == AType_aloc_lab; }

	public IConstructor $aloc() { return $VF.constructor(AType_aloc); }
	public IConstructor $aloc(IString label) { return $VF.constructor(AType_aloc_lab, label); }
	public IConstructor $aloc(String label) { return $VF.constructor(AType_aloc_lab, $VF.string(label)); }

	// adatetime
	public final Type AType_adatetime = $TF.constructor($TS, AType,  "adatetime");
	public final Type AType_adatetime_lab = $TF.constructor($TS, AType,  "adatetime", str, "alabel");
	public final boolean $is_adatetime(Type t) { return t == AType_adatetime || t == AType_adatetime_lab; }

	public IConstructor $adatetime() { return $VF.constructor(AType_adatetime); }
	public IConstructor $adatetime(IString label) { return $VF.constructor(AType_adatetime_lab, label); }
	public IConstructor $adatetime(String label) { return $adatetime($VF.string(label)); }

	// alist
	public final Type AType_alist = $TF.constructor($TS, AType, "alist", AType, "elmType");
	public final Type AType_alist_lab = $TF.constructor($TS, AType, "alist", AType, "elmType", str, "alabel");
	public final boolean $is_alist(Type t) { return t == AType_alist || t == AType_alist_lab; }

	public IConstructor $alist(IConstructor t) { return $VF.constructor(AType_alist, t); }
	public IConstructor $alist(IConstructor t, IString label) { return $VF.constructor(AType_alist_lab, t, label); }
	public IConstructor $alist(IConstructor t, String label) { return $alist(t, $VF.string(label)); }

	// abag
	public final Type AType_abag = $TF.constructor($TS, AType, "abag", AType, "elmType");
	public final Type AType_abag_lab = $TF.constructor($TS, AType, "abag", AType, "elmType", str, "alabel");
	public final boolean $is_abag(Type t) { return t == AType_abag || t == AType_abag_lab; }

	public IConstructor $abag(IConstructor t) { return $VF.constructor(AType_abag, t); }
	public IConstructor $abag(IConstructor t, IString label) { return $VF.constructor(AType_abag_lab, t, label); }
	public IConstructor $abag(IConstructor t, String label) { return $abag(t, $VF.string(label)); }

	// aset
	public final Type AType_aset = $TF.constructor($TS, AType, "aset", AType, "elmType");
	public final Type AType_aset_lab = $TF.constructor($TS, AType, "aset", AType, "elmType", str, "alabel");
	public final boolean $is_aset(Type t) { return t == AType_aset || t == AType_aset_lab; }

	public IConstructor $aset(IConstructor t) { return $VF.constructor(AType_aset, t); }
	public IConstructor $aset(IConstructor t, IString label) { return $VF.constructor(AType_aset_lab, t, label); }
	public IConstructor $aset(IConstructor t, String label) { return $aset(t, $VF.string(label)); }

	// arel
	public final Type AType_arel = $TF.constructor($TS, AType, "arel", AType, "elmType");
	public final Type AType_arel_lab = $TF.constructor($TS, AType, "arel", AType, "elmType", str, "alabel");
	public final boolean $is_arel(Type t) { return t == AType_arel || t == AType_arel_lab; }

	public IConstructor $arel(IConstructor[] ts) { return $VF.constructor(AType_arel, ts); }
	public IConstructor $arel(IConstructor[] ts, IString label) { 
		IValue[] vals = Arrays.copyOf(ts, ts.length+1);
		vals[ts.length]= label; 
		return $VF.constructor(AType_arel_lab, vals);
	}
	public IConstructor $arel(IConstructor[] ts, String label) { return $arel(ts, $VF.string(label)); }

	// alrel
	public final Type AType_alrel = $TF.constructor($TS, AType, "alrel",AType, "elmType");
	public final Type AType_alrel_lab = $TF.constructor($TS, AType, "alrel",AType, "elmType", str, "alabel");
	public final boolean $is_alrel(Type t) { return t == AType_alrel || t == AType_alrel_lab; }

	public IConstructor $alrel(IConstructor[] ts) { return $VF.constructor(AType_alrel, ts); }
	public IConstructor $alrel(IConstructor[] ts, IString label) {
		IValue[] vals = Arrays.copyOf(ts, ts.length+1);
		vals[ts.length]= label; 
		return $VF.constructor(AType_alrel, vals); 
	}
	public IConstructor $alrel(IConstructor[] ts, String label) { return $alrel(ts,  $VF.string(label)); }

	// atuple
	public final Type AType_atuple = $TF.constructor($TS, AType, "atuple", AType, "elmType");
	public final Type AType_atuple_lab = $TF.constructor($TS, AType, "atuple", AType, "elmType", str, "alabel");
	public final boolean $is_atuple(Type t) { return t == AType_atuple || t == AType_atuple_lab; }

	public IConstructor $atuple(IConstructor[] ts) { return $VF.constructor(AType_atuple, ts); }
	public IConstructor $atuple(IConstructor[] ts, IString label) {
		IValue[] vals = Arrays.copyOf(ts, ts.length+1);
		vals[ts.length]= label; 
		return $VF.constructor(AType_atuple, vals); 
	}
	public IConstructor $atuple(IConstructor[] ts, String label) { return $atuple(ts, $VF.string(label)); }

	// amap
	public final Type AType_amap = $TF.constructor($TS, AType, "amap", AType, "from", AType, "to");
	public final Type AType_amap_lab = $TF.constructor($TS, AType, "amap", AType, "from", AType, "to", str, "alabel");
	public final boolean $is_amap(Type t) { return t == AType_amap || t == AType_amap_lab; }

	public IConstructor $amap(IConstructor k, IConstructor v) { return $VF.constructor(AType_amap, k, v); }
	public IConstructor $amap(IConstructor k, IConstructor v, IString label) { return $VF.constructor(AType_amap_lab, k, v, label); }
	public IConstructor $amap(IConstructor k, IConstructor v, String label) { return $amap(k, v, $VF.string(label)); }

	// afunc
//	public final Type AType_afunc = $TF.constructor($TS, AType, "afunc", AType, "ret", $TF.listType(AType), "formals", $TF.listType(Keyword), "kwFormals");
//	public final Type AType_afunc_lab = $TF.constructor($TS, AType, "afunc", AType, "ret", $TF.listType(AType), "formals", $TF.listType(Keyword), "kwFormals", str, "alabel");
//	public final boolean $is_afunc(Type t) { return t == AType_afunc || t == AType_afunc_lab; }
//	//TODO: bool varArgs=false, str deprecationMessage="", bool isConcreteArg=false, int abstractFingerprint=0, int concreteFingerprint=0)
//	
//	public IConstructor $afunc(IConstructor ret, IList formals, IList keywords) { return $VF.constructor(AType_afunc, formals, keywords); }

	// anode
	public final Type AType_anode = $TF.constructor($TS, AType,  "anode");
	public final Type AType_anode_lab = $TF.constructor($TS, AType,  "anode", str, "alabel");
	public final boolean $is_anode(Type t) { return t == AType_anode || t == AType_anode_lab; }

	public IConstructor $anode() { return $VF.constructor(AType_anode); }
	public IConstructor $anode(IString label) { return $VF.constructor(AType_anode_lab, label); }
	public IConstructor $anode(String label) { return $anode($VF.string(label)); }

	// aadt
	public final Type AType_aadt = $TF.constructor($TS, AType, "aadt", str, "name", $TF.listType(AType), "parameters");
	public final Type AType_aadt_lab = $TF.constructor($TS, AType, "aadt", str, "name", $TF.listType(AType), "parameters", str, "alabel");
	public final boolean $is_aadt(Type t) { return t == AType_aadt || t == AType_aadt_lab; }

	public IConstructor $aadt(IString adtName, IList parameters, IConstructor syntaxRole) { return $VF.constructor(AType_aadt, adtName, parameters, syntaxRole); }
	public IConstructor $aadt(IString adtName, IList parameters, IConstructor syntaxRole, IString label) { return $VF.constructor(AType_aadt_lab, adtName, parameters, syntaxRole, label); }
	public IConstructor $aadt(IString adtName, IList parameters, IConstructor syntaxRole, String label) { return $aadt(adtName, parameters, syntaxRole, $VF.string(label)); }

	// acons
	public final Type AType_acons = $TF.constructor($TS, AType, "acons", AType, "aadt", $TF.listType(AType), "fields", $TF.listType(Keyword), "kwFields");
	public final Type AType_acons_lab = $TF.constructor($TS, AType, "acons", AType, "aadt", $TF.listType(AType), "fields", $TF.listType(Keyword), "kwFields", str, "alabel");
	public final boolean $is_acons(Type t) { return t == AType_acons || t == AType_acons_lab; }

	public IConstructor $acons(IConstructor adt, IList fields, IList kwFields) { return $VF.constructor(AType_acons, adt, fields, kwFields); }
	public IConstructor $acons(IConstructor adt, IList fields, IList kwFields, IString label) { return $VF.constructor(AType_acons_lab, adt, fields, kwFields, label); }
	public IConstructor $acons(IConstructor adt, IList fields, IList kwFields, String label) { return $acons(adt, fields, kwFields, $VF.string(label)); }

	// aprod
	public final Type AType_aprod = $TF.constructor($TS, AType,  "aprod", AProduction, "production");
	public final Type AType_aprod_lab = $TF.constructor($TS, AType,  "aprod", AProduction, "production", str, "alabel");
	public final boolean $is_aprod(Type t) { return t == AType_aprod || t == AType_aprod_lab; }

	public IConstructor $aprod(IConstructor production) { return $VF.constructor(AType_aprod, production); }
	public IConstructor $aprod(IConstructor production, IString label) { return $VF.constructor(AType_aprod_lab, production, label); }
	public IConstructor $aprod(IConstructor production, String label) { return $aprod(production, $VF.string(label)); }

	// aparameter
	public final Type AType_aparameter = $TF.constructor($TS, AType, "aparameter", str , "pname", AType, "bound");
	public final Type AType_aparameter_lab = $TF.constructor($TS, AType, "aparameter", str , "pname", AType, "bound", str, "alabel");
	public final boolean $is_aparameter(Type t) { return t == AType_aparameter || t == AType_aparameter_lab; }

	public IConstructor $aparameter(IString pname, IConstructor bound) { return $VF.constructor(AType_aparameter, pname, bound); }

	// areified
	
	public final Type AType_areified = $TF.constructor($TS, AType, "areified", AType, "atype");
	public final Type AType_areified_lab = $TF.constructor($TS, AType, "areified", AType, "atype", str, "alabel");
	public final boolean $is_areified(Type t) { return t == AType_areified || t == AType_areified_lab; }

	public IConstructor $areified(IConstructor t) { return $VF.constructor(AType_areified, t); }
	public IConstructor $areified(IConstructor t, IString label) { return $VF.constructor(AType_areified_lab, t, label); }
	public IConstructor $areified(IConstructor t, String label) { return $areified(t, $VF.string(label)); }

	// avalue
	public final Type AType_avalue = $TF.constructor($TS, AType,  "avalue");
	public final Type AType_avalue_lab = $TF.constructor($TS, AType,  "avalue", str, "alabel");
	public final boolean $is_avalue(Type t) { return t == AType_avalue || t == AType_avalue_lab; }

	public IConstructor $avalue() { return $VF.constructor(AType_avalue); }
	public IConstructor $avalue(IString label) { return $VF.constructor(AType_avalue_lab, label); }
	public IConstructor $avalue(String label) { return $avalue($VF.string(label)); }

	// atype (reified type constructor)
	final Type AType_atype = $TF.constructor($TS, AType, "atype", ReifiedAType, "symbol", $TF.mapType(ReifiedAType,$TF.setType(ReifiedAType)), "definitions");

	public final boolean $isReified(IValue v) {
		return v.getType() == ReifiedAType;
	}

	// ---- SyntaxRole --------------------------------------------------------

	public final Type SyntaxRole_dataSyntax = $TF.constructor($TS,  SyntaxRole, "dataSyntax");
	public final Type SyntaxRole_contextFreeSyntax = $TF.constructor($TS,  SyntaxRole, "contextfreeSyntax");
	public final Type SyntaxRole_lexicalSyntax = $TF.constructor($TS,  SyntaxRole, "lexicalSyntax");
	public final Type SyntaxRole_keywordSyntax = $TF.constructor($TS,  SyntaxRole, "keywordSyntax");
	public final Type SyntaxRole_layoutSyntax = $TF.constructor($TS,  SyntaxRole, "layoutSyntax");
	public final Type SyntaxRole_illegalSyntax = $TF.constructor($TS,  SyntaxRole, "illegalSyntax");

	public IConstructor dataSyntax = $VF.constructor(SyntaxRole_dataSyntax);
	public IConstructor contextFreeSyntax = $VF.constructor(SyntaxRole_contextFreeSyntax);
	public IConstructor lexicalSyntax = $VF.constructor(SyntaxRole_lexicalSyntax);
	public IConstructor keywordSyntax = $VF.constructor(SyntaxRole_keywordSyntax);
	public IConstructor layoutSyntax = $VF.constructor(SyntaxRole_layoutSyntax);
	public IConstructor illegalSyntax = $VF.constructor(SyntaxRole_illegalSyntax);

	/*************************************************************************/
	/*		Parse Trees														 */
	/*************************************************************************/

	// ---- Associativity -----------------------------------------------------

	// left
	public final Type Associativity_left = $TF.constructor($TS,  Associativity, "left");

	public IConstructor $left() { return $VF.constructor(Associativity_left); }

	// right
	public final Type Associativity_right = $TF.constructor($TS,  Associativity, "right");

	public IConstructor $right() { return $VF.constructor(Associativity_right); }

	// assoc
	public final Type Associativity_assoc = $TF.constructor($TS,  Associativity, "assoc");

	public IConstructor $assoc() { return $VF.constructor(Associativity_assoc); }

	// non-assoc
	public final Type Associativity_non_assoc = $TF.constructor($TS,  Associativity, "non_assoc");

	public IConstructor $non_assoc() { return $VF.constructor(Associativity_non_assoc); }

	// ---- Attr --------------------------------------------------------------

	// tag
	public final Type Attr_tag = $TF.constructor($TS,  Attr, "tag", $TF.valueType(), "tag");

	public IConstructor $tag(IValue tag) { return $VF.constructor(Attr_tag, tag); }

	// assoc
	public final Type Attr_assoc = $TF.constructor($TS,  Attr, "assoc", Associativity, "assoc");

	public IConstructor $assoc(IConstructor assoc) { return $VF.constructor(Attr_assoc, assoc); }

	// bracket
	public final Type Attr_bracket = $TF.constructor($TS,  Attr, "bracket");

	public IConstructor $bracket() { return $VF.constructor(Attr_bracket); }

	// ---- Tree --------------------------------------------------------------

	// appl
	public final Type Tree_appl = $TF.constructor($TS,  ATree, "appl",  AProduction, "aprod",  $TF.listType(ATree), "args");
	public final Type Tree_appl_loc = $TF.constructor($TS,  ATree, "appl",  AProduction, "aprod",  $TF.listType(ATree), "args", $TF.sourceLocationType(), "src");

	public IConstructor $appl(IConstructor aprod, IList args) { return $VF.constructor(Tree_appl, aprod, args); }
	public IConstructor $appl(IConstructor aprod, IList args, ISourceLocation src) { return $VF.constructor(Tree_appl_loc, aprod, args, src); }

	// cycle
	public final Type Tree_cycle = $TF.constructor($TS,  ATree, "cycle",  AType, "atype",  $TF.integerType(), "cyclelength");

	public IConstructor $cycle(IConstructor atype, IInteger cyclelength) { return $VF.constructor(Tree_cycle, atype, cyclelength);  }

	// amb
	public final Type Tree_amb = $TF.constructor($TS,  ATree, "amb",  $TF.setType(ATree), "alternatives");

	public IConstructor $amb(ISet alternatives) { return $VF.constructor(Tree_amb, alternatives);  }

	// char
	public final Type Tree_char = $TF.constructor($TS,  ATree, "char",  $TF.integerType(), "character");

	public IConstructor $char(IInteger character) { return $VF.constructor(Tree_char, character);  }

	// ---- AProduction -------------------------------------------------------

	// choice
	public final Type AProduction_achoice = $TF.constructor($TS,  AProduction, "achoice", AType, "def", $TF.setType(AProduction), "alternatives");

	public IConstructor $achoice(IConstructor def, ISet alternatives) { return $VF.constructor(AProduction_achoice, def, alternatives); }

	// prod
	public final Type AProduction_prod = $TF.constructor($TS,  AProduction, "prod", AType, "def", $TF.listType(AType), "atypes");
	public final Type AProduction_prod_attr = $TF.constructor($TS,  AProduction, "prod", AType, "def", $TF.listType(AType), "atypes", $TF.listType(Attr), "attributes");
	public final Type AProduction_prod_src = $TF.constructor($TS,  AProduction, "prod", AType, "def", $TF.listType(AType), "atypes", $TF.sourceLocationType(), "src");
	public final Type AProduction_prod_attr_src = $TF.constructor($TS,  AProduction, "prod", AType, "def", $TF.listType(AType), "atypes", $TF.listType(Attr), "attributes", $TF.sourceLocationType(), "src");

	public IConstructor $prod(IConstructor def, IList atypes) { return $VF.constructor(AProduction_prod, def, atypes); }
	public IConstructor $prod(IConstructor def, IList atypes, IConstructor attributes) { return $VF.constructor(AProduction_prod_attr, def, atypes, attributes); }
	public IConstructor $prod(IConstructor def, IList atypes, ISourceLocation src) { return $VF.constructor(AProduction_prod_src, def, atypes, src); }
	public IConstructor $prod(IConstructor def, IList atypes, IConstructor attributes, ISourceLocation src) { return $VF.constructor(AProduction_prod_attr_src, def, atypes, src); }

	// regular
	public final Type AProduction_regular = $TF.constructor($TS,  AProduction, "regular", AType, "def");

	public IConstructor $regular(IConstructor def) { return $VF.constructor(AProduction_regular, def); }

	//error
	public final Type AProduction_error = $TF.constructor($TS,  AProduction, "error", AProduction, "prod", $TF.integerType(), "dot");

	public IConstructor $error(IConstructor prod, IInteger dot) { return $VF.constructor(AProduction_error, prod, dot); }

	// skipped
	public final Type AProduction_skipped = $TF.constructor($TS,  AProduction, "skipped");

	public IConstructor $skipped() { return $VF.constructor(AProduction_skipped); }

	// priority
	public final Type AProduction_priority = $TF.constructor($TS,  AProduction, "priority", AType, "def", $TF.listType(AProduction), "choices");

	public IConstructor $priority(IConstructor def, IList choices) { return $VF.constructor(AProduction_priority, def, choices); }

	// associativity
	public final Type AProduction_associativity = $TF.constructor($TS,  AProduction, "associativity", AType, "def", Associativity, "assoc", $TF.setType(AProduction), "alternatives");

	public IConstructor $associativity(IConstructor def, IConstructor assoc, IList alternatives) { return $VF.constructor(AProduction_priority, def, assoc, alternatives); }

	// others
	public final Type AProduction_others = $TF.constructor($TS, AProduction, "others", AType, "def");

	public IConstructor $others(IConstructor def) { return $VF.constructor(AProduction_others, def); }

	// reference
	public final Type AProduction_reference = $TF.constructor($TS, AProduction, "reference", AType, "def", str, "cons");

	public IConstructor $reference(IConstructor def, IString cons) { return $VF.constructor(AProduction_reference, def, cons); }

	// ---- ACharRange --------------------------------------------------------

	public final Type CharRange_range = $TF.constructor($TS, ACharRange, "range", $TF.integerType(), "begin", $TF.integerType(), "end");

	public IConstructor $range(IInteger begin, IInteger end) { return $VF.constructor(CharRange_range, begin, end); }

	// ---- AType extensions for parse trees ----------------------------------

	// lit
	public final Type AType_alit = $TF.constructor($TS, AType, "alit", str, "string");

	public IConstructor $alit(IString string) {  return $VF.constructor(AType_alit, string); }

	// cilit
	public final Type AType_acilit = $TF.constructor($TS, AType, "acilit", str, "string");

	public IConstructor $acilit(IString string) {  return $VF.constructor(AType_acilit, string); }

	// char-class
	public final Type AType_char_class = $TF.constructor($TS, AType, "char_class", $TF.listType(ACharRange), "ranges");

	public IConstructor $char_class(IList ranges) {  return $VF.constructor(AType_char_class, ranges); }

	// empty
	public final Type AType_aempty = $TF.constructor($TS, AType, "aempty");

	public IConstructor $aempty() {  return $VF.constructor(AType_aempty); }

	// opt
	public final Type AType_opt = $TF.constructor($TS, AType, "opt", AType, "atype");

	public IConstructor $opt(IConstructor atype) {  return $VF.constructor(AType_opt, atype); }

	// iter
	public final Type AType_iter = $TF.constructor($TS, AType, "iter", AType, "atype");

	public IConstructor $iter(IConstructor atype) {  return $VF.constructor(AType_iter, atype); }

	// iter-star
	public final Type AType_iter_star = $TF.constructor($TS, AType, "iter_star", AType, "atype");

	public IConstructor $iter_star(IConstructor atype) {  return $VF.constructor(AType_iter_star, atype); }

	// iter-seps
	public final Type AType_iter_seps= $TF.constructor($TS, AType, "iter_seps", AType, "atype", $TF.listType(AType), "separators");

	public IConstructor $iter_seps(IConstructor atype, IList separators) {  return $VF.constructor(AType_iter_seps, atype, separators); }

	// iter-star-seps
	public final Type AType_iter_star_seps= $TF.constructor($TS, AType, "iter_star_seps", AType, "atype", $TF.listType(AType), "separators");

	public IConstructor $iter_star_seps(IConstructor atype, IList separators) {  return $VF.constructor(AType_iter_star_seps, atype, separators); }

	// alt
	public final Type AType_alt = $TF.constructor($TS, AType, "alt", AType, "atype", $TF.setType(AType), "alternatives");

	public IConstructor $alt(IConstructor atype, ISet alternatives) {  return $VF.constructor(AType_alt, atype, alternatives); }

	// seq
	public final Type AType_seq = $TF.constructor($TS, AType, "seq", AType, "atype", $TF.listType(AType), "atypes");

	public IConstructor $seq(IConstructor atype, IList atypes) {  return $VF.constructor(AType_seq, atype, atypes); }

	// start
	public final Type AType_start = $TF.constructor($TS, AType, "start", AType, "atype");

	public IConstructor $start(IConstructor atype) {  return $VF.constructor(AType_start, atype); }

	// ---- ACondition --------------------------------------------------------

	// follow
	public final Type ACondition_follow = $TF.constructor($TS, ACondition, "follow", AType, "atype");

	public IConstructor $follow(IConstructor atype) {  return $VF.constructor(ACondition_follow, atype); }

	// not_follow
	public final Type ACondition_not_follow = $TF.constructor($TS, ACondition, "not_follow", AType, "atype");

	public IConstructor $not_follow(IConstructor atype) {  
		return $VF.constructor(ACondition_not_follow, atype); 
	}

	// precede
	public final Type ACondition_precede = $TF.constructor($TS, ACondition, "precede", AType, "atype");

	public IConstructor $precede(IConstructor atype) {  return $VF.constructor(ACondition_precede, atype); }

	// not_precede
	public final Type ACondition_not_precede = $TF.constructor($TS, ACondition, "not_precede", AType, "atype");

	public IConstructor $not_precede(IConstructor atype) {  return $VF.constructor(ACondition_not_precede, atype); }

	// delete
	public final Type ACondition_delete = $TF.constructor($TS, ACondition, "delete", AType, "atype");

	public IConstructor $delete(IConstructor atype) {  return $VF.constructor(ACondition_delete, atype); }

	// at_column
	public final Type ACondition_at_column = $TF.constructor($TS, ACondition, "at_column", $TF.integerType(), "column");

	public IConstructor $at_column(IInteger column) {  return $VF.constructor(ACondition_at_column, column); }

	// begin_of_line
	public final Type ACondition_begin_of_line= $TF.constructor($TS, ACondition, "begin_of_line");

	public IConstructor $begin_of_line() {  return $VF.constructor(ACondition_begin_of_line); }

	// except
	public final Type ACondition_except = $TF.constructor($TS, ACondition, "except", str, "label");

	public IConstructor $except(IString label) {  return $VF.constructor(ACondition_except, label); }

}
