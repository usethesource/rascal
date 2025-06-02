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
public class $List 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.$List_$I {

    private final $List_$I $me;
    private final IList $constants;
    
    
    public final rascal.$Exception M_Exception;
    public final rascal.$Map M_Map;

    
    final org.rascalmpl.library.Prelude $Prelude; // TODO: asBaseClassName will generate name collisions if there are more of the same name in different packages

    
    public final io.usethesource.vallang.type.Type $T1;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T10;	/*aparameter("T",avalue(),closed=false,alabel="h")*/
    public final io.usethesource.vallang.type.Type $T15;	/*aparameter("U",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T62;	/*aparameter("V",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T13;	/*aparameter("B",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T6;	/*aint()*/
    public final io.usethesource.vallang.type.Type $T31;	/*aparameter("T",avalue(),closed=false,alabel="_")*/
    public final io.usethesource.vallang.type.Type $T54;	/*aparameter("A",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T48;	/*aparameter("U",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T36;	/*aparameter("T",avalue(),closed=false,alabel="t")*/
    public final io.usethesource.vallang.type.Type $T23;	/*anum()*/
    public final io.usethesource.vallang.type.Type $T4;	/*aparameter("K",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T41;	/*aint(alabel="occurs")*/
    public final io.usethesource.vallang.type.Type $T38;	/*aparameter("T",avalue(),closed=false,alabel="b")*/
    public final io.usethesource.vallang.type.Type $T2;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T24;	/*aparameter("T",anum(),closed=false,alabel="hd")*/
    public final io.usethesource.vallang.type.Type $T12;	/*aparameter("A",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T17;	/*aparameter("T",avalue(),closed=false,alabel="f")*/
    public final io.usethesource.vallang.type.Type $T68;	/*aparameter("V",avalue(),closed=true,alabel="third")*/
    public final io.usethesource.vallang.type.Type $T7;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T44;	/*aparameter("T",avalue(),closed=true,alabel="element")*/
    public final io.usethesource.vallang.type.Type $T65;	/*aparameter("T",avalue(),closed=true,alabel="first")*/
    public final io.usethesource.vallang.type.Type $T55;	/*aparameter("B",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T40;	/*aparameter("T",avalue(),closed=false,alabel="element")*/
    public final io.usethesource.vallang.type.Type $T26;	/*aparameter("T",anum(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T29;	/*aparameter("T",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T66;	/*aparameter("U",avalue(),closed=true,alabel="second")*/
    public final io.usethesource.vallang.type.Type $T5;	/*aparameter("V",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T52;	/*aparameter("T",anum(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T20;	/*aparameter("T",avalue(),closed=false,alabel="a")*/
    public final io.usethesource.vallang.type.Type $T64;	/*alrel(atypeList([aparameter("T",avalue(),closed=true,alabel="first"),aparameter("U",avalue(),closed=true,alabel="second")]))*/
    public final io.usethesource.vallang.type.Type $T9;	/*alist(aparameter("T",avalue(),closed=false,alabel="h"))*/
    public final io.usethesource.vallang.type.Type $T39;	/*amap(aparameter("T",avalue(),closed=false,alabel="element"),aint(alabel="occurs"))*/
    public final io.usethesource.vallang.type.Type $T35;	/*alist(aparameter("T",avalue(),closed=false,alabel="t"))*/
    public final io.usethesource.vallang.type.Type $T25;	/*alist(aparameter("T",anum(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T42;	/*alist(aparameter("T",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T19;	/*abool()*/
    public final io.usethesource.vallang.type.Type $T37;	/*afunc(abool(),[aparameter("T",avalue(),closed=false,alabel="a"),aparameter("T",avalue(),closed=false,alabel="b")],[])*/
    public final io.usethesource.vallang.type.Type $T32;	/*alist(aparameter("U",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T11;	/*alrel(atypeList([aparameter("A",avalue(),closed=false),aparameter("B",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type $T30;	/*alist(aparameter("T",avalue(),closed=false,alabel="_"))*/
    public final io.usethesource.vallang.type.Type $T46;	/*alist(aint())*/
    public final io.usethesource.vallang.type.Type $T21;	/*alrel(atypeList([aparameter("T",avalue(),closed=false),aparameter("U",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type $T0;	/*alist(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T16;	/*alist(aparameter("T",avalue(),closed=false,alabel="f"))*/
    public final io.usethesource.vallang.type.Type $T8;	/*alist(avalue())*/
    public final io.usethesource.vallang.type.Type $T51;	/*atuple(atypeList([alist(aparameter("T",avalue(),closed=true)),alist(aparameter("T",avalue(),closed=true))]))*/
    public final io.usethesource.vallang.type.Type $T3;	/*amap(aparameter("K",avalue(),closed=false),aparameter("V",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T18;	/*afunc(abool(),[aparameter("T",avalue(),closed=false,alabel="a")],[])*/
    public final io.usethesource.vallang.type.Type $T14;	/*afunc(aparameter("U",avalue(),closed=false),[aparameter("T",avalue(),closed=false)],[])*/
    public final io.usethesource.vallang.type.Type $T50;	/*afunc(abool(),[aparameter("T",avalue(),closed=false,alabel="a"),aparameter("T",avalue(),closed=false,alabel="b")],[],returnsViaAllPath=true)*/
    public final io.usethesource.vallang.type.Type $T47;	/*alist(aparameter("U",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T56;	/*alist(aparameter("B",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T67;	/*alrel(atypeList([aparameter("T",avalue(),closed=true,alabel="first"),aparameter("U",avalue(),closed=true,alabel="second"),aparameter("V",avalue(),closed=true,alabel="third")]))*/
    public final io.usethesource.vallang.type.Type $T53;	/*amap(aparameter("A",avalue(),closed=true),alist(aparameter("B",avalue(),closed=true)))*/
    public final io.usethesource.vallang.type.Type $T57;	/*amap(aparameter("A",avalue(),closed=true),aparameter("B",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T59;	/*aset(aparameter("T",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T63;	/*alist(aparameter("V",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T58;	/*arel(atypeList([aparameter("T",avalue(),closed=true),aparameter("T",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T28;	/*afunc(aparameter("T",avalue(),closed=true),[aparameter("T",avalue(),closed=false),aparameter("T",avalue(),closed=false)],[])*/
    public final io.usethesource.vallang.type.Type $T43;	/*amap(aparameter("T",avalue(),closed=true,alabel="element"),aint(alabel="occurs"))*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T60;	/*atuple(atypeList([alist(aparameter("T",avalue(),closed=true)),alist(aparameter("U",avalue(),closed=true))]))*/
    public final io.usethesource.vallang.type.Type $T33;	/*alist(aparameter("V",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T61;	/*atuple(atypeList([alist(aparameter("T",avalue(),closed=true)),alist(aparameter("U",avalue(),closed=true)),alist(aparameter("V",avalue(),closed=true))]))*/
    public final io.usethesource.vallang.type.Type $T34;	/*alist(alist(aparameter("T",avalue(),closed=false)))*/
    public final io.usethesource.vallang.type.Type $T22;	/*alist(aparameter("T",anum(),closed=false,alabel="hd"))*/
    public final io.usethesource.vallang.type.Type $T49;	/*aset(alist(aparameter("T",avalue(),closed=true)))*/
    public final io.usethesource.vallang.type.Type $T45;	/*atuple(atypeList([aparameter("T",avalue(),closed=true),alist(aparameter("T",avalue(),closed=true))]))*/
    public final io.usethesource.vallang.type.Type $T27;	/*alrel(atypeList([aparameter("T",avalue(),closed=false),aparameter("U",avalue(),closed=false),aparameter("V",avalue(),closed=false)]))*/

    public $List(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $List(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($List_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.$List.class, this);
       
       mstore.importModule(rascal.$Exception.class, rex, rascal.$Exception::new);
       mstore.importModule(rascal.$Map.class, rex, rascal.$Map::new); 
       
       M_Exception = mstore.getModule(rascal.$Exception.class);
       M_Map = mstore.getModule(rascal.$Map.class); 
       
                          
       
       $TS.importStore(M_Exception.$TS);
       $TS.importStore(M_Map.$TS);
       
       $Prelude = $initLibrary("org.rascalmpl.library.Prelude"); 
    
       $constants = readBinaryConstantsFile(this.getClass(), "rascal//$List.constants", 11, "ec2d05959de6a65091904c172c026df2");
       ADT_RuntimeException = $adt("RuntimeException");
       $T1 = $TF.valueType();
       $T10 = $TF.parameterType("T", $T1);
       $T15 = $TF.parameterType("U", $T1);
       $T62 = $TF.parameterType("V", $T1);
       $T13 = $TF.parameterType("B", $T1);
       $T6 = $TF.integerType();
       $T31 = $TF.parameterType("T", $T1);
       $T54 = $TF.parameterType("A", $T1);
       $T48 = $TF.parameterType("U", $T1);
       $T36 = $TF.parameterType("T", $T1);
       $T23 = $TF.numberType();
       $T4 = $TF.parameterType("K", $T1);
       $T41 = $TF.integerType();
       $T38 = $TF.parameterType("T", $T1);
       $T2 = $TF.parameterType("T", $T1);
       $T24 = $TF.parameterType("T", $T23);
       $T12 = $TF.parameterType("A", $T1);
       $T17 = $TF.parameterType("T", $T1);
       $T68 = $TF.parameterType("V", $T1);
       $T7 = $TF.stringType();
       $T44 = $TF.parameterType("T", $T1);
       $T65 = $TF.parameterType("T", $T1);
       $T55 = $TF.parameterType("B", $T1);
       $T40 = $TF.parameterType("T", $T1);
       $T26 = $TF.parameterType("T", $T23);
       $T29 = $TF.parameterType("T", $T1);
       $T66 = $TF.parameterType("U", $T1);
       $T5 = $TF.parameterType("V", $T1);
       $T52 = $TF.parameterType("T", $T23);
       $T20 = $TF.parameterType("T", $T1);
       $T64 = $TF.listType($TF.tupleType($T65, $T66));
       $T9 = $TF.listType($T10);
       $T39 = $TF.mapType($T40, "element", $T41, "occurs");
       $T35 = $TF.listType($T36);
       $T25 = $TF.listType($T26);
       $T42 = $TF.listType($T29);
       $T19 = $TF.boolType();
       $T37 = $TF.functionType($T19, $TF.tupleType($T20, "a", $T38, "b"), $TF.tupleEmpty());
       $T32 = $TF.listType($T15);
       $T11 = $TF.listType($TF.tupleType($T12, $T13));
       $T30 = $TF.listType($T31);
       $T46 = $TF.listType($T6);
       $T21 = $TF.listType($TF.tupleType($T2, $T15));
       $T0 = $TF.listType($T2);
       $T16 = $TF.listType($T17);
       $T8 = $TF.listType($T1);
       $T51 = $TF.tupleType($T42, $T42);
       $T3 = $TF.mapType($T4,$T5);
       $T18 = $TF.functionType($T19, $TF.tupleType($T20, "a"), $TF.tupleEmpty());
       $T14 = $TF.functionType($T15, $TF.tupleType($T2), $TF.tupleEmpty());
       $T50 = $TF.functionType($T19, $TF.tupleType($T20, "a", $T38, "b"), $TF.tupleEmpty());
       $T47 = $TF.listType($T48);
       $T56 = $TF.listType($T55);
       $T67 = $TF.listType($TF.tupleType($T65, $T66, $T68));
       $T53 = $TF.mapType($T54,$T56);
       $T57 = $TF.mapType($T54,$T55);
       $T59 = $TF.setType($T29);
       $T63 = $TF.listType($T62);
       $T58 = $TF.setType($TF.tupleType($T29, $T29));
       $T28 = $TF.functionType($T29, $TF.tupleType($T2, $T2), $TF.tupleEmpty());
       $T43 = $TF.mapType($T44, "element", $T41, "occurs");
       $T60 = $TF.tupleType($T42, $T47);
       $T33 = $TF.listType($T5);
       $T61 = $TF.tupleType($T42, $T47, $T63);
       $T34 = $TF.listType($T0);
       $T22 = $TF.listType($T24);
       $T49 = $TF.setType($T42);
       $T45 = $TF.tupleType($T29, $T42);
       $T27 = $TF.listType($TF.tupleType($T2, $T15, $T5));
    
       
       
    }
    public IBool isEmpty(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IBool)List_isEmpty$fdfe8b76f8afe83f((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IBool)M_Map.Map_isEmpty$ed672b4b9c5f3bbb((IMap) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue last(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IValue)List_last$452c6357cf59d51d((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList shuffle(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)List_shuffle$33a2b58b4f0a6e3a((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList shuffle(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T6)){
         $result = (IList)List_shuffle$ef4eb9552ad766a9((IList) $P0, (IInteger) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IString intercalate(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T7) && $isSubtypeOf($P1Type,$T8)){
         $result = (IString)List_intercalate$6d96d640d678090f((IString) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IValue head(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T9)){
         $result = (IValue)List_head$d783f64b0cdf56cc((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IValue)List_head$0a8ce23c83613597((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList head(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T6)){
         $result = (IList)List_head$31bed95364700c65((IList) $P0, (IInteger) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IInteger size(IValue $P0){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IInteger)List_size$ba7443328d8b4a27((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IInteger)M_Map.Map_size$9404b041dab68eb5((IMap) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IMap toMap(IValue $P0){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T11)){
         $result = (IMap)List_toMap$795bdddf805b0c4b((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList mapper(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T14)){
         $result = (IList)List_mapper$12df1ca4a10ff2b1((IList) $P0, (TypedFunctionInstance1<IValue, IValue>) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IList reverse(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)List_reverse$9dfd2c061e6148ac((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList remove(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T6)){
         $result = (IList)List_remove$6047315caa154842((IList) $P0, (IInteger) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IValue max(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T9)){
         $result = (IValue)List_max$c8bc65c8275c2ea6((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IValue)List_max$4d35fec0913b9adc((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IMap distribution(IValue $P0){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IMap)List_distribution$fe0e5f52b44a463f((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue getFirstFrom(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T16)){
         $result = (IValue)List_getFirstFrom$8b59769acd262783((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IValue)List_getFirstFrom$ecaac22228d5233c((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet toSet(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ISet)List_toSet$7a7d987a16d99f97((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isSorted(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IBool)List_isSorted$293ad5967a3bf1ea((IList) $P0, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList take(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T6) && $isSubtypeOf($P1Type,$T0)){
         $result = (IList)List_take$95b89daedfc23844((IInteger) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ITuple takeOneFrom(IValue $P0){ // Generated by Resolver
       ITuple $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ITuple)List_takeOneFrom$48bb3b6062ea97b1((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IInteger indexOf(IValue $P0, IValue $P1){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T2)){
         $result = (IInteger)List_indexOf$27a7fd44855c0cd1((IList) $P0, (IValue) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IList takeWhile(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T18)){
         $result = (IList)List_takeWhile$557e76d9c8e487c3((IList) $P0, (TypedFunctionInstance1<IValue, IValue>) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ITuple unzip2(IValue $P0){ // Generated by Resolver
       ITuple $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T21)){
         $result = (ITuple)List_unzip2$2f0030d08b0b515c((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList delete(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T6)){
         $result = (IList)List_delete$d7e38e0bb055d671((IList) $P0, (IInteger) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public INumber sum(IValue $P0){ // Generated by Resolver
       INumber $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T22)){
         $result = (INumber)List_sum$9ba9391d87d7cdce((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T25)){
         $result = (INumber)List_sum$7fab443836973776((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ITuple unzip3(IValue $P0){ // Generated by Resolver
       ITuple $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T27)){
         $result = (ITuple)List_unzip3$70df47ebf833367b((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList insertAt(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T6) && $isSubtypeOf($P2Type,$T2)){
         $result = (IList)List_insertAt$983b3761096c321e((IList) $P0, (IInteger) $P1, (IValue) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IString toString(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)List_toString$a04f1c5d8efcd2e2((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue reducer(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T28) && $isSubtypeOf($P2Type,$T2)){
         $result = (IValue)List_reducer$13d810a90f3d6575((IList) $P0, (TypedFunctionInstance2<IValue, IValue, IValue>) $P1, (IValue) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public ISet permutations(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ISet)List_permutations$c819f16d8907d74b((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList drop(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T6) && $isSubtypeOf($P1Type,$T0)){
         $result = (IList)List_drop$c77fb9b1073eace8((IInteger) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IValue elementAt(IValue $P0, IValue $P1){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T6)){
         $result = (IValue)List_elementAt$e76f0a052171ebb6((IList) $P0, (IInteger) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ITuple headTail(IValue $P0){ // Generated by Resolver
       ITuple $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T9)){
         $result = (ITuple)List_headTail$b593cd2ebbc70fb1((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ITuple)List_headTail$bb288b37efeecc6b((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList upTill(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T6)){
         $result = (IList)List_upTill$fafbd14901ea615f((IInteger) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList tail(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T30)){
         $result = (IList)List_tail$a4bd36b1f369026a((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)List_tail$33110d2c64b6f3cf((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList tail(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T6)){
         $result = (IList)List_tail$28d274b499bfa1ce((IList) $P0, (IInteger) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IList zip2(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T32)){
         $result = (IList)List_zip2$5cf6b97a195b0a20((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ITuple pop(IValue $P0){ // Generated by Resolver
       ITuple $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ITuple)List_pop$564116ba8d629906((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IMap toMapUnique(IValue $P0){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T11)){
         $result = (IMap)List_toMapUnique$1ee65c954c28c774((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList index(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)List_index$90228c781d131b76((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList slice(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T6) && $isSubtypeOf($P2Type,$T6)){
         $result = (IList)List_slice$dac0d5be581790d0((IList) $P0, (IInteger) $P1, (IInteger) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IValue min(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T9)){
         $result = (IValue)List_min$86af046c90057650((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IValue)List_min$acb3d58fffd8b2f1((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList prefix(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)List_prefix$09e7c5b6eb264f3e((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList zip3(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T32) && $isSubtypeOf($P2Type,$T33)){
         $result = (IList)List_zip3$cd5c8c83ab2f56d9((IList) $P0, (IList) $P1, (IList) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IInteger lastIndexOf(IValue $P0, IValue $P1){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T2)){
         $result = (IInteger)List_lastIndexOf$66d10d5e9466bed6((IList) $P0, (IValue) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IList concat(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T34)){
         $result = (IList)List_concat$e1275851155358ce((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue top(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T35)){
         $result = (IValue)List_top$7d8d56824622aa16((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString itoString(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)List_itoString$dc18c931a359f8dc((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList intersperse(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T2) && $isSubtypeOf($P1Type,$T0)){
         $result = (IList)List_intersperse$2082f04fb71c32f9((IValue) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IList merge(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0) && $isSubtypeOf($P2Type,$T37)){
         $result = (IList)List_merge$0532f44b2eda13d8((IList) $P0, (IList) $P1, (TypedFunctionInstance2<IValue, IValue, IValue>) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IList merge(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IList)List_merge$587ced50ba87c4a7((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IValue getOneFrom(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IValue)List_getOneFrom$4d823dc007dd1cd9((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IMap removeFromBag(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T39) && $isSubtypeOf($P1Type,$T2) && $isSubtypeOf($P2Type,$T6)){
         $result = (IMap)List_removeFromBag$5c4ef2614668a761((IMap) $P0, (IValue) $P1, (IInteger) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IMap removeFromBag(IValue $P0, IValue $P1){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T39) && $isSubtypeOf($P1Type,$T2)){
         $result = (IMap)List_removeFromBag$3427b7346e20c720((IMap) $P0, (IValue) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ITuple split(IValue $P0){ // Generated by Resolver
       ITuple $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ITuple)List_split$19c747b75c8a251d((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList push(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T2) && $isSubtypeOf($P1Type,$T0)){
         $result = (IList)List_push$a31a54c2a21ec30e((IValue) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ISet permutationsBag(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T39)){
         $result = (ISet)List_permutationsBag$8b2de212b4f23a16((IMap) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList mix(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IList)List_mix$5b11827cf2a66f4b((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ISet toRel(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ISet)List_toRel$87081beb0169e3cf((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList sort(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)List_sort$1fe4426c8c8039da((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList sort(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T37)){
         $result = (IList)List_sort$a9bbc6fca4e60d0a((IList) $P0, (TypedFunctionInstance2<IValue, IValue, IValue>) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IList dup(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)List_dup$668836823e08d219((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(681,249,<24,0>,<36,31>) 
    public IList List_concat$e1275851155358ce(IList xxs_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T34.match(xxs_0.getType(), $typeBindings)){
           if(true){
              final IListWriter $listwriter0 = (IListWriter)$RVF.listWriter();
              $LCOMP1_GEN910:
              for(IValue $elem2_for : ((IList)xxs_0)){
                  IList $elem2 = (IList) $elem2_for;
                  if($isSubtypeOf($elem2.getType(),$T42.instantiate($typeBindings))){
                     IList xs_1 = null;
                     $listwriter_splice($listwriter0,$elem2);
                  
                  } else {
                     continue $LCOMP1_GEN910;
                  }
              }
              
                          final IList $result3 = ((IList)($listwriter0.done()));
              if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result3.getType(),$T42)){
                 return ((IList)($result3));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(933,432,<39,0>,<52,42>) 
    public IList List_delete$d7e38e0bb055d671(IList lst_0, IInteger n_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final IList $result4 = ((IList)((IList)$Prelude.delete(lst_0, n_1)));
           if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result4.getType(),$T42)){
              return ((IList)($result4));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(1368,372,<55,0>,<69,1>) 
    public IMap List_distribution$fe0e5f52b44a463f(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           if(true){
              IMap res_1 = ((IMap)$constants.get(0)/*()*/);
              /*muExists*/FOR0: 
                  do {
                      FOR0_GEN1677:
                      for(IValue $elem5_for : ((IList)lst_0)){
                          IValue $elem5 = (IValue) $elem5_for;
                          IValue e_2 = null;
                          GuardedIValue guarded1 = $guarded_map_subscript(((IMap)res_1),((IValue)($elem5)));
                          res_1 = ((IMap)($amap_update($elem5,$aint_add_aint(((IInteger)(($is_defined_value(guarded1) ? ((IInteger)$get_defined_value(guarded1)) : ((IInteger)$constants.get(1)/*0*/)))),((IInteger)$constants.get(2)/*1*/)), ((IMap)(((IMap)res_1))))));
                      
                      }
                      continue FOR0;
                                  
                  } while(false);
              /* void:  muCon([]) */final IMap $result6 = ((IMap)res_1);
              if($T43.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result6.getType(),$T43)){
                 return ((IMap)($result6));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(1744,437,<73,0>,<87,41>) 
    public IList List_drop$c77fb9b1073eace8(IInteger n_0, IList lst_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_1.getType(), $typeBindings)){
           final IList $result7 = ((IList)((IList)$Prelude.drop(n_0, lst_1)));
           if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result7.getType(),$T42)){
              return ((IList)($result7));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(2183,251,<89,0>,<97,54>) 
    public IList List_dup$668836823e08d219(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           if(true){
              IList $reducer9 = (IList)(((IList)$constants.get(3)/*[]*/));
              $REDUCER8_GEN2420:
              for(IValue $elem11_for : ((IList)lst_0)){
                  IValue $elem11 = (IValue) $elem11_for;
                  if($isSubtypeOf($elem11.getType(),$T29.instantiate($typeBindings))){
                     IValue ix_2 = null;
                     if((((IBool)($RVF.bool(((IList)($reducer9)).contains(((IValue)($elem11))))))).getValue()){
                        $reducer9 = ((IList)($reducer9));
                     
                     } else {
                        $reducer9 = ((IList)($alist_add_alist(((IList)($reducer9)),((IList)($RVF.list(((IValue)($elem11))))))));
                     
                     }
                  } else {
                     continue $REDUCER8_GEN2420;
                  }
              }
              
                          final IList $result12 = ((IList)($reducer9));
              if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result12.getType(),$T42)){
                 return ((IList)($result12));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(2436,374,<99,0>,<110,43>) 
    public IValue List_elementAt$e76f0a052171ebb6(IList lst_0, IInteger index_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final IValue $result13 = ((IValue)((IValue)$Prelude.elementAt(lst_0, index_1)));
           if($T29.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result13.getType(),$T29)){
              return ((IValue)($result13));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(2814,454,<113,0>,<126,33>) 
    public IValue List_getOneFrom$4d823dc007dd1cd9(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final IValue $result14 = ((IValue)((IValue)$Prelude.getOneFrom(lst_0)));
           if($T29.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result14.getType(),$T29)){
              return ((IValue)($result14));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(3271,223,<129,0>,<133,35>) 
    public IValue List_getFirstFrom$8b59769acd262783(IList $0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/getFirstFrom: 
            do {
                if($T16.match($0.getType(), $typeBindings)){
                   final IList $subject18 = ((IList)$0);
                   int $subject18_cursor = 0;
                   if($isSubtypeOf($subject18.getType(),$T16)){
                      final int $subject18_len = (int)((IList)($subject18)).length();
                      if($subject18_len >= 1){
                         if($subject18_cursor < $subject18_len){
                            IValue f_0 = ((IValue)($alist_subscript_int(((IList)($subject18)),$subject18_cursor)));
                            $subject18_cursor += 1;
                            final int $__119_start = (int)$subject18_cursor;
                            final int $__119_len = (int)$subject18_len - $__119_start - 0;
                            $subject18_cursor = $__119_start + $__119_len;
                            /*muExists*/getFirstFrom_LIST_VARf_MVAR$_16: 
                                do {
                                    if($subject18_cursor == $subject18_len){
                                       final IValue $result17 = ((IValue)f_0);
                                       if($T29.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result17.getType(),$T29)){
                                          return ((IValue)($result17));
                                       
                                       } else {
                                          return null;
                                       }
                                    } else {
                                       continue getFirstFrom_LIST_VARf_MVAR$_16;/*list match1*/
                                    }
                                } while(false);
                            return null;
                         } else {
                            return null;
                         }
                      } else {
                         return null;
                      }
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(3495,54,<134,0>,<134,54>) 
    public IValue List_getFirstFrom$ecaac22228d5233c(IList $__0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/getFirstFrom: 
            do {
                if($T0.match($__0.getType(), $typeBindings)){
                   if($__0.equals(((IList)$constants.get(3)/*[]*/))){
                      throw new Throw($RVF.constructor(M_Exception.RuntimeException_EmptyList_, new IValue[]{}));
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(3552,877,<137,0>,<167,27>) 
    public IValue List_head$d783f64b0cdf56cc(IList $0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/head: 
            do {
                if($T9.match($0.getType(), $typeBindings)){
                   final IList $subject23 = ((IList)$0);
                   int $subject23_cursor = 0;
                   if($isSubtypeOf($subject23.getType(),$T9)){
                      final int $subject23_len = (int)((IList)($subject23)).length();
                      if($subject23_len >= 1){
                         if($subject23_cursor < $subject23_len){
                            IValue h_0 = ((IValue)($alist_subscript_int(((IList)($subject23)),$subject23_cursor)));
                            $subject23_cursor += 1;
                            final int $__124_start = (int)$subject23_cursor;
                            final int $__124_len = (int)$subject23_len - $__124_start - 0;
                            $subject23_cursor = $__124_start + $__124_len;
                            /*muExists*/head_LIST_VARh_MVAR$_21: 
                                do {
                                    if($subject23_cursor == $subject23_len){
                                       final IValue $result22 = ((IValue)h_0);
                                       if($T29.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result22.getType(),$T29)){
                                          return ((IValue)($result22));
                                       
                                       } else {
                                          return null;
                                       }
                                    } else {
                                       continue head_LIST_VARh_MVAR$_21;/*list match1*/
                                    }
                                } while(false);
                            return null;
                         } else {
                            return null;
                         }
                      } else {
                         return null;
                      }
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(4431,45,<168,0>,<168,45>) 
    public IValue List_head$0a8ce23c83613597(IList $__0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/head: 
            do {
                if($T0.match($__0.getType(), $typeBindings)){
                   if($__0.equals(((IList)$constants.get(3)/*[]*/))){
                      throw new Throw($RVF.constructor(M_Exception.RuntimeException_EmptyList_, new IValue[]{}));
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(4516,106,<171,0>,<172,64>) 
    public IList List_head$31bed95364700c65(IList lst_0, IInteger n_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final IList $result25 = ((IList)((IList)$Prelude.head(lst_0, n_1)));
           if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result25.getType(),$T42)){
              return ((IList)($result25));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(4626,297,<176,0>,<188,53>) 
    public ITuple List_headTail$b593cd2ebbc70fb1(IList $0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/headTail: 
            do {
                if($T9.match($0.getType(), $typeBindings)){
                   final IList $subject27 = ((IList)$0);
                   int $subject27_cursor = 0;
                   if($isSubtypeOf($subject27.getType(),$T9)){
                      final int $subject27_len = (int)((IList)($subject27)).length();
                      if($subject27_len >= 1){
                         if($subject27_cursor < $subject27_len){
                            IValue h_0 = ((IValue)($alist_subscript_int(((IList)($subject27)),$subject27_cursor)));
                            $subject27_cursor += 1;
                            final int $t_128_start = (int)$subject27_cursor;
                            headTail_LIST_VARh_MVARt:
                            
                            for(int $t_128_len = 0; $t_128_len <= $subject27_len - $t_128_start - 0; $t_128_len += 1){
                               IList t_1 = ((IList)($subject27.sublist($t_128_start, $t_128_len)));
                               $subject27_cursor = $t_128_start + $t_128_len;
                               if($subject27_cursor == $subject27_len){
                                  final ITuple $result26 = ((ITuple)($RVF.tuple(((IValue)h_0), ((IList)t_1))));
                                  if($T45.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result26.getType(),$T45)){
                                     return ((ITuple)($result26));
                                  
                                  } else {
                                     return null;
                                  }
                               } else {
                                  continue headTail_LIST_VARh_MVARt;/*list match1*/
                               }
                            }
                            return null;
                         
                         } else {
                            return null;
                         }
                      } else {
                         return null;
                      }
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(4925,66,<189,0>,<189,66>) 
    public ITuple List_headTail$bb288b37efeecc6b(IList $__0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/headTail: 
            do {
                if($T0.match($__0.getType(), $typeBindings)){
                   if($__0.equals(((IList)$constants.get(3)/*[]*/))){
                      throw new Throw($RVF.constructor(M_Exception.RuntimeException_EmptyList_, new IValue[]{}));
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(4994,354,<192,0>,<206,50>) 
    public IList List_index$90228c781d131b76(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final IList $result29 = ((IList)($me.upTill(((IInteger)($me.size(((IList)lst_0)))))));
           if($T46.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result29.getType(),$T46)){
              return ((IList)($result29));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(5352,518,<210,0>,<229,1>) 
    public IInteger List_indexOf$27a7fd44855c0cd1(IList lst_0, IValue elt_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           if(true){
              if($T2.match(elt_1.getType(), $typeBindings)){
                 if(true){
                    /*muExists*/FOR2: 
                        do {
                            final IInteger $lst2 = ((IInteger)($me.size(((IList)lst_0))));
                            final boolean $dir3 = ((IInteger)$constants.get(1)/*0*/).less($lst2).getValue();
                            
                            FOR2_GEN5797:
                            for(IInteger $elem30 = ((IInteger)$constants.get(1)/*0*/); $dir3 ? $aint_less_aint($elem30,$lst2).getValue() 
                                                      : $aint_lessequal_aint($elem30,$lst2).not().getValue(); $elem30 = $aint_add_aint($elem30,$dir3 ? ((IInteger)$constants.get(2)/*1*/) : ((IInteger)$constants.get(4)/*-1*/))){
                                if($isSubtypeOf($elem30.getType(),$T6.instantiate($typeBindings))){
                                   IInteger i_2 = null;
                                   if((((IBool)($equal(((IValue)($alist_subscript_int(((IList)lst_0),((IInteger)($elem30)).intValue()))), ((IValue)elt_1))))).getValue()){
                                      final IInteger $result31 = ((IInteger)($elem30));
                                      if($T6.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result31.getType(),$T6)){
                                         return ((IInteger)($result31));
                                      
                                      } else {
                                         return null;
                                      }
                                   }
                                
                                } else {
                                   continue FOR2_GEN5797;
                                }}
                            continue FOR2;
                    
                        } while(false);
                    /* void:  muCon([]) */final IInteger $result31 = ((IInteger)$constants.get(4)/*-1*/);
                    if($T6.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result31.getType(),$T6)){
                       return ((IInteger)($result31));
                    
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(5873,549,<232,0>,<248,76>) 
    public IList List_insertAt$983b3761096c321e(IList lst_0, IInteger n_1, IValue elm_2){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           if($T2.match(elm_2.getType(), $typeBindings)){
              final IList $result32 = ((IList)((IList)$Prelude.insertAt(lst_0, n_1, elm_2)));
              if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result32.getType(),$T42)){
                 return ((IList)($result32));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(6425,366,<251,0>,<262,58>) 
    public IString List_intercalate$6d96d640d678090f(IString sep_0, IList l_1){ 
        
        
        final Template $template33 = (Template)new Template($RVF, "");
        /*muExists*/LAB4: 
            do {
                LAB4_GEN6741:
                for(IValue $elem35_for : ((IList)($me.index(((IList)l_1))))){
                    IInteger $elem35 = (IInteger) $elem35_for;
                    IInteger i_2 = null;
                    ;$template33.addStr(((IString)(((((IBool)($equal(((IInteger)($elem35)), ((IInteger)$constants.get(1)/*0*/))))).getValue() ? ((IString)$constants.get(5)/*""*/) : sep_0))).getValue());
                    ;$template33.addVal($alist_subscript_int(((IList)l_1),((IInteger)($elem35)).intValue()));
                
                }
                continue LAB4;
                            
            } while(false);
        return ((IString)($template33.close()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(6794,319,<265,0>,<276,66>) 
    public IList List_intersperse$2082f04fb71c32f9(IValue sep_0, IList xs_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T2.match(sep_0.getType(), $typeBindings)){
           if($T0.match(xs_1.getType(), $typeBindings)){
              if((((IBool)($me.isEmpty(((IList)xs_1))))).getValue()){
                 final IList $result40 = ((IList)$constants.get(3)/*[]*/);
                 if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result40.getType(),$T42)){
                    return ((IList)($result40));
                 
                 } else {
                    return null;
                 }
              } else {
                 IList $reducer38 = (IList)($RVF.list(((IValue)($me.head(((IList)xs_1))))));
                 $REDUCER37_GEN7098:
                 for(IValue $elem39_for : ((IList)($me.tail(((IList)xs_1))))){
                     IValue $elem39 = (IValue) $elem39_for;
                     IValue x_3 = null;
                     $reducer38 = ((IList)($alist_add_alist(((IList)($reducer38)),((IList)($RVF.list(((IValue)sep_0), $elem39))))));
                 
                 }
                 
                             final IList $result40 = ((IList)($reducer38));
                 if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result40.getType(),$T42)){
                    return ((IList)($result40));
                 
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
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(7116,269,<279,0>,<291,32>) 
    public IBool List_isEmpty$fdfe8b76f8afe83f(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final IBool $result41 = ((IBool)((IBool)$Prelude.isEmpty(lst_0)));
           if($T19.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result41.getType(),$T19)){
              return ((IBool)($result41));
           
           } else {
              return ((IBool)$constants.get(6)/*false*/);
           
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(7388,388,<294,0>,<308,44>) 
    public IValue List_last$452c6357cf59d51d(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final IValue $result42 = ((IValue)((IValue)$Prelude.last(lst_0)));
           if($T29.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result42.getType(),$T29)){
              return ((IValue)($result42));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(7778,425,<310,0>,<327,1>) 
    public IInteger List_lastIndexOf$66d10d5e9466bed6(IList lst_0, IValue elt_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           if(true){
              if($T2.match(elt_1.getType(), $typeBindings)){
                 if(true){
                    /*muExists*/FOR5: 
                        do {
                            FOR5_GEN8129:
                            for(IValue $elem43_for : ((IList)($me.reverse(((IList)($me.index(((IList)lst_0)))))))){
                                IInteger $elem43 = (IInteger) $elem43_for;
                                IInteger i_2 = null;
                                if((((IBool)($equal(((IValue)($alist_subscript_int(((IList)lst_0),((IInteger)($elem43)).intValue()))), ((IValue)elt_1))))).getValue()){
                                   final IInteger $result44 = ((IInteger)($elem43));
                                   if($T6.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result44.getType(),$T6)){
                                      return ((IInteger)($result44));
                                   
                                   } else {
                                      return null;
                                   }
                                }
                            
                            }
                            continue FOR5;
                                        
                        } while(false);
                    /* void:  muCon([]) */final IInteger $result44 = ((IInteger)$constants.get(4)/*-1*/);
                    if($T6.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result44.getType(),$T6)){
                       return ((IInteger)($result44));
                    
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(8206,351,<330,0>,<341,71>) 
    public IList List_mapper$12df1ca4a10ff2b1(IList lst_0, TypedFunctionInstance1<IValue, IValue> fn_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           if(true){
              if($T14.match(fn_1.getType(), $typeBindings)){
                 if(true){
                    final IListWriter $listwriter45 = (IListWriter)$RVF.listWriter();
                    $LCOMP46_GEN8542:
                    for(IValue $elem47_for : ((IList)lst_0)){
                        IValue $elem47 = (IValue) $elem47_for;
                        if($isSubtypeOf($elem47.getType(),$T29.instantiate($typeBindings))){
                           IValue elm_2 = null;
                           $listwriter45.append(((TypedFunctionInstance1<IValue, IValue>)fn_1).typedCall(((IValue)($elem47))));
                        
                        } else {
                           continue $LCOMP46_GEN8542;
                        }
                    }
                    
                                final IList $result48 = ((IList)($listwriter45.done()));
                    if($T47.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result48.getType(),$T47)){
                       return ((IList)($result48));
                    
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(8560,219,<344,0>,<352,55>) 
    public IValue List_max$c8bc65c8275c2ea6(IList $0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/max: 
            do {
                if($T9.match($0.getType(), $typeBindings)){
                   final IList $subject54 = ((IList)$0);
                   int $subject54_cursor = 0;
                   if($isSubtypeOf($subject54.getType(),$T9)){
                      final int $subject54_len = (int)((IList)($subject54)).length();
                      if($subject54_len >= 1){
                         if($subject54_cursor < $subject54_len){
                            IValue h_0 = ((IValue)($alist_subscript_int(((IList)($subject54)),$subject54_cursor)));
                            $subject54_cursor += 1;
                            final int $t_155_start = (int)$subject54_cursor;
                            max_LIST_VARh_MVARt:
                            
                            for(int $t_155_len = 0; $t_155_len <= $subject54_len - $t_155_start - 0; $t_155_len += 1){
                               IList t_1 = ((IList)($subject54.sublist($t_155_start, $t_155_len)));
                               $subject54_cursor = $t_155_start + $t_155_len;
                               if($subject54_cursor == $subject54_len){
                                  IValue $reducer50 = (IValue)(h_0);
                                  $REDUCER49_GEN8771:
                                  for(IValue $elem52_for : ((IList)t_1)){
                                      IValue $elem52 = (IValue) $elem52_for;
                                      IValue e_3 = null;
                                      if((((IBool)($lessequal(((IValue)($elem52)),((IValue)($reducer50))).not()))).getValue()){
                                         $reducer50 = ((IValue)($elem52));
                                      
                                      } else {
                                         $reducer50 = ((IValue)($reducer50));
                                      
                                      }
                                  }
                                  
                                              final IValue $result53 = ((IValue)($reducer50));
                                  if($T29.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result53.getType(),$T29)){
                                     return ((IValue)($result53));
                                  
                                  } else {
                                     return null;
                                  }
                               } else {
                                  continue max_LIST_VARh_MVARt;/*list match1*/
                               }
                            }
                            return null;
                         
                         } else {
                            return null;
                         }
                      } else {
                         return null;
                      }
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(8780,44,<353,0>,<353,44>) 
    public IValue List_max$4d35fec0913b9adc(IList $__0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/max: 
            do {
                if($T0.match($__0.getType(), $typeBindings)){
                   if($__0.equals(((IList)$constants.get(3)/*[]*/))){
                      throw new Throw($RVF.constructor(M_Exception.RuntimeException_EmptyList_, new IValue[]{}));
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(8828,997,<356,0>,<384,1>) 
    public IList List_merge$587ced50ba87c4a7(IList left_0, IList right_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(left_0.getType(), $typeBindings)){
           if($T0.match(right_1.getType(), $typeBindings)){
              final IListWriter listwriter_WHILE7 = (IListWriter)$RVF.listWriter();
              /*muExists*/WHILE7_BT: 
                  do {
                      WHILE7:
                          while((((IBool)((((IBool)($me.isEmpty(((IList)left_0))))).not()))).getValue()){
                              if((((IBool)((((IBool)($me.isEmpty(((IList)right_1))))).not()))).getValue()){
                                if((((IBool)($lessequal(((IValue)($me.head(((IList)left_0)))),((IValue)($me.head(((IList)right_1)))))))).getValue()){
                                   listwriter_WHILE7.append($me.head(((IList)left_0)));
                                   left_0 = ((IList)($me.tail(((IList)left_0))));
                                
                                } else {
                                   listwriter_WHILE7.append($me.head(((IList)right_1)));
                                   right_1 = ((IList)($me.tail(((IList)right_1))));
                                
                                }
                              } else {
                                break WHILE7; // muBreak
                              
                              }
                      
                          }
              
                  } while(false);
              IList res_2 = ((IList)(listwriter_WHILE7.done()));
              final IList $result56 = ((IList)($alist_add_alist(((IList)($alist_add_alist(((IList)res_2),((IList)left_0)))),((IList)right_1))));
              if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result56.getType(),$T42)){
                 return ((IList)($result56));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(9827,331,<386,0>,<397,1>) 
    public IList List_merge$0532f44b2eda13d8(IList left_0, IList right_1, TypedFunctionInstance2<IValue, IValue, IValue> lessOrEqual_2){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(left_0.getType(), $typeBindings)){
           if($T0.match(right_1.getType(), $typeBindings)){
              if($T37.match(lessOrEqual_2.getType(), $typeBindings)){
                 final IListWriter listwriter_WHILE9 = (IListWriter)$RVF.listWriter();
                 /*muExists*/WHILE9_BT: 
                     do {
                         WHILE9:
                             while((((IBool)((((IBool)($me.isEmpty(((IList)left_0))))).not()))).getValue()){
                                 if((((IBool)((((IBool)($me.isEmpty(((IList)right_1))))).not()))).getValue()){
                                   if((((IBool)(((TypedFunctionInstance2<IValue, IValue, IValue>)lessOrEqual_2).typedCall(((IValue)($me.head(((IList)left_0)))), ((IValue)($me.head(((IList)right_1)))))))).getValue()){
                                      listwriter_WHILE9.append($me.head(((IList)left_0)));
                                      left_0 = ((IList)($me.tail(((IList)left_0))));
                                   
                                   } else {
                                      listwriter_WHILE9.append($me.head(((IList)right_1)));
                                      right_1 = ((IList)($me.tail(((IList)right_1))));
                                   
                                   }
                                 } else {
                                   break WHILE9; // muBreak
                                 
                                 }
                         
                             }
                 
                     } while(false);
                 IList res_3 = ((IList)(listwriter_WHILE9.done()));
                 final IList $result57 = ((IList)($alist_add_alist(((IList)($alist_add_alist(((IList)res_3),((IList)left_0)))),((IList)right_1))));
                 if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result57.getType(),$T42)){
                    return ((IList)($result57));
                 
                 } else {
                    return null;
                 }
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(10161,220,<400,0>,<408,55>) 
    public IValue List_min$86af046c90057650(IList $0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/min: 
            do {
                if($T9.match($0.getType(), $typeBindings)){
                   final IList $subject63 = ((IList)$0);
                   int $subject63_cursor = 0;
                   if($isSubtypeOf($subject63.getType(),$T9)){
                      final int $subject63_len = (int)((IList)($subject63)).length();
                      if($subject63_len >= 1){
                         if($subject63_cursor < $subject63_len){
                            IValue h_0 = ((IValue)($alist_subscript_int(((IList)($subject63)),$subject63_cursor)));
                            $subject63_cursor += 1;
                            final int $t_164_start = (int)$subject63_cursor;
                            min_LIST_VARh_MVARt:
                            
                            for(int $t_164_len = 0; $t_164_len <= $subject63_len - $t_164_start - 0; $t_164_len += 1){
                               IList t_1 = ((IList)($subject63.sublist($t_164_start, $t_164_len)));
                               $subject63_cursor = $t_164_start + $t_164_len;
                               if($subject63_cursor == $subject63_len){
                                  IValue $reducer59 = (IValue)(h_0);
                                  $REDUCER58_GEN10373:
                                  for(IValue $elem61_for : ((IList)t_1)){
                                      IValue $elem61 = (IValue) $elem61_for;
                                      IValue e_3 = null;
                                      if((((IBool)($less(((IValue)($elem61)),((IValue)($reducer59)))))).getValue()){
                                         $reducer59 = ((IValue)($elem61));
                                      
                                      } else {
                                         $reducer59 = ((IValue)($reducer59));
                                      
                                      }
                                  }
                                  
                                              final IValue $result62 = ((IValue)($reducer59));
                                  if($T29.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result62.getType(),$T29)){
                                     return ((IValue)($result62));
                                  
                                  } else {
                                     return null;
                                  }
                               } else {
                                  continue min_LIST_VARh_MVARt;/*list match1*/
                               }
                            }
                            return null;
                         
                         } else {
                            return null;
                         }
                      } else {
                         return null;
                      }
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(10382,45,<409,0>,<409,45>) 
    public IValue List_min$acb3d58fffd8b2f1(IList $__0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/min: 
            do {
                if($T0.match($__0.getType(), $typeBindings)){
                   if($__0.equals(((IList)$constants.get(3)/*[]*/))){
                      throw new Throw($RVF.constructor(M_Exception.RuntimeException_EmptyList_, new IValue[]{}));
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(10430,639,<412,0>,<431,1>) 
    public IList List_mix$5b11827cf2a66f4b(IList l_0, IList r_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(l_0.getType(), $typeBindings)){
           if($T0.match(r_1.getType(), $typeBindings)){
              IInteger sizeL_2 = ((IInteger)($me.size(((IList)l_0))));
              IInteger sizeR_3 = ((IInteger)($me.size(((IList)r_1))));
              IInteger minSize_4 = null;
              if((((IBool)($aint_less_aint(((IInteger)sizeL_2),((IInteger)sizeR_3))))).getValue()){
                 minSize_4 = ((IInteger)sizeL_2);
              
              } else {
                 minSize_4 = ((IInteger)sizeR_3);
              
              }final IListWriter $listwriter66 = (IListWriter)$RVF.listWriter();
              final IInteger $lst7 = ((IInteger)minSize_4);
              final boolean $dir8 = ((IInteger)$constants.get(1)/*0*/).less($lst7).getValue();
              
              $LCOMP67_GEN11014:
              for(IInteger $elem68 = ((IInteger)$constants.get(1)/*0*/); $dir8 ? $aint_less_aint($elem68,$lst7).getValue() 
                                        : $aint_lessequal_aint($elem68,$lst7).not().getValue(); $elem68 = $aint_add_aint($elem68,$dir8 ? ((IInteger)$constants.get(2)/*1*/) : ((IInteger)$constants.get(4)/*-1*/))){
                  IInteger i_5 = null;
                  $listwriter66.append($alist_subscript_int(((IList)l_0),((IInteger)($elem68)).intValue()));
                  $listwriter66.append($alist_subscript_int(((IList)r_1),((IInteger)($elem68)).intValue()));
              }
              
              final IList $result69 = ((IList)($alist_add_alist(((IList)($alist_add_alist(((IList)($listwriter66.done())),((IList)($me.drop(((IInteger)sizeR_3), ((IList)l_0))))))),((IList)($me.drop(((IInteger)sizeL_2), ((IList)r_1)))))));
              if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result69.getType(),$T42)){
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(11071,195,<433,0>,<441,36>) 
    public ISet List_permutations$c819f16d8907d74b(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final ISet $result70 = ((ISet)($me.permutationsBag(((IMap)($me.distribution(((IList)lst_0)))))));
           if($T49.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result70.getType(),$T49)){
              return ((ISet)($result70));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(11268,172,<443,0>,<446,74>) 
    public ISet List_permutationsBag$8b2de212b4f23a16(IMap b_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T39.match(b_0.getType(), $typeBindings)){
           if(true){
              if((((IBool)(M_Map.isEmpty(((IMap)b_0))))).getValue()){
                 final ISet $result76 = ((ISet)$constants.get(7)/*{[]}*/);
                 if($T49.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result76.getType(),$T49)){
                    return ((ISet)($result76));
                 
                 } else {
                    return null;
                 }
              } else {
                 final ISetWriter $setwriter72 = (ISetWriter)$RVF.setWriter();
                 ;
                 $SCOMP73_GEN11387:
                 for(IValue $elem75_for : ((IMap)b_0)){
                     IValue $elem75 = (IValue) $elem75_for;
                     IValue e_1 = ((IValue)($elem75));
                     $SCOMP73_GEN11387_GEN11395:
                     for(IValue $elem74_for : ((ISet)($me.permutationsBag(((IMap)($me.removeFromBag(((IMap)b_0), ((IValue)e_1)))))))){
                         IList $elem74 = (IList) $elem74_for;
                         IList rest_2 = null;
                         $setwriter72.insert($alist_add_alist(((IList)($RVF.list(((IValue)e_1)))),((IList)($elem74))));
                     
                     }
                     continue $SCOMP73_GEN11387;
                                 
                 }
                 
                             final ISet $result76 = ((ISet)($setwriter72.done()));
                 if($T49.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result76.getType(),$T49)){
                    return ((ISet)($result76));
                 
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
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(11443,342,<449,0>,<462,54>) 
    public ITuple List_pop$564116ba8d629906(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final ITuple $result77 = ((ITuple)($me.headTail(((IList)lst_0))));
           if($T45.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result77.getType(),$T45)){
              return ((ITuple)($result77));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(11788,259,<465,0>,<475,36>) 
    public IList List_prefix$09e7c5b6eb264f3e(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final IList $result78 = ((IList)((IList)$Prelude.prefix(lst_0)));
           if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result78.getType(),$T42)){
              return ((IList)($result78));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(12050,278,<478,0>,<489,52>) 
    public IList List_push$a31a54c2a21ec30e(IValue elem_0, IList lst_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T2.match(elem_0.getType(), $typeBindings)){
           if($T0.match(lst_1.getType(), $typeBindings)){
              final IList $result79 = ((IList)($alist_add_alist(((IList)($RVF.list(((IValue)elem_0)))),((IList)lst_1))));
              if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result79.getType(),$T42)){
                 return ((IList)($result79));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(12331,715,<492,0>,<513,38>) 
    public IValue List_reducer$13d810a90f3d6575(IList lst_0, TypedFunctionInstance2<IValue, IValue, IValue> fn_1, IValue unit_2){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           if($T28.match(fn_1.getType(), $typeBindings)){
              if($T2.match(unit_2.getType(), $typeBindings)){
                 IValue $reducer81 = (IValue)(unit_2);
                 $REDUCER80_GEN13034:
                 for(IValue $elem82_for : ((IList)lst_0)){
                     IValue $elem82 = (IValue) $elem82_for;
                     IValue elm_4 = null;
                     $reducer81 = ((IValue)(((TypedFunctionInstance2<IValue, IValue, IValue>)fn_1).typedCall(((IValue)($reducer81)), ((IValue)($elem82)))));
                 
                 }
                 
                             final IValue $result83 = ((IValue)($reducer81));
                 if($T29.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result83.getType(),$T29)){
                    return ((IValue)($result83));
                 
                 } else {
                    return null;
                 }
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(13048,102,<515,0>,<516,53>) 
    public IList List_remove$6047315caa154842(IList lst_0, IInteger indexToDelete_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           if(true){
              if(true){
                 final IListWriter $listwriter84 = (IListWriter)$RVF.listWriter();
                 $LCOMP85_GEN13112:
                 for(IValue $elem86_for : ((IList)($me.index(((IList)lst_0))))){
                     IInteger $elem86 = (IInteger) $elem86_for;
                     IInteger i_2 = null;
                     if((((IBool)($equal(((IInteger)($elem86)),((IInteger)indexToDelete_1)).not()))).getValue()){
                       $listwriter84.append($alist_subscript_int(((IList)lst_0),((IInteger)($elem86)).intValue()));
                     
                     } else {
                       continue $LCOMP85_GEN13112;
                     }
                 
                 }
                 
                             final IList $result87 = ((IList)($listwriter84.done()));
                 if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result87.getType(),$T42)){
                    return ((IList)($result87));
                 
                 } else {
                    return null;
                 }
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(13152,115,<518,0>,<519,26>) 
    public IMap List_removeFromBag$3427b7346e20c720(IMap b_0, IValue el_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T39.match(b_0.getType(), $typeBindings)){
           if($T2.match(el_1.getType(), $typeBindings)){
              final IMap $result88 = ((IMap)($me.removeFromBag(((IMap)b_0), ((IValue)el_1), ((IInteger)$constants.get(2)/*1*/))));
              if($T43.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result88.getType(),$T43)){
                 return ((IMap)($result88));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(13269,175,<521,0>,<522,78>) 
    public IMap List_removeFromBag$5c4ef2614668a761(IMap b_0, IValue el_1, IInteger nr_2){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T39.match(b_0.getType(), $typeBindings)){
           if($T2.match(el_1.getType(), $typeBindings)){
              if($is_defined_value($guarded_map_subscript(((IMap)b_0),((IValue)el_1)))){
                 if((((IBool)($aint_lessequal_aint(((IInteger)($amap_subscript(((IMap)b_0),((IValue)el_1)))),((IInteger)nr_2))))).getValue()){
                    final IMap $result91 = ((IMap)(((IMap)b_0).remove(((IMap)($buildMap(((IValue)el_1), ((IInteger)($amap_subscript(((IMap)b_0),((IValue)el_1))))))))));
                    if($T43.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result91.getType(),$T43)){
                       return ((IMap)($result91));
                    
                    } else {
                       return null;
                    }
                 } else {
                    final IMap $result91 = ((IMap)($amap_add_amap(((IMap)b_0),((IMap)($buildMap(((IValue)el_1), ((IInteger)(((IInteger) ((IInteger)($amap_subscript(((IMap)b_0),((IValue)el_1)))).subtract(((IInteger)nr_2)))))))))));
                    if($T43.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result91.getType(),$T43)){
                       return ((IMap)($result91));
                    
                    } else {
                       return null;
                    }
                 }
              } else {
                 final IMap $result91 = ((IMap)b_0);
                 if($T43.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result91.getType(),$T43)){
                    return ((IMap)($result91));
                 
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
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(13448,295,<525,0>,<537,36>) 
    public IList List_reverse$9dfd2c061e6148ac(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final IList $result92 = ((IList)((IList)$Prelude.reverse(lst_0)));
           if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result92.getType(),$T42)){
              return ((IList)($result92));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(13746,236,<540,0>,<549,28>) 
    public IInteger List_size$ba7443328d8b4a27(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final IInteger $result93 = ((IInteger)((IInteger)$Prelude.size(lst_0)));
           if($T6.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result93.getType(),$T6)){
              return ((IInteger)($result93));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(13985,678,<552,0>,<575,54>) 
    public IList List_slice$dac0d5be581790d0(IList lst_0, IInteger begin_1, IInteger len_2){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final IList $result94 = ((IList)((IList)$Prelude.slice(lst_0, begin_1, len_2)));
           if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result94.getType(),$T42)){
              return ((IList)($result94));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(15190,35,<596,11>,<596,46>) 
    public IBool $CLOSURE_0(IValue a_0, IValue b_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T20.match(a_0.getType(), $typeBindings)){
           if($T38.match(b_1.getType(), $typeBindings)){
              final IBool $result95 = ((IBool)($less(((IValue)a_0),((IValue)b_1))));
              if($T19.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result95.getType(),$T19)){
                 return ((IBool)($result95));
              
              } else {
                 return ((IBool)$constants.get(6)/*false*/);
              
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(14666,562,<578,0>,<596,49>) 
    public IList List_sort$1fe4426c8c8039da(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final IList $result96 = ((IList)($me.sort(((IList)lst_0), new TypedFunctionInstance2<IValue,IValue,IValue>(($15190_0, $15190_1) -> { return $CLOSURE_0((IValue)$15190_0, (IValue)$15190_1); }, $T50))));
           if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result96.getType(),$T42)){
              return ((IList)($result96));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(15231,98,<598,0>,<599,56>) 
    public IList List_sort$a9bbc6fca4e60d0a(IList l_0, TypedFunctionInstance2<IValue, IValue, IValue> less_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(l_0.getType(), $typeBindings)){
           if($T37.match(less_1.getType(), $typeBindings)){
              final IList $result97 = ((IList)((IList)$Prelude.sort(l_0, less_1)));
              if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result97.getType(),$T42)){
                 return ((IList)($result97));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(15716,35,<609,51>,<609,86>) 
    public IBool $CLOSURE_1(IValue a_0, IValue b_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T20.match(a_0.getType(), $typeBindings)){
           if($T38.match(b_1.getType(), $typeBindings)){
              final IBool $result98 = ((IBool)($less(((IValue)a_0),((IValue)b_1))));
              if($T19.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result98.getType(),$T19)){
                 return ((IBool)($result98));
              
              } else {
                 return ((IBool)$constants.get(6)/*false*/);
              
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(15332,468,<602,0>,<610,47>) 
    public IBool List_isSorted$293ad5967a3bf1ea(IList l_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = Util.kwpMap();
        TypedFunctionInstance2<IValue, IValue, IValue> $kwpDefault_less = new TypedFunctionInstance2<IValue,IValue,IValue>(($15716_0, $15716_1) -> { return $CLOSURE_1((IValue)$15716_0, (IValue)$15716_1); }, $T50);
        $kwpDefaults.put("less", $kwpDefault_less);
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(l_0.getType(), $typeBindings)){
           if(true){
              IBool $done99 = (IBool)(((IBool)$constants.get(8)/*true*/));
              /*muExists*/$ANY100: 
                  do {
                      final IList $subject103 = ((IList)l_0);
                      int $subject103_cursor = 0;
                      if($isSubtypeOf($subject103.getType(),$T0)){
                         final int $subject103_len = (int)((IList)($subject103)).length();
                         if($subject103_len >= 2){
                            final int $__1105_start = (int)$subject103_cursor;
                            $ANY100_LIST_MVAR$_101:
                            
                            for(int $__1105_len = 0; $__1105_len <= $subject103_len - $__1105_start - 2; $__1105_len += 1){
                               $subject103_cursor = $__1105_start + $__1105_len;
                               if($subject103_cursor < $subject103_len){
                                  IValue a_2 = ((IValue)($alist_subscript_int(((IList)($subject103)),$subject103_cursor)));
                                  $subject103_cursor += 1;
                                  if($subject103_cursor < $subject103_len){
                                     IValue b_3 = ((IValue)($alist_subscript_int(((IList)($subject103)),$subject103_cursor)));
                                     $subject103_cursor += 1;
                                     final int $__1104_start = (int)$subject103_cursor;
                                     final int $__1104_len = (int)$subject103_len - $__1104_start - 0;
                                     $subject103_cursor = $__1104_start + $__1104_len;
                                     /*muExists*/$ANY100_LIST_MVAR$_101_VARa_VARb_MVAR$_102: 
                                         do {
                                             if($subject103_cursor == $subject103_len){
                                                if((((IBool)(((TypedFunctionInstance2<IValue, IValue, IValue>)((TypedFunctionInstance2<IValue, IValue, IValue>) ($kwpActuals.containsKey("less") ? $kwpActuals.get("less") : $kwpDefaults.get("less")))).typedCall(((IValue)b_3), ((IValue)a_2))))).getValue()){
                                                   $done99 = ((IBool)$constants.get(6)/*false*/);
                                                   break $ANY100; // muSucceed
                                                } else {
                                                   continue $ANY100;/*not any*/
                                                }
                                             } else {
                                                continue $ANY100_LIST_MVAR$_101_VARa_VARb_MVAR$_102;/*list match1*/
                                             }
                                         } while(false);
                                     continue $ANY100_LIST_MVAR$_101;/*computeFail*/
                                  } else {
                                     continue $ANY100_LIST_MVAR$_101;/*computeFail*/
                                  }
                               } else {
                                  continue $ANY100_LIST_MVAR$_101;/*computeFail*/
                               }
                            }
                            
                         
                         }
                      
                      }
              
                  } while(false);
              final IBool $result106 = ((IBool)($done99));
              if($T19.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result106.getType(),$T19)){
                 return ((IBool)($result106));
              
              } else {
                 return ((IBool)$constants.get(6)/*false*/);
              
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(15803,276,<613,0>,<625,34>) 
    public IList List_shuffle$33a2b58b4f0a6e3a(IList l_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(l_0.getType(), $typeBindings)){
           final IList $result107 = ((IList)((IList)$Prelude.shuffle(l_0)));
           if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result107.getType(),$T42)){
              return ((IList)($result107));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(16082,374,<628,0>,<642,44>) 
    public IList List_shuffle$ef4eb9552ad766a9(IList l_0, IInteger seed_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(l_0.getType(), $typeBindings)){
           final IList $result108 = ((IList)((IList)$Prelude.shuffle(l_0, seed_1)));
           if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result108.getType(),$T42)){
              return ((IList)($result108));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(16459,260,<645,0>,<656,1>) 
    public ITuple List_split$19c747b75c8a251d(IList l_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(l_0.getType(), $typeBindings)){
           IInteger half_1 = ((IInteger)($aint_divide_aint(((IInteger)($me.size(((IList)l_0)))),((IInteger)$constants.get(9)/*2*/))));
           final ITuple $result109 = ((ITuple)($RVF.tuple(((IList)($me.take(((IInteger)half_1), ((IList)l_0)))), ((IList)($me.drop(((IInteger)half_1), ((IList)l_0)))))));
           if($T51.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result109.getType(),$T51)){
              return ((ITuple)($result109));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(16722,201,<659,0>,<667,76>) 
    public INumber List_sum$9ba9391d87d7cdce(IList $0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/sum: 
            do {
                if($T22.match($0.getType(), $typeBindings)){
                   final IList $subject114 = ((IList)$0);
                   int $subject114_cursor = 0;
                   if($isSubtypeOf($subject114.getType(),$T22)){
                      final int $subject114_len = (int)((IList)($subject114)).length();
                      if($subject114_len >= 1){
                         if($subject114_cursor < $subject114_len){
                            INumber hd_0 = ((INumber)($alist_subscript_int(((IList)($subject114)),$subject114_cursor)));
                            $subject114_cursor += 1;
                            final int $tl_1115_start = (int)$subject114_cursor;
                            sum_LIST_VARhd_MVARtl:
                            
                            for(int $tl_1115_len = 0; $tl_1115_len <= $subject114_len - $tl_1115_start - 0; $tl_1115_len += 1){
                               IList tl_1 = ((IList)($subject114.sublist($tl_1115_start, $tl_1115_len)));
                               $subject114_cursor = $tl_1115_start + $tl_1115_len;
                               if($subject114_cursor == $subject114_len){
                                  INumber $reducer111 = (INumber)(hd_0);
                                  $REDUCER110_GEN16914:
                                  for(IValue $elem112_for : ((IList)tl_1)){
                                      INumber $elem112 = (INumber) $elem112_for;
                                      INumber i_3 = null;
                                      $reducer111 = ((INumber)($anum_add_anum(((INumber)($reducer111)),((INumber)($elem112)))));
                                  
                                  }
                                  
                                              final INumber $result113 = ((INumber)($reducer111));
                                  if($T52.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result113.getType(),$T52)){
                                     return ((INumber)($result113));
                                  
                                  } else {
                                     return null;
                                  }
                               } else {
                                  continue sum_LIST_VARhd_MVARtl;/*list match1*/
                               }
                            }
                            return null;
                         
                         } else {
                            return null;
                         }
                      } else {
                         return null;
                      }
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(16924,53,<668,0>,<668,53>) 
    public IValue List_sum$7fab443836973776(IList $__0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/sum: 
            do {
                if($T0.match($__0.getType(), $typeBindings)){
                   if($__0.equals(((IList)$constants.get(3)/*[]*/))){
                      throw new Throw($RVF.constructor(M_Exception.RuntimeException_EmptyList_, new IValue[]{}));
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(16980,558,<671,0>,<695,33>) 
    public IList List_tail$a4bd36b1f369026a(IList $0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/tail: 
            do {
                if($T30.match($0.getType(), $typeBindings)){
                   final IList $subject119 = ((IList)$0);
                   int $subject119_cursor = 0;
                   if($isSubtypeOf($subject119.getType(),$T30)){
                      final int $subject119_len = (int)((IList)($subject119)).length();
                      if($subject119_len >= 1){
                         if($subject119_cursor < $subject119_len){
                            $subject119_cursor += 1;
                            final int $t_0120_start = (int)$subject119_cursor;
                            tail_LIST_VAR$_117_MVARt:
                            
                            for(int $t_0120_len = 0; $t_0120_len <= $subject119_len - $t_0120_start - 0; $t_0120_len += 1){
                               IList t_0 = ((IList)($subject119.sublist($t_0120_start, $t_0120_len)));
                               $subject119_cursor = $t_0120_start + $t_0120_len;
                               if($subject119_cursor == $subject119_len){
                                  final IList $result118 = ((IList)t_0);
                                  if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result118.getType(),$T42)){
                                     return ((IList)($result118));
                                  
                                  } else {
                                     return null;
                                  }
                               } else {
                                  continue tail_LIST_VAR$_117_MVARt;/*list match1*/
                               }
                            }
                            return null;
                         
                         } else {
                            return null;
                         }
                      } else {
                         return null;
                      }
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(17539,51,<696,0>,<696,51>) 
    public IList List_tail$33110d2c64b6f3cf(IList $__0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/tail: 
            do {
                if($T0.match($__0.getType(), $typeBindings)){
                   if($__0.equals(((IList)$constants.get(3)/*[]*/))){
                      throw new Throw($RVF.constructor(M_Exception.RuntimeException_EmptyList_, new IValue[]{}));
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(17593,108,<698,0>,<699,66>) 
    public IList List_tail$28d274b499bfa1ce(IList lst_0, IInteger len_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final IList $result121 = ((IList)((IList)$Prelude.tail(lst_0, len_1)));
           if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result121.getType(),$T42)){
              return ((IList)($result121));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(17704,448,<702,0>,<716,40>) 
    public IList List_take$95b89daedfc23844(IInteger n_0, IList lst_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_1.getType(), $typeBindings)){
           final IList $result122 = ((IList)((IList)$Prelude.take(n_0, lst_1)));
           if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result122.getType(),$T42)){
              return ((IList)($result122));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(18155,750,<719,0>,<741,51>) 
    public ITuple List_takeOneFrom$48bb3b6062ea97b1(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final ITuple $result123 = ((ITuple)((ITuple)$Prelude.takeOneFrom(lst_0)));
           if($T45.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result123.getType(),$T45)){
              return ((ITuple)($result123));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(18908,341,<744,0>,<758,1>) 
    public IList List_takeWhile$557e76d9c8e487c3(IList lst_0, TypedFunctionInstance1<IValue, IValue> take_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           if($T18.match(take_1.getType(), $typeBindings)){
              IInteger i_2 = ((IInteger)$constants.get(1)/*0*/);
              final IListWriter listwriter_WHILE11 = (IListWriter)$RVF.listWriter();
              /*muExists*/WHILE11_BT: 
                  do {
                      WHILE11:
                          while((((IBool)($aint_less_aint(((IInteger)i_2),((IInteger)($me.size(((IList)lst_0)))))))).getValue()){
                              if((((IBool)(((TypedFunctionInstance1<IValue, IValue>)take_1).typedCall(((IValue)($alist_subscript_int(((IList)lst_0),((IInteger)i_2).intValue()))))))).getValue()){
                                listwriter_WHILE11.append($alist_subscript_int(((IList)lst_0),((IInteger)i_2).intValue()));
                                i_2 = ((IInteger)($aint_add_aint(((IInteger)i_2),((IInteger)$constants.get(2)/*1*/))));
                              
                              } else {
                                break WHILE11; // muBreak
                              
                              }
                      
                          }
              
                  } while(false);
              final IList $result124 = ((IList)(listwriter_WHILE11.done()));
              if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result124.getType(),$T42)){
                 return ((IList)($result124));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(19252,768,<761,0>,<778,72>) 
    public IMap List_toMap$795bdddf805b0c4b(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T11.match(lst_0.getType(), $typeBindings)){
           final IMap $result125 = ((IMap)((IMap)$Prelude.toMap(lst_0)));
           if($T53.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result125.getType(),$T53)){
              return ((IMap)($result125));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(20023,630,<781,0>,<800,72>) 
    public IMap List_toMapUnique$1ee65c954c28c774(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T11.match(lst_0.getType(), $typeBindings)){
           final IMap $result126 = ((IMap)((IMap)$Prelude.toMapUnique(lst_0)));
           if($T57.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result126.getType(),$T57)){
              return ((IMap)($result126));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(20656,280,<803,0>,<815,26>) 
    public IValue List_top$7d8d56824622aa16(IList $0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        /*muExists*/top: 
            do {
                if($T35.match($0.getType(), $typeBindings)){
                   final IList $subject130 = ((IList)$0);
                   int $subject130_cursor = 0;
                   if($isSubtypeOf($subject130.getType(),$T35)){
                      final int $subject130_len = (int)((IList)($subject130)).length();
                      if($subject130_len >= 1){
                         if($subject130_cursor < $subject130_len){
                            IValue t_0 = ((IValue)($alist_subscript_int(((IList)($subject130)),$subject130_cursor)));
                            $subject130_cursor += 1;
                            final int $__1131_start = (int)$subject130_cursor;
                            final int $__1131_len = (int)$subject130_len - $__1131_start - 0;
                            $subject130_cursor = $__1131_start + $__1131_len;
                            /*muExists*/top_LIST_VARt_MVAR$_128: 
                                do {
                                    if($subject130_cursor == $subject130_len){
                                       final IValue $result129 = ((IValue)t_0);
                                       if($T29.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result129.getType(),$T29)){
                                          return ((IValue)($result129));
                                       
                                       } else {
                                          return null;
                                       }
                                    } else {
                                       continue top_LIST_VARt_MVAR$_128;/*list match1*/
                                    }
                                } while(false);
                            return null;
                         } else {
                            return null;
                         }
                      } else {
                         return null;
                      }
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(20939,439,<818,0>,<832,1>) 
    public ISet List_toRel$87081beb0169e3cf(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           if(true){
              final ISetWriter $setwriter132 = (ISetWriter)$RVF.setWriter();
              ;
              /*muExists*/$SCOMP133: 
                  do {
                      final IList $subject136 = ((IList)lst_0);
                      int $subject136_cursor = 0;
                      if($isSubtypeOf($subject136.getType(),$T0)){
                         final int $subject136_len = (int)((IList)($subject136)).length();
                         if($subject136_len >= 2){
                            final int $__1138_start = (int)$subject136_cursor;
                            $SCOMP133_LIST_MVAR$_134:
                            
                            for(int $__1138_len = 0; $__1138_len <= $subject136_len - $__1138_start - 2; $__1138_len += 1){
                               $subject136_cursor = $__1138_start + $__1138_len;
                               if($subject136_cursor < $subject136_len){
                                  IValue from_1 = ((IValue)($alist_subscript_int(((IList)($subject136)),$subject136_cursor)));
                                  $subject136_cursor += 1;
                                  if($subject136_cursor < $subject136_len){
                                     IValue to_2 = ((IValue)($alist_subscript_int(((IList)($subject136)),$subject136_cursor)));
                                     $subject136_cursor += 1;
                                     final int $__1137_start = (int)$subject136_cursor;
                                     final int $__1137_len = (int)$subject136_len - $__1137_start - 0;
                                     $subject136_cursor = $__1137_start + $__1137_len;
                                     /*muExists*/$SCOMP133_LIST_MVAR$_134_VARfrom_VARto_MVAR$_135: 
                                         do {
                                             if($subject136_cursor == $subject136_len){
                                                $setwriter132.insert($RVF.tuple(((IValue)from_1), ((IValue)to_2)));
                                             
                                             } else {
                                                continue $SCOMP133_LIST_MVAR$_134_VARfrom_VARto_MVAR$_135;/*list match1*/
                                             }
                                         } while(false);
                                     continue $SCOMP133_LIST_MVAR$_134;/*computeFail*/
                                  } else {
                                     continue $SCOMP133_LIST_MVAR$_134;/*computeFail*/
                                  }
                               } else {
                                  continue $SCOMP133_LIST_MVAR$_134;/*computeFail*/
                               }
                            }
                            
                         
                         }
                      
                      }
              
                  } while(false);
              final ISet $result139 = ((ISet)($setwriter132.done()));
              if($T58.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result139.getType(),$T58)){
                 return ((ISet)($result139));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(21381,373,<835,0>,<853,33>) 
    public ISet List_toSet$7a7d987a16d99f97(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final ISet $result140 = ((ISet)((ISet)$Prelude.toSet(lst_0)));
           if($T59.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result140.getType(),$T59)){
              return ((ISet)($result140));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(21757,275,<856,0>,<868,32>) 
    public IString List_toString$a04f1c5d8efcd2e2(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final IString $result141 = ((IString)((IString)$Prelude.toString(lst_0)));
           if($T7.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result141.getType(),$T7)){
              return ((IString)($result141));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(22036,297,<872,0>,<884,33>) 
    public IString List_itoString$dc18c931a359f8dc(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(lst_0.getType(), $typeBindings)){
           final IString $result142 = ((IString)((IString)$Prelude.itoString(lst_0)));
           if($T7.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result142.getType(),$T7)){
              return ((IString)($result142));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(22337,369,<888,0>,<900,42>) 
    public ITuple List_unzip2$2f0030d08b0b515c(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T21.match(lst_0.getType(), $typeBindings)){
           if(true){
              final IListWriter $listwriter143 = (IListWriter)$RVF.listWriter();
              $LCOMP144_GEN22671:
              for(IValue $elem145_for : ((IList)lst_0)){
                  IValue $elem145 = (IValue) $elem145_for;
                  final IValue $tuple_subject146 = ((IValue)($elem145));
                  if($tuple_subject146 instanceof ITuple && ((ITuple)$tuple_subject146).arity() == 2){
                     /*muExists*/$LCOMP144_GEN22671_TUPLE: 
                         do {
                             IValue t_1 = null;
                             $listwriter143.append($subscript_int(((IValue)($tuple_subject146)),0));
                     
                         } while(false);
                  
                  } else {
                     continue $LCOMP144_GEN22671;
                  }
              }
              
                          final IListWriter $listwriter147 = (IListWriter)$RVF.listWriter();
              $LCOMP148_GEN22691:
              for(IValue $elem149_for : ((IList)lst_0)){
                  IValue $elem149 = (IValue) $elem149_for;
                  final IValue $tuple_subject150 = ((IValue)($elem149));
                  if($tuple_subject150 instanceof ITuple && ((ITuple)$tuple_subject150).arity() == 2){
                     /*muExists*/$LCOMP148_GEN22691_TUPLE: 
                         do {
                             IValue u_2 = null;
                             $listwriter147.append($subscript_int(((IValue)($tuple_subject150)),1));
                     
                         } while(false);
                  
                  } else {
                     continue $LCOMP148_GEN22691;
                  }
              }
              
                          final ITuple $result151 = ((ITuple)($RVF.tuple(((IList)($listwriter143.done())), ((IList)($listwriter147.done())))));
              if($T60.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result151.getType(),$T60)){
                 return ((ITuple)($result151));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(22758,138,<903,0>,<904,68>) 
    public ITuple List_unzip3$70df47ebf833367b(IList lst_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T27.match(lst_0.getType(), $typeBindings)){
           if(true){
              final IListWriter $listwriter152 = (IListWriter)$RVF.listWriter();
              $LCOMP153_GEN22835:
              for(IValue $elem154_for : ((IList)lst_0)){
                  IValue $elem154 = (IValue) $elem154_for;
                  final IValue $tuple_subject155 = ((IValue)($elem154));
                  if($tuple_subject155 instanceof ITuple && ((ITuple)$tuple_subject155).arity() == 3){
                     /*muExists*/$LCOMP153_GEN22835_TUPLE: 
                         do {
                             IValue t_1 = null;
                             $listwriter152.append($subscript_int(((IValue)($tuple_subject155)),0));
                     
                         } while(false);
                  
                  } else {
                     continue $LCOMP153_GEN22835;
                  }
              }
              
                          final IListWriter $listwriter156 = (IListWriter)$RVF.listWriter();
              $LCOMP157_GEN22857:
              for(IValue $elem158_for : ((IList)lst_0)){
                  IValue $elem158 = (IValue) $elem158_for;
                  final IValue $tuple_subject159 = ((IValue)($elem158));
                  if($tuple_subject159 instanceof ITuple && ((ITuple)$tuple_subject159).arity() == 3){
                     /*muExists*/$LCOMP157_GEN22857_TUPLE: 
                         do {
                             IValue u_2 = null;
                             $listwriter156.append($subscript_int(((IValue)($tuple_subject159)),1));
                     
                         } while(false);
                  
                  } else {
                     continue $LCOMP157_GEN22857;
                  }
              }
              
                          final IListWriter $listwriter160 = (IListWriter)$RVF.listWriter();
              $LCOMP161_GEN22879:
              for(IValue $elem162_for : ((IList)lst_0)){
                  IValue $elem162 = (IValue) $elem162_for;
                  final IValue $tuple_subject163 = ((IValue)($elem162));
                  if($tuple_subject163 instanceof ITuple && ((ITuple)$tuple_subject163).arity() == 3){
                     /*muExists*/$LCOMP161_GEN22879_TUPLE: 
                         do {
                             IValue w_3 = null;
                             $listwriter160.append($subscript_int(((IValue)($tuple_subject163)),2));
                     
                         } while(false);
                  
                  } else {
                     continue $LCOMP161_GEN22879;
                  }
              }
              
                          final ITuple $result164 = ((ITuple)($RVF.tuple(((IList)($listwriter152.done())), ((IList)($listwriter156.done())), ((IList)($listwriter160.done())))));
              if($T61.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result164.getType(),$T61)){
                 return ((ITuple)($result164));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(22899,298,<907,0>,<918,29>) 
    public IList List_upTill$fafbd14901ea615f(IInteger n_0){ 
        
        
        return ((IList)((IList)$Prelude.upTill(n_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(23200,485,<921,0>,<936,1>) 
    public IList List_zip2$5cf6b97a195b0a20(IList a_0, IList b_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(a_0.getType(), $typeBindings)){
           if($T32.match(b_1.getType(), $typeBindings)){
              if((((IBool)($equal(((IInteger)($me.size(((IList)a_0)))),((IInteger)($me.size(((IList)b_1))))).not()))).getValue()){
                 throw new Throw($RVF.constructor(M_Exception.RuntimeException_IllegalArgument_value_str, new IValue[]{((IValue)($RVF.tuple(((IInteger)($me.size(((IList)a_0)))), ((IInteger)($me.size(((IList)b_1))))))), ((IString)$constants.get(10)/*"List size mismatch"*/)}));
              }
              final IListWriter $listwriter165 = (IListWriter)$RVF.listWriter();
              $LCOMP166_GEN23668:
              for(IValue $elem167_for : ((IList)($me.index(((IList)a_0))))){
                  IInteger $elem167 = (IInteger) $elem167_for;
                  IInteger i_2 = ((IInteger)($elem167));
                  $listwriter165.append($RVF.tuple(((IValue)($me.elementAt(((IList)a_0), ((IInteger)i_2)))), ((IValue)($me.elementAt(((IList)b_1), ((IInteger)i_2))))));
              
              }
              
                          final IList $result168 = ((IList)($listwriter165.done()));
              if($T64.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result168.getType(),$T64)){
                 return ((IList)($result168));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/List.rsc|(23687,283,<938,0>,<942,1>) 
    public IList List_zip3$cd5c8c83ab2f56d9(IList a_0, IList b_1, IList c_2){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(a_0.getType(), $typeBindings)){
           if($T32.match(b_1.getType(), $typeBindings)){
              if($T33.match(c_2.getType(), $typeBindings)){
                 if((((IBool)($equal(((IInteger)($me.size(((IList)a_0)))),((IInteger)($me.size(((IList)b_1))))).not()))).getValue()){
                    throw new Throw($RVF.constructor(M_Exception.RuntimeException_IllegalArgument_value_str, new IValue[]{((IValue)($RVF.tuple(((IInteger)($me.size(((IList)a_0)))), ((IInteger)($me.size(((IList)b_1)))), ((IInteger)($me.size(((IList)c_2))))))), ((IString)$constants.get(10)/*"List size mismatch"*/)}));
                 } else {
                    if((((IBool)($equal(((IInteger)($me.size(((IList)a_0)))),((IInteger)($me.size(((IList)c_2))))).not()))).getValue()){
                       throw new Throw($RVF.constructor(M_Exception.RuntimeException_IllegalArgument_value_str, new IValue[]{((IValue)($RVF.tuple(((IInteger)($me.size(((IList)a_0)))), ((IInteger)($me.size(((IList)b_1)))), ((IInteger)($me.size(((IList)c_2))))))), ((IString)$constants.get(10)/*"List size mismatch"*/)}));
                    }
                 
                 }final IListWriter $listwriter169 = (IListWriter)$RVF.listWriter();
                 $LCOMP170_GEN23953:
                 for(IValue $elem171_for : ((IList)($me.index(((IList)a_0))))){
                     IInteger $elem171 = (IInteger) $elem171_for;
                     IInteger i_3 = ((IInteger)($elem171));
                     $listwriter169.append($RVF.tuple(((IValue)($me.elementAt(((IList)a_0), ((IInteger)i_3)))), ((IValue)($me.elementAt(((IList)b_1), ((IInteger)i_3)))), ((IValue)($me.elementAt(((IList)c_2), ((IInteger)i_3))))));
                 
                 }
                 
                             final IList $result172 = ((IList)($listwriter169.done()));
                 if($T67.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result172.getType(),$T67)){
                    return ((IList)($result172));
                 
                 } else {
                    return null;
                 }
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
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `List`");
    }
}