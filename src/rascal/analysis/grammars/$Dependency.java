package rascal.analysis.grammars;
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
public class $Dependency 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.analysis.grammars.$Dependency_$I {

    private final $Dependency_$I $me;
    private final IList $constants;
    
    
    public final rascal.analysis.graphs.$Graph M_analysis_graphs_Graph;
    public final rascal.$ParseTree M_ParseTree;
    public final rascal.$Type M_Type;
    public final rascal.$List M_List;
    public final rascal.$Grammar M_Grammar;
    public final rascal.$Message M_Message;

    
    
    public final io.usethesource.vallang.type.Type $T2;	/*avalue()*/
    public final io.usethesource.vallang.type.Type ADT_Exception;	/*aadt("Exception",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Item;	/*aadt("Item",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Grammar;	/*aadt("Grammar",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationType;	/*aadt("LocationType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Associativity;	/*aadt("Associativity",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarDefinition;	/*aadt("GrammarDefinition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarModule;	/*aadt("GrammarModule",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CharRange;	/*aadt("CharRange",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeEvent;	/*aadt("LocationChangeEvent",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T0;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T1;	/*alist(aadt("Symbol",[],dataSyntax()))*/

    public $Dependency(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Dependency(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Dependency_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.analysis.grammars.$Dependency.class, this);
       
       mstore.importModule(rascal.analysis.graphs.$Graph.class, rex, rascal.analysis.graphs.$Graph::new);
       mstore.importModule(rascal.$ParseTree.class, rex, rascal.$ParseTree::new);
       mstore.importModule(rascal.$Type.class, rex, rascal.$Type::new);
       mstore.importModule(rascal.$List.class, rex, rascal.$List::new);
       mstore.importModule(rascal.$Grammar.class, rex, rascal.$Grammar::new);
       mstore.importModule(rascal.$Message.class, rex, rascal.$Message::new); 
       
       M_analysis_graphs_Graph = mstore.getModule(rascal.analysis.graphs.$Graph.class);
       M_ParseTree = mstore.getModule(rascal.$ParseTree.class);
       M_Type = mstore.getModule(rascal.$Type.class);
       M_List = mstore.getModule(rascal.$List.class);
       M_Grammar = mstore.getModule(rascal.$Grammar.class);
       M_Message = mstore.getModule(rascal.$Message.class); 
       
                          
       
       $TS.importStore(M_analysis_graphs_Graph.$TS);
       $TS.importStore(M_ParseTree.$TS);
       $TS.importStore(M_Type.$TS);
       $TS.importStore(M_List.$TS);
       $TS.importStore(M_Grammar.$TS);
       $TS.importStore(M_Message.$TS);
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/analysis/grammars/$Dependency.constants", 3, "2d6609f5b74a13fd7dd7606ef7eded19");
       ADT_Exception = $adt("Exception");
       ADT_Item = $adt("Item");
       ADT_Grammar = $adt("Grammar");
       ADT_LocationType = $adt("LocationType");
       ADT_Associativity = $adt("Associativity");
       ADT_GrammarDefinition = $adt("GrammarDefinition");
       ADT_IOCapability = $adt("IOCapability");
       ADT_GrammarModule = $adt("GrammarModule");
       ADT_Tree = $adt("Tree");
       ADT_CharRange = $adt("CharRange");
       ADT_Attr = $adt("Attr");
       ADT_LocationChangeType = $adt("LocationChangeType");
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       ADT_RuntimeException = $adt("RuntimeException");
       ADT_Symbol = $adt("Symbol");
       ADT_Message = $adt("Message");
       ADT_Production = $adt("Production");
       ADT_Condition = $adt("Condition");
       $T2 = $TF.valueType();
       $T0 = $TF.parameterType("T", ADT_Tree);
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T0 });
       $T1 = $TF.listType(ADT_Symbol);
    
       
       
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
    public ISet symbolDependencies(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_GrammarDefinition)){
         $result = (ISet)analysis_grammars_Dependency_symbolDependencies$593b66cc107be4d0((IConstructor) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar)){
         $result = (ISet)analysis_grammars_Dependency_symbolDependencies$4423bb0246bba137((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
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
    public IConstructor delabel(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IConstructor)analysis_grammars_Dependency_delabel$b2d06636a5fe2e59((IConstructor) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IConstructor)analysis_grammars_Dependency_delabel$a3a594b13cfc4d55((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
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
    public IBool isIntType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isIntType($P0);
    }
    public IBool isDateTimeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isDateTimeType($P0);
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/analysis/grammars/Dependency.rsc|(448,402,<15,0>,<19,221>) 
    public ISet analysis_grammars_Dependency_symbolDependencies$4423bb0246bba137(IConstructor g_0){ 
        
        
        final ISetWriter $setwriter0 = (ISetWriter)$RVF.setWriter();
        ;
        /*muExists*/$SCOMP1: 
            do {
                $SCOMP1_DESC645:
                for(IValue $elem8 : new DescendantMatchIterator(g_0, 
                    new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                    if($isComparable($elem8.getType(), M_ParseTree.ADT_Production)){
                       if($has_type_and_arity($elem8, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                          IValue $arg0_14 = (IValue)($subscript_int(((IValue)($elem8)),0));
                          if($isComparable($arg0_14.getType(), M_ParseTree.ADT_Symbol)){
                             if(true){
                                IConstructor s_1 = null;
                                IValue $arg1_10 = (IValue)($subscript_int(((IValue)($elem8)),1));
                                if($isComparable($arg1_10.getType(), $T1)){
                                   final IList $subject11 = ((IList)($arg1_10));
                                   int $subject11_cursor = 0;
                                   if($isSubtypeOf($subject11.getType(),$T1)){
                                      final int $subject11_len = (int)((IList)($subject11)).length();
                                      if($subject11_len >= 1){
                                         final int $__113_start = (int)$subject11_cursor;
                                         $SCOMP1_DESC645_CONS_prod_LIST_MVAR$_2:
                                         
                                         for(int $__113_len = 0; $__113_len <= $subject11_len - $__113_start - 1; $__113_len += 1){
                                            $subject11_cursor = $__113_start + $__113_len;
                                            if($subject11_cursor < $subject11_len){
                                               IConstructor elem_2 = ((IConstructor)($alist_subscript_int(((IList)($subject11)),$subject11_cursor)));
                                               $subject11_cursor += 1;
                                               final int $__112_start = (int)$subject11_cursor;
                                               final int $__112_len = (int)$subject11_len - $__112_start - 0;
                                               $subject11_cursor = $__112_start + $__112_len;
                                               /*muExists*/$SCOMP1_DESC645_CONS_prod_LIST_MVAR$_2_VARelem_MVAR$_3: 
                                                   do {
                                                       if($subject11_cursor == $subject11_len){
                                                          IValue $arg2_9 = (IValue)($subscript_int(((IValue)($elem8)),2));
                                                          if($isComparable($arg2_9.getType(), $T2)){
                                                             final IConstructor $subject_val7 = ((IConstructor)($me.delabel(((IConstructor)($arg0_14)))));
                                                             if(true){
                                                                IConstructor from_3 = null;
                                                                $SCOMP1_DESC716:
                                                                for(IValue $elem6 : new DescendantMatchIterator(elem_2, 
                                                                    new DescendantDescriptor(new io.usethesource.vallang.type.Type[]{$TF.listType(ADT_Symbol), M_Type.ADT_Exception, $TF.setType(ADT_Symbol), $TF.setType(ADT_Condition), M_ParseTree.ADT_Tree, M_ParseTree.ADT_TreeSearchResult_1, M_ParseTree.ADT_Condition, M_ParseTree.ADT_Production, M_ParseTree.ADT_Symbol, M_Grammar.ADT_Grammar, M_ParseTree.ADT_CharRange, M_Grammar.ADT_Item, M_Grammar.ADT_GrammarModule, $TF.listType(ADT_CharRange), M_Grammar.ADT_GrammarDefinition}, 
                                                                                             new io.usethesource.vallang.IConstructor[]{}, 
                                                                                             $RVF.bool(false)))){
                                                                    if($isComparable($elem6.getType(), M_ParseTree.ADT_Symbol)){
                                                                       if($isSubtypeOf($elem6.getType(),M_ParseTree.ADT_Symbol)){
                                                                          IConstructor to_4 = null;
                                                                          if($is(((IConstructor)($elem6)),((IString)$constants.get(0)/*"sort"*/))){
                                                                            if($is(((IConstructor)($subject_val7)),((IString)$constants.get(0)/*"sort"*/))){
                                                                              $setwriter0.insert($RVF.tuple(((IConstructor)($subject_val7)), ((IConstructor)($elem6))));
                                                                            
                                                                            } else {
                                                                              if($is(((IConstructor)($subject_val7)),((IString)$constants.get(1)/*"lex"*/))){
                                                                                $setwriter0.insert($RVF.tuple(((IConstructor)($subject_val7)), ((IConstructor)($elem6))));
                                                                              
                                                                              } else {
                                                                                if($is(((IConstructor)($subject_val7)),((IString)$constants.get(2)/*"parameterized-sort"*/))){
                                                                                  $setwriter0.insert($RVF.tuple(((IConstructor)($subject_val7)), ((IConstructor)($elem6))));
                                                                                
                                                                                } else {
                                                                                  continue $SCOMP1_DESC716;
                                                                                }
                                                                              
                                                                              }
                                                                            
                                                                            }
                                                                          
                                                                          } else {
                                                                            if($is(((IConstructor)($elem6)),((IString)$constants.get(1)/*"lex"*/))){
                                                                              if($is(((IConstructor)($subject_val7)),((IString)$constants.get(0)/*"sort"*/))){
                                                                                $setwriter0.insert($RVF.tuple(((IConstructor)($subject_val7)), ((IConstructor)($elem6))));
                                                                              
                                                                              } else {
                                                                                if($is(((IConstructor)($subject_val7)),((IString)$constants.get(1)/*"lex"*/))){
                                                                                  $setwriter0.insert($RVF.tuple(((IConstructor)($subject_val7)), ((IConstructor)($elem6))));
                                                                                
                                                                                } else {
                                                                                  if($is(((IConstructor)($subject_val7)),((IString)$constants.get(2)/*"parameterized-sort"*/))){
                                                                                    $setwriter0.insert($RVF.tuple(((IConstructor)($subject_val7)), ((IConstructor)($elem6))));
                                                                                  
                                                                                  } else {
                                                                                    continue $SCOMP1_DESC716;
                                                                                  }
                                                                                
                                                                                }
                                                                              
                                                                              }
                                                                            
                                                                            } else {
                                                                              if($is(((IConstructor)($elem6)),((IString)$constants.get(2)/*"parameterized-sort"*/))){
                                                                                if($is(((IConstructor)($subject_val7)),((IString)$constants.get(0)/*"sort"*/))){
                                                                                  $setwriter0.insert($RVF.tuple(((IConstructor)($subject_val7)), ((IConstructor)($elem6))));
                                                                                
                                                                                } else {
                                                                                  if($is(((IConstructor)($subject_val7)),((IString)$constants.get(1)/*"lex"*/))){
                                                                                    $setwriter0.insert($RVF.tuple(((IConstructor)($subject_val7)), ((IConstructor)($elem6))));
                                                                                  
                                                                                  } else {
                                                                                    if($is(((IConstructor)($subject_val7)),((IString)$constants.get(2)/*"parameterized-sort"*/))){
                                                                                      $setwriter0.insert($RVF.tuple(((IConstructor)($subject_val7)), ((IConstructor)($elem6))));
                                                                                    
                                                                                    } else {
                                                                                      continue $SCOMP1_DESC716;
                                                                                    }
                                                                                  
                                                                                  }
                                                                                
                                                                                }
                                                                              
                                                                              } else {
                                                                                continue $SCOMP1_DESC716;
                                                                              }
                                                                            
                                                                            }
                                                                          
                                                                          }
                                                                       
                                                                       } else {
                                                                          continue $SCOMP1_DESC716;
                                                                       }
                                                                    } else {
                                                                       continue $SCOMP1_DESC716;
                                                                    }
                                                                }
                                                                continue $SCOMP1_DESC645;
                                                                             
                                                             } else {
                                                                continue $SCOMP1_DESC645;
                                                             }
                                                          } else {
                                                             continue $SCOMP1_DESC645_CONS_prod_LIST_MVAR$_2_VARelem_MVAR$_3;/*computeFail*/
                                                          }
                                                       } else {
                                                          continue $SCOMP1_DESC645_CONS_prod_LIST_MVAR$_2_VARelem_MVAR$_3;/*list match1*/
                                                       }
                                                   } while(false);
                                               continue $SCOMP1_DESC645_CONS_prod_LIST_MVAR$_2;/*computeFail*/
                                            } else {
                                               continue $SCOMP1_DESC645_CONS_prod_LIST_MVAR$_2;/*computeFail*/
                                            }
                                         }
                                         continue $SCOMP1_DESC645;
                                      
                                      } else {
                                         continue $SCOMP1_DESC645;
                                      }
                                   } else {
                                      continue $SCOMP1_DESC645;
                                   }
                                } else {
                                   continue $SCOMP1_DESC645;
                                }
                             } else {
                                continue $SCOMP1_DESC645;
                             }
                          } else {
                             continue $SCOMP1_DESC645;
                          }
                       } else {
                          continue $SCOMP1_DESC645;
                       }
                    } else {
                       continue $SCOMP1_DESC645;
                    }
                }
                
                             
            } while(false);
        return ((ISet)($setwriter0.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/analysis/grammars/Dependency.rsc|(852,121,<21,0>,<22,65>) 
    public ISet analysis_grammars_Dependency_symbolDependencies$593b66cc107be4d0(IConstructor d_0){ 
        
        
        final ISetWriter $setwriter15 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP16_GEN956:
        for(IValue $elem17_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)d_0), "modules")))))){
            IString $elem17 = (IString) $elem17_for;
            IString m_1 = ((IString)($elem17));
            $setwriter_splice($setwriter15,$me.symbolDependencies(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)d_0), "modules"))))),((IString)m_1)))), "grammar")))))));
        
        }
        
                    return ((ISet)($setwriter15.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/analysis/grammars/Dependency.rsc|(975,47,<24,0>,<24,47>) 
    public IConstructor analysis_grammars_Dependency_delabel$b2d06636a5fe2e59(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_Type.Symbol_label_str_Symbol, 2)){
           IValue $arg0_19 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_19.getType(), $T2)){
              IValue $arg1_18 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_18.getType(), M_ParseTree.ADT_Symbol)){
                 IConstructor t_0 = null;
                 return ((IConstructor)($arg1_18));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/analysis/grammars/Dependency.rsc|(1023,45,<25,0>,<25,45>) 
    public IConstructor analysis_grammars_Dependency_delabel$a3a594b13cfc4d55(IConstructor x_0){ 
        
        
        return ((IConstructor)x_0);
    
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `analysis::grammars::Dependency`");
    }
}