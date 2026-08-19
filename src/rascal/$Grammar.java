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
public class $Grammar 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.$ParseTree_$I,
    	rascal.$Type_$I,
    	rascal.$List_$I,
    	rascal.$Grammar_$I,
    	rascal.$Message_$I {

    private final $Grammar_$I $me;
    private final IList $constants;
    
    
    public final rascal.$Message M_Message;
    public final rascal.$ParseTree M_ParseTree;
    public final rascal.$Type M_Type;
    public final rascal.$List M_List;

    
    
    public final io.usethesource.vallang.type.Type $T7;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T14;	/*aint(alabel="index")*/
    public final io.usethesource.vallang.type.Type $T10;	/*aparameter("U",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T12;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T15;	/*astr(alabel="main")*/
    public final io.usethesource.vallang.type.Type $T8;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T16;	/*astr(alabel="name")*/
    public final io.usethesource.vallang.type.Type $T18;	/*aset(astr(),alabel="imports")*/
    public final io.usethesource.vallang.type.Type ADT_Item;	/*aadt("Item",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarModule;	/*aadt("GrammarModule",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T19;	/*aset(astr(),alabel="extends")*/
    public final io.usethesource.vallang.type.Type GrammarModule_module_str_set_str_set_str_Grammar;	/*acons(aadt("GrammarModule",[],dataSyntax()),[astr(alabel="name"),aset(astr(),alabel="imports"),aset(astr(),alabel="extends"),aadt("Grammar",[],dataSyntax(),alabel="grammar")],[],alabel="module")*/
    public final io.usethesource.vallang.type.Type ADT_LocationType;	/*aadt("LocationType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T9;	/*areified(aparameter("U",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T13;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_Exception;	/*aadt("Exception",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T4;	/*amap(aadt("Symbol",[],dataSyntax(),alabel="sort"),aadt("Production",[],dataSyntax(),alabel="def"))*/
    public final io.usethesource.vallang.type.Type ADT_Grammar;	/*aadt("Grammar",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T20;	/*aset(aadt("Symbol",[],dataSyntax()),alabel="starts")*/
    public final io.usethesource.vallang.type.Type $T21;	/*amap(aadt("Symbol",[],dataSyntax(),alabel="sort"),aadt("Production",[],dataSyntax(),alabel="def"),alabel="rules")*/
    public final io.usethesource.vallang.type.Type Grammar_grammar_set_Symbol_map_Symbol_Production;	/*acons(aadt("Grammar",[],dataSyntax()),[aset(aadt("Symbol",[],dataSyntax()),alabel="starts"),amap(aadt("Symbol",[],dataSyntax(),alabel="sort"),aadt("Production",[],dataSyntax(),alabel="def"),alabel="rules")],[],alabel="grammar")*/
    public final io.usethesource.vallang.type.Type ADT_Associativity;	/*aadt("Associativity",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T11;	/*alist(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_GrammarDefinition;	/*aadt("GrammarDefinition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T23;	/*aset(astr())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T1;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=false)*/
    public final io.usethesource.vallang.type.Type $T0;	/*areified(aparameter("T",aadt("Tree",[],dataSyntax()),closed=false))*/
    public final io.usethesource.vallang.type.Type $T17;	/*amap(astr(alabel="name"),aadt("GrammarModule",[],dataSyntax(),alabel="mod"),alabel="modules")*/
    public final io.usethesource.vallang.type.Type GrammarDefinition_definition_str_map_str_GrammarModule;	/*acons(aadt("GrammarDefinition",[],dataSyntax()),[astr(alabel="main"),amap(astr(alabel="name"),aadt("GrammarModule",[],dataSyntax(),alabel="mod"),alabel="modules")],[],alabel="definition")*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeEvent;	/*aadt("LocationChangeEvent",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T2;	/*aset(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type $T5;	/*alist(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type Item_item_Production_int;	/*acons(aadt("Item",[],dataSyntax()),[aadt("Production",[],dataSyntax(),alabel="production"),aint(alabel="index")],[],alabel="item")*/
    public final io.usethesource.vallang.type.Type $T6;	/*areified(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T22;	/*alist(aadt("Production",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_CharRange;	/*aadt("CharRange",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T3;	/*aset(aadt("Production",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/

    public $Grammar(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Grammar(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Grammar_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.$Grammar.class, this);
        
        
       
       M_Message = mstore.extendModule(rascal.$Message.class, rex, rascal.$Message::new, $me);
       M_ParseTree = mstore.extendModule(rascal.$ParseTree.class, rex, rascal.$ParseTree::new, $me);
       M_Type = mstore.extendModule(rascal.$Type.class, rex, rascal.$Type::new, $me);
       M_List = mstore.extendModule(rascal.$List.class, rex, rascal.$List::new, $me);
                          
       
       $TS.importStore(M_Message.$TS);
       $TS.importStore(M_ParseTree.$TS);
       $TS.importStore(M_Type.$TS);
       $TS.importStore(M_List.$TS);
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/$Grammar.constants", 3, "b8f08e6a04df5bb64c3002b55ef54ea0");
       ADT_Item = $adt("Item");
       ADT_GrammarModule = $adt("GrammarModule");
       ADT_LocationType = $adt("LocationType");
       ADT_Tree = $adt("Tree");
       ADT_Exception = $adt("Exception");
       ADT_Grammar = $adt("Grammar");
       ADT_Symbol = $adt("Symbol");
       ADT_Associativity = $adt("Associativity");
       ADT_GrammarDefinition = $adt("GrammarDefinition");
       ADT_Attr = $adt("Attr");
       ADT_Production = $adt("Production");
       ADT_LocationChangeType = $adt("LocationChangeType");
       ADT_RuntimeException = $adt("RuntimeException");
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       ADT_CharRange = $adt("CharRange");
       ADT_Condition = $adt("Condition");
       ADT_Message = $adt("Message");
       ADT_IOCapability = $adt("IOCapability");
       $T7 = $TF.valueType();
       $T14 = $TF.integerType();
       $T10 = $TF.parameterType("U", $T7);
       $T12 = $TF.stringType();
       $T15 = $TF.stringType();
       $T8 = $TF.parameterType("T", $T7);
       $T16 = $TF.stringType();
       $T18 = $TF.setType($T12);
       $T19 = $TF.setType($T12);
       $T9 = $RTF.reifiedType($T10);
       $T13 = $TF.parameterType("T", ADT_Tree);
       $T4 = $TF.mapType(ADT_Symbol, "sort", ADT_Production, "def");
       $T20 = $TF.setType(ADT_Symbol);
       $T21 = $TF.mapType(ADT_Symbol, "sort", ADT_Production, "def");
       $T11 = $TF.listType($T8);
       $T23 = $TF.setType($T12);
       $T1 = $TF.parameterType("T", ADT_Tree);
       $T0 = $RTF.reifiedType($T1);
       $T17 = $TF.mapType($T16, "name", ADT_GrammarModule, "mod");
       $T2 = $TF.setType(ADT_Symbol);
       $T5 = $TF.listType(ADT_Symbol);
       $T6 = $RTF.reifiedType($T8);
       $T22 = $TF.listType(ADT_Production);
       $T3 = $TF.setType(ADT_Production);
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T13 });
       GrammarModule_module_str_set_str_set_str_Grammar = $TF.constructor($TS, ADT_GrammarModule, "module", $TF.stringType(), "name", $TF.setType($T12), "imports", $TF.setType($T12), "extends", ADT_Grammar, "grammar");
       Grammar_grammar_set_Symbol_map_Symbol_Production = $TF.constructor($TS, ADT_Grammar, "grammar", $TF.setType(ADT_Symbol), "starts", $TF.mapType(ADT_Symbol, "sort", ADT_Production, "def"), "rules");
       GrammarDefinition_definition_str_map_str_GrammarModule = $TF.constructor($TS, ADT_GrammarDefinition, "definition", $TF.stringType(), "main", $TF.mapType($T16, "name", ADT_GrammarModule, "mod"), "modules");
       Item_item_Production_int = $TF.constructor($TS, ADT_Item, "item", M_ParseTree.ADT_Production, "production", $TF.integerType(), "index");
    
       
       
    }
    public IList addLabels(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_Type.addLabels($P0, $P1);
    }
    public IBool sameType(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IBool) M_ParseTree.sameType($P0, $P1);
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
       return (TypedFunctionInstance2<IValue, IValue, IValue>) M_ParseTree.firstAmbiguityFinder($P0, $kwpActuals);
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
       return (IConstructor) M_ParseTree.priority($P0, $P1);
    }
    public IValue getFirstFrom(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.getFirstFrom($P0);
    }
    public IMap distribution(IValue $P0){ // Generated by Resolver
       return (IMap) M_List.distribution($P0);
    }
    public TypedFunctionInstance2<IValue, IValue, IValue> loadParser(IValue $P0, IValue $P1, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (TypedFunctionInstance2<IValue, IValue, IValue>) M_ParseTree.loadParser($P0, $P1, $kwpActuals);
    }
    public IString printSymbol(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IString) M_ParseTree.printSymbol($P0, $P1);
    }
    public IBool isNodeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNodeType($P0);
    }
    public IConstructor grammar(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IConstructor)Grammar_grammar$63f051bdb3ded668((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor grammar(IValue $P0, IValue $P1){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T2) && $isSubtypeOf($P1Type,$T3)){
         $result = (IConstructor)Grammar_grammar$6b8efb64875ca243((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T2) && $isSubtypeOf($P1Type,$T4)){
         return $RVF.constructor(Grammar_grammar_set_Symbol_map_Symbol_Production, new IValue[]{(ISet) $P0, (IMap) $P1});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
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
    public ISet dependencies(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, ADT_GrammarDefinition)){
         $result = (ISet)Grammar_dependencies$37f6dca76704bff7((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
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
    public IValue reducer(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IValue) M_List.reducer($P0, $P1, $P2);
    }
    public IString toString(IValue $P0){ // Generated by Resolver
       return (IString) M_List.toString($P0);
    }
    public ISet permutations(IValue $P0){ // Generated by Resolver
       return (ISet) M_List.permutations($P0);
    }
    public TypedFunctionInstance3<IValue, IValue, IValue, IValue> loadParsers(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (TypedFunctionInstance3<IValue, IValue, IValue, IValue>) M_ParseTree.loadParsers($P0, $kwpActuals);
    }
    public IBool isBoolType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isBoolType($P0);
    }
    public ISet imports(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, ADT_GrammarDefinition)){
         $result = (ISet)Grammar_imports$8df7b944feeb651c((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
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
       return (IValue) M_ParseTree.implode($P0, $P1);
    }
    public TypedFunctionInstance3<IValue, IValue, IValue, IValue> firstAmbiguityFinders(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (TypedFunctionInstance3<IValue, IValue, IValue, IValue>) M_ParseTree.firstAmbiguityFinders($P0, $kwpActuals);
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
       return (IConstructor) M_ParseTree.treeAt($P0, $P1, $P2);
    }
    public IMap toMapUnique(IValue $P0){ // Generated by Resolver
       return (IMap) M_List.toMapUnique($P0);
    }
    public IList index(IValue $P0){ // Generated by Resolver
       return (IList) M_List.index($P0);
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
    public IList slice(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IList) M_List.slice($P0, $P1, $P2);
    }
    public IConstructor compose(IValue $P0, IValue $P1){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, ADT_Grammar) && $isSubtypeOf($P1Type, ADT_Grammar)){
         $result = (IConstructor)Grammar_compose$e2ea649801d63847((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isSetType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isSetType($P0);
    }
    public void storeParsers(IValue $P0, IValue $P1){ // Generated by Resolver
        M_ParseTree.storeParsers($P0, $P1);
    }
    public IInteger lastIndexOf(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IInteger) M_List.lastIndexOf($P0, $P1);
    }
    public ITree parse(IValue $P0, IValue $P1, IValue $P2, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (ITree) M_ParseTree.parse($P0, $P1, $P2, $kwpActuals);
    }
    public ITree parse(IValue $P0, IValue $P1, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (ITree) M_ParseTree.parse($P0, $P1, $kwpActuals);
    }
    public TypedFunctionInstance3<IValue, IValue, IValue, IValue> parsers(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (TypedFunctionInstance3<IValue, IValue, IValue, IValue>) M_ParseTree.parsers($P0, $kwpActuals);
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
       return (IBool) M_ParseTree.isNonTerminalType($P0);
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
    public ISet $extends(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, ADT_GrammarDefinition)){
         $result = (ISet)Grammar_extends$53b862904270c644((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
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
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$162da85a0f5a9f0d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 26576112:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$258479665eae36af((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$0462d461bde80a82((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1725888:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$f6957636a33615ae((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$b674428cffef84bc((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 97904160:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$98167e340333c9a5((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$4fe5b133e2ee1de9((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 28290288:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_ParseTree.ParseTree_subtype$384d8d76f0c7a053((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 910096:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$ca59d9bf5276e15d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$e77633ea9a4ac6a5((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 902344:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$21c6b8b775030d1d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$98e19b11a09faf67((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$0862159b9fa78cf9((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 26641768:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$ab363c241c416a71((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$4de9a977591be6e5((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 778304:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$23f59dc1171dc69d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 100948096:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$ddf53e134f4d5416((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 112955840:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$bc5943e83a6df899((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$282ad33dd55efdcc((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1542928:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$5f5250bbf1aff423((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$15cedff9916fdbee((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 885800512:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$44422dfea95218a8((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
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
       if($isSubtypeOf($P0Type,$T5) && $isSubtypeOf($P1Type,$T5)){
         $result = (IBool)M_Type.Type_subtype$e6962df5576407da((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T6) && $isSubtypeOf($P1Type,$T9)){
         $result = (IBool)M_Type.Type_subtype$7b9c005ac35dd586((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
         $result = (IBool)M_Type.Type_subtype$06d2c71d010480ef((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T5) && $isSubtypeOf($P1Type,$T5)){
         $result = (IBool)M_Type.Type_subtype$812a7f34ff841fdb((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IConstructor associativity(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IConstructor) M_ParseTree.associativity($P0, $P1, $P2);
    }
    public IValue getOneFrom(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.getOneFrom($P0);
    }
    public IString unparse(IValue $P0){ // Generated by Resolver
       return (IString) M_ParseTree.unparse($P0);
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
       return (ITree) M_ParseTree.firstAmbiguity($P0, $P1);
    }
    public ISet toRel(IValue $P0){ // Generated by Resolver
       return (ISet) M_List.toRel($P0);
    }
    public IValue sort(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T11)){
         $result = (IValue)M_List.List_sort$1fe4426c8c8039da((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T12)){
         return $RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{(IString) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList sort(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.sort($P0, $P1);
    }
    public TypedFunctionInstance2<IValue, IValue, IValue> parser(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (TypedFunctionInstance2<IValue, IValue, IValue>) M_ParseTree.parser($P0, $kwpActuals);
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

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Grammar.rsc|(1136,476,<34,0>,<53,1>) 
    public IConstructor Grammar_grammar$6b8efb64875ca243(ISet starts_0, ISet prods_1){ 
        
        
        IMap rules_2 = ((IMap)$constants.get(0)/*()*/);
        /*muExists*/FOR0: 
            do {
                FOR0_GEN1250:
                for(IValue $elem4_for : ((ISet)prods_1)){
                    IConstructor $elem4 = (IConstructor) $elem4_for;
                    IConstructor p_3 = null;
                    IConstructor t_4 = null;
                    if($is(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($elem4)), "def"))))),((IString)$constants.get(1)/*"label"*/))){
                       t_4 = ((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($elem4)), "def"))))), "symbol")))));
                    
                    } else {
                       t_4 = ((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($elem4)), "def")))));
                    
                    }if((((IBool)($RVF.bool(((IMap)rules_2).containsKey(((IConstructor)t_4)))))).getValue()){
                       final IConstructor $subject_val1 = ((IConstructor)($amap_subscript(((IMap)rules_2),((IConstructor)t_4))));
                       if($has_type_and_arity($subject_val1, M_Type.Production_choice_Symbol_set_Production, 2)){
                          IValue $arg0_3 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val1)),0));
                          if($isComparable($arg0_3.getType(), $T7)){
                             IValue $arg1_2 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val1)),1));
                             if($isComparable($arg1_2.getType(), $T3)){
                                ISet existing_5 = null;
                                rules_2 = ((IMap)($amap_update(t_4,$me.choice(((IConstructor)t_4), ((ISet)($aset_add_elm(((ISet)($arg1_2)),((IConstructor)($elem4)))))), ((IMap)(((IMap)rules_2))))));
                             
                             } else {
                                rules_2 = ((IMap)($amap_update(t_4,$me.choice(((IConstructor)t_4), ((ISet)($RVF.set(((IConstructor)($elem4)), $amap_subscript(((IMap)rules_2),((IConstructor)t_4)))))), ((IMap)(((IMap)rules_2))))));
                             
                             }
                          } else {
                             rules_2 = ((IMap)($amap_update(t_4,$me.choice(((IConstructor)t_4), ((ISet)($RVF.set(((IConstructor)($elem4)), $amap_subscript(((IMap)rules_2),((IConstructor)t_4)))))), ((IMap)(((IMap)rules_2))))));
                          
                          }
                       } else {
                          rules_2 = ((IMap)($amap_update(t_4,$me.choice(((IConstructor)t_4), ((ISet)($RVF.set(((IConstructor)($elem4)), $amap_subscript(((IMap)rules_2),((IConstructor)t_4)))))), ((IMap)(((IMap)rules_2))))));
                       
                       }
                    } else {
                       rules_2 = ((IMap)($amap_update(t_4,$me.choice(((IConstructor)t_4), ((ISet)($RVF.set(((IConstructor)($elem4)))))), ((IMap)(((IMap)rules_2))))));
                    
                    }
                }
                continue FOR0;
                            
            } while(false);
        /* void:  muCon([]) */return ((IConstructor)($RVF.constructor(Grammar_grammar_set_Symbol_map_Symbol_Production, new IValue[]{((ISet)starts_0), ((IMap)rules_2)})));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Grammar.rsc|(1626,80,<55,0>,<56,42>) 
    public IConstructor Grammar_grammar$63f051bdb3ded668(IConstructor sym_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(sym_0.getType(), $typeBindings)){
           final IConstructor $result5 = ((IConstructor)($RVF.constructor(Grammar_grammar_set_Symbol_map_Symbol_Production, new IValue[]{((ISet)($RVF.set(((IConstructor)(((IConstructor)($areified_get_field(sym_0, "symbol")))))))), ((IMap)(((IMap)($areified_get_field(sym_0, "definitions")))))})));
           if(ADT_Grammar.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result5.getType(),ADT_Grammar)){
              return ((IConstructor)($result5));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Grammar.rsc|(1842,765,<64,0>,<88,1>) 
    public IConstructor Grammar_compose$e2ea649801d63847(IConstructor g1_0, IConstructor g2_1){ 
        
        
        /*muExists*/FOR3: 
            do {
                FOR3_GEN2090:
                for(IValue $elem6_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)g2_1), "rules")))))){
                    IConstructor $elem6 = (IConstructor) $elem6_for;
                    IConstructor s_2 = null;
                    if($is_defined_value($guarded_map_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)g1_0), "rules"))))),((IConstructor)($elem6))))){
                       g1_0 = ((IConstructor)(((IConstructor)($aadt_field_update("rules", $amap_update($elem6,$me.choice(((IConstructor)($elem6)), ((ISet)($RVF.set(((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)g1_0), "rules"))))),((IConstructor)($elem6))))), $amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)g2_1), "rules"))))),((IConstructor)($elem6))))))), ((IMap)(((IMap)($aadt_get_field(((IConstructor)g1_0), "rules")))))), ((IConstructor)g1_0))))));
                    
                    } else {
                       g1_0 = ((IConstructor)(((IConstructor)($aadt_field_update("rules", $amap_update($elem6,$amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)g2_1), "rules"))))),((IConstructor)($elem6))), ((IMap)(((IMap)($aadt_get_field(((IConstructor)g1_0), "rules")))))), ((IConstructor)g1_0))))));
                    
                    }
                }
                continue FOR3;
                            
            } while(false);
        /* void:  muCon([]) */g1_0 = ((IConstructor)(((IConstructor)($aadt_field_update("starts", $aset_add_aset(((ISet)(((ISet)($aadt_get_field(((IConstructor)g1_0), "starts"))))),((ISet)(((ISet)($aadt_get_field(((IConstructor)g2_1), "starts")))))), ((IConstructor)g1_0))))));
        IMap reduced_rules_3 = ((IMap)$constants.get(0)/*()*/);
        /*muExists*/FOR5: 
            do {
                FOR5_GEN2283:
                for(IValue $elem18_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)g1_0), "rules")))))){
                    IConstructor $elem18 = (IConstructor) $elem18_for;
                    IConstructor s_4 = null;
                    IConstructor c_5 = ((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)g1_0), "rules"))))),((IConstructor)($elem18)))));
                    if($is(((IConstructor)c_5),((IString)$constants.get(2)/*"choice"*/))){
                       final ISetWriter $setwriter7 = (ISetWriter)$RVF.setWriter();
                       ;
                       /*muExists*/$SCOMP8_GEN2383_CONS_priority: 
                           do {
                               $SCOMP8_GEN2383:
                               for(IValue $elem9_for : ((ISet)(((ISet)($aadt_get_field(((IConstructor)c_5), "alternatives")))))){
                                   IConstructor $elem9 = (IConstructor) $elem9_for;
                                   if($has_type_and_arity($elem9, M_ParseTree.Production_priority_Symbol_list_Production, 2)){
                                      IValue $arg0_11 = (IValue)($aadt_subscript_int(((IConstructor)($elem9)),0));
                                      if($isComparable($arg0_11.getType(), $T7)){
                                         IValue $arg1_10 = (IValue)($aadt_subscript_int(((IConstructor)($elem9)),1));
                                         if($isComparable($arg1_10.getType(), $T22)){
                                            IList choices_6 = null;
                                            $setwriter_splice($setwriter7,$arg1_10);
                                         
                                         } else {
                                            continue $SCOMP8_GEN2383;
                                         }
                                      } else {
                                         continue $SCOMP8_GEN2383;
                                      }
                                   } else {
                                      continue $SCOMP8_GEN2383;
                                   }
                               }
                               
                                           
                           } while(false);
                       final ISetWriter $setwriter12 = (ISetWriter)$RVF.setWriter();
                       ;
                       /*muExists*/$SCOMP13_GEN2458_CONS_associativity: 
                           do {
                               $SCOMP13_GEN2458:
                               for(IValue $elem14_for : ((ISet)(((ISet)($aadt_get_field(((IConstructor)c_5), "alternatives")))))){
                                   IConstructor $elem14 = (IConstructor) $elem14_for;
                                   if($has_type_and_arity($elem14, M_ParseTree.Production_associativity_Symbol_Associativity_set_Production, 3)){
                                      IValue $arg0_17 = (IValue)($aadt_subscript_int(((IConstructor)($elem14)),0));
                                      if($isComparable($arg0_17.getType(), $T7)){
                                         IValue $arg1_16 = (IValue)($aadt_subscript_int(((IConstructor)($elem14)),1));
                                         if($isComparable($arg1_16.getType(), $T7)){
                                            IValue $arg2_15 = (IValue)($aadt_subscript_int(((IConstructor)($elem14)),2));
                                            if($isComparable($arg2_15.getType(), $T3)){
                                               ISet alts_7 = null;
                                               $setwriter_splice($setwriter12,$arg2_15);
                                            
                                            } else {
                                               continue $SCOMP13_GEN2458;
                                            }
                                         } else {
                                            continue $SCOMP13_GEN2458;
                                         }
                                      } else {
                                         continue $SCOMP13_GEN2458;
                                      }
                                   } else {
                                      continue $SCOMP13_GEN2458;
                                   }
                               }
                               
                                           
                           } while(false);
                       c_5 = ((IConstructor)(((IConstructor)($aadt_field_update("alternatives", ((ISet)(((ISet)($aadt_get_field(((IConstructor)c_5), "alternatives"))))).subtract(((ISet)($aset_add_aset(((ISet)($setwriter7.done())),((ISet)($setwriter12.done())))))), ((IConstructor)c_5))))));
                    
                    }
                    reduced_rules_3 = ((IMap)($amap_update($elem18,c_5, ((IMap)(((IMap)reduced_rules_3))))));
                
                }
                continue FOR5;
                            
            } while(false);
        /* void:  muCon([]) */return ((IConstructor)($RVF.constructor(Grammar_grammar_set_Symbol_map_Symbol_Production, new IValue[]{((ISet)(((ISet)($aadt_get_field(((IConstructor)g1_0), "starts"))))), ((IMap)reduced_rules_3)})));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Grammar.rsc|(3305,354,<110,0>,<116,1>) 
    public ISet Grammar_extends$53b862904270c644(IConstructor def_0){ 
        
        
        final ISetWriter $setwriter19 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP20_GEN3584:
        for(IValue $elem27_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)def_0), "modules")))))){
            IString $elem27 = (IString) $elem27_for;
            IString m_1 = null;
            final IConstructor $subject_val22 = ((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_0), "modules"))))),((IString)($elem27)))));
            if($has_type_and_arity($subject_val22, GrammarModule_module_str_set_str_set_str_Grammar, 4)){
               IValue $arg0_26 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val22)),0));
               if($isComparable($arg0_26.getType(), $T7)){
                  IValue $arg1_25 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val22)),1));
                  if($isComparable($arg1_25.getType(), $T7)){
                     IValue $arg2_24 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val22)),2));
                     if($isComparable($arg2_24.getType(), $T23)){
                        ISet exts_2 = null;
                        IValue $arg3_23 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val22)),3));
                        if($isComparable($arg3_23.getType(), $T7)){
                           $SCOMP20_GEN3584_GEN3645:
                           for(IValue $elem21_for : ((ISet)($arg2_24))){
                               IString $elem21 = (IString) $elem21_for;
                               IString e_3 = null;
                               $setwriter19.insert($RVF.tuple(((IString)($elem27)), ((IString)($elem21))));
                           
                           }
                           continue $SCOMP20_GEN3584;
                                       
                        } else {
                           continue $SCOMP20_GEN3584;
                        }
                     } else {
                        continue $SCOMP20_GEN3584;
                     }
                  } else {
                     continue $SCOMP20_GEN3584;
                  }
               } else {
                  continue $SCOMP20_GEN3584;
               }
            } else {
               continue $SCOMP20_GEN3584;
            }
        }
        
                    return ((ISet)(((ISet)($setwriter19.done())).asRelation().closure()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Grammar.rsc|(3661,242,<118,0>,<121,1>) 
    public ISet Grammar_imports$8df7b944feeb651c(IConstructor def_0){ 
        
        
        final ISetWriter $setwriter28 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP29_GEN3829:
        for(IValue $elem36_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)def_0), "modules")))))){
            IString $elem36 = (IString) $elem36_for;
            IString m_1 = null;
            final IConstructor $subject_val31 = ((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_0), "modules"))))),((IString)($elem36)))));
            if($has_type_and_arity($subject_val31, GrammarModule_module_str_set_str_set_str_Grammar, 4)){
               IValue $arg0_35 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val31)),0));
               if($isComparable($arg0_35.getType(), $T7)){
                  IValue $arg1_34 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val31)),1));
                  if($isComparable($arg1_34.getType(), $T23)){
                     ISet imps_2 = null;
                     IValue $arg2_33 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val31)),2));
                     if($isComparable($arg2_33.getType(), $T7)){
                        IValue $arg3_32 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val31)),3));
                        if($isComparable($arg3_32.getType(), $T7)){
                           $SCOMP29_GEN3829_GEN3890:
                           for(IValue $elem30_for : ((ISet)($arg1_34))){
                               IString $elem30 = (IString) $elem30_for;
                               IString i_3 = null;
                               $setwriter28.insert($RVF.tuple(((IString)($elem36)), ((IString)($elem30))));
                           
                           }
                           continue $SCOMP29_GEN3829;
                                       
                        } else {
                           continue $SCOMP29_GEN3829;
                        }
                     } else {
                        continue $SCOMP29_GEN3829;
                     }
                  } else {
                     continue $SCOMP29_GEN3829;
                  }
               } else {
                  continue $SCOMP29_GEN3829;
               }
            } else {
               continue $SCOMP29_GEN3829;
            }
        }
        
                    return ((ISet)($setwriter28.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Grammar.rsc|(3905,683,<123,0>,<137,1>) 
    public ISet Grammar_dependencies$37f6dca76704bff7(IConstructor def_0){ 
        
        
        ISet imps_1 = ((ISet)($me.imports(((IConstructor)def_0))));
        ISet exts_2 = ((ISet)($me.$extends(((IConstructor)def_0))));
        return ((ISet)($aset_add_aset(((ISet)($aset_add_aset(((ISet)imps_1),((ISet)exts_2)))),((ISet)(((ISet)imps_1).asRelation().compose(((ISet)exts_2).asRelation()))))));
    
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `Grammar`");
    }
}