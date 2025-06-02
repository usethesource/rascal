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
public class $String 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.$Exception_$I,
    	rascal.$String_$I {

    private final $String_$I $me;
    private final IList $constants;
    
    
    public final rascal.$Message M_Message;
    public final rascal.$Exception M_Exception;
    public final rascal.$ParseTree M_ParseTree;
    public final rascal.$Type M_Type;
    public final rascal.$List M_List;

    
    final org.rascalmpl.library.Prelude $Prelude; // TODO: asBaseClassName will generate name collisions if there are more of the same name in different packages

    
    public IString DEFAULT_CHARSET;
    public final io.usethesource.vallang.type.Type $T15;	/*aloc(alabel="a")*/
    public final io.usethesource.vallang.type.Type $T9;	/*aloc()*/
    public final io.usethesource.vallang.type.Type $T0;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T5;	/*aint()*/
    public final io.usethesource.vallang.type.Type $T2;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T16;	/*aloc(alabel="b")*/
    public final io.usethesource.vallang.type.Type $T12;	/*aparameter("CharClass",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T3;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T7;	/*aparameter("CharClass",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T14;	/*abool()*/
    public final io.usethesource.vallang.type.Type ADT_LocationType;	/*aadt("LocationType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T13;	/*afunc(abool(),[aloc(alabel="a"),aloc(alabel="b")],[],returnsViaAllPath=true)*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Exception;	/*aadt("Exception",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T4;	/*amap(astr(),astr())*/
    public final io.usethesource.vallang.type.Type $T8;	/*alist(aint())*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T10;	/*amap(aloc(),astr())*/
    public final io.usethesource.vallang.type.Type $T11;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T6;	/*areified(aparameter("CharClass",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Associativity;	/*aadt("Associativity",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CharRange;	/*aadt("CharRange",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T1;	/*alist(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeEvent;	/*aadt("LocationChangeEvent",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/

    public $String(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $String(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($String_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.$String.class, this);
       
       mstore.importModule(rascal.$Message.class, rex, rascal.$Message::new);
       mstore.importModule(rascal.$ParseTree.class, rex, rascal.$ParseTree::new);
       mstore.importModule(rascal.$Type.class, rex, rascal.$Type::new);
       mstore.importModule(rascal.$List.class, rex, rascal.$List::new); 
       
       M_Message = mstore.getModule(rascal.$Message.class);
       M_ParseTree = mstore.getModule(rascal.$ParseTree.class);
       M_Type = mstore.getModule(rascal.$Type.class);
       M_List = mstore.getModule(rascal.$List.class); 
       
       M_Exception = mstore.extendModule(rascal.$Exception.class, rex, rascal.$Exception::new, $me);
                          
       
       $TS.importStore(M_Message.$TS);
       $TS.importStore(M_Exception.$TS);
       $TS.importStore(M_ParseTree.$TS);
       $TS.importStore(M_Type.$TS);
       $TS.importStore(M_List.$TS);
       
       $Prelude = $initLibrary("org.rascalmpl.library.Prelude"); 
    
       $constants = readBinaryConstantsFile(this.getClass(), "rascal//$String.constants", 17, "806c95ef22aa243d4485be9aa38ae20f");
       ADT_LocationType = $adt("LocationType");
       ADT_IOCapability = $adt("IOCapability");
       ADT_Tree = $adt("Tree");
       ADT_Symbol = $adt("Symbol");
       ADT_Exception = $adt("Exception");
       ADT_RuntimeException = $adt("RuntimeException");
       ADT_Attr = $adt("Attr");
       ADT_LocationChangeType = $adt("LocationChangeType");
       ADT_Production = $adt("Production");
       ADT_Associativity = $adt("Associativity");
       ADT_CharRange = $adt("CharRange");
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       ADT_Message = $adt("Message");
       ADT_Condition = $adt("Condition");
       $T15 = $TF.sourceLocationType();
       $T9 = $TF.sourceLocationType();
       $T0 = $TF.stringType();
       $T5 = $TF.integerType();
       $T2 = $TF.valueType();
       $T16 = $TF.sourceLocationType();
       $T12 = $TF.parameterType("CharClass", $T2);
       $T3 = $TF.parameterType("T", $T2);
       $T7 = $TF.parameterType("CharClass", $T2);
       $T14 = $TF.boolType();
       $T13 = $TF.functionType($T14, $TF.tupleType($T15, "a", $T16, "b"), $TF.tupleEmpty());
       $T4 = $TF.mapType($T0,$T0);
       $T8 = $TF.listType($T5);
       $T10 = $TF.mapType($T9,$T0);
       $T11 = $TF.parameterType("T", ADT_Tree);
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T11 });
       $T6 = $RTF.reifiedType($T7);
       $T1 = $TF.listType($T3);
    
       DEFAULT_CHARSET = ((IString)$constants.get(16)/*"UTF-8"*/);
    
       
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
    public IInteger size(IValue $P0){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IInteger)String_size$4611676944e933d5((IString) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T1)){
         $result = (IInteger)M_List.List_size$ba7443328d8b4a27((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
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
    public IString escape(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T4)){
         $result = (IString)String_escape$7fa23c31a411d9dc((IString) $P0, (IMap) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isStrType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isStrType($P0);
    }
    public IString trim(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)String_trim$8faad6373d3f1827((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isRealType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRealType($P0);
    }
    public IString arbString(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T5)){
         $result = (IString)String_arbString$bfea25a11df8dffa((IInteger) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString squeeze(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IString)String_squeeze$d1d8b81dff6f8420((IString) $P0, (IString) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T6)){
         $result = (IString)String_squeeze$6e2735f43e585785((IString) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isListType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isListType($P0);
    }
    public IString deescape(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)String_deescape$463da4cc9241a765((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString reverse(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)String_reverse$5c483498523ba125((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isTypeVar(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isTypeVar($P0);
    }
    public IBool isEmpty(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IBool)String_isEmpty$0288736cc2315249((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor priority(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_ParseTree.priority($P0, $P1);
    }
    public IString uncapitalize(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)String_uncapitalize$5a860f2aab2710e3((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isNodeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNodeType($P0);
    }
    public IString stringChar(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T5)){
         $result = (IString)String_stringChar$29485394ec702339((IInteger) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString left(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5) && $isSubtypeOf($P2Type,$T0)){
         $result = (IString)String_left$b5982a84598a5a87((IString) $P0, (IInteger) $P1, (IString) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IString left(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5)){
         $result = (IString)String_left$ea4ca9949ebc690b((IString) $P0, (IInteger) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IList findAll(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IList)String_findAll$bd53b9d00cf0465e((IString) $P0, (IString) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isReifiedType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isReifiedType($P0);
    }
    public IInteger charAt(IValue $P0, IValue $P1){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5)){
         $result = (IInteger)String_charAt$c43d01db8f754016((IString) $P0, (IInteger) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isRelType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRelType($P0);
    }
    public IString wrap(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5)){
         $result = (IString)String_wrap$c21d49e5b8cae059((IString) $P0, (IInteger) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IString replaceLast(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0) && $isSubtypeOf($P2Type,$T0)){
         $result = (IString)String_replaceLast$e73336ea981874fc((IString) $P0, (IString) $P1, (IString) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IBool isConstructorType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isConstructorType($P0);
    }
    public IString toBase64(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)String_toBase64$e60dafecaa2ee56e((IString) $P0, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isListRelType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isListRelType($P0);
    }
    public IList addParamLabels(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_Type.addParamLabels($P0, $P1);
    }
    public IString toBase32(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)String_toBase32$49dc832874fb11d9((IString) $P0, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isMapType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isMapType($P0);
    }
    public IString stringChars(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T8)){
         $result = (IString)String_stringChars$990634670d003421((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString capitalize(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)String_capitalize$241c1cc358b38921((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString replaceAll(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0) && $isSubtypeOf($P2Type,$T0)){
         $result = (IString)String_replaceAll$40dfb6273a17a7ab((IString) $P0, (IString) $P1, (IString) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IBool isBoolType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isBoolType($P0);
    }
    public IInteger findLast(IValue $P0, IValue $P1){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IInteger)String_findLast$e5f4379f5988beac((IString) $P0, (IString) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IList tail(IValue $P0){ // Generated by Resolver
       return (IList) M_List.tail($P0);
    }
    public IBool isLocType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isLocType($P0);
    }
    public ISourceLocation toLocation(IValue $P0){ // Generated by Resolver
       ISourceLocation $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ISourceLocation)String_toLocation$347a629c083bfdf1((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString String_substitute$a969725dc1004f33_subst1(IValue $P0, IValue $P1, IValue $P2, ValueRef<IInteger> shift_2){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T9) && $isSubtypeOf($P2Type,$T0)){
         $result = (IString)String_subst1$0127b165869021ec((IString) $P0, (ISourceLocation) $P1, (IString) $P2, shift_2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public ITuple headTail(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.headTail($P0);
    }
    public IString substring(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5) && $isSubtypeOf($P2Type,$T5)){
         $result = (IString)String_substring$77404656fd06e485((IString) $P0, (IInteger) $P1, (IInteger) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IString substring(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5)){
         $result = (IString)String_substring$1516e7be77c7268c((IString) $P0, (IInteger) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IConstructor treeAt(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IConstructor) M_ParseTree.treeAt($P0, $P1, $P2);
    }
    public IBool isSetType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isSetType($P0);
    }
    public IInteger toInt(IValue $P0){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IInteger)String_toInt$8c1572a980336ee2((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IInteger toInt(IValue $P0, IValue $P1){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5)){
         $result = (IInteger)String_toInt$f408c9e5b956da4b((IString) $P0, (IInteger) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isRatType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRatType($P0);
    }
    public IString center(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5) && $isSubtypeOf($P2Type,$T0)){
         $result = (IString)String_center$e854b35d975f4ec8((IString) $P0, (IInteger) $P1, (IString) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IString center(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5)){
         $result = (IString)String_center$244f457ebb47db3f((IString) $P0, (IInteger) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IString toLowerCase(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)String_toLowerCase$aa30e5ee71d42d20((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isNumType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNumType($P0);
    }
    public IInteger findFirst(IValue $P0, IValue $P1){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IInteger)String_findFirst$d5769aac4911388a((IString) $P0, (IString) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool startsWith(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IBool)String_startsWith$cac4396a9880251b((IString) $P0, (IString) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isValidCharacter(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T5)){
         $result = (IBool)String_isValidCharacter$e3998b941251ff24((IInteger) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString toUpperCase(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)String_toUpperCase$52fd8e341a749153((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString format(IValue $P0, IValue $P1, IValue $P2, IValue $P3){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       Type $P3Type = $P3.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0) && $isSubtypeOf($P2Type,$T5) && $isSubtypeOf($P3Type,$T0)){
         $result = (IString)String_format$695b8ae2ac4bdb0c((IString) $P0, (IString) $P1, (IInteger) $P2, (IString) $P3);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2, $P3));
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
    public IString fromBase64(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)String_fromBase64$843832a7f2134466((IString) $P0, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IReal toReal(IValue $P0){ // Generated by Resolver
       IReal $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IReal)String_toReal$7ebfa25a05356a51((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue lub(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_Type.lub($P0, $P1);
    }
    public IBool rexpMatch(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IBool)String_rexpMatch$ce341cfb7d30e780((IString) $P0, (IString) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IString substitute(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T10)){
         $result = (IString)String_substitute$a969725dc1004f33((IString) $P0, (IMap) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IString fromBase32(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)String_fromBase32$17f4bf9042e6dd8c((IString) $P0, $kwpActuals);
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
    public IList split(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IList)String_split$01009c87f3d82e4c((IString) $P0, (IString) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IString replaceFirst(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0) && $isSubtypeOf($P2Type,$T0)){
         $result = (IString)String_replaceFirst$57c02d183940731e((IString) $P0, (IString) $P1, (IString) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IString right(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5) && $isSubtypeOf($P2Type,$T0)){
         $result = (IString)String_right$7b00c29ca5a570ed((IString) $P0, (IInteger) $P1, (IString) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IString right(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5)){
         $result = (IString)String_right$546d0f195fc78d79((IString) $P0, (IInteger) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isFunctionType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isFunctionType($P0);
    }
    public IValue glb(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_Type.glb($P0, $P1);
    }
    public IList chars(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)String_chars$92da80ccae0c69cc((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool endsWith(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IBool)String_endsWith$ecb90a76ab31d048((IString) $P0, (IString) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
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
    public IBool contains(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IBool)String_contains$91af143d76f59783((IString) $P0, (IString) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(893,362,<28,0>,<44,1>) 
    public IString String_center$244f457ebb47db3f(IString s_0, IInteger n_1){ 
        
        
        return ((IString)($me.format(((IString)s_0), ((IString)$constants.get(0)/*"center"*/), ((IInteger)n_1), ((IString)$constants.get(1)/*" "*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(1257,82,<46,0>,<49,1>) 
    public IString String_center$e854b35d975f4ec8(IString s_0, IInteger n_1, IString pad_2){ 
        
        
        return ((IString)($me.format(((IString)s_0), ((IString)$constants.get(0)/*"center"*/), ((IInteger)n_1), ((IString)pad_2))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(1342,433,<52,0>,<65,61>) 
    public IInteger String_charAt$c43d01db8f754016(IString s_0, IInteger i_1){ 
        
        
        return ((IInteger)((IInteger)$Prelude.charAt(s_0, i_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(1778,368,<68,0>,<80,67>) 
    public IList String_chars$92da80ccae0c69cc(IString s_0){ 
        
        
        final IListWriter $listwriter0 = (IListWriter)$RVF.listWriter();
        final IInteger $lst2 = ((IInteger)($me.size(((IString)s_0))));
        final boolean $dir3 = ((IInteger)$constants.get(2)/*0*/).less($lst2).getValue();
        
        $LCOMP1_GEN2127:
        for(IInteger $elem2 = ((IInteger)$constants.get(2)/*0*/); $dir3 ? $aint_less_aint($elem2,$lst2).getValue() 
                                  : $aint_lessequal_aint($elem2,$lst2).not().getValue(); $elem2 = $aint_add_aint($elem2,$dir3 ? ((IInteger)$constants.get(3)/*1*/) : ((IInteger)$constants.get(4)/*-1*/))){
            IInteger i_1 = ((IInteger)($elem2));
            $listwriter0.append($me.charAt(((IString)s_0), ((IInteger)i_1)));
        }
        
        return ((IList)($listwriter0.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(2149,348,<83,0>,<95,47>) 
    public IBool String_contains$91af143d76f59783(IString input_0, IString find_1){ 
        
        
        return ((IBool)((IBool)$Prelude.contains(input_0, find_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(2500,636,<98,0>,<111,1>) 
    public IString String_deescape$463da4cc9241a765(IString s_0){ 
        
        
        try {
            IString res_1 = ((IString)($TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.Yes, 
                 new DescendantDescriptor(new io.usethesource.vallang.type.Type[]{$TF.stringType()}, 
                                          new io.usethesource.vallang.IConstructor[]{}, 
                                          $RVF.bool(false)),
                 s_0,
                 (IVisitFunction) (IValue $VISIT0_subject, TraversalState $traversalState) -> {
                     VISIT0:switch(Fingerprint.getFingerprint($VISIT0_subject)){
                     
                         case 0:
                             
                     
                         default: 
                             if($isSubtypeOf($VISIT0_subject.getType(),$T0)){
                                /*muExists*/CASE_0_0: 
                                    do {
                                        final Matcher $matcher4 = (Matcher)$regExpCompile("^\\\\([\" \' \\< \\> \\\\])", ((IString)($VISIT0_subject)).getValue());
                                        boolean $found5 = true;
                                        
                                            while($found5){
                                                $found5 = $matcher4.find();
                                                if($found5){
                                                   IString c_2 = ((IString)($RVF.string($matcher4.group(1))));
                                                   $traversalState.setBegin($matcher4.start());
                                                   $traversalState.setEnd($matcher4.end());
                                                   IString $replacement3 = (IString)(c_2);
                                                   if($isSubtypeOf($replacement3.getType(),$VISIT0_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement3;
                                                   
                                                   } else {
                                                      break VISIT0;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT0_subject.getType(),$T0)){
                                /*muExists*/CASE_0_1: 
                                    do {
                                        final Matcher $matcher7 = (Matcher)$regExpCompile("^\\\\t", ((IString)($VISIT0_subject)).getValue());
                                        boolean $found8 = true;
                                        
                                            while($found8){
                                                $found8 = $matcher7.find();
                                                if($found8){
                                                   $traversalState.setBegin($matcher7.start());
                                                   $traversalState.setEnd($matcher7.end());
                                                   IString $replacement6 = (IString)(((IString)$constants.get(5)/*"	"*/));
                                                   if($isSubtypeOf($replacement6.getType(),$VISIT0_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement6;
                                                   
                                                   } else {
                                                      break VISIT0;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT0_subject.getType(),$T0)){
                                /*muExists*/CASE_0_2: 
                                    do {
                                        final Matcher $matcher10 = (Matcher)$regExpCompile("^\\\\n", ((IString)($VISIT0_subject)).getValue());
                                        boolean $found11 = true;
                                        
                                            while($found11){
                                                $found11 = $matcher10.find();
                                                if($found11){
                                                   $traversalState.setBegin($matcher10.start());
                                                   $traversalState.setEnd($matcher10.end());
                                                   IString $replacement9 = (IString)(((IString)$constants.get(6)/*"
                                                   "*/));
                                                   if($isSubtypeOf($replacement9.getType(),$VISIT0_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement9;
                                                   
                                                   } else {
                                                      break VISIT0;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT0_subject.getType(),$T0)){
                                /*muExists*/CASE_0_3: 
                                    do {
                                        final Matcher $matcher13 = (Matcher)$regExpCompile("^\\\\f", ((IString)($VISIT0_subject)).getValue());
                                        boolean $found14 = true;
                                        
                                            while($found14){
                                                $found14 = $matcher13.find();
                                                if($found14){
                                                   $traversalState.setBegin($matcher13.start());
                                                   $traversalState.setEnd($matcher13.end());
                                                   IString $replacement12 = (IString)(((IString)$constants.get(7)/*""*/));
                                                   if($isSubtypeOf($replacement12.getType(),$VISIT0_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement12;
                                                   
                                                   } else {
                                                      break VISIT0;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT0_subject.getType(),$T0)){
                                /*muExists*/CASE_0_4: 
                                    do {
                                        final Matcher $matcher16 = (Matcher)$regExpCompile("^\\\\b", ((IString)($VISIT0_subject)).getValue());
                                        boolean $found17 = true;
                                        
                                            while($found17){
                                                $found17 = $matcher16.find();
                                                if($found17){
                                                   $traversalState.setBegin($matcher16.start());
                                                   $traversalState.setEnd($matcher16.end());
                                                   IString $replacement15 = (IString)(((IString)$constants.get(8)/*""*/));
                                                   if($isSubtypeOf($replacement15.getType(),$VISIT0_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement15;
                                                   
                                                   } else {
                                                      break VISIT0;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT0_subject.getType(),$T0)){
                                /*muExists*/CASE_0_5: 
                                    do {
                                        final Matcher $matcher20 = (Matcher)$regExpCompile("^\\\\u([0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F])", ((IString)($VISIT0_subject)).getValue());
                                        boolean $found21 = true;
                                        
                                            while($found21){
                                                $found21 = $matcher20.find();
                                                if($found21){
                                                   IString hex_3 = ((IString)($RVF.string($matcher20.group(1))));
                                                   $traversalState.setBegin($matcher20.start());
                                                   $traversalState.setEnd($matcher20.end());
                                                   final Template $template19 = (Template)new Template($RVF, "0x");
                                                   $template19.beginIndent("  ");
                                                   $template19.addStr(((IString)hex_3).getValue());
                                                   $template19.endIndent("  ");
                                                   IString $replacement18 = (IString)($me.stringChar(((IInteger)($me.toInt(((IString)($template19.close())))))));
                                                   if($isSubtypeOf($replacement18.getType(),$VISIT0_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement18;
                                                   
                                                   } else {
                                                      break VISIT0;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT0_subject.getType(),$T0)){
                                /*muExists*/CASE_0_6: 
                                    do {
                                        final Matcher $matcher24 = (Matcher)$regExpCompile("^\\\\U([0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F])", ((IString)($VISIT0_subject)).getValue());
                                        boolean $found25 = true;
                                        
                                            while($found25){
                                                $found25 = $matcher24.find();
                                                if($found25){
                                                   IString hex_4 = ((IString)($RVF.string($matcher24.group(1))));
                                                   $traversalState.setBegin($matcher24.start());
                                                   $traversalState.setEnd($matcher24.end());
                                                   final Template $template23 = (Template)new Template($RVF, "0x");
                                                   $template23.beginIndent("  ");
                                                   $template23.addStr(((IString)hex_4).getValue());
                                                   $template23.endIndent("  ");
                                                   IString $replacement22 = (IString)($me.stringChar(((IInteger)($me.toInt(((IString)($template23.close())))))));
                                                   if($isSubtypeOf($replacement22.getType(),$VISIT0_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement22;
                                                   
                                                   } else {
                                                      break VISIT0;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT0_subject.getType(),$T0)){
                                /*muExists*/CASE_0_7: 
                                    do {
                                        final Matcher $matcher28 = (Matcher)$regExpCompile("^\\\\a([0-7][0-9a-fA-F])", ((IString)($VISIT0_subject)).getValue());
                                        boolean $found29 = true;
                                        
                                            while($found29){
                                                $found29 = $matcher28.find();
                                                if($found29){
                                                   IString hex_5 = ((IString)($RVF.string($matcher28.group(1))));
                                                   $traversalState.setBegin($matcher28.start());
                                                   $traversalState.setEnd($matcher28.end());
                                                   final Template $template27 = (Template)new Template($RVF, "0x");
                                                   $template27.beginIndent("  ");
                                                   $template27.addStr(((IString)hex_5).getValue());
                                                   $template27.endIndent("  ");
                                                   IString $replacement26 = (IString)($me.stringChar(((IInteger)($me.toInt(((IString)($template27.close())))))));
                                                   if($isSubtypeOf($replacement26.getType(),$VISIT0_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement26;
                                                   
                                                   } else {
                                                      break VISIT0;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
            
                     }
                     return $VISIT0_subject;
                 })));
            return ((IString)res_1);
        
        } catch (ReturnFromTraversalException e) {
            return (IString) e.getValue();
        }
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(3139,316,<114,0>,<125,51>) 
    public IBool String_endsWith$ecb90a76ab31d048(IString subject_0, IString suffix_1){ 
        
        
        return ((IBool)((IBool)$Prelude.endsWith(subject_0, suffix_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(3458,463,<128,0>,<143,58>) 
    public IString String_escape$7fa23c31a411d9dc(IString subject_0, IMap mapping_1){ 
        
        
        return ((IString)((IString)$Prelude.escape(subject_0, mapping_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(3925,485,<147,0>,<163,53>) 
    public IList String_findAll$bd53b9d00cf0465e(IString subject_0, IString find_1){ 
        
        
        return ((IList)((IList)$Prelude.findAll(subject_0, find_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(4413,501,<166,0>,<182,49>) 
    public IInteger String_findFirst$d5769aac4911388a(IString subject_0, IString find_1){ 
        
        
        return ((IInteger)((IInteger)$Prelude.findFirst(subject_0, find_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(4917,496,<185,0>,<201,48>) 
    public IInteger String_findLast$e5f4379f5988beac(IString subject_0, IString find_1){ 
        
        
        return ((IInteger)((IInteger)$Prelude.findLast(subject_0, find_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(5416,250,<204,0>,<216,32>) 
    public IBool String_isEmpty$0288736cc2315249(IString s_0){ 
        
        
        return ((IBool)((IBool)$Prelude.isEmpty(s_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(5669,275,<219,0>,<231,33>) 
    public IString String_arbString$bfea25a11df8dffa(IInteger n_0){ 
        
        
        return ((IString)((IString)$Prelude.arbString(n_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(5947,366,<234,0>,<249,1>) 
    public IString String_left$ea4ca9949ebc690b(IString s_0, IInteger n_1){ 
        
        
        return ((IString)($me.format(((IString)s_0), ((IString)$constants.get(9)/*"left"*/), ((IInteger)n_1), ((IString)$constants.get(1)/*" "*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(6315,78,<251,0>,<254,1>) 
    public IString String_left$b5982a84598a5a87(IString s_0, IInteger n_1, IString pad_2){ 
        
        
        return ((IString)($me.format(((IString)s_0), ((IString)$constants.get(9)/*"left"*/), ((IInteger)n_1), ((IString)pad_2))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(6396,616,<257,0>,<274,67>) 
    public IString String_replaceAll$40dfb6273a17a7ab(IString subject_0, IString find_1, IString replacement_2){ 
        
        
        return ((IString)((IString)$Prelude.replaceAll(subject_0, find_1, replacement_2)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(7015,637,<277,0>,<294,69>) 
    public IString String_replaceFirst$57c02d183940731e(IString subject_0, IString find_1, IString replacement_2){ 
        
        
        return ((IString)((IString)$Prelude.replaceFirst(subject_0, find_1, replacement_2)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(7655,633,<297,0>,<314,68>) 
    public IString String_replaceLast$e73336ea981874fc(IString subject_0, IString find_1, IString replacement_2){ 
        
        
        return ((IString)((IString)$Prelude.replaceLast(subject_0, find_1, replacement_2)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(8291,285,<317,0>,<328,31>) 
    public IString String_reverse$5c483498523ba125(IString s_0){ 
        
        
        return ((IString)((IString)$Prelude.reverse(s_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(8580,533,<332,0>,<356,1>) 
    public IString String_right$546d0f195fc78d79(IString s_0, IInteger n_1){ 
        
        
        return ((IString)($me.format(((IString)s_0), ((IString)$constants.get(10)/*"right"*/), ((IInteger)n_1), ((IString)$constants.get(1)/*" "*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(9115,80,<358,0>,<361,1>) 
    public IString String_right$7b00c29ca5a570ed(IString s_0, IInteger n_1, IString pad_2){ 
        
        
        return ((IString)($me.format(((IString)s_0), ((IString)$constants.get(10)/*"right"*/), ((IInteger)n_1), ((IString)pad_2))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(9199,261,<365,0>,<377,28>) 
    public IInteger String_size$4611676944e933d5(IString s_0){ 
        
        
        return ((IInteger)((IInteger)$Prelude.size(s_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(9463,321,<380,0>,<391,53>) 
    public IBool String_startsWith$cac4396a9880251b(IString subject_0, IString prefix_1){ 
        
        
        return ((IBool)((IBool)$Prelude.startsWith(subject_0, prefix_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(9788,153,<395,0>,<397,60>) 
    public IString String_stringChar$29485394ec702339(IInteger $char_0){ 
        
        
        return ((IString)((IString)$Prelude.stringChar($char_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(9944,170,<400,0>,<402,68>) 
    public IString String_stringChars$990634670d003421(IList chars_0){ 
        
        
        return ((IString)((IString)$Prelude.stringChars(chars_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(10117,159,<405,0>,<407,42>) 
    public IBool String_isValidCharacter$e3998b941251ff24(IInteger ch_0){ 
        
        
        return ((IBool)((IBool)$Prelude.isValidCharacter(ch_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(10279,421,<410,0>,<423,44>) 
    public IString String_substring$1516e7be77c7268c(IString s_0, IInteger begin_1){ 
        
        
        return ((IString)((IString)$Prelude.substring(s_0, begin_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(10702,95,<425,0>,<426,53>) 
    public IString String_substring$77404656fd06e485(IString s_0, IInteger begin_1, IInteger end_2){ 
        
        
        return ((IString)((IString)$Prelude.substring(s_0, begin_1, end_2)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(10801,456,<430,0>,<450,52>) 
    public IInteger String_toInt$8c1572a980336ee2(IString s_0){ 
        
        
        return ((IInteger)((IInteger)$Prelude.toInt(s_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(11259,101,<452,0>,<453,59>) 
    public IInteger String_toInt$f408c9e5b956da4b(IString s_0, IInteger r_1){ 
        
        
        return ((IInteger)((IInteger)$Prelude.toInt(s_0, r_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(11363,311,<456,0>,<467,35>) 
    public IString String_toLowerCase$aa30e5ee71d42d20(IString s_0){ 
        
        
        return ((IString)((IString)$Prelude.toLowerCase(s_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(11678,320,<471,0>,<484,31>) 
    public IReal String_toReal$7ebfa25a05356a51(IString s_0){ 
        
        
        return ((IReal)((IReal)$Prelude.toReal(s_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(12001,314,<487,0>,<500,35>) 
    public IString String_toUpperCase$52fd8e341a749153(IString s_0){ 
        
        
        return ((IString)((IString)$Prelude.toUpperCase(s_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(12318,215,<503,0>,<512,28>) 
    public IString String_trim$8faad6373d3f1827(IString s_0){ 
        
        
        return ((IString)((IString)$Prelude.trim(s_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(12536,660,<515,0>,<531,46>) 
    public IString String_squeeze$d1d8b81dff6f8420(IString src_0, IString charSet_1){ 
        
        
        return ((IString)((IString)$Prelude.squeeze(src_0, charSet_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(13198,1219,<533,0>,<564,2>) 
    public IString String_squeeze$6e2735f43e585785(IString src_0, IConstructor $__1){ 
        
        
        try {
            HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
            if($T6.match($__1.getType(), $typeBindings)){
               final IString $result34 = ((IString)($TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.Yes, 
                    new DescendantDescriptor(new io.usethesource.vallang.type.Type[]{$TF.stringType()}, 
                                             new io.usethesource.vallang.IConstructor[]{}, 
                                             $RVF.bool(false)),
                    src_0,
                    (IVisitFunction) (IValue $VISIT9_subject, TraversalState $traversalState) -> {
                        VISIT9:switch(Fingerprint.getFingerprint($VISIT9_subject)){
                        
                            case 0:
                                
                        
                            default: 
                                if($isSubtypeOf($VISIT9_subject.getType(),$T0)){
                                   /*muExists*/CASE_0_0: 
                                       do {
                                           final Matcher $matcher32 = (Matcher)$regExpCompile("(.)\\1+", ((IString)($VISIT9_subject)).getValue());
                                           boolean $found33 = true;
                                           
                                               while($found33){
                                                   $found33 = $matcher32.find();
                                                   if($found33){
                                                      IString c_1 = ((IString)($RVF.string($matcher32.group(1))));
                                                      $traversalState.setBegin($matcher32.start());
                                                      $traversalState.setEnd($matcher32.end());
                                                      final ITree $subject_val31 = ((ITree)($RVF.constructor(M_ParseTree.Tree_char_int, new IValue[]{((IInteger)($me.charAt(((IString)c_1), ((IInteger)$constants.get(2)/*0*/))))})));
                                                      if($isSubtypeOf($subject_val31.getType(),$T12.instantiate($typeBindings))){
                                                         IString $replacement30 = (IString)(c_1);
                                                         if($isSubtypeOf($replacement30.getType(),$VISIT9_subject.getType())){
                                                            $traversalState.setMatchedAndChanged(true, true);
                                                            return $replacement30;
                                                         
                                                         } else {
                                                            break VISIT9;// switch
                                                         
                                                         }
                                                      }
                                                   
                                                   }
                                           
                                               }
                                   
                                       } while(false);
                                
                                }
               
                        }
                        return $VISIT9_subject;
                    })));
               if($T0.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result34.getType(),$T0)){
                  return ((IString)($result34));
               
               } else {
                  return null;
               }
            } else {
               return null;
            }
        } catch (ReturnFromTraversalException e) {
            return (IString) e.getValue();
        }
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(14420,167,<567,0>,<569,46>) 
    public IList String_split$01009c87f3d82e4c(IString sep_0, IString src_1){ 
        
        
        return ((IList)((IList)$Prelude.split(sep_0, src_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(14589,78,<571,0>,<572,36>) 
    public IString String_capitalize$241c1cc358b38921(IString src_0){ 
        
        
        return ((IString)((IString)$Prelude.capitalize(src_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(14669,80,<574,0>,<575,38>) 
    public IString String_uncapitalize$5a860f2aab2710e3(IString src_0){ 
        
        
        return ((IString)((IString)$Prelude.uncapitalize(src_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(14751,395,<577,0>,<583,89>) 
    public IString String_toBase64$e60dafecaa2ee56e(IString src_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = Util.kwpMap();
        IString $kwpDefault_charset = ((IString)DEFAULT_CHARSET);
        $kwpDefaults.put("charset", $kwpDefault_charset);IBool $kwpDefault_includePadding = ((IBool)$constants.get(11)/*true*/);
        $kwpDefaults.put("includePadding", $kwpDefault_includePadding);
        return ((IString)((IString)$Prelude.toBase64(src_0, (IString)($kwpActuals.containsKey("charset") ? $kwpActuals.get("charset") : $kwpDefaults.get("charset")), (IBool)($kwpActuals.containsKey("includePadding") ? $kwpActuals.get("includePadding") : $kwpDefaults.get("includePadding")))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(15148,378,<585,0>,<591,65>) 
    public IString String_fromBase64$843832a7f2134466(IString src_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = Util.kwpMap();
        IString $kwpDefault_charset = ((IString)DEFAULT_CHARSET);
        $kwpDefaults.put("charset", $kwpDefault_charset);
        return ((IString)((IString)$Prelude.fromBase64(src_0, (IString)($kwpActuals.containsKey("charset") ? $kwpActuals.get("charset") : $kwpDefaults.get("charset")))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(15528,395,<593,0>,<599,89>) 
    public IString String_toBase32$49dc832874fb11d9(IString src_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = Util.kwpMap();
        IString $kwpDefault_charset = ((IString)DEFAULT_CHARSET);
        $kwpDefaults.put("charset", $kwpDefault_charset);IBool $kwpDefault_includePadding = ((IBool)$constants.get(11)/*true*/);
        $kwpDefaults.put("includePadding", $kwpDefault_includePadding);
        return ((IString)((IString)$Prelude.toBase32(src_0, (IString)($kwpActuals.containsKey("charset") ? $kwpActuals.get("charset") : $kwpDefaults.get("charset")), (IBool)($kwpActuals.containsKey("includePadding") ? $kwpActuals.get("includePadding") : $kwpDefaults.get("includePadding")))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(15925,378,<601,0>,<607,65>) 
    public IString String_fromBase32$17f4bf9042e6dd8c(IString src_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = Util.kwpMap();
        IString $kwpDefault_charset = ((IString)DEFAULT_CHARSET);
        $kwpDefaults.put("charset", $kwpDefault_charset);
        return ((IString)((IString)$Prelude.fromBase32(src_0, (IString)($kwpActuals.containsKey("charset") ? $kwpActuals.get("charset") : $kwpDefaults.get("charset")))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(16306,269,<610,0>,<615,46>) 
    public IString String_wrap$c21d49e5b8cae059(IString src_0, IInteger wrapLength_1){ 
        
        
        return ((IString)((IString)$Prelude.wrap(src_0, wrapLength_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(16740,98,<622,0>,<623,56>) 
    public IString String_format$695b8ae2ac4bdb0c(IString s_0, IString dir_1, IInteger n_2, IString pad_3){ 
        
        
        return ((IString)((IString)$Prelude.format(s_0, dir_1, n_2, pad_3)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(16841,208,<626,0>,<631,42>) 
    public IBool String_rexpMatch$ce341cfb7d30e780(IString s_0, IString re_1){ 
        
        
        return ((IBool)((IBool)$Prelude.rexpMatch(s_0, re_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(17052,522,<634,0>,<647,96>) 
    public ISourceLocation String_toLocation$347a629c083bfdf1(IString s_0){ 
        
        
        final Matcher $matcher36 = (Matcher)$regExpCompile("(.*)\\://(.*)", ((IString)s_0).getValue());
        boolean $found37 = true;
        
            while($found37){
                $found37 = $matcher36.find();
                if($found37){
                   IString car_1 = ((IString)($RVF.string($matcher36.group(1))));
                   IString cdr_2 = ((IString)($RVF.string($matcher36.group(2))));
                   return ((ISourceLocation)($create_aloc(((IString)($astr_add_astr(((IString)($astr_add_astr(((IString)$constants.get(12)/*""*/),$astr_add_astr(((IString)car_1),((IString)$constants.get(13)/*"://"*/))))),((IString)($astr_add_astr(((IString)$constants.get(12)/*""*/),$astr_add_astr(((IString)cdr_2),((IString)$constants.get(12)/*""*/)))))))))));
                
                } else {
                   return ((ISourceLocation)($create_aloc(((IString)($astr_add_astr(((IString)$constants.get(14)/*"cwd://"*/),((IString)($astr_add_astr(((IString)$constants.get(15)/*"/"*/),$astr_add_astr(((IString)s_0),((IString)$constants.get(12)/*""*/)))))))))));
                
                }
            }
        return null;
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(17832,197,<659,4>,<664,5>) 
    public IString String_subst1$0127b165869021ec(IString src_0, ISourceLocation x_1, IString y_2, ValueRef<IInteger> shift_2){ 
        
        
        IInteger delta_3 = ((IInteger)(((IInteger) ((IInteger)($me.size(((IString)y_2)))).subtract(((IInteger)(((IInteger)($aloc_get_field(((ISourceLocation)x_1), "length")))))))));
        src_0 = ((IString)($astr_add_astr(((IString)($astr_add_astr(((IString)($astr_slice(((IString)(src_0)), 0, null, ((IInteger)($aint_add_aint(((IInteger)(((IInteger)($aloc_get_field(((ISourceLocation)x_1), "offset"))))),shift_2.getValue()))).intValue()))),((IString)y_2)))),((IString)($astr_slice(((IString)(src_0)), ((IInteger)($aint_add_aint(((IInteger)($aint_add_aint(((IInteger)(((IInteger)($aloc_get_field(((ISourceLocation)x_1), "offset"))))),((IInteger)(((IInteger)($aloc_get_field(((ISourceLocation)x_1), "length")))))))),shift_2.getValue()))).intValue(), null, null))))));
        IInteger $aux_1 = ((IInteger)delta_3);
        shift_2.setValue($aint_add_aint(shift_2.getValue(),$aux_1));
        return ((IString)src_0);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(18063,50,<665,33>,<665,83>) 
    public IBool $CLOSURE_0(ISourceLocation a_0, ISourceLocation b_1){ 
        
        
        return ((IBool)($aint_less_aint(((IInteger)(((IInteger)($aloc_get_field(((ISourceLocation)a_0), "offset"))))),((IInteger)(((IInteger)($aloc_get_field(((ISourceLocation)b_1), "offset"))))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/String.rsc|(17577,595,<650,0>,<667,1>) 
    public IString String_substitute$a969725dc1004f33(IString src_0, IMap s_1){ 
        
        
        final ValueRef<IInteger> shift_2 = new ValueRef<IInteger>("shift", ((IInteger)$constants.get(2)/*0*/));
        final IListWriter $listwriter38 = (IListWriter)$RVF.listWriter();
        $LCOMP39_GEN18053:
        for(IValue $elem40_for : ((IMap)s_1)){
            ISourceLocation $elem40 = (ISourceLocation) $elem40_for;
            ISourceLocation k_4 = null;
            $listwriter38.append($elem40);
        
        }
        
                    IList order_3 = ((IList)(M_List.sort(((IList)($listwriter38.done())), new TypedFunctionInstance2<IValue,IValue,IValue>(($18063_0, $18063_1) -> { return $CLOSURE_0((ISourceLocation)$18063_0, (ISourceLocation)$18063_1); }, $T13))));
        IString $reducer42 = (IString)(src_0);
        $REDUCER41_GEN18157:
        for(IValue $elem43_for : ((IList)order_3)){
            ISourceLocation $elem43 = (ISourceLocation) $elem43_for;
            ISourceLocation x_6 = ((ISourceLocation)($elem43));
            $reducer42 = ((IString)(String_substitute$a969725dc1004f33_subst1(((IString)($reducer42)), ((ISourceLocation)x_6), ((IString)($amap_subscript(((IMap)s_1),((ISourceLocation)x_6)))), shift_2)));
        
        }
        
                    return ((IString)($reducer42));
    
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `String`");
    }
}