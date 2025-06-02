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
public class $Set 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.$Set_$I {

    private final $Set_$I $me;
    private final IList $constants;
    
    
    public final rascal.util.$Math M_util_Math;
    public final rascal.$Exception M_Exception;
    public final rascal.$List M_List;

    
    final org.rascalmpl.library.Prelude $Prelude; // TODO: asBaseClassName will generate name collisions if there are more of the same name in different packages

    
    public final io.usethesource.vallang.type.Type $T1;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T31;	/*aparameter("K",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T2;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T10;	/*aparameter("B",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T28;	/*aint()*/
    public final io.usethesource.vallang.type.Type $T46;	/*aparameter("A",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T21;	/*anum()*/
    public final io.usethesource.vallang.type.Type $T22;	/*aparameter("T",anum(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T42;	/*aparameter("T",anum(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T9;	/*aparameter("A",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T37;	/*aparameter("T",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T17;	/*aparameter("K",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T19;	/*aparameter("T",avalue(),closed=false,alabel="f")*/
    public final io.usethesource.vallang.type.Type $T7;	/*aparameter("T",avalue(),closed=false,alabel="b")*/
    public final io.usethesource.vallang.type.Type $T50;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T6;	/*aparameter("T",avalue(),closed=false,alabel="a")*/
    public final io.usethesource.vallang.type.Type $T24;	/*aparameter("T",anum(),closed=false,alabel="e")*/
    public final io.usethesource.vallang.type.Type $T41;	/*aparameter("U",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T15;	/*aparameter("V",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T47;	/*aparameter("B",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T13;	/*aparameter("U",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T32;	/*aparameter("V",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T38;	/*aset(aparameter("T",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T5;	/*abool()*/
    public final io.usethesource.vallang.type.Type $T4;	/*afunc(abool(),[aparameter("T",avalue(),closed=false,alabel="a"),aparameter("T",avalue(),closed=false,alabel="b")],[])*/
    public final io.usethesource.vallang.type.Type $T11;	/*alrel(atypeList([aparameter("A",avalue(),closed=false),aparameter("B",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type $T23;	/*aset(aparameter("T",anum(),closed=false,alabel="e"))*/
    public final io.usethesource.vallang.type.Type $T27;	/*aset(aparameter("T",avalue(),closed=false,alabel="f"))*/
    public final io.usethesource.vallang.type.Type $T14;	/*aset(aparameter("V",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T49;	/*amap(aparameter("A",avalue(),closed=true),aparameter("B",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T3;	/*aset(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T0;	/*alist(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T20;	/*aset(aparameter("T",anum(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T29;	/*aset(avalue())*/
    public final io.usethesource.vallang.type.Type $T16;	/*afunc(aparameter("K",avalue(),closed=false),[aparameter("V",avalue(),closed=false)],[])*/
    public final io.usethesource.vallang.type.Type $T48;	/*aset(aparameter("B",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T45;	/*amap(aparameter("A",avalue(),closed=true),aset(aparameter("B",avalue(),closed=true)))*/
    public final io.usethesource.vallang.type.Type $T12;	/*afunc(aparameter("U",avalue(),closed=false),[aparameter("T",avalue(),closed=false)],[])*/
    public final io.usethesource.vallang.type.Type $T36;	/*aset(aset(aparameter("T",avalue(),closed=true)))*/
    public final io.usethesource.vallang.type.Type $T43;	/*atuple(atypeList([aparameter("T",avalue(),closed=true),aset(aparameter("T",avalue(),closed=true))]))*/
    public final io.usethesource.vallang.type.Type $T33;	/*aset(aparameter("V",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T44;	/*alist(aparameter("T",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T18;	/*alist(aparameter("T",avalue(),closed=false,alabel="f"))*/
    public final io.usethesource.vallang.type.Type $T35;	/*afunc(abool(),[aparameter("T",avalue(),closed=false,alabel="a")],[],returnsViaAllPath=true)*/
    public final io.usethesource.vallang.type.Type $T8;	/*arel(atypeList([aparameter("A",avalue(),closed=false),aparameter("B",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type $T40;	/*aset(aparameter("U",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T34;	/*afunc(abool(),[aparameter("T",avalue(),closed=false,alabel="a"),aparameter("T",avalue(),closed=false,alabel="b")],[],returnsViaAllPath=true)*/
    public final io.usethesource.vallang.type.Type $T39;	/*amap(aparameter("T",avalue(),closed=true),aint())*/
    public final io.usethesource.vallang.type.Type $T26;	/*aset(aset(aparameter("T",avalue(),closed=false)))*/
    public final io.usethesource.vallang.type.Type $T30;	/*amap(aparameter("K",avalue(),closed=true),aset(aparameter("V",avalue(),closed=true)))*/
    public final io.usethesource.vallang.type.Type $T25;	/*afunc(aparameter("T",avalue(),closed=false),[aparameter("T",avalue(),closed=false),aparameter("T",avalue(),closed=false)],[])*/

    public $Set(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Set(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Set_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.$Set.class, this);
       
       mstore.importModule(rascal.util.$Math.class, rex, rascal.util.$Math::new);
       mstore.importModule(rascal.$Exception.class, rex, rascal.$Exception::new);
       mstore.importModule(rascal.$List.class, rex, rascal.$List::new); 
       
       M_util_Math = mstore.getModule(rascal.util.$Math.class);
       M_Exception = mstore.getModule(rascal.$Exception.class);
       M_List = mstore.getModule(rascal.$List.class); 
       
                          
       
       $TS.importStore(M_util_Math.$TS);
       $TS.importStore(M_Exception.$TS);
       $TS.importStore(M_List.$TS);
       
       $Prelude = $initLibrary("org.rascalmpl.library.Prelude"); 
    
       $constants = readBinaryConstantsFile(this.getClass(), "rascal//$Set.constants", 8, "d2a3ee989602c1579cddb092162c57bf");
       ADT_RuntimeException = $adt("RuntimeException");
       $T1 = $TF.valueType();
       $T31 = $TF.parameterType("K", $T1);
       $T2 = $TF.parameterType("T", $T1);
       $T10 = $TF.parameterType("B", $T1);
       $T28 = $TF.integerType();
       $T46 = $TF.parameterType("A", $T1);
       $T21 = $TF.numberType();
       $T22 = $TF.parameterType("T", $T21);
       $T42 = $TF.parameterType("T", $T21);
       $T9 = $TF.parameterType("A", $T1);
       $T37 = $TF.parameterType("T", $T1);
       $T17 = $TF.parameterType("K", $T1);
       $T19 = $TF.parameterType("T", $T1);
       $T7 = $TF.parameterType("T", $T1);
       $T50 = $TF.stringType();
       $T6 = $TF.parameterType("T", $T1);
       $T24 = $TF.parameterType("T", $T21);
       $T41 = $TF.parameterType("U", $T1);
       $T15 = $TF.parameterType("V", $T1);
       $T47 = $TF.parameterType("B", $T1);
       $T13 = $TF.parameterType("U", $T1);
       $T32 = $TF.parameterType("V", $T1);
       $T38 = $TF.setType($T37);
       $T5 = $TF.boolType();
       $T4 = $TF.functionType($T5, $TF.tupleType($T6, "a", $T7, "b"), $TF.tupleEmpty());
       $T11 = $TF.listType($TF.tupleType($T9, $T10));
       $T23 = $TF.setType($T24);
       $T27 = $TF.setType($T19);
       $T14 = $TF.setType($T15);
       $T49 = $TF.mapType($T46,$T47);
       $T3 = $TF.setType($T2);
       $T0 = $TF.listType($T2);
       $T20 = $TF.setType($T22);
       $T29 = $TF.setType($T1);
       $T16 = $TF.functionType($T17, $TF.tupleType($T15), $TF.tupleEmpty());
       $T48 = $TF.setType($T47);
       $T45 = $TF.mapType($T46,$T48);
       $T12 = $TF.functionType($T13, $TF.tupleType($T2), $TF.tupleEmpty());
       $T36 = $TF.setType($T38);
       $T43 = $TF.tupleType($T37, $T38);
       $T33 = $TF.setType($T32);
       $T44 = $TF.listType($T37);
       $T18 = $TF.listType($T19);
       $T35 = $TF.functionType($T5, $TF.tupleType($T6, "a"), $TF.tupleEmpty());
       $T8 = $TF.setType($TF.tupleType($T9, $T10));
       $T40 = $TF.setType($T41);
       $T34 = $TF.functionType($T5, $TF.tupleType($T6, "a", $T7, "b"), $TF.tupleEmpty());
       $T39 = $TF.mapType($T37,$T28);
       $T26 = $TF.setType($T3);
       $T30 = $TF.mapType($T31,$T33);
       $T25 = $TF.functionType($T2, $TF.tupleType($T2, $T2), $TF.tupleEmpty());
    
       
       
    }
    public IBool isEmpty(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IBool)M_List.List_isEmpty$fdfe8b76f8afe83f((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IBool)Set_isEmpty$42ff0d21e8590723((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList toList(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IList)Set_toList$c29313189aeae08a((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue getOneFrom(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IValue)Set_getOneFrom$385242ba381fd613((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList sort(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)M_List.List_sort$1fe4426c8c8039da((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IList)Set_sort$2d7ce904b21febd4((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList sort(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T4)){
         $result = (IList)M_List.List_sort$a9bbc6fca4e60d0a((IList) $P0, (TypedFunctionInstance2<IValue, IValue, IValue>) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T3) && $isSubtypeOf($P1Type,$T4)){
         $result = (IList)Set_sort$4b3ff1abd5c398df((ISet) $P0, (TypedFunctionInstance2<IValue, IValue, IValue>) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IValue head(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.head($P0);
    }
    public IList head(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.head($P0, $P1);
    }
    public IInteger size(IValue $P0){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IInteger)Set_size$215788d71e8b2455((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IInteger)M_List.List_size$ba7443328d8b4a27((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IMap toMap(IValue $P0){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T8)){
         $result = (IMap)Set_toMap$5f7d7ee44cb2e11d((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T11)){
         $result = (IMap)M_List.List_toMap$795bdddf805b0c4b((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet mapper(IValue $P0, IValue $P1){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T3) && $isSubtypeOf($P1Type,$T12)){
         $result = (ISet)Set_mapper$24f74ceda05f6c34((ISet) $P0, (TypedFunctionInstance1<IValue, IValue>) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IValue max(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IValue)Set_max$a286307ecf1dce32((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IMap classify(IValue $P0, IValue $P1){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T14) && $isSubtypeOf($P1Type,$T16)){
         $result = (IMap)Set_classify$495ec1221140dd2f((ISet) $P0, (TypedFunctionInstance1<IValue, IValue>) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IValue getFirstFrom(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T18)){
         $result = (IValue)M_List.List_getFirstFrom$8b59769acd262783((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IValue)M_List.List_getFirstFrom$ecaac22228d5233c((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IValue)Set_getFirstFrom$f7e01cd92c21a6cb((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet power(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (ISet)Set_power$91b962acd7a7701a((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue getSingleFrom(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IValue)Set_getSingleFrom$42d5ad5d40a25ab7((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ITuple takeOneFrom(IValue $P0){ // Generated by Resolver
       ITuple $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (ITuple)Set_takeOneFrom$291ddec83a7e9a61((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ITuple)M_List.List_takeOneFrom$48bb3b6062ea97b1((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public INumber sum(IValue $P0){ // Generated by Resolver
       INumber $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T20)){
         $result = (INumber)Set_sum$059718d9ca35d7b7((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T23)){
         $result = (INumber)Set_sum$2faf8190d541068a((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString toString(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IString)Set_toString$2266fa6e4b318c58((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet power1(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (ISet)Set_power1$28a82373d451a91c((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue reducer(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IValue)Set_reducer$3562adf6c81d1f9f((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue reducer(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T3) && $isSubtypeOf($P1Type,$T25) && $isSubtypeOf($P2Type,$T2)){
         $result = (IValue)Set_reducer$549b0aa4c7a77384((ISet) $P0, (TypedFunctionInstance2<IValue, IValue, IValue>) $P1, (IValue) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public ISet union(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T26)){
         $result = (ISet)Set_union$356d5959884f94d7((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ITuple headTail(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.headTail($P0);
    }
    public IList tail(IValue $P0){ // Generated by Resolver
       return (IList) M_List.tail($P0);
    }
    public IList tail(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.tail($P0, $P1);
    }
    public IMap toMapUnique(IValue $P0){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T8)){
         $result = (IMap)Set_toMapUnique$fca205feb507456a((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue index(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IValue)Set_index$31fadea181d3071e((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IValue)M_List.List_index$90228c781d131b76((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue min(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IValue)Set_min$68b6ebc4d32e8c20((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ITuple takeFirstFrom(IValue $P0){ // Generated by Resolver
       ITuple $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T27)){
         $result = (ITuple)Set_takeFirstFrom$b4e24a7c33f584da((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T3)){
         $result = (ITuple)Set_takeFirstFrom$dc2a600de1d8389f((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString itoString(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IString)Set_itoString$d920014271a23230((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue top(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.top($P0);
    }
    public IList top(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T28) && $isSubtypeOf($P1Type,$T3) && $isSubtypeOf($P2Type,$T4)){
         $result = (IList)Set_top$cbec7e0e8e8db73b((IInteger) $P0, (ISet) $P1, (TypedFunctionInstance2<IValue, IValue, IValue>) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IList top(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T28) && $isSubtypeOf($P1Type,$T3)){
         $result = (IList)Set_top$3a2a3e2325787ee4((IInteger) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IReal pow(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IReal) M_util_Math.pow($P0, $P1);
    }
    public IReal jaccard(IValue $P0, IValue $P1){ // Generated by Resolver
       IReal $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T29) && $isSubtypeOf($P1Type,$T29)){
         $result = (IReal)Set_jaccard$6b63114dcc758902((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ISet group(IValue $P0, IValue $P1){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T3) && $isSubtypeOf($P1Type,$T4)){
         $result = (ISet)Set_group$8f9750fd06d83138((ISet) $P0, (TypedFunctionInstance2<IValue, IValue, IValue>) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(649,757,<27,0>,<48,105>) 
    public IMap Set_classify$495ec1221140dd2f(ISet input_0, TypedFunctionInstance1<IValue, IValue> getClass_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T14.match(input_0.getType(), $typeBindings)){
           if($T16.match(getClass_1.getType(), $typeBindings)){
              final ISetWriter $setwriter0 = (ISetWriter)$RVF.setWriter();
              ;
              $SCOMP1_GEN1393:
              for(IValue $elem2_for : ((ISet)input_0)){
                  IValue $elem2 = (IValue) $elem2_for;
                  IValue e_2 = null;
                  $setwriter0.insert($RVF.tuple(((IValue)(((TypedFunctionInstance1<IValue, IValue>)getClass_1).typedCall(((IValue)($elem2))))), ((IValue)($elem2))));
              
              }
              
                          final IMap $result3 = ((IMap)($me.toMap(((ISet)($setwriter0.done())))));
              if($T30.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result3.getType(),$T30)){
                 return ((IMap)($result3));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(2255,59,<77,23>,<77,82>) 
    public IBool $CLOSURE_0(IValue a_0, IValue b_1, ValueRef<TypedFunctionInstance2<IValue, IValue, IValue>> similar_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T6.match(a_0.getType(), $typeBindings)){
           if($T7.match(b_1.getType(), $typeBindings)){
              if((((IBool)(((TypedFunctionInstance2<IValue, IValue, IValue>)similar_1.getValue()).typedCall(((IValue)a_0), ((IValue)b_1))))).getValue()){
                 final IBool $result5 = ((IBool)$constants.get(0)/*false*/);
                 if($T5.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result5.getType(),$T5)){
                    return ((IBool)($result5));
                 
                 } else {
                    return ((IBool)$constants.get(0)/*false*/);
                 
                 }
              } else {
                 final IBool $result5 = ((IBool)($less(((IValue)a_0),((IValue)b_1))));
                 if($T5.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result5.getType(),$T5)){
                    return ((IBool)($result5));
                 
                 } else {
                    return ((IBool)$constants.get(0)/*false*/);
                 
                 }
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(2414,36,<80,38>,<80,74>) 
    public IBool $CLOSURE_1(IValue a_0, ValueRef<IValue> h_4, ValueRef<TypedFunctionInstance2<IValue, IValue, IValue>> similar_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T6.match(a_0.getType(), $typeBindings)){
           final IBool $result6 = ((IBool)(((TypedFunctionInstance2<IValue, IValue, IValue>)similar_1.getValue()).typedCall(((IValue)a_0), h_4.getValue())));
           if($T5.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result6.getType(),$T5)){
              return ((IBool)($result6));
           
           } else {
              return ((IBool)$constants.get(0)/*false*/);
           
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(1411,1125,<53,0>,<85,1>) 
    public ISet Set_group$8f9750fd06d83138(ISet input_0, TypedFunctionInstance2<IValue, IValue, IValue> $aux_similar_1){ 
        ValueRef<TypedFunctionInstance2<IValue, IValue, IValue>> similar_1 = new ValueRef<TypedFunctionInstance2<IValue, IValue, IValue>>("similar_1", $aux_similar_1);
    
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(input_0.getType(), $typeBindings)){
           if($T4.match(similar_1.getValue().getType(), $typeBindings)){
              IList sinput_2 = ((IList)($me.sort(((ISet)input_0), new TypedFunctionInstance2<IValue,IValue,IValue>(($2255_0, $2255_1) -> { return $CLOSURE_0((IValue)$2255_0, (IValue)$2255_1, similar_1); }, $T34))));
              final IListWriter listwriter_WHILE0 = (IListWriter)$RVF.listWriter();
              /*muExists*/WHILE0_BT: 
                  do {
                      WHILE0:
                          while((((IBool)((((IBool)(M_List.isEmpty(((IList)sinput_2))))).not()))).getValue()){
                              final ValueRef<IValue> h_4 = new ValueRef<IValue>("h", ((IValue)(M_List.head(((IList)sinput_2)))));
                              IList sim_5 = ((IList)($elm_add_alist(h_4.getValue(),((IList)(M_List.takeWhile(((IList)(M_List.tail(((IList)sinput_2)))), new TypedFunctionInstance1<IValue,IValue>(($2414_0) -> { return $CLOSURE_1((IValue)$2414_0, h_4, similar_1); }, $T35)))))));
                              listwriter_WHILE0.append(M_List.toSet(((IList)sim_5)));
                              sinput_2 = ((IList)(M_List.drop(((IInteger)(M_List.size(((IList)sim_5)))), ((IList)sinput_2))));
                      
                          }
              
                  } while(false);
              IList lres_3 = ((IList)(listwriter_WHILE0.done()));
              final ISet $result7 = ((ISet)(M_List.toSet(((IList)lres_3))));
              if($T36.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result7.getType(),$T36)){
                 return ((ISet)($result7));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(2539,226,<88,0>,<98,1>) 
    public IMap Set_index$31fadea181d3071e(ISet s_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(s_0.getType(), $typeBindings)){
           IList sl_1 = ((IList)($me.toList(((ISet)s_0))));
           final IMapWriter $mapwriter8 = (IMapWriter)$RVF.mapWriter();
           $MCOMP9_GEN2747:
           for(IValue $elem10_for : ((IList)(M_List.index(((IList)sl_1))))){
               IInteger $elem10 = (IInteger) $elem10_for;
               IInteger i_2 = null;
               $mapwriter8.insert($RVF.tuple($alist_subscript_int(((IList)sl_1),((IInteger)($elem10)).intValue()), $elem10));
           
           }
           
                       final IMap $result11 = ((IMap)($mapwriter8.done()));
           if($T39.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result11.getType(),$T39)){
              return ((IMap)($result11));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(2772,267,<105,0>,<117,37>) 
    public IBool Set_isEmpty$42ff0d21e8590723(ISet st_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(st_0.getType(), $typeBindings)){
           final IBool $result12 = ((IBool)((IBool)$Prelude.isEmpty(st_0)));
           if($T5.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result12.getType(),$T5)){
              return ((IBool)($result12));
           
           } else {
              return ((IBool)$constants.get(0)/*false*/);
           
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(3042,358,<120,0>,<134,1>) 
    public ISet Set_mapper$24f74ceda05f6c34(ISet st_0, TypedFunctionInstance1<IValue, IValue> fn_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(st_0.getType(), $typeBindings)){
           if(true){
              if($T12.match(fn_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter13 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP14_GEN3384:
                    for(IValue $elem15_for : ((ISet)st_0)){
                        IValue $elem15 = (IValue) $elem15_for;
                        if($isSubtypeOf($elem15.getType(),$T37.instantiate($typeBindings))){
                           IValue elm_2 = null;
                           $setwriter13.insert(((TypedFunctionInstance1<IValue, IValue>)fn_1).typedCall(((IValue)($elem15))));
                        
                        } else {
                           continue $SCOMP14_GEN3384;
                        }
                    }
                    
                                final ISet $result16 = ((ISet)($setwriter13.done()));
                    if($T40.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result16.getType(),$T40)){
                       return ((ISet)($result16));
                    
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(3403,250,<137,0>,<148,1>) 
    public IValue Set_max$a286307ecf1dce32(ISet st_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(st_0.getType(), $typeBindings)){
           ITuple $TMP17 = (ITuple)($me.takeOneFrom(((ISet)st_0)));
           IValue h_1 = ((IValue)($atuple_subscript_int(((ITuple)($TMP17)),0)));
           ISet t_2 = ((ISet)($atuple_subscript_int(((ITuple)($TMP17)),1)));
           IValue $reducer19 = (IValue)(h_1);
           $REDUCER18_GEN3643:
           for(IValue $elem21_for : ((ISet)t_2)){
               IValue $elem21 = (IValue) $elem21_for;
               IValue e_4 = null;
               if((((IBool)($lessequal(((IValue)($elem21)),((IValue)($reducer19))).not()))).getValue()){
                  $reducer19 = ((IValue)($elem21));
               
               } else {
                  $reducer19 = ((IValue)($reducer19));
               
               }
           }
           
                       final IValue $result22 = ((IValue)($reducer19));
           if($T37.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result22.getType(),$T37)){
              return ((IValue)($result22));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(3656,357,<151,0>,<170,1>) 
    public IValue Set_min$68b6ebc4d32e8c20(ISet st_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(st_0.getType(), $typeBindings)){
           ITuple $TMP23 = (ITuple)($me.takeOneFrom(((ISet)st_0)));
           IValue h_1 = ((IValue)($atuple_subscript_int(((ITuple)($TMP23)),0)));
           ISet t_2 = ((ISet)($atuple_subscript_int(((ITuple)($TMP23)),1)));
           IValue $reducer25 = (IValue)(h_1);
           $REDUCER24_GEN4003:
           for(IValue $elem27_for : ((ISet)t_2)){
               IValue $elem27 = (IValue) $elem27_for;
               IValue e_4 = null;
               if((((IBool)($less(((IValue)($elem27)),((IValue)($reducer25)))))).getValue()){
                  $reducer25 = ((IValue)($elem27));
               
               } else {
                  $reducer25 = ((IValue)($reducer25));
               
               }
           }
           
                       final IValue $result28 = ((IValue)($reducer25));
           if($T37.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result28.getType(),$T37)){
              return ((IValue)($result28));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(4016,644,<173,0>,<206,1>) 
    public ISet Set_power$91b962acd7a7701a(ISet st_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(st_0.getType(), $typeBindings)){
           final IListWriter $writer29 = (IListWriter)$RVF.listWriter();
           $listwriter_splice($writer29,st_0);
           IList stl_1 = ((IList)($writer29.done()));
           IInteger i_2 = ((IInteger)$constants.get(1)/*0*/);
           final IListWriter listwriter_WHILE1 = (IListWriter)$RVF.listWriter();
           /*muExists*/WHILE1_BT: 
               do {
                   WHILE1:
                       while((((IBool)($aint_less_areal(((IInteger)i_2),((IReal)(M_util_Math.pow(((IInteger)$constants.get(2)/*2*/), ((IInteger)($me.size(((ISet)st_0))))))))))).getValue()){
                           IInteger j_4 = ((IInteger)i_2);
                           IInteger elIndex_5 = ((IInteger)$constants.get(1)/*0*/);
                           final IListWriter listwriter_WHILE2 = (IListWriter)$RVF.listWriter();
                           /*muExists*/WHILE2_BT: 
                               do {
                                   WHILE2:
                                       while((((IBool)($aint_lessequal_aint(((IInteger)j_4),((IInteger)$constants.get(1)/*0*/)).not()))).getValue()){
                                           if((((IBool)($equal(((IInteger)(((IInteger)j_4).mod(((IInteger)$constants.get(2)/*2*/)))), ((IInteger)$constants.get(3)/*1*/))))).getValue()){
                                              listwriter_WHILE2.append($alist_subscript_int(((IList)stl_1),((IInteger)elIndex_5).intValue()));
                                           
                                           }
                                           elIndex_5 = ((IInteger)($aint_add_aint(((IInteger)elIndex_5),((IInteger)$constants.get(3)/*1*/))));
                                           j_4 = ((IInteger)($aint_divide_aint(((IInteger)j_4),((IInteger)$constants.get(2)/*2*/))));
                                   
                                       }
                           
                               } while(false);
                           IList sub_6 = ((IList)(listwriter_WHILE2.done()));
                           final ISetWriter $writer30 = (ISetWriter)$RVF.setWriter();
                           ;
                           $setwriter_splice($writer30,sub_6);
                           listwriter_WHILE1.append($writer30.done());
                           i_2 = ((IInteger)($aint_add_aint(((IInteger)i_2),((IInteger)$constants.get(3)/*1*/))));
                   
                       }
           
               } while(false);
           IList res_3 = ((IList)(listwriter_WHILE1.done()));
           final ISetWriter $writer31 = (ISetWriter)$RVF.setWriter();
           ;
           $setwriter_splice($writer31,res_3);
           final ISet $result32 = ((ISet)($writer31.done()));
           if($T36.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result32.getType(),$T36)){
              return ((ISet)($result32));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(4663,258,<209,0>,<219,58>) 
    public ISet Set_power1$28a82373d451a91c(ISet st_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(st_0.getType(), $typeBindings)){
           final ISet $result33 = ((ISet)(((ISet)($me.power(((ISet)st_0)))).subtract(((ISet)$constants.get(4)/*{{}}*/))));
           if($T36.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result33.getType(),$T36)){
              return ((ISet)($result33));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(4923,477,<221,0>,<234,33>) 
    public IValue Set_reducer$549b0aa4c7a77384(ISet st_0, TypedFunctionInstance2<IValue, IValue, IValue> fn_1, IValue unit_2){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(st_0.getType(), $typeBindings)){
           if($T25.match(fn_1.getType(), $typeBindings)){
              if($T2.match(unit_2.getType(), $typeBindings)){
                 IValue $reducer35 = (IValue)(unit_2);
                 $REDUCER34_GEN5389:
                 for(IValue $elem36_for : ((ISet)st_0)){
                     IValue $elem36 = (IValue) $elem36_for;
                     IValue elm_4 = null;
                     $reducer35 = ((IValue)(((TypedFunctionInstance2<IValue, IValue, IValue>)fn_1).typedCall(((IValue)($reducer35)), ((IValue)($elem36)))));
                 
                 }
                 
                             final IValue $result37 = ((IValue)($reducer35));
                 if($T37.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result37.getType(),$T37)){
                    return ((IValue)($result37));
                 
                 } else {
                    return null;
                 }
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(5402,53,<236,0>,<236,53>) 
    public IValue Set_reducer$3562adf6c81d1f9f(ISet $__0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/reducer: 
            do {
                if($T3.match($__0.getType(), $typeBindings)){
                   if($__0.equals(((ISet)$constants.get(5)/*{}*/))){
                      throw new Throw($RVF.constructor(M_Exception.RuntimeException_EmptySet_, new IValue[]{}));
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(5458,239,<239,0>,<249,33>) 
    public IInteger Set_size$215788d71e8b2455(ISet st_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(st_0.getType(), $typeBindings)){
           final IInteger $result38 = ((IInteger)((IInteger)$Prelude.size(st_0)));
           if($T28.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result38.getType(),$T28)){
              return ((IInteger)($result38));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(5700,355,<252,0>,<258,1>) 
    public INumber Set_sum$059718d9ca35d7b7(ISet $__0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/sum: 
            do {
                if($T20.match($__0.getType(), $typeBindings)){
                   if($__0.equals(((ISet)$constants.get(5)/*{}*/))){
                      throw new Throw($RVF.constructor(M_Exception.RuntimeException_ArithmeticException_str, new IValue[]{((IString)$constants.get(6)/*"For the emtpy set it is not possible to decide the correct precision to return.
                      
                      If you want to call ..."*/)}));
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(6057,211,<260,0>,<269,25>) 
    public INumber Set_sum$2faf8190d541068a(ISet $0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/sum: 
            do {
                if($T23.match($0.getType(), $typeBindings)){
                   ISet $subject43 = (ISet)($0);
                   if(((ISet)($subject43)).size() >= 1){
                      sum_SET_VARe:
                      for(IValue $elem48_for : ((ISet)($subject43))){
                          INumber $elem48 = (INumber) $elem48_for;
                          INumber e_0 = ((INumber)($elem48));
                          final ISet $subject45 = ((ISet)(((ISet)($subject43)).delete(((INumber)e_0))));
                          sum_SET_VARe_MVARr:
                          for(IValue $elem47_for : new SubSetGenerator(((ISet)($subject45)))){
                              ISet $elem47 = (ISet) $elem47_for;
                              ISet r_1 = ((ISet)($elem47));
                              final ISet $subject46 = ((ISet)(((ISet)($subject45)).subtract(((ISet)($elem47)))));
                              if(((ISet)($subject46)).size() == 0){
                                 INumber $reducer40 = (INumber)(e_0);
                                 $REDUCER39_GEN6260:
                                 for(IValue $elem41_for : ((ISet)r_1)){
                                     INumber $elem41 = (INumber) $elem41_for;
                                     INumber i_3 = null;
                                     $reducer40 = ((INumber)($anum_add_anum(((INumber)($reducer40)),((INumber)($elem41)))));
                                 
                                 }
                                 
                                             final INumber $result42 = ((INumber)($reducer40));
                                 if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result42.getType(),$T42)){
                                    return ((INumber)($result42));
                                 
                                 } else {
                                    return null;
                                 }
                              } else {
                                 continue sum_SET_VARe_MVARr;/*set pat3*/
                              }
                          }
                          continue sum_SET_VARe;/*set pat4*/
                                      
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(6272,1208,<273,0>,<303,38>) 
    public IValue Set_getOneFrom$385242ba381fd613(ISet st_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(st_0.getType(), $typeBindings)){
           final IValue $result49 = ((IValue)((IValue)$Prelude.getOneFrom(st_0)));
           if($T37.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result49.getType(),$T37)){
              return ((IValue)($result49));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(7483,1333,<306,0>,<330,40>) 
    public IValue Set_getFirstFrom$f7e01cd92c21a6cb(ISet st_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(st_0.getType(), $typeBindings)){
           final IValue $result50 = ((IValue)((IValue)$Prelude.getFirstFrom(st_0)));
           if($T37.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result50.getType(),$T37)){
              return ((IValue)($result50));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(8818,995,<332,0>,<346,74>) 
    public IValue Set_getSingleFrom$42d5ad5d40a25ab7(ISet st_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(st_0.getType(), $typeBindings)){
           if((((IBool)($equal(((IInteger)($me.size(((ISet)st_0)))), ((IInteger)$constants.get(3)/*1*/))))).getValue()){
              final IValue $result51 = ((IValue)($me.getFirstFrom(((ISet)st_0))));
              if($T37.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result51.getType(),$T37)){
                 return ((IValue)($result51));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(9995,490,<353,0>,<367,55>) 
    public ITuple Set_takeOneFrom$291ddec83a7e9a61(ISet st_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(st_0.getType(), $typeBindings)){
           final ITuple $result52 = ((ITuple)((ITuple)$Prelude.takeOneFrom(st_0)));
           if($T43.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result52.getType(),$T43)){
              return ((ITuple)($result52));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(10489,196,<370,0>,<374,64>) 
    public ITuple Set_takeFirstFrom$b4e24a7c33f584da(ISet $0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/takeFirstFrom: 
            do {
                if($T27.match($0.getType(), $typeBindings)){
                   ISet $subject54 = (ISet)($0);
                   if(((ISet)($subject54)).size() >= 1){
                      takeFirstFrom_SET_VARf:
                      for(IValue $elem59_for : ((ISet)($subject54))){
                          IValue $elem59 = (IValue) $elem59_for;
                          IValue f_0 = ((IValue)($elem59));
                          final ISet $subject56 = ((ISet)(((ISet)($subject54)).delete(((IValue)f_0))));
                          takeFirstFrom_SET_VARf_MVARr:
                          for(IValue $elem58_for : new SubSetGenerator(((ISet)($subject56)))){
                              ISet $elem58 = (ISet) $elem58_for;
                              ISet r_1 = ((ISet)($elem58));
                              final ISet $subject57 = ((ISet)(((ISet)($subject56)).subtract(((ISet)($elem58)))));
                              if(((ISet)($subject57)).size() == 0){
                                 final ITuple $result53 = ((ITuple)($RVF.tuple(((IValue)f_0), ((ISet)r_1))));
                                 if($T43.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result53.getType(),$T43)){
                                    return ((ITuple)($result53));
                                 
                                 } else {
                                    return null;
                                 }
                              } else {
                                 continue takeFirstFrom_SET_VARf_MVARr;/*set pat3*/
                              }
                          }
                          continue takeFirstFrom_SET_VARf;/*set pat4*/
                                      
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(10688,75,<375,0>,<375,75>) 
    public ITuple Set_takeFirstFrom$dc2a600de1d8389f(ISet $__0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/takeFirstFrom: 
            do {
                if($T3.match($__0.getType(), $typeBindings)){
                   if($__0.equals(((ISet)$constants.get(5)/*{}*/))){
                      throw new Throw($RVF.constructor(M_Exception.RuntimeException_EmptySet_, new IValue[]{}));
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(10769,460,<378,0>,<394,43>) 
    public IList Set_toList$c29313189aeae08a(ISet st_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(st_0.getType(), $typeBindings)){
           final IListWriter $writer60 = (IListWriter)$RVF.listWriter();
           $listwriter_splice($writer60,st_0);
           final IList $result61 = ((IList)($writer60.done()));
           if($T44.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result61.getType(),$T44)){
              return ((IList)($result61));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(11232,447,<397,0>,<409,50>) 
    public IMap Set_toMap$5f7d7ee44cb2e11d(ISet st_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T8.match(st_0.getType(), $typeBindings)){
           final IMap $result62 = ((IMap)((IMap)$Prelude.toMap(st_0)));
           if($T45.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result62.getType(),$T45)){
              return ((IMap)($result62));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(11682,520,<412,0>,<427,70>) 
    public IMap Set_toMapUnique$fca205feb507456a(ISet st_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T8.match(st_0.getType(), $typeBindings)){
           final IMap $result63 = ((IMap)((IMap)$Prelude.toMapUnique(st_0)));
           if($T49.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result63.getType(),$T49)){
              return ((IMap)($result63));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(12205,389,<430,0>,<442,37>) 
    public IString Set_toString$2266fa6e4b318c58(ISet st_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(st_0.getType(), $typeBindings)){
           final IString $result64 = ((IString)((IString)$Prelude.toString(st_0)));
           if($T50.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result64.getType(),$T50)){
              return ((IString)($result64));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(12597,400,<445,0>,<457,38>) 
    public IString Set_itoString$d920014271a23230(ISet st_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(st_0.getType(), $typeBindings)){
           final IString $result65 = ((IString)((IString)$Prelude.itoString(st_0)));
           if($T50.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result65.getType(),$T50)){
              return ((IString)($result65));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(13759,34,<484,9>,<484,43>) 
    public IBool $CLOSURE_2(IValue a_0, IValue b_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T6.match(a_0.getType(), $typeBindings)){
           if($T7.match(b_1.getType(), $typeBindings)){
              final IBool $result66 = ((IBool)($less(((IValue)a_0),((IValue)b_1))));
              if($T5.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result66.getType(),$T5)){
                 return ((IBool)($result66));
              
              } else {
                 return ((IBool)$constants.get(0)/*false*/);
              
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(13001,795,<461,0>,<484,46>) 
    public IList Set_sort$2d7ce904b21febd4(ISet s_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(s_0.getType(), $typeBindings)){
           final IList $result67 = ((IList)($me.sort(((ISet)s_0), new TypedFunctionInstance2<IValue,IValue,IValue>(($13759_0, $13759_1) -> { return $CLOSURE_2((IValue)$13759_0, (IValue)$13759_1); }, $T34))));
           if($T44.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result67.getType(),$T44)){
              return ((IList)($result67));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(13799,104,<486,0>,<487,62>) 
    public IList Set_sort$4b3ff1abd5c398df(ISet l_0, TypedFunctionInstance2<IValue, IValue, IValue> less_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(l_0.getType(), $typeBindings)){
           if($T4.match(less_1.getType(), $typeBindings)){
              final IList $result68 = ((IList)((IList)$Prelude.sort(l_0, less_1)));
              if($T44.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result68.getType(),$T44)){
                 return ((IList)($result68));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(13906,676,<490,0>,<501,68>) 
    public IList Set_top$cbec7e0e8e8db73b(IInteger k_0, ISet l_1, TypedFunctionInstance2<IValue, IValue, IValue> less_2){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(l_1.getType(), $typeBindings)){
           if($T4.match(less_2.getType(), $typeBindings)){
              final IList $result69 = ((IList)((IList)$Prelude.top(k_0, l_1, less_2)));
              if($T44.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result69.getType(),$T44)){
                 return ((IList)($result69));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(14634,35,<503,50>,<503,85>) 
    public IBool $CLOSURE_3(IValue a_0, IValue b_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T6.match(a_0.getType(), $typeBindings)){
           if($T7.match(b_1.getType(), $typeBindings)){
              final IBool $result70 = ((IBool)($less(((IValue)a_0),((IValue)b_1))));
              if($T5.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result70.getType(),$T5)){
                 return ((IBool)($result70));
              
              } else {
                 return ((IBool)$constants.get(0)/*false*/);
              
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(14584,87,<503,0>,<503,87>) 
    public IList Set_top$3a2a3e2325787ee4(IInteger k_0, ISet l_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(l_1.getType(), $typeBindings)){
           final IList $result71 = ((IList)($me.top(((IInteger)k_0), ((ISet)l_1), new TypedFunctionInstance2<IValue,IValue,IValue>(($14634_0, $14634_1) -> { return $CLOSURE_3((IValue)$14634_0, (IValue)$14634_1); }, $T34))));
           if($T44.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result71.getType(),$T44)){
              return ((IList)($result71));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(14674,111,<506,0>,<507,59>) 
    public ISet Set_union$356d5959884f94d7(ISet sets_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T26.match(sets_0.getType(), $typeBindings)){
           final ISetWriter $setwriter72 = (ISetWriter)$RVF.setWriter();
           ;
           $SCOMP73_GEN14774:
           for(IValue $elem74_for : ((ISet)sets_0)){
               ISet $elem74 = (ISet) $elem74_for;
               ISet s_1 = null;
               $setwriter_splice($setwriter72,$elem74);
           
           }
           
                       final ISet $result75 = ((ISet)($setwriter72.done()));
           if($T38.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result75.getType(),$T38)){
              return ((ISet)($result75));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Set.rsc|(14788,136,<510,0>,<511,76>) 
    public IReal Set_jaccard$6b63114dcc758902(ISet x_0, ISet y_1){ 
        
        
        return ((IReal)($areal_divide_aint(((IReal)($areal_product_aint(((IReal)$constants.get(7)/*1.*/),((IInteger)($me.size(((ISet)(((ISet)x_0).intersect(((ISet)y_1)))))))))),((IInteger)($me.size(((ISet)($aset_add_aset(((ISet)x_0),((ISet)y_1))))))))));
    
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `Set`");
    }
}