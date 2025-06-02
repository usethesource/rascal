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
public class $ParseTree 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.$Message_$I,
    	rascal.$ParseTree_$I,
    	rascal.$Type_$I,
    	rascal.$List_$I {

    private final $ParseTree_$I $me;
    private final IList $constants;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_ParseTree_parse$4d2afd5837b53c80;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_ParseTree_parse$696136d9f024501e;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_ParseTree_parse$55b9e584aedc91f7;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_ParseTree_parser$c4d258086f531875;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_ParseTree_firstAmbiguityFinder$4c88e56e1de6a5c6;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_ParseTree_parsers$43e4839e939e0971;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_ParseTree_firstAmbiguityFinders$4e066d437479bb46;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_ParseTree_loadParsers$12936d804ae9b4a7;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_ParseTree_loadParser$a320f401f0ea0902;

    
    public final rascal.$Message M_Message;
    public final rascal.$Type M_Type;
    public final rascal.$List M_List;

    
    final org.rascalmpl.library.Prelude $Prelude; // TODO: asBaseClassName will generate name collisions if there are more of the same name in different packages

    
    public final io.usethesource.vallang.type.Type $T34;	/*aint(alabel="character")*/
    public final io.usethesource.vallang.type.Type $T3;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T8;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T31;	/*aint(alabel="dot")*/
    public final io.usethesource.vallang.type.Type $T5;	/*aloc()*/
    public final io.usethesource.vallang.type.Type $T4;	/*aparameter("U",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T22;	/*aint(alabel="column")*/
    public final io.usethesource.vallang.type.Type $T32;	/*astr(alabel="label")*/
    public final io.usethesource.vallang.type.Type $T35;	/*aint(alabel="begin")*/
    public final io.usethesource.vallang.type.Type $T20;	/*astr(alabel="name")*/
    public final io.usethesource.vallang.type.Type $T21;	/*astr(alabel="cons")*/
    public final io.usethesource.vallang.type.Type $T33;	/*astr(alabel="string")*/
    public final io.usethesource.vallang.type.Type $T19;	/*aint(alabel="cycleLength")*/
    public final io.usethesource.vallang.type.Type $T11;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T36;	/*aint(alabel="end")*/
    public final io.usethesource.vallang.type.Type $T40;	/*avalue(alabel="input")*/
    public final io.usethesource.vallang.type.Type $T43;	/*aparameter("U",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T41;	/*aloc(alabel="origin")*/
    public final io.usethesource.vallang.type.Type $T39;	/*aparameter("T",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type Symbol_keywords_str;	/*acons(aadt("Symbol",[],dataSyntax()),[astr(alabel="name")],[],alabel="keywords")*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type Tree_cycle_Symbol_int;	/*acons(aadt("Tree",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol"),aint(alabel="cycleLength")],[],alabel="cycle")*/
    public final io.usethesource.vallang.type.Type $T37;	/*alist(aadt("Tree",[],dataSyntax()),alabel="args")*/
    public final io.usethesource.vallang.type.Type Symbol_lex_str;	/*acons(aadt("Symbol",[],dataSyntax()),[astr(alabel="name")],[],alabel="lex")*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T29;	/*alist(aadt("Symbol",[],dataSyntax()),alabel="separators")*/
    public final io.usethesource.vallang.type.Type Symbol_iter_star_seps_Symbol_list_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol"),alist(aadt("Symbol",[],dataSyntax()),alabel="separators")],[],alabel="iter-star-seps")*/
    public final io.usethesource.vallang.type.Type Symbol_cilit_str;	/*acons(aadt("Symbol",[],dataSyntax()),[astr(alabel="string")],[],alabel="cilit")*/
    public final io.usethesource.vallang.type.Type $T45;	/*afunc(aparameter("U",avalue(),closed=true),[avalue(alabel="input"),aloc(alabel="origin")],[])*/
    public final io.usethesource.vallang.type.Type $T0;	/*areified(aadt("Tree",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type $T18;	/*alist(aadt("Symbol",[],dataSyntax()),alabel="symbols")*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T27;	/*alist(aadt("Production",[],dataSyntax()),alabel="choices")*/
    public final io.usethesource.vallang.type.Type ADT_CharRange;	/*aadt("CharRange",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T16;	/*alist(aadt("CharRange",[],dataSyntax()),alabel="ranges")*/
    public final io.usethesource.vallang.type.Type $T26;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=false,alabel="tree")*/
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeEvent;	/*aadt("LocationChangeEvent",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T25;	/*aset(aadt("Condition",[],dataSyntax()),alabel="conditions")*/
    public final io.usethesource.vallang.type.Type $T10;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=false)*/
    public final io.usethesource.vallang.type.Type Symbol_iter_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol")],[],alabel="iter")*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type Attr_assoc_Associativity;	/*acons(aadt("Attr",[],dataSyntax()),[aadt("Associativity",[],dataSyntax(),alabel="assoc")],[],alabel="assoc")*/
    public final io.usethesource.vallang.type.Type ADT_Associativity;	/*aadt("Associativity",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T28;	/*alist(aadt("Symbol",[],dataSyntax()),alabel="parameters")*/
    public final io.usethesource.vallang.type.Type Symbol_parameterized_lex_str_list_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[astr(alabel="name"),alist(aadt("Symbol",[],dataSyntax()),alabel="parameters")],[],alabel="parameterized-lex")*/
    public final io.usethesource.vallang.type.Type CharRange_range_int_int;	/*acons(aadt("CharRange",[],dataSyntax()),[aint(alabel="begin"),aint(alabel="end")],[],alabel="range")*/
    public final io.usethesource.vallang.type.Type Associativity_assoc_;	/*acons(aadt("Associativity",[],dataSyntax()),[],[],alabel="assoc")*/
    public final io.usethesource.vallang.type.Type Production_reference_Symbol_str;	/*acons(aadt("Production",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="def"),astr(alabel="cons")],[],alabel="reference")*/
    public final io.usethesource.vallang.type.Type Associativity_non_assoc_;	/*acons(aadt("Associativity",[],dataSyntax()),[],[],alabel="non-assoc")*/
    public final io.usethesource.vallang.type.Type Tree_appl_Production_list_Tree;	/*acons(aadt("Tree",[],dataSyntax()),[aadt("Production",[],dataSyntax(),alabel="prod"),alist(aadt("Tree",[],dataSyntax()),alabel="args")],[],alabel="appl")*/
    public final io.usethesource.vallang.type.Type Condition_not_precede_Symbol;	/*acons(aadt("Condition",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol")],[],alabel="not-precede")*/
    public final io.usethesource.vallang.type.Type Symbol_start_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol")],[],alabel="start")*/
    public final io.usethesource.vallang.type.Type Symbol_sort_str;	/*acons(aadt("Symbol",[],dataSyntax()),[astr(alabel="name")],[],alabel="sort")*/
    public final io.usethesource.vallang.type.Type $T30;	/*aset(aadt("Tree",[],dataSyntax()),alabel="alternatives")*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type Condition_end_of_line_;	/*acons(aadt("Condition",[],dataSyntax()),[],[],alabel="end-of-line")*/
    public final io.usethesource.vallang.type.Type Tree_char_int;	/*acons(aadt("Tree",[],dataSyntax()),[aint(alabel="character")],[],alabel="char")*/
    public final io.usethesource.vallang.type.Type $T14;	/*alist(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type Symbol_lit_str;	/*acons(aadt("Symbol",[],dataSyntax()),[astr(alabel="string")],[],alabel="lit")*/
    public final io.usethesource.vallang.type.Type Condition_delete_Symbol;	/*acons(aadt("Condition",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol")],[],alabel="delete")*/
    public final io.usethesource.vallang.type.Type Symbol_layouts_str;	/*acons(aadt("Symbol",[],dataSyntax()),[astr(alabel="name")],[],alabel="layouts")*/
    public final io.usethesource.vallang.type.Type $T6;	/*abool()*/
    public final io.usethesource.vallang.type.Type Attr_bracket_;	/*acons(aadt("Attr",[],dataSyntax()),[],[],alabel="bracket")*/
    public final io.usethesource.vallang.type.Type Symbol_opt_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol")],[],alabel="opt")*/
    public final io.usethesource.vallang.type.Type Associativity_left_;	/*acons(aadt("Associativity",[],dataSyntax()),[],[],alabel="left")*/
    public final io.usethesource.vallang.type.Type $T23;	/*aset(aadt("Attr",[],dataSyntax()),alabel="attributes")*/
    public final io.usethesource.vallang.type.Type Production_prod_Symbol_list_Symbol_set_Attr;	/*acons(aadt("Production",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="def"),alist(aadt("Symbol",[],dataSyntax()),alabel="symbols"),aset(aadt("Attr",[],dataSyntax()),alabel="attributes")],[],alabel="prod")*/
    public final io.usethesource.vallang.type.Type $T24;	/*aset(aadt("Production",[],dataSyntax()),alabel="alternatives")*/
    public final io.usethesource.vallang.type.Type Symbol_conditional_Symbol_set_Condition;	/*acons(aadt("Symbol",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol"),aset(aadt("Condition",[],dataSyntax()),alabel="conditions")],[],alabel="conditional")*/
    public final io.usethesource.vallang.type.Type $T17;	/*aset(aadt("Symbol",[],dataSyntax()),alabel="alternatives")*/
    public final io.usethesource.vallang.type.Type $T15;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type TreeSearchResult_1_treeFound_;	/*acons(aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax()),[aparameter("T",aadt("Tree",[],dataSyntax()),closed=false,alabel="tree")],[],alabel="treeFound")*/
    public final io.usethesource.vallang.type.Type Condition_begin_of_line_;	/*acons(aadt("Condition",[],dataSyntax()),[],[],alabel="begin-of-line")*/
    public final io.usethesource.vallang.type.Type Production_associativity_Symbol_Associativity_set_Production;	/*acons(aadt("Production",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="def"),aadt("Associativity",[],dataSyntax(),alabel="assoc"),aset(aadt("Production",[],dataSyntax()),alabel="alternatives")],[],alabel="associativity")*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type Symbol_seq_list_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[alist(aadt("Symbol",[],dataSyntax()),alabel="symbols")],[],alabel="seq")*/
    public final io.usethesource.vallang.type.Type $T1;	/*alist(aadt("Production",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type $T12;	/*alist(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type TreeSearchResult_1_treeNotFound_;	/*acons(aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax()),[],[],alabel="treeNotFound")*/
    public final io.usethesource.vallang.type.Type Condition_at_column_int;	/*acons(aadt("Condition",[],dataSyntax()),[aint(alabel="column")],[],alabel="at-column")*/
    public final io.usethesource.vallang.type.Type $T44;	/*areified(aparameter("U",avalue(),closed=true),alabel="nonterminal")*/
    public final io.usethesource.vallang.type.Type Symbol_char_class_list_CharRange;	/*acons(aadt("Symbol",[],dataSyntax()),[alist(aadt("CharRange",[],dataSyntax()),alabel="ranges")],[],alabel="char-class")*/
    public final io.usethesource.vallang.type.Type $T7;	/*areified(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type Condition_follow_Symbol;	/*acons(aadt("Condition",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol")],[],alabel="follow")*/
    public final io.usethesource.vallang.type.Type Production_skipped_Symbol;	/*acons(aadt("Production",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="def")],[],alabel="skipped")*/
    public final io.usethesource.vallang.type.Type Condition_precede_Symbol;	/*acons(aadt("Condition",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol")],[],alabel="precede")*/
    public final io.usethesource.vallang.type.Type $T38;	/*afunc(aparameter("T",avalue(),closed=true),[avalue(alabel="input"),aloc(alabel="origin")],[])*/
    public final io.usethesource.vallang.type.Type Production_error_Symbol_Production_int;	/*acons(aadt("Production",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="def"),aadt("Production",[],dataSyntax(),alabel="prod"),aint(alabel="dot")],[],alabel="error")*/
    public final io.usethesource.vallang.type.Type Symbol_empty_;	/*acons(aadt("Symbol",[],dataSyntax()),[],[],alabel="empty")*/
    public final io.usethesource.vallang.type.Type Condition_not_follow_Symbol;	/*acons(aadt("Condition",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol")],[],alabel="not-follow")*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type Symbol_alt_set_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[aset(aadt("Symbol",[],dataSyntax()),alabel="alternatives")],[],alabel="alt")*/
    public final io.usethesource.vallang.type.Type Condition_except_str;	/*acons(aadt("Condition",[],dataSyntax()),[astr(alabel="label")],[],alabel="except")*/
    public final io.usethesource.vallang.type.Type ADT_Exception;	/*aadt("Exception",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationType;	/*aadt("LocationType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T9;	/*areified(aparameter("T",aadt("Tree",[],dataSyntax()),closed=false))*/
    public final io.usethesource.vallang.type.Type $T13;	/*aset(aadt("Production",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type Production_regular_Symbol;	/*acons(aadt("Production",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="def")],[],alabel="regular")*/
    public final io.usethesource.vallang.type.Type $T2;	/*areified(aparameter("U",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type Symbol_iter_seps_Symbol_list_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol"),alist(aadt("Symbol",[],dataSyntax()),alabel="separators")],[],alabel="iter-seps")*/
    public final io.usethesource.vallang.type.Type Symbol_parameterized_sort_str_list_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[astr(alabel="name"),alist(aadt("Symbol",[],dataSyntax()),alabel="parameters")],[],alabel="parameterized-sort")*/
    public final io.usethesource.vallang.type.Type Tree_amb_set_Tree;	/*acons(aadt("Tree",[],dataSyntax()),[aset(aadt("Tree",[],dataSyntax()),alabel="alternatives")],[],alabel="amb")*/
    public final io.usethesource.vallang.type.Type Production_priority_Symbol_list_Production;	/*acons(aadt("Production",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="def"),alist(aadt("Production",[],dataSyntax()),alabel="choices")],[],alabel="priority")*/
    public final io.usethesource.vallang.type.Type $T42;	/*afunc(aparameter("U",avalue(),closed=true),[areified(aparameter("U",avalue(),closed=true),alabel="nonterminal"),avalue(alabel="input"),aloc(alabel="origin")],[])*/
    public final io.usethesource.vallang.type.Type Associativity_right_;	/*acons(aadt("Associativity",[],dataSyntax()),[],[],alabel="right")*/
    public final io.usethesource.vallang.type.Type Symbol_iter_star_Symbol;	/*acons(aadt("Symbol",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="symbol")],[],alabel="iter-star")*/

    public $ParseTree(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $ParseTree(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($ParseTree_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.$ParseTree.class, this);
        
        
       
       M_Message = mstore.extendModule(rascal.$Message.class, rex, rascal.$Message::new, $me);
       M_Type = mstore.extendModule(rascal.$Type.class, rex, rascal.$Type::new, $me);
       M_List = mstore.extendModule(rascal.$List.class, rex, rascal.$List::new, $me);
                          
       
       $TS.importStore(M_Message.$TS);
       $TS.importStore(M_Type.$TS);
       $TS.importStore(M_List.$TS);
       
       $Prelude = $initLibrary("org.rascalmpl.library.Prelude"); 
    
       $constants = readBinaryConstantsFile(this.getClass(), "rascal//$ParseTree.constants", 8, "40d90c4f6e73240a2157ffdc5e5da56b");
       ADT_Symbol = $adt("Symbol");
       ADT_Tree = $adt("Tree");
       ADT_LocationChangeType = $adt("LocationChangeType");
       ADT_Production = $adt("Production");
       ADT_CharRange = $adt("CharRange");
       ADT_Condition = $adt("Condition");
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       ADT_Attr = $adt("Attr");
       ADT_Associativity = $adt("Associativity");
       ADT_IOCapability = $adt("IOCapability");
       ADT_Message = $adt("Message");
       ADT_RuntimeException = $adt("RuntimeException");
       ADT_Exception = $adt("Exception");
       ADT_LocationType = $adt("LocationType");
       $T34 = $TF.integerType();
       $T3 = $TF.valueType();
       $T8 = $TF.parameterType("T", $T3);
       $T31 = $TF.integerType();
       $T5 = $TF.sourceLocationType();
       $T4 = $TF.parameterType("U", $T3);
       $T22 = $TF.integerType();
       $T32 = $TF.stringType();
       $T35 = $TF.integerType();
       $T20 = $TF.stringType();
       $T21 = $TF.stringType();
       $T33 = $TF.stringType();
       $T19 = $TF.integerType();
       $T11 = $TF.stringType();
       $T36 = $TF.integerType();
       $T40 = $TF.valueType();
       $T43 = $TF.parameterType("U", $T3);
       $T41 = $TF.sourceLocationType();
       $T39 = $TF.parameterType("T", $T3);
       $T37 = $TF.listType(ADT_Tree);
       $T29 = $TF.listType(ADT_Symbol);
       $T45 = $TF.functionType($T43, $TF.tupleType($T40, "input", $T41, "origin"), $TF.tupleEmpty());
       $T0 = $RTF.reifiedType(ADT_Tree);
       $T18 = $TF.listType(ADT_Symbol);
       $T27 = $TF.listType(ADT_Production);
       $T16 = $TF.listType(ADT_CharRange);
       $T26 = $TF.parameterType("T", ADT_Tree);
       $T25 = $TF.setType(ADT_Condition);
       $T10 = $TF.parameterType("T", ADT_Tree);
       $T28 = $TF.listType(ADT_Symbol);
       $T30 = $TF.setType(ADT_Tree);
       $T14 = $TF.listType($T8);
       $T6 = $TF.boolType();
       $T23 = $TF.setType(ADT_Attr);
       $T24 = $TF.setType(ADT_Production);
       $T17 = $TF.setType(ADT_Symbol);
       $T15 = $TF.parameterType("T", ADT_Tree);
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T15 });
       $T1 = $TF.listType(ADT_Production);
       $T12 = $TF.listType(ADT_Symbol);
       $T44 = $RTF.reifiedType($T43);
       $T7 = $RTF.reifiedType($T8);
       $T38 = $TF.functionType($T39, $TF.tupleType($T40, "input", $T41, "origin"), $TF.tupleEmpty());
       $T9 = $RTF.reifiedType($T10);
       $T13 = $TF.setType(ADT_Production);
       $T2 = $RTF.reifiedType($T4);
       $T42 = $TF.functionType($T43, $TF.tupleType($T44, "nonterminal", $T40, "input", $T41, "origin"), $TF.tupleEmpty());
       Symbol_keywords_str = $TF.constructor($TS, ADT_Symbol, "keywords", $TF.stringType(), "name");
       Tree_cycle_Symbol_int = $TF.constructor($TS, ADT_Tree, "cycle", M_Type.ADT_Symbol, "symbol", $TF.integerType(), "cycleLength");
       Symbol_lex_str = $TF.constructor($TS, ADT_Symbol, "lex", $TF.stringType(), "name");
       Symbol_iter_star_seps_Symbol_list_Symbol = $TF.constructor($TS, ADT_Symbol, "iter-star-seps", M_Type.ADT_Symbol, "symbol", $TF.listType(ADT_Symbol), "separators");
       Symbol_cilit_str = $TF.constructor($TS, ADT_Symbol, "cilit", $TF.stringType(), "string");
       Symbol_iter_Symbol = $TF.constructor($TS, ADT_Symbol, "iter", M_Type.ADT_Symbol, "symbol");
       Attr_assoc_Associativity = $TF.constructor($TS, ADT_Attr, "assoc", ADT_Associativity, "assoc");
       Symbol_parameterized_lex_str_list_Symbol = $TF.constructor($TS, ADT_Symbol, "parameterized-lex", $TF.stringType(), "name", $TF.listType(ADT_Symbol), "parameters");
       CharRange_range_int_int = $TF.constructor($TS, ADT_CharRange, "range", $TF.integerType(), "begin", $TF.integerType(), "end");
       Associativity_assoc_ = $TF.constructor($TS, ADT_Associativity, "assoc");
       Production_reference_Symbol_str = $TF.constructor($TS, ADT_Production, "reference", M_Type.ADT_Symbol, "def", $TF.stringType(), "cons");
       Associativity_non_assoc_ = $TF.constructor($TS, ADT_Associativity, "non-assoc");
       Tree_appl_Production_list_Tree = $TF.constructor($TS, ADT_Tree, "appl", M_Type.ADT_Production, "prod", $TF.listType(ADT_Tree), "args");
       Condition_not_precede_Symbol = $TF.constructor($TS, ADT_Condition, "not-precede", M_Type.ADT_Symbol, "symbol");
       Symbol_start_Symbol = $TF.constructor($TS, ADT_Symbol, "start", M_Type.ADT_Symbol, "symbol");
       Symbol_sort_str = $TF.constructor($TS, ADT_Symbol, "sort", $TF.stringType(), "name");
       Condition_end_of_line_ = $TF.constructor($TS, ADT_Condition, "end-of-line");
       Tree_char_int = $TF.constructor($TS, ADT_Tree, "char", $TF.integerType(), "character");
       Symbol_lit_str = $TF.constructor($TS, ADT_Symbol, "lit", $TF.stringType(), "string");
       Condition_delete_Symbol = $TF.constructor($TS, ADT_Condition, "delete", M_Type.ADT_Symbol, "symbol");
       Symbol_layouts_str = $TF.constructor($TS, ADT_Symbol, "layouts", $TF.stringType(), "name");
       Attr_bracket_ = $TF.constructor($TS, ADT_Attr, "bracket");
       Symbol_opt_Symbol = $TF.constructor($TS, ADT_Symbol, "opt", M_Type.ADT_Symbol, "symbol");
       Associativity_left_ = $TF.constructor($TS, ADT_Associativity, "left");
       Production_prod_Symbol_list_Symbol_set_Attr = $TF.constructor($TS, ADT_Production, "prod", M_Type.ADT_Symbol, "def", $TF.listType(ADT_Symbol), "symbols", $TF.setType(ADT_Attr), "attributes");
       Symbol_conditional_Symbol_set_Condition = $TF.constructor($TS, ADT_Symbol, "conditional", M_Type.ADT_Symbol, "symbol", $TF.setType(ADT_Condition), "conditions");
       TreeSearchResult_1_treeFound_ = $TF.constructor($TS, ADT_TreeSearchResult_1, "treeFound", $TF.parameterType("T", ADT_Tree), "tree");
       Condition_begin_of_line_ = $TF.constructor($TS, ADT_Condition, "begin-of-line");
       Production_associativity_Symbol_Associativity_set_Production = $TF.constructor($TS, ADT_Production, "associativity", M_Type.ADT_Symbol, "def", ADT_Associativity, "assoc", $TF.setType(ADT_Production), "alternatives");
       Symbol_seq_list_Symbol = $TF.constructor($TS, ADT_Symbol, "seq", $TF.listType(ADT_Symbol), "symbols");
       TreeSearchResult_1_treeNotFound_ = $TF.constructor($TS, ADT_TreeSearchResult_1, "treeNotFound");
       Condition_at_column_int = $TF.constructor($TS, ADT_Condition, "at-column", $TF.integerType(), "column");
       Symbol_char_class_list_CharRange = $TF.constructor($TS, ADT_Symbol, "char-class", $TF.listType(ADT_CharRange), "ranges");
       Condition_follow_Symbol = $TF.constructor($TS, ADT_Condition, "follow", M_Type.ADT_Symbol, "symbol");
       Production_skipped_Symbol = $TF.constructor($TS, ADT_Production, "skipped", M_Type.ADT_Symbol, "def");
       Condition_precede_Symbol = $TF.constructor($TS, ADT_Condition, "precede", M_Type.ADT_Symbol, "symbol");
       Production_error_Symbol_Production_int = $TF.constructor($TS, ADT_Production, "error", M_Type.ADT_Symbol, "def", M_Type.ADT_Production, "prod", $TF.integerType(), "dot");
       Symbol_empty_ = $TF.constructor($TS, ADT_Symbol, "empty");
       Condition_not_follow_Symbol = $TF.constructor($TS, ADT_Condition, "not-follow", M_Type.ADT_Symbol, "symbol");
       Symbol_alt_set_Symbol = $TF.constructor($TS, ADT_Symbol, "alt", $TF.setType(ADT_Symbol), "alternatives");
       Condition_except_str = $TF.constructor($TS, ADT_Condition, "except", $TF.stringType(), "label");
       Production_regular_Symbol = $TF.constructor($TS, ADT_Production, "regular", M_Type.ADT_Symbol, "def");
       Symbol_iter_seps_Symbol_list_Symbol = $TF.constructor($TS, ADT_Symbol, "iter-seps", M_Type.ADT_Symbol, "symbol", $TF.listType(ADT_Symbol), "separators");
       Symbol_parameterized_sort_str_list_Symbol = $TF.constructor($TS, ADT_Symbol, "parameterized-sort", $TF.stringType(), "name", $TF.listType(ADT_Symbol), "parameters");
       Tree_amb_set_Tree = $TF.constructor($TS, ADT_Tree, "amb", $TF.setType(ADT_Tree), "alternatives");
       Production_priority_Symbol_list_Production = $TF.constructor($TS, ADT_Production, "priority", M_Type.ADT_Symbol, "def", $TF.listType(ADT_Production), "choices");
       Associativity_right_ = $TF.constructor($TS, ADT_Associativity, "right");
       Symbol_iter_star_Symbol = $TF.constructor($TS, ADT_Symbol, "iter-star", M_Type.ADT_Symbol, "symbol");
    
       
       $kwpDefaults_ParseTree_parse$4d2afd5837b53c80 = Util.kwpMap("allowAmbiguity", ((IBool)$constants.get(2)/*false*/), "maxAmbDepth", ((IInteger)$constants.get(3)/*2*/), "allowRecovery", ((IBool)$constants.get(2)/*false*/), "maxRecoveryAttempts", ((IInteger)$constants.get(4)/*30*/), "maxRecoveryTokens", ((IInteger)$constants.get(5)/*3*/), "hasSideEffects", ((IBool)$constants.get(2)/*false*/), "filters", ((ISet)$constants.get(6)/*{}*/));
       $kwpDefaults_ParseTree_parse$696136d9f024501e = Util.kwpMap("allowAmbiguity", ((IBool)$constants.get(2)/*false*/), "maxAmbDepth", ((IInteger)$constants.get(3)/*2*/), "allowRecovery", ((IBool)$constants.get(2)/*false*/), "maxRecoveryAttempts", ((IInteger)$constants.get(4)/*30*/), "maxRecoveryTokens", ((IInteger)$constants.get(5)/*3*/), "hasSideEffects", ((IBool)$constants.get(2)/*false*/), "filters", ((ISet)$constants.get(6)/*{}*/));
       $kwpDefaults_ParseTree_parse$55b9e584aedc91f7 = Util.kwpMap("allowAmbiguity", ((IBool)$constants.get(2)/*false*/), "maxAmbDepth", ((IInteger)$constants.get(3)/*2*/), "allowRecovery", ((IBool)$constants.get(2)/*false*/), "maxRecoveryAttempts", ((IInteger)$constants.get(4)/*30*/), "maxRecoveryTokens", ((IInteger)$constants.get(5)/*3*/), "hasSideEffects", ((IBool)$constants.get(2)/*false*/), "filters", ((ISet)$constants.get(6)/*{}*/));
       $kwpDefaults_ParseTree_parser$c4d258086f531875 = Util.kwpMap("allowAmbiguity", ((IBool)$constants.get(2)/*false*/), "maxAmbDepth", ((IInteger)$constants.get(3)/*2*/), "allowRecovery", ((IBool)$constants.get(2)/*false*/), "maxRecoveryAttempts", ((IInteger)$constants.get(4)/*30*/), "maxRecoveryTokens", ((IInteger)$constants.get(5)/*3*/), "hasSideEffects", ((IBool)$constants.get(2)/*false*/), "filters", ((ISet)$constants.get(6)/*{}*/));
       $kwpDefaults_ParseTree_firstAmbiguityFinder$4c88e56e1de6a5c6 = Util.kwpMap("hasSideEffects", ((IBool)$constants.get(2)/*false*/), "filters", ((ISet)$constants.get(6)/*{}*/));
       $kwpDefaults_ParseTree_parsers$43e4839e939e0971 = Util.kwpMap("allowAmbiguity", ((IBool)$constants.get(2)/*false*/), "maxAmbDepth", ((IInteger)$constants.get(3)/*2*/), "allowRecovery", ((IBool)$constants.get(2)/*false*/), "maxRecoveryAttempts", ((IInteger)$constants.get(4)/*30*/), "maxRecoveryTokens", ((IInteger)$constants.get(5)/*3*/), "hasSideEffects", ((IBool)$constants.get(2)/*false*/), "filters", ((ISet)$constants.get(6)/*{}*/));
       $kwpDefaults_ParseTree_firstAmbiguityFinders$4e066d437479bb46 = Util.kwpMap("hasSideEffects", ((IBool)$constants.get(2)/*false*/), "filters", ((ISet)$constants.get(6)/*{}*/));
       $kwpDefaults_ParseTree_loadParsers$12936d804ae9b4a7 = Util.kwpMap("allowAmbiguity", ((IBool)$constants.get(2)/*false*/), "maxAmbDepth", ((IInteger)$constants.get(3)/*2*/), "allowRecovery", ((IBool)$constants.get(2)/*false*/), "maxRecoveryAttempts", ((IInteger)$constants.get(4)/*30*/), "maxRecoveryTokens", ((IInteger)$constants.get(5)/*3*/), "hasSideEffects", ((IBool)$constants.get(2)/*false*/), "filters", ((ISet)$constants.get(6)/*{}*/));
       $kwpDefaults_ParseTree_loadParser$a320f401f0ea0902 = Util.kwpMap("allowAmbiguity", ((IBool)$constants.get(2)/*false*/), "maxAmbDepth", ((IInteger)$constants.get(3)/*2*/), "allowRecovery", ((IBool)$constants.get(2)/*false*/), "maxRecoveryAttempts", ((IInteger)$constants.get(4)/*30*/), "maxRecoveryTokens", ((IInteger)$constants.get(5)/*3*/), "hasSideEffects", ((IBool)$constants.get(2)/*false*/), "filters", ((ISet)$constants.get(6)/*{}*/));
    
    }
    public IList addLabels(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_Type.addLabels($P0, $P1);
    }
    public IBool sameType(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)ParseTree_sameType$b275756ae4e2966e((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case -2144737184:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)ParseTree_sameType$10233296380130e3((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
         $result = (IBool)ParseTree_sameType$a84782db931f7a30((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)ParseTree_sameType$62cda661d939c9fa((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)ParseTree_sameType$3698dd87f53b84eb((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)ParseTree_sameType$e3b3acd78100d719((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isAliasType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isAliasType($P0);
    }
    public IString intercalate(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IString) M_List.intercalate($P0, $P1);
    }
    public IValue head(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.head($P0);
    }
    public IList head(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.head($P0, $P1);
    }
    public IInteger size(IValue $P0){ // Generated by Resolver
       return (IInteger) M_List.size($P0);
    }
    public IBool isStrType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isStrType($P0);
    }
    public IMap toMap(IValue $P0){ // Generated by Resolver
       return (IMap) M_List.toMap($P0);
    }
    public IConstructor choice(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_Type.choice($P0, $P1);
    }
    public IBool isADTType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isADTType($P0);
    }
    public IBool isValueType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isValueType($P0);
    }
    public IList mapper(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.mapper($P0, $P1);
    }
    public IBool isListType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isListType($P0);
    }
    public IBool isRealType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRealType($P0);
    }
    public IList reverse(IValue $P0){ // Generated by Resolver
       return (IList) M_List.reverse($P0);
    }
    public IBool isTypeVar(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isTypeVar($P0);
    }
    public TypedFunctionInstance2<IValue, IValue, IValue> firstAmbiguityFinder(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       TypedFunctionInstance2<IValue, IValue, IValue> $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (TypedFunctionInstance2<IValue, IValue, IValue>)ParseTree_firstAmbiguityFinder$4c88e56e1de6a5c6((IConstructor) $P0, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isEmpty(IValue $P0){ // Generated by Resolver
       return (IBool) M_List.isEmpty($P0);
    }
    public IList remove(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.remove($P0, $P1);
    }
    public IValue max(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.max($P0);
    }
    public IConstructor priority(IValue $P0, IValue $P1){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type,$T1)){
         $result = (IConstructor)ParseTree_priority$ee30aba6f6d82e37((IConstructor) $P0, (IList) $P1);
         if($result != null) return $result;
         return $RVF.constructor(Production_priority_Symbol_list_Production, new IValue[]{(IConstructor) $P0, (IList) $P1});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IValue getFirstFrom(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.getFirstFrom($P0);
    }
    public IMap distribution(IValue $P0){ // Generated by Resolver
       return (IMap) M_List.distribution($P0);
    }
    public TypedFunctionInstance2<IValue, IValue, IValue> loadParser(IValue $P0, IValue $P1, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       TypedFunctionInstance2<IValue, IValue, IValue> $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T2) && $isSubtypeOf($P1Type,$T5)){
         $result = (TypedFunctionInstance2<IValue, IValue, IValue>)ParseTree_loadParser$a320f401f0ea0902((IConstructor) $P0, (ISourceLocation) $P1, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IString printSymbol(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type,$T6)){
         $result = (IString)ParseTree_printSymbol$4ce577b2a14fc038((IConstructor) $P0, (IBool) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isNodeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNodeType($P0);
    }
    public IBool isSorted(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (IBool) M_List.isSorted($P0, $kwpActuals);
    }
    public IList take(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.take($P0, $P1);
    }
    public ISet toSet(IValue $P0){ // Generated by Resolver
       return (ISet) M_List.toSet($P0);
    }
    public IBool isReifiedType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isReifiedType($P0);
    }
    public ITuple takeOneFrom(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.takeOneFrom($P0);
    }
    public IInteger indexOf(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IInteger) M_List.indexOf($P0, $P1);
    }
    public IBool isRelType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRelType($P0);
    }
    public ITuple unzip2(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.unzip2($P0);
    }
    public IBool isConstructorType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isConstructorType($P0);
    }
    public IConstructor var_func(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IConstructor) M_Type.var_func($P0, $P1, $P2);
    }
    public IBool equivalent(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IBool) M_Type.equivalent($P0, $P1);
    }
    public IList takeWhile(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.takeWhile($P0, $P1);
    }
    public INumber sum(IValue $P0){ // Generated by Resolver
       return (INumber) M_List.sum($P0);
    }
    public IList addParamLabels(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_Type.addParamLabels($P0, $P1);
    }
    public IList delete(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.delete($P0, $P1);
    }
    public IBool keepParams(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IBool) M_Type.keepParams($P0, $P1);
    }
    public IBool isListRelType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isListRelType($P0);
    }
    public ITuple unzip3(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.unzip3($P0);
    }
    public IBool eq(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IBool) M_Type.eq($P0, $P1);
    }
    public IList insertAt(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IList) M_List.insertAt($P0, $P1, $P2);
    }
    public IBool isMapType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isMapType($P0);
    }
    public IString toString(IValue $P0){ // Generated by Resolver
       return (IString) M_List.toString($P0);
    }
    public IValue reducer(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IValue) M_List.reducer($P0, $P1, $P2);
    }
    public ISet permutations(IValue $P0){ // Generated by Resolver
       return (ISet) M_List.permutations($P0);
    }
    public TypedFunctionInstance3<IValue, IValue, IValue, IValue> loadParsers(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       TypedFunctionInstance3<IValue, IValue, IValue, IValue> $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T5)){
         $result = (TypedFunctionInstance3<IValue, IValue, IValue, IValue>)ParseTree_loadParsers$12936d804ae9b4a7((ISourceLocation) $P0, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isBoolType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isBoolType($P0);
    }
    public IValue make(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IValue) M_Type.make($P0, $P1, $P2);
    }
    public IValue make(IValue $P0, IValue $P1, IValue $P2, IValue $P3){ // Generated by Resolver
       return (IValue) M_Type.make($P0, $P1, $P2, $P3);
    }
    public IList drop(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.drop($P0, $P1);
    }
    public IValue elementAt(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_List.elementAt($P0, $P1);
    }
    public IBool isLocType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isLocType($P0);
    }
    public IValue implode(IValue $P0, IValue $P1){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T7) && $isSubtypeOf($P1Type, ADT_Tree)){
         $result = (IValue)ParseTree_implode$13172869ec254a12((IConstructor) $P0, (ITree) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public TypedFunctionInstance3<IValue, IValue, IValue, IValue> firstAmbiguityFinders(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       TypedFunctionInstance3<IValue, IValue, IValue, IValue> $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (TypedFunctionInstance3<IValue, IValue, IValue, IValue>)ParseTree_firstAmbiguityFinders$4e066d437479bb46((IConstructor) $P0, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList upTill(IValue $P0){ // Generated by Resolver
       return (IList) M_List.upTill($P0);
    }
    public IList tail(IValue $P0){ // Generated by Resolver
       return (IList) M_List.tail($P0);
    }
    public IList tail(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.tail($P0, $P1);
    }
    public ITuple headTail(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.headTail($P0);
    }
    public IList zip2(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.zip2($P0, $P1);
    }
    public ITuple pop(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.pop($P0);
    }
    public IConstructor typeOf(IValue $P0){ // Generated by Resolver
       return (IConstructor) M_Type.typeOf($P0);
    }
    public IConstructor treeAt(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T9) && $isSubtypeOf($P1Type,$T5) && $isSubtypeOf($P2Type, ADT_Tree)){
         $result = (IConstructor)ParseTree_treeAt$d353ab20d6109b21((IConstructor) $P0, (ISourceLocation) $P1, (ITree) $P2);
         if($result != null) return $result;
         $result = (IConstructor)ParseTree_treeAt$070d7a31ea8abb2e((IConstructor) $P0, (ISourceLocation) $P1, (ITree) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IMap toMapUnique(IValue $P0){ // Generated by Resolver
       return (IMap) M_List.toMapUnique($P0);
    }
    public IList index(IValue $P0){ // Generated by Resolver
       return (IList) M_List.index($P0);
    }
    public IList slice(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IList) M_List.slice($P0, $P1, $P2);
    }
    public IBool allLabeled(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.allLabeled($P0);
    }
    public IValue min(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.min($P0);
    }
    public IList prefix(IValue $P0){ // Generated by Resolver
       return (IList) M_List.prefix($P0);
    }
    public IList zip3(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IList) M_List.zip3($P0, $P1, $P2);
    }
    public IBool isSetType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isSetType($P0);
    }
    public void storeParsers(IValue $P0, IValue $P1){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5)){
         try { ParseTree_storeParsers$e7c8ce1688fd48e5((IConstructor) $P0, (ISourceLocation) $P1); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IInteger lastIndexOf(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IInteger) M_List.lastIndexOf($P0, $P1);
    }
    public ITree parse(IValue $P0, IValue $P1, IValue $P2, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       ITree $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T9) && $isSubtypeOf($P1Type,$T11) && $isSubtypeOf($P2Type,$T5)){
         $result = (ITree)ParseTree_parse$696136d9f024501e((IConstructor) $P0, (IString) $P1, (ISourceLocation) $P2, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public ITree parse(IValue $P0, IValue $P1, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       ITree $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T9) && $isSubtypeOf($P1Type,$T11)){
         $result = (ITree)ParseTree_parse$4d2afd5837b53c80((IConstructor) $P0, (IString) $P1, $kwpActuals);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T9) && $isSubtypeOf($P1Type,$T5)){
         $result = (ITree)ParseTree_parse$55b9e584aedc91f7((IConstructor) $P0, (ISourceLocation) $P1, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public TypedFunctionInstance3<IValue, IValue, IValue, IValue> parsers(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       TypedFunctionInstance3<IValue, IValue, IValue, IValue> $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T7)){
         $result = (TypedFunctionInstance3<IValue, IValue, IValue, IValue>)ParseTree_parsers$43e4839e939e0971((IConstructor) $P0, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList concat(IValue $P0){ // Generated by Resolver
       return (IList) M_List.concat($P0);
    }
    public IBool isRatType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRatType($P0);
    }
    public IValue top(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.top($P0);
    }
    public IString itoString(IValue $P0){ // Generated by Resolver
       return (IString) M_List.itoString($P0);
    }
    public IList getLabels(IValue $P0){ // Generated by Resolver
       return (IList) M_Type.getLabels($P0);
    }
    public IValue last(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.last($P0);
    }
    public IList getParamLabels(IValue $P0){ // Generated by Resolver
       return (IList) M_Type.getParamLabels($P0);
    }
    public IBool isNumType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNumType($P0);
    }
    public IList stripLabels(IValue $P0){ // Generated by Resolver
       return (IList) M_Type.stripLabels($P0);
    }
    public IBool isTupleType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isTupleType($P0);
    }
    public IBool isBagType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isBagType($P0);
    }
    public IList intersperse(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.intersperse($P0, $P1);
    }
    public IList merge(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IList) M_List.merge($P0, $P1, $P2);
    }
    public IList merge(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.merge($P0, $P1);
    }
    public IBool isVoidType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isVoidType($P0);
    }
    public IBool isNonTerminalType(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1444258592:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)ParseTree_isNonTerminalType$fd8640b90be01f96((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -109773488:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)ParseTree_isNonTerminalType$596a4e65f73f6ddd((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 878060304:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)ParseTree_isNonTerminalType$f27daba5b606f35d((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 856312:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)ParseTree_isNonTerminalType$bd8ebacb8470da66((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1154855088:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)ParseTree_isNonTerminalType$bec4d445d8286a07((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 28290288:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)ParseTree_isNonTerminalType$897add07fd0679fa((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -333228984:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)ParseTree_isNonTerminalType$60a31525ab4124d2((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, M_Type.ADT_Symbol)){
         $result = (IBool)ParseTree_isNonTerminalType$c6eab95f5fe5b746((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue typeCast(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_Type.typeCast($P0, $P1);
    }
    public IValue lub(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_Type.lub($P0, $P1);
    }
    public IList shuffle(IValue $P0){ // Generated by Resolver
       return (IList) M_List.shuffle($P0);
    }
    public IList shuffle(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.shuffle($P0, $P1);
    }
    public IBool comparable(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IBool) M_Type.comparable($P0, $P1);
    }
    public IBool subtype(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$162da85a0f5a9f0d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 26576112:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$258479665eae36af((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$0462d461bde80a82((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1725888:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$f6957636a33615ae((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$b674428cffef84bc((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 97904160:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$98167e340333c9a5((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$4fe5b133e2ee1de9((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 28290288:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)ParseTree_subtype$384d8d76f0c7a053((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 910096:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$ca59d9bf5276e15d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$e77633ea9a4ac6a5((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 902344:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$21c6b8b775030d1d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$98e19b11a09faf67((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$0862159b9fa78cf9((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 26641768:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$ab363c241c416a71((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$4de9a977591be6e5((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 778304:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$23f59dc1171dc69d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 100948096:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$ddf53e134f4d5416((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 112955840:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$bc5943e83a6df899((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$282ad33dd55efdcc((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1542928:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$5f5250bbf1aff423((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$15cedff9916fdbee((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 885800512:
       		if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$44422dfea95218a8((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
         $result = (IBool)M_Type.Type_subtype$cfecefb3bc3fa773((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)M_Type.Type_subtype$53c4de769757bddc((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)M_Type.Type_subtype$2750c116f0b05084((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)M_Type.Type_subtype$39fbab80e9db10e1((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)M_Type.Type_subtype$3eada106dbc66d2d((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)M_Type.Type_subtype$30215aaed6c33fd7((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)M_Type.Type_subtype$1b2387a35f10c1e0((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)M_Type.Type_subtype$80633493313ebd18((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)M_Type.Type_subtype$3aa09e73e41fcf84((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T12) && $isSubtypeOf($P1Type,$T12)){
         $result = (IBool)M_Type.Type_subtype$e6962df5576407da((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T7) && $isSubtypeOf($P1Type,$T2)){
         $result = (IBool)M_Type.Type_subtype$7b9c005ac35dd586((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, M_Type.ADT_Symbol)){
         $result = (IBool)M_Type.Type_subtype$06d2c71d010480ef((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T12) && $isSubtypeOf($P1Type,$T12)){
         $result = (IBool)M_Type.Type_subtype$812a7f34ff841fdb((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IConstructor associativity(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type, M_Type.ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Associativity) && $isSubtypeOf($P2Type,$T13)){
         $result = (IConstructor)ParseTree_associativity$95843a2f3959b22f((IConstructor) $P0, (IConstructor) $P1, (ISet) $P2);
         if($result != null) return $result;
         $result = (IConstructor)ParseTree_associativity$05ee42b13b7e96fb((IConstructor) $P0, (IConstructor) $P1, (ISet) $P2);
         if($result != null) return $result;
         $result = (IConstructor)ParseTree_associativity$9299e943b00366a7((IConstructor) $P0, (IConstructor) $P1, (ISet) $P2);
         if($result != null) return $result;
         return $RVF.constructor(Production_associativity_Symbol_Associativity_set_Production, new IValue[]{(IConstructor) $P0, (IConstructor) $P1, (ISet) $P2});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IValue getOneFrom(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.getOneFrom($P0);
    }
    public IString unparse(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, ADT_Tree)){
         $result = (IString)ParseTree_unparse$8685ce60c1159ea5((ITree) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IMap removeFromBag(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IMap) M_List.removeFromBag($P0, $P1, $P2);
    }
    public IMap removeFromBag(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IMap) M_List.removeFromBag($P0, $P1);
    }
    public ITuple split(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.split($P0);
    }
    public IList push(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.push($P0, $P1);
    }
    public IBool noneLabeled(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.noneLabeled($P0);
    }
    public ISet permutationsBag(IValue $P0){ // Generated by Resolver
       return (ISet) M_List.permutationsBag($P0);
    }
    public IList mix(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.mix($P0, $P1);
    }
    public IInteger mainMessageHandler(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (IInteger) M_Message.mainMessageHandler($P0, $kwpActuals);
    }
    public IBool isFunctionType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isFunctionType($P0);
    }
    public IValue glb(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_Type.glb($P0, $P1);
    }
    public ITree firstAmbiguity(IValue $P0, IValue $P1){ // Generated by Resolver
       ITree $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5)){
         $result = (ITree)ParseTree_firstAmbiguity$a49129037d5d17ba((IConstructor) $P0, (ISourceLocation) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T11)){
         $result = (ITree)ParseTree_firstAmbiguity$70a6c6fb84d56e8f((IConstructor) $P0, (IString) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ISet toRel(IValue $P0){ // Generated by Resolver
       return (ISet) M_List.toRel($P0);
    }
    public IValue sort(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T14)){
         $result = (IValue)M_List.List_sort$1fe4426c8c8039da((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T11)){
         return $RVF.constructor(Symbol_sort_str, new IValue[]{(IString) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList sort(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.sort($P0, $P1);
    }
    public TypedFunctionInstance2<IValue, IValue, IValue> parser(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       TypedFunctionInstance2<IValue, IValue, IValue> $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T7)){
         $result = (TypedFunctionInstance2<IValue, IValue, IValue>)ParseTree_parser$c4d258086f531875((IConstructor) $P0, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList dup(IValue $P0){ // Generated by Resolver
       return (IList) M_List.dup($P0);
    }
    public IBool isIntType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isIntType($P0);
    }
    public IBool isDateTimeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isDateTimeType($P0);
    }
    public IString write(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (IString) M_Message.write($P0, $kwpActuals);
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(10584,63,<292,0>,<292,63>) 
    public IBool ParseTree_subtype$384d8d76f0c7a053(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, Symbol_sort_str, 1)){
           IValue $arg0_2 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_2.getType(), $T3)){
              if($has_type_and_arity($1, M_Type.Symbol_adt_str_list_Symbol, 2)){
                 IValue $arg0_1 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                 if($isComparable($arg0_1.getType(), $T11)){
                    if(((IString)$constants.get(0)/*"Tree"*/).equals($arg0_1)){
                       IValue $arg1_0 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
                       if($isComparable($arg1_0.getType(), $T3)){
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
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(11322,165,<315,0>,<317,22>) 
    public IConstructor ParseTree_priority$ee30aba6f6d82e37(IConstructor s_0, IList $1){ 
        
        
        /*muExists*/priority: 
            do {
                final IList $subject3 = ((IList)$1);
                int $subject3_cursor = 0;
                if($isSubtypeOf($subject3.getType(),$T1)){
                   final int $subject3_len = (int)((IList)($subject3)).length();
                   if($subject3_len >= 1){
                      final int $a_18_start = (int)$subject3_cursor;
                      priority_LIST_MVARa:
                      
                      for(int $a_18_len = 0; $a_18_len <= $subject3_len - $a_18_start - 1; $a_18_len += 1){
                         IList a_1 = ((IList)($subject3.sublist($a_18_start, $a_18_len)));
                         $subject3_cursor = $a_18_start + $a_18_len;
                         final IConstructor $subject5 = ((IConstructor)($alist_subscript_int(((IList)($subject3)),$subject3_cursor)));
                         if($has_type_and_arity($subject5, Production_priority_Symbol_list_Production, 2)){
                            IValue $arg0_7 = (IValue)($aadt_subscript_int(((IConstructor)($subject5)),0));
                            if($isComparable($arg0_7.getType(), M_Type.ADT_Symbol)){
                               IValue $arg1_6 = (IValue)($aadt_subscript_int(((IConstructor)($subject5)),1));
                               if($isComparable($arg1_6.getType(), $T1)){
                                  if(true){
                                     IList b_2 = null;
                                     $subject3_cursor += 1;
                                     final int $c_34_start = (int)$subject3_cursor;
                                     priority_LIST_MVARa_CONS_priority_MVARc:
                                     
                                     for(int $c_34_len = 0; $c_34_len <= $subject3_len - $c_34_start - 0; $c_34_len += 1){
                                        IList c_3 = ((IList)($subject3.sublist($c_34_start, $c_34_len)));
                                        $subject3_cursor = $c_34_start + $c_34_len;
                                        if($subject3_cursor == $subject3_len){
                                           return ((IConstructor)($me.priority(((IConstructor)s_0), ((IList)($alist_add_alist(((IList)($alist_add_alist(((IList)a_1),((IList)($arg1_6))))),((IList)c_3)))))));
                                        
                                        } else {
                                           continue priority_LIST_MVARa_CONS_priority_MVARc;/*list match1*/
                                        }
                                     }
                                     continue priority_LIST_MVARa;/*computeFail*/
                                  
                                  } else {
                                     continue priority_LIST_MVARa;/*computeFail*/
                                  }
                               } else {
                                  continue priority_LIST_MVARa;/*computeFail*/
                               }
                            } else {
                               continue priority_LIST_MVARa;/*computeFail*/
                            }
                         } else {
                            continue priority_LIST_MVARa;/*computeFail*/
                         }
                      }
                      return null;
                   
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(11493,384,<320,0>,<327,30>) 
    public IConstructor ParseTree_associativity$95843a2f3959b22f(IConstructor s_0, IConstructor as_1, ISet $2){ 
        
        
        /*muExists*/associativity: 
            do {
                ISet $subject9 = (ISet)($2);
                associativity_SET_MVARa:
                for(IValue $elem17_for : new SubSetGenerator(((ISet)($subject9)))){
                    ISet $elem17 = (ISet) $elem17_for;
                    ISet a_2 = ((ISet)($elem17));
                    final ISet $subject11 = ((ISet)(((ISet)($subject9)).subtract(((ISet)($elem17)))));
                    associativity_SET_MVARa_CONS_choice$_DFLT_SET_ELM14:
                    for(IValue $elem13_for : ((ISet)($subject11))){
                        IConstructor $elem13 = (IConstructor) $elem13_for;
                        if($has_type_and_arity($elem13, M_Type.Production_choice_Symbol_set_Production, 2)){
                           IValue $arg0_16 = (IValue)($aadt_subscript_int(((IConstructor)($elem13)),0));
                           if($isComparable($arg0_16.getType(), M_Type.ADT_Symbol)){
                              if(true){
                                 IConstructor t_3 = null;
                                 IValue $arg1_15 = (IValue)($aadt_subscript_int(((IConstructor)($elem13)),1));
                                 if($isComparable($arg1_15.getType(), $T13)){
                                    if(true){
                                       ISet b_4 = null;
                                       final ISet $subject12 = ((ISet)(((ISet)($subject11)).delete(((IConstructor)($elem13)))));
                                       if(((ISet)($subject12)).size() == 0){
                                          return ((IConstructor)($me.associativity(((IConstructor)s_0), ((IConstructor)as_1), ((ISet)($aset_add_aset(((ISet)a_2),((ISet)($arg1_15))))))));
                                       
                                       } else {
                                          continue associativity_SET_MVARa_CONS_choice$_DFLT_SET_ELM14;/*redirected associativity_SET_MVARa_CONS_choice to associativity_SET_MVARa_CONS_choice$_DFLT_SET_ELM14; set pat3*/
                                       }
                                    } else {
                                       continue associativity_SET_MVARa_CONS_choice$_DFLT_SET_ELM14;/*default set elem*/
                                    }
                                 } else {
                                    continue associativity_SET_MVARa_CONS_choice$_DFLT_SET_ELM14;/*default set elem*/
                                 }
                              } else {
                                 continue associativity_SET_MVARa_CONS_choice$_DFLT_SET_ELM14;/*default set elem*/
                              }
                           } else {
                              continue associativity_SET_MVARa_CONS_choice$_DFLT_SET_ELM14;/*default set elem*/
                           }
                        } else {
                           continue associativity_SET_MVARa_CONS_choice$_DFLT_SET_ELM14;/*default set elem*/
                        }
                    }
                    continue associativity_SET_MVARa;/*set pat4*/
                                
                }
                return null;
                            
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(11892,174,<329,0>,<330,39>) 
    public IConstructor ParseTree_associativity$05ee42b13b7e96fb(IConstructor rhs_0, IConstructor a_1, ISet $2){ 
        
        
        /*muExists*/associativity: 
            do {
                ISet $subject18 = (ISet)($2);
                associativity_SET_CONS_associativity$_DFLT_SET_ELM24:
                for(IValue $elem23_for : ((ISet)($subject18))){
                    IConstructor $elem23 = (IConstructor) $elem23_for;
                    if($has_type_and_arity($elem23, Production_associativity_Symbol_Associativity_set_Production, 3)){
                       IValue $arg0_27 = (IValue)($aadt_subscript_int(((IConstructor)($elem23)),0));
                       if($isComparable($arg0_27.getType(), M_Type.ADT_Symbol)){
                          if((rhs_0 != null)){
                             if(rhs_0.match($arg0_27)){
                                IValue $arg1_26 = (IValue)($aadt_subscript_int(((IConstructor)($elem23)),1));
                                if($isComparable($arg1_26.getType(), ADT_Associativity)){
                                   if(true){
                                      IConstructor b_2 = null;
                                      IValue $arg2_25 = (IValue)($aadt_subscript_int(((IConstructor)($elem23)),2));
                                      if($isComparable($arg2_25.getType(), $T13)){
                                         if(true){
                                            ISet alts_3 = null;
                                            final ISet $subject20 = ((ISet)(((ISet)($subject18)).delete(((IConstructor)($elem23)))));
                                            associativity_SET_CONS_associativity_MVARrest:
                                            for(IValue $elem22_for : new SubSetGenerator(((ISet)($subject20)))){
                                                ISet $elem22 = (ISet) $elem22_for;
                                                ISet rest_4 = ((ISet)($elem22));
                                                final ISet $subject21 = ((ISet)(((ISet)($subject20)).subtract(((ISet)($elem22)))));
                                                if(((ISet)($subject21)).size() == 0){
                                                   return ((IConstructor)($me.associativity(((IConstructor)($arg0_27)), ((IConstructor)a_1), ((ISet)($aset_add_aset(((ISet)rest_4),((ISet)($arg2_25))))))));
                                                
                                                } else {
                                                   continue associativity_SET_CONS_associativity_MVARrest;/*set pat3*/
                                                }
                                            }
                                            continue associativity_SET_CONS_associativity$_DFLT_SET_ELM24;/*redirected associativity_SET_CONS_associativity to associativity_SET_CONS_associativity$_DFLT_SET_ELM24; set pat4*/
                                                        
                                         } else {
                                            continue associativity_SET_CONS_associativity$_DFLT_SET_ELM24;/*default set elem*/
                                         }
                                      } else {
                                         continue associativity_SET_CONS_associativity$_DFLT_SET_ELM24;/*default set elem*/
                                      }
                                   } else {
                                      continue associativity_SET_CONS_associativity$_DFLT_SET_ELM24;/*default set elem*/
                                   }
                                } else {
                                   continue associativity_SET_CONS_associativity$_DFLT_SET_ELM24;/*default set elem*/
                                }
                             } else {
                                continue associativity_SET_CONS_associativity$_DFLT_SET_ELM24;/*default set elem*/
                             }
                          } else {
                             rhs_0 = ((IConstructor)($arg0_27));
                             IValue $arg1_26 = (IValue)($aadt_subscript_int(((IConstructor)($elem23)),1));
                             if($isComparable($arg1_26.getType(), ADT_Associativity)){
                                if(true){
                                   IConstructor b_2 = null;
                                   IValue $arg2_25 = (IValue)($aadt_subscript_int(((IConstructor)($elem23)),2));
                                   if($isComparable($arg2_25.getType(), $T13)){
                                      if(true){
                                         ISet alts_3 = null;
                                         final ISet $subject20 = ((ISet)(((ISet)($subject18)).delete(((IConstructor)($elem23)))));
                                         associativity_SET_CONS_associativity_MVARrest:
                                         for(IValue $elem22_for : new SubSetGenerator(((ISet)($subject20)))){
                                             ISet $elem22 = (ISet) $elem22_for;
                                             ISet rest_4 = ((ISet)($elem22));
                                             final ISet $subject21 = ((ISet)(((ISet)($subject20)).subtract(((ISet)($elem22)))));
                                             if(((ISet)($subject21)).size() == 0){
                                                return ((IConstructor)($me.associativity(((IConstructor)($arg0_27)), ((IConstructor)a_1), ((ISet)($aset_add_aset(((ISet)rest_4),((ISet)($arg2_25))))))));
                                             
                                             } else {
                                                continue associativity_SET_CONS_associativity_MVARrest;/*set pat3*/
                                             }
                                         }
                                         continue associativity_SET_CONS_associativity$_DFLT_SET_ELM24;/*redirected associativity_SET_CONS_associativity to associativity_SET_CONS_associativity$_DFLT_SET_ELM24; set pat4*/
                                                     
                                      } else {
                                         continue associativity_SET_CONS_associativity$_DFLT_SET_ELM24;/*default set elem*/
                                      }
                                   } else {
                                      continue associativity_SET_CONS_associativity$_DFLT_SET_ELM24;/*default set elem*/
                                   }
                                } else {
                                   continue associativity_SET_CONS_associativity$_DFLT_SET_ELM24;/*default set elem*/
                                }
                             } else {
                                continue associativity_SET_CONS_associativity$_DFLT_SET_ELM24;/*default set elem*/
                             }
                          }
                       } else {
                          continue associativity_SET_CONS_associativity$_DFLT_SET_ELM24;/*default set elem*/
                       }
                    } else {
                       continue associativity_SET_CONS_associativity$_DFLT_SET_ELM24;/*default set elem*/
                    }
                }
                return null;
                            
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(12128,146,<332,0>,<333,35>) 
    public IConstructor ParseTree_associativity$9299e943b00366a7(IConstructor s_0, IConstructor as_1, ISet $2){ 
        
        
        /*muExists*/associativity: 
            do {
                ISet $subject29 = (ISet)($2);
                associativity_SET_MVARa:
                for(IValue $elem37_for : new SubSetGenerator(((ISet)($subject29)))){
                    ISet $elem37 = (ISet) $elem37_for;
                    ISet a_2 = ((ISet)($elem37));
                    final ISet $subject31 = ((ISet)(((ISet)($subject29)).subtract(((ISet)($elem37)))));
                    associativity_SET_MVARa_CONS_priority$_DFLT_SET_ELM34:
                    for(IValue $elem33_for : ((ISet)($subject31))){
                        IConstructor $elem33 = (IConstructor) $elem33_for;
                        if($has_type_and_arity($elem33, Production_priority_Symbol_list_Production, 2)){
                           IValue $arg0_36 = (IValue)($aadt_subscript_int(((IConstructor)($elem33)),0));
                           if($isComparable($arg0_36.getType(), M_Type.ADT_Symbol)){
                              if(true){
                                 IConstructor t_3 = null;
                                 IValue $arg1_35 = (IValue)($aadt_subscript_int(((IConstructor)($elem33)),1));
                                 if($isComparable($arg1_35.getType(), $T1)){
                                    if(true){
                                       IList b_4 = null;
                                       final ISet $subject32 = ((ISet)(((ISet)($subject31)).delete(((IConstructor)($elem33)))));
                                       if(((ISet)($subject32)).size() == 0){
                                          final ISetWriter $writer28 = (ISetWriter)$RVF.setWriter();
                                          ;
                                          $setwriter_splice($writer28,a_2);
                                          $setwriter_splice($writer28,$arg1_35);
                                          return ((IConstructor)($me.associativity(((IConstructor)s_0), ((IConstructor)as_1), ((ISet)($writer28.done())))));
                                       
                                       } else {
                                          continue associativity_SET_MVARa_CONS_priority$_DFLT_SET_ELM34;/*redirected associativity_SET_MVARa_CONS_priority to associativity_SET_MVARa_CONS_priority$_DFLT_SET_ELM34; set pat3*/
                                       }
                                    } else {
                                       continue associativity_SET_MVARa_CONS_priority$_DFLT_SET_ELM34;/*default set elem*/
                                    }
                                 } else {
                                    continue associativity_SET_MVARa_CONS_priority$_DFLT_SET_ELM34;/*default set elem*/
                                 }
                              } else {
                                 continue associativity_SET_MVARa_CONS_priority$_DFLT_SET_ELM34;/*default set elem*/
                              }
                           } else {
                              continue associativity_SET_MVARa_CONS_priority$_DFLT_SET_ELM34;/*default set elem*/
                           }
                        } else {
                           continue associativity_SET_MVARa_CONS_priority$_DFLT_SET_ELM34;/*default set elem*/
                        }
                    }
                    continue associativity_SET_MVARa;/*set pat4*/
                                
                }
                return null;
                            
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(12370,5387,<340,0>,<415,251>) 
    public ITree ParseTree_parse$4d2afd5837b53c80(IConstructor begin_0, IString input_1, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_ParseTree_parse$4d2afd5837b53c80;
    
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T9.match(begin_0.getType(), $typeBindings)){
           final ITree $result38 = ((ITree)(((TypedFunctionInstance2<IValue, IValue, IValue>)$me.parser(((IConstructor)begin_0), Util.kwpMapExtend(Util.kwpMapRemoveRedeclared($kwpActuals, "allowAmbiguity", "maxAmbDepth", "allowRecovery", "maxRecoveryAttempts", "maxRecoveryTokens", "hasSideEffects", "filters"), "allowAmbiguity", ((IBool) ($kwpActuals.containsKey("allowAmbiguity") ? $kwpActuals.get("allowAmbiguity") : $kwpDefaults.get("allowAmbiguity"))), "maxAmbDepth", ((IInteger) ($kwpActuals.containsKey("maxAmbDepth") ? $kwpActuals.get("maxAmbDepth") : $kwpDefaults.get("maxAmbDepth"))), "allowRecovery", ((IBool) ($kwpActuals.containsKey("allowRecovery") ? $kwpActuals.get("allowRecovery") : $kwpDefaults.get("allowRecovery"))), "maxRecoveryAttempts", ((IInteger) ($kwpActuals.containsKey("maxRecoveryAttempts") ? $kwpActuals.get("maxRecoveryAttempts") : $kwpDefaults.get("maxRecoveryAttempts"))), "maxRecoveryTokens", ((IInteger) ($kwpActuals.containsKey("maxRecoveryTokens") ? $kwpActuals.get("maxRecoveryTokens") : $kwpDefaults.get("maxRecoveryTokens"))), "hasSideEffects", ((IBool) ($kwpActuals.containsKey("hasSideEffects") ? $kwpActuals.get("hasSideEffects") : $kwpDefaults.get("hasSideEffects"))), "filters", ((ISet) ($kwpActuals.containsKey("filters") ? $kwpActuals.get("filters") : $kwpDefaults.get("filters")))))).typedCall(((IValue)input_1), ((ISourceLocation)($create_aloc(((IString)$constants.get(7)/*"unknown:///"*/)))))));
           if($T15.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result38.getType(),$T15)){
              return ((ITree)($result38));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(17759,484,<417,0>,<418,244>) 
    public ITree ParseTree_parse$696136d9f024501e(IConstructor begin_0, IString input_1, ISourceLocation origin_2, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_ParseTree_parse$696136d9f024501e;
    
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T9.match(begin_0.getType(), $typeBindings)){
           final ITree $result39 = ((ITree)(((TypedFunctionInstance2<IValue, IValue, IValue>)$me.parser(((IConstructor)begin_0), Util.kwpMapExtend(Util.kwpMapRemoveRedeclared($kwpActuals, "allowAmbiguity", "maxAmbDepth", "allowRecovery", "maxRecoveryAttempts", "maxRecoveryTokens", "hasSideEffects", "filters"), "allowAmbiguity", ((IBool) ($kwpActuals.containsKey("allowAmbiguity") ? $kwpActuals.get("allowAmbiguity") : $kwpDefaults.get("allowAmbiguity"))), "maxAmbDepth", ((IInteger) ($kwpActuals.containsKey("maxAmbDepth") ? $kwpActuals.get("maxAmbDepth") : $kwpDefaults.get("maxAmbDepth"))), "allowRecovery", ((IBool) ($kwpActuals.containsKey("allowRecovery") ? $kwpActuals.get("allowRecovery") : $kwpDefaults.get("allowRecovery"))), "maxRecoveryAttempts", ((IInteger) ($kwpActuals.containsKey("maxRecoveryAttempts") ? $kwpActuals.get("maxRecoveryAttempts") : $kwpDefaults.get("maxRecoveryAttempts"))), "maxRecoveryTokens", ((IInteger) ($kwpActuals.containsKey("maxRecoveryTokens") ? $kwpActuals.get("maxRecoveryTokens") : $kwpDefaults.get("maxRecoveryTokens"))), "hasSideEffects", ((IBool) ($kwpActuals.containsKey("hasSideEffects") ? $kwpActuals.get("hasSideEffects") : $kwpDefaults.get("hasSideEffects"))), "filters", ((ISet) ($kwpActuals.containsKey("filters") ? $kwpActuals.get("filters") : $kwpDefaults.get("filters")))))).typedCall(((IValue)input_1), ((ISourceLocation)origin_2))));
           if($T15.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result39.getType(),$T15)){
              return ((ITree)($result39));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(18247,471,<420,0>,<421,243>) 
    public ITree ParseTree_parse$55b9e584aedc91f7(IConstructor begin_0, ISourceLocation input_1, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_ParseTree_parse$55b9e584aedc91f7;
    
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T9.match(begin_0.getType(), $typeBindings)){
           final ITree $result40 = ((ITree)(((TypedFunctionInstance2<IValue, IValue, IValue>)$me.parser(((IConstructor)begin_0), Util.kwpMapExtend(Util.kwpMapRemoveRedeclared($kwpActuals, "allowAmbiguity", "maxAmbDepth", "allowRecovery", "maxRecoveryAttempts", "maxRecoveryTokens", "hasSideEffects", "filters"), "allowAmbiguity", ((IBool) ($kwpActuals.containsKey("allowAmbiguity") ? $kwpActuals.get("allowAmbiguity") : $kwpDefaults.get("allowAmbiguity"))), "maxAmbDepth", ((IInteger) ($kwpActuals.containsKey("maxAmbDepth") ? $kwpActuals.get("maxAmbDepth") : $kwpDefaults.get("maxAmbDepth"))), "allowRecovery", ((IBool) ($kwpActuals.containsKey("allowRecovery") ? $kwpActuals.get("allowRecovery") : $kwpDefaults.get("allowRecovery"))), "maxRecoveryAttempts", ((IInteger) ($kwpActuals.containsKey("maxRecoveryAttempts") ? $kwpActuals.get("maxRecoveryAttempts") : $kwpDefaults.get("maxRecoveryAttempts"))), "maxRecoveryTokens", ((IInteger) ($kwpActuals.containsKey("maxRecoveryTokens") ? $kwpActuals.get("maxRecoveryTokens") : $kwpDefaults.get("maxRecoveryTokens"))), "hasSideEffects", ((IBool) ($kwpActuals.containsKey("hasSideEffects") ? $kwpActuals.get("hasSideEffects") : $kwpDefaults.get("hasSideEffects"))), "filters", ((ISet) ($kwpActuals.containsKey("filters") ? $kwpActuals.get("filters") : $kwpDefaults.get("filters")))))).typedCall(((IValue)input_1), ((ISourceLocation)input_1))));
           if($T15.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result40.getType(),$T15)){
              return ((ITree)($result40));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(18721,3103,<424,0>,<455,239>) 
    public TypedFunctionInstance2<IValue, IValue, IValue> ParseTree_parser$c4d258086f531875(IConstructor grammar_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_ParseTree_parser$c4d258086f531875;
    
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T7.match(grammar_0.getType(), $typeBindings)){
           final TypedFunctionInstance2<IValue, IValue, IValue> $result41 = ((TypedFunctionInstance2<IValue, IValue, IValue>)((TypedFunctionInstance2<IValue, IValue, IValue>)$Prelude.parser(grammar_0, (IBool)($kwpActuals.containsKey("allowAmbiguity") ? $kwpActuals.get("allowAmbiguity") : $kwpDefaults.get("allowAmbiguity")), (IInteger)($kwpActuals.containsKey("maxAmbDepth") ? $kwpActuals.get("maxAmbDepth") : $kwpDefaults.get("maxAmbDepth")), (IBool)($kwpActuals.containsKey("allowRecovery") ? $kwpActuals.get("allowRecovery") : $kwpDefaults.get("allowRecovery")), (IInteger)($kwpActuals.containsKey("maxRecoveryAttempts") ? $kwpActuals.get("maxRecoveryAttempts") : $kwpDefaults.get("maxRecoveryAttempts")), (IInteger)($kwpActuals.containsKey("maxRecoveryTokens") ? $kwpActuals.get("maxRecoveryTokens") : $kwpDefaults.get("maxRecoveryTokens")), (IBool)($kwpActuals.containsKey("hasSideEffects") ? $kwpActuals.get("hasSideEffects") : $kwpDefaults.get("hasSideEffects")), (ISet)($kwpActuals.containsKey("filters") ? $kwpActuals.get("filters") : $kwpDefaults.get("filters")))));
           if($T38.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result41.getType(),$T38)){
              return ((TypedFunctionInstance2<IValue, IValue, IValue>)($result41));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(21827,860,<457,0>,<468,132>) 
    public TypedFunctionInstance2<IValue, IValue, IValue> ParseTree_firstAmbiguityFinder$4c88e56e1de6a5c6(IConstructor grammar_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_ParseTree_firstAmbiguityFinder$4c88e56e1de6a5c6;
    
        return ((TypedFunctionInstance2<IValue, IValue, IValue>)((TypedFunctionInstance2<IValue, IValue, IValue>)$Prelude.firstAmbiguityFinder(grammar_0, (IBool)($kwpActuals.containsKey("hasSideEffects") ? $kwpActuals.get("hasSideEffects") : $kwpDefaults.get("hasSideEffects")), (ISet)($kwpActuals.containsKey("filters") ? $kwpActuals.get("filters") : $kwpDefaults.get("filters")))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(22690,699,<470,0>,<476,263>) 
    public TypedFunctionInstance3<IValue, IValue, IValue, IValue> ParseTree_parsers$43e4839e939e0971(IConstructor grammar_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_ParseTree_parsers$43e4839e939e0971;
    
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T7.match(grammar_0.getType(), $typeBindings)){
           final TypedFunctionInstance3<IValue, IValue, IValue, IValue> $result42 = ((TypedFunctionInstance3<IValue, IValue, IValue, IValue>)((TypedFunctionInstance3<IValue, IValue, IValue, IValue>)$Prelude.parsers(grammar_0, (IBool)($kwpActuals.containsKey("allowAmbiguity") ? $kwpActuals.get("allowAmbiguity") : $kwpDefaults.get("allowAmbiguity")), (IInteger)($kwpActuals.containsKey("maxAmbDepth") ? $kwpActuals.get("maxAmbDepth") : $kwpDefaults.get("maxAmbDepth")), (IBool)($kwpActuals.containsKey("allowRecovery") ? $kwpActuals.get("allowRecovery") : $kwpDefaults.get("allowRecovery")), (IInteger)($kwpActuals.containsKey("maxRecoveryAttempts") ? $kwpActuals.get("maxRecoveryAttempts") : $kwpDefaults.get("maxRecoveryAttempts")), (IInteger)($kwpActuals.containsKey("maxRecoveryTokens") ? $kwpActuals.get("maxRecoveryTokens") : $kwpDefaults.get("maxRecoveryTokens")), (IBool)($kwpActuals.containsKey("hasSideEffects") ? $kwpActuals.get("hasSideEffects") : $kwpDefaults.get("hasSideEffects")), (ISet)($kwpActuals.containsKey("filters") ? $kwpActuals.get("filters") : $kwpDefaults.get("filters")))));
           if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result42.getType(),$T42)){
              return ((TypedFunctionInstance3<IValue, IValue, IValue, IValue>)($result42));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(23392,886,<478,0>,<489,158>) 
    public TypedFunctionInstance3<IValue, IValue, IValue, IValue> ParseTree_firstAmbiguityFinders$4e066d437479bb46(IConstructor grammar_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_ParseTree_firstAmbiguityFinders$4e066d437479bb46;
    
        return ((TypedFunctionInstance3<IValue, IValue, IValue, IValue>)((TypedFunctionInstance3<IValue, IValue, IValue, IValue>)$Prelude.firstAmbiguityFinders(grammar_0, (IBool)($kwpActuals.containsKey("hasSideEffects") ? $kwpActuals.get("hasSideEffects") : $kwpDefaults.get("hasSideEffects")), (ISet)($kwpActuals.containsKey("filters") ? $kwpActuals.get("filters") : $kwpDefaults.get("filters")))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(24281,617,<491,0>,<500,54>) 
    public ITree ParseTree_firstAmbiguity$70a6c6fb84d56e8f(IConstructor begin_0, IString input_1){ 
        
        
        return ((ITree)(((TypedFunctionInstance2<IValue, IValue, IValue>)$me.firstAmbiguityFinder(((IConstructor)begin_0), Util.kwpMap())).typedCall(((IValue)input_1), ((ISourceLocation)($create_aloc(((IString)$constants.get(7)/*"unknown:///"*/)))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(24900,95,<502,0>,<503,46>) 
    public ITree ParseTree_firstAmbiguity$a49129037d5d17ba(IConstructor begin_0, ISourceLocation input_1){ 
        
        
        return ((ITree)(((TypedFunctionInstance2<IValue, IValue, IValue>)$me.firstAmbiguityFinder(((IConstructor)begin_0), Util.kwpMap())).typedCall(((IValue)input_1), ((ISourceLocation)input_1))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(24997,1076,<505,0>,<526,61>) 
    public void ParseTree_storeParsers$e7c8ce1688fd48e5(IConstructor grammar_0, ISourceLocation saveLocation_1){ 
        
        
        $Prelude.storeParsers(grammar_0, saveLocation_1); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(26075,1593,<528,0>,<567,266>) 
    public TypedFunctionInstance3<IValue, IValue, IValue, IValue> ParseTree_loadParsers$12936d804ae9b4a7(ISourceLocation savedParsers_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_ParseTree_loadParsers$12936d804ae9b4a7;
    
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        final TypedFunctionInstance3<IValue, IValue, IValue, IValue> $result43 = ((TypedFunctionInstance3<IValue, IValue, IValue, IValue>)((TypedFunctionInstance3<IValue, IValue, IValue, IValue>)$Prelude.loadParsers(savedParsers_0, (IBool)($kwpActuals.containsKey("allowAmbiguity") ? $kwpActuals.get("allowAmbiguity") : $kwpDefaults.get("allowAmbiguity")), (IInteger)($kwpActuals.containsKey("maxAmbDepth") ? $kwpActuals.get("maxAmbDepth") : $kwpDefaults.get("maxAmbDepth")), (IBool)($kwpActuals.containsKey("allowRecovery") ? $kwpActuals.get("allowRecovery") : $kwpDefaults.get("allowRecovery")), (IInteger)($kwpActuals.containsKey("maxRecoveryAttempts") ? $kwpActuals.get("maxRecoveryAttempts") : $kwpDefaults.get("maxRecoveryAttempts")), (IInteger)($kwpActuals.containsKey("maxRecoveryTokens") ? $kwpActuals.get("maxRecoveryTokens") : $kwpDefaults.get("maxRecoveryTokens")), (IBool)($kwpActuals.containsKey("hasSideEffects") ? $kwpActuals.get("hasSideEffects") : $kwpDefaults.get("hasSideEffects")), (ISet)($kwpActuals.containsKey("filters") ? $kwpActuals.get("filters") : $kwpDefaults.get("filters")))));
        if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result43.getType(),$T42)){
           return ((TypedFunctionInstance3<IValue, IValue, IValue, IValue>)($result43));
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(27670,555,<569,0>,<575,265>) 
    public TypedFunctionInstance2<IValue, IValue, IValue> ParseTree_loadParser$a320f401f0ea0902(IConstructor nonterminal_0, ISourceLocation savedParsers_1, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_ParseTree_loadParser$a320f401f0ea0902;
    
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T2.match(nonterminal_0.getType(), $typeBindings)){
           final TypedFunctionInstance2<IValue, IValue, IValue> $result44 = ((TypedFunctionInstance2<IValue, IValue, IValue>)((TypedFunctionInstance2<IValue, IValue, IValue>)$Prelude.loadParser(nonterminal_0, savedParsers_1, (IBool)($kwpActuals.containsKey("allowAmbiguity") ? $kwpActuals.get("allowAmbiguity") : $kwpDefaults.get("allowAmbiguity")), (IInteger)($kwpActuals.containsKey("maxAmbDepth") ? $kwpActuals.get("maxAmbDepth") : $kwpDefaults.get("maxAmbDepth")), (IBool)($kwpActuals.containsKey("allowRecovery") ? $kwpActuals.get("allowRecovery") : $kwpDefaults.get("allowRecovery")), (IInteger)($kwpActuals.containsKey("maxRecoveryAttempts") ? $kwpActuals.get("maxRecoveryAttempts") : $kwpDefaults.get("maxRecoveryAttempts")), (IInteger)($kwpActuals.containsKey("maxRecoveryTokens") ? $kwpActuals.get("maxRecoveryTokens") : $kwpDefaults.get("maxRecoveryTokens")), (IBool)($kwpActuals.containsKey("hasSideEffects") ? $kwpActuals.get("hasSideEffects") : $kwpDefaults.get("hasSideEffects")), (ISet)($kwpActuals.containsKey("filters") ? $kwpActuals.get("filters") : $kwpDefaults.get("filters")))));
           if($T45.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result44.getType(),$T45)){
              return ((TypedFunctionInstance2<IValue, IValue, IValue>)($result44));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(28227,581,<577,0>,<596,34>) 
    public IString ParseTree_unparse$8685ce60c1159ea5(ITree tree_0){ 
        
        
        final Template $template45 = (Template)new Template($RVF, "");
        $template45.addVal(tree_0);
        return ((IString)($template45.close()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(28810,92,<598,0>,<599,50>) 
    public IString ParseTree_printSymbol$4ce577b2a14fc038(IConstructor sym_0, IBool withLayout_1){ 
        
        
        return ((IString)((IString)$Prelude.printSymbol(sym_0, withLayout_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(28904,4953,<601,0>,<721,53>) 
    public IValue ParseTree_implode$13172869ec254a12(IConstructor t_0, ITree tree_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T7.match(t_0.getType(), $typeBindings)){
           final IValue $result46 = ((IValue)((IValue)$Prelude.implode(t_0, tree_1)));
           if($T39.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result46.getType(),$T39)){
              return ((IValue)($result46));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(34837,494,<760,0>,<775,1>) 
    public IConstructor ParseTree_treeAt$d353ab20d6109b21(IConstructor t_0, ISourceLocation l_1, ITree a_2){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T9.match(t_0.getType(), $typeBindings)){
           if(true){
              if(true){
                 if($has_type_and_arity(a_2, Tree_appl_Production_list_Tree, 2)){
                    IValue $arg0_53 = (IValue)($aadt_subscript_int(((ITree)a_2),0));
                    if($isComparable($arg0_53.getType(), $T3)){
                       IValue $arg1_52 = (IValue)($aadt_subscript_int(((ITree)a_2),1));
                       if($isComparable($arg1_52.getType(), $T3)){
                          if($is_defined_value($guarded_annotation_get(((INode)a_2),"src"))){
                             final ISourceLocation $subject_val50 = ((ISourceLocation)($annotation_get(((INode)a_2),"src")));
                             ISourceLocation al_3 = null;
                             if((((IBool)($aint_lessequal_aint(((IInteger)(((IInteger)($aloc_get_field(((ISourceLocation)($subject_val50)), "offset"))))),((IInteger)(((IInteger)($aloc_get_field(((ISourceLocation)l_1), "offset"))))))))).getValue()){
                               if((((IBool)($aint_less_aint(((IInteger)($aint_add_aint(((IInteger)(((IInteger)($aloc_get_field(((ISourceLocation)($subject_val50)), "offset"))))),((IInteger)(((IInteger)($aloc_get_field(((ISourceLocation)($subject_val50)), "length")))))))),((IInteger)($aint_add_aint(((IInteger)(((IInteger)($aloc_get_field(((ISourceLocation)l_1), "offset"))))),((IInteger)(((IInteger)($aloc_get_field(((ISourceLocation)l_1), "length"))))))))).not()))).getValue()){
                                 /*muExists*/FOR1: 
                                     do {
                                         FOR1_GEN35134:
                                         for(IValue $elem49_for : ((IList)(((IList)($aadt_get_field(((IConstructor)a_2), "args")))))){
                                             ITree $elem49 = (ITree) $elem49_for;
                                             ITree arg_4 = null;
                                             final IConstructor $subject_val47 = ((IConstructor)($me.treeAt(((IConstructor)t_0), ((ISourceLocation)l_1), ((ITree)($elem49)))));
                                             if($has_type_and_arity($subject_val47, TreeSearchResult_1_treeFound_, 1)){
                                                IValue $arg0_48 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val47)),0));
                                                if($isComparable($arg0_48.getType(), $T15)){
                                                   if($isSubtypeOf($arg0_48.getType(),$T15.instantiate($typeBindings))){
                                                      IConstructor r_5 = ((IConstructor)($subject_val47));
                                                      final IConstructor $result51 = ((IConstructor)r_5);
                                                      if(ADT_TreeSearchResult_1.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result51.getType(),ADT_TreeSearchResult_1)){
                                                         return ((IConstructor)($result51));
                                                      
                                                      } else {
                                                         return null;
                                                      }
                                                   } else {
                                                      continue FOR1_GEN35134;
                                                   }
                                                } else {
                                                   continue FOR1_GEN35134;
                                                }
                                             } else {
                                                continue FOR1_GEN35134;
                                             }
                                         }
                                         continue FOR1;
                                                     
                                     } while(false);
                                 /* void:  muCon([]) */if($isSubtypeOf(a_2.getType(),$T15.instantiate($typeBindings))){
                                    ITree tree_6 = null;
                                    final IConstructor $result51 = ((IConstructor)($RVF.constructor(TreeSearchResult_1_treeFound_, new IValue[]{((ITree)a_2)})));
                                    if(ADT_TreeSearchResult_1.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result51.getType(),ADT_TreeSearchResult_1)){
                                       return ((IConstructor)($result51));
                                    
                                    } else {
                                       return null;
                                    }
                                 }
                               
                               }
                             
                             }
                          
                          }
                          final IConstructor $result51 = ((IConstructor)($RVF.constructor(TreeSearchResult_1_treeNotFound_, new IValue[]{})));
                          if(ADT_TreeSearchResult_1.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result51.getType(),ADT_TreeSearchResult_1)){
                             return ((IConstructor)($result51));
                          
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(35333,95,<777,0>,<777,95>) 
    public IConstructor ParseTree_treeAt$070d7a31ea8abb2e(IConstructor t_0, ISourceLocation l_1, ITree root_2){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T9.match(t_0.getType(), $typeBindings)){
           final IConstructor $result54 = ((IConstructor)($RVF.constructor(TreeSearchResult_1_treeNotFound_, new IValue[]{})));
           if(ADT_TreeSearchResult_1.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result54.getType(),ADT_TreeSearchResult_1)){
              return ((IConstructor)($result54));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(35430,58,<779,0>,<779,58>) 
    public IBool ParseTree_sameType$b275756ae4e2966e(IConstructor $0, IConstructor t_1){ 
        
        
        if($has_type_and_arity($0, M_Type.Symbol_label_str_Symbol, 2)){
           IValue $arg0_56 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_56.getType(), $T3)){
              IValue $arg1_55 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_55.getType(), M_Type.ADT_Symbol)){
                 IConstructor s_0 = null;
                 return ((IBool)($me.sameType(((IConstructor)($arg1_55)), ((IConstructor)t_1))));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(35489,58,<780,0>,<780,58>) 
    public IBool ParseTree_sameType$a84782db931f7a30(IConstructor s_0, IConstructor $1){ 
        
        
        if($has_type_and_arity($1, M_Type.Symbol_label_str_Symbol, 2)){
           IValue $arg0_58 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
           if($isComparable($arg0_58.getType(), $T3)){
              IValue $arg1_57 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
              if($isComparable($arg1_57.getType(), M_Type.ADT_Symbol)){
                 IConstructor t_1 = null;
                 return ((IBool)($me.sameType(((IConstructor)s_0), ((IConstructor)($arg1_57)))));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(35548,64,<781,0>,<781,64>) 
    public IBool ParseTree_sameType$62cda661d939c9fa(IConstructor s_0, IConstructor $1){ 
        
        
        if($has_type_and_arity($1, Symbol_conditional_Symbol_set_Condition, 2)){
           IValue $arg0_60 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
           if($isComparable($arg0_60.getType(), M_Type.ADT_Symbol)){
              IConstructor t_1 = null;
              IValue $arg1_59 = (IValue)($aadt_subscript_int(((IConstructor)$1),1));
              if($isComparable($arg1_59.getType(), $T3)){
                 return ((IBool)($me.sameType(((IConstructor)s_0), ((IConstructor)($arg0_60)))));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(35613,65,<782,0>,<782,65>) 
    public IBool ParseTree_sameType$10233296380130e3(IConstructor $0, IConstructor t_1){ 
        
        
        if($has_type_and_arity($0, Symbol_conditional_Symbol_set_Condition, 2)){
           IValue $arg0_62 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_62.getType(), M_Type.ADT_Symbol)){
              IConstructor s_0 = null;
              IValue $arg1_61 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_61.getType(), $T3)){
                 return ((IBool)($me.sameType(((IConstructor)($arg0_62)), ((IConstructor)t_1))));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(35679,34,<783,0>,<783,34>) 
    public IBool ParseTree_sameType$3698dd87f53b84eb(IConstructor s_0, IConstructor s){ 
        
        
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(35714,50,<784,0>,<784,50>) 
    public IBool ParseTree_sameType$e3b3acd78100d719(IConstructor s_0, IConstructor t_1){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(35767,115,<787,0>,<788,52>) 
    public IBool ParseTree_isNonTerminalType$897add07fd0679fa(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_sort_str, 1)){
           IValue $arg0_63 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_63.getType(), $T11)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(35883,51,<789,0>,<789,51>) 
    public IBool ParseTree_isNonTerminalType$bd8ebacb8470da66(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_lex_str, 1)){
           IValue $arg0_64 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_64.getType(), $T11)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(35935,55,<790,0>,<790,55>) 
    public IBool ParseTree_isNonTerminalType$60a31525ab4124d2(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_layouts_str, 1)){
           IValue $arg0_65 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_65.getType(), $T11)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(35991,56,<791,0>,<791,56>) 
    public IBool ParseTree_isNonTerminalType$596a4e65f73f6ddd(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_keywords_str, 1)){
           IValue $arg0_66 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_66.getType(), $T11)){
              return ((IBool)$constants.get(1)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(36048,82,<792,0>,<792,82>) 
    public IBool ParseTree_isNonTerminalType$fd8640b90be01f96(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameterized_sort_str_list_Symbol, 2)){
           IValue $arg0_68 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_68.getType(), $T11)){
              IValue $arg1_67 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_67.getType(), $T12)){
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(36131,81,<793,0>,<793,81>) 
    public IBool ParseTree_isNonTerminalType$bec4d445d8286a07(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_parameterized_lex_str_list_Symbol, 2)){
           IValue $arg0_70 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_70.getType(), $T11)){
              IValue $arg1_69 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_69.getType(), $T12)){
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(36213,72,<794,0>,<794,72>) 
    public IBool ParseTree_isNonTerminalType$f27daba5b606f35d(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, Symbol_start_Symbol, 1)){
           IValue $arg0_71 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_71.getType(), M_Type.ADT_Symbol)){
              IConstructor s_0 = null;
              return ((IBool)($me.isNonTerminalType(((IConstructor)($arg0_71)))));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/ParseTree.rsc|(36286,49,<795,0>,<795,49>) 
    public IBool ParseTree_isNonTerminalType$c6eab95f5fe5b746(IConstructor s_0){ 
        
        
        return ((IBool)$constants.get(2)/*false*/);
    
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `ParseTree`");
    }
}