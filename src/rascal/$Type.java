package rascal;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import io.usethesource.vallang.*;
import io.usethesource.vallang.type.*;
import org.rascalmpl.runtime.*;
import org.rascalmpl.runtime.RascalExecutionContext;
import org.rascalmpl.runtime.function.*;
import org.rascalmpl.runtime.traverse.*;
import org.rascalmpl.runtime.utils.*;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw; 
import org.rascalmpl.runtime.RascalExecutionContext;
import org.rascalmpl.interpreter.control_exceptions.Filtered;
import org.rascalmpl.types.NonTerminalType;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.util.ExpiringFunctionResultCache;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;



@SuppressWarnings({"unused","unchecked","deprecation"})
public class $Type 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.$Type_$I {

    private final $Type_$I $me;
    private final IList $constants;
    
    
    public final rascal.$List M_List;

    
    final org.rascalmpl.library.Type $Type; // TODO: asBaseClassName will generate name collisions if there are more of the same name in different packages

    
    public final io.usethesource.vallang.type.Type $T11;	/*avalue(alabel="tag")*/
    public final io.usethesource.vallang.type.Type $T2;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T4;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T10;	/*aparameter("U",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T21;	/*aparameter("T",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T6;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T13;	/*astr(alabel="name")*/
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type Symbol_node_;	/*acons(aadt("Symbol",[],dataSyntax()),[],[],alabel="node")*/
    public final io.usethesource.vallang.type.Type Symbol_bag_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol")],[],alabel="bag")*/
    public final io.usethesource.vallang.type.Type Symbol_label_str_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[astr(alabel="name"),aadt("Symbol",[],dataSyntax(),alabel="symbol")],[],alabel="label")*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T14;	/*alist(aadt("Symbol",[],dataSyntax()),alabel="parameters")*/
    public final io.usethesource.vallang.type.Type Symbol_adt_str_list_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[astr(alabel="name"),alist(aadt("Symbol",[],dataSyntax()),alabel="parameters")],[],alabel="adt")*/
    public final io.usethesource.vallang.type.Type Symbol_var_func_Symbol_list_Symbol_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="ret"),alist(aadt("Symbol",[],dataSyntax()),alabel="parameters"),aadt("Symbol",[],dataSyntax(),alabel="varArg")],[],alabel="var-func")*/
    public final io.usethesource.vallang.type.Type Symbol_cons_Symbol_str_list_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="adt"),astr(alabel="name"),alist(aadt("Symbol",[],dataSyntax()),alabel="parameters")],[],alabel="cons")*/
    public final io.usethesource.vallang.type.Type Symbol_rat_;	/*acons(aadt("Symbol",[],dataSyntax()),[],[],alabel="rat")*/
    public final io.usethesource.vallang.type.Type $T17;	/*alist(aadt("Symbol",[],dataSyntax()),alabel="kwTypes")*/
    public final io.usethesource.vallang.type.Type Symbol_func_Symbol_list_Symbol_list_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="ret"),alist(aadt("Symbol",[],dataSyntax()),alabel="parameters"),alist(aadt("Symbol",[],dataSyntax()),alabel="kwTypes")],[],alabel="func")*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T19;	/*aset(aadt("Attr",[],dataSyntax()),alabel="attributes")*/
    public final io.usethesource.vallang.type.Type $T12;	/*alist(aadt("Symbol",[],dataSyntax()),alabel="symbols")*/
    public final io.usethesource.vallang.type.Type Symbol_lrel_list_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[alist(aadt("Symbol",[],dataSyntax()),alabel="symbols")],[],alabel="lrel")*/
    public final io.usethesource.vallang.type.Type Symbol_real_;	/*acons(aadt("Symbol",[],dataSyntax()),[],[],alabel="real")*/
    public final io.usethesource.vallang.type.Type $T7;	/*alist(avalue())*/
    public final io.usethesource.vallang.type.Type Symbol_set_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol")],[],alabel="set")*/
    public final io.usethesource.vallang.type.Type Symbol_void_;	/*acons(aadt("Symbol",[],dataSyntax()),[],[],alabel="void")*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type Symbol_tuple_list_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[alist(aadt("Symbol",[],dataSyntax()),alabel="symbols")],[],alabel="tuple")*/
    public final io.usethesource.vallang.type.Type Production_cons_Symbol_list_Symbol_list_Symbol_set_Attr;	/*acons(aadt("Production",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="def"),alist(aadt("Symbol",[],dataSyntax()),alabel="symbols"),alist(aadt("Symbol",[],dataSyntax()),alabel="kwTypes"),aset(aadt("Attr",[],dataSyntax()),alabel="attributes")],[],alabel="cons")*/
    public final io.usethesource.vallang.type.Type Symbol_loc_;	/*acons(aadt("Symbol",[],dataSyntax()),[],[],alabel="loc")*/
    public final io.usethesource.vallang.type.Type $T16;	/*areified(avalue(),alabel="to")*/
    public final io.usethesource.vallang.type.Type $T8;	/*amap(astr(),avalue())*/
    public final io.usethesource.vallang.type.Type $T1;	/*alist(astr())*/
    public final io.usethesource.vallang.type.Type Symbol_bool_;	/*acons(aadt("Symbol",[],dataSyntax()),[],[],alabel="bool")*/
    public final io.usethesource.vallang.type.Type Production_composition_Production_Production;	/*acons(aadt("Production",[],dataSyntax()),[aadt("Production",[],dataSyntax(),alabel="lhs"),aadt("Production",[],dataSyntax(),alabel="rhs")],[],alabel="composition")*/
    public final io.usethesource.vallang.type.Type $T0;	/*alist(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type Attr_tag_value;	/*acons(aadt("Attr",[],dataSyntax()),[avalue(alabel="tag")],[],alabel="tag")*/
    public final io.usethesource.vallang.type.Type Symbol_value_;	/*acons(aadt("Symbol",[],dataSyntax()),[],[],alabel="value")*/
    public final io.usethesource.vallang.type.Type Symbol_str_;	/*acons(aadt("Symbol",[],dataSyntax()),[],[],alabel="str")*/
    public final io.usethesource.vallang.type.Type Symbol_datetime_;	/*acons(aadt("Symbol",[],dataSyntax()),[],[],alabel="datetime")*/
    public final io.usethesource.vallang.type.Type Symbol_num_;	/*acons(aadt("Symbol",[],dataSyntax()),[],[],alabel="num")*/
    public final io.usethesource.vallang.type.Type $T15;	/*aset(aadt("Symbol",[],dataSyntax()),alabel="alternatives")*/
    public final io.usethesource.vallang.type.Type Symbol_overloaded_set_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[aset(aadt("Symbol",[],dataSyntax()),alabel="alternatives")],[],alabel="overloaded")*/
    public final io.usethesource.vallang.type.Type $T5;	/*areified(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type Symbol_reified_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol")],[],alabel="reified")*/
    public final io.usethesource.vallang.type.Type Symbol_alias_str_list_Symbol_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[astr(alabel="name"),alist(aadt("Symbol",[],dataSyntax()),alabel="parameters"),aadt("Symbol",[],dataSyntax(),alabel="aliased")],[],alabel="alias")*/
    public final io.usethesource.vallang.type.Type $T3;	/*aset(aadt("Production",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type Symbol_parameter_str_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[astr(alabel="name"),aadt("Symbol",[],dataSyntax(),alabel="bound")],[],alabel="parameter")*/
    public final io.usethesource.vallang.type.Type $T9;	/*areified(aparameter("U",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_Exception;	/*aadt("Exception",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type Symbol_map_Symbol_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="from"),aadt("Symbol",[],dataSyntax(),alabel="to")],[],alabel="map")*/
    public final io.usethesource.vallang.type.Type $T18;	/*aset(aadt("Production",[],dataSyntax()),alabel="alternatives")*/
    public final io.usethesource.vallang.type.Type Production_choice_Symbol_set_Production;	/*acons(aadt("Production",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="def"),aset(aadt("Production",[],dataSyntax()),alabel="alternatives")],[],alabel="choice")*/
    public final io.usethesource.vallang.type.Type Symbol_rel_list_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[alist(aadt("Symbol",[],dataSyntax()),alabel="symbols")],[],alabel="rel")*/
    public final io.usethesource.vallang.type.Type $T20;	/*abool()*/
    public final io.usethesource.vallang.type.Type Exception_typeCastException_Symbol_reified_value;	/*acons(aadt("Exception",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="from"),areified(avalue(),alabel="to")],[],alabel="typeCastException")*/
    public final io.usethesource.vallang.type.Type Symbol_int_;	/*acons(aadt("Symbol",[],dataSyntax()),[],[],alabel="int")*/
    public final io.usethesource.vallang.type.Type Symbol_list_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol")],[],alabel="list")*/

    public $Type(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Type(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Type_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.$Type.class, this);
       
       mstore.importModule(rascal.$List.class, rex, rascal.$List::new); 
       
       M_List = mstore.getModule(rascal.$List.class); 
       
                          
       
       $TS.importStore(M_List.$TS);
       
       $Type = $initLibrary("org.rascalmpl.library.Type"); 
    
       $constants = readBinaryConstantsFile(this.getClass(), "rascal//$Type.constants", 11, "657c830a85ece57cf5a3525f7fd4d29d");
       ADT_Symbol = $adt("Symbol");
       ADT_RuntimeException = $adt("RuntimeException");
       ADT_Attr = $adt("Attr");
       ADT_Production = $adt("Production");
       ADT_Exception = $adt("Exception");
       $T11 = $TF.valueType();
       $T2 = $TF.stringType();
       $T4 = $TF.valueType();
       $T10 = $TF.parameterType("U", $T4);
       $T21 = $TF.parameterType("T", $T4);
       $T6 = $TF.parameterType("T", $T4);
       $T13 = $TF.stringType();
       $T14 = $TF.listType(ADT_Symbol);
       $T17 = $TF.listType(ADT_Symbol);
       $T19 = $TF.setType(ADT_Attr);
       $T12 = $TF.listType(ADT_Symbol);
       $T7 = $TF.listType($T4);
       $T16 = $RTF.reifiedType($T4);
       $T8 = $TF.mapType($T2,$T4);
       $T1 = $TF.listType($T2);
       $T0 = $TF.listType(ADT_Symbol);
       $T15 = $TF.setType(ADT_Symbol);
       $T5 = $RTF.reifiedType($T6);
       $T3 = $TF.setType(ADT_Production);
       $T9 = $RTF.reifiedType($T10);
       $T18 = $TF.setType(ADT_Production);
       $T20 = $TF.boolType();
       Symbol_node_ = $TF.constructor($TS, ADT_Symbol, "node");
       Symbol_bag_Symbol = $TF.constructor($TS, ADT_Symbol, "bag", ADT_Symbol, "symbol");
       Symbol_label_str_Symbol = $TF.constructor($TS, ADT_Symbol, "label", $TF.stringType(), "name", ADT_Symbol, "symbol");
       Symbol_adt_str_list_Symbol = $TF.constructor($TS, ADT_Symbol, "adt", $TF.stringType(), "name", $TF.listType(ADT_Symbol), "parameters");
       Symbol_var_func_Symbol_list_Symbol_Symbol = $TF.constructor($TS, ADT_Symbol, "var-func", ADT_Symbol, "ret", $TF.listType(ADT_Symbol), "parameters", ADT_Symbol, "varArg");
       Symbol_cons_Symbol_str_list_Symbol = $TF.constructor($TS, ADT_Symbol, "cons", ADT_Symbol, "adt", $TF.stringType(), "name", $TF.listType(ADT_Symbol), "parameters");
       Symbol_rat_ = $TF.constructor($TS, ADT_Symbol, "rat");
       Symbol_func_Symbol_list_Symbol_list_Symbol = $TF.constructor($TS, ADT_Symbol, "func", ADT_Symbol, "ret", $TF.listType(ADT_Symbol), "parameters", $TF.listType(ADT_Symbol), "kwTypes");
       Symbol_lrel_list_Symbol = $TF.constructor($TS, ADT_Symbol, "lrel", $TF.listType(ADT_Symbol), "symbols");
       Symbol_real_ = $TF.constructor($TS, ADT_Symbol, "real");
       Symbol_set_Symbol = $TF.constructor($TS, ADT_Symbol, "set", ADT_Symbol, "symbol");
       Symbol_void_ = $TF.constructor($TS, ADT_Symbol, "void");
       Symbol_tuple_list_Symbol = $TF.constructor($TS, ADT_Symbol, "tuple", $TF.listType(ADT_Symbol), "symbols");
       Production_cons_Symbol_list_Symbol_list_Symbol_set_Attr = $TF.constructor($TS, ADT_Production, "cons", ADT_Symbol, "def", $TF.listType(ADT_Symbol), "symbols", $TF.listType(ADT_Symbol), "kwTypes", $TF.setType(ADT_Attr), "attributes");
       Symbol_loc_ = $TF.constructor($TS, ADT_Symbol, "loc");
       Symbol_bool_ = $TF.constructor($TS, ADT_Symbol, "bool");
       Production_composition_Production_Production = $TF.constructor($TS, ADT_Production, "composition", ADT_Production, "lhs", ADT_Production, "rhs");
       Attr_tag_value = $TF.constructor($TS, ADT_Attr, "tag", $TF.valueType(), "tag");
       Symbol_value_ = $TF.constructor($TS, ADT_Symbol, "value");
       Symbol_str_ = $TF.constructor($TS, ADT_Symbol, "str");
       Symbol_datetime_ = $TF.constructor($TS, ADT_Symbol, "datetime");
       Symbol_num_ = $TF.constructor($TS, ADT_Symbol, "num");
       Symbol_overloaded_set_Symbol = $TF.constructor($TS, ADT_Symbol, "overloaded", $TF.setType(ADT_Symbol), "alternatives");
       Symbol_reified_Symbol = $TF.constructor($TS, ADT_Symbol, "reified", ADT_Symbol, "symbol");
       Symbol_alias_str_list_Symbol_Symbol = $TF.constructor($TS, ADT_Symbol, "alias", $TF.stringType(), "name", $TF.listType(ADT_Symbol), "parameters", ADT_Symbol, "aliased");
       Symbol_parameter_str_Symbol = $TF.constructor($TS, ADT_Symbol, "parameter", $TF.stringType(), "name", ADT_Symbol, "bound");
       Symbol_map_Symbol_Symbol = $TF.constructor($TS, ADT_Symbol, "map", ADT_Symbol, "from", ADT_Symbol, "to");
       Production_choice_Symbol_set_Production = $TF.constructor($TS, ADT_Production, "choice", ADT_Symbol, "def", $TF.setType(ADT_Production), "alternatives");
       Symbol_rel_list_Symbol = $TF.constructor($TS, ADT_Symbol, "rel", $TF.listType(ADT_Symbol), "symbols");
       Exception_typeCastException_Symbol_reified_value = $TF.constructor($TS, ADT_Exception, "typeCastException", ADT_Symbol, "from", $RTF.reifiedType($T4), "to");
       Symbol_int_ = $TF.constructor($TS, ADT_Symbol, "int");
       Symbol_list_Symbol = $TF.constructor($TS, ADT_Symbol, "list", ADT_Symbol, "symbol");
    
       
       
    }
    public IBool isTypeVar(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isTypeVar$78fc26e12b32aede((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isTypeVar$301a952e924ee4a8((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isTypeVar$b0dd3673fa06bcfe((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isTypeVar$7fdb3c6deae5e954((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue glb(IValue $P0, IValue $P1){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_glb$34d371f0513c1ec9((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 26576112:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_glb$4b91e5fe473aa816((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$563c14f02706f88a((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1725888:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_glb$17ed4c750a842e8b((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$a9afab04303a8d6a((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$4c151198f5c36983((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$c70f2ec5079b5d5a((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$a5f417161c5ea6ff((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_glb$9ee1fb0fc2528775((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 97904160:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_glb$cad5f842cc182e58((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$b25c5c3f39fc09b1((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$cd7b70aadcf0e6f7((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 910096:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_glb$ccebb93560b76f62((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$a731272cac728f82((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 902344:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_glb$a786ee037405dc5c((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$525503476d5dd0c9((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$3f1318612901961d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$1d00df4abea8627d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$22a77fe40bb560cb((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$9665334317df7954((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$fedc2ca18d852381((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_glb$6917d863f1e00280((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 26641768:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_glb$0f0ddb52abd0ac6a((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$fc31ec41aa84d7a9((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$c86a7a40892f2e13((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$a57301fdb26a70d4((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$3b40dfcfaf8346f5((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$b721378e469ce285((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$fcc8ca1603beb4a9((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 778304:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_glb$5aaa99890ed611bb((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 100948096:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_glb$69c036b2f7ba2c93((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 112955840:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_glb$265e93665a676ba9((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$6a5d82307ef38ff1((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1542928:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_glb$e59f6ec3e8ac7cf8((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$0b0dd3255cd61470((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$e2846391aba6c513((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$fcb766b6fadeffe6((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$57382441fa985d45((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 885800512:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_glb$9e7e1f5985f9f398((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$a04284ca9b91ca7f((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$48b8563e75023e27((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$69e769596d4bd481((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_glb$ba92934d6828a4fc((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IValue)Type_glb$588ab965903d12a1((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
         $result = (IValue)Type_glb$c33fe84981451d5d((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_glb$bb211af2491da9e9((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_glb$4c1d7a7db4cf16ca((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_glb$c62adde378db5726((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_glb$44d70430e3ea2351((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_glb$2a7a0f345df74609((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_glb$1e653208ecb5fb70((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_glb$f4d1e05744c88722((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_glb$e78099bd23f8b441((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_glb$5a78c895f775bd16((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_glb$60eb504a9ebf23b3((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_glb$13f9a897070bdb41((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_glb$f5f3963b9ac36b44((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_glb$748cc45995ed4ab6((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_glb$062f850b66975974((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_glb$c7e9058fa842d39e((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IValue)Type_glb$c557bcbeba468980((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IList addLabels(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T1)){
         $result = (IList)Type_addLabels$981aeb1962053f6d((IList) $P0, (IList) $P1);
         if($result != null) return $result;
         $result = (IList)Type_addLabels$c61a5cc3caa0d718((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IValue head(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.head($P0);
    }
    public IBool isAliasType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isAliasType$53ed17cc6d4d56d5((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isAliasType$36bb377f97e90786((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isAliasType$be7b4838d41f7750((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isAliasType$a82f2a52c32629d2((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isStrType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isStrType$7c6935fdcbba3a91((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isStrType$f001e5ed63b40aa7((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isStrType$f6b0f7a14a810d8f((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isStrType$9ae2419b08ae933c((IConstructor) $P0);
         if($result != null) return $result;
         $result = (IBool)Type_isStrType$f3e2471bbdc6578c((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor choice(IValue $P0, IValue $P1){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type,$T3)){
         $result = (IConstructor)Type_choice$1fd88c645dec3500((IConstructor) $P0, (ISet) $P1);
         if($result != null) return $result;
         return $RVF.constructor(Production_choice_Symbol_set_Production, new IValue[]{(IConstructor) $P0, (ISet) $P1});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isValueType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isValueType$dca7aa346bf90886((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isValueType$b9ffdec5c297602d((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isValueType$e0359cce2680806c((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isValueType$667072f59b8f8f70((IConstructor) $P0);
         if($result != null) return $result;
         $result = (IBool)Type_isValueType$e6f62b5b7d9c7817((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isADTType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isADTType$c9e59ac203bf16c6((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isADTType$eec08a18faad2421((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isADTType$75f588280052e41a((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1542928:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isADTType$06ad40947bb0990a((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 112955840:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isADTType$55f1893a25b5971e((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isADTType$4079510b224fe1ba((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isListType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isListType$f9325150bcff8f64((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 26576112:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isListType$e2050780d70f05ef((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isListType$2cd097e45f390e11((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isListType$5cb20471614ceb14((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 26641768:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isListType$67c1630c1e94e46f((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isListType$a5f9e5ebaadf5ccd((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isRealType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isRealType$bf4dadc558e2bc2a((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isRealType$5af787b150624afb((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isRealType$4d3ef284eba47817((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isRealType$f338a01b49d8b0d2((IConstructor) $P0);
         if($result != null) return $result;
         $result = (IBool)Type_isRealType$ccca7a2c3ee9a4fb((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isNodeType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isNodeType$753d652f78ce3ee8((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isNodeType$fe2d6187af7e20d8((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isNodeType$cd583a32160e7d2e((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1542928:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isNodeType$70b01dc65abe027f((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isNodeType$4f701f28416ac047((IConstructor) $P0);
         if($result != null) return $result;
         $result = (IBool)Type_isNodeType$b82b31bdf0843130((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isReifiedType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isReifiedType$6fed314f55ec225d((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isReifiedType$2398657586b47cef((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isReifiedType$afaab65144832e90((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 112955840:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isReifiedType$a8d50b8b608f8ee7((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isReifiedType$f758db94422c9072((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isRelType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isRelType$58345a7801d3f289((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isRelType$dfe2996f326788f1((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 910096:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isRelType$e684041f960d6150((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 902344:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isRelType$1cd229b6628898e4((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isRelType$619927ec85f3de3a((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isRelType$4451272f25c97a41((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isConstructorType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isConstructorType$bfafd605f17a2265((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isConstructorType$76715e3a6e16c474((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 97904160:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isConstructorType$5f7682c23fcd1429((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isConstructorType$03b6fed0e3f2a6ac((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isConstructorType$7c73a110d1ea5afa((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor var_func(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type,$T0) && $isSubtypeOf($P2Type, ADT_Symbol)){
         $result = (IConstructor)Type_var_func$33d788c08f15290a((IConstructor) $P0, (IList) $P1, (IConstructor) $P2);
         if($result != null) return $result;
         return $RVF.constructor(Symbol_var_func_Symbol_list_Symbol_Symbol, new IValue[]{(IConstructor) $P0, (IList) $P1, (IConstructor) $P2});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IBool equivalent(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
         $result = (IBool)Type_equivalent$da65f34f54cbbcfd((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IList addParamLabels(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T1)){
         $result = (IList)Type_addParamLabels$9a2fcec6caf1a7af((IList) $P0, (IList) $P1);
         if($result != null) return $result;
         $result = (IList)Type_addParamLabels$237a32acd91d3f4b((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool keepParams(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
         $result = (IBool)Type_keepParams$932e8cdb74f21297((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isListRelType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isListRelType$8e3e16055e697805((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 26576112:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isListRelType$41e9de5fc098b33d((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isListRelType$4fa364f56f01bd93((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isListRelType$fc572333d7ccc035((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 26641768:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isListRelType$521530780a74b452((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isListRelType$fe978cfc20935991((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool eq(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T4) && $isSubtypeOf($P1Type,$T4)){
         $result = (IBool)Type_eq$3b65029c0d5a1b77((IValue) $P0, (IValue) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isMapType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isMapType$023e4a504202eeef((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1725888:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isMapType$12f2aa85890447c6((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isMapType$aa695167615ea00a((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isMapType$dd63ec350694a9b0((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isMapType$67db2d90e02d2133((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isBoolType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isBoolType$82e078ed93f04711((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isBoolType$8cefc437e5ae4c94((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isBoolType$37ece13e0d5add6a((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isBoolType$0e05363bfd11b324((IConstructor) $P0);
         if($result != null) return $result;
         $result = (IBool)Type_isBoolType$07e3c834d8dd35ef((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue make(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T5) && $isSubtypeOf($P1Type,$T2) && $isSubtypeOf($P2Type,$T7)){
         $result = (IValue)Type_make$c03dd112cd83002d((IConstructor) $P0, (IString) $P1, (IList) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IValue make(IValue $P0, IValue $P1, IValue $P2, IValue $P3){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       Type $P3Type = $P3.getType();
       if($isSubtypeOf($P0Type,$T5) && $isSubtypeOf($P1Type,$T2) && $isSubtypeOf($P2Type,$T7) && $isSubtypeOf($P3Type,$T8)){
         $result = (IValue)Type_make$ed639cfbd11999e2((IConstructor) $P0, (IString) $P1, (IList) $P2, (IMap) $P3);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2, $P3));
    }
    public IList tail(IValue $P0){ // Generated by Resolver
       return (IList) M_List.tail($P0);
    }
    public IBool isLocType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isLocType$9f0441d4fb931246((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isLocType$aee7bcb7f5d15058((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isLocType$59ad589102bf053b((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isLocType$3b8aa102e65cda31((IConstructor) $P0);
         if($result != null) return $result;
         $result = (IBool)Type_isLocType$7df812781da274e3((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ITuple headTail(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.headTail($P0);
    }
    public IConstructor typeOf(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T4)){
         $result = (IConstructor)Type_typeOf$6061dcc1215fd746((IValue) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool allLabeled(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IBool)Type_allLabeled$448fbafcf2280718((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isSetType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isSetType$5b2f15b95451429d((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isSetType$691733c8181a3052((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 910096:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isSetType$5940dec4cf1357e7((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 902344:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isSetType$6dbbe5f1ad04e52d((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isSetType$af2b3a70c68fa026((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isSetType$0a6211803a6f3b37((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isRatType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isRatType$dcf2381b59098cd9((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isRatType$b9693bea2192ed8c((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isRatType$8af97bb6a2786bed((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isRatType$45bbf043839d3d63((IConstructor) $P0);
         if($result != null) return $result;
         $result = (IBool)Type_isRatType$3364a9bd0fe98d3a((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList getLabels(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)Type_getLabels$75df4fafda7b0225((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList getParamLabels(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)Type_getParamLabels$d148bd4a03929246((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isNumType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isNumType$5f826fd8c150a884((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isNumType$8f39481fe207a2d9((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isNumType$cebed1c4e2f4e0c1((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isNumType$f905e6a61b4f9fe9((IConstructor) $P0);
         if($result != null) return $result;
         $result = (IBool)Type_isNumType$4a01c31ce8d55ae6((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList stripLabels(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)Type_stripLabels$476c8af6f8a4ff77((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isTupleType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isTupleType$af04c969df138641((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isTupleType$d75f7f1755750be1((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isTupleType$027f2dd0a4d10869((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 885800512:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isTupleType$529699f52c598fc9((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isTupleType$670c18b105a6fd46((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isBagType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isBagType$5bfea3bad135ca42((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isBagType$978835a2e17706d0((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isBagType$b98ae82cf86bb221((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 778304:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isBagType$16ada3424c9263d8((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isBagType$78a6a76069464291((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isVoidType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isVoidType$2ed30c48114cc0fc((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isVoidType$7407b4b3f99d147a((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isVoidType$baba77517aa47f53((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isVoidType$c37d3d034ac8acb0((IConstructor) $P0);
         if($result != null) return $result;
         $result = (IBool)Type_isVoidType$c29ea295639835d3((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue typeCast(IValue $P0, IValue $P1){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T5) && $isSubtypeOf($P1Type,$T4)){
         $result = (IValue)Type_typeCast$0e794e9add95c83b((IConstructor) $P0, (IValue) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IValue lub(IValue $P0, IValue $P1){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_lub$9915f83b2e9ee2f9((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 26576112:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_lub$be05e62f5f33e70a((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$42beb0d51d9c47f2((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1725888:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_lub$525d494ef32e780f((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$3f4d0193ec654be4((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$79c1b7694ffd9fd8((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$89bef2ae42badcac((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$8e262d13a8e22e5b((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_lub$dbf1dfef76adf321((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$e8aaf393ebf72b9b((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$11e6e64c52d2b7bc((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 97904160:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_lub$9c3d6ec5108762e5((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$db14cac8db6fcb97((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$ce98626caf2b01c0((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 910096:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_lub$4cd4964d29fd898b((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$9f0bb9bd89ef79f4((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 902344:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_lub$0dbf401cac8b4535((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$ec1dccaf2da25afa((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$734046054ee464ba((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$4b68476103d104fd((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$111a6137b422fe0a((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$458a19628ff06574((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$286a95d36c4d9271((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_lub$2bf17a517ba3b924((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 26641768:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_lub$7a4636c028a0a06c((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$cdf48c3a28135a1b((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$89bf39f91d0c3172((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$363046ae837d0ed7((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$57f39e76485c88cd((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$ea0cf15c1bb98cd7((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$1ce55522faa742da((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 778304:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_lub$fdf7de38b8e6e752((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 100948096:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_lub$21fcb18b79a5d185((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 112955840:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_lub$4c51769afaf2977a((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$ba70259261126d74((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1542928:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_lub$9f97945a73fdc528((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$a720eedc169409ce((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$771c2324f17afc4f((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$03e04b7cfdcf0b97((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$4d768567dbe9a5cf((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 885800512:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IValue)Type_lub$24cbcbc48e7f84f7((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$c4a2a53eec0c75c1((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$3cb3aedf8aed5365((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$a95613250b5d7982((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IValue)Type_lub$645ef43b093722bb((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
         $result = (IValue)Type_lub$f7177db64f5afb02((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$8b44730d15ad9495((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$02594451bbb24254((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$bc35bf8a90c0c581((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$ce2d305ea2a296ea((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$5cff2f0984f8ab98((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$7399cb4ee7561f75((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$126762fa2597c906((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$7dfd4aaa3c2fb078((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$e906ecba7e33c9ab((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$3baf3045e8079ba6((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$1f474fa5fa994fad((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$28f89ecd1e2960ee((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$578d8f8156ab8f3c((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$fe83795f0144eb99((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$d06daffd5ad013e1((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$2cd9a56d59418ef9((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$3503ff016335f16b((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$df08b24dcf17ae58((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$afcfe5adb1fb93eb((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IValue)Type_lub$d3a3e906a73734c7((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IValue)Type_lub$b4ef79728aa03e7e((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
         $result = (IValue)Type_lub$f318ce489488a1e6((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IValue)Type_lub$2ad0993f943ef630((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool comparable(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
         $result = (IBool)Type_comparable$f7b7abd45db5e5d1((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool subtype(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IBool)Type_subtype$162da85a0f5a9f0d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 26576112:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IBool)Type_subtype$258479665eae36af((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)Type_subtype$0462d461bde80a82((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1725888:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IBool)Type_subtype$f6957636a33615ae((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IBool)Type_subtype$b674428cffef84bc((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 97904160:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IBool)Type_subtype$98167e340333c9a5((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)Type_subtype$4fe5b133e2ee1de9((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 910096:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IBool)Type_subtype$ca59d9bf5276e15d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)Type_subtype$e77633ea9a4ac6a5((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 902344:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IBool)Type_subtype$21c6b8b775030d1d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)Type_subtype$98e19b11a09faf67((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IBool)Type_subtype$0862159b9fa78cf9((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 26641768:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IBool)Type_subtype$ab363c241c416a71((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)Type_subtype$4de9a977591be6e5((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 778304:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IBool)Type_subtype$23f59dc1171dc69d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 100948096:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IBool)Type_subtype$ddf53e134f4d5416((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 112955840:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IBool)Type_subtype$bc5943e83a6df899((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)Type_subtype$282ad33dd55efdcc((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1542928:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IBool)Type_subtype$5f5250bbf1aff423((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)Type_subtype$15cedff9916fdbee((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 885800512:
       		if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
       		  $result = (IBool)Type_subtype$44422dfea95218a8((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IBool)Type_subtype$e6962df5576407da((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
         $result = (IBool)Type_subtype$cfecefb3bc3fa773((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)Type_subtype$53c4de769757bddc((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)Type_subtype$2750c116f0b05084((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)Type_subtype$39fbab80e9db10e1((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)Type_subtype$3eada106dbc66d2d((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)Type_subtype$30215aaed6c33fd7((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)Type_subtype$1b2387a35f10c1e0((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)Type_subtype$80633493313ebd18((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)Type_subtype$3aa09e73e41fcf84((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T5) && $isSubtypeOf($P1Type,$T9)){
         $result = (IBool)Type_subtype$7b9c005ac35dd586((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Symbol)){
         $result = (IBool)Type_subtype$06d2c71d010480ef((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IBool)Type_subtype$812a7f34ff841fdb((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool noneLabeled(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IBool)Type_noneLabeled$59f0d0b15364377f((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isFunctionType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isFunctionType$25cdd570591a3b3f((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isFunctionType$d383dd66853ee549((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isFunctionType$cdb5ce9942d16bd2((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 100948096:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isFunctionType$670ad7351fb38cc0((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isFunctionType$50a9927a87b4bf31((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isIntType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isIntType$8b21ce695f297291((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isIntType$bad3a4974e63bce6((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isIntType$609e2442e96ec30b((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isIntType$ea27a84c58784f0e((IConstructor) $P0);
         if($result != null) return $result;
         $result = (IBool)Type_isIntType$51bf141928a07cf0((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isDateTimeType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isDateTimeType$db79d83909345377((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isDateTimeType$5e2f6f86adbeae05((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, ADT_Symbol)){
       		  $result = (IBool)Type_isDateTimeType$8f25f84f6afb443d((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, ADT_Symbol)){
         $result = (IBool)Type_isDateTimeType$bb2c79987fe265e6((IConstructor) $P0);
         if($result != null) return $result;
         $result = (IBool)Type_isDateTimeType$af85ab799baee4b4((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(3902,231,<119,0>,<121,57>) 
    public IConstructor Type_var_func$33d788c08f15290a(IConstructor ret_0, IList parameters_1, IConstructor varArg_2){ 
        
        
        return ((IConstructor)($RVF.constructor(Symbol_func_Symbol_list_Symbol_list_Symbol, new IValue[]{((IConstructor)ret_0), ((IList)($alist_add_elm(((IList)parameters_1),((IConstructor)($RVF.constructor(Symbol_list_Symbol, new IValue[]{((IConstructor)varArg_2)})))))), ((IList)$constants.get(0)/*[]*/)})));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(4137,995,<125,0>,<154,1>) 
    public IConstructor Type_choice$1fd88c645dec3500(IConstructor s_0, ISet choices_1){ 
        
        
        /*muExists*/IF3: 
            do {
                IBool $done3 = (IBool)(((IBool)$constants.get(1)/*true*/));
                /*muExists*/$ANY4_GEN4422_CONS_choice: 
                    do {
                        $ANY4_GEN4422:
                        for(IValue $elem5_for : ((ISet)choices_1)){
                            IConstructor $elem5 = (IConstructor) $elem5_for;
                            if($has_type_and_arity($elem5, Production_choice_Symbol_set_Production, 2)){
                               IValue $arg0_7 = (IValue)($aadt_subscript_int(((IConstructor)($elem5)),0));
                               if($isComparable($arg0_7.getType(), ADT_Symbol)){
                                  IValue $arg1_6 = (IValue)($aadt_subscript_int(((IConstructor)($elem5)),1));
                                  if($isComparable($arg1_6.getType(), $T3)){
                                     $done3 = ((IBool)$constants.get(2)/*false*/);
                                     break $ANY4_GEN4422_CONS_choice; // muSucceed
                                  } else {
                                     continue $ANY4_GEN4422;
                                  }
                               } else {
                                  continue $ANY4_GEN4422;
                               }
                            } else {
                               continue $ANY4_GEN4422;
                            }
                        }
                        
                                    
                    } while(false);
                if((((IBool)($done3))).getValue()){
                  return null;
                }
        
            } while(false);
        IBool changed_2 = ((IBool)$constants.get(2)/*false*/);
        ISet new_choices_3 = ((ISet)$constants.get(3)/*{}*/);
        /*muExists*/FOR0: 
            do {
                FOR0_GEN4799:
                for(IValue $elem2_for : ((ISet)choices_1)){
                    IConstructor $elem2 = (IConstructor) $elem2_for;
                    IConstructor ch_4 = null;
                    if($has_type_and_arity($elem2, Production_choice_Symbol_set_Production, 2)){
                       IValue $arg0_1 = (IValue)($aadt_subscript_int(((IConstructor)($elem2)),0));
                       if($isComparable($arg0_1.getType(), ADT_Symbol)){
                          IValue $arg1_0 = (IValue)($aadt_subscript_int(((IConstructor)($elem2)),1));
                          if($isComparable($arg1_0.getType(), $T3)){
                             ISet b_5 = null;
                             changed_2 = ((IBool)$constants.get(1)/*true*/);
                             new_choices_3 = ((ISet)($aset_add_aset(((ISet)new_choices_3),((ISet)($arg1_0)))));
                          
                          } else {
                             new_choices_3 = ((ISet)($aset_add_elm(((ISet)new_choices_3),((IConstructor)($elem2)))));
                          
                          }
                       } else {
                          new_choices_3 = ((ISet)($aset_add_elm(((ISet)new_choices_3),((IConstructor)($elem2)))));
                       
                       }
                    } else {
                       new_choices_3 = ((ISet)($aset_add_elm(((ISet)new_choices_3),((IConstructor)($elem2)))));
                    
                    }
                }
                continue FOR0;
                            
            } while(false);
        /* void:  muCon([]) */if((((IBool)changed_2)).getValue()){
           return ((IConstructor)($me.choice(((IConstructor)s_0), ((ISet)new_choices_3))));
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(5353,482,<162,0>,<168,74>) 
    public IBool Type_subtype$7b9c005ac35dd586(IConstructor t_0, IConstructor u_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T5.match(t_0.getType(), $typeBindings)){
           if($T9.match(u_1.getType(), $typeBindings)){
              final IBool $result8 = ((IBool)($me.subtype(((IValue)(((IConstructor)($areified_get_field(t_0, "symbol"))))), ((IValue)(((IConstructor)($areified_get_field(u_1, "symbol"))))))));
              if($T20.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result8.getType(),$T20)){
                 return ((IBool)($result8));
              
              } else {
                 return ((IBool)$constants.get(2)/*false*/);
              
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(5837,136,<170,0>,<171,40>) 
    public IBool Type_subtype$cfecefb3bc3fa773(IConstructor s_0, IConstructor s){ 
        
        
        if((s_0 != null)){
           if(s_0.match(s)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           s_0 = ((IConstructor)s);
           return ((IBool)$constants.get(1)/*true*/);
        
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(5974,56,<172,0>,<172,56>) 
    public IBool Type_subtype$06d2c71d010480ef(IConstructor s_0, IConstructor t_1){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(6032,55,<174,0>,<174,55>) 
    public IBool Type_subtype$53c4de769757bddc(IConstructor $__0, IConstructor $1){ 
        
        
        if($has_type_and_arity($1, Symbol_value_, 0)){
           return ((IBool)$constants.get(1)/*true*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(6088,54,<175,0>,<175,54>) 
    public IBool Type_subtype$2750c116f0b05084(IConstructor $0, IConstructor $__1){ 
        
        
        if($has_type_and_arity($0, Symbol_void_, 0)){
           return ((IBool)$constants.get(1)/*true*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(6143,74,<176,0>,<176,74>) 
    public IBool Type_subtype$98167e340333c9a5(IConstructor $0, IConstructor a){ 
        
        
        if($has_type_and_arity($0, Symbol_cons_Symbol_str_list_Symbol, 3)){
           IValue $arg0_11 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_11.getType(), ADT_Symbol)){
              IConstructor a_0 = null;
              IValue $arg1_10 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_10.getType(), $T4)){
                 IValue $arg2_9 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_9.getType(), $T0)){
                    if(($arg0_11 != null)){
                       if($arg0_11.match($arg0_11)){
                          return ((IBool)$constants.get(1)/*true*/);
                       
                       } else {
                          return null;
                       }
                    } else {
                       $arg0_11 = ((IValue)($arg0_11));
                       return ((IBool)$constants.get(1)/*true*/);
                    
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(6218,128,<177,0>,<177,128>) 
    public IBool Type_subtype$4fe5b133e2ee1de9(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_cons_Symbol_str_list_Symbol, 3)){
           IValue $arg0_17 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_17.getType(), ADT_Symbol)){
              IConstructor a_0 = null;
              IValue $arg1_16 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_16.getType(), $T13)){
                 IString name_1 = null;
                 IValue $arg2_15 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_15.getType(), $T0)){
                    IList ap_2 = null;
                    if($has_type_and_arity($1, Symbol_cons_Symbol_str_list_Symbol, 3)){
                       IValue $arg0_14 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                       if($isComparable($arg0_14.getType(), ADT_Symbol)){
                          if(($arg0_17 != null)){
                             if($arg0_17.match($arg0_14)){
                                IValue $arg1_13 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                                if($isComparable($arg1_13.getType(), $T13)){
                                   if(($arg1_16 != null)){
                                      if($arg1_16.match($arg1_13)){
                                         IValue $arg2_12 = (IValue)($aadt_subscript_int(((IConstructor)$1),2));
                                         if($isComparable($arg2_12.getType(), $T0)){
                                            IList bp_3 = null;
                                            return ((IBool)($me.subtype(((IValue)($arg2_15)), ((IValue)($arg2_12)))));
                                         
                                         } else {
                                            return null;
                                         }
                                      } else {
                                         return null;
                                      }
                                   } else {
                                      $arg1_16 = ((IValue)($arg1_13));
                                      IValue $arg2_12 = (IValue)($aadt_subscript_int(((IConstructor)$1),2));
                                      if($isComparable($arg2_12.getType(), $T0)){
                                         IList bp_3 = null;
                                         return ((IBool)($me.subtype(((IValue)($arg2_15)), ((IValue)($arg2_12)))));
                                      
                                      } else {
                                         return null;
                                      }
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             $arg0_17 = ((IValue)($arg0_14));
                             IValue $arg1_13 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                             if($isComparable($arg1_13.getType(), $T13)){
                                if(($arg1_16 != null)){
                                   if($arg1_16.match($arg1_13)){
                                      IValue $arg2_12 = (IValue)($aadt_subscript_int(((IConstructor)$1),2));
                                      if($isComparable($arg2_12.getType(), $T0)){
                                         IList bp_3 = null;
                                         return ((IBool)($me.subtype(((IValue)($arg2_15)), ((IValue)($arg2_12)))));
                                      
                                      } else {
                                         return null;
                                      }
                                   } else {
                                      return null;
                                   }
                                } else {
                                   $arg1_16 = ((IValue)($arg1_13));
                                   IValue $arg2_12 = (IValue)($aadt_subscript_int(((IConstructor)$1),2));
                                   if($isComparable($arg2_12.getType(), $T0)){
                                      IList bp_3 = null;
                                      return ((IBool)($me.subtype(((IValue)($arg2_15)), ((IValue)($arg2_12)))));
                                   
                                   } else {
                                      return null;
                                   }
                                }
                             } else {
                                return null;
                             }
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(6347,81,<178,0>,<178,81>) 
    public IBool Type_subtype$5f5250bbf1aff423(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_adt_str_list_Symbol, 2)){
           IValue $arg0_19 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_19.getType(), $T2)){
              IValue $arg1_18 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_18.getType(), $T0)){
                 if($has_type_and_arity($1, Symbol_node_, 0)){
                    return ((IBool)$constants.get(1)/*true*/);
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(6429,106,<179,0>,<179,106>) 
    public IBool Type_subtype$15cedff9916fdbee(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_adt_str_list_Symbol, 2)){
           IValue $arg0_23 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_23.getType(), $T2)){
              IString n_0 = null;
              IValue $arg1_22 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_22.getType(), $T0)){
                 IList l_1 = null;
                 if($has_type_and_arity($1, Symbol_adt_str_list_Symbol, 2)){
                    IValue $arg0_21 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                    if($isComparable($arg0_21.getType(), $T2)){
                       if(($arg0_23 != null)){
                          if($arg0_23.match($arg0_21)){
                             IValue $arg1_20 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                             if($isComparable($arg1_20.getType(), $T0)){
                                IList r_2 = null;
                                return ((IBool)($me.subtype(((IValue)($arg1_22)), ((IValue)($arg1_20)))));
                             
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          $arg0_23 = ((IValue)($arg0_21));
                          IValue $arg1_20 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                          if($isComparable($arg1_20.getType(), $T0)){
                             IList r_2 = null;
                             return ((IBool)($me.subtype(((IValue)($arg1_22)), ((IValue)($arg1_20)))));
                          
                          } else {
                             return null;
                          }
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(6536,107,<180,0>,<180,107>) 
    public IBool Type_subtype$0862159b9fa78cf9(IConstructor $0, IConstructor r_1){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_26 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_26.getType(), $T2)){
              IValue $arg1_25 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_25.getType(), $T0)){
                 IValue $arg2_24 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_24.getType(), ADT_Symbol)){
                    IConstructor aliased_0 = null;
                    return ((IBool)($me.subtype(((IValue)($arg2_24)), ((IValue)r_1))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(6644,99,<181,0>,<181,99>) 
    public IBool Type_subtype$39fbab80e9db10e1(IConstructor l_0, IConstructor $1){ 
        
        
        if($has_type_and_arity($1, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_29 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
           if($isComparable($arg0_29.getType(), $T2)){
              IValue $arg1_28 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
              if($isComparable($arg1_28.getType(), $T0)){
                 IValue $arg2_27 = (IValue)($aadt_subscript_int(((IConstructor)$1),2));
                 if($isComparable($arg2_27.getType(), ADT_Symbol)){
                    IConstructor aliased_1 = null;
                    return ((IBool)($me.subtype(((IValue)l_0), ((IValue)($arg2_27)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(6744,59,<182,0>,<182,59>) 
    public IBool Type_subtype$3eada106dbc66d2d(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_int_, 0)){
           if($has_type_and_arity($1, Symbol_num_, 0)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(6804,59,<183,0>,<183,59>) 
    public IBool Type_subtype$30215aaed6c33fd7(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rat_, 0)){
           if($has_type_and_arity($1, Symbol_num_, 0)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(6864,60,<184,0>,<184,60>) 
    public IBool Type_subtype$1b2387a35f10c1e0(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_real_, 0)){
           if($has_type_and_arity($1, Symbol_num_, 0)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(6925,92,<185,0>,<185,92>) 
    public IBool Type_subtype$44422dfea95218a8(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_tuple_list_Symbol, 1)){
           IValue $arg0_31 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_31.getType(), $T0)){
              IList l_0 = null;
              if($has_type_and_arity($1, Symbol_tuple_list_Symbol, 1)){
                 IValue $arg0_30 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_30.getType(), $T0)){
                    IList r_1 = null;
                    return ((IBool)($me.subtype(((IValue)($arg0_31)), ((IValue)($arg0_30)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(7036,86,<188,0>,<188,86>) 
    public IBool Type_subtype$258479665eae36af(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_list_Symbol, 1)){
           IValue $arg0_33 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_33.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_list_Symbol, 1)){
                 IValue $arg0_32 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_32.getType(), ADT_Symbol)){
                    IConstructor t_1 = null;
                    return ((IBool)($me.subtype(((IValue)($arg0_33)), ((IValue)($arg0_32)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(7124,98,<189,0>,<189,98>) 
    public IBool Type_subtype$ab363c241c416a71(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_35 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_35.getType(), $T0)){
              IList l_0 = null;
              if($has_type_and_arity($1, Symbol_lrel_list_Symbol, 1)){
                 IValue $arg0_34 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_34.getType(), $T0)){
                    IList r_1 = null;
                    return ((IBool)($me.subtype(((IValue)($arg0_35)), ((IValue)($arg0_34)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(7479,108,<195,0>,<195,108>) 
    public IBool Type_subtype$0462d461bde80a82(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_list_Symbol, 1)){
           IValue $arg0_37 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_37.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_lrel_list_Symbol, 1)){
                 IValue $arg0_36 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_36.getType(), $T0)){
                    IList r_1 = null;
                    return ((IBool)($me.subtype(((IValue)($arg0_37)), ((IValue)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($arg0_36))}))))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(7588,100,<196,0>,<196,100>) 
    public IBool Type_subtype$4de9a977591be6e5(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_39 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_39.getType(), $T0)){
              IList l_0 = null;
              if($has_type_and_arity($1, Symbol_list_Symbol, 1)){
                 IValue $arg0_38 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_38.getType(), ADT_Symbol)){
                    IConstructor r_1 = null;
                    return ((IBool)($me.subtype(((IValue)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($arg0_39))}))), ((IValue)($arg0_38)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(7705,84,<199,0>,<199,84>) 
    public IBool Type_subtype$ca59d9bf5276e15d(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_set_Symbol, 1)){
           IValue $arg0_41 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_41.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_set_Symbol, 1)){
                 IValue $arg0_40 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_40.getType(), ADT_Symbol)){
                    IConstructor t_1 = null;
                    return ((IBool)($me.subtype(((IValue)($arg0_41)), ((IValue)($arg0_40)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(7790,96,<200,0>,<200,96>) 
    public IBool Type_subtype$21c6b8b775030d1d(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_43 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_43.getType(), $T0)){
              IList l_0 = null;
              if($has_type_and_arity($1, Symbol_rel_list_Symbol, 1)){
                 IValue $arg0_42 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_42.getType(), $T0)){
                    IList r_1 = null;
                    return ((IBool)($me.subtype(((IValue)($arg0_43)), ((IValue)($arg0_42)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(8138,106,<206,0>,<206,106>) 
    public IBool Type_subtype$e77633ea9a4ac6a5(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_set_Symbol, 1)){
           IValue $arg0_45 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_45.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_rel_list_Symbol, 1)){
                 IValue $arg0_44 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_44.getType(), $T0)){
                    IList r_1 = null;
                    return ((IBool)($me.subtype(((IValue)($arg0_45)), ((IValue)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($arg0_44))}))))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(8245,106,<207,0>,<207,106>) 
    public IBool Type_subtype$98e19b11a09faf67(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_47 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_47.getType(), $T0)){
              IList l_0 = null;
              if($has_type_and_arity($1, Symbol_set_Symbol, 1)){
                 IValue $arg0_46 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_46.getType(), ADT_Symbol)){
                    IConstructor r_1 = null;
                    return ((IBool)($me.subtype(((IValue)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($arg0_47))}))), ((IValue)($arg0_46)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(8353,84,<209,0>,<209,84>) 
    public IBool Type_subtype$23f59dc1171dc69d(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_bag_Symbol, 1)){
           IValue $arg0_49 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_49.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_bag_Symbol, 1)){
                 IValue $arg0_48 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_48.getType(), ADT_Symbol)){
                    IConstructor t_1 = null;
                    return ((IBool)($me.subtype(((IValue)($arg0_49)), ((IValue)($arg0_48)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(8440,145,<210,0>,<210,145>) 
    public IBool Type_subtype$f6957636a33615ae(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_map_Symbol_Symbol, 2)){
           IValue $arg0_54 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_54.getType(), ADT_Symbol)){
              IConstructor from1_0 = null;
              IValue $arg1_53 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_53.getType(), ADT_Symbol)){
                 IConstructor to1_1 = null;
                 if($has_type_and_arity($1, Symbol_map_Symbol_Symbol, 2)){
                    IValue $arg0_52 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                    if($isComparable($arg0_52.getType(), ADT_Symbol)){
                       IConstructor from2_2 = null;
                       IValue $arg1_51 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                       if($isComparable($arg1_51.getType(), ADT_Symbol)){
                          IConstructor to2_3 = null;
                          if((((IBool)($me.subtype(((IValue)($arg0_54)), ((IValue)($arg0_52)))))).getValue()){
                             return ((IBool)($me.subtype(((IValue)($arg1_53)), ((IValue)($arg1_51)))));
                          
                          } else {
                             return ((IBool)$constants.get(2)/*false*/);
                          
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(8674,179,<212,0>,<212,179>) 
    public IBool Type_subtype$ddf53e134f4d5416(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_func_Symbol_list_Symbol_list_Symbol, 3)){
           IValue $arg0_61 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_61.getType(), ADT_Symbol)){
              IConstructor r1_0 = null;
              IValue $arg1_60 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_60.getType(), $T0)){
                 IList p1_1 = null;
                 IValue $arg2_59 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_59.getType(), $T0)){
                    IList kw1_2 = null;
                    if($has_type_and_arity($1, Symbol_func_Symbol_list_Symbol_list_Symbol, 3)){
                       IValue $arg0_58 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                       if($isComparable($arg0_58.getType(), ADT_Symbol)){
                          IConstructor r2_3 = null;
                          IValue $arg1_57 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                          if($isComparable($arg1_57.getType(), $T0)){
                             IList p2_4 = null;
                             IValue $arg2_56 = (IValue)($aadt_subscript_int(((IConstructor)$1),2));
                             if($isComparable($arg2_56.getType(), $T0)){
                                IList kw2_5 = null;
                                if((((IBool)($me.subtype(((IValue)($arg0_61)), ((IValue)($arg0_58)))))).getValue()){
                                   return ((IBool)($me.subtype(((IValue)($arg1_57)), ((IValue)($arg1_60)))));
                                
                                } else {
                                   return ((IBool)$constants.get(2)/*false*/);
                                
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(8855,91,<213,0>,<213,91>) 
    public IBool Type_subtype$b674428cffef84bc(IConstructor $0, IConstructor r_1){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_63 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_63.getType(), $T2)){
              IValue $arg1_62 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_62.getType(), ADT_Symbol)){
                 IConstructor bound_0 = null;
                 return ((IBool)($me.subtype(((IValue)($arg1_62)), ((IValue)r_1))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(8947,91,<214,0>,<214,91>) 
    public IBool Type_subtype$80633493313ebd18(IConstructor l_0, IConstructor $1){ 
        
        
        if($has_type_and_arity($1, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_65 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
           if($isComparable($arg0_65.getType(), $T2)){
              IValue $arg1_64 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
              if($isComparable($arg1_64.getType(), ADT_Symbol)){
                 IConstructor bound_1 = null;
                 return ((IBool)($me.subtype(((IValue)l_0), ((IValue)($arg1_64)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(9039,78,<215,0>,<215,78>) 
    public IBool Type_subtype$162da85a0f5a9f0d(IConstructor $0, IConstructor t_1){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_67 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_67.getType(), $T2)){
              IValue $arg1_66 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_66.getType(), ADT_Symbol)){
                 IConstructor s_0 = null;
                 return ((IBool)($me.subtype(((IValue)($arg1_66)), ((IValue)t_1))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(9118,78,<216,0>,<216,78>) 
    public IBool Type_subtype$3aa09e73e41fcf84(IConstructor s_0, IConstructor $1){ 
        
        
        if($has_type_and_arity($1, Symbol_label_str_Symbol, 2)){
           IValue $arg0_69 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
           if($isComparable($arg0_69.getType(), $T2)){
              IValue $arg1_68 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
              if($isComparable($arg1_68.getType(), ADT_Symbol)){
                 IConstructor t_1 = null;
                 return ((IBool)($me.subtype(((IValue)s_0), ((IValue)($arg1_68)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(9197,91,<217,0>,<217,91>) 
    public IBool Type_subtype$bc5943e83a6df899(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_reified_Symbol, 1)){
           IValue $arg0_71 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_71.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_reified_Symbol, 1)){
                 IValue $arg0_70 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_70.getType(), ADT_Symbol)){
                    IConstructor t_1 = null;
                    return ((IBool)($me.subtype(((IValue)($arg0_71)), ((IValue)($arg0_70)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(9289,72,<218,0>,<218,72>) 
    public IBool Type_subtype$282ad33dd55efdcc(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_reified_Symbol, 1)){
           IValue $arg0_72 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_72.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_node_, 0)){
                 return ((IBool)$constants.get(1)/*true*/);
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(9362,133,<219,0>,<219,133>) 
    public IBool Type_subtype$e6962df5576407da(IList l_0, IList r_1){ 
        
        
        if((((IBool)($equal(((IInteger)(M_List.size(((IList)l_0)))), ((IInteger)(M_List.size(((IList)r_1)))))))).getValue()){
           if((((IBool)($aint_lessequal_aint(((IInteger)(M_List.size(((IList)l_0)))),((IInteger)$constants.get(4)/*0*/)).not()))).getValue()){
              IBool $done74 = (IBool)(((IBool)$constants.get(1)/*true*/));
              $ALL75_GEN9420:
              for(IValue $elem76_for : ((IList)(M_List.index(((IList)l_0))))){
                  IInteger $elem76 = (IInteger) $elem76_for;
                  IInteger i_2 = null;
                  if((((IBool)($me.subtype(((IValue)($alist_subscript_int(((IList)l_0),((IInteger)($elem76)).intValue()))), ((IValue)($alist_subscript_int(((IList)r_1),((IInteger)($elem76)).intValue()))))))).getValue()){
                    continue $ALL75_GEN9420;
                  
                  } else {
                    $done74 = ((IBool)$constants.get(2)/*false*/);
                    break $ALL75_GEN9420; // muBreak
                  
                  }
              
              }
              
                          return ((IBool)($done74));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(9496,91,<220,0>,<220,91>) 
    public IBool Type_subtype$812a7f34ff841fdb(IList l_0, IList r_1){ 
        
        
        if((((IBool)($equal(((IInteger)(M_List.size(((IList)l_0)))), ((IInteger)$constants.get(4)/*0*/))))).getValue()){
           return ((IBool)($equal(((IInteger)(M_List.size(((IList)r_1)))), ((IInteger)$constants.get(4)/*0*/))));
        
        } else {
           return ((IBool)$constants.get(2)/*false*/);
        
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(11180,171,<256,0>,<257,74>) 
    public IBool Type_comparable$f7b7abd45db5e5d1(IConstructor s_0, IConstructor t_1){ 
        
        
        if((((IBool)($me.subtype(((IValue)s_0), ((IValue)t_1))))).getValue()){
           return ((IBool)$constants.get(1)/*true*/);
        
        } else {
           return ((IBool)($me.subtype(((IValue)t_1), ((IValue)s_0))));
        
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(11354,163,<259,0>,<260,74>) 
    public IBool Type_equivalent$da65f34f54cbbcfd(IConstructor s_0, IConstructor t_1){ 
        
        
        if((((IBool)($me.subtype(((IValue)s_0), ((IValue)t_1))))).getValue()){
           return ((IBool)($me.subtype(((IValue)t_1), ((IValue)s_0))));
        
        } else {
           return ((IBool)$constants.get(2)/*false*/);
        
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(11520,558,<263,0>,<279,38>) 
    public IBool Type_eq$3b65029c0d5a1b77(IValue x_0, IValue y_1){ 
        
        
        return ((IBool)((IBool)$Type.eq(x_0, y_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(12080,243,<281,0>,<285,35>) 
    public IConstructor Type_lub$f7177db64f5afb02(IConstructor s_0, IConstructor s){ 
        
        
        if((s_0 != null)){
           if(s_0.match(s)){
              return ((IConstructor)s_0);
           
           } else {
              return null;
           }
        } else {
           s_0 = ((IConstructor)s);
           return ((IConstructor)s_0);
        
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(12324,57,<286,0>,<286,57>) 
    public IConstructor Type_lub$f318ce489488a1e6(IConstructor s_0, IConstructor t_1){ 
        
        
        return ((IConstructor)$constants.get(5)/*value()*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(12383,65,<288,0>,<288,65>) 
    public IConstructor Type_lub$8b44730d15ad9495(IConstructor $0, IConstructor t_0){ 
        
        
        if($has_type_and_arity($0, Symbol_value_, 0)){
           return ((IConstructor)($RVF.constructor(Symbol_value_, new IValue[]{})));
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(12449,65,<289,0>,<289,65>) 
    public IConstructor Type_lub$02594451bbb24254(IConstructor s_0, IConstructor $1){ 
        
        
        if($has_type_and_arity($1, Symbol_value_, 0)){
           return ((IConstructor)($RVF.constructor(Symbol_value_, new IValue[]{})));
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(12515,49,<290,0>,<290,49>) 
    public IConstructor Type_lub$bc35bf8a90c0c581(IConstructor $0, IConstructor t_0){ 
        
        
        if($has_type_and_arity($0, Symbol_void_, 0)){
           return ((IConstructor)t_0);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(12565,49,<291,0>,<291,49>) 
    public IConstructor Type_lub$ce2d305ea2a296ea(IConstructor s_0, IConstructor $1){ 
        
        
        if($has_type_and_arity($1, Symbol_void_, 0)){
           return ((IConstructor)s_0);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(12615,67,<292,0>,<292,67>) 
    public IConstructor Type_lub$5cff2f0984f8ab98(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_int_, 0)){
           if($has_type_and_arity($1, Symbol_num_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_num_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(12683,68,<293,0>,<293,68>) 
    public IConstructor Type_lub$7399cb4ee7561f75(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_int_, 0)){
           if($has_type_and_arity($1, Symbol_real_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_num_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(12752,67,<294,0>,<294,67>) 
    public IConstructor Type_lub$126762fa2597c906(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_int_, 0)){
           if($has_type_and_arity($1, Symbol_rat_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_num_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(12820,67,<295,0>,<295,67>) 
    public IConstructor Type_lub$7dfd4aaa3c2fb078(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rat_, 0)){
           if($has_type_and_arity($1, Symbol_num_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_num_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(12888,68,<296,0>,<296,68>) 
    public IConstructor Type_lub$e906ecba7e33c9ab(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rat_, 0)){
           if($has_type_and_arity($1, Symbol_real_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_num_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(12957,67,<297,0>,<297,67>) 
    public IConstructor Type_lub$3baf3045e8079ba6(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rat_, 0)){
           if($has_type_and_arity($1, Symbol_int_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_num_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(13025,68,<298,0>,<298,68>) 
    public IConstructor Type_lub$1f474fa5fa994fad(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_real_, 0)){
           if($has_type_and_arity($1, Symbol_num_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_num_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(13094,68,<299,0>,<299,68>) 
    public IConstructor Type_lub$28f89ecd1e2960ee(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_real_, 0)){
           if($has_type_and_arity($1, Symbol_int_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_num_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(13163,68,<300,0>,<300,68>) 
    public IConstructor Type_lub$578d8f8156ab8f3c(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_real_, 0)){
           if($has_type_and_arity($1, Symbol_rat_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_num_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(13232,67,<301,0>,<301,67>) 
    public IConstructor Type_lub$fe83795f0144eb99(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_num_, 0)){
           if($has_type_and_arity($1, Symbol_int_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_num_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(13300,68,<302,0>,<302,68>) 
    public IConstructor Type_lub$d06daffd5ad013e1(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_num_, 0)){
           if($has_type_and_arity($1, Symbol_real_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_num_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(13369,67,<303,0>,<303,67>) 
    public IConstructor Type_lub$2cd9a56d59418ef9(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_num_, 0)){
           if($has_type_and_arity($1, Symbol_rat_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_num_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(13438,92,<305,0>,<305,92>) 
    public IConstructor Type_lub$4cd4964d29fd898b(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_set_Symbol, 1)){
           IValue $arg0_81 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_81.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_set_Symbol, 1)){
                 IValue $arg0_80 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_80.getType(), ADT_Symbol)){
                    IConstructor t_1 = null;
                    return ((IConstructor)($RVF.constructor(Symbol_set_Symbol, new IValue[]{((IConstructor)($me.lub(((IConstructor)($arg0_81)), ((IConstructor)($arg0_80)))))})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(13533,115,<306,0>,<306,115>) 
    public IConstructor Type_lub$9f0bb9bd89ef79f4(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_set_Symbol, 1)){
           IValue $arg0_83 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_83.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_rel_list_Symbol, 1)){
                 IValue $arg0_82 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_82.getType(), $T0)){
                    IList ts_1 = null;
                    return ((IConstructor)($RVF.constructor(Symbol_set_Symbol, new IValue[]{((IConstructor)($me.lub(((IConstructor)($arg0_83)), ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($arg0_82))}))))))})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(13651,115,<307,0>,<307,115>) 
    public IConstructor Type_lub$0dbf401cac8b4535(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_85 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_85.getType(), $T0)){
              IList ts_0 = null;
              if($has_type_and_arity($1, Symbol_set_Symbol, 1)){
                 IValue $arg0_84 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_84.getType(), ADT_Symbol)){
                    IConstructor s_1 = null;
                    return ((IConstructor)($RVF.constructor(Symbol_set_Symbol, new IValue[]{((IConstructor)($me.lub(((IConstructor)($arg0_84)), ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($arg0_85))}))))))})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(13768,244,<309,0>,<309,244>) 
    public IConstructor Type_lub$ec1dccaf2da25afa(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_87 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_87.getType(), $T0)){
              IList l_0 = ((IList)($arg0_87));
              if($has_type_and_arity($1, Symbol_rel_list_Symbol, 1)){
                 IValue $arg0_86 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_86.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_86));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_87))))), ((IInteger)(M_List.size(((IList)($arg0_86))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_87)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_86)))))).getValue()){
                             if((((IBool)($equal(((IList)($me.getLabels(((IList)($arg0_87))))), ((IList)($me.getLabels(((IList)($arg0_86))))))))).getValue()){
                                return ((IConstructor)($RVF.constructor(Symbol_rel_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_87))))), ((IList)($me.stripLabels(((IList)($arg0_86)))))))), ((IList)($me.getLabels(((IList)($arg0_87))))))))})));
                             
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(14013,220,<310,0>,<310,220>) 
    public IConstructor Type_lub$734046054ee464ba(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_89 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_89.getType(), $T0)){
              IList l_0 = ((IList)($arg0_89));
              if($has_type_and_arity($1, Symbol_rel_list_Symbol, 1)){
                 IValue $arg0_88 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_88.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_88));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_89))))), ((IInteger)(M_List.size(((IList)($arg0_88))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_89)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_88)))))).getValue()){
                             if((((IBool)($equal(((IList)($me.getLabels(((IList)($arg0_89))))),((IList)($me.getLabels(((IList)($arg0_88)))))).not()))).getValue()){
                                return ((IConstructor)($RVF.constructor(Symbol_rel_list_Symbol, new IValue[]{((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_89))))), ((IList)($me.stripLabels(((IList)($arg0_88))))))))})));
                             
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(14234,213,<311,0>,<311,213>) 
    public IConstructor Type_lub$4b68476103d104fd(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_91 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_91.getType(), $T0)){
              IList l_0 = ((IList)($arg0_91));
              if($has_type_and_arity($1, Symbol_rel_list_Symbol, 1)){
                 IValue $arg0_90 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_90.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_90));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_91))))), ((IInteger)(M_List.size(((IList)($arg0_90))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_91)))))).getValue()){
                          if((((IBool)($me.noneLabeled(((IList)($arg0_90)))))).getValue()){
                             return ((IConstructor)($RVF.constructor(Symbol_rel_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_91))))), ((IList)($me.stripLabels(((IList)($arg0_90)))))))), ((IList)($me.getLabels(((IList)($arg0_91))))))))})));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(14448,213,<312,0>,<312,213>) 
    public IConstructor Type_lub$111a6137b422fe0a(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_93 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_93.getType(), $T0)){
              IList l_0 = ((IList)($arg0_93));
              if($has_type_and_arity($1, Symbol_rel_list_Symbol, 1)){
                 IValue $arg0_92 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_92.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_92));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_93))))), ((IInteger)(M_List.size(((IList)($arg0_92))))))))).getValue()){
                       if((((IBool)($me.noneLabeled(((IList)($arg0_93)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_92)))))).getValue()){
                             return ((IConstructor)($RVF.constructor(Symbol_rel_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_93))))), ((IList)($me.stripLabels(((IList)($arg0_92)))))))), ((IList)($me.getLabels(((IList)($arg0_92))))))))})));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(14662,190,<313,0>,<313,190>) 
    public IConstructor Type_lub$458a19628ff06574(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_95 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_95.getType(), $T0)){
              IList l_0 = ((IList)($arg0_95));
              if($has_type_and_arity($1, Symbol_rel_list_Symbol, 1)){
                 IValue $arg0_94 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_94.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_94));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_95))))), ((IInteger)(M_List.size(((IList)($arg0_94))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_95)))))).getValue()){
                          return null;
                       } else {
                          if((((IBool)($me.allLabeled(((IList)($arg0_94)))))).getValue()){
                             return null;
                          } else {
                             return ((IConstructor)($RVF.constructor(Symbol_rel_list_Symbol, new IValue[]{((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_95))))), ((IList)($me.stripLabels(((IList)($arg0_94))))))))})));
                          
                          }
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(14853,119,<314,0>,<314,119>) 
    public IConstructor Type_lub$286a95d36c4d9271(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_97 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_97.getType(), $T0)){
              IList l_0 = ((IList)($arg0_97));
              if($has_type_and_arity($1, Symbol_rel_list_Symbol, 1)){
                 IValue $arg0_96 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_96.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_96));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_97))))),((IInteger)(M_List.size(((IList)($arg0_96)))))).not()))).getValue()){
                       return ((IConstructor)$constants.get(6)/*set(value())*/);
                    
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(14974,95,<316,0>,<316,95>) 
    public IConstructor Type_lub$be05e62f5f33e70a(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_list_Symbol, 1)){
           IValue $arg0_99 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_99.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_list_Symbol, 1)){
                 IValue $arg0_98 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_98.getType(), ADT_Symbol)){
                    IConstructor t_1 = null;
                    return ((IConstructor)($RVF.constructor(Symbol_list_Symbol, new IValue[]{((IConstructor)($me.lub(((IConstructor)($arg0_99)), ((IConstructor)($arg0_98)))))})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(15072,102,<317,0>,<317,102>) 
    public IConstructor Type_lub$42beb0d51d9c47f2(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_list_Symbol, 1)){
           IValue $arg0_101 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_101.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_lrel_list_Symbol, 1)){
                 IValue $arg0_100 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_100.getType(), $T0)){
                    IList ts_1 = null;
                    return ((IConstructor)($RVF.constructor(Symbol_list_Symbol, new IValue[]{((IConstructor)($me.lub(((IConstructor)($arg0_101)), ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($arg0_100))}))))))})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(15177,110,<318,0>,<318,110>) 
    public IConstructor Type_lub$7a4636c028a0a06c(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_103 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_103.getType(), $T0)){
              IList ts_0 = null;
              if($has_type_and_arity($1, Symbol_list_Symbol, 1)){
                 IValue $arg0_102 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_102.getType(), ADT_Symbol)){
                    IConstructor s_1 = null;
                    return ((IConstructor)($RVF.constructor(Symbol_list_Symbol, new IValue[]{((IConstructor)($me.lub(((IConstructor)($arg0_102)), ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($arg0_103))}))))))})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(15289,247,<320,0>,<320,247>) 
    public IConstructor Type_lub$89bf39f91d0c3172(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_105 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_105.getType(), $T0)){
              IList l_0 = ((IList)($arg0_105));
              if($has_type_and_arity($1, Symbol_lrel_list_Symbol, 1)){
                 IValue $arg0_104 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_104.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_104));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_105))))), ((IInteger)(M_List.size(((IList)($arg0_104))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_105)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_104)))))).getValue()){
                             if((((IBool)($equal(((IList)($me.getLabels(((IList)($arg0_105))))), ((IList)($me.getLabels(((IList)($arg0_104))))))))).getValue()){
                                return ((IConstructor)($RVF.constructor(Symbol_lrel_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_105))))), ((IList)($me.stripLabels(((IList)($arg0_104)))))))), ((IList)($me.getLabels(((IList)($arg0_105))))))))})));
                             
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(15537,223,<321,0>,<321,223>) 
    public IConstructor Type_lub$cdf48c3a28135a1b(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_107 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_107.getType(), $T0)){
              IList l_0 = ((IList)($arg0_107));
              if($has_type_and_arity($1, Symbol_lrel_list_Symbol, 1)){
                 IValue $arg0_106 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_106.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_106));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_107))))), ((IInteger)(M_List.size(((IList)($arg0_106))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_107)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_106)))))).getValue()){
                             if((((IBool)($equal(((IList)($me.getLabels(((IList)($arg0_107))))),((IList)($me.getLabels(((IList)($arg0_106)))))).not()))).getValue()){
                                return ((IConstructor)($RVF.constructor(Symbol_lrel_list_Symbol, new IValue[]{((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_107))))), ((IList)($me.stripLabels(((IList)($arg0_106))))))))})));
                             
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(15761,216,<322,0>,<322,216>) 
    public IConstructor Type_lub$363046ae837d0ed7(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_109 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_109.getType(), $T0)){
              IList l_0 = ((IList)($arg0_109));
              if($has_type_and_arity($1, Symbol_lrel_list_Symbol, 1)){
                 IValue $arg0_108 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_108.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_108));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_109))))), ((IInteger)(M_List.size(((IList)($arg0_108))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_109)))))).getValue()){
                          if((((IBool)($me.noneLabeled(((IList)($arg0_108)))))).getValue()){
                             return ((IConstructor)($RVF.constructor(Symbol_lrel_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_109))))), ((IList)($me.stripLabels(((IList)($arg0_108)))))))), ((IList)($me.getLabels(((IList)($arg0_109))))))))})));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(15978,216,<323,0>,<323,216>) 
    public IConstructor Type_lub$57f39e76485c88cd(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_111 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_111.getType(), $T0)){
              IList l_0 = ((IList)($arg0_111));
              if($has_type_and_arity($1, Symbol_lrel_list_Symbol, 1)){
                 IValue $arg0_110 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_110.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_110));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_111))))), ((IInteger)(M_List.size(((IList)($arg0_110))))))))).getValue()){
                       if((((IBool)($me.noneLabeled(((IList)($arg0_111)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_110)))))).getValue()){
                             return ((IConstructor)($RVF.constructor(Symbol_lrel_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_111))))), ((IList)($me.stripLabels(((IList)($arg0_110)))))))), ((IList)($me.getLabels(((IList)($arg0_110))))))))})));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(16195,193,<324,0>,<324,193>) 
    public IConstructor Type_lub$ea0cf15c1bb98cd7(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_113 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_113.getType(), $T0)){
              IList l_0 = ((IList)($arg0_113));
              if($has_type_and_arity($1, Symbol_lrel_list_Symbol, 1)){
                 IValue $arg0_112 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_112.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_112));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_113))))), ((IInteger)(M_List.size(((IList)($arg0_112))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_113)))))).getValue()){
                          return null;
                       } else {
                          if((((IBool)($me.allLabeled(((IList)($arg0_112)))))).getValue()){
                             return null;
                          } else {
                             return ((IConstructor)($RVF.constructor(Symbol_lrel_list_Symbol, new IValue[]{((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_113))))), ((IList)($me.stripLabels(((IList)($arg0_112))))))))})));
                          
                          }
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(16389,130,<325,0>,<325,130>) 
    public IConstructor Type_lub$1ce55522faa742da(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_115 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_115.getType(), $T0)){
              IList l_0 = ((IList)($arg0_115));
              if($has_type_and_arity($1, Symbol_lrel_list_Symbol, 1)){
                 IValue $arg0_114 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_114.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_114));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_115))))),((IInteger)(M_List.size(((IList)($arg0_114)))))).not()))).getValue()){
                       return ((IConstructor)($RVF.constructor(Symbol_list_Symbol, new IValue[]{((IConstructor)$constants.get(5)/*value()*/)})));
                    
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(16521,250,<327,0>,<327,250>) 
    public IConstructor Type_lub$24cbcbc48e7f84f7(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_tuple_list_Symbol, 1)){
           IValue $arg0_117 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_117.getType(), $T0)){
              IList l_0 = ((IList)($arg0_117));
              if($has_type_and_arity($1, Symbol_tuple_list_Symbol, 1)){
                 IValue $arg0_116 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_116.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_116));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_117))))), ((IInteger)(M_List.size(((IList)($arg0_116))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_117)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_116)))))).getValue()){
                             if((((IBool)($equal(((IList)($me.getLabels(((IList)($arg0_117))))), ((IList)($me.getLabels(((IList)($arg0_116))))))))).getValue()){
                                return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_117))))), ((IList)($me.stripLabels(((IList)($arg0_116)))))))), ((IList)($me.getLabels(((IList)($arg0_117))))))))})));
                             
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(16772,226,<328,0>,<328,226>) 
    public IConstructor Type_lub$c4a2a53eec0c75c1(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_tuple_list_Symbol, 1)){
           IValue $arg0_119 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_119.getType(), $T0)){
              IList l_0 = ((IList)($arg0_119));
              if($has_type_and_arity($1, Symbol_tuple_list_Symbol, 1)){
                 IValue $arg0_118 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_118.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_118));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_119))))), ((IInteger)(M_List.size(((IList)($arg0_118))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_119)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_118)))))).getValue()){
                             if((((IBool)($equal(((IList)($me.getLabels(((IList)($arg0_119))))),((IList)($me.getLabels(((IList)($arg0_118)))))).not()))).getValue()){
                                return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_119))))), ((IList)($me.stripLabels(((IList)($arg0_118))))))))})));
                             
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(16999,219,<329,0>,<329,219>) 
    public IConstructor Type_lub$3cb3aedf8aed5365(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_tuple_list_Symbol, 1)){
           IValue $arg0_121 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_121.getType(), $T0)){
              IList l_0 = ((IList)($arg0_121));
              if($has_type_and_arity($1, Symbol_tuple_list_Symbol, 1)){
                 IValue $arg0_120 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_120.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_120));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_121))))), ((IInteger)(M_List.size(((IList)($arg0_120))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_121)))))).getValue()){
                          if((((IBool)($me.noneLabeled(((IList)($arg0_120)))))).getValue()){
                             return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_121))))), ((IList)($me.stripLabels(((IList)($arg0_120)))))))), ((IList)($me.getLabels(((IList)($arg0_121))))))))})));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(17219,219,<330,0>,<330,219>) 
    public IConstructor Type_lub$a95613250b5d7982(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_tuple_list_Symbol, 1)){
           IValue $arg0_123 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_123.getType(), $T0)){
              IList l_0 = ((IList)($arg0_123));
              if($has_type_and_arity($1, Symbol_tuple_list_Symbol, 1)){
                 IValue $arg0_122 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_122.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_122));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_123))))), ((IInteger)(M_List.size(((IList)($arg0_122))))))))).getValue()){
                       if((((IBool)($me.noneLabeled(((IList)($arg0_123)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_122)))))).getValue()){
                             return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_123))))), ((IList)($me.stripLabels(((IList)($arg0_122)))))))), ((IList)($me.getLabels(((IList)($arg0_122))))))))})));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(17439,275,<331,0>,<331,275>) 
    public IConstructor Type_lub$645ef43b093722bb(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_tuple_list_Symbol, 1)){
           IValue $arg0_130 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_130.getType(), $T0)){
              IList l_0 = ((IList)($arg0_130));
              if($has_type_and_arity($1, Symbol_tuple_list_Symbol, 1)){
                 IValue $arg0_129 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_129.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_129));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_130))))), ((IInteger)(M_List.size(((IList)($arg0_129))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_130)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_129)))))).getValue()){
                             return null;
                          } else {
                             if((((IBool)($me.allLabeled(((IList)($arg0_130)))))).getValue()){
                                if((((IBool)($me.noneLabeled(((IList)($arg0_129)))))).getValue()){
                                   return null;
                                } else {
                                   if((((IBool)($me.noneLabeled(((IList)($arg0_130)))))).getValue()){
                                      if((((IBool)($me.allLabeled(((IList)($arg0_129)))))).getValue()){
                                         return null;
                                      } else {
                                         return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_130))))), ((IList)($me.stripLabels(((IList)($arg0_129))))))))})));
                                      
                                      }
                                   } else {
                                      return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_130))))), ((IList)($me.stripLabels(((IList)($arg0_129))))))))})));
                                   
                                   }
                                }
                             } else {
                                if((((IBool)($me.noneLabeled(((IList)($arg0_130)))))).getValue()){
                                   if((((IBool)($me.allLabeled(((IList)($arg0_129)))))).getValue()){
                                      return null;
                                   } else {
                                      return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_130))))), ((IList)($me.stripLabels(((IList)($arg0_129))))))))})));
                                   
                                   }
                                } else {
                                   return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_130))))), ((IList)($me.stripLabels(((IList)($arg0_129))))))))})));
                                
                                }
                             }
                          }
                       } else {
                          if((((IBool)($me.allLabeled(((IList)($arg0_130)))))).getValue()){
                             if((((IBool)($me.noneLabeled(((IList)($arg0_129)))))).getValue()){
                                return null;
                             } else {
                                if((((IBool)($me.noneLabeled(((IList)($arg0_130)))))).getValue()){
                                   if((((IBool)($me.allLabeled(((IList)($arg0_129)))))).getValue()){
                                      return null;
                                   } else {
                                      return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_130))))), ((IList)($me.stripLabels(((IList)($arg0_129))))))))})));
                                   
                                   }
                                } else {
                                   return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_130))))), ((IList)($me.stripLabels(((IList)($arg0_129))))))))})));
                                
                                }
                             }
                          } else {
                             if((((IBool)($me.noneLabeled(((IList)($arg0_130)))))).getValue()){
                                if((((IBool)($me.allLabeled(((IList)($arg0_129)))))).getValue()){
                                   return null;
                                } else {
                                   return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_130))))), ((IList)($me.stripLabels(((IList)($arg0_129))))))))})));
                                
                                }
                             } else {
                                return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.lub(((IList)($me.stripLabels(((IList)($arg0_130))))), ((IList)($me.stripLabels(((IList)($arg0_129))))))))})));
                             
                             }
                          }
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(17716,253,<333,0>,<333,253>) 
    public IConstructor Type_lub$525d494ef32e780f(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_map_Symbol_Symbol, 2)){
           IValue $arg0_140 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_140.getType(), ADT_Symbol)){
              if($has_type_and_arity($arg0_140, Symbol_label_str_Symbol, 2)){
                 IValue $arg0_142 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_140)),0));
                 if($isComparable($arg0_142.getType(), $T2)){
                    IString lfl_0 = null;
                    IValue $arg1_141 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_140)),1));
                    if($isComparable($arg1_141.getType(), ADT_Symbol)){
                       IConstructor lf_1 = null;
                       IValue $arg1_137 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
                       if($isComparable($arg1_137.getType(), ADT_Symbol)){
                          if($has_type_and_arity($arg1_137, Symbol_label_str_Symbol, 2)){
                             IValue $arg0_139 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_137)),0));
                             if($isComparable($arg0_139.getType(), $T2)){
                                IString ltl_2 = null;
                                IValue $arg1_138 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_137)),1));
                                if($isComparable($arg1_138.getType(), ADT_Symbol)){
                                   IConstructor lt_3 = null;
                                   if($has_type_and_arity($1, Symbol_map_Symbol_Symbol, 2)){
                                      IValue $arg0_134 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                                      if($isComparable($arg0_134.getType(), ADT_Symbol)){
                                         if($has_type_and_arity($arg0_134, Symbol_label_str_Symbol, 2)){
                                            IValue $arg0_136 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_134)),0));
                                            if($isComparable($arg0_136.getType(), $T2)){
                                               IString rfl_4 = null;
                                               IValue $arg1_135 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_134)),1));
                                               if($isComparable($arg1_135.getType(), ADT_Symbol)){
                                                  IConstructor rf_5 = null;
                                                  IValue $arg1_131 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                                                  if($isComparable($arg1_131.getType(), ADT_Symbol)){
                                                     if($has_type_and_arity($arg1_131, Symbol_label_str_Symbol, 2)){
                                                        IValue $arg0_133 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_131)),0));
                                                        if($isComparable($arg0_133.getType(), $T2)){
                                                           IString rtl_6 = null;
                                                           IValue $arg1_132 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_131)),1));
                                                           if($isComparable($arg1_132.getType(), ADT_Symbol)){
                                                              IConstructor rt_7 = null;
                                                              if((((IBool)($equal(((IString)($arg0_142)), ((IString)($arg0_136)))))).getValue()){
                                                                 if((((IBool)($equal(((IString)($arg0_139)), ((IString)($arg0_133)))))).getValue()){
                                                                    return ((IConstructor)($RVF.constructor(Symbol_map_Symbol_Symbol, new IValue[]{((IConstructor)($RVF.constructor(Symbol_label_str_Symbol, new IValue[]{((IString)($arg0_142)), ((IConstructor)($me.lub(((IConstructor)($arg1_141)), ((IConstructor)($arg1_135)))))}))), ((IConstructor)($RVF.constructor(Symbol_label_str_Symbol, new IValue[]{((IString)($arg0_139)), ((IConstructor)($me.lub(((IConstructor)($arg1_138)), ((IConstructor)($arg1_132)))))})))})));
                                                                 
                                                                 } else {
                                                                    return null;
                                                                 }
                                                              } else {
                                                                 return null;
                                                              }
                                                           } else {
                                                              return null;
                                                           }
                                                        } else {
                                                           return null;
                                                        }
                                                     } else {
                                                        return null;
                                                     }
                                                  } else {
                                                     return null;
                                                  }
                                               } else {
                                                  return null;
                                               }
                                            } else {
                                               return null;
                                            }
                                         } else {
                                            return null;
                                         }
                                      } else {
                                         return null;
                                      }
                                   } else {
                                      return null;
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(17970,227,<334,0>,<334,227>) 
    public IConstructor Type_lub$3f4d0193ec654be4(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_map_Symbol_Symbol, 2)){
           IValue $arg0_152 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_152.getType(), ADT_Symbol)){
              if($has_type_and_arity($arg0_152, Symbol_label_str_Symbol, 2)){
                 IValue $arg0_154 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_152)),0));
                 if($isComparable($arg0_154.getType(), $T2)){
                    IString lfl_0 = null;
                    IValue $arg1_153 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_152)),1));
                    if($isComparable($arg1_153.getType(), ADT_Symbol)){
                       IConstructor lf_1 = null;
                       IValue $arg1_149 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
                       if($isComparable($arg1_149.getType(), ADT_Symbol)){
                          if($has_type_and_arity($arg1_149, Symbol_label_str_Symbol, 2)){
                             IValue $arg0_151 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_149)),0));
                             if($isComparable($arg0_151.getType(), $T2)){
                                IString ltl_2 = null;
                                IValue $arg1_150 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_149)),1));
                                if($isComparable($arg1_150.getType(), ADT_Symbol)){
                                   IConstructor lt_3 = null;
                                   if($has_type_and_arity($1, Symbol_map_Symbol_Symbol, 2)){
                                      IValue $arg0_146 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                                      if($isComparable($arg0_146.getType(), ADT_Symbol)){
                                         if($has_type_and_arity($arg0_146, Symbol_label_str_Symbol, 2)){
                                            IValue $arg0_148 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_146)),0));
                                            if($isComparable($arg0_148.getType(), $T2)){
                                               IString rfl_4 = null;
                                               IValue $arg1_147 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_146)),1));
                                               if($isComparable($arg1_147.getType(), ADT_Symbol)){
                                                  IConstructor rf_5 = null;
                                                  IValue $arg1_143 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                                                  if($isComparable($arg1_143.getType(), ADT_Symbol)){
                                                     if($has_type_and_arity($arg1_143, Symbol_label_str_Symbol, 2)){
                                                        IValue $arg0_145 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_143)),0));
                                                        if($isComparable($arg0_145.getType(), $T2)){
                                                           IString rtl_6 = null;
                                                           IValue $arg1_144 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_143)),1));
                                                           if($isComparable($arg1_144.getType(), ADT_Symbol)){
                                                              IConstructor rt_7 = null;
                                                              if((((IBool)($equal(((IString)($arg0_154)),((IString)($arg0_148))).not()))).getValue()){
                                                                 return ((IConstructor)($RVF.constructor(Symbol_map_Symbol_Symbol, new IValue[]{((IConstructor)($me.lub(((IConstructor)($arg1_153)), ((IConstructor)($arg1_147))))), ((IConstructor)($me.lub(((IConstructor)($arg1_150)), ((IConstructor)($arg1_144)))))})));
                                                              
                                                              } else {
                                                                 if((((IBool)($equal(((IString)($arg0_151)),((IString)($arg0_145))).not()))).getValue()){
                                                                    return ((IConstructor)($RVF.constructor(Symbol_map_Symbol_Symbol, new IValue[]{((IConstructor)($me.lub(((IConstructor)($arg1_153)), ((IConstructor)($arg1_147))))), ((IConstructor)($me.lub(((IConstructor)($arg1_150)), ((IConstructor)($arg1_144)))))})));
                                                                 
                                                                 } else {
                                                                    return null;
                                                                 }
                                                              }
                                                           } else {
                                                              return null;
                                                           }
                                                        } else {
                                                           return null;
                                                        }
                                                     } else {
                                                        return null;
                                                     }
                                                  } else {
                                                     return null;
                                                  }
                                               } else {
                                                  return null;
                                               }
                                            } else {
                                               return null;
                                            }
                                         } else {
                                            return null;
                                         }
                                      } else {
                                         return null;
                                      }
                                   } else {
                                      return null;
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(18198,236,<335,0>,<335,236>) 
    public IConstructor Type_lub$79c1b7694ffd9fd8(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_map_Symbol_Symbol, 2)){
           IValue $arg0_167 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_167.getType(), ADT_Symbol)){
              if($has_type_and_arity($arg0_167, Symbol_label_str_Symbol, 2)){
                 IValue $arg0_169 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_167)),0));
                 if($isComparable($arg0_169.getType(), $T2)){
                    if(true){
                       IString lfl_0 = null;
                       IValue $arg1_168 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_167)),1));
                       if($isComparable($arg1_168.getType(), ADT_Symbol)){
                          if(true){
                             IConstructor lf_1 = null;
                             IValue $arg1_164 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
                             if($isComparable($arg1_164.getType(), ADT_Symbol)){
                                if($has_type_and_arity($arg1_164, Symbol_label_str_Symbol, 2)){
                                   IValue $arg0_166 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_164)),0));
                                   if($isComparable($arg0_166.getType(), $T2)){
                                      if(true){
                                         IString ltl_2 = null;
                                         IValue $arg1_165 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_164)),1));
                                         if($isComparable($arg1_165.getType(), ADT_Symbol)){
                                            if(true){
                                               IConstructor lt_3 = null;
                                               if($has_type_and_arity($1, Symbol_map_Symbol_Symbol, 2)){
                                                  IValue $arg0_163 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                                                  if($isComparable($arg0_163.getType(), ADT_Symbol)){
                                                     if(true){
                                                        IConstructor rf_4 = null;
                                                        IValue $arg1_162 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                                                        if($isComparable($arg1_162.getType(), ADT_Symbol)){
                                                           if(true){
                                                              IConstructor rt_5 = null;
                                                              IBool $aux0 = (IBool)(((IBool)$constants.get(2)/*false*/));
                                                              $aux0 = ((IBool)$constants.get(2)/*false*/);
                                                              /*muExists*/$EXP156: 
                                                                  do {
                                                                      if($has_type_and_arity($arg0_163, Symbol_label_str_Symbol, 2)){
                                                                         IValue $arg0_158 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_163)),0));
                                                                         if($isComparable($arg0_158.getType(), $T4)){
                                                                            IValue $arg1_157 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_163)),1));
                                                                            if($isComparable($arg1_157.getType(), $T4)){
                                                                               $aux0 = ((IBool)$constants.get(1)/*true*/);
                                                                               break $EXP156; // muSucceed
                                                                            } else {
                                                                               $aux0 = ((IBool)$constants.get(2)/*false*/);
                                                                               continue $EXP156;
                                                                            }
                                                                         } else {
                                                                            $aux0 = ((IBool)$constants.get(2)/*false*/);
                                                                            continue $EXP156;
                                                                         }
                                                                      } else {
                                                                         $aux0 = ((IBool)$constants.get(2)/*false*/);
                                                                         continue $EXP156;
                                                                      }
                                                                  } while(false);
                                                              if((((IBool)($aux0))).getValue()){
                                                                 return null;
                                                              } else {
                                                                 IBool $aux1 = (IBool)(((IBool)$constants.get(2)/*false*/));
                                                                 $aux1 = ((IBool)$constants.get(2)/*false*/);
                                                                 /*muExists*/$EXP159: 
                                                                     do {
                                                                         if($has_type_and_arity($arg1_162, Symbol_label_str_Symbol, 2)){
                                                                            IValue $arg0_161 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_162)),0));
                                                                            if($isComparable($arg0_161.getType(), $T4)){
                                                                               IValue $arg1_160 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_162)),1));
                                                                               if($isComparable($arg1_160.getType(), $T4)){
                                                                                  $aux1 = ((IBool)$constants.get(1)/*true*/);
                                                                                  break $EXP159; // muSucceed
                                                                               } else {
                                                                                  $aux1 = ((IBool)$constants.get(2)/*false*/);
                                                                                  continue $EXP159;
                                                                               }
                                                                            } else {
                                                                               $aux1 = ((IBool)$constants.get(2)/*false*/);
                                                                               continue $EXP159;
                                                                            }
                                                                         } else {
                                                                            $aux1 = ((IBool)$constants.get(2)/*false*/);
                                                                            continue $EXP159;
                                                                         }
                                                                     } while(false);
                                                                 if((((IBool)($aux1))).getValue()){
                                                                    return null;
                                                                 } else {
                                                                    return ((IConstructor)($RVF.constructor(Symbol_map_Symbol_Symbol, new IValue[]{((IConstructor)($RVF.constructor(Symbol_label_str_Symbol, new IValue[]{((IString)($arg0_169)), ((IConstructor)($me.lub(((IConstructor)($arg1_168)), ((IConstructor)($arg0_163)))))}))), ((IConstructor)($RVF.constructor(Symbol_label_str_Symbol, new IValue[]{((IString)($arg0_166)), ((IConstructor)($me.lub(((IConstructor)($arg1_165)), ((IConstructor)($arg1_162)))))})))})));
                                                                 
                                                                 }
                                                              }
                                                           } else {
                                                              return null;
                                                           }
                                                        } else {
                                                           return null;
                                                        }
                                                     } else {
                                                        return null;
                                                     }
                                                  } else {
                                                     return null;
                                                  }
                                               } else {
                                                  return null;
                                               }
                                            } else {
                                               return null;
                                            }
                                         } else {
                                            return null;
                                         }
                                      } else {
                                         return null;
                                      }
                                   } else {
                                      return null;
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(18435,236,<336,0>,<336,236>) 
    public IConstructor Type_lub$89bef2ae42badcac(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_map_Symbol_Symbol, 2)){
           IValue $arg0_184 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_184.getType(), ADT_Symbol)){
              if(true){
                 IConstructor lf_0 = null;
                 IValue $arg1_183 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
                 if($isComparable($arg1_183.getType(), ADT_Symbol)){
                    if(true){
                       IConstructor lt_1 = null;
                       if($has_type_and_arity($1, Symbol_map_Symbol_Symbol, 2)){
                          IValue $arg0_180 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                          if($isComparable($arg0_180.getType(), ADT_Symbol)){
                             if($has_type_and_arity($arg0_180, Symbol_label_str_Symbol, 2)){
                                IValue $arg0_182 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_180)),0));
                                if($isComparable($arg0_182.getType(), $T2)){
                                   if(true){
                                      IString rfl_2 = null;
                                      IValue $arg1_181 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_180)),1));
                                      if($isComparable($arg1_181.getType(), ADT_Symbol)){
                                         if(true){
                                            IConstructor rf_3 = null;
                                            IValue $arg1_177 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                                            if($isComparable($arg1_177.getType(), ADT_Symbol)){
                                               if($has_type_and_arity($arg1_177, Symbol_label_str_Symbol, 2)){
                                                  IValue $arg0_179 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_177)),0));
                                                  if($isComparable($arg0_179.getType(), $T2)){
                                                     if(true){
                                                        IString rtl_4 = null;
                                                        IValue $arg1_178 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_177)),1));
                                                        if($isComparable($arg1_178.getType(), ADT_Symbol)){
                                                           if(true){
                                                              IConstructor rt_5 = null;
                                                              IBool $aux2 = (IBool)(((IBool)$constants.get(2)/*false*/));
                                                              $aux2 = ((IBool)$constants.get(2)/*false*/);
                                                              /*muExists*/$EXP171: 
                                                                  do {
                                                                      if($has_type_and_arity($arg0_184, Symbol_label_str_Symbol, 2)){
                                                                         IValue $arg0_173 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_184)),0));
                                                                         if($isComparable($arg0_173.getType(), $T4)){
                                                                            IValue $arg1_172 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_184)),1));
                                                                            if($isComparable($arg1_172.getType(), $T4)){
                                                                               $aux2 = ((IBool)$constants.get(1)/*true*/);
                                                                               break $EXP171; // muSucceed
                                                                            } else {
                                                                               $aux2 = ((IBool)$constants.get(2)/*false*/);
                                                                               continue $EXP171;
                                                                            }
                                                                         } else {
                                                                            $aux2 = ((IBool)$constants.get(2)/*false*/);
                                                                            continue $EXP171;
                                                                         }
                                                                      } else {
                                                                         $aux2 = ((IBool)$constants.get(2)/*false*/);
                                                                         continue $EXP171;
                                                                      }
                                                                  } while(false);
                                                              if((((IBool)($aux2))).getValue()){
                                                                 return null;
                                                              } else {
                                                                 IBool $aux3 = (IBool)(((IBool)$constants.get(2)/*false*/));
                                                                 $aux3 = ((IBool)$constants.get(2)/*false*/);
                                                                 /*muExists*/$EXP174: 
                                                                     do {
                                                                         if($has_type_and_arity($arg1_183, Symbol_label_str_Symbol, 2)){
                                                                            IValue $arg0_176 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_183)),0));
                                                                            if($isComparable($arg0_176.getType(), $T4)){
                                                                               IValue $arg1_175 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_183)),1));
                                                                               if($isComparable($arg1_175.getType(), $T4)){
                                                                                  $aux3 = ((IBool)$constants.get(1)/*true*/);
                                                                                  break $EXP174; // muSucceed
                                                                               } else {
                                                                                  $aux3 = ((IBool)$constants.get(2)/*false*/);
                                                                                  continue $EXP174;
                                                                               }
                                                                            } else {
                                                                               $aux3 = ((IBool)$constants.get(2)/*false*/);
                                                                               continue $EXP174;
                                                                            }
                                                                         } else {
                                                                            $aux3 = ((IBool)$constants.get(2)/*false*/);
                                                                            continue $EXP174;
                                                                         }
                                                                     } while(false);
                                                                 if((((IBool)($aux3))).getValue()){
                                                                    return null;
                                                                 } else {
                                                                    return ((IConstructor)($RVF.constructor(Symbol_map_Symbol_Symbol, new IValue[]{((IConstructor)($RVF.constructor(Symbol_label_str_Symbol, new IValue[]{((IString)($arg0_182)), ((IConstructor)($me.lub(((IConstructor)($arg0_184)), ((IConstructor)($arg1_181)))))}))), ((IConstructor)($RVF.constructor(Symbol_label_str_Symbol, new IValue[]{((IString)($arg0_179)), ((IConstructor)($me.lub(((IConstructor)($arg1_183)), ((IConstructor)($arg1_178)))))})))})));
                                                                 
                                                                 }
                                                              }
                                                           } else {
                                                              return null;
                                                           }
                                                        } else {
                                                           return null;
                                                        }
                                                     } else {
                                                        return null;
                                                     }
                                                  } else {
                                                     return null;
                                                  }
                                               } else {
                                                  return null;
                                               }
                                            } else {
                                               return null;
                                            }
                                         } else {
                                            return null;
                                         }
                                      } else {
                                         return null;
                                      }
                                   } else {
                                      return null;
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(18672,218,<337,0>,<337,218>) 
    public IConstructor Type_lub$8e262d13a8e22e5b(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_map_Symbol_Symbol, 2)){
           IValue $arg0_203 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_203.getType(), ADT_Symbol)){
              if(true){
                 IConstructor lf_0 = null;
                 IValue $arg1_202 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
                 if($isComparable($arg1_202.getType(), ADT_Symbol)){
                    if(true){
                       IConstructor lt_1 = null;
                       if($has_type_and_arity($1, Symbol_map_Symbol_Symbol, 2)){
                          IValue $arg0_201 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                          if($isComparable($arg0_201.getType(), ADT_Symbol)){
                             if(true){
                                IConstructor rf_2 = null;
                                IValue $arg1_200 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                                if($isComparable($arg1_200.getType(), ADT_Symbol)){
                                   if(true){
                                      IConstructor rt_3 = null;
                                      IBool $aux4 = (IBool)(((IBool)$constants.get(2)/*false*/));
                                      $aux4 = ((IBool)$constants.get(2)/*false*/);
                                      /*muExists*/$EXP188: 
                                          do {
                                              if($has_type_and_arity($arg0_203, Symbol_label_str_Symbol, 2)){
                                                 IValue $arg0_190 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_203)),0));
                                                 if($isComparable($arg0_190.getType(), $T4)){
                                                    IValue $arg1_189 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_203)),1));
                                                    if($isComparable($arg1_189.getType(), $T4)){
                                                       $aux4 = ((IBool)$constants.get(1)/*true*/);
                                                       break $EXP188; // muSucceed
                                                    } else {
                                                       $aux4 = ((IBool)$constants.get(2)/*false*/);
                                                       continue $EXP188;
                                                    }
                                                 } else {
                                                    $aux4 = ((IBool)$constants.get(2)/*false*/);
                                                    continue $EXP188;
                                                 }
                                              } else {
                                                 $aux4 = ((IBool)$constants.get(2)/*false*/);
                                                 continue $EXP188;
                                              }
                                          } while(false);
                                      if((((IBool)($aux4))).getValue()){
                                         return null;
                                      } else {
                                         IBool $aux5 = (IBool)(((IBool)$constants.get(2)/*false*/));
                                         $aux5 = ((IBool)$constants.get(2)/*false*/);
                                         /*muExists*/$EXP191: 
                                             do {
                                                 if($has_type_and_arity($arg1_202, Symbol_label_str_Symbol, 2)){
                                                    IValue $arg0_193 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_202)),0));
                                                    if($isComparable($arg0_193.getType(), $T4)){
                                                       IValue $arg1_192 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_202)),1));
                                                       if($isComparable($arg1_192.getType(), $T4)){
                                                          $aux5 = ((IBool)$constants.get(1)/*true*/);
                                                          break $EXP191; // muSucceed
                                                       } else {
                                                          $aux5 = ((IBool)$constants.get(2)/*false*/);
                                                          continue $EXP191;
                                                       }
                                                    } else {
                                                       $aux5 = ((IBool)$constants.get(2)/*false*/);
                                                       continue $EXP191;
                                                    }
                                                 } else {
                                                    $aux5 = ((IBool)$constants.get(2)/*false*/);
                                                    continue $EXP191;
                                                 }
                                             } while(false);
                                         if((((IBool)($aux5))).getValue()){
                                            return null;
                                         } else {
                                            IBool $aux6 = (IBool)(((IBool)$constants.get(2)/*false*/));
                                            $aux6 = ((IBool)$constants.get(2)/*false*/);
                                            /*muExists*/$EXP194: 
                                                do {
                                                    if($has_type_and_arity($arg0_201, Symbol_label_str_Symbol, 2)){
                                                       IValue $arg0_196 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_201)),0));
                                                       if($isComparable($arg0_196.getType(), $T4)){
                                                          IValue $arg1_195 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_201)),1));
                                                          if($isComparable($arg1_195.getType(), $T4)){
                                                             $aux6 = ((IBool)$constants.get(1)/*true*/);
                                                             break $EXP194; // muSucceed
                                                          } else {
                                                             $aux6 = ((IBool)$constants.get(2)/*false*/);
                                                             continue $EXP194;
                                                          }
                                                       } else {
                                                          $aux6 = ((IBool)$constants.get(2)/*false*/);
                                                          continue $EXP194;
                                                       }
                                                    } else {
                                                       $aux6 = ((IBool)$constants.get(2)/*false*/);
                                                       continue $EXP194;
                                                    }
                                                } while(false);
                                            if((((IBool)($aux6))).getValue()){
                                               return null;
                                            } else {
                                               IBool $aux7 = (IBool)(((IBool)$constants.get(2)/*false*/));
                                               $aux7 = ((IBool)$constants.get(2)/*false*/);
                                               /*muExists*/$EXP197: 
                                                   do {
                                                       if($has_type_and_arity($arg1_200, Symbol_label_str_Symbol, 2)){
                                                          IValue $arg0_199 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_200)),0));
                                                          if($isComparable($arg0_199.getType(), $T4)){
                                                             IValue $arg1_198 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_200)),1));
                                                             if($isComparable($arg1_198.getType(), $T4)){
                                                                $aux7 = ((IBool)$constants.get(1)/*true*/);
                                                                break $EXP197; // muSucceed
                                                             } else {
                                                                $aux7 = ((IBool)$constants.get(2)/*false*/);
                                                                continue $EXP197;
                                                             }
                                                          } else {
                                                             $aux7 = ((IBool)$constants.get(2)/*false*/);
                                                             continue $EXP197;
                                                          }
                                                       } else {
                                                          $aux7 = ((IBool)$constants.get(2)/*false*/);
                                                          continue $EXP197;
                                                       }
                                                   } while(false);
                                               if((((IBool)($aux7))).getValue()){
                                                  return null;
                                               } else {
                                                  return ((IConstructor)($RVF.constructor(Symbol_map_Symbol_Symbol, new IValue[]{((IConstructor)($me.lub(((IConstructor)($arg0_203)), ((IConstructor)($arg0_201))))), ((IConstructor)($me.lub(((IConstructor)($arg1_202)), ((IConstructor)($arg1_200)))))})));
                                               
                                               }
                                            }
                                         }
                                      }
                                   } else {
                                      return null;
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(18892,92,<339,0>,<339,92>) 
    public IConstructor Type_lub$fdf7de38b8e6e752(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_bag_Symbol, 1)){
           IValue $arg0_205 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_205.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_bag_Symbol, 1)){
                 IValue $arg0_204 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_204.getType(), ADT_Symbol)){
                    IConstructor t_1 = null;
                    return ((IConstructor)($RVF.constructor(Symbol_bag_Symbol, new IValue[]{((IConstructor)($me.lub(((IConstructor)($arg0_205)), ((IConstructor)($arg0_204)))))})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(18985,90,<340,0>,<340,90>) 
    public IConstructor Type_lub$9f97945a73fdc528(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_adt_str_list_Symbol, 2)){
           IValue $arg0_207 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_207.getType(), $T2)){
              IString n_0 = null;
              IValue $arg1_206 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_206.getType(), $T0)){
                 if($has_type_and_arity($1, Symbol_node_, 0)){
                    return ((IConstructor)($RVF.constructor(Symbol_node_, new IValue[]{})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(19076,82,<341,0>,<341,82>) 
    public IConstructor Type_lub$3503ff016335f16b(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_node_, 0)){
           if($has_type_and_arity($1, Symbol_adt_str_list_Symbol, 2)){
              IValue $arg0_209 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
              if($isComparable($arg0_209.getType(), $T2)){
                 IString n_0 = null;
                 IValue $arg1_208 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                 if($isComparable($arg1_208.getType(), $T0)){
                    return ((IConstructor)($RVF.constructor(Symbol_node_, new IValue[]{})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(19159,257,<342,0>,<342,257>) 
    public IConstructor Type_lub$a720eedc169409ce(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_adt_str_list_Symbol, 2)){
           IValue $arg0_213 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_213.getType(), $T2)){
              IString n_0 = ((IString)($arg0_213));
              IValue $arg1_212 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_212.getType(), $T0)){
                 IList lp_1 = ((IList)($arg1_212));
                 if($has_type_and_arity($1, Symbol_adt_str_list_Symbol, 2)){
                    IValue $arg0_211 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                    if($isComparable($arg0_211.getType(), $T2)){
                       if(($arg0_213 != null)){
                          if($arg0_213.match($arg0_211)){
                             IValue $arg1_210 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                             if($isComparable($arg1_210.getType(), $T0)){
                                IList rp_2 = ((IList)($arg1_210));
                                if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg1_212))))), ((IInteger)(M_List.size(((IList)($arg1_210))))))))).getValue()){
                                   if((((IBool)($equal(((IList)($me.getParamLabels(((IList)($arg1_212))))), ((IList)($me.getParamLabels(((IList)($arg1_210))))))))).getValue()){
                                      if((((IBool)($aint_lessequal_aint(((IInteger)(M_List.size(((IList)($me.getParamLabels(((IList)($arg1_212)))))))),((IInteger)$constants.get(4)/*0*/)).not()))).getValue()){
                                         return ((IConstructor)($RVF.constructor(Symbol_adt_str_list_Symbol, new IValue[]{((IString)($arg0_211)), ((IList)($me.addParamLabels(((IList)($me.lub(((IList)($arg1_212)), ((IList)($arg1_210))))), ((IList)($me.getParamLabels(((IList)($arg1_212))))))))})));
                                      
                                      } else {
                                         return null;
                                      }
                                   } else {
                                      return null;
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          $arg0_213 = ((IValue)($arg0_211));
                          IValue $arg1_210 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                          if($isComparable($arg1_210.getType(), $T0)){
                             IList rp_2 = ((IList)($arg1_210));
                             if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg1_212))))), ((IInteger)(M_List.size(((IList)($arg1_210))))))))).getValue()){
                                if((((IBool)($equal(((IList)($me.getParamLabels(((IList)($arg1_212))))), ((IList)($me.getParamLabels(((IList)($arg1_210))))))))).getValue()){
                                   if((((IBool)($aint_lessequal_aint(((IInteger)(M_List.size(((IList)($me.getParamLabels(((IList)($arg1_212)))))))),((IInteger)$constants.get(4)/*0*/)).not()))).getValue()){
                                      return ((IConstructor)($RVF.constructor(Symbol_adt_str_list_Symbol, new IValue[]{((IString)($arg0_211)), ((IList)($me.addParamLabels(((IList)($me.lub(((IList)($arg1_212)), ((IList)($arg1_210))))), ((IList)($me.getParamLabels(((IList)($arg1_212))))))))})));
                                   
                                   } else {
                                      return null;
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(19417,179,<343,0>,<343,179>) 
    public IConstructor Type_lub$771c2324f17afc4f(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_adt_str_list_Symbol, 2)){
           IValue $arg0_217 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_217.getType(), $T2)){
              IString n_0 = ((IString)($arg0_217));
              IValue $arg1_216 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_216.getType(), $T0)){
                 IList lp_1 = ((IList)($arg1_216));
                 if($has_type_and_arity($1, Symbol_adt_str_list_Symbol, 2)){
                    IValue $arg0_215 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                    if($isComparable($arg0_215.getType(), $T2)){
                       if(($arg0_217 != null)){
                          if($arg0_217.match($arg0_215)){
                             IValue $arg1_214 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                             if($isComparable($arg1_214.getType(), $T0)){
                                IList rp_2 = ((IList)($arg1_214));
                                if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg1_216))))), ((IInteger)(M_List.size(((IList)($arg1_214))))))))).getValue()){
                                   if((((IBool)($equal(((IInteger)(M_List.size(((IList)($me.getParamLabels(((IList)($arg1_216)))))))), ((IInteger)$constants.get(4)/*0*/))))).getValue()){
                                      return ((IConstructor)($RVF.constructor(Symbol_adt_str_list_Symbol, new IValue[]{((IString)($arg0_215)), ((IList)($me.lub(((IList)($arg1_216)), ((IList)($arg1_214)))))})));
                                   
                                   } else {
                                      return null;
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          $arg0_217 = ((IValue)($arg0_215));
                          IValue $arg1_214 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                          if($isComparable($arg1_214.getType(), $T0)){
                             IList rp_2 = ((IList)($arg1_214));
                             if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg1_216))))), ((IInteger)(M_List.size(((IList)($arg1_214))))))))).getValue()){
                                if((((IBool)($equal(((IInteger)(M_List.size(((IList)($me.getParamLabels(((IList)($arg1_216)))))))), ((IInteger)$constants.get(4)/*0*/))))).getValue()){
                                   return ((IConstructor)($RVF.constructor(Symbol_adt_str_list_Symbol, new IValue[]{((IString)($arg0_215)), ((IList)($me.lub(((IList)($arg1_216)), ((IList)($arg1_214)))))})));
                                
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(19597,124,<344,0>,<344,124>) 
    public IConstructor Type_lub$03e04b7cfdcf0b97(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_adt_str_list_Symbol, 2)){
           IValue $arg0_221 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_221.getType(), $T2)){
              IString n_0 = null;
              IValue $arg1_220 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_220.getType(), $T0)){
                 IList lp_1 = null;
                 if($has_type_and_arity($1, Symbol_adt_str_list_Symbol, 2)){
                    IValue $arg0_219 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                    if($isComparable($arg0_219.getType(), $T2)){
                       IString m_2 = null;
                       IValue $arg1_218 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                       if($isComparable($arg1_218.getType(), $T0)){
                          IList rp_3 = null;
                          if((((IBool)($equal(((IString)($arg0_221)),((IString)($arg0_219))).not()))).getValue()){
                             return ((IConstructor)($RVF.constructor(Symbol_node_, new IValue[]{})));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(19722,130,<345,0>,<345,130>) 
    public IConstructor Type_lub$4d768567dbe9a5cf(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_adt_str_list_Symbol, 2)){
           IValue $arg0_226 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_226.getType(), $T2)){
              IString ln_0 = null;
              IValue $arg1_225 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_225.getType(), $T0)){
                 IList lp_1 = null;
                 if($has_type_and_arity($1, Symbol_cons_Symbol_str_list_Symbol, 3)){
                    IValue $arg0_224 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                    if($isComparable($arg0_224.getType(), ADT_Symbol)){
                       IConstructor b_2 = null;
                       IValue $arg1_223 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                       if($isComparable($arg1_223.getType(), $T4)){
                          IValue $arg2_222 = (IValue)($aadt_subscript_int(((IConstructor)$1),2));
                          if($isComparable($arg2_222.getType(), $T0)){
                             return ((IConstructor)($me.lub(((IConstructor)($RVF.constructor(Symbol_adt_str_list_Symbol, new IValue[]{((IString)($arg0_226)), ((IList)($arg1_225))}))), ((IConstructor)($arg0_224)))));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(19854,121,<347,0>,<347,121>) 
    public IConstructor Type_lub$9c3d6ec5108762e5(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_cons_Symbol_str_list_Symbol, 3)){
           IValue $arg0_232 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_232.getType(), ADT_Symbol)){
              IConstructor la_0 = null;
              IValue $arg1_231 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_231.getType(), $T4)){
                 IValue $arg2_230 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_230.getType(), $T0)){
                    if($has_type_and_arity($1, Symbol_cons_Symbol_str_list_Symbol, 3)){
                       IValue $arg0_229 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                       if($isComparable($arg0_229.getType(), ADT_Symbol)){
                          IConstructor ra_1 = null;
                          IValue $arg1_228 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                          if($isComparable($arg1_228.getType(), $T4)){
                             IValue $arg2_227 = (IValue)($aadt_subscript_int(((IConstructor)$1),2));
                             if($isComparable($arg2_227.getType(), $T0)){
                                return ((IConstructor)($me.lub(((IConstructor)($arg0_232)), ((IConstructor)($arg0_229)))));
                             
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(19976,129,<348,0>,<348,129>) 
    public IConstructor Type_lub$db14cac8db6fcb97(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_cons_Symbol_str_list_Symbol, 3)){
           IValue $arg0_237 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_237.getType(), ADT_Symbol)){
              IConstructor a_0 = null;
              IValue $arg1_236 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_236.getType(), $T4)){
                 IValue $arg2_235 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_235.getType(), $T0)){
                    IList lp_1 = null;
                    if($has_type_and_arity($1, Symbol_adt_str_list_Symbol, 2)){
                       IValue $arg0_234 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                       if($isComparable($arg0_234.getType(), $T2)){
                          IString n_2 = null;
                          IValue $arg1_233 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                          if($isComparable($arg1_233.getType(), $T0)){
                             IList rp_3 = null;
                             return ((IConstructor)($me.lub(((IConstructor)($arg0_237)), ((IConstructor)($RVF.constructor(Symbol_adt_str_list_Symbol, new IValue[]{((IString)($arg0_234)), ((IList)($arg1_233))}))))));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(20106,97,<349,0>,<349,97>) 
    public IConstructor Type_lub$ce98626caf2b01c0(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_cons_Symbol_str_list_Symbol, 3)){
           IValue $arg0_240 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_240.getType(), ADT_Symbol)){
              IValue $arg1_239 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_239.getType(), $T4)){
                 IValue $arg2_238 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_238.getType(), $T0)){
                    if($has_type_and_arity($1, Symbol_node_, 0)){
                       return ((IConstructor)($RVF.constructor(Symbol_node_, new IValue[]{})));
                    
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(20205,101,<351,0>,<351,101>) 
    public IConstructor Type_lub$2bf17a517ba3b924(IConstructor $0, IConstructor r_1){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_243 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_243.getType(), $T2)){
              IValue $arg1_242 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_242.getType(), $T0)){
                 IValue $arg2_241 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_241.getType(), ADT_Symbol)){
                    IConstructor aliased_0 = null;
                    return ((IConstructor)($me.lub(((IConstructor)($arg2_241)), ((IConstructor)r_1))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(20307,93,<352,0>,<352,93>) 
    public IConstructor Type_lub$df08b24dcf17ae58(IConstructor l_0, IConstructor $1){ 
        
        
        if($has_type_and_arity($1, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_246 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
           if($isComparable($arg0_246.getType(), $T2)){
              IValue $arg1_245 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
              if($isComparable($arg1_245.getType(), $T0)){
                 IValue $arg2_244 = (IValue)($aadt_subscript_int(((IConstructor)$1),2));
                 if($isComparable($arg2_244.getType(), ADT_Symbol)){
                    IConstructor aliased_1 = null;
                    return ((IConstructor)($me.lub(((IConstructor)l_0), ((IConstructor)($arg2_244)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(20402,149,<354,0>,<354,149>) 
    public IBool Type_keepParams$932e8cdb74f21297(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_251 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_251.getType(), $T2)){
              IString s1_0 = ((IString)($arg0_251));
              IValue $arg1_250 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_250.getType(), ADT_Symbol)){
                 IConstructor bound1_1 = ((IConstructor)($arg1_250));
                 if($has_type_and_arity($1, Symbol_parameter_str_Symbol, 2)){
                    IValue $arg0_249 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                    if($isComparable($arg0_249.getType(), $T2)){
                       IString s2_2 = ((IString)($arg0_249));
                       IValue $arg1_248 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                       if($isComparable($arg1_248.getType(), ADT_Symbol)){
                          IConstructor bound2_3 = ((IConstructor)($arg1_248));
                          if((((IBool)($equal(((IString)($arg0_251)), ((IString)($arg0_249)))))).getValue()){
                             return ((IBool)($me.equivalent(((IConstructor)($arg1_250)), ((IConstructor)($arg1_248)))));
                          
                          } else {
                             return ((IBool)$constants.get(2)/*false*/);
                          
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(20553,147,<356,0>,<356,147>) 
    public IConstructor Type_lub$dbf1dfef76adf321(IConstructor l_0, IConstructor r_3){ 
        
        
        if($has_type_and_arity(l_0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_255 = (IValue)($aadt_subscript_int(((IConstructor)l_0),0));
           if($isComparable($arg0_255.getType(), $T2)){
              IString s1_1 = ((IString)($arg0_255));
              IValue $arg1_254 = (IValue)($aadt_subscript_int(((IConstructor)l_0),1));
              if($isComparable($arg1_254.getType(), ADT_Symbol)){
                 IConstructor bound1_2 = ((IConstructor)($arg1_254));
                 if($has_type_and_arity(r_3, Symbol_parameter_str_Symbol, 2)){
                    IValue $arg0_253 = (IValue)($aadt_subscript_int(((IConstructor)r_3),0));
                    if($isComparable($arg0_253.getType(), $T2)){
                       IString s2_4 = ((IString)($arg0_253));
                       IValue $arg1_252 = (IValue)($aadt_subscript_int(((IConstructor)r_3),1));
                       if($isComparable($arg1_252.getType(), ADT_Symbol)){
                          IConstructor bound2_5 = ((IConstructor)($arg1_252));
                          if((((IBool)($me.keepParams(((IConstructor)l_0), ((IConstructor)r_3))))).getValue()){
                             return ((IConstructor)l_0);
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(20701,165,<357,0>,<357,165>) 
    public IConstructor Type_lub$e8aaf393ebf72b9b(IConstructor l_0, IConstructor r_3){ 
        
        
        if($has_type_and_arity(l_0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_259 = (IValue)($aadt_subscript_int(((IConstructor)l_0),0));
           if($isComparable($arg0_259.getType(), $T2)){
              IString s1_1 = ((IString)($arg0_259));
              IValue $arg1_258 = (IValue)($aadt_subscript_int(((IConstructor)l_0),1));
              if($isComparable($arg1_258.getType(), ADT_Symbol)){
                 IConstructor bound1_2 = ((IConstructor)($arg1_258));
                 if($has_type_and_arity(r_3, Symbol_parameter_str_Symbol, 2)){
                    IValue $arg0_257 = (IValue)($aadt_subscript_int(((IConstructor)r_3),0));
                    if($isComparable($arg0_257.getType(), $T2)){
                       IString s2_4 = ((IString)($arg0_257));
                       IValue $arg1_256 = (IValue)($aadt_subscript_int(((IConstructor)r_3),1));
                       if($isComparable($arg1_256.getType(), ADT_Symbol)){
                          IConstructor bound2_5 = ((IConstructor)($arg1_256));
                          if((((IBool)($me.keepParams(((IConstructor)l_0), ((IConstructor)r_3))))).getValue()){
                             return null;
                          } else {
                             return ((IConstructor)($me.lub(((IConstructor)($arg1_258)), ((IConstructor)($arg1_256)))));
                          
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(20867,106,<358,0>,<358,106>) 
    public IConstructor Type_lub$11e6e64c52d2b7bc(IConstructor $0, IConstructor r_1){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_261 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_261.getType(), $T2)){
              IValue $arg1_260 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_260.getType(), ADT_Symbol)){
                 IConstructor bound_0 = null;
                 if((((IBool)($me.isTypeVar(((IConstructor)r_1))))).getValue()){
                    return null;
                 } else {
                    return ((IConstructor)($me.lub(((IConstructor)($arg1_260)), ((IConstructor)r_1))));
                 
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(20974,106,<359,0>,<359,106>) 
    public IConstructor Type_lub$afcfe5adb1fb93eb(IConstructor l_0, IConstructor $1){ 
        
        
        if($has_type_and_arity($1, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_263 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
           if($isComparable($arg0_263.getType(), $T2)){
              IValue $arg1_262 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
              if($isComparable($arg1_262.getType(), ADT_Symbol)){
                 IConstructor bound_1 = null;
                 if((((IBool)($me.isTypeVar(((IConstructor)l_0))))).getValue()){
                    return null;
                 } else {
                    return ((IConstructor)($me.lub(((IConstructor)l_0), ((IConstructor)($arg1_262)))));
                 
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(21082,103,<361,0>,<361,103>) 
    public IConstructor Type_lub$4c51769afaf2977a(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_reified_Symbol, 1)){
           IValue $arg0_265 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_265.getType(), ADT_Symbol)){
              IConstructor l_0 = null;
              if($has_type_and_arity($1, Symbol_reified_Symbol, 1)){
                 IValue $arg0_264 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_264.getType(), ADT_Symbol)){
                    IConstructor r_1 = null;
                    return ((IConstructor)($RVF.constructor(Symbol_reified_Symbol, new IValue[]{((IConstructor)($me.lub(((IConstructor)($arg0_265)), ((IConstructor)($arg0_264)))))})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(21186,81,<362,0>,<362,81>) 
    public IConstructor Type_lub$ba70259261126d74(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_reified_Symbol, 1)){
           IValue $arg0_266 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_266.getType(), ADT_Symbol)){
              IConstructor l_0 = null;
              if($has_type_and_arity($1, Symbol_node_, 0)){
                 return ((IConstructor)($RVF.constructor(Symbol_node_, new IValue[]{})));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(21269,406,<364,0>,<373,1>) 
    public IConstructor Type_lub$21fcb18b79a5d185(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_func_Symbol_list_Symbol_list_Symbol, 3)){
           IValue $arg0_273 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_273.getType(), ADT_Symbol)){
              IConstructor lr_0 = null;
              IValue $arg1_272 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_272.getType(), $T0)){
                 IList lp_1 = null;
                 IValue $arg2_271 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_271.getType(), $T0)){
                    IList lkw_2 = null;
                    if($has_type_and_arity($1, Symbol_func_Symbol_list_Symbol_list_Symbol, 3)){
                       IValue $arg0_270 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                       if($isComparable($arg0_270.getType(), ADT_Symbol)){
                          IConstructor rr_3 = null;
                          IValue $arg1_269 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                          if($isComparable($arg1_269.getType(), $T0)){
                             IList rp_4 = null;
                             IValue $arg2_268 = (IValue)($aadt_subscript_int(((IConstructor)$1),2));
                             if($isComparable($arg2_268.getType(), $T0)){
                                IList rkw_5 = null;
                                IConstructor lubReturn_6 = ((IConstructor)($me.lub(((IConstructor)($arg0_273)), ((IConstructor)($arg0_270)))));
                                IConstructor lubParams_7 = ((IConstructor)($me.glb(((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($arg1_272))}))), ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($arg1_269))}))))));
                                if((((IBool)($me.isTupleType(((IConstructor)lubParams_7))))).getValue()){
                                   return ((IConstructor)($RVF.constructor(Symbol_func_Symbol_list_Symbol_list_Symbol, new IValue[]{((IConstructor)lubReturn_6), ((IList)(((IList)($aadt_get_field(((IConstructor)lubParams_7), "symbols"))))), ((IList)(((((IBool)($equal(((IList)($arg2_271)), ((IList)($arg2_268)))))).getValue() ? $arg2_271 : ((IList)$constants.get(0)/*[]*/))))})));
                                
                                } else {
                                   return ((IConstructor)($RVF.constructor(Symbol_value_, new IValue[]{})));
                                
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(21677,67,<375,0>,<375,67>) 
    public IConstructor Type_lub$9915f83b2e9ee2f9(IConstructor $0, IConstructor r_1){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_275 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_275.getType(), $T4)){
              IValue $arg1_274 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_274.getType(), ADT_Symbol)){
                 IConstructor l_0 = null;
                 return ((IConstructor)($me.lub(((IConstructor)($arg1_274)), ((IConstructor)r_1))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(21745,67,<376,0>,<376,67>) 
    public IConstructor Type_lub$d3a3e906a73734c7(IConstructor l_0, IConstructor $1){ 
        
        
        if($has_type_and_arity($1, Symbol_label_str_Symbol, 2)){
           IValue $arg0_277 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
           if($isComparable($arg0_277.getType(), $T4)){
              IValue $arg1_276 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
              if($isComparable($arg1_276.getType(), ADT_Symbol)){
                 IConstructor r_1 = null;
                 return ((IConstructor)($me.lub(((IConstructor)l_0), ((IConstructor)($arg1_276)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(21814,121,<378,0>,<378,121>) 
    public IList Type_lub$b4ef79728aa03e7e(IList l_0, IList r_1){ 
        
        
        if((((IBool)($equal(((IInteger)(M_List.size(((IList)l_0)))), ((IInteger)(M_List.size(((IList)r_1)))))))).getValue()){
           final IListWriter $listwriter278 = (IListWriter)$RVF.listWriter();
           $LCOMP279_GEN21894:
           for(IValue $elem280_for : ((IList)(M_List.index(((IList)l_0))))){
               IInteger $elem280 = (IInteger) $elem280_for;
               IInteger idx_2 = null;
               $listwriter278.append($me.lub(((IConstructor)($alist_subscript_int(((IList)l_0),((IInteger)($elem280)).intValue()))), ((IConstructor)($alist_subscript_int(((IList)r_1),((IInteger)($elem280)).intValue())))));
           
           }
           
                       return ((IList)($listwriter278.done()));
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(21937,77,<379,0>,<379,77>) 
    public IList Type_lub$2ad0993f943ef630(IList l_0, IList r_1){ 
        
        
        return ((IList)$constants.get(7)/*[value()]*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(22017,82,<381,0>,<381,82>) 
    public IBool Type_allLabeled$448fbafcf2280718(IList l_0){ 
        
        
        IBool $done282 = (IBool)(((IBool)$constants.get(1)/*true*/));
        $ALL283_GEN22063:
        for(IValue $elem286_for : ((IList)l_0)){
            IConstructor $elem286 = (IConstructor) $elem286_for;
            IConstructor li_1 = null;
            if($has_type_and_arity($elem286, Symbol_label_str_Symbol, 2)){
               IValue $arg0_285 = (IValue)($aadt_subscript_int(((IConstructor)($elem286)),0));
               if($isComparable($arg0_285.getType(), $T4)){
                  IValue $arg1_284 = (IValue)($aadt_subscript_int(((IConstructor)($elem286)),1));
                  if($isComparable($arg1_284.getType(), $T4)){
                     continue $ALL283_GEN22063;
                  
                  } else {
                     $done282 = ((IBool)$constants.get(2)/*false*/);
                     break $ALL283_GEN22063; // muBreak
                  
                  }
               } else {
                  $done282 = ((IBool)$constants.get(2)/*false*/);
                  break $ALL283_GEN22063; // muBreak
               
               }
            } else {
               $done282 = ((IBool)$constants.get(2)/*false*/);
               break $ALL283_GEN22063; // muBreak
            
            }
        }
        
                    return ((IBool)($done282));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(22100,84,<382,0>,<382,84>) 
    public IBool Type_noneLabeled$59f0d0b15364377f(IList l_0){ 
        
        
        IBool $done288 = (IBool)(((IBool)$constants.get(1)/*true*/));
        $ALL289_GEN22147:
        for(IValue $elem292_for : ((IList)l_0)){
            IConstructor $elem292 = (IConstructor) $elem292_for;
            IConstructor li_1 = null;
            if($has_type_and_arity($elem292, Symbol_label_str_Symbol, 2)){
               IValue $arg0_291 = (IValue)($aadt_subscript_int(((IConstructor)($elem292)),0));
               if($isComparable($arg0_291.getType(), $T4)){
                  IValue $arg1_290 = (IValue)($aadt_subscript_int(((IConstructor)($elem292)),1));
                  if($isComparable($arg1_290.getType(), $T4)){
                     $done288 = ((IBool)$constants.get(2)/*false*/);
                     break $ALL289_GEN22147; // muBreak
                  
                  } else {
                     continue $ALL289_GEN22147;
                  
                  }
               } else {
                  $done288 = ((IBool)$constants.get(2)/*false*/);
                  break $ALL289_GEN22147; // muBreak
               
               }
            } else {
               $done288 = ((IBool)$constants.get(2)/*false*/);
               break $ALL289_GEN22147; // muBreak
            
            }
        }
        
                    return ((IBool)($done288));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(22185,89,<383,0>,<383,89>) 
    public IList Type_getLabels$75df4fafda7b0225(IList l_0){ 
        
        
        final IListWriter $listwriter293 = (IListWriter)$RVF.listWriter();
        $LCOMP294_GEN22237:
        for(IValue $elem297_for : ((IList)l_0)){
            IConstructor $elem297 = (IConstructor) $elem297_for;
            IConstructor li_1 = null;
            if($has_type_and_arity($elem297, Symbol_label_str_Symbol, 2)){
               IValue $arg0_296 = (IValue)($aadt_subscript_int(((IConstructor)($elem297)),0));
               if($isComparable($arg0_296.getType(), $T2)){
                  IString s_2 = null;
                  IValue $arg1_295 = (IValue)($aadt_subscript_int(((IConstructor)($elem297)),1));
                  if($isComparable($arg1_295.getType(), $T4)){
                     $listwriter293.append($arg0_296);
                  
                  } else {
                     continue $LCOMP294_GEN22237;
                  }
               } else {
                  continue $LCOMP294_GEN22237;
               }
            } else {
               continue $LCOMP294_GEN22237;
            }
        }
        
                    return ((IList)($listwriter293.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(22275,138,<384,0>,<384,138>) 
    public IList Type_addLabels$981aeb1962053f6d(IList l_0, IList s_1){ 
        
        
        if((((IBool)($equal(((IInteger)(M_List.size(((IList)l_0)))), ((IInteger)(M_List.size(((IList)s_1)))))))).getValue()){
           final IListWriter $listwriter298 = (IListWriter)$RVF.listWriter();
           $LCOMP299_GEN22371:
           for(IValue $elem300_for : ((IList)(M_List.index(((IList)l_0))))){
               IInteger $elem300 = (IInteger) $elem300_for;
               IInteger idx_2 = null;
               $listwriter298.append($RVF.constructor(Symbol_label_str_Symbol, new IValue[]{((IString)($alist_subscript_int(((IList)s_1),((IInteger)($elem300)).intValue()))), ((IConstructor)($alist_subscript_int(((IList)l_0),((IInteger)($elem300)).intValue())))}));
           
           }
           
                       return ((IList)($listwriter298.done()));
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(22414,136,<385,0>,<385,136>) 
    public IList Type_addLabels$c61a5cc3caa0d718(IList l_0, IList s_1){ 
        
        
        final Template $template301 = (Template)new Template($RVF, "Length of symbol list ");
        $template301.beginIndent("                      ");
        $template301.addVal(l_0);
        $template301.endIndent("                      ");
        $template301.addStr(" and label list ");
        $template301.beginIndent("                ");
        $template301.addVal(s_1);
        $template301.endIndent("                ");
        $template301.addStr(" much match");
        throw new Throw($template301.close());
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(22551,102,<386,0>,<386,102>) 
    public IList Type_stripLabels$476c8af6f8a4ff77(IList l_0){ 
        
        
        final IListWriter $listwriter302 = (IListWriter)$RVF.listWriter();
        $LCOMP303_GEN22643:
        for(IValue $elem307_for : ((IList)l_0)){
            IConstructor $elem307 = (IConstructor) $elem307_for;
            IConstructor li_2 = null;
            IValue $arg0_306 = (IValue)($aadt_subscript_int(((IConstructor)($elem307)),0));
            IValue $arg1_305 = (IValue)($aadt_subscript_int(((IConstructor)($elem307)),1));
            IConstructor v_1 = null;
            $listwriter302.append(($has_type_and_arity($elem307, Symbol_label_str_Symbol, 2) ? ($isComparable($arg0_306.getType(), $T4) ? ($isComparable($arg1_305.getType(), ADT_Symbol) ? $arg1_305 : $elem307) : $elem307) : $elem307));
        
        }
        
                    return ((IList)($listwriter302.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(22656,98,<388,0>,<388,98>) 
    public IList Type_getParamLabels$d148bd4a03929246(IList l_0){ 
        
        
        final IListWriter $listwriter308 = (IListWriter)$RVF.listWriter();
        $LCOMP309_GEN22713:
        for(IValue $elem312_for : ((IList)l_0)){
            IConstructor $elem312 = (IConstructor) $elem312_for;
            IConstructor li_1 = null;
            if($has_type_and_arity($elem312, Symbol_parameter_str_Symbol, 2)){
               IValue $arg0_311 = (IValue)($aadt_subscript_int(((IConstructor)($elem312)),0));
               if($isComparable($arg0_311.getType(), $T2)){
                  IString s_2 = null;
                  IValue $arg1_310 = (IValue)($aadt_subscript_int(((IConstructor)($elem312)),1));
                  if($isComparable($arg1_310.getType(), $T4)){
                     $listwriter308.append($arg0_311);
                  
                  } else {
                     continue $LCOMP309_GEN22713;
                  }
               } else {
                  continue $LCOMP309_GEN22713;
               }
            } else {
               continue $LCOMP309_GEN22713;
            }
        }
        
                    return ((IList)($listwriter308.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(22755,147,<389,0>,<389,147>) 
    public IList Type_addParamLabels$9a2fcec6caf1a7af(IList l_0, IList s_1){ 
        
        
        if((((IBool)($equal(((IInteger)(M_List.size(((IList)l_0)))), ((IInteger)(M_List.size(((IList)s_1)))))))).getValue()){
           final IListWriter $listwriter313 = (IListWriter)$RVF.listWriter();
           $LCOMP314_GEN22860:
           for(IValue $elem315_for : ((IList)(M_List.index(((IList)l_0))))){
               IInteger $elem315 = (IInteger) $elem315_for;
               IInteger idx_2 = null;
               $listwriter313.append($RVF.constructor(Symbol_parameter_str_Symbol, new IValue[]{((IString)($alist_subscript_int(((IList)s_1),((IInteger)($elem315)).intValue()))), ((IConstructor)($alist_subscript_int(((IList)l_0),((IInteger)($elem315)).intValue())))}));
           
           }
           
                       return ((IList)($listwriter313.done()));
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(22903,133,<390,0>,<390,133>) 
    public IList Type_addParamLabels$237a32acd91d3f4b(IList l_0, IList s_1){ 
        
        
        throw new Throw(((IString)$constants.get(8)/*"Length of symbol list and label list much match"*/));
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(23039,265,<392,0>,<396,35>) 
    public IConstructor Type_glb$c33fe84981451d5d(IConstructor s_0, IConstructor s){ 
        
        
        if((s_0 != null)){
           if(s_0.match(s)){
              return ((IConstructor)s_0);
           
           } else {
              return null;
           }
        } else {
           s_0 = ((IConstructor)s);
           return ((IConstructor)s_0);
        
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(23305,56,<397,0>,<397,56>) 
    public IConstructor Type_glb$c7e9058fa842d39e(IConstructor s_0, IConstructor t_1){ 
        
        
        return ((IConstructor)$constants.get(9)/*void()*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(23363,63,<399,0>,<399,63>) 
    public IConstructor Type_glb$bb211af2491da9e9(IConstructor $0, IConstructor t_0){ 
        
        
        if($has_type_and_arity($0, Symbol_void_, 0)){
           return ((IConstructor)($RVF.constructor(Symbol_void_, new IValue[]{})));
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(23427,63,<400,0>,<400,63>) 
    public IConstructor Type_glb$4c1d7a7db4cf16ca(IConstructor s_0, IConstructor $1){ 
        
        
        if($has_type_and_arity($1, Symbol_void_, 0)){
           return ((IConstructor)($RVF.constructor(Symbol_void_, new IValue[]{})));
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(23491,50,<401,0>,<401,50>) 
    public IConstructor Type_glb$c62adde378db5726(IConstructor $0, IConstructor t_0){ 
        
        
        if($has_type_and_arity($0, Symbol_value_, 0)){
           return ((IConstructor)t_0);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(23542,50,<402,0>,<402,50>) 
    public IConstructor Type_glb$44d70430e3ea2351(IConstructor s_0, IConstructor $1){ 
        
        
        if($has_type_and_arity($1, Symbol_value_, 0)){
           return ((IConstructor)s_0);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(23594,67,<404,0>,<404,67>) 
    public IConstructor Type_glb$2a7a0f345df74609(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_int_, 0)){
           if($has_type_and_arity($1, Symbol_num_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_int_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(23662,67,<405,0>,<405,67>) 
    public IConstructor Type_glb$1e653208ecb5fb70(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_num_, 0)){
           if($has_type_and_arity($1, Symbol_int_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_int_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(23730,66,<406,0>,<406,66>) 
    public IConstructor Type_glb$f4d1e05744c88722(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rat_, 0)){
           if($has_type_and_arity($1, Symbol_num_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_rat_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(23797,67,<407,0>,<407,67>) 
    public IConstructor Type_glb$e78099bd23f8b441(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_num_, 0)){
           if($has_type_and_arity($1, Symbol_rat_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_rat_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(23865,69,<408,0>,<408,69>) 
    public IConstructor Type_glb$5a78c895f775bd16(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_real_, 0)){
           if($has_type_and_arity($1, Symbol_num_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_real_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(23935,69,<409,0>,<409,69>) 
    public IConstructor Type_glb$60eb504a9ebf23b3(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_num_, 0)){
           if($has_type_and_arity($1, Symbol_real_, 0)){
              return ((IConstructor)($RVF.constructor(Symbol_real_, new IValue[]{})));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(24006,92,<411,0>,<411,92>) 
    public IConstructor Type_glb$ccebb93560b76f62(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_set_Symbol, 1)){
           IValue $arg0_317 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_317.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_set_Symbol, 1)){
                 IValue $arg0_316 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_316.getType(), ADT_Symbol)){
                    IConstructor t_1 = null;
                    return ((IConstructor)($RVF.constructor(Symbol_set_Symbol, new IValue[]{((IConstructor)($me.glb(((IConstructor)($arg0_317)), ((IConstructor)($arg0_316)))))})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(24101,115,<412,0>,<412,115>) 
    public IConstructor Type_glb$a731272cac728f82(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_set_Symbol, 1)){
           IValue $arg0_319 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_319.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_rel_list_Symbol, 1)){
                 IValue $arg0_318 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_318.getType(), $T0)){
                    IList ts_1 = null;
                    return ((IConstructor)($RVF.constructor(Symbol_set_Symbol, new IValue[]{((IConstructor)($me.glb(((IConstructor)($arg0_319)), ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($arg0_318))}))))))})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(24219,107,<413,0>,<413,107>) 
    public IConstructor Type_glb$a786ee037405dc5c(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_321 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_321.getType(), $T0)){
              IList ts_0 = null;
              if($has_type_and_arity($1, Symbol_set_Symbol, 1)){
                 IValue $arg0_320 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_320.getType(), ADT_Symbol)){
                    IConstructor s_1 = null;
                    return ((IConstructor)($RVF.constructor(Symbol_set_Symbol, new IValue[]{((IConstructor)($me.glb(((IConstructor)($arg0_320)), ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($arg0_321))}))))))})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(24328,244,<415,0>,<415,244>) 
    public IConstructor Type_glb$525503476d5dd0c9(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_323 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_323.getType(), $T0)){
              IList l_0 = ((IList)($arg0_323));
              if($has_type_and_arity($1, Symbol_rel_list_Symbol, 1)){
                 IValue $arg0_322 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_322.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_322));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_323))))), ((IInteger)(M_List.size(((IList)($arg0_322))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_323)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_322)))))).getValue()){
                             if((((IBool)($equal(((IList)($me.getLabels(((IList)($arg0_323))))), ((IList)($me.getLabels(((IList)($arg0_322))))))))).getValue()){
                                return ((IConstructor)($RVF.constructor(Symbol_rel_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.glb(((IList)($me.stripLabels(((IList)($arg0_323))))), ((IList)($me.stripLabels(((IList)($arg0_322)))))))), ((IList)($me.getLabels(((IList)($arg0_323))))))))})));
                             
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(24573,220,<416,0>,<416,220>) 
    public IConstructor Type_glb$3f1318612901961d(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_325 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_325.getType(), $T0)){
              IList l_0 = ((IList)($arg0_325));
              if($has_type_and_arity($1, Symbol_rel_list_Symbol, 1)){
                 IValue $arg0_324 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_324.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_324));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_325))))), ((IInteger)(M_List.size(((IList)($arg0_324))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_325)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_324)))))).getValue()){
                             if((((IBool)($equal(((IList)($me.getLabels(((IList)($arg0_325))))),((IList)($me.getLabels(((IList)($arg0_324)))))).not()))).getValue()){
                                return ((IConstructor)($RVF.constructor(Symbol_rel_list_Symbol, new IValue[]{((IList)($me.glb(((IList)($me.stripLabels(((IList)($arg0_325))))), ((IList)($me.stripLabels(((IList)($arg0_324))))))))})));
                             
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(24794,213,<417,0>,<417,213>) 
    public IConstructor Type_glb$1d00df4abea8627d(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_327 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_327.getType(), $T0)){
              IList l_0 = ((IList)($arg0_327));
              if($has_type_and_arity($1, Symbol_rel_list_Symbol, 1)){
                 IValue $arg0_326 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_326.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_326));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_327))))), ((IInteger)(M_List.size(((IList)($arg0_326))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_327)))))).getValue()){
                          if((((IBool)($me.noneLabeled(((IList)($arg0_326)))))).getValue()){
                             return ((IConstructor)($RVF.constructor(Symbol_rel_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.glb(((IList)($me.stripLabels(((IList)($arg0_327))))), ((IList)($me.stripLabels(((IList)($arg0_326)))))))), ((IList)($me.getLabels(((IList)($arg0_327))))))))})));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(25008,213,<418,0>,<418,213>) 
    public IConstructor Type_glb$22a77fe40bb560cb(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_329 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_329.getType(), $T0)){
              IList l_0 = ((IList)($arg0_329));
              if($has_type_and_arity($1, Symbol_rel_list_Symbol, 1)){
                 IValue $arg0_328 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_328.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_328));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_329))))), ((IInteger)(M_List.size(((IList)($arg0_328))))))))).getValue()){
                       if((((IBool)($me.noneLabeled(((IList)($arg0_329)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_328)))))).getValue()){
                             return ((IConstructor)($RVF.constructor(Symbol_rel_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.glb(((IList)($me.stripLabels(((IList)($arg0_329))))), ((IList)($me.stripLabels(((IList)($arg0_328)))))))), ((IList)($me.getLabels(((IList)($arg0_328))))))))})));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(25222,190,<419,0>,<419,190>) 
    public IConstructor Type_glb$9665334317df7954(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_331 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_331.getType(), $T0)){
              IList l_0 = ((IList)($arg0_331));
              if($has_type_and_arity($1, Symbol_rel_list_Symbol, 1)){
                 IValue $arg0_330 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_330.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_330));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_331))))), ((IInteger)(M_List.size(((IList)($arg0_330))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_331)))))).getValue()){
                          return null;
                       } else {
                          if((((IBool)($me.allLabeled(((IList)($arg0_330)))))).getValue()){
                             return null;
                          } else {
                             return ((IConstructor)($RVF.constructor(Symbol_rel_list_Symbol, new IValue[]{((IList)($me.glb(((IList)($me.stripLabels(((IList)($arg0_331))))), ((IList)($me.stripLabels(((IList)($arg0_330))))))))})));
                          
                          }
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(25413,119,<420,0>,<420,119>) 
    public IConstructor Type_glb$fedc2ca18d852381(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_333 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_333.getType(), $T0)){
              IList l_0 = ((IList)($arg0_333));
              if($has_type_and_arity($1, Symbol_rel_list_Symbol, 1)){
                 IValue $arg0_332 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_332.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_332));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_333))))),((IInteger)(M_List.size(((IList)($arg0_332)))))).not()))).getValue()){
                       return ((IConstructor)$constants.get(6)/*set(value())*/);
                    
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(25534,95,<422,0>,<422,95>) 
    public IConstructor Type_glb$4b91e5fe473aa816(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_list_Symbol, 1)){
           IValue $arg0_335 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_335.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_list_Symbol, 1)){
                 IValue $arg0_334 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_334.getType(), ADT_Symbol)){
                    IConstructor t_1 = null;
                    return ((IConstructor)($RVF.constructor(Symbol_list_Symbol, new IValue[]{((IConstructor)($me.glb(((IConstructor)($arg0_335)), ((IConstructor)($arg0_334)))))})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(25632,118,<423,0>,<423,118>) 
    public IConstructor Type_glb$563c14f02706f88a(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_list_Symbol, 1)){
           IValue $arg0_337 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_337.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_lrel_list_Symbol, 1)){
                 IValue $arg0_336 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_336.getType(), $T0)){
                    IList ts_1 = null;
                    return ((IConstructor)($RVF.constructor(Symbol_list_Symbol, new IValue[]{((IConstructor)($me.glb(((IConstructor)($arg0_337)), ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($arg0_336))}))))))})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(25753,118,<424,0>,<424,118>) 
    public IConstructor Type_glb$0f0ddb52abd0ac6a(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_339 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_339.getType(), $T0)){
              IList ts_0 = null;
              if($has_type_and_arity($1, Symbol_list_Symbol, 1)){
                 IValue $arg0_338 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_338.getType(), ADT_Symbol)){
                    IConstructor s_1 = null;
                    return ((IConstructor)($RVF.constructor(Symbol_list_Symbol, new IValue[]{((IConstructor)($me.glb(((IConstructor)($arg0_338)), ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($arg0_339))}))))))})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(25873,247,<426,0>,<426,247>) 
    public IConstructor Type_glb$fc31ec41aa84d7a9(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_341 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_341.getType(), $T0)){
              IList l_0 = ((IList)($arg0_341));
              if($has_type_and_arity($1, Symbol_lrel_list_Symbol, 1)){
                 IValue $arg0_340 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_340.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_340));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_341))))), ((IInteger)(M_List.size(((IList)($arg0_340))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_341)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_340)))))).getValue()){
                             if((((IBool)($equal(((IList)($me.getLabels(((IList)($arg0_341))))), ((IList)($me.getLabels(((IList)($arg0_340))))))))).getValue()){
                                return ((IConstructor)($RVF.constructor(Symbol_lrel_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.glb(((IList)($me.stripLabels(((IList)($arg0_341))))), ((IList)($me.stripLabels(((IList)($arg0_340)))))))), ((IList)($me.getLabels(((IList)($arg0_341))))))))})));
                             
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(26121,223,<427,0>,<427,223>) 
    public IConstructor Type_glb$c86a7a40892f2e13(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_343 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_343.getType(), $T0)){
              IList l_0 = ((IList)($arg0_343));
              if($has_type_and_arity($1, Symbol_lrel_list_Symbol, 1)){
                 IValue $arg0_342 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_342.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_342));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_343))))), ((IInteger)(M_List.size(((IList)($arg0_342))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_343)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_342)))))).getValue()){
                             if((((IBool)($equal(((IList)($me.getLabels(((IList)($arg0_343))))),((IList)($me.getLabels(((IList)($arg0_342)))))).not()))).getValue()){
                                return ((IConstructor)($RVF.constructor(Symbol_lrel_list_Symbol, new IValue[]{((IList)($me.glb(((IList)($me.stripLabels(((IList)($arg0_343))))), ((IList)($me.stripLabels(((IList)($arg0_342))))))))})));
                             
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(26345,216,<428,0>,<428,216>) 
    public IConstructor Type_glb$a57301fdb26a70d4(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_345 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_345.getType(), $T0)){
              IList l_0 = ((IList)($arg0_345));
              if($has_type_and_arity($1, Symbol_lrel_list_Symbol, 1)){
                 IValue $arg0_344 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_344.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_344));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_345))))), ((IInteger)(M_List.size(((IList)($arg0_344))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_345)))))).getValue()){
                          if((((IBool)($me.noneLabeled(((IList)($arg0_344)))))).getValue()){
                             return ((IConstructor)($RVF.constructor(Symbol_lrel_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.glb(((IList)($me.stripLabels(((IList)($arg0_345))))), ((IList)($me.stripLabels(((IList)($arg0_344)))))))), ((IList)($me.getLabels(((IList)($arg0_345))))))))})));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(26562,216,<429,0>,<429,216>) 
    public IConstructor Type_glb$3b40dfcfaf8346f5(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_347 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_347.getType(), $T0)){
              IList l_0 = ((IList)($arg0_347));
              if($has_type_and_arity($1, Symbol_lrel_list_Symbol, 1)){
                 IValue $arg0_346 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_346.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_346));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_347))))), ((IInteger)(M_List.size(((IList)($arg0_346))))))))).getValue()){
                       if((((IBool)($me.noneLabeled(((IList)($arg0_347)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_346)))))).getValue()){
                             return ((IConstructor)($RVF.constructor(Symbol_lrel_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.glb(((IList)($me.stripLabels(((IList)($arg0_347))))), ((IList)($me.stripLabels(((IList)($arg0_346)))))))), ((IList)($me.getLabels(((IList)($arg0_346))))))))})));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(26779,193,<430,0>,<430,193>) 
    public IConstructor Type_glb$b721378e469ce285(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_349 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_349.getType(), $T0)){
              IList l_0 = ((IList)($arg0_349));
              if($has_type_and_arity($1, Symbol_lrel_list_Symbol, 1)){
                 IValue $arg0_348 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_348.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_348));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_349))))), ((IInteger)(M_List.size(((IList)($arg0_348))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_349)))))).getValue()){
                          return null;
                       } else {
                          if((((IBool)($me.allLabeled(((IList)($arg0_348)))))).getValue()){
                             return null;
                          } else {
                             return ((IConstructor)($RVF.constructor(Symbol_lrel_list_Symbol, new IValue[]{((IList)($me.glb(((IList)($me.stripLabels(((IList)($arg0_349))))), ((IList)($me.stripLabels(((IList)($arg0_348))))))))})));
                          
                          }
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(26973,130,<431,0>,<431,130>) 
    public IConstructor Type_glb$fcc8ca1603beb4a9(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_351 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_351.getType(), $T0)){
              IList l_0 = ((IList)($arg0_351));
              if($has_type_and_arity($1, Symbol_lrel_list_Symbol, 1)){
                 IValue $arg0_350 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_350.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_350));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_351))))),((IInteger)(M_List.size(((IList)($arg0_350)))))).not()))).getValue()){
                       return ((IConstructor)($RVF.constructor(Symbol_list_Symbol, new IValue[]{((IConstructor)$constants.get(5)/*value()*/)})));
                    
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(27105,250,<433,0>,<433,250>) 
    public IConstructor Type_glb$9e7e1f5985f9f398(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_tuple_list_Symbol, 1)){
           IValue $arg0_353 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_353.getType(), $T0)){
              IList l_0 = ((IList)($arg0_353));
              if($has_type_and_arity($1, Symbol_tuple_list_Symbol, 1)){
                 IValue $arg0_352 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_352.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_352));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_353))))), ((IInteger)(M_List.size(((IList)($arg0_352))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_353)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_352)))))).getValue()){
                             if((((IBool)($equal(((IList)($me.getLabels(((IList)($arg0_353))))), ((IList)($me.getLabels(((IList)($arg0_352))))))))).getValue()){
                                return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.glb(((IList)($me.stripLabels(((IList)($arg0_353))))), ((IList)($me.stripLabels(((IList)($arg0_352)))))))), ((IList)($me.getLabels(((IList)($arg0_353))))))))})));
                             
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(27356,226,<434,0>,<434,226>) 
    public IConstructor Type_glb$a04284ca9b91ca7f(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_tuple_list_Symbol, 1)){
           IValue $arg0_355 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_355.getType(), $T0)){
              IList l_0 = ((IList)($arg0_355));
              if($has_type_and_arity($1, Symbol_tuple_list_Symbol, 1)){
                 IValue $arg0_354 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_354.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_354));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_355))))), ((IInteger)(M_List.size(((IList)($arg0_354))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_355)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_354)))))).getValue()){
                             if((((IBool)($equal(((IList)($me.getLabels(((IList)($arg0_355))))),((IList)($me.getLabels(((IList)($arg0_354)))))).not()))).getValue()){
                                return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.glb(((IList)($me.stripLabels(((IList)($arg0_355))))), ((IList)($me.stripLabels(((IList)($arg0_354))))))))})));
                             
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(27583,219,<435,0>,<435,219>) 
    public IConstructor Type_glb$48b8563e75023e27(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_tuple_list_Symbol, 1)){
           IValue $arg0_357 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_357.getType(), $T0)){
              IList l_0 = ((IList)($arg0_357));
              if($has_type_and_arity($1, Symbol_tuple_list_Symbol, 1)){
                 IValue $arg0_356 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_356.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_356));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_357))))), ((IInteger)(M_List.size(((IList)($arg0_356))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_357)))))).getValue()){
                          if((((IBool)($me.noneLabeled(((IList)($arg0_356)))))).getValue()){
                             return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.glb(((IList)($me.stripLabels(((IList)($arg0_357))))), ((IList)($me.stripLabels(((IList)($arg0_356)))))))), ((IList)($me.getLabels(((IList)($arg0_357))))))))})));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(27803,219,<436,0>,<436,219>) 
    public IConstructor Type_glb$69e769596d4bd481(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_tuple_list_Symbol, 1)){
           IValue $arg0_359 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_359.getType(), $T0)){
              IList l_0 = ((IList)($arg0_359));
              if($has_type_and_arity($1, Symbol_tuple_list_Symbol, 1)){
                 IValue $arg0_358 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_358.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_358));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_359))))), ((IInteger)(M_List.size(((IList)($arg0_358))))))))).getValue()){
                       if((((IBool)($me.noneLabeled(((IList)($arg0_359)))))).getValue()){
                          if((((IBool)($me.allLabeled(((IList)($arg0_358)))))).getValue()){
                             return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.addLabels(((IList)($me.glb(((IList)($me.stripLabels(((IList)($arg0_359))))), ((IList)($me.stripLabels(((IList)($arg0_358)))))))), ((IList)($me.getLabels(((IList)($arg0_358))))))))})));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(28023,196,<437,0>,<437,196>) 
    public IConstructor Type_glb$ba92934d6828a4fc(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_tuple_list_Symbol, 1)){
           IValue $arg0_361 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_361.getType(), $T0)){
              IList l_0 = ((IList)($arg0_361));
              if($has_type_and_arity($1, Symbol_tuple_list_Symbol, 1)){
                 IValue $arg0_360 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_360.getType(), $T0)){
                    IList r_1 = ((IList)($arg0_360));
                    if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg0_361))))), ((IInteger)(M_List.size(((IList)($arg0_360))))))))).getValue()){
                       if((((IBool)($me.allLabeled(((IList)($arg0_361)))))).getValue()){
                          return null;
                       } else {
                          if((((IBool)($me.allLabeled(((IList)($arg0_360)))))).getValue()){
                             return null;
                          } else {
                             return ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($me.glb(((IList)($me.stripLabels(((IList)($arg0_361))))), ((IList)($me.stripLabels(((IList)($arg0_360))))))))})));
                          
                          }
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(28221,253,<439,0>,<439,253>) 
    public IConstructor Type_glb$17ed4c750a842e8b(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_map_Symbol_Symbol, 2)){
           IValue $arg0_371 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_371.getType(), ADT_Symbol)){
              if($has_type_and_arity($arg0_371, Symbol_label_str_Symbol, 2)){
                 IValue $arg0_373 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_371)),0));
                 if($isComparable($arg0_373.getType(), $T2)){
                    IString lfl_0 = null;
                    IValue $arg1_372 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_371)),1));
                    if($isComparable($arg1_372.getType(), ADT_Symbol)){
                       IConstructor lf_1 = null;
                       IValue $arg1_368 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
                       if($isComparable($arg1_368.getType(), ADT_Symbol)){
                          if($has_type_and_arity($arg1_368, Symbol_label_str_Symbol, 2)){
                             IValue $arg0_370 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_368)),0));
                             if($isComparable($arg0_370.getType(), $T2)){
                                IString ltl_2 = null;
                                IValue $arg1_369 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_368)),1));
                                if($isComparable($arg1_369.getType(), ADT_Symbol)){
                                   IConstructor lt_3 = null;
                                   if($has_type_and_arity($1, Symbol_map_Symbol_Symbol, 2)){
                                      IValue $arg0_365 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                                      if($isComparable($arg0_365.getType(), ADT_Symbol)){
                                         if($has_type_and_arity($arg0_365, Symbol_label_str_Symbol, 2)){
                                            IValue $arg0_367 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_365)),0));
                                            if($isComparable($arg0_367.getType(), $T2)){
                                               IString rfl_4 = null;
                                               IValue $arg1_366 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_365)),1));
                                               if($isComparable($arg1_366.getType(), ADT_Symbol)){
                                                  IConstructor rf_5 = null;
                                                  IValue $arg1_362 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                                                  if($isComparable($arg1_362.getType(), ADT_Symbol)){
                                                     if($has_type_and_arity($arg1_362, Symbol_label_str_Symbol, 2)){
                                                        IValue $arg0_364 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_362)),0));
                                                        if($isComparable($arg0_364.getType(), $T2)){
                                                           IString rtl_6 = null;
                                                           IValue $arg1_363 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_362)),1));
                                                           if($isComparable($arg1_363.getType(), ADT_Symbol)){
                                                              IConstructor rt_7 = null;
                                                              if((((IBool)($equal(((IString)($arg0_373)), ((IString)($arg0_367)))))).getValue()){
                                                                 if((((IBool)($equal(((IString)($arg0_370)), ((IString)($arg0_364)))))).getValue()){
                                                                    return ((IConstructor)($RVF.constructor(Symbol_map_Symbol_Symbol, new IValue[]{((IConstructor)($RVF.constructor(Symbol_label_str_Symbol, new IValue[]{((IString)($arg0_373)), ((IConstructor)($me.glb(((IConstructor)($arg1_372)), ((IConstructor)($arg1_366)))))}))), ((IConstructor)($RVF.constructor(Symbol_label_str_Symbol, new IValue[]{((IString)($arg0_370)), ((IConstructor)($me.glb(((IConstructor)($arg1_369)), ((IConstructor)($arg1_363)))))})))})));
                                                                 
                                                                 } else {
                                                                    return null;
                                                                 }
                                                              } else {
                                                                 return null;
                                                              }
                                                           } else {
                                                              return null;
                                                           }
                                                        } else {
                                                           return null;
                                                        }
                                                     } else {
                                                        return null;
                                                     }
                                                  } else {
                                                     return null;
                                                  }
                                               } else {
                                                  return null;
                                               }
                                            } else {
                                               return null;
                                            }
                                         } else {
                                            return null;
                                         }
                                      } else {
                                         return null;
                                      }
                                   } else {
                                      return null;
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(28475,227,<440,0>,<440,227>) 
    public IConstructor Type_glb$a9afab04303a8d6a(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_map_Symbol_Symbol, 2)){
           IValue $arg0_383 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_383.getType(), ADT_Symbol)){
              if($has_type_and_arity($arg0_383, Symbol_label_str_Symbol, 2)){
                 IValue $arg0_385 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_383)),0));
                 if($isComparable($arg0_385.getType(), $T2)){
                    IString lfl_0 = null;
                    IValue $arg1_384 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_383)),1));
                    if($isComparable($arg1_384.getType(), ADT_Symbol)){
                       IConstructor lf_1 = null;
                       IValue $arg1_380 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
                       if($isComparable($arg1_380.getType(), ADT_Symbol)){
                          if($has_type_and_arity($arg1_380, Symbol_label_str_Symbol, 2)){
                             IValue $arg0_382 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_380)),0));
                             if($isComparable($arg0_382.getType(), $T2)){
                                IString ltl_2 = null;
                                IValue $arg1_381 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_380)),1));
                                if($isComparable($arg1_381.getType(), ADT_Symbol)){
                                   IConstructor lt_3 = null;
                                   if($has_type_and_arity($1, Symbol_map_Symbol_Symbol, 2)){
                                      IValue $arg0_377 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                                      if($isComparable($arg0_377.getType(), ADT_Symbol)){
                                         if($has_type_and_arity($arg0_377, Symbol_label_str_Symbol, 2)){
                                            IValue $arg0_379 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_377)),0));
                                            if($isComparable($arg0_379.getType(), $T2)){
                                               IString rfl_4 = null;
                                               IValue $arg1_378 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_377)),1));
                                               if($isComparable($arg1_378.getType(), ADT_Symbol)){
                                                  IConstructor rf_5 = null;
                                                  IValue $arg1_374 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                                                  if($isComparable($arg1_374.getType(), ADT_Symbol)){
                                                     if($has_type_and_arity($arg1_374, Symbol_label_str_Symbol, 2)){
                                                        IValue $arg0_376 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_374)),0));
                                                        if($isComparable($arg0_376.getType(), $T2)){
                                                           IString rtl_6 = null;
                                                           IValue $arg1_375 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_374)),1));
                                                           if($isComparable($arg1_375.getType(), ADT_Symbol)){
                                                              IConstructor rt_7 = null;
                                                              if((((IBool)($equal(((IString)($arg0_385)),((IString)($arg0_379))).not()))).getValue()){
                                                                 return ((IConstructor)($RVF.constructor(Symbol_map_Symbol_Symbol, new IValue[]{((IConstructor)($me.glb(((IConstructor)($arg1_384)), ((IConstructor)($arg1_378))))), ((IConstructor)($me.glb(((IConstructor)($arg1_381)), ((IConstructor)($arg1_375)))))})));
                                                              
                                                              } else {
                                                                 if((((IBool)($equal(((IString)($arg0_382)),((IString)($arg0_376))).not()))).getValue()){
                                                                    return ((IConstructor)($RVF.constructor(Symbol_map_Symbol_Symbol, new IValue[]{((IConstructor)($me.glb(((IConstructor)($arg1_384)), ((IConstructor)($arg1_378))))), ((IConstructor)($me.glb(((IConstructor)($arg1_381)), ((IConstructor)($arg1_375)))))})));
                                                                 
                                                                 } else {
                                                                    return null;
                                                                 }
                                                              }
                                                           } else {
                                                              return null;
                                                           }
                                                        } else {
                                                           return null;
                                                        }
                                                     } else {
                                                        return null;
                                                     }
                                                  } else {
                                                     return null;
                                                  }
                                               } else {
                                                  return null;
                                               }
                                            } else {
                                               return null;
                                            }
                                         } else {
                                            return null;
                                         }
                                      } else {
                                         return null;
                                      }
                                   } else {
                                      return null;
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(28703,236,<441,0>,<441,236>) 
    public IConstructor Type_glb$4c151198f5c36983(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_map_Symbol_Symbol, 2)){
           IValue $arg0_398 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_398.getType(), ADT_Symbol)){
              if($has_type_and_arity($arg0_398, Symbol_label_str_Symbol, 2)){
                 IValue $arg0_400 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_398)),0));
                 if($isComparable($arg0_400.getType(), $T2)){
                    if(true){
                       IString lfl_0 = null;
                       IValue $arg1_399 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_398)),1));
                       if($isComparable($arg1_399.getType(), ADT_Symbol)){
                          if(true){
                             IConstructor lf_1 = null;
                             IValue $arg1_395 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
                             if($isComparable($arg1_395.getType(), ADT_Symbol)){
                                if($has_type_and_arity($arg1_395, Symbol_label_str_Symbol, 2)){
                                   IValue $arg0_397 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_395)),0));
                                   if($isComparable($arg0_397.getType(), $T2)){
                                      if(true){
                                         IString ltl_2 = null;
                                         IValue $arg1_396 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_395)),1));
                                         if($isComparable($arg1_396.getType(), ADT_Symbol)){
                                            if(true){
                                               IConstructor lt_3 = null;
                                               if($has_type_and_arity($1, Symbol_map_Symbol_Symbol, 2)){
                                                  IValue $arg0_394 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                                                  if($isComparable($arg0_394.getType(), ADT_Symbol)){
                                                     if(true){
                                                        IConstructor rf_4 = null;
                                                        IValue $arg1_393 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                                                        if($isComparable($arg1_393.getType(), ADT_Symbol)){
                                                           if(true){
                                                              IConstructor rt_5 = null;
                                                              IBool $aux8 = (IBool)(((IBool)$constants.get(2)/*false*/));
                                                              $aux8 = ((IBool)$constants.get(2)/*false*/);
                                                              /*muExists*/$EXP387: 
                                                                  do {
                                                                      if($has_type_and_arity($arg0_394, Symbol_label_str_Symbol, 2)){
                                                                         IValue $arg0_389 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_394)),0));
                                                                         if($isComparable($arg0_389.getType(), $T4)){
                                                                            IValue $arg1_388 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_394)),1));
                                                                            if($isComparable($arg1_388.getType(), $T4)){
                                                                               $aux8 = ((IBool)$constants.get(1)/*true*/);
                                                                               break $EXP387; // muSucceed
                                                                            } else {
                                                                               $aux8 = ((IBool)$constants.get(2)/*false*/);
                                                                               continue $EXP387;
                                                                            }
                                                                         } else {
                                                                            $aux8 = ((IBool)$constants.get(2)/*false*/);
                                                                            continue $EXP387;
                                                                         }
                                                                      } else {
                                                                         $aux8 = ((IBool)$constants.get(2)/*false*/);
                                                                         continue $EXP387;
                                                                      }
                                                                  } while(false);
                                                              if((((IBool)($aux8))).getValue()){
                                                                 return null;
                                                              } else {
                                                                 IBool $aux9 = (IBool)(((IBool)$constants.get(2)/*false*/));
                                                                 $aux9 = ((IBool)$constants.get(2)/*false*/);
                                                                 /*muExists*/$EXP390: 
                                                                     do {
                                                                         if($has_type_and_arity($arg1_393, Symbol_label_str_Symbol, 2)){
                                                                            IValue $arg0_392 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_393)),0));
                                                                            if($isComparable($arg0_392.getType(), $T4)){
                                                                               IValue $arg1_391 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_393)),1));
                                                                               if($isComparable($arg1_391.getType(), $T4)){
                                                                                  $aux9 = ((IBool)$constants.get(1)/*true*/);
                                                                                  break $EXP390; // muSucceed
                                                                               } else {
                                                                                  $aux9 = ((IBool)$constants.get(2)/*false*/);
                                                                                  continue $EXP390;
                                                                               }
                                                                            } else {
                                                                               $aux9 = ((IBool)$constants.get(2)/*false*/);
                                                                               continue $EXP390;
                                                                            }
                                                                         } else {
                                                                            $aux9 = ((IBool)$constants.get(2)/*false*/);
                                                                            continue $EXP390;
                                                                         }
                                                                     } while(false);
                                                                 if((((IBool)($aux9))).getValue()){
                                                                    return null;
                                                                 } else {
                                                                    return ((IConstructor)($RVF.constructor(Symbol_map_Symbol_Symbol, new IValue[]{((IConstructor)($RVF.constructor(Symbol_label_str_Symbol, new IValue[]{((IString)($arg0_400)), ((IConstructor)($me.glb(((IConstructor)($arg1_399)), ((IConstructor)($arg0_394)))))}))), ((IConstructor)($RVF.constructor(Symbol_label_str_Symbol, new IValue[]{((IString)($arg0_397)), ((IConstructor)($me.glb(((IConstructor)($arg1_396)), ((IConstructor)($arg1_393)))))})))})));
                                                                 
                                                                 }
                                                              }
                                                           } else {
                                                              return null;
                                                           }
                                                        } else {
                                                           return null;
                                                        }
                                                     } else {
                                                        return null;
                                                     }
                                                  } else {
                                                     return null;
                                                  }
                                               } else {
                                                  return null;
                                               }
                                            } else {
                                               return null;
                                            }
                                         } else {
                                            return null;
                                         }
                                      } else {
                                         return null;
                                      }
                                   } else {
                                      return null;
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(28940,236,<442,0>,<442,236>) 
    public IConstructor Type_glb$c70f2ec5079b5d5a(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_map_Symbol_Symbol, 2)){
           IValue $arg0_415 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_415.getType(), ADT_Symbol)){
              if(true){
                 IConstructor lf_0 = null;
                 IValue $arg1_414 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
                 if($isComparable($arg1_414.getType(), ADT_Symbol)){
                    if(true){
                       IConstructor lt_1 = null;
                       if($has_type_and_arity($1, Symbol_map_Symbol_Symbol, 2)){
                          IValue $arg0_411 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                          if($isComparable($arg0_411.getType(), ADT_Symbol)){
                             if($has_type_and_arity($arg0_411, Symbol_label_str_Symbol, 2)){
                                IValue $arg0_413 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_411)),0));
                                if($isComparable($arg0_413.getType(), $T2)){
                                   if(true){
                                      IString rfl_2 = null;
                                      IValue $arg1_412 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_411)),1));
                                      if($isComparable($arg1_412.getType(), ADT_Symbol)){
                                         if(true){
                                            IConstructor rf_3 = null;
                                            IValue $arg1_408 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                                            if($isComparable($arg1_408.getType(), ADT_Symbol)){
                                               if($has_type_and_arity($arg1_408, Symbol_label_str_Symbol, 2)){
                                                  IValue $arg0_410 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_408)),0));
                                                  if($isComparable($arg0_410.getType(), $T2)){
                                                     if(true){
                                                        IString rtl_4 = null;
                                                        IValue $arg1_409 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_408)),1));
                                                        if($isComparable($arg1_409.getType(), ADT_Symbol)){
                                                           if(true){
                                                              IConstructor rt_5 = null;
                                                              IBool $aux10 = (IBool)(((IBool)$constants.get(2)/*false*/));
                                                              $aux10 = ((IBool)$constants.get(2)/*false*/);
                                                              /*muExists*/$EXP402: 
                                                                  do {
                                                                      if($has_type_and_arity($arg0_415, Symbol_label_str_Symbol, 2)){
                                                                         IValue $arg0_404 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_415)),0));
                                                                         if($isComparable($arg0_404.getType(), $T4)){
                                                                            IValue $arg1_403 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_415)),1));
                                                                            if($isComparable($arg1_403.getType(), $T4)){
                                                                               $aux10 = ((IBool)$constants.get(1)/*true*/);
                                                                               break $EXP402; // muSucceed
                                                                            } else {
                                                                               $aux10 = ((IBool)$constants.get(2)/*false*/);
                                                                               continue $EXP402;
                                                                            }
                                                                         } else {
                                                                            $aux10 = ((IBool)$constants.get(2)/*false*/);
                                                                            continue $EXP402;
                                                                         }
                                                                      } else {
                                                                         $aux10 = ((IBool)$constants.get(2)/*false*/);
                                                                         continue $EXP402;
                                                                      }
                                                                  } while(false);
                                                              if((((IBool)($aux10))).getValue()){
                                                                 return null;
                                                              } else {
                                                                 IBool $aux11 = (IBool)(((IBool)$constants.get(2)/*false*/));
                                                                 $aux11 = ((IBool)$constants.get(2)/*false*/);
                                                                 /*muExists*/$EXP405: 
                                                                     do {
                                                                         if($has_type_and_arity($arg1_414, Symbol_label_str_Symbol, 2)){
                                                                            IValue $arg0_407 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_414)),0));
                                                                            if($isComparable($arg0_407.getType(), $T4)){
                                                                               IValue $arg1_406 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_414)),1));
                                                                               if($isComparable($arg1_406.getType(), $T4)){
                                                                                  $aux11 = ((IBool)$constants.get(1)/*true*/);
                                                                                  break $EXP405; // muSucceed
                                                                               } else {
                                                                                  $aux11 = ((IBool)$constants.get(2)/*false*/);
                                                                                  continue $EXP405;
                                                                               }
                                                                            } else {
                                                                               $aux11 = ((IBool)$constants.get(2)/*false*/);
                                                                               continue $EXP405;
                                                                            }
                                                                         } else {
                                                                            $aux11 = ((IBool)$constants.get(2)/*false*/);
                                                                            continue $EXP405;
                                                                         }
                                                                     } while(false);
                                                                 if((((IBool)($aux11))).getValue()){
                                                                    return null;
                                                                 } else {
                                                                    return ((IConstructor)($RVF.constructor(Symbol_map_Symbol_Symbol, new IValue[]{((IConstructor)($RVF.constructor(Symbol_label_str_Symbol, new IValue[]{((IString)($arg0_413)), ((IConstructor)($me.glb(((IConstructor)($arg0_415)), ((IConstructor)($arg1_412)))))}))), ((IConstructor)($RVF.constructor(Symbol_label_str_Symbol, new IValue[]{((IString)($arg0_410)), ((IConstructor)($me.glb(((IConstructor)($arg1_414)), ((IConstructor)($arg1_409)))))})))})));
                                                                 
                                                                 }
                                                              }
                                                           } else {
                                                              return null;
                                                           }
                                                        } else {
                                                           return null;
                                                        }
                                                     } else {
                                                        return null;
                                                     }
                                                  } else {
                                                     return null;
                                                  }
                                               } else {
                                                  return null;
                                               }
                                            } else {
                                               return null;
                                            }
                                         } else {
                                            return null;
                                         }
                                      } else {
                                         return null;
                                      }
                                   } else {
                                      return null;
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(29177,218,<443,0>,<443,218>) 
    public IConstructor Type_glb$a5f417161c5ea6ff(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_map_Symbol_Symbol, 2)){
           IValue $arg0_434 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_434.getType(), ADT_Symbol)){
              if(true){
                 IConstructor lf_0 = null;
                 IValue $arg1_433 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
                 if($isComparable($arg1_433.getType(), ADT_Symbol)){
                    if(true){
                       IConstructor lt_1 = null;
                       if($has_type_and_arity($1, Symbol_map_Symbol_Symbol, 2)){
                          IValue $arg0_432 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                          if($isComparable($arg0_432.getType(), ADT_Symbol)){
                             if(true){
                                IConstructor rf_2 = null;
                                IValue $arg1_431 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                                if($isComparable($arg1_431.getType(), ADT_Symbol)){
                                   if(true){
                                      IConstructor rt_3 = null;
                                      IBool $aux12 = (IBool)(((IBool)$constants.get(2)/*false*/));
                                      $aux12 = ((IBool)$constants.get(2)/*false*/);
                                      /*muExists*/$EXP419: 
                                          do {
                                              if($has_type_and_arity($arg0_434, Symbol_label_str_Symbol, 2)){
                                                 IValue $arg0_421 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_434)),0));
                                                 if($isComparable($arg0_421.getType(), $T4)){
                                                    IValue $arg1_420 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_434)),1));
                                                    if($isComparable($arg1_420.getType(), $T4)){
                                                       $aux12 = ((IBool)$constants.get(1)/*true*/);
                                                       break $EXP419; // muSucceed
                                                    } else {
                                                       $aux12 = ((IBool)$constants.get(2)/*false*/);
                                                       continue $EXP419;
                                                    }
                                                 } else {
                                                    $aux12 = ((IBool)$constants.get(2)/*false*/);
                                                    continue $EXP419;
                                                 }
                                              } else {
                                                 $aux12 = ((IBool)$constants.get(2)/*false*/);
                                                 continue $EXP419;
                                              }
                                          } while(false);
                                      if((((IBool)($aux12))).getValue()){
                                         return null;
                                      } else {
                                         IBool $aux13 = (IBool)(((IBool)$constants.get(2)/*false*/));
                                         $aux13 = ((IBool)$constants.get(2)/*false*/);
                                         /*muExists*/$EXP422: 
                                             do {
                                                 if($has_type_and_arity($arg1_433, Symbol_label_str_Symbol, 2)){
                                                    IValue $arg0_424 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_433)),0));
                                                    if($isComparable($arg0_424.getType(), $T4)){
                                                       IValue $arg1_423 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_433)),1));
                                                       if($isComparable($arg1_423.getType(), $T4)){
                                                          $aux13 = ((IBool)$constants.get(1)/*true*/);
                                                          break $EXP422; // muSucceed
                                                       } else {
                                                          $aux13 = ((IBool)$constants.get(2)/*false*/);
                                                          continue $EXP422;
                                                       }
                                                    } else {
                                                       $aux13 = ((IBool)$constants.get(2)/*false*/);
                                                       continue $EXP422;
                                                    }
                                                 } else {
                                                    $aux13 = ((IBool)$constants.get(2)/*false*/);
                                                    continue $EXP422;
                                                 }
                                             } while(false);
                                         if((((IBool)($aux13))).getValue()){
                                            return null;
                                         } else {
                                            IBool $aux14 = (IBool)(((IBool)$constants.get(2)/*false*/));
                                            $aux14 = ((IBool)$constants.get(2)/*false*/);
                                            /*muExists*/$EXP425: 
                                                do {
                                                    if($has_type_and_arity($arg0_432, Symbol_label_str_Symbol, 2)){
                                                       IValue $arg0_427 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_432)),0));
                                                       if($isComparable($arg0_427.getType(), $T4)){
                                                          IValue $arg1_426 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_432)),1));
                                                          if($isComparable($arg1_426.getType(), $T4)){
                                                             $aux14 = ((IBool)$constants.get(1)/*true*/);
                                                             break $EXP425; // muSucceed
                                                          } else {
                                                             $aux14 = ((IBool)$constants.get(2)/*false*/);
                                                             continue $EXP425;
                                                          }
                                                       } else {
                                                          $aux14 = ((IBool)$constants.get(2)/*false*/);
                                                          continue $EXP425;
                                                       }
                                                    } else {
                                                       $aux14 = ((IBool)$constants.get(2)/*false*/);
                                                       continue $EXP425;
                                                    }
                                                } while(false);
                                            if((((IBool)($aux14))).getValue()){
                                               return null;
                                            } else {
                                               IBool $aux15 = (IBool)(((IBool)$constants.get(2)/*false*/));
                                               $aux15 = ((IBool)$constants.get(2)/*false*/);
                                               /*muExists*/$EXP428: 
                                                   do {
                                                       if($has_type_and_arity($arg1_431, Symbol_label_str_Symbol, 2)){
                                                          IValue $arg0_430 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_431)),0));
                                                          if($isComparable($arg0_430.getType(), $T4)){
                                                             IValue $arg1_429 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_431)),1));
                                                             if($isComparable($arg1_429.getType(), $T4)){
                                                                $aux15 = ((IBool)$constants.get(1)/*true*/);
                                                                break $EXP428; // muSucceed
                                                             } else {
                                                                $aux15 = ((IBool)$constants.get(2)/*false*/);
                                                                continue $EXP428;
                                                             }
                                                          } else {
                                                             $aux15 = ((IBool)$constants.get(2)/*false*/);
                                                             continue $EXP428;
                                                          }
                                                       } else {
                                                          $aux15 = ((IBool)$constants.get(2)/*false*/);
                                                          continue $EXP428;
                                                       }
                                                   } while(false);
                                               if((((IBool)($aux15))).getValue()){
                                                  return null;
                                               } else {
                                                  return ((IConstructor)($RVF.constructor(Symbol_map_Symbol_Symbol, new IValue[]{((IConstructor)($me.glb(((IConstructor)($arg0_434)), ((IConstructor)($arg0_432))))), ((IConstructor)($me.glb(((IConstructor)($arg1_433)), ((IConstructor)($arg1_431)))))})));
                                               
                                               }
                                            }
                                         }
                                      }
                                   } else {
                                      return null;
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(29397,92,<445,0>,<445,92>) 
    public IConstructor Type_glb$5aaa99890ed611bb(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_bag_Symbol, 1)){
           IValue $arg0_436 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_436.getType(), ADT_Symbol)){
              IConstructor s_0 = null;
              if($has_type_and_arity($1, Symbol_bag_Symbol, 1)){
                 IValue $arg0_435 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_435.getType(), ADT_Symbol)){
                    IConstructor t_1 = null;
                    return ((IConstructor)($RVF.constructor(Symbol_bag_Symbol, new IValue[]{((IConstructor)($me.glb(((IConstructor)($arg0_436)), ((IConstructor)($arg0_435)))))})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(29490,90,<446,0>,<446,90>) 
    public IConstructor Type_glb$e59f6ec3e8ac7cf8(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_adt_str_list_Symbol, 2)){
           IValue $arg0_438 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_438.getType(), $T2)){
              IString n_0 = null;
              IValue $arg1_437 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_437.getType(), $T0)){
                 if($has_type_and_arity($1, Symbol_node_, 0)){
                    return ((IConstructor)($RVF.constructor(Symbol_node_, new IValue[]{})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(29581,82,<447,0>,<447,82>) 
    public IConstructor Type_glb$13f9a897070bdb41(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_node_, 0)){
           if($has_type_and_arity($1, Symbol_adt_str_list_Symbol, 2)){
              IValue $arg0_440 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
              if($isComparable($arg0_440.getType(), $T2)){
                 IString n_0 = null;
                 IValue $arg1_439 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                 if($isComparable($arg1_439.getType(), $T0)){
                    return ((IConstructor)($RVF.constructor(Symbol_node_, new IValue[]{})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(29664,257,<448,0>,<448,257>) 
    public IConstructor Type_glb$0b0dd3255cd61470(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_adt_str_list_Symbol, 2)){
           IValue $arg0_444 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_444.getType(), $T2)){
              IString n_0 = ((IString)($arg0_444));
              IValue $arg1_443 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_443.getType(), $T0)){
                 IList lp_1 = ((IList)($arg1_443));
                 if($has_type_and_arity($1, Symbol_adt_str_list_Symbol, 2)){
                    IValue $arg0_442 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                    if($isComparable($arg0_442.getType(), $T2)){
                       if(($arg0_444 != null)){
                          if($arg0_444.match($arg0_442)){
                             IValue $arg1_441 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                             if($isComparable($arg1_441.getType(), $T0)){
                                IList rp_2 = ((IList)($arg1_441));
                                if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg1_443))))), ((IInteger)(M_List.size(((IList)($arg1_441))))))))).getValue()){
                                   if((((IBool)($equal(((IList)($me.getParamLabels(((IList)($arg1_443))))), ((IList)($me.getParamLabels(((IList)($arg1_441))))))))).getValue()){
                                      if((((IBool)($aint_lessequal_aint(((IInteger)(M_List.size(((IList)($me.getParamLabels(((IList)($arg1_443)))))))),((IInteger)$constants.get(4)/*0*/)).not()))).getValue()){
                                         return ((IConstructor)($RVF.constructor(Symbol_adt_str_list_Symbol, new IValue[]{((IString)($arg0_442)), ((IList)($me.addParamLabels(((IList)($me.glb(((IList)($arg1_443)), ((IList)($arg1_441))))), ((IList)($me.getParamLabels(((IList)($arg1_443))))))))})));
                                      
                                      } else {
                                         return null;
                                      }
                                   } else {
                                      return null;
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          $arg0_444 = ((IValue)($arg0_442));
                          IValue $arg1_441 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                          if($isComparable($arg1_441.getType(), $T0)){
                             IList rp_2 = ((IList)($arg1_441));
                             if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg1_443))))), ((IInteger)(M_List.size(((IList)($arg1_441))))))))).getValue()){
                                if((((IBool)($equal(((IList)($me.getParamLabels(((IList)($arg1_443))))), ((IList)($me.getParamLabels(((IList)($arg1_441))))))))).getValue()){
                                   if((((IBool)($aint_lessequal_aint(((IInteger)(M_List.size(((IList)($me.getParamLabels(((IList)($arg1_443)))))))),((IInteger)$constants.get(4)/*0*/)).not()))).getValue()){
                                      return ((IConstructor)($RVF.constructor(Symbol_adt_str_list_Symbol, new IValue[]{((IString)($arg0_442)), ((IList)($me.addParamLabels(((IList)($me.glb(((IList)($arg1_443)), ((IList)($arg1_441))))), ((IList)($me.getParamLabels(((IList)($arg1_443))))))))})));
                                   
                                   } else {
                                      return null;
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(29922,179,<449,0>,<449,179>) 
    public IConstructor Type_glb$e2846391aba6c513(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_adt_str_list_Symbol, 2)){
           IValue $arg0_448 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_448.getType(), $T2)){
              IString n_0 = ((IString)($arg0_448));
              IValue $arg1_447 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_447.getType(), $T0)){
                 IList lp_1 = ((IList)($arg1_447));
                 if($has_type_and_arity($1, Symbol_adt_str_list_Symbol, 2)){
                    IValue $arg0_446 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                    if($isComparable($arg0_446.getType(), $T2)){
                       if(($arg0_448 != null)){
                          if($arg0_448.match($arg0_446)){
                             IValue $arg1_445 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                             if($isComparable($arg1_445.getType(), $T0)){
                                IList rp_2 = ((IList)($arg1_445));
                                if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg1_447))))), ((IInteger)(M_List.size(((IList)($arg1_445))))))))).getValue()){
                                   if((((IBool)($equal(((IInteger)(M_List.size(((IList)($me.getParamLabels(((IList)($arg1_447)))))))), ((IInteger)$constants.get(4)/*0*/))))).getValue()){
                                      return ((IConstructor)($RVF.constructor(Symbol_adt_str_list_Symbol, new IValue[]{((IString)($arg0_446)), ((IList)($me.glb(((IList)($arg1_447)), ((IList)($arg1_445)))))})));
                                   
                                   } else {
                                      return null;
                                   }
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          $arg0_448 = ((IValue)($arg0_446));
                          IValue $arg1_445 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                          if($isComparable($arg1_445.getType(), $T0)){
                             IList rp_2 = ((IList)($arg1_445));
                             if((((IBool)($equal(((IInteger)(M_List.size(((IList)($arg1_447))))), ((IInteger)(M_List.size(((IList)($arg1_445))))))))).getValue()){
                                if((((IBool)($equal(((IInteger)(M_List.size(((IList)($me.getParamLabels(((IList)($arg1_447)))))))), ((IInteger)$constants.get(4)/*0*/))))).getValue()){
                                   return ((IConstructor)($RVF.constructor(Symbol_adt_str_list_Symbol, new IValue[]{((IString)($arg0_446)), ((IList)($me.glb(((IList)($arg1_447)), ((IList)($arg1_445)))))})));
                                
                                } else {
                                   return null;
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(30102,124,<450,0>,<450,124>) 
    public IConstructor Type_glb$fcb766b6fadeffe6(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_adt_str_list_Symbol, 2)){
           IValue $arg0_452 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_452.getType(), $T2)){
              IString n_0 = null;
              IValue $arg1_451 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_451.getType(), $T0)){
                 IList lp_1 = null;
                 if($has_type_and_arity($1, Symbol_adt_str_list_Symbol, 2)){
                    IValue $arg0_450 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                    if($isComparable($arg0_450.getType(), $T2)){
                       IString m_2 = null;
                       IValue $arg1_449 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                       if($isComparable($arg1_449.getType(), $T0)){
                          IList rp_3 = null;
                          if((((IBool)($equal(((IString)($arg0_452)),((IString)($arg0_450))).not()))).getValue()){
                             return ((IConstructor)($RVF.constructor(Symbol_node_, new IValue[]{})));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(30227,130,<451,0>,<451,130>) 
    public IConstructor Type_glb$57382441fa985d45(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_adt_str_list_Symbol, 2)){
           IValue $arg0_457 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_457.getType(), $T2)){
              IString ln_0 = null;
              IValue $arg1_456 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_456.getType(), $T0)){
                 IList lp_1 = null;
                 if($has_type_and_arity($1, Symbol_cons_Symbol_str_list_Symbol, 3)){
                    IValue $arg0_455 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                    if($isComparable($arg0_455.getType(), ADT_Symbol)){
                       IConstructor b_2 = null;
                       IValue $arg1_454 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                       if($isComparable($arg1_454.getType(), $T4)){
                          IValue $arg2_453 = (IValue)($aadt_subscript_int(((IConstructor)$1),2));
                          if($isComparable($arg2_453.getType(), $T0)){
                             return ((IConstructor)($me.glb(((IConstructor)($RVF.constructor(Symbol_adt_str_list_Symbol, new IValue[]{((IString)($arg0_457)), ((IList)($arg1_456))}))), ((IConstructor)($arg0_455)))));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(30359,121,<453,0>,<453,121>) 
    public IConstructor Type_glb$cad5f842cc182e58(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_cons_Symbol_str_list_Symbol, 3)){
           IValue $arg0_463 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_463.getType(), ADT_Symbol)){
              IConstructor la_0 = null;
              IValue $arg1_462 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_462.getType(), $T4)){
                 IValue $arg2_461 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_461.getType(), $T0)){
                    if($has_type_and_arity($1, Symbol_cons_Symbol_str_list_Symbol, 3)){
                       IValue $arg0_460 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                       if($isComparable($arg0_460.getType(), ADT_Symbol)){
                          IConstructor ra_1 = null;
                          IValue $arg1_459 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                          if($isComparable($arg1_459.getType(), $T4)){
                             IValue $arg2_458 = (IValue)($aadt_subscript_int(((IConstructor)$1),2));
                             if($isComparable($arg2_458.getType(), $T0)){
                                return ((IConstructor)($me.glb(((IConstructor)($arg0_463)), ((IConstructor)($arg0_460)))));
                             
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(30481,129,<454,0>,<454,129>) 
    public IConstructor Type_glb$b25c5c3f39fc09b1(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_cons_Symbol_str_list_Symbol, 3)){
           IValue $arg0_468 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_468.getType(), ADT_Symbol)){
              IConstructor a_0 = null;
              IValue $arg1_467 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_467.getType(), $T4)){
                 IValue $arg2_466 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_466.getType(), $T0)){
                    IList lp_1 = null;
                    if($has_type_and_arity($1, Symbol_adt_str_list_Symbol, 2)){
                       IValue $arg0_465 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                       if($isComparable($arg0_465.getType(), $T2)){
                          IString n_2 = null;
                          IValue $arg1_464 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                          if($isComparable($arg1_464.getType(), $T0)){
                             IList rp_3 = null;
                             return ((IConstructor)($me.glb(((IConstructor)($arg0_468)), ((IConstructor)($RVF.constructor(Symbol_adt_str_list_Symbol, new IValue[]{((IString)($arg0_465)), ((IList)($arg1_464))}))))));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(30611,81,<455,0>,<455,81>) 
    public IConstructor Type_glb$cd7b70aadcf0e6f7(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_cons_Symbol_str_list_Symbol, 3)){
           IValue $arg0_471 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_471.getType(), ADT_Symbol)){
              IValue $arg1_470 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_470.getType(), $T4)){
                 IValue $arg2_469 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_469.getType(), $T0)){
                    if($has_type_and_arity($1, Symbol_node_, 0)){
                       return ((IConstructor)$constants.get(10)/*node()*/);
                    
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(30694,101,<457,0>,<457,101>) 
    public IConstructor Type_glb$6917d863f1e00280(IConstructor $0, IConstructor r_1){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_474 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_474.getType(), $T2)){
              IValue $arg1_473 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_473.getType(), $T0)){
                 IValue $arg2_472 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_472.getType(), ADT_Symbol)){
                    IConstructor aliased_0 = null;
                    return ((IConstructor)($me.glb(((IConstructor)($arg2_472)), ((IConstructor)r_1))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(30796,101,<458,0>,<458,101>) 
    public IConstructor Type_glb$748cc45995ed4ab6(IConstructor l_0, IConstructor $1){ 
        
        
        if($has_type_and_arity($1, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_477 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
           if($isComparable($arg0_477.getType(), $T2)){
              IValue $arg1_476 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
              if($isComparable($arg1_476.getType(), $T0)){
                 IValue $arg2_475 = (IValue)($aadt_subscript_int(((IConstructor)$1),2));
                 if($isComparable($arg2_475.getType(), ADT_Symbol)){
                    IConstructor aliased_1 = null;
                    return ((IConstructor)($me.glb(((IConstructor)l_0), ((IConstructor)($arg2_475)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(30899,85,<460,0>,<460,85>) 
    public IConstructor Type_glb$9ee1fb0fc2528775(IConstructor $0, IConstructor r_1){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_479 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_479.getType(), $T2)){
              IValue $arg1_478 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_478.getType(), ADT_Symbol)){
                 IConstructor bound_0 = null;
                 return ((IConstructor)($me.glb(((IConstructor)($arg1_478)), ((IConstructor)r_1))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(30985,85,<461,0>,<461,85>) 
    public IConstructor Type_glb$f5f3963b9ac36b44(IConstructor l_0, IConstructor $1){ 
        
        
        if($has_type_and_arity($1, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_481 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
           if($isComparable($arg0_481.getType(), $T2)){
              IValue $arg1_480 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
              if($isComparable($arg1_480.getType(), ADT_Symbol)){
                 IConstructor bound_1 = null;
                 return ((IConstructor)($me.glb(((IConstructor)l_0), ((IConstructor)($arg1_480)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(31072,103,<463,0>,<463,103>) 
    public IConstructor Type_glb$265e93665a676ba9(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_reified_Symbol, 1)){
           IValue $arg0_483 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_483.getType(), ADT_Symbol)){
              IConstructor l_0 = null;
              if($has_type_and_arity($1, Symbol_reified_Symbol, 1)){
                 IValue $arg0_482 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_482.getType(), ADT_Symbol)){
                    IConstructor r_1 = null;
                    return ((IConstructor)($RVF.constructor(Symbol_reified_Symbol, new IValue[]{((IConstructor)($me.glb(((IConstructor)($arg0_483)), ((IConstructor)($arg0_482)))))})));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(31176,81,<464,0>,<464,81>) 
    public IConstructor Type_glb$6a5d82307ef38ff1(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_reified_Symbol, 1)){
           IValue $arg0_484 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_484.getType(), ADT_Symbol)){
              IConstructor l_0 = null;
              if($has_type_and_arity($1, Symbol_node_, 0)){
                 return ((IConstructor)($RVF.constructor(Symbol_node_, new IValue[]{})));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(31259,356,<466,0>,<473,1>) 
    public IConstructor Type_glb$69c036b2f7ba2c93(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_func_Symbol_list_Symbol_list_Symbol, 3)){
           IValue $arg0_491 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_491.getType(), ADT_Symbol)){
              IConstructor lr_0 = null;
              IValue $arg1_490 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_490.getType(), $T0)){
                 IList lp_1 = null;
                 IValue $arg2_489 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_489.getType(), $T0)){
                    IList kwl_2 = null;
                    if($has_type_and_arity($1, Symbol_func_Symbol_list_Symbol_list_Symbol, 3)){
                       IValue $arg0_488 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                       if($isComparable($arg0_488.getType(), ADT_Symbol)){
                          IConstructor rr_3 = null;
                          IValue $arg1_487 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                          if($isComparable($arg1_487.getType(), $T0)){
                             IList rp_4 = null;
                             IValue $arg2_486 = (IValue)($aadt_subscript_int(((IConstructor)$1),2));
                             if($isComparable($arg2_486.getType(), $T0)){
                                IList kwr_5 = null;
                                IConstructor glbReturn_6 = ((IConstructor)($me.glb(((IConstructor)($arg0_491)), ((IConstructor)($arg0_488)))));
                                IConstructor glbParams_7 = ((IConstructor)($me.lub(((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($arg1_490))}))), ((IConstructor)($RVF.constructor(Symbol_tuple_list_Symbol, new IValue[]{((IList)($arg1_487))}))))));
                                if((((IBool)($me.isTupleType(((IConstructor)glbParams_7))))).getValue()){
                                   return ((IConstructor)($RVF.constructor(Symbol_func_Symbol_list_Symbol_list_Symbol, new IValue[]{((IConstructor)glbReturn_6), ((IList)(((IList)($aadt_get_field(((IConstructor)glbParams_7), "symbols"))))), ((IList)(((((IBool)($equal(((IList)($arg2_489)), ((IList)($arg2_486)))))).getValue() ? $arg2_489 : ((IList)$constants.get(0)/*[]*/))))})));
                                
                                } else {
                                   return ((IConstructor)($RVF.constructor(Symbol_value_, new IValue[]{})));
                                
                                }
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(31617,67,<475,0>,<475,67>) 
    public IConstructor Type_glb$34d371f0513c1ec9(IConstructor $0, IConstructor r_1){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_493 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_493.getType(), $T4)){
              IValue $arg1_492 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_492.getType(), ADT_Symbol)){
                 IConstructor l_0 = null;
                 return ((IConstructor)($me.glb(((IConstructor)($arg1_492)), ((IConstructor)r_1))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(31685,67,<476,0>,<476,67>) 
    public IConstructor Type_glb$062f850b66975974(IConstructor l_0, IConstructor $1){ 
        
        
        if($has_type_and_arity($1, Symbol_label_str_Symbol, 2)){
           IValue $arg0_495 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
           if($isComparable($arg0_495.getType(), $T4)){
              IValue $arg1_494 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
              if($isComparable($arg1_494.getType(), ADT_Symbol)){
                 IConstructor r_1 = null;
                 return ((IConstructor)($me.glb(((IConstructor)l_0), ((IConstructor)($arg1_494)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(31754,121,<478,0>,<478,121>) 
    public IList Type_glb$588ab965903d12a1(IList l_0, IList r_1){ 
        
        
        if((((IBool)($equal(((IInteger)(M_List.size(((IList)l_0)))), ((IInteger)(M_List.size(((IList)r_1)))))))).getValue()){
           final IListWriter $listwriter496 = (IListWriter)$RVF.listWriter();
           $LCOMP497_GEN31834:
           for(IValue $elem498_for : ((IList)(M_List.index(((IList)l_0))))){
               IInteger $elem498 = (IInteger) $elem498_for;
               IInteger idx_2 = null;
               $listwriter496.append($me.glb(((IConstructor)($alist_subscript_int(((IList)l_0),((IInteger)($elem498)).intValue()))), ((IConstructor)($alist_subscript_int(((IList)r_1),((IInteger)($elem498)).intValue())))));
           
           }
           
                       return ((IList)($listwriter496.done()));
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(31877,77,<479,0>,<479,77>) 
    public IList Type_glb$c557bcbeba468980(IList l_0, IList r_1){ 
        
        
        return ((IList)$constants.get(7)/*[value()]*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(32030,119,<485,0>,<489,1>) 
    public IValue Type_typeCast$0e794e9add95c83b(IConstructor typ_0, IValue v_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T5.match(typ_0.getType(), $typeBindings)){
           if($isSubtypeOf(v_1.getType(),$T21.instantiate($typeBindings))){
              IValue x_2 = null;
              final IValue $result499 = ((IValue)v_1);
              if($T21.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result499.getType(),$T21)){
                 return ((IValue)($result499));
              
              } else {
                 return null;
              }
           }
           throw new Throw($RVF.constructor(Exception_typeCastException_Symbol_reified_value, new IValue[]{((IConstructor)($me.typeOf(((IValue)v_1)))), ((IConstructor)typ_0)}));
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(32151,500,<491,0>,<498,62>) 
    public IValue Type_make$c03dd112cd83002d(IConstructor typ_0, IString name_1, IList args_2){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T5.match(typ_0.getType(), $typeBindings)){
           final IValue $result500 = ((IValue)((IValue)$Type.make(typ_0, name_1, args_2)));
           if($T21.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result500.getType(),$T21)){
              return ((IValue)($result500));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(32654,129,<500,0>,<501,90>) 
    public IValue Type_make$ed639cfbd11999e2(IConstructor typ_0, IString name_1, IList args_2, IMap keywordArgs_3){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T5.match(typ_0.getType(), $typeBindings)){
           final IValue $result501 = ((IValue)((IValue)$Type.make(typ_0, name_1, args_2, keywordArgs_3)));
           if($T21.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result501.getType(),$T21)){
              return ((IValue)($result501));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(32786,686,<503,0>,<526,35>) 
    public IConstructor Type_typeOf$6061dcc1215fd746(IValue v_0){ 
        
        
        return ((IConstructor)((IConstructor)$Type.typeOf(v_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(33474,119,<528,0>,<529,69>) 
    public IBool Type_isIntType$609e2442e96ec30b(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_504 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_504.getType(), $T4)){
              IValue $arg1_503 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_503.getType(), $T4)){
                 IValue $arg2_502 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_502.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isIntType(((IConstructor)($arg2_502)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(33594,73,<530,0>,<530,73>) 
    public IBool Type_isIntType$bad3a4974e63bce6(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_506 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_506.getType(), $T4)){
              IValue $arg1_505 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_505.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isIntType(((IConstructor)($arg1_505)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(33668,67,<531,0>,<531,67>) 
    public IBool Type_isIntType$8b21ce695f297291(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_508 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_508.getType(), $T4)){
              IValue $arg1_507 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_507.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isIntType(((IConstructor)($arg1_507)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(33736,45,<532,0>,<532,45>) 
    public IBool Type_isIntType$ea27a84c58784f0e(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_int_, 0)){
           return ((IBool)$constants.get(1)/*true*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(33782,48,<533,0>,<533,48>) 
    public IBool Type_isIntType$51bf141928a07cf0(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(33832,122,<535,0>,<536,71>) 
    public IBool Type_isBoolType$37ece13e0d5add6a(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_511 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_511.getType(), $T4)){
              IValue $arg1_510 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_510.getType(), $T4)){
                 IValue $arg2_509 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_509.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isBoolType(((IConstructor)($arg2_509)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(33955,75,<537,0>,<537,75>) 
    public IBool Type_isBoolType$8cefc437e5ae4c94(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_513 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_513.getType(), $T4)){
              IValue $arg1_512 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_512.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isBoolType(((IConstructor)($arg1_512)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(34031,69,<538,0>,<538,69>) 
    public IBool Type_isBoolType$82e078ed93f04711(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_515 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_515.getType(), $T4)){
              IValue $arg1_514 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_514.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isBoolType(((IConstructor)($arg1_514)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(34101,47,<539,0>,<539,47>) 
    public IBool Type_isBoolType$0e05363bfd11b324(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_bool_, 0)){
           return ((IBool)$constants.get(1)/*true*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(34149,49,<540,0>,<540,49>) 
    public IBool Type_isBoolType$07e3c834d8dd35ef(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(34200,122,<542,0>,<543,71>) 
    public IBool Type_isRealType$4d3ef284eba47817(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_518 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_518.getType(), $T4)){
              IValue $arg1_517 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_517.getType(), $T4)){
                 IValue $arg2_516 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_516.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isRealType(((IConstructor)($arg2_516)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(34323,75,<544,0>,<544,75>) 
    public IBool Type_isRealType$5af787b150624afb(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_520 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_520.getType(), $T4)){
              IValue $arg1_519 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_519.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isRealType(((IConstructor)($arg1_519)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(34399,69,<545,0>,<545,69>) 
    public IBool Type_isRealType$bf4dadc558e2bc2a(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_522 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_522.getType(), $T4)){
              IValue $arg1_521 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_521.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isRealType(((IConstructor)($arg1_521)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(34469,47,<546,0>,<546,47>) 
    public IBool Type_isRealType$f338a01b49d8b0d2(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_real_, 0)){
           return ((IBool)$constants.get(1)/*true*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(34517,49,<547,0>,<547,49>) 
    public IBool Type_isRealType$ccca7a2c3ee9a4fb(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(34568,119,<549,0>,<550,69>) 
    public IBool Type_isRatType$8af97bb6a2786bed(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_525 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_525.getType(), $T4)){
              IValue $arg1_524 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_524.getType(), $T4)){
                 IValue $arg2_523 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_523.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isRatType(((IConstructor)($arg2_523)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(34688,73,<551,0>,<551,73>) 
    public IBool Type_isRatType$b9693bea2192ed8c(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_527 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_527.getType(), $T4)){
              IValue $arg1_526 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_526.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isRatType(((IConstructor)($arg1_526)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(34762,67,<552,0>,<552,67>) 
    public IBool Type_isRatType$dcf2381b59098cd9(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_529 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_529.getType(), $T4)){
              IValue $arg1_528 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_528.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isRatType(((IConstructor)($arg1_528)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(34830,45,<553,0>,<553,45>) 
    public IBool Type_isRatType$45bbf043839d3d63(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_rat_, 0)){
           return ((IBool)$constants.get(1)/*true*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(34876,48,<554,0>,<554,48>) 
    public IBool Type_isRatType$3364a9bd0fe98d3a(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(34926,119,<556,0>,<557,69>) 
    public IBool Type_isStrType$f6b0f7a14a810d8f(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_532 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_532.getType(), $T4)){
              IValue $arg1_531 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_531.getType(), $T4)){
                 IValue $arg2_530 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_530.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isStrType(((IConstructor)($arg2_530)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(35046,73,<558,0>,<558,73>) 
    public IBool Type_isStrType$f001e5ed63b40aa7(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_534 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_534.getType(), $T4)){
              IValue $arg1_533 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_533.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isStrType(((IConstructor)($arg1_533)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(35120,67,<559,0>,<559,67>) 
    public IBool Type_isStrType$7c6935fdcbba3a91(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_536 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_536.getType(), $T4)){
              IValue $arg1_535 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_535.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isStrType(((IConstructor)($arg1_535)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(35188,45,<560,0>,<560,45>) 
    public IBool Type_isStrType$9ae2419b08ae933c(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_str_, 0)){
           return ((IBool)$constants.get(1)/*true*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(35234,48,<561,0>,<561,48>) 
    public IBool Type_isStrType$f3e2471bbdc6578c(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(35284,119,<563,0>,<564,69>) 
    public IBool Type_isNumType$cebed1c4e2f4e0c1(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_539 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_539.getType(), $T4)){
              IValue $arg1_538 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_538.getType(), $T4)){
                 IValue $arg2_537 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_537.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isNumType(((IConstructor)($arg2_537)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(35404,73,<565,0>,<565,73>) 
    public IBool Type_isNumType$8f39481fe207a2d9(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_541 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_541.getType(), $T4)){
              IValue $arg1_540 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_540.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isNumType(((IConstructor)($arg1_540)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(35478,67,<566,0>,<566,67>) 
    public IBool Type_isNumType$5f826fd8c150a884(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_543 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_543.getType(), $T4)){
              IValue $arg1_542 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_542.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isNumType(((IConstructor)($arg1_542)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(35546,45,<567,0>,<567,45>) 
    public IBool Type_isNumType$f905e6a61b4f9fe9(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_num_, 0)){
           return ((IBool)$constants.get(1)/*true*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(35592,48,<568,0>,<568,48>) 
    public IBool Type_isNumType$4a01c31ce8d55ae6(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(35642,122,<570,0>,<571,71>) 
    public IBool Type_isNodeType$cd583a32160e7d2e(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_546 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_546.getType(), $T4)){
              IValue $arg1_545 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_545.getType(), $T4)){
                 IValue $arg2_544 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_544.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isNodeType(((IConstructor)($arg2_544)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(35765,75,<572,0>,<572,75>) 
    public IBool Type_isNodeType$fe2d6187af7e20d8(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_548 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_548.getType(), $T4)){
              IValue $arg1_547 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_547.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isNodeType(((IConstructor)($arg1_547)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(35841,69,<573,0>,<573,69>) 
    public IBool Type_isNodeType$753d652f78ce3ee8(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_550 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_550.getType(), $T4)){
              IValue $arg1_549 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_549.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isNodeType(((IConstructor)($arg1_549)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(35911,47,<574,0>,<574,47>) 
    public IBool Type_isNodeType$4f701f28416ac047(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_node_, 0)){
           return ((IBool)$constants.get(1)/*true*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(35959,49,<575,0>,<575,49>) 
    public IBool Type_isNodeType$70b01dc65abe027f(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_adt_str_list_Symbol, 2)){
           IValue $arg0_552 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_552.getType(), $T4)){
              IValue $arg1_551 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_551.getType(), $T4)){
                 return ((IBool)$constants.get(1)/*true*/);
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(36009,49,<576,0>,<576,49>) 
    public IBool Type_isNodeType$b82b31bdf0843130(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(36060,122,<578,0>,<579,71>) 
    public IBool Type_isVoidType$baba77517aa47f53(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_555 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_555.getType(), $T4)){
              IValue $arg1_554 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_554.getType(), $T4)){
                 IValue $arg2_553 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_553.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isVoidType(((IConstructor)($arg2_553)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(36183,75,<580,0>,<580,75>) 
    public IBool Type_isVoidType$7407b4b3f99d147a(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_557 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_557.getType(), $T4)){
              IValue $arg1_556 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_556.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isVoidType(((IConstructor)($arg1_556)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(36259,69,<581,0>,<581,69>) 
    public IBool Type_isVoidType$2ed30c48114cc0fc(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_559 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_559.getType(), $T4)){
              IValue $arg1_558 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_558.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isVoidType(((IConstructor)($arg1_558)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(36329,47,<582,0>,<582,47>) 
    public IBool Type_isVoidType$c37d3d034ac8acb0(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_void_, 0)){
           return ((IBool)$constants.get(1)/*true*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(36377,49,<583,0>,<583,49>) 
    public IBool Type_isVoidType$c29ea295639835d3(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(36428,124,<585,0>,<586,73>) 
    public IBool Type_isValueType$e0359cce2680806c(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_562 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_562.getType(), $T4)){
              IValue $arg1_561 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_561.getType(), $T4)){
                 IValue $arg2_560 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_560.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isValueType(((IConstructor)($arg2_560)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(36553,77,<587,0>,<587,77>) 
    public IBool Type_isValueType$b9ffdec5c297602d(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_564 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_564.getType(), $T4)){
              IValue $arg1_563 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_563.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isValueType(((IConstructor)($arg1_563)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(36631,71,<588,0>,<588,71>) 
    public IBool Type_isValueType$dca7aa346bf90886(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_566 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_566.getType(), $T4)){
              IValue $arg1_565 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_565.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isValueType(((IConstructor)($arg1_565)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(36703,49,<589,0>,<589,49>) 
    public IBool Type_isValueType$667072f59b8f8f70(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_value_, 0)){
           return ((IBool)$constants.get(1)/*true*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(36753,50,<590,0>,<590,50>) 
    public IBool Type_isValueType$e6f62b5b7d9c7817(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(36805,119,<592,0>,<593,69>) 
    public IBool Type_isLocType$59ad589102bf053b(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_569 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_569.getType(), $T4)){
              IValue $arg1_568 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_568.getType(), $T4)){
                 IValue $arg2_567 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_567.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isLocType(((IConstructor)($arg2_567)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(36925,73,<594,0>,<594,73>) 
    public IBool Type_isLocType$aee7bcb7f5d15058(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_571 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_571.getType(), $T4)){
              IValue $arg1_570 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_570.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isLocType(((IConstructor)($arg1_570)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(36999,67,<595,0>,<595,67>) 
    public IBool Type_isLocType$9f0441d4fb931246(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_573 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_573.getType(), $T4)){
              IValue $arg1_572 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_572.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isLocType(((IConstructor)($arg1_572)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(37067,45,<596,0>,<596,45>) 
    public IBool Type_isLocType$3b8aa102e65cda31(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_loc_, 0)){
           return ((IBool)$constants.get(1)/*true*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(37113,48,<597,0>,<597,48>) 
    public IBool Type_isLocType$7df812781da274e3(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(37163,134,<599,0>,<600,79>) 
    public IBool Type_isDateTimeType$8f25f84f6afb443d(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_576 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_576.getType(), $T4)){
              IValue $arg1_575 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_575.getType(), $T4)){
                 IValue $arg2_574 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_574.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isDateTimeType(((IConstructor)($arg2_574)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(37298,83,<601,0>,<601,83>) 
    public IBool Type_isDateTimeType$5e2f6f86adbeae05(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_578 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_578.getType(), $T4)){
              IValue $arg1_577 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_577.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isDateTimeType(((IConstructor)($arg1_577)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(37382,77,<602,0>,<602,77>) 
    public IBool Type_isDateTimeType$db79d83909345377(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_580 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_580.getType(), $T4)){
              IValue $arg1_579 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_579.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isDateTimeType(((IConstructor)($arg1_579)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(37460,55,<603,0>,<603,55>) 
    public IBool Type_isDateTimeType$bb2c79987fe265e6(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_datetime_, 0)){
           return ((IBool)$constants.get(1)/*true*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(37516,53,<604,0>,<604,53>) 
    public IBool Type_isDateTimeType$af85ab799baee4b4(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(37571,119,<606,0>,<607,69>) 
    public IBool Type_isSetType$af2b3a70c68fa026(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_583 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_583.getType(), $T4)){
              IValue $arg1_582 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_582.getType(), $T4)){
                 IValue $arg2_581 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_581.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isSetType(((IConstructor)($arg2_581)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(37691,73,<608,0>,<608,73>) 
    public IBool Type_isSetType$691733c8181a3052(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_585 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_585.getType(), $T4)){
              IValue $arg1_584 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_584.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isSetType(((IConstructor)($arg1_584)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(37765,67,<609,0>,<609,67>) 
    public IBool Type_isSetType$5b2f15b95451429d(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_587 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_587.getType(), $T4)){
              IValue $arg1_586 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_586.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isSetType(((IConstructor)($arg1_586)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(37833,46,<610,0>,<610,46>) 
    public IBool Type_isSetType$5940dec4cf1357e7(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_set_Symbol, 1)){
           IValue $arg0_588 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_588.getType(), $T4)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(37880,46,<611,0>,<611,46>) 
    public IBool Type_isSetType$6dbbe5f1ad04e52d(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_589 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_589.getType(), $T4)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(37927,48,<612,0>,<612,48>) 
    public IBool Type_isSetType$0a6211803a6f3b37(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(37977,119,<614,0>,<615,69>) 
    public IBool Type_isRelType$619927ec85f3de3a(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_592 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_592.getType(), $T4)){
              IValue $arg1_591 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_591.getType(), $T4)){
                 IValue $arg2_590 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_590.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isRelType(((IConstructor)($arg2_590)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(38097,73,<616,0>,<616,73>) 
    public IBool Type_isRelType$dfe2996f326788f1(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_594 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_594.getType(), $T4)){
              IValue $arg1_593 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_593.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isRelType(((IConstructor)($arg1_593)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(38171,67,<617,0>,<617,67>) 
    public IBool Type_isRelType$58345a7801d3f289(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_596 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_596.getType(), $T4)){
              IValue $arg1_595 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_595.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isRelType(((IConstructor)($arg1_595)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(38239,46,<618,0>,<618,46>) 
    public IBool Type_isRelType$1cd229b6628898e4(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_rel_list_Symbol, 1)){
           IValue $arg0_597 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_597.getType(), $T4)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(38286,75,<619,0>,<619,75>) 
    public IBool Type_isRelType$e684041f960d6150(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_set_Symbol, 1)){
           IValue $arg0_598 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_598.getType(), ADT_Symbol)){
              IConstructor tp_0 = null;
              if((((IBool)($me.isTupleType(((IConstructor)($arg0_598)))))).getValue()){
                 return ((IBool)$constants.get(1)/*true*/);
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(38362,48,<620,0>,<620,48>) 
    public IBool Type_isRelType$4451272f25c97a41(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(38412,128,<622,0>,<623,77>) 
    public IBool Type_isListRelType$fc572333d7ccc035(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_601 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_601.getType(), $T4)){
              IValue $arg1_600 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_600.getType(), $T4)){
                 IValue $arg2_599 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_599.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isListRelType(((IConstructor)($arg2_599)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(38541,81,<624,0>,<624,81>) 
    public IBool Type_isListRelType$4fa364f56f01bd93(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_603 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_603.getType(), $T4)){
              IValue $arg1_602 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_602.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isListRelType(((IConstructor)($arg1_602)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(38623,75,<625,0>,<625,75>) 
    public IBool Type_isListRelType$8e3e16055e697805(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_605 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_605.getType(), $T4)){
              IValue $arg1_604 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_604.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isListRelType(((IConstructor)($arg1_604)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(38699,51,<626,0>,<626,51>) 
    public IBool Type_isListRelType$521530780a74b452(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_606 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_606.getType(), $T4)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(38751,80,<627,0>,<627,80>) 
    public IBool Type_isListRelType$41e9de5fc098b33d(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_list_Symbol, 1)){
           IValue $arg0_607 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_607.getType(), ADT_Symbol)){
              IConstructor tp_0 = null;
              if((((IBool)($me.isTupleType(((IConstructor)($arg0_607)))))).getValue()){
                 return ((IBool)$constants.get(1)/*true*/);
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(38832,52,<628,0>,<628,52>) 
    public IBool Type_isListRelType$fe978cfc20935991(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(38886,125,<630,0>,<631,73>) 
    public IBool Type_isTupleType$027f2dd0a4d10869(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_610 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_610.getType(), $T4)){
              IValue $arg1_609 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_609.getType(), $T4)){
                 IValue $arg2_608 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_608.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isTupleType(((IConstructor)($arg2_608)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(39012,77,<632,0>,<632,77>) 
    public IBool Type_isTupleType$d75f7f1755750be1(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_612 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_612.getType(), $T4)){
              IValue $arg1_611 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_611.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isTupleType(((IConstructor)($arg1_611)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(39090,71,<633,0>,<633,71>) 
    public IBool Type_isTupleType$af04c969df138641(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_614 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_614.getType(), $T4)){
              IValue $arg1_613 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_613.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isTupleType(((IConstructor)($arg1_613)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(39162,50,<634,0>,<634,50>) 
    public IBool Type_isTupleType$529699f52c598fc9(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_tuple_list_Symbol, 1)){
           IValue $arg0_615 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_615.getType(), $T4)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(39213,50,<635,0>,<635,50>) 
    public IBool Type_isTupleType$670c18b105a6fd46(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(39265,121,<637,0>,<638,71>) 
    public IBool Type_isListType$5cb20471614ceb14(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_618 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_618.getType(), $T4)){
              IValue $arg1_617 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_617.getType(), $T4)){
                 IValue $arg2_616 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_616.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isListType(((IConstructor)($arg2_616)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(39387,75,<639,0>,<639,75>) 
    public IBool Type_isListType$2cd097e45f390e11(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_620 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_620.getType(), $T4)){
              IValue $arg1_619 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_619.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isListType(((IConstructor)($arg1_619)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(39463,69,<640,0>,<640,69>) 
    public IBool Type_isListType$f9325150bcff8f64(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_622 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_622.getType(), $T4)){
              IValue $arg1_621 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_621.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isListType(((IConstructor)($arg1_621)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(39533,48,<641,0>,<641,48>) 
    public IBool Type_isListType$e2050780d70f05ef(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_list_Symbol, 1)){
           IValue $arg0_623 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_623.getType(), $T4)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(39582,48,<642,0>,<642,48>) 
    public IBool Type_isListType$67c1630c1e94e46f(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_lrel_list_Symbol, 1)){
           IValue $arg0_624 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_624.getType(), $T4)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(39631,49,<643,0>,<643,49>) 
    public IBool Type_isListType$a5f9e5ebaadf5ccd(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(39682,119,<645,0>,<646,69>) 
    public IBool Type_isMapType$dd63ec350694a9b0(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_627 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_627.getType(), $T4)){
              IValue $arg1_626 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_626.getType(), $T4)){
                 IValue $arg2_625 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_625.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isMapType(((IConstructor)($arg2_625)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(39802,73,<647,0>,<647,73>) 
    public IBool Type_isMapType$aa695167615ea00a(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_629 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_629.getType(), $T4)){
              IValue $arg1_628 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_628.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isMapType(((IConstructor)($arg1_628)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(39876,67,<648,0>,<648,67>) 
    public IBool Type_isMapType$023e4a504202eeef(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_631 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_631.getType(), $T4)){
              IValue $arg1_630 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_630.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isMapType(((IConstructor)($arg1_630)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(39944,48,<649,0>,<649,48>) 
    public IBool Type_isMapType$12f2aa85890447c6(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_map_Symbol_Symbol, 2)){
           IValue $arg0_633 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_633.getType(), $T4)){
              IValue $arg1_632 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_632.getType(), $T4)){
                 return ((IBool)$constants.get(1)/*true*/);
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(39993,48,<650,0>,<650,48>) 
    public IBool Type_isMapType$67db2d90e02d2133(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(40043,119,<652,0>,<653,69>) 
    public IBool Type_isBagType$b98ae82cf86bb221(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_636 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_636.getType(), $T4)){
              IValue $arg1_635 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_635.getType(), $T4)){
                 IValue $arg2_634 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_634.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isBagType(((IConstructor)($arg2_634)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(40163,73,<654,0>,<654,73>) 
    public IBool Type_isBagType$978835a2e17706d0(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_638 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_638.getType(), $T4)){
              IValue $arg1_637 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_637.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isBagType(((IConstructor)($arg1_637)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(40237,67,<655,0>,<655,67>) 
    public IBool Type_isBagType$5bfea3bad135ca42(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_640 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_640.getType(), $T4)){
              IValue $arg1_639 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_639.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isBagType(((IConstructor)($arg1_639)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(40305,46,<656,0>,<656,46>) 
    public IBool Type_isBagType$16ada3424c9263d8(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_bag_Symbol, 1)){
           IValue $arg0_641 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_641.getType(), $T4)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(40352,48,<657,0>,<657,48>) 
    public IBool Type_isBagType$78a6a76069464291(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(40402,119,<659,0>,<660,69>) 
    public IBool Type_isADTType$75f588280052e41a(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_644 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_644.getType(), $T4)){
              IValue $arg1_643 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_643.getType(), $T4)){
                 IValue $arg2_642 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_642.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isADTType(((IConstructor)($arg2_642)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(40522,73,<661,0>,<661,73>) 
    public IBool Type_isADTType$eec08a18faad2421(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_646 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_646.getType(), $T4)){
              IValue $arg1_645 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_645.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isADTType(((IConstructor)($arg1_645)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(40596,67,<662,0>,<662,67>) 
    public IBool Type_isADTType$c9e59ac203bf16c6(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_648 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_648.getType(), $T4)){
              IValue $arg1_647 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_647.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isADTType(((IConstructor)($arg1_647)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(40664,48,<663,0>,<663,48>) 
    public IBool Type_isADTType$06ad40947bb0990a(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_adt_str_list_Symbol, 2)){
           IValue $arg0_650 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_650.getType(), $T4)){
              IValue $arg1_649 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_649.getType(), $T4)){
                 return ((IBool)$constants.get(1)/*true*/);
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(40713,50,<664,0>,<664,50>) 
    public IBool Type_isADTType$55f1893a25b5971e(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_reified_Symbol, 1)){
           IValue $arg0_651 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_651.getType(), $T4)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(40764,48,<665,0>,<665,48>) 
    public IBool Type_isADTType$4079510b224fe1ba(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(40814,143,<667,0>,<668,85>) 
    public IBool Type_isConstructorType$03b6fed0e3f2a6ac(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_654 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_654.getType(), $T4)){
              IValue $arg1_653 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_653.getType(), $T4)){
                 IValue $arg2_652 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_652.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isConstructorType(((IConstructor)($arg2_652)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(40958,89,<669,0>,<669,89>) 
    public IBool Type_isConstructorType$76715e3a6e16c474(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_656 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_656.getType(), $T4)){
              IValue $arg1_655 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_655.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isConstructorType(((IConstructor)($arg1_655)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(41048,83,<670,0>,<670,83>) 
    public IBool Type_isConstructorType$bfafd605f17a2265(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_658 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_658.getType(), $T4)){
              IValue $arg1_657 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_657.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isConstructorType(((IConstructor)($arg1_657)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(41132,83,<671,0>,<671,83>) 
    public IBool Type_isConstructorType$5f7682c23fcd1429(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_cons_Symbol_str_list_Symbol, 3)){
           IValue $arg0_661 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_661.getType(), ADT_Symbol)){
              IValue $arg1_660 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_660.getType(), $T2)){
                 IValue $arg2_659 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_659.getType(), $T0)){
                    return ((IBool)$constants.get(1)/*true*/);
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(41216,56,<672,0>,<672,56>) 
    public IBool Type_isConstructorType$7c73a110d1ea5afa(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(41274,106,<674,0>,<675,54>) 
    public IBool Type_isAliasType$be7b4838d41f7750(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_664 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_664.getType(), $T4)){
              IValue $arg1_663 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_663.getType(), $T4)){
                 IValue $arg2_662 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_662.getType(), $T4)){
                    return ((IBool)$constants.get(1)/*true*/);
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(41381,77,<676,0>,<676,77>) 
    public IBool Type_isAliasType$36bb377f97e90786(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_666 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_666.getType(), $T4)){
              IValue $arg1_665 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_665.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isAliasType(((IConstructor)($arg1_665)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(41459,71,<677,0>,<677,71>) 
    public IBool Type_isAliasType$53ed17cc6d4d56d5(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_668 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_668.getType(), $T4)){
              IValue $arg1_667 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_667.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isAliasType(((IConstructor)($arg1_667)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(41531,50,<678,0>,<678,50>) 
    public IBool Type_isAliasType$a82f2a52c32629d2(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(41583,134,<680,0>,<681,79>) 
    public IBool Type_isFunctionType$cdb5ce9942d16bd2(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_671 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_671.getType(), $T4)){
              IValue $arg1_670 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_670.getType(), $T4)){
                 IValue $arg2_669 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_669.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isFunctionType(((IConstructor)($arg2_669)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(41718,83,<682,0>,<682,83>) 
    public IBool Type_isFunctionType$d383dd66853ee549(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_673 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_673.getType(), $T4)){
              IValue $arg1_672 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_672.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isFunctionType(((IConstructor)($arg1_672)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(41802,77,<683,0>,<683,77>) 
    public IBool Type_isFunctionType$25cdd570591a3b3f(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_675 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_675.getType(), $T4)){
              IValue $arg1_674 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_674.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isFunctionType(((IConstructor)($arg1_674)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(41880,56,<684,0>,<684,56>) 
    public IBool Type_isFunctionType$670ad7351fb38cc0(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_func_Symbol_list_Symbol_list_Symbol, 3)){
           IValue $arg0_678 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_678.getType(), $T4)){
              IValue $arg1_677 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_677.getType(), $T4)){
                 IValue $arg2_676 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_676.getType(), $T4)){
                    return ((IBool)$constants.get(1)/*true*/);
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(41937,53,<685,0>,<685,53>) 
    public IBool Type_isFunctionType$50a9927a87b4bf31(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(41992,131,<687,0>,<688,77>) 
    public IBool Type_isReifiedType$afaab65144832e90(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_681 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_681.getType(), $T4)){
              IValue $arg1_680 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_680.getType(), $T4)){
                 IValue $arg2_679 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_679.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isReifiedType(((IConstructor)($arg2_679)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(42124,81,<689,0>,<689,81>) 
    public IBool Type_isReifiedType$2398657586b47cef(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_683 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_683.getType(), $T4)){
              IValue $arg1_682 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_682.getType(), ADT_Symbol)){
                 IConstructor tvb_0 = null;
                 return ((IBool)($me.isReifiedType(((IConstructor)($arg1_682)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(42206,75,<690,0>,<690,75>) 
    public IBool Type_isReifiedType$6fed314f55ec225d(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_685 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_685.getType(), $T4)){
              IValue $arg1_684 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_684.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isReifiedType(((IConstructor)($arg1_684)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(42282,54,<691,0>,<691,54>) 
    public IBool Type_isReifiedType$a8d50b8b608f8ee7(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_reified_Symbol, 1)){
           IValue $arg0_686 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_686.getType(), $T4)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(42337,52,<692,0>,<692,52>) 
    public IBool Type_isReifiedType$f758db94422c9072(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(42391,115,<694,0>,<695,54>) 
    public IBool Type_isTypeVar$301a952e924ee4a8(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameter_str_Symbol, 2)){
           IValue $arg0_688 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_688.getType(), $T4)){
              IValue $arg1_687 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_687.getType(), $T4)){
                 return ((IBool)$constants.get(1)/*true*/);
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(42507,69,<696,0>,<696,69>) 
    public IBool Type_isTypeVar$b0dd3673fa06bcfe(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_alias_str_list_Symbol_Symbol, 3)){
           IValue $arg0_691 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_691.getType(), $T4)){
              IValue $arg1_690 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_690.getType(), $T4)){
                 IValue $arg2_689 = (IValue)($aadt_subscript_int(((IConstructor)$0),2));
                 if($isComparable($arg2_689.getType(), ADT_Symbol)){
                    IConstructor at_0 = null;
                    return ((IBool)($me.isTypeVar(((IConstructor)($arg2_689)))));
                 
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(42577,67,<697,0>,<697,67>) 
    public IBool Type_isTypeVar$78fc26e12b32aede(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_label_str_Symbol, 2)){
           IValue $arg0_693 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_693.getType(), $T4)){
              IValue $arg1_692 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_692.getType(), ADT_Symbol)){
                 IConstructor lt_0 = null;
                 return ((IBool)($me.isTypeVar(((IConstructor)($arg1_692)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Type.rsc|(42645,48,<698,0>,<698,48>) 
    public IBool Type_isTypeVar$7fdb3c6deae5e954(IConstructor $__0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `Type`");
    }
}