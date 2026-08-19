package rascal.lang.rascal.grammar.tests;
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
public class $TestGrammars 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.lang.rascal.grammar.tests.$TestGrammars_$I {

    private final $TestGrammars_$I $me;
    private final IList $constants;
    
    
    public final rascal.$ParseTree M_ParseTree;
    public final rascal.$Type M_Type;
    public final rascal.$List M_List;
    public final rascal.$Grammar M_Grammar;
    public final rascal.$Message M_Message;

    
    
    public IConstructor GEMPTY;
    public IConstructor G0;
    public IMap Lit1;
    public IConstructor GEXP;
    public IConstructor GEXPPRIO;
    public final io.usethesource.vallang.type.Type $T4;	/*astr()*/
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
    public final io.usethesource.vallang.type.Type $T5;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T0;	/*alist(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T1;	/*alist(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/

    public $TestGrammars(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $TestGrammars(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($TestGrammars_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.lang.rascal.grammar.tests.$TestGrammars.class, this);
       
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
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/lang/rascal/grammar/tests/$TestGrammars.constants", 37, "0216de9aa14f535c59b1e3472ab6f119");
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
       $T4 = $TF.stringType();
       $T2 = $TF.valueType();
       $T3 = $TF.parameterType("T", $T2);
       $T5 = $TF.parameterType("T", ADT_Tree);
       $T0 = $TF.listType(ADT_Symbol);
       $T1 = $TF.listType($T3);
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T5 });
    
       GEMPTY = ((IConstructor)$constants.get(1)/*grammar({sort("S")},())*/);
       G0 = ((IConstructor)($RVF.constructor(M_Grammar.Grammar_grammar_set_Symbol_map_Symbol_Production, new IValue[]{((ISet)$constants.get(2)/*{sort("S")}*/), ((IMap)($buildMap(((IConstructor)$constants.get(3)/*sort("S")*/), ((IConstructor)(M_Type.choice(((IConstructor)$constants.get(3)/*sort("S")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(3)/*sort("S")*/), ((IList)$constants.get(4)/*[lit("0")]*/)))))))))), ((IConstructor)$constants.get(5)/*lit("0")*/), M_Type.choice(((IConstructor)$constants.get(5)/*lit("0")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(5)/*lit("0")*/), ((IList)$constants.get(6)/*[\char-class([range(48,48)])]*/)))))))))))})));
       Lit1 = ((IMap)($buildMap(((IConstructor)$constants.get(7)/*lit("*")*/), ((IConstructor)(M_Type.choice(((IConstructor)$constants.get(7)/*lit("*")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(7)/*lit("*")*/), ((IList)$constants.get(8)/*[\char-class([range(42,42)])]*/)))))))))), ((IConstructor)$constants.get(9)/*lit("+")*/), M_Type.choice(((IConstructor)$constants.get(9)/*lit("+")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(9)/*lit("+")*/), ((IList)$constants.get(10)/*[\char-class([range(43,43)])]*/)))))))), ((IConstructor)$constants.get(5)/*lit("0")*/), M_Type.choice(((IConstructor)$constants.get(5)/*lit("0")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(5)/*lit("0")*/), ((IList)$constants.get(6)/*[\char-class([range(48,48)])]*/)))))))), ((IConstructor)$constants.get(11)/*lit("1")*/), M_Type.choice(((IConstructor)$constants.get(11)/*lit("1")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(11)/*lit("1")*/), ((IList)$constants.get(12)/*[\char-class([range(49,49)])]*/)))))))))));
       GEXP = ((IConstructor)($RVF.constructor(M_Grammar.Grammar_grammar_set_Symbol_map_Symbol_Production, new IValue[]{((ISet)$constants.get(13)/*{sort("E")}*/), ((IMap)($amap_add_amap(((IMap)($buildMap(((IConstructor)$constants.get(14)/*sort("E")*/), ((IConstructor)(M_Type.choice(((IConstructor)$constants.get(14)/*sort("E")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(14)/*sort("E")*/), ((IList)$constants.get(15)/*[sort("E"),lit("*"),sort("B")]*/)))), $me.pr(((IConstructor)$constants.get(14)/*sort("E")*/), ((IList)$constants.get(16)/*[sort("E"),lit("+"),sort("B")]*/)), $me.pr(((IConstructor)$constants.get(14)/*sort("E")*/), ((IList)$constants.get(17)/*[sort("B")]*/)))))))), ((IConstructor)$constants.get(18)/*sort("B")*/), M_Type.choice(((IConstructor)$constants.get(18)/*sort("B")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(18)/*sort("B")*/), ((IList)$constants.get(4)/*[lit("0")]*/)))), $me.pr(((IConstructor)$constants.get(18)/*sort("B")*/), ((IList)$constants.get(19)/*[lit("1")]*/))))))))),((IMap)Lit1))))})));
       GEXPPRIO = ((IConstructor)($RVF.constructor(M_Grammar.Grammar_grammar_set_Symbol_map_Symbol_Production, new IValue[]{((ISet)$constants.get(13)/*{sort("E")}*/), ((IMap)($buildMap(((IConstructor)$constants.get(14)/*sort("E")*/), ((IConstructor)(M_Type.choice(((IConstructor)$constants.get(14)/*sort("E")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(14)/*sort("E")*/), ((IList)$constants.get(20)/*[sort("T"),sort("E1")]*/)))))))))), ((IConstructor)$constants.get(21)/*sort("E1")*/), M_Type.choice(((IConstructor)$constants.get(21)/*sort("E1")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(21)/*sort("E1")*/), ((IList)$constants.get(22)/*[lit("+"),sort("T"),sort("E1")]*/)))), $me.pr(((IConstructor)$constants.get(21)/*sort("E1")*/), ((IList)$constants.get(23)/*[]*/)))))), ((IConstructor)$constants.get(24)/*sort("T")*/), M_Type.choice(((IConstructor)$constants.get(24)/*sort("T")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(24)/*sort("T")*/), ((IList)$constants.get(25)/*[sort("F"),sort("T1")]*/)))))))), ((IConstructor)$constants.get(26)/*sort("T1")*/), M_Type.choice(((IConstructor)$constants.get(26)/*sort("T1")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(27)/*sort("F")*/), ((IList)$constants.get(28)/*[lit("*"),sort("F"),sort("T1")]*/)))), $me.pr(((IConstructor)$constants.get(26)/*sort("T1")*/), ((IList)$constants.get(23)/*[]*/)))))), ((IConstructor)$constants.get(27)/*sort("F")*/), M_Type.choice(((IConstructor)$constants.get(27)/*sort("F")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(27)/*sort("F")*/), ((IList)$constants.get(29)/*[lit("("),sort("E"),lit(")")]*/)))), $me.pr(((IConstructor)$constants.get(27)/*sort("F")*/), ((IList)$constants.get(30)/*[lit("id")]*/)))))), ((IConstructor)$constants.get(9)/*lit("+")*/), M_Type.choice(((IConstructor)$constants.get(9)/*lit("+")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(9)/*lit("+")*/), ((IList)$constants.get(10)/*[\char-class([range(43,43)])]*/)))))))), ((IConstructor)$constants.get(7)/*lit("*")*/), M_Type.choice(((IConstructor)$constants.get(7)/*lit("*")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(7)/*lit("*")*/), ((IList)$constants.get(8)/*[\char-class([range(42,42)])]*/)))))))), ((IConstructor)$constants.get(31)/*lit("(")*/), M_Type.choice(((IConstructor)$constants.get(31)/*lit("(")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(31)/*lit("(")*/), ((IList)$constants.get(32)/*[\char-class([range(40,40)])]*/)))))))), ((IConstructor)$constants.get(33)/*lit(")")*/), M_Type.choice(((IConstructor)$constants.get(33)/*lit(")")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(33)/*lit(")")*/), ((IList)$constants.get(34)/*[\char-class([range(41,41)])]*/)))))))), ((IConstructor)$constants.get(35)/*lit("id")*/), M_Type.choice(((IConstructor)$constants.get(35)/*lit("id")*/), ((ISet)($RVF.set(((IConstructor)($me.pr(((IConstructor)$constants.get(35)/*lit("id")*/), ((IList)$constants.get(36)/*[\char-class([range(105,105)]),\char-class([range(100,100)])]*/)))))))))))})));
    
       
    }
    public IConstructor choice(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_Type.choice($P0, $P1);
    }
    public IConstructor pr(IValue $P0, IValue $P1){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type,$T0)){
         $result = (IConstructor)lang_rascal_grammar_tests_TestGrammars_pr$303e719a3feee0c1((IConstructor) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IConstructor grammar(IValue $P0){ // Generated by Resolver
       return (IConstructor) M_Grammar.grammar($P0);
    }
    public IConstructor grammar(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_Grammar.grammar($P0, $P1);
    }
    public IValue sort(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         $result = (IValue)M_List.List_sort$1fe4426c8c8039da((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T4)){
         return $RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{(IString) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList sort(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.sort($P0, $P1);
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/TestGrammars.rsc|(137,82,<8,0>,<10,1>) 
    public IConstructor lang_rascal_grammar_tests_TestGrammars_pr$303e719a3feee0c1(IConstructor rhs_0, IList lhs_1){ 
        
        
        return ((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)rhs_0), ((IList)lhs_1), ((ISet)$constants.get(0)/*{}*/)})));
    
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `lang::rascal::grammar::tests::TestGrammars`");
    }
}