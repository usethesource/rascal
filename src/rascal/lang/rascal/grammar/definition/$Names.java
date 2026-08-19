package rascal.lang.rascal.grammar.definition;
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
public class $Names 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.lang.rascal.grammar.definition.$Names_$I {

    private final $Names_$I $me;
    private final IList $constants;
    
    
    public final rascal.$ParseTree M_ParseTree;
    public final rascal.$Type M_Type;
    public final rascal.$List M_List;
    public final rascal.$Grammar M_Grammar;
    public final rascal.$Message M_Message;

    
    
    public final io.usethesource.vallang.type.Type $T0;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T2;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T3;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_LocationType;	/*aadt("LocationType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Exception;	/*aadt("Exception",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CharRange;	/*aadt("CharRange",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Grammar;	/*aadt("Grammar",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Associativity;	/*aadt("Associativity",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarDefinition;	/*aadt("GrammarDefinition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarModule;	/*aadt("GrammarModule",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Item;	/*aadt("Item",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeEvent;	/*aadt("LocationChangeEvent",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T4;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T5;	/*alist(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T1;	/*alist(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/

    public $Names(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Names(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Names_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.lang.rascal.grammar.definition.$Names.class, this);
       
       mstore.importModule(rascal.$ParseTree.class, rex, rascal.$ParseTree::new);
       mstore.importModule(rascal.$Type.class, rex, rascal.$Type::new);
       mstore.importModule(rascal.$List.class, rex, rascal.$List::new);
       mstore.importModule(rascal.$Grammar.class, rex, rascal.$Grammar::new);
       mstore.importModule(rascal.$Message.class, rex, rascal.$Message::new); 
       
       M_ParseTree = mstore.getModule(rascal.$ParseTree.class);
       M_Type = mstore.getModule(rascal.$Type.class);
       M_List = mstore.getModule(rascal.$List.class);
       M_Grammar = mstore.getModule(rascal.$Grammar.class);
       M_Message = mstore.getModule(rascal.$Message.class); 
       
                          
       
       $TS.importStore(M_ParseTree.$TS);
       $TS.importStore(M_Type.$TS);
       $TS.importStore(M_List.$TS);
       $TS.importStore(M_Grammar.$TS);
       $TS.importStore(M_Message.$TS);
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/lang/rascal/grammar/definition/$Names.constants", 0, "d751713988987e9331980363e24189ce");
       ADT_LocationType = $adt("LocationType");
       ADT_Exception = $adt("Exception");
       ADT_CharRange = $adt("CharRange");
       ADT_Grammar = $adt("Grammar");
       ADT_Associativity = $adt("Associativity");
       ADT_GrammarDefinition = $adt("GrammarDefinition");
       ADT_IOCapability = $adt("IOCapability");
       ADT_GrammarModule = $adt("GrammarModule");
       ADT_Tree = $adt("Tree");
       ADT_Item = $adt("Item");
       ADT_Attr = $adt("Attr");
       ADT_LocationChangeType = $adt("LocationChangeType");
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       ADT_RuntimeException = $adt("RuntimeException");
       ADT_Symbol = $adt("Symbol");
       ADT_Message = $adt("Message");
       ADT_Production = $adt("Production");
       ADT_Condition = $adt("Condition");
       $T0 = $TF.stringType();
       $T2 = $TF.valueType();
       $T3 = $TF.parameterType("T", $T2);
       $T4 = $TF.parameterType("T", ADT_Tree);
       $T5 = $TF.listType(ADT_Symbol);
       $T1 = $TF.listType($T3);
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T4 });
    
       
       
    }
    public IBool isTypeVar(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isTypeVar($P0);
    }
    public IList addLabels(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_Type.addLabels($P0, $P1);
    }
    public IBool sameType(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IBool) M_ParseTree.sameType($P0, $P1);
    }
    public IValue head(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.head($P0);
    }
    public IBool isAliasType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isAliasType($P0);
    }
    public IBool isStrType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isStrType($P0);
    }
    public IConstructor choice(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_Type.choice($P0, $P1);
    }
    public IBool isValueType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isValueType($P0);
    }
    public IBool isADTType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isADTType($P0);
    }
    public IBool isListType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isListType($P0);
    }
    public IBool isRealType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRealType($P0);
    }
    public IConstructor priority(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_ParseTree.priority($P0, $P1);
    }
    public IBool isNodeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNodeType($P0);
    }
    public IBool isReifiedType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isReifiedType($P0);
    }
    public IBool isRelType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRelType($P0);
    }
    public IBool isConstructorType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isConstructorType($P0);
    }
    public IBool isListRelType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isListRelType($P0);
    }
    public IList addParamLabels(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_Type.addParamLabels($P0, $P1);
    }
    public IConstructor resolve(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar)){
         $result = (IConstructor)lang_rascal_grammar_definition_Names_resolve$b1807eb13cb5ba05((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isMapType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isMapType($P0);
    }
    public IBool isBoolType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isBoolType($P0);
    }
    public IList tail(IValue $P0){ // Generated by Resolver
       return (IList) M_List.tail($P0);
    }
    public IBool isLocType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isLocType($P0);
    }
    public ITuple headTail(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.headTail($P0);
    }
    public IConstructor treeAt(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IConstructor) M_ParseTree.treeAt($P0, $P1, $P2);
    }
    public IBool isSetType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isSetType($P0);
    }
    public IBool isRatType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRatType($P0);
    }
    public IBool isNumType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNumType($P0);
    }
    public IBool isTupleType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isTupleType($P0);
    }
    public IBool isBagType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isBagType($P0);
    }
    public IBool isVoidType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isVoidType($P0);
    }
    public IBool isNonTerminalType(IValue $P0){ // Generated by Resolver
       return (IBool) M_ParseTree.isNonTerminalType($P0);
    }
    public IValue lub(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_Type.lub($P0, $P1);
    }
    public IString unescape(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)lang_rascal_grammar_definition_Names_unescape$24eb09f0489e62c1((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool subtype(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IBool) M_Type.subtype($P0, $P1);
    }
    public IConstructor associativity(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IConstructor) M_ParseTree.associativity($P0, $P1, $P2);
    }
    public IBool isFunctionType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isFunctionType($P0);
    }
    public IValue glb(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_Type.glb($P0, $P1);
    }
    public IValue sort(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         $result = (IValue)M_List.List_sort$1fe4426c8c8039da((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         return $RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{(IString) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList sort(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.sort($P0, $P1);
    }
    public IBool isIntType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isIntType($P0);
    }
    public IBool isDateTimeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isDateTimeType($P0);
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Names.rsc|(368,1102,<13,0>,<47,1>) 
    public IConstructor lang_rascal_grammar_definition_Names_resolve$b1807eb13cb5ba05(IConstructor d_0){ 
        
        
        try {
            final ISetWriter $setwriter0 = (ISetWriter)$RVF.setWriter();
            ;
            /*muExists*/$SCOMP1_GEN685_CONS_sort: 
                do {
                    $SCOMP1_GEN685:
                    for(IValue $elem2_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)d_0), "rules")))))){
                        IConstructor $elem2 = (IConstructor) $elem2_for;
                        if($has_type_and_arity($elem2, M_ParseTree.Symbol_sort_str, 1)){
                           IValue $arg0_3 = (IValue)($aadt_subscript_int(((IConstructor)($elem2)),0));
                           if($isComparable($arg0_3.getType(), $T0)){
                              IString n_2 = null;
                              $setwriter0.insert($arg0_3);
                           
                           } else {
                              continue $SCOMP1_GEN685;
                           }
                        } else {
                           continue $SCOMP1_GEN685;
                        }
                    }
                    
                                
                } while(false);
            final ValueRef<ISet> cd_1 = new ValueRef<ISet>("cd", ((ISet)($setwriter0.done())));
            final ISetWriter $setwriter4 = (ISetWriter)$RVF.setWriter();
            ;
            /*muExists*/$SCOMP5_GEN720_CONS_parameterized_sort: 
                do {
                    $SCOMP5_GEN720:
                    for(IValue $elem6_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)d_0), "rules")))))){
                        IConstructor $elem6 = (IConstructor) $elem6_for;
                        if($has_type_and_arity($elem6, M_ParseTree.Symbol_parameterized_sort_str_list_Symbol, 2)){
                           IValue $arg0_8 = (IValue)($aadt_subscript_int(((IConstructor)($elem6)),0));
                           if($isComparable($arg0_8.getType(), $T0)){
                              IString n_4 = null;
                              IValue $arg1_7 = (IValue)($aadt_subscript_int(((IConstructor)($elem6)),1));
                              if($isComparable($arg1_7.getType(), $T2)){
                                 $setwriter4.insert($arg0_8);
                              
                              } else {
                                 continue $SCOMP5_GEN720;
                              }
                           } else {
                              continue $SCOMP5_GEN720;
                           }
                        } else {
                           continue $SCOMP5_GEN720;
                        }
                    }
                    
                                
                } while(false);
            final ValueRef<ISet> pcd_3 = new ValueRef<ISet>("pcd", ((ISet)($setwriter4.done())));
            final ISetWriter $setwriter9 = (ISetWriter)$RVF.setWriter();
            ;
            /*muExists*/$SCOMP10_GEN770_CONS_lex: 
                do {
                    $SCOMP10_GEN770:
                    for(IValue $elem11_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)d_0), "rules")))))){
                        IConstructor $elem11 = (IConstructor) $elem11_for;
                        if($has_type_and_arity($elem11, M_ParseTree.Symbol_lex_str, 1)){
                           IValue $arg0_12 = (IValue)($aadt_subscript_int(((IConstructor)($elem11)),0));
                           if($isComparable($arg0_12.getType(), $T0)){
                              IString n_6 = null;
                              $setwriter9.insert($arg0_12);
                           
                           } else {
                              continue $SCOMP10_GEN770;
                           }
                        } else {
                           continue $SCOMP10_GEN770;
                        }
                    }
                    
                                
                } while(false);
            final ValueRef<ISet> lx_5 = new ValueRef<ISet>("lx", ((ISet)($setwriter9.done())));
            final ISetWriter $setwriter13 = (ISetWriter)$RVF.setWriter();
            ;
            /*muExists*/$SCOMP14_GEN804_CONS_parameterized_lex: 
                do {
                    $SCOMP14_GEN804:
                    for(IValue $elem15_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)d_0), "rules")))))){
                        IConstructor $elem15 = (IConstructor) $elem15_for;
                        if($has_type_and_arity($elem15, M_ParseTree.Symbol_parameterized_lex_str_list_Symbol, 2)){
                           IValue $arg0_17 = (IValue)($aadt_subscript_int(((IConstructor)($elem15)),0));
                           if($isComparable($arg0_17.getType(), $T0)){
                              IString n_8 = null;
                              IValue $arg1_16 = (IValue)($aadt_subscript_int(((IConstructor)($elem15)),1));
                              if($isComparable($arg1_16.getType(), $T2)){
                                 $setwriter13.insert($arg0_17);
                              
                              } else {
                                 continue $SCOMP14_GEN804;
                              }
                           } else {
                              continue $SCOMP14_GEN804;
                           }
                        } else {
                           continue $SCOMP14_GEN804;
                        }
                    }
                    
                                
                } while(false);
            final ValueRef<ISet> plx_7 = new ValueRef<ISet>("plx", ((ISet)($setwriter13.done())));
            final ISetWriter $setwriter18 = (ISetWriter)$RVF.setWriter();
            ;
            /*muExists*/$SCOMP19_GEN853_CONS_keywords: 
                do {
                    $SCOMP19_GEN853:
                    for(IValue $elem20_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)d_0), "rules")))))){
                        IConstructor $elem20 = (IConstructor) $elem20_for;
                        if($has_type_and_arity($elem20, M_ParseTree.Symbol_keywords_str, 1)){
                           IValue $arg0_21 = (IValue)($aadt_subscript_int(((IConstructor)($elem20)),0));
                           if($isComparable($arg0_21.getType(), $T0)){
                              IString n_10 = null;
                              $setwriter18.insert($arg0_21);
                           
                           } else {
                              continue $SCOMP19_GEN853;
                           }
                        } else {
                           continue $SCOMP19_GEN853;
                        }
                    }
                    
                                
                } while(false);
            final ValueRef<ISet> ks_9 = new ValueRef<ISet>("ks", ((ISet)($setwriter18.done())));
            final ISetWriter $setwriter22 = (ISetWriter)$RVF.setWriter();
            ;
            /*muExists*/$SCOMP23_GEN891_CONS_layouts: 
                do {
                    $SCOMP23_GEN891:
                    for(IValue $elem24_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)d_0), "rules")))))){
                        IConstructor $elem24 = (IConstructor) $elem24_for;
                        if($has_type_and_arity($elem24, M_ParseTree.Symbol_layouts_str, 1)){
                           IValue $arg0_25 = (IValue)($aadt_subscript_int(((IConstructor)($elem24)),0));
                           if($isComparable($arg0_25.getType(), $T0)){
                              IString n_12 = null;
                              $setwriter22.insert($arg0_25);
                           
                           } else {
                              continue $SCOMP23_GEN891;
                           }
                        } else {
                           continue $SCOMP23_GEN891;
                        }
                    }
                    
                                
                } while(false);
            final ValueRef<ISet> ls_11 = new ValueRef<ISet>("ls", ((ISet)($setwriter22.done())));
            IValue $visitResult = $TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.Yes, 
                 new DescendantDescriptorAlwaysTrue($RVF.bool(false)),
                 d_0,
                 (IVisitFunction) (IValue $VISIT0_subject, TraversalState $traversalState) -> {
                     VISIT0:switch(Fingerprint.getFingerprint($VISIT0_subject)){
                     
                         case 1444258592:
                             if($isSubtypeOf($VISIT0_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_1444258592_1: 
                                    do {
                                        if($has_type_and_arity($VISIT0_subject, M_ParseTree.Symbol_parameterized_sort_str_list_Symbol, 2)){
                                           IValue $arg0_28 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT0_subject)),0));
                                           if($isComparable($arg0_28.getType(), $T0)){
                                              ValueRef<IString> n_14 = new ValueRef<IString>();
                                              IValue $arg1_27 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT0_subject)),1));
                                              if($isComparable($arg1_27.getType(), $T5)){
                                                 ValueRef<IList> ps_15 = new ValueRef<IList>();
                                                 if((((IBool)($RVF.bool(plx_7.getValue().contains(((IString)($arg0_28))))))).getValue()){
                                                    $traversalState.setMatchedAndChanged(true, true);
                                                    return $RVF.constructor(M_ParseTree.Symbol_parameterized_lex_str_list_Symbol, new IValue[]{((IString)($arg0_28)), ((IList)($arg1_27))});
                                                 
                                                 }
                                                 continue CASE_1444258592_1;
                                              }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                         case 856312:
                             if($isSubtypeOf($VISIT0_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_856312_2: 
                                    do {
                                        if($has_type_and_arity($VISIT0_subject, M_ParseTree.Symbol_lex_str, 1)){
                                           IValue $arg0_29 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT0_subject)),0));
                                           if($isComparable($arg0_29.getType(), $T0)){
                                              ValueRef<IString> n_16 = new ValueRef<IString>();
                                              if((((IBool)($RVF.bool(cd_1.getValue().contains(((IString)($arg0_29))))))).getValue()){
                                                 $traversalState.setMatchedAndChanged(true, true);
                                                 return $RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)($arg0_29))});
                                              
                                              }
                                              if((((IBool)($RVF.bool(ks_9.getValue().contains(((IString)($arg0_29))))))).getValue()){
                                                 $traversalState.setMatchedAndChanged(true, true);
                                                 return $RVF.constructor(M_ParseTree.Symbol_keywords_str, new IValue[]{((IString)($arg0_29))});
                                              
                                              }
                                              if((((IBool)($RVF.bool(ls_11.getValue().contains(((IString)($arg0_29))))))).getValue()){
                                                 $traversalState.setMatchedAndChanged(true, true);
                                                 return $RVF.constructor(M_ParseTree.Symbol_layouts_str, new IValue[]{((IString)($arg0_29))});
                                              
                                              }
                                              continue CASE_856312_2;
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                         case 1154855088:
                             if($isSubtypeOf($VISIT0_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_1154855088_3: 
                                    do {
                                        if($has_type_and_arity($VISIT0_subject, M_ParseTree.Symbol_parameterized_lex_str_list_Symbol, 2)){
                                           IValue $arg0_31 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT0_subject)),0));
                                           if($isComparable($arg0_31.getType(), $T0)){
                                              ValueRef<IString> n_17 = new ValueRef<IString>();
                                              IValue $arg1_30 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT0_subject)),1));
                                              if($isComparable($arg1_30.getType(), $T5)){
                                                 ValueRef<IList> ps_18 = new ValueRef<IList>();
                                                 if((((IBool)($RVF.bool(pcd_3.getValue().contains(((IString)($arg0_31))))))).getValue()){
                                                    $traversalState.setMatchedAndChanged(true, true);
                                                    return $RVF.constructor(M_ParseTree.Symbol_parameterized_sort_str_list_Symbol, new IValue[]{((IString)($arg0_31)), ((IList)($arg1_30))});
                                                 
                                                 }
                                                 continue CASE_1154855088_3;
                                              }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                         case 28290288:
                             if($isSubtypeOf($VISIT0_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_28290288_0: 
                                    do {
                                        if($has_type_and_arity($VISIT0_subject, M_ParseTree.Symbol_sort_str, 1)){
                                           IValue $arg0_26 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT0_subject)),0));
                                           if($isComparable($arg0_26.getType(), $T0)){
                                              ValueRef<IString> n_13 = new ValueRef<IString>();
                                              if((((IBool)($RVF.bool(lx_5.getValue().contains(((IString)($arg0_26))))))).getValue()){
                                                 $traversalState.setMatchedAndChanged(true, true);
                                                 return $RVF.constructor(M_ParseTree.Symbol_lex_str, new IValue[]{((IString)($arg0_26))});
                                              
                                              }
                                              if((((IBool)($RVF.bool(ks_9.getValue().contains(((IString)($arg0_26))))))).getValue()){
                                                 $traversalState.setMatchedAndChanged(true, true);
                                                 return $RVF.constructor(M_ParseTree.Symbol_keywords_str, new IValue[]{((IString)($arg0_26))});
                                              
                                              }
                                              if((((IBool)($RVF.bool(ls_11.getValue().contains(((IString)($arg0_26))))))).getValue()){
                                                 $traversalState.setMatchedAndChanged(true, true);
                                                 return $RVF.constructor(M_ParseTree.Symbol_layouts_str, new IValue[]{((IString)($arg0_26))});
                                              
                                              }
                                              continue CASE_28290288_0;
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                     
                     }
                     return $VISIT0_subject;
                 });
            return (IConstructor)$visitResult;
        
        } catch (ReturnFromTraversalException e) {
            return (IConstructor) e.getValue();
        }
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Names.rsc|(1472,91,<49,0>,<52,1>) 
    public IString lang_rascal_grammar_definition_Names_unescape$24eb09f0489e62c1(IString name_0){ 
        
        
        /*muExists*/IF9: 
            do {
                final Matcher $matcher32 = (Matcher)$regExpCompile("\\\\(.*)", ((IString)name_0).getValue());
                boolean $found33 = true;
                
                    while($found33){
                        $found33 = $matcher32.find();
                        if($found33){
                           IString rest_1 = ((IString)($RVF.string($matcher32.group(1))));
                           return ((IString)rest_1);
                        
                        }
                
                    }
        
            } while(false);
        return ((IString)name_0);
    
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `lang::rascal::grammar::definition::Names`");
    }
}